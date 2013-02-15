package com.extia.webscraper.io.csv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.extia.webscraper.data.ViadeoPerson;

public class CSVPersonListIO {

	private CSVFileIO cSVFileWriter;

	private CSVFileIO getcSVFileWriter() {
		return cSVFileWriter;
	}

	public void setcSVFileWriter(CSVFileIO cSVFileWriter) {
		this.cSVFileWriter = cSVFileWriter;
	}

	public void writeLine(ViadeoPerson person) throws IOException{
		if(person != null){
			getcSVFileWriter().write(Arrays.asList(new String[]{person.getKeywords(), person.getCountry(), person.getCity(), person.getName(), person.getJob(), person.getCompany(), person.getPreviousJob(), person.getPreviousCompany(), person.getOverview(), person.getProfileLink()}));
		}
	}

	public void writeTitle() throws IOException{
		getcSVFileWriter().write(Arrays.asList(new String[]{"keywords", "country", "city", "name", "job", "company", "previousJob", "previousCompany", "overview", "profileLink"}));
	}

	public List<ViadeoPerson> read() throws IOException{
		List<ViadeoPerson> result = null;
		List<List<String>> csvLineList = getcSVFileWriter().read();
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
		return getcSVFileWriter().isEmptyFile();
	}


}
