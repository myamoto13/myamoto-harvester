package com.extia.socialnetharvester.business;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
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

import org.apache.log4j.Logger;
import org.jsoup.Connection.Method;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.extia.socialnetharvester.business.data.ScrapingHistory;
import com.extia.socialnetharvester.business.data.UrlConnectionWrapper;

public class ScrapingHistoryXmlIO {
	
	private static Logger logger = Logger.getLogger(ScrapingHistoryXmlIO.class);
	
	private static final String PROPERTY_URL = "url";
	private static final String PROPERTY_DATE = "date";
	private static final String PROPERTY_USERAGENT = "userAgent";
	private static final String PROPERTY_REFERER = "referer";
	private static final String PROPERTY_METHOD = "method";
	private static final String PROPERTY_TIMEOUT = "timeout";
	
	private static final String PROPERTY_NAME = "name";
	private static final String PROPERTY_VALUE = "value";
	
	@Resource(name="systemFilesFactory")
	private ScraperSystemFilesFactory systemFilesFactory;
	
	@Resource(name="userSettings")
	private ViadeoUserSettings userSettings;
	
	public void setUserSettings(ViadeoUserSettings scrappingSettings) {
		this.userSettings = scrappingSettings;
	}

	private ScraperSystemFilesFactory getSystemFilesFactory() {
		return systemFilesFactory;
	}

	public void setSystemFilesFactory(ScraperSystemFilesFactory systemFilesFactory) {
		this.systemFilesFactory = systemFilesFactory;
	}
	
	private ViadeoUserSettings getUserSettings() {
		return userSettings;
	}

	private int getRequestTimeout() {
		return getUserSettings().getTimeout();
	}

	private ScrapingHistory getViadeoConnectionWrapper(Document document) throws ParserConfigurationException, XPathExpressionException, ParseException {
		ScrapingHistory result = null;

		Node rootEle = getFirstElementByTagName(document, "ScrappingHistory");
		if(rootEle != null){
			
			result = new ScrapingHistory();
			result.setDate(getDateFormat().parse(getAttributeValue((Element)rootEle, PROPERTY_DATE)));
			
			NodeList searchConnectionNodeList = getNodeList(document, "//ScrappingHistory/ViadeoSearch");
			for (int indexSearchCon = 0; indexSearchCon < searchConnectionNodeList.getLength(); indexSearchCon++) {
				
				Node searchConnectionNode = searchConnectionNodeList.item(indexSearchCon);
				
				UrlConnectionWrapper searchConnection = new UrlConnectionWrapper();
				searchConnection.setUrl(getAttributeValue((Element)searchConnectionNode, PROPERTY_URL));
				searchConnection.setUserAgent(getAttributeValue((Element)searchConnectionNode, PROPERTY_USERAGENT));
				searchConnection.setReferer(getAttributeValue((Element)searchConnectionNode, PROPERTY_REFERER));
				searchConnection.setMethod(Method.valueOf(getAttributeValue((Element)searchConnectionNode, PROPERTY_METHOD)));
				searchConnection.setScrapped(Boolean.valueOf((getAttributeValue((Element)searchConnectionNode, "scrapped"))));
				
				
				int timeOut = getRequestTimeout();
				try{
					timeOut = (int)Float.parseFloat(getAttributeValue((Element)searchConnectionNode, PROPERTY_TIMEOUT));
				}catch(NumberFormatException ex){
					logger.error(ex.getMessage(), ex);
				}
				searchConnection.setTimeout(timeOut);

				NodeList postParamNodeList = getNodeList(searchConnectionNode, "PostParameterList/PostParameter");
				for (int i = 0; i < postParamNodeList.getLength(); i++) {
					Element element = (Element)postParamNodeList.item(i);
					searchConnection.putPostParameter(element.getAttribute(PROPERTY_NAME), element.getAttribute(PROPERTY_VALUE));
				}


				NodeList cookieNodeList = getNodeList(searchConnectionNode, "CookieList/Cookie");
				for (int i = 0; i < cookieNodeList.getLength(); i++) {
					Element element = (Element)cookieNodeList.item(i);
					searchConnection.putCookie(element.getAttribute(PROPERTY_NAME), element.getAttribute(PROPERTY_VALUE));
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

	private Document getXmlDocument(ScrapingHistory scrappingHistory) throws ParserConfigurationException {
		Document result = null;
		if(scrappingHistory != null){
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			result = docBuilder.newDocument();
			Element rootElement = result.createElement("ScrappingHistory");
			addAttribute(result, rootElement, PROPERTY_DATE, getDateFormat().format(scrappingHistory.getDate()));
			
			result.appendChild(rootElement);
			
			for(UrlConnectionWrapper searchConnection : scrappingHistory.getSearchConnectionList()){

				Element viadeoSearchEle = result.createElement("ViadeoSearch");
				rootElement.appendChild(viadeoSearchEle);

				addAttribute(result, viadeoSearchEle, "scrapped", searchConnection.isScrapped() ? "true" : "false");
				addAttribute(result, viadeoSearchEle, PROPERTY_URL, searchConnection.getUrl());
				addAttribute(result, viadeoSearchEle, PROPERTY_USERAGENT, searchConnection.getUserAgent());
				addAttribute(result, viadeoSearchEle, PROPERTY_REFERER, searchConnection.getReferer());
				addAttribute(result, viadeoSearchEle, PROPERTY_TIMEOUT, "" + searchConnection.getTimeout());
				addAttribute(result, viadeoSearchEle, PROPERTY_METHOD, "" + searchConnection.getMethod().toString());
				
				Element postParamListEle = result.createElement("PostParameterList");
				viadeoSearchEle.appendChild(postParamListEle);
				for(String paramName : searchConnection.getPostParameterMap().keySet()){
					String paramValue = searchConnection.getPostParameterMap().get(paramName);

					Element postParamEle = result.createElement("PostParameter");

					addAttribute(result, postParamEle, PROPERTY_NAME, paramName);
					addAttribute(result, postParamEle, PROPERTY_VALUE, paramValue);

					postParamListEle.appendChild(postParamEle);
				}

				Element cookieListEle = result.createElement("CookieList");
				viadeoSearchEle.appendChild(cookieListEle);
				for(String cookieName : searchConnection.getCookies().keySet()){
					String cookieValue = searchConnection.getCookies().get(cookieName);

					Element cookieEle = result.createElement("Cookie");

					addAttribute(result, cookieEle, PROPERTY_NAME, cookieName);
					addAttribute(result, cookieEle, PROPERTY_VALUE, cookieValue);

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
	
	private File getHistoryFile() throws ScraperException{
		return new File(getSystemFilesFactory().getHistoryDir(), getHistoryFileName());
	}
	
	private String getHistoryFileName(){
		return "History_" + getDateFormat().format(new Date())+".xml";
	}
	
	public ScrapingHistory readScrappingHistory() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, ParseException, ScraperException {
		ScrapingHistory result = null;
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
	
	public boolean saveHistory(ScrapingHistory scrappingHistory) throws TransformerException, ParserConfigurationException, ScraperException{
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
	
}
