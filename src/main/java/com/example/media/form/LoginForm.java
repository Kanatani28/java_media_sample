package com.example.media.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.ObjectError;

public class LoginForm implements Serializable {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private String userId = "";
	private String password = "";
	private List<ObjectError> loginErrors = new ArrayList<>();

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 * the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 * the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public List<ObjectError> getLoginErrors() {
		return loginErrors;
	}

	public void addLoginError(ObjectError objectError) {
		this.loginErrors.add(objectError);
	}

	public void clearLoginErrors(List<ObjectError> loginErrors) {
		this.loginErrors = new ArrayList<>();
	}
}
