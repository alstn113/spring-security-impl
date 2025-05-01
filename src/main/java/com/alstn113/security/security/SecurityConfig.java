package com.alstn113.security.security;

import static com.alstn113.security.security.authorization.AuthorityAuthorizationManager.hasAuthority;

import com.alstn113.security.app.application.AuthService;
import com.alstn113.security.app.application.TokenProvider;
import com.alstn113.security.security.authentication.JwtAuthenticationFilter;
import com.alstn113.security.security.authentication.TokenResolver;
import com.alstn113.security.security.authorization.AuthorizationDecision;
import com.alstn113.security.security.authorization.AuthorizationFilter;
import com.alstn113.security.security.authorization.AuthorizationManager;
import com.alstn113.security.security.authorization.RequestMatcherDelegatingAuthorizationManager;
import com.alstn113.security.security.context.SecurityContextHolderFilter;
import com.alstn113.security.security.exception.AccessDeniedHandler;
import com.alstn113.security.security.exception.AuthenticationEntryPoint;
import com.alstn113.security.security.exception.ExceptionTranslationFilter;
import com.alstn113.security.security.filter.FilterChainProxy;
import com.alstn113.security.security.filter.SecurityFilterChain;
import com.alstn113.security.security.util.RequestMatcher;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import java.util.EnumSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String FILTER_CHAIN_PROXY_BEAN_NAME = "filterChainProxy";

    private final TokenProvider tokenProvider;
    private final TokenResolver tokenResolver;
    private final AuthService authService;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain() {
        AuthorizationManager authenticated =
                (authentication, request) -> new AuthorizationDecision(authentication.get() != null);

        RequestMatcherDelegatingAuthorizationManager authorizationManager = new RequestMatcherDelegatingAuthorizationManager()
                .add(new RequestMatcher(null, "/api/private/admin/**"), hasAuthority("ADMIN"))
                .add(new RequestMatcher(null, "/api/**"), authenticated);

        List<Filter> filters = List.of(
                new SecurityContextHolderFilter(),
                new JwtAuthenticationFilter(tokenProvider, tokenResolver, authService, authenticationEntryPoint),
                new ExceptionTranslationFilter(authenticationEntryPoint, accessDeniedHandler),
                new AuthorizationFilter(authorizationManager)
        );

        return new SecurityFilterChain(new RequestMatcher(null, "/api/**"), filters);
    }

    @Bean(name = FILTER_CHAIN_PROXY_BEAN_NAME)
    public FilterChainProxy filterChainProxy(List<SecurityFilterChain> securityFilterChains) {
        return new FilterChainProxy(securityFilterChains);
    }

    @Bean
    public DelegatingFilterProxyRegistrationBean securityFilterChainRegistration() {
        DelegatingFilterProxyRegistrationBean registration =
                new DelegatingFilterProxyRegistrationBean(FILTER_CHAIN_PROXY_BEAN_NAME);
        registration.setOrder(1);
        registration.addUrlPatterns("/*");
        registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        return registration;
    }
}
