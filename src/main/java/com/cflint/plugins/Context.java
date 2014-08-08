package com.cflint.plugins;

import com.cflint.StackHandler;

import net.htmlparser.jericho.Element;

public class Context {

	final String filename;
	final Element element;
	final String functionName;
	final boolean inAssignmentExpression;
	boolean inComponent;
	final StackHandler callStack;

	public Context(final String filename, final Element element, final String functionName,
			final boolean inAssignmentExpression, final StackHandler handler) {
		super();
		this.filename = filename;
		this.element = element;
		this.functionName = functionName == null ? "" : functionName.trim();
		this.inAssignmentExpression = inAssignmentExpression;
		this.callStack = handler;
	}

	public String getFilename() {
		return filename;
	}

	public Element getElement() {
		return element;
	}

	public String getFunctionName() {
		return functionName;
	}

	public boolean isInFunction() {
		return functionName != null && functionName.length() > 0;
	}

	public boolean isInAssignmentExpression() {
		return inAssignmentExpression;
	}

	public StackHandler getCallStack() {
		return callStack;
	}

	public String fileFunctionString(){
		if(functionName == null && filename == null){
			return null;
		}
		StringBuilder key = new StringBuilder();
		if(filename != null){
			key.append(filename.trim());
		}
		key.append(":");
		if(functionName != null){
			key.append(functionName.trim());
		}
		return key.toString();
	}

	public boolean isInComponent() {
		return inComponent;
	}

	public void setInComponent(boolean inComponent) {
		this.inComponent = inComponent;
	}
}
