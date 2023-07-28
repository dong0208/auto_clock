package com.yang.auto.config.mybatisPlus;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@Slf4j
public class BaseMybatisPlusConfig {

    @Value("${datasource.url}")
    private String dbUrl;
    @Value("${datasource.username}")
    private String dbUserName;
    @Value("${datasource.password}")
    private String dbPassword;

    protected DataSource dataSource0(String dbName) {
        try {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://" + dbUrl + ":3306/" + dbName + "?useUnicode=true&allowMultiQueries=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=convertToNull");
            dataSource.setUsername(dbUserName);
            dataSource.setPassword(dbPassword);
            dataSource.setInitialSize(1);
            dataSource.setMaxActive(500);
            dataSource.setMinIdle(1);
            dataSource.setMaxWait(60_000);
            dataSource.setPoolPreparedStatements(true);
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
            dataSource.setTimeBetweenEvictionRunsMillis(60_000);
            dataSource.setMinEvictableIdleTimeMillis(300_000);
            dataSource.setValidationQuery("SELECT 1");
            return dataSource;
        } catch (Throwable throwable) {
            log.error("ex caught, dbUrl={}, dbUserName={}", dbUrl, dbUserName, throwable);
            throw new RuntimeException();
        }
    }



    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");

        return page;
    }

    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor page = new PerformanceInterceptor();
        page.setFormat(true);
        return page;
    }



}
