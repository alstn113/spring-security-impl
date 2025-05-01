package com.alstn113.security.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.util.List;

public class VirtualFilterChain implements FilterChain {

    private final List<Filter> filters;
    private final FilterChain originalChain;
    private int currentPosition;

    public VirtualFilterChain(List<Filter> filters, FilterChain originalChain) {
        this.filters = filters;
        this.originalChain = originalChain;
        this.currentPosition = 0;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if (this.currentPosition == filters.size()) {
            originalChain.doFilter(request, response);
            return;
        }
        this.currentPosition++;
        Filter nextFilter = filters.get(currentPosition - 1);
        nextFilter.doFilter(request, response, this);
    }
}
