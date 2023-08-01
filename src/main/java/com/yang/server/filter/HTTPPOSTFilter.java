package com.yang.server.filter;
import com.github.isrsal.logging.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author WBG
 * @date 2020/6/22 14:32
 * @describe
 */
@Slf4j
public class HTTPPOSTFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("开始");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper=null;
        if (request instanceof HttpServletRequest) {
            requestWrapper=new BodyReaderHttpServletRequestWrapper((HttpServletRequest)request);
        }
        if (requestWrapper==null) {
            chain.doFilter(request, response);
        }else {
            log.info("------------------------------请求报文----------------------------------");
            log.info("URI:{} 请求报文:{}",((RequestFacade) request).getRequestURI(),getParamsFromRequestBody((HttpServletRequest) requestWrapper));
            log.info("------------------------------请求报文----------------------------------");
            ResponseWrapper responseWrapper = new ResponseWrapper(Thread.currentThread().getId(), (HttpServletResponse) response);
            chain.doFilter(requestWrapper, responseWrapper);
            String responseStr = new String(responseWrapper.toByteArray(), responseWrapper.getCharacterEncoding());
            log.info("------------------------------返回报文----------------------------------");
            log.info("URI:{} 返回报文:{}",((RequestFacade) request).getRequestURI(),responseStr);
            log.info("------------------------------返回报文----------------------------------");
        }

    }

    /* *
     * 获取请求体内容
     * @return
     * @throws IOException
     */

    private String getParamsFromRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader br = null;
        String listString = "";
        try {
            br = request.getReader();

            String str = "";

            while ((str = br.readLine()) != null) {
                listString += str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listString;
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }
}