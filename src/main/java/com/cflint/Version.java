package com.cflint;

import com.cflint.main.CFLintMain;

public class Version {

    public static String getVersion() {
        try {
            String version = CFLintMain.class.getPackage().getImplementationVersion();
            if (version != null) {
                return version;
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }
}
