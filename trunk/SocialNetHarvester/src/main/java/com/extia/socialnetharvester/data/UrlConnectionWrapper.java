package com.extia.socialnetharvester.data;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

public class UrlConnectionWrapper implements Comparable<UrlConnectionWrapper> {
	
	private String url;
	private String userAgent;
	private String referer;
	private int timeout;
	private Map<String, String> cookies;
	private Map<String, String> postParameterMap;
	private Method method;
	private boolean scrapped;
	
	public UrlConnectionWrapper() {
		cookies = new HashMap<String, String>();
		postParameterMap = new HashMap<String, String>();
		setMethod(Method.GET);
	}
	
	public boolean isScrapped() {
		return scrapped;
	}

	public void setScrapped(boolean scrapped) {
		this.scrapped = scrapped;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public void addCookies(Map<String, String> cookies) {
		for (String cookieName: cookies.keySet()) {
			cookies.put(cookieName, cookies.get(cookieName));
		}
	}
	public Map<String, String> getCookies() {
		return this.cookies;
	}
	
	public Map<String, String> getPostParameterMap() {
		return postParameterMap;
	}
	public void putPostParameter(String name, String value) {
		getPostParameterMap().put(name, value);
	}
	
	public String getPostParameterValue(String name) {
		return getPostParameterMap().get(name);
	}
	
	public void putCookie(String name, String value) {
		getCookies().put(name, value);
	}
	
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}

	public Connection getConnection(Map<String, String> cookies) {
		Connection result = Jsoup.connect(getUrl())
				.userAgent(getUserAgent())
				.referrer(getReferer())
				.timeout(getTimeout())
				.cookies(cookies)
				.method(getMethod());
		for (String parameterName : getPostParameterMap().keySet()) {
			result = result.data(parameterName, getPostParameterMap().get(parameterName));
		}
		return result;
	}

	@Override
	public String toString() {
		return "ViadeoConnectionWrapper [url=" + url + ", userAgent="
				+ userAgent + ", referer=" + referer + ", timeout=" + timeout
				+ ", cookies=" + cookies + ", postParameterMap="
				+ postParameterMap + ", method=" + method + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime
				* result
				+ ((postParameterMap == null) ? 0 : postParameterMap.hashCode());
		result = prime * result + ((referer == null) ? 0 : referer.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result
				+ ((userAgent == null) ? 0 : userAgent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UrlConnectionWrapper other = (UrlConnectionWrapper) obj;
		if (method != other.method)
			return false;
		if (postParameterMap == null) {
			if (other.postParameterMap != null)
				return false;
		} else if (!postParameterMap.equals(other.postParameterMap))
			return false;
		if (referer == null) {
			if (other.referer != null)
				return false;
		} else if (!referer.equals(other.referer))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (userAgent == null) {
			if (other.userAgent != null)
				return false;
		} else if (!userAgent.equals(other.userAgent))
			return false;
		return true;
	}

	public int compareTo(UrlConnectionWrapper o) {
		int result = 0;
		if(getUrl() != null){
			if(o.getUrl() != null){
				result = getUrl().compareTo(o.getUrl());
			}else{
				result = 1;
			}
		}
		
		return result;
	}

}
