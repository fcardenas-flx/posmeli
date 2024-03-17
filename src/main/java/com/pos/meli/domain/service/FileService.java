package com.pos.meli.domain.service;

import org.apache.poi.ss.usermodel.Workbook;

public interface FileService
{

	Workbook getXlsFileFromSftp(String fileName);
}
