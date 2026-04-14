package com.deepak.training_batch_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deepak.training_batch_management.entity.Batch;
import com.deepak.training_batch_management.entity.SyllabusTopic;
import com.deepak.training_batch_management.repository.BatchRepository;
import com.deepak.training_batch_management.repository.SyllabusRepository;

@RestController
@RequestMapping("/trainer")
public class SyllabusController 
{
		@Autowired
	    private SyllabusRepository syllabusRepository;

	    @Autowired
	    private BatchRepository batchRepository;

	    
	    @PostMapping("/add-topic/{batchId}")
		public ResponseEntity<?> addTopic(@PathVariable Long batchId, @RequestBody SyllabusTopic topic) {

		    Batch batch = batchRepository.findById(batchId)
		            .orElseThrow(() -> new RuntimeException("Batch not found"));

		    boolean exists = syllabusRepository.findByBatchId(batchId)
		    		.stream()
		    		.anyMatch(t -> t.getTopicName().equalsIgnoreCase(topic.getTopicName()));
		    
		    if ("COMPLETED".equalsIgnoreCase(batch.getStatus())) {
		        return ResponseEntity.badRequest().body("Cannot add topic to completed batch ❌");
		    }
		    if(exists)
		    {
		    	return ResponseEntity.badRequest().body("Topic Already Exists ❌");
		    }
		    topic.setBatch(batch);
		    topic.setCompleted(false);
		    
		    syllabusRepository.save(topic);

		    return ResponseEntity.ok("Topic added ✅");
		}
}