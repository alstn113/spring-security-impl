package com.alstn113.security.security;

import com.alstn113.security.app.application.AuthService;
import com.alstn113.security.app.application.TokenProvider;
import com.alstn113.security.security.authentication.JwtAuthenticationFilter;
import com.alstn113.security.security.authentication.TokenResolver;
import com.alstn113.security.security.dsl.HttpSecurity;
import com.alstn113.security.security.exception.AccessDeniedHandler;
import com.alstn113.security.security.exception.AuthenticationEntryPoint;
import com.alstn113.security.security.filter.FilterChainProxy;
import com.alstn113.security.security.filter.SecurityFilterChain;
import jakarta.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
                tokenProvider,
                tokenResolver,
                authService,
                authenticationEntryPoint
        );

        return http
                .securityMatcher("/api/**")
                .addFilterBefore(jwtAuthenticationFilter)
                .exceptionHandling(it -> it
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .authorizeHttpRequests(it -> it
                        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                        .requestMatchers("/api/private/member/**").hasAuthority("MEMBER")
                        .requestMatchers("/api/private/admin/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .build();
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
