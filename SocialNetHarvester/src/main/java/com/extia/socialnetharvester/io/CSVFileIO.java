package com.extia.socialnetharvester.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.helper.StringUtil;

public class CSVFileIO {

	private File file;

	private static final String SEPARATION_CHAR = ";";

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public void write(List<String> stringList, StandardOpenOption openOption) throws IOException{
		if(stringList != null){
			BufferedWriter keyWordReportFW = null;
			try{
				keyWordReportFW = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8, new OpenOption[] {openOption});
				keyWordReportFW.append(StringUtil.join(stringList, SEPARATION_CHAR));
				keyWordReportFW.append(System.lineSeparator());
				keyWordReportFW.close();
			}finally{
				if(keyWordReportFW != null){
					keyWordReportFW.close();
				}
			}
		}
	}
	
	public List<List<String>> read() throws IOException{
		List<List<String>> result = null;
		List<String> lineList = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
		if(lineList != null){
			result = new ArrayList<List<String>>();
			for (String line : lineList) {
				String[] valueList = line.split(SEPARATION_CHAR);
				if(valueList != null){
					result.add(Arrays.asList(valueList));
				}
			}
		}		
		return result;
	}

	public boolean isEmptyFile() {
		return getFile() != null && getFile().length() == 0;
	}
	
}
