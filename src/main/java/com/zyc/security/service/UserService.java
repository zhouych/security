package com.zyc.security.service;

import java.util.List;

import com.zyc.security.entities.User;

public interface UserService {
	
	List<User> selectAll(); 
	
	User selectById(String id);

	User create(User user) throws Exception;
	
	User modify(User user) throws Exception;
	
	void delete(String id) throws Exception;
}
