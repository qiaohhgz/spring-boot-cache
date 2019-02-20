package com.itunion.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class H2DataSourceConfig {

    @Bean(name = "h2")
    @Qualifier("h2")
    @ConfigurationProperties(prefix="spring.datasource.h2")
    DataSource h2(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "h2JdbcTemplate")
    JdbcTemplate h2JdbcTemplate(@Autowired @Qualifier("h2") DataSource h2){
        return new JdbcTemplate(h2);
    }
}
