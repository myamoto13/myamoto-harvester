package com.extia.socialnetharvester.http.viadeo.avancement;

import java.util.HashMap;
import java.util.Map;

public class AvancementManager {
	
	private Map<String, Progress> avancementMap;
	private int nbResultTotal;
	private int nbScrappedResult;

	private Map<String, Progress> getAvancementMap(){
		if(avancementMap == null){
			avancementMap = new HashMap<String, Progress>();
		}
		return avancementMap;
	}

	public void clear() {
		getAvancementMap().clear();
		setNbResultTotal(0);
		setNbScrappedResult(0);
	}

	public void add(String keyWords, int nbTotal, int nbDone) {
		getAvancementMap().put(keyWords, new Progress(nbTotal, nbDone));
		setNbResultTotal(getNbResultTotal() + nbTotal);
		setNbScrappedResult(getNbScrappedResult() + nbDone);
	}

	public void incrementScrappedNumber(String keyWords, int nbDone) {
		if(keyWords != null){
			Progress avancement = getAvancementMap().get(keyWords);
			if(avancement != null){
				avancement.setActualProgress(avancement.getActualProgress() + nbDone);
			}
			setNbScrappedResult(getNbScrappedResult() + nbDone);
		}
	}

	public int getProgressPercent() {
		float progress = getNbResultTotal() != 0 ? (float)getNbScrappedResult() / (float)getNbResultTotal() : 0; 
		return Math.round(progress * 100);
	}

	public Integer getNbResultTotal() {
		return nbResultTotal;
	}

	private void setNbResultTotal(Integer nbResultTotal) {
		this.nbResultTotal = nbResultTotal;
	}

	public Integer getNbScrappedResult() {
		return nbScrappedResult;
	}

	private void setNbScrappedResult(Integer nbScrappedResult) {
		this.nbScrappedResult = nbScrappedResult;
	}
	
	

}
