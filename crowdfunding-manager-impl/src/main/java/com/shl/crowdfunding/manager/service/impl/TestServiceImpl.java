package com.shl.crowdfunding.manager.service.impl;

import com.shl.crowdfunding.manager.mapper.TestMapper;
import com.shl.crowdfunding.manager.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public void insert() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name","didijiang");
        testMapper.insert(hashMap);
    }
}
