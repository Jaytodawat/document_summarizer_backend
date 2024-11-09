package com.jay.paper_summarizer.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomLoggingFilter extends GenericFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("Request: {} {}", ((HttpServletRequest) request).getMethod(), ((HttpServletRequest) request).getRequestURI());
        filterChain.doFilter(request, response);
        log.info("Response: {}", ((HttpServletResponse) response).getStatus());
    }
}
