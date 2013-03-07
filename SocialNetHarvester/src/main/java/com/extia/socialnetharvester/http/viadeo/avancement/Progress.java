package com.extia.socialnetharvester.http.viadeo.avancement;

public class Progress{
	
	private int maxProgress;
	private int actualProgress;

	public Progress(int maxProgress, int actualProgress) {
		super();
		this.maxProgress = maxProgress;
		this.actualProgress = actualProgress;
	}
	
	public int getMaxProgress() {
		return maxProgress;
	}
	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}

	public int getActualProgress() {
		return actualProgress;
	}
	public void setActualProgress(int actualProgress) {
		this.actualProgress = actualProgress;
	}

}