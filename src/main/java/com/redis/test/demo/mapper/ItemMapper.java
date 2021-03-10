package com.redis.test.demo.mapper;

import com.redis.test.demo.entity.Item;
import io.lettuce.core.dynamic.annotation.Param;

public interface ItemMapper {
    // 根据编码查询
    Item selectByCode(@Param("code") String code);

}