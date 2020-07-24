package com.ecmp.ch.mofc.pa;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 实现功能:
 * SpringBoot War Servlet
 */
public class ApiAppServlet extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ApiApp.class);
    }
}
