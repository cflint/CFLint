package com.cflint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import cfml.CFSCRIPTLexer;
import cfml.CFSCRIPTParser;
import cfml.parsing.CFMLParser;
import cfml.parsing.CFMLSource;
import cfml.parsing.cfscript.CFAssignmentExpression;
import cfml.parsing.cfscript.CFBinaryExpression;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFLiteral;
import cfml.parsing.cfscript.CFNestedExpression;
import cfml.parsing.cfscript.CFStatement;
import cfml.parsing.cfscript.CFStringExpression;
import cfml.parsing.cfscript.CFUnaryExpression;
import cfml.parsing.cfscript.CFVarDeclExpression;
import cfml.parsing.cfscript.script.CFCompDeclStatement;
import cfml.parsing.cfscript.script.CFCompoundStatement;
import cfml.parsing.cfscript.script.CFExpressionStatement;
import cfml.parsing.cfscript.script.CFForInStatement;
import cfml.parsing.cfscript.script.CFForStatement;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFIfStatement;
import cfml.parsing.cfscript.script.CFParsedStatement;
import cfml.parsing.cfscript.script.CFReturnStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import cfml.parsing.reporting.IErrorReporter;
import cfml.parsing.reporting.ParseException;

import com.cflint.BugInfo.BugInfoBuilder;
import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigRuntime;
import com.cflint.config.ConfigUtils;
import com.cflint.listeners.ProgressMonitorListener;
import com.cflint.listeners.ScanProgressListener;
import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.CFLintStructureListener;
import com.cflint.plugins.Context;
import com.cflint.plugins.Context.ContextMessage;
import com.cflint.plugins.exceptions.CFLintExceptionListener;
import com.cflint.plugins.exceptions.DefaultCFLintExceptionListener;
import com.cflint.tools.CFLintFilter;

public class CFLint implements IErrorReporter {

	private static final String FILE_ERROR = "FILE_ERROR";
	private static final String PARSE_ERROR = "PARSE_ERROR";
	final CFMLParser cfmlParser = new CFMLParser();
	
	
	StackHandler handler = new StackHandler();
	boolean inFunction = false;
	boolean inAssignment = false;
	boolean inComponent = false;
	BugList bugs;
	List<CFLintScanner> extensions = new ArrayList<CFLintScanner>();
	List<String> allowedExtensions = new ArrayList<String>();
	boolean verbose = false;
	boolean logError = false;
	boolean quiet = false;
	boolean showProgress = false;
	boolean progressUsesThread = true;

	// constants
	final String cfcExtension = ".cfc";
	final String cfmExtenstion = ".cfm";
	final String resourceBundleName = "com.cflint.cflint";
	final String allowedExtensionsName = "allowedextensions";
	final String progressMonitorName = "CFLint";

	private String currentFile;
	List<ScanProgressListener> scanProgressListeners = new ArrayList<ScanProgressListener>();
	List<CFLintExceptionListener> exceptionListeners = new ArrayList<CFLintExceptionListener>();
	
	ConfigRuntime configuration;
	private Stack<Element> currentElement = new Stack<Element>();

	public CFLint() throws IOException {
		this((CFLintConfig)null);
	}
	public CFLint(CFLintConfig configFile) throws IOException {
		final CFLintPluginInfo pluginInfo = ConfigUtils.loadDefaultPluginInfo();
		configuration = new ConfigRuntime(configFile,pluginInfo);
		for(PluginInfoRule ruleInfo:configuration.getRules()){
			extensions.add(ConfigUtils.loadPlugin(ruleInfo));
		}
		final CFLintFilter filter = CFLintFilter.createFilter(verbose);
		bugs = new BugList(filter);
		if(exceptionListeners.size() == 0){
			addExceptionListener(new DefaultCFLintExceptionListener(bugs));
		}
		try {
			allowedExtensions.addAll(Arrays.asList(ResourceBundle.getBundle(resourceBundleName)
					.getString(allowedExtensionsName).split(",")));
		} catch (final Exception e) {
			allowedExtensions.add(cfcExtension);
			allowedExtensions.add(cfmExtenstion);
		}
		cfmlParser.setErrorReporter(this);
	}
	public CFLint(final CFLintScanner... bugsScanners) {
		this(null,bugsScanners);
	}
	@Deprecated
	public CFLint(ConfigRuntime configuration,final CFLintScanner... bugsScanners) {
		super();
		this.configuration=configuration;
		// DictionaryPreferences dprefs = new DictionaryPreferences();
		// dprefs.setDictionaryDir("C:\\projects\\cfml.dictionary-master\\dictionary");
		// DictionaryManager.initDictionaries(dprefs);

		for (final CFLintScanner scanner : bugsScanners) {
			extensions.add(scanner);
			if(configuration != null){
				PluginInfoRule ruleInfo = configuration.getRuleByClass(scanner.getClass());
				if(ruleInfo != null){
					ruleInfo.setPluginInstance(scanner);
				}
			}
		}
		CFLintFilter filter;
		try {
			filter = CFLintFilter.createFilter(verbose);
			bugs = new BugList(filter);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(exceptionListeners.size() == 0){
			addExceptionListener(new DefaultCFLintExceptionListener(bugs));
		}
		try {
			allowedExtensions.addAll(Arrays.asList(ResourceBundle.getBundle(resourceBundleName)
					.getString(allowedExtensionsName).split(",")));
		} catch (final Exception e) {
			allowedExtensions.add(cfcExtension);
			allowedExtensions.add(cfmExtenstion);
		}
		cfmlParser.setErrorReporter(this);
	}

	public void scan(final String folder) {
		final File f = new File(folder);
		if (showProgress) {
			final ProgressMonitorListener progressMonitorListener = new ProgressMonitorListener(progressMonitorName);
			addScanProgressListener(progressMonitorListener);
			if (progressUsesThread) {
				new Thread(new Runnable() {
					
					public void run() {
						prescan(f,0,progressMonitorListener);
					}
				}).start();
			}else{
				prescan(f,0,progressMonitorListener);
			}
		}
		scan(f);
		fireClose();
	}

	private int prescan(File folderOrFile, int counter,final ProgressMonitorListener progressMonitorListener) {
		if (folderOrFile.isDirectory()) {
			for (final File file : folderOrFile.listFiles()) {
				counter=prescan(file,counter,progressMonitorListener);
			}
			if(counter>10){
				
				progressMonitorListener.setTotalToProcess(counter);
			}
			return counter;
		} else if (!folderOrFile.isHidden() && checkExtension(folderOrFile)) {
			return counter+1;
		}else{
			return counter;
		}
	}

	public void scan(final File folderOrFile) {
		if (folderOrFile.isDirectory()) {
			for (final File file : folderOrFile.listFiles()) {
				scan(file);
			}
		} else if (!folderOrFile.isHidden() && checkExtension(folderOrFile)) {
			final String src = load(folderOrFile);
			// System.out.println("processing " + file);
			try {
				process(src, folderOrFile.getAbsolutePath());
			} catch (final Exception e) {
				if (!quiet) {
					if (verbose) {
						e.printStackTrace(System.err);
					}else{
						System.err.println(e.getMessage());
					}
				}
				/*bugs.add(new BugInfo.BugInfoBuilder().setMessageCode("FILE_ERROR")
						.setFilename(folderOrFile.getAbsolutePath()).setMessage(e.getMessage()).setSeverity("ERROR")
						.build());*/
				if (logError) {
					System.out.println("Logging Error: " + FILE_ERROR);
					fireCFLintException(e,FILE_ERROR,folderOrFile.getAbsolutePath(),null,null,null,null);
				}
			}
		}
	}

	protected boolean checkExtension(final File file) {
		for (final String ext : allowedExtensions) {
			if (file.getName().endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	static String load(final File file) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			final byte[] b = new byte[fis.available()];
			fis.read(b);
			return new String(b);
		} catch (final Exception e) {
			return null;
		}
	}
	
	int parserCounter = 1;

	public void process(final String src, final String filename) throws ParseException, IOException {
		fireStartedProcessing(filename);
		final CFMLSource cfmlSource = new CFMLSource(src);
		final List<Element> elements = cfmlSource.getChildElements();
		if (elements.size() == 0 && src.contains("component")) {
			// Check if pure cfscript
			final CFScriptStatement scriptStatement = cfmlParser.parseScript(src);
			process(scriptStatement, filename, null, (String)null);
		} else {
			processStack(elements, " ", filename, null);
		}
		fireFinishedProcessing(filename);
	}

	public void processStack(final List<Element> elements, final String space, final String filename,
			final CFIdentifier functionName) throws ParseException, IOException {
		for (final Element elem : elements) {
			final Context context = new Context(filename, elem, functionName, inAssignment, handler);
			process(elem,space,context);
		}
	}
	public void processStack(final List<Element> elements, final String space, final Context context) throws ParseException, IOException {
		for (final Element elem : elements) {
			process(elem, space, context.subContext(elem));
		}
	}

	
	private void process(final Element elem, final String space, Context context)
			throws ParseException, IOException {
		context.setInComponent(inComponent);
		currentElement.push(elem);

		if (elem.getName().equalsIgnoreCase("cfcomponent")) {
			inComponent = true;
			handler.push("component");

			for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
				try{
					structurePlugin.startComponent(context, bugs);
				}catch(Exception e){
					e.printStackTrace();
				}
			}

			context.setInComponent(true);
			context.setComponentName(elem.getAttributeValue("displayname"));
		}
		else if (elem.getName().equalsIgnoreCase("cffunction")) {
			context.setFunctionName(elem.getAttributeValue("name"));
			inFunction = true;
			handler.push("function");
			
			for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
				try{
					structurePlugin.startFunction(context, bugs);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		try{
		for (final CFLintScanner plugin : extensions) {
			try{
				plugin.element(elem, context, bugs);
				for(final ContextMessage message : context.getMessages()){
					reportRule(elem, null, context, plugin, message);
				}
				context.getMessages().clear();
			}catch(Exception e){
				if(verbose){
					e.printStackTrace();
				}
				reportRule(elem, null, context, plugin, "PLUGIN_ERROR:" + exceptionText(e));
			}
		}
		if (elem.getName().equalsIgnoreCase("cfset") || elem.getName().equalsIgnoreCase("cfif")
				|| elem.getName().equalsIgnoreCase("cfelseif")|| elem.getName().equalsIgnoreCase("cfreturn")) {
			final int elemLine = elem.getSource().getRow(elem.getBegin());
			final int elemColumn = elem.getSource().getColumn(elem.getBegin());
			final Pattern p = Pattern.compile("<\\w+\\s(.*[^/])/?>",Pattern.MULTILINE|Pattern.DOTALL);
			final String expr = elem.getFirstStartTag().toString();
			final Matcher m = p.matcher(expr);
			if (m.matches()) {
				final String cfscript = m.group(1);
				try {
//					if(elem.getName().equalsIgnoreCase("cfset")){
//						System.out.println("parse: " + cfscript + ";");
//						final CFScriptStatement scriptStatement = cfmlParser.parseScript(cfscript + ";");
//						process(scriptStatement, context.getFilename(), elem, context.getFunctionName());
//					}else{
						final CFExpression expression = cfmlParser.parseCFExpression(cfscript,this);
						
						if (expression == null) {
							throw new NullPointerException("expression is null, parsing error");
						}
						process(expression, context.getFilename(), elem, context.getFunctionName());
//					}
				} catch (final Exception npe) {
					final int line = elem.getSource().getRow(elem.getBegin());
					final int column = elem.getSource().getColumn(elem.getBegin());
					if (!quiet) {
						System.err.println("Error in: " + shortSource(elem.getSource(), line) + " @ " + line + ":");
						if (verbose) {
							npe.printStackTrace(System.err);
						}
					}
					/*bugs.add(new BugInfo.BugInfoBuilder().setLine(elemLine).setColumn(elemColumn + column)
							.setMessageCode("PARSE_ERROR").setSeverity("ERROR").setExpression(m.group(1))
							.setFilename(filename).setFunction(functionName).setMessage("Unable to parse").build());*/
//					if (logError) {
//						System.out.println("Logging Error: " + PARSE_ERROR);
//						fireCFLintException(npe,PARSE_ERROR,context.getFilename(),elemLine,elemColumn + column,context.getFunctionName(),m.group(1));
//					}
				}
			}
			
		} else if (elem.getName().equalsIgnoreCase("cfargument")) {
			final String name = elem.getAttributeValue("name");
			if (name != null) {
				handler.addArgument(name);
			}
		} else if (elem.getName().equalsIgnoreCase("cfscript")) {
			final String cfscript = elem.getContent().toString();
			final CFScriptStatement scriptStatement = cfmlParser.parseScript(cfscript);

			process(scriptStatement, context.getFilename(), elem, context.getFunctionName());
			// } else if (elem.getName().equalsIgnoreCase("cfoutput")) {
			// final Element parent = CFTool.getNamedParent(elem, "cfoutput");
			// if (parent != null && parent.getAttributeValue("query") != null
			// && parent.getAttributeValue("group") == null) {
			// final int line = elem.getSource().getRow(elem.getBegin());
			// final int column = elem.getSource().getColumn(elem.getBegin());
			// bugs.add(new
			// BugInfo.BugInfoBuilder().setLine(line).setColumn(column).setMessageCode("NESTED_CFOUTPUT")
			// .setSeverity("ERROR").setFilename(filename)
			// .setMessage("Nested CFOutput, outer CFOutput has @query.").build());
			// }
		} else {
		}

		if (elem.getName().equalsIgnoreCase("cffunction")) {
			processStack(elem.getChildElements(), space + " ", context);
			for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
				try{
					structurePlugin.endFunction(context, bugs);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			inFunction = false;
			handler.pop();
		} else if (elem.getName().equalsIgnoreCase("cfcomponent")) {
			processStack(elem.getChildElements(), space + " ", context);
			for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
				try{
					structurePlugin.endComponent(context, bugs);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			inComponent = false;
			handler.pop();
		} else if (elem.getName().equalsIgnoreCase("cfquery")) {
			final List<Element> list = elem.getAllElements();
			processStack(list.subList(1, list.size()), space + " ", context);
		} else if (elem.getName().equalsIgnoreCase("cfqueryparam")) {
			if (elem.getAttributeValue("value") != null) {
				//final CFScriptStatement scriptStatement = cfmlParser.parseScript(elem.getAttributeValue("value") + ";");
				//process(scriptStatement, context.getFilename(), elem, context.getFunctionName());
				//TODO this could be 'parsed if the value has a # sign
			}
		} else {
			processStack(elem.getChildElements(), space + " ", context);
		}
		}finally{
			currentElement.pop();
		}
	}

	private List<CFLintStructureListener> getStructureListeners(final List<CFLintScanner> extensions) {
		final List<CFLintStructureListener> retval = new ArrayList<CFLintStructureListener>();
		for(CFLintScanner plugin: extensions)
		if(plugin instanceof CFLintStructureListener){
			retval.add((CFLintStructureListener) plugin);
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
			return sr.readLine().replaceAll("\t", " ");
		} catch (final Exception e) {
		}
		return retval.substring(0, 300);
	}

	public String stripLineComments(final String cfscript) {
		return cfscript.replaceAll("//[^\r\n]*\r?\n", "\r\n");
	}

	private void process(final CFScriptStatement expression, final String filename, final Element elem,
			final CFIdentifier functionName) {
		process(expression,filename,elem,functionName.getName());
	}
	private void process(final CFScriptStatement expression, final String filename, final Element elem,
			String functionName) {
		final Context context = new Context(filename, elem, functionName, inAssignment, handler);
		
		context.setInComponent(inComponent);
		if (expression instanceof CFCompDeclStatement) {
			inComponent = true;
			//do startComponent notifications
			for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
				try{
					structurePlugin.startComponent(context, bugs);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		else if (expression instanceof CFFuncDeclStatement) {
			final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
			functionName = function.getName().getName();
			context.setFunctionName(functionName);
			inFunction = true;
			handler.push("function");
			for (final CFFunctionParameter param : function.getFormals()) {
				handler.addArgument(param.getName());
			}
			for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
				try{
					structurePlugin.startFunction(context, bugs);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		for (final CFLintScanner plugin : extensions) {
			try{
				plugin.expression(expression, context, bugs);
				for(final ContextMessage message : context.getMessages()){
					reportRule(elem, expression, context, plugin, message);
				}
				context.getMessages().clear();
			}catch(Exception e){
				if(verbose){
					e.printStackTrace();
				}
				reportRule(elem, expression, context, plugin, "PLUGIN_ERROR:" + exceptionText(e));
			}
		}

		if (expression instanceof CFCompoundStatement) {
			for (final CFScriptStatement statement : ((CFCompoundStatement) expression).getStatements()) {
				process(statement, filename, elem, functionName);
			}
		} else if (expression instanceof CFExpressionStatement) {
			process(((CFExpressionStatement) expression).getExpression(), filename, elem, functionName);
		} else if (expression instanceof CFCompDeclStatement) {
			//process the component declaration
			process(((CFCompDeclStatement) expression).getBody(), filename, elem, functionName);
			//do endComponent notifications
			for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
				try{
					structurePlugin.endComponent(context, bugs);
				}catch(Exception e){
					e.printStackTrace();
				}
			}

			inComponent = false;
		} else if (expression instanceof CFForStatement) {
			process(((CFForStatement) expression).getInit(), filename, elem, functionName);
			process(((CFForStatement) expression).getCond(), filename, elem, functionName);
			process(((CFForStatement) expression).getNext(), filename, elem, functionName);
			process(((CFForStatement) expression).getBody(), filename, elem, functionName);
		} else if (expression instanceof CFForInStatement) {
			process(((CFForInStatement) expression).getVariable(), filename, elem, functionName);
			process(((CFForInStatement) expression).getStructure(), filename, elem, functionName);
			process(((CFForInStatement) expression).getBody(), filename, elem, functionName);
		} else if (expression instanceof CFIfStatement) {
			final CFIfStatement cfif = (CFIfStatement) expression;
			process(cfif.getCond(), filename, elem, functionName);
			process(cfif.getThenStatement(), filename, elem, functionName);
			if (cfif.getElseStatement() != null) {
				process(cfif.getElseStatement(), filename, elem, functionName);
			}
		} else if (expression instanceof CFReturnStatement) {
			final CFReturnStatement cfreturn = (CFReturnStatement) expression;
			//TODO replace with cfreturn.getExpression() when next cfmlparser is released
			Field f;
			try {
				f = CFReturnStatement.class.getDeclaredField("_ret");
				f.setAccessible(true);
				CFExpression subExpression = (CFExpression) f.get(cfreturn);
				if(subExpression !=null){
					process(subExpression, filename, elem, functionName);
				}
			} catch (Exception e) {
			} //NoSuchFieldException
			
			 
		} else if (expression instanceof CFFuncDeclStatement) {
			final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
			process(function.getBody(), filename, elem, function.getName());
			for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
				try{
					structurePlugin.endFunction(context, bugs);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			inFunction = false;
			handler.pop();
		} else {
		}
	}

	/**
	 * Return the exception message, or its class name
	 * @param e
	 * @return
	 */
	private String exceptionText(Exception e) {
		final String msg = e.getMessage();
		if(msg == null || msg.trim().length()==0){
			return e.getClass().toString();
		}
		return msg;
	}
	private void process(final CFExpression expression, final String filename, final Element elem,
			final String functionName) {
		final Context context = new Context(filename, elem, functionName, inAssignment, handler);
		context.setInComponent(inComponent);


		for (final CFLintScanner plugin : extensions) {
			try{
				plugin.expression(expression, context, bugs);
				for(final ContextMessage message : context.getMessages()){
					reportRule(elem, expression, context, plugin, message);
				}
				context.getMessages().clear();
			}catch(Exception e){
				if(verbose){
					e.printStackTrace();
				}
				reportRule(elem, expression, context, plugin, "PLUGIN_ERROR:" + exceptionText(e));
			}
		}
		if (expression instanceof CFUnaryExpression) {
			process(((CFUnaryExpression) expression).getSub(), filename, elem, functionName);
		}else if (expression instanceof CFNestedExpression) {
				process(((CFNestedExpression) expression).getSub(), filename, elem, functionName);
		} else if (expression instanceof CFAssignmentExpression) {
			inAssignment = true;
			process(((CFAssignmentExpression) expression).getLeft(), filename, elem, functionName);
			inAssignment = false;
			process(((CFAssignmentExpression) expression).getRight(), filename, elem, functionName);
		} else if (expression instanceof CFBinaryExpression) {
			process(((CFBinaryExpression) expression).getLeft(), filename, elem, functionName);
			process(((CFBinaryExpression) expression).getRight(), filename, elem, functionName);
		} else if (expression instanceof CFFunctionExpression) {
			final CFFunctionExpression cfFunctionExpr = (CFFunctionExpression) expression;
			// if ("QueryNew".equalsIgnoreCase(cfFunctionExpr.getName()) &&
			// cfFunctionExpr.getArgs().size() == 1) {
			// bugs.add(new
			// BugInfo.BugInfoBuilder().setMessageCode("QUERYNEW_DATATYPE")
			// .setMessage("QueryNew statement should specify datatypes.").setVariable("QueryNew")
			// .setFunction(functionName).setSeverity("WARNING").setFilename(filename)
			// .setExpression(expression.Decompile(0)).build(expression, elem));
			// }
			for (final CFExpression expr : cfFunctionExpr.getArgs()) {
				process(expr, filename, elem, functionName);
			}
		} else if (expression instanceof CFIdentifier) {
			final String name = ((CFIdentifier) expression).getName();
			handler.checkVariable(name);
			// if (inFunction && inAssignment && !handler.checkVariable(name)) {
			// bugs.add(new
			// BugInfo.BugInfoBuilder().setMessageCode("MISSING_VAR")
			// .setMessage("Variable " + name +
			// " is not declared with a var statement.").setVariable(name)
			// .setFunction(functionName).setSeverity("ERROR").setFilename(filename)
			// .setExpression(expression.Decompile(0)).build(expression, elem));
			// }
			if(expression instanceof CFFullVarExpression){
				CFFullVarExpression fullVarExpression = (CFFullVarExpression)expression;
				for(CFExpression expr: fullVarExpression.getExpressions()){
					if(expr instanceof CFFunctionExpression){
						process(expr,filename,elem,functionName);
					}
				}
			}
		} else if (expression instanceof CFVarDeclExpression) {
			handler.addVariable(((CFVarDeclExpression) expression).getName());
			process(((CFVarDeclExpression) expression).getInit(), filename, elem, functionName);
		} else if (expression instanceof CFStringExpression) {
			CFStringExpression stringExpression = (CFStringExpression) expression;
			for(CFExpression expr: stringExpression.getSubExpressions()){
				process(expr,filename,elem,functionName);
			}
		} else if (expression instanceof CFLiteral) {
//			if(expression.getToken().getType() == CFSCRIPTLexer.STRING_LITERAL){
//				for(String subExprStr : splitHash(expression.Decompile(0))){
//					try{
//						final CFExpression subExpression = cfmlParser.parseCFExpression(subExprStr,this);
//						if (expression == null) {
//							throw new NullPointerException("expression is null, parsing error");
//						}
//						process(subExpression, filename, elem,functionName);
//					} catch (final Exception npe) {
//						final int line = elem.getSource().getRow(elem.getBegin());
//						final int column = elem.getSource().getColumn(elem.getBegin());
//						if (!quiet) {
//							System.err.println("Error in: " + shortSource(elem.getSource(), line) + " @ " + line + ":");
//							if (verbose) {
//								npe.printStackTrace(System.err);
//							}
//						}
//					}
//				}
//					
//			}
			// } else if (expression instanceof CFFullVarExpression) {
			// if (((CFFullVarExpression) expression).getExpressions().size() ==
			// 1) {
			// process(((CFFullVarExpression)
			// expression).getExpressions().get(0), filename, elem,
			// functionName);
			// }
		} 
		else if (expression instanceof CFStringExpression){
			for(CFExpression expr : ((CFStringExpression) expression).getSubExpressions()){
				process(expr,filename,elem,functionName);
			}
		}
		else {
		}
	}
	protected void reportRule(final Element elem, final Object expression, final Context context, final CFLintScanner plugin, String msg) {
		final String[] exceptionmsg = (msg!=null?msg:"").split(":");
		final String msgcode = exceptionmsg[0].trim();
		final String nameVar = exceptionmsg.length>1?exceptionmsg[1].trim():null;
		reportRule(elem, expression,context,plugin,new ContextMessage(msgcode, nameVar));
	}
	
	public static Element getPreviousSibling(Element element){
		
		if(element.getParentElement() != null){
			List<Element> parentElements = element.getParentElement().getChildElements();
			int idx = parentElements.indexOf(element);
			if(idx > 0){
				return parentElements.get(idx-1);
			}
		}else if(element.getSource() != null){
			List<Element> parentElements = element.getSource().getChildElements();
			int idx = parentElements.indexOf(element);
			if(idx > 0){
				return parentElements.get(idx-1);
			}
		}
		return null;
	}
	
	/*
	 * Check for <!--- CFLINT-DISABLE ---> in the tag hierarchy
	 */
	protected boolean checkForDisabled(final Element element,
			final String msgcode) {
		Element elem = element;
		while (elem != null) {
			Element prevSibling = getPreviousSibling(elem);
			if (prevSibling != null && prevSibling.getName().equals("!---")) {
				Pattern p = Pattern
						.compile(".*---\\s*CFLINT-DISABLE\\s+(.*)\\s*---.*");
				Matcher m = p.matcher(prevSibling.toString().toUpperCase()
						.trim());
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
								System.out.println("Skipping disabled "
										+ msgcode);
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

	protected void reportRule(final Element elem, final Object expression, final Context context, final CFLintScanner plugin, ContextMessage msg) {
		final String msgcode = msg.getMessageCode();
		final String nameVar = msg.getVariable();
		
		if(checkForDisabled(elem,msgcode)){
			return;
		}
		if(configuration == null){
			throw new NullPointerException("Configuration is null");
		}
		PluginInfoRule ruleInfo = null;
		if("PLUGIN_ERROR".equals(msgcode)){
			ruleInfo = new PluginInfoRule();
			PluginMessage msgInfo = new PluginMessage("PLUGIN_ERROR");
			msgInfo.setMessageText("Error in plugin: ${variable}");
			msgInfo.setSeverity("ERROR");
			ruleInfo.getMessages().add(msgInfo);
		}else{
			ruleInfo = configuration.getRuleForPlugin(plugin);
		}
		if(ruleInfo == null){
			throw new NullPointerException("Rule not found for " + plugin.getClass().getSimpleName());
		}
		final PluginMessage msgInfo = ruleInfo.getMessageByCode(msgcode);
		if(configuration == null){
			throw new NullPointerException("Message definition not found for [" + msgcode + "] in " + plugin.getClass().getSimpleName());
		}
		BugInfoBuilder bldr = new BugInfo.BugInfoBuilder().setMessageCode(msgcode).setVariable(nameVar)
				.setFunction(context.getFunctionName())
				.setFilename(context.getFilename());
		if(msgInfo != null){
			bldr.setSeverity(msgInfo.getSeverity());
			bldr.setMessage(msgInfo.getMessageText());
		}else{
			System.err.println("Message code: " + msgcode + " is not configured correctly.");
			bldr.setSeverity("WARNING");
			bldr.setMessage(msgcode);
		}
		if(expression instanceof CFStatement){
			bldr.setExpression(((CFStatement) expression).Decompile(0));
		}
		else if(expression instanceof CFScriptStatement){
			bldr.setExpression(((CFScriptStatement) expression).Decompile(0));
		}
		else if(elem != null){
			bldr.setExpression(elem.toString());
		}
		bldr.setRuleParameters(ruleInfo.getParameters());
		if (expression instanceof CFExpression){
			bugs.add(bldr.build((CFExpression)expression, elem));
		}else{
			bugs.add(bldr.build((CFParsedStatement)expression, elem));
		}
	}

	public BugList getBugs() {
		return bugs;
	}

	public List<String> getAllowedExtensions() {
		return allowedExtensions;
	}

	public void setAllowedExtensions(final List<String> allowedExtensions) {
		this.allowedExtensions = allowedExtensions;
	}

	public void reportError(final String arg0) {
		// TODO Auto-generated method stub
		final String file = currentFile == null ? "" : currentFile + "\r\n";
		System.out.println(file + "------4-" + arg0);

	}

	public void reportError(final RecognitionException arg0) {
		// TODO Auto-generated method stub
		final String file = currentFile == null ? "" : currentFile + "\r\n";
		System.out.println(file + "-------" + arg0);

	}

	public void reportError(final String[] arg0, final RecognitionException arg1) {
		// TODO Auto-generated method stub
		final String file = currentFile == null ? "" : currentFile + "\r\n";
		System.out.println(file + "-------" + arg0);
	}

	public void reportError(final IntStream arg0, final RecognitionException arg1, final BitSet arg2) {
		// TODO Auto-generated method stub
		final String file = currentFile == null ? "" : currentFile + "\r\n";
		System.out.println(file + "-------" + arg0);

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
		currentFile = srcidentifier;
		for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
			try{
				structurePlugin.startFile(srcidentifier, bugs);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		for (final ScanProgressListener p : scanProgressListeners) {
			p.startedProcessing(srcidentifier);
		}
	}

	protected void fireFinishedProcessing(final String srcidentifier) {
		for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
			try{
				structurePlugin.endFile(srcidentifier, bugs);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		for (final ScanProgressListener p : scanProgressListeners) {
			p.finishedProcessing(srcidentifier);
		}
		currentFile = null;
	}
	protected void fireClose() {
		for (final ScanProgressListener p : scanProgressListeners) {
			p.close();
		}
	}

	public void addExceptionListener(final CFLintExceptionListener exceptionListener) {
		exceptionListeners.add(exceptionListener);
	}

	protected void fireCFLintException(final Exception e, final String messageCode, final String filename,
			final Integer line, final Integer column,final String functionName, final String expression) {
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
	public void syntaxError(Recognizer<?, ?> recognizer,
			Object offendingSymbol, int line, int charPositionInLine,
			String msg, org.antlr.v4.runtime.RecognitionException e) {
		final String file = currentFile == null ? "" : currentFile + "\r\n";
		String expression=null;
		if(offendingSymbol instanceof Token){
			expression = ((Token) offendingSymbol).getText();
			if(expression.length() > 50){
				expression=expression.substring(1,40) + "...";
			}
		}
		if(!currentElement.isEmpty()){
			Element elem = currentElement.peek();
			if(line == 1){
				line = elem.getSource().getRow(elem.getBegin());
				charPositionInLine = charPositionInLine + elem.getSource().getColumn(elem.getBegin());
			}else{
				line = elem.getSource().getRow(elem.getBegin()) + line - 1;
			}
		}
		if(recognizer instanceof Parser && ((Parser)recognizer).isExpectedToken(CFSCRIPTParser.SEMICOLON)){
			bugs.add(new BugInfo.BugInfoBuilder().setMessageCode("MISSING_SEMI")
			.setFilename(file).setMessage("End of statement(;) expected instead of " + expression).setSeverity("ERROR")
			.setExpression(expression)
			.setLine(line).setColumn(charPositionInLine)
			.build());

		}else{
			
			fireCFLintException(e,PARSE_ERROR,file,line,charPositionInLine,"",msg);
		}
	}
	public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex,
			int stopIndex, boolean exact, java.util.BitSet ambigAlts,
			ATNConfigSet configs) {
//		final String file = currentFile == null ? "" : currentFile + "\r\n";
//		System.out.println(file + "----reportAmbiguity ---");
	}
	public void reportAttemptingFullContext(Parser recognizer, DFA dfa,
			int startIndex, int stopIndex, java.util.BitSet conflictingAlts,
			ATNConfigSet configs) {
//		final String file = currentFile == null ? "" : currentFile + "\r\n";
//		System.out.println(file + "----reportAttemptingFullContext ---");
	}
	public void reportContextSensitivity(Parser recognizer, DFA dfa,
			int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
//		final String file = currentFile == null ? "" : currentFile + "\r\n";
//		System.out.println(file + "----reportContextSensitivity ---");
	}
	public void reportError(org.antlr.v4.runtime.RecognitionException re) {
		final String file = currentFile == null ? "" : currentFile + "\r\n";
		System.out.println(file + "----RecognitionException ---" + re.getCtx().getSourceInterval().a + " : " + 
				re.getCtx().getSourceInterval().b);
	}
	public void reportError(String[] tokenNames,
			org.antlr.v4.runtime.RecognitionException re) {
		final String file = currentFile == null ? "" : currentFile + "\r\n";
		System.out.println(file + "----reportError ---" + re.getCtx().getSourceInterval().a + " : " + 
				re.getCtx().getSourceInterval().b);
	}
	public void reportError(org.antlr.v4.runtime.IntStream input,
			org.antlr.v4.runtime.RecognitionException re, BitSet follow) {
		final String file = currentFile == null ? "" : currentFile + "\r\n";
		System.out.println(file + "----reportError ---" );
	}
	

	List<String> splitHash(String input){
		input = input + "   ";
		List<String> retval = new ArrayList<String>();
		
		int start = 0;
		int end= -1;
		
		while(start<input.length() && end < input.length()){
			if(end>start){
				retval.add(input.substring(start, end));
				
			}
			start = end + 1;
			while(start<input.length() && input.charAt(start)!='#'){
				start++;
			}
			start++;
			end=start;
			while(end<input.length() && input.charAt(end)!='#'){
				end++;
				if(input.charAt(end)=='#'&&input.charAt(end+1)=='#'){
					end +=2;
				}
			}
		}
		return retval;
	}
}
