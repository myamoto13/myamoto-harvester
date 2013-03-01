package com.extia.socialnetharvester.io.csv;

import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class CSVKeywordReportIO {
	
	private CSVFileIO cSVFileWriter;
	
	private CSVFileIO getcSVFileWriter() {
		return cSVFileWriter;
	}

	public void setcSVFileWriter(CSVFileIO cSVFileWriter) {
		this.cSVFileWriter = cSVFileWriter;
	}

	public void writeTitle() throws IOException{
		getcSVFileWriter().write(Arrays.asList(new String[]{"keyWords", "nbResultsRetrieved", "nbResults"}), StandardOpenOption.WRITE);
	}

	public void writeLine(String keyWords, String nbResultsRetrieved, String nbResults) throws IOException{
		getcSVFileWriter().write(Arrays.asList(new String[]{keyWords, nbResultsRetrieved, nbResults}), StandardOpenOption.APPEND);
	}
}
