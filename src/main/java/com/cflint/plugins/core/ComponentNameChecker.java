package com.cflint.plugins.core;

import com.cflint.CF;
import com.cflint.config.CFLintConfiguration;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFCompDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

/**
 * Check is a component name is valid.
 */
@Extension
public class ComponentNameChecker extends CFLintScannerAdapter {
    /**
     * Minimum number of characters for an component name.
     */
    private int minComponentLength = ValidName.MIN_COMPONENT_LENGTH;

    /**
     * Maximum number of characters for an component name.
     */
    private int maxComponentLength = ValidName.MAX_COMPONENT_LENGTH;

    /**
     * Minimum number of words in an component name.
     */
    private int maxComponentWords = ValidName.MAX_COMPONENT_WORDS;

    /**
     * Parse a CFScript component declaration to see if the component name is invalid.
     */
    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFCompDeclStatement) {
            final String name = context.getFilename().replace(".cfc", "");
            checkNameForBugs(context, actualFileName(name), context.getFilename(), context.startLine(), ((CFCompDeclStatement) expression).getOffset(), bugs);
        }
    }

    /**
     * Parse CF component tag declaration to see if the component name is invalid.
     */
    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFCOMPONENT)) {
            final String name = context.getFilename().replace(".cfc", "");
            checkNameForBugs(context, actualFileName(name), context.getFilename(), context.startLine(), element.getBegin(), bugs);
        }
    }

    /**
     * Get the file name from the path.
     *
     * @param fileName path of file name.
     * @return file name.
     */
    private String actualFileName(final String fileName) {
        String actualFileName = fileName;
        final String separator = System.getProperty("file.separator");
        final int seperatorPosition = fileName.lastIndexOf(separator);

        if (seperatorPosition >= 0) {
            actualFileName = fileName.substring(seperatorPosition + 1);
        }

        return actualFileName;
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
    private void parseParameters(CFLintConfiguration configuration) throws ConfigError {
        if (configuration.getParameter(this,"minLength") != null) {
            try {
                minComponentLength = Integer.parseInt(configuration.getParameter(this,"minLength"));
            } catch (final Exception e) {
                throw new ConfigError("Minimum length need to be an integer.");
            }
        }

        if (configuration.getParameter(this,"maxLength") != null) {
            try {
                maxComponentLength = Integer.parseInt(configuration.getParameter(this,"maxLength"));
            } catch (final Exception e) {
                throw new ConfigError("Maximum length need to be an integer.");
            }
        }

        if (configuration.getParameter(this,"maxWords") != null) {
            try {
                maxComponentWords = Integer.parseInt(configuration.getParameter(this,"maxWords"));
            } catch (final Exception e) {
                throw new ConfigError("Maximum no of words need to be an integer.");
            }
        }
    }

    /**
     * Check if an argument name is "bad" is some way.
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
    public void checkNameForBugs(final Context context, final String component, final String filename,
            final int line, final int offset, final BugList bugs) {

        try {
            parseParameters(context.getConfiguration());
        } catch (ConfigError configError) {
            // Carry on with defaults
        }
        final ValidName name = new ValidName(minComponentLength, maxComponentLength, maxComponentWords);

        // TODO check package name as well?

        String caseTypeParm = context.getConfiguration().getParameter(this, "case");
        if (name.isInvalidComponent(component,caseTypeParm==null?"PascalCase":caseTypeParm)) {
            context.addMessage("COMPONENT_INVALID_NAME", null, this, line, offset);
        }
        if (name.isUpperCase(component)) {
            context.addMessage("COMPONENT_ALLCAPS_NAME", null, this, line, offset);
        }
        if (name.tooShort(component)) {
            context.addMessage("COMPONENT_TOO_SHORT", null, this, line, offset);
        }
        if (name.tooLong(component)) {
            context.addMessage("COMPONENT_TOO_LONG", null, this, line, offset);
        }
        if (!name.isUpperCase(component) && name.tooWordy(component)) {
            context.addMessage("COMPONENT_TOO_WORDY", null, this, line, offset);
        }
        if (name.isTemporary(component)) {
            context.addMessage("COMPONENT_IS_TEMPORARY", null, this, line, offset);
        }
        if (name.hasPrefixOrPostfix(component)) {
            context.addMessage("COMPONENT_HAS_PREFIX_OR_POSTFIX", null, this, line, offset);
        }
    }
}
