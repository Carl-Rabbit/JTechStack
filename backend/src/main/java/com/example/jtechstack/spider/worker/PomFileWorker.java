package com.example.jtechstack.spider.worker;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.jtechstack.entity.Dependency;
import com.example.jtechstack.entity.MavenRepo;
import com.example.jtechstack.service.DependencyService;
import com.example.jtechstack.service.MavenRepoService;
import com.example.jtechstack.spider.PageWorker;
import com.example.jtechstack.utils.RequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class PomFileWorker implements PageWorker {

    private static final Pattern POM_FILE_URL = Pattern.compile("https://raw\\.githubusercontent\\.com/[^/].*/[^/].*/[^/].*/pom\\.xml");

    private static final Pattern PARAM_PATTERN = Pattern.compile("\\$\\{(.*)}");

    private static final Logger logger = LoggerFactory.getLogger(PomFileWorker.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    // some names for result field
    private static final String DEP_LIST = "dependency_list";
    private static final String JAVA_VERSION = "java_version";

    private final DependencyService dependencyService;
    private final MavenRepoService mavenRepoService;

    public PomFileWorker(DependencyService dependencyService, MavenRepoService mavenRepoService) {
        this.dependencyService = dependencyService;
        this.mavenRepoService = mavenRepoService;
    }

    @Override
    public Pattern getPagePattern() {
        return POM_FILE_URL;
    }

    @Override
    public void process(Page page) {
        logger.info("Start process " + page.getRequest().getUrl());

        Document doc = Jsoup.parse(page.getRawText());

        /* read properties */

        Map<String, String> propertyMap = doc
                .select("properties > *")
                .stream()
                .map(child -> new String[]{child.nodeName(), child.text()})
                .collect(Collectors.toMap(p -> p[0], p -> p[1]));

        /* read dependency */

        List<Map<String, String>> dependencyList = doc
                .select("dependencies > dependency")
                .stream()
                .map(el -> parseDependency(el, propertyMap))
                .filter(d -> d.containsKey("groupId") && d.containsKey("artifactId"))
                .collect(Collectors.toList());

        /* save result */

        page.putField(JAVA_VERSION, propertyMap.getOrDefault("java.version", "unknown"));
        page.putField(DEP_LIST, dependencyList);

        /* add target page */

        for (Map<String, String> dependency : dependencyList.subList(0, 1)) {       // FIXME
            String groupId = dependency.get("groupId");
            String artifactId = dependency.get("artifactId");
            if (isMavenRepoOverdue(groupId, artifactId)) {
                continue;
            }
            String mavenSearchUrl = String.format("https://mvnrepository.com/artifact/%s/%s", groupId, artifactId);
            page.addTargetRequest(RequestUtil.create(mavenSearchUrl).putExtra("JTechStack-UseCurl", true));
            logger.info("Add target {}", mavenSearchUrl);
        }
    }

    private Map<String, String> parseDependency(Element el, Map<String, String> propertyMap) {
        return el.children()
                .stream()
                .map(child -> {
                    String key = child.nodeName();
                    if (key.matches("(?i)groupId")) {
                        key = "groupId";
                    } else if (key.matches("(?i)artifactId")) {
                        key = "artifactId";
                    }
                    String value = child.text();
                    Matcher m = PARAM_PATTERN.matcher(value);
                    if (m.matches()) {
                        value = propertyMap.getOrDefault(m.group(1), "unknown");
                    }
                    return new String[]{key, value};
                })
                .collect(Collectors.toMap(p -> p[0], p -> p[1]));
    }

    private boolean isMavenRepoOverdue(String groupId, String artifactId) {
        MavenRepo mavenRepo = mavenRepoService.getOne(new QueryWrapper<MavenRepo>()
                .allEq(new HashMap<String, String>(){{
                    this.put("group_id", groupId);
                    this.put("artifact_id", artifactId);
                }}));
        if (mavenRepo == null) {
            return true;
        }
        // FIXME
//        return Duration.between(LocalDateTime.now(), mavenRepo.getJtsTimestamp()).toMinutes() > 24 * 60;
        return false;
    }

    @Override
    public boolean checkOverdue(ResultItems resultItems) {
        return false;
    }

    @Override
    public void save(ResultItems resultItems, Task task) {
        String javaVersion = resultItems.get(JAVA_VERSION);
        List<Map<String, String>> dependencyList = resultItems.get(DEP_LIST);
        System.out.println(javaVersion);

//        List<Dependency> resultList = new ArrayList<>();
//        for (Map<String, String> map : dependencyList) {
//            String content;
//            try {
//                content = objectMapper.writeValueAsString(map);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//                content = "{\"status\": \"Parse error\"}";
//            }
//            Dependency dependency = Dependency.builder()
//                    .version(map.get("version"))
//                    .content(content)
//                    .build();
//            resultList.add(dependency);
//        }
    }
}
