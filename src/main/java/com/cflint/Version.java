package com.cflint;

import com.cflint.cli.CFLintCLI;

public class Version {

    private Version() {
        throw new IllegalStateException("Version utility class");
    }

    
    /** 
     * @return String
     */
    public static String getVersion() {

        String version = CFLintCLI.class.getPackage().getImplementationVersion();
        if (version != null) {
            return version;
        } else {
            return "";
        }

    }
}
