package com.example.media.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.example.media.model.ILoginUser;

public class SecurityUser extends User {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private final ILoginUser loginUser;

	public static ILoginUser getCurrentLoginUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof SecurityUser) {
			return ((SecurityUser) principal).getLoginUser();
			
		} else {
			throw new IllegalStateException("\"getCurrentLoginUser\" is available only during authenticated.");
		}
	}

	public SecurityUser(ILoginUser loginUser) {
		super(loginUser.getUserId(), loginUser.getPassword(), AuthorityUtils.createAuthorityList(loginUser.getRole()));
		this.loginUser = loginUser;
	}

	public ILoginUser getLoginUser() {
		return loginUser;
	}
}
