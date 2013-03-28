package com.extia.socialnetharvester.ui.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Arrays;

import javax.annotation.Resource;
import javax.swing.JPanel;

import com.extia.socialnetharvester.ui.controller.ScrapTask.ScrapTaskListener;
import com.extia.socialnetharvester.ui.model.MViadeoScraper;
import com.extia.socialnetharvester.ui.view.VViadeoScraper;
import com.extia.socialnetharvester.ui.view.VViadeoScraper.VViadeoScraperListener;

public class GUIViadeoScrapper implements VViadeoScraperListener {
	
	@Resource(name="guiSettings")
	private GUIViadeoScrappingSettings guiSettings;

	@Resource(name="mViadeoScraper")
	private MViadeoScraper modele;
	
	
	@Resource(name="vViadeoScraper")
	private VViadeoScraper vue;
	
	private ScrapTask scrapTask;
	
	private ScrapTaskListener keywordsSearchTaskListener;
	private PropertyChangeListener keywordsSearchTaskPListener;
	
	private ScrapTaskListener keywordsListSearchTaskListener;
	private PropertyChangeListener keywordsListSearchTaskPListener;
	
	public void init(){
		vue.addVueListener(this);
		modele.addModeleListener(vue);
	}

	public void setGuiSettings(GUIViadeoScrappingSettings guiSettings) {
		vue.setUiSettings(guiSettings.getUI());
		this.guiSettings = guiSettings;
	}

	private MViadeoScraper getModele() {
		return modele;
	}

	public void setModele(MViadeoScraper modele) {
		this.modele = modele;
	}

	private VViadeoScraper getVue() {
		return vue;
	}

	public void setVue(VViadeoScraper vue) {
		this.vue = vue;
	}

	public JPanel getUI() throws IOException{
		return getVue().getUi();
	}

	public void fireScrapingStopped() {
		getScrapTask().cancelScraping();
	}

	public void fireEnterSettings() throws Exception {
		getModele().showSettings();
	}

	
	public void fireSearchLaunched(String keywords) {
		if(getScrapTask() != null){
			getScrapTask().removePropertyChangeListener(getKeywordsSearchTaskPListener());
			getScrapTask().removeScrapTaskListener(getKeywordsSearchTaskListener());
		}
		//Instances of javax.swing.SwingWorker are not reusuable
		getModele().updateScrapProgress(0);
		ScrapTask scrapTask = new ScrapTask();
		setScrapTask(scrapTask);
		scrapTask.setViadeoScraper(getModele().getViadeoScrapper());
		scrapTask.setKeyWordsList(Arrays.asList(new String[]{keywords}));
		scrapTask.addPropertyChangeListener(getKeywordsSearchTaskPListener());
		scrapTask.addScrapTaskListener(getKeywordsSearchTaskListener());
		scrapTask.execute();
	}
	
	private ScrapTaskListener getKeywordsSearchTaskListener(){
		if(keywordsSearchTaskListener == null){
			keywordsSearchTaskListener = new ScrapTaskListener() {

				public void searchStarted() {
					getModele().enableSearch(false);				
				}

				public void searchFinished() {
					getModele().enableSearch(true);				
				}

				public void searchError(Exception ex) {
					getModele().errorMsg(ex);				
				}

				public void scrapingKeyWordsStarted(String keyWords) {
				}
			};
		}
		return keywordsSearchTaskListener;
	}
	private PropertyChangeListener getKeywordsSearchTaskPListener(){
		if(keywordsSearchTaskPListener == null){
			keywordsSearchTaskPListener = new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					if ("progress" == evt.getPropertyName()) {
						int progress = (Integer) evt.getNewValue();
						getModele().updateScrapProgress(progress);
					}
				}

			};
		}
		return keywordsSearchTaskPListener;
	}

	public void fireScrapingFromFileLaunched() throws IOException {
		//Instances of javax.swing.SwingWorker are not reusuable
		if(getScrapTask() != null){
			getScrapTask().removePropertyChangeListener(getKeywordsListSearchTaskPListener());
			getScrapTask().removeScrapTaskListener(getKeywordsListSearchTaskListener());
		}
		getModele().updateScrapListProgress(0);
		ScrapTask scrapTask = new ScrapTask();
		setScrapTask(scrapTask);
		scrapTask.setViadeoScraper(getModele().getViadeoScrapper());
		scrapTask.setKeyWordsList(getModele().getKeyWordList());
		scrapTask.addPropertyChangeListener(getKeywordsListSearchTaskPListener());

		scrapTask.addScrapTaskListener(getKeywordsListSearchTaskListener());

		scrapTask.execute();
	}
	
	private ScrapTaskListener getKeywordsListSearchTaskListener(){
		if(keywordsListSearchTaskListener == null){
			keywordsListSearchTaskListener = new ScrapTaskListener() {

				public void searchStarted() {
					getModele().enableSearchList(false);				
				}

				public void searchFinished() {
					getModele().enableSearchList(true);				
				}

				public void searchError(Exception ex) {
					getModele().errorMsg(ex);
				}

				public void scrapingKeyWordsStarted(String keyWords) {
					getModele().highlightKeywords(keyWords);
				}


			};
		}
		return keywordsListSearchTaskListener;
	}
	private PropertyChangeListener getKeywordsListSearchTaskPListener(){
		if(keywordsListSearchTaskPListener == null){
			keywordsListSearchTaskPListener = new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					if ("progress" == evt.getPropertyName()) {
						int progress = (Integer) evt.getNewValue();
						getModele().updateScrapListProgress(progress);
					}
				}

			};
		}
		return keywordsListSearchTaskPListener;
	}

	private ScrapTask getScrapTask() {
		return scrapTask;
	}

	private void setScrapTask(ScrapTask scrapTask) {
		this.scrapTask = scrapTask;
	}



}
