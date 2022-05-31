package com.example.jtechstack.spider.common;

import us.codecraft.webmagic.Request;

public class RequestUtil {
    private static final String[][] headers;
    static {
        String str = "accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\n" +
                "accept-encoding: gzip, deflate, br\n" +
                "accept-language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6\n" +
                "cache-control: no-cache\n" +
                "pragma: no-cache\n" +
                "referer: https://www.bing.com/\n" +
                "sec-ch-ua: \" Not A;Brand\";v=\"99\", \"Chromium\";v=\"101\", \"Microsoft Edge\";v=\"101\"\n" +
                "sec-ch-ua-mobile: ?0\n" +
                "sec-ch-ua-platform: \"Windows\"\n" +
                "sec-fetch-dest: document\n" +
                "sec-fetch-mode: navigate\n" +
                "sec-fetch-site: cross-site\n" +
                "sec-fetch-user: ?1\n" +
                "upgrade-insecure-requests: 1\n" +
                "user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.64 Safari/537.36 Edg/101.0.1210.53";
        String[] strArr = str.split("\n");
        headers = new String[strArr.length][];
        for (int i = 0; i < strArr.length; i++) {
            headers[i] = strArr[i].split(": ", 2);
        }
    }

    public static Request create(String url) {
        Request r = new Request(url);
        r.setMethod("GET");
        for (String[] header : headers) {
            r.addHeader(header[0], header[1]);
        }
        return r;
    }

    public static Request createWithAuth(String url) {
        String auth = "Basic ZWY2MDFhODFhY2Q5M2RlOGQ5OTI6NjMzY2QzOWVmYWRlODJjZDBkZDU3MDY3ZDI0MmJkMWMxYjJmZDU0ZQ==";
        return create(url).addHeader("Authorization", auth);
    }
}
