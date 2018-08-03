package com.zyc.security.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zyc.baselibs.annotation.EntityFieldUtils;
import com.zyc.baselibs.asserts.AssertThrowNonRuntime;
import com.zyc.baselibs.commons.CollectionUtils;
import com.zyc.baselibs.commons.StringUtils;
import com.zyc.baselibs.entities.DataStatus;
import com.zyc.baselibs.ex.BussinessException;
import com.zyc.baselibs.service.AbstractBaseService;
import com.zyc.baselibs.vo.DeleteMode;
import com.zyc.security.dao.UserMapper;
import com.zyc.security.entities.User;
import com.zyc.security.service.UserService;

@Service
public class UserServiceImpl extends AbstractBaseService implements UserService {

    @Autowired
    private UserMapper userMapper;
    
	@Override
	public List<User> selectAll() {
		//return this.userMapper.selectAll();
		return this.userMapper.select(new User().clean());
	}
	
	@Override
	public User selectById(String id) {
		//return StringUtils.isBlank(id) ? null : this.userMapper.selectById(id);
		if(StringUtils.isBlank(id)) {
			return null;
		}
		
		User condition = new User().clean();
		condition.setId(id);
		
		//List<User> users = this.userMapper.select(condition);
		//return CollectionUtils.hasElement(users) ? users.get(0) : null;
		return this.userMapper.load(id, User.class);
	}

	@Override
	public User selectByUsername(String username) {
		if(StringUtils.isBlank(username)) {
			return null;
		}
		
		User condition = new User().clean();
		condition.setUsername(username);
		
		List<User> users = this.userMapper.select(condition);
		return CollectionUtils.hasElement(users) ? users.get(0) : null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public User create(User user) throws Exception {
		EntityFieldUtils.verifyRequired(user);
		
		if(null != this.selectById(user.getId()) || null != this.selectByUsername(user.getUsername())) {
			throw new BussinessException("This user already exists. (id=" + user.getId() + ", username=" + user.getUsername() + ")");
		}
		
		user.init();
		
		int result = this.userMapper.insert(user);
		
		return result > 0 ? this.selectById(user.getId()) : null;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delete(String id, DeleteMode mode) throws Exception {
		AssertThrowNonRuntime.notNull(mode, "This parameter 'mode' is null or empty. (mode=" + mode.toString() + ")");
		
		if(mode.equals(DeleteMode.LOGIC)) {
			return this.deleteOnLogic(id);
		} else if(mode.equals(DeleteMode.PHYSICAL)) {
			return this.deleteOnPhysical(id);
		} else {
			throw new BussinessException("This deletion mode is not supported. (mode=" + mode.toString() + ")");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteOnLogic(String id) throws Exception {
		AssertThrowNonRuntime.hasText(id, "This parameter 'id' is null or empty. (id=" + id + ")");
		
		User entity = this.selectById(id);
		if(entity.getDatastatus().equals(DataStatus.DELETED.toString()) || entity.getDatastatus().equals(DataStatus.LOCKED.toString())) {
			throw new BussinessException("The user was " + entity.getDatastatus().toLowerCase() + ". (username=" + entity.getUsername() + ")");
		}
		
		entity.setDatastatus(DataStatus.DELETED.toString());
		return this.update(this.userMapper, entity, ACTION_DELETE) > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteOnPhysical(String id) throws Exception {
		AssertThrowNonRuntime.hasText(id, "This parameter 'id' is null or empty. (id=" + id + ")");

		User entity = this.selectById(id);
		if(entity.getDatastatus().equals(DataStatus.LOCKED.toString())) {
			throw new BussinessException("The user was " + entity.getDatastatus().toLowerCase() + ". (username=" + entity.getUsername() + ")");
		}
		
		User condition = new User().clean();
		condition.setId(entity.getId());
		condition.setVersion(entity.getVersion());
		return this.userMapper.delete(entity)> 0;
	}

}
