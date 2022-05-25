package com.example.jtechstack.controller;

import com.example.jtechstack.entity.TestObject;
import com.example.jtechstack.service.TestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    private final TestService testService;
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/test/getAll")
    public List<TestObject> getAllTestObject() {
        return testService.getAllTestObject();
    }

    @PostMapping("/test/create")
    public int createTestObject(@RequestBody Map<String, Object> param) {
        return testService.createTestObject((String) param.get("str"));
    }
}