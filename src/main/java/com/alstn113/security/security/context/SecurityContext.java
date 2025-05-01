package com.alstn113.security.security.context;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityContext {

    private Authentication authentication;

    public static SecurityContext createEmptyContext() {
        return new SecurityContext(null);
    }

    public SecurityContext(Authentication authentication) {
        this.authentication = authentication;
    }
}
