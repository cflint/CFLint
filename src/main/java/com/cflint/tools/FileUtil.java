package com.cflint.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.tools.ant.util.FileUtils;

public class FileUtil {
    
    static final String defaultEncoding = "UTF-8";

    static public String loadFile(final File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            try {
                final BOMInputStream bOMInputStream = new BOMInputStream(fis);
                final ByteOrderMark bom = bOMInputStream.getBOM();
                final String charsetName = bom == null ? defaultEncoding : bom.getCharsetName();
                InputStreamReader reader = new InputStreamReader(new BufferedInputStream(bOMInputStream), charsetName);
                return FileUtils.readFully(reader);
            } finally {
                fis.close();
            }
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
