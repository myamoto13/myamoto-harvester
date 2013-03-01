package com.extia.socialnetharvester.io.csv;

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

import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;

public class CSVFileIO {
	static Logger logger = Logger.getLogger(CSVFileIO.class);

	private File file;

	private final String separationCharacter = ";";

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
				keyWordReportFW.append(StringUtil.join(stringList, separationCharacter));
				keyWordReportFW.append(System.lineSeparator());
				keyWordReportFW.close();
			}catch(IOException ex){
				throw ex;
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
				String[] valueList = line.split(separationCharacter);
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
