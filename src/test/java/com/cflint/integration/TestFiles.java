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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.CFLintChainedConfig;
import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintConfiguration;
import com.cflint.config.ConfigUtils;
import com.cflint.exception.CFLintConfigurationException;
import com.cflint.exception.CFLintScanException;
import com.cflint.tools.FileUtil;

/**
 * Run a test over each *.cf* file in src/test/resources/com/cflint/tests
 *
 */
@RunWith(Parameterized.class)
public class TestFiles {

    private final File sourceFile;
    private boolean autoReplaceFailed = false;

    static String singleTestName = null;
    static {
        try {
            singleTestName = ResourceBundle.getBundle("com.cflint.test").getString("RunSingleTest");
        } catch (final Exception e) {
        }
    }

    public TestFiles(final File sourceFile, final String testName) {
        super();
        this.sourceFile = sourceFile;
        try {
            autoReplaceFailed = "Y".equalsIgnoreCase(
                    ResourceBundle.getBundle("com.cflint.test").getString("AutoReplaceFailedTestResults"));
        } catch (final Exception e) {
        }
    }

    @Test
    public void test() throws IOException, CFLintScanException, CFLintConfigurationException {
        String inputString = FileUtil.loadFile(sourceFile);
        //Normalize EOL
        if(inputString != null)
            inputString = inputString.replaceAll("\\r\\n?", "\n");
        final File expectedFile = new File(sourceFile.getPath().replaceAll("\\.cf.", ".expected.txt"));
        final String expectedFileText = expectedFile.exists() ? FileUtil.loadFile(expectedFile) : null;
        String expectedText = expectedFileText;

        final CFLintConfiguration config = loadPluginInfo(sourceFile.getParentFile());
        final CFLintAPI cflint = new CFLintAPI(config);
        cflint.setLogError(true);
        CFLintResult result = cflint.scan(inputString, sourceFile.getPath());
        // Support the processing of a second source file in a single test
        final File nextFile = new File(sourceFile.getPath() + ".2");
        if (nextFile.exists()) {
            final String inputString2 = FileUtil.loadFile(nextFile);
            result = cflint.scan(inputString2, nextFile.getPath().replaceAll("[.]2$", ""));
        }
        final StringWriter writer = new StringWriter();
        result.writeJSON(writer);

        final String actualTree = writer.toString();
        if (expectedText == null || expectedText.trim().length() == 0) {
            writeExpectFile(expectedFile, actualTree);
            System.out.println("Tree written to " + expectedFile);
        } else {
            if (autoReplaceFailed && !actualTree.equals(expectedText)) {
                System.out.println("Replaced content of " + expectedFile);
                expectedText = actualTree;
                writeExpectFile(expectedFile, actualTree);
            }
            assertEquals(
                    expectedText.replaceAll("\\\\", "/").replaceAll("/+", "/").replaceAll("\r\n", "\n")
                            .replaceAll("\"timestamp\" : \\d+", "\"timestamp\" : 0"),
                    actualTree.replaceAll("\\\\", "/").replaceAll("/+", "/").replaceAll("\r\n", "\n")
                            .replaceAll("\"timestamp\" : \\d+", "\"timestamp\" : 0"));
        }
    }

    private void writeExpectFile(final File expectedFile, final String actualTree) throws IOException {
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
        for (final File s : listing) {
            retval.add(new Object[] { s, baseFolder.toPath().relativize(s.toPath()).toString() });
        }
        return retval;
    }

    private static void fillResourceListing(final File file, final List<File> retval) {
        if (file != null) {
            if (file.isDirectory()) {
                for (final File subfile : file.listFiles()) {
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

    public static CFLintConfiguration loadPluginInfo(final File folder) throws IOException {
        final CFLintChainedConfig config = new CFLintChainedConfig(CFLintConfig.createDefault());
        try {
            final InputStream jsonInputStream = new FileInputStream(folder.getPath() + "/.cflintrc");
            final CFLintConfig retval = ConfigUtils.unmarshalJson(jsonInputStream, CFLintConfig.class);
            jsonInputStream.close();
            return config.createNestedConfig(retval);
        } catch (final FileNotFoundException fnfe) {
        }

        final InputStream inputStream = new FileInputStream(folder.getPath() + "/.cflintrc.xml");
        try {
            final CFLintConfig retval = ConfigUtils.unmarshal(inputStream, CFLintConfig.class);
            return config.createNestedConfig(retval);
        } catch (final JAXBException e) {
            throw new IOException(e);
        }
    }

}
