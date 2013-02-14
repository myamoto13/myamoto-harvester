package com.extia.webscraper.ui.setting;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.extia.webscraper.ui.setting.MViadeoScrapingSettings.MViadeoScrapingSettingsListener;

public class VViadeoScrapingSettings implements MViadeoScrapingSettingsListener {

	private MViadeoScrapingSettings modele;
	private JPanel ui;

	private List<VViadeoScrapingSettingsListener> vueListenerList;
	public VViadeoScrapingSettings() {
		vueListenerList = new ArrayList<VViadeoScrapingSettingsListener>();
	}

	public void addVueListener(VViadeoScrapingSettingsListener vueListener) {
		vueListenerList.add(vueListener);		
	}

	public void setModele(MViadeoScrapingSettings modele) {
		this.modele = modele;		
	}

	private MViadeoScrapingSettings getModele() {
		return modele;
	}

	public JPanel getUi() {
		if(ui == null){
			
			ui = new JPanel(new GridBagLayout());
			ui.setOpaque(false);
			

//			ui.add(pnlSearch, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//			ui.add(pnlProgress, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			
			ui.revalidate();
			ui.repaint();
		}
		return ui;
	}
	
//	private void fireEnterSettings() {
//		for (VViadeoScrapingSettingsListener vueListener : vueListenerList) {
//		}
//	}


	interface VViadeoScrapingSettingsListener{
	}

	private JFrame getParentFrame() {
		Component p = ui;
		while ( (p = p.getParent()) != null && !(p instanceof JFrame));
		return((JFrame)p);
	}

	public void fireShowSettings() {
		
	}
	
}
