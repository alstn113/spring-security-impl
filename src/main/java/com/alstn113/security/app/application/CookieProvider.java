package com.alstn113.security.app.application;

import org.springframework.http.ResponseCookie;

public interface CookieProvider {

    ResponseCookie createTokenCookie(String token);

    ResponseCookie createExpiredTokenCookie();
}
