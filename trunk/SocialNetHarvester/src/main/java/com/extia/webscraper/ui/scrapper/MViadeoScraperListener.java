package com.extia.webscraper.ui.scrapper;

interface MViadeoScraperListener{
		void error(Exception ex);
		void searchEnabled(boolean enableb);
		void showSettings() throws Exception;
		void progressUpdatedList(int progress);
		void progressUpdated(int progress);
		void searchEnabledList(boolean enabled);
		void highlightKeyword(String keyWords);
	}