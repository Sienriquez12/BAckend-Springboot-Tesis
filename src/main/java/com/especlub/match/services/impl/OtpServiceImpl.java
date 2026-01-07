package com.especlub.match.services.impl;

import com.especlub.match.models.SystemParameters;
import com.especlub.match.repositories.SystemParametersRepository;
import com.especlub.match.services.interfaces.OtpService;
import com.especlub.match.shared.enums.CatalogEnums;
import com.especlub.match.shared.exceptions.CustomExceptions;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final SystemParametersRepository systemParametersRepository;
    private final Random random = new Random();

    public String generatePin() {
        return String.format("%06d", random.nextInt(999999));
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
