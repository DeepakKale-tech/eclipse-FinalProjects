package com.deepak.training_batch_management.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class SyllabusTopic {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String topicName;
	private String description;
	private boolean isCompleted;
	private LocalDate complitionDate;
	
	@ManyToOne
	@JoinColumn(name="batch_id")
	private Batch batch;
}
