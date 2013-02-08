package com.extia.webscraper.viadeo.ui.scrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.extia.webscraper.viadeo.ViadeoScraper;

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
		modeleListenerList = new ArrayList<MViadeoScraper.MViadeoScraperListener>();
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
	
	
	interface MViadeoScraperListener{
		void fireUpdateScrapProgress(int progress);
		void fireErrorMsg(Exception ex);
		void fireEnableSearch(boolean enableb);
		void fireShowSettings() throws Exception;
		void updateScrapListProgress(int progress);
		void fireEnableSearchList(boolean enabled);
		void fireHighlightKeywords(String keyWords);
	}

	public void updateScrapProgress(int progress) {
		for (MViadeoScraperListener modeleListener : modeleListenerList) {
			modeleListener.fireUpdateScrapProgress(progress);
		}
	}

	public void errorMsg(Exception ex) {
		for (MViadeoScraperListener modeleListener : modeleListenerList) {
			modeleListener.fireErrorMsg(ex);
		}
	}

	public void enableSearch(boolean enableb) {
		for (MViadeoScraperListener modeleListener : modeleListenerList) {
			modeleListener.fireEnableSearch(enableb);
		}
	}

	public void showSettings() throws Exception {
		for (MViadeoScraperListener modeleListener : modeleListenerList) {
			modeleListener.fireShowSettings();
		}
	}

	public void updateScrapListProgress(int progress) {
		for (MViadeoScraperListener modeleListener : modeleListenerList) {
			modeleListener.updateScrapListProgress(progress);
		}		
	}

	public void enableSearchList(boolean enabled) {
		for (MViadeoScraperListener modeleListener : modeleListenerList) {
			modeleListener.fireEnableSearchList(enabled);
		}		
	}

	public void highlightKeywords(String keyWords) {
		for (MViadeoScraperListener modeleListener : modeleListenerList) {
			modeleListener.fireHighlightKeywords(keyWords);
		}
	}

}
