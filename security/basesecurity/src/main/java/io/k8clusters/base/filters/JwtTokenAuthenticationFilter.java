package io.k8clusters.base.filters;

import io.k8clusters.base.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    private static Logger log4j = LoggerFactory.getLogger(JwtTokenAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 1. get the authentication header. Tokens are supposed to be passed in the
        // authentication header
        String header = request.getHeader("Authorization");
        log4j.error(String.format("Security Check for Request: %s", request.getRequestURI()));
        // 2. validate the header and check the prefix
        if (header == null) {
            chain.doFilter(request, response); // If not valid, go to the next filter.
            return;
        }

        // If there is no token provided and hence the user won't be authenticated.
        // It's Ok. Maybe the user accessing a public path or asking for a token.

        // All secured paths that needs a token are already defined and secured in
        // config class.
        // And If user tried to access without access token, then he won't be
        // authenticated and an exception will be thrown.

        // 3. Get the token
        // Temp Token:
        // eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE1Njg0NDMwMzUsImV4cCI6MTU5OTk3OTAzNSwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIkdpdmVuTmFtZSI6IkFtaXQiLCJTdXJuYW1lIjoiS3NoaXJzYWdhciIsIkVtYWlsIjoiYW1pdC5rc2hpcnNhZ2FyLjEzQGdtYWlsLmNvbSIsIlJvbGUiOlsiQWRtaW4iLCJDRU8iXX0.kGTAnKis7k30Y4K6tvDor7Y6tRkfzQaunlMIoVzC8gc
        String token = header;

        try { // exceptions might be thrown in creating the claims if for example the token is
            // expired
            Map<String, String> claims = null;
            // 4. Validate the token
            try {
                claims = JwtUtil.getVerifiedClaims(token);
            } catch (IllegalArgumentException e) {
            }

            if (claims != null && claims.size() > 0) {
                // List<String> authorities = (List<String>) claims.get("authorities");
                List<String> authorities = new ArrayList<>();

                String username = claims.get("GivenName");

                // 5. Create auth object
                // UsernamePasswordAuthenticationToken: A built-in object, used by spring to
                // represent the current authenticated / being authenticated user.
                // It needs a list of authorities, which has type of GrantedAuthority interface,
                // where SimpleGrantedAuthority is an implementation of that interface
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
                        authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

                // 6. Authenticate the user
                // Now, user is authenticated
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            // In case of failure. Make sure it's clear; so guarantee user won't be
            // authenticated
            SecurityContextHolder.clearContext();
        }

        // go to the next filter in the filter chain
        chain.doFilter(request, response);
    }

}