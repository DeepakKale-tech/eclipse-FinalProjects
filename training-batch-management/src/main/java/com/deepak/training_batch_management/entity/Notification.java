package com.deepak.training_batch_management.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String message;
	private LocalDateTime timestamp;
	
	private boolean readStatus;
	
	//@ManyToOne
	//@JoinColumn(name = "batch_id")
	//private Batch batch;
	
	@ManyToOne
	private User user;
}
