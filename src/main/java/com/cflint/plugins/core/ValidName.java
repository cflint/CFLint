
package com.cflint.plugins.core;

import java.util.regex.Pattern;

/**
 * Works out if a name is valid or not.
 */
public class ValidName {
    /**
     * Default minimum length of a variable name.
     */
    public static final int MIN_VAR_LENGTH = 3;

    /**
     * Default maximum length of a variable name.
     */
    public static final int MAX_VAR_LENGTH = 20;

    /**
     * Default maximum no of words in a variable name.
     */
    public static final int MAX_VAR_WORDS = 4;

    /**
     * Default minimum length of an argument name.
     */
    public static final int MIN_ARGUMENT_LENGTH = 3;

    /**
     * Default maximum length of an argument name.
     */
    public static final int MAX_ARGUMENT_LENGTH = 20;

    /**
     * Default maximum no of words in an argument name.
     */
    public static final int MAX_ARGUMENT_WORDS = 4;

    /**
     * Default minimum length of a method name.
     */
    public static final int MIN_METHOD_LENGTH = 3;

    /**
     * Default maximum length of a method name.
     */
    public static final int MAX_METHOD_LENGTH = 25;

    /**
     * Default maximum no of words in a method name.
     */
    public static final int MAX_METHOD_WORDS = 5;

    /**
     * Default minimum length of a component name.
     */
    public static final int MIN_COMPONENT_LENGTH = 3;

    /**
     * Default maximum length of a component name.
     */
    public static final int MAX_COMPONENT_LENGTH = 20;

    /**
     * Default maximum no of words in a component name.
     */
    public static final int MAX_COMPONENT_WORDS = 3;

    /**
     * Minimum length of a name.
     */
    protected int minLength = MIN_VAR_LENGTH;

    /**
     * Maximum length of a name.
     */
    protected int maxLength = MAX_VAR_LENGTH;

    /**
     * Maximum no of words in a words in a name.
     */
    protected int maxWords = MAX_VAR_WORDS;

    /**
     * Default prefixes to avoid.
     */
    private static final String[] DEFAULT_PREFIXES_TO_AVOID = { "s", "st", "str", "o", "obj", "b", "q", "a", "arr", "this", "my", "stu" };

    /**
     * Prefixes to avoid.
     */
    private String[] prefixesToAvoid = DEFAULT_PREFIXES_TO_AVOID;

    /**
     * Suffixes to avoid.
     */
    private String[] suffixesToAvoid = DEFAULT_PREFIXES_TO_AVOID;

    /**
     * Required prefixes.
     */
    private String[] requiredPrefixes = null;

    /**
     * Set up the minimum and maximum lengths.
     *
     * @param minLength minimum length of name.
     * @param maxLength maximum length of name.
     * @param maxWords maximum no of words in a name.
     */
    public ValidName(final int minLength, final int maxLength, final int maxWords) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.maxWords = maxWords;
    }

    /**
     * Is a name valid?.
     *
     * @param name name of variable.
     * @return true if name is valid or false if it is not.
     */
    public boolean isInvalid(final String name,final String caseType) {
    	if(name == null) {
    		return false;
    	}
        boolean caseOk = ("PascalCase".equalsIgnoreCase(caseType)? isPascalCase(name) : isCamelCase(name));
        return !validChars(name) //|| endsInNumber(name)
                || !(isSameCase(name) || caseOk || usesUnderscores(name));
    }

    /**
     * Is a component name valid?.
     *
     * @param name name of variable.
     * @return true if name is valid or false if it is not.
     */
    public boolean isInvalidComponent(final String name,final String caseType) {
    	if(name == null) {
    		return false;
    	}
        boolean caseOk = ("PascalCase".equalsIgnoreCase(caseType)? isPascalCase(name) : isCamelCase(name));
        return !validChars(name) || endsInNumber(name)
                || !(isUpperCase(name) || isPascalCase(name) || usesUnderscores(name));
    }

    /**
     * Valid characters in a name.
     *
     * @param name name of variable.
     * @return true if the name consists of valid chartacters, false if it does not.
     */
    public boolean validChars(final String name) {
    	if(name == null) {
    		return true;
    	}
        final Pattern valid = Pattern.compile("^[A-Za-z0-9_]+$");
        return valid.matcher(name).matches();
    }

    /**
     * Is a name all upper case?.
     *
     * @param name name of variable.
     * @return true if the name is all upppercase, false if it is not.
     */
    public boolean isUpperCase(final String name) {
    	if(name == null) {
    		return false;
    	}
        return name.toUpperCase().equals(name);
    }

    /**
     * Is a name all the same case.
     *
     * @param name name of variable.
     * @return true if the name is all upppercase, false if it is not.
     */
    public boolean isSameCase(final String name) {
    	if(name == null) {
    		return false;
    	}
        return name.equals(name.toLowerCase()) || name.equals(name.toUpperCase());
    }

    /**
     * Is a name camel case starting wth a lower case letter?.
     *
     * @param name name of variable.
     * @return true if the camel case, false if it is not.
     */
    public boolean isCamelCase(final String name) {
    	if(name == null) {
    		return false;
    	}
        // [A-Z0-9]{2,5} catch names like productID, phone4G, requestURL etc etc
        final Pattern valid = Pattern.compile("^[a-z0-9]+([A-Z]{1,5}[a-z0-9]+)*([A-Z0-9]{2,5}){0,1}[A-Z]?$");
        return valid.matcher(name).matches();
    }

    /**
     * Is a name pascal case starting wth a upper case letter?.
     *
     * @param name name of variable.
     * @return true if the camel case, false if it is not.
     */
    public boolean isPascalCase(final String name) {
    	if(name == null) {
    		return false;
    	}
        final Pattern valid = Pattern.compile("^([A-Z]{1,5}[a-z0-9]+)+([A-Z0-9]{2,5}){0,1}[A-Z]?$");
        return valid.matcher(name).matches();
    }

    /**
     * Does the name include an underscore?.
     *
     * @param name name of variable.
     * @return true if name contains an underscore, false if it does not.
     */
    public boolean usesUnderscores(final String name) {
    	if(name == null) {
    		return false;
    	}
       return name.indexOf('_') != -1;
    }

    /**
     * Does the name end in one or more digits?
     *
     * @param name name of variable.
     * @return true if name ends in one or more digits, false if it does not.
     */
    public boolean endsInNumber(final String name) {
    	if(name == null) {
    		return false;
    	}
        final char lastLetter = name.charAt(name.length() - 1);
        return Character.isDigit(lastLetter);
    }

    /**
     * Is the name too short?.
     *
     * @param name name of variable.
     * @return true if name is too short, false if it does not.
     */
    public boolean tooShort(final String name) {
    	if(name == null) {
    		return false;
    	}
        return name.length() < minLength;
    }

    /**
     * Is the name too long?.
     *
     * @param name name of variable.
     * @return true if name is too long, false if it is not.
     */
    public boolean tooLong(final String name) {
    	if(name == null) {
    		return false;
    	}
        return name.length() > maxLength;
    }

    /**
     * Is the name too wordy?.
     *
     * @param name name of variable.
     * @return true if name contains too many words, false if it does not.
     */
    public boolean tooWordy(final String name) {
    	if(name == null) {
    		return false;
    	}
       final String[] words = name.split("[A-Z_]+");
        int count = 0;

        for (int i = 0; i < words.length; i++) {
            if (words[i] != null && !words[i].equals("")) {
                count++;
            }
        }

        return count > maxWords;
    }

    /**
     * Is the name temporary?.
     *
     * @param name name of variable.
     * @return true if the name is temporary, false if it does not.
     */
    public boolean isTemporary(final String name) {
    	if(name == null) {
    		return false;
    	}
        final String[] wordsToAvoid = { "temp", "tmp", "var", "func", "obj", "object", "bool", "struct", "string",
                "array", "comp" };
        String sentence = name.replaceAll("_", " ");
        sentence = sentence.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
        final String[] words = sentence.split(" ");

        for (final String badWord : wordsToAvoid) {
            for (final String word : words) {
                if (word.equalsIgnoreCase(badWord)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Does the name have a prefix or postfix?
     *
     * @param name  name of variable.
     * @return  true if the name has a prefix or postfix, false if it does not.
     */
    public boolean hasPrefixOrPostfix(final String name) {
    	if(name == null) {
    		return false;
    	}
        String sentence = name.replaceAll("_", " ");
        sentence = sentence.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");

        if (sentence.trim().length() == 0) {
        	return false;
        }
        
        final String[] words = sentence.split(" ");
        final String firstWord = words[0];
        final String lastWord = words[words.length - 1];

        if (words.length > 1 && shouldAvoidWord(firstWord, lastWord)) {
            return true;
        }
        if (requiredPrefixes != null) {
            for (final String badName : requiredPrefixes) {
                if (firstWord.equalsIgnoreCase(badName)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Should avoid first or last word in a name?.
     *
     * @param firstWord first word of a name.
     * @param lastWord  last word of a name.
     * @return true if first or last word is to be avoided in a name.
     */

    private boolean shouldAvoidWord(final String firstWord, final String lastWord) {
        for (final String badName : prefixesToAvoid) {
            if (firstWord.equalsIgnoreCase(badName)) {
                return true;
            }
        }
        for (final String badName : suffixesToAvoid) {
            if (lastWord.equalsIgnoreCase(badName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set the prefixes to avoid.
     *
     * @param prefixesToAvoid prefixes to avoid.
     */
    public void setPrefixesToAvoid(final String[] prefixesToAvoid) {
        this.prefixesToAvoid = prefixesToAvoid;
    }

    /**
     *  Set the suffixes to avoid.
     *
     * @param suffixesToAvoid suffixes to avoid.
     */
    public void setSuffixesToAvoid(final String[] suffixesToAvoid) {
        this.suffixesToAvoid = suffixesToAvoid;
    }

    /**
     * Set the required prefixes.
     *
     * @param prefixes
     */
    public void setRequiredPrefixList(final String[] prefixes) {
        this.requiredPrefixes = prefixes;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setMaxWords(int maxWords) {
        this.maxWords = maxWords;
    }
}
