package com.especlub.match.services.interfaces;

import org.springframework.scheduling.annotation.Async;

import java.util.Map;

public interface EmailServiceAsync {
    @Async
    void sendHtmlEmail(String subject, String to, String templateName, Map<String, Object> variables);
}
