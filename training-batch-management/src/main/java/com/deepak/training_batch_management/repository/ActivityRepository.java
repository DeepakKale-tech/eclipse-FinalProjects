package com.deepak.training_batch_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deepak.training_batch_management.entity.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

}
