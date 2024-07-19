package com.morning.mem.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
//
//    public void sendCurrentPasswordEmail(String email, String currentPassword) {
//        String subject = "密碼查詢";
//        String body = "您的當前密碼是: " + currentPassword;
//        sendEmail(email, subject, body);
//    }
//
//    private void sendEmail(String to, String subject, String body) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(body);
//        mailSender.send(message);
//    }
	
	

	    public void sendResetPasswordEmail(String email, String resetLink) {
	        String subject = "重置密碼";
	        String body = "請點擊以下鏈接重置您的密碼: " + resetLink;
	        sendEmail(email, subject, body);
	    }

	    private void sendEmail(String to, String subject, String body) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setFrom("morningcodes@gmail.com");
	        message.setTo(to);
	        message.setSubject(subject);
	        message.setText(body);
	        mailSender.send(message);
	    }
}