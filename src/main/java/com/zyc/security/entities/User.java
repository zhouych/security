package com.zyc.security.entities;

import com.zyc.baselibs.annotation.DatabaseTable;
import com.zyc.baselibs.annotation.EntityField;
import com.zyc.baselibs.entities.BaseEntity;

@DatabaseTable(name = "users")
public class User extends BaseEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = -9185122376449426262L;
	
	@EntityField(required = true, uneditable = true)
	private String username;
	private String nickname;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	@Override
	public User clean() {
		super.clean();
		this.username = null;
		this.nickname = null;
		return this;
	}
	
	@Override
	public String toString() {
		return String.format("{ id: \"%s\", username: \"%s\", nickname: \"%s\" }", this.getId(), this.getUsername(), this.nickname);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(null == this.getId() || null == this.getUsername()) {
			return false;
		}
		
		if(null == obj || !(obj instanceof User)) {
			return false;
		}
		
		User user = (User) obj;
		
		return this.getId().equals(user.getId()) && this.getUsername().equals(user.getUsername());
	}
	
	@Override
	public int hashCode() {
		int nums[] = { 
			null == this.getId() ? 0 : this.getId().hashCode(),
			null == this.getUsername() ? 0 : this.getUsername().hashCode() 
		};

		int hash = 0;
		for (int num : nums) {
			hash = 31 * hash + num;
		}
		return hash;
	}
}
