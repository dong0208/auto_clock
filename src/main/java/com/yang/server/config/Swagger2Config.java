package com.yang.server.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
@EnableSwaggerBootstrapUI
public class Swagger2Config implements WebMvcConfigurer {

   /* @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(builderApiInfo())
               // .enable(true)
                .forCodeGeneration(true)
                .select()
                // 扫描所有带有 @ApiOperation 注解的类
                .apis( RequestHandlerSelectors.basePackage("com.yang.auto.controller"))
                // 扫描所有的 controller
//                .apis(RequestHandlerSelectors.basePackage("com.shebang.product.library.management.controller"))
              //  .paths(PathSelectors.any())
                .build();
    }

    //"http://192.168.0.165:8080/xue/doc.html",
    private ApiInfo builderApiInfo() {
        return   new ApiInfoBuilder()
               *//* .contact(
                        new Contact(
                                "系统名称",
                                "http://192.168.0.165:8080/xue/swagger-ui.html#/",
                                "邮箱地址(123456@163.com)"
                        )
                )*//*
                .title("自动打卡项目接口文档")
                .description("自动打卡项目接口文档")
                .version("1.0")
                .build();
    }*/

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yang.server.controller"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("登陆系统").description("接口文档").version("1.0-SNAPSHOT").build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }




}
