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
import java.util.*;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.apache.commons.cli.*;
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
    private static final String CFLINT_USAGE = "java -jar CFLint-" + Version.getVersion() + "-all.jar";
    private static final String FINDBUGS = "findbugs";
    private static final String DISPLAY_THIS_HELP = "display this help";

    private final List<String> folder = new ArrayList<>();
    private String filterFile = null;
    private boolean verbose = false;
    private boolean logerror = false;
    private boolean quiet = false;
    private boolean debug = false;
    private boolean xmlOutput = false;
    private boolean jsonOutput = false;
    private boolean htmlOutput = true;
    private boolean textOutput = false;
    private String xmlOutFile = "cflint-result.xml";
    private String xmlstyle = CFLINT;
    private String htmlOutFile = "cflint-result.html";
    private String htmlStyle = "plain.xsl";
    private String jsonOutFile = "cflint-result.json";
    private String textOutFile = "cflint-result.txt";
    private String extensions;
    private Boolean stdIn = false;
    private String stdInFile = "source.cfc";
    private Boolean stdOut = false;
    private boolean strictInclude;
	private String environmentName;

    public static void main(final String[] args) throws Exception {
        final Options commandOptions = new Options();
        final Options helpOptions = new Options();

        // documented
        Option optionMARKDOWN = new Option(Settings.MARKDOWN, false, "generate MarkDown of all supported rules");
        Option optionRULES = new Option(Settings.RULES, false, "list of all supported rules");
        Option optionCONFIG = new Option(Settings.CONFIG, false, "list of rules in config file");
        Option optionINCLUDE_RULE = new Option(Settings.INCLUDE_RULE, true, "specify rules to include");
        Option optionEXCLUDE_RULE = new Option(Settings.EXCLUDE_RULE, true, "specify rules to exclude");
        Option optionFOLDER = new Option(Settings.FOLDER, true, "folder(s) to scan");
        Option optionFILE = new Option(Settings.FILE, true, "file(s) to scan");
        Option optionFILTER_FILE = new Option(Settings.FILTER_FILE, true, "filter file");
        Option optionV = new Option(Settings.V, false, "verbose output during linting");
        Option optionVERSION = new Option(Settings.VERSION, false, "show the version number");
        Option optionVERBOSE = new Option(Settings.VERBOSE, false, "verbose output during linting");
        Option optionSTRICT_INCLUDE = new Option(Settings.STRICT_INCLUDE, false, "Check every include and try to parse it");
        Option optionLOGERROR = new Option(Settings.LOGERROR, false, "log parsing errors as bugs");
        Option optionE = new Option(Settings.E, false, "log parsing errors as bugs");
        Option optionQUIET = new Option(Settings.QUIET, false, "quiet mode surpresses most linting error and commentary output");
        Option optionQ = new Option(Settings.Q, false, "quiet mode surpresses most linting error and commentary output");
        Option optionDEBUG = new Option(Settings.DEBUG, false, "debug-level output during linting");
        Option optionD = new Option(Settings.D, false, "debug-level output during linting");
        Option optionHELP = new Option(Settings.HELP, false, DISPLAY_THIS_HELP);
        Option optionQUESTION_MARK = new Option(Settings.QUESTION_MARK, false, DISPLAY_THIS_HELP);
        Option optionH = new Option(Settings.H, false, DISPLAY_THIS_HELP);
        Option optionXML = new Option(Settings.XML, false, "output in xml format");
        Option optionXMLFILE = new Option(Settings.XMLFILE, true, "specify the output xml file (default: cflint-results.xml)");
        Option optionXMLSTYLE = new Option(Settings.XMLSTYLE, true, "cflint,findbugs");
        Option optionHTML = new Option(Settings.HTML, false, "output in html format (default)");
        Option optionHTMLFILE = new Option(Settings.HTMLFILE, true, "specify the output html file (default: cflint-results.html)");
        Option optionHTMLSTYLE = new Option(Settings.HTMLSTYLE, true, "default,plain"); // fancy,fancy-hist,summary
        Option optionJSON = new Option(Settings.JSON, false, "output in json format");
        Option optionJSONFILE = new Option(Settings.JSONFILE, true, "specify the output json file (default: cflint-results.json)");
        Option optionTEXT = new Option(Settings.TEXT, false, "output in plain text");
        Option optionTEXTFILE = new Option(Settings.TEXTFILE, true, "specify the output text file (default: cflint-results.txt)");
        Option optionEXTENSIONS = new Option(Settings.EXTENSIONS, true, "specify the extensions of the CF source files (default: .cfm,.cfc)");
        Option optionSTDIN = new Option(Settings.STDIN, true, "use stdin for file input (default: source.cfc)");
        Option optionSTDOUT = new Option(Settings.STDOUT, false, "output to stdout only");
        Option optionLIST_RULE_GROUPS = new Option(Settings.LIST_RULE_GROUPS, false, "list rule groups");
        Option optionRULE_GROUPS = new Option(Settings.RULE_GROUPS, true, "rule groups");


        // undocumented
        Option optionCONFIGFILE = new Option(Settings.CONFIGFILE, true, "specify the location of the config file");

        // supported options
        commandOptions.addOption(optionMARKDOWN)
                        .addOption(optionRULES)
                        .addOption(optionCONFIG)
                        .addOption(optionINCLUDE_RULE)
                        .addOption(optionEXCLUDE_RULE)
                        .addOption(optionFOLDER)
                        .addOption(optionFILE)
                        .addOption(optionFILTER_FILE)
                        .addOption(optionV)
                        .addOption(optionVERSION)
                        .addOption(optionVERBOSE)
                        .addOption(optionSTRICT_INCLUDE)
                        .addOption(optionLOGERROR)
                        .addOption(optionE)
                        .addOption(optionQUIET)
                        .addOption(optionQ)
                        .addOption(optionHELP)
                        .addOption(optionQUESTION_MARK)
                        .addOption(optionH)
                        .addOption(optionXML)
                        .addOption(optionXMLFILE)
                        .addOption(optionXMLSTYLE)
                        .addOption(optionHTML)
                        .addOption(optionHTMLFILE)
                        .addOption(optionHTMLSTYLE)
                        .addOption(optionJSON)
                        .addOption(optionJSONFILE)
                        .addOption(optionTEXT)
                        .addOption(optionTEXTFILE)
                        .addOption(optionEXTENSIONS)
                        .addOption(optionSTDIN)
                        .addOption(optionSTDOUT)
                        .addOption(optionLIST_RULE_GROUPS)
                        .addOption(optionRULE_GROUPS)
                        .addOption(optionCONFIGFILE)
                        .addOption(optionDEBUG)
                        .addOption(optionD);

        // documented options for HelpFormatter
        helpOptions.addOption(optionMARKDOWN)
                        .addOption(optionRULES)
                        .addOption(optionCONFIG)
                        .addOption(optionINCLUDE_RULE)
                        .addOption(optionEXCLUDE_RULE)
                        .addOption(optionFOLDER)
                        .addOption(optionFILE)
                        .addOption(optionFILTER_FILE)
                        .addOption(optionV)
                        .addOption(optionVERSION)
                        .addOption(optionVERBOSE)
                        .addOption(optionSTRICT_INCLUDE)
                        .addOption(optionLOGERROR)
                        .addOption(optionE)
                        .addOption(optionQUIET)
                        .addOption(optionQ)
                        .addOption(optionHELP)
                        .addOption(optionQUESTION_MARK)
                        .addOption(optionH)
                        .addOption(optionXML)
                        .addOption(optionXMLFILE)
                        .addOption(optionXMLSTYLE)
                        .addOption(optionHTML)
                        .addOption(optionHTMLFILE)
                        .addOption(optionHTMLSTYLE)
                        .addOption(optionJSON)
                        .addOption(optionJSONFILE)
                        .addOption(optionTEXT)
                        .addOption(optionTEXTFILE)
                        .addOption(optionEXTENSIONS)
                        .addOption(optionSTDIN)
                        .addOption(optionSTDOUT)
                        .addOption(optionLIST_RULE_GROUPS)
                        .addOption(optionRULE_GROUPS)
                        .addOption(optionDEBUG)
                        .addOption(optionD);

        final CommandLineParser parser = new GnuParser();
        final CommandLine cmd = parser.parse(commandOptions, args);
        final CFLintCLI main = new CFLintCLI();

        if (cmd.hasOption(Settings.H) || cmd.hasOption(Settings.HELP) || cmd.hasOption(Settings.QUESTION_MARK)) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(CFLINT_USAGE, helpOptions);
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
            System.out.println("Supported rules:");
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
            System.out.println("Rules written to RULES.MD");
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
        if(cmd.hasOption(Settings.ENVIRONMENT)) {
        	main.environmentName=cmd.getOptionValue(Settings.ENVIRONMENT);
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

        main.quiet = (cmd.hasOption(Settings.Q) || cmd.hasOption(Settings.QUIET));

        main.verbose = (cmd.hasOption(Settings.V) || cmd.hasOption(Settings.VERBOSE));
        if (main.verbose) {
            System.out.println("Verbose is enabled");
        }

        main.debug = (cmd.hasOption(Settings.D) || cmd.hasOption(Settings.DEBUG));
        if (main.debug) {
            System.out.println("Debug is enabled");
            // Setting verbose = true and quiet = false in debug mode, regardless of settings actually being passed in.
            main.verbose = true;
            main.quiet = false;
        }

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
            formatter.printHelp(CFLINT_USAGE, helpOptions);
        }
    }

    private void execute(final CFLintConfiguration cfLintConfig) throws IOException, TransformerException,
            MarshallerException, JAXBException, CFLintScanException, CFLintConfigurationException {
        final CFLintAPI api = new CFLintAPI(cfLintConfig);
        api.setVerbose(verbose);
        api.setLogError(logerror);
        api.setQuiet(quiet);
        api.setDebug(debug);
        api.setStrictInclude(strictInclude);
        api.setEnvironmentName(environmentName);
        if (extensions != null && extensions.trim().length() > 0) {
            try {
                api.setExtensions(Arrays.asList(extensions.trim().split(",")));
            } catch (final Exception e) {
                if (!quiet) {
                    System.err.println("Unable to use extensions (" + extensions + "), using default instead. " + e.getMessage());
                }
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
            try (final Writer textwriter = stdOut ? new OutputStreamWriter(System.out) : new FileWriter(textOutFile)) {
                if (verbose) {
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
            System.err.println("Set -folder, -file or -stdin to use CFLint");
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
