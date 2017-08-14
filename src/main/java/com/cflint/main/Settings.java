package com.cflint.main;

public class Settings {
    /**
     * More verbose output.
     */
    public static final String VERBOSE = "verbose";

    /**
     * More verbose output (short option).
     */
    public static final String V = "v";

    /**
     * Less output.
     */
    public static final String QUIET = "quiet";
    
    /**
     * Less output (short option).
     */
    public static final String Q = "Q";

    /**
     * Full list of all supported rules.
     */
    public static final String RULES = "rules";

    /**
     *  List of rules to currently check for.
     */
    public static final String CONFIG = "config";

    /**
     * Coma separated list of additional rules to check for.
     */
    public static final String INCLUDE_RULE = "includeRule";

    /**
     * Coma separated list of rules to ignore.
     */
    public static final String EXCLUDE_RULE = "excludeRule";

    /**
     * Folder(s) to scan for files.
     */
    public static final String FOLDER = "folder";

    /**
     * TODO
     */
    public static final String FILTER_FILE = "filterFile";

    /**
     * Show programs as the scan takes place.
     */
    public static final String SHOWPROGRESS = "showprogress";

    /**
     * Output to a named XML file rather than the default cflint-results.xml.
     */
    public static final String XMLFILE = "xmlfile";

    /**
     * Format of the XML output findbugs or cflint.
     */
    public static final String XMLSTYLE = "xmlstyle";

    /**
     * Output to a named HTML file rather than the default cflint-results.html.
     */
    public static final String HTMLFILE = "htmlfile";

    /**
     * Format of the HTML file default or plain.
     */
    public static final String HTMLSTYLE = "htmlstyle";

    /**
     * Output to a named XML file rather than the default cflint-results.json.
     */
    public static final String JSONFILE = "jsonfile";

    /**
     * Output to a named text file rather than the default cflint-results.txt.
     */
    public static final String TEXTFILE = "textfile";

    /**
     * Extensions of the CF source files to scan.
     */
    public static final String EXTENSIONS = "extensions";

    /**
     *  XML or JSON file containing rule config.
     */
    public static final String CONFIGFILE = "configfile";

    /**
     * TODO
     */
    public static final String STRICT_INCLUDE = "strictinclude";

    /**
     * Scan on standard input.
     */
    public static final String STDIN = "stdin";

    /**
     * Output to standard output only.
     */
    public static final String STDOUT = "stdout";

    /**
     * File(s) to scan.
     */
    public static final String FILE = "file";

    /**
     * Output version numbers of CFLint and CFParser.
     */
    public static final String VERSION = "version";

    /**
     * Only use a single thread.
     */
    public static final String SINGLETHREAD = "singlethread";

    /**
     * Log errors.
     */
    public static final String LOGERROR = "logerror";

    /**
     * Log errors (short option).
     */
    public static final String E = "E";

    /**
     * Show a simpe UI letting you select files to scan.
     */
    public static final String UI = "ui";

    /**
     * Output in XML format.
     */
    public static final String XML = "xml";

    /**
     * Output in text format.
     */
    public static final String TEXT = "text";

    /**
     * Output in HTML format.
     */
    public static final String HTML = "html";

    /**
     * Output in JSON format.
     */
    public static final String JSON = "json";

    /**
     * Display help on all the options.
     */
    public static final String HELP = "help";

    /**
     * Display help on all the options (short version).
     */
    public static final String QUESTION_MARK = "?";

    /**
     * Display help on all the options (short version).
     */
    public static final String H = "h";

    /**
     * List all rules orgered via teh groups they are in.
     */
    public static final String LIST_RULE_GROUPS = "listrulegroups";

    /**
     * Only use rules in the provided rule group(s).
     */
    public static final String RULE_GROUPS = "rulegroups";

    /**
     * Cannot be constructed.
     */
    private Settings() {
      throw new IllegalStateException("Settings utility class");
    }
}
