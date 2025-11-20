package com.zealepsoluciones.libertybackend.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
public class EmailTemplateService {

    private final SpringTemplateEngine templateEngine;

    public EmailTemplateService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String renderTemplate(String templateName, Map<String, Object> variables) {
        Context ctx = new Context();
        if (variables != null) ctx.setVariables(variables);
        return templateEngine.process(templateName, ctx);
    }
}

