package com.bridegelabz.fundoo.util;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

//import com.bridegelabz.fundoo.elasticsearch.ElasticSearchImpl;
import com.bridegelabz.fundoo.notes.model.Notes;
import com.bridegelabz.fundoo.user.model.Email;
import com.bridegelabz.fundoo.user.services.EmailService;
@Component
public class GenerateMail 
{
	@Autowired
	private AmqpTemplate rabbitTemplate;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	EmailService emailService;
	@Autowired
	private com.bridegelabz.fundoo.elasticsearch.ElasticSearchImpl elasticSearch;
	private String elasticRountingKey = "elasticRountingKey";
	private String exchange = "exchange";
	private String routingkey = "Jerry";
	
	@RabbitListener(queues = "queue")
	public void send(Email email) {
		
//		SimpleMailMessage message = new SimpleMailMessage(); 
//	    message.setTo(email.getTo()); 
//	    message.setSubject(email.getSubject()); 
//	    message.setText(email.getBody());
//	    
//	    System.out.println("Sending Email ");
//	    
//	    javaMailSender.send(message);
		emailService.sendMail(email);

	    System.out.println("Email Sent Successfully!!");

	}
//	public void send(Email email) 
//	{
//		rabbitTemplate.convertAndSend(exchange , routingkey , email);
//	}
	public void sendNote(NoteContainer noteContainer) {
		rabbitTemplate.convertAndSend(exchange,elasticRountingKey, noteContainer);
	}
	@RabbitListener(queues = "elasticQueue")
	public void operation(NoteContainer notecontainer) {
		System.out.println("operation");
		Notes note=notecontainer.getNote();
		switch(notecontainer.getNoteOperation()) {
		
		case CREATE:
			elasticSearch.create(note);
			break;
			
		case UPDATE :
			elasticSearch.updateNote(note);
			break;

		case DELETE :
			elasticSearch.deleteNote(note.getId());
			break;
		}
	}
}	
