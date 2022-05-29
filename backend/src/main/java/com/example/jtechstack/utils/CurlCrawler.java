package com.example.jtechstack.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CurlCrawler {

    public static void main(String[] args) {
        String result = CurlCrawler.get("https://mvnrepository.com/artifact/org.jsoup/jsoup");
        System.out.println(result);
    }

    public static String get(String url) {
        String[] cmd = {
                "powershell.exe",
                "\"curl " + url +  " | Select -ExpandProperty Content\""
        };
        return execCmd(cmd);
    }

    private static String execCmd(String[] cmd) {
        ProcessBuilder process = new ProcessBuilder(cmd);
        Process p;
        try {
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            return builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
