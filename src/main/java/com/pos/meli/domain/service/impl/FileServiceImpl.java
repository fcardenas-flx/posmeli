package com.pos.meli.domain.service.impl;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.pos.meli.domain.provider.meli.MeliConnector;
import com.pos.meli.domain.provider.sftp.SftpConnector;
import com.pos.meli.domain.service.FileService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Properties;

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
	public InputStream getFileFromSftp(String fileName)
	{
		InputStream file = sftpConnector.getFile(fileName);

		return file;
	}


	private void readXlsFile() throws IOException
	{
	}
}
