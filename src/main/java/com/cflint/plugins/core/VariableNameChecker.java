package com.cflint.plugins.core;

import ro.fortsoft.pf4j.Extension;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Attributes;
import cfml.parsing.cfscript.CFAssignmentExpression;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFullVarExpression;
import cfml.parsing.cfscript.CFIdentifier;
import cfml.parsing.cfscript.CFVarDeclExpression;

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
		if (expression instanceof CFVarDeclExpression) {
			final CFVarDeclExpression cfVarDeclExpression = (CFVarDeclExpression)expression;
			int lineNo = expression.getLine() + context.startLine() - 1;
			checkNameForBugs(cfVarDeclExpression.getName(), context.getFilename(), lineNo, bugs);
		}
		else if (expression instanceof CFFullVarExpression) {
			final CFFullVarExpression cfFullVarExpression = (CFFullVarExpression)expression;
			for(final CFExpression subexpression : cfFullVarExpression.getExpressions()){
				expression(subexpression,context,bugs);
			}
		}
		else if (expression instanceof CFIdentifier) {
			String varName = ((CFIdentifier) expression).getName();
			int lineNo = ((CFIdentifier) expression).getLine() + context.startLine() - 1;
			
			checkNameForBugs(varName, context.getFilename(), lineNo, bugs);
		}
	}

	public void checkNameForBugs(String variable, String filename, int line, BugList bugs) {
		if (isInvalid(variable)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_INVALID_NAME")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable " + variable + " is not a valid name. Please use CamelCase or underscores.")
				.build());
		}
		if (isUpperCase(variable)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_ALLCAPS_NAME")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable " + variable + " should not be upper case.")
				.build());
		}
		if (tooShort(variable)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_TOO_SHORT")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable " + variable + " should be longer than " + MIN_VAR_LENGTH + " characters.")
				.build());
		}
		if (tooLong(variable)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_TOO_LONG")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable " + variable + " should be shorter than " + MAX_VAR_LENGTH + " characters.")
				.build());
		}
		if (!isUpperCase(variable) && tooWordy(variable)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_TOO_WORDY")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable " + variable + " is too wordy, can you think of a more concise name?")
				.build());
		}
		if (isTemporary(variable)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_IS_TEMPORARY")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Temporary variable " + variable + " could be named better.")
				.build());
		}
		if (hasPrefixOrPostfix(variable)) {
			bugs.add(new BugInfo.BugInfoBuilder().setLine(line).setMessageCode("VAR_HAS_PREFIX_OR_POSTFIX")
				.setSeverity(severity).setFilename(filename)
				.setMessage("Variable has prefix or postfix " + variable + " and could be named better.")
				.build());
		}
	}
 
	protected boolean isInvalid(String variable) {
		return !validChars(variable) || !(isSameCase(variable) || isCamelCase(variable) || usesUnderscores(variable)) || endsInNumber(variable);
	}

	protected boolean validChars(String variable) {
		 Pattern valid = Pattern.compile("[A-Za-z0-9_]+");
		 return valid.matcher(variable).matches();
	}

	protected boolean isUpperCase(String variable) {
		return variable.toUpperCase().equals(variable);
	}

	protected boolean isSameCase(String variable) {
		return variable.equals(variable.toLowerCase()) || variable.equals(variable.toUpperCase());
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
		char lastLetter = variable.charAt(variable.length() - 1);

		if (Character.isDigit(lastLetter)) {
			return true;
		}

		return false;
	}

	protected boolean tooShort(String variable) {
		
		int minVarLength = MIN_VAR_LENGTH;
		if(getParameter("MinLength") !=null){
			try {
				minVarLength= Integer.parseInt(getParameter("MinLength"));
			}catch(Exception e){}
		}
		return variable.length() < minVarLength;
	}

	protected boolean tooLong(String variable) {
		return variable.length() > MAX_VAR_LENGTH;
	}

	protected boolean tooWordy(String variable) {
		String[] words = variable.split("[A-Z_]");
		return words.length > MAX_VAR_WORDS;
	}

	protected boolean isTemporary(String variable) {
		String[] wordsToAvoid = {"temp", "tmp", "var", "func", "obj", "object", "bool", "struct", "string", "array"};
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
		String[] namesToAvoid = {"s", "st", "str", "o", "obj", "b", "q", "a", "arr", "this", "my"};
		String sentence = variable.replaceAll("_", " ");
		sentence = sentence.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
		String[] words = sentence.split(" ");
		String firstWord = words[0];
		String lastWord = words[words.length-1];

		if (words.length > 1) {
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
		}

		return false;
	}
}