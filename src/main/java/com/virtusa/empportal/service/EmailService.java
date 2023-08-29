package com.virtusa.empportal.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.virtusa.empportal.utils.Response;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;

	public Response sendSimpleMessage(String to, String subject, String text) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("chinmay2742@gmail.com");
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);
	
			emailSender.send(message);
			return new Response(LocalDateTime.now(), HttpStatus.OK, "Mail sent", null);
		}
		catch (Exception e) {
			return new Response(LocalDateTime.now(), HttpStatus.OK, "Error", e);
		}

	}
}
