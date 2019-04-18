package com.jiajie.subjecttable.subject.service;
import com.jiajie.subjecttable.subject.mapper.UserMapper;
import com.jiajie.subjecttable.subject.model.Person;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {

    @Autowired
    UserMapper userMapper;


    public Person selectUser(int id) {
        return userMapper.selectUser(1);
    }

}
