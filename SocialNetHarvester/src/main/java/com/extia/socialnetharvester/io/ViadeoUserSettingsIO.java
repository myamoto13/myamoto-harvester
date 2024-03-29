package com.extia.socialnetharvester.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.extia.socialnetharvester.business.ScraperException;
import com.extia.socialnetharvester.business.ViadeoUserSettings;

public class ViadeoUserSettingsIO {
	
	private static Logger logger = Logger.getLogger(ViadeoUserSettingsIO.class);
	
	private String configFilePath;
	
	private String getConfigFilePath() {
		return configFilePath;
	}

	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}
	
	private ViadeoUserProperty createProperty(String propertyName, String defaultValue, boolean mandatory, ViadeoUserSettingsSetter setter){
		ViadeoUserProperty result = null;
		if(propertyName != null){
			result = new ViadeoUserProperty();
			result.setPropertyName(propertyName);
			result.setDefaultValue(defaultValue);
			result.setMandatory(mandatory);
			result.setSetter(setter);
		}
		return result;
	}

	public ViadeoUserSettings readScrappingSettings() throws ScraperException {
		final ViadeoUserSettings result = new ViadeoUserSettings();
		if(getConfigFilePath() != null){
			result.setHttpCallsDelay(2000);
			
			List<ViadeoUserProperty> viadeoUserPropertyList = new ArrayList<ViadeoUserProperty>();
			
			viadeoUserPropertyList.add(createProperty("keyWordListFilePath", null, true, new ViadeoUserSettingsSetter() {
				public void setValue(String value) {
					result.setKeyWordListFilePath(value);
				}
			}));
			
			viadeoUserPropertyList.add(createProperty("workindDirPath", System.getProperty("user.home"), false, new ViadeoUserSettingsSetter() {
				public void setValue(String value) {
					result.setWorkingDirPath(value);
				}
			}));
			
			viadeoUserPropertyList.add(createProperty("viadeoLogin", null, true, new ViadeoUserSettingsSetter() {
				public void setValue(String value) {
					result.setViadeoLogin(value);
				}
			}));
			
			viadeoUserPropertyList.add(createProperty("viadeoPassword", null, true, new ViadeoUserSettingsSetter() {
				public void setValue(String value) {
					result.setViadeoPassword(value);
				}
			}));
			
			viadeoUserPropertyList.add(createProperty("userAgent", "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6", false, new ViadeoUserSettingsSetter() {
				public void setValue(String value) {
					result.setUserAgent(value);
				}
			}));
			
			viadeoUserPropertyList.add(createProperty("timeout", "20000", false, new ViadeoUserSettingsSetter() {
				public void setValue(String value) {
					result.setTimeout(Integer.parseInt(value));
				}
			}));
			
			viadeoUserPropertyList.add(createProperty("httpCallsDelay", "1000", false, new ViadeoUserSettingsSetter() {
				public void setValue(String value) {
					result.setHttpCallsDelay(Integer.parseInt(value));
				}
			}));
			
			viadeoUserPropertyList.add(createProperty("autoResumeScraping", "false", false, new ViadeoUserSettingsSetter() {
				public void setValue(String value) {
					result.setAutoResumeScraping(Boolean.parseBoolean(value));
				}
			}));
			
			Properties prop = new Properties();
	    	try {
	    		prop.load(new FileInputStream(getConfigFilePath()));
	    		
	    		for (ViadeoUserProperty viadeoUserProperty : viadeoUserPropertyList) {
	    			String propertyValue = prop.getProperty(viadeoUserProperty.getPropertyName());

	    			String value = propertyValue;
	    			
	    			if(propertyValue == null){
	    				if(viadeoUserProperty.isMandatory()){
	    					logger.error(viadeoUserProperty.getPropertyName() + " not found, you must specify it.");
	    				}else{
	    					String msg = viadeoUserProperty.getPropertyName() + " not found.";
	    					if(viadeoUserProperty.getDefaultValue() != null){
	    						msg += "Going with default : " + viadeoUserProperty.getDefaultValue();
	    					}
	    					logger.warn(msg);
	    					
	    					value = viadeoUserProperty.getDefaultValue();
	    				}
	    			}
	    			
	    			try{
	    				viadeoUserProperty.getSetter().setValue(value);
	    			}catch(NumberFormatException ex){
	    				String msg = "Could not parse " + viadeoUserProperty.getPropertyName() + ", got error : " + ex.getMessage();
	    				if(viadeoUserProperty.getDefaultValue() != null){
	    					msg += "Going with default : " + viadeoUserProperty.getDefaultValue();
	    					viadeoUserProperty.getSetter().setValue(viadeoUserProperty.getDefaultValue());
	    				}
			    		logger.warn(msg);
	    			}
				}
	    		
	    	} catch (IOException ex) {
	    		logger.error("Could not retrieve config file, got error : " + ex.getMessage());
	        }
			
		}else{
			throw new ScraperException("Scraping config file path must be specified in the first argument.");
		}
		return result;
	}
	
	private class ViadeoUserProperty{
		private String propertyName;
		private String defaultValue;
		private boolean mandatory;
		private ViadeoUserSettingsSetter setter;
		
		public ViadeoUserSettingsSetter getSetter() {
			return setter;
		}
		public void setSetter(ViadeoUserSettingsSetter setter) {
			this.setter = setter;
		}
		public String getPropertyName() {
			return propertyName;
		}
		public void setPropertyName(String propertyName) {
			this.propertyName = propertyName;
		}
		public String getDefaultValue() {
			return defaultValue;
		}
		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}
		public boolean isMandatory() {
			return mandatory;
		}
		public void setMandatory(boolean mandatory) {
			this.mandatory = mandatory;
		}
	}
	
	private interface ViadeoUserSettingsSetter{
		void setValue(String value);
	}
}
