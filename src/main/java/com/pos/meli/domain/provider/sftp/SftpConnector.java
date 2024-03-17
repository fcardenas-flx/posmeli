package com.pos.meli.domain.provider.sftp;

import org.apache.poi.ss.usermodel.Workbook;

public interface SftpConnector
{

	Workbook getXlsFile(String fileName);
}
