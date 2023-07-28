package com.yang.auto.config.mybatisPlus;

import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * mybatis plus 配置
 * <p>
 * 如果有多个数据源，新建一个类似的类
 */
@Configuration
@MapperScan(basePackages = "com.yang.auto.entity", sqlSessionTemplateRef = "sqlSessionTemplateAuth")
@Slf4j
public class MybatisPlusConfigAutoClock extends BaseMybatisPlusConfig {

    @Bean(name = "dataSourceAutoClock")
    @Primary
    public DataSource dataSourceAutoClock() {
        return dataSource0("auto_clock");
    }

    @Bean(name = "sqlSessionFactoryAutoClock")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSourceAutoClock") DataSource dataSource) throws Exception {
        //org.mybatis.spring.SqlSessionFactoryBean factoryBean = new org.mybatis.spring.SqlSessionFactoryBean();
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setVfs(SpringBootVFS.class);
        factoryBean.setTypeAliasesPackage("com.yang.auto.entity");

        Resource[] mapperResources = new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*Dao.xml");
        factoryBean.setMapperLocations(mapperResources);

        //org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.addInterceptor(new PaginationInterceptor());
        factoryBean.setConfiguration(configuration);

        return factoryBean.getObject();
    }

    @Primary
    @Bean(name = "sqlSessionTemplateAutoClock")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactoryAutoClock") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Primary
    @Bean(name = "transactionManagerAutoClock")
    public PlatformTransactionManager platformTransactionManager(@Qualifier("dataSourceAutoClock") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "transactionTemplateAutoClock")
    public TransactionTemplate transactionTemplate(@Qualifier("transactionManagerAutoClock") PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

}
