// src/main/java/com/zealepsoluciones/libertybackend/ManualRunner.java
package com.zealepsoluciones.libertybackend;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import com.zealepsoluciones.libertybackend.service.NotificationService;

public class ManualRunner {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext ctx = new SpringApplicationBuilder(LibertybackendApplication.class)
                .properties(
                        "spring.main.web-application-type=none",
                        "scheduler.enabled=false" // evitar que se ejecuten schedulers automáticos
                )
                .run(args)) {

            NotificationService notificationService = ctx.getBean(NotificationService.class);
            notificationService.sendDailyInstallmentNotifications();
        }
    }
}