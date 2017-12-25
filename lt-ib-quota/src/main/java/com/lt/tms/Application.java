package com.lt.tms;

import com.lt.tms.comm.spring.SpringUtils;
import com.lt.tms.utils.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ResourceUtils;

import java.io.File;

@SpringBootApplication
@ComponentScan
@EnableCaching
@EnableScheduling
public class Application {

    @Bean
    public SpringUtils getSpringUtils() {
        return new SpringUtils();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}
