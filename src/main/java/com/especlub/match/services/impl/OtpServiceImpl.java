package com.especlub.match.services.impl;

import com.especlub.match.models.SystemParameters;
import com.especlub.match.repositories.SystemParametersRepository;
import com.especlub.match.services.interfaces.OtpService;
import com.especlub.match.shared.enums.CatalogEnums;
import com.especlub.match.shared.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final SystemParametersRepository systemParametersRepository;
    // Use SecureRandom for cryptographic safety rather than java.util.Random.
    // SecureRandom is suitable for generating one-time PINs and is thread-safe.
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public String generatePin() {
        // Use bound 1_000_000 to include 0..999_999 (six digits).
        int pin = SECURE_RANDOM.nextInt(1_000_000);
        return String.format("%06d", pin);
    }

    public int getPinExpireMinutes() {
        SystemParameters param = systemParametersRepository.findByMnemonicAndRecordStatusTrue(CatalogEnums.PIN_EXPIRED_MIN.getMnemonic());
        int expireMinutes = 15;
        if (param != null && param.getValue() != null) {
            try {
                expireMinutes = Integer.parseInt(param.getValue());
            } catch (NumberFormatException ignored) {
                throw new CustomExceptions(CatalogEnums.PIN_EXPIRED_MIN.getMnemonic(), HttpStatus.BAD_REQUEST.value());
            }
        }
        return expireMinutes;
    }

}
