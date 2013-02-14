package com.extia.webscraper.ui.scrapper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JPanel;

import com.extia.webscraper.scraper.ViadeoScraper;
import com.extia.webscraper.ui.scrapper.ScrapTask.ScrapTaskListener;
import com.extia.webscraper.ui.scrapper.VViadeoScraper.VViadeoScraperListener;
import com.extia.webscraper.ui.setting.GUIViadeoScrappingSettings;

public class GUIViadeoScrapper implements VViadeoScraperListener {

	private GUIViadeoScrappingSettings guiSettings;

	private MViadeoScraper modele;
	private VViadeoScraper vue;
	private ScrapTask scrapTask;
	
	private ScrapTaskListener keywordsSearchTaskListener;
	private PropertyChangeListener keywordsSearchTaskPListener;
	
	private ScrapTaskListener keywordsListSearchTaskListener;
	private PropertyChangeListener keywordsListSearchTaskPListener;
	
	public GUIViadeoScrapper(){
		MViadeoScraper modele = new MViadeoScraper();
		VViadeoScraper vue = new VViadeoScraper();

		setModele(modele);
		setVue(vue);

		vue.setModele(modele);
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

	private void setModele(MViadeoScraper modele) {
		this.modele = modele;
	}

	private VViadeoScraper getVue() {
		return vue;
	}

	private void setVue(VViadeoScraper vue) {
		this.vue = vue;
	}

	public void setViadeoScraper(ViadeoScraper viadeoScrapper){
		getModele().setViadeoScraper(viadeoScrapper);
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

				public void fireSearchStarting() {
					getModele().enableSearch(false);				
				}

				public void fireSearchFinished() {
					getModele().enableSearch(true);				
				}

				public void fireSearchError(Exception ex) {
					getModele().errorMsg(ex);				
				}

				public void fireScrapingKeyWordsStarted(String keyWords) {
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

				public void fireSearchStarting() {
					getModele().enableSearchList(false);				
				}

				public void fireSearchFinished() {
					getModele().enableSearchList(true);				
				}

				public void fireSearchError(Exception ex) {
					getModele().errorMsg(ex);
				}

				public void fireScrapingKeyWordsStarted(String keyWords) {
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
