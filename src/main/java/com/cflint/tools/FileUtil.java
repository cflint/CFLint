package com.cflint.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;

public class FileUtil {
    
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final int BUF_SIZE = 8192;

    public static String loadFile(final File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            try {
                final BOMInputStream bOMInputStream = new BOMInputStream(fis);
                final ByteOrderMark bom = bOMInputStream.getBOM();
                final String charsetName = bom == null ? DEFAULT_ENCODING : bom.getCharsetName();
                InputStreamReader reader = new InputStreamReader(new BufferedInputStream(bOMInputStream), charsetName);
                return readFully(reader);
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
            } catch (final IOException e) {}
        }
    }

    public static boolean checkExtension(final File file, final List<String> allowedExtensions) {
        for (final String ext : allowedExtensions) {
            if (file.getName().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Read from reader till EOF.
     * @param rdr the reader from which to read.
     * @return the contents read out of the given reader.
     *
     * @throws IOException if the contents could not be read out from the
     *         reader.
     */
    public static final String readFully(final Reader rdr) throws IOException {
        return readFully(rdr, BUF_SIZE);
    }

    /**
     * Read from reader till EOF.
     *
     * @param rdr the reader from which to read.
     * @param bufferSize the buffer size to use when reading.
     *
     * @return the contents read out of the given reader.
     *
     * @throws IOException if the contents could not be read out from the
     *         reader.
     */
    public static final String readFully(final Reader rdr, int bufferSize)
        throws IOException {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Buffer size must be greater "
                                               + "than 0");
        }
        final char[] buffer = new char[bufferSize];
        int bufferLength = 0;
        StringBuffer textBuffer = null;
        while (bufferLength != -1) {
            bufferLength = rdr.read(buffer);
            if (bufferLength > 0) {
                textBuffer = (textBuffer == null) ? new StringBuffer() : textBuffer;
                textBuffer.append(new String(buffer, 0, bufferLength));
            }
        }
        return (textBuffer == null) ? null : textBuffer.toString();
    }

}
