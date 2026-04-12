package com.deepak.training_batch_management.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

	//private String domain;
	
	private String timing;
	private double progressPercentage;
	
	@ManyToOne
	@JoinColumn(name = "domain_id")
	private Domain domain;
	
	@ManyToOne
	@JoinColumn(name = "trainer_id")
	private User trainer;
	
	@ManyToMany
	@JoinTable(
	    name = "batch_students",
	    joinColumns = @JoinColumn(name = "batch_id"),
	    inverseJoinColumns = @JoinColumn(name = "student_id")
	)
	private List<User> students;
}
