package com.especlub.match.services.interfaces;

import com.especlub.match.models.SystemParameters;

public interface SystemParametersService {
    SystemParameters getActiveByMnemonic(String mnemonic);
}
