package com.extia.webscraper.io.csv;

import java.io.IOException;
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
		getcSVFileWriter().write(Arrays.asList(new String[]{"keyWords", "nbResultsRetrieved", "nbResults"}));
	}

	public void writeLine(String keyWords, String nbResultsRetrieved, String nbResults) throws IOException{
		getcSVFileWriter().write(Arrays.asList(new String[]{keyWords, nbResultsRetrieved, nbResults}));
	}
}
