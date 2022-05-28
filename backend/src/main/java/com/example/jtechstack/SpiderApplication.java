package com.example.jtechstack;

import com.example.jtechstack.spider.SpiderManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@MapperScan("com.example.jtechstack.mapper")
@SpringBootApplication
public class SpiderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpiderApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    private final SpiderManager spiderManager;

    public SpiderApplication(SpiderManager spiderManager) {
        this.spiderManager = spiderManager;
    }

    @Override
    public void run(String... args) throws Exception {
        spiderManager.start();
    }
}
