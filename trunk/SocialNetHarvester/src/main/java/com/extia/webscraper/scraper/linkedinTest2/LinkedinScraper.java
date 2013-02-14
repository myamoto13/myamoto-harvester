package com.extia.webscraper.scraper.linkedinTest2;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.extia.webscraper.data.ScrappingHistory;
import com.extia.webscraper.data.UrlConnectionWrapper;
import com.extia.webscraper.exception.ScrapperException;
import com.extia.webscraper.io.ScrappingHistoryXmlIO;
import com.extia.webscraper.system.ScraperSystemFilesFactory;
import com.extia.webscraper.system.ScrappingSettings;
import com.extia.webscraper.system.ViadeoProperties;

/**
 * @author hp
 *
 */
public class LinkedinScraper {


	static Logger logger = Logger.getLogger(LinkedinScraper.class);

	private Map<String,String> cookies;
	private ViadeoProperties viadeoProperties;
	private ScrappingHistoryXmlIO scrappingHistoryXml;
	private ScrappingHistory history;
	private ScrappingSettings scrappingSettings;
	private HashSet<String> nameSet;
	private List<LinkedinScrapingProgressListener> scrapingProgressListenerList;
	private ScraperSystemFilesFactory systemFilesFactory;

	private boolean interruptFlag;

	public LinkedinScraper() {
		scrapingProgressListenerList = new ArrayList<LinkedinScrapingProgressListener>();
	}

	public boolean addScrapingProgressListener(LinkedinScrapingProgressListener scrapingProgressListener){
		return scrapingProgressListenerList.add(scrapingProgressListener);
	}

	public boolean removeScrapingProgressListener(LinkedinScrapingProgressListener scrapingProgressListener){
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

	private ScrappingSettings getScrappingSettings() {
		return scrappingSettings;
	}

	public void setScrappingSettings(ScrappingSettings scrappingSettings) {
		this.scrappingSettings = scrappingSettings;
	}

	/*
	 * End of POJO get/set
	 */

	public void authenticate() throws Exception{
		//		int requestsTimeout = getRequestTimeout();
		//
		//		String viadeoURL = getViadeoUrl();
		//		logger.info("logging in...");
		//		Response res = Jsoup.connect(viadeoURL)
		//		.userAgent(getUserAgent())
		//		.referrer(viadeoURL)
		//		.timeout(requestsTimeout)
		//		.method(Method.GET).execute();
		//		Thread.sleep(getHttpCallsDelay());
		//
		//		setCookies(res.cookies());
		//		
		//		res = Jsoup.connect(getViadeoProperties().getViadeoSigninUrl())
		//		.cookies(getCookies())
		//		.userAgent(getUserAgent())
		//		.referrer(viadeoURL)
		//		.data("email", getScrappingSettings().getViadeoLogin(), "password", getScrappingSettings().getViadeoPassword())
		//		.timeout(requestsTimeout)
		//		.method(Method.POST).execute();
		//		Thread.sleep(getHttpCallsDelay());

		final OAuthService service = new ServiceBuilder()
		.provider(LinkedInApi.class)
		.apiKey("n97yvokjb06u")
		.apiSecret("IvQxGkjq6VsCTtwC")
		.build();

		final Token requestToken = service.getRequestToken();

		String authUrl = service.getAuthorizationUrl(requestToken);
		System.out.println(authUrl);
		String regEx = "(.*oauth_token=)(.*)";
		final String oAuthToken = authUrl.replaceAll(regEx, "$2");


		final JTextField codeTxtField = new JTextField();
		codeTxtField.setPreferredSize(new Dimension(150, 30));

		JButton buttonValidate = new JButton("valider");
		buttonValidate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String verifierAuth = codeTxtField.getText();
				if(verifierAuth != null){
					System.out.println(verifierAuth);
					Token accessToken = service.getAccessToken(requestToken, new Verifier(verifierAuth)); // the requestToken you had from step 2

					System.out.println("access token : " + accessToken);
					
					OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.linkedin.com/v1/people/~/connections:(id,last-name)");
					service.signRequest(accessToken, request); // the access token from step 4
					Response response = request.send();
					System.out.println(response.getBody());
					//		https://api.linkedin.com/uas/oauth/authenticate?oauth_token=4b1ac6c5-efdb-42c9-96e4-2560c133d9e4
					//		setCookies(res.cookies());

					logger.info("logged.");
				}
			}
		});

		JPanel pnl = new JPanel(new GridBagLayout());
		pnl.add(new JLabel("code : "), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		pnl.add(codeTxtField, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		pnl.add(buttonValidate, new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		JFrame frame = new JFrame();
		frame.getContentPane().add(pnl);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(300, 300));
		frame.setVisible(true);
		
	}

	public List<String> getKeyWordList() throws IOException {
		return Files.readAllLines(Paths.get(getScrappingSettings().getKeyWordListFilePath()), StandardCharsets.UTF_8);
	}


	public boolean isKeywordsValid(String keyWords) {
		return keyWords != null && !"".equals(keyWords);
	}

	private void fireScrapingProgressUpdated(int progress){
		for (LinkedinScrapingProgressListener scrapingProgressListener : scrapingProgressListenerList) {
			scrapingProgressListener.progressUpdated(progress);
		}
	}

	private void fireScrapingKeyWordsStarted(String keyWords) {
		for (LinkedinScrapingProgressListener scrapingProgressListener : scrapingProgressListenerList) {
			scrapingProgressListener.fireScrapingKeyWordsStarted(keyWords);
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



	private long getHttpCallsDelay() {
		return getScrappingSettings().getHttpCallsDelay();
	}

	private String getResultFilePath() throws IOException, ScrapperException {
		return getSystemFilesFactory().getResultFilePath();
	}


	private HashSet<String> getNameSet() throws IOException, ScrapperException {
		if(nameSet == null){
			nameSet = new HashSet<String>();
			List<String> lineList = Files.readAllLines(Paths.get(getResultFilePath()), StandardCharsets.UTF_8);
			for (String line : lineList) {
				String[] valueList = line.split(";");
				if(valueList.length > 2){
					String nom = valueList[2];
					nameSet.add(nom);	
				}
			}
		}
		return nameSet;
	}

	private String getUserAgent() {
		return getScrappingSettings().getUserAgent();
	}

	private int getRequestTimeout() {
		return getScrappingSettings().getTimeout();
	}

	public interface LinkedinScrapingProgressListener{
		public void progressUpdated(int progress);

		public void fireScrapingKeyWordsStarted(String keyWords);
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

	public static void main(String[] args) {
		try {

			new LinkedinScraper().authenticate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
