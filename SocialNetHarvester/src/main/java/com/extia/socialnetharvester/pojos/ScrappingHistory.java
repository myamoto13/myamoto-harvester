package com.extia.socialnetharvester.pojos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScrappingHistory{
	
	private List<UrlConnectionWrapper> searchConnectionList;
	private Date date;
	
	public ScrappingHistory(){
		searchConnectionList = new ArrayList<UrlConnectionWrapper>();
	}
	
	public List<UrlConnectionWrapper> getSearchConnectionList() {
		return searchConnectionList;
	}
	
	public boolean addSearchConnection(UrlConnectionWrapper searchConnection){
		return getSearchConnectionList().add(searchConnection);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime
				* result
				+ ((searchConnectionList == null) ? 0 : searchConnectionList
						.hashCode());
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
		ScrappingHistory other = (ScrappingHistory) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (searchConnectionList == null) {
			if (other.searchConnectionList != null)
				return false;
		} else if (!searchConnectionList.equals(other.searchConnectionList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ScrappingHistory [searchConnectionList=" + searchConnectionList
				+ ", date=" + date + "]";
	}

}
