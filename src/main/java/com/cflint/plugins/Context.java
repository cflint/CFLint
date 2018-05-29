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
import cfml.parsing.cfscript.HasToken;
import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Segment;

public class Context {

    public enum ContextType {
        COMPONENT, FUNCTION, OTHER, QUERY_LOOP
    }

    private String filename;
    private String componentName;
    private final Element element;
    private CFFuncDeclStatement functionInfo;
    private ContextType contextType;
    private String functionName;
    private boolean inAssignmentExpression;

    private boolean inComponent;
    private final StackHandler callStack;
    private final CommonTokenStream tokens;
    private final List<ContextMessage> messages = new ArrayList<>();
    private Context parent = null;
    private final List<String> ignores = new ArrayList<>();
    final private CFLintConfiguration configuration;

    public Context(final String filename, final Element element, final CFIdentifier functionName,
            final boolean inAssignmentExpression, final StackHandler handler, final CFLintConfiguration configuration) {
        super();
        this.filename = filename;
        this.element = element;
        this.functionName = functionName == null ? "" : functionName.Decompile(0);
        this.inAssignmentExpression = inAssignmentExpression;
        this.callStack = handler;
        this.tokens = null;
        this.configuration = configuration;
    }

    public Context(final String filename, final Element element, final String functionName,
            final boolean inAssignmentExpression, final StackHandler handler, final CommonTokenStream tokens,
            final CFLintConfiguration configuration) {
        super();
        this.filename = filename;
        this.element = element;
        this.functionName = functionName == null ? "" : functionName;
        this.inAssignmentExpression = inAssignmentExpression;
        this.callStack = handler;
        this.tokens = tokens;
        this.configuration = configuration;
    }

    public void setInAssignmentExpression(final boolean inAssignmentExpression) {
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

    public void addUniqueMessage(final ContextType contextType, final String messageCode, final String variable, final CFLintScanner source,
            final CFExpression cfExpression) {
        final Context parent = contextType!=null && getParent(contextType)==null?this:getParent(contextType);
        if (messageCode != null) {
            for (final ContextMessage msg : parent.messages) {
                if (ObjectEquals.equals(msg.getMessageCode(), messageCode)
                        && ObjectEquals.equals(variable, msg.getVariable())) {
                    return;
                }
            }
        }
        parent.addMessage(messageCode, variable, source, cfExpression,this);
    }

    public void addMessage(final String messageCode, final String variable) {
        messages.add(new ContextMessage(messageCode, variable));
    }

    public void addMessage(final String messageCode, final String variable, final CFLintScanner source,
            final Integer line, final Integer offset) {
        messages.add(new ContextMessage(messageCode, variable, source, line, offset, null));
    }

    public void addMessage(final String messageCode, final String variable, final CFLintScanner source,
            final Integer line, final Integer offset, final HasToken cfExpression) {
        messages.add(new ContextMessage(messageCode, variable, source, line, offset, cfExpression));
    }
    public ContextMessage addMessage(final String messageCode, final String variable, final CFLintScanner source,
            final HasToken cfExpression,final Context relativecontext) {
        ContextMessage cm = new ContextMessage(messageCode, variable, source, cfExpression, relativecontext);
        messages.add(cm);
        return cm;
    }
    public ContextMessage addMessage(final String messageCode, final String variable, final CFLintScanner source,
            final HasToken cfExpression) {
        return addMessage(messageCode,variable,source,cfExpression,null);
    }
    public ContextMessage addMessage(final String messageCode, final String variable, final CFLintScanner source) {
        ContextMessage cm = new ContextMessage(messageCode, variable, source, (CFExpression)null, null);
        messages.add(cm);
        return cm;
    }

    public void addMessage(final String messageCode, final String variable, final Integer line, final Integer offset) {
        messages.add(new ContextMessage(messageCode, variable, line, offset));
    }

    public MessageBuilder messageBuilder(final CFLintScanner source) {
        final MessageBuilder bldr = new MessageBuilder(messages);
        bldr.message = new ContextMessage(null, null);
        bldr.message.source = source;
        return bldr;
    }

    public class MessageBuilder {
        ContextMessage message;
        private final List<ContextMessage> messages;
        int additionalOffset=0;

        public MessageBuilder(final List<ContextMessage> messages) {
            super();
            this.messages = messages;
        }

        public MessageBuilder at(final CFExpression expression) {
            return at(expression == null ? null : expression.getToken());
        }

        public ContextMessage build(final String messageCode, final String variable) {
            message.messageCode = messageCode;
            message.variable = variable;
            messages.add(message);
            final ContextMessage message2 = new ContextMessage(null, null);
            message2.line = message.line;
            message2.offset = message.offset;
            message2.column = message.column;
            message = message2;
            return message;
        }
        public ContextMessage buildUnique(final String messageCode, final String variable) {
            message.messageCode = messageCode;
            message.variable = variable;
            if (message.messageCode != null) {
                for (final ContextMessage msg : messages) {
                    if (ObjectEquals.equals(msg.getMessageCode(), messageCode)
                            && ObjectEquals.equals(variable, msg.getVariable())) {
                        message.messageCode = null;
                        message.variable = null;
                        return message;
                    }
                }
            }
            return build(messageCode,variable);
        }

        public MessageBuilder at(final HasToken parsedStatement) {
            return at(parsedStatement == null ? null : parsedStatement.getToken());
        }

        public MessageBuilder at(final Token token) {
            message.line = token.getLine();
            message.offset = additionalOffset + token.getStartIndex();
            message.column = token.getCharPositionInLine() + 1;
            if (element != null) {
                final int elemoffset = offset();
                final int elemLine = element.getSource().getRow(elemoffset);
                final int elemColumn = element.getSource().getColumn(elemoffset);
                //Only add the columns on the first line.
                if (message.line <=1 && elemColumn > 0) {
                    message.column += elemColumn-1;
                }
                if (elemLine > 0) {
                    message.line += elemLine - 1;
                }
                message.offset += elemoffset;
            }
            return this;
        }

        public MessageBuilder at(final Element element) {
            message.offset = additionalOffset + element.getBegin();
            message.line = element.getSource().getRow(element.getBegin());
            message.column = element.getSource().getColumn(element.getBegin());
            return this;
        }

        public MessageBuilder at(final Attribute attributeObj) {
            message.offset = additionalOffset + attributeObj.getBegin();
            message.line = attributeObj.getSource().getRow(attributeObj.getBegin());
            message.column = attributeObj.getSource().getColumn(attributeObj.getBegin());
            return this;
        }

        public MessageBuilder at(final Segment attributeObj) {
            message.offset = additionalOffset + attributeObj.getBegin();
            message.line = attributeObj.getSource().getRow(attributeObj.getBegin());
            message.column = attributeObj.getSource().getColumn(attributeObj.getBegin());
            return this;
        }

        public MessageBuilder extraOffset(int additionalOffset) {
            this.additionalOffset = additionalOffset;
            message.offset+=4;
            return this;
        }
    }

    public static class ContextMessage {
        private String messageCode;
        private String variable;
        private Integer line;
        private Integer column;
        private Integer offset;
        private CFLintScanner source;
        private final HasToken cfExpression;
        private Context relativeContext;
        private Integer extraOffset;
        private Object locator;

        public ContextMessage(final String messageCode, final String variable) {
            super();
            this.messageCode = messageCode;
            this.variable = variable;
            this.source = null;
            this.cfExpression = null;
        }

        public ContextMessage(final String messageCode, final String variable, final CFLintScanner source,
                final Integer line, final Integer offset, final HasToken cfExpression) {
            super();
            this.messageCode = messageCode;
            this.variable = variable;
            this.source = source;
            this.line = line;
            this.offset = offset;
            this.cfExpression = cfExpression;
        }
        public ContextMessage(final String messageCode, final String variable, final CFLintScanner source,
                final HasToken cfExpression, final Context relativeContext) {
            super();
            this.messageCode = messageCode;
            this.variable = variable;
            this.source = source;
            this.cfExpression = cfExpression;
            this.relativeContext = relativeContext;
        }

        public ContextMessage(final String messageCode, final String variable, final CFLintScanner source,
                final Integer line, final Integer offset) {
            super();
            this.messageCode = messageCode;
            this.variable = variable;
            this.source = source;
            this.line = line;
            this.offset = offset;
            this.cfExpression = null;
        }

        public ContextMessage(final String messageCode, final String variable, final Integer line,
                final Integer offset) {
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

        public HasToken getCfExpression() {
            return cfExpression;
        }

        public Integer getColumn() {
            return column;
        }

        public Context getRelativeContext() {
            return relativeContext;
        }

        public Integer getExtraOffset() {
            return extraOffset;
        }

        public void setExtraOffset(Integer extraOffset) {
            this.extraOffset = extraOffset;
        }

        public Object getLocator() {
            return locator;
        }

        public void setLocator(Object locator) {
            this.locator = locator;
        }
        public ContextMessage atLocation(Segment locator) {
            this.locator = locator;
            return this;
        }
    }

    public Context subContext(final Element elem, final CommonTokenStream tokens) {
        final Context context2 = new Context(getFilename(), elem == null ? this.element : elem, getFunctionName(),
                isInAssignmentExpression(), callStack, tokens, configuration);
        context2.setInComponent(isInComponent());
        context2.parent = this;
        context2.componentName = componentName;
        return context2;
    }

    public Context subContext(final Element elem) {
        final Context context2 = new Context(getFilename(), elem == null ? this.element : elem, getFunctionName(),
                isInAssignmentExpression(), callStack, tokens, configuration);
        context2.setInComponent(isInComponent());
        context2.parent = this;
        context2.componentName = componentName;
        return context2;
    }

    public Context subContextInAssignment() {
        return subContextInAssignment(true);
    }

    public Context subContextInAssignment(final boolean assignment) {
        final Context context2 = new Context(getFilename(), this.element, getFunctionName(), isInAssignmentExpression(),
                callStack, tokens, configuration);
        context2.setInComponent(isInComponent());
        context2.parent = this;
        context2.componentName = componentName;
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
        return offset(null);
    }
    public int offset(ContextMessage message) {
        if(message!=null && message.getLocator()!=null){
            if(message.getLocator() instanceof Segment){
                return ((Segment)message.getLocator()).getBegin();
            }
        }
        if (element != null) {
            if (element.getName().equalsIgnoreCase(CF.CFSCRIPT)){
                return element.getStartTag().getEnd();
            }
            else if(element.getEndTag() != null && element.getContent().length()>0
                    && (element.getName().equalsIgnoreCase(CF.CFQUERY)
                            || element.getName().equalsIgnoreCase(CF.CFSAVECONTENT)
                            )
                    ) {
                return element.getStartTag().getEnd();
            } else if (element.getName().equalsIgnoreCase(CF.CFSET)
                    //|| element.getName().equalsIgnoreCase(CF.CFQUERY)
                    //|| element.getName().equalsIgnoreCase(CF.CFSAVECONTENT)
                    || element.getName().equalsIgnoreCase(CF.CFIF)
                    || element.getName().equalsIgnoreCase(CF.CFELSEIF)) {
                return element.getStartTag().getTagContent().getBegin() + 1;
            }
            return element.getStartTag().getBegin();
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

    public Iterable<Token> beforeTokens(final Token t) {
        return new ContextTokensIterable(t, -1);
    }

    public Iterable<Token> afterTokens(final Token t) {
        return new ContextTokensIterable(t, 1);
    }

    public class ContextTokensIterable implements Iterable<Token> {

        private final Token token;
        private final int direction;

        public ContextTokensIterable(final Token token, final int direction) {
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

        public ContextTokensIterator(final Token token, final int direction) {
            this.tokenIndex = token.getTokenIndex() + direction;
            this.direction = direction;
        }

        @Override
        public boolean hasNext() {
            if (direction < 0) {
                return tokens != null && tokenIndex >= 0;
            } else {
                return tokens != null && tokenIndex < tokens.getTokens().size();
            }
        }

        @Override
        public Token next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (tokens != null && tokenIndex >= 0) {
                final Token retval = tokens.getTokens().get(tokenIndex);
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
     * @param type
     *            the type of context to retrieve from the parent tree
     * @return the parent context of the given type OR the root context if none
     *         matches
     */
    public Context getParent(final ContextType type) {
        Context p = this;
        while (p.parent != null && p.contextType != type) {
            p = p.parent;
        }
        return p;
    }

    public void ignore(final List<String> ignores) {
        this.ignores.addAll(ignores);
    }

    public boolean isSuppressed(final BugInfo bugInfo) {
        return ignores.contains(bugInfo.getMessageCode()) || (parent != null && parent.isSuppressed(bugInfo));
    }

    public CFFuncDeclStatement getFunctionInfo() {
        return functionInfo;
    }

    public void setFunctionInfo(final CFFuncDeclStatement functionInfo) {
        this.functionInfo = functionInfo;
        if (this.functionInfo != null) {
            this.functionName = functionInfo.getName() == null ? "" : functionInfo.getName().Decompile(0);
        }
    }

    public ContextType getContextType() {
        return contextType;
    }

    public void setContextType(final ContextType contextType) {
        this.contextType = contextType;
        if (contextType == ContextType.COMPONENT && componentName == null) {
            assignComponentNameFromFile();
        }
    }

    private void assignComponentNameFromFile() {
        if (filename != null && filename.trim().length() > 0) {
            componentName = new File(filename.trim()).getName().replaceAll("[.]\\w+", "");
        }
    }

    public CFLintConfiguration getConfiguration() {
        return configuration;
    }
}
