package com.extia.webscraper.io;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jsoup.Connection.Method;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.extia.webscraper.data.ScrappingHistory;
import com.extia.webscraper.data.UrlConnectionWrapper;
import com.extia.webscraper.exception.ScrapperException;
import com.extia.webscraper.system.ScraperSystemFilesFactory;
import com.extia.webscraper.system.ScrappingSettings;

public class ScrappingHistoryXmlIO {
	
	private ScraperSystemFilesFactory systemFilesFactory;
	private ScraperSystemFilesFactory getSystemFilesFactory() {
		return systemFilesFactory;
	}

	public void setSystemFilesFactory(ScraperSystemFilesFactory systemFilesFactory) {
		this.systemFilesFactory = systemFilesFactory;
	}

	private ScrappingHistory getViadeoConnectionWrapper(Document document) throws ParserConfigurationException, XPathExpressionException, ParseException {
		ScrappingHistory result = null;

		Node rootEle = getFirstElementByTagName(document, "ScrappingHistory");
		if(rootEle != null){
			
			result = new ScrappingHistory();
			result.setDate(getDateFormat().parse(getAttributeValue((Element)rootEle, "date")));
			
			NodeList searchConnectionNodeList = getNodeList(document, "//ScrappingHistory/ViadeoSearch");
			for (int indexSearchCon = 0; indexSearchCon < searchConnectionNodeList.getLength(); indexSearchCon++) {
				
				Node searchConnectionNode = searchConnectionNodeList.item(indexSearchCon);
				
				UrlConnectionWrapper searchConnection = new UrlConnectionWrapper();

				searchConnection.setUrl(getAttributeValue((Element)searchConnectionNode, "url"));
				searchConnection.setUserAgent(getAttributeValue((Element)searchConnectionNode, "userAgent"));
				searchConnection.setReferer(getAttributeValue((Element)searchConnectionNode, "referer"));
				searchConnection.setMethod(Method.valueOf(getAttributeValue((Element)searchConnectionNode, "method")));
				searchConnection.setScrapped("true".equals(getAttributeValue((Element)searchConnectionNode, "scrapped")));
				
				
				int timeOut = 10000;
				try{
					timeOut = (int)Float.parseFloat(getAttributeValue((Element)searchConnectionNode, "timeout"));
				}catch(NumberFormatException ex){
					throw ex;
				}
				searchConnection.setTimeout(timeOut);

				NodeList postParamNodeList = getNodeList(searchConnectionNode, "PostParameterList/PostParameter");
				for (int i = 0; i < postParamNodeList.getLength(); i++) {
					Element element = (Element)postParamNodeList.item(i);
					searchConnection.putPostParameter(element.getAttribute("name"), element.getAttribute("value"));
				}


				NodeList cookieNodeList = getNodeList(searchConnectionNode, "CookieList/Cookie");
				for (int i = 0; i < cookieNodeList.getLength(); i++) {
					Element element = (Element)cookieNodeList.item(i);
					searchConnection.putCookie(element.getAttribute("name"), element.getAttribute("value"));
				}
				
				result.addSearchConnection(searchConnection);
				
			}
		}

		return result;
	}

	private NodeList getNodeList(Object element, String xPathExp) throws XPathExpressionException {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expr = xpath.compile(xPathExp);
		Object obj = expr.evaluate(element, XPathConstants.NODESET);
		return obj instanceof NodeList ? (NodeList)obj : null;
	}

	private String getAttributeValue(Element element, String attrName) {
		String result = null;
		if(element != null && attrName != null){
			result = element.getAttribute(attrName);
		}
		return result;
	}

	private Node getFirstElementByTagName(Document doc, String tagName) {
		Node result = null;
		if(doc != null && tagName != null){
			NodeList nodeList = doc.getElementsByTagName(tagName);
			if(nodeList != null && nodeList.getLength() > 0){
				result = nodeList.item(0);
			}
		}
		return result;
	}
	
	private DateFormat getDateFormat(){
		return new SimpleDateFormat("yyyy-MM-dd");
	}

	private Document getXmlDocument(ScrappingHistory scrappingHistory) throws ParserConfigurationException {
		Document result = null;
		if(scrappingHistory != null){
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			result = docBuilder.newDocument();
			Element rootElement = result.createElement("ScrappingHistory");
			addAttribute(result, rootElement, "date", getDateFormat().format(scrappingHistory.getDate()));
			
			result.appendChild(rootElement);
			
			for(UrlConnectionWrapper searchConnection : scrappingHistory.getSearchConnectionList()){

				Element viadeoSearchEle = result.createElement("ViadeoSearch");
				rootElement.appendChild(viadeoSearchEle);

				addAttribute(result, viadeoSearchEle, "scrapped", searchConnection.isScrapped() ? "true" : "false");
				addAttribute(result, viadeoSearchEle, "url", searchConnection.getUrl());
				addAttribute(result, viadeoSearchEle, "userAgent", searchConnection.getUserAgent());
				addAttribute(result, viadeoSearchEle, "referer", searchConnection.getReferer());
				addAttribute(result, viadeoSearchEle, "timeout", "" + searchConnection.getTimeout());
				addAttribute(result, viadeoSearchEle, "method", "" + searchConnection.getMethod().toString());
				
				Element postParamListEle = result.createElement("PostParameterList");
				viadeoSearchEle.appendChild(postParamListEle);
				for(String paramName : searchConnection.getPostParameterMap().keySet()){
					String paramValue = searchConnection.getPostParameterMap().get(paramName);

					Element postParamEle = result.createElement("PostParameter");

					addAttribute(result, postParamEle, "name", paramName);
					addAttribute(result, postParamEle, "value", paramValue);

					postParamListEle.appendChild(postParamEle);
				}

				Element cookieListEle = result.createElement("CookieList");
				viadeoSearchEle.appendChild(cookieListEle);
				for(String cookieName : searchConnection.getCookies().keySet()){
					String cookieValue = searchConnection.getCookies().get(cookieName);

					Element cookieEle = result.createElement("Cookie");

					addAttribute(result, cookieEle, "name", cookieName);
					addAttribute(result, cookieEle, "value", cookieValue);

					cookieListEle.appendChild(cookieEle);
				}
			}
		}
		return result;
	}

	private void addAttribute(Document doc, Element rootElement, String attrName, String attrValue) {
		if(doc != null && rootElement != null && attrName != null){
			Attr attr = doc.createAttribute(attrName);
			attr.setValue(attrValue);
			rootElement.setAttributeNode(attr);
		}
	}
	
	private File getHistoryFile() throws ScrapperException{
		return new File(getSystemFilesFactory().getHistoryDir(), getHistoryFileName());
	}
	
	private String getHistoryFileName(){
		return "History_" + getDateFormat().format(new Date())+".xml";
	}
	
	public ScrappingHistory readScrappingHistory() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, ParseException, ScrapperException {
		ScrappingHistory result = null;
		File historyFile = getHistoryFile();
		if(historyFile.exists()){

			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();


			Document document = builder.parse(historyFile);
			result = getViadeoConnectionWrapper(document);
		}
		return result;
	}
	
	public boolean saveHistory(ScrappingHistory scrappingHistory) throws TransformerException, ParserConfigurationException, ScrapperException{
		boolean result = false;
		Document document = getXmlDocument(scrappingHistory);
		File historyFile = getHistoryFile();
		if(document != null && historyFile.getParentFile() != null && historyFile.getParentFile().exists()){
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);

			transformer.transform(source, new StreamResult(getHistoryFile()));
			result = true;
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		try{
			
			UrlConnectionWrapper viadeoConWra = new UrlConnectionWrapper();
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
			
			ScrappingHistory scrappingHistory = new ScrappingHistory();
			scrappingHistory.setDate(new Date());
			scrappingHistory.addSearchConnection(viadeoConWra);
			scrappingHistory.addSearchConnection(viadeoConWra2);
			
			ScrappingSettings scrappingSettings = new ScrappingSettings();
			
			ScraperSystemFilesFactory systemFilesFactory = new ScraperSystemFilesFactory();
			systemFilesFactory.setScrappingSettings(scrappingSettings);
			
			ScrappingHistoryXmlIO xmlHistory = new ScrappingHistoryXmlIO();
			xmlHistory.setSystemFilesFactory(systemFilesFactory);
			xmlHistory.saveHistory(scrappingHistory);
			

			ScrappingHistory scrappingHistory2 = xmlHistory.readScrappingHistory();
			
			System.out.println(scrappingHistory.getSearchConnectionList().equals(scrappingHistory2.getSearchConnectionList()));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
