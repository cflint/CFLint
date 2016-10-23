package com.cflint;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import com.cflint.listeners.ScanProgressListener;
import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.CFLintStructureListener;
import com.cflint.plugins.Context;
import com.cflint.plugins.Context.ContextMessage;
import com.cflint.plugins.exceptions.CFLintExceptionListener;
import com.cflint.plugins.exceptions.DefaultCFLintExceptionListener;
import com.cflint.tools.AllowedExtensionsLoader;
import com.cflint.tools.CFLintFilter;
import com.cflint.tools.CFNestedExpressionProvider;
import com.cflint.tools.FileUtil;
import com.cflint.tools.ScanningProgressMonitorLookAhead;

import cfml.CFSCRIPTLexer;
import cfml.CFSCRIPTParser;
import cfml.parsing.CFMLParser;
import cfml.parsing.CFMLSource;
import cfml.parsing.ParserTag;
import cfml.parsing.cfscript.CFAssignmentExpression;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFStatement;
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
	BugList bugs;
	List<CFLintScanner> extensions = new ArrayList<CFLintScanner>();
	List<String> allowedExtensions = new ArrayList<String>();
	boolean verbose = false;
	boolean logError = false;
	boolean quiet = false;
	boolean showProgress = false;
	boolean progressUsesThread = true;

	// constants
	final String resourceBundleName = "com.cflint.cflint";

	private String currentFile;
	List<ScanProgressListener> scanProgressListeners = new ArrayList<ScanProgressListener>();
	List<CFLintExceptionListener> exceptionListeners = new ArrayList<CFLintExceptionListener>();

	ConfigRuntime configuration;
	private final Stack<Element> currentElement = new Stack<Element>();

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
		allowedExtensions = AllowedExtensionsLoader.init(resourceBundleName);
		cfmlParser.setErrorReporter(this);
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
		allowedExtensions = AllowedExtensionsLoader.init(resourceBundleName);
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
			for (final File file : folderOrFile.listFiles()) {
				scan(file);
			}
		} else if (!folderOrFile.isHidden() && FileUtil.checkExtension(folderOrFile, allowedExtensions)) {
			final String src = FileUtil.loadFile(folderOrFile);
			try {
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
			Context context = new Context(filename, null, null, false, handler, scriptStatement.getTokens());
			process(scriptStatement, context);
		} else {
			processStack(elements, " ", filename, null);
		}
		fireFinishedProcessing(filename);
	}

	public void processStack(final List<Element> elements, final String space, final String filename,
			final CFIdentifier functionName) throws ParseException, IOException {
		for (final Element elem : elements) {
			final Context context = new Context(filename, elem, functionName, false, handler);
			context.setSiblingElements(elements);
			process(elem, space, context);
		}
	}

	public void processStack(final List<Element> elements, final String space, final Context context)
			throws ParseException, IOException {
		for (final Element elem : elements) {
			process(elem, space, context.subContext(elem, elements));
		}
	}

	private void process(final Element elem, final String space, final Context context)
			throws ParseException, IOException {
		currentElement.push(elem);

		if (elem.getName().equalsIgnoreCase("cfcomponent")) {
			final Context componentContext = context.subContext(elem);
			componentContext.setInComponent(true);
			componentContext.setComponentName(elem.getAttributeValue("displayname"));
			registerRuleOverrides(componentContext);
			handler.push("component");
			doStructureStart(elem, componentContext, CFCompDeclStatement.class);
		} else if (elem.getName().equalsIgnoreCase("cffunction")) {
			final Context functionContext = context.subContext(elem);
			functionContext.setFunctionName(elem.getAttributeValue("name"));
			inFunction = true;
			registerRuleOverrides(functionContext);
			handler.push("function");
			doStructureStart(elem, functionContext, CFFuncDeclStatement.class);
		}

		try {
			if (elem.getName().equalsIgnoreCase("cfset") || elem.getName().equalsIgnoreCase("cfif")
					|| elem.getName().equalsIgnoreCase("cfelseif") || elem.getName().equalsIgnoreCase("cfreturn")) {
				scanElement(elem, context);
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
						printException(npe, elem);
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
				final String cfscript = elem.getContent().toString();
				final CFScriptStatement scriptStatement = cfmlParser.parseScript(cfscript);

				Context subcontext = context.subContext(elem);
				process(scriptStatement, subcontext);
				processStack(elem.getChildElements(), space + " ", context);
			} else if (elem.getName().equalsIgnoreCase("cffunction")) {
				final Context functionContext = context.subContext(elem);
				functionContext.setFunctionName(elem.getAttributeValue("name"));
				registerRuleOverrides(functionContext);
				scanElement(elem, functionContext);
				processStack(elem.getChildElements(), space + " ", functionContext);
				for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
					try {
						structurePlugin.endFunction(functionContext, bugs);
						for (final ContextMessage message : functionContext.getMessages()) {
							reportRule(elem, null, functionContext, (CFLintScanner) structurePlugin, message);
						}
						functionContext.getMessages().clear();
					} catch (final Exception e) {
						printException(e);
					}
				}
				inFunction = false;
				handler.pop();
			} else if (elem.getName().equalsIgnoreCase("cfcomponent")) {
				final Context componentContext = context.subContext(elem);
				componentContext.setInComponent(true);
				componentContext.setComponentName(elem.getAttributeValue("displayname"));
				registerRuleOverrides(componentContext);
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
					}
				}
				handler.pop();
			} else if (elem.getName().equalsIgnoreCase("cfquery")) {
				scanElement(elem, context);
				final List<Element> list = elem.getAllElements();
				processStack(list.subList(1, list.size()), space + " ", context);
			} else if (elem.getName().equalsIgnoreCase("cfqueryparam")) {
				scanElement(elem, context);
				if (elem.getAttributeValue("value") != null) {
				}
			} else {
				scanElement(elem, context);
				processStack(elem.getChildElements(), space + " ", context);
			}
		} finally {
			currentElement.pop();
		}
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
				reportRule(elem, null, context, plugin, PLUGIN_ERROR + exceptionText(e));
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
			return sLine == null?null: sLine.replaceAll("\t", " ");
		} catch (final Exception e) {
		}
		return retval.substring(0, 300);
	}

	private void process(final CFScriptStatement expression, Context context) {
		final Element elem = context.getElement();
		try {
			if (expression instanceof CFCompoundStatement) {
				scanExpression(expression, context, elem);
				for (final CFScriptStatement statement : ((CFCompoundStatement) expression).getStatements()) {
					process(statement, context);
				}
			} else if (expression instanceof CFExpressionStatement) {
				scanExpression(expression, context, elem);
				process(((CFExpressionStatement) expression).getExpression(), elem, context);
			} else if (expression instanceof CFCompDeclStatement) {
				CFCompDeclStatement compDeclStatement = (CFCompDeclStatement) expression;
				final Context componentContext = context.subContext(null);
				componentContext.setInComponent(true);
				// componentContext.setComponentName(compDeclStatement.get); //
				// TODO

				// Register any overrides from multi-line comments.
				registerRuleOverrides(componentContext, expression.getToken());
				// do startComponent notifications
				doStructureStart(elem, componentContext, expression.getClass());

				scanExpression(compDeclStatement, componentContext, elem);
				// process the component declaration
				process(compDeclStatement.getBody(), componentContext);
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
			} else if (expression instanceof CFReturnStatement) {
				scanExpression(expression, context, elem);
				final CFReturnStatement cfreturn = (CFReturnStatement) expression;
				final CFExpression subExpression = cfreturn.getExpression();
				process(subExpression, elem, context);
			} else if (expression instanceof CFFuncDeclStatement) {
				final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
				final Context functionContext = context.subContext(null);
				functionContext.setFunctionIdentifier(function.getName());
				registerRuleOverrides(functionContext, function.getToken());
				inFunction = true;
				handler.push("function");
				for (final CFFunctionParameter param : function.getFormals()) {
					handler.addArgument(param.getName());
				}
				doStructureStart(elem, functionContext, CFFuncDeclStatement.class);
				scanExpression(expression, functionContext, elem);

				process(function.getBody(), functionContext);

				for (final CFLintStructureListener structurePlugin : getStructureListeners(extensions)) {
					try {
						structurePlugin.endFunction(context, bugs);
						for (final ContextMessage message : context.getMessages()) {
							reportRule(elem, null, context, (CFLintScanner) structurePlugin, message);
						}
						context.getMessages().clear();
					} catch (final Exception e) {
						printException(e);
					}
				}
				inFunction = false;
				handler.pop();
			} else {
				scanExpression(expression, context, elem);
			}
		} catch (final StackOverflowError soe) {
			System.err.println("Stack overflow in " + context.getFilename());
			final int line = context.startLine();
			fireCFLintException(soe, PARSE_ERROR, context.getFilename(), line, 1, "",
					"Stack overflow on " + expression.getClass());
		}
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
			}
		}
	}

	protected void scanExpression(final CFScriptStatement expression, Context context, final Element elem) {
		for (final CFLintScanner plugin : extensions) {
			try {
				plugin.expression(expression, context, bugs);
				for (final ContextMessage message : context.getMessages()) {
					reportRule(elem, expression, context, plugin, message);
				}
				context.getMessages().clear();
			} catch (final Exception e) {
				printException(e);
				reportRule(elem, expression, context, plugin, PLUGIN_ERROR + exceptionText(e));
			}
		}
	}

	/**
	 * Register any overrides from multi-line comments.
	 */
	protected void registerRuleOverrides(Context context, final Token functionToken) {
		Iterable<Token> tokens = context.beforeTokens(functionToken);
		for (Token currentTok : tokens) {
			if (currentTok.getChannel() == Token.HIDDEN_CHANNEL && currentTok.getType() == CFSCRIPTLexer.ML_COMMENT) {
				String mlText = currentTok.getText();
				Pattern pattern = Pattern.compile(".*\\s*@CFLintIgnore\\s+([\\w,_]+)\\s*.*", Pattern.DOTALL);
				Matcher matcher = pattern.matcher(mlText);
				if (matcher.matches()) {
					String ignoreCodes = matcher.group(1);
					context.ignore(Arrays.asList(ignoreCodes.split(",\\s*")));
				}
			} else if (currentTok.getLine() < functionToken.getLine()) {
				break;
			}
		}
	}

	/**
	 * Register any overrides from comment elements before functions/components.
	 */
	protected void registerRuleOverrides(Context context) {

		final Element priorElement = context == null ? null : context.getPreviousSiblingElement();
		if (priorElement != null && "!---".equals(priorElement.getName())) {
			String mlText = priorElement.toString();
			Pattern pattern = Pattern.compile(".*\\s*@CFLintIgnore\\s+([\\w,_]+)\\s*.*", Pattern.DOTALL);
			Matcher matcher = pattern.matcher(mlText);
			if (matcher.matches()) {
				String ignoreCodes = matcher.group(1);
				context.ignore(Arrays.asList(ignoreCodes.split(",\\s*")));
			}
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
					reportRule(elem, expression, context, plugin, PLUGIN_ERROR + exceptionText(e));
				}
			}
			// Handle a few expression types in a special fashion.
			if (expression instanceof CFAssignmentExpression) {
				final Context assignmentContext = context.subContext(elem);
				assignmentContext.setInAssignmentExpression(true);
				process(((CFAssignmentExpression) expression).getLeft(), elem, assignmentContext);
				// Right hand side is handled below. Left hand side gets a
				// special context.
			} else if (expression instanceof CFIdentifier) {
				final String name = ((CFIdentifier) expression).getName();
				handler.checkVariable(name);
			} else if (expression instanceof CFVarDeclExpression) {
				handler.addVariable(((CFVarDeclExpression) expression).getName());
			}

			// Loop into all relevant nested (child) expressions.
			List<CFExpression> childExpressions = CFNestedExpressionProvider.createInstance(expression)
					.getChildExpressions();
			for (CFExpression child : childExpressions) {
				process(child, elem, context);
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

			if (configuration.getIncludes().isEmpty()
					|| configuration.getIncludes().contains(ruleInfo.getMessageByCode(msgcode))) {
				if (configuration.getExcludes().isEmpty()
						|| !configuration.getExcludes().contains(ruleInfo.getMessageByCode(msgcode))) {
					if (!suppressed(bugInfo, token, context)) {
						bugs.add(bugInfo);
					}
				}
			}
		} else {
			final BugInfo bug = bldr.build((CFParsedStatement) expression, elem);
			if (msg.getLine() != null) {
				bug.setLine(msg.getLine());
				bug.setColumn(0);
			}
			if (configuration.getIncludes().isEmpty()
					|| configuration.getIncludes().contains(ruleInfo.getMessageByCode(msgcode))) {
				if (configuration.getExcludes().isEmpty()
						|| !configuration.getExcludes().contains(ruleInfo.getMessageByCode(msgcode))) {
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
				printException(e);
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

}
