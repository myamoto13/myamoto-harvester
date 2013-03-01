package com.extia.socialnetharvester.ui.scrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.extia.socialnetharvester.http.viadeo.ViadeoScraper;

public class MViadeoScraper {
	
	private List<MViadeoScraperListener> modeleListenerList;
	
	private ViadeoScraper viadeoScraper;
	
	public ViadeoScraper getViadeoScrapper() {
		return viadeoScraper;
	}

	public void setViadeoScraper(ViadeoScraper viadeoScraper) {
		this.viadeoScraper = viadeoScraper;
	}

	public MViadeoScraper() {
		modeleListenerList = new ArrayList<MViadeoScraperListener>();
	}
	
	public void addModeleListener(MViadeoScraperListener modeleListener) {
		modeleListenerList.add(modeleListener);		
	}
	
	public List<String> getKeyWordList() throws IOException {
		return viadeoScraper != null ? viadeoScraper.getKeyWordList() : null;
	}

	public String getKeyWordFilePath() {
		return viadeoScraper != null ? viadeoScraper.getKeyWordFilePath() : null;
	}
	
	public void updateScrapProgress(int progress) {
		for (MViadeoScraperListener modeleListener : modeleListenerList) {
			modeleListener.progressUpdated(progress);
		}
	}

	public void errorMsg(Exception ex) {
		for (MViadeoScraperListener modeleListener : modeleListenerList) {
			modeleListener.error(ex);
		}
	}

	public void enableSearch(boolean enableb) {
		for (MViadeoScraperListener modeleListener : modeleListenerList) {
			modeleListener.searchEnabled(enableb);
		}
	}

	public void showSettings() throws Exception {
		for (MViadeoScraperListener modeleListener : modeleListenerList) {
			modeleListener.showSettings();
		}
	}

	public void updateScrapListProgress(int progress) {
		for (MViadeoScraperListener modeleListener : modeleListenerList) {
			modeleListener.progressUpdatedList(progress);
		}		
	}

	public void enableSearchList(boolean enabled) {
		for (MViadeoScraperListener modeleListener : modeleListenerList) {
			modeleListener.searchEnabledList(enabled);
		}		
	}

	public void highlightKeywords(String keyWords) {
		for (MViadeoScraperListener modeleListener : modeleListenerList) {
			modeleListener.highlightKeyword(keyWords);
		}
	}

}
