package com.especlub.match.services.interfaces;

public interface OtpService {
    String generatePin();
    int getPinExpireMinutes();
}
