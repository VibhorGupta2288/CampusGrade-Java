package com.vibhor.campusgrade.util;

import com.vibhor.campusgrade.exception.ValidationException;

public final class InputValidator {
    private InputValidator() {}

    public static String required(String value, String label) throws ValidationException {
        if (value == null || value.trim().isEmpty()) throw new ValidationException(label + " cannot be empty.");
        if (value.contains("|")) throw new ValidationException(label + " cannot contain the | character.");
        return value.trim();
    }

    public static double number(String value, String label) throws ValidationException {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException(label + " must be a number.");
        }
    }

    public static int integer(String value, String label) throws ValidationException {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException(label + " must be an integer.");
        }
    }
}
