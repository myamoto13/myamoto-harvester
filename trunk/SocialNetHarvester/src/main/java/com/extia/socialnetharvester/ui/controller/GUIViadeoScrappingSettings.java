package com.extia.socialnetharvester.ui.controller;

import javax.swing.JPanel;

import com.extia.socialnetharvester.ui.model.MViadeoScrapingSettings;
import com.extia.socialnetharvester.ui.view.VViadeoScrapingSettings;
import com.extia.socialnetharvester.ui.view.VViadeoScrapingSettings.VViadeoScrapingSettingsListener;



public class GUIViadeoScrappingSettings implements VViadeoScrapingSettingsListener{
	
	private MViadeoScrapingSettings modele;
	private VViadeoScrapingSettings vue;
	
	public GUIViadeoScrappingSettings() {
		
		MViadeoScrapingSettings modele = new MViadeoScrapingSettings();
		VViadeoScrapingSettings vue = new VViadeoScrapingSettings();
		
		setModele(modele);
		
		setVue(vue);
		vue.addVueListener(this);
		
		modele.addModeleListener(vue);
		vue.setModele(modele);
	}
	
	public JPanel getUI(){
		return getVue().getUi();
	}
	
	private MViadeoScrapingSettings getModele() {
		return modele;
	}

	private void setModele(MViadeoScrapingSettings modele) {
		this.modele = modele;
	}

	private VViadeoScrapingSettings getVue() {
		return vue;
	}

	private void setVue(VViadeoScrapingSettings vue) {
		this.vue = vue;
	}
}
