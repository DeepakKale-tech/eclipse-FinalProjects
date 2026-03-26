package com.deepak.training_batch_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deepak.training_batch_management.entity.User;
import com.deepak.training_batch_management.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User createUser(User user)
	{
		return userRepository.save(user);
	}
}
