package com.example.jtechstack.spider.common;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SpiderParam {

    /* worker param name */

    public static final String PAGE_SIZE = "per_page@page_extra";
    public static final String PAGE_NUM = "page@page_extra";
    public static final String REPO_ID = "repo_id@page_extra";

    /* request extra config name */

    public static final String P_USE_CURL = "use_curl@spider_param";
    public static final String P_STATUS_LINE = "status_line@spider_param";

    /* spider config */

    public static final String CACHE_PATH = Paths.get(System.getProperty("user.dir"), "./.cache").toString();

    public static final int THREAD_CNT = 5;
    public static final String[] ROOT_URL_LIST = {
            "https://api.github.com/search/repositories?q=language:java&sort=stars&per_page=10&page=1",
            // only for test
//            "https://api.github.com/repos/doocs/advanced-java/contributors",
//            "https://api.github.com/repos/macrozheng/mall/contents",
//            "https://api.github.com/repos/GoogleContainerTools/jib/contents",
//            "https://raw.githubusercontent.com/macrozheng/mall/master/pom.xml",
//            "https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-actuator",
    };
    public static Map<String, Object> ROOT_PAGE_EXTRA = new HashMap<String, Object>(){{
        this.put(PAGE_SIZE, 10);
        this.put(PAGE_NUM, 1);
        this.put(REPO_ID, 127988011);   // macrozheng/mall
    }};

    public static final int RETRY_TIMES = 3;
    public static final int SLEEP_TIME = 5_000;        // ms

    public static final int PRIORITY_ROOT = 999;
    public static final int PRIORITY_CONTENT = 100;
    public static final int PRIORITY_POM = 99;
    public static final int PRIORITY_MVN_REPO = 90;
    public static final int PRIORITY_CONTRIBUTOR = 1;
    public static final int PRIORITY_SEARCH_REPO = 0;

    public static final int REFRESH_REPO_SEARCH = 24 * 60;     // minutes
    public static final int REFRESH_REPO = 24 * 60;     // minutes
    public static final int REFRESH_MAVEN_REPO = 24 * 60;      // minutes
}
