package com.example.jtechstack.spider;

import com.fasterxml.jackson.core.JsonProcessingException;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

import java.util.regex.Pattern;

public interface PageWorker {
    Pattern getPagePattern();

    void process(Page page) throws Exception;

    boolean checkOverdue(ResultItems resultItems);

    void save(ResultItems resultItems, Task task);
}
