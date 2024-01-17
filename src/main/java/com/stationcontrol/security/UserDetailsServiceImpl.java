package com.stationcontrol.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.stationcontrol.repository.FuncionarioRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new UserDetailsImpl(funcionarioRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Funcionário [" + username + "] não foi encontrado.")));
	}
}
