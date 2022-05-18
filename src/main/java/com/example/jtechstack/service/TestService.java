package com.example.jtechstack.service;

import com.example.jtechstack.entity.TestObject;

import java.util.List;

public interface TestService {
    List<TestObject> getAllTestObject();
    int createTestObject(String str);
    TestObject getFirst();
}
