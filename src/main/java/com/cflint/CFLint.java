package com.cflint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.runtime.BitSet;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import com.cflint.BugInfo.BugInfoBuilder;
import com.cflint.config.CFLintChainedConfig;
import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintConfiguration;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigUtils;
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
import cfml.parsing.cfscript.CFStatement;
import cfml.parsing.cfscript.CFStringExpression;
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
import cfml.parsing.cfscript.script.CFParsedStatement;
import cfml.parsing.cfscript.script.CFPropertyStatement;
import cfml.parsing.cfscript.script.CFReturnStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import cfml.parsing.cfscript.script.CFSwitchStatement;
import cfml.parsing.cfscript.script.CFTryCatchStatement;
import cfml.parsing.reporting.IErrorReporter;
import cfml.parsing.reporting.ParseException;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.EndTag;
import net.htmlparser.jericho.Source;

public class CFLint implements IErrorReporter {

    static final String FILE_ERROR = "FILE_ERROR";
    static final String PARSE_ERROR = "PARSE_ERROR";
    static final String PLUGIN_ERROR = "PLUGIN_ERROR";

    static final String RESOURCE_BUNDLE_NAME = "com.cflint.cflint";

    CFMLParser cfmlParser = new CFMLParser();
    StackHandler handler = new StackHandler();
    BugList bugs;
    final List<CFLintScanner> extensions = new ArrayList<CFLintScanner>();
    final List<String> allowedExtensions = new ArrayList<String>();
    boolean verbose = false;
    boolean logError = false;
    boolean quiet = false;
    boolean showProgress = false;
    boolean progressUsesThread = true;
    CFLintStats stats = new CFLintStats();

    // constants

    public CFLintStats getStats() {
		return stats;
	}

	final List<ScanProgressListener> scanProgressListeners = new ArrayList<ScanProgressListener>();
    final List<CFLintExceptionListener> exceptionListeners = new ArrayList<CFLintExceptionListener>();

    CFLintConfiguration configuration;
    
    // Stack to store include file depth to ensure no recursion
    final Stack<File> includeFileStack = new Stack<File>();

    public CFLint(final CFLintConfiguration configFile) throws IOException {
        configuration = configFile == null ? new CFLintConfig() : configFile;
        for (final PluginInfoRule ruleInfo : configuration.getRules()) {
            addScanner(ConfigUtils.loadPlugin(ruleInfo));// TODO load them all
        }
        final CFLintFilter filter = CFLintFilter.createFilter(verbose);
        bugs = new BugList(filter);
        if (exceptionListeners.isEmpty()) {
            addExceptionListener(new DefaultCFLintExceptionListener(bugs));
        }
        allowedExtensions.addAll(AllowedExtensionsLoader.init(RESOURCE_BUNDLE_NAME));
        cfmlParser.setErrorReporter(this);
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
            if(scanner instanceof CFLintSet){
            	((CFLintSet)scanner).setCFLint(this);
            }
        }
        CFLintFilter filter;
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
    }

    public void scan(final String folderName) {
        if (showProgress) {
            ScanningProgressMonitorLookAhead.createInstance(this, folderName, progressUsesThread).startPreScan();
        }
        scan(new File(folderName));
        fireClose();
    }

    public void scan(final File folderOrFile) {
        if (getBugs().getFileFilter() != null && !getBugs().getFileFilter().includeFile(folderOrFile)) {
            return;
        }
        if (folderOrFile.isDirectory()) {
            final CFLintConfiguration saveConfig = configuration;
            try {
                for (final File file : folderOrFile.listFiles()) {
                    if (file.getName().toLowerCase().startsWith(".cflintrc")) {
                        try {
                            System.out.println("read config " + file);
                            CFLintConfiguration newConfig = file.getName().toLowerCase().endsWith(".xml") ?
                                    ConfigUtils.unmarshal(new FileInputStream(file), CFLintConfig.class) :
                                    ConfigUtils.unmarshalJson(new FileInputStream(file), CFLintConfig.class);
                            configuration = new CFLintChainedConfig(newConfig, configuration);
                        } catch (Exception e) {
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
            final String src = FileUtil.loadFile(folderOrFile);
            includeFileStack.clear();
            try {
            	//Report number of lines in the source
            	stats.addFile(src==null||src.length()==0?0:src.split("\\R").length + 1);
                process(src, folderOrFile.getAbsolutePath());
            } catch (final Exception e) {
                printException(e);
                if (logError) {
                    System.out.println("Logging Error: " + FILE_ERROR);
                    fireCFLintException(e, FILE_ERROR, folderOrFile.getAbsolutePath(), null, null, null, null);
                }
            }
        }
    }

    protected void printException(final Exception e, Element... elem) {
        if (!quiet) {
            if (elem != null && elem.length > 0 && elem[0] != null) {
                final int line = elem[0].getSource().getRow(elem[0].getBegin());
                System.err.println("Error in: " + shortSource(elem[0].getSource(), line) + " @ " + line + ":");
            }
            if (verbose) {
                e.printStackTrace(System.err);
            } else {
                System.err.println(e.getMessage());
            }
        }
    }

    public void process(final String src, final String filename) throws ParseException, IOException {
    	
    	fireStartedProcessing(filename);
    	if(src==null || src.trim().length() == 0){
            final Context context = new Context(filename, null, null, false, handler);
    		reportRule(null, null, context,null, new ContextMessage("AVOID_EMPTY_FILES", null));
    	}else{
	        final CFMLSource cfmlSource = new CFMLSource(
	                src.contains("<!---") ? CommentReformatting.wrap(src) : src);
	        final ParserTag firstTag = getFirstTagQuietly(cfmlSource);
	        final List<Element> elements = new ArrayList<Element>();
	        if (firstTag != null) {
	            elements.addAll(cfmlSource.getChildElements());
	        }
	        if (src.contains("component")
	                && (elements.isEmpty() || elements.get(0).getBegin() > src.indexOf("component"))) {
	            // Check if pure cfscript
	            final CFScriptStatement scriptStatement = cfmlParser.parseScript(src);
	            Context context = new Context(filename, null, null, false, handler, scriptStatement.getTokens());
	            process(scriptStatement, context);
	        } else {
	            processStack(elements, " ", filename, null);
	        }
    	}
        fireFinishedProcessing(filename);
    }

    protected ParserTag getFirstTagQuietly(final CFMLSource cfmlSource) {
        try {
            return cfmlSource.getNextTag(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void processStack(final List<Element> elements, final String space, final String filename,
                             final CFIdentifier functionName) throws ParseException, IOException {
        Element commentElement = null;
        for (final Element elem : elements) {
            if (elem.getName().equals("!---")) {
                commentElement = elem;
            } else {
                final Context context = new Context(filename, elem, functionName, false, handler);
                if (commentElement != null) {
                    applyRuleOverrides(context, commentElement);
                    commentElement = null;
                }
                process(elem, space, context);

            }
        }
    }

    public void processStack(final List<Element> elements, final String space, final Context context)
            throws ParseException, IOException {
        Element commentElement = null;
        for (final Element elem : elements) {
            if (elem.getName().equals("!---")) {
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

    int skipToPosition = 0;
    private void process(final Element elem, final String space, final Context context)
            throws ParseException, IOException {
    	if (skipToPosition > elem.getBegin()) {
			return;
		}else{
			skipToPosition=0;
		}
        currentElement = elem;
        if (elem.getName().equalsIgnoreCase("cfcomponent")) {
            final Context componentContext = context.subContext(elem);
            componentContext.setInComponent(true);
            componentContext.setComponentName(elem.getAttributeValue("displayname"));
            componentContext.setContextType(ContextType.Component);
            handler.push("component");
            doStructureStart(elem, componentContext, CFCompDeclStatement.class);
        } else if (elem.getName().equalsIgnoreCase("cffunction")) {
            final Context functionContext = context.subContext(elem);
            functionContext.setFunctionName(elem.getAttributeValue("name"));
            functionContext.setContextType(ContextType.Function);
            handler.push("function");
            doStructureStart(elem, functionContext, CFFuncDeclStatement.class);
        } else if (elem.getName().equalsIgnoreCase("cfloop") && elem.getAttributeValue("query")!=null) {
        	//Give a cfloop for query its own context and set the column names as variables if they are available
        	final Context loopContext = context.subContext(elem);
            loopContext.setContextType(ContextType.QueryLoop);
            handler.push("cfloop");
            
            final String qryName = elem.getAttributeValue("query");
        	handler.addVariables(handler.getQueryColumns(qryName));
        	doStructureStart(elem, loopContext, CFFuncDeclStatement.class);
        }

        if (elem.getName().equalsIgnoreCase("cfset") || elem.getName().equalsIgnoreCase("cfif")
                || elem.getName().equalsIgnoreCase("cfelseif") || elem.getName().equalsIgnoreCase("cfreturn")) {
            scanElement(elem, context);
            final Pattern p = Pattern.compile("<\\w+\\s(.*[^/])/?>", Pattern.MULTILINE | Pattern.DOTALL);
            final String expr = elem.getFirstStartTag().toString();
            final Matcher m = p.matcher(expr);
            if (m.matches()) {

                // TODO if LUCEE?
                // final int uglyNotPos = elem.toString().lastIndexOf("<>");
                // int endPos = elem.getStartTag().getEnd() - 1;
                //
                // if (uglyNotPos > 0) {
                // final int nextPos = elem.toString().indexOf(">", uglyNotPos +
                // 2);
                // if (nextPos > 0 && nextPos < elem.getEndTag().getBegin()) {
                // endPos = nextPos;
                // }
                // }

                // final String cfscript =
                // elem.toString().substring(elem.getName().length() + 1,
                // Math.min(endPos,elem.toString().length()-1));
                final String cfscript = m.group(1);
                try {
                    final CFExpression expression = cfmlParser.parseCFExpression(cfscript, this);
                    if (expression != null) {
                        process(expression, elem, context);
                    }
                } catch (final Exception npe) {
                    printException(npe, elem);
                    fireCFLintException(npe, PARSE_ERROR, context.getFilename(), null, null, null, null);
                }
            }
            processStack(elem.getChildElements(), space + " ", context);

        } else if (elem.getName().equalsIgnoreCase("cfargument")) {
            scanElement(elem, context);
            final String name = elem.getAttributeValue("name");
            if (name != null) {
                handler.addArgument(name);
            }
            processStack(elem.getChildElements(), space + " ", context);
        } else if (elem.getName().equalsIgnoreCase("cfscript")) {
            scanElement(elem, context);
            String cfscript =elem.getContent().toString();
            if (elem.getEndTag() == null) {
				// Hack to fetch the entire cfscript text, if cfscript is a word in the content somewhere, and causes
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
            final CFScriptStatement scriptStatement = cfmlParser.parseScript(cfscript);

            final Context subcontext = context.subContext(elem);
            process(scriptStatement, subcontext);
            processStack(elem.getChildElements(), space + " ", context);
        } else if (elem.getName().equalsIgnoreCase("cffunction")) {
            final Context functionContext = context.subContext(elem);
            functionContext.setFunctionName(elem.getAttributeValue("name"));
            functionContext.setContextType(ContextType.Function);
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
                    fireCFLintException(e, PARSE_ERROR, context.getFilename(), null, null, null, null);
                }
            }
            handler.pop();
        } else if (elem.getName().equalsIgnoreCase("cfcomponent")) {
            final Context componentContext = context.subContext(elem);
            componentContext.setInComponent(true);
            componentContext.setComponentName(elem.getAttributeValue("displayname"));
            componentContext.setContextType(ContextType.Component);

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
                    fireCFLintException(e, PARSE_ERROR, context.getFilename(), null, null, null, null);
                }
            }
            handler.pop();
        } else if (elem.getName().equalsIgnoreCase("cfquery")) {
            scanElement(elem, context);
            final List<Element> list = elem.getAllElements();
            processStack(list.subList(1, list.size()), space + " ", context);
            //Save any columns from the cfquery
            final String qryName = elem.getAttributeValue("name");
            if(qryName!=null && qryName.trim().length()>0){
	            final String qryText = elem.getTextExtractor().toString().toUpperCase();
	            final Matcher m = Pattern.compile(".*SELECT\\s(\\w+(\\s*,\\s*\\w+)+)\\s+FROM\\s+.*").matcher(qryText);
	            final List<String> cols = new ArrayList<String>();
	            if(m.matches()){
	            	cols.addAll(Arrays.asList(m.group(1).trim().split("\\s*,\\s*")));
		            handler.addQueryColumnSet(qryName,cols);
	            }
            }
        } else if (elem.getName().equalsIgnoreCase("cfqueryparam")) {
            scanElement(elem, context);
            if (elem.getAttributeValue("value") != null) {
            }
        } else if (elem.getName().equalsIgnoreCase("cfinclude")) {
            scanElement(elem, context);
            final String path = elem.getAttributeValue("template");
            final File include = new File(new File(context.getFilename()).getParentFile(), path);
            if (strictInclude || include.exists()){
            	if(includeFileStack.contains(include)){
            		System.err.println("Terminated a recursive call to include file " + include);
            	}else{
	            	includeFileStack.push(include);
	                process(FileUtil.loadFile(include), context.getFilename());
	                includeFileStack.pop();
            	}
            }
        } else if (elem.getName().equalsIgnoreCase("cfloop") && elem.getAttributeValue("query")!=null) {
            scanElement(elem, context);
            processStack(elem.getChildElements(), space + " ", context);
            handler.pop();
        } else {
            scanElement(elem, context);
            processStack(elem.getChildElements(), space + " ", context);
        }
        // Process any messages added by downstream parsing.
        for (final ContextMessage message : context.getMessages()) {
            reportRule(elem, null, context, message.getSource(), message);
        }
        context.getMessages().clear();
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
                fireCFLintException(e, PLUGIN_ERROR, context.getFilename(), null, null, null, null);
            }
        }
    }

    private List<CFLintStructureListener> getStructureListeners(final List<CFLintScanner> extensions) {
        final List<CFLintStructureListener> retval = new ArrayList<CFLintStructureListener>();
        for (final CFLintScanner plugin : extensions) {
            if (plugin instanceof CFLintStructureListener) {
                retval.add((CFLintStructureListener) plugin);
            }
        }
        return retval;
    }

    private String shortSource(final Source source, final int line) {
        final String retval = source == null ? "" : source.toString().trim();
        if (retval.length() < 300) {
            return retval;
        }
        try {
            final BufferedReader sr = new BufferedReader(new StringReader(source.toString()));
            for (int i = 1; i < line; i++) {
                sr.readLine();
            }
            String sLine = sr.readLine();
            return sLine == null ? null : sLine.replaceAll("\t", " ");
        } catch (final Exception e) {
        }
        return retval.substring(0, 300);
    }

    Set<List<Object>> processed = new HashSet<List<Object>>();

    private void process(final CFScriptStatement expression, Context context) {
        if (expression == null) {
            return;
        }
        if (expression != null && expression.getToken() != null) {
            List<Object> checkItem = Arrays.asList(expression, expression.getToken());
            if (processed.contains(checkItem)) {
                System.err.println("Attempt to process expression twice aborted.  This may be a parsing bug in " + context.getFilename() + " : " + (expression.getToken() != null ? expression.getToken().getLine() : ""));
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
                    //TODO fix this to use getPropertyName() when it is available and not null.
                    Field field = CFPropertyStatement.class.getDeclaredField("propertyName");
                    field.setAccessible(true);
                    CFExpression value = (CFExpression) field.get(expression);
                    if (value == null) {
                        for (Entry<CFIdentifier, CFExpression> entry : ((CFPropertyStatement) expression).getAttributes().entrySet()) {
                            if ("name".equals(entry.getKey().getName())) {
                                value = entry.getValue();
                            }
                        }
                    }
                    String name = value.Decompile(0);
                    handler.addVariable(name.substring(1, name.length() - 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                scanExpression(expression, context, elem);
//                for(CFExpression expr: ((CFPropertyStatement) expression).decomposeExpression()){
//                    process(expr, elem, context);
//                }
            } else if (expression instanceof CFCompDeclStatement) {
                CFCompDeclStatement compDeclStatement = (CFCompDeclStatement) expression;
                final Context componentContext = context.subContext(null);
                componentContext.setInComponent(true);
                componentContext.setContextType(ContextType.Component);
                for (Entry<CFExpression, CFExpression> entry : compDeclStatement.getAttributes().entrySet()) {
                    if (entry.getKey() != null && entry.getKey().Decompile(0).equalsIgnoreCase("name")) {
                        componentContext.setComponentName(entry.getValue().Decompile(0));
                    }
                }
                // componentContext.setComponentName(compDeclStatement.get); //
                // TODO

                // Register any overrides from multi-line comments.
                registerRuleOverrides(componentContext, expression.getToken());
                // do startComponent notifications
                doStructureStart(elem, componentContext, expression.getClass());

                scanExpression(compDeclStatement, componentContext, elem);
                // process the component declaration
                if (compDeclStatement.getBody() instanceof CFCompoundStatement) {
                    //Process property expressions first
                    for (CFScriptStatement subscript : compDeclStatement.getBody().decomposeScript()) {
                        if (subscript instanceof CFPropertyStatement) {
                            process(subscript, componentContext);
                        }
                    }
                    for (CFScriptStatement subscript : compDeclStatement.getBody().decomposeScript()) {
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
                        fireCFLintException(e, PARSE_ERROR, context.getFilename(), null, null, null, null);
                    }
                }
            } else if (expression instanceof CFForStatement) {
                scanExpression(expression, context, elem);
                process(((CFForStatement) expression).getInit(), elem, context);
                process(((CFForStatement) expression).getCond(), elem, context);
                process(((CFForStatement) expression).getNext(), elem, context);
                process(((CFForStatement) expression).getBody(), context);
            } else if (expression instanceof CFForInStatement) {
                scanExpression(expression, context, elem);
                process(((CFForInStatement) expression).getVariable(), elem, context);
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
                for (CFCase _case : cfswitch.getCases()) {
                    process(_case, context);
                }
            } else if (expression instanceof CFCase) {
                scanExpression(expression, context, elem);
                final CFCase cfcase = (CFCase) expression;
                for (CFScriptStatement cfstatement : cfcase.getStatements()) {
                    process(cfstatement, context);
                }
            } else if (expression instanceof CFTryCatchStatement) {
                scanExpression(expression, context, elem);
                final CFTryCatchStatement cftry = (CFTryCatchStatement) expression;
                process(cftry.getBody(), context);
                for (CFCatchStatement stmt : cftry.getCatchStatements()) {
                    process(stmt.getCatchBody(), context);
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
                functionContext.setContextType(ContextType.Function);
                functionContext.setFunctionInfo(function);

                registerRuleOverrides(functionContext, function.getToken());
                handler.push("function");
                for (final CFFunctionParameter param : function.getFormals()) {
                    handler.addArgument(param.getName());
                }
                doStructureStart(elem, functionContext, CFFuncDeclStatement.class);
                scanExpression(expression, functionContext, elem);

                Context functionBodyContext = functionContext.subContext(null);
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
                        fireCFLintException(e, PARSE_ERROR, context.getFilename(), null, null, null, null);
                    }
                }
                handler.pop();
            } else if (expression instanceof CFIncludeStatement) {
                scanExpression(expression, context, elem);
                final List<CFExpression> subExpressions = ((CFStringExpression) ((CFIncludeStatement) expression)
                        .getTemplate()).getSubExpressions();
                if (subExpressions.size() == 1 && subExpressions.get(0) instanceof CFLiteral) {
                    final String path = ((CFLiteral) subExpressions.get(0)).getVal();
                    final File include = new File(new File(context.getFilename()).getParentFile(), path);
                    if(include.exists() || strictInclude){
                        try {
                        	if(includeFileStack.contains(include)){
                        		System.err.println("Terminated a recursive call to include file " + include);
                        	}else{
            	            	includeFileStack.push(include);
            	                process(FileUtil.loadFile(include), context.getFilename());
            	                includeFileStack.pop();
                        	}
                        } catch (IOException ex) {
                            System.err.println("Invalid include file " + context.getFilename());
                            final int line = context.startLine();
                            fireCFLintException(ex, PARSE_ERROR, context.getFilename(), line, 1, "",
                                    "Invalid include file " + expression.getClass());
                        }
                    }
                } else if(strictInclude){
                    System.err.println("Unable to resolve template value " + context.getFilename());
                    final int line = context.startLine();
                    fireCFLintException(new Exception(), PARSE_ERROR, context.getFilename(), line, 1, "",
                            "Unable to resolve template value " + expression.getClass());
                }
            } else {
                scanExpression(expression, context, elem);
            }
        } catch (final StackOverflowError soe) {
            System.err.println("Stack overflow in " + context.getFilename());
            final int line = context.startLine();
            fireCFLintException(soe, PARSE_ERROR, context.getFilename(), line, 1, "",
                    "Stack overflow on " + expression.getClass());
        }
        // Process any messages added by downstream parsing.
        for (final ContextMessage message : context.getMessages()) {
            reportRule(elem, null, context, message.getSource(), message);
        }
        context.getMessages().clear();

    }

    protected void doStructureStart(final Element elem, final Context context,
                                    Class<? extends CFScriptStatement> class1) {
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
                fireCFLintException(e, PARSE_ERROR, context.getFilename(), null, null, null, null);
            }
        }
    }

    protected void scanExpression(final CFScriptStatement expression, Context context, final Element elem) {
        if (expression == null)
            return;
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
                fireCFLintException(e, PLUGIN_ERROR, context.getFilename(), null, null, null, null);
            }
        }
    }

    /**
     Register any overrides from multi-line comments.
     * @param context       The current context.
     * @param functionToken A token that points to the current function
     */
    protected void registerRuleOverrides(Context context, final Token functionToken) {
        final String mlText = PrecedingCommentReader.getMultiLine(context, functionToken);
        if (mlText != null && !mlText.isEmpty()) {
            final Pattern pattern = Pattern.compile(".*\\s*@CFLintIgnore\\s+([\\w,_]+)\\s*.*", Pattern.DOTALL);
            final Matcher matcher = pattern.matcher(mlText);
            if (matcher.matches()) {
                String ignoreCodes = matcher.group(1);
                context.ignore(Arrays.asList(ignoreCodes.split(",\\s*")));
            }
        }
    }
    
    /**
     * @param context       The current context.
     * @param expression the expression statement to check
     */
    protected void registerRuleOverrides(Context context, final CFExpressionStatement expression) {
    	if(expression.getTokens() == null) {
    		return;
    	}
        Iterable<Token> tokens = expression.getTokens().getTokens();
        for (Token currentTok : tokens) {
        	System.out.println(currentTok.toString());
            if (currentTok.getLine() == expression.getExpression().getLine()) {
                if (currentTok.getChannel() == Token.HIDDEN_CHANNEL && currentTok.getType() == CFSCRIPTLexer.LINE_COMMENT) {
                	final String commentText = currentTok.getText().replaceFirst("^//\\s*", "").trim();
                	if (commentText.startsWith("cflint ")) {
                		Pattern pattern = Pattern.compile("cflint\\s+ignore:([\\w,]+).*");
                		final Matcher matcher = pattern.matcher(commentText);
                		if (matcher.matches()) {
                			String ignoreCodes = matcher.group(1);
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
     * @param context        The current context.
     * @param commentElement The CFML comment element
     */
    protected void applyRuleOverrides(Context context, Element commentElement) {

        if (commentElement != null && "!---".equals(commentElement.getName())) {
            String mlText = commentElement.toString();
            Pattern pattern = Pattern.compile(".*\\s*@CFLintIgnore\\s+([\\w,_]+)\\s*.*", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(mlText);
            if (matcher.matches()) {
                String ignoreCodes = matcher.group(1);
                context.ignore(Arrays.asList(ignoreCodes.split(",\\s*")));
            }
        }
    }
    
/**
 * 
 * @param expression	CF expression
 * @param elem			Jericho HTML element
 * @param oldcontext	The previous context
 */
    private void process(final CFExpression expression, final Element elem, Context oldcontext) {

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
                    fireCFLintException(e, PLUGIN_ERROR, context.getFilename(), null, null, null, null);
                }
            }
            // Handle a few expression types in a special fashion.
            if (expression instanceof CFVarDeclExpression) {
                handler.addVariable(((CFVarDeclExpression) expression).getName());
            }


            //CFIdentifier should not decompose
            if (expression instanceof CFIdentifier) {
                final String name = ((CFIdentifier) expression).getName();
                handler.checkVariable(name);
            } 
            if (expression instanceof CFAssignmentExpression && !(expression instanceof CFTernaryExpression)) {
                final Context assignmentContext = context.subContext(elem);
                assignmentContext.setInAssignmentExpression(true);
                process(((CFAssignmentExpression) expression).getLeft(), elem, assignmentContext);
                // Right hand side is handled below. Left hand side gets a
                // special context.
                process(((CFAssignmentExpression) expression).getRight(), elem, context);
                //Only process function call expressions
            } else if (expression instanceof CFFullVarExpression) {
                final CFFullVarExpression fullVarExpression = (CFFullVarExpression) expression;
                if(context.isInAssignmentExpression() && new CFScopes().isScoped(fullVarExpression,"local") && fullVarExpression.getExpressions().size()>1){
                    handler.addVariable(fullVarExpression.getExpressions().get(1).Decompile(0));
                }
                Context subContext = context.subContext(context.getElement());
                subContext.setInAssignmentExpression(false);
                for (final CFExpression expr : fullVarExpression.getExpressions()) {
                    if (expr instanceof CFFunctionExpression) {
                        process(expr, elem, subContext);
                    }
                    if (expr instanceof CFMember){
                        process(((CFMember) expr).getExpression(), elem, subContext);
                    }
                    if (expr instanceof CFArrayExpression){
                    	CFArrayExpression aryExpr = (CFArrayExpression)expr;
                    	if(aryExpr.getElements().size()>0){
                    		process(aryExpr.getElements().get(0), elem, subContext);
                    	}
                    }
                }
            } else {
                // Loop into all relevant nested (child) expressions.
                for (CFExpression child : expression.decomposeExpression()) {
                    process(child, elem, context);
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
 *  Returns the previous sibling of a given element
 * @param element   The Jericho HTML element object
 * @return
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
     * @param element	The element to process
     * @param msgcode	The message code to check for
     * @return
     */
    protected boolean checkForDisabled(final Element element, final String msgcode) {
        Element elem = element;
        while (elem != null) {
            final Element prevSibling = getPreviousSibling(elem);
            if (prevSibling != null && prevSibling.getName().equals("!---")) {
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

    public void reportRule(final Element elem, final Object expression, final Context context,
                              final CFLintScanner pluginParm, final ContextMessage msg) {

        final String msgcode = msg.getMessageCode();
        final String nameVar = msg.getVariable();
        final CFLintScanner plugin = msg.getSource() == null ? pluginParm : msg.getSource();
        if (checkForDisabled(elem, msgcode)) {
            return;
        }
        if (configuration == null) {
            throw new NullPointerException("Configuration is null");
        }
        PluginInfoRule ruleInfo;
        if ("PLUGIN_ERROR".equals(msgcode)) {
            ruleInfo = new PluginInfoRule();
            final PluginMessage msgInfo = new PluginMessage("PLUGIN_ERROR");
            msgInfo.setMessageText("Error in plugin: ${variable}");
            msgInfo.setSeverity("ERROR");
            ruleInfo.getMessages().add(msgInfo);
        }else if ("AVOID_EMPTY_FILES".equals(msgcode)) {
            ruleInfo = new PluginInfoRule();
            final PluginMessage msgInfo = new PluginMessage("AVOID_EMPTY_FILES");
            msgInfo.setMessageText("CF file is empty: ${file}");
            msgInfo.setSeverity("WARNING");
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
        if (configuration == null) {
            throw new NullPointerException(
                    "Message definition not found for [" + msgcode + "] in " + plugin.getClass().getSimpleName());
        }
        final BugInfoBuilder bldr = new BugInfo.BugInfoBuilder().setMessageCode(msgcode).setVariable(nameVar)
                .setFunction(context.getFunctionName()).setFilename(context.getFilename())
                .setComponent(context.getComponentName());
        if (msgInfo != null) {
            bldr.setSeverity(msgInfo.getSeverity());
            bldr.setMessage(msgInfo.getMessageText());
        } else {
            String errMessage = "Message code: " + msgcode + " is not configured correctly.";
            fireCFLintException(new NullPointerException(errMessage), PLUGIN_ERROR, "", null, null, null, null);
            bldr.setSeverity("WARNING");
            bldr.setMessage(msgcode);
        }
        if (expression instanceof CFStatement) {
            bldr.setExpression(((CFStatement) expression).Decompile(0));
        } else if (expression instanceof CFScriptStatement) {
            bldr.setExpression(((CFScriptStatement) expression).Decompile(0));
        } else if (elem != null) {
            bldr.setExpression(elem.toString());
        }
        bldr.setRuleParameters(ruleInfo.getParameters());
        if (configuration.includes(ruleInfo.getMessageByCode(msgcode))
                && !configuration.excludes(ruleInfo.getMessageByCode(msgcode))) {
            if (expression instanceof CFExpression) {
                BugInfo bugInfo = bldr.build((CFExpression) expression, elem);
                final Token token = ((CFExpression) expression).getToken();
                if (!suppressed(bugInfo, token, context)) {
                    bugs.add(bugInfo);
                }
            } else {
                final BugInfo bug = bldr.build((CFParsedStatement) expression, elem);
                if (msg.getLine() != null) {
                    bug.setLine(msg.getLine());
                    bug.setColumn(0);
                }
                if (context != null && !context.isSuppressed(bug)) {
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
    protected boolean suppressed(BugInfo bugInfo, Token token, Context context) {
        if (context == null || context.isSuppressed(bugInfo))
            return true;
        Iterable<Token> tokens = context.afterTokens(token);
        for (Token currentTok : tokens) {
            if (currentTok.getLine() != token.getLine()) {
                break;
            }
            if (currentTok.getChannel() == Token.HIDDEN_CHANNEL && currentTok.getType() == CFSCRIPTLexer.LINE_COMMENT) {
                final String commentText = currentTok.getText().replaceFirst("^//\\s*", "").trim();
                if (commentText.startsWith("cflint ")) {
                    Pattern pattern = Pattern.compile("cflint\\s+ignore:([\\w,]+).*");
                    Matcher matcher = pattern.matcher(commentText);
                    if (matcher.matches() && matcher.groupCount() > 0) {
                        final String ignoreCodes = matcher.group(1);
                        if (ignoreCodes.equalsIgnoreCase("line")) {
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
        this.allowedExtensions.addAll(allowedExtensions);
    }

    @Override
    public void reportError(final String arg0) {
    	//Empty implementation
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
                fireCFLintException(e, PARSE_ERROR, srcidentifier, null, null, null, null);
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
                fireCFLintException(e, PARSE_ERROR, srcidentifier, null, null, null, null);
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
        if (plugin != null){
            extensions.add(plugin);
            if(plugin instanceof CFLintSet){
            	((CFLintSet)plugin).setCFLint(this);
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

    String currentFile = null;
    Element currentElement = null;
    private boolean strictInclude;

    @Override
    public void syntaxError(final Recognizer<?, ?> recognizer, final Object offendingSymbol, int line,
                            int charPositionInLine, final String msg, final org.antlr.v4.runtime.RecognitionException e) {
        final String file = currentFile == null ? "" : currentFile + "\r\n";
        String expression = null;
        if (offendingSymbol instanceof Token) {
            expression = ((Token) offendingSymbol).getText();
            if (expression.length() > 50) {
                expression = expression.substring(1, 40) + "...";
            }
        }
        if (currentElement != null) {
            if (line == 1) {
                line = currentElement.getSource().getRow(currentElement.getBegin());
                charPositionInLine = charPositionInLine
                        + currentElement.getSource().getColumn(currentElement.getBegin());
            } else {
                line = currentElement.getSource().getRow(currentElement.getBegin()) + line - 1;
            }
        }
        if (recognizer instanceof Parser && ((Parser) recognizer).isExpectedToken(CFSCRIPTParser.SEMICOLON)) {
            bugs.add(new BugInfo.BugInfoBuilder().setMessageCode("MISSING_SEMI").setFilename(file)
                    .setMessage("End of statement(;) expected instead of " + expression).setSeverity("ERROR")
                    .setExpression(expression).setLine(line).setColumn(charPositionInLine).build());

        } else {
            fireCFLintException(e, PARSE_ERROR, file, line, charPositionInLine, "", msg);
        }
    }

    @Override
    public void reportAmbiguity(final Parser recognizer, final DFA dfa, final int startIndex, final int stopIndex,
                                final boolean exact, final java.util.BitSet ambigAlts, final ATNConfigSet configs) {
    	//Empty implementation
    }

    @Override
    public void reportAttemptingFullContext(final Parser recognizer, final DFA dfa, final int startIndex,
                                            final int stopIndex, final java.util.BitSet conflictingAlts, final ATNConfigSet configs) {
    	//Empty implementation
    }

    @Override
    public void reportContextSensitivity(final Parser recognizer, final DFA dfa, final int startIndex,
                                         final int stopIndex, final int prediction, final ATNConfigSet configs) {
    	//Empty implementation
    }

    @Override
    public void reportError(final org.antlr.v4.runtime.RecognitionException re) {
    	//Empty implementation
    }

    @Override
    public void reportError(final String[] tokenNames, final org.antlr.v4.runtime.RecognitionException re) {
    	//Empty implementation
    }

    @Override
    public void reportError(final org.antlr.v4.runtime.IntStream input,
                            final org.antlr.v4.runtime.RecognitionException re, final BitSet follow) {
    	//Empty implementation
    }

    public void setStrictIncludes(boolean strictInclude) {
        this.strictInclude=strictInclude;
    }

}
