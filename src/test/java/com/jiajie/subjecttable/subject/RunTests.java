package com.jiajie.subjecttable.subject;

import com.jiajie.subjecttable.subject.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@EnableAutoConfiguration
public class RunTests {
    @Resource
    private UserMapper userMapper;

    @Test
    public void contextLoads() {
    }


    @Test
    public void G() {
        System.out.println(userMapper.selectUser(1));
    }
}