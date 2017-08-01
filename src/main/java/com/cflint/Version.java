package com.cflint;

import com.cflint.main.CFLintMain;

public class Version {

    public static String getVersion() {
        try {
            return CFLintMain.class.getPackage().getImplementationVersion();
        } catch (Exception e) {
            return "";
        }
    }
}
