package com.deepak.training_batch_management.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deepak.training_batch_management.entity.User;
import com.deepak.training_batch_management.repository.UserRepository;

@RestController
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/profile")
	public User getProfile(Authentication auth) {
	    String email = auth.getName();
	    return userRepository.findByEmail(email).orElseThrow();
	}
	
	@PutMapping("/profile")
	public ResponseEntity<?> updateProfile(Authentication auth, @RequestBody User updated) {

	    String email = auth.getName();
	    User user = userRepository.findByEmail(email).orElseThrow();

	    user.setName(updated.getName());
	    user.setEmail(updated.getEmail());
	    user.setPhone(updated.getPhone());
	    
	    userRepository.save(user);

	    return ResponseEntity.ok("Profile updated");
	}
	
	@PutMapping("/change-password")
	public ResponseEntity<?> changePassword(Authentication auth, @RequestBody Map<String, String> data) {

	    String email = auth.getName();
	    User user = userRepository.findByEmail(email).orElseThrow();

	    String oldPass = data.get("oldPassword");
	    String newPass = data.get("newPassword");

	    if (!user.getPassword().equals(oldPass)) {
	        return ResponseEntity.badRequest().body("Old password incorrect ❌");
	    }

	    user.setPassword(newPass);
	    userRepository.save(user);

	    return ResponseEntity.ok("Password changed ✅");
	}
}
