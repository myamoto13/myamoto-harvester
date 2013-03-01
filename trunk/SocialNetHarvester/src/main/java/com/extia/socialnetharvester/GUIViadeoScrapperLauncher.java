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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.extia.socialnetharvester.http.viadeo.ViadeoScraper;
import com.extia.socialnetharvester.io.ViadeoUserSettingsReader;
import com.extia.socialnetharvester.ui.scrapper.GUIViadeoScrapper;
import com.extia.socialnetharvester.ui.setting.GUIViadeoScrappingSettings;

public class GUIViadeoScrapperLauncher {
	
	static Logger logger = Logger.getLogger(GUIViadeoScrapperLauncher.class);
	
	private GUIViadeoScrappingSettings guiSettings;
	private GUIViadeoScrapper guiViadeoScraper;

	public void launch(String configFilePath) throws Exception{
		ViadeoUserSettingsReader settingReader = new ViadeoUserSettingsReader();
		settingReader.setConfigFilePath(configFilePath);
		
		ViadeoScraper viadeoScraper = new ViadeoScrapperLauncher().initViadeoScraper(configFilePath);
		
		guiSettings = new GUIViadeoScrappingSettings();
		
		guiViadeoScraper = new GUIViadeoScrapper();
		guiViadeoScraper.setGuiSettings(guiSettings);
		
		//TODO si fichier non spécifié, permettre de le spécifier dans l'UI
		guiViadeoScraper.setViadeoScraper(viadeoScraper);
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		JPanel contentPane = new JPanel(new GridBagLayout()){
			protected void paintComponent(Graphics g) {
				Color color1 = new Color(231, 248, 252);
				Color color2 = new Color(17, 156, 190);
				
				GradientPaint gradient = new GradientPaint(new Point(0, 0), color1, new Point(0, getHeight()), color2);
				
				((Graphics2D)g).setPaint(gradient);
				((Graphics2D)g).fill(getBounds());
			}
		};
		contentPane.add(guiViadeoScraper.getUI(), new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		JFrame frame = new JFrame();
		frame.setSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(contentPane);
		frame.setVisible(true);
	}
	
	
	
	public static void main(String[] args) {
		try {
			String configFilePath = null;
			if(args != null){
				if(args.length > 0){
					configFilePath = args[0];
				}
			}
			
			GUIViadeoScrapperLauncher launcher = new GUIViadeoScrapperLauncher();
			launcher.launch(configFilePath);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
}