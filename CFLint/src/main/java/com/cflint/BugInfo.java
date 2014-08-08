package com.cflint;

import net.htmlparser.jericho.Element;
import com.parser.main.cfscript.CFParsedStatement;

public class BugInfo {

	String filename;
	int line;
	int column;
	String message;
	String messageCode;
	String expression;
	String function;
	String variable;
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
		public BugInfo build(){return bugInfo;}
		public BugInfo build(CFParsedStatement expression,final Element elem) {
			int elemLine = 0;
			int elemColumn = 0;
			if(elem != null){
				elemLine = elem.getSource().getRow(elem.getBegin());
				elemColumn = elem.getSource().getColumn(elem.getBegin());
			}
			bugInfo.setLine(elemLine + Math.max(expression==null?0:expression.getLine()-1,0));
			bugInfo.setColumn(elemColumn + Math.max(expression==null?0:expression.getColumn()-1,0));
			return bugInfo;
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
}
