package com.application.orderservice.jwt;

import com.azure.spring.autoconfigure.aad.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * A service to extract user id from jwt token
 */
@Component
public class JwtService {

    public String getUserId() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUserPrincipalName();
    }
}
