package com.extia.socialnetharvester.business;


/**
 * @author Michael Cortes
 *
 */
public class ViadeoUserSettings {
	
	private String workingDirPath;
	private String viadeoLogin;
	private String viadeoPassword;
	private String userAgent;
	private int timeout;
	private int httpCallsDelay;
	private String keyWordListFilePath;
	private boolean autoResumeScraping;
	
	public boolean isAutoResumeScraping() {
		return autoResumeScraping;
	}
	public void setAutoResumeScraping(boolean autoResumeScraping) {
		this.autoResumeScraping = autoResumeScraping;
	}
	public String getWorkingDirPath() {
		return workingDirPath;
	}
	public void setWorkingDirPath(String workingDirPath) {
		this.workingDirPath = workingDirPath;
	}
	public String getViadeoLogin() {
		return viadeoLogin;
	}
	public void setViadeoLogin(String viadeoLogin) {
		this.viadeoLogin = viadeoLogin;
	}
	public String getViadeoPassword() {
		return viadeoPassword;
	}
	public void setViadeoPassword(String viadeoPassword) {
		this.viadeoPassword = viadeoPassword;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public String getKeyWordListFilePath() {
		return keyWordListFilePath;
	}
	public void setKeyWordListFilePath(String keyWordListFilePath) {
		this.keyWordListFilePath = keyWordListFilePath;
	}
	public int getHttpCallsDelay() {
		return httpCallsDelay;
	}
	public void setHttpCallsDelay(int httpCallsDelay) {
		this.httpCallsDelay = httpCallsDelay;
	}
	public String getKeyWordsReportFilePath() {
		return getWorkingDirPath() + "\\keywordsReport.csv";
	}
	public String getResultFilePath() {
		return getWorkingDirPath() + "\\results.csv";
	}
	
}
