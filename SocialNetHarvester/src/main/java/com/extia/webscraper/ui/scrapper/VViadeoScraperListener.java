package com.extia.webscraper.ui.scrapper;

import java.io.IOException;

interface VViadeoScraperListener{
		public void fireSearchLaunched(String keywords);
		public void fireScrapingFromFileLaunched() throws IOException;
		public void fireEnterSettings() throws Exception;
		public void fireScrapingStopped();
	}