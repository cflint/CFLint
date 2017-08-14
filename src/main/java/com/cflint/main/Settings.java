package com.cflint.main;

public class Settings {
    public static final String VERBOSE = "verbose";
    public static final String V = "v";
    public static final String QUIET = "quiet";
    public static final String Q = "Q";
    public static final String RULES = "rules";
    public static final String CONFIG = "config";
    public static final String INCLUDE_RULE = "includeRule";
    public static final String EXCLUDE_RULE = "excludeRule";
    public static final String FOLDER = "folder";
    public static final String FILTER_FILE = "filterFile";
    public static final String SHOWPROGRESS = "showprogress";
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
    public static final String STDOUT = "stdout";
    public static final String FILE = "file";
    public static final String VERSION = "version";
    public static final String SINGLETHREAD = "singlethread";
    public static final String LOGERROR = "logerror";
    public static final String E = "E";
    public static final String UI = "ui";
    public static final String XML = "xml";
    public static final String TEXT = "text";
    public static final String HTML = "html";
    public static final String JSON = "json";
    public static final String QUESTION_MARK = "?";
    public static final String H = "h";
    public static final String HELP = "help";
    public static final String LIST_RULE_GROUPS = "listrulegroups";
    public static final String RULE_GROUPS = "rulegroups";

    /**
     * Cannot be constructed.
     */
    private Settings() {
      throw new IllegalStateException("Settings utility class");
    }
}
