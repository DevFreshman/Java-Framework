package org.example.javaframework.web.api;

public record Response<T>(
        String code,
        String message,
        T data,
        PageMeta pageMeta
) {
    public static <T> Response<T> success(T data) {
        return new Response<>("200", "Success", data, null);
    }

    public static <T> Response<T> success(T data, String code) {
        return new Response<>(code, "Success", data, null);
    }

    public static <T> Response<T> success(T data, PageMeta pageMeta) {
        return new Response<>("200", "Success", data, pageMeta);
    }

    public static <T> Response<T> failure(String code, String message) {
        return new Response<>(code, message, null, null);
    }

    public static <T> Response<T> failure(Errors error) {
        return new Response<>(error.code(), error.message(), null, null);
    }
}