package com.teillet.parcelle.service;

import java.io.IOException;

public interface ITemporaryFileService {
	String saveTemporaryFile(byte[] fileBytes, String fileName) throws IOException;
}
