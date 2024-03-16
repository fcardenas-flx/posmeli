package com.pos.meli.domain.provider.sftp.impl;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.pos.meli.domain.provider.sftp.SftpConnector;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.stream.Stream;

@Service
@EnableConfigurationProperties
public class SftpConnectorImpl implements SftpConnector
{
	private static String remoteHost = "172.18.0.5";
	private static int port = 22;
	private static String username = "foo";
	private static String password = "pass";

	private static Session session;

	@Override
	public InputStream getFile(String fileName)
	{
		JSch jsch = new JSch();

		InputStream stream = null;

		try
		{

			ChannelSftp channel = connect();
			Stream<String> contents = getContents(channel, fileName);
			contents.forEach(a-> System.out.println(a));
			//disconnect(channel);

			//disconnect(channel);
		}

		catch (JSchException e)
		{
			e.printStackTrace();
		} catch (SftpException e)
		{
			e.printStackTrace();
		}


		return stream;
	}

	private static Session getSession() throws JSchException
	{
		JSch jsch = new JSch();
		Session session = jsch.getSession(username, remoteHost, port);
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.setPassword(password);
		session.connect();
		return session;
	}

	private static ChannelSftp connect() throws JSchException {
		session = getSession();
		ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
		channel.connect();
		return channel;
	}

	private static void disconnect(ChannelSftp channel) {
		channel.exit();
		session.disconnect();
	}

	private static Stream<String> getContents(ChannelSftp channel, String fileName) throws SftpException {
		InputStream inputStream = channel.get("upload/files/"+ fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		return reader.lines();
	}
}
