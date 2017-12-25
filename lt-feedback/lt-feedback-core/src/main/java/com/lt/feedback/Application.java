package com.lt.feedback;

import com.lt.feedback.comm.spring.SpringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * 创建：cndym
 * 时间：2017/4/26 16:01
 */

@SpringBootApplication
@ComponentScan
public class Application {

    @Bean
    public SpringUtils getSpringUtils() {
        return new SpringUtils();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
