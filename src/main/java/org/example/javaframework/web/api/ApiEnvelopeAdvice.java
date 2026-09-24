package org.example.javaframework.web.api;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ApiEnvelopeAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.getParameterType().equals(String.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        if (body instanceof Response) {
            return body;
        }

        // Chỉ bọc nếu content type thực sự là JSON — actuator prometheus (text/plain),
        // actuator root (application/vnd.spring-boot.actuator.v3+json) sẽ tự động bypass
        if (!MediaType.APPLICATION_JSON.isCompatibleWith(selectedContentType)) {
            return body;
        }

        String actualStatusCode = resolveActualStatus(response);

        if (body instanceof Page<?> page) {
            PageMeta pageMeta = new PageMeta(
                    page.getNumber() + 1,
                    page.getSize(),
                    page.getTotalElements(),
                    page.getTotalPages()
            );
            return Response.success(page.getContent(), pageMeta);
        }

        return Response.success(body, actualStatusCode);
    }

    private String resolveActualStatus(ServerHttpResponse response) {
        if (response instanceof ServletServerHttpResponse servletResponse) {
            return String.valueOf(servletResponse.getServletResponse().getStatus());
        }
        return "200";
    }
}