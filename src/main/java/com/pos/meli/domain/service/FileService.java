package com.pos.meli.domain.service;

import java.io.InputStream;

public interface FileService
{

	InputStream getFileFromSftp(String fileName);
}
