/*
 * Created on Jan 21, 2005
 * by Christopher Bradford
 *
 * Do we need to include the MIT License in each .java file?
 * 
 */
package com.dictionary.main.syntax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.dictionary.main.ISyntaxDictionary;

/**
 * @author Christopher Bradford The dictionary for SQL keywords to be used in CFQuery tags. Extends CFSyntaxDicionary
 *         for now because CF operators should be color coded. The keywords are loaded from an external file by calling
 *         loadKeywords
 * 
 *         RR - I took this a step further and just made this the default dictionary for all coldfusion syntax. Instead
 *         of using CFSyntaxDictionary for the document and an instance of SQLSyntaxDictionary for only the sql
 *         partitions, this class is used for the document and sql partitions now.
 */
public class SQLSyntaxDictionary extends CFSyntaxDictionary implements ISyntaxDictionary {
	protected static Set sqlkeywords;
	
	/**
     * 
     */
	public SQLSyntaxDictionary() {
		super();
		sqlkeywords = new HashSet();
		operators = new HashSet();
		buildOperatorSyntax();
	}
	
	/**
	 * gets SQL specific keywords (SELECT, FROM etc.);
	 * 
	 * @return all the keywords
	 */
	public Set getSQLKeywords() {
		return sqlkeywords;
	}
	
	/**
	 * load SQL keywords from the specified file; relative to dictionaryBaseURL
	 * 
	 * @param keywordFilename
	 *            The file to read from
	 */
	public void loadKeywords(URL keywordsURL) {
		try {
			if (keywordsURL == null)
				throw new IOException("Keyword file name cannot be null!");
			
			InputStream iStream = keywordsURL.openStream();
			// InputStream iStream = DictionaryManager.class.getResourceAsStream("dictionary/" + keywordFilename);
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(iStream));
			String line = fileReader.readLine();
			List keywords = new ArrayList();
			
			while (line != null) {
				keywords.add(line.toLowerCase().trim());
				line = fileReader.readLine();
			}
			buildSQLKeywordSyntax(keywords);
		} catch (IOException e) {
			System.err.println(keywordsURL);
			e.printStackTrace();
		}
	}
	
	/**
	 * build all the SQL keywords
	 * 
	 * @param keywords
	 *            The List of keywords to add to the Set
	 */
	protected void buildSQLKeywordSyntax(List keywords) {
		Iterator it = keywords.iterator();
		while (it.hasNext()) {
			String word = (String) it.next();
			sqlkeywords.add(word);
		}
	}
}
