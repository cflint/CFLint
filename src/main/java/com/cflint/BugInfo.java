package com.cflint;

import java.util.List;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginParameter;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFParsedStatement;
import net.htmlparser.jericho.Element;

public class BugInfo implements Comparable<BugInfo>{

	String filename;
	int line;
	int column;
	String message;
	String messageCode;
	String expression;
	String function;
	String variable;
	public BugInfo() {
		super();
	}
	String severity;
	
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int row) {
		this.column = row;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessageCode() {
		return messageCode;
	}
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	
	public static class BugInfoBuilder{
		BugInfo bugInfo = new BugInfo();
		List<PluginParameter> parameters = null;

		public void setRuleParameters(List<PluginParameter> parameters) {
			this.parameters = parameters;
		}
		
		public BugInfoBuilder setLine(int line) {
			bugInfo.line = line;
			return this;
		}
		public BugInfoBuilder setColumn(int column) {
			bugInfo.column = column;
			return this;
		}
		public BugInfoBuilder setMessage(String message) {
			bugInfo.message = message;
			return this;
		}
		public BugInfoBuilder setMessageCode(String messageCode) {
			bugInfo.messageCode = messageCode;
			return this;
		}
		public BugInfoBuilder setMessageInfo(PluginMessage messageInfo){
			setMessageCode(messageInfo.getCode());
			setSeverity(messageInfo.getSeverity());
			setMessage(messageInfo.getMessageText());
			return this;
		}
		public BugInfoBuilder setFilename(String filename) {
			bugInfo.filename = filename;
			return this;
		}
		public BugInfoBuilder setFunction(String function) {
			bugInfo.function = function;
			return this;
		}
		public BugInfoBuilder setVariable(String variable) {
			bugInfo.variable = variable;
			return this;
		}
		public BugInfoBuilder setSeverity(String severity) {
			bugInfo.severity = severity;
			return this;
		}
		public BugInfoBuilder setExpression(String expression) {
			if(expression == null){
				expression = "";
			}
			if(expression.length() >200){
				bugInfo.expression = expression.substring(0,200);
			}else{
				bugInfo.expression = expression;
			}
			return this;
		}
		public BugInfo build(){
			doMessageText(null);
			return bugInfo;
		}
		public BugInfo build(CFParsedStatement expression,final Element elem) {
			int elemLine = 1;
			int elemColumn = 1;
			if(elem != null){
				elemLine = elem.getSource().getRow(elem.getBegin());
				elemColumn = elem.getSource().getColumn(elem.getBegin());
			}
			bugInfo.setLine(elemLine + Math.max(expression==null?0:expression.getLine()-1,0));
			bugInfo.setColumn(elemColumn + Math.max(expression==null?0:expression.getColumn()-1,0));
			doMessageText(elem);
			return bugInfo;
		}
		public BugInfo build(CFExpression expression,final Element elem) {
			int elemLine = 1;
			int elemColumn = 1;
			if(elem != null){
				elemLine = elem.getSource().getRow(elem.getBegin());
				elemColumn = elem.getSource().getColumn(elem.getBegin());
			}
			bugInfo.setLine(elemLine + Math.max(expression==null?0:expression.getLine()-1,0));
			bugInfo.setColumn(elemColumn + Math.max(expression==null?0:expression.getColumn()-1,0));
			doMessageText(elem);
			return bugInfo;
		}
		
		private final String notNull(String in){
			if(in == null){
				return "";
			}
			return in.trim();
		}
		
		private void doMessageText(final Element elem) {
			String message = notNull(bugInfo.getMessage());
			message = message.replaceAll("\\$\\{variable\\}",notNull(bugInfo.getVariable()));
			if(message.contains("{tag}") && elem != null){
				message = message.replaceAll("\\$\\{tag\\}",notNull(elem.getName()));
			}
			
			if(parameters != null){
				for(PluginParameter param: parameters){
					message = message.replaceAll("\\$\\{"+param.getName()+"\\}",notNull(param.getValue()));
				}
			}
			setMessage(message);
		}
	}

	@Override
	public String toString() {
		return "BugInfo [filename=" + filename + ", line=" + line + ", column=" + column + ", message=" + message
				+ ", messageCode=" + messageCode + ", expression=" + expression + "]";
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	/**
	 * Makes BugInfo sortable by filename,line,col,messagecode
	 */
	@Override
	public int compareTo(BugInfo o) {
		if(filename.compareTo(o.filename) != 0){
			return filename.compareTo(o.filename);
		}
		if(((Integer)line).compareTo(((Integer)o.line)) != 0){
			return ((Integer)line).compareTo(((Integer)o.line));
		}
		if(((Integer)column).compareTo(((Integer)o.column)) != 0){
			return ((Integer)column).compareTo(((Integer)o.column));
		}
		return messageCode.compareTo(o.messageCode);
	}
}
