package it.aldinucci.todoapp.webcommons.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import it.aldinucci.todoapp.domain.User;

public class UserDetailsImpl implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private transient User user;
	
	
	public UserDetailsImpl(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

	@Override
	public int hashCode() {
		return Objects.hash(user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDetailsImpl other = (UserDetailsImpl) obj;
		return Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "UserDetailsImpl [user=" + user + "]";
	}

}
