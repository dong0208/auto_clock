package com.yang.auto.util;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.yang.auto.filter.CrossDomainFilter;
import com.yang.auto.filter.HTTPPOSTFilter;
import com.yang.auto.filter.SbtTraceLoggerInitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;
import java.util.Collections;
import java.util.List;

/**
 * @author lww
 * @date 2019-04-17 11:59 AM
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}

	/**
	 * 跨域请求支持
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*")
						.allowedMethods("*").allowedHeaders("*")
						.allowCredentials(true)
						.exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L);
			}
		};
	}

	@Bean
	public FilterRegistrationBean<Filter> sbtTraceLoggerInitFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		SbtTraceLoggerInitFilter filter = new SbtTraceLoggerInitFilter();
		filterRegistrationBean.setFilter(filter);
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<Filter> characterEncodingFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
		filterRegistrationBean.setFilter(filter);
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<Filter> crossDomainFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		CrossDomainFilter filter = new CrossDomainFilter();
		filterRegistrationBean.setFilter(filter);
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 2);
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<Filter> requestBodyFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		HTTPPOSTFilter filter = new HTTPPOSTFilter();
		filterRegistrationBean.setFilter(filter);
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 2);
		filterRegistrationBean.addUrlPatterns("/apiOpen/*");
		return filterRegistrationBean;
	}

	// 启用fastjson
	// https://github.com/alibaba/fastjson/wiki/%E5%9C%A8-Spring-%E4%B8%AD%E9%9B%86%E6%88%90-Fastjson
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));

		//自定义配置...
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		//- https://github.com/alibaba/fastjson/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98
		//- 不用 WriteDateUseDateFormat ，日期由客户端自行格式化
		fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.BrowserCompatible);
		converter.setFastJsonConfig(fastJsonConfig);
		converters.add(0, converter);
	}
}
