package com.yang.server.filter;

import com.yang.server.util.SbtLogTrace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by zhs on 18-8-12.
 * <p/>
 * 初始化并终结可追踪日志的过滤器
 * <p>
 * Spring Boot 需要在Application加上 @ServletComponentScan 注解
 */
//@WebFilter(filterName = "sbtTraceLoggerInitFilter", urlPatterns = "/*") // 不在这里配置，不同的框架可以有不同的配置方法
public class SbtTraceLoggerInitFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SbtTraceLoggerInitFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            try {
                SbtLogTrace.enterTrace();
            } catch (Throwable throwable) {
                LOGGER.error("enterTrace failed", throwable);
                // ignore
            }
            chain.doFilter(request, response);
        } finally {
            try {
                SbtLogTrace.exitTrace();
            } catch (Throwable throwable) {
                LOGGER.error("exitTrace failed", throwable);
            }
        }
    }

    @Override
    public void destroy() {

    }

}
