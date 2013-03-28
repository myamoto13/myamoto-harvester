package com.extia.socialnetharvester.io.csv;

import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import com.extia.socialnetharvester.data.ViadeoPerson;

public class CSVPersonListIO {

	@Resource(name="cSVFileWriterResult")
	private CSVFileIO cSVFileIO;

	private CSVFileIO getcSVFileIO() {
		return cSVFileIO;
	}

	public void setcSVFileIO(CSVFileIO cSVFileIO) {
		this.cSVFileIO = cSVFileIO;
	}

	public void writeLine(ViadeoPerson person) throws IOException{
		if(person != null){
			getcSVFileIO().write(Arrays.asList(new String[]{person.getKeywords(), person.getCountry(), person.getCity(), person.getName(), person.getJob(), person.getCompany(), person.getPreviousJob(), person.getPreviousCompany(), person.getOverview(), person.getProfileLink()}), StandardOpenOption.APPEND);
		}
	}

	public void writeTitle() throws IOException{
		getcSVFileIO().write(Arrays.asList(new String[]{"keywords", "country", "city", "name", "job", "company", "previousJob", "previousCompany", "overview", "profileLink"}), StandardOpenOption.WRITE);
	}

	public List<ViadeoPerson> read() throws IOException{
		List<ViadeoPerson> result = null;
		List<List<String>> csvLineList = getcSVFileIO().read();
		if(csvLineList != null){
			result = new ArrayList<ViadeoPerson>();
			for (List<String> csvLine : csvLineList) {
				if(csvLine.size() >= 10){
					ViadeoPerson person = new ViadeoPerson();
					person.setKeywords(csvLine.get(0));
					person.setCountry(csvLine.get(1));
					person.setCity(csvLine.get(2));
					person.setName(csvLine.get(3));
					person.setJob(csvLine.get(4));
					person.setCompany(csvLine.get(5));
					person.setPreviousJob(csvLine.get(6));
					person.setPreviousCompany(csvLine.get(7));
					person.setOverview(csvLine.get(8));
					person.setProfileLink(csvLine.get(9));
					result.add(person);
				}
			}
		}
		return result;
	}
	
	public boolean isEmptyFile(){
		return getcSVFileIO().isEmptyFile();
	}


}
