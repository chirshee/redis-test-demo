package com.redis.test.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.test.demo.entity.Item;
import com.redis.test.demo.mapper.ItemMapper;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service()
public class cachePassService {
    private static final Logger log = LoggerFactory.getLogger(cachePassService.class);
    // 定义缓存中key的命名前缀
    private static final String key = "item1:";
    //定义Mapper
    @Resource
    private ItemMapper itemMapper;
    //定义redis操作组件
    @Resource
    private RedisTemplate redisTemplate;
    // 定义json序列化和反序列框架
    @Resource
    private ObjectMapper objectMapper;

    public Object getItemInfo(String code) throws Exception {
        Item item = null;
        // 定义一个真正的key
        final String realKey = key + code;
        log.info("key值Wie{}", realKey);
        // 定义的redisTemplate数据操作框架
        ValueOperations valueOperations = redisTemplate.opsForValue();
        if (redisTemplate.hasKey(realKey)) {
            log.info("---------从缓存中取出数据------");
            Object res = valueOperations.get(realKey);
            if (res != null && !Strings.isEmpty(res.toString())) {
                item = objectMapper.readValue(res.toString(), Item.class);
            }
        } else {
            // 缓存中没有该商品
            log.info("----------从数据库中取出数据----------");
            item = itemMapper.selectByCode(code);
            if (item != null) {
                log.info("------------将数据库中的数据写入缓存---------");
                // 将数据库中查到的数据进行序列号写入缓存中
                valueOperations.set(realKey, objectMapper.writeValueAsString(item));
            } else {
                // 设置失效时间，并存入空
                log.info("查询数据");
                valueOperations.set(realKey, "", 30, TimeUnit.MINUTES); // minutes
            }
        }
        return item;
    }

    public Item selectByCode(String code) {
        return itemMapper.selectByCode(code);
    }
}
