package com.alstn113.security.security;

import com.alstn113.security.app.application.AuthService;
import com.alstn113.security.app.application.TokenProvider;
import com.alstn113.security.security.authentication.JwtAuthenticationFilter;
import com.alstn113.security.security.authentication.TokenResolver;
import com.alstn113.security.security.authorization.AuthorityAuthorizationManager;
import com.alstn113.security.security.authorization.AuthorizationDecision;
import com.alstn113.security.security.authorization.AuthorizationFilter;
import com.alstn113.security.security.authorization.AuthorizationManager;
import com.alstn113.security.security.exception.AccessDeniedHandler;
import com.alstn113.security.security.exception.AuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final TokenResolver tokenResolver;
    private final AuthService authService;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilter() {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
                tokenProvider,
                tokenResolver,
                authService,
                authenticationEntryPoint
        );

        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthenticationFilter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authorityAuthorizationFilter() {
        AuthorizationFilter authorizationFilter = new AuthorizationFilter(
                AuthorityAuthorizationManager.hasAuthority("ADMIN"),
                authenticationEntryPoint,
                accessDeniedHandler
        );

        FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(authorizationFilter);
        registrationBean.addUrlPatterns("/api/private/admin/*");
        registrationBean.setOrder(2);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authenticatedAuthorizationFilter() {
        AuthorizationManager authenticatedAuthorizationManager =
                (authentication, request) -> new AuthorizationDecision(authentication.get() != null);
        AuthorizationFilter authorizationFilter = new AuthorizationFilter(
                authenticatedAuthorizationManager,
                authenticationEntryPoint,
                accessDeniedHandler
        );

        FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(authorizationFilter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(3);

        return registrationBean;
    }
}
