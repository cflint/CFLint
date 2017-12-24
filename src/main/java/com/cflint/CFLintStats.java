package com.cflint;

import java.math.BigInteger;

public class CFLintStats {

    // Epoch timestamp for XML format output
    private long timestamp = System.currentTimeMillis() / 1000L;
    // Number of files
    private long fileCount;
    // Number of lines
    private BigInteger totalLines = BigInteger.ZERO;
    // Bug counts for current execution
    private BugCounts counts = new BugCounts();

    public CFLintStats() {
        super();
    }

    public CFLintStats(final long timestamp, final long fileCount, final BigInteger totalLines) {
        super();
        this.timestamp = timestamp;
        this.fileCount = fileCount;
        this.totalLines = totalLines;
    }

    public CFLintStats(final long timestamp, final long fileCount, final BigInteger totalLines, final BugCounts counts) {
        super();
        this.timestamp = timestamp;
        this.fileCount = fileCount;
        this.totalLines = totalLines;
        this.counts = counts;
    }

    public void addFile(final long numberOfLines) {
        fileCount++;
        totalLines = totalLines.add(BigInteger.valueOf(numberOfLines));
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getFileCount() {
        return fileCount;
    }

    public BigInteger getTotalLines() {
        return totalLines;
    }

    public BugCounts getCounts() {
        return counts;
    }
}
