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
	    public ResponseEntity<?> addTopic(@PathVariable Long batchId, @RequestBody SyllabusTopic topic) 
	    {

	        Batch batch = batchRepository.findById(batchId).orElseThrow();

	        topic.setBatch(batch);

	        return ResponseEntity.ok(syllabusRepository.save(topic));
	    }
}