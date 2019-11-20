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

    PrintStream printStreamOut =  System.out;
    PrintStream printStreamErr = System.err;
    final CFLintPluginInfo pluginInfo = ConfigUtils.loadDefaultPluginInfo();

    String filterFile = null;
    boolean verbose = false;
    boolean logError = false;
    boolean quiet = false;
    boolean debug = false;

    /**
     * List of file extensions to scan.  Default to *.cfc and *.cfm
     */
    private List<String> extensions = new ArrayList<>(Arrays.asList("cfc","cfm"));
    
    private boolean strictInclude;
    private String environmentName;

	final CFLintConfiguration configuration;
    final CFLint cflint;

    public CFLintAPI(final CFLintConfiguration configuration) throws CFLintConfigurationException {
        super();
        this.configuration = configuration;
        this.cflint = createCFLintInstance();
    }

    public CFLintAPI() throws CFLintConfigurationException {
        this(new ConfigBuilder().build());
    }


    public CFLintResult scan(final List<String> fileOrFolder) throws CFLintScanException, CFLintConfigurationException {

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
            throws CFLintScanException {
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
            cflint.setDebug(debug);
            cflint.setStrictIncludes(strictInclude);
            cflint.setEnvironmentName(environmentName);
            cflint.setAllowedExtensions(extensions);
            if(filterFile!=null){
                cflint.getBugs().setFilter(createFilter());
            }
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
     * @return  the current version of CFLint
     */
    public String getVersion() {
        return Version.getVersion();
    }

    /**
     * Return the version of CFParser used by the current CFLint
     *
     * @return  the version of CFParser used by this version of CFLint
     */
    public String getCFParserVersion() {
        return cfml.parsing.Version.getVersion();
    }

    /**
     * List the rule groups
     *
     * @return  the list of rule groups
     */
    public List<RuleGroup> getRuleGroups() {
        return pluginInfo.getRuleGroups();
    }

    /**
     * Limit file extensions to this list
     *
     * @param extensions        list of allowed extensions
     */
    public void setExtensions(final List<String> extensions) {
        this.extensions = extensions;
    }

    /**
     * Verbose output
     *
     * @param verbose   verbose output
     */
    public void setVerbose(final boolean verbose) {
        this.verbose = verbose;
        if(cflint != null) {
            cflint.setVerbose(verbose);
        }
    }

    /**
     * Log errors to standard error.
     *
     * @param logError  log errors to standard error
     */
    public void setLogError(final boolean logError) {
        this.logError = logError;
        if(cflint != null) {
            cflint.setLogError(logError);
        }
    }

    /**
     * Run quietly
     *
     * @param quiet     run quietly
     */
    public void setQuiet(final boolean quiet) {
        this.quiet = quiet;
        if(cflint != null) {
            cflint.setQuiet(quiet);
        }
    }

    /**
     * Run in debug mode
     *
     * @param debug     run quietly
     */
    public void setDebug(final boolean debug) {
        this.debug = debug;
        if(cflint != null) {
            cflint.setDebug(debug);
        }
    }

    /**
     * Follow include paths and report an error if the included file cannot be
     * found
     *
     * @param strictInclude     strict include
     */
    public void setStrictInclude(final boolean strictInclude) {
        this.strictInclude = strictInclude;
    }

    /**
     * Get the configuration object used by the API
     *
     * @return  the configuration object
     */
    public CFLintConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Set filter file
     *
     * @param filterFile        filter file
     */
    public void setFilterFile(final String filterFile) {
        this.filterFile = filterFile;
    }

    /**
     * Provide the stream to use for standard output, by default System.out is
     * used.
     *
     * @param printStreamOut    standard out stream
     */
    public void setPrintStreamOut(final PrintStream printStreamOut) {
        this.printStreamOut = printStreamOut;
    }

    /**
     * Provide the stream to use for error output, by default System.err is
     * used.
     *
     * @param printStreamErr    standard error stream
     */
    public void setPrintStreamErr(final PrintStream printStreamErr) {
        this.printStreamErr = printStreamErr;
    }

    public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}
}