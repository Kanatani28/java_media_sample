package com.example.media.model;

import com.example.media.Constants;

public class Admin implements ILoginUser {
	
	private int id;
	
	private String email;
	
	private String password;
	
	private String name;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getUserId() {
		return this.email;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getRole() {
		return Constants.LoginUserRole.ADMINISTRATOR;
	}

}
