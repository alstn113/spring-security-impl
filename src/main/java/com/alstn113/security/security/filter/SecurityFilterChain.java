package com.alstn113.security.security.filter;

import com.alstn113.security.security.util.RequestMatcher;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SecurityFilterChain {

    private final RequestMatcher matcher;
    private final List<Filter> filters;

    public boolean matches(HttpServletRequest request) {
        return matcher.matches(request);
    }

    public List<Filter> getFilters() {
        return new ArrayList<>(filters);
    }
}
