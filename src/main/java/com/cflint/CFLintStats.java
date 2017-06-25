package com.cflint;

import java.math.BigInteger;

public class CFLintStats {

	private long timestamp = System.currentTimeMillis() / 1000L;
	private long fileCount;
	//Number of lines
	private BigInteger totalSize = BigInteger.ZERO;
	
	public void addFile(long numberOfLines){
		fileCount++;
		totalSize = totalSize.add(BigInteger.valueOf(numberOfLines));
	}

	public long getTimestamp() { return timestamp; }

	public long getFileCount() {
		return fileCount;
	}

	//Number of lines
	public BigInteger getTotalSize() {
		return totalSize;
	}
}
