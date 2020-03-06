package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.CF;
import com.cflint.config.CFLintConfiguration;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFFunctionParameter;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

/**
 * Checks that function arguments are named correctly.
 */
@Extension
public class ArgumentNameChecker extends CFLintScannerAdapter {
    /**
     * Name of minimum length parameter.
     */
    private static final String MIN_LENGTH = "minLength";

    /**
     * Name of maximum length parameter.
     */
    private static final String MAX_LENGTH = "maxLength";

    /**
     * Name of maximum words parameter.
     */
    private static final String MAX_WORDS = "maxWords";

    /**
     * List of disallowed prefix names.
     */
    private static final String NAME_PREFIX = "FlagArgumentNamePrefix";

    /**
     * List of disallowed suffix names.
     */
    private static final String NAME_SUFFIX = "FlagArgumentNameSuffix";

    /**
     * List of required prefix names.
     */
    private static final String REQUIRED_NAME_PREFIX = "RequiredArgumentNamePrefix";

    /**
     * Minimum number of characters for an argument name.
     */
    private int minArgLength = ValidName.MIN_ARGUMENT_LENGTH;

    /**
     * Maximum number of characters for an argument name.
     */
    private int maxArgLength = ValidName.MAX_ARGUMENT_LENGTH;

    /**
     * Maximum number of words for an argument name.
     */
    private int maxArgWords = ValidName.MAX_ARGUMENT_WORDS;

    /**
     * Parse a CFScript function declaration to see if any of the arguments names are invalid.
     */
    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement function = (CFFuncDeclStatement) expression;

            for (final CFFunctionParameter argument : function.getFormals()) {
                final int lineNo = argument.getToken().getLine() + context.startLine() - 1;
                final int offset = context.offset() + argument.getOffset();
                checkNameForBugs(context, argument.getName(), context.getFilename(), context.getFunctionName(), lineNo, offset,
                        bugs,argument);
            }
        }
    }

    /**
     * Parse a CF argument tag to see if any of the arguments names are invalid.
     */
    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFARGUMENT)) {
            final int lineNo = context.startLine();
            int offset = context.offset();
            final String name = element.getAttributeValue(CF.NAME);
            if (name != null && name.length() > 0) {
                offset = element.getAttributes().get(CF.NAME).getValueSegment().getBegin();
                checkNameForBugs(context, name, context.getFilename(), context.getFunctionName(), lineNo, offset, bugs,null);
            } else {
                context.addMessage("ARGUMENT_MISSING_NAME", null, this, lineNo, offset);
            }
        }
    }

    /**
     * Parse rule parameters.
     *
     * Parameters include:
     * - minimum length of valid name
     * - maximum length of valid name
     * - maximum number of words in a camel case name
     * - name prefixes to avoid
     * - name postfixes to avoid
     *
     * See @ValidName for defaults.
     */
    private void parseParameters(final ValidName name,CFLintConfiguration configuration) throws ConfigError {
        if (configuration.getParameter(this,MIN_LENGTH) != null) {
            try {
                minArgLength = Integer.parseInt(configuration.getParameter(this,MIN_LENGTH));
                name.setMinLength(minArgLength);
            } catch (final Exception e) {
                throw new ConfigError("Minimum length need to be an integer.");
            }
        }

        if (configuration.getParameter(this,MAX_LENGTH) != null) {
            try {
                maxArgLength = Integer.parseInt(configuration.getParameter(this,MAX_LENGTH));
                name.setMaxLength(maxArgLength);
            } catch (final Exception e) {
                throw new ConfigError("Maximum length need to be an integer.");
            }
        }

        if (configuration.getParameter(this,MAX_WORDS) != null) {
            try {
                maxArgWords = Integer.parseInt(configuration.getParameter(this,MAX_WORDS));
                name.setMaxWords(maxArgWords);
            } catch (final Exception e) {
                throw new ConfigError("Maximum no of words need to be an integer.");
            }
        }

        if (configuration.getParameter(this,NAME_PREFIX) != null) {
            name.setPrefixesToAvoid(configuration.getParameter(this,NAME_PREFIX).split(","));
        }
        if (configuration.getParameter(this,NAME_SUFFIX) != null) {
            name.setSuffixesToAvoid(configuration.getParameter(this,NAME_SUFFIX).split(","));
        }
        if (configuration.getParameter(this,REQUIRED_NAME_PREFIX) != null) {
            name.setRequiredPrefixList(configuration.getParameter(this,REQUIRED_NAME_PREFIX).split(","));
        }
    }

    /**
     * Check if a function's argument name is "bad" is some way.
     *
     * Bad arguments include:
     * - Invalid names (contains an invalid character, ends in a number, not camelCase or does not use underscores)
     * - Names all in upper case
     * - Names that are too short
     * - Names that are too long
     * - Names that are too wordy
     * - Names that look like temporary variables
     * - Names having a prefix or postfix
     */
    public void checkNameForBugs(final Context context, final String argument, final String filename,
            final String functionName, final int line, final int offset, final BugList bugs,final CFFunctionParameter argExpr)  {
        final ValidName name = new ValidName(minArgLength, maxArgLength, maxArgWords);

        try {
            parseParameters(name,context.getConfiguration());
        } catch (ConfigError configError) {
            // Carry on with defaults
        }

        
        if (name.isInvalid(argument,context.getConfiguration().getParameter(this, "case"))) {
            context.addMessage("ARGUMENT_INVALID_NAME", argument, this, line, offset);
        }
        if (name.isUpperCase(argument)) {
            context.addMessage("ARGUMENT_ALLCAPS_NAME", argument, this, line, offset);
        }
        if (name.tooShort(argument)) {
            context.addMessage("ARGUMENT_TOO_SHORT", argument, this, line, offset);
        }
        if (name.tooLong(argument)) {
            context.addMessage("ARGUMENT_TOO_LONG", argument, this, line, offset);
        }
        if (!name.isUpperCase(argument) && name.tooWordy(argument)) {
            context.addMessage("ARGUMENT_TOO_WORDY", argument, this, line, offset);
        }
        if (name.isTemporary(argument)) {
            context.addMessage("ARGUMENT_IS_TEMPORARY", argument, this, line, offset);
        }
        if (name.hasPrefixOrPostfix(argument)) {
            context.addMessage("ARGUMENT_HAS_PREFIX_OR_POSTFIX", argument, this, line, offset);
        }
    }
}
