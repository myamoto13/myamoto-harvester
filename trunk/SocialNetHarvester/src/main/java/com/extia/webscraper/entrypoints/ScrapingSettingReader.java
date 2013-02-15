package com.extia.webscraper.entrypoints;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.extia.webscraper.exception.ScraperException;
import com.extia.webscraper.system.ScrappingSettings;

public class ScrapingSettingReader {
	static Logger logger = Logger.getLogger(GUIViadeoScrapperLauncher.class);
	private String configFilePath;
	
	private String getConfigFilePath() {
		return configFilePath;
	}

	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}

	public ScrappingSettings readScrappingSettings() throws ScraperException {
		ScrappingSettings result = null;
		String configFilePath = getConfigFilePath();
		if(configFilePath != null){
			result = new ScrappingSettings();
			result.setHttpCallsDelay(2000);
			result.setTimeout(20000);
			result.setWorkingDirPath(System.getProperty("user.home"));
			result.setUserAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
			
			Properties prop = new Properties();
	    	try {
	    		prop.load(new FileInputStream(configFilePath));
	    		
	    		String keyWordListFilePath = prop.getProperty("keyWordListFilePath");
	    		if(keyWordListFilePath != null){
	    			result.setKeyWordListFilePath(keyWordListFilePath);
	    		}else{
	    			logger.error("keyWordListFilePath not found, you must specify one.");
	    		}
	    		
	    		String workindDirPath = prop.getProperty("workingDirPath");
	    		if(workindDirPath != null){
	    			result.setWorkingDirPath(workindDirPath);
	    		}else{
	    			logger.warn("workindDirPath not found.");
		    		logger.warn("going with default : " + result.getWorkingDirPath());
	    		}
	    		
	    		String viadeoLogin = prop.getProperty("viadeoLogin");
	    		if(viadeoLogin != null){
	    			result.setViadeoLogin(viadeoLogin);
	    		}else{
	    			logger.error("viadeoLogin not found, you must specify one.");
	    		}
	    		
	    		String viadeoPassword = prop.getProperty("viadeoPassword");
	    		if(viadeoPassword != null){
	    			result.setViadeoPassword(viadeoPassword);
	    		}else{
	    			logger.error("viadeoPassword not found, you must specify one.");
	    		}
	    		
	    		String userAgent = prop.getProperty("userAgent");
	    		if(userAgent != null){
	    			result.setUserAgent(userAgent);
	    		}else{
	    			logger.warn("userAgent not found.");
		    		logger.warn("going with default : " + result.getUserAgent());
	    		}
	    		
	    		try{ 
	    			result.setTimeout(Integer.parseInt(prop.getProperty("timeout")));
	    		}catch(NumberFormatException ex){
	    			logger.warn("Could not parse timeout, got error : " + ex.getMessage());
		    		logger.warn("going with default (in seconds) : " + result.getTimeout());
	    		}
	    		
	    		try{ 
	    			result.setHttpCallsDelay(Integer.parseInt(prop.getProperty("httpCallsDelay")));
	    		}catch(NumberFormatException ex){
	    			logger.warn("Could not parse httpCallsDelay, got error : " + ex.getMessage());
		    		logger.warn("going with default (in seconds) : " + result.getHttpCallsDelay());
	    		}
	    		
	    		String autoResumeScraping = prop.getProperty("autoResumeScraping");
	    		if(autoResumeScraping != null){
	    			result.setAutoResumeScraping(Boolean.parseBoolean(autoResumeScraping));
	    		}else{
	    			logger.warn("autoResumeSearch not found.");
		    		logger.warn("going with default : " + result.isAutoResumeScraping());
	    		}
	    		
	    	} catch (IOException ex) {
	    		logger.error("Could not retrieve config file, got error : " + ex.getMessage());
	        }
			
		}else{
			throw new ScraperException("Scraping config file path must be specified in the first argument.");
		}
		return result;
	}
	
	private void writeScrappingSettings() throws ScraperException {
		
	}
}
