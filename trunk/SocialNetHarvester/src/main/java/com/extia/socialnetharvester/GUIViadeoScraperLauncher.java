package com.extia.socialnetharvester;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.io.IOException;

import javax.annotation.Resource;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import com.extia.socialnetharvester.business.ScraperException;
import com.extia.socialnetharvester.business.ScraperSystemFilesFactory;
import com.extia.socialnetharvester.business.ViadeoUserSettings;
import com.extia.socialnetharvester.io.CSVFileIO;
import com.extia.socialnetharvester.io.ViadeoUserSettingsIO;
import com.extia.socialnetharvester.ui.controller.GUIViadeoScrapper;
import com.extia.socialnetharvester.ui.controller.GUIViadeoScrappingSettings;

public class GUIViadeoScraperLauncher {
	
	private static Logger logger = Logger.getLogger(GUIViadeoScraperLauncher.class);
	
	@Resource(name="settingsIO")
	private ViadeoUserSettingsIO settingsIO;
	
	@Resource(name="guiSettings")
	private GUIViadeoScrappingSettings guiSettings;
	
	@Resource(name="guiScraper")
	private GUIViadeoScrapper guiViadeoScraper;
	
	public ViadeoUserSettingsIO getSettingsIO() {
		return settingsIO;
	}

	public void setSettingsIO(ViadeoUserSettingsIO settingsIO) {
		this.settingsIO = settingsIO;
	}

	public GUIViadeoScrappingSettings getGuiSettings() {
		return guiSettings;
	}

	public void setGuiSettings(GUIViadeoScrappingSettings guiSettings) {
		this.guiSettings = guiSettings;
	}

	public GUIViadeoScrapper getGuiViadeoScraper() {
		return guiViadeoScraper;
	}

	public void setGuiViadeoScraper(GUIViadeoScrapper guiViadeoScraper) {
		this.guiViadeoScraper = guiViadeoScraper;
	}

	public void launch(String configFilePath) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IOException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		final JPanel contentPane = new JPanel(new GridBagLayout()){
			protected void paintComponent(Graphics g) {
				Color color1 = new Color(231, 248, 252);
				Color color2 = new Color(17, 156, 190);
				
				GradientPaint gradient = new GradientPaint(new Point(0, 0), color1, new Point(0, getHeight()), color2);
				
				((Graphics2D)g).setPaint(gradient);
				((Graphics2D)g).fill(getBounds());
			}
		};
		contentPane.add(guiViadeoScraper.getUI(), new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				JFrame frame = new JFrame();
				frame.setSize(new Dimension(800, 600));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().add(contentPane);
				frame.setVisible(true);				
			}
		});
		
	}
	
	
	
	public static void main(String[] args) {
		try {
			String configFilePath = null;
			if(args != null && args.length > 0){
				configFilePath = args[0];
			}
			
			ViadeoUserSettingsIO settingsIO = new ViadeoUserSettingsIO();
			settingsIO.setConfigFilePath(configFilePath);
			
			ViadeoUserSettings userSettings = settingsIO.readScrappingSettings();

			if(userSettings.getViadeoLogin() == null || "".equals(userSettings.getViadeoLogin()) 
					|| userSettings.getViadeoPassword() == null || "".equals(userSettings.getViadeoPassword())){
				throw new ScraperException("You must provide valid viadeoLogin and viadeoPassword.");
			}

			DefaultListableBeanFactory parentBeanFactory = new DefaultListableBeanFactory();
			parentBeanFactory.registerSingleton("userSettings", userSettings);
			parentBeanFactory.registerSingleton("settingsIO", settingsIO);
			GenericApplicationContext parentContext = new GenericApplicationContext(parentBeanFactory);
			parentContext.refresh();
			
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"spring.xml"}, parentContext);
			
			ScraperSystemFilesFactory systemFilesFactory = context.getBean("systemFilesFactory", ScraperSystemFilesFactory.class);
			
			CSVFileIO csvIOReport = context.getBean("cSVFileWriterKeywordReport", CSVFileIO.class);
			csvIOReport.setFile(systemFilesFactory.getKeyWordsReportFile());
			
			CSVFileIO csvIOResult = context.getBean("cSVFileWriterResult", CSVFileIO.class);
			csvIOResult.setFile(systemFilesFactory.getResultFile());
			
			GUIViadeoScraperLauncher launcher = context.getBean("guiLauncher", GUIViadeoScraperLauncher.class);
			launcher.launch(configFilePath);
			
			context.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
}