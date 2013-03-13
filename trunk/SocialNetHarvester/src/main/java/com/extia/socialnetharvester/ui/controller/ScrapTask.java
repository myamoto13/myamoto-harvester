package com.extia.socialnetharvester.ui.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import com.extia.socialnetharvester.http.viadeo.ViadeoScraper;
import com.extia.socialnetharvester.http.viadeo.ViadeoScraper.ScrapingProgressListener;

public class ScrapTask extends SwingWorker<Void, Void> {
    private List<String> keyWordsList;
    private ViadeoScraper viadeoScraper;
    private List<ScrapTaskListener> scrapTaskListenerList;
	
	public ScrapTask() {
		scrapTaskListenerList = new ArrayList<ScrapTaskListener>();
	}
	
	public void removeScrapTaskListener(ScrapTaskListener scrapTaskListener) {
		scrapTaskListenerList.remove(scrapTaskListener);
	}
	
	public void addScrapTaskListener(ScrapTaskListener scrapTaskListener) {
		scrapTaskListenerList.add(scrapTaskListener);		
	}
	
    public void setViadeoScraper(ViadeoScraper viadeoScraper) {
		this.viadeoScraper = viadeoScraper;
	}
    
    public void setKeyWordsList(List<String> keyWordsList) {
    	this.keyWordsList = keyWordsList;
	}

    public Void doInBackground() {
    	
    	ScrapingProgressListener scrapingProgressListener = new ScrapingProgressListener(){
    		public void progressUpdated(int progress){
    			setProgress(progress);
    		}

			public void fireScrapingKeyWordsStarted(String keyWords) {
				ScrapTask.this.fireScrapingKeyWordsStarted(keyWords);
			}

    	};
    	
    	try{
    		setProgress(0);
    		fireSearchStarting();
    		viadeoScraper.addScrapingProgressListener(scrapingProgressListener);
    		viadeoScraper.connectToViadeo();
    		viadeoScraper.scrapDatas(keyWordsList);
    	}catch(Exception ex){
    		fireSearchError(ex);
    	}finally{
    		viadeoScraper.removeScrapingProgressListener(scrapingProgressListener);
    	}
    	return null;
    }
    
    protected void fireScrapingKeyWordsStarted(String keyWords) {
    	for (ScrapTaskListener scrapTaskListener : scrapTaskListenerList) {
			scrapTaskListener.scrapingKeyWordsStarted(keyWords);
		}		
	}

	public void done() {
    	viadeoScraper.setInterruptFlag(false);
		fireSearchFinished();
    }
    
	private void fireSearchError(Exception ex) {
		for (ScrapTaskListener scrapTaskListener : scrapTaskListenerList) {
			scrapTaskListener.searchError(ex);
		}		
	}
	
	private void fireSearchFinished() {
		for (ScrapTaskListener scrapTaskListener : scrapTaskListenerList) {
			scrapTaskListener.searchFinished();
		}		
	}

	private void fireSearchStarting() {
		for (ScrapTaskListener scrapTaskListener : scrapTaskListenerList) {
			scrapTaskListener.searchStarted();
		}		
	}
	
	public void cancelScraping(){
    	viadeoScraper.setInteruptFlag(true);
    }
	
	public interface ScrapTaskListener{
		public void searchError(Exception ex);
		public void scrapingKeyWordsStarted(String keyWords);
		public void searchFinished();
		public void searchStarted();
	}

	
}