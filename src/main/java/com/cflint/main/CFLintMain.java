package com.cflint.main;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.xml.transform.TransformerException;

import com.cflint.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

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
    private static final String CFLINT = "cflint";
    private static final String FINDBUGS = "findbugs";
    private static final String DISPLAY_THIS_HELP = "display this help";

    private List<String> folder = new ArrayList<>();
    private String filterFile = null;
    private boolean verbose = false;
    private boolean logerror = false;
    private boolean quiet = false;
    private boolean xmlOutput = false;
    private boolean jsonOutput = false;
    private boolean htmlOutput = true;
    private boolean textOutput = false;
    private String xmlOutFile = "cflint-result.xml";
    private String xmlstyle = CFLINT;
    private String htmlOutFile = "cflint-result.html";
    private String htmlStyle = "plain.xsl";
    private String jsonOutFile = "cflint-result.json";
    private String textOutFile = null;
    private CFLintConfig cmdLineConfig = null;
    private CFLintConfig configFileConfig = null;
    private CFLintConfig defaultConfig = null;
    private String extensions;
    private boolean showprogress = false;
    private boolean progressUsesThread = true;
    private Boolean stdIn = false;
    private String stdInFile = "source.cfc";
    private Boolean stdOut = false;
    // private String configfile = null;
    private boolean strictInclude;

    public static void main(final String[] args) throws Exception {
        final Options options = new Options();
        options.addOption(Settings.RULES, false, "list of all supported rules");
        options.addOption(Settings.CONFIG, false, "list of rules in config file");
        options.addOption(Settings.INCLUDE_RULE, true, "specify rules to include");
        options.addOption(Settings.EXCLUDE_RULE, true, "specify rules to exclude");
        options.addOption(Settings.FOLDER, true, "folder(s) to scan");
        options.addOption(Settings.FILE, true, "file(s) to scan");
        options.addOption(Settings.FILTER_FILE, true, "filter file");
        options.addOption(Settings.V, false, "verbose output");
        options.addOption(Settings.VERSION, false, "show the version number");
        options.addOption(Settings.UI, false, "show UI");
        options.addOption(Settings.VERBOSE, false, "verbose output");
        options.addOption(Settings.STRICT_INCLUDE, false, "Check every include and try to parse it");
        options.addOption(Settings.SHOWPROGRESS, false, "show progress bar");
        options.addOption(Settings.SINGLETHREAD, false, "run single threaded");

        options.addOption(Settings.LOGERROR, false, "log parsing errors as bugs");
        options.addOption(Settings.E, false, "log parsing errors as bugs");
        options.addOption(Settings.QUIET, false, "less output");
        options.addOption(Settings.Q, false, "less output");
        options.addOption(Settings.HELP, false, DISPLAY_THIS_HELP);
        options.addOption(Settings.QUESTION_MARK, false, DISPLAY_THIS_HELP);
        options.addOption(Settings.H, false, DISPLAY_THIS_HELP);
        options.addOption(Settings.XML, false, "output in xml format");
        options.addOption(Settings.XMLFILE, true, "specify the output xml file (default: cflint-results.xml)");
        options.addOption(Settings.XMLSTYLE, true, "cflint,findbugs");
        options.addOption(Settings.HTML, false, "output in html format (default)");
        options.addOption(Settings.HTMLFILE, true, "specify the output html file (default: cflint-results.html)");
        options.addOption(Settings.HTMLSTYLE, true, "default,plain"); // fancy,fancy-hist,summary
        options.addOption(Settings.JSON, false, "output in json format");
        options.addOption(Settings.JSONFILE, true, "specify the output json file (default: cflint-results.json)");
        options.addOption(Settings.TEXT, false, "output in plain text");
        options.addOption(Settings.TEXTFILE, true, "specify the output text file (default: cflint-results.txt)");
        options.addOption(Settings.EXTENSIONS, true, "specify the extensions of the CF source files (default: .cfm,.cfc)");
        options.addOption(Settings.CONFIGFILE, true, "specify the location of the config file");
        options.addOption(Settings.STDIN, true, "use stdin for file input (default: source.cfc)");
        options.addOption(Settings.STDOUT, false, "output to stdout only");
        options.addOption(Settings.LIST_RULE_GROUPS, false, "list rule groups");
        options.addOption(Settings.RULE_GROUPS, true, "rule groups");

        final CommandLineParser parser = new GnuParser();
        final CommandLine cmd = parser.parse(options, args);
        final CFLintMain main = new CFLintMain();

        if (cmd.hasOption(Settings.H) || cmd.hasOption(Settings.HELP) || cmd.hasOption(Settings.QUESTION_MARK)) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(CFLINT, options);
            return;
        }
        if (cmd.hasOption(Settings.VERSION)) {
            System.out.println("CFLint " + Version.getVersion());
            System.out.println("CFParser " + cfml.parsing.Version.getVersion());
            return;
        }
        main.strictInclude = cmd.hasOption(Settings.STRICT_INCLUDE);
        if (cmd.hasOption(Settings.RULES) || cmd.hasOption(Settings.CONFIG)) {
            final CFLintPluginInfo pluginInfo = cmd.hasOption(Settings.RULES) ? ConfigUtils.loadDefaultPluginInfo()
                : new CFLintPluginInfo();
            main.defaultConfig = new CFLintConfig();
            main.defaultConfig.setRules(pluginInfo.getRules());

            CFLintChainedConfig myConfig = new CFLintChainedConfig(main.defaultConfig);
            if (cmd.hasOption(Settings.CONFIGFILE)) {
                final String configfile = cmd.getOptionValue(Settings.CONFIGFILE);
                myConfig = myConfig.createNestedConfig(loadConfig(configfile));
            }

            final Map<String, String> descriptions = ConfigUtils.loadDescriptions();
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

        if (cmd.hasOption(Settings.LIST_RULE_GROUPS)) {
            listRuleGroups(pluginInfo);
            return;
        }
        // Load the codes from the rule groups into the includes list, if rule
        // groups are specified
        if (cmd.hasOption(Settings.RULE_GROUPS)) {
            final String rulegroups = cmd.getOptionValue(Settings.RULE_GROUPS);
            applyRuleGroups(main, pluginInfo, rulegroups);
        } else {
            // Exclude the experimental by default.
            applyRuleGroups(main, pluginInfo, "!Experimental");
        }

        main.verbose = (cmd.hasOption(Settings.V) || cmd.hasOption(Settings.VERBOSE));
        main.quiet = (cmd.hasOption(Settings.Q) || cmd.hasOption(Settings.QUIET));
        main.logerror = (cmd.hasOption(Settings.E) || cmd.hasOption(Settings.LOGERROR));
        main.xmlOutput = cmd.hasOption(Settings.XML) || cmd.hasOption(Settings.XMLSTYLE) || cmd.hasOption(Settings.XMLFILE);
        main.textOutput = cmd.hasOption(Settings.TEXT) || cmd.hasOption(Settings.TEXTFILE);
        main.jsonOutput = cmd.hasOption(Settings.JSON) || cmd.hasOption(Settings.JSONFILE);

        if (cmd.hasOption(Settings.UI)) {
            main.ui();
        }
        // If an output is specified, htmlOutput is not defaulted to true.
        if (main.xmlOutput || main.textOutput || main.jsonOutput) {
            main.htmlOutput = cmd.hasOption(Settings.HTML) || cmd.hasOption(Settings.HTMLSTYLE) || cmd.hasOption(Settings.HTMLFILE);
        }

        if (main.verbose) {
            System.out.println("XML Output " + main.xmlOutput);
            System.out.println("Text Output " + main.textOutput);
            System.out.println("JSON Output " + main.jsonOutput);
            System.out.println("HTML Output " + main.htmlOutput);
        }

        if (cmd.hasOption(Settings.FOLDER)) {
            main.folder.addAll(Arrays.asList(cmd.getOptionValue(Settings.FOLDER).split(",")));
        }
        if (cmd.hasOption(Settings.FILE)) {
            main.folder.addAll(Arrays.asList(cmd.getOptionValue(Settings.FILE).split(",")));
        }
        if (cmd.hasOption(Settings.HTMLSTYLE)) {
            main.htmlStyle = cmd.getOptionValue(Settings.HTMLSTYLE);
            if (!main.htmlStyle.endsWith(".xsl") && !main.htmlStyle.endsWith(".xslt")) {
                main.htmlStyle = main.htmlStyle + ".xsl";
            }
        }
        if (cmd.hasOption(Settings.XMLSTYLE)) {
            main.xmlstyle = cmd.getOptionValue(Settings.XMLSTYLE);
        }
        if (cmd.hasOption(Settings.FILTER_FILE)) {
            main.filterFile = cmd.getOptionValue(Settings.FILTER_FILE);
        }
        if (cmd.hasOption(Settings.XMLFILE)) {
            main.xmlOutFile = cmd.getOptionValue(Settings.XMLFILE);
        }
        if (cmd.hasOption(Settings.JSONFILE)) {
            main.jsonOutFile = cmd.getOptionValue(Settings.JSONFILE);
        }
        if (cmd.hasOption(Settings.CONFIGFILE)) {
            final String configfile = cmd.getOptionValue(Settings.CONFIGFILE);
            main.configFileConfig = loadConfig(configfile);
        }
        if (cmd.hasOption(Settings.HTMLFILE)) {
            main.htmlOutFile = cmd.getOptionValue(Settings.HTMLFILE);
        }
        if (cmd.hasOption(Settings.TEXTFILE)) {
            main.textOutFile = cmd.getOptionValue(Settings.TEXTFILE);
        }
        if (cmd.hasOption(Settings.JSONFILE)) {
            main.jsonOutFile = cmd.getOptionValue(Settings.JSONFILE);
        }
        if (cmd.hasOption(Settings.EXTENSIONS)) {
            main.extensions = cmd.getOptionValue(Settings.EXTENSIONS);
        }

        if (cmd.hasOption(Settings.INCLUDE_RULE) || cmd.hasOption(Settings.EXCLUDE_RULE)) {
            main.cmdLineConfig = new CFLintConfig();
            if (cmd.hasOption(Settings.INCLUDE_RULE)) {
                for (final String code : cmd.getOptionValue(Settings.INCLUDE_RULE).split(",")) {
                    main.cmdLineConfig.addInclude(new PluginMessage(code));
                    main.cmdLineConfig.setInheritParent(false);
                }
            }
            if (cmd.hasOption(Settings.EXCLUDE_RULE)) {
                for (final String code : cmd.getOptionValue(Settings.EXCLUDE_RULE).split(",")) {
                    main.cmdLineConfig.addExclude(new PluginMessage(code));
                }
            }
        }
        main.showprogress = cmd.hasOption(Settings.SHOWPROGRESS) || (!cmd.hasOption(Settings.SHOWPROGRESS) && cmd.hasOption(Settings.UI));
        main.progressUsesThread = !cmd.hasOption(Settings.SINGLETHREAD);
        main.stdIn = cmd.hasOption(Settings.STDIN);
        if (main.stdIn) {
            final String stdInOptionValue = cmd.getOptionValue(Settings.STDIN);
            if (stdInOptionValue != null) {
                main.stdInFile = stdInOptionValue;
            }
        }
        main.stdOut = cmd.hasOption(Settings.STDOUT);
        if (main.isValid()) {
            main.execute();
            if (cmd.hasOption(Settings.UI)) {
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
    private static void listRuleGroups(final CFLintPluginInfo pluginInfo) {
        Map<String, PluginMessage> allCodes = new LinkedHashMap<>();
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

        final String[] slist = new String[]{"xml", "html", "text", "txt", "json"};
        final JList<String> list = new JList<>(slist);
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
                CFLintPluginInfo pluginInfo = null;
                if (configfile.toLowerCase().endsWith(".xml")) {
                    final Object configObj = ConfigUtils.unmarshal(new FileInputStream(configfile));
                    if (configObj instanceof CFLintPluginInfo)
                        pluginInfo = (CFLintPluginInfo) configObj;
                    else if (configObj instanceof CFLintConfig) {
                        return (CFLintConfig) configObj;
                    }
                } else {
                    return ConfigUtils.unmarshalJson(new FileInputStream(configfile), CFLintConfig.class);
                }
                CFLintConfig returnVal = new CFLintConfig();
                if (pluginInfo != null)
                    returnVal.setRules(pluginInfo.getRules());
                return returnVal;
            } catch (final Exception e) {
                System.err.println("Unable to load config file. " + e.getMessage());
            }
        }
        return null;
    }

    private void execute() throws IOException, TransformerException, MarshallerException {
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
            final File starterFile = new File(stdInFile);
            if (starterFile.exists() && starterFile.getParentFile().exists()) {
                cflint.setupConfigAncestry(starterFile.getParentFile());
            }
            cflint.process(source.toString(), stdInFile);
        }
        for (BugInfo bug : cflint.getBugs()) {
            cflint.getStats().getCounts().add(bug.getMessageCode(), bug.getSeverity());
        }
        if (xmlOutput) {
            Writer xmlwriter = null;
            try {
                xmlwriter = stdOut ? new OutputStreamWriter(System.out) : createWriter(xmlOutFile, StandardCharsets.UTF_8);
                if (FINDBUGS.equalsIgnoreCase(xmlstyle)) {
                    if (verbose) {
                        display("Writing XML (style: findbugs)" + (stdOut ? "." : " to " + xmlOutFile));
                    }
                    new XMLOutput().outputFindBugs(cflint.getBugs(), xmlwriter, cflint.getStats());
                } else {
                    if (verbose) {
                        display("Writing XML" + (stdOut ? "." : " to " + xmlOutFile));
                    }
                    new DefaultCFlintResultMarshaller().output(cflint.getBugs(), xmlwriter, cflint.getStats());
                }
            } catch (final TransformerException e) {
                throw new TransformerException(e);
            } catch (final IOException e) {
                throw new IOException(e);
            } finally {
                if (xmlwriter != null) {
                    xmlwriter.close();
                }
            }
        }
        if (textOutput) {
            Writer textwriter = null;
            try {
                if (textOutFile != null && verbose) {
                    display("Writing text" + (stdOut ? "." : " to " + textOutFile));
                }
                textwriter = stdOut || textOutFile == null ? new OutputStreamWriter(System.out)
                    : new FileWriter(textOutFile);
                new TextOutput().output(cflint.getBugs(), textwriter, cflint.getStats());
            } catch (final IOException e) {
                throw new IOException(e);
            } finally {
                if (textwriter != null) {
                    textwriter.close();
                }
            }
        }
        if (htmlOutput) {
            Writer htmlwriter = null;
            try {
                htmlwriter = stdOut ? new OutputStreamWriter(System.out) : new FileWriter(htmlOutFile);
                if (verbose) {
                    display("Writing HTML (style: " + htmlStyle + ")" + (stdOut ? "." : " to " + htmlOutFile));
                }
                new HTMLOutput(htmlStyle).output(cflint.getBugs(), htmlwriter, cflint.getStats());
            } catch (final TransformerException e) {
                throw new TransformerException(e);
            } catch (final IOException e) {
                throw new IOException(e);
            } finally {
                if (htmlwriter != null) {
                    htmlwriter.close();
                }
            }
        }
        if (jsonOutput) {
            Writer jsonwriter = null;
            try {
                jsonwriter = stdOut ? new OutputStreamWriter(System.out) : new FileWriter(jsonOutFile);
                if (verbose) {
                    display("Writing JSON" + (stdOut ? "." : " to " + jsonOutFile));
                }
                new JSONOutput().output(cflint.getBugs(), jsonwriter, cflint.getStats());
            } catch (final IOException e) {
                throw new IOException(e);
            } finally {
                if (jsonwriter != null) {
                    jsonwriter.close();
                }
            }
        }
        if (verbose) {
            display("Total files scanned: " + cflint.getStats().getFileCount());
            display("Total LOC scanned: " + cflint.getStats().getTotalLines());
        }
    }

    private CFLintConfiguration buildConfigChain() {
        CFLintChainedConfig myConfig = new CFLintChainedConfig(defaultConfig);
        myConfig = myConfig.createNestedConfig(configFileConfig);
        return myConfig.createNestedConfig(cmdLineConfig);
    }

    protected CFLintFilter createBaseFilter() throws IOException {
        CFLintFilter filter = CFLintFilter.createFilter(verbose);
        if (filterFile != null) {
            final File ffile = new File(filterFile);
            if (ffile.exists()) {
                final FileInputStream fis = new FileInputStream(ffile);
                try {
                    final byte[] b = new byte[fis.available()];
                    if (fis.read(b) > 0) {
                        fis.close();
                        filter = CFLintFilter.createFilter(new String(b), verbose);
                    }
                } catch (final IOException e) {
                    throw new IOException(e);
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                }
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
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(xmlOutFile), encoding);
        try {
            out.append(String.format("<?xml version=\"1.0\" encoding=\"%s\" ?>%n", encoding));
        } catch (final IOException e) {
            throw new IOException(e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return out;
    }
}
