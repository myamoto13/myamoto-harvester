package com.extia.socialnetharvester.business;

import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import javax.annotation.Resource;

import com.extia.socialnetharvester.io.CSVFileIO;

public class CSVKeywordReportIO {
	
	@Resource(name="cSVFileWriterKeywordReport")
	private CSVFileIO cSVFileIO;
	
	private CSVFileIO getcSVFileIO() {
		return cSVFileIO;
	}

	public void setcSVFileIO(CSVFileIO cSVFileWriter) {
		this.cSVFileIO = cSVFileWriter;
	}

	public void writeTitle() throws IOException{
		getcSVFileIO().write(Arrays.asList(new String[]{"keyWords", "nbResultsRetrieved", "nbResults"}), StandardOpenOption.WRITE);
	}

	public void writeLine(String keyWords, String nbResultsRetrieved, String nbResults) throws IOException{
		getcSVFileIO().write(Arrays.asList(new String[]{keyWords, nbResultsRetrieved, nbResults}), StandardOpenOption.APPEND);
	}
}
