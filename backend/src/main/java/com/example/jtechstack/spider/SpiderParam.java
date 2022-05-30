package com.example.jtechstack.spider;

public class SpiderParam {
    /* spider config */

    public static final int THREAD_CNT = 5;
    public static final String[] ROOT_URL_LIST = {
            "https://api.github.com/search/repositories?q=language:java&sort=stars",
            // only for test
//            "https://api.github.com/repos/doocs/advanced-java/contributors",
//            "https://api.github.com/repos/macrozheng/mall/contents",
//            "https://api.github.com/repos/GoogleContainerTools/jib/contents",
//            "https://raw.githubusercontent.com/macrozheng/mall/master/pom.xml",
//            "https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-actuator",
    };
    public static final int RETRY_TIMES = 3;
    public static final int SLEEP_TIME = 20_000;        // ms

    /* worker param name */

    public static final String REPO_ID = "repo_id@page_extra";

    /* request extra config name */

    public static final String P_USE_CURL = "use_curl@spider_param";
}
