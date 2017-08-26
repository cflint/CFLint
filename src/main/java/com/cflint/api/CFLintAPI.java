package com.cflint.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.CFLint;
import com.cflint.Version;
import com.cflint.config.CFLintConfiguration;
import com.cflint.config.CFLintPluginInfo;
import com.cflint.config.CFLintPluginInfo.RuleGroup;
import com.cflint.config.ConfigBuilder;
import com.cflint.config.ConfigUtils;
import com.cflint.exception.CFLintConfigurationException;
import com.cflint.exception.CFLintScanException;
import com.cflint.tools.CFLintFilter;

/**
 * Provides a public API for integrating CFLint directly into another JVM environment.
 *
 */
public class CFLintAPI {

    PrintStream printStreamOut = System.out;
    PrintStream printStreamErr = System.err;
    final CFLintPluginInfo pluginInfo = ConfigUtils.loadDefaultPluginInfo();

    String filterFile = null;
    boolean verbose = false;
    boolean logError = false;
    boolean quiet = false;

    /**
     * List of file extensions to scan.  Default to *.cfc and *.cfm
     */
    private List<String> extensions = new ArrayList<>(Arrays.asList("cfc","cfm"));
    
    private boolean strictInclude;

    final CFLintConfiguration configuration;
    CFLint cflint;

    public CFLintAPI(final CFLintConfiguration configuration) {
        super();
        this.configuration = configuration;
    }

    public CFLintAPI() {
        this(new ConfigBuilder().build());
    }


    public CFLintResult scan(final List<String> fileOrFolder) throws CFLintScanException, CFLintConfigurationException {
        cflint = createCFLintInstance();

        for (final String scanfolder : fileOrFolder) {
            cflint.scan(scanfolder);
        }
        for (final BugInfo bug : cflint.getBugs()) {
            cflint.getStats().getCounts().add(bug.getMessageCode(), bug.getSeverity());
        }
        return new CFLintResult(cflint);
    }

    public CFLintResult scan(final String source) throws CFLintScanException, CFLintConfigurationException {
        return scan(source, "source.cfc");
    }

    public CFLintResult scan(final String source, final String filename)
            throws CFLintScanException, CFLintConfigurationException {
        cflint = createCFLintInstance();
        final File starterFile = new File(filename);
        if (starterFile.exists() && starterFile.getParentFile().exists()) {
            cflint.setupConfigAncestry(starterFile.getParentFile());
        }
        cflint.process(source, filename);
        for (final BugInfo bug : cflint.getBugs()) {
            cflint.getStats().getCounts().add(bug.getMessageCode(), bug.getSeverity());
        }
        return new CFLintResult(cflint);
    }

    public BugList getResults() {
        return cflint.getBugs();
    }

    private CFLint createCFLintInstance() throws CFLintConfigurationException {
        try {
            final CFLint cflint = new CFLint(configuration);
            cflint.setVerbose(verbose);
            cflint.setLogError(logError);
            cflint.setQuiet(quiet);
            cflint.setStrictIncludes(strictInclude);
            cflint.setAllowedExtensions(extensions);
            cflint.getBugs().setFilter(createFilter());
            return cflint;
        } catch (final Exception e) {
            throw new CFLintConfigurationException(e);
        }
    }

    protected CFLintFilter createFilter() throws CFLintConfigurationException {
        try {
            if (filterFile != null) {
                final File ffile = new File(filterFile);
                if (ffile.exists()) {
                    final FileInputStream fis = new FileInputStream(ffile);
                    final byte b[] = new byte[fis.available()];
                    IOUtils.read(fis, b);
                    fis.close();
                    return CFLintFilter.createFilter(new String(b), verbose);
                }
            }
            return CFLintFilter.createFilter(verbose);
        } catch (final Exception e) {
            throw new CFLintConfigurationException(e);
        }
    }

    /**
     * Return the current version of CFLint
     *
     * @return
     */
    public String getVersion() {
        return Version.getVersion();
    }

    /**
     * Return the version of CFParser used by the current CFLint
     *
     * @return
     */
    public String getCFParserVersion() {
        return cfml.parsing.Version.getVersion();
    }

    /**
     * List the rule groups
     *
     * @param pluginInfo
     * @return
     */
    public List<RuleGroup> getRuleGroups() {
        return pluginInfo.getRuleGroups();
    }

    /**
     * Limit file extensions to this list
     *
     * @param extensions
     */
    public void setExtensions(final List<String> extensions) {
        this.extensions = extensions;
    }

    /**
     * Verbose output
     *
     * @param verbose
     */
    public void setVerbose(final boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Log errors to output
     *
     * @param logerror
     */
    public void setLogError(final boolean logerror) {
        this.logError = logerror;
    }

    /**
     * Run quietly
     *
     * @param quiet
     */
    public void setQuiet(final boolean quiet) {
        this.quiet = quiet;
    }

    /**
     * Follow include paths and report an error if the included file cannot be
     * found
     *
     * @param strictInclude
     */
    public void setStrictInclude(final boolean strictInclude) {
        this.strictInclude = strictInclude;
    }

    /**
     * Get the configuration object
     *
     * @return
     */
    public CFLintConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Set filter file
     *
     * @param filterFile
     */
    public void setFilterFile(final String filterFile) {
        this.filterFile = filterFile;
    }

    /**
     * Provide the stream to use for standard output, by default System.out is
     * used.
     *
     * @param printStreamOut
     */
    public void setPrintStreamOut(final PrintStream printStreamOut) {
        this.printStreamOut = printStreamOut;
    }

    /**
     * Provide the stream to use for error output, by default System.err is
     * used.
     *
     * @param printStreamErr
     */
    public void setPrintStreamErr(final PrintStream printStreamErr) {
        this.printStreamErr = printStreamErr;
    }

}