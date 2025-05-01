package com.alstn113.security.security;

import com.alstn113.security.app.application.AuthService;
import com.alstn113.security.app.application.TokenProvider;
import com.alstn113.security.security.authentication.JwtAuthenticationFilter;
import com.alstn113.security.security.authentication.TokenResolver;
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
}
