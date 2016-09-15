package com.cflint.plugins;

import java.util.ArrayList;
import java.util.List;

import com.cflint.StackHandler;

import cfml.parsing.cfscript.CFIdentifier;
import net.htmlparser.jericho.Element;

public class Context {

	String filename;
	String componentName;
	final Element element;
	String functionName;
	final boolean inAssignmentExpression;
	boolean inComponent;
	final StackHandler callStack;
	final List<ContextMessage> messages = new ArrayList<ContextMessage>();

	public Context(final String filename, final Element element, final CFIdentifier functionName,
			final boolean inAssignmentExpression, final StackHandler handler) {
		super();
		this.filename = filename;
		this.element = element;
		this.functionName = functionName == null ? "" : functionName.Decompile(0);
		this.inAssignmentExpression = inAssignmentExpression;
		this.callStack = handler;
	}

	public Context(final String filename, final Element element, final String functionName,
			final boolean inAssignmentExpression, final StackHandler handler) {
		super();
		this.filename = filename;
		this.element = element;
		this.functionName = functionName == null ? "" : functionName;
		this.inAssignmentExpression = inAssignmentExpression;
		this.callStack = handler;
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

	public void setFunctionIdentifier(final CFIdentifier functionName) {
		this.functionName = functionName == null ? "" : functionName.Decompile(0);
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
		final Context context2 = new Context(getFilename(), elem, getFunctionName(), isInAssignmentExpression(),
				callStack);
		context2.setInComponent(isInComponent());
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
}
