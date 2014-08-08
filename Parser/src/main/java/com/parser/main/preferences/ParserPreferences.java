package com.parser.main.preferences;

import java.util.Properties;

public class ParserPreferences {
	
	private class defaults {
		/** the dictionaries folder */
		public static final String DICTIONARY_DIR = "";
		/** the cfml dictionary */
		public static final String CF_DICTIONARY = "ColdFusion9";
	}
	
	Properties fProps;
	
	public ParserPreferences(Properties props) {
		fProps = props;
	}
	
	public ParserPreferences() {
		fProps = new Properties();
	}
	
	public void setDictionaryDir(String dictDir) {
		fProps.setProperty(ParserPreferenceConstants.DICTIONARY_DIR, dictDir);
	}
	
	public String getDictionaryDir() {
		return fProps.getProperty(ParserPreferenceConstants.DICTIONARY_DIR, defaults.DICTIONARY_DIR);
	}
	
	public void setCFDictionary(String dict) {
		fProps.setProperty(ParserPreferenceConstants.CF_DICTIONARY, dict);
	}
	
	public String getCFDictionary() {
		return fProps.getProperty(ParserPreferenceConstants.CF_DICTIONARY, defaults.CF_DICTIONARY);
	}
	
}