package com.cflint.integration;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.cflint.BugInfo;
import com.cflint.CFLint;
import com.cflint.JSONOutput;
import com.cflint.config.CFLintConfig;
import com.cflint.config.ConfigUtils;


/**
 * Run a test over each *.cf* file in src/test/resources/com/cflint/tests
 * 
 * @author ryaneberly
 *
 */
@RunWith(Parameterized.class)
public class TestFiles {
	
	File sourceFile;
	boolean autoReplaceFailed = false;
	static String singleTestName = null;
	static {
		try {
			singleTestName = ResourceBundle.getBundle("com.cflint.test").getString("RunSingleTest");
		} catch (Exception e) {
		}
	}
	
	public TestFiles(File sourceFile) {
		super();
		this.sourceFile = sourceFile;
		try {
			autoReplaceFailed = "Y".equalsIgnoreCase(ResourceBundle.getBundle("com.cflint.test").getString(
					"AutoReplaceFailedTestResults"));
		} catch (Exception e) {
		}
	}
	
	@Test
	public void test() throws IOException, URISyntaxException, JAXBException, TransformerException {
		final String inputString = loadFile(sourceFile);
		final File expectedFile = new File(sourceFile.getPath().replaceAll("\\.cf.", ".expected.txt"));
		final String expectedFileText = expectedFile.exists() ? loadFile(expectedFile) : null;
		String expectedText = expectedFileText ;
	
		final CFLintConfig config = loadPluginInfo(sourceFile.getParentFile());
		CFLint cflint = new CFLint(config );
		
		cflint.process(inputString, sourceFile.getPath());
		List<BugInfo> result = cflint.getBugs().getFlatBugList();
		StringWriter writer = new StringWriter();
		new JSONOutput().output(cflint.getBugs(), writer);
		
		String actualTree=writer.toString();
		if (expectedText == null || expectedText.trim().length() == 0) {
			writeExpectFile(expectedFile,  actualTree);
			System.out.println("Tree written to " + expectedFile);
		} else {
			if (autoReplaceFailed && !actualTree.equals(expectedText)) {
				System.out.println("Replaced content of " + expectedFile);
				expectedText = actualTree;
				writeExpectFile(expectedFile, actualTree);
			}
			assertEquals(actualTree.replaceAll("\\\\","/").replaceAll("/+","/").replaceAll("\r\n", "\n"), 
					expectedText.replaceAll("\\\\","/").replaceAll("/+","/").replaceAll("\r\n", "\n"));
		}
	}
	
	private void writeExpectFile(File expectedFile, String actualTree) throws IOException {
		final FileOutputStream fos = new FileOutputStream(expectedFile, false);
		fos.write(actualTree.getBytes());
		fos.close();
		
	}
	
	
	
	
	private String getTree(String expectedFileText) {
		if (expectedFileText != null && expectedFileText.contains("/*===TREE===*/")) {
			int startIdx = expectedFileText.indexOf("/*===TREE===*/") + 14;
			while (expectedFileText.charAt(startIdx) == '\r' || expectedFileText.charAt(startIdx) == '\n') {
				startIdx++;
			}
			int endIndex = expectedFileText.indexOf("/*======*/", startIdx);
			if (endIndex > startIdx) {
				while (expectedFileText.charAt(endIndex - 1) == '\r' || expectedFileText.charAt(endIndex - 1) == '\n') {
					endIndex--;
				}
				return expectedFileText.substring(startIdx, endIndex);
			}
		}
		if (expectedFileText != null && expectedFileText.contains("/*===")) {
			return null;
		}
		return expectedFileText;
	}
	
	@Parameterized.Parameters(name = "{index}{0}")
	public static Collection<Object[]> primeNumbers() throws URISyntaxException, IOException {
		final ArrayList<Object[]> retval = new ArrayList<Object[]>();
		final List<File> listing = new ArrayList<File>();
		fillResourceListing(new File("src/test/resources/com/cflint/tests"), listing);
		for (File s : listing) {
			retval.add(new Object[] { s });
		}
		return retval;
	}
	
	private static void fillResourceListing(File file, List<File> retval) {
		if (file != null) {
			if (file.isDirectory()) {
				for (File subfile : file.listFiles()) {
					fillResourceListing(subfile, retval);
				}
			} else if (file.getName().toLowerCase().endsWith(".cfc") || file.getName().toLowerCase().endsWith(".cfm")) {
				if (singleTestName == null || singleTestName.equals(file.getName())) {
					retval.add(file);
				} else if (singleTestName.equals("*LAST")) {
					if (retval.size() == 0 || file.lastModified() > retval.get(0).lastModified()) {
						retval.clear();
						retval.add(file);
					}
				}
			}
		}
	}
	public static String loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		byte[] b = new byte[is.available()];
		is.read(b);
		is.close();
		return new String(b);
	}
	
	public static CFLintConfig loadPluginInfo(File folder) throws IOException {
		try{
		final InputStream jsonInputStream = new FileInputStream(folder.getPath() + "/.cflintrc");
			if (jsonInputStream != null) {
				return ConfigUtils.unmarshalJson(jsonInputStream, CFLintConfig.class);
			}
		}catch(FileNotFoundException fnfe){}
		
		final InputStream inputStream = new FileInputStream(folder.getPath() + "/.cflintrc.xml");
		if (inputStream != null) {
			try {
				return ConfigUtils.unmarshal(inputStream, CFLintConfig.class);
			} catch (JAXBException e) {
				throw new IOException(e);
			}
		}
		return null;
	}
	
}
