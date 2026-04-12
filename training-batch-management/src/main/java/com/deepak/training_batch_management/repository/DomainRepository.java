package com.deepak.training_batch_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deepak.training_batch_management.entity.Domain;
import java.util.List;


public interface DomainRepository extends JpaRepository<Domain, Long> {

	Optional<Domain> findByNameIgnoreCase(String name);
	boolean existsByName(String name);
}
