package com.yang.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 放到WebConfig统一注册，便于控制顺序
public class CrossDomainFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrossDomainFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        {
//            LOGGER.info("doFilter, requestURI={}", httpServletRequest.getRequestURI());
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            String origin = httpServletRequest.getHeader("Origin");
            if (origin == null || origin.isEmpty()) {
                origin = "*";
            }
            httpServletResponse.addHeader("Access-Control-Allow-Origin", origin);
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpServletResponse.addHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
            httpServletResponse.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
            httpServletResponse.addHeader("Access-Control-Max-Age", "1800"); // 30 min
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
