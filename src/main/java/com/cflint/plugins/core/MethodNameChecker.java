package com.cflint.plugins.core;

import com.cflint.CF;
import com.cflint.config.CFLintConfiguration;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFFuncDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

/**
 * Check if a function or method name is valid.
 */
@Extension
public class MethodNameChecker extends CFLintScannerAdapter {

    /**
     * Minimum number of characters for a function name.
     */
    private int minMethodLength = ValidName.MIN_METHOD_LENGTH;

    /**
     * Maximum number of characters for a function name.
     */
    private int maxMethodLength = ValidName.MAX_METHOD_LENGTH;

    /**
     * Minimum number of words in a function name.
     */
    private int maxMethodWords = ValidName.MAX_METHOD_WORDS;


    /**
     * Parse a CFScript function declaration to see if the function name is invalid.
     */
    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFFuncDeclStatement) {
            final CFFuncDeclStatement method = (CFFuncDeclStatement) expression;
            final int lineNo = method.getLine() + context.startLine() - 1;
            final int offset = method.getOffset() + context.offset();
            checkNameForBugs(context, lineNo, offset);
        }
    }

    /**
     * Parse CF function tag declaration to see if the function name is invalid.
     */
    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFFUNCTION)) {
            final int lineNo = element.getSource().getRow(element.getBegin());
            checkNameForBugs(context, lineNo, element.getBegin());
        }
    }

    /**
     * Parse rule parameters.
     *
     * Parameters include:
     * - minimum length of valid name
     * - maximum length of valid name
     * - maximum number of words in a camel case name
     *
     * See @ValidName for defaults.
     */
    private void parseParameters(CFLintConfiguration configuration)  throws ConfigError {
        if (configuration.getParameter(this,"minLength") != null) {
            try {
                minMethodLength = Integer.parseInt(configuration.getParameter(this,"minLength"));
            } catch (final Exception e) {
                throw new ConfigError("Minimum length need to be an integer.");
            }
        }

        if (configuration.getParameter(this,"maxLength") != null) {
            try {
                maxMethodLength = Integer.parseInt(configuration.getParameter(this,"maxLength"));
            } catch (final Exception e) {
                throw new ConfigError("Maximum length need to be an integer.");
            }
        }

        if (configuration.getParameter(this,"maxWords") != null) {
            try {
                maxMethodWords = Integer.parseInt(configuration.getParameter(this,"maxWords"));
            } catch (final Exception e) {
                throw new ConfigError("Maximum no of words need to be an integer.");
            }
        }
    }

    /**
     * Check if an function name is "bad" is some way.
     *
     * Bad argument name include:
     * - Invalid names (contains an invalid character, ends in a number, not camelCase or does not use underscores)
     * - Names all in upper case
     * - Names that are too short
     * - Names that are too long
     * - Names that are too wordy
     * - Names that look like temporary variables
     * - Names having a prefix or postfix
     */
    public void checkNameForBugs(final Context context, final int line, final int offset) {
        final String method = context.getFunctionName();

        try {
            parseParameters(context.getConfiguration());
        } catch (ConfigError configError) {
            // Carry on with defaults
        }

        final ValidName name = new ValidName(minMethodLength, maxMethodLength, maxMethodWords);

        if (name.isInvalid(method,context.getConfiguration().getParameter(this, "case"))) {
            context.addMessage("METHOD_INVALID_NAME", null, line, offset);
        }
        if (name.isUpperCase(method)) {
            context.addMessage("METHOD_ALLCAPS_NAME", null, line, offset);
        }
        if (name.tooShort(method)) {
            context.addMessage("METHOD_TOO_SHORT", null, line, offset);
        }
        if (name.tooLong(method)) {
            context.addMessage("METHOD_TOO_LONG", null, line, offset);
        }
        if (!name.isUpperCase(method) && name.tooWordy(method)) {
            context.addMessage("METHOD_TOO_WORDY", null, line, offset);
        }
        if (name.isTemporary(method)) {
            context.addMessage("METHOD_IS_TEMPORARY", null, line, offset);
        }
        if (name.hasPrefixOrPostfix(method)) {
            context.addMessage("METHOD_HAS_PREFIX_OR_POSTFIX", null, line, offset);
        }
    }
}
