package org.example.javaframework.configuration;

import org.example.javaframework.web.common.ErrorResponseWriter;
import org.example.javaframework.web.filter.HeaderValidationFilter;
import org.example.javaframework.web.filter.MdcFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({
        ErrorResponseWriter.class,
        HeaderValidationFilter.class,
        MdcFilter.class
})
public class WebFilterConfig {
}