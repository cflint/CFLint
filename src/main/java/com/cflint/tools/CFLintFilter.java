package com.cflint.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import com.cflint.Levels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cflint.BugInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Filter the resulting lint issues.
 *
 */
public class CFLintFilter {

    private static final Logger logger = LoggerFactory.getLogger(CFLintFilter.class);
    private ArrayList<Map<String, ?>> data = null;
    private boolean verbose = false;

    @SuppressWarnings("unchecked")
    private CFLintFilter(final String data) throws IOException {
        if (data != null && data.trim().length() > 0) {
            final ObjectMapper mapper = new ObjectMapper();
            this.data = mapper.readValue(data, ArrayList.class);
        } else {
            this.data = new ArrayList<>();
        }
    }

    private CFLintFilter(final ArrayList<Map<String, ?>> data) {
        this.data = data;
    }

    @Deprecated
    public void addFilter(final Map<String, String> filter) {
        data.add(filter);
    }

    public static CFLintFilter createFilter(final boolean verbose) throws IOException {
        String data = null;

        try {
            final InputStream is = CFLintFilter.class.getResourceAsStream("/cflintexclude.json");
            if (is == null) {
                if (verbose) {
                    logger.info("No cflintexclude.json on classpath.");
                }
                return new CFLintFilter((String) null);
            }
            if (verbose) {
                final URL url = CFLintFilter.class.getResource("/cflintexclude.json");
                logger.info("Using exclude file %s", url);
            }

            final byte[] b = new byte[is.available()];
            if (is.read(b) > 0) {
                is.close();
                data = new String(b);
            }
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }
        final CFLintFilter filter = new CFLintFilter(data);
        filter.setVerbose(verbose);
        if (verbose) {
            logger.info("Exclude rule count : %d", filter.data.size());
        }
        return filter;
    }

    public static CFLintFilter createFilter(final String excludeJSON, final boolean verbose) throws IOException {
        final CFLintFilter filter = new CFLintFilter(excludeJSON);
        filter.setVerbose(verbose);
        return filter;
    }

    public static CFLintFilter createFilter(final String excludeJSON) throws IOException {
        final CFLintFilter filter = new CFLintFilter(excludeJSON);
        return filter;
    }

    public boolean includeFile(final File file) {
        if (data != null) {
            for (final Map<String, ?> item : data) {
                if (item.containsKey("file")) {
                    if (!file.getName().matches(item.get("file").toString())) {
                        continue;
                    } else if (verbose) {
                        logger.info("Exclude matched file - prescan " + file.getName());
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean include(final BugInfo bugInfo) {
        if (data != null) {
            for (final Map<String, ?> item : data) {

                if (item.containsKey("file")) {
                    if (!bugInfo.getFilename().matches(item.get("file").toString().trim())) {
                        continue;
                    } else if (verbose) {
                        logger.info("Exclude matched file " + bugInfo.getFilename());
                    }
                }
                if (item.containsKey("code")) {
                    if (!bugInfo.getMessageCode().matches(item.get("code").toString())) {
                        continue;
                    } else if (verbose) {
                        logger.info("Exclude matched message code " + bugInfo.getMessageCode());
                    }
                }
                if (item.containsKey("function")) {
                    if (bugInfo.getFunction() == null
                            || !bugInfo.getFunction().matches(item.get("function").toString())) {
                        continue;
                    } else if (verbose) {
                        logger.info("Exclude matched function name " + bugInfo.getFunction());
                    }
                }
                if (item.containsKey("variable")) {
                    if (bugInfo.getVariable() == null
                            || !bugInfo.getVariable().matches(item.get("variable").toString())) {
                        continue;
                    } else if (verbose) {
                        logger.info("Exclude matched variable name " + bugInfo.getVariable());
                    }
                }
                if (item.containsKey("line")) {
                    if (bugInfo.getLine() > 0
                            || !Integer.toString(bugInfo.getLine()).matches(item.get("line").toString())) {
                        continue;
                    } else if (verbose) {
                        logger.info("Exclude matched line " + bugInfo.getLine());
                    }
                }
                if (item.containsKey("severity")) {
                    if (bugInfo.getSeverity() == Levels.UNKNOWN
                            || bugInfo.getSeverity() != Levels.fromString(item.get("severity").toString())) {
                        continue;
                    } else if (verbose) {
                        logger.info("Exclude matched severity " + bugInfo.getLine());
                    }
                }
                return false;
            }
        }
        return true;
    }

    public void setVerbose(final boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Identify a subset of the filter that can be applied without parsing i.e.
     * filename.
     * 
     * @return instance of CFLintFilter
     */
    public CFLintFilter createFilePreFilter() {
        final ArrayList<Map<String, ?>> newdata = new ArrayList<>();
        for (final Map<String, ?> map : this.data) {
            if (map.keySet().size() == 1 && map.containsKey("file")) {
                newdata.add(map);
            }
        }
        return new CFLintFilter(newdata);
    }
}
