package com.especlub.match.shared.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CatalogEnums {
    PIN_EXPIRED_MIN("PIN_EXPIRED_MIN"),
    ESTADO_AUDITORIA("ESTADO_AUDITORIA"),
    WHITE_LISTED_IP("WHITE_LISTED_IP");
    private String mnemonic;
}
