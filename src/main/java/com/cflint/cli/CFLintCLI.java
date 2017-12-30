package com.cflint.cli;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.io.IOUtils;

import com.cflint.Version;
import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.cli.Settings;
import com.cflint.config.CFLintConfiguration;
import com.cflint.config.CFLintPluginInfo;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.CFLintPluginInfo.RuleGroup;
import com.cflint.config.ConfigBuilder;
import com.cflint.config.ConfigUtils;
import com.cflint.exception.CFLintConfigurationException;
import com.cflint.exception.CFLintScanException;
import com.cflint.tools.CFLintDoc;
import com.cflint.xml.MarshallerException;

public class CFLintCLI {
    private static final String CFLINT = "cflint";
    private static final String FINDBUGS = "findbugs";
    private static final String DISPLAY_THIS_HELP = "display this help";

    private final List<String> folder = new ArrayList<>();
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
    private String extensions;
    private Boolean stdIn = false;
    private String stdInFile = "source.cfc";
    private Boolean stdOut = false;
    private boolean strictInclude;

    public static void main(final String[] args) throws Exception {
        final Options options = new Options();
        options.addOption(Settings.MARKDOWN, false, "generate MarkDown of all supported rules");
        options.addOption(Settings.RULES, false, "list of all supported rules");
        options.addOption(Settings.CONFIG, false, "list of rules in config file");
        options.addOption(Settings.INCLUDE_RULE, true, "specify rules to include");
        options.addOption(Settings.EXCLUDE_RULE, true, "specify rules to exclude");
        options.addOption(Settings.FOLDER, true, "folder(s) to scan");
        options.addOption(Settings.FILE, true, "file(s) to scan");
        options.addOption(Settings.FILTER_FILE, true, "filter file");
        options.addOption(Settings.V, false, "verbose output");
        options.addOption(Settings.VERSION, false, "show the version number");
        options.addOption(Settings.VERBOSE, false, "verbose output");
        options.addOption(Settings.STRICT_INCLUDE, false, "Check every include and try to parse it");

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
        options.addOption(Settings.EXTENSIONS, true,
                "specify the extensions of the CF source files (default: .cfm,.cfc)");
        options.addOption(Settings.CONFIGFILE, true, "specify the location of the config file");
        options.addOption(Settings.STDIN, true, "use stdin for file input (default: source.cfc)");
        options.addOption(Settings.STDOUT, false, "output to stdout only");
        options.addOption(Settings.LIST_RULE_GROUPS, false, "list rule groups");
        options.addOption(Settings.RULE_GROUPS, true, "rule groups");

        final CommandLineParser parser = new GnuParser();
        final CommandLine cmd = parser.parse(options, args);
        final CFLintCLI main = new CFLintCLI();

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
            final ConfigBuilder configBuilder = new ConfigBuilder(
                    cmd.hasOption(Settings.RULES) ? ConfigUtils.loadDefaultPluginInfo() : new CFLintPluginInfo());
            if (cmd.hasOption(Settings.CONFIGFILE)) {
                final String configfile = cmd.getOptionValue(Settings.CONFIGFILE);
                try {
                    configBuilder.addCustomConfig(configfile);
                } catch (final Exception e) {
                    System.err.println("Unable to load config file " + configfile + ".  " + e.getMessage());
                }
            }
            final Map<String, String> descriptions = ConfigUtils.loadDescriptions();
            System.out.println("Supported rules");
            for (final PluginInfoRule rule : configBuilder.build().getRules()) {
                System.out.println("  " + rule.getName());
                for (final PluginMessage message : rule.getMessages()) {
                    System.out.println("    " + message.getCode() + " - " + descriptions.get(message.getCode()));
                }
            }
            return;
        }

        final CFLintPluginInfo pluginInfo = ConfigUtils.loadDefaultPluginInfo();
        final ConfigBuilder configBuilder = new ConfigBuilder(pluginInfo);
        if (cmd.hasOption(Settings.MARKDOWN)){
            final FileWriter out = new FileWriter("RULES.MD");
            CFLintDoc.generateRuleMarkDown(pluginInfo, new PrintWriter(out));
            System.err.println("Rules written to RULES.MD");
            out.close();
            return;
        }
        if (cmd.hasOption(Settings.CONFIGFILE)) {
            final String configfile = cmd.getOptionValue(Settings.CONFIGFILE);
            try {
                configBuilder.addCustomConfig(configfile);
            } catch (final Exception e) {
                System.err.println("Unable to load config file " + configfile + ".  " + e.getMessage());
            }
        }
        if (cmd.hasOption(Settings.INCLUDE_RULE)) {
            configBuilder.include(Arrays.asList(cmd.getOptionValue(Settings.INCLUDE_RULE).split(",")));
        }
        if (cmd.hasOption(Settings.EXCLUDE_RULE)) {
            configBuilder.exclude(Arrays.asList(cmd.getOptionValue(Settings.EXCLUDE_RULE).split(",")));
        }

        if (cmd.hasOption(Settings.LIST_RULE_GROUPS)) {
            CFLintDoc.generateRuleGroup(pluginInfo, new PrintWriter(System.out));
            return;
        }
        // Load the codes from the rule groups into the includes list, if rule
        // groups are specified
        if (cmd.hasOption(Settings.RULE_GROUPS)) {
            final String rulegroups = cmd.getOptionValue(Settings.RULE_GROUPS);
            configBuilder.ruleGroups(rulegroups);
        }

        main.verbose = (cmd.hasOption(Settings.V) || cmd.hasOption(Settings.VERBOSE));
        main.quiet = (cmd.hasOption(Settings.Q) || cmd.hasOption(Settings.QUIET));
        main.logerror = (cmd.hasOption(Settings.E) || cmd.hasOption(Settings.LOGERROR));
        main.xmlOutput = cmd.hasOption(Settings.XML) || cmd.hasOption(Settings.XMLSTYLE)
                || cmd.hasOption(Settings.XMLFILE);
        main.textOutput = cmd.hasOption(Settings.TEXT) || cmd.hasOption(Settings.TEXTFILE);
        main.jsonOutput = cmd.hasOption(Settings.JSON) || cmd.hasOption(Settings.JSONFILE);

        // If an output is specified, htmlOutput is not defaulted to true.
        if (main.xmlOutput || main.textOutput || main.jsonOutput) {
            main.htmlOutput = cmd.hasOption(Settings.HTML) || cmd.hasOption(Settings.HTMLSTYLE)
                    || cmd.hasOption(Settings.HTMLFILE);
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
        main.stdIn = cmd.hasOption(Settings.STDIN);
        if (main.stdIn) {
            final String stdInOptionValue = cmd.getOptionValue(Settings.STDIN);
            if (stdInOptionValue != null) {
                main.stdInFile = stdInOptionValue;
            }
        }
        main.stdOut = cmd.hasOption(Settings.STDOUT);
        if (main.isValid()) {
            main.execute(configBuilder.build());
        } else {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(CFLINT, options);
        }
    }

    private void execute(final CFLintConfiguration cfLintConfig) throws IOException, TransformerException,
            MarshallerException, JAXBException, CFLintScanException, CFLintConfigurationException {
        final CFLintAPI api = new CFLintAPI(cfLintConfig);
        api.setVerbose(verbose);
        api.setLogError(logerror);
        api.setQuiet(quiet);
        api.setStrictInclude(strictInclude);
        if (extensions != null && extensions.trim().length() > 0) {
            try {
                api.setExtensions(Arrays.asList(extensions.trim().split(",")));
            } catch (final Exception e) {
                System.err.println(
                        "Unable to use extensions (" + extensions + ") using default instead. " + e.getMessage());
            }
        }
        api.setFilterFile(filterFile);

        CFLintResult lintResult = null;
        if (stdIn) {
            final StringWriter source = new StringWriter();
            IOUtils.copy(new InputStreamReader(System.in), source);
            lintResult = api.scan(source.toString(), stdInFile);
        } else {
            lintResult = api.scan(folder);
        }
        if (xmlOutput) {
            try (final Writer xmlwriter = stdOut ? new OutputStreamWriter(System.out)
                    : createXMLWriter(xmlOutFile, StandardCharsets.UTF_8)) {
                if (FINDBUGS.equalsIgnoreCase(xmlstyle)) {
                    if (verbose) {
                        display("Writing XML (style: findbugs)" + (stdOut ? "." : " to " + xmlOutFile));
                    }
                    lintResult.writeFindBugsXml(xmlwriter);
                } else {
                    if (verbose) {
                        display("Writing XML" + (stdOut ? "." : " to " + xmlOutFile));
                    }
                    lintResult.writeXml(xmlwriter);
                }
            }
        }
        if (textOutput) {
            try (final Writer textwriter = stdOut || textOutFile == null ? new OutputStreamWriter(System.out)
                    : new FileWriter(textOutFile)) {
                if (textOutFile != null && verbose) {
                    display("Writing text" + (stdOut ? "." : " to " + textOutFile));
                }
                lintResult.writeText(textwriter);
            }
        }
        if (htmlOutput) {
            try (final Writer htmlwriter = stdOut ? new OutputStreamWriter(System.out) : new FileWriter(htmlOutFile)) {
                if (verbose) {
                    display("Writing HTML (style: " + htmlStyle + ")" + (stdOut ? "." : " to " + htmlOutFile));
                }
                lintResult.writeHTML(htmlStyle, htmlwriter);
            }
        }
        if (jsonOutput) {
            try (final Writer jsonwriter = stdOut ? new OutputStreamWriter(System.out) : new FileWriter(jsonOutFile)) {
                if (verbose) {
                    display("Writing JSON" + (stdOut ? "." : " to " + jsonOutFile));
                }
                lintResult.writeJSON(jsonwriter);
            }
        }
        if (verbose) {
            display("Total files scanned: " + lintResult.getStats().getFileCount());
            display("Total LOC scanned: " + lintResult.getStats().getTotalLines());
        }
    }

    private void display(final String text) {
        if (verbose) {
            System.out.println(text);
        }
    }

    private boolean isValid() {
        if (folder.isEmpty() && !stdIn) {
            System.err.println("Set -folder or -stdin to use CFLint");
            return false;
        }
        return true;
    }

    private Writer createXMLWriter(final String xmlOutFile, final Charset encoding) throws IOException {
        final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(xmlOutFile), encoding);
        try {
            out.append(String.format("<?xml version=\"1.0\" encoding=\"%s\" ?>%n", encoding));
        } catch (final IOException e) {
            throw new IOException(e);
        }
        return out;
    }

}
