package com.cflint;

import java.math.BigInteger;

public class CFLintStats {

	long fileCount;
	//Number of lines
	BigInteger totalSize = BigInteger.ZERO;
	
	public void addFile(long numberOfLines){
		fileCount++;
		totalSize = totalSize.add(BigInteger.valueOf(numberOfLines));
	}
	
	public long getFileCount() {
		return fileCount;
	}

	//Number of lines
	public BigInteger getTotalSize() {
		return totalSize;
	}
}
