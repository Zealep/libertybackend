package com.zealepsoluciones.libertybackend.service;

import com.zealepsoluciones.libertybackend.model.entity.Installment;
import com.zealepsoluciones.libertybackend.model.enums.InstallmentStatus;
import com.zealepsoluciones.libertybackend.repository.InstallmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private final InstallmentRepository installmentRepository;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;
    private final String TO_DEFAULT_EMAIL = "cristhianpelaez13@gmail.com";

    public NotificationService(InstallmentRepository installmentRepository, EmailService emailService, EmailTemplateService emailTemplateService) {
        this.emailTemplateService = emailTemplateService;
        this.installmentRepository = installmentRepository;
        this.emailService = emailService;
    }

    public void sendDailyInstallmentNotifications() {
        LocalDate today = LocalDate.now();
        LocalDate inThreeDays = today.plusDays(3);


        // Cuotas próximas a vencer
        List<Installment> pross = installmentRepository.findByStatusAndDueDateBetween(InstallmentStatus.PENDING, today, inThreeDays);
        for (Installment i : pross) {
            if (i.getLoan() != null && i.getLoan().getCustomer() != null && i.getLoan().getCustomer().getEmail() != null) {
                //String to = i.getLoan().getCustomer().getEmail();

                Map<String,Object> vars = Map.of(
                        "title", "Recordatorio: cuota próxima a vencer",
                        "customerFirstName", i.getLoan().getCustomer().getFirstName(),
                        "installmentNumber", i.getNumber(),
                        "loanId", i.getLoan().getId(),
                        "dueDate", i.getDueDate().toString(),
                        "amount", i.getAmount().toString(),
                        "actionUrl", "https://tuapp.example.com/loans/" + i.getLoan().getId(),
                        "type", "upcoming" // o "overdue"
                );
                String html = emailTemplateService.renderTemplate("email/aviso-cuota", vars);
                emailService.sendHtmlMessage(TO_DEFAULT_EMAIL, "Recordatorio de cuota", html);
            }
        }

        // Cuotas vencidas
        List<Installment> vencidas = installmentRepository.findByStatusAndDueDateBefore(InstallmentStatus.PENDING, today);
        for (Installment i : vencidas) {
            if (i.getLoan() != null && i.getLoan().getCustomer() != null && i.getLoan().getCustomer().getEmail() != null) {
                //String to = i.getLoan().getCustomer().getEmail();
                Map<String,Object> vars = Map.of(
                        "title", "Recordatorio: cuota próxima a vencer",
                        "customerFirstName", i.getLoan().getCustomer().getFirstName(),
                        "installmentNumber", i.getNumber(),
                        "loanId", i.getLoan().getId(),
                        "dueDate", i.getDueDate().toString(),
                        "amount", i.getAmount().toString(),
                        "actionUrl", "https://tuapp.example.com/loans/" + i.getLoan().getId(),
                        "type", "upcoming" // o "overdue"
                );
                String html = emailTemplateService.renderTemplate("email/aviso-cuota", vars);
                emailService.sendHtmlMessage(TO_DEFAULT_EMAIL, "Recordatorio de cuota", html);
            }
            }
        }
}

