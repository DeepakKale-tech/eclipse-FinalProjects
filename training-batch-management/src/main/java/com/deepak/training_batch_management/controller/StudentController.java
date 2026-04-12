package com.deepak.training_batch_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deepak.training_batch_management.entity.Batch;
import com.deepak.training_batch_management.entity.Notification;
import com.deepak.training_batch_management.entity.SyllabusTopic;
import com.deepak.training_batch_management.entity.User;
import com.deepak.training_batch_management.repository.BatchRepository;
import com.deepak.training_batch_management.repository.NotificationRepository;
import com.deepak.training_batch_management.repository.SyllabusRepository;
import com.deepak.training_batch_management.repository.UserRepository;

@RestController
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BatchRepository batchRepository;
	
	@Autowired
	private SyllabusRepository syllabusRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@GetMapping("/batches")
	public List<Batch> getStudentBatches(Authentication auth) {

		String email = auth.getName();
		User student = userRepository.findByEmail(email).orElseThrow();

		return batchRepository.findByStudentsContains(student);
	}

	@GetMapping("/notifications")
	public List<Notification> getNotifications(Authentication auth) {

		String email = auth.getName();
		User user = userRepository.findByEmail(email).orElseThrow();

		return notificationRepository.findByUser(user);
	}
	
	@GetMapping("/topics/{batchId}")
	public List<SyllabusTopic> getTopicsForStudent(@PathVariable Long batchId) {
	    return syllabusRepository.findByBatchId(batchId);
	}
	
	@PutMapping("/notifications/read/{id}")
	public ResponseEntity<?> markAsRead(@PathVariable Long id) {

	    Notification n = notificationRepository.findById(id).orElseThrow();
	    n.setReadStatus(true);

	    notificationRepository.save(n);

	    return ResponseEntity.ok("Read");
	}
}
