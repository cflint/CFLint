package com.cflint.integration;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

import com.cflint.BugInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.cflint.CFLint;
import com.cflint.JSONOutput;
import com.cflint.config.CFLintChainedConfig;
import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintConfiguration;
import com.cflint.config.ConfigUtils;
import com.cflint.plugins.exceptions.CFLintExceptionListener;
import com.cflint.tools.FileUtil;


/**
 * Run a test over each *.cf* file in src/test/resources/com/cflint/tests
 * 
 * @author ryaneberly
 *
 */
@RunWith(Parameterized.class)
public class TestFiles {
	
	private File sourceFile;
	private String testName;
	private boolean autoReplaceFailed = false;
	
	static String singleTestName = null;
	static {
		try {
			singleTestName = ResourceBundle.getBundle("com.cflint.test").getString("RunSingleTest");
		} catch (Exception e) {
		}
	}
	
	public TestFiles(File sourceFile,String testName) {
		super();
		this.sourceFile = sourceFile;
		this.testName=testName;
		try {
			autoReplaceFailed = "Y".equalsIgnoreCase(ResourceBundle.getBundle("com.cflint.test").getString(
					"AutoReplaceFailedTestResults"));
		} catch (Exception e) {
		}
	}
	
	@Test
	public void test() throws IOException, URISyntaxException, JAXBException, TransformerException {
		final String inputString = FileUtil.loadFile(sourceFile);
		final File expectedFile = new File(sourceFile.getPath().replaceAll("\\.cf.", ".expected.txt"));
		final String expectedFileText = expectedFile.exists() ? FileUtil.loadFile(expectedFile) : null;
		String expectedText = expectedFileText ;
	
		final CFLintConfiguration config = loadPluginInfo(sourceFile.getParentFile());
		CFLint cflint = new CFLint(config );
		cflint.setVerbose(true);
		cflint.setLogError(true);
		cflint.addExceptionListener(new CFLintExceptionListener() {
			@Override
			public void exceptionOccurred(Throwable exception, String messageCode, String filename, Integer line,
					Integer column, String functionName, String expression) {
				if(exception != null){
					exception.printStackTrace();
				}
				fail("Error scanning " + filename);
			}
		});
		cflint.process(inputString, sourceFile.getPath());
		//Support the processing of a second source file in a single test
		File nextFile = new File(sourceFile.getPath()+ ".2");
		if(nextFile.exists()){
			final String inputString2 = FileUtil.loadFile(nextFile);
			cflint.process(inputString2, nextFile.getPath().replaceAll("[.]2$", ""));		
		}
		for (BugInfo bug : cflint.getBugs()) {
			cflint.getStats().getCounts().add(bug.getMessageCode(), bug.getSeverity());
		}
		//List<BugInfo> result = cflint.getBugs().getFlatBugList();
		StringWriter writer = new StringWriter();
		new JSONOutput().output(cflint.getBugs(), writer, cflint.getStats());
		
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
			assertEquals( 
					expectedText.replaceAll("\\\\","/").replaceAll("/+","/").replaceAll("\r\n", "\n").replaceAll("\"timestamp\" : \\d+", "\"timestamp\" : 0"),
					actualTree.replaceAll("\\\\","/").replaceAll("/+","/").replaceAll("\r\n", "\n").replaceAll("\"timestamp\" : \\d+", "\"timestamp\" : 0"));
		}
	}
	
	private void writeExpectFile(File expectedFile, String actualTree) throws IOException {
		final FileOutputStream fos = new FileOutputStream(expectedFile, false);
		fos.write(actualTree.getBytes());
		fos.close();
		
	}
	
	@Parameterized.Parameters(name = "{1}")
	public static Collection<Object[]> primeNumbers() throws URISyntaxException, IOException {
		final ArrayList<Object[]> retval = new ArrayList<Object[]>();
		final List<File> listing = new ArrayList<File>();
		final File baseFolder = new File("src/test/resources/com/cflint/tests");
		fillResourceListing(baseFolder, listing);
		for (File s : listing) {
			retval.add(new Object[] { s, baseFolder.toPath().relativize(s.toPath() ).toString() });
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

	public static CFLintConfiguration loadPluginInfo(File folder) throws IOException {
		CFLintChainedConfig config = new CFLintChainedConfig(CFLintConfig.createDefault());
		try{
  		    final InputStream jsonInputStream = new FileInputStream(folder.getPath() + "/.cflintrc");
			final CFLintConfig retval = ConfigUtils.unmarshalJson(jsonInputStream, CFLintConfig.class);
			jsonInputStream.close();
			return config.createNestedConfig(retval);
		}catch(FileNotFoundException fnfe){}
		
		final InputStream inputStream = new FileInputStream(folder.getPath() + "/.cflintrc.xml");
		try {
			final CFLintConfig retval =  ConfigUtils.unmarshal(inputStream, CFLintConfig.class);
			return config.createNestedConfig(retval);
		} catch (JAXBException e) {
			throw new IOException(e);
		}
	}
	
}
