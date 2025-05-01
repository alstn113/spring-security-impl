package com.alstn113.security.security.dsl;

import com.alstn113.security.security.authorization.AuthorityAuthorizationManager;
import com.alstn113.security.security.authorization.AuthorizationDecision;
import com.alstn113.security.security.authorization.AuthorizationFilter;
import com.alstn113.security.security.authorization.AuthorizationManager;
import com.alstn113.security.security.authorization.RequestMatcherDelegatingAuthorizationManager;
import com.alstn113.security.security.util.RequestMatcher;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpMethod;

public class AuthorizeHttpRequestsConfigurer {

    private static final AuthorizationManager permitAllAuthorizationManager =
            (authentication, request) -> new AuthorizationDecision(true);
    private static final AuthorizationManager authenticatedAuthorizationManager =
            (authentication, request) -> new AuthorizationDecision(authentication.get() != null);

    private final AuthorizationManagerRequestMatcherRegistry registry;

    public AuthorizeHttpRequestsConfigurer() {
        this.registry = new AuthorizationManagerRequestMatcherRegistry();
    }

    public void configure(HttpSecurity http) {
        AuthorizationManager delegatingAuthorizationManager = this.registry.authorizationManager();
        AuthorizationFilter authorizationFilter = new AuthorizationFilter(delegatingAuthorizationManager);
        http.addFilter(authorizationFilter);
    }

    public AuthorizationManagerRequestMatcherRegistry getRegistry() {
        return this.registry;
    }

    private AuthorizationManagerRequestMatcherRegistry addMapping(
            AuthorizationManager manager,
            List<RequestMatcher> matchers
    ) {
        for (RequestMatcher matcher : matchers) {
            this.registry.addMapping(matcher, manager);
        }
        return this.registry;
    }

    public class AuthorizationManagerRequestMatcherRegistry {

        private final RequestMatcherDelegatingAuthorizationManager manager = new RequestMatcherDelegatingAuthorizationManager();

        public AuthorizedUrl anyRequest() {
            return new AuthorizedUrl(RequestMatcher.ANY_REQUEST);
        }

        public AuthorizedUrl requestMatchers(String... patterns) {
            return requestMatchers(null, patterns);
        }

        public AuthorizedUrl requestMatchers(HttpMethod method, String... patterns) {
            List<RequestMatcher> matchers = Arrays.stream(patterns)
                    .map(pattern -> new RequestMatcher(method, pattern))
                    .toList();

            return new AuthorizedUrl(matchers);
        }

        public AuthorizationManager authorizationManager() {
            return manager;
        }

        private void addMapping(RequestMatcher matcher, AuthorizationManager authorizationManager) {
            manager.add(matcher, authorizationManager);
        }
    }

    public class AuthorizedUrl {

        private final List<RequestMatcher> matchers;

        public AuthorizedUrl(RequestMatcher... matchers) {
            this(List.of(matchers));
        }

        public AuthorizedUrl(List<RequestMatcher> matchers) {
            this.matchers = matchers;
        }

        public AuthorizationManagerRequestMatcherRegistry permitAll() {
            return access(permitAllAuthorizationManager);
        }

        public AuthorizationManagerRequestMatcherRegistry authenticated() {
            return access(authenticatedAuthorizationManager);
        }

        public AuthorizationManagerRequestMatcherRegistry hasAuthority(String authority) {
            return access(AuthorityAuthorizationManager.hasAuthority(authority));
        }

        public AuthorizationManagerRequestMatcherRegistry access(AuthorizationManager manager) {
            return AuthorizeHttpRequestsConfigurer.this.addMapping(manager, matchers);
        }
    }
}
