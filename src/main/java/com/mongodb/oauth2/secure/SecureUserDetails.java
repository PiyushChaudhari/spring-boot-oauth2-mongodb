package com.mongodb.oauth2.secure;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SecureUserDetails implements UserDetails {

	private static final long serialVersionUID = 505241529365502116L;

	private String username;
	private String password;
	private boolean accountNonLocked;
	private Collection<GrantedAuthority> authorities;
	
	public SecureUserDetails() {
	}
	
	public SecureUserDetails(String username,String password,boolean accountNonLocked,Collection<GrantedAuthority> authorities) {
		this.username=username;
		this.password=password;
		this.authorities=authorities;
		this.accountNonLocked = accountNonLocked;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SecureUserDetails [username=");
		builder.append(username);
		builder.append(", password=");
		builder.append(password);
		builder.append(", authorities=");
		builder.append(authorities);
		builder.append("]");
		return builder.toString();
	}

}
