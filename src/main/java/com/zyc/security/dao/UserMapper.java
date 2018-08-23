package com.zyc.security.dao;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import com.zyc.baselibs.dao.MybatisBaseMapper;
import com.zyc.security.cache.RedisCache;
import com.zyc.security.entities.User;


@CacheNamespace(implementation = RedisCache.class,  flushInterval = 60000)
@Mapper
public interface UserMapper extends MybatisBaseMapper<User> {

}
