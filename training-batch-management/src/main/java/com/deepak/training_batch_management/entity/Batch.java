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
public class Batch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String batchName;
	private LocalDate startDate;
	private LocalDate endDate;
	private String status;
	
	private double progressPercentage;
	
	@ManyToOne
	@JoinColumn(name = "trainer_id")
	private User trainer;
}
