package com.deepak.training_batch_management.controller;

import com.deepak.training_batch_management.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deepak.training_batch_management.entity.Batch;
import com.deepak.training_batch_management.entity.User;
import com.deepak.training_batch_management.repository.BatchRepository;
import com.deepak.training_batch_management.repository.UserRepository;

@RestController
@RequestMapping("/trainer")
public class BatchController {

	private final BatchService batchService;
	@Autowired
	private BatchRepository batchRepository;
	@Autowired
	private UserRepository userRepository;
	
	BatchController(BatchService batchService) {
		this.batchService = batchService;
	}
	@PostMapping("/create-batch")
	public ResponseEntity<?> createBatch(@RequestBody Batch batch, Authentication auth) {

	    String email = auth.getName();

	    User trainer = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    batch.setTrainer(trainer);

	    return ResponseEntity.ok(batchRepository.save(batch));
	}
	
	@GetMapping("/batch/{id}")
	public ResponseEntity<?> getBatch(@PathVariable Long id) {
	    return ResponseEntity.ok(batchRepository.findById(id));
	}
}
