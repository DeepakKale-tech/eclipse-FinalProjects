package com.deepak.training_batch_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deepak.training_batch_management.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	boolean existsByEmail(String email);
	
	Optional<User> findByEmail(String email);
	List<User> findByNameContainingIgnoreCaseAndRole(String name, String role);
	
	@Query("SELECT u FROM User u WHERE LOWER(TRIM(u.role)) = 'trainer'")
	List<User> findAllTrainers();
}
