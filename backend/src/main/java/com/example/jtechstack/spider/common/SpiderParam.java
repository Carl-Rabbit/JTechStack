package com.example.jtechstack.spider.common;

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
    public static final int SLEEP_TIME = 60_000;        // ms

    public static final int PRIORITY_ROOT = 999;
    public static final int PRIORITY_CONTENT = 100;
    public static final int PRIORITY_POM = 99;
    public static final int PRIORITY_MVN_REPO = 90;
    public static final int PRIORITY_CONTRIBUTOR = 1;

    /* worker param name */

    public static final String REPO_ID = "repo_id@page_extra";

    /* request extra config name */

    public static final String P_USE_CURL = "use_curl@spider_param";
}