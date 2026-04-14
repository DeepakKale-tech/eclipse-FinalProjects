package com.deepak.training_batch_management.controller;

import com.deepak.training_batch_management.repository.BatchRepository;
import com.deepak.training_batch_management.repository.DomainRepository;
import com.deepak.training_batch_management.repository.SyllabusRepository;
import com.deepak.training_batch_management.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deepak.training_batch_management.entity.Batch;
import com.deepak.training_batch_management.entity.Domain;
import com.deepak.training_batch_management.entity.Role;
import com.deepak.training_batch_management.entity.SyllabusTopic;
import com.deepak.training_batch_management.entity.User;
import com.deepak.training_batch_management.service.BatchService;
import com.deepak.training_batch_management.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {
	private final UserRepository userRepository;
	
	@Autowired
	private DomainRepository domainRepository; 
	@Autowired
	private UserService userService;
	
	@Autowired
	private BatchService batchService;

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
	public ResponseEntity<?> createUser(@Valid @RequestBody User user)
	{
		try
		{
			return ResponseEntity.ok(userService.createUser(user));
		}catch(Exception e)
		{
	        return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@GetMapping("/users")
	public List<User> getAllUsers()
	{
		return userRepository.findAll();
	}
	
	@PostMapping("/batch/{trainerId}")
	public ResponseEntity<?> createBatch(@PathVariable Long trainerId, @RequestBody Batch batch) {
	    return ResponseEntity.ok(batchService.createBatch(trainerId, batch));
	}
	
	@GetMapping("/batches")
	public List<Batch> getAllBatches() {
	    return batchService.findAll();
	}
	
	@PutMapping("/user/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updateUser) {
		
		User user = userRepository.findById(id).orElseThrow();
		
		user.setName(updateUser.getName());
		user.setEmail(updateUser.getEmail());
		
		if(updateUser.getPassword() != null && !updateUser.getPassword().isEmpty())
		{
			user.setPassword(updateUser.getPassword());
		}
		
		userRepository.save(user);
	    return ResponseEntity.ok("Updated");
	}
	
	@DeleteMapping("/user/{id}")
	public void deleteUser(@PathVariable Long id) {
	    userService.delete(id);
	}
	
	
	
	@GetMapping("/trainer/search")
	public List<User> searchTrainer(@RequestParam String name) {
	    return userService.findTrainerByName(name);
	}
	
	@GetMapping("/batch/search")
	public List<Batch> searchByDomain(@RequestParam String domain) {
	    return batchService.findByDomain(domain);
	}
	
	@GetMapping("/batches/completed")
	public List<Batch> completedBatches() {
	    return batchService.findByStatus("COMPLETED");
	}
	
	@DeleteMapping("/batch/{id}")
	public void deleteBatch(@PathVariable Long id) {
	    batchRepository.deleteById(id);
	}

	@PutMapping("/batch/update/{id}/{trainerId}")
	public Batch updateBatch(@PathVariable Long id, @PathVariable Long trainerId, @RequestBody Batch batch) {
	    Batch existing = batchRepository.findById(id).orElseThrow();

	    User trainer = userRepository.findById(trainerId)
	            .orElseThrow(() -> new RuntimeException("Trainer not found"));
	    
	    
	    existing.setBatchName(batch.getBatchName());
	    existing.setDomain(batch.getDomain());
	    existing.setTiming(batch.getTiming());
	    existing.setStartDate(batch.getStartDate());
	    existing.setEndDate(batch.getEndDate());
	    existing.setStatus(batch.getStatus());
	    existing.setTrainer(trainer);

	    return batchRepository.save(existing);
	}
	
	@GetMapping("/batch/{id}")
	public ResponseEntity<?> getBatchById(@PathVariable Long id) {

	    Batch batch = batchRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Batch not found"));

	    return ResponseEntity.ok(batch);
	}
	@GetMapping("/trainers")
	public List<User> getAllTrainers() {
	    return userRepository.findAll().stream()
	    		.filter(u -> u.getRole() == Role.TRAINER)
	    		.toList();
	}
	
	@PostMapping("/domain")
	public ResponseEntity<?> addDomain(@RequestBody Domain domain) {
		
		String name = domain.getName().toLowerCase().trim();
		domain.setName(domain.getName().toLowerCase().trim());
		
		if (name.isEmpty()) {
	        return ResponseEntity.badRequest().body("Domain required");
	    }

	    if (domainRepository.existsByName(name)) {
	        return ResponseEntity.badRequest().body("Domain already exists");
	    }
	    
	    domain.setName(name);
	    domainRepository.save(domain);

	    return ResponseEntity.ok("Domain added");
	}

	@GetMapping("/domains")
	public List<Domain> getDomains() {
	    return domainRepository.findAll();
	}
	
	@GetMapping("/user/{id}")
	public User getUserById(@PathVariable Long id) {
	    return userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	}
	
}
