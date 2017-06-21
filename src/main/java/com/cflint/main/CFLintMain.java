package com.cflint.main;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.cflint.CFLint;
import com.cflint.HTMLOutput;
import com.cflint.JSONOutput;
import com.cflint.TextOutput;
import com.cflint.Version;
import com.cflint.XMLOutput;
import com.cflint.config.CFLintChainedConfig;
import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintConfiguration;
import com.cflint.config.CFLintPluginInfo;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.CFLintPluginInfo.RuleGroup;
import com.cflint.config.ConfigUtils;
import com.cflint.tools.CFLintFilter;
import com.cflint.xml.MarshallerException;
import com.cflint.xml.stax.DefaultCFlintResultMarshaller;

public class CFLintMain {

    public static final String CFLINT = "cflint";
    public static final String RULES = "rules";
    public static final String INCLUDE_RULE = "includeRule";
    public static final String EXCLUDE_RULE = "excludeRule";
    public static final String FOLDER = "folder";
    public static final String FILTER_FILE = "filterFile";
    public static final String VERBOSE = "verbose";
    public static final String SHOWPROGRESS = "showprogress";
    public static final String QUIET = "quiet";
    public static final String DISPLAY_THIS_HELP = "display this help";
    public static final String XMLFILE = "xmlfile";
    public static final String XMLSTYLE = "xmlstyle";
    public static final String HTMLFILE = "htmlfile";
    public static final String HTMLSTYLE = "htmlstyle";
    public static final String JSONFILE = "jsonfile";
    public static final String TEXTFILE = "textfile";
    public static final String EXTENSIONS = "extensions";
    public static final String CONFIGFILE = "configfile";
    public static final String STRICT_INCLUDE = "strictinclude";
    public static final String STDIN = "stdin";
    List<String> folder = new ArrayList<String>();
    String filterFile = null;
    boolean verbose = false;
    boolean logerror = false;
    boolean quiet = false;
    boolean xmlOutput = false;
    boolean jsonOutput = false;
    boolean htmlOutput = true;
    boolean textOutput = false;
    String xmlOutFile = "cflint-result.xml";
    String xmlstyle = CFLINT;
    String htmlOutFile = "cflint-result.html";
    String htmlStyle = "plain.xsl";
    String jsonOutFile = "cflint-result.json";
    String textOutFile = null;
    CFLintConfig cmdLineConfig = null;
    CFLintConfig configFileConfig = null;
    CFLintConfig defaultConfig = null;
    private String extensions;
    boolean showprogress = false;
    boolean progressUsesThread = true;
    private Boolean stdIn = false;
    private String stdInFile = "source.cfc";
    private Boolean stdOut = false;
    // private String configfile = null;
    boolean showStats = false;
    private boolean strictInclude;

    public static void main(final String[] args) throws Exception {
        final Options options = new Options();
        options.addOption(RULES, false, "list of all supported rules");
        options.addOption("config", false, "list of rules in config file");
        options.addOption(INCLUDE_RULE, true, "specify rules to include");
        options.addOption(EXCLUDE_RULE, true, "specify rules to exclude");
        options.addOption(FOLDER, true, "folder(s) to scan");
        options.addOption("file", true, "file(s) to scan");
        options.addOption(FILTER_FILE, true, "filter file");
        options.addOption("v", false, VERBOSE);
        options.addOption("version", false, "show the version number");
        options.addOption("ui", false, "show UI");
        options.addOption(VERBOSE, false, VERBOSE);
        options.addOption(STRICT_INCLUDE, false, "Check every include and try to parse it");
        options.addOption(SHOWPROGRESS, false, "show progress bar");
        options.addOption("singlethread", false, "show progress bar");

        options.addOption("logerror", false, "log parsing errors as bugs");
        options.addOption("e", false, "log parsing errors as bugs");
        options.addOption("q", false, QUIET);
        options.addOption(QUIET, false, QUIET);
        options.addOption("?", false, DISPLAY_THIS_HELP);
        options.addOption("h", false, DISPLAY_THIS_HELP);
        options.addOption("help", false, DISPLAY_THIS_HELP);
        options.addOption("xml", false, "output in xml format");
        options.addOption(XMLFILE, true, "specify the output xml file (default: cflint-results.xml)");
        options.addOption(XMLSTYLE, true, "cflint,findbugs");
        options.addOption("html", false, "output in html format (default)");
        options.addOption(HTMLFILE, true, "specify the output html file (default: cflint-results.html)");
        options.addOption(HTMLSTYLE, true, "default,plain");// fancy,fancy-hist,summary
        options.addOption("json", false, "output in json format");
        options.addOption(JSONFILE, true, "specify the output json file (default: cflint-results.json)");
        options.addOption("text", false, "output in plain text");
        options.addOption(TEXTFILE, true, "specify the output text file (default: cflint-results.txt)");
        options.addOption("stats", false, "show bug count statstics");
        options.addOption(EXTENSIONS, true, "specify the extensions of the CF source files (default: .cfm,.cfc)");
        options.addOption(CONFIGFILE, true, "specify the location of the config file");
        options.addOption(STDIN, true, "use stdin for file input (default: source.cfc)");
        options.addOption("stdout", false, "output to stdout only");
        options.addOption("listrulegroups", false, "list rule groups");
        options.addOption("rulegroups", true, "rule groups");

        final CommandLineParser parser = new GnuParser();
        final CommandLine cmd = parser.parse(options, args);
        final CFLintMain main = new CFLintMain();

        if (cmd.hasOption('h') || cmd.hasOption("help") || cmd.hasOption("?")) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(CFLINT, options);
            return;
        }
        if (cmd.hasOption("version")) {
            System.out.println("CFLint " + Version.getVersion());
            System.out.println("CFParser " + cfml.parsing.Version.getVersion());
            return;
        }
        main.strictInclude = cmd.hasOption(STRICT_INCLUDE);
        if (cmd.hasOption(RULES) || cmd.hasOption("config")) {
            final CFLintPluginInfo pluginInfo = cmd.hasOption(RULES) ? ConfigUtils.loadDefaultPluginInfo()
                    : new CFLintPluginInfo();
            main.defaultConfig = new CFLintConfig();
            main.defaultConfig.setRules(pluginInfo.getRules());

            CFLintChainedConfig myConfig = new CFLintChainedConfig(main.defaultConfig);
            if (cmd.hasOption(CONFIGFILE)) {
                final String configfile = cmd.getOptionValue(CONFIGFILE);
                myConfig = myConfig.createNestedConfig(loadConfig(configfile));
            }

            final HashMap<String, String> descriptions = ConfigUtils.loadDescriptions();
            System.out.println("Supported rules");
            for (final PluginInfoRule rule : myConfig.getRules()) {
                System.out.println("  " + rule.getName());
                for (final PluginMessage message : rule.getMessages()) {
                    System.out.println("    " + message.getCode() + " - " + descriptions.get(message.getCode()));
                }
            }

            return;
        }

        final CFLintPluginInfo pluginInfo = ConfigUtils.loadDefaultPluginInfo();
        main.defaultConfig = new CFLintConfig();
        main.defaultConfig.setRules(pluginInfo.getRules());

        if (cmd.hasOption("listrulegroups")) {
            listRuleGroups(pluginInfo);
            return;
        }
        // Load the codes from the rule groups into the includes list, if rule
        // groups are specified
        if (cmd.hasOption("rulegroups")) {
            final String rulegroups = cmd.getOptionValue("rulegroups");
            applyRuleGroups(main, pluginInfo, rulegroups);
        } else {
            // Exclude the experimental by default.
            applyRuleGroups(main, pluginInfo, "!Experimental");
        }

        main.verbose = (cmd.hasOption('v') || cmd.hasOption(VERBOSE));
        main.quiet = (cmd.hasOption('q') || cmd.hasOption(QUIET));
        main.logerror = (cmd.hasOption('e') || cmd.hasOption("logerror"));
        main.xmlOutput = cmd.hasOption("xml") || cmd.hasOption(XMLSTYLE) || cmd.hasOption(XMLFILE);
        main.textOutput = cmd.hasOption("text") || cmd.hasOption(TEXTFILE);
        main.jsonOutput = cmd.hasOption("json") || cmd.hasOption("jsonFile");
        main.showStats = cmd.hasOption("stats");

        if (cmd.hasOption("ui")) {
            main.ui();
        }
        // If an output is specified, htmlOutput is not defaulted to true.
        if (main.xmlOutput || main.textOutput || main.jsonOutput) {
            main.htmlOutput = cmd.hasOption("html") || cmd.hasOption(HTMLSTYLE) || cmd.hasOption(HTMLFILE);
        }

        if (main.verbose) {
            System.out.println("XML Output " + main.xmlOutput);
            System.out.println("Text Output " + main.textOutput);
            System.out.println("JSON Output " + main.jsonOutput);
            System.out.println("HTML Output " + main.htmlOutput);
        }

        if (cmd.hasOption(FOLDER)) {
            main.folder.addAll(Arrays.asList(cmd.getOptionValue(FOLDER).split(",")));
        }
        if (cmd.hasOption("file")) {
            main.folder.addAll(Arrays.asList(cmd.getOptionValue("file").split(",")));
        }
        if (cmd.hasOption(HTMLSTYLE)) {
            main.htmlStyle = cmd.getOptionValue(HTMLSTYLE);
            if (!main.htmlStyle.endsWith(".xsl") && !main.htmlStyle.endsWith(".xslt")) {
                main.htmlStyle = main.htmlStyle + ".xsl";
            }
        }
        if (cmd.hasOption(XMLSTYLE)) {
            main.xmlstyle = cmd.getOptionValue(XMLSTYLE);
        }
        if (cmd.hasOption(FILTER_FILE)) {
            main.filterFile = cmd.getOptionValue(FILTER_FILE);
        }
        if (cmd.hasOption(XMLFILE)) {
            main.xmlOutFile = cmd.getOptionValue(XMLFILE);
        }
        if (cmd.hasOption(JSONFILE)) {
            main.jsonOutFile = cmd.getOptionValue(JSONFILE);
        }
        if (cmd.hasOption(CONFIGFILE)) {
            final String configfile = cmd.getOptionValue(CONFIGFILE);
            main.configFileConfig = loadConfig(configfile);
        }
        if (cmd.hasOption(HTMLFILE)) {
            main.htmlOutFile = cmd.getOptionValue(HTMLFILE);
        }
        if (cmd.hasOption(TEXTFILE)) {
            main.textOutFile = cmd.getOptionValue(TEXTFILE);
        }
        if (cmd.hasOption(JSONFILE)) {
            main.jsonOutFile = cmd.getOptionValue(JSONFILE);
        }
        if (cmd.hasOption(EXTENSIONS)) {
            main.extensions = cmd.getOptionValue(EXTENSIONS);
        }

        if (cmd.hasOption(INCLUDE_RULE) || cmd.hasOption(EXCLUDE_RULE)) {
            main.cmdLineConfig = new CFLintConfig();
            if (cmd.hasOption(INCLUDE_RULE)) {
                for (final String code : cmd.getOptionValue(INCLUDE_RULE).split(",")) {
                    main.cmdLineConfig.addInclude(new PluginMessage(code));
                    main.cmdLineConfig.setInheritParent(false);
                }
            }
            if (cmd.hasOption(EXCLUDE_RULE)) {
                for (final String code : cmd.getOptionValue(EXCLUDE_RULE).split(",")) {
                    main.cmdLineConfig.addExclude(new PluginMessage(code));
                }
            }
        }
        main.showprogress = cmd.hasOption(SHOWPROGRESS) || (!cmd.hasOption(SHOWPROGRESS) && cmd.hasOption("ui"));
        main.progressUsesThread = !cmd.hasOption("singlethread");
        main.stdIn = cmd.hasOption(STDIN);
        if (main.stdIn) {
            final String stdInOptionValue = cmd.getOptionValue(STDIN);
            if (stdInOptionValue != null) {
                main.stdInFile = stdInOptionValue;
            }
        }
        main.stdOut = cmd.hasOption("stdout");
        if (main.isValid()) {
            main.execute();
            if (cmd.hasOption("ui")) {
                main.open();
            }
        } else {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(CFLINT, options);
        }
    }

    /**
     * Apply the listed rule groups
     * 
     * @param main
     * @param pluginInfo
     * @param rulegroups
     */
    protected static void applyRuleGroups(final CFLintMain main, final CFLintPluginInfo pluginInfo,
            final String rulegroups) {
        final boolean include = !rulegroups.startsWith("!");
        for (RuleGroup rg : pluginInfo.getRuleGroups()) {
            if (rulegroups.contains(rg.getName())) {
                if (include) {
                    main.defaultConfig.getIncludes().addAll(rg.getMessages());
                } else {
                    main.defaultConfig.getExcludes().addAll(rg.getMessages());
                }
            }
        }
    }

    /**
     * List the rule groups
     * 
     * @param pluginInfo
     */
    private static void listRuleGroups(CFLintPluginInfo pluginInfo) {
        Map<String, PluginMessage> allCodes = new LinkedHashMap<String, PluginMessage>();
        for (PluginInfoRule rule : pluginInfo.getRules()) {
            for (PluginMessage msg : rule.getMessages()) {
                allCodes.put(msg.getCode(), msg);
            }
        }
        for (RuleGroup ruleGroup : pluginInfo.getRuleGroups()) {
            System.out.println("Rule Group : " + ruleGroup.getName());
            for (PluginMessage msg : ruleGroup.getMessages()) {
                System.out.println("\t" + msg.getCode() + " : " + msg.getSeverity());
                allCodes.remove(msg.getCode());
            }
        }
        if (!allCodes.isEmpty()) {
            System.out.println("Rule Group : UNASSIGNED");
            for (PluginMessage msg : allCodes.values()) {
                System.out.println("\t" + msg.getCode() + " : " + msg.getSeverity());
            }
        }
    }

    private void open() throws IOException {
        if (xmlOutput) {
            Desktop.getDesktop().open(new File(xmlOutFile));
            return;
        }
        if (textOutput && textOutFile != null) {
            Desktop.getDesktop().open(new File(textOutFile));
            return;
        }
        if (htmlOutput) {
            Desktop.getDesktop().open(new File(htmlOutFile));
            return;
        }
        if (jsonOutput) {
            Desktop.getDesktop().open(new File(jsonOutFile));
            return;
        }
    }

    private void ui() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select target directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        final int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            folder.add(chooser.getSelectedFile().getAbsolutePath());
        } else {
            return;
        }

        final String[] slist = new String[] { "xml", "html", "text", "txt", "json" };
        final JList<String> list = new JList<String>(slist);
        JOptionPane.showMessageDialog(null, list, "Output Type", JOptionPane.PLAIN_MESSAGE);

        final int[] indxs = list.getSelectedIndices();
        // If selected set htmlOutput to false
        for (final int indx : indxs) {
            if (indx == 0) {
                xmlOutput = true;
            }
            if (indx == 1) {
                htmlOutput = true;
            }
            if (indx == 2 || indx == 3) {
                textOutput = true;
            }
            if (indx == 4) {
                jsonOutput = true;
            }
        }
    }

    private static CFLintConfig loadConfig(final String configfile) {
        if (configfile != null) {
            try {
                CFLintPluginInfo pluginInfo=null;
                if (configfile.toLowerCase().endsWith(".xml")) {
                    final Object configObj = ConfigUtils.unmarshal(new FileInputStream(configfile));
                    if (configObj instanceof CFLintPluginInfo)
                        pluginInfo= (CFLintPluginInfo) configObj;
                    else if(configObj instanceof CFLintConfig ){
                        return (CFLintConfig) configObj;
                    }
                } else {
                    pluginInfo= ConfigUtils.unmarshalJson(new FileInputStream(configfile), CFLintPluginInfo.class);
                }
                CFLintConfig returnVal = new CFLintConfig();
                returnVal.setRules(pluginInfo.getRules());
                return returnVal;
            } catch (final Exception e) {
                System.err.println("Unable to load config file. " + e.getMessage());
            }
        }
        return null;
    }

    private void execute() throws IOException, TransformerException, JAXBException, MarshallerException {
        final CFLint cflint = new CFLint(buildConfigChain());
        cflint.setVerbose(verbose);
        cflint.setLogError(logerror);
        cflint.setQuiet(quiet);
        cflint.setStrictIncludes(strictInclude);
        cflint.setShowProgress(showprogress);
        cflint.setProgressUsesThread(progressUsesThread);
        if (extensions != null && extensions.trim().length() > 0) {
            try {
                cflint.setAllowedExtensions(Arrays.asList(extensions.trim().split(",")));
            } catch (final Exception e) {
                System.err.println(
                        "Unable to use extensions (" + extensions + ") using default instead. " + e.getMessage());
            }
        }
        final CFLintFilter filter = createBaseFilter();
        cflint.getBugs().setFilter(filter);

        for (final String scanfolder : folder) {
            cflint.scan(scanfolder);
        }
        if (stdIn) {
            final StringBuilder source = new StringBuilder();
            final Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                final String nextLine = scanner.nextLine();
                source.append(nextLine);
                source.append(System.lineSeparator());
            }
            scanner.close();
            cflint.process(source.toString(), stdInFile);
        }
        if (xmlOutput) {
            final Writer xmlwriter = stdOut ? new OutputStreamWriter(System.out)
                    : createWriter(xmlOutFile, StandardCharsets.UTF_8);
            if ("findbugs".equalsIgnoreCase(xmlstyle)) {
                if (verbose) {
                    display("Writing XML findbugs style" + (stdOut ? "." : " to " + xmlOutFile));
                }
                new XMLOutput().outputFindBugs(cflint.getBugs(), xmlwriter, showStats);
            } else {
                if (verbose) {
                    display("Writing XML" + (stdOut ? "." : " to " + xmlOutFile));
                }
                new DefaultCFlintResultMarshaller().output(cflint.getBugs(), xmlwriter, showStats);
            }
        }
        if (textOutput) {
            if (textOutFile != null && verbose) {
                display("Writing text" + (stdOut ? "." : " to " + textOutFile));
            }
            final Writer textwriter = stdOut || textOutFile == null ? new OutputStreamWriter(System.out)
                    : new FileWriter(textOutFile);
            new TextOutput().output(cflint.getBugs(), textwriter, showStats);
        }
        if (htmlOutput) {
            try {
                if (verbose) {
                    display("Writing HTML" + (stdOut ? "." : " to " + htmlOutFile));
                }
                final Writer htmlwriter = stdOut ? new OutputStreamWriter(System.out) : new FileWriter(htmlOutFile);
                new HTMLOutput(htmlStyle).output(cflint.getBugs(), htmlwriter, showStats);
            } catch (final TransformerException e) {
                throw new IOException(e);
            }
        }
        if (jsonOutput) {
            if (verbose) {
                display("Writing JSON" + (stdOut ? "." : " to " + jsonOutFile));
            }
            final Writer jsonwriter = stdOut ? new OutputStreamWriter(System.out) : new FileWriter(jsonOutFile);
            new JSONOutput().output(cflint.getBugs(), jsonwriter, showStats);
        }
        if (verbose) {
            display("Total files scanned: " + cflint.getStats().getFileCount());
            display("Total size: " + cflint.getStats().getTotalSize());
        }
    }

    private CFLintConfiguration buildConfigChain() {
        CFLintChainedConfig myConfig = new CFLintChainedConfig(defaultConfig);
        myConfig = myConfig.createNestedConfig(configFileConfig);
        return myConfig.createNestedConfig(cmdLineConfig);
    }

    protected CFLintFilter createBaseFilter() throws IOException, FileNotFoundException {
        CFLintFilter filter = CFLintFilter.createFilter(verbose);
        if (filterFile != null) {
            final File ffile = new File(filterFile);
            if (ffile.exists()) {
                final FileInputStream fis = new FileInputStream(ffile);
                final byte b[] = new byte[fis.available()];
                fis.read(b);
                fis.close();
                filter = CFLintFilter.createFilter(new String(b), verbose);
            }
        }
        return filter;
    }

    private void display(final String text) {
        if (verbose) {
            System.out.println(text);
        }
    }

    private boolean isValid() {
        if (folder.isEmpty() && !stdIn) {
            System.err.println("Set -folder or -stdin");
            return false;
        }
        return true;
    }

    private Writer createWriter(final String xmlOutFile, final Charset encoding) throws IOException {
        final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(xmlOutFile), encoding);
        out.append(String.format("<?xml version=\"1.0\" encoding=\"%s\" ?>%n", encoding));
        return out;
    }
}
