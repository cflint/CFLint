package com.cflint;

import java.math.BigInteger;

public class CFLintStats {

	long fileCount;
	BigInteger totalSize = BigInteger.ZERO;
	
	public void addFile(long fileSize){
		fileCount++;
		totalSize = totalSize.add(BigInteger.valueOf(fileSize));
	}
	
	public long getFileCount() {
		return fileCount;
	}

	public BigInteger getTotalSize() {
		return totalSize;
	}
}
