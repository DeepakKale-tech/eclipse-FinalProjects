package com.deepak.training_batch_management.controller;

import com.deepak.training_batch_management.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deepak.training_batch_management.entity.Batch;
import com.deepak.training_batch_management.repository.BatchRepository;

@RestController
@RequestMapping("/trainer")
public class BatchController {

	private final BatchService batchService;
	@Autowired
	private BatchRepository batchRepository;
	
	BatchController(BatchService batchService) {
		this.batchService = batchService;
	}
	@PostMapping("/create-batch/{trainerId}")
	public ResponseEntity<?> createBatch(@PathVariable Long trainerId, @RequestBody Batch batch)
	{
		return ResponseEntity.ok(batchService.createBatch(trainerId, batch));
	}
	
	@GetMapping("/batch/{id}")
	public ResponseEntity<?> getBatch(@PathVariable Long id) {
	    return ResponseEntity.ok(batchRepository.findById(id));
	}
}
