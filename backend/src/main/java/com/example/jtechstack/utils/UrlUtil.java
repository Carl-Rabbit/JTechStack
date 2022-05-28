package com.example.jtechstack.utils;

public class UrlUtil {
    public static String appendVersion(String url, int version) {
        if (url.matches(".+[?].+")) {
            return url + "&_jts_version=" + version;
        } else {
            return url + "?_jts_version=" + System.currentTimeMillis();
        }
    }

    public static String appendVersionPattern(String urlPattern) {
        return urlPattern + "[&|?]_jts_version=\\d+$";
    }
}
