package com.pos.meli.domain.provider.email;

import org.springframework.core.io.InputStreamSource;

import javax.mail.MessagingException;

public interface EmailConnector
{
	void send(String from, String to, String subject, String text);

	void sendWithAttach(String from, String to, String subject,
						String text, String attachName,
						InputStreamSource inputStream) throws MessagingException;
}
