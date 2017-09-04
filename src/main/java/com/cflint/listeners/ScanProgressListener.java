package com.cflint.listeners;

/**
 * Receives actions when a new source (file) is processed.
 */
public interface ScanProgressListener {

    void startedProcessing(String srcidentifier);

    void finishedProcessing(String srcidentifier);

    void close();
}
