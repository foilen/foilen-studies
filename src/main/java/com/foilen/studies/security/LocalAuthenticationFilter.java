package com.foilen.studies.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filter that authenticates all requests when local authentication is enabled.
 * This is for development purposes only and must not be used in production.
 */
public class LocalAuthenticationFilter extends OncePerRequestFilter {

    private final String localUserId;

    public LocalAuthenticationFilter(String localUserId) {
        this.localUserId = localUserId;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Only set authentication if it's not already set
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            // Create a simple authentication with the local user ID
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    localUserId, null,
                    List.of()
            );

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.debug("Set local authentication for user: " + localUserId);
        }

        filterChain.doFilter(request, response);
    }
}
