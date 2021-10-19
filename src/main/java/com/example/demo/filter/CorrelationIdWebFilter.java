package com.example.demo.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.UUID;

/**
 * Filters HTTP requests for "X-Correlation-Id" and sets it as "correlation-id" into the SLF4J MDC.
 *
 * @author rlichti
 * @version 1.0.0 2021-10-18
 * @since 1.0.0 2021-10-18
 */
@Component
@Slf4j
public class CorrelationIdWebFilter implements Filter {
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String CORRELATION_ID_MDC_VARIABLE = "correlation-id";

    public CorrelationIdWebFilter() {
        log.info("Started correlation-id filter. http-header='{}', mdc-variable='{}'",
                 CORRELATION_ID_HEADER, CORRELATION_ID_MDC_VARIABLE);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        MDC.remove(CORRELATION_ID_MDC_VARIABLE);

        String correlationId = ((HttpServletRequest) servletRequest).getHeader(CORRELATION_ID_HEADER);
        if (Strings.isNotBlank(correlationId)) {
            MDC.put(CORRELATION_ID_MDC_VARIABLE, correlationId);

            log.debug("Read correlation id from request: correlation-id={}", correlationId);
        } else {
            MDC.put(CORRELATION_ID_MDC_VARIABLE, UUID.randomUUID().toString());

            log.debug("Created new correlation id: correlation-id={}", MDC.get(CORRELATION_ID_MDC_VARIABLE));
        }

        String auth = ((HttpServletRequest) servletRequest).getHeader(AUTHORIZATION_HEADER);
        if (Strings.isNotBlank(auth)) {
            log.debug("Saved authorization to MDC. key='{}'", AUTHORIZATION_HEADER);
            MDC.put(AUTHORIZATION_HEADER, auth);
        }

        filterChain.doFilter(servletRequest, servletResponse);

        MDC.remove(CORRELATION_ID_MDC_VARIABLE);
    }
}
