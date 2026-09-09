package org.example.javaframework.web.api;

public record PageMeta(
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
