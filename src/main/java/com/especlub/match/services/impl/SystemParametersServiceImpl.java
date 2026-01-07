package com.especlub.match.services.impl;

import com.especlub.match.models.SystemParameters;
import com.especlub.match.repositories.SystemParametersRepository;
import com.especlub.match.services.interfaces.SystemParametersService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemParametersServiceImpl implements SystemParametersService {

    private final SystemParametersRepository systemParametersRepository;

    @Cacheable(cacheNames = "systemParam", key = "#mnemonic")
    public SystemParameters getActiveByMnemonic(String mnemonic) {
        return systemParametersRepository.findByMnemonicAndRecordStatusTrue(mnemonic);
    }
}