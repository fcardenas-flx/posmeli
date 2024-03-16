package com.pos.meli.domain.provider.sftp;

import java.io.InputStream;

public interface SftpConnector
{

	InputStream getFile(String fileName);
}
