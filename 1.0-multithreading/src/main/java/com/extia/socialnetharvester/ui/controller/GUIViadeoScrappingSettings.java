package com.extia.socialnetharvester.ui.controller;

import javax.annotation.Resource;
import javax.swing.JPanel;

import com.extia.socialnetharvester.ui.model.MViadeoScrapingSettings;
import com.extia.socialnetharvester.ui.view.VViadeoScrapingSettings;
import com.extia.socialnetharvester.ui.view.VViadeoScrapingSettings.VViadeoScrapingSettingsListener;



public class GUIViadeoScrappingSettings implements VViadeoScrapingSettingsListener{
	
	@Resource(name="mViadeoScrapingSettings")
	private MViadeoScrapingSettings modele;
	
	@Resource(name="vViadeoScrapingSettings")
	private VViadeoScrapingSettings vue;
	
	public void init(){
		vue.addVueListener(this);

		modele.addModeleListener(vue);
	}
	
	public JPanel getUI(){
		return getVue().getUi();
	}
	
	public void setModele(MViadeoScrapingSettings modele) {
		this.modele = modele;
	}

	private VViadeoScrapingSettings getVue() {
		return vue;
	}

	public void setVue(VViadeoScrapingSettings vue) {
		this.vue = vue;
	}
}
