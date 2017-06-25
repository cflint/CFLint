package com.cflint;

import java.math.BigInteger;

public class CFLintStats {

	long timestamp = System.currentTimeMillis() / 1000L;
	long fileCount;
	//Number of lines
	BigInteger totalSize = BigInteger.ZERO;
	
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
