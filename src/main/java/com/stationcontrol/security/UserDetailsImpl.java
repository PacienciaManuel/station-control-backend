package com.stationcontrol.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.stationcontrol.model.Funcionario;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SuppressWarnings("serial")
public class UserDetailsImpl implements UserDetails {

	@Getter
	private final transient Funcionario funcionario;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(funcionario.getPapel());
	}

	@Override
	public String getPassword() {
		return funcionario.getSenha();
	}

	@Override
	public String getUsername() {
		return funcionario.getEmail();
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
		return true;
	}
}
