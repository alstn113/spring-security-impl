package com.alstn113.security.app.ui;

import com.alstn113.security.app.application.AuthService;
import com.alstn113.security.app.application.CookieProvider;
import com.alstn113.security.app.application.request.LoginRequest;
import com.alstn113.security.app.application.request.RegisterRequest;
import com.alstn113.security.app.application.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieProvider cookieProvider;

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody RegisterRequest request
    ) {
        authService.register(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody LoginRequest request
    ) {
        TokenResponse tokenResponse = authService.login(request);
        ResponseCookie tokenCookie = cookieProvider.createTokenCookie(tokenResponse.token());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, tokenCookie.toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie expiredTokenCookie = cookieProvider.createExpiredTokenCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredTokenCookie.toString())
                .build();
    }
}
