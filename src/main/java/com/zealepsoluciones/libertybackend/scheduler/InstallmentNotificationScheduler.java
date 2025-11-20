package com.zealepsoluciones.libertybackend.scheduler;

import com.zealepsoluciones.libertybackend.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InstallmentNotificationScheduler {

    private final NotificationService notificationService;

    public InstallmentNotificationScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Se ejecuta todos los días a las 08:00
    @Scheduled(cron = "0 30 10 * * *")
    public void dailyRun() {
        notificationService.sendDailyInstallmentNotifications();
    }


}

