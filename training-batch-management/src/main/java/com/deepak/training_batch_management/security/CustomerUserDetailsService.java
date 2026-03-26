package com.deepak.training_batch_management.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.deepak.training_batch_management.entity.User;
import com.deepak.training_batch_management.repository.UserRepository;

@Service
public class CustomerUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
	{
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User Not found"));
		
		return new org.springframework.security.core.userdetails.User(
					user.getEmail(),
					user.getPassword(),
					Collections.singleton(() -> 
							"ROLE_" + user.getRole().name()
							)
				);
	}
}
