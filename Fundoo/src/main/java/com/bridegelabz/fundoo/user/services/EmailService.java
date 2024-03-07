package com.bridegelabz.fundoo.user.services;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bridegelabz.fundoo.user.model.Email;
import com.bridegelabz.fundoo.util.UserToken;

@Service
public class EmailService 
{
	@Autowired
	JavaMailSender emailSender;
	public EmailService() 
	{
		
	}
	@RabbitListener(queues = "queue")
	public void sendMail(Email email) 
	{
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(email.getTo());
		simpleMailMessage.setFrom(email.getFrom());
		simpleMailMessage.setSubject(email.getSubject());
		simpleMailMessage.setText(email.getBody());
		emailSender.send(simpleMailMessage);
	}
	
}
