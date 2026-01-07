package com.especlub.match.shared.exceptions;

public record ValidationErrorDto(String field, String message, Object rejectedValue) {}

