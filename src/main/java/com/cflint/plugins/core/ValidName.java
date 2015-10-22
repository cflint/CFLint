
package com.cflint.plugins.core;

import java.util.regex.Pattern;

public class ValidName {
	public static final int MIN_VAR_LENGTH = 3;
	public static final int MAX_VAR_LENGTH = 20;
	public static final int MAX_VAR_WORDS = 4;

	public static final int MIN_METHOD_LENGTH = 3;
	public static final int MAX_METHOD_LENGTH = 25;
	public static final int MAX_METHOD_WORDS = 5;

	public static final int MIN_COMPONENT_LENGTH = 3;
	public static final int MAX_COMPONENT_LENGTH = 15;
	public static final int MAX_COMPONENT_WORDS = 3;

	protected int minLength = MIN_VAR_LENGTH;
	protected int maxLength = MAX_VAR_LENGTH;
	protected int maxWords = MAX_VAR_WORDS;

	public ValidName() {
	}

	public ValidName(int minLength, int maxLength, int maxWords) {
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.maxWords = maxWords;
	}

	public boolean isInvalid(String name) {
		return !validChars(name) || endsInNumber(name) || !(isSameCase(name) || isCamelCaseLower(name) || usesUnderscores(name));
	}

	public boolean isInvalidComponent(String name) {
		return !validChars(name) || endsInNumber(name) || !(isSameCase(name) || isCamelCaseUpper(name) || usesUnderscores(name));
	}

	public boolean validChars(String name) {
		 Pattern valid = Pattern.compile("^[A-Za-z0-9_]+$");
		 return valid.matcher(name).matches();
	}

	public boolean isUpperCase(String name) {
		return name.toUpperCase().equals(name);
	}

	public boolean isSameCase(String name) {
		return name.equals(name.toLowerCase()) || name.equals(name.toUpperCase());
	}

	public boolean isCamelCaseLower(String name) {
		// [A-Z0-9]{2,5} catch names like productID, phone4G, requestURL etc etc
		Pattern valid = Pattern.compile("^[a-z0-9]+([A-Z]{1,5}[a-z0-9]+)*([A-Z0-9]{2,5}){0,1}$");
		return valid.matcher(name).matches();
	}

	public boolean isCamelCaseUpper(String name) {
		Pattern valid = Pattern.compile("^([A-Z]{1,5}[a-z0-9]+)+([A-Z0-9]{2,5}){0,1}$");
		return valid.matcher(name).matches();
	}

	public boolean usesUnderscores(String name) {
		return name.indexOf('_') != -1;
	}

	public boolean endsInNumber(String name) {
		char lastLetter = name.charAt(name.length() - 1);

		if (Character.isDigit(lastLetter)) {
			return true;
		}

		return false;
	}

	public boolean tooShort(String name) {
		return name.length() < minLength;
	}

	public boolean tooLong(String name) {
		return name.length() > maxLength;
	}

	public boolean tooWordy(String name) {
		String[] words = name.split("[A-Z_]+");
		int count = 0;

		for (int i = 0; i < words.length; i++) {
			if (words[i] != null && !words[i].equals("")) {
				count++;
			}
		}

		return count > maxWords;
	}

	public boolean isTemporary(String name) {
		String[] wordsToAvoid = {"temp", "tmp", "var", "func", "obj", "object", "bool", "struct", "string", "array", "comp"};
		String sentence = name.replaceAll("_", " ");
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

	public boolean hasPrefixOrPostfix(String name) {
		String[] namesToAvoid = {"s", "st", "str", "o", "obj", "b", "q", "a", "arr", "this", "my"};
		String sentence = name.replaceAll("_", " ");
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