package com.extia.socialnetharvester;
import java.io.IOException;
import java.text.ParseException;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.extia.socialnetharvester.business.ScraperException;
import com.extia.socialnetharvester.business.ViadeoScraper;

public class ViadeoScrapperLauncher {
	
	private static Logger logger = Logger.getLogger(ViadeoScrapperLauncher.class);
	
	@Resource(name="viadeoScraper")
	private ViadeoScraper viadeoScraper;
	
	
	
	public ViadeoScraper getViadeoScraper() {
		return viadeoScraper;
	}

	public void setViadeoScraper(ViadeoScraper viadeoScraper) {
		this.viadeoScraper = viadeoScraper;
	}

	public void launch(String configFilePath) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, ParseException, TransformerException, ScraperException, InterruptedException {
		viadeoScraper.connectToViadeo();
		viadeoScraper.scrapUnfinishedHistoryDatas();
		viadeoScraper.scrapDatas(viadeoScraper.getKeyWordList());
	}

	public static void main(String[] args) {
		try {
			String configFilePath = null;
			if(args != null && args.length > 0){
				configFilePath = args[0];
			}

			ViadeoScrapperLauncher launcher = new ViadeoScrapperLauncher();
			launcher.launch(configFilePath);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}