package com.deepak.training_batch_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deepak.training_batch_management.entity.SyllabusTopic;

public interface SyllabusRepository extends JpaRepository<SyllabusTopic, Long> {
	List<SyllabusTopic> findByBatchId(Long batchId);

}
