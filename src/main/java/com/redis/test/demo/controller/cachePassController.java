package com.redis.test.demo.controller;

import com.redis.test.demo.entity.Item;
import com.redis.test.demo.service.cachePassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class cachePassController {
    private static final Logger log = LoggerFactory.getLogger(cachePassController.class);

    @Resource
    private cachePassService cacheService;

    @RequestMapping("info/{code}")
    public Map<String, Object> getItem(@PathVariable String code) {
        //定义接口返回的数据格式
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 0);
        resMap.put("msg", "成功");
        try {
            resMap.put("data", cacheService.getItemInfo(code));
        } catch (Exception e) {
            resMap.put("code", -1);
            resMap.put("msg", "失败");
        }
        return resMap;
    }

    @RequestMapping("getUser/{code}")
    public Item hello(@PathVariable String code) {
        return cacheService.selectByCode(code);
    }
}
