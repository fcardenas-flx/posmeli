package com.pos.meli.domain.service.impl;

import com.jcraft.jsch.Session;
import com.pos.meli.domain.provider.sftp.SftpConnector;
import com.pos.meli.domain.service.FileService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
public class FileServiceImpl implements FileService
{

	private static String remoteHost = "172.18.0.5";
	private static int port = 22;
	private static String username = "foo";
	private static String password = "pass";

	private static Session session;

	@Autowired
	SftpConnector sftpConnector;

	@Override
	public Workbook getXlsFileFromSftp(String fileName)
	{
		Workbook workbook = sftpConnector.getXlsFile(fileName);

		return workbook;
	}


	private void readXlsFile() throws IOException
	{
	}
}
