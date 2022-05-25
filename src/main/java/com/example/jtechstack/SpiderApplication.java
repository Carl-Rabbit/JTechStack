package com.example.jtechstack;

import com.example.jtechstack.service.TestService;
import com.example.jtechstack.utils.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@MapperScan("com.example.jtechstack.mapper")
@SpringBootApplication
public class SpiderApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpiderApplication.class, args);

        ApplicationContext context = SpringUtil.getApplicationContext();
        TestService testService = context.getBean(TestService.class);
        System.out.println(testService.getFirst());

        System.exit(0);
    }
}
