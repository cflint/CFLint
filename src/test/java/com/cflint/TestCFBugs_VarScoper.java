package com.cflint;

/**
 * tests from 
 * https://github.com/mschierberl/varscoper/blob/master/varScoper.cfc
 * 
 */
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintConfigurationException;
import com.cflint.exception.CFLintScanException;

public class TestCFBugs_VarScoper {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws CFLintConfigurationException {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("MISSING_VAR");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testCFProcParam_Out() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "   <cfset var yy=\"\"/>\r\n"
                + "	<cfstoredproc name=\"yy\" >\r\n" + "	   <cfprocparam variable=\"xx\" type=\"out\">\r\n"
                + "	</cfstoredproc>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(1, lintresult.getIssues().size());
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("MISSING_VAR", result.get(0).getMessageCode());
        assertEquals("xx", result.get(0).getVariable());
        assertEquals(5, result.get(0).getLine());
    }

    @Test
    public void testCFProcParam_InOut() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "   <cfset var yy=\"\"/>\r\n"
                + "	<cfstoredproc name=\"yy\" >\r\n" + "	   <cfprocparam variable=\"xx\" type=\"inout\">\r\n"
                + "	</cfstoredproc>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(1, lintresult.getIssues().size());
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("MISSING_VAR", result.get(0).getMessageCode());
        assertEquals("xx", result.get(0).getVariable());
        assertEquals(5, result.get(0).getLine());
    }

    // @Test
    // public void testCFProcParam_In() throws CFLintScanException{
    // final String cfcSrc = "<cfcomponent>\r\n" +
    // "<cffunction name=\"test\">\r\n" +
    // " <cfset var yy=\"\"/>\r\n" +
    // " <cfstoredproc name=\"yy\" >\r\n" +
    // " <cfprocparam variable=\"xx\" type=\"in\">\r\n" +
    // " </cfstoredproc>\r\n" +
    // "</cffunction>\r\n" +
    // "</cfcomponent>";
    // CFLintResult lintresult = cfBugs.scan(cfcSrc,"test");
    // assertEquals(0,lintresult.getIssues().size());
    // }

    @Test
    public void testCFFeed_Read_Vard() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "   <cfset var yy=\"\"/>\r\n"
                + "	<cffeed query=\"yy\" action=\"read\">\r\n" + "	</cffeed>\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

    @Test
    public void testCFFeed_Read_UnVard() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cffeed query=\"yy\" action=\"read\">\r\n" + "	</cffeed>\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(1, lintresult.getIssues().size());
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("MISSING_VAR", result.get(0).getMessageCode());
        assertEquals("yy", result.get(0).getVariable());
        assertEquals(3, result.get(0).getLine());
    }

    @Test
    public void testScript_UnVard() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	<cfscript>\r\n"
                + "   yy = 123;\r\n" + "	</cfscript>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(1, lintresult.getIssues().size());
        List<BugInfo> result = lintresult.getIssues().get("MISSING_VAR");
        assertEquals(1, result.size());
        assertEquals("MISSING_VAR", result.get(0).getMessageCode());
        assertEquals("yy", result.get(0).getVariable());
        assertEquals(4, result.get(0).getLine());
        assertEquals(Levels.ERROR, result.get(0).getSeverity());
        assertEquals("Variable yy is not declared with a var statement.", result.get(0).getMessage());
    }

    @Test
    public void testScript_UnVard_Scoped() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	<cfscript>\r\n"
                + "   cfevent.yy = 123;\r\n" + "	</cfscript>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(1, lintresult.getIssues().size());
    }

    @Test
    public void testScript_UnVard_ArrayFalsePositive() throws CFLintScanException {
        final String cfcSrc = "<cffunction name=\"x\"><cfset var myRet = [] />\n" + "\n" + "<cfstoredproc>\n"
                + "   <cfprocresult name=\"myRet[1]\" resultset=\"1\" />\n"
                + "   <cfprocresult name=\"myRet[2]\" resultset=\"2\" />\n"
                + "   <cfprocresult name=\"myRet[3]\" resultset=\"3\" />\n" + "</cfstoredproc></cffunction>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

}
