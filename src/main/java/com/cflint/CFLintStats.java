package com.cflint;

import java.math.BigInteger;

public class CFLintStats {

	// Epoch timestamp for XML format output
	private long timestamp = System.currentTimeMillis() / 1000L;
	// Number of files
	private long fileCount;
	// Number of lines
	private BigInteger totalSize = BigInteger.ZERO;
	// Bug counts for current execution
	private BugCounts counts = new BugCounts();

	public CFLintStats() {
		super();
	}

	public CFLintStats(long timestamp, long fileCount, BigInteger totalSize) {
		super();
		this.timestamp = timestamp;
		this.fileCount = fileCount;
		this.totalSize = totalSize;
	}
	
	public void addFile(long numberOfLines){
		fileCount++;
		totalSize = totalSize.add(BigInteger.valueOf(numberOfLines));
	}

	public long getTimestamp() { return timestamp; }

	public long getFileCount() { return fileCount; }

	public BigInteger getTotalSize() {
		return totalSize;
	}

	public BugCounts getCounts() { return counts; }
}
