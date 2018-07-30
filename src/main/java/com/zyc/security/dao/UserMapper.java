package com.zyc.security.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zyc.security.entities.User;

@Mapper
public interface UserMapper {

	@Select("select * from users")
	List<User> selectAll();
	
	@Select("select * from users where id=#{id}")
	User selectById(@Param("id") String id);

	@Select("select * from users where username=#{username}")
	User selectByUsername(@Param("username") String username);

	@Insert("insert into users(id,username,nickname) values(#{user.id},#{user.username},#{user.nickname,jdbcType=VARCHAR})")
	int add(@Param("user") User user);
	
    @Update("update users set username=#{user.username},nickname=#{user.nickname,jdbcType=VARCHAR} where id=#{user.id}")
	void update(@Param("user") User user);

    @Delete("delete from users where id=#{id}")
	void delete(@Param("id") String id);

}
