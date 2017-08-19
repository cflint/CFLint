package com.cflint.listeners;

import javax.swing.ProgressMonitor;

public class ProgressMonitorListener implements ScanProgressListener {

    private int fileCounter = 0;
    private int totalFileCount = 1;
    private final ProgressMonitor progressMonitor;
    private boolean indeterminate = true;

    public ProgressMonitorListener(final String progressLabel) {
        super();
        progressMonitor = new ProgressMonitor(null, progressLabel, "processing", 0, 1);
    }

    @Override
    public void startedProcessing(final String srcidentifier) {
        if (progressMonitor.isCanceled()) {
            throw new RuntimeException("Cancelled by user");
        }
        if (indeterminate && fileCounter + 1 >= totalFileCount) {
            totalFileCount += 10;
            progressMonitor.setMaximum(totalFileCount);
        }
        progressMonitor.setProgress(fileCounter++);
        progressMonitor.setNote("[" + fileCounter + "/" + totalFileCount + "] processing " + shorten(srcidentifier));
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

    @Override
    public void finishedProcessing(final String srcidentifier) {
    	//Empty implementation
    }

    public void setTotalToProcess(final int total) {
        indeterminate = false;
        totalFileCount = total;
        progressMonitor.setMaximum(total);
    }

    @Override
    public void close() {
        progressMonitor.close();
    }

}
