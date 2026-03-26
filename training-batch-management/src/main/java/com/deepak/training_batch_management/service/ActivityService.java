package com.deepak.training_batch_management.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deepak.training_batch_management.entity.Activity;
import com.deepak.training_batch_management.entity.User;
import com.deepak.training_batch_management.repository.ActivityRepository;

@Service
public class ActivityService {

	@Autowired
	private ActivityRepository activityRepository;
	
	 public void logActivity(String message, User user) {

	        Activity activity = new Activity();
	        activity.setMessage(message);
	        activity.setTimestamp(LocalDateTime.now());
	        activity.setUser(user);

	        activityRepository.save(activity);
	    }
}
