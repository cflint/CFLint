package com.cflint;

import java.util.List;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginParameter;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFParsedStatement;
import net.htmlparser.jericho.Element;

public class BugInfo implements Comparable<BugInfo> {

    private String filename;
    private int line = 1; // Default to non-zero task #230
    private int column = 1;
    private String message;
    private String messageCode;
    private String expression;
    private String function;
    private String variable;
    private String component;
    private String severity;

    public BugInfo() {
        super();
    }

    public int getLine() {
        return line;
    }

    public void setLine(final int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(final int row) {
        this.column = row;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(final String messageCode) {
        this.messageCode = messageCode;
    }

    public static class BugInfoBuilder {
        BugInfo bugInfo = new BugInfo();
        List<PluginParameter> parameters = null;

        public void setRuleParameters(final List<PluginParameter> parameters) {
            this.parameters = parameters;
        }

        public BugInfoBuilder setLine(final int line) {
            bugInfo.line = line;
            return this;
        }

        public BugInfoBuilder setColumn(final int column) {
            bugInfo.column = column;
            return this;
        }

        public BugInfoBuilder setMessage(final String message) {
            bugInfo.message = message;
            return this;
        }

        public BugInfoBuilder setMessageCode(final String messageCode) {
            bugInfo.messageCode = messageCode;
            return this;
        }

        public BugInfoBuilder setMessageInfo(final PluginMessage messageInfo) {
            setMessageCode(messageInfo.getCode());
            setSeverity(messageInfo.getSeverity());
            setMessage(messageInfo.getMessageText());
            return this;
        }

        public BugInfoBuilder setFilename(final String filename) {
            bugInfo.filename = filename.replaceAll("(\\r|\\n)", "");
            return this;
        }

        public BugInfoBuilder setFunction(final String function) {
            bugInfo.function = function;
            return this;
        }

        public BugInfoBuilder setComponent(final String component) {
            bugInfo.component = component;
            return this;
        }

        public BugInfoBuilder setVariable(final String variable) {
            bugInfo.variable = variable;
            return this;
        }

        public BugInfoBuilder setSeverity(final String severity) {
            bugInfo.severity = severity;
            return this;
        }

        public BugInfoBuilder setExpression(final String expression) {
            if (expression == null) {
            	bugInfo.expression = "";
            } else if (expression.length() > 200) {
                bugInfo.expression = expression.substring(0, 200);
            } else {
                bugInfo.expression = expression;
            }
            return this;
        }

        public BugInfo build() {
            doMessageText(null);
            return bugInfo;
        }

        public BugInfo build(final CFParsedStatement expression, final Element elem) {
            int elemLine = 1;
            int elemColumn = 1;
            if (elem != null) {
                elemLine = elem.getSource().getRow(elem.getBegin());
                elemColumn = elem.getSource().getColumn(elem.getBegin());
            }
            bugInfo.setLine(elemLine + Math.max(expression == null ? 0 : expression.getLine() - 1, 0));
            bugInfo.setColumn(elemColumn + Math.max(expression == null ? 0 : expression.getColumn() - 1, 0));
            doMessageText(elem);
            return bugInfo;
        }

        public BugInfo build(final CFExpression expression, final Element elem) {
            int elemLine = 1;
            int elemColumn = 1;
            if (elem != null) {
                elemLine = elem.getSource().getRow(elem.getBegin());
                elemColumn = elem.getSource().getColumn(elem.getBegin());
            }
            bugInfo.setLine(elemLine + Math.max(expression == null ? 0 : expression.getLine() - 1, 0));
            if(expression == null || expression.getColumn() < 1){
            	bugInfo.setColumn(elemColumn);
            }else if (expression.getLine()>1){
            	bugInfo.setColumn(expression.getColumn());
            }else{
            	bugInfo.setColumn(elemColumn + expression.getColumn() - 1);
            }
            doMessageText(elem);
            return bugInfo;
        }

        private final String notNull(final String in) {
            if (in == null) {
                return "";
            }
            return in.trim();
        }

        private void doMessageText(final Element elem) {
            String message = notNull(bugInfo.getMessage());
            final String variable = notNull(bugInfo.getVariable());
            message = message.replace("${variable}", variable);
            if (bugInfo.function != null) {
                message = message.replace("${function}", bugInfo.function);
            }
            if (bugInfo.filename != null) {
                message = message.replace("${filename}", bugInfo.filename);
            }

            if (bugInfo.component != null) {
                message = message.replace("${component}", bugInfo.component);
            }else{
                message = message.replace("${component}", "unknown");
            }

            if (message.contains("{tag}") && elem != null) {
                message = message.replaceAll("\\$\\{tag\\}", notNull(elem.getName()));
            }

            if (parameters != null) {
                for (final PluginParameter param : parameters) {
                    message = message.replaceAll("\\$\\{" + param.getName() + "\\}", notNull(param.getValue()));
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

    public void setExpression(final String expression) {
        this.expression = expression;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(final String filename) {
        this.filename = filename.trim();
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(final String function) {
        this.function = function;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(final String variable) {
        this.variable = variable;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(final String severity) {
        this.severity = severity;
    }

    /**
     * Makes BugInfo sortable by filename,line,col,messagecode
     */
    @Override
    public int compareTo(final BugInfo o) {
        if (filename.compareTo(o.filename) != 0) {
            return filename.compareTo(o.filename);
        }
        if (((Integer) line).compareTo((o.line)) != 0) {
            return ((Integer) line).compareTo((o.line));
        }
        if (((Integer) column).compareTo((o.column)) != 0) {
            return ((Integer) column).compareTo((o.column));
        }
        return messageCode.compareTo(o.messageCode);
    }
}
