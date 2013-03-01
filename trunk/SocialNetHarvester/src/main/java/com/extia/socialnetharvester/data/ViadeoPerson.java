package com.extia.socialnetharvester.data;


public class ViadeoPerson {
	
	private String country;
	private String city;
	private String name;
	private String job;
	private String company;
	private String previousJob;
	private String previousCompany;
	private String overview;
	private String profileLink;
	private String peopleCsvLine;
	private String keywords;
	
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getPreviousJob() {
		return previousJob;
	}
	public void setPreviousJob(String previousJob) {
		this.previousJob = previousJob;
	}
	
	public String getPreviousCompany() {
		return previousCompany;
	}
	public void setPreviousCompany(String previousCompany) {
		this.previousCompany = previousCompany;
	}
	
	public String getOverview() {
		return overview;
	}
	public void setOverview(String overview) {
		this.overview = overview;
	}
	
	public String getProfileLink() {
		return profileLink;
	}
	public void setProfileLink(String profileLink) {
		this.profileLink = profileLink;
	}
	
	public String getPeopleCsvLine() {
		return peopleCsvLine;
	}
	public void setPeopleCsvLine(String peopleCsvLine) {
		this.peopleCsvLine = peopleCsvLine;
	}
	
	public String toString() {
		return name; 
	}
}
