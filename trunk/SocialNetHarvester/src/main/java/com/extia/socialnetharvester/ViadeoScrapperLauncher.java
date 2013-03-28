package com.extia.socialnetharvester;
import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.extia.socialnetharvester.http.viadeo.ViadeoScraper;

public class ViadeoScrapperLauncher {
	
	static Logger logger = Logger.getLogger(ViadeoScrapperLauncher.class);
	
	@Resource(name="viadeoScraper")
	private ViadeoScraper viadeoScraper;
	
	
	
	public ViadeoScraper getViadeoScraper() {
		return viadeoScraper;
	}

	public void setViadeoScraper(ViadeoScraper viadeoScraper) {
		this.viadeoScraper = viadeoScraper;
	}

	public void launch(String configFilePath) throws Exception{
//		ViadeoScraper viadeoScraper = scraperBuilder.build(configFilePath);

		viadeoScraper.connectToViadeo();
		viadeoScraper.scrapUnfinishedHistoryDatas();
		viadeoScraper.scrapDatas(viadeoScraper.getKeyWordList());

	}

	public static void main(String[] args) {
		try {
			String configFilePath = null;
			if(args != null){
				if(args.length > 0){
					configFilePath = args[0];
				}
			}

			ViadeoScrapperLauncher launcher = new ViadeoScrapperLauncher();
			launcher.launch(configFilePath);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}