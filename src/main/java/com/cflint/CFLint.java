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

import org.antlr.runtime.BitSet;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

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

import cfml.CFSCRIPTLexer;
import cfml.CFSCRIPTParser;
import cfml.parsing.CFMLParser;
import cfml.parsing.CFMLSource;
import cfml.parsing.ParserTag;
import cfml.parsing.cfml.antlr.CFLexer;
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
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class CFLint implements IErrorReporter {

	private static final String FILE_ERROR = "FILE_ERROR";
	private static final String PARSE_ERROR = "PARSE_ERROR";
	public static final String PLUGIN_ERROR = "PLUGIN_ERROR:";
	CFMLParser cfmlParser = new CFMLParser();

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
	private final Stack<Element> currentElement = new Stack<Element>();

	public CFLint() throws IOException {
		this((CFLintConfig) null);
	}

	public CFLint(final CFLintConfig configFile) throws IOException {
		final CFLintPluginInfo pluginInfo = ConfigUtils.loadDefaultPluginInfo();
		configuration = new ConfigRuntime(configFile, pluginInfo);
		for (final PluginInfoRule ruleInfo : configuration.getRules()) {
			addScanner(ConfigUtils.loadPlugin(ruleInfo));
		}
		final CFLintFilter filter = CFLintFilter.createFilter(verbose);
		bugs = new BugList(filter);
		if (exceptionListeners.isEmpty()) {
			addExceptionListener(new DefaultCFLintExceptionListener(bugs));
		}
		try {
			allowedExtensions.addAll(Arrays
					.asList(ResourceBundle.getBundle(resourceBundleName).getString(allowedExtensionsName).split(",")));
		} catch (final Exception e) {
			allowedExtensions.add(cfcExtension);
			allowedExtensions.add(cfmExtenstion);
		}
		cfmlParser.setErrorReporter(this);
	}

	public CFLint(final CFLintScanner... bugsScanners) {
		this(null, bugsScanners);
	}

	@Deprecated
	public CFLint(final ConfigRuntime configuration, final CFLintScanner... bugsScanners) {
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
		try {
			allowedExtensions.addAll(Arrays
					.asList(ResourceBundle.getBundle(resourceBundleName).getString(allowedExtensionsName).split(",")));
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

					@Override
					public void run() {
						prescan(f, 0, progressMonitorListener);
					}
				}).start();
			} else {
				prescan(f, 0, progressMonitorListener);
			}
		}
		scan(f);
		fireClose();
	}

	private int prescan(final File folderOrFile, int counter, final ProgressMonitorListener progressMonitorListener) {
		if (folderOrFile.isDirectory()) {
			for (final File file : folderOrFile.listFiles()) {
				counter = prescan(file, counter, progressMonitorListener);
			}
			if (counter > 10) {

				progressMonitorListener.setTotalToProcess(counter);
			}
			return counter;
		} else if (!folderOrFile.isHidden() && checkExtension(folderOrFile)) {
			return counter + 1;
		} else {
			return counter;
		}
	}

	public void scan(final File folderOrFile) {
		if (getBugs().getFileFilter() != null 
				&& !getBugs().getFileFilter().includeFile(folderOrFile)){
			return;
		}
		if (folderOrFile.isDirectory()) {
			for (final File file : folderOrFile.listFiles()) {
				scan(file);
			}
		} else if (!folderOrFile.isHidden() && checkExtension(folderOrFile)) {
			final String src = load(folderOrFile);
			try {
				process(src, folderOrFile.getAbsolutePath());
			} catch (final Exception e) {
				if (!quiet) {
					if (verbose) {
						e.printStackTrace(System.err);
					} else {
						System.err.println(e.getMessage());
					}
				}
				if (logError) {
					System.out.println("Logging Error: " + FILE_ERROR);
					fireCFLintException(e, FILE_ERROR, folderOrFile.getAbsolutePath(), null, null, null, null);
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
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			final byte[] b = new byte[fis.available()];
			fis.read(b);
			return new String(b);
		} catch (final Exception e) {
			return null;
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (final IOException e) {
				return null;
			}
		}
	}

	int parserCounter = 1;

	public void process(final String src, final String filename) throws ParseException, IOException {
		fireStartedProcessing(filename);
		final CFMLSource cfmlSource = new CFMLSource(src);
		final ParserTag firstTag = cfmlSource.getNextTag(0);
		final List<Element> elements = new ArrayList<Element>();
		if (firstTag != null) {
			elements.addAll(cfmlSource.getChildElements());
		}
		if (src.contains("component")
				&& (elements.isEmpty() || elements.get(0).getBegin() > src.indexOf("component"))) {
			// Check if pure cfscript
			final CFScriptStatement scriptStatement = cfmlParser.parseScript(src);
			Context context = new Context(filename,null,null,inAssignment,handler,scriptStatement.getTokens());
			process(scriptStatement, context);
		} else {
			processStack(elements, " ", filename, null);
		}
		fireFinishedProcessing(filename);
	}

	public void processStack(final List<Element> elements, final String space, final String filename,
			final CFIdentifier functionName) throws ParseException, IOException {
		for (final Element elem : elements) {
			final Context context = new Context(filename, elem, functionName, inAssignment, handler);
			process(elem, space, context);
		}
	}

	public void processStack(final List<Element> elements, final String space, final Context context)
			throws ParseException, IOException {
		for (final Element elem : elements) {
			process(elem, space, context.subContext(elem));
		}
	}

	private void process(final Element elem, final String space, final Context context)
			throws ParseException, IOException {
		context.setInComponent(inComponent);
		currentElement.push(elem);

		if (elem.getName().equalsIgnoreCase("cfcomponent")) {
			inComponent = true;
			handler.push("component");

			for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
				try {
					structurePlugin.startComponent(context, bugs);
					for (final ContextMessage message : context.getMessages()) {
						reportRule(elem, null, context, (CFLintScanner) structurePlugin, message);
					}
					context.getMessages().clear();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}

			context.setInComponent(true);
			context.setComponentName(elem.getAttributeValue("displayname"));
		} else if (elem.getName().equalsIgnoreCase("cffunction")) {
			context.setFunctionName(elem.getAttributeValue("name"));
			inFunction = true;
			handler.push("function");

			for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
				try {
					structurePlugin.startFunction(context, bugs);
					for (final ContextMessage message : context.getMessages()) {
						reportRule(elem, null, context, (CFLintScanner) structurePlugin, message);
					}
					context.getMessages().clear();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}

		try {
			for (final CFLintScanner plugin : extensions) {
				try {
					plugin.element(elem, context, bugs);
					for (final ContextMessage message : context.getMessages()) {
						reportRule(elem, null, context, plugin, message);
					}
					context.getMessages().clear();
				} catch (final Exception e) {
					if (verbose) {
						e.printStackTrace();
					}
					reportRule(elem, null, context, plugin, PLUGIN_ERROR + exceptionText(e));
				}
			}
			if (elem.getName().equalsIgnoreCase("cfset") || elem.getName().equalsIgnoreCase("cfif")
					|| elem.getName().equalsIgnoreCase("cfelseif") || elem.getName().equalsIgnoreCase("cfreturn")) {
				final Pattern p = Pattern.compile("<\\w+\\s(.*[^/])/?>", Pattern.MULTILINE | Pattern.DOTALL);
				final String expr = elem.getFirstStartTag().toString();
				final Matcher m = p.matcher(expr);
				if (m.matches()) {
					final String cfscript = m.group(1);
					try {
						final CFExpression expression = cfmlParser.parseCFExpression(cfscript, this);

						if (expression == null) {
							throw new NullPointerException("expression is null, parsing error");
						}
						process(expression, elem, context);
					} catch (final Exception npe) {
						final int line = elem.getSource().getRow(elem.getBegin());
						if (!quiet) {
							System.err.println("Error in: " + shortSource(elem.getSource(), line) + " @ " + line + ":");
							if (verbose) {
								npe.printStackTrace(System.err);
							}
						}
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

				Context subcontext = context.subContext(elem);
				process(scriptStatement, subcontext);
			} else {
			}

			if (elem.getName().equalsIgnoreCase("cffunction")) {
				processStack(elem.getChildElements(), space + " ", context);
				for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
					try {
						structurePlugin.endFunction(context, bugs);
						for (final ContextMessage message : context.getMessages()) {
							reportRule(elem, null, context, (CFLintScanner) structurePlugin, message);
						}
						context.getMessages().clear();

					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
				inFunction = false;
				handler.pop();
			} else if (elem.getName().equalsIgnoreCase("cfcomponent")) {
				processStack(elem.getChildElements(), space + " ", context);
				for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
					try {
						structurePlugin.endComponent(context, bugs);
						for (final ContextMessage message : context.getMessages()) {
							reportRule(elem, null, context, (CFLintScanner) structurePlugin, message);
						}
						context.getMessages().clear();
					} catch (final Exception e) {
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
				}
			} else {
				processStack(elem.getChildElements(), space + " ", context);
			}
		} finally {
			currentElement.pop();
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
			return sr.readLine().replaceAll("\t", " ");
		} catch (final Exception e) {
		}
		return retval.substring(0, 300);
	}

	public String stripLineComments(final String cfscript) {
		return cfscript.replaceAll("//[^\r\n]*\r?\n", "\r\n");
	}


	private void process(final CFScriptStatement expression, Context context) {
		
		Element elem = context.getElement();
		String functionName = context.getFunctionName();
		try {

			context.setInComponent(inComponent);
			if (expression instanceof CFCompDeclStatement) {
				inComponent = true;
				// do startComponent notifications
				for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
					try {
						structurePlugin.startComponent(context, bugs);
						for (final ContextMessage message : context.getMessages()) {
							reportRule(elem, null, context, (CFLintScanner) structurePlugin, message);
						}
						context.getMessages().clear();
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			} else if (expression instanceof CFFuncDeclStatement) {
				final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
				functionName = function.getName().getName();
				context.setFunctionName(functionName);
				inFunction = true;
				handler.push("function");
				for (final CFFunctionParameter param : function.getFormals()) {
					handler.addArgument(param.getName());
				}
				for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
					try {
						structurePlugin.startFunction(context, bugs);
						for (final ContextMessage message : context.getMessages()) {
							reportRule(elem, null, context, (CFLintScanner) structurePlugin, message);
						}
						context.getMessages().clear();
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}

			for (final CFLintScanner plugin : extensions) {
				try {
					plugin.expression(expression, context, bugs);
					for (final ContextMessage message : context.getMessages()) {
						reportRule(elem, expression, context, plugin, message);
					}
					context.getMessages().clear();
				} catch (final Exception e) {
					if (verbose) {
						e.printStackTrace();
					}
					reportRule(elem, expression, context, plugin, PLUGIN_ERROR + exceptionText(e));
				}
			}

			if (expression instanceof CFCompoundStatement) {
				for (final CFScriptStatement statement : ((CFCompoundStatement) expression).getStatements()) {
					process(statement, context);
				}
			} else if (expression instanceof CFExpressionStatement) {
				process(((CFExpressionStatement) expression).getExpression(), elem, context);
			} else if (expression instanceof CFCompDeclStatement) {
				// process the component declaration
				process(((CFCompDeclStatement) expression).getBody(), context);
				// do endComponent notifications
				for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
					try {
						structurePlugin.endComponent(context, bugs);
						for (final ContextMessage message : context.getMessages()) {
							reportRule(elem, null, context, (CFLintScanner) structurePlugin, message);
						}
						context.getMessages().clear();
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
				inComponent = false;
			} else if (expression instanceof CFForStatement) {
				process(((CFForStatement) expression).getInit(), elem, context);
				process(((CFForStatement) expression).getCond(), elem, context);
				process(((CFForStatement) expression).getNext(), elem, context);
				process(((CFForStatement) expression).getBody(), context);
			} else if (expression instanceof CFForInStatement) {
				process(((CFForInStatement) expression).getVariable(), elem, context);
				process(((CFForInStatement) expression).getStructure(), elem, context);
				process(((CFForInStatement) expression).getBody(), context);
			} else if (expression instanceof CFIfStatement) {
				final CFIfStatement cfif = (CFIfStatement) expression;
				process(cfif.getCond(), elem, context);
				process(cfif.getThenStatement(), context);
				if (cfif.getElseStatement() != null) {
					process(cfif.getElseStatement(), context);
				}
			} else if (expression instanceof CFReturnStatement) {
				final CFReturnStatement cfreturn = (CFReturnStatement) expression;
				Field f;
				try {
					f = CFReturnStatement.class.getDeclaredField("_ret");
					f.setAccessible(true);
					final CFExpression subExpression = (CFExpression) f.get(cfreturn);
					if (subExpression != null) {
						process(subExpression, elem, context);
					}
				} catch (final Exception e) {
				} 
			} else if (expression instanceof CFFuncDeclStatement) {
				final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
				Context subcontext = context.subContext(context.getElement());
				subcontext.setFunctionIdentifier(function.getName());
				process(function.getBody(), subcontext);

				for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
					try {
						structurePlugin.endFunction(context, bugs);
						for (final ContextMessage message : context.getMessages()) {
							reportRule(elem, null, context, (CFLintScanner) structurePlugin, message);
						}
						context.getMessages().clear();
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
				inFunction = false;
				handler.pop();
			} else {
			}
		} catch (final StackOverflowError soe) {
			System.err.println("Stack overflow in " + context.getFilename());
			final int line = context.startLine();
			fireCFLintException(soe, PARSE_ERROR, context.getFilename(), line, 1, "", "Stack overflow on " + expression.getClass());
		}
	}

	/**
	 * Return the exception message, or its class name
	 * 
	 * @param e
	 * @return
	 */
	private String exceptionText(final Exception e) {
		final String msg = e.getMessage();
		if (msg == null || msg.trim().length() == 0) {
			return e.getClass().toString();
		}
		return msg;
	}

	private void process(final CFExpression expression, final Element elem,
			Context oldcontext) {
		
		final String filename = oldcontext.getFilename();
		final String functionName = oldcontext.getFunctionName();
		final Context context = new Context(filename, elem, functionName, inAssignment, handler, oldcontext.getTokens());
		context.setInComponent(inComponent);

		for (final CFLintScanner plugin : extensions) {
			try {
				plugin.expression(expression, context, bugs);
				for (final ContextMessage message : context.getMessages()) {
					reportRule(elem, expression, context, plugin, message);
				}
				context.getMessages().clear();
			} catch (final Exception e) {
				if (verbose) {
					e.printStackTrace();
				}
				reportRule(elem, expression, context, plugin, PLUGIN_ERROR + exceptionText(e));
			}
		}
		if (expression instanceof CFUnaryExpression) {
			process(((CFUnaryExpression) expression).getSub(), elem, context);
		} else if (expression instanceof CFNestedExpression) {
			process(((CFNestedExpression) expression).getSub(), elem, context);
		} else if (expression instanceof CFAssignmentExpression) {
			inAssignment = true;
			process(((CFAssignmentExpression) expression).getLeft(), elem, context);
			inAssignment = false;
			process(((CFAssignmentExpression) expression).getRight(), elem, context);
		} else if (expression instanceof CFBinaryExpression) {
			process(((CFBinaryExpression) expression).getLeft(), elem, context);
			process(((CFBinaryExpression) expression).getRight(), elem, context);
		} else if (expression instanceof CFFunctionExpression) {
			final CFFunctionExpression cfFunctionExpr = (CFFunctionExpression) expression;
			for (final CFExpression expr : cfFunctionExpr.getArgs()) {
				if (expr instanceof CFAssignmentExpression) {
					process(((CFAssignmentExpression) expr).getRight(), elem, context);
				} else {
					process(expr, elem, context);
				}
			}
		} else if (expression instanceof CFIdentifier) {
			final String name = ((CFIdentifier) expression).getName();
			handler.checkVariable(name);
			if (expression instanceof CFFullVarExpression) {
				final CFFullVarExpression fullVarExpression = (CFFullVarExpression) expression;
				for (final CFExpression expr : fullVarExpression.getExpressions()) {
					if (expr instanceof CFFunctionExpression) {
						process(expr, elem, context);
					}
				}
			}
		} else if (expression instanceof CFVarDeclExpression) {
			handler.addVariable(((CFVarDeclExpression) expression).getName());
			process(((CFVarDeclExpression) expression).getInit(), elem, context);
		} else if (expression instanceof CFStringExpression) {
			final CFStringExpression stringExpression = (CFStringExpression) expression;
			for (final CFExpression expr : stringExpression.getSubExpressions()) {
				process(expr, elem, context);
			}
		} else if (expression instanceof CFLiteral) {
		} else if (expression instanceof CFStringExpression) {
			for (final CFExpression expr : ((CFStringExpression) expression).getSubExpressions()) {
				process(expr, elem, context);
			}
		} else {
		}
	}

	protected void reportRule(final Element elem, final Object expression, final Context context,
			final CFLintScanner plugin, final String msg) {
		final String[] exceptionmsg = (msg != null ? msg : "").split(":");
		final String msgcode = exceptionmsg[0].trim();
		final String nameVar = exceptionmsg.length > 1 ? exceptionmsg[1].trim() : null;
		reportRule(elem, expression, context, plugin, new ContextMessage(msgcode, nameVar));
	}

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

	/*
	 * Check for <!--- CFLINT-DISABLE ---> in the tag hierarchy
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

	protected void reportRule(final Element elem, final Object expression, final Context context,
			final CFLintScanner plugin, final ContextMessage msg) {
		final String msgcode = msg.getMessageCode();
		final String nameVar = msg.getVariable();

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
		} else {
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
				.setFunction(context.getFunctionName()).setFilename(context.getFilename());
		if (msgInfo != null) {
			bldr.setSeverity(msgInfo.getSeverity());
			bldr.setMessage(msgInfo.getMessageText());
		} else {
			System.err.println("Message code: " + msgcode + " is not configured correctly.");
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
		if (expression instanceof CFExpression) {
			BugInfo bugInfo = bldr.build((CFExpression) expression, elem);
			final Token token = ((CFExpression) expression).getToken();
			if(!suppressed(bugInfo,token,context)){
				bugs.add(bugInfo);
			}
		} else {
			final BugInfo bug = bldr.build((CFParsedStatement) expression, elem);
			if (msg.getLine() != null) {
				bug.setLine(msg.getLine());
				bug.setColumn(0);
			}
			bugs.add(bug);
		}
	}
	
	/*
	 * Look for a suppress comment on the same line.
	 * cflint:line - suppresses any messages on the same line
	 * cflint:MESSAGE_CODE - suppresses any message matching that code
	 */
	protected boolean suppressed(BugInfo bugInfo, Token token, Context context) {
		Iterable<Token> tokens = context.afterTokens(token);
		for (Token currentTok : tokens) {
			if (currentTok.getLine() != token.getLine()) {
				break;
			}
			if (currentTok.getChannel() == Token.HIDDEN_CHANNEL
					&& currentTok.getType() == CFSCRIPTLexer.LINE_COMMENT) {
				final String commentText = currentTok.getText().replaceFirst("^//\\s*", "").trim();
				if(commentText.startsWith("cflint ")){
					Pattern pattern = Pattern.compile("cflint\\s+ignore:([\\w,]+).*");
					Matcher matcher = pattern.matcher(commentText);
					if(matcher.matches() && matcher.groupCount() > 0){
						final String ignoreCodes = matcher.group(1);
						if(ignoreCodes.equalsIgnoreCase("line")){
							return true;
						}
						for(final String ignoreCode : ignoreCodes.split(",\\s*")){
							if(ignoreCode.equals(bugInfo.getMessageCode())){
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
		this.allowedExtensions = allowedExtensions;
	}

	@Override
	public void reportError(final String arg0) {
	}

	public void reportError(final RecognitionException arg0) {
	}

	public void reportError(final String[] arg0, final RecognitionException arg1) {
	}

	public void reportError(final IntStream arg0, final RecognitionException arg1, final BitSet arg2) {
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
		for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
			try {
				structurePlugin.startFile(srcidentifier, bugs);
			} catch (final Exception e) {
				e.printStackTrace();
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

	public void addScanner(final CFLintScanner plugin) {
		extensions.add(plugin);
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
			int charPositionInLine, final String msg, final org.antlr.v4.runtime.RecognitionException e) {
		final String file = currentFile == null ? "" : currentFile + "\r\n";
		String expression = null;
		if (offendingSymbol instanceof Token) {
			expression = ((Token) offendingSymbol).getText();
			if (expression.length() > 50) {
				expression = expression.substring(1, 40) + "...";
			}
		}
		if (!currentElement.isEmpty()) {
			final Element elem = currentElement.peek();
			if (line == 1) {
				line = elem.getSource().getRow(elem.getBegin());
				charPositionInLine = charPositionInLine + elem.getSource().getColumn(elem.getBegin());
			} else {
				line = elem.getSource().getRow(elem.getBegin()) + line - 1;
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
	}

	@Override
	public void reportAttemptingFullContext(final Parser recognizer, final DFA dfa, final int startIndex,
			final int stopIndex, final java.util.BitSet conflictingAlts, final ATNConfigSet configs) {
	}

	@Override
	public void reportContextSensitivity(final Parser recognizer, final DFA dfa, final int startIndex,
			final int stopIndex, final int prediction, final ATNConfigSet configs) {
	}

	@Override
	public void reportError(final org.antlr.v4.runtime.RecognitionException re) {
		final String file = currentFile == null ? "" : currentFile + "\r\n";
	}

	@Override
	public void reportError(final String[] tokenNames, final org.antlr.v4.runtime.RecognitionException re) {
		final String file = currentFile == null ? "" : currentFile + "\r\n";
	}

	@Override
	public void reportError(final org.antlr.v4.runtime.IntStream input,
			final org.antlr.v4.runtime.RecognitionException re, final BitSet follow) {
	}

	List<String> splitHash(String input) {
		input = input + "   ";
		final List<String> retval = new ArrayList<String>();

		int start = 0;
		int end = -1;

		while (start < input.length() && end < input.length()) {
			if (end > start) {
				retval.add(input.substring(start, end));

			}
			start = end + 1;
			while (start < input.length() && input.charAt(start) != '#') {
				start++;
			}
			start++;
			end = start;
			while (end < input.length() && input.charAt(end) != '#') {
				end++;
				if (input.charAt(end) == '#' && input.charAt(end + 1) == '#') {
					end += 2;
				}
			}
		}
		return retval;
	}
}
