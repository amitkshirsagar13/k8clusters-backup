package io.k8clusters.base.filters;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class CorrelationHeaderFilter extends OncePerRequestFilter {
    public final static String CORRELATIONID = "correlation-id";
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws java.io.IOException, ServletException {
        try {
            final String token;
            if (!StringUtils.isEmpty(request.getHeader(CORRELATIONID))) {
                token = request.getHeader(CORRELATIONID);
            } else {
                token = UUID.randomUUID().toString().toUpperCase();
            }
            MDC.put(CORRELATIONID, token);
            response.addHeader(CORRELATIONID, token);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(CORRELATIONID);
        }
    }
}
