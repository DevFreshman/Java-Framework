package org.example.javaframework.web.common;

import jakarta.servlet.http.HttpServletResponse;
import org.example.javaframework.web.api.Errors;
import org.example.javaframework.web.api.Response;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class ErrorResponseWriter {

    private final ObjectMapper objectMapper;

    public ErrorResponseWriter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void write(HttpServletResponse response, InterfaceErrorCode code) throws IOException {
        Errors error = new Errors(code.getCode(), code.getHttpStatus().toString());
        Response<?> body = Response.failure(error);

        response.setStatus(code.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}