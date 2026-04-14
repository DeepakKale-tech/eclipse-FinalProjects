package com.deepak.training_batch_management.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class WhatsAppService {

	 @Value("${twilio.account.sid}")
	 public String ACCOUNT_SID ;
	 @Value("${twilio.auth.token}")
	 public String AUTH_TOKEN ;
	 
	 public void sendWhatsApp(String to, String message) {

	        try
	        {
	        	Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

	        	//"HXb5b62575e6e4ff6129ad7c8efe1f983e", "{'1':'12/1','2':'3pm'}"
		        Message.creator(
		                new PhoneNumber("whatsapp:" + to),
		                new PhoneNumber("whatsapp:+14155238886"),// Twilio sandbox
		                message
		        ).create();
		        System.out.println("WhatsApp sent ✅");
	        }catch(Exception e)
	        {
	        	System.out.println("WhatsApp ERROR ❌: " + e.getMessage());
	        }
	    }
}
