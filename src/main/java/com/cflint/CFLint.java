package com.cflint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.runtime.BitSet;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import com.cflint.BugInfo.BugInfoBuilder;
import com.cflint.config.CFLintChainedConfig;
import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintConfiguration;
import com.cflint.config.CFLintPluginInfo;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginParameter;
import com.cflint.config.ConfigUtils;
import com.cflint.exception.CFLintScanException;
import com.cflint.listeners.ScanProgressListener;
import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.CFLintSet;
import com.cflint.plugins.CFLintStructureListener;
import com.cflint.plugins.Context;
import com.cflint.plugins.Context.ContextMessage;
import com.cflint.plugins.Context.ContextType;
import com.cflint.plugins.core.CFScopes;
import com.cflint.plugins.exceptions.CFLintExceptionListener;
import com.cflint.plugins.exceptions.DefaultCFLintExceptionListener;
import com.cflint.tools.AllowedExtensionsLoader;
import com.cflint.tools.CFLintFilter;
import com.cflint.tools.CFMLTagInfo;
import com.cflint.tools.CommentReformatting;
import com.cflint.tools.FileUtil;
import com.cflint.tools.PrecedingCommentReader;
import com.cflint.tools.ScanningProgressMonitorLookAhead;

import cfml.CFSCRIPTLexer;
import cfml.CFSCRIPTParser;
import cfml.parsing.CFMLParser;
import cfml.parsing.CFMLSource;
import cfml.parsing.ParserTag;
import cfml.parsing.cfscript.CFArrayExpression;
import cfml.parsing.cfscript.CFAssignmentExpression;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFLiteral;
import cfml.parsing.cfscript.CFMember;
import cfml.parsing.cfscript.CFNewExpression;
import cfml.parsing.cfscript.CFStatement;
import cfml.parsing.cfscript.CFStringExpression;
import cfml.parsing.cfscript.CFStructElementExpression;
import cfml.parsing.cfscript.CFTernaryExpression;
import cfml.parsing.cfscript.CFVarDeclExpression;
import cfml.parsing.cfscript.script.CFCase;
import cfml.parsing.cfscript.script.CFCatchStatement;
import cfml.parsing.cfscript.script.CFCompDeclStatement;
import cfml.parsing.cfscript.script.CFCompoundStatement;
import cfml.parsing.cfscript.script.CFExpressionStatement;
import cfml.parsing.cfscript.script.CFForInStatement;
import cfml.parsing.cfscript.script.CFForStatement;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFIfStatement;
import cfml.parsing.cfscript.script.CFIncludeStatement;
import cfml.parsing.cfscript.script.CFPropertyStatement;
import cfml.parsing.cfscript.script.CFReturnStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import cfml.parsing.cfscript.script.CFSwitchStatement;
import cfml.parsing.cfscript.script.CFTryCatchStatement;
import cfml.parsing.cfscript.script.CFWhileStatement;
import cfml.parsing.reporting.ArrayErrorListener;
import cfml.parsing.reporting.IErrorReporter;
import cfml.parsing.reporting.ParseException;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.EndTag;
import net.htmlparser.jericho.Source;

public class CFLint implements IErrorReporter {

    // constants
    private static final String FILE_ERROR = "FILE_ERROR";
    private static final String PARSE_ERROR = "PARSE_ERROR";
    private static final String PLUGIN_ERROR = "PLUGIN_ERROR";
    private static final String MISSING_SEMI = "MISSING_SEMI";
    private static final String AVOID_EMPTY_FILES = "AVOID_EMPTY_FILES";
    private static final String RESOURCE_BUNDLE_NAME = "com.cflint.cflint";

    private CFMLTagInfo tagInfo;
    private CFMLParser cfmlParser = new CFMLParser();
    private StackHandler handler = new StackHandler();
    private BugList bugs;
    private final List<CFLintScanner> extensions = new ArrayList<>();
    private final List<String> allowedExtensions = new ArrayList<>();
    private boolean verbose = false;
    private boolean logError = false;
    private boolean quiet = false;
    private boolean debug = false;
    private boolean showProgress = false;
    private boolean progressUsesThread = true;
    private CFLintStats stats = new CFLintStats();
    private final List<ScanProgressListener> scanProgressListeners = new ArrayList<>();
    private final List<CFLintExceptionListener> exceptionListeners = new ArrayList<>();
    private CFLintConfiguration configuration;
    private int skipToPosition = 0;
    private String currentFile = null;
    private String environmentName="";
	private Element currentElement = null;
    private boolean strictInclude;
    private Set<List<Object>> processed = new HashSet<>();

    // Stack to store include file depth to ensure no recursion
    private final Stack<File> includeFileStack = new Stack<>();
    private int[] lineOffsets;

    public CFLint(final CFLintConfiguration configFile) throws IOException {
        final CFLintFilter filter = CFLintFilter.createFilter(verbose);
        bugs = new BugList(filter);
        cfmlParser.setErrorReporter(this);
        tagInfo = new CFMLTagInfo(cfmlParser.getDictionary());
        setConfiguration(configFile);
        if (exceptionListeners.isEmpty()) {
            addExceptionListener(new DefaultCFLintExceptionListener(bugs));
        }
    }

    public void setConfiguration(final CFLintConfiguration configFile) throws IOException {
        configuration = configFile == null ? new CFLintConfig() : configFile;
        extensions.clear();
        allowedExtensions.clear();
        scanProgressListeners.clear();
        exceptionListeners.clear();
        processed.clear();
        for (final PluginInfoRule ruleInfo : configuration.getRules()) {
            addScanner(ConfigUtils.loadPlugin(ruleInfo));// TODO load them all
        }
        allowedExtensions.addAll(AllowedExtensionsLoader.init(RESOURCE_BUNDLE_NAME));
        bugs.clearBugList();
    }

    @Deprecated
    public CFLint(final CFLintConfiguration configuration, final CFLintScanner... bugsScanners) {
        super();
        this.configuration = configuration;
        for (final CFLintScanner scanner : bugsScanners) {
            extensions.add(scanner);
            if (configuration != null) {
                final PluginInfoRule ruleInfo = configuration.getRuleByClass(scanner.getClass());
                if (ruleInfo != null) {
                    ruleInfo.setPluginInstance(scanner);
                }
            }
            if (scanner instanceof CFLintSet) {
                ((CFLintSet) scanner).setCFLint(this);
            }
        }
        final CFLintFilter filter;
        try {
            filter = CFLintFilter.createFilter(verbose);
            bugs = new BugList(filter);
        } catch (final IOException e1) {
            e1.printStackTrace();
        }
        if (exceptionListeners.isEmpty()) {
            addExceptionListener(new DefaultCFLintExceptionListener(bugs));
        }
        allowedExtensions.addAll(AllowedExtensionsLoader.init(RESOURCE_BUNDLE_NAME));
        cfmlParser.setErrorReporter(this);
        tagInfo = new CFMLTagInfo(cfmlParser.getDictionary());
    }

    public CFLintStats getStats() {
        return stats;
    }

    public void scan(final String folderName) {
        if (showProgress) {
            ScanningProgressMonitorLookAhead.createInstance(this, folderName, progressUsesThread).startPreScan();
        }
        final File starterFile = new File(folderName);
        setupConfigAncestry(starterFile.getAbsoluteFile().getParentFile());
        scan(starterFile);
        fireClose();
    }

    public void setupConfigAncestry(File folder) {
        final Stack<CFLintConfig> configFiles = new Stack<>();
        fileLoop: while (folder != null && folder.exists()) {
            for (final File file : folder.listFiles()) {
                if (file.getName().toLowerCase().equals(".cflintrc" + getEnvSuffix())) {
                    if (verbose) {
                        System.out.println("read config " + file);
                    }
                    try {
                        @SuppressWarnings("deprecation")
                        final CFLintConfig newConfig = file.getName().toLowerCase().endsWith(".xml")
                                ? ConfigUtils.unmarshal(file, CFLintConfig.class)
                                : ConfigUtils.unmarshalJson(new FileInputStream(file), CFLintConfig.class);
                        configFiles.push(newConfig);
                        if (!newConfig.isInheritParent()) {
                            break fileLoop;
                        }
                    } catch (final Exception e) {
                        if (!quiet) {
                            System.err.println("Could not read config file " + file + ". Check for duplicates.");
                        }
                    }
                }
            }
            folder = folder.getParentFile();
        }
        for (final CFLintConfig newConfig : configFiles) {
            configuration = new CFLintChainedConfig(newConfig, configuration);
        }
    }

    private String getEnvSuffix() {
		return environmentName.equals("")?"":"-" + environmentName;
	}

	public void scan(final File folderOrFile) {
        if (debug) {
            System.out.println("Current file: " + folderOrFile.getAbsolutePath());
        }
        if (getBugs().getFileFilter() != null && !getBugs().getFileFilter().includeFile(folderOrFile)) {
            return;
        }
        if (!folderOrFile.exists()) {
            System.err.println("File " + folderOrFile + " does not exist.");
            return;
        }
        if (folderOrFile.isDirectory()) {
            if (folderOrFile.getName().startsWith(".") && !folderOrFile.getName().equals(".")){
                if(verbose) {
                	System.out.println("Skipping folder and its children: " + folderOrFile.getAbsolutePath());
                }
                return;
            }
            final CFLintConfiguration saveConfig = configuration;
            try {
                for (final File file : folderOrFile.listFiles()) {
                    if (file.getName().toLowerCase().equals(".cflintrc" + getEnvSuffix())) {
                        try {
                            if (verbose) {
                                System.out.println("read config " + file);
                            }
                            @SuppressWarnings("deprecation")
                            final CFLintConfiguration newConfig = file.getName().toLowerCase().endsWith(".xml")
                                    ? ConfigUtils.unmarshal(file, CFLintConfig.class)
                                    : ConfigUtils.unmarshalJson(new FileInputStream(file), CFLintConfig.class);
                            configuration = new CFLintChainedConfig(newConfig, configuration);
                        } catch (final Exception e) {
                            System.err.println("Could not read config file " + file);
                        }
                    }
                }
                for (final File file : folderOrFile.listFiles()) {
                    scan(file);
                }
            } finally {
                configuration = saveConfig;
            }
        } else if (!folderOrFile.isHidden() && FileUtil.checkExtension(folderOrFile, allowedExtensions)) {
            if (!debug && verbose) {
                System.out.println("Current file: " + folderOrFile.getAbsolutePath());
            }
            final String src = FileUtil.loadFile(folderOrFile);
            includeFileStack.clear();
            try {
                // Report number of lines in the source
                stats.addFile(src == null || src.length() == 0 ? 0 : src.split("\\R").length + 1);
                process(src, folderOrFile.getAbsolutePath());
            } catch (final Exception e) {
                printException(e);
                if (logError) {
                    System.err.println("Logging Error: " + FILE_ERROR);
                    fireCFLintException(e, FILE_ERROR, folderOrFile.getAbsolutePath(), null, null, null, null);
                }
            }
        }
    }

    protected void printException(final Exception e, final Element... elem) {
        e.printStackTrace();
        if (!quiet) {
            if (elem != null && elem.length > 0 && elem[0] != null) {
                final int line = elem[0].getSource().getRow(elem[0].getBegin());
                System.err.println("Error in: " + shortSource(elem[0].getSource(), line) + " @ " + line + ":");
            }
            if (verbose) {
                e.printStackTrace(System.err);
            } else {
                System.err.println("Error: \"" + e.getMessage() + "\" Location: " + (e.getStackTrace().length > 0 ? e.getStackTrace()[0] : "''"));
            }
        }
    }

    public void process(final String src, final String filename) throws CFLintScanException {
        try{
            fireStartedProcessing(filename);
            lineOffsets = null;
            if (src == null || src.trim().length() == 0) {
                final Context context = new Context(filename, null, null, false, handler,configuration);
                reportRule(null, null, context, null, new ContextMessage(AVOID_EMPTY_FILES, null));
            } else {
                lineOffsets = getLineOffsets(src.split("\n"));
                final CFMLSource cfmlSource = new CFMLSource(src.contains("<!---") ? CommentReformatting.wrap(src) : src);
                final ParserTag firstTag = getFirstTagQuietly(cfmlSource);
                final List<Element> elements = new ArrayList<>();
                if (firstTag != null) {
                    elements.addAll(cfmlSource.getChildElements());
                }
                if (isComponentOrInterfaceScript(src, elements)) {
                    // Check if pure cfscript
                    final CFScriptStatement scriptStatement = cfmlParser.parseScript(src);
                    final Context context = new Context(filename, null, null, false, handler, scriptStatement==null?null:scriptStatement.getTokens(),configuration);
                    process(scriptStatement, context);
                } else {
                    processStack(elements, " ", filename, null);
                }
            }
            fireFinishedProcessing(filename);
        }catch(final Exception e){
            throw new CFLintScanException(e);
        }
    }

    private int[] getLineOffsets(final String[] lines) {
        final int[] offsets = new int[lines.length];
        int charCount = 0;
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = charCount;
            charCount += lines[i].length() + 1;
        }
        return offsets;
    }

    /**
     * 'Detect' if this is a pure cfscript component or interface
     * 
     * @param src
     * @param elements
     * @return
     */
    private boolean isComponentOrInterfaceScript(final String src, final List<Element> elements) {
        return (src.contains(CF.COMPONENT)
                && (elements.isEmpty() || elements.get(0).getBegin() > src.indexOf(CF.COMPONENT)))
                || (src.contains(CF.INTERFACE)
                        && (elements.isEmpty() || elements.get(0).getBegin() > src.indexOf(CF.INTERFACE)));
    }

    protected ParserTag getFirstTagQuietly(final CFMLSource cfmlSource) {
        try {
            return cfmlSource.getNextTag(0);
        } catch (final Exception e) {
            if (verbose) {
                System.out.println("Unable to find a CFML tag in source.  This is not a hard error.");
                e.printStackTrace();
            }
        }
        return null;
    }

    public void processStack(final List<Element> elements, final String space, final String filename,
            final CFIdentifier functionName) throws IOException, ParseException, CFLintScanException {
        Element commentElement = null;
        for (final Element elem : elements) {
            if (elem.getName().equals(CF.COMMENT)) {
                commentElement = elem;
            } else {
                final Context context = new Context(filename, elem, functionName, false, handler,configuration);
                if (commentElement != null) {
                    applyRuleOverrides(context, commentElement);
                    commentElement = null;
                }
                process(elem, space, context);
            }
        }
    }

    public void processStack(final List<Element> elements, final String space, final Context context)
            throws CFLintScanException {
        Element commentElement = null;
        for (final Element elem : elements) {
            if (elem.getName().equals(CF.COMMENT)) {
                commentElement = elem;
            } else {
                final Context subContext = context.subContext(elem);
                if (commentElement != null) {
                    applyRuleOverrides(subContext, commentElement);
                    commentElement = null;
                }
                process(elem, space, subContext);
            }
        }
    }

    private void process(final Element elem, final String space, final Context context)
            throws CFLintScanException {
        if (skipToPosition > elem.getBegin()) {
            return;
        } else {
            skipToPosition = 0;
        }
        try {
            currentElement = elem;
            if (elem.getName().equalsIgnoreCase(CF.CFCOMPONENT)) {
                final Context componentContext = context.subContext(elem);
                componentContext.setInComponent(true);
                componentContext.setComponentName(context.getFilename().replaceAll(".[cC][fF][cC]", "").replaceAll("^.*[/\\\\]", ""));//elem.getAttributeValue(CF.DISPLAYNAME)
                componentContext.setContextType(ContextType.COMPONENT);
                handler.push(CF.COMPONENT);
                doStructureStart(elem, componentContext, CFCompDeclStatement.class);
            } else if (elem.getName().equalsIgnoreCase(CF.CFFUNCTION)) {
                final Context functionContext = context.subContext(elem);
                functionContext.setFunctionName(elem.getAttributeValue(CF.NAME));
                functionContext.setContextType(ContextType.FUNCTION);
                handler.push(CF.FUNCTION);
                doStructureStart(elem, functionContext, CFFuncDeclStatement.class);
            } else if (elem.getName().equalsIgnoreCase(CF.CFLOOP) && elem.getAttributeValue(CF.QUERY) != null) {
                // Give a cfloop for query its own context and set the column
                // names as variables if they are available
                final Context loopContext = context.subContext(elem);
                loopContext.setContextType(ContextType.QUERY_LOOP);
                handler.push(CF.CFLOOP);
                final String qryName = elem.getAttributeValue(CF.QUERY);
                handler.addVariables(handler.getQueryColumns(qryName));
                doStructureStart(elem, loopContext, CFFuncDeclStatement.class);
            }

            if (elem.getName().equalsIgnoreCase(CF.CFSET) || elem.getName().equalsIgnoreCase(CF.CFIF)
                    || elem.getName().equalsIgnoreCase(CF.CFELSEIF) || elem.getName().equalsIgnoreCase(CF.CFRETURN)) {
                scanElement(elem, context);
                final Pattern p = Pattern.compile("<\\w+\\s(.*[^/])/?>", Pattern.MULTILINE | Pattern.DOTALL);
                final String expr = elem.getFirstStartTag().toString();
                final Matcher m = p.matcher(expr);
                if (m.matches()) {
                    final String cfscript = m.group(1).trim();
                    if (!cfscript.isEmpty()) {
                        try {
                            final CFExpression expression = cfmlParser.parseCFMLExpression(cfscript, this);
                            if (expression != null) {
                                process(expression, elem, context);
                            }
                        } catch (final Exception npe) {
                            printException(npe, elem);
                            final ContextMessage cm = new ContextMessage(PARSE_ERROR, null, null, context.startLine(), elem.getBegin());
                            reportRule(currentElement, null, context, null, cm);
                        }
                    }
                }
                processStack(elem.getChildElements(), space + " ", context);

            } else if (elem.getName().equalsIgnoreCase(CF.CFARGUMENT)
                    || elem.getName().equalsIgnoreCase(CF.CFDOCUMENTSECTION)
                    ) {
                scanElement(elem, context);
                final String name = elem.getAttributeValue(CF.NAME);
                if (name != null) {
                    handler.addArgument(name);
                }
                processStack(elem.getChildElements(), space + " ", context);
            } else if (elem.getName().equalsIgnoreCase(CF.CFSCRIPT)) {
                scanElement(elem, context);
                String cfscript = elem.getContent().toString();
                if (elem.getEndTag() == null) {
                    // Hack to fetch the entire cfscript text, if cfscript is a
                    // word in the content somewhere, and causes
                    // the jericho parser to fail
                    EndTag nextTag = elem.getSource().getNextEndTag(elem.getBegin());
                    while (nextTag != null && !nextTag.getName().equalsIgnoreCase(elem.getName())) {
                        nextTag = elem.getSource().getNextEndTag(nextTag.getEnd());
                    }
                    if (nextTag.getName().equalsIgnoreCase(elem.getName())) {
                        cfscript = elem.getSource().subSequence(elem.getStartTag().getEnd(), nextTag.getBegin())
                                .toString();
                        skipToPosition = nextTag.getEnd();
                    }
                }
                final CFScriptStatement scriptStatement;
                try {
                    scriptStatement = cfmlParser.parseScript(cfscript);
                } catch (Exception e) {
                    throw new CFLintScanException(e);
                }
                final Context subcontext = context.subContext(elem,scriptStatement==null?null:scriptStatement.getTokens());
                process(scriptStatement, subcontext);
                processStack(elem.getChildElements(), space + " ", context);
            } else if (elem.getName().equalsIgnoreCase(CF.CFFUNCTION)) {
                final Context functionContext = context.subContext(elem);
                functionContext.setFunctionName(elem.getAttributeValue(CF.NAME));
                functionContext.setContextType(ContextType.FUNCTION);
                scanElement(elem, functionContext);
                processStack(elem.getChildElements(), space + " ", functionContext);
                // Process any messages added by downstream parsing.
                for (final ContextMessage message : functionContext.getMessages()) {
                    reportRule(elem, null, functionContext, message.getSource(), message);
                }
                functionContext.getMessages().clear();

                for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
                    try {
                        structurePlugin.endFunction(functionContext, bugs);
                        for (final ContextMessage message : functionContext.getMessages()) {
                            reportRule(elem, null, functionContext, (CFLintScanner) structurePlugin, message);
                        }
                        functionContext.getMessages().clear();
                    } catch (final Exception e) {
                        printException(e);
                        final ContextMessage cm = new ContextMessage(PARSE_ERROR, null, null, context.startLine());
                        reportRule(currentElement, null, context, null, cm);
                    }
                }
                handler.pop();
            } else if (elem.getName().equalsIgnoreCase(CF.CFCOMPONENT)) {
                final Context componentContext = context.subContext(elem);
                componentContext.setInComponent(true);
                componentContext.setComponentName(context.getFilename().replaceAll(".[cC][fF][cC]", "").replaceAll("^.*[/\\\\]", ""));//elem.getAttributeValue(CF.DISPLAYNAME)
                componentContext.setContextType(ContextType.COMPONENT);
                scanElement(elem, componentContext);
                processStack(elem.getChildElements(), space + " ", componentContext);
                for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
                    try {
                        structurePlugin.endComponent(componentContext, bugs);
                        for (final ContextMessage message : componentContext.getMessages()) {
                            reportRule(elem, null, componentContext, (CFLintScanner) structurePlugin, message);
                        }
                        componentContext.getMessages().clear();
                    } catch (final Exception e) {
                        printException(e);
                        final ContextMessage cm = new ContextMessage(PARSE_ERROR, null, null, context.startLine());
                        reportRule(currentElement, null, context, null, cm);
                    }
                }
                handler.pop();
            } else if (elem.getName().equalsIgnoreCase(CF.CFQUERY)) {
                scanElement(elem, context);
                for (final Entry<String, CFExpression> expression : unpackTagExpressions(elem).entrySet()) {
                    if (expression != null) {
                        process(expression.getValue(), elem, context);
                    }
                }
                final List<Element> list = elem.getAllElements();
                processStack(list.subList(1, list.size()), space + " ", context);
                // Save any columns from the cfquery
                final String qryName = elem.getAttributeValue(CF.NAME);
                if (qryName != null && qryName.trim().length() > 0) {
                    final String qryText = elem.getTextExtractor().toString().toUpperCase();
                    final Matcher m = Pattern.compile(".*SELECT\\s(\\w+(\\s*,\\s*\\w+)+)\\s+FROM\\s+.*")
                            .matcher(qryText);
                    final List<String> cols = new ArrayList<>();
                    if (m.matches()) {
                        cols.addAll(Arrays.asList(m.group(1).trim().split("\\s*,\\s*")));
                        handler.addQueryColumnSet(qryName, cols);
                    }
                }
            } else if (elem.getName().equalsIgnoreCase(CF.CFQUERYPARAM)) {
                scanElement(elem, context);
                for (final Entry<String, CFExpression> expression : unpackTagExpressions(elem).entrySet()) {
                    if (expression != null) {
                        process(expression.getValue(), elem, context);
                    }
                }
            } else if (elem.getName().equalsIgnoreCase(CF.CFINCLUDE)) {
                scanElement(elem, context);
                final String path = elem.getAttributeValue(CF.TEMPLATE);
                final File include = new File(new File(context.getFilename()).getParentFile(), path);
                if (strictInclude || include.exists()) {
                    if (includeFileStack.contains(include)) {
                        if (!quiet) {
                            System.err.println("Terminated a recursive call to include file " + include);
                        }
                    } else {
                        includeFileStack.push(include);
                        process(FileUtil.loadFile(include), context.getFilename());
                        includeFileStack.pop();
                    }
                }
            } else if (elem.getName().equalsIgnoreCase(CF.CFLOOP) && elem.getAttributeValue(CF.QUERY) != null) {
                scanElement(elem, context);
                processStack(elem.getChildElements(), space + " ", context);
                handler.pop();
            } else if (elem.getName().equalsIgnoreCase(CF.CFCATCH)) {
                scanElement(elem, context);
                handler.push(CF.CFCATCH);
                if(elem.getAttributeValue("name")!=null){
                    handler.addVariable(elem.getAttributeValue("name"));
                }
                processStack(elem.getChildElements(), space + " ", context);
                handler.pop();
            } else {
                scanElement(elem, context);
                for (final Entry<String, CFExpression> expression : unpackTagExpressions(elem).entrySet()) {
                    if (expression != null) {
                        process(expression.getValue(), elem, tagInfo.isAssignmentAttribute(elem, expression.getKey())
                                ? context.subContextInAssignment() : context);
                    }
                }
                processStack(elem.getChildElements(), space + " ", context);
            }
            // Process any messages added by downstream parsing.
            for (final ContextMessage message : context.getMessages()) {
                reportRule(elem, null, context, message.getSource(), message);
            }
            context.getMessages().clear();
        } catch (final NullPointerException npe) {
            printException(npe);
            final ContextMessage cm = new ContextMessage(PARSE_ERROR, null, null, context.startLine());
            reportRule(currentElement, null, context, null, cm);
        }
    }

    private Map<String, CFExpression> unpackTagExpressions(final Element elem) {
        // Use LinkedHashMap to preserve the order
        final Map<String, CFExpression> expressions = new LinkedHashMap<>();
        if (!elem.getName().toLowerCase().startsWith("cf") || elem.getAttributes() == null) {
            return expressions;
        }
        // Unpack any attributes that have a hash expression or is normally a
        // variable attribute
        for (final Attribute attr : elem.getAttributes()) {
            if (detectScript(attr)) {
                // Try wrapping the expression in single or double quotes for
                // parsing.
                final List<String> literalChar = attr.getValue().contains("'") ? Arrays.asList("\"", "'")
                        : Arrays.asList("'", "\"");
                try {
                    final List<String> errors = new ArrayList<>();
                    final ANTLRErrorListener errorReporter = new ArrayErrorListener(errors);
                    final CFExpression exp = cfmlParser.parseCFMLExpression(
                            literalChar.get(0) + attr.getValue() + literalChar.get(0), errorReporter);
                    if (errors.isEmpty()) {
                        expressions.put(attr.getName().toLowerCase(), exp);
                        continue;
                    }
                } catch (final Exception e) {
                }
                // Try other quotes before reporting a failure
                try {
                    final CFExpression exp = cfmlParser
                            .parseCFMLExpression(literalChar.get(1) + attr.getValue() + literalChar.get(1), this);
                    expressions.put(attr.getName().toLowerCase(), exp);
                } catch (final Exception e2) {
                    if (!quiet) {
                        System.err.println("Error in parsing : " + attr.getValue() + " on tag " + elem.getName());
                    }
                }
            } else if (tagInfo.isExpressionAttribute(elem, attr.getName())) {
                try {
                    final CFExpression exp = cfmlParser.parseCFMLExpression(attr.getValue(), this);
                    expressions.put(attr.getName().toLowerCase(), exp);
                } catch (final Exception e2) {
                    if (!quiet) {
                        System.err.println("Error in parsing : " + attr.getValue() + " on tag " + elem.getName());
                    }
                }
            }
        }
        //Parse the body
        if(elem.getName().toLowerCase().equals("cfoutput")) {
        	final String content = elem.getContent().getTextExtractor().toString();
        	if(content !=null && content.length()>0 && content.contains("#")) {
        		final List<String> errors = new ArrayList<>();
                final ANTLRErrorListener errorReporter = new ArrayErrorListener(errors);
                try {
                    final CFExpression exp = cfmlParser.parseCFMLExpression(
                    content, errorReporter);
	        		if(errors.isEmpty()) {
	        		     expressions.put("body", exp);
	        		}
                }catch(final Exception e) {}
        	}
            
        }
        return expressions;
    }

    private boolean detectScript(final Attribute attr) {
        return attr.getValue() != null && attr.getValue().contains("#") && !attr.getValue().startsWith("'")
                && !attr.getValue().startsWith("\"");
    }

    protected void scanElement(final Element elem, final Context context) {
        for (final CFLintScanner plugin : extensions) {
            try {
                plugin.element(elem, context, bugs);
                for (final ContextMessage message : context.getMessages()) {
                    reportRule(elem, null, context, plugin, message);
                }
                context.getMessages().clear();
            } catch (final Exception e) {
                printException(e);
                reportRule(elem, null, context, plugin, PLUGIN_ERROR);
            }
        }
    }

    private List<CFLintStructureListener> getStructureListeners(final List<CFLintScanner> extensions) {
        final List<CFLintStructureListener> retval = new ArrayList<>();
        for (final CFLintScanner plugin : extensions) {
            if (plugin instanceof CFLintStructureListener) {
                retval.add((CFLintStructureListener) plugin);
            }
        }
        return retval;
    }

    private String shortSource(final Source source, final int line) {
        final String retval = source == null ? "" : source.toString().trim();
        if (retval.length() < 300 || source == null) {
            return retval;
        }
        try(final BufferedReader sr = new BufferedReader(new StringReader(source.toString()));) {
            for (int i = 1; i < line; i++) {
                @SuppressWarnings("unused")
                String skip = sr.readLine();
            }
            final String sLine = sr.readLine();
            return sLine == null ? null : sLine.replaceAll("\t", " ");
        } catch (final Exception e) { }
        return retval.substring(0, 300);
    }

    private void process(final CFScriptStatement expression, final Context context) {
        if (expression == null) {
            return;
        }
        if (expression != null && expression.getToken() != null) {
            final List<Object> checkItem = Arrays.asList(expression, expression.getToken());
            if (processed.contains(checkItem)) {
                if (!quiet) {
                    System.err.println("Attempt to process expression twice aborted.  This may be a parsing bug in "
                        + context.getFilename() + " : "
                        + (expression.getToken() != null ? expression.getToken().getLine() : ""));
                }
                return;
            }
            processed.add(checkItem);
        }
        final Element elem = context.getElement();
        try {
            if (expression instanceof CFCompoundStatement) {
                scanExpression(expression, context, elem);
                for (final CFScriptStatement statement : ((CFCompoundStatement) expression).getStatements()) {
                    process(statement, context);
                }
            } else if (expression instanceof CFExpressionStatement) {
                scanExpression(expression, context, elem);
                registerRuleOverrides(context, (CFExpressionStatement) expression);
                process(((CFExpressionStatement) expression).getExpression(), elem, context);
            } else if (expression instanceof CFPropertyStatement) {
                try {
                    final CFPropertyStatement propertyStatement = (CFPropertyStatement) expression;
                    CFExpression value = propertyStatement.getPropertyName();
                    if (value == null) {
                        for (final Entry<CFIdentifier, CFExpression> entry : propertyStatement.getAttributes().entrySet()) {
                            if (CF.NAME.equalsIgnoreCase(entry.getKey().getName())) {
                                value = entry.getValue();
                            }
                        }
                    }
                    if (value != null) {
                        final String name = value.Decompile(0);
                        handler.addVariable(name.substring(1, name.length() - 1));
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                scanExpression(expression, context, elem);
            } else if (expression instanceof CFCompDeclStatement) {
                final CFCompDeclStatement compDeclStatement = (CFCompDeclStatement) expression;
                final Context componentContext = context.subContext(null);
                componentContext.setInComponent(true);
                componentContext.setContextType(ContextType.COMPONENT);
                for (final Entry<CFExpression, CFExpression> entry : compDeclStatement.getAttributes().entrySet()) {
                    if (entry.getKey() != null && entry.getKey().Decompile(0).equalsIgnoreCase(CF.NAME)) {
                        componentContext.setComponentName(entry.getValue().Decompile(0));
                    }
                }
                // Register any overrides from multi-line comments.
                registerRuleOverrides(componentContext, expression.getToken());
                // do startComponent notifications
                doStructureStart(elem, componentContext, expression.getClass());
                scanExpression(compDeclStatement, componentContext, elem);
                // process the component declaration
                if (compDeclStatement.getBody() instanceof CFCompoundStatement) {
                    // Process property expressions first
                    for (final CFScriptStatement subscript : compDeclStatement.getBody().decomposeScript()) {
                        if (subscript instanceof CFPropertyStatement) {
                            process(subscript, componentContext);
                        }
                    }
                    for (final CFScriptStatement subscript : compDeclStatement.getBody().decomposeScript()) {
                        if (!(subscript instanceof CFPropertyStatement)) {
                            process(subscript, componentContext);
                        }
                    }
                } else {
                    process(compDeclStatement.getBody(), componentContext);
                }
                // do endComponent notifications
                for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
                    try {
                        structurePlugin.endComponent(componentContext, bugs);
                        for (final ContextMessage message : componentContext.getMessages()) {
                            reportRule(elem, null, componentContext, (CFLintScanner) structurePlugin, message);
                        }
                        componentContext.getMessages().clear();
                    } catch (final Exception e) {
                        printException(e);
                        final ContextMessage cm = new ContextMessage(PARSE_ERROR, null, null, context.startLine());
                        reportRule(currentElement, null, context, null, cm);
                    }
                }
            } else if (expression instanceof CFForStatement) {
                scanExpression(expression, context, elem);
                process(((CFForStatement) expression).getInit(), elem, context);
                process(((CFForStatement) expression).getCond(), elem, context);
                process(((CFForStatement) expression).getNext(), elem, context);
                process(((CFForStatement) expression).getBody(), context);
                
            } else if (expression instanceof CFWhileStatement) {
                scanExpression(expression, context, elem);
                process(((CFWhileStatement) expression).getCond(), elem, context);
                process(((CFWhileStatement) expression).getBody(), context);
            } else if (expression instanceof CFForInStatement) {
                scanExpression(expression, context, elem);
                final Context forInitContext = context.subContext(elem);
                forInitContext.setInAssignmentExpression(true);
                process(((CFForInStatement) expression).getVariable(), elem, forInitContext);
                process(((CFForInStatement) expression).getStructure(), elem, context);
                process(((CFForInStatement) expression).getBody(), context);
            } else if (expression instanceof CFIfStatement) {
                scanExpression(expression, context, elem);
                final CFIfStatement cfif = (CFIfStatement) expression;
                process(cfif.getCond(), elem, context);
                process(cfif.getThenStatement(), context);
                process(cfif.getElseStatement(), context);
            } else if (expression instanceof CFSwitchStatement) {
                scanExpression(expression, context, elem);
                final CFSwitchStatement cfswitch = (CFSwitchStatement) expression;
                process(cfswitch.getVariable(), elem, context);
                for (final CFCase _case : cfswitch.getCases()) {
                    process(_case, context);
                }
            } else if (expression instanceof CFCase) {
                scanExpression(expression, context, elem);
                final CFCase cfcase = (CFCase) expression;
                for (final CFScriptStatement cfstatement : cfcase.getStatements()) {
                    process(cfstatement, context);
                }
            } else if (expression instanceof CFTryCatchStatement) {
                scanExpression(expression, context, elem);
                final CFTryCatchStatement cftry = (CFTryCatchStatement) expression;
                process(cftry.getBody(), context);
                for (final CFCatchStatement stmt : cftry.getCatchStatements()) {
                    handler.push(CF.CFCATCH);
                    if(stmt.getVariable()!=null && stmt.getVariable().getName()!=null){
                        handler.addVariable(stmt.getVariable().getName());
                    }
                    process(stmt.getCatchBody(), context);
                    handler.pop();
                }
                process(cftry.getFinallyStatement(), context);
            } else if (expression instanceof CFReturnStatement) {
                scanExpression(expression, context, elem);
                final CFReturnStatement cfreturn = (CFReturnStatement) expression;
                final CFExpression subExpression = cfreturn.getExpression();
                process(subExpression, elem, context);
            } else if (expression instanceof CFFuncDeclStatement) {
                final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
                final Context functionContext = context.subContext(null);
                functionContext.setContextType(ContextType.FUNCTION);
                functionContext.setFunctionInfo(function);
                registerRuleOverrides(functionContext, function.getToken());
                handler.push(CF.FUNCTION);
                for (final CFFunctionParameter param : function.getFormals()) {
                    handler.addArgument(param.getName());
                }
                doStructureStart(elem, functionContext, CFFuncDeclStatement.class);
                scanExpression(expression, functionContext, elem);
                final Context functionBodyContext = functionContext.subContext(null);
                process(function.getBody(), functionBodyContext);

                for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
                    try {
                        structurePlugin.endFunction(functionContext, bugs);
                        for (final ContextMessage message : functionContext.getMessages()) {
                            reportRule(elem, null, functionContext, (CFLintScanner) structurePlugin, message);
                        }
                        functionContext.getMessages().clear();
                    } catch (final Exception e) {
                        printException(e);
                        final ContextMessage cm = new ContextMessage(PARSE_ERROR, null, null, context.startLine());
                        reportRule(currentElement, null, context, null, cm);
                    }
                }
                handler.pop();
            } else if (expression instanceof CFIncludeStatement) {
                scanExpression(expression, context, elem);
                final CFExpression includeExpr = ((CFIncludeStatement) expression)
                        .getTemplate();
                if(includeExpr instanceof CFStringExpression){
                    final List<CFExpression> subExpressions = ((CFStringExpression) includeExpr).getSubExpressions();
                    if (subExpressions.size() == 1 && subExpressions.get(0) instanceof CFLiteral) {
                        final String path = ((CFLiteral) subExpressions.get(0)).getVal();
                        final File include = new File(new File(context.getFilename()).getParentFile(), path);
                        if (include.exists() || strictInclude) {
                            try {
                                if (includeFileStack.contains(include)) {
                                    if (!quiet) {
                                        System.err.println("Terminated a recursive call to include file " + include);
                                    }
                                } else {
                                    includeFileStack.push(include);
                                    process(FileUtil.loadFile(include), context.getFilename());
                                    includeFileStack.pop();
                                }
                            } catch (final CFLintScanException ex) {
                                if (!quiet) {
                                    System.err.println("Invalid include file " + context.getFilename());
                                }
                                final int line = context.startLine();
                                final ContextMessage cm = new ContextMessage(PARSE_ERROR, null, null, line);
                                reportRule(currentElement, "Invalid include file " + expression.getClass(), context, null,
                                        cm);
                            }
                        }
                    } else if (strictInclude) {
                        if (!quiet) {
                            System.err.println("Unable to resolve template value " + context.getFilename());
                        }
                        final int line = context.startLine();
                        final ContextMessage cm = new ContextMessage(PARSE_ERROR, null, null, line);
                        reportRule(currentElement, "Unable to resolve template value " + expression.getClass(), context,
                                null, cm);
                    }
                }
            } else {
                scanExpression(expression, context, elem);
                for(CFScriptStatement childExpression: expression.decomposeScript()){
                    process(childExpression, context);
                }
            }
        } catch (final StackOverflowError soe) {
            if (!quiet) {
                System.err.println("Stack overflow in " + context.getFilename());
            }
            final int line = context.startLine();
            final ContextMessage cm = new ContextMessage(PARSE_ERROR, null, null, line);
            reportRule(currentElement, "Stack overflow on " + expression.getClass(), context, null, cm);
        }
        // Process any messages added by downstream parsing.
        for (final ContextMessage message : context.getMessages()) {
            reportRule(elem, null, context, message.getSource(), message);
        }
        context.getMessages().clear();
    }

    protected void doStructureStart(final Element elem, final Context context,
            final Class<? extends CFScriptStatement> class1) {
        for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
            try {
                if (class1.equals(CFCompDeclStatement.class)) {
                    structurePlugin.startComponent(context, bugs);
                } else if (class1.equals(CFFuncDeclStatement.class)) {
                    structurePlugin.startFunction(context, bugs);
                }
                for (final ContextMessage message : context.getMessages()) {
                    reportRule(elem, null, context, (CFLintScanner) structurePlugin, message);
                }
                context.getMessages().clear();
            } catch (final Exception e) {
                printException(e);
                final ContextMessage cm = new ContextMessage(PARSE_ERROR, null, null, context.startLine());
                reportRule(currentElement, null, context, null, cm);
            }
        }
    }

    protected void scanExpression(final CFScriptStatement expression, final Context context, final Element elem) {
        if (expression == null) {
            return;
        }
        for (final CFLintScanner plugin : extensions) {
            try {
                plugin.expression(expression, context, bugs);
                for (final ContextMessage message : context.getMessages()) {
                    reportRule(elem, expression, context, plugin, message);
                }
                context.getMessages().clear();
            } catch (final Exception e) {
                printException(e);
                reportRule(elem, expression, context, plugin, PLUGIN_ERROR);
            }
        }
    }

    /**
     * Register any overrides from multi-line comments.
     * 
     * @param context
     *            The current context.
     * @param functionToken
     *            A token that points to the current function
     */
    protected void registerRuleOverrides(final Context context, final Token functionToken) {
        final String mlText = PrecedingCommentReader.getMultiLine(context, functionToken);
        if (mlText != null && !mlText.isEmpty()) {
            final Pattern pattern = Pattern.compile(".*\\s*@CFLintIgnore\\s+([\\w,_]+)\\s*.*", Pattern.DOTALL);
            final Matcher matcher = pattern.matcher(mlText);
            if (matcher.matches()) {
                final String ignoreCodes = matcher.group(1);
                context.ignore(Arrays.asList(ignoreCodes.split(",\\s*")));
            }
        }
    }

    /**
     * @param context
     *            The current context.
     * @param expression
     *            The expression statement to check
     */
    protected void registerRuleOverrides(final Context context, final CFExpressionStatement expression) {
        if (expression.getTokens() == null) {
            return;
        }
        final Iterable<Token> tokens = expression.getTokens().getTokens();
        for (final Token currentTok : tokens) {
            if (debug) {
                System.out.println(currentTok.toString());
            }
            if (currentTok.getLine() == expression.getExpression().getLine()) {
                if (currentTok.getChannel() == Token.HIDDEN_CHANNEL
                        && currentTok.getType() == CFSCRIPTLexer.LINE_COMMENT) {
                    final String commentText = currentTok.getText().replaceFirst("^//\\s*", "").trim();
                    if (commentText.startsWith("cflint ")) {
                        final Pattern pattern = Pattern.compile("cflint\\s+ignore:([\\w,]+).*");
                        final Matcher matcher = pattern.matcher(commentText);
                        if (matcher.matches()) {
                            final String ignoreCodes = matcher.group(1);
                            context.ignore(Arrays.asList(ignoreCodes.split(",\\s*")));
                        }
                    }
                }
            }
        }
    }

    /**
     * Register any overrides from comment elements before functions/components.
     *
     * @param context
     *            The current context.
     * @param commentElement
     *            The CFML comment element
     */
    protected void applyRuleOverrides(final Context context, final Element commentElement) {
        if (commentElement != null && CF.COMMENT.equals(commentElement.getName())) {
            final String mlText = commentElement.toString();
            final Pattern pattern = Pattern.compile(".*\\s*@CFLintIgnore\\s+([\\w,_]+)\\s*.*", Pattern.DOTALL);
            final Matcher matcher = pattern.matcher(mlText);
            if (matcher.matches()) {
                final String ignoreCodes = matcher.group(1);
                context.ignore(Arrays.asList(ignoreCodes.split(",\\s*")));
            }
        }
    }

    /**
     * 
     * @param expression
     *            CF expression
     * @param elem
     *            Jericho HTML element
     * @param oldcontext
     *            The previous context
     */
    private void process(final CFExpression expression, final Element elem, final Context oldcontext) {
        if (expression != null) {
            final Context context = oldcontext.subContext(elem);
            for (final CFLintScanner plugin : extensions) {
                try {
                    plugin.expression(expression, context, bugs);
                    for (final ContextMessage message : context.getMessages()) {
                        reportRule(elem, expression, context, plugin, message);
                    }
                    context.getMessages().clear();
                } catch (final Exception e) {
                    printException(e);
                    reportRule(elem, expression, context, plugin, PLUGIN_ERROR);
                }
            }
            // Handle a few expression types in a special fashion.
            if (expression instanceof CFVarDeclExpression) {
                handler.addVariable(((CFVarDeclExpression) expression).getName());
            }

            // CFIdentifier should not decompose
            if (expression.getClass().equals(CFIdentifier.class)) {
                final String name = ((CFIdentifier) expression).getName();
                handler.checkVariable(name);
            }
            if (expression instanceof CFStructElementExpression) {
                final Context assignmentContext = context.subContext(elem);
                assignmentContext.setInStructKeyExpression(true);
                handler.push(CF.STRUCT);
                process(((CFStructElementExpression) expression).getKey(), elem, assignmentContext);
                handler.pop();
                process(((CFStructElementExpression) expression).getValue(), elem, context);
            }else if (expression instanceof CFVarDeclExpression) {
                final Context assignmentContext = context.subContext(elem);
                assignmentContext.setInAssignmentExpression(true);
                process(((CFVarDeclExpression) expression).getVar(), elem, assignmentContext);
                // Right hand side is handled below. Left hand side gets a
                // special context.
                process(((CFVarDeclExpression) expression).getInit(), elem, context);
                // Only process function call expressions
            } else if (expression instanceof CFAssignmentExpression && !(expression instanceof CFTernaryExpression)) {
                final Context assignmentContext = context.subContext(elem);
                assignmentContext.setInAssignmentExpression(true);
                process(((CFAssignmentExpression) expression).getLeft(), elem, assignmentContext);
                // Right hand side is handled below. Left hand side gets a
                // special context.
                process(((CFAssignmentExpression) expression).getRight(), elem, context);
                // Only process function call expressions
            } else if (expression instanceof CFFullVarExpression) {
                final CFFullVarExpression fullVarExpression = (CFFullVarExpression) expression;
                if (context.isInAssignmentExpression() && new CFScopes().isScoped(fullVarExpression, "local")
                        && fullVarExpression.getExpressions().size() > 1) {
                    handler.addVariable(fullVarExpression.getExpressions().get(1).Decompile(0));
                }
                final Context subContext = context.subContext(context.getElement());
                subContext.setInAssignmentExpression(false);
                for (final CFExpression expr : fullVarExpression.getExpressions()) {
                    if (expr instanceof CFFunctionExpression) {
                        process(expr, elem, subContext);
                    }
                    else if (expr instanceof CFMember) {
                        process(((CFMember) expr).getExpression(), elem, subContext);
                    }
                    else if (expr instanceof CFArrayExpression) {
                        final CFArrayExpression aryExpr = (CFArrayExpression) expr;
                        if (!aryExpr.getElements().isEmpty()) {
                            process(aryExpr.getElements().get(0), elem, subContext);
                        }
                    }
                }
            } else if (expression instanceof CFFunctionExpression && tagInfo.isTag(((CFFunctionExpression)expression).getFunctionName())){
                final CFFunctionExpression functionExpr = (CFFunctionExpression)expression;
                final StringBuilder sb = new StringBuilder();
                sb.append("<").append(functionExpr.getFunctionName()).append(" ");
                for (final CFExpression expr : functionExpr.getArgs()) {
                    if (expr instanceof CFAssignmentExpression) {
                        final CFAssignmentExpression assignExpr = (CFAssignmentExpression) expr;
                        sb.append(assignExpr.getLeft().Decompile(0)).append("=")
                        .append(assignExpr.getRight().Decompile(0)).append(" ");
                    }
                }
                sb.append("/>");
                final CFMLSource source = new CFMLSource(sb.toString());
                try {
                    processStack(source.getChildElements(), " ", 
                            context.subContextCFML(source.getChildElements().size()>0?source.getChildElements().get(0):null,expression));
                } catch (CFLintScanException e) { }
                if(functionExpr.getBody()!=null) {
                	process(functionExpr.getBody(),context);
                }
            } else if (!(expression instanceof CFNewExpression)){
                // Loop into all relevant nested (child) expressions.
                //  EXCEPT CFNewExpressions.
                for (final CFExpression child : expression.decomposeExpression()) {
                    process(child, elem, context.subContextInAssignment(false));
                }
            }else{
                //Process only the right hand side of new expressions
                final CFNewExpression newExpr = (CFNewExpression) expression;
                for (final CFExpression child : (List<CFExpression>)newExpr.getArgs()) {
                    if(child instanceof CFAssignmentExpression){
                        process(((CFAssignmentExpression)child).getRight(), elem, context.subContextInAssignment(false));
                    }
                }
            }
        }
    }

    protected void reportRule(final Element elem, final Object expression, final Context context,
            final CFLintScanner plugin, final String msg) {
        final String[] exceptionmsg = (msg != null ? msg : "").split(":");
        final String msgcode = exceptionmsg[0].trim();
        final String nameVar = exceptionmsg.length > 1 ? exceptionmsg[1].trim() : null;
        reportRule(elem, expression, context, plugin, new ContextMessage(msgcode, nameVar));
    }

    /**
     * Returns the previous sibling of a given element
     * 
     * @param element
     *            The Jericho HTML element object
     * @return the previous sibling of the given element.
     */
    public static Element getPreviousSibling(final Element element) {
        if (element.getParentElement() != null) {
            final List<Element> parentElements = element.getParentElement().getChildElements();
            final int idx = parentElements.indexOf(element);
            if (idx > 0) {
                return parentElements.get(idx - 1);
            }
        } else if (element.getSource() != null) {
            final List<Element> parentElements = element.getSource().getChildElements();
            final int idx = parentElements.indexOf(element);
            if (idx > 0) {
                return parentElements.get(idx - 1);
            }
        }
        return null;
    }

    /**
     * Check for <!--- CFLINT-DISABLE ---> in the tag hierarchy
     * 
     * @param element
     *            The element to process
     * @param msgcode
     *            The message code to check for
     * @return true if the msgcode is disabled for the given element.
     */
    protected boolean checkForDisabled(final Element element, final String msgcode) {
        Element elem = element;
        while (elem != null) {
            final Element prevSibling = getPreviousSibling(elem);
            if (prevSibling != null && prevSibling.getName().equals(CF.COMMENT)) {
                final Pattern p = Pattern.compile(".*---\\s*CFLINT-DISABLE\\s+(.*)\\s*---.*");
                final Matcher m = p.matcher(prevSibling.toString().toUpperCase().trim());
                if (m.matches()) {
                    // No message codes in CFLINT-DISABLE
                    if (m.group(1).trim().length() == 0) {
                        if (verbose) {
                            System.out.println("Skipping disabled " + msgcode);
                        }
                        return true;
                    }
                    // check for matching message codes in CFLINT-DISABLE
                    for (String skipcode : m.group(1).split(",")) {
                        skipcode = skipcode.trim();
                        if (msgcode.equals(skipcode)) {
                            if (verbose) {
                                System.out.println("Skipping disabled " + msgcode);
                            }
                            return true;
                        }
                    }
                }
            }
            elem = elem.getParentElement();
        }
        return false;
    }

    public void reportRule(Element elem, Object currentExpression, final Context context,
            final CFLintScanner pluginParm, final ContextMessage msg) {
    	//If the message carries a context, use it for generating error info like line number
    	if(msg.getOriginalContext()!=null && !context.equals(msg.getOriginalContext())) {
    		reportRule(msg.getOriginalContext().getElement(), currentExpression, msg.getOriginalContext(), pluginParm, msg);
    		return;
    	}
        final Context pseudoCfmlParent = context.getParent(ContextType.PSEUDO_CFML);
        if(pseudoCfmlParent!=null && ContextType.PSEUDO_CFML.equals(pseudoCfmlParent.getContextType())){
            elem = pseudoCfmlParent.getElement();
            currentExpression = pseudoCfmlParent.getPseudoCfmlExpression();
        }
        final Object expression = msg.getCfExpression() != null? msg.getCfExpression():currentExpression;
        // If we are processing includes, do NOT report any errors
        if (!includeFileStack.isEmpty()) {
            return;
        }
        final String msgcode = msg.getMessageCode();
        final String nameVar = msg.getVariable();
        final CFLintScanner plugin = msg.getSource() == null ? pluginParm : msg.getSource();
        if (checkForDisabled(elem, msgcode)) {
            return;
        }
        if (configuration == null) {
            throw new NullPointerException("Configuration is null");
        }
        final PluginInfoRule ruleInfo;
        if (MISSING_SEMI.equals(msgcode)) {
            ruleInfo = new PluginInfoRule();
            final PluginMessage msgInfo = new PluginMessage(MISSING_SEMI);
            msgInfo.setMessageText("End of statement(;) expected after ${variable}");
            msgInfo.setSeverity(Levels.ERROR);
            ruleInfo.getMessages().add(msgInfo);
        } else if (PLUGIN_ERROR.equals(msgcode)) {
            ruleInfo = new PluginInfoRule();
            final PluginMessage msgInfo = new PluginMessage(PLUGIN_ERROR);
            msgInfo.setMessageText("Error in plugin: ${variable}");
            msgInfo.setSeverity(Levels.ERROR);
            ruleInfo.getMessages().add(msgInfo);
        } else if (AVOID_EMPTY_FILES.equals(msgcode)) {
            ruleInfo = new PluginInfoRule();
            final PluginMessage msgInfo = new PluginMessage(AVOID_EMPTY_FILES);
            msgInfo.setMessageText("CF file is empty: ${file}");
            msgInfo.setSeverity(Levels.WARNING);
            ruleInfo.getMessages().add(msgInfo);
        } else if (PARSE_ERROR.equals(msgcode)) {
            ruleInfo = new CFLintPluginInfo.PluginInfoRule();
            final CFLintPluginInfo.PluginInfoRule.PluginMessage msgInfo = new CFLintPluginInfo.PluginInfoRule.PluginMessage(
                    PARSE_ERROR);
            msgInfo.setMessageText("Unable to parse");
            msgInfo.setSeverity(Levels.ERROR);
            ruleInfo.getMessages().add(msgInfo);
        } else {
            if (plugin == null) {
                throw new NullPointerException(
                        "Plugin not set.  Plugin should be using addMessage(messageCode,variable,source) to report messages in parent contexts");
            }
            ruleInfo = configuration.getRuleForPlugin(plugin);
        }
        if (ruleInfo == null) {
            throw new NullPointerException("Rule not found for " + plugin.getClass().getSimpleName());
        }
        final PluginMessage msgInfo = ruleInfo.getMessageByCode(msgcode);
        if (msgInfo == null) {
            throw new NullPointerException(
                    "Message definition not found for [" + msgcode + "] in " + plugin.getClass().getSimpleName());
        }
        final BugInfoBuilder bldr = new BugInfo.BugInfoBuilder().setMessageCode(msgcode).setVariable(nameVar)
                .setFunction(context.getFunctionName()).setFilename(context.getFilename())
                .setComponent(context.getComponentName());
        bldr.setSeverity(msgInfo.getSeverity());
        bldr.setMessage(msgInfo.getMessageText());

        if (expression instanceof CFStatement) {
            bldr.setExpression(((CFStatement) expression).Decompile(0));
        } else if (expression instanceof CFScriptStatement) {
            bldr.setExpression(((CFScriptStatement) expression).Decompile(0));
        } else if (elem != null) {
            bldr.setExpression(elem.toString().replaceAll("\r\n", "\n"));
        }
        //Rebuild the parameter list so that custom configurations are picked up
        final List<PluginParameter> parameters = new ArrayList<>();
        for(PluginParameter x: ruleInfo.getParameters()){
            parameters.add(new PluginParameter(x.getName(),context.getConfiguration().getParameter(plugin,x.getName())));
        }
        bldr.setRuleParameters(parameters);
        if (configuration.includes(ruleInfo.getMessageByCode(msgcode))
                && !configuration.excludes(ruleInfo.getMessageByCode(msgcode))) {
            //A bit of a hack to fix the offset issue 
            //This could be handled better at the source where line and offset are calc'd.
            int idxOffSet = 1;
            try{
                if(lineOffsets != null && msg.getLine()!=null && msg.getOffset() != null && msg.getOffset() >=lineOffsets[msg.getLine()] ){
                    idxOffSet=0;
                }
            }catch(ArrayIndexOutOfBoundsException e){}
            if (expression instanceof CFExpression) {
                final BugInfo bugInfo = bldr.build((CFExpression) expression, elem);
                final Token token = ((CFExpression) expression).getToken();
                if (!suppressed(bugInfo, token, context)) {
                    bugs.add(bugInfo);
                }
                //If the context expression is attached, use the context line and column
                if( msg.getCfExpression() != null && msg.getCfExpression() != currentExpression){
                    if (msg.getLine() != null) {
                        bugInfo.setLine(msg.getLine());
                        if (msg.getOffset() != null) {
                            bugInfo.setOffset(msg.getOffset());
                            try{
                                bugInfo.setColumn(msg.getOffset() - lineOffsets[msg.getLine() - idxOffSet]);
                            }catch(ArrayIndexOutOfBoundsException aie){bugInfo.setColumn(0);}
                        } else {
                            bugInfo.setOffset(lineOffsets != null ? lineOffsets[msg.getLine() - idxOffSet] : 0);
                            bugInfo.setColumn(0);
                        }
                    }    
                }
            } else {
                final BugInfo bug = bldr.build((CFScriptStatement) expression, elem);
                if (msg.getLine() != null) {
                    bug.setLine(msg.getLine());
                    if (msg.getOffset() != null) {
                        bug.setOffset(msg.getOffset());
                        try{
                            bug.setColumn(msg.getOffset() - lineOffsets[msg.getLine() - idxOffSet]);
                        }catch(ArrayIndexOutOfBoundsException aie){bug.setColumn(0);}
                    } else {
                        bug.setOffset(lineOffsets != null ? lineOffsets[msg.getLine() - idxOffSet] : 0);
                        bug.setColumn(0);
                    }
                    bug.setLength(msg.getVariable() != null ? msg.getVariable().length() : 0);
                }
                if (!suppressed(bug, expression==null?null:((CFScriptStatement)expression).getToken(), context)) {
                    bugs.add(bug);
                }
            }
        }
    }

    /*
     * Look for a suppress comment on the same line. cflint:line - suppresses
     * any messages on the same line cflint:MESSAGE_CODE - suppresses any
     * message matching that code
     */
    protected boolean suppressed(final BugInfo bugInfo, final Token token, final Context context) {
        if (context == null || context.isSuppressed(bugInfo)) {
            return true;
        }
        if(token == null){
            return false;
        }
        final Iterable<Token> tokens = context.afterTokens(token);
        for (final Token currentTok : tokens) {
            if (debug) {
                System.out.println(currentTok.toString());
            }
            if (currentTok.getLine() != token.getLine()) {
                break;
            }
            if (currentTok.getChannel() == Token.HIDDEN_CHANNEL && currentTok.getType() == CFSCRIPTLexer.LINE_COMMENT) {
                final String commentText = currentTok.getText().replaceFirst("^//\\s*", "").trim();
                if (commentText.startsWith("cflint ")) {
                    final Pattern pattern = Pattern.compile("cflint\\s+ignore:([\\w,]+).*");
                    final Matcher matcher = pattern.matcher(commentText);
                    if (matcher.matches() && matcher.groupCount() > 0) {
                        final String ignoreCodes = matcher.group(1);
                        if ("line".equalsIgnoreCase(ignoreCodes)) {
                            return true;
                        }
                        for (final String ignoreCode : ignoreCodes.split(",\\s*")) {
                            if (ignoreCode.equals(bugInfo.getMessageCode())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public BugList getBugs() {
        return bugs;
    }

    public List<String> getAllowedExtensions() {
        return allowedExtensions;
    }

    public void setAllowedExtensions(final List<String> allowedExtensions) {
        this.allowedExtensions.clear();
        if(allowedExtensions != null){
            this.allowedExtensions.addAll(allowedExtensions);
        }
    }

    @Override
    public void reportError(final String arg0) {
        // Empty implementation
    }

    public void setVerbose(final boolean verbose) {
        this.verbose = verbose;
    }

    public void setLogError(final boolean logError) {
        this.logError = logError;
    }

    public void setQuiet(final boolean quiet) {
        this.quiet = quiet;
    }

    public void setDebug(final boolean debug) {
        this.debug = debug;
    }

    public void addScanProgressListener(final ScanProgressListener scanProgressListener) {
        scanProgressListeners.add(scanProgressListener);
    }

    protected void fireStartedProcessing(final String srcidentifier) {
        cfmlParser = new CFMLParser();
        cfmlParser.setErrorReporter(this);
        currentFile = srcidentifier;
        currentElement = null;
        for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
            try {
                structurePlugin.startFile(srcidentifier, bugs);
            } catch (final Exception e) {
                printException(e);
                final Context context = new Context(currentFile, currentElement, null, true, null, null);
                final ContextMessage cm = new ContextMessage(PARSE_ERROR, null, null, context.startLine());
                reportRule(currentElement, null, context, null, cm);
            }
        }
        for (final ScanProgressListener p : scanProgressListeners) {
            p.startedProcessing(srcidentifier);
        }
    }

    protected void fireFinishedProcessing(final String srcidentifier) {
        for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
            try {
                structurePlugin.endFile(srcidentifier, bugs);
            } catch (final Exception e) {
                printException(e);
                final Context context = new Context(currentFile, currentElement, null, true, null, null);
                final ContextMessage cm = new ContextMessage(PARSE_ERROR, null, null, context.startLine());
                reportRule(currentElement, null, context, null, cm);
            }
        }
        for (final ScanProgressListener p : scanProgressListeners) {
            p.finishedProcessing(srcidentifier);
        }
        processed.clear();
    }

    protected void fireClose() {
        for (final ScanProgressListener p : scanProgressListeners) {
            p.close();
        }
    }

    public void addScanner(final CFLintScanner plugin) {
        if (plugin != null) {
            extensions.add(plugin);
            if (plugin instanceof CFLintSet) {
                ((CFLintSet) plugin).setCFLint(this);
            }
        }
    }

    public List<CFLintScanner> getScanners() {
        return extensions;
    }

    public void addExceptionListener(final CFLintExceptionListener exceptionListener) {
        exceptionListeners.add(exceptionListener);
    }

    protected void fireCFLintException(final Throwable e, final String messageCode, final String filename,
            final Integer line, final Integer column, final String functionName, final String expression) {
        for (final CFLintExceptionListener p : exceptionListeners) {
            p.exceptionOccurred(e, messageCode, filename, line, column, functionName, expression);
        }
    }

    public void setShowProgress(final boolean showProgress) {
        this.showProgress = showProgress;
    }

    public void setProgressUsesThread(final boolean progressUsesThread) {
        this.progressUsesThread = progressUsesThread;
    }

    @Override
    public void syntaxError(final Recognizer<?, ?> recognizer, final Object offendingSymbol, int line,
            int charPositionInLine, final String msg, final org.antlr.v4.runtime.RecognitionException re) {
        String expression = null;
        int offset = charPositionInLine;
        int startLine = 0;
        int startOffset = 0;
        final Context context = new Context(currentFile, currentElement, null, true, null, null);
        if (currentElement != null) {
            startOffset = context.offset();
            if(context.startLine() != 1) {
                startLine = currentElement.getSource().getRow(startOffset) - 1;
            }
        }
        if (offendingSymbol instanceof Token && re != null) {
            // grab the first non-comment previous token, which is actually the cause of the syntax error theoretically
            CommonTokenStream tokenStream = (CommonTokenStream)recognizer.getInputStream();
            Token previousToken = tokenStream.get(re.getOffendingToken().getTokenIndex()-1);
            if (previousToken != null) {
                while(previousToken.getChannel() == Token.HIDDEN_CHANNEL && tokenStream.get(previousToken.getTokenIndex()-1) != null) {
                    previousToken = tokenStream.get(previousToken.getTokenIndex()-1);
                }
                line = previousToken.getLine();
                offset = previousToken.getStopIndex();
                expression = previousToken.getText();
            } else {
                expression = re.getOffendingToken().getText();
            }
            if (expression.length() > 50) {
                expression = expression.substring(1, 40) + "...";
            }
        }
        offset +=  startOffset;
        line += startLine;
        if (recognizer instanceof Parser && ((Parser) recognizer).isExpectedToken(CFSCRIPTParser.SEMICOLON)) {
            final ContextMessage cm = new ContextMessage(MISSING_SEMI, expression, null, line, offset);
            reportRule(currentElement, null, context, null, cm);
        } else {
            final ContextMessage cm = new ContextMessage(PARSE_ERROR, expression, null, line, offset);
            reportRule(currentElement, null, context, null, cm);
        }
    }

    @Override
    public void reportAmbiguity(final Parser recognizer, final DFA dfa, final int startIndex, final int stopIndex,
            final boolean exact, final java.util.BitSet ambigAlts, final ATNConfigSet configs) {
        // Empty implementation
    }

    @Override
    public void reportAttemptingFullContext(final Parser recognizer, final DFA dfa, final int startIndex,
            final int stopIndex, final java.util.BitSet conflictingAlts, final ATNConfigSet configs) {
        // Empty implementation
    }

    @Override
    public void reportContextSensitivity(final Parser recognizer, final DFA dfa, final int startIndex,
            final int stopIndex, final int prediction, final ATNConfigSet configs) {
        // Empty implementation
    }

    @Override
    public void reportError(final org.antlr.v4.runtime.RecognitionException re) {
        // Empty implementation
    }

    @Override
    public void reportError(final String[] tokenNames, final org.antlr.v4.runtime.RecognitionException re) {
        // Empty implementation
    }

    @Override
    public void reportError(final org.antlr.v4.runtime.IntStream input,
            final org.antlr.v4.runtime.RecognitionException re, final BitSet follow) {
        // Empty implementation
    }

    public void setStrictIncludes(final boolean strictInclude) {
        this.strictInclude = strictInclude;
    }
    
    public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName==null?"":environmentName.trim();
	}


}