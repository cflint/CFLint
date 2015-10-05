package com.cflint.plugins.core;

import ro.fortsoft.pf4j.Extension;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Attributes;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import java.util.regex.Pattern;

@Extension
public class VariableNameChecker extends CFLintScannerAdapter {
	final String severity = "WARNING";

	final int MIN_VAR_LENGTH = 3;
	final int MAX_VAR_LENGTH = 20;
	final int WORD_LENGTH = 10;
	final int MAX_VAR_WORDS = 4;
		
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		// TODO	
	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
		// TODO
	}
	
	public void element(final Element element, final Context context, final BugList bugs) {
		String tag = element.getName();
		if (tag.equals("cfset")) { 
			// TODO is there a better way to do this?
			String content = element.getStartTag().getTagContent().toString();
			String parts[] = content.split("=");
			String allVariables = parts[0];
			String filename = context.getFilename();
			int line = element.getSource().getRow(element.getBegin());

			allVariables = allVariables.replace("var ","");
			allVariables = allVariables.replace("["," ");
			allVariables = allVariables.replace("]"," ");
			allVariables = allVariables.replace("."," ");

			String[] subVariables = allVariables.split(" ");

			System.out.println(allVariables);
		
			for (String variable : subVariables) {
				if (Integer.parseInt(variable) == 0) {
					checkNameForBugs(variable, filename, line, bugs);
				}
			}
		}
	}

	public void checkNameForBugs(String variable, String filename, int line, BugList bugs) {
		System.out.println("Var:" + variable);

		if (isValid(variable)) {
			System.out.println("Not valid");
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_INVALID_NAME")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable " + variable + " is not a valid name. Please use CamelCase or underscores.")
				.build());
		}
		if (isUpperCase(variable)) {
			System.out.println("all caps");
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_ALLCAPS_NAME")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable " + variable + " should not be upper case.")
				.build());
		}
		if (tooShort(variable)) {
			System.out.println("tooshort");
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_TOO_SHORT")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable " + variable + " should be longer than " + MIN_VAR_LENGTH + " characters.")
				.build());
		}
		if (tooLong(variable)) {
			System.out.println("toolong");
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_TOO_LONG")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable " + variable + " should be short than " + MAX_VAR_LENGTH + " characters.")
				.build());
		}
		if (!isUpperCase(variable) && tooWordy(variable)) {
			System.out.println("toowordy");
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_TOO_WORDY")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable " + variable + " is too wordy.")
				.build());
		}
		if (isTemporary(variable)) {
			System.out.println("istemporary");
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_IS_TEMPORARY")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable sounds tempory " + variable + " and could be named better.")
				.build());
		}
		if (hasPrefixOrPostfix(variable)) {
			System.out.println("hasprefix");
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_HAS_PREFIX")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable has prefix " + variable + " and could be named better.")
				.build());
		}
	}
 
	protected boolean isValid(String variable) {
		return validChars(variable) && (isCamelCase(variable) || usesUnderscores(variable)) && !endsInNumber(variable);
	}

	protected boolean validChars(String variable) {
		 Pattern valid = Pattern.compile("[A-Za-z0-9_]+");
		 return valid.matcher(variable).matches();
	}

	protected boolean isUpperCase(String variable) {
		return variable.toUpperCase().equals(variable);
	}

	protected boolean isCamelCase(String variable) {
		Pattern valid = Pattern.compile("[a-z0-9]+([A-Z][a-z0-9]+)*");
		return valid.matcher(variable).matches();
	}

	protected boolean usesUnderscores(String variable) {
		Pattern valid = Pattern.compile("[a-z0-9]+[a-z0-9_]+");
		return valid.matcher(variable).matches();
	}

	protected boolean endsInNumber(String variable) {
		String sentence = variable.replaceAll("_", " ");
		sentence = sentence.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
		String[] words = sentence.split(" ");
		String lastWord = words[words.length-1];

		System.out.println(words);
		System.out.println(lastWord);

		if (Integer.parseInt(lastWord) > 0) {
			return true;
		}

		return false;
	}

	protected boolean tooShort(String variable) {
		return variable.length() < MIN_VAR_LENGTH;
	}

	protected boolean tooLong(String variable) {
		return variable.length() > MAX_VAR_LENGTH;
	}

	protected boolean tooWordy(String variable) {
		String[] words = variable.split("[A-Z_]");
		return words.length > MAX_VAR_WORDS;
	}

	protected boolean isTemporary(String variable) {
		String[] wordsToAvoid = {"temp", "tmp", "var", "func", "obj", "object", "str", "struct", "arr", "array"};
		String sentence = variable.replaceAll("_", " ");
		sentence = sentence.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
		String[] words = sentence.split(" ");
		
		for (String badWord : wordsToAvoid) {
			for (String word : words) {
				if (word.toLowerCase().equals(badWord))
				{
					return true;
				}
			}
		}

		return false;
	}

	protected boolean hasPrefixOrPostfix(String variable) {
		String[] namesToAvoid = {"st", "str", "o", "obj", "b", "q", "arr", "a", "this", "my"};
		String sentence = variable.replaceAll("_", " ");
		sentence = sentence.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
		String[] words = sentence.split(" ");
		String firstWord = words[0];
		String lastWord = words[words.length-1];

		for (String badName : namesToAvoid) {
			if (firstWord.toLowerCase().equals(badName))
			{
				return true;
			}
			if (lastWord.toLowerCase().equals(badName))
			{
				return true;
			}
		}

		return false;
	}
}