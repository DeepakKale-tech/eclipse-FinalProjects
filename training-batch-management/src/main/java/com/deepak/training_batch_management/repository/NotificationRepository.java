package com.deepak.training_batch_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deepak.training_batch_management.entity.Notification;
import com.deepak.training_batch_management.entity.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByUser(User user);
}
