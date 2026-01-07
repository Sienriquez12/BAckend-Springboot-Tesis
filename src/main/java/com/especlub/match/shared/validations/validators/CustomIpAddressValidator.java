package com.especlub.match.shared.validations.validators;

import com.especlub.match.shared.validations.annotations.CustomIpAddress;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import java.net.InetAddress;

public class CustomIpAddressValidator implements ConstraintValidator<CustomIpAddress, String> {

    private static final String IPV4_REGEX = "^(\\d{1,3}\\.){3}\\d{1,3}$";
    private static final Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        // Validación IPv4: formato y rango
        if (IPV4_PATTERN.matcher(value).matches()) {
            String[] parts = value.split("\\.");
            for (String part : parts) {
                int n = Integer.parseInt(part);
                if (n < 0 || n > 255) {
                    return false;
                }
            }
            return true;
        }
        return isValidIPv6(value);
    }

    private boolean isValidIPv6(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address instanceof java.net.Inet6Address;
        } catch (Exception e) {
            return false;
        }
    }
}
