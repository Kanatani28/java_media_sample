package com.example.media.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.media.mapper.AdminMapper;
import com.example.media.model.ILoginUser;
import com.example.media.security.SecurityUser;

public class LoginAdministratorDetailsService implements UserDetailsService {
	
	@Autowired
	AdminMapper adminMapper;

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

		ILoginUser loginUser = adminMapper.find(id);

		if (loginUser == null) {
			throw new UsernameNotFoundException("User not found for login id: " + id);

		} else {
			
			SecurityUser springSecurityUser = new SecurityUser(loginUser);
			
			return springSecurityUser;
		}
	}
}
