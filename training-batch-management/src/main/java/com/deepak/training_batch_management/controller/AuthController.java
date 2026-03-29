package com.deepak.training_batch_management.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deepak.training_batch_management.dto.AuthRequest;
import com.deepak.training_batch_management.entity.User;
import com.deepak.training_batch_management.repository.UserRepository;
import com.deepak.training_batch_management.security.JwtUtil;


@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/login")
	public Map<String, Object> login(@RequestBody AuthRequest request)
	{
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("User Not Found"));
		
		if(!user.getPassword().equals(request.getPassword()))
		{
			throw new RuntimeException("Invalid Password");
		}
		
		String token = jwtUtil.generateToken(user.getEmail());
		
		return Map.of("token", token,
				"role", user.getRole().name());
	}
}
