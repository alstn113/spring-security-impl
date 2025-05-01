package com.alstn113.security.security.context;

public class SecurityContextHolder {

    private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

    private SecurityContextHolder() {
    }

    public static SecurityContext getContext() {
        SecurityContext context = contextHolder.get();
        if (context == null) {
            context = SecurityContext.createEmptyContext();
            contextHolder.set(context);
        }
        return context;
    }

    public static void clearContext() {
        contextHolder.remove();
    }
}
