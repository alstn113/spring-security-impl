package com.alstn113.security.app.application.request;

public record RegisterRequest(
        String username,
        String password,
        String role
) {
}
