package com.cflint.listeners;

/**
 * Receives actions when a new source (file) is processed
 * @author eberlyrh
 *
 */
public interface ScanProgressListener {

	public void startedProcessing(String srcidentifier);
	public void finishedProcessing(String srcidentifier);
	public void close();
}
