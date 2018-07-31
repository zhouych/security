package com.zyc.security.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zyc.baselibs.annotation.EntityFieldUtils;
import com.zyc.baselibs.asserts.AssertThrowNonRuntime;
import com.zyc.baselibs.entities.DataStatus;
import com.zyc.baselibs.ex.BussinessException;
import com.zyc.security.dao.UserMapper;
import com.zyc.security.entities.User;
import com.zyc.security.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

	@Override
	public List<User> selectAll() {
		return this.userMapper.selectAll();
	}
	
	@Override
	public User selectById(String id) {
		return StringUtils.isBlank(id) ? null : this.userMapper.selectById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public User create(User user) throws Exception {
		EntityFieldUtils.verifyRequired(user);
		
		if(null != this.userMapper.selectById(user.getId()) || null != this.userMapper.selectByUsername(user.getUsername())) {
			throw new BussinessException("This user already exists. (id=" + user.getId() + ", username=" + user.getUsername() + ")");
		}
		
		user.init();
		
		int result = this.userMapper.add(user);
		
		return result > 0 ? this.userMapper.selectById(user.getId()) : null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public User modify(User user) throws Exception {
		EntityFieldUtils.verifyRequired(user);
		
		User old = this.selectById(user.getId());
		if(null == old || !old.equals(user)) {
			throw new BussinessException("This user does not exist or this user infomation does not matchs. (id=" + user.getId() + ")");
		}
		
		BeanUtils.copyProperties(user, old, EntityFieldUtils.uneditableFields(old));
		
		this.userMapper.update(old);
		
		return old;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(String id) throws Exception {
		AssertThrowNonRuntime.hasText(id, "This parameter 'id' is null or empty. (id=" + id + ")");
		
		User user = this.selectById(id);
		if(user.getDatastatus().equals(DataStatus.DELETED.toString()) ) {
			throw new BussinessException("The user was deleted. (username=" + user.getUsername() + ")");
		} else if(user.getDatastatus().equals(DataStatus.LOCKED.toString())) {
			throw new BussinessException("The user was locked. (username=" + user.getUsername() + ")");
		}
		
		this.userMapper.delete(id);
	}

}
