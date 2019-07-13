package com.example.media.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.example.media.Constants;
import com.example.media.LogFormat;
import com.example.media.form.LoginForm;
import com.example.media.service.LoginAdministratorDetailsService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static Logger _logger = LoggerFactory.getLogger(SecurityConfig.class);
	
	private static final String TOP_PAGE = "/articles/list";
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new LoginAdministratorDetailsService();
	};
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	};
	
	@Override 
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/imgs/**", "/css/**", "/js/**", "/fonts/**", "/webjars/**", "/audio/**", "/tablet/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/articles/list", "/articles/detail", "/login", "/login/auth").permitAll()
								.anyRequest().authenticated();
		// Login
		http.formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/login/auth")
			.successHandler(new AuthenticationSuccessHandlerImpl())
			.failureHandler(new AuthenticationFailureHandlerImpl())
			.usernameParameter("userId").passwordParameter("password");

		// Logout
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/login").invalidateHttpSession(true);

		// Session
		http.sessionManagement().invalidSessionUrl("/login");
	}

	public static class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

		@Override
		public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication)
				throws IOException, ServletException {
			_logger.info(LogFormat.LOGIN_SUCCESS, httpServletRequest.getParameter("userId"));
			RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
			redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, TOP_PAGE);
		}
	}
	
	public static class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

		@Override
		public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
				HttpServletResponse httpServletResponse, AuthenticationException authenticationException)
				throws IOException, ServletException {

			LoginForm loginForm = new LoginForm();
			loginForm.setUserId(httpServletRequest.getParameter("userId"));
			loginForm.setPassword(httpServletRequest.getParameter("password"));

			_logger.warn(LogFormat.LOGIN_FAILURE, loginForm.getUserId());

			if (StringUtils.isEmpty(loginForm.getUserId())) {
				// UserId not inputted
				loginForm.addLoginError(new FieldError("loginForm", "userId", null, false,
						new String[] { "NotNull" }, new String[] {"UserID"}, null));
			}

			if (StringUtils.isEmpty(loginForm.getPassword())) {
				// Password not inputted
				loginForm.addLoginError(new FieldError("loginForm", "password", null, false,
						new String[] { "NotNull" }, new String[] {"Password"}, null));
			}

			if (loginForm.getLoginErrors().size() < 1) {
				if (authenticationException.getClass() == UsernameNotFoundException.class
						|| authenticationException.getClass() == BadCredentialsException.class) {
					loginForm.addLoginError(
							new ObjectError("loginForm", new String[] { "error.auth.badcredentials" }, null, null));

				} else {
					throw new IllegalStateException("Incorrect Authentication Config.");
				}
			}
			
			httpServletRequest.getSession().setAttribute(Constants.SESSION_KEY_LOGIN_FORM, loginForm);

			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login/");
			return;
		}
	}

}
