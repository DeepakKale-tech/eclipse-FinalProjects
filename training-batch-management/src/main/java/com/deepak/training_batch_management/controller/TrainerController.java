package com.deepak.training_batch_management.controller;

import com.deepak.training_batch_management.repository.ActivityRepository;
import com.deepak.training_batch_management.repository.BatchRepository;
import com.deepak.training_batch_management.repository.NotificationRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deepak.training_batch_management.entity.Activity;
import com.deepak.training_batch_management.entity.Batch;
import com.deepak.training_batch_management.entity.Notification;
import com.deepak.training_batch_management.entity.SyllabusTopic;
import com.deepak.training_batch_management.entity.User;
import com.deepak.training_batch_management.repository.SyllabusRepository;
import com.deepak.training_batch_management.repository.UserRepository;
import com.deepak.training_batch_management.service.ActivityService;
import com.deepak.training_batch_management.service.BatchService;
import com.deepak.training_batch_management.service.NotificationService;

@RestController
@RequestMapping("/trainer")
public class TrainerController {

	private final NotificationRepository notificationRepository;
	private final BatchRepository batchRepository;
	@Autowired
	private SyllabusRepository syllabusRepository;
	@Autowired
	private BatchService batchService;
	@Autowired
	private ActivityService activityService;

	@Autowired
	private NotificationService notificationService;
	@Autowired
	private UserRepository userRepository;
	
	TrainerController(BatchRepository batchRepository, NotificationRepository notificationRepository) {
		this.batchRepository = batchRepository;
		this.notificationRepository = notificationRepository;
	}
	
	
	@PutMapping("/complete-topic/{id}")
	public ResponseEntity<?> completeTopic(@PathVariable Long id)
	{
		SyllabusTopic topic = syllabusRepository.findById(id).orElseThrow();
		
		topic.setCompleted(true);
		topic.setComplitionDate(LocalDate.now());
		
		syllabusRepository.save(topic);
		
		batchService.updateBatchProgress(topic.getBatch().getId());
		
		activityService.logActivity("Completed topic: " + topic.getTopicName(), topic.getBatch().getTrainer());

		notificationService.sendNotification(
		    topic.getBatch().getTrainer(),
		    "Topic completed: " + topic.getTopicName()
		);
		return ResponseEntity.ok("Topic Completed");
	}
	
	
	@GetMapping("/batches")
	public List<Batch> getBatches(Authentication auth) {

		System.out.println("AUTH NAME: " + auth.getName());
	    String email = auth.getName();
	    User trainer = userRepository.findByEmail(email).orElseThrow();

	    return batchRepository.findByTrainer(trainer);
	}
	
	@GetMapping("/notifications")
	public List<Notification> getNotifications(Authentication auth) {

	    String email = auth.getName();
	    User user = userRepository.findByEmail(email).orElseThrow();

	    return notificationRepository.findByUser(user);
	}
	
	@GetMapping("/topics/{batchId}")
	public List<SyllabusTopic> getTopicsByBatch(@PathVariable Long batchId) {
	    return syllabusRepository.findByBatchId(batchId);
	}
	
	@GetMapping("/me")
	public User getLoggedInUser(Authentication auth)
	{
		String email = auth.getName();
		return userRepository.findByEmail(email).orElseThrow();
	}
	
	@PutMapping("/notifications/read/{id}")
	public ResponseEntity<?> markAsRead(@PathVariable Long id) {

	    Notification n = notificationRepository.findById(id).orElseThrow();
	    n.setReadStatus(true);

	    notificationRepository.save(n);

	    return ResponseEntity.ok("Read");
	}
	
	@PostMapping("/assign-student/{batchId}/{studentId}")
	public ResponseEntity<?> assignStudent(@PathVariable Long batchId, @PathVariable Long studentId) {

	    Batch batch = batchRepository.findById(batchId).orElseThrow();
	    User student = userRepository.findById(studentId).orElseThrow();

	    batch.getStudents().add(student);
	    batchRepository.save(batch);

	    return ResponseEntity.ok("Student Assigned");
	}
	
}
