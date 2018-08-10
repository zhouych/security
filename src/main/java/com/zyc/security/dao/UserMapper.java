package com.zyc.security.dao;

import org.apache.ibatis.annotations.Mapper;

import com.zyc.baselibs.dao.MybatisBaseMapper;
import com.zyc.security.entities.User;


@Mapper
public interface UserMapper extends MybatisBaseMapper<User> {

}
