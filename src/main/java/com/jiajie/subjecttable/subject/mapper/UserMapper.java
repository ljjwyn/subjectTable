package com.jiajie.subjecttable.subject.mapper;

import com.jiajie.subjecttable.subject.model.Person;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    @Select("SELECT * FROM Person WHERE id = #{id}")
    Person selectUser(int id);
}