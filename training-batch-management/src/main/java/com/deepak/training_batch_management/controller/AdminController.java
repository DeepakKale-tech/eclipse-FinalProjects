package com.deepak.training_batch_management.controller;

import com.deepak.training_batch_management.repository.BatchRepository;
import com.deepak.training_batch_management.repository.SyllabusRepository;
import com.deepak.training_batch_management.repository.UserRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deepak.training_batch_management.entity.Batch;
import com.deepak.training_batch_management.entity.SyllabusTopic;
import com.deepak.training_batch_management.entity.User;
import com.deepak.training_batch_management.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	private final UserRepository userRepository;
	@Autowired
	private UserService userService;

	AdminController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	@Autowired
	private SyllabusRepository syllabusRepository;

	@Autowired
	private BatchRepository batchRepository;

	@PostMapping("/add-topic/{batchId}")
	public ResponseEntity<?> addTopic(@PathVariable Long batchId, @RequestBody SyllabusTopic topic) {

	    Batch batch = batchRepository.findById(batchId)
	            .orElseThrow(() -> new RuntimeException("Batch not found"));

	    topic.setBatch(batch);
	    topic.setCompleted(false);

	    return ResponseEntity.ok(syllabusRepository.save(topic));
	}
	
	@PostMapping("/create-user")
	public ResponseEntity<?> createUser(@RequestBody User user)
	{
		return ResponseEntity.ok(userService.createUser(user));
	}
	
	@GetMapping("/users")
	public List<User> getAllUsers()
	{
		return userRepository.findAll();
	}
}
