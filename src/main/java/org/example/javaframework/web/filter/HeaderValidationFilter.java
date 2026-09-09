package org.example.javaframework.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.javaframework.web.common.ErrorResponseWriter;
import org.example.javaframework.web.common.ErrorCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HeaderValidationFilter extends OncePerRequestFilter {

    private final ErrorResponseWriter errorResponseWriter;

    public HeaderValidationFilter(ErrorResponseWriter errorResponseWriter) {
        this.errorResponseWriter = errorResponseWriter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String messageId = request.getHeader("clientMessageId");
        String clientTime = request.getHeader("clientTime");

        if (messageId == null || messageId.isBlank()) {
            errorResponseWriter.write(response, ErrorCode.MISSING_REQUIRED_HEADER);
            return;
        }
        if (clientTime == null || clientTime.isBlank()) {
            errorResponseWriter.write(response, ErrorCode.MISSING_REQUIRED_HEADER);
            return;
        }

        chain.doFilter(request, response);
    }
}