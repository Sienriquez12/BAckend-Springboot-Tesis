package com.especlub.match.services.impl;

import com.especlub.match.services.interfaces.EmailService;
import com.especlub.match.shared.exceptions.CustomExceptions;
import com.especlub.match.services.interfaces.EmailServiceAsync;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailServiceAsync, EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${smtp.alert.from}")
    private String from;

    /**
     * Send an HTML email using Thymeleaf template
     * @param subject Email subject
     * @param to Recipient email address (dynamic)
     * @param templateName Template file name (e.g. "order-confirmation-email.html")
     * @param variables Variables to inject into the template
     */
    public void sendHtmlEmail(String subject, String to, String templateName, Map<String, Object> variables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new CustomExceptions("Upps, hubo un problema enviando el correo: " + to, 500);
        }
    }
}
