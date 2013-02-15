package com.extia.webscraper.entrypoints;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.extia.webscraper.exception.ScraperException;
import com.extia.webscraper.io.FileIO;
import com.extia.webscraper.io.ScrappingHistoryXmlIO;
import com.extia.webscraper.io.csv.CSVFileIO;
import com.extia.webscraper.io.csv.CSVKeywordReportIO;
import com.extia.webscraper.io.csv.CSVPersonListIO;
import com.extia.webscraper.scraper.ViadeoScraper;
import com.extia.webscraper.system.ScraperSystemFilesFactory;
import com.extia.webscraper.system.ScrappingSettings;
import com.extia.webscraper.system.ViadeoProperties;

public class ViadeoScrapperLauncher {

	static Logger logger = Logger.getLogger(ViadeoScrapperLauncher.class);

	public void launch(String configFilePath) throws Exception{
		ViadeoScraper viadeoScraper = initViadeoScraper(configFilePath);

		viadeoScraper.connectToViadeo();
		viadeoScraper.scrapUnfinishedHistoryDatas();
		viadeoScraper.scrapDatas(viadeoScraper.getKeyWordList());

	}

	public ViadeoScraper initViadeoScraper(String configFilePath) throws ScraperException, IOException{

		ScrappingHistoryXmlIO scrappingHistoryXlml = new ScrappingHistoryXmlIO();
		
		ScrapingSettingReader scrapingSettingReader = new ScrapingSettingReader();
		scrapingSettingReader.setConfigFilePath(configFilePath);
		ScrappingSettings scrappingSettings = scrapingSettingReader.readScrappingSettings();

		if(scrappingSettings.getViadeoLogin() == null || "".equals(scrappingSettings.getViadeoLogin()) 
				|| scrappingSettings.getViadeoPassword() == null || "".equals(scrappingSettings.getViadeoPassword()) ){
			throw new ScraperException("You must provide valid viadeoLogin and viadeoPassword.");
		}
		ScraperSystemFilesFactory systemFilesFactory = new ScraperSystemFilesFactory();
		systemFilesFactory.setScrappingSettings(scrappingSettings);

		scrappingHistoryXlml.setSystemFilesFactory(systemFilesFactory);

		CSVFileIO cSVFileWriterKeywordReport = new CSVFileIO();
		cSVFileWriterKeywordReport.setFile(systemFilesFactory.getKeyWordsReportFile());
		
		CSVKeywordReportIO cSVWriterKeywordReport = new CSVKeywordReportIO();
		cSVWriterKeywordReport.setcSVFileWriter(cSVFileWriterKeywordReport);
		
		
		
		CSVFileIO cSVFileWriterResult = new CSVFileIO();
		cSVFileWriterResult.setFile(systemFilesFactory.getResultFile());
		
		CSVPersonListIO cSVWriterViadeoPersonList = new CSVPersonListIO();
		cSVWriterViadeoPersonList.setcSVFileWriter(cSVFileWriterResult);
		
		FileIO keywordListFileIO = new FileIO();
		keywordListFileIO.setFilePath(scrappingSettings.getKeyWordListFilePath());
		
		ViadeoScraper result = new ViadeoScraper();
		result.setSystemFilesFactory(systemFilesFactory);
		result.setScrappingSettings(scrappingSettings);
		result.setViadeoProperties(new ViadeoProperties());
		result.setScrappingHistoryXml(scrappingHistoryXlml);
		result.setcSVWriterKeywordReport(cSVWriterKeywordReport);
		result.setcSVWriterResult(cSVWriterViadeoPersonList);
		result.setKeywordListFileIO(keywordListFileIO);
		return result;
	}

	public static void main(String[] args) {
		try {
			String configFilePath = null;
			if(args != null){
				if(args.length > 0){
					configFilePath = args[0];
				}
			}

			ViadeoScrapperLauncher launcher = new ViadeoScrapperLauncher();
			launcher.launch(configFilePath);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}