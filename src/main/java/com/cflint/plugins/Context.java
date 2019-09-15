package com.cflint.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import com.cflint.BugInfo;
import com.cflint.CF;
import com.cflint.StackHandler;
import com.cflint.config.CFLintConfiguration;
import com.cflint.tools.ObjectEquals;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import net.htmlparser.jericho.Element;

public class Context {

    public enum ContextType {
        COMPONENT, FUNCTION, OTHER, QUERY_LOOP, PSEUDO_CFML
    }

    private String filename;
    private String componentName;
    private final Element element;
    private CFExpression pseudoCfmlExpression;
    private CFFuncDeclStatement functionInfo;
    private ContextType contextType;
    private String functionName;
    private boolean inAssignmentExpression;
    private boolean inStructKeyExpression;

    public boolean isInStructKeyExpression() {
		return inStructKeyExpression;
	}

	public void setInStructKeyExpression(boolean inStructKeyExpression) {
		this.inStructKeyExpression = inStructKeyExpression;
	}

	private boolean inComponent;
    private final StackHandler callStack;
    private final CommonTokenStream tokens;
    private final List<ContextMessage> messages = new ArrayList<>();
    private Context parent = null;
    private List<String> ignores = new ArrayList<>();
    final private CFLintConfiguration configuration;

    public Context(final String filename, final Element element, final CFIdentifier functionName,
            final boolean inAssignmentExpression, final StackHandler handler,CFLintConfiguration configuration) {
        super();
        this.filename = filename;
        this.element = element;
        this.functionName = functionName == null ? "" : functionName.Decompile(0);
        this.inAssignmentExpression = inAssignmentExpression;
        this.callStack = handler;
        this.tokens = null;
        this.configuration=configuration;
    }

    public Context(final String filename, final Element element, final String functionName,
            final boolean inAssignmentExpression, final StackHandler handler, CommonTokenStream tokens,CFLintConfiguration configuration) {
        super();
        this.filename = filename;
        this.element = element;
        this.functionName = functionName == null ? "" : functionName;
        this.inAssignmentExpression = inAssignmentExpression;
        this.callStack = handler;
        this.tokens = tokens;
        this.configuration=configuration;
    }

    public void setInAssignmentExpression(boolean inAssignmentExpression) {
        this.inAssignmentExpression = inAssignmentExpression;
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
        if (componentName != null && !componentName.trim().isEmpty()) {
            return componentName.trim();
        }
        if (filename == null) {
            return "";
        }
        // Return filename without the cfc extension
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

    public void addUniqueMessage(final String messageCode, final String variable, final CFLintScanner source) {
        addUniqueMessage(messageCode, variable, source, null, null, null);
    }

    public void addUniqueMessage(final String messageCode, final String variable, final CFLintScanner source,
            final Integer line, final Integer offset, final CFExpression cfExpression) {
        if (messageCode != null) {
            for (ContextMessage msg : messages) {
                if (ObjectEquals.equals(msg.getMessageCode(), messageCode)
                        && ObjectEquals.equals(variable, msg.getVariable())) {
                    return;
                }
            }
        }
        addMessage(messageCode, variable, source, line, offset, cfExpression);
    }

    public void addMessage(final String messageCode, final String variable) {
        messages.add(new ContextMessage(messageCode, variable));
    }

    public void addMessage(final String messageCode, final String variable, final CFLintScanner source,
            final Integer line, final Integer offset) {
        messages.add(new ContextMessage(messageCode, variable, source, line, offset,null));
    }
    public void addMessage(final String messageCode, final String variable, final CFLintScanner source,
            final Integer line, final Integer offset, final CFExpression cfExpression) {
        messages.add(new ContextMessage(messageCode, variable, source, line, offset,cfExpression));
    }
    public void addMessage(final String messageCode, final String variable, final CFLintScanner source,
            final Integer line, final Integer offset, final CFExpression cfExpression, final Context originalContext) {
        messages.add(new ContextMessage(messageCode, variable, source, line, offset,cfExpression,originalContext));
    }

    public void addMessage(final String messageCode, final String variable, final Integer line,
            final Integer offset) {
        messages.add(new ContextMessage(messageCode, variable, line, offset));
    }

    public static class ContextMessage {
        private String messageCode;
        private String variable;
        private Integer line;
        private Integer offset;
        private CFLintScanner source;
        private final CFExpression cfExpression;
        final Context originalContext;

        public Context getOriginalContext() {
			return originalContext;
		}

		public ContextMessage(final String messageCode, final String variable) {
            super();
            this.messageCode = messageCode;
            this.variable = variable;
            this.source = null;
            this.cfExpression = null;
            this.originalContext=null;
        }

        public ContextMessage(final String messageCode, final String variable, CFLintScanner source,
                final Integer line, final Integer offset, final CFExpression cfExpression) {
            super();
            this.messageCode = messageCode;
            this.variable = variable;
            this.source = source;
            this.line = line;
            this.offset = offset;
            this.cfExpression = cfExpression;
            this.originalContext=null;
        }
        public ContextMessage(final String messageCode, final String variable, CFLintScanner source,
                final Integer line, final Integer offset, final CFExpression cfExpression,final Context originalContext) {
            super();
            this.messageCode = messageCode;
            this.variable = variable;
            this.source = source;
            this.line = line;
            this.offset = offset;
            this.cfExpression = cfExpression;
            this.originalContext=originalContext;
        }
        public ContextMessage(final String messageCode, final String variable, CFLintScanner source,
                final Integer line, final Integer offset) {
            super();
            this.messageCode = messageCode;
            this.variable = variable;
            this.source = source;
            this.line = line;
            this.offset = offset;
            this.cfExpression = null;
            this.originalContext=null;
        }

        public ContextMessage(final String messageCode, final String variable, final Integer line, final Integer offset) {
            this(messageCode, variable);
            this.line = line;
            this.offset = offset;
        }

        public CFLintScanner getSource() {
            return source;
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

        public Integer getOffset() {
        	return offset;
        }

        public CFExpression getCfExpression() {
            return cfExpression;
        }
    }

    public Context subContext(final Element elem,final CommonTokenStream tokens) {
        final Context context2 = new Context(getFilename(), elem == null ? this.element : elem, getFunctionName(),
                isInAssignmentExpression(), callStack, tokens,configuration);
        context2.setInComponent(isInComponent());
        context2.parent = this;
        context2.componentName=componentName;
        context2.inStructKeyExpression=inStructKeyExpression;
        return context2;
    }
    public Context subContext(final Element elem) {
        final Context context2 = new Context(getFilename(), elem == null ? this.element : elem, getFunctionName(),
                isInAssignmentExpression(), callStack, tokens,configuration);
        context2.setInComponent(isInComponent());
        context2.parent = this;
        context2.componentName=componentName;
        context2.inStructKeyExpression=inStructKeyExpression;
        return context2;
    }
    public Context subContextCFML(final Element elem, final CFExpression pseudoCfmlExpression) {
        final Context context2 = new Context(getFilename(), elem, getFunctionName(),
                isInAssignmentExpression(), callStack, tokens,configuration);
        this.pseudoCfmlExpression = pseudoCfmlExpression;
        this.contextType=ContextType.PSEUDO_CFML;
        context2.setInComponent(isInComponent());
        context2.parent = this;
        context2.componentName=componentName;
        context2.inStructKeyExpression=inStructKeyExpression;
        return context2;
    }
    
    public Context subContextInAssignment() {
        return subContextInAssignment(true);
    }
    public Context subContextInAssignment(boolean assignment) {
        final Context context2 = new Context(getFilename(), this.element, getFunctionName(),
                isInAssignmentExpression(), callStack, tokens,configuration);
        context2.setInComponent(isInComponent());
        context2.parent = this;
        context2.componentName=componentName;
        context2.setInAssignmentExpression(assignment);
        return context2;
    }

    public int startLine() {
        if (element != null && element.getSource() != null) {
            return element.getSource().getRow(element.getBegin());
        } else {
            return 1; // not zero
        }
    }

    public int offset() {
        if (element != null) {
            if (element.getName().equalsIgnoreCase(CF.CFSCRIPT)) {
                return element.getStartTag().getEnd();
            } else if (element.getName().equalsIgnoreCase(CF.CFSET)) {
                return element.getStartTag().getTagContent().getBegin() + 1;
            }
            
            return element.getBegin();
        } else {
            return 0;
        }
    }

    protected String componentFromFile(final String filename) {
        final int dotPosition = filename.lastIndexOf('.');
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

    public Iterable<Token> beforeTokens(Token t) {
        return new ContextTokensIterable(t, -1);
    }

    public Iterable<Token> afterTokens(Token t) {
        return new ContextTokensIterable(t, 1);
    }

    public class ContextTokensIterable implements Iterable<Token> {

        private final Token token;
        private final int direction;

        public ContextTokensIterable(Token token, int direction) {
            this.token = token;
            this.direction = direction;
        }

        @Override
        public Iterator<Token> iterator() {
            return new ContextTokensIterator(token, direction);
        }
    }

    public class ContextTokensIterator implements Iterator<Token> {

        private int tokenIndex;
        private final int direction;

        public ContextTokensIterator(Token token, int direction) {
            this.tokenIndex = token.getTokenIndex() + direction;
            this.direction = direction;
        }

        @Override
        public boolean hasNext() {
            if (direction < 0)
                return tokens != null && tokenIndex >= 0;
            else
                return tokens != null && tokenIndex < tokens.getTokens().size();
        }

        @Override
        public Token next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (tokens != null && tokenIndex >= 0) {
                Token retval = tokens.getTokens().get(tokenIndex);
                tokenIndex += direction;
                return retval;
            }
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Context getParent() {
        return parent;
    }

    /**
     * 
     * @param type      the type of context to retrieve from the parent tree
     * @return          the parent context of the given type OR the root context if none
     *         matches
     */
    public Context getParent(ContextType type) {
        Context p = this;
        while (p.parent != null && p.contextType != type) {
            p = p.parent;
        }
        return p;
    }

    public void ignore(List<String> ignores) {
        this.ignores.addAll(ignores);
    }

    public boolean isSuppressed(BugInfo bugInfo) {
        return ignores.contains(bugInfo.getMessageCode()) || (parent != null && parent.isSuppressed(bugInfo));
    }

    public CFFuncDeclStatement getFunctionInfo() {
        return functionInfo;
    }

    public void setFunctionInfo(CFFuncDeclStatement functionInfo) {
        this.functionInfo = functionInfo;
        if (this.functionInfo != null) {
            this.functionName = functionInfo.getName() == null ? "" : functionInfo.getName().Decompile(0);
        }
    }

    public ContextType getContextType() {
        return contextType;
    }

    public void setContextType(ContextType contextType) {
        this.contextType = contextType;
        if(contextType == ContextType.COMPONENT && componentName==null){
            assignComponentNameFromFile();
        }
    }

	private void assignComponentNameFromFile() {
		if(filename != null && filename.trim().length()>0){
		    componentName= new File(filename.trim()).getName().replaceAll("[.]\\w+", "");
		}
	}

    public CFLintConfiguration getConfiguration() {
        return configuration;
    }

    public CFExpression getPseudoCfmlExpression() {
        return pseudoCfmlExpression;
    }

    public void setPseudoCfmlExpression(CFExpression pseudoCfmlExpression) {
        this.pseudoCfmlExpression = pseudoCfmlExpression;
    }
}
