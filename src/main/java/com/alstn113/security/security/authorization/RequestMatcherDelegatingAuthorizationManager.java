package com.alstn113.security.security.authorization;

import com.alstn113.security.security.context.Authentication;
import com.alstn113.security.security.util.RequestMatcher;
import com.alstn113.security.security.util.RequestMatcherEntry;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RequestMatcherDelegatingAuthorizationManager implements AuthorizationManager {

    private final List<RequestMatcherEntry<AuthorizationManager>> mappings;

    public RequestMatcherDelegatingAuthorizationManager() {
        this.mappings = new ArrayList<>();
    }

    @Override
    public AuthorizationResult authorize(Supplier<Authentication> authentication, HttpServletRequest request) {
        for (RequestMatcherEntry<AuthorizationManager> mapping : mappings) {
            RequestMatcher matcher = mapping.matcher();
            AuthorizationManager manager = mapping.entry();

            if (matcher.matches(request)) {
                return manager.authorize(authentication, request);
            }
        }

        return new AuthorizationDecision(false);
    }

    public RequestMatcherDelegatingAuthorizationManager add(RequestMatcher matcher, AuthorizationManager manager) {
        this.mappings.add(new RequestMatcherEntry<>(matcher, manager));
        return this;
    }
}
