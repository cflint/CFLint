package com.cflint;

/**
 * tests from
 * https://github.com/mschierberl/varscoper/blob/master/varScoper.cfc
 *
 */
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintConfigurationException;
import com.cflint.exception.CFLintScanException;

@RunWith(Parameterized.class)
public class TestCFBugs_VarScoper_Names {

    final String tagName;
    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws CFLintConfigurationException {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("MISSING_VAR");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<?> primeNumbers() {
        return Arrays.asList(
                new String[][] { new String[] { "CFQuery" }, new String[] { "CFFeed" }, new String[] { "CFHttp" },
                        new String[] { "CFDirectory" }, new String[] { "CFForm" }, new String[] { "CFFtp" },
                        new String[] { "CFObject" }, new String[] { "CFSearch" }, new String[] { "CFProcResult" },
                        new String[] { "CFPop" }, new String[] { "CFRegistry" }, new String[] { "CFReport" },
                        new String[] { "CFDBInfo" }, new String[] { "CFDocument" }, new String[] { "CFCollection" },
                        new String[] { "CFPdf" }, new String[] { "CFZip" }, new String[] { "CFLdap" },
                        new String[] { "CFChart" }, new String[] { "CFHtmlToPdf" }, new String[] { "CFImage" },
                        new String[] { "CFImap" }, new String[] { "CFSharepoint" }, new String[] { "CFSpreadsheet" } });
    }

    public TestCFBugs_VarScoper_Names(final String tagName) {
        super();
        this.tagName = tagName;
    }

    @Test
    public void testScope_Name() throws CFLintScanException, CFLintConfigurationException {
        runTagAttrTest(tagName.toLowerCase(), "name", "xx");
        setUp();
        runTagAttrTest(tagName, "Name", "xx");
    }

    @Test
    public void testScope_Name_Vard() throws CFLintScanException, CFLintConfigurationException {
        runTagAttrTestVard(tagName.toLowerCase(), "name", "xx");
        setUp();
        runTagAttrTestVard(tagName, "Name", "xx");
    }

    public void runTagAttrTest(final String tag, final String attr, final String variable) throws CFLintScanException, CFLintConfigurationException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "   <" + tag + " " + attr + "=\""
                + variable + "\">\r\n" + "</" + tag + ">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(1, lintresult.getIssues().size());
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();

        assertEquals(1, result.size());
        assertEquals("MISSING_VAR", result.get(0).getMessageCode());
        assertEquals(variable, result.get(0).getVariable());
        assertEquals(3, result.get(0).getLine());
        assertEquals("test", result.get(0).getFunction());
        assertEquals("test", result.get(0).getFilename());
        String expected = "<cfstoredproc name=\"xx\">".replaceAll("cfstoredproc", tag).replaceAll("name", attr);
        if (!result.get(0).getExpression().startsWith(expected)) {
            assertEquals(expected, result.get(0).getExpression());
        }
    }

    public void runTagAttrTestVard(final String tag, final String attr, final String variable)
            throws CFLintScanException, CFLintConfigurationException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "   <cfset var " + variable
                + "=123/>\r\n" + "   <" + tag + " " + attr + "=\"" + variable + "\">\r\n" + "   </" + tag + ">\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }
}
