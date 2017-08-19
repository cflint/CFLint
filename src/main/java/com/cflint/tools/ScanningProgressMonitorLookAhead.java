package com.cflint.tools;

import java.io.File;
import java.util.List;

import com.cflint.CFLint;
import com.cflint.listeners.ProgressMonitorListener;

public class ScanningProgressMonitorLookAhead {

    private final ProgressMonitorListener progressMonitorListener;
    private final boolean startNewThread;
    private final File folder;
    private List<String> allowedExtensions;

    public static ScanningProgressMonitorLookAhead createInstance(CFLint cflint, final String folderName,
            boolean startNewThread) {
        ScanningProgressMonitorLookAhead lookAhead = new ScanningProgressMonitorLookAhead(folderName, startNewThread);
        cflint.addScanProgressListener(lookAhead.progressMonitorListener);
        lookAhead.allowedExtensions = cflint.getAllowedExtensions();
        return lookAhead;
    }

    public ScanningProgressMonitorLookAhead(final String folderName, final boolean startNewThread) {
        super();
        progressMonitorListener = new ProgressMonitorListener("CFLint");
        this.startNewThread = startNewThread;
        folder = new File(folderName);
    }

    public void startPreScan() {
        if (startNewThread) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    prescan(folder, 0, progressMonitorListener);
                }
            }).start();
        } else {
            prescan(folder, 0, progressMonitorListener);
        }

    }

    private int prescan(final File folderOrFile, int counter, final ProgressMonitorListener progressMonitorListener) {
        if (folderOrFile.isDirectory()) {
            for (final File file : folderOrFile.listFiles()) {
                counter = prescan(file, counter, progressMonitorListener);
            }
            if (counter > 10) {
                progressMonitorListener.setTotalToProcess(counter);
            }
            return counter;
        } else if (!folderOrFile.isHidden() && FileUtil.checkExtension(folderOrFile, allowedExtensions)) {
            return counter + 1;
        } else {
            return counter;
        }
    }
}
