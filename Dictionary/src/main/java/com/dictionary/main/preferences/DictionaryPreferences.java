package com.dictionary.main.preferences;

import java.util.Properties;

public class DictionaryPreferences {
	
	private class defaults {
		/** the dictionaries folder */
		public static final String DICTIONARY_DIR = "";
		/** the cfml dictionary */
		public static final String CFDIC = "ColdFusion9";
		/** the javascript dictionary */
		public static final String JSDIC = "emca";
		/** the SQL dictionary */
		public static final String SQLDIC = "mssql";
		/** the html dictionary */
		public static final String HTDIC = "xhtml";
	}
	
	Properties fProps;
	
	public DictionaryPreferences(Properties props) {
		fProps = props;
	}
	
	public DictionaryPreferences() {
		fProps = new Properties();
	}
	
	public void setDictionaryDir(String dictDir) {
		fProps.setProperty(DictionaryPreferenceConstants.DICTIONARY_DIR, dictDir);
	}
	
	public String getDictionaryDir() {
		return fProps.getProperty(DictionaryPreferenceConstants.DICTIONARY_DIR, defaults.DICTIONARY_DIR);
	}
	
	public void setCFDictionary(String dict) {
		fProps.setProperty(DictionaryPreferenceConstants.CFDIC, dict);
	}
	
	public String getCFDictionary() {
		return fProps.getProperty(DictionaryPreferenceConstants.CFDIC, defaults.CFDIC);
	}
	
	public void setJSDictionary(String dict) {
		fProps.setProperty(DictionaryPreferenceConstants.JSDIC, dict);
	}
	
	public String getJSDictionary() {
		return fProps.getProperty(DictionaryPreferenceConstants.JSDIC, defaults.JSDIC);
	}
	
	public void setHTMLDictionary(String dict) {
		fProps.setProperty(DictionaryPreferenceConstants.HTDIC, dict);
	}
	
	public String getHTMLDictionary() {
		return fProps.getProperty(DictionaryPreferenceConstants.HTDIC, defaults.HTDIC);
	}
	
	public void setSQLDictionary(String dict) {
		fProps.setProperty(DictionaryPreferenceConstants.SQLDIC, dict);
	}
	
	public String getSQLDictionary() {
		return fProps.getProperty(DictionaryPreferenceConstants.SQLDIC, defaults.SQLDIC);
	}
}