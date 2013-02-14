package com.extia.webscraper.ui.scrapper;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import com.extia.webscraper.scraper.ViadeoScraper;
import com.extia.webscraper.scraper.ViadeoScraper.ScrapingProgressListener;

class ScrapTask extends SwingWorker<Void, Void> {
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
			scrapTaskListener.fireScrapingKeyWordsStarted(keyWords);
		}		
	}

	public void done() {
    	viadeoScraper.setInterruptFlag(false);
		fireSearchFinished();
    }
    
	private void fireSearchError(Exception ex) {
		for (ScrapTaskListener scrapTaskListener : scrapTaskListenerList) {
			scrapTaskListener.fireSearchError(ex);
		}		
	}
	
	private void fireSearchFinished() {
		for (ScrapTaskListener scrapTaskListener : scrapTaskListenerList) {
			scrapTaskListener.fireSearchFinished();
		}		
	}

	private void fireSearchStarting() {
		for (ScrapTaskListener scrapTaskListener : scrapTaskListenerList) {
			scrapTaskListener.fireSearchStarting();
		}		
	}
	
	public void cancelScraping(){
    	viadeoScraper.setInteruptFlag(true);
    }
	
	interface ScrapTaskListener{
		public void fireSearchError(Exception ex);
		public void fireScrapingKeyWordsStarted(String keyWords);
		public void fireSearchFinished();
		public void fireSearchStarting();
	}

	
}