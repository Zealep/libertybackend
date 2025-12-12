package com.zealepsoluciones.libertybackend.service.impl;

import com.zealepsoluciones.libertybackend.model.entity.Installment;
import com.zealepsoluciones.libertybackend.model.enums.InstallmentStatus;
import com.zealepsoluciones.libertybackend.repository.InstallmentRepository;
import com.zealepsoluciones.libertybackend.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service("notificationWspService")
public class NotificationWspServiceImpl implements NotificationService {

    @Value("${whatsapp.api.token}")
    private String apiToken;

    @Value("${whatsapp.phone.number.id}")
    private String phoneNumberId;

    private final WebClient webClient;
    private final InstallmentRepository installmentRepository;

    public NotificationWspServiceImpl(InstallmentRepository installmentRepository) {
        this.installmentRepository = installmentRepository;
        this.webClient = WebClient.builder()
                .baseUrl("https://graph.facebook.com/v19.0/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    @Override
    public void sendDailyInstallmentNotifications() {
        LocalDate today = LocalDate.now();
        List<Installment> installToday = installmentRepository.findByStatusAndDueDateBetween(InstallmentStatus.PENDING, today, today);

        installToday.stream()
                .filter(installment -> {
                    if (installment.getLoan() == null) return false;
                    String isSent = installment.getLoan().getIsNotificationSent();
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
                            .doOnError(Throwable::printStackTrace)
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

        String body = """
        {
          "messaging_product": "whatsapp",
          "to": "%s",
          "type": "template",
          "template": {
            "name": "recordatorio_cuota_personal",
            "language": { "code": "es" },
            "components": [
                {
                    "type": "body",
                    "parameters": [
                        { "type": "text", "text": "%s" },
                        { "type": "text", "text": "%s" },
                        { "type": "text", "text": "%s" },
                        { "type": "text", "text": "%s" },
                        { "type": "text", "text": "%s" }
                    ]
                }
            ]
          }
        }
        """.formatted(
                to,
                nombre,
                cuota,
                monto,
                fechaVenc,
                saldoPendiente
        );

        return webClient.post()
                .uri("/" + phoneNumberId + "/messages")
                .header("Authorization", "Bearer " + apiToken)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class);
    }
}
