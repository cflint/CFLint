package com.cflint.listeners;

import java.io.File;

import javax.swing.ProgressMonitor;

public class ProgressMonitorListener implements ScanProgressListener {

	int fileCounter = 0;
	int totalFileCount = 1;
	final ProgressMonitor progressMonitor;
	boolean indeterminate = true;

	public ProgressMonitorListener(final String progressLabel) {
		super();
		progressMonitor = new ProgressMonitor(null, progressLabel, "processing", 0, 1);
	}

	public void startedProcessing(final String srcidentifier) {
		if(progressMonitor.isCanceled()){
			throw new RuntimeException("Cancelled by user");
		}
		if(indeterminate && fileCounter + 1 >= totalFileCount){
			totalFileCount += 10;
			progressMonitor.setMaximum(totalFileCount);
		}
		progressMonitor.setProgress(fileCounter++);
		progressMonitor.setNote("[" +fileCounter + "/" + totalFileCount + "] processing " +  shorten(srcidentifier));
	}

	private String shorten(final String srcidentifier) {
		if (srcidentifier == null) {
			return "";
		}
		if (srcidentifier.length() < 80) {
			return srcidentifier;
		}
		return srcidentifier.substring(0, 78) + "..";
	}

	public void finishedProcessing(final String srcidentifier) {
	}

	public void setTotalToProcess(final int total) {
		indeterminate=false;
		totalFileCount = total;
		progressMonitor.setMaximum(total);
	}

	public void close() {
		progressMonitor.close();
	}
	
}
