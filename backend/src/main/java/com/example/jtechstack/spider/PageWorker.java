package com.example.jtechstack.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Task;

public interface PageWorker {
    String getPagePattern();

    void process(Page page);

    void save(Task task);
}
