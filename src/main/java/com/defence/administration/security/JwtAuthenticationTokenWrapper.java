package com.defence.administration.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationTokenWrapper extends AbstractAuthenticationToken {

	private static final long serialVersionUID = -8313986794491286822L;

	private String token;
	private final UserDetails principle;

	public JwtAuthenticationTokenWrapper(UserDetails principle, String token) {
		super(principle.getAuthorities());
		this.principle = principle;
		this.token = token;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.AbstractAuthenticationToken#
	 * isAuthenticated()
	 */
	@Override
	public boolean isAuthenticated() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.Authentication#getCredentials()
	 */
	@Override
	public Object getCredentials() {
		return getToken();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.Authentication#getPrincipal()
	 */
	@Override
	public UserDetails getPrincipal() {
		return principle;
	}

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}