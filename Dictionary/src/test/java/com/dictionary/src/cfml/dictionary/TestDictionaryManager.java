package com.dictionary.src.cfml.dictionary;

import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.dictionary.main.DictionaryManager;
import com.dictionary.main.SyntaxDictionary;
import com.dictionary.main.preferences.DictionaryPreferenceConstants;
import com.dictionary.main.preferences.DictionaryPreferences;

public class TestDictionaryManager {
	
	DictionaryPreferences fPrefs;
	
	@Before
	public void setUp() throws Exception {
		fPrefs = new DictionaryPreferences();
	}
	
	@Test
	public void testGetConfiguredDictionaries() {
		DictionaryManager.initDictionaries();
		String[][] fun = DictionaryManager.getConfiguredDictionaries();
		assertNotNull(fun);
	}
	
//	@Test
//	public void testGetDictionary() {
//		DictionaryManager.initDictionaries();
//		SyntaxDictionary fun = DictionaryManager.getDictionary(DictionaryPreferenceConstants.CFDIC_KEY);
//		System.err.println(fun.getDictionaryURL());
//		Set wee = fun.getAllTags();
//		assertNotNull(fun);
//	}
	
//	@Test
//	public void testGetDictionaryByVersion() {
//		DictionaryManager.initDictionaries();
//		SyntaxDictionary fun = DictionaryManager.getDictionaryByVersion(fPrefs.getCFDictionary());
//		System.err.println(fun.getDictionaryURL());
//		Set wee = fun.getAllTags();
//		assertNotNull(fun);
//	}
	
//	@Test
//	public void testExternalDictionaryLocation() {
//		DictionaryPreferences dprefs = new DictionaryPreferences();
//		dprefs.setDictionaryDir("test/data/dictionary");
//		dprefs.setCFDictionary("awesomedic");
//		DictionaryManager.initDictionaries(dprefs);
//		String[][] fun = DictionaryManager.getConfiguredDictionaries();
//		assertNotNull(fun);
//	}
	
//	@Test
//	public void testGetDictionaryByURL() {
//		DictionaryPreferences dprefs = new DictionaryPreferences();
//		dprefs.setDictionaryDir("");
//		dprefs.setCFDictionary("awesomedic");
//		DictionaryManager.initDictionaries(dprefs);
//		String[][] fun = DictionaryManager.getConfiguredDictionaries();
//		assertNotNull(fun);
//	}
	
}
