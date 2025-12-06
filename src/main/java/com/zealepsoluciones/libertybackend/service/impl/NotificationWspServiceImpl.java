package com.zealepsoluciones.libertybackend.service.impl;

import com.zealepsoluciones.libertybackend.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service("notificationWspService")
public class NotificationWspServiceImpl implements NotificationService {

    @Value("${whatsapp.api.token}")
    private String apiToken;

    @Value("${whatsapp.phone.number.id}")
    private String phoneNumberId;

    private final WebClient webClient;

    public NotificationWspServiceImpl() {
        this.webClient = WebClient.builder()
                .baseUrl("https://graph.facebook.com/v19.0/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    @Override
    public void sendDailyInstallmentNotifications() {

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
