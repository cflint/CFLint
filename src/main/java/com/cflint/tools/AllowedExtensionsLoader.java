package com.cflint.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AllowedExtensionsLoader {

    public static final String CFC_DEFAULT_EXTENSION = ".cfc";
    public static final String CFM_DEFAULT_EXTENSION = ".cfm";

    private AllowedExtensionsLoader() {
        throw new IllegalStateException("AllowedExtensionsLoader utility class");
    }

    public static List<String> init(final String resourceBundleName) {
        List<String> allowedExtensions = new ArrayList<>();
        try {
            allowedExtensions.addAll(Arrays
                    .asList(ResourceBundle.getBundle(resourceBundleName).getString("allowedextensions").split(",")));
        } catch (final Exception e) {
            allowedExtensions.add(CFC_DEFAULT_EXTENSION);
            allowedExtensions.add(CFM_DEFAULT_EXTENSION);
        }
        return allowedExtensions;
    }
}
