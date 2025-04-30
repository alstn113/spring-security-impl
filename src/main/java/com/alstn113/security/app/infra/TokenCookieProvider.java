package com.alstn113.security.app.infra;

import com.alstn113.security.app.application.CookieProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenCookieProvider implements CookieProvider {

    private final TokenCookieProperties properties;

    public ResponseCookie createTokenCookie(String token) {
        return ResponseCookie.from(properties.key(), token)
                .httpOnly(properties.httpOnly())
                .secure(properties.secure())
                .domain(properties.domain())
                .path(properties.path())
                .maxAge(properties.maxAge())
                .build();
    }

    public ResponseCookie createExpiredTokenCookie() {
        return ResponseCookie.from(properties.key(), "")
                .httpOnly(properties.httpOnly())
                .secure(properties.secure())
                .domain(properties.domain())
                .path(properties.path())
                .maxAge(0)
                .build();
    }
}