package com.cflint.plugins;

import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFIdentifier;

import com.cflint.StackHandler;

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

	public void setFilename(String filename) {
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

	public void setFunctionIdentifier(CFIdentifier functionName) {
		this.functionName = functionName==null?"":functionName.Decompile(0);
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public void setComponentName(String componentName) {
		if (componentName == null) {
			this.componentName = componentFromFile(this.filename);
		}
		else {
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
			key.append(functionName);
		}
		return key.toString();
	}

	public boolean isInComponent() {
		return inComponent;
	}

	public void setInComponent(boolean inComponent) {
		this.inComponent = inComponent;
	}

	public List<ContextMessage> getMessages() {
		return messages;
	}

	public void addMessage(String messageCode, String variable) {
		messages.add(new ContextMessage(messageCode,variable));
	}
	
	public static class ContextMessage{
		String messageCode;
		String variable;
		
		public ContextMessage(String messageCode, String variable) {
			super();
			this.messageCode = messageCode;
			this.variable = variable;
		}
		public String getMessageCode() {
			return messageCode;
		}
		public String getVariable() {
			return variable;
		}
	}
	
	public Context subContext(final Element elem){
		Context context2 = new Context(getFilename(), elem, getFunctionName(),isInAssignmentExpression(), callStack);
		context2.setInComponent(isInComponent());
		return context2;
	}

	public int startLine() {
		if(element != null && element.getSource() !=null)
			return element.getSource().getRow(element.getBegin());
		else 
			return 1;
	}

	protected String componentFromFile(String filename) {
		int dotPosition = filename.lastIndexOf(".");
		String separator = System.getProperty("file.separator");
    	int seperatorPosition = filename.lastIndexOf(separator);

    	if (dotPosition == -1 || seperatorPosition == -1) {
    		return null;
    	}
    	
    	return filename.substring(seperatorPosition+1, dotPosition);
	}
}
