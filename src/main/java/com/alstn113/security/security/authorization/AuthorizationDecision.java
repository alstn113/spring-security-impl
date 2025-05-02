package com.alstn113.security.security.authorization;

public class AuthorizationDecision implements AuthorizationResult {

    private final boolean granted;

    public AuthorizationDecision(boolean granted) {
        this.granted = granted;
    }

    @Override
    public boolean isGranted() {
        return this.granted;
    }

    @Override
    public String toString() {
        return "%s: [ granted= %s ]".formatted(getClass().getSimpleName(), granted);
    }
}
