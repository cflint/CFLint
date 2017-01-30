package com.cflint.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FileUtil {

    static public String loadFile(final File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            final byte[] b = new byte[fis.available()];
            fis.read(b);
            return new String(b);
        } catch (final Exception e) {
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (final IOException e) {
                return null;
            }
        }
    }

    static public boolean checkExtension(final File file, final List<String> allowedExtensions) {
        for (final String ext : allowedExtensions) {
            if (file.getName().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
}
