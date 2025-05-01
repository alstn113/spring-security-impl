package com.alstn113.security.security.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

@RequiredArgsConstructor
public class RequestMatcher {

    public static final RequestMatcher ANY_REQUEST = new RequestMatcher(null, "/**");

    private final HttpMethod method;
    private final String pattern;

    public boolean matches(HttpServletRequest request) {
        boolean pathMatches = pathMatches(request.getRequestURI());

        if (method == null) {
            return pathMatches;
        }

        return pathMatches && method.matches(request.getMethod());
    }

    private boolean pathMatches(String path) {
        if (pattern.endsWith("/**")) {
            String base = pattern.substring(0, pattern.length() - 3);
            return path.startsWith(base);
        }
        return path.equals(pattern);
    }
}
