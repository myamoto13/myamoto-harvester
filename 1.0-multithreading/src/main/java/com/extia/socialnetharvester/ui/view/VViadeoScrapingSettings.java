package com.extia.socialnetharvester.ui.view;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.extia.socialnetharvester.ui.model.MViadeoScrapingSettings.MViadeoScrapingSettingsListener;

public class VViadeoScrapingSettings implements MViadeoScrapingSettingsListener {
	
	
	private JPanel ui;

	private List<VViadeoScrapingSettingsListener> vueListenerList;
	public VViadeoScrapingSettings() {
		vueListenerList = new ArrayList<VViadeoScrapingSettingsListener>();
	}

	public void addVueListener(VViadeoScrapingSettingsListener vueListener) {
		vueListenerList.add(vueListener);		
	}

	public JPanel getUi() {
		if(ui == null){
			
			ui = new JPanel(new GridBagLayout());
			ui.setOpaque(false);
			
			ui.revalidate();
			ui.repaint();
		}
		return ui;
	}
	
	public interface VViadeoScrapingSettingsListener{
	}

	public void fireShowSettings() {
		
	}
	
}
