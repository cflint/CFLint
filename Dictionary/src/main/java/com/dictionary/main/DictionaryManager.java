/*
 * Created on Feb 26, 2004
 *
 * The MIT License
 * Copyright (c) 2004 Rob Rohan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software 
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE.
 */
package com.dictionary.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dictionary.main.preferences.DictionaryPreferenceConstants;
import com.dictionary.main.preferences.DictionaryPreferences;
import com.dictionary.main.syntax.HTMLSyntaxDictionary;
import com.dictionary.main.syntax.JSSyntaxDictionary;
import com.dictionary.main.syntax.SQLSyntaxDictionary;

/**
 * @author Rob
 * 
 *         This class loads all the dictionaries and can be used to get specific dictionaries. This (hopefully) well
 *         help in abstracting the dictionaries not intended to be instantiated
 */
public class DictionaryManager {
	
	/** the coldfusion dictionaries folder */
	public static String DICTIONARY_DIR = "";
	public static String CF_DICTIONARY;
	/** the coldfusion dictionary */
	public static final String CFDIC_KEY = DictionaryPreferenceConstants.CFDIC_KEY;
	/** the javascript dictionary */
	public static final String JSDIC_KEY = DictionaryPreferenceConstants.JSDIC_KEY;
	/** the SQL dictionary */
	public static final String SQLDIC_KEY = DictionaryPreferenceConstants.SQLDIC_KEY;
	/** the (yet to be made) html dictionary */
	public static final String HTDIC_KEY = DictionaryPreferenceConstants.HTDIC_KEY;
	/** all the dictionaries */
	private static Map dictionaries = new HashMap();
	/** the dictionary cache - for swtiching between grammars */
	private static Map dictionariesCache = new HashMap();
	
	/** map of versions, might be a replication of the above */
	private static Map dictionaryVersionCache = new HashMap();
	
	/** the dictionary config file in DOM form */
	private static Document dictionaryConfig = null;
	private static DictionaryPreferences fPrefs;
	private static String fBuiltInDictionaryPath;
	
	private DictionaryManager(DictionaryPreferences prefs) {
		fPrefs = prefs;
		init();
	}
	
	static private void init() {
		fBuiltInDictionaryPath = "jar:file:" + DictionaryManager.class.getResource("/dictionaries.zip").getFile()
				+ "!/org.cfeclipse.cfml/dictionary/";
		DICTIONARY_DIR = fPrefs.getDictionaryDir();
		CF_DICTIONARY = fPrefs.getCFDictionary();
	}
	
	/**
	 * Loads the dictionary config file. The config file lists all the dictionary files that are available to the system
	 */
	private static void loadDictionaryConfig() {
		try {
			if (fPrefs == null) {
				fPrefs = new DictionaryPreferences();
				init();
			}			
	
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setIgnoringElementContentWhitespace(true);
			factory.setCoalescing(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			if (fPrefs.getDictionaryDir().length() != 0) {
				dictionaryConfig = builder.parse(new File(fPrefs.getDictionaryDir() + "/dictionaryconfig.xml"));
			} else {
				URL dc = new URL(fBuiltInDictionaryPath + "dictionaryconfig.xml");
				dictionaryConfig = builder.parse(dc.openStream());
			}
			// URL configurl = DictionaryManager.class.getResource("/dictionary/dictionaryconfig.xml");
			// URL configurl = new URL(dictionaryConfigURL + "/dictionaryconfig.xml");
			// System.err.println(configurl.getPath());
			// System.err.println(configurl.getFile().toString());
			// dictionaryConfig = builder.parse(configurl.getFile());
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	/**
	 * Tell the dictionaries to load based on the config file
	 */
	public static void initDictionaries() {
		
		// long time = System.currentTimeMillis();
		// System.out.println("Dictionaries initialized start");
		
		// get the dictionary config file into a DOM
		loadDictionaryConfig();
		String cfdictversion = getInitialDictVersion();
		if (dictionaryConfig == null)
			throw new IllegalArgumentException("Problem loading dictionaryconfig.xml");
		
		// load the default dictionaries into the cache
		// this is kind of weak but it'll do pig... it'll do...
		if (cfdictversion.trim().length() == 0) {
			cfdictversion = getFirstVersion(CFDIC_KEY);
		}
		String htdictversion = getFirstVersion(HTDIC_KEY);
		String jsdictversion = getFirstVersion(JSDIC_KEY);
		
		// load the dictionary into the cache
		loadDictionaryByVersion(cfdictversion);
		loadDictionaryByVersion(htdictversion);
		loadDictionaryByVersion(jsdictversion);
		
		// load from the cache to the live
		loadDictionaryFromCache(cfdictversion, CFDIC_KEY);
		loadDictionaryFromCache(cfdictversion, SQLDIC_KEY);
		loadDictionaryFromCache(htdictversion, HTDIC_KEY);
		loadDictionaryFromCache(jsdictversion, JSDIC_KEY);
		
		// System.out.println("Dictionaries initialized in " +
		// (System.currentTimeMillis() - time) + " ms");
	}
	
	/**
	 * Tell the dictionaries to load based on the config file
	 */
	public static void initDictionaries(DictionaryPreferences prefs) {
		fPrefs = prefs;
		init();
		initDictionaries();
	}
	
	private static String getInitialDictVersion() {
		// return
		// propertyManager.getCurrentDictionary(fInput.getFile().getProject());
		return CF_DICTIONARY;
	}
	
	/**
	 * Gets the first version set in the dictionaryconfig file for the given dictionary. This is useful when hitting an
	 * error and wanting to get a fall back dictionary. This might error if there are no versions defined for the given
	 * dictionary (or if the document is not valid)
	 * 
	 * @param forDictionary
	 * @return
	 */
	public static String getFirstVersion(String forDictionary) {
		return dictionaryConfig.getElementById(forDictionary).getFirstChild().getAttributes().getNamedItem("key")
				.getNodeValue();
	}
	
	/**
	 * This gets a string array of supported types. This is mostly used in the radio display on the project settings. It
	 * builds a string array from the key and label attributes in the dictionary config file
	 * 
	 * @return {key, label} array
	 */
	public static String[][] getConfiguredDictionaries() {
		NodeList cfmltypes = dictionaryConfig.getElementById(CFDIC_KEY).getChildNodes();
		
		byte typeslen = (byte) cfmltypes.getLength();
		
		String[][] options = new String[typeslen][2];
		
		for (byte z = 0; z < typeslen; z++) {
			String key = cfmltypes.item(z).getAttributes().getNamedItem("key").getNodeValue();
			String label = cfmltypes.item(z).getAttributes().getNamedItem("label").getNodeValue();
			
			options[z][0] = label;
			options[z][1] = key;
		}
		
		return options;
	}
	
	/**
	 * Loads a syntax file into the cache by the key defined in the dictionary config file. for example
	 * 
	 * <pre>
	 * ...
	 * &lt;version key="cfmx701" label="Coldfusion 7.0"&gt;
	 * 	&lt;grammar location="cfml.xml" /&gt;
	 * 	&lt;grammar location="user.xml" /&gt;
	 * &lt;/version&gt;
	 * ...
	 * </pre>
	 * 
	 * "key" is the version key you would pass in here to load the Coldfusion 7.0 grammar
	 * 
	 * @param versionkey
	 */
	public static void loadDictionaryByVersion(String versionkey) {
		SyntaxDictionary dic = getDictionaryByVersion(versionkey);
		
		if (dic == null) {
			throw new IllegalArgumentException("Problem loading version node " + versionkey
					+ " from dictionaryconfig.xml");
		}
		// add finally add them to the cache
		addDictionaryToCache(versionkey, dic);
	}
	
	/**
	 * Alternate version using JDOM
	 * 
	 * @param versionkey
	 * @return
	 */
	public static SyntaxDictionary getDictionaryByVersionAlt(String versionkey) {
		
		if (dictionaryVersionCache.containsKey(versionkey)) {
			return (SyntaxDictionary) dictionaryVersionCache.get(versionkey);
		} else {
			SAXBuilder builder = new SAXBuilder();
			SyntaxDictionary dic = new SQLSyntaxDictionary();
			URL dictionaryConfigURL = null;
			
			try {
				dictionaryConfigURL = new URL(DICTIONARY_DIR);
				URL configurl = new URL(dictionaryConfigURL, "dictionaryconfig.xml");
				org.jdom.Document document = builder.build(configurl);
				
				XPath x = XPath.newInstance("//dictionary[@id='CF_DICTIONARY']/version[@key=\'" + versionkey
						+ "\']/grammar[1]");
				
				Element grammerElement = (Element) x.selectSingleNode(document);
				dic = new SQLSyntaxDictionary();
				dic.loadDictionary(getDictionaryLocation(grammerElement.getAttributeValue("location")));
				dictionaryVersionCache.put(versionkey, dic);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return (SyntaxDictionary) dictionaryVersionCache.get(versionkey);
	}
	
	public static SyntaxDictionary getDictionaryByVersion(String versionkey) {
		if (dictionaryConfig == null)
			throw new IllegalArgumentException("Problem loading dictionaryconfig.xml");
		
		// grab the cfml dictionary
		// Node n = dictionaryConfig.getElementById(CFDIC).getFirstChild();
		Node versionNode = dictionaryConfig.getElementById(versionkey);

		if (versionNode == null) {
			return null;
			//
		}
		
		// get the dictype from the parent node
		String dicttype = versionNode.getParentNode().getAttributes().getNamedItem("id").getNodeValue();
		
		// now, make and load the dictionary based on the type
		SyntaxDictionary dic = null;
		
		if (dicttype.equals(CFDIC_KEY)) {
			// load the sql keywords into the cache
			dic = new SQLSyntaxDictionary();
			// TODO: make this not a hack
			String sqlwords = dictionaryConfig.getElementById(SQLDIC_KEY).getFirstChild().getFirstChild()
					.getAttributes().getNamedItem("location").getNodeValue();
			try {
				((SQLSyntaxDictionary) dic).loadKeywords(new URL(getDictionaryLocation(sqlwords)));
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException("Problem loading version node " + sqlwords
						+ " from dictionaryconfig.xml");
			}
		} else if (dicttype.equals(JSDIC_KEY)) {
			dic = new JSSyntaxDictionary();
		} else if (dicttype.equals(HTDIC_KEY)) {
			dic = new HTMLSyntaxDictionary();
		}
		
		// get a list of all the grammars to load
		NodeList grammars = versionNode.getChildNodes();
		byte nlen = (byte) grammars.getLength();
		
		// loop over the grammars and add them to the dictionaries
		Node n = null;
		for (byte z = 0; z < nlen; z++) {
			n = grammars.item(z);
			String filename = n.getAttributes().getNamedItem("location").getNodeValue().trim();
			dic.loadDictionary(getDictionaryLocation(filename));
		}
		return dic;
	}
	
	private static String getDictionaryLocation(String path) {
		if (fPrefs.getDictionaryDir().length() == 0) {
			path = fBuiltInDictionaryPath + path;
		} else {
			if (path.startsWith("http")) {
				File dictFile = new File(fPrefs.getDictionaryDir() + path);
				if (!dictFile.exists()) {
					try {
						URL url = new URL(path);
						InputStream urlStream = url.openStream();
						// write out the freaking file
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// download the file to dictDir
			} else {
				File dictDir = new File(fPrefs.getDictionaryDir());
				path = "file:" + dictDir.getAbsolutePath() + "/" + path;
			}
		}
		return path;
	}
	
	/**
	 * Takes a Syntax dictionary from the cache and puts it into the live dictionary
	 * 
	 * @param cachekey
	 * @param livekey
	 */
	public static synchronized void loadDictionaryFromCache(String cachekey, String livekey) {
		
		loadDictionaryFromCache(cachekey, livekey, false);
	}
	
	/**
	 * Takes a Syntax dictionary from the cache and puts it into the live dictionary. Doesn't try to load dictionary if
	 * retry is true.
	 * 
	 * @param cachekey
	 * @param livekey
	 * @param retry
	 *            - Indicates if we have already tried to load the dictionary into the cache.
	 */
	private static synchronized void loadDictionaryFromCache(String cachekey, String livekey, boolean retry) {
		
		if (dictionariesCache.containsKey(cachekey) && dictionaries.containsKey(livekey)) {
			// Object tdic = dictionaries.get(livekey);
			Object tdic = dictionariesCache.get(cachekey);
			
			dictionaries.put(livekey, tdic);
		} else if (dictionariesCache.containsKey(cachekey)) {
			dictionaries.put(livekey, dictionariesCache.get(cachekey));
		} else if (!dictionariesCache.containsKey(cachekey) && cachekey != null && cachekey.length() > 0) {
			if (retry) {
				// We've already tried to load the dictionary, so something must
				// be broken.
				System.out
						.println("Error! Dictionary "
								+ cachekey
								+ " could not be loaded.\n This may cause CFEclipse to work unpredictably or, in some cases, not at all.\n\nTry closing Eclipse and starting it from the command line with -clean as a command line argument.");
				throw new IllegalArgumentException("Problem loading version node " + cachekey
						+ " from dictionaryconfig.xml");
			}
			// the dictionary is not in the cache, lets try to load it...
			loadDictionaryByVersion(cachekey);
			// try again..
			loadDictionaryFromCache(cachekey, livekey, true);
		} else if (cachekey != null || cachekey.length() > 0) {
			return;
		} else {
			throw new IllegalArgumentException("Cache key: " + cachekey + " is not in the cache"
					+ dictionariesCache.keySet().toString());
		}
	}
	
	/**
	 * Adds a SyntaxDictionary to the cache
	 * 
	 * @param key
	 * @param sd
	 */
	public static void addDictionaryToCache(String key, SyntaxDictionary sd) {
		dictionariesCache.put(key, sd);
	}
	
	/**
	 * Add a dictionary to the manager
	 * 
	 * @param key
	 *            the key to be used to get the dictionary
	 * @param sd
	 *            the dictionary
	 */
	public static void addDictionary(String key, SyntaxDictionary sd) {
		dictionaries.put(key, sd);
	}
	
	/**
	 * Get a dictionary from the live dictionaries
	 * 
	 * @param key
	 *            the dictionary's key (often one of the statics above)
	 * @return the dictionary
	 */
	public static SyntaxDictionary getDictionary(String key) {
		// System.out.println("Getting dictionary " + key);
		SyntaxDictionary dict = (SyntaxDictionary) dictionaries.get(key);
		// System.out.println("GOT: " + dict);
		return dict;
	}
	
	/**
	 * @return Returns the live dictionaries.
	 */
	public static Map getDictionaries() {
		return DictionaryManager.dictionaries;
	}
	
	public static Map getDictionariesCache() {
		return dictionariesCache;
	}
	
	public static void setDictionariesCache(Map dictionariesCache) {
		DictionaryManager.dictionariesCache = dictionariesCache;
	}
}
