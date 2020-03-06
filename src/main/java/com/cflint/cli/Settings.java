package com.cflint.cli;

public class Settings {
    /**
     * verbose output during linting
     */
    public static final String VERBOSE = "verbose";

    /**
     * verbose output during linting
     */
    public static final String V = "v";

    /**
     * quiet mode surpresses most linting error and commentary output
     */
    public static final String QUIET = "quiet";

    /**
     * quiet mode surpresses most linting error and commentary output
     */
    public static final String Q = "q";

    /**
     * debug-level output during linting
     */
    public static final String DEBUG = "debug";

    /**
     * debug-level output during linting
     */
    public static final String D = "d";

    /**
     * Full list of all supported rules.
     */
    public static final String RULES = "rules";

    /**
     * Generate MARKDOWN of all supported rules.
     */
    public static final String MARKDOWN = "markdown";

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
     * Folder(s) to scan for files.
     */
    public static final String ENVIRONMENT = "environment";

    /**
     * Filter file.
     */
    public static final String FILTER_FILE = "filterFile";

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
     * Check every CF include and try to parse it's contents as well.
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
     * Log errors.
     */
    public static final String LOGERROR = "logerror";

    /**
     * Log errors (short option).
     */
    public static final String E = "e";

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
