package com.alstn113.security.security.exception;

import com.alstn113.security.security.context.SecurityContext;
import com.alstn113.security.security.context.SecurityContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class ExceptionTranslationFilter extends GenericFilterBean {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            authenticationEntryPoint.commence(request, response, e);
        } catch (AuthorizationDeniedException e) {
            if (isAnonymous()) {
                authenticationEntryPoint.commence(request, response, new AuthenticationException("인증되지 않은 사용자입니다."));
            } else {
                accessDeniedHandler.handle(request, response, e);
            }
        }
    }

    private boolean isAnonymous() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication() == null;
    }
}