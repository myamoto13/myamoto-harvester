package com.extia.webscraper.viadeo.entrypoints;
import org.apache.log4j.Logger;

import com.extia.webscraper.exception.ScrapperException;
import com.extia.webscraper.io.ScrappingHistoryXmlIO;
import com.extia.webscraper.viadeo.ViadeoScraper;
import com.extia.webscraper.viadeo.system.ScraperSystemFilesFactory;
import com.extia.webscraper.viadeo.system.ScrappingSettings;
import com.extia.webscraper.viadeo.system.ViadeoProperties;

public class ViadeoScrapperLauncher {

	static Logger logger = Logger.getLogger(ViadeoScrapperLauncher.class);

	public void launch(String configFilePath) throws Exception{
		ViadeoScraper viadeoScraper = initViadeoScraper(configFilePath);

		viadeoScraper.connectToViadeo();
		viadeoScraper.scrapUnfinishedHistoryDatas();
		viadeoScraper.scrapDatas(viadeoScraper.getKeyWordList());

	}

	public ViadeoScraper initViadeoScraper(String configFilePath) throws ScrapperException{

		ScrappingHistoryXmlIO scrappingHistoryXlml = new ScrappingHistoryXmlIO();
		
		ScrapingSettingReader scrapingSettingReader = new ScrapingSettingReader();
		scrapingSettingReader.setConfigFilePath(configFilePath);
		ScrappingSettings scrappingSettings = scrapingSettingReader.readScrappingSettings();

		if(scrappingSettings.getViadeoLogin() == null || "".equals(scrappingSettings.getViadeoLogin()) 
				|| scrappingSettings.getViadeoPassword() == null || "".equals(scrappingSettings.getViadeoPassword()) ){
			throw new ScrapperException("You must provide valid viadeoLogin and viadeoPassword.");
		}
		ScraperSystemFilesFactory systemFilesFactory = new ScraperSystemFilesFactory();
		systemFilesFactory.setScrappingSettings(scrappingSettings);

		scrappingHistoryXlml.setSystemFilesFactory(systemFilesFactory);

		ViadeoScraper result = new ViadeoScraper();
		result.setSystemFilesFactory(systemFilesFactory);
		result.setScrappingSettings(scrappingSettings);
		result.setViadeoProperties(new ViadeoProperties());
		result.setScrappingHistoryXml(scrappingHistoryXlml);

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