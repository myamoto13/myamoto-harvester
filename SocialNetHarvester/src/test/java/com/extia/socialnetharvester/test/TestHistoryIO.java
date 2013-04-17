package com.extia.socialnetharvester.test;
import java.util.Date;

import org.apache.log4j.Logger;
import org.jsoup.Connection.Method;
import org.junit.Assert;
import org.junit.Test;

import com.extia.socialnetharvester.data.ScrapingHistory;
import com.extia.socialnetharvester.data.UrlConnectionWrapper;
import com.extia.socialnetharvester.http.viadeo.ScraperSystemFilesFactory;
import com.extia.socialnetharvester.http.viadeo.ViadeoUserSettings;
import com.extia.socialnetharvester.io.ScrapingHistoryXmlIO;


public class TestHistoryIO {
	
	private static Logger logger = Logger.getLogger(TestHistoryIO.class);

	@Test
	public void testHistoryIO(){
		try{
			
			UrlConnectionWrapper viadeoConWra = new UrlConnectionWrapper();
			viadeoConWra.setMethod(Method.GET);
			viadeoConWra.setUrl("http://www.viadeo.com/v/search/members");
			viadeoConWra.setUserAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
			viadeoConWra.setReferer("http://viadeo.com");
			viadeoConWra.setTimeout(10000);
			viadeoConWra.putCookie("SESSIONID", "12345");
			viadeoConWra.putPostParameter("ga_from", "Tfrom:search-members;Bfrom:default-form;Efrom:;");
			viadeoConWra.putPostParameter("fullName", "");
			viadeoConWra.putPostParameter("keywords", "'" + "J2EE" + "'");
			viadeoConWra.putPostParameter("search", "Chercher");
			viadeoConWra.putPostParameter("company", "");
			viadeoConWra.putPostParameter("companyExactSearch", "on");
			viadeoConWra.putPostParameter("position", "");
			viadeoConWra.putPostParameter("positionExactSearch", "off");
			viadeoConWra.putPostParameter("schoolName", "");
			viadeoConWra.putPostParameter("sector", "on");
			viadeoConWra.putPostParameter("town", "");
			viadeoConWra.putPostParameter("countryForm", "");
			viadeoConWra.putPostParameter("county", "");
			viadeoConWra.putPostParameter("joinDateId", "0020");
			viadeoConWra.putPostParameter("language", "");
			viadeoConWra.putPostParameter("btnRadio", "0020");
			
			UrlConnectionWrapper viadeoConWra2 = new UrlConnectionWrapper();
			viadeoConWra2.setUrl("https://secure.viadeo.com/r/account/authentication/signin");
			viadeoConWra2.setUserAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
			viadeoConWra2.setReferer("http://viadeo.com");
			viadeoConWra2.setTimeout(10000);
			viadeoConWra2.putCookie("SESSIONID", "1234567");
			viadeoConWra2.putPostParameter("email", "thonio.banderas@laposte.net");
			viadeoConWra2.putPostParameter("password", "extiapaca");
			viadeoConWra2.setMethod(Method.POST);
			
			ScrapingHistory scrappingHistory = new ScrapingHistory();
			scrappingHistory.setDate(new Date());
			scrappingHistory.addSearchConnection(viadeoConWra);
			scrappingHistory.addSearchConnection(viadeoConWra2);
			
			ViadeoUserSettings scrappingSettings = new ViadeoUserSettings();
			scrappingSettings.setWorkingDirPath(System.getProperty("java.io.tmpdir"));
			scrappingSettings.setTimeout(2000);
			
			ScraperSystemFilesFactory systemFilesFactory = new ScraperSystemFilesFactory();
			systemFilesFactory.setUserSettings(scrappingSettings);
			
			ScrapingHistoryXmlIO xmlHistory = new ScrapingHistoryXmlIO();
			xmlHistory.setUserSettings(scrappingSettings);
			xmlHistory.setSystemFilesFactory(systemFilesFactory);
			xmlHistory.saveHistory(scrappingHistory);
			

			ScrapingHistory scrappingHistory2 = xmlHistory.readScrappingHistory();
			
			Assert.assertEquals(scrappingHistory.getSearchConnectionList(), scrappingHistory2.getSearchConnectionList());
			
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
		}
	}
}
