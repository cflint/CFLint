package com.cflint.plugins.core;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.script.CFCompDeclStatement;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;
import ro.fortsoft.pf4j.Extension;

@Extension
public class ComponentNameChecker extends CFLintScannerAdapter {
	public static final String COMPONENT_NAME = "Component name ";
	final String severity = "INFO";

	@Override
	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		if (expression instanceof CFCompDeclStatement) {
			String name = context.getFilename().replace(".cfc","");
			checkNameForBugs(actualFileName(name), context.getFilename(), bugs);
		}
	}

	@Override
	public void element(final Element element, final Context context, final BugList bugs) {
		if (element.getName().equals("cfcomponent")) {
			String name = context.getFilename().replace(".cfc","");
			checkNameForBugs(actualFileName(name), context.getFilename(), bugs);
		}
	}

	private String actualFileName(final String fileName) {
		String actualFileName = fileName;
		String separator = System.getProperty("file.separator");
    	int seperatorPosition = fileName.lastIndexOf(separator);

    	if (seperatorPosition >= 0) {
    		actualFileName = fileName.substring(seperatorPosition + 1);
    	}

    	return actualFileName;
	}

	public void checkNameForBugs(String component, String filename, BugList bugs) {
		int minComponentLength = ValidName.MIN_COMPONENT_LENGTH;
		int maxComponentLength = ValidName.MAX_COMPONENT_LENGTH;
		int maxComponentWords = ValidName.MAX_COMPONENT_WORDS;
		int line = 1;

		if (getParameter("MinLength") != null) {
			try {
				minComponentLength = Integer.parseInt(getParameter("MinLength"));
			} catch(Exception e) {}
		}

		if (getParameter("MaxLength") != null) {
			try {
				maxComponentLength = Integer.parseInt(getParameter("MaxLength"));
			} catch(Exception e) {}
		}

		if (getParameter("MaxWords") != null) {
			try {
				maxComponentWords = Integer.parseInt(getParameter("MaxWords"));
			} catch(Exception e) {}
		}

		ValidName name = new ValidName(minComponentLength, maxComponentLength, maxComponentWords);

		// TODO check package name as well?

		if (name.isInvalidComponent(component)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("COMPONENT_INVALID_NAME")
				.setSeverity(severity).setFilename(filename)
				.setMessage(COMPONENT_NAME + component + " is not a valid name. Please use CamelCase and start with a capital letter.")
				.build());
		}
		if (name.isUpperCase(component)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("COMPONENT_ALLCAPS_NAME")
				.setSeverity(severity).setFilename(filename)
				.setMessage(COMPONENT_NAME + component + " should not be all upper case.")
				.build());
		}
		if (name.tooShort(component)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("COMPONENT_TOO_SHORT")
				.setSeverity(severity).setFilename(filename)
				.setMessage(COMPONENT_NAME + component + " should be longer than " + minComponentLength + " characters.")
				.build());
		}
		if (name.tooLong(component)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("COMPONENT_TOO_LONG")
				.setSeverity(severity).setFilename(filename)
				.setMessage(COMPONENT_NAME + component + " should be shorter than " + maxComponentLength + " characters.")
				.build());
		}
		if (!name.isUpperCase(component) && name.tooWordy(component)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("COMPONENT_TOO_WORDY")
				.setSeverity(severity).setFilename(filename)
				.setMessage(COMPONENT_NAME + component + " is too wordy, can you think of a more concise name?")
				.build());
		}
		if (name.isTemporary(component)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("COMPONENT_IS_TEMPORARY")
				.setSeverity(severity).setFilename(filename)
				.setMessage(COMPONENT_NAME + component + " could be named better.")
				.build());
		}
		if (name.hasPrefixOrPostfix(component)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("COMPONENT_HAS_PREFIX_OR_POSTFIX")
				.setSeverity(severity).setFilename(filename)
				.setMessage(COMPONENT_NAME + component + " has prefix or postfix and could be named better.")
				.build());
		}
	}
}
