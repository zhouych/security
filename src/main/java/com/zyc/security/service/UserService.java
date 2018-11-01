package com.zyc.security.service;

import java.util.List;

import com.zyc.baselibs.service.EntityDeleteService;
import com.zyc.security.entities.User;

public interface UserService extends EntityDeleteService {
	
	List<User> selectAll(); 
	
	User selectById(String id);
	
	User selectByUsername(String username);

	User create(User user) throws Exception;
	
	User modify(User user) throws Exception;
}
