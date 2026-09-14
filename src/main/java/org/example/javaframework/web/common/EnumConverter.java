package org.example.javaframework.web.common;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class EnumConverter {

    private EnumConverter() {
    }

    /**
     * Converts a String to the target enum type, case-insensitively.
     *
     * @param enumClass the enum's Class object (needed because of type erasure —
     *                  Java can't infer T's runtime type from generics alone)
     * @param value     the raw string, e.g. from a request param
     * @throws IllegalArgumentException with a message listing valid values
     */
    public static <T extends Enum<T>> T fromString(Class<T> enumClass, String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Value for enum " + enumClass.getSimpleName() + " must not be blank");
        }

        for (T constant : enumClass.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(value.trim())) {
                return constant;
            }
        }

        String allowed = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.joining(", "));

        throw new IllegalArgumentException(
                "Invalid value '" + value + "' for " + enumClass.getSimpleName()
                        + ". Allowed values: " + allowed);
    }
}