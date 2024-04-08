package com.learning.ecommerce.dto;

import com.learning.ecommerce.models.Role;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class UserDto {
	private int uid;

	private String userName;
	private String userFirstName;
	private String userLastName;
	private String userPassword;

	private Set<RoleDto> role;



	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Set<RoleDto> getRole() {
		return role;
	}

	public void setRole(Set<RoleDto> role) {
		this.role = role;
	}
}
