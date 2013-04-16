package com.extia.socialnetharvester.ui.view;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.extia.socialnetharvester.ui.HyperlinkBuilder;
import com.extia.socialnetharvester.ui.model.MViadeoScraper;
import com.extia.socialnetharvester.ui.model.MViadeoScraper.MViadeoScraperListener;

public class VViadeoScraper implements MViadeoScraperListener {
	
	private static Logger logger = Logger.getLogger(VViadeoScraper.class);

	private enum PageScraper{SETTINGS, KEYWORDS_SEARCH, KEYWORDLIST_SEARCH}

	@Resource(name="mViadeoScraper")
	private MViadeoScraper modele;

	private JPanel ui;
	
	private JPanel uiSearch;
	private JTextField keywordsTxtFld;
	private JButton scrapButton;
	private JButton stopButton;
	private JButton linkModeKeywordlist;
	private JButton linkSettings;
	private JProgressBar progressBar;
	private JPanel pnlSearch;
	private JPanel pnlProgress;
	
	private JPanel uiSearchList;
	private JTextField fileKeyWordTxtFld;
	private JButton scrapButtonList;
	private JButton stopButtonList;
	private JButton linkModeKeyword;
	private JButton linkSettingsList;
	private JProgressBar progressBarList;
	private JPanel pnlSearchList;
	private JPanel pnlProgressList;
	private JList keyWordJList;
	
	private JPanel uiSettings;
	private JPanel pnlSettings;
	private JButton linkModeKeywordlistSttgs;
	private JButton linkModeKeywordSttgs;
	
	private List<VViadeoScraperListener> vueListenerList;
	
	public VViadeoScraper() {
		vueListenerList = new ArrayList<VViadeoScraperListener>();
	}

	public void addVueListener(VViadeoScraperListener vueListener) {
		vueListenerList.add(vueListener);		
	}

	public void setModele(MViadeoScraper modele) {
		this.modele = modele;		
	}

	private MViadeoScraper getModele() {
		return modele;
	}
	
	private Dimension getPrefSizeSearch(){
		return new Dimension(250, 27);
	}

	private JPanel getUiSearch(){
		if(uiSearch == null){
			scrapButton = new JButton(new ImageIcon(getClass().getResource("/icones/website-scraper-icon-32.png")));
			scrapButton.setToolTipText("perform scraping");
			scrapButton.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try{
						String keywords = keywordsTxtFld.getText();
						if(keywords != null && !"".equals(keywords)){
							fireScrapingLaunched(keywords);
						} 
					}catch(Exception ex){
						error(ex);
					}
				}

			});

			stopButton = new JButton(new ImageIcon(getClass().getResource("/icones/stop_icon_32.png")));
			stopButton.setToolTipText("stop scraping");
			stopButton.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try{
						fireScrapingStopped();
					}catch(Exception ex){
						error(ex);
					}
				}
			});
			
			linkModeKeywordlist = HyperlinkBuilder.buildLink(new JButton("keywords-list"));
			linkModeKeywordlist.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try{
						fillUI(PageScraper.KEYWORDLIST_SEARCH);
					}catch(Exception ex){
						error(ex);
					}
				}
			});
			

			linkSettings = HyperlinkBuilder.buildLink(new JButton("Settings"));
			linkSettings.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try{
						fireEnterSettings();
					}catch(Exception ex){
						error(ex);
					}
				}

			});

			Dimension prefSizSearch = getPrefSizeSearch();

			keywordsTxtFld = new JTextField();
			keywordsTxtFld.setPreferredSize(prefSizSearch);
			
			int maxValue = 100;

			progressBar = new JProgressBar(0, maxValue);
			progressBar.setValue(0);
			progressBar.setStringPainted(true);
			progressBar.setPreferredSize(prefSizSearch);

			pnlProgress = new JPanel(new GridBagLayout());
			pnlProgress.setOpaque(false);
			pnlProgress.setVisible(false);
			pnlProgress.add(stopButton, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
			pnlProgress.add(progressBar, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));


			JPanel pnlLinks = new JPanel(new GridBagLayout());
			pnlLinks.setOpaque(false);
			pnlLinks.add(linkSettings, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			pnlLinks.add(linkModeKeywordlist, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
			
			pnlSearch = new JPanel(new GridBagLayout());
			pnlSearch.setOpaque(false);
			pnlSearch.add(scrapButton, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			pnlSearch.add(keywordsTxtFld, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));

			pnlSearch.add(pnlLinks, new GridBagConstraints(0, 1, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));

			
			uiSearch = new JPanel(new GridBagLayout());
			uiSearch.setOpaque(false);

			uiSearch.add(pnlSearch, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			uiSearch.add(pnlProgress, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

			uiSearch.revalidate();
			uiSearch.repaint();
		}
		return uiSearch;
	}
	
	private Component getUiSearchList() throws IOException {
		if(uiSearchList == null){
			scrapButtonList = new JButton(new ImageIcon(getClass().getResource("/icones/website-scraper-icon-32.png")));
			scrapButtonList.setToolTipText("perform scraping");
			scrapButtonList.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try{
						fireScrapingFromFileLaunched();
					}catch(Exception ex){
						error(ex);
					}
				}

			});

			stopButtonList = new JButton(new ImageIcon(getClass().getResource("/icones/stop_icon_32.png")));
			stopButtonList.setToolTipText("stop scraping");
			stopButtonList.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try{
						fireScrapingStopped();
					}catch(Exception ex){
						error(ex);
					}
				}

			});
			
			linkModeKeyword = HyperlinkBuilder.buildLink(new JButton("keywords"));
			linkModeKeyword.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try{
						fillUI(PageScraper.KEYWORDS_SEARCH);
					}catch(Exception ex){
						error(ex);
					}
				}
			});
			

			linkSettingsList = HyperlinkBuilder.buildLink(new JButton("Settings"));
			linkSettingsList.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try{
						fireEnterSettings();
					}catch(Exception ex){
						error(ex);
					}
				}

			});
			
			Dimension prefSizSearch = getPrefSizeSearch();
			
			String fileKeyword = getModele().getKeyWordFilePath();
			if(fileKeyword == null){
				fileKeyword = "keyWordListFilePath not specified.";
			}
			
			fileKeyWordTxtFld = new JTextField();
			fileKeyWordTxtFld.setEditable(false);
			fileKeyWordTxtFld.setText(fileKeyword);
			
			fileKeyWordTxtFld.setPreferredSize(prefSizSearch);
			
			Object[] keywordList = getModele().getKeyWordList() != null ? 
					getModele().getKeyWordList().toArray() : new Object[0];
			
			keyWordJList = new JList(keywordList);
			keyWordJList.setEnabled(false);
			JScrollPane scrollPane = new JScrollPane(keyWordJList);
			scrollPane.getViewport().setView(keyWordJList);
			scrollPane.getViewport().setOpaque(false);

			progressBarList = new JProgressBar(0, 100);
			progressBarList.setValue(0);
			progressBarList.setStringPainted(true);
			progressBarList.setPreferredSize(prefSizSearch);


			pnlProgressList = new JPanel(new GridBagLayout());
			pnlProgressList.setOpaque(false);
			pnlProgressList.setVisible(false);
			pnlProgressList.add(stopButtonList, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
			pnlProgressList.add(progressBarList, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));


			JPanel pnlLinks = new JPanel(new GridBagLayout());
			pnlLinks.setOpaque(false);
			pnlLinks.add(linkSettingsList, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			pnlLinks.add(linkModeKeyword, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
			
			pnlSearchList = new JPanel(new GridBagLayout());
			pnlSearchList.setOpaque(false);
			pnlSearchList.add(scrapButtonList, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			pnlSearchList.add(fileKeyWordTxtFld, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
			pnlSearchList.add(pnlLinks, new GridBagConstraints(0, 1, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));

			uiSearchList = new JPanel(new GridBagLayout());
			uiSearchList.setOpaque(false);
			
			uiSearchList.add(pnlSearchList, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			uiSearchList.add(pnlProgressList, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			uiSearchList.add(scrollPane, new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
			
			uiSearchList.revalidate();
			uiSearchList.repaint();
		}
		return uiSearchList;
	}
	
	private JPanel getUiSettings() {
		if(uiSettings == null){
			linkModeKeywordSttgs = HyperlinkBuilder.buildLink(new JButton("keywords"));
			linkModeKeywordSttgs.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try{
						fillUI(PageScraper.KEYWORDS_SEARCH);
					}catch(Exception ex){
						error(ex);
					}
				}
			});
			
			linkModeKeywordlistSttgs = HyperlinkBuilder.buildLink(new JButton("keywords-list"));
			linkModeKeywordlistSttgs.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try{
						fillUI(PageScraper.KEYWORDLIST_SEARCH);
					}catch(Exception ex){
						error(ex);
					}
				}
			});
			
			JPanel pnlLinks = new JPanel(new GridBagLayout());
			pnlLinks.setOpaque(false);
			pnlLinks.add(linkModeKeywordSttgs, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			pnlLinks.add(linkModeKeywordlistSttgs, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
			
			uiSettings = new JPanel(new GridBagLayout());
			uiSettings.setOpaque(false);
			
			uiSettings.add(pnlSettings, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			uiSettings.add(pnlLinks, new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));

			uiSettings.revalidate();
			uiSettings.repaint();
		}
		return uiSettings;
	}


	public JPanel getUi() throws IOException {
		if(ui == null){
			ui = new JPanel(new GridBagLayout());
			ui.setOpaque(false);
			fillUI(PageScraper.KEYWORDS_SEARCH);
		}
		return ui;
	}

	private void fillUI(PageScraper page) throws IOException {
		ui.removeAll();
		
		if(page == PageScraper.SETTINGS){
			ui.add(getUiSettings(), new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}else if(page == PageScraper.KEYWORDS_SEARCH){
			ui.add(getUiSearch(), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}else if(page == PageScraper.KEYWORDLIST_SEARCH){
			ui.add(getUiSearchList(), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		
		ui.revalidate();
		ui.repaint();		
	}

	private void fireEnterSettings() throws IOException  {
		for (VViadeoScraperListener vueListener : vueListenerList) {
			vueListener.fireEnterSettings();
		}
	}

	private void fireScrapingStopped() {
		for (VViadeoScraperListener vueListener : vueListenerList) {
			vueListener.fireScrapingStopped();
		}
	}

	private void fireScrapingLaunched(String keywords) {
		for (VViadeoScraperListener vueListener : vueListenerList) {
			vueListener.fireSearchLaunched(keywords);
		}
	}
	
	private void fireScrapingFromFileLaunched() throws IOException {
		for (VViadeoScraperListener vueListener : vueListenerList) {
			vueListener.fireScrapingFromFileLaunched();
		}		
	}
	
	private JFrame getParentFrame() {
		Component p = ui;
		while (!(p instanceof JFrame)){
			p = p.getParent();
		}
		return ((JFrame)p);
	}

	public void searchEnabledList(boolean enabled) {
		pnlSearchList.setVisible(enabled);
		pnlProgressList.setVisible(!enabled);
		getParentFrame().setCursor(enabled ? null : Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}
	
	public void searchEnabled(boolean enabled) {
		pnlSearch.setVisible(enabled);
		pnlProgress.setVisible(!enabled);
		getParentFrame().setCursor(enabled ? null : Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	public void showSettings() throws IOException {
		fillUI(PageScraper.SETTINGS);
	}
	
	public void progressUpdatedList(int progress) {
		progressBarList.setValue(progress);
	}

	public void progressUpdated(int progress) {
		progressBar.setValue(progress);
	}

	public void error(Exception ex) {
		logger.error(ex.getMessage(), ex);
	}

	public void setPnlSettings(JPanel pnlSettings) {
		this.pnlSettings = pnlSettings;
	}
	
	public void highlightKeyword(String keyWords) {
		keyWordJList.setSelectedValue(keyWords, true);
	}
	
	public interface VViadeoScraperListener{
		void fireSearchLaunched(String keywords);
		void fireScrapingFromFileLaunched() throws IOException;
		void fireEnterSettings() throws IOException ;
		void fireScrapingStopped();
	}

}
