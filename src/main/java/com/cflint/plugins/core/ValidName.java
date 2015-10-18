
package com.cflint.plugins.core;

import java.util.regex.Pattern;

public class ValidName {
	public static final int MIN_VAR_LENGTH = 3;
	public static final int MAX_VAR_LENGTH = 20;
	public static final int MAX_VAR_WORDS = 4;

	protected int minVarLength = MIN_VAR_LENGTH;
	protected int maxVarLength = MAX_VAR_LENGTH;
	protected int maxVarWords = MAX_VAR_WORDS;

	public ValidName() {
	}

	public ValidName(int minVarLength, int maxVarLength, int maxVarWords) {
		this.minVarLength = minVarLength;
		this.maxVarLength = maxVarLength;
		this.maxVarWords = maxVarWords;
	}

	public boolean isInvalid(String variable) {
		return !validChars(variable) || !(isSameCase(variable) || isCamelCase(variable) || usesUnderscores(variable)) || endsInNumber(variable);
	}

	public boolean validChars(String variable) {
		 Pattern valid = Pattern.compile("[A-Za-z0-9_]+");
		 return valid.matcher(variable).matches();
	}

	public boolean isUpperCase(String variable) {
		return variable.toUpperCase().equals(variable);
	}

	public boolean isSameCase(String variable) {
		return variable.equals(variable.toLowerCase()) || variable.equals(variable.toUpperCase());
	}

	public boolean isCamelCase(String variable) {
		Pattern valid = Pattern.compile("[a-z0-9]+([A-Z][a-z0-9]+)*");
		return valid.matcher(variable).matches();
	}

	public boolean usesUnderscores(String variable) {
		Pattern valid = Pattern.compile("[a-z0-9]+[a-z0-9_]+");
		return valid.matcher(variable).matches();
	}

	public boolean endsInNumber(String variable) {
		char lastLetter = variable.charAt(variable.length() - 1);

		if (Character.isDigit(lastLetter)) {
			return true;
		}

		return false;
	}

	public boolean tooShort(String variable) {
		return variable.length() < minVarLength;
	}

	public boolean tooLong(String variable) {
		return variable.length() > maxVarLength;
	}

	public boolean tooWordy(String variable) {
		String[] words = variable.split("[A-Z_]");
		return words.length > maxVarWords;
	}

	public boolean isTemporary(String variable) {
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

	public boolean hasPrefixOrPostfix(String variable) {
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