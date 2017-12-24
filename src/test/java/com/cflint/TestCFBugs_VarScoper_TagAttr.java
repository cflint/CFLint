package com.cflint;

/**
 * tests from
 * https://github.com/mschierberl/varscoper/blob/master/varScoper.cfc
 *
 */
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
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
public class TestCFBugs_VarScoper_TagAttr {

    final String tagName;
    final String attribute;

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws CFLintConfigurationException {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("MISSING_VAR");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<String[]> primeNumbers() {
        List<String[]> retval = new ArrayList<String[]>();
        retval.add(new String[] { "CFExecute", "Variable" });
        retval.add(new String[] { "CFFeed", "Query" });
        retval.add(new String[] { "CFFile", "Variable" });
        retval.add(new String[] { "CFFile", "Result" });
        retval.add(new String[] { "CFFtp", "Result" });
        retval.add(new String[] { "CFHttp", "Result" });
        retval.add(new String[] { "CFImage", "StructName" });
        retval.add(new String[] { "CFInvoke", "ReturnVariable" });
        retval.add(new String[] { "CFLoop", "Index" });
        retval.add(new String[] { "CFLoop", "Item" });
        retval.add(new String[] { "CFNtAuthenticate", "Result" });
        retval.add(new String[] { "CFProcParam", "Variable" });
        retval.add(new String[] { "CFQuery", "Result" });
        retval.add(new String[] { "CFRegistry", "Variable" });
        retval.add(new String[] { "CFSavecontent", "Variable" });
        retval.add(new String[] { "CFSpreadsheet", "Query" });
        retval.add(new String[] { "CFStoredProc", "Result" });
        retval.add(new String[] { "CFWddx", "Output" });
        retval.add(new String[] { "CFXml", "Variable" });
        retval.add(new String[] { "CFZip", "Variable" });
        return retval;
    }

    public TestCFBugs_VarScoper_TagAttr(final String tagName, final String attribute) {
        super();
        this.tagName = tagName;
        this.attribute = attribute;
    }

    @Test
    public void testUnvarrd() throws CFLintScanException, CFLintConfigurationException {
        runTagAttrTest(tagName.toLowerCase(), attribute.toLowerCase(), "xx");
        setUp();
        runTagAttrTest(tagName, attribute, "xx");
        assertEquals(true, true); // Codacity
    }

    @Test
    public void testVarrd() throws CFLintScanException, CFLintConfigurationException {
        runTagAttrTestVard(tagName.toLowerCase(), attribute.toLowerCase(), "xx");
        setUp();
        runTagAttrTestVard(tagName, attribute, "xx");
        assertEquals(true, true); // Codacity
    }

    @Test
    public void testDotVarrd() throws CFLintScanException, CFLintConfigurationException {
        runTagAttrDotVarTest(tagName.toLowerCase(), attribute.toLowerCase(), "zz.xx", "zz");
        setUp();
        runTagAttrDotVarTest(tagName, attribute, "zz.xx", "zz");
        setUp();
        runTagAttrDotVarTest(tagName, attribute, "this.xx", "zz");
        assertEquals(true, true); // Codacity
    }

    public void runTagAttrTest(final String tag, final String attr, final String variable) throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "   <" + tag + " " + attr + "=\""
                + variable + "\">\r\n" + "	</" + tag + ">\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(1, lintresult.getIssues().size());
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("MISSING_VAR", result.get(0).getMessageCode());
        assertEquals(variable, result.get(0).getVariable());
        assertEquals(3, result.get(0).getLine());
    }

    public void runTagAttrDotVarTest(final String tag, final String attr, final String variable, final String initVar)
            throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "   <cfset var " + initVar
                + "=123/>\r\n" + "   <" + tag + " " + attr + "=\"" + variable + "\">\r\n" + "   </" + tag + ">\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

    public void runTagAttrTestVard(final String tag, final String attr, final String variable)
            throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "   <cfset var " + variable
                + "=123/>\r\n" + "   <" + tag + " " + attr + "=\"" + variable + "\">\r\n" + "   </" + tag + ">\r\n"
                + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }
}
