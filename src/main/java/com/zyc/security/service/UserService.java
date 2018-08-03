package com.zyc.security.service;

import java.util.List;

import com.zyc.baselibs.vo.DeleteMode;
import com.zyc.security.entities.User;

public interface UserService {
	
	List<User> selectAll(); 
	
	User selectById(String id);
	
	User selectByUsername(String username);

	User create(User user) throws Exception;
	
	User modify(User user) throws Exception;
	
	/**
	 * 删除数据
	 * @param id 待删除的数据主键ID
	 * @param mode 删除模式：逻辑删除 | 物理删除
	 * @return 删除结果：true - 成功；false - 失败。
	 * @throws Exception
	 */
	boolean delete(String id, DeleteMode mode) throws Exception;
	
	/**
	 * 逻辑删除（执行update操作，将数据更新为已删除状态）
	 * @param id 待删除的数据主键ID
	 * @return 删除结果：true - 成功；false - 失败。
	 * @throws Exception
	 */
	boolean deleteOnLogic(String id) throws Exception;

	/**
	 * 物理删除（执行delete操作，将数据从数据库删除）
	 * @param id 待删除的数据主键ID
	 * @return 删除结果：true - 成功；false - 失败。
	 * @throws Exception
	 */
	boolean deleteOnPhysical(String id) throws Exception;
}
