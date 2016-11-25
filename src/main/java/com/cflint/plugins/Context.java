package com.cflint.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import com.cflint.BugInfo;
import com.cflint.StackHandler;

import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import net.htmlparser.jericho.Element;

public class Context {

	String filename;
	String componentName;
	final Element element;
	CFFuncDeclStatement functionInfo;

	String functionName;
	boolean inAssignmentExpression;
	public void setInAssignmentExpression(boolean inAssignmentExpression) {
		this.inAssignmentExpression = inAssignmentExpression;
	}

	boolean inComponent;
	final StackHandler callStack;
	final CommonTokenStream tokens;
	final List<ContextMessage> messages = new ArrayList<ContextMessage>();
	Context parent=null;
	List<String> ignores = new ArrayList<String>();

	public Context(final String filename, final Element element, final CFIdentifier functionName,
			final boolean inAssignmentExpression, final StackHandler handler) {
		super();
		this.filename = filename;
		this.element = element;
		this.functionName = functionName == null ? "" : functionName.Decompile(0);
		this.inAssignmentExpression = inAssignmentExpression;
		this.callStack = handler;
		this.tokens=null;
	}

	public Context(final String filename, final Element element, final String functionName,
			final boolean inAssignmentExpression, final StackHandler handler,CommonTokenStream tokens) {
		super();
		this.filename = filename;
		this.element = element;
		this.functionName = functionName == null ? "" : functionName;
		this.inAssignmentExpression = inAssignmentExpression;
		this.callStack = handler;
		this.tokens=tokens;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(final String filename) {
		this.filename = filename;
	}

	public Element getElement() {
		return element;
	}

	public String getFunctionName() {
		return functionName;
	}

	public String getComponentName() {
		return componentName;
	}
	public String calcComponentName() {
		if (componentName!= null && !componentName.trim().isEmpty()){
			return componentName.trim();
		}
		if(filename == null){
			return "";
		}
		//Return filename without the cfc extension
		return new File(filename).getName().replaceAll("\\.\\w+$", "");
	}

	public void setFunctionName(final String functionName) {
		this.functionName = functionName;
	}

	public void setComponentName(final String componentName) {
		if (componentName == null) {
			this.componentName = componentFromFile(this.filename);
		} else {
			this.componentName = componentName;
		}
	}

	public boolean isInFunction() {
		return functionName != null && getFunctionName().length() > 0;
	}

	public boolean isInAssignmentExpression() {
		return inAssignmentExpression;
	}

	public StackHandler getCallStack() {
		return callStack;
	}

	public String fileFunctionString() {
		if (functionName == null && filename == null) {
			return null;
		}
		final StringBuilder key = new StringBuilder();
		if (filename != null) {
			key.append(filename.trim());
		}
		key.append(":");
		if (functionName != null) {
			key.append(functionName);
		}
		return key.toString();
	}

	public boolean isInComponent() {
		return inComponent;
	}

	public void setInComponent(final boolean inComponent) {
		this.inComponent = inComponent;
	}

	public List<ContextMessage> getMessages() {
		return messages;
	}

	public void addMessage(final String messageCode, final String variable) {
		messages.add(new ContextMessage(messageCode, variable));
	}

	public void addMessage(final String messageCode, final String variable, final Integer line) {
		messages.add(new ContextMessage(messageCode, variable, line));
	}

	public static class ContextMessage {
		String messageCode;
		String variable;
		Integer line;

		public ContextMessage(final String messageCode, final String variable) {
			super();
			this.messageCode = messageCode;
			this.variable = variable;
		}

		public ContextMessage(final String messageCode, final String variable, final Integer line) {
			this(messageCode, variable);
			this.line = line;
		}

		public String getMessageCode() {
			return messageCode;
		}

		public String getVariable() {
			return variable;
		}

		public Integer getLine() {
			return line;
		}
	}

	
	public Context subContext(final Element elem) {
		final Context context2 = new Context(getFilename(), elem == null? this.element:elem, 
				getFunctionName(), isInAssignmentExpression(),
				callStack,tokens);
		context2.setInComponent(isInComponent());
		context2.parent = this;
		return context2;
	}

	public int startLine() {
		if (element != null && element.getSource() != null) {
			return element.getSource().getRow(element.getBegin());
		} else {
			return 1; // not zero
		}
	}

	protected String componentFromFile(final String filename) {
		final int dotPosition = filename.lastIndexOf(".");
		final String separator = System.getProperty("file.separator");
		final int seperatorPosition = filename.lastIndexOf(separator);

		if (dotPosition == -1 || seperatorPosition == -1) {
			return null;
		}

		return filename.substring(seperatorPosition + 1, dotPosition);
	}

	public CommonTokenStream getTokens() {
		return tokens;
	}
	
	public Iterable<Token> beforeTokens(Token t){
		return new ContextTokensIterable(t,-1);
	}
	public Iterable<Token> afterTokens(Token t){
		return new ContextTokensIterable(t,1);
	}
	
	public class ContextTokensIterable implements Iterable<Token> {

		final Token token;
		final int direction;
		
		public ContextTokensIterable(Token token, int direction){
			this.token=token;
			this.direction=direction;
		}
		
		@Override
		public Iterator<Token> iterator() {
			return new ContextTokensIterator(token,direction);
		}
	}
	public class ContextTokensIterator implements Iterator<Token> {

		int tokenIndex;
		final int direction;
		
		public ContextTokensIterator(Token token, int direction){
			this.tokenIndex=token.getTokenIndex() + direction;
			this.direction=direction;
		}

		@Override
		public boolean hasNext() {
			if(direction <0)
				return tokens != null && tokenIndex >= 0;
			else
				return tokens != null && tokenIndex < tokens.getTokens().size();		
		}

		@Override
		public Token next() {
			if (tokens != null && tokenIndex >= 0){
				Token retval = tokens.getTokens().get(tokenIndex);
				tokenIndex += direction;
				return retval;
			}
			return null;
		}
	
		@Override
		public void remove(){
			throw new UnsupportedOperationException();
		}
	}

	public Context getParent() {
		return parent;
	}

	public void ignore(List<String> ignores) {
		this.ignores.addAll(ignores);
	}
	
	public boolean isSuppressed(BugInfo bugInfo){
		return ignores.contains(bugInfo.getMessageCode())
		 || (parent != null && parent.isSuppressed(bugInfo));
	}
	
	public CFFuncDeclStatement getFunctionInfo() {
		return functionInfo;
	}

	public void setFunctionInfo(CFFuncDeclStatement functionInfo) {
		this.functionInfo = functionInfo;
		if(this.functionInfo != null){
			this.functionName = functionInfo.getName() == null ? "" : functionInfo.getName().Decompile(0);
		}
	}

}
