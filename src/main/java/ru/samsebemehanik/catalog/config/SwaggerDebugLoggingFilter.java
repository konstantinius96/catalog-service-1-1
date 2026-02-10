package ru.samsebemehanik.catalog.config;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SwaggerDebugLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(SwaggerDebugLoggingFilter.class);

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !(path.startsWith("/swagger-ui")
            || path.startsWith("/v3/api-docs")
            || path.startsWith("/openapi.yaml")
            || path.startsWith("/webjars")
            || path.startsWith("/error"));
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            log.info(
                "Swagger/OpenAPI request: method={}, uri={}{} , status={}, durationMs={}",
                method,
                uri,
                query == null ? "" : "?" + query,
                response.getStatus(),
                duration
            );
        }
    }
}
