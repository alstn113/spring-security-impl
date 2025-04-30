package com.alstn113.security.app.infra;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.cookie.token")
public record TokenCookieProperties(
        String key,
        boolean httpOnly,
        boolean secure,
        String domain,
        String path,
        long maxAge
) {
}
