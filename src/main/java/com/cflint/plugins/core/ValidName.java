
package com.cflint.plugins.core;

import java.util.regex.Pattern;

public class ValidName {
    public static final int MIN_VAR_LENGTH = 3;
    public static final int MAX_VAR_LENGTH = 20;
    public static final int MAX_VAR_WORDS = 4;

    public static final int MIN_ARGUMENT_LENGTH = 3;
    public static final int MAX_ARGUMENT_LENGTH = 20;
    public static final int MAX_ARGUMENT_WORDS = 4;

    public static final int MIN_METHOD_LENGTH = 3;
    public static final int MAX_METHOD_LENGTH = 25;
    public static final int MAX_METHOD_WORDS = 5;

    public static final int MIN_COMPONENT_LENGTH = 3;
    public static final int MAX_COMPONENT_LENGTH = 20;
    public static final int MAX_COMPONENT_WORDS = 3;

    protected int minLength = MIN_VAR_LENGTH;
    protected int maxLength = MAX_VAR_LENGTH;
    protected int maxWords = MAX_VAR_WORDS;

    public ValidName() {
    }

    public ValidName(final int minLength, final int maxLength, final int maxWords) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.maxWords = maxWords;
    }

    public boolean isInvalid(final String name) {
        return !validChars(name) || endsInNumber(name)
                || !(isSameCase(name) || isCamelCaseLower(name) || usesUnderscores(name));
    }

    public boolean isInvalidComponent(final String name) {
        return !validChars(name) || endsInNumber(name)
                || !(isUpperCase(name) || isCamelCaseUpper(name) || usesUnderscores(name));
    }

    public boolean validChars(final String name) {
        final Pattern valid = Pattern.compile("^[A-Za-z0-9_]+$");
        return valid.matcher(name).matches();
    }

    public boolean isUpperCase(final String name) {
        return name.toUpperCase().equals(name);
    }

    public boolean isSameCase(final String name) {
        return name.equals(name.toLowerCase()) || name.equals(name.toUpperCase());
    }

    public boolean isCamelCaseLower(final String name) {
        // [A-Z0-9]{2,5} catch names like productID, phone4G, requestURL etc etc
        final Pattern valid = Pattern.compile("^[a-z0-9]+([A-Z]{1,5}[a-z0-9]+)*([A-Z0-9]{2,5}){0,1}$");
        return valid.matcher(name).matches();
    }

    public boolean isCamelCaseUpper(final String name) {
        final Pattern valid = Pattern.compile("^([A-Z]{1,5}[a-z0-9]+)+([A-Z0-9]{2,5}){0,1}$");
        return valid.matcher(name).matches();
    }

    public boolean usesUnderscores(final String name) {
        return name.indexOf('_') != -1;
    }

    public boolean endsInNumber(final String name) {
        final char lastLetter = name.charAt(name.length() - 1);

        if (Character.isDigit(lastLetter)) {
            return true;
        }

        return false;
    }

    public boolean tooShort(final String name) {
    	System.out.println("============== Variable to Short ==============");
    	System.out.println(name);
    	System.out.println(minLength);
    	System.out.println("================================================");
        return name.length() < minLength;
    }

    public boolean tooLong(final String name) {
        return name.length() > maxLength;
    }

    public boolean tooWordy(final String name) {
        final String[] words = name.split("[A-Z_]+");
        int count = 0;

        for (int i = 0; i < words.length; i++) {
            if (words[i] != null && !words[i].equals("")) {
                count++;
            }
        }

        return count > maxWords;
    }

    public boolean isTemporary(final String name) {
        final String[] wordsToAvoid = { "temp", "tmp", "var", "func", "obj", "object", "bool", "struct", "string",
                "array", "comp" };
        String sentence = name.replaceAll("_", " ");
        sentence = sentence.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
        final String[] words = sentence.split(" ");

        for (final String badWord : wordsToAvoid) {
            for (final String word : words) {
                if (word.toLowerCase().equals(badWord)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasPrefixOrPostfix(final String name) {
        final String[] namesToAvoid = { "s", "st", "str", "o", "obj", "b", "q", "a", "arr", "this", "my", "stu" };
        String sentence = name.replaceAll("_", " ");
        sentence = sentence.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
        final String[] words = sentence.split(" ");
        final String firstWord = words[0];
        final String lastWord = words[words.length - 1];

        if (words.length > 1) {
            for (final String badName : namesToAvoid) {
                if (firstWord.toLowerCase().equals(badName)) {
                    return true;
                }
                if (lastWord.toLowerCase().equals(badName)) {
                    return true;
                }
            }
        }

        return false;
    }
}