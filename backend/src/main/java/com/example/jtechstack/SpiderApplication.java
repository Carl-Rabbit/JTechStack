package com.example.jtechstack;

import com.example.jtechstack.spider.SpiderManager;
import com.example.jtechstack.utils.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.util.*;

import static com.example.jtechstack.spider.common.SpiderParam.REFRESH_REPO_SEARCH;

@EnableOpenApi
@MapperScan("com.example.jtechstack.mapper")
@SpringBootApplication
public class SpiderApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpiderApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        SpiderManager spiderManager = SpringUtil.getBean(SpiderManager.class);
        spiderManager.start();

        (new Timer()).schedule(new TimerTask() {
            public void run() {
                spiderManager.stop();
                spiderManager.initSpider();
                spiderManager.start();
            }
        }, REFRESH_REPO_SEARCH * 1000, REFRESH_REPO_SEARCH * 1000);
    }
}
