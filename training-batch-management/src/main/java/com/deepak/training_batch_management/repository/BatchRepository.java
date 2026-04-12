package com.deepak.training_batch_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deepak.training_batch_management.entity.Batch;
import com.deepak.training_batch_management.entity.User;

public interface BatchRepository extends JpaRepository<Batch, Long> {

	List<Batch> findByTrainer(User trainer);
	
	List<Batch> findByStudentsContains(User student);
	
	List<Batch> findByDomainContainingIgnoreCase(String domain);

	List<Batch> findByStatus(String status);
}
