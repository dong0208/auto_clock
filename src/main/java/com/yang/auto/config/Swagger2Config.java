package com.yang.auto.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2Config{

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(builderApiInfo())
                .enable(true)
                .select()
                // 扫描所有带有 @ApiOperation 注解的类
                .apis( RequestHandlerSelectors.basePackage("com.yang.auto.controller"))
                // 扫描所有的 controller
//                .apis(RequestHandlerSelectors.basePackage("com.shebang.product.library.management.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo builderApiInfo() {
        return   new ApiInfoBuilder()
                .contact(
                        new Contact(
                                "系统名称",
                                "http://192.168.0.165:8080/xue/doc.html",
                                "邮箱地址(123456@163.com)"
                        )
                )
                .title("自动打卡项目接口文档")
                .description("自动打卡项目接口文档")
                .version("1.0")
                .build();
    }



}
