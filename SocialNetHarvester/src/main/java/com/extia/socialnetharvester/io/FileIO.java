package com.extia.socialnetharvester.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileIO {
	
	private String filePath;
	
	private String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<String> read() throws IOException {
		return getFilePath() != null ? Files.readAllLines(Paths.get(getFilePath()), StandardCharsets.UTF_8) : null;
	}

}
