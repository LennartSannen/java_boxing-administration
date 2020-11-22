package com.defence.administration.security;


import com.defence.administration.model.User;
import com.defence.administration.service.UserService;
import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private JwtTokenUtil jwtTokenUtil;
	private UserService userService;

	/**
	 * Constructor
	 * 
	 * @param jwtTokenUtil
	 * @param authManager
	 */
	public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserService userService) {
		this.jwtTokenUtil = jwtTokenUtil;
		this.userService = userService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.
	 * servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String username;
		String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (Objects.nonNull(authToken)) {
			// get username from token
			username = jwtTokenUtil.getUsernameFromToken(authToken);
			if (Objects.nonNull(username)) {
				// get user
				User user = userService.findUserByUsername(username);
				if (jwtTokenUtil.validateToken(authToken, user)) {
					// create authentication
					SecurityContextHolder.getContext()
							.setAuthentication(new JwtAuthenticationTokenWrapper(user, authToken));
				}
			}
		}
		chain.doFilter(request, response);
	}
}