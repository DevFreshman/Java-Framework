package org.example.javaframework.infra.model;

public record UserInfo(
        String username,
        String email,
        String fullName,
        String role
) {
}
