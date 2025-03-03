package com.pos.meli.domain.provider.email.Impl;

import com.pos.meli.domain.provider.email.EmailConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailConnectorImpl implements EmailConnector
{
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void send(String from, String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		mailSender.send(message);
	}

	@Override
	public void send(String from, String to, String subject, String text, String body) throws MessagingException
	{

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text);

		helper.setText(body, true); // HTML

		mailSender.send(message);
	}

	@Override
	public void sendWithAttach(String from, String to, String subject,
							   String text, String attachName,
							   InputStreamSource inputStream) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text, true);
		helper.addAttachment(attachName, inputStream);
		mailSender.send(message);
	}
}
