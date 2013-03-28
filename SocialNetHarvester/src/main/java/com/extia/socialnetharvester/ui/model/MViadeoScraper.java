package com.extia.socialnetharvester.ui.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.extia.socialnetharvester.http.viadeo.ViadeoScraper;

public class MViadeoScraper {
	
	private List<MViadeoScraperListener> modeleListenerList;
	
	@Resource(name="viadeoScraper")
	private ViadeoScraper viadeoScraper;
	
	public MViadeoScraper() {
		modeleListenerList = new ArrayList<MViadeoScraperListener>();
	}
	
	public ViadeoScraper getViadeoScrapper() {
		return viadeoScraper;
	}

	public void setViadeoScraper(ViadeoScraper viadeoScraper) {
		this.viadeoScraper = viadeoScraper;
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
	
	public interface MViadeoScraperListener{
			void error(Exception ex);
			void searchEnabled(boolean enableb);
			void showSettings() throws Exception;
			void progressUpdatedList(int progress);
			void progressUpdated(int progress);
			void searchEnabledList(boolean enabled);
			void highlightKeyword(String keyWords);
		}

}
