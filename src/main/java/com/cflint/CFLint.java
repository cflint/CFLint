package com.cflint;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.Context;
import com.cflint.plugins.core.ArgDefChecker;
import com.cflint.plugins.core.ArgVarChecker;
import com.cflint.plugins.core.GlobalVarChecker;
import com.cflint.plugins.core.NestedCFOutput;
import com.cflint.plugins.core.OutputParmMissing;
import com.cflint.plugins.core.TypedQueryNew;
import com.cflint.plugins.core.VarScoper;
import com.cflint.tools.CFLintFilter;

import net.htmlparser.jericho.Element;
import cfml.parsing.CFMLParser;
import cfml.parsing.CFMLSource;
import cfml.parsing.cfscript.CFAssignmentExpression;
import cfml.parsing.cfscript.CFBinaryExpression;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFLiteral;
import cfml.parsing.cfscript.CFUnaryExpression;
import cfml.parsing.cfscript.CFVarDeclExpression;
import cfml.parsing.cfscript.ParseException;
import cfml.parsing.cfscript.cfFullVarExpression;
import cfml.parsing.cfscript.script.CFCompDeclStatement;
import cfml.parsing.cfscript.script.CFCompoundStatement;
import cfml.parsing.cfscript.script.CFExpressionStatement;
import cfml.parsing.cfscript.script.CFForInStatement;
import cfml.parsing.cfscript.script.CFForStatement;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFIfStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;

public class CFLint {

	StackHandler handler = new StackHandler();
	boolean inFunction = false;
	boolean inAssignment = false;
	boolean inComponent = false;
	BugList bugs;
	List<CFLintScanner> extensions = new ArrayList<CFLintScanner>();

	public CFLint() {
		super();
		extensions.add(new NestedCFOutput());
		extensions.add(new TypedQueryNew());
		extensions.add(new VarScoper());
		extensions.add(new ArgVarChecker());
		extensions.add(new ArgDefChecker());
		extensions.add(new OutputParmMissing());
		extensions.add(new GlobalVarChecker());
		
		final CFLintFilter filter = CFLintFilter.createFilter();
		bugs = new BugList(filter);
	}
	public CFLint(CFLintScanner ...bugsScanners) {
		super();
		for(CFLintScanner scanner: bugsScanners){
			extensions.add(scanner);
		}
		final CFLintFilter filter = CFLintFilter.createFilter();
		bugs = new BugList(filter);
	}

	public void scan(final String folder) {
		final File f = new File(folder);
		scan(f);
	}

	public void scan(final File folder) {
		for (final File file : folder.listFiles()) {
			if (file.isDirectory()) {
				if (!file.isHidden()) {
					scan(file);
				}
			} else {
				final String src = load(file);
				//System.out.println("processing " + file);
				try {
					process(src, file.getAbsolutePath());
				} catch (final Exception e) {
					bugs.add(new BugInfo.BugInfoBuilder().setMessageCode("FILE_ERROR")
							.setFilename(file.getAbsolutePath()).setMessage(e.getMessage()).setSeverity("ERROR")
							.build());
				}
			}
		}
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

	public void process(final String src, final String filename) throws ParseException, IOException {
		final CFMLSource cfmlSource = new CFMLSource(src);
		final List<Element> elements = cfmlSource.getChildElements();
		if (elements.size() == 0 && src.contains("component")) {
			// Check if pure cfscript
			final CFScriptStatement scriptStatement = new CFMLParser().parseScript(src);
			process(scriptStatement, filename, null, null);
		} else {
			processStack(elements, " ", filename, null);
		}
	}


	public void processStack(final List<Element> elements, final String space, final String filename,
			final String functionName) throws ParseException, IOException {
		for (final Element elem : elements) {
			process(elem, space, filename, functionName);
		}
	}

	private void process(final Element elem, final String space, final String filename, final String functionName)
			throws ParseException, IOException {
		final Context context = new Context(filename, elem, functionName, inAssignment, handler);
		context.setInComponent(inComponent);
		
		for (final CFLintScanner plugin : extensions) {
			plugin.element(elem, context, bugs);
		}
		if (elem.getName().equals("cfset") || elem.getName().equals("cfif")) {
			final int elemLine = elem.getSource().getRow(elem.getBegin());
			final int elemColumn = elem.getSource().getColumn(elem.getBegin());
			final Pattern p = Pattern.compile("<\\w+\\s(.*[^/])/?>");
			final String expr = elem.getFirstStartTag().toString();
			final Matcher m = p.matcher(expr);
			if (m.matches()) {
				try {
					final CFExpression expression = CFExpression.getCFExpression(m.group(1));
					if(expression == null){
						throw new NullPointerException("expression is null, parsing error");
					}
					process(expression, filename, elem, functionName);
				} catch (final NullPointerException npe) {
					final int line = elem.getSource().getRow(elem.getBegin());
					final int column = elem.getSource().getColumn(elem.getBegin());
					System.err.println("Error in " + elem.getSource() + " @ " + line + ":");
					bugs.add(new BugInfo.BugInfoBuilder().setLine(elemLine).setColumn(elemColumn + column)
							.setMessageCode("PARSE_ERROR").setSeverity("ERROR").setExpression(m.group(1))
							.setFilename(filename).setFunction(functionName).setMessage("Unable to parse").build());
				}
			}
		} else if (elem.getName().equals("cfargument")) {
			final String name = elem.getAttributeValue("name");
			if (name != null) {
				handler.addArgument(name);
			}
		} else if (elem.getName().equals("cfscript")) {
			final String cfscript = elem.getContent().toString();
			final CFScriptStatement scriptStatement = new CFMLParser().parseScript(cfscript);
			process(scriptStatement, filename, elem, functionName);
			// } else if (elem.getName().equals("cfoutput")) {
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

		if (elem.getName().equals("cffunction")) {
			inFunction = true;
			handler.push("function");
			processStack(elem.getChildElements(), space + " ", filename, elem.getAttributeValue("name"));
			inFunction = false;
			handler.pop();
		} else if (elem.getName().equals("cfcomponent")) {
			inComponent = true;
			handler.push("component");
			processStack(elem.getChildElements(), space + " ", filename, elem.getAttributeValue("name"));
			inComponent = false;
			handler.pop();
		} else {
			processStack(elem.getChildElements(), space + " ", filename, functionName);
		}
	}

	public String stripLineComments(final String cfscript) {
		return cfscript.replaceAll("//[^\r\n]*\r?\n", "\r\n");
	}

	private void process(final CFScriptStatement expression, final String filename, final Element elem,
			final String functionName) {
		final Context context = new Context(filename, elem, functionName, inAssignment, handler);
		context.setInComponent(inComponent);
		
		for (final CFLintScanner plugin : extensions) {
			plugin.expression(expression, context, bugs);
		}
		
		if (expression instanceof CFCompoundStatement) {
			for (final CFScriptStatement statement : ((CFCompoundStatement) expression).getStatements()) {
				process(statement, filename, elem, functionName);
			}
		} else if (expression instanceof CFExpressionStatement) {
			process(((CFExpressionStatement) expression).getExpression(), filename, elem, functionName);
		} else if (expression instanceof CFCompDeclStatement) {
			inComponent = true;
			process(((CFCompDeclStatement) expression).getBody(), filename, elem, functionName);
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
		} else if (expression instanceof CFFuncDeclStatement) {
			final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;
			inFunction = true;
			handler.push("function");
			if ("init".equals(function.getName())) {
				inFunction = false;
			}

			for (final CFFunctionParameter param : function.getFormals()) {
				handler.addArgument(param.getName());
			}
			process(function.getBody(), filename, elem, function.getName());
			inFunction = false;
			handler.pop();
		} else {
		}
	}

	private void process(final CFExpression expression, final String filename, final Element elem,
			final String functionName) {
		final Context context = new Context(filename, elem, functionName, inAssignment, handler);
		context.setInComponent(inComponent);
		
		for (final CFLintScanner plugin : extensions) {
			plugin.expression(expression, context, bugs);
		}
		if (expression instanceof CFUnaryExpression) {
			process(((CFUnaryExpression) expression).getSub(), filename, elem, functionName);
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
		} else if (expression instanceof CFVarDeclExpression) {
			handler.addVariable(((CFVarDeclExpression) expression).getName());
			process(((CFVarDeclExpression) expression).getInit(), filename, elem, functionName);
		} else if (expression instanceof CFLiteral) {
//		} else if (expression instanceof cfFullVarExpression) {
//			if (((cfFullVarExpression) expression).getExpressions().size() == 1) {
//				process(((cfFullVarExpression) expression).getExpressions().get(0), filename, elem, functionName);
//			}
		} else {
		}
	}

	public BugList getBugs() {
		return bugs;
	}
}
