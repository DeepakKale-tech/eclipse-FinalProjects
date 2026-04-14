package com.deepak.training_batch_management.service;

import java.util.List;

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
		if(userRepository.existsByEmail(user.getEmail()))
		{
			throw new RuntimeException("Email Already Exists ❌");
		}
		return userRepository.save(user);
	}
	
	public List<User> findAll() {
	    return userRepository.findAll();
	}

	public User update(Long id, User user) {
	    User existing = userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    existing.setName(user.getName());
	    existing.setEmail(user.getEmail());
	    existing.setPhone(user.getPhone());
	    existing.setRole(user.getRole());

	    return userRepository.save(existing);
	}

	public void delete(Long id) {
	    userRepository.deleteById(id);
	}

	public List<User> findTrainerByName(String name) {
	    return userRepository.findByNameContainingIgnoreCaseAndRole(name, "TRAINER");
	}
}
