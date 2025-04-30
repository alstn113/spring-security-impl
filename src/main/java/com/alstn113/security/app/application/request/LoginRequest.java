package com.alstn113.security.app.application.request;

public record LoginRequest(
        String username,
        String password
) {
}
