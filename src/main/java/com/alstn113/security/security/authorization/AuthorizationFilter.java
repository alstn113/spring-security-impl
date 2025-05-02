package com.alstn113.security.security.authorization;

import com.alstn113.security.security.context.Authentication;
import com.alstn113.security.security.context.SecurityContextHolder;
import com.alstn113.security.security.exception.AccessDeniedHandler;
import com.alstn113.security.security.exception.AuthenticationEntryPoint;
import com.alstn113.security.security.exception.AuthenticationException;
import com.alstn113.security.security.exception.AuthorizationDeniedException;
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
public class AuthorizationFilter extends GenericFilterBean {

    private final AuthorizationManager authorizationManager;
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthorizationResult result = authorizationManager.authorize(() -> authentication, request);

        if (result != null && !result.isGranted()) {
            handleAccessDeniedException(request, response, authentication);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleAccessDeniedException(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        if (isAnonymous(authentication)) {
            authenticationEntryPoint.commence(request, response, new AuthenticationException("인증되지 않은 사용자입니다."));
            return;
        }
        accessDeniedHandler.handle(request, response, new AuthorizationDeniedException("접근을 위한 권한이 없습니다."));
    }

    private boolean isAnonymous(Authentication authentication) {
        return authentication == null;
    }
}
