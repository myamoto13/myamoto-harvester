package com.extia.socialnetharvester.ui.model;

import java.util.ArrayList;
import java.util.List;

public class MViadeoScrapingSettings {
	
	private List<MViadeoScrapingSettingsListener> modeleListenerList;
	

	public MViadeoScrapingSettings() {
		modeleListenerList = new ArrayList<MViadeoScrapingSettingsListener>();
	}
	
	public void addModeleListener(MViadeoScrapingSettingsListener modeleListener) {
		modeleListenerList.add(modeleListener);		
	}
	
	public interface MViadeoScrapingSettingsListener{
	}

//	public void updateScrapProgress(int progress) {
//		for (MViadeoScrapingSettingsListener modeleListener : modeleListenerList) {
//		}
//	}

}
