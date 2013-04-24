package com.extia.socialnetharvester.ui.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

public class ScraperMultithread {

	private ScrapTask scrapTask;
	private PropertyChangeListener keywordsSearchTaskPListener;

	private PropertyChangeListener getKeywordsSearchTaskPListener(){
		if(keywordsSearchTaskPListener == null){
			keywordsSearchTaskPListener = new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					if ("progress".equals(evt.getPropertyName())) {
						fireProgressUpdated(evt.getNewValue());
					}
				}

			};
		}
		return keywordsSearchTaskPListener;
	}
	
	private void fireProgressUpdated(Object newValue) {
		// TODO Auto-generated method stub
		
	}

	private ScrapTask getScrapTask() {
		return scrapTask;
	}

	private void setScrapTask(ScrapTask scrapTask) {
		this.scrapTask = scrapTask;
	}
	
	public void scrap(List<String> keywordList){
//		//Instances of javax.swing.SwingWorker are not reusuable
//		if(getScrapTask() != null){
//			getScrapTask().removePropertyChangeListener(getKeywordsListSearchTaskPListener());
//			getScrapTask().removeScrapTaskListener(getKeywordsListSearchTaskListener());
//		}
//		fireProgressUpdated(0);
//		ScrapTask scrapTsk = new ScrapTask();
//		setScrapTask(scrapTsk);
//		scrapTsk.setViadeoScraper();
//		scrapTsk.setKeyWordsList(keywordList);
//		scrapTsk.addPropertyChangeListener(getKeywordsListSearchTaskPListener());
//
//		scrapTsk.addScrapTaskListener(getKeywordsListSearchTaskListener());
//
//		scrapTsk.execute();
	}

	
	public void scrap(String keywords) {
//		if(getScrapTask() != null){
//			getScrapTask().removePropertyChangeListener(getKeywordsSearchTaskPListener());
//			getScrapTask().removeScrapTaskListener(getKeywordsSearchTaskListener());
//		}
//		//Instances of javax.swing.SwingWorker are not reusuable
//		getModele().updateScrapProgress(0);
//		ScrapTask scrapTsk = new ScrapTask();
//		setScrapTask(scrapTsk);
//		scrapTsk.setViadeoScraper(getModele().getViadeoScrapper());
//		scrapTsk.setKeyWordsList(Arrays.asList(new String[]{keywords}));
//		scrapTsk.addPropertyChangeListener(getKeywordsSearchTaskPListener());
//		scrapTsk.addScrapTaskListener(getKeywordsSearchTaskListener());
//		scrapTsk.execute();
	}
}
