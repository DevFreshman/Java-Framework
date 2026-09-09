package org.example.javaframework.web.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;

public class ClientLocaleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String localeHeader = request.getHeader("clientLocale");
        Locale locale = (localeHeader == null || localeHeader.isBlank())
                ? Locale.forLanguageTag("vi")
                : Locale.forLanguageTag(localeHeader);
        LocaleContextHolder.setLocale(locale);
        return true;
    }
}