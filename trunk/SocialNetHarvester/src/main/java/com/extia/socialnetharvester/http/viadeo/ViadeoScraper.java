package com.extia.socialnetharvester.http.viadeo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import com.extia.socialnetharvester.ScraperException;
import com.extia.socialnetharvester.data.ScrapingHistory;
import com.extia.socialnetharvester.data.UrlConnectionWrapper;
import com.extia.socialnetharvester.data.ViadeoPerson;
import com.extia.socialnetharvester.http.viadeo.avancement.AvancementManager;
import com.extia.socialnetharvester.io.FileIO;
import com.extia.socialnetharvester.io.ScrappingHistoryXmlIO;
import com.extia.socialnetharvester.io.csv.CSVKeywordReportIO;
import com.extia.socialnetharvester.io.csv.CSVPersonListIO;

public class ViadeoScraper {
	
	private static Logger logger = Logger.getLogger(ViadeoScraper.class);

	private Map<String,String> cookies;
	
	@Resource(name="viadeoProperties")
	private ViadeoProperties viadeoProperties;
	
	@Resource(name="scrappingHistoryXmlIO")
	private ScrappingHistoryXmlIO scrappingHistoryXml;
	private ScrapingHistory history;
	
	@Resource(name="userSettings")
	private ViadeoUserSettings scrappingSettings;
	private Set<String> nameSet;
	private List<ScrapingProgressListener> scrapingProgressListenerList;
	private Map<UrlConnectionWrapper, Document> domBufferMap;
	
	@Resource(name="systemFilesFactory")
	private ScraperSystemFilesFactory systemFilesFactory;
	
	@Resource(name="keywordListFileIO")
	private FileIO keywordListFileIO;

	@Resource(name="cSVKeywordReportIO")
	private CSVKeywordReportIO cSVKeywordReportIO;
	
	@Resource(name="cSVWriterViadeoPersonList")
	private CSVPersonListIO cSVWriterResult;
	
	private boolean interruptFlag;
	
	private AvancementManager avancementManager;

	public AvancementManager getAvancementManager() {
		if(avancementManager == null){
			avancementManager = new AvancementManager();
		}
		return avancementManager;
	}

	public ViadeoScraper() {
		scrapingProgressListenerList = new ArrayList<ViadeoScraper.ScrapingProgressListener>();
	}
	
	private FileIO getKeywordListFileIO() {
		return keywordListFileIO;
	}

	public void setKeywordListFileIO(FileIO keywordListFileIO) {
		this.keywordListFileIO = keywordListFileIO;
	}
	
	private CSVKeywordReportIO getcSVKeywordReportIO() {
		return cSVKeywordReportIO;
	}

	public void setcSVKeywordReportIO(CSVKeywordReportIO cSVKeywordReportIO) {
		this.cSVKeywordReportIO = cSVKeywordReportIO;
	}

	private CSVPersonListIO getcSVWriterResult() {
		return cSVWriterResult;
	}

	public void setcSVWriterResult(CSVPersonListIO cSVWriterResult) {
		this.cSVWriterResult = cSVWriterResult;
	}

	public boolean addScrapingProgressListener(ScrapingProgressListener scrapingProgressListener){
		return scrapingProgressListenerList.add(scrapingProgressListener);
	}

	public boolean removeScrapingProgressListener(ScrapingProgressListener scrapingProgressListener){
		return scrapingProgressListenerList.remove(scrapingProgressListener);
	}

	private ScrappingHistoryXmlIO getScrappingHistoryXml() {
		return scrappingHistoryXml;
	}
	
	private ScraperSystemFilesFactory getSystemFilesFactory() {
		return systemFilesFactory;
	}

	public void setSystemFilesFactory(ScraperSystemFilesFactory systemFilesFactory) {
		this.systemFilesFactory = systemFilesFactory;
	}

	private int getMaximumScrapableResults(){
		return getViadeoProperties().getMaximumSearchResults();
	}
	
	private Map<UrlConnectionWrapper, Document> getDomBufferMap(){
		if(domBufferMap == null){
			domBufferMap = new HashMap<UrlConnectionWrapper, Document>();
		}
		return domBufferMap;
	}

	private ScrapingHistory getHistory() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, ParseException, TransformerException, ScraperException {
		if(history == null){
			logger.info("loading history..");
			history = getScrappingHistoryXml().readScrappingHistory();

			if(history == null){
				history = new ScrapingHistory();
				history.setDate(new Date());
				getScrappingHistoryXml().saveHistory(history);
			}
			logger.info("history loaded.");
		}
		return history;
	}

	private ViadeoProperties getViadeoProperties(){
		return viadeoProperties;
	}

	public void setViadeoProperties(ViadeoProperties viadeoProperties) {
		this.viadeoProperties = viadeoProperties;
	}


	private Map<String, String> getCookies() {
		return cookies;
	}

	private void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	private String getViadeoUrl() {
		return getViadeoProperties().getViadeoUrl();
	}

	private List<String> getThemesDisjointsList() {
		return getViadeoProperties().getThemesDisjointsList();
	}

	public void setScrappingHistoryXml(ScrappingHistoryXmlIO scrappingHistoryXml) {
		this.scrappingHistoryXml = scrappingHistoryXml;
	}

	private ViadeoUserSettings getScrappingSettings() {
		return scrappingSettings;
	}

	public void setScrappingSettings(ViadeoUserSettings scrappingSettings) {
		this.scrappingSettings = scrappingSettings;
	}


	public void connectToViadeo() throws IOException, InterruptedException {
		int requestsTimeout = getRequestTimeout();

		String viadeoURL = getViadeoUrl();
		logger.info("logging in...");
		Response res = Jsoup.connect(viadeoURL)
		.userAgent(getUserAgent())
		.referrer(viadeoURL)
		.timeout(requestsTimeout)
		.method(Method.GET).execute();
		Thread.sleep(getHttpCallsDelay());

		setCookies(res.cookies());
		
		res = Jsoup.connect(getViadeoProperties().getViadeoSigninUrl())
		.cookies(getCookies())
		.userAgent(getUserAgent())
		.referrer(viadeoURL)
		.data("email", getScrappingSettings().getViadeoLogin(), "password", getScrappingSettings().getViadeoPassword())
		.timeout(requestsTimeout)
		.method(Method.POST).execute();
		Thread.sleep(getHttpCallsDelay());

		setCookies(res.cookies());

		logger.info("logged.");
	}

	public void scrapUnfinishedHistoryDatas() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, ParseException, TransformerException, ScraperException, InterruptedException {
		List<UrlConnectionWrapper> historyList = getHistory().getSearchConnectionList();


		logger.info("checking history... " + historyList.size() + " url to scrap found.");
		if(historyList.size() > 0){
			List<UrlConnectionWrapper> urlConnexionToScrapList = new ArrayList<UrlConnectionWrapper>();
			for (UrlConnectionWrapper connectionWra : historyList) {
				if (!connectionWra.isScrapped()){
					urlConnexionToScrapList.add(connectionWra);
				}
			}
			logger.info("resuming scrapping from history...");
			scrapSearchPageList(urlConnexionToScrapList);
		}
	}

	public List<String> getKeyWordList() throws IOException {
		return getKeywordListFileIO().read();
	}

	public void scrapDatas(List<String> keyWordList) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException, ParseException, TransformerException, ScraperException, InterruptedException {
		if(keyWordList != null){
			fireScrapingProgressUpdated(0);
			getcSVKeywordReportIO().writeTitle();

			//retrieve result number for each keyWord
			//TODO retrieve it from History ?
			Map<String, List<UrlConnectionWrapper>> urlsForKeywordsMap = new HashMap<String, List<UrlConnectionWrapper>>();
			int indexKeywords = 0;
			getDomBufferMap().clear();
			getAvancementManager().clear();
			for (String keyWords : keyWordList) {
				fireScrapingProgressUpdated(0);

				if(isInterruptFlag()){
					logger.info("interuption called. Stopping search for keywords (" + keyWordList.size() + ").");
					break;
				}else{
					if(keyWords != null && !"".equals(keyWords) && urlsForKeywordsMap.get(keyWords) == null){
						logger.info("searching urls for keywords '" + keyWords + "'");

						fireScrapingKeyWordsStarted(keyWords);

						fireScrapingProgressUpdated(Math.round(((float)indexKeywords / (float)keyWordList.size()) * 100));
						
						UrlConnectionWrapper viadeoConWra = new UrlConnectionWrapper();
						viadeoConWra.setUrl(getViadeoProperties().getViadeoSearchUrl());
						viadeoConWra.setUserAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
						viadeoConWra.setReferer(getViadeoUrl());
						viadeoConWra.setTimeout(getRequestTimeout());
						viadeoConWra.addCookies(getCookies());
						viadeoConWra.putPostParameter("ga_from", "Tfrom:search-members;Bfrom:default-form;Efrom:;");
						viadeoConWra.putPostParameter("fullName", "");
						viadeoConWra.putPostParameter("keywords", "\"" + keyWords + "\"");
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

						ScrapingHistory scrapingHistory = getHistory();

						List<UrlConnectionWrapper> urlConnexionToScrapList = getSearchUrlList(viadeoConWra);
						if(urlConnexionToScrapList != null) {

							int nbResultsRetrieved = 0;
							for (UrlConnectionWrapper urlConWra : urlConnexionToScrapList) {
								Document dom = getDomBufferMap().get(urlConWra);
								Elements h1NbElementsResult = dom.select("div[class=searchResultsTitleWrapper pal] h1[class=fl mrs]");
								int nbResultsSubConnec = getInteger(h1NbElementsResult.text());
								nbResultsRetrieved += Math.min(nbResultsSubConnec, getMaximumScrapableResults());

							}

							Connection connection = viadeoConWra.getConnection(getCookies());
							Document dom = connection.execute().parse();
							Thread.sleep(getHttpCallsDelay());

							Elements h1NbElements = dom.select("div[class=searchResultsTitleWrapper pal] h1[class=fl mrs]");
							int nbResults = getInteger(h1NbElements.text());

							logger.debug(nbResultsRetrieved + " results out of " + nbResults + " will be retrieved For keywords : '" + keyWords + "'");
							getAvancementManager().add(keyWords, nbResultsRetrieved, 0);
							
							/*
							 * TODO 
							 * for each parent URL, add the last child URL for which scraping started.
							 * for now, history will make the search start from the last keyword being searched.
							 * It'd be better to start from the last page.
							 */
							for (Iterator<UrlConnectionWrapper> it = urlConnexionToScrapList.iterator(); it.hasNext();) {
								UrlConnectionWrapper conWra = it.next();
								int index = scrapingHistory.getSearchConnectionList().indexOf(conWra);

								if (index > -1 && scrapingHistory.getSearchConnectionList().get(index).isScrapped()){
									it.remove();
								}else{
									scrapingHistory.addSearchConnection(conWra);
								}
							}
							getScrappingHistoryXml().saveHistory(scrapingHistory);
							getcSVKeywordReportIO().writeLine(keyWords, "" + nbResultsRetrieved, "" + nbResults);

							/*
							 * before scraping, set total of people to scrap for this search to keep track of progress.
							 */
							urlsForKeywordsMap.put(keyWords, urlConnexionToScrapList);
						}
						indexKeywords++;
					}
				}
			}
			fireScrapingProgressUpdated(100);

			//Scrap each keyword
			fireScrapingProgressUpdated(0);
			for (String keyWords : keyWordList) {
				if(isInterruptFlag()){
					logger.info("interuption called. Stopping scraping for keywords (" + keyWordList.size() + ").");
					break;
				}else{
					logger.info("scrapping datas for keywords '" + keyWords + "'");

					fireScrapingKeyWordsStarted(keyWords);

					scrapSearchPageList(urlsForKeywordsMap.get(keyWords));
				}
			}
			fireScrapingProgressUpdated(100);
		} 
	}

	private String getKeyWords(UrlConnectionWrapper urlConWra) throws UnsupportedEncodingException{
		String result = null;
		if(urlConWra != null){
			result = urlConWra.getPostParameterValue("keywords");
			if(result == null){
				String url =  URLDecoder.decode(urlConWra.getUrl(), "UTF-8");
				String regEx = "(.*keywords=)([^\\&]*)(\\&.*)";
				result = url.replaceAll(regEx, "$2");
			}
			if(result != null){
				result = result.replace("\"" , "");
			}
		}
		return result;
	}

	private List<UrlConnectionWrapper> getSearchUrlList(UrlConnectionWrapper viadeoConWra) throws InterruptedException, IOException {
		List<UrlConnectionWrapper> result = new ArrayList<UrlConnectionWrapper>();
		if(viadeoConWra != null){
			if(isInterruptFlag()){
				logger.info("interuption called. Stopping URL optimization.");
			}else{
				Document dom = viadeoConWra.getConnection(getCookies()).execute().parse();
				Thread.sleep(getHttpCallsDelay());

				Elements h1NbElements = dom.select("div[class=searchResultsTitleWrapper pal] h1[class=fl mrs]");

				int nbResults = getInteger(h1NbElements.text());

				List<String> themeDisjointsList = getThemesDisjointsList();

				logger.debug("found " + nbResults + " results.");

				boolean addUrl = false;

				if(nbResults > 0){
					if(nbResults > getMaximumScrapableResults()){
						Elements divFacetList = dom.select("div[class=facet phm pvs]");
						logger.debug("divFacetList : " + divFacetList.size());

						String themeRetenu = null;
						int maxResultTheme = 0;
						for (Iterator<Element> divFacetIterator = divFacetList.iterator(); divFacetIterator.hasNext();) {
							Element divFacet = ((Element) divFacetIterator.next()).parent();
							String theme = divFacet.select("p[class^=facet-title open mbxs phm pvs]").text();

							if(themeDisjointsList.contains(theme)){
								Elements liList = divFacet.select("ul > li");
								int resultNumber = 0;
								for (Iterator<Element> liIterator = liList.iterator(); liIterator.hasNext();) {
									Element liElement = (Element) liIterator.next();
									resultNumber += getInteger(liElement.select("small[class=number phxs]").text());
								}
								if(resultNumber > maxResultTheme && resultNumber > getMaximumScrapableResults()){
									maxResultTheme = resultNumber;
									themeRetenu = theme;
								}
							}
						}
						logger.debug("Thème retenu pour " + nbResults + " : " + themeRetenu);
						if(themeRetenu != null){
							for (Iterator<Element> divFacetIterator = divFacetList.iterator(); divFacetIterator.hasNext();) {
								Element divFacet = ((Element) divFacetIterator.next()).parent();
								String themeLabel = divFacet.select("p[class^=facet-title open mbxs phm pvs]").text();

								if(themeRetenu.equals(themeLabel)){
									Elements liList = divFacet.select("ul > li");
									for (Iterator<Element> liIterator = liList.iterator(); liIterator.hasNext();) {
										Element liElement = (Element) liIterator.next();
										String href = liElement.select("a").attr("abs:href");

										if(href != null && !"".equals(href)){
											UrlConnectionWrapper subViadeoConWra = new UrlConnectionWrapper();
											subViadeoConWra.setUrl(href);
											subViadeoConWra.setTimeout(getRequestTimeout());
											subViadeoConWra.addCookies(getCookies());
											subViadeoConWra.setUserAgent(getUserAgent());
											subViadeoConWra.setReferer(getViadeoUrl());

											result.addAll(getSearchUrlList(subViadeoConWra));
										}
									}
								}
							}
						}else{
							addUrl = true;
						}
					}else{
						addUrl = true;
					}

					if(addUrl){
						getDomBufferMap().put(viadeoConWra, dom);
						result.add(viadeoConWra);
					}
				}
			}
		}
		return result;
	}


	private int getInteger(String text) {
		int result = 0;
		if(text != null){
			try{
				result = (int)Float.parseFloat(text.replaceAll("([^0-9])", ""));
			}catch(Exception ex){}
		}
		return result;
	}

	private void scrapSearchPageList(List<UrlConnectionWrapper> searchUrlConnexionToScrapList) throws IOException, ScraperException, InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException, ParseException, TransformerException {
		String resultFilePath =  getSystemFilesFactory().getResultFilePath();
		if(searchUrlConnexionToScrapList != null && resultFilePath != null){

			if(getcSVWriterResult().isEmptyFile()){
				getcSVWriterResult().writeTitle();
			}

			for (UrlConnectionWrapper searchUrlConWra : searchUrlConnexionToScrapList) {
				if(isInterruptFlag()){
					logger.info("interuption called, stopping list scrapping for URL list.");
					break;
				}else {

					UrlConnectionWrapper urlConWra = searchUrlConWra;
					String keyWords = getKeyWords(searchUrlConWra);
					do{
						Thread.sleep(getHttpCallsDelay());

						if(isInterruptFlag()){
							logger.info("interuption called. Stopping scrapping for keyWords " + keyWords + ".");
							break;
						}else{
							Response res = null;

							Connection con = urlConWra.getConnection(getCookies());

							if(getScrappingSettings().isAutoResumeScraping()){
								while(res == null){
									try{
										res = con.execute();
									}catch(Exception ex){
										//TODO : traiter différemment HTTP STATUS 401
										logger.error(ex.getMessage());
									}
									if(res == null){
										Thread.sleep(getHttpCallsDelay());
									}
								}
							}else{
								res = con.execute();
							}


							Document dom = res.parse();

							List<ViadeoPerson> personList = getPersonList(dom, keyWords);

							int nbScrapedPerson = 0;
							for (ViadeoPerson person : personList) {
								if(!resultFileContains(person)){
									getNameSet().add(person.getName());
									getcSVWriterResult().writeLine(person);
									logger.debug("scrapping datas for " + person);
									nbScrapedPerson ++;
								}
							}
							
							
							getAvancementManager().incrementScrappedNumber(keyWords, nbScrapedPerson);
							
							fireScrapingProgressUpdated(getAvancementManager().getProgressPercent());
							
							if(urlConWra != searchUrlConWra){
								urlConWra.setScrapped(true);
							}

							Elements buttonNext = dom.select("p[class=numerotation] > a[class=nextPage]");

							String href = buttonNext.attr("abs:href");
							if(href != null && !"".equals(href)){
								urlConWra = new UrlConnectionWrapper();
								urlConWra.setUrl(href);
								urlConWra.setTimeout(getRequestTimeout());
								urlConWra.setUserAgent(getUserAgent());
								urlConWra.setReferer(getViadeoUrl());

								getHistory().addSearchConnection(urlConWra);

								getScrappingHistoryXml().saveHistory(getHistory());
							}else{
								urlConWra = null;
								searchUrlConWra.setScrapped(true);
							}
						}
					}while(urlConWra != null);
				}
			}
		}
	}


	private long getHttpCallsDelay() {
		return getScrappingSettings().getHttpCallsDelay();
	}

	private boolean resultFileContains(ViadeoPerson person) throws IOException, ScraperException {
		boolean result = false;
		if(person != null && person.getName() != null){
			getNameSet().contains(person.getName());
			if(result){
				List<ViadeoPerson> personList = getcSVWriterResult().read();
				result = personList != null && personList.contains(person);
			}
		}
		return result;
	}

	private List<ViadeoPerson> getPersonList(Document dom, String keyWords) {
		List<ViadeoPerson> result = null;
		if(dom != null){
			result = new ArrayList<ViadeoPerson>();
			Elements divPeopleList = dom.select("div[class^=no-prevent hcard bram partial]");
			for (Iterator<Element> iterator = divPeopleList.iterator(); iterator.hasNext();) {

				Element divPersonne = (Element) iterator.next();

				ViadeoPerson person = new ViadeoPerson();
				person.setKeywords(keyWords);
				person.setCountry(getCleanCSVString(divPersonne.select("p[class=address ptxs pbs mbn] > a[class=country]").text()));
				person.setCity(getCleanCSVString(divPersonne.select("p[class=address ptxs pbs mbn] > a[class=city]").text()));
				person.setName(getCleanCSVString(divPersonne.select("h1[class=name mbn] > a[class=profile-link]").text()));
				person.setJob(getCleanCSVString(divPersonne.select("ul[class=experiences mbxs] > li:eq(0) > a:eq(1)").text()));
				person.setCompany(getCleanCSVString(divPersonne.select("ul[class=experiences mbxs] > li:eq(0) > a:eq(2)").text()));
				person.setPreviousJob(getCleanCSVString(divPersonne.select("ul[class=experiences mbxs] > li:eq(1) > a:eq(1)").text()));
				person.setPreviousCompany(getCleanCSVString(divPersonne.select("ul[class=experiences mbxs] > li:eq(1) > a:eq(2)").text()));
				person.setOverview("");
				/*
				 * TODO : sur Firefox, l'élément est défini mais pas sur les pages chargées automatiquement : "div[class=details pvs prs cf] p"
				 */
				String profileLink = getCleanCSVString(divPersonne.select("h1[class=name mbn] > a[class=profile-link]").attr("abs:href"));
				profileLink = profileLink.replaceAll("([^\\?])(\\?.*)", "$1");
				person.setProfileLink(profileLink);

				result.add(person);
			}
		}
		return result;
	}

	private Set<String> getNameSet() throws IOException, ScraperException {
		if(nameSet == null){
			nameSet = new HashSet<String>();
			List<ViadeoPerson> personList = getcSVWriterResult().read();
			for (ViadeoPerson person : personList) {
				nameSet.add(person.getName());
			}
		}
		return nameSet;
	}

	private String getCleanCSVString(String string) {
		return string != null ? string.replaceAll("(\\t|\\s+|(\\r?\\n|\\r))", " ").replaceAll(";", ",") : null;
	}

	private String getUserAgent() {
		return getScrappingSettings().getUserAgent();
	}

	private int getRequestTimeout() {
		return getScrappingSettings().getTimeout();
	}

	public void setInteruptFlag(boolean interruptFlag) {
		this.interruptFlag = interruptFlag;
	}

	public boolean isInterruptFlag() {
		return interruptFlag;
	}

	public void setInterruptFlag(boolean interruptFlag) {
		this.interruptFlag = interruptFlag;
	}

	public String getKeyWordFilePath() {
		return getScrappingSettings() != null ? getScrappingSettings().getKeyWordListFilePath() : null;
	}
	
	private void fireScrapingProgressUpdated(int progress){
		for (ScrapingProgressListener scrapingProgressListener : scrapingProgressListenerList) {
			scrapingProgressListener.progressUpdated(progress);
		}
	}

	private void fireScrapingKeyWordsStarted(String keyWords) {
		for (ScrapingProgressListener scrapingProgressListener : scrapingProgressListenerList) {
			scrapingProgressListener.fireScrapingKeyWordsStarted(keyWords);
		}
	}
	
	public interface ScrapingProgressListener{
		void progressUpdated(int progress);
		void fireScrapingKeyWordsStarted(String keyWords);
	}
	
}
