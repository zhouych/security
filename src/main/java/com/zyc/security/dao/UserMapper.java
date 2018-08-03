package com.zyc.security.dao;

import org.apache.ibatis.annotations.Mapper;

import com.zyc.baselibs.dao.MybatisBaseMapper;
import com.zyc.security.entities.User;


@Mapper
public interface UserMapper extends MybatisBaseMapper<User> {

/*	@Select("select * from users")
	List<User> selectAll();*/
	
	/*@Select("select * from users where id=#{id}")
	User selectById(@Param("id") String id);*/

/*	@Select("select * from users where username=#{username}")
	User selectByUsername(@Param("username") String username);*/

	/*@Insert("insert into users(id,username,nickname,datastatus,createdat,updatedat,version) " +
		    "values(#{user.id},#{user.username},#{user.nickname,jdbcType=VARCHAR},#{user.datastatus},#{user.createdat},#{user.updatedat},#{user.version})")
	int add(@Param("user") User user);
	*/
	
    /*@Delete("delete from users where id=#{id}")
	int delete(@Param("id") String id);*/

}
