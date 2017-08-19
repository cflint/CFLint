package com.cflint.plugins.core;

import com.cflint.CF;
import com.cflint.Levels;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFCompDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class ComponentNameChecker extends CFLintScannerAdapter {
    private static final String COMPONENT_NAME = "Component name ";
    private final Levels severity = Levels.INFO;

    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        if (expression instanceof CFCompDeclStatement) {
            final String name = context.getFilename().replace(".cfc", "");
            checkNameForBugs(context, actualFileName(name), context.getFilename(), bugs);
        }
    }

    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        if (element.getName().equals(CF.CFCOMPONENT)) {
            final String name = context.getFilename().replace(".cfc", "");
            checkNameForBugs(context, actualFileName(name), context.getFilename(), bugs);
        }
    }

    private String actualFileName(final String fileName) {
        String actualFileName = fileName;
        final String separator = System.getProperty("file.separator");
        final int seperatorPosition = fileName.lastIndexOf(separator);

        if (seperatorPosition >= 0) {
            actualFileName = fileName.substring(seperatorPosition + 1);
        }

        return actualFileName;
    }

    public void checkNameForBugs(final Context context, final String component, final String filename,
            final BugList bugs) {
        int minComponentLength = ValidName.MIN_COMPONENT_LENGTH;
        int maxComponentLength = ValidName.MAX_COMPONENT_LENGTH;
        int maxComponentWords = ValidName.MAX_COMPONENT_WORDS;
        final int line = 1;

        if (getParameter("MinLength") != null) {
            try {
                minComponentLength = Integer.parseInt(getParameter("MinLength"));
            } catch (final Exception e) {
            }
        }

        if (getParameter("MaxLength") != null) {
            try {
                maxComponentLength = Integer.parseInt(getParameter("MaxLength"));
            } catch (final Exception e) {
            }
        }

        if (getParameter("MaxWords") != null) {
            try {
                maxComponentWords = Integer.parseInt(getParameter("MaxWords"));
            } catch (final Exception e) {
            }
        }

        final ValidName name = new ValidName(minComponentLength, maxComponentLength, maxComponentWords);

        // TODO check package name as well?

        if (name.isInvalidComponent(component)) {
            context.addMessage("COMPONENT_INVALID_NAME", null, this, line);
        }
        if (name.isUpperCase(component)) {
            context.addMessage("COMPONENT_ALLCAPS_NAME", null, this, line);
        }
        if (name.tooShort(component)) {
            context.addMessage("COMPONENT_TOO_SHORT", null, this, line);
        }
        if (name.tooLong(component)) {
            context.addMessage("COMPONENT_TOO_LONG", null, this, line);
        }
        if (!name.isUpperCase(component) && name.tooWordy(component)) {
            context.addMessage("COMPONENT_TOO_WORDY", null, this, line);
        }
        if (name.isTemporary(component)) {
            context.addMessage("COMPONENT_IS_TEMPORARY", null, this, line);
        }
        if (name.hasPrefixOrPostfix(component)) {
            context.addMessage("COMPONENT_HAS_PREFIX_OR_POSTFIX", null, this, line);
        }
    }
}