package com.cflint;

import com.cflint.cli.CFLintCLI;

public class Version {

    public static String getVersion() {

        String version = CFLintCLI.class.getPackage().getImplementationVersion();
        if (version != null) {
            return version;
        } else {
            return "";
        }

    }
}
