package com.deepak.training_batch_management.controller;

import com.deepak.training_batch_management.repository.BatchRepository;
import com.deepak.training_batch_management.repository.DomainRepository;
import com.deepak.training_batch_management.repository.NotificationRepository;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deepak.training_batch_management.entity.Activity;
import com.deepak.training_batch_management.entity.Batch;
import com.deepak.training_batch_management.entity.Domain;
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
	private DomainRepository domainRepository;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private UserRepository userRepository;
	
	TrainerController(BatchRepository batchRepository, NotificationRepository notificationRepository) {
		this.batchRepository = batchRepository;
		this.notificationRepository = notificationRepository;
	}
	
	
	 @PutMapping("/complete-topic/{id}")
	    public ResponseEntity<?> completeTopic(@PathVariable Long id) {

	        SyllabusTopic topic = syllabusRepository.findById(id).orElseThrow();

	        if(topic.isCompleted())
	        {
	        	return ResponseEntity.badRequest().body("Topic Already Completed ⚠️");
	        }
	        
	        
	        topic.setCompleted(true);
	        topic.setComplitionDate(LocalDate.now());

	        syllabusRepository.save(topic);

	        Batch batch = topic.getBatch();

	        batchService.updateBatchProgress(batch.getId());

	        activityService.logActivity(
	                "Completed topic: " + topic.getTopicName(),
	                batch.getTrainer()
	        );

	        // ✅ Trainer Notification
	        notificationService.sendNotification(
	                batch.getTrainer(),
	                "Topic completed: " + topic.getTopicName()
	        );

	        // 🔥 NEW: Student Notifications
	        for (User student : batch.getStudents()) {
	            notificationService.sendNotification(
	                    student,
	                    "New topic completed in " + batch.getBatchName() + ": " + topic.getTopicName()
	            );
	        }

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
	
	@GetMapping("/students")
	public List<User> getStudentsForTrainer() {
	    return userRepository.findAll()
	            .stream()
	            .filter(u -> u.getRole().name().equals("STUDENT"))
	            .toList();
	}
	
	@GetMapping("/trainer/test")
	public String test() {
	    return "Trainer API Working";
	}
	
	@PutMapping("/batch/complete/{id}")
	public ResponseEntity<?> completeBatch(@PathVariable Long id) {

	    Batch batch = batchRepository.findById(id).orElseThrow();

	    batch.setStatus("COMPLETED");
	    batch.setProgressPercentage(100);

	    batchRepository.save(batch);

	    return ResponseEntity.ok("Batch completed");
	}
	
	@GetMapping("/domains")
	public List<Domain> getDomains() {
	    return domainRepository.findAll();
	}
	
	
}
