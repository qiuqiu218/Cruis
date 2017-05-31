package com.magic.crius.bootstrap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan({"com.magic"})
@EnableAspectJAutoProxy
@EnableWebMvc
@Import({ServerPropertiesAutoConfiguration.class, DispatcherServletAutoConfiguration.class,
        EmbeddedServletContainerAutoConfiguration.class})
@PropertySource(value = {"classpath:codis-crius.properties", "classpath:crius-db.properties", "classpath:crius-kafka.properties", "classpath:crius-mongo.properties"})
@ImportResource(value = {"classpath:spring/spring-main.xml"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
