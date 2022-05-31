package com.example.jtechstack.spider;

import us.codecraft.webmagic.Page;

import java.util.regex.Pattern;

public interface PageWorker {
    Pattern getPagePattern();

    void process(Page page) throws Exception;
}
