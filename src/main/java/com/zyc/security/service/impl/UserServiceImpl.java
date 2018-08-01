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
import com.zyc.baselibs.service.AbstractBaseService;
import com.zyc.security.dao.UserMapper;
import com.zyc.security.entities.User;
import com.zyc.security.service.UserService;

@Service
public class UserServiceImpl extends AbstractBaseService implements UserService {

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
		
		User entity = this.selectById(user.getId());
		if(null == entity || !entity.equals(user)) {
			throw new BussinessException("This user does not exist or this user infomation does not matchs. (username=" + user.getUsername() + ")");
		}

		BeanUtils.copyProperties(user, entity, EntityFieldUtils.uneditableFields(entity));
		this.update(this.userMapper, entity, ACTION_UPDATE);
		return entity;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(String id) throws Exception {
		AssertThrowNonRuntime.hasText(id, "This parameter 'id' is null or empty. (id=" + id + ")");
		
		User entity = this.selectById(id);
		if(entity.getDatastatus().equals(DataStatus.DELETED.toString()) || entity.getDatastatus().equals(DataStatus.LOCKED.toString())) {
			throw new BussinessException("The user was " + entity.getDatastatus().toLowerCase() + ". (username=" + entity.getUsername() + ")");
		}
		
		entity.setDatastatus(DataStatus.DELETED.toString());
		this.update(this.userMapper, entity, ACTION_DELETE);
	}

}
