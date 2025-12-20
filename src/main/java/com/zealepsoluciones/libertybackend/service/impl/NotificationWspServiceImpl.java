package com.zealepsoluciones.libertybackend.service.impl;

import com.zealepsoluciones.libertybackend.model.entity.Installment;
import com.zealepsoluciones.libertybackend.model.enums.InstallmentStatus;
import com.zealepsoluciones.libertybackend.repository.InstallmentRepository;
import com.zealepsoluciones.libertybackend.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service("notificationWspService")
public class NotificationWspServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationWspServiceImpl.class);

    @Value("${whatsapp.api.token}")
    private String apiToken;

    @Value("${whatsapp.phone.number.id}")
    private String phoneNumberId;

    private final WebClient webClient;
    private final InstallmentRepository installmentRepository;

    public NotificationWspServiceImpl(InstallmentRepository installmentRepository) {
        this.installmentRepository = installmentRepository;
        this.webClient = WebClient.builder()
                .baseUrl("https://graph.facebook.com/v22.0/")
                .build();
    }

    @Override
    public void sendDailyInstallmentNotifications() {
        LocalDate today = LocalDate.now();
        List<Installment> installToday = installmentRepository.findByStatusAndDueDateBetween(InstallmentStatus.PENDING, today, today);

        log.info("Enviando {} notificaciones de WhatsApp para cuotas pendientes", installToday.size());

        installToday.stream()
                .filter(installment -> {
                    if (installment.getLoan() == null) return false;
                    String isSent = installment.getLoan().getSendNotificacion();
                    if (!"S".equals(isSent)) return false;
                    if (installment.getLoan().getCustomer() == null) return false;
                    String phone = installment.getLoan().getCustomer().getPhone();
                    return phone != null && !phone.isBlank();
                })
                .forEach(i -> {
                    String to = i.getLoan().getCustomer().getPhone().replaceAll("\\D+", "");
                    String nombre = i.getLoan().getCustomer().getFirstName();
                    String cuota = String.valueOf(i.getNumber());
                    String monto = i.getAmount() != null ? i.getAmount().toString() : "0";
                    String fechaVenc = i.getDueDate() != null ? i.getDueDate().toString() : "";
                    String saldoPendiente = i.getPrincipalPart() != null ? i.getPrincipalPart().toString() : "0";

                    sendLoanReminder(to, nombre, cuota, monto, fechaVenc, saldoPendiente)
                            .doOnSuccess(response -> log.info("WhatsApp enviado exitosamente a {}: {}", to, response))
                            .doOnError(error -> {
                                log.error("Error al enviar WhatsApp a {}: {}", to, error.getMessage());
                                if (error instanceof WebClientResponseException) {
                                    WebClientResponseException webError = (WebClientResponseException) error;
                                    log.error("Status: {}, Body: {}", webError.getStatusCode(), webError.getResponseBodyAsString());
                                }
                            })
                            .subscribe();
                });
    }

    public Mono<String> sendLoanReminder(
            String to,
            String nombre,
            String cuota,
            String monto,
            String fechaVenc,
            String saldoPendiente
    ) {
        // Construir el body como Map para que Spring lo serialice correctamente
        Map<String, Object> requestBody = Map.of(
                "messaging_product", "whatsapp",
                "to", formatPhoneNumber(to),
                "type", "template",
                "template", Map.of(
                        "name", "recordatorio_cuota_personal",
                        "language", Map.of("code", "es"),
                        "components", List.of(
                                Map.of(
                                        "type", "body",
                                        "parameters", List.of(
                                                Map.of("type", "text", "text", nombre, "parameter_name", "nombre"),
                                                Map.of("type", "text", "text", cuota," parameter_name", "cuota"),
                                                Map.of("type", "text", "text", monto, "parameter_name", "monto"),
                                                Map.of("type", "text", "text", fechaVenc, "parameter_name", "fecha"),
                                                Map.of("type", "text", "text", saldoPendiente, "parameter_name", "pendiente")
                                        )
                                )
                        )
                )
        );

        log.debug("Enviando mensaje de WhatsApp a: {}", to);

        return webClient.post()
                .uri(phoneNumberId + "/messages")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    log.error("Error en la respuesta de WhatsApp API: {} - {}",
                            ex.getStatusCode(), ex.getResponseBodyAsString());
                    return Mono.error(ex);
                });
    }

    private String formatPhoneNumber(String phoneNumber) {
        // Elimina espacios, guiones y otros caracteres
        String cleaned = phoneNumber.replaceAll("[\\s\\-\\(\\)]", "");

        // Si no empieza con código de país, agrega el de Colombia (57)
        if (!cleaned.startsWith("+") && !cleaned.startsWith("51")) {
            cleaned = "51" + cleaned;
        }

        // Elimina el + si lo tiene
        cleaned = cleaned.replace("+", "");

        log.info("Número formateado: {} -> {}", phoneNumber, cleaned);
        return cleaned;
    }
}
