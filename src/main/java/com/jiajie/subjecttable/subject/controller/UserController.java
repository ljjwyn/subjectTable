package com.jiajie.subjecttable.subject.controller;

import com.alibaba.fastjson.JSONObject;
import com.jiajie.subjecttable.subject.mapper.UserMapper;
import com.jiajie.subjecttable.subject.model.Person;
import com.jiajie.subjecttable.subject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    public UserMapper usermapper;

    @RequestMapping("/showUser/{id}")
    public Person selectUser (@PathVariable int id){
        String test = usermapper.selectUser(id).toString();
        return usermapper.selectUser(id);

    }
    @RequestMapping(value = "/tables", produces = { "application/json;charset=UTF-8" }, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> showTables(@RequestBody String str) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> resquestParams = JSONObject.parseObject(str, Map.class);
        Map<String, String> map = getDBConfig(resquestParams);
        if(map == null || map.isEmpty()){
            return null;
        }
        else{
            //result.put("data", "");
            return resquestParams;
        }
    }

    private Map<String, String> getDBConfig(Map<String, Object> requestParams) {
        Map<String, String> map = new HashMap<>();
        if(requestParams == null){
            return null;
        }
        else{
            String item = (String) requestParams.get("Item");
            String sourcetable = (String) requestParams.get("Sourcetable");
            String schema = (String) requestParams.get("Schema");
            String targettable = (String) requestParams.get("Targettable");
            String targetschema = (String) requestParams.get("Targetschema");
            map.put("Item", item);
            map.put("Sourcetable", sourcetable);
            map.put("Schema", schema);
            map.put("Targettable", targettable);
            map.put("Targetschema", targetschema);
            return map;
        }
    }
}