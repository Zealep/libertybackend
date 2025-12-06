// src/main/java/com/zealepsoluciones/libertybackend/ManualRunner.java
package com.zealepsoluciones.libertybackend;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import com.zealepsoluciones.libertybackend.service.impl.NotificationEmailServiceImpl;

public class ManualRunner {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext ctx = new SpringApplicationBuilder(LibertybackendApplication.class)
                .properties(
                        "spring.main.web-application-type=none",
                        "scheduler.enabled=false" // evitar que se ejecuten schedulers automáticos
                )
                .run(args)) {

            NotificationEmailServiceImpl notificationService = ctx.getBean(NotificationEmailServiceImpl.class);
            notificationService.sendDailyInstallmentNotifications();
        }
    }
}