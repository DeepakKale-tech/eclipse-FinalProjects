package com.deepak.training_batch_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Entity
@Table(name= "users")
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Name is required")
	private String name;
	
	@Email(message = "Invalid Email")
	@NotBlank(message = "Email is required")
	@Column(unique = true, nullable = false)
	private String email;
	
	@NotBlank(message = "Password is required")
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@NotBlank(message = "Phone is required,must be indian number")
	@Pattern( regexp = "^(\\+91)?[6-9]\\d{9}$")
	@Column(nullable = false, unique = true)
	private String phone;
}
