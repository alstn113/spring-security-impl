package com.alstn113.security.security.authorization;

import com.alstn113.security.security.context.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import java.util.function.Supplier;

@FunctionalInterface
public interface AuthorizationManager {

    AuthorizationResult authorize(Supplier<Authentication> authentication, HttpServletRequest request);
}
