package com.deepak.training_batch_management.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.deepak.training_batch_management.entity.Notification;
import com.deepak.training_batch_management.entity.User;
import com.deepak.training_batch_management.repository.NotificationRepository;

@Service
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private EmailService emailService;
	@Autowired
	private WhatsAppService whatsAppService;
	
	 public void sendNotification(User user, String message) {

	        Notification n = new Notification();
	        n.setUser(user);
	        n.setMessage(message);
	        n.setTimestamp(LocalDateTime.now());
	        n.setReadStatus(false);

	        notificationRepository.save(n);
	        
	        //email
	        emailService.sendEmail(user.getEmail(), 
	        		"Batch Training Notification", message);
	        
	        //whatsapp
	        whatsAppService.sendWhatsApp(user.getPhone(), message);
	        
	        messagingTemplate.convertAndSend("/topic/notifications/"+user.getId() , n);
	    }
}
