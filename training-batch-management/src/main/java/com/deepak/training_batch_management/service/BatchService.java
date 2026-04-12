package com.deepak.training_batch_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deepak.training_batch_management.entity.Batch;
import com.deepak.training_batch_management.entity.SyllabusTopic;
import com.deepak.training_batch_management.entity.User;
import com.deepak.training_batch_management.repository.BatchRepository;
import com.deepak.training_batch_management.repository.SyllabusRepository;
import com.deepak.training_batch_management.repository.UserRepository;

@Service
public class BatchService {

	@Autowired
	private SyllabusRepository syllabusRepository;
	@Autowired
	private BatchRepository batchRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ActivityService activityService;

	@Autowired
	private NotificationService notificationService;
	
	public double calculateProgress(Long batchId)
	{
		List<SyllabusTopic> topics = syllabusRepository.findByBatchId(batchId);
		
		long total = topics.size();
		
		long completed = topics.stream().filter(SyllabusTopic::isCompleted).count();
		
		if(total == 0) return 0;
		
		return (completed * 100.00) / total;
	}
	
	public void updateBatchProgress(Long batchId)
	{
		double progress = calculateProgress(batchId);
		
		Batch batch = batchRepository.findById(batchId).orElseThrow();
		
		batch.setProgressPercentage(progress);
		
		batchRepository.save(batch);
	}
	public Batch createBatch(Long trainerId, Batch batch) {

	    User trainer = userRepository.findById(trainerId)
	            .orElseThrow(() -> new RuntimeException("Trainer not found"));

	    batch.setTrainer(trainer);
	    batch.setProgressPercentage(0);
	    Batch saved = batchRepository.save(batch);
	    
	    activityService.logActivity("Created batch: " + batch.getBatchName(), trainer);

	    notificationService.sendNotification(trainer, "Batch created: " + batch.getBatchName());

	    return saved;
	}
	
	public Batch save(Batch batch) {
	    return batchRepository.save(batch);
	}

	public List<Batch> findAll() {
	    return batchRepository.findAll();
	}

	public List<Batch> findByDomain(String domain) {
	    return batchRepository.findByDomainContainingIgnoreCase(domain);
	}

	public List<Batch> findByStatus(String status) {
	    return batchRepository.findByStatus(status);
	}
}
