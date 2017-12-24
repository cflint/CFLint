package com.cflint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintConfigurationException;
import com.cflint.exception.CFLintScanException;

public class TestCFBugs {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws IOException, CFLintConfigurationException {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("MISSING_VAR","GLOBAL_VAR","NESTED_CFOUTPUT","QUERYNEW_DATATYPE","MISSING_SEMI");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testSimpleCFSET() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	<cfif 1 EQ 1>\r\n"
                + "	<cfset x=123/>\r\n" + "	<cfset var y=123/>\r\n" + "	</cfif>\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("MISSING_VAR", result.get(0).getMessageCode());
        assertEquals(4, result.get(0).getLine());
    }

    @Test
    public void testSimpleCFSETFirstOffenseOnly() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	<cfif 1 EQ 1>\r\n"
                + "	<cfset x=123/>\r\n" + "	<cfset x=555/>\r\n" + "	</cfif>\r\n" + "</cffunction>\r\n"
                + "<cffunction name=\"test2\">\r\n" + "	<cfif 1 EQ 1>\r\n" + "	<cfset x=123/>\r\n"
                + "	<cfset x=555/>\r\n" + "	</cfif>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("MISSING_VAR", result.get(0).getMessageCode());
        assertEquals(4, result.get(0).getLine());
        assertEquals("MISSING_VAR", result.get(1).getMessageCode());
        assertEquals(10, result.get(1).getLine());
    }

    @Test
    public void testSimpleCFReadVar() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfif trim(Privilege) EQ \"\" or isUserInRole('#Privilege#')>\r\n"
                + "	<cfoutput>#x#</cfoutput>\r\n" + "	<cfset var y=x/>\r\n" + "	</cfif>"
                + "</cffunction>" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testSimpleCFSETArg() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "<cfargument name=\"x\" default=\"\">\r\n" + "	<cfif 1 EQ 1>\r\n" + "	<cfset x=123/>\r\n"
                + "	<cfset var y=123/>\r\n" + "	</cfif>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testSimpleCFSETNoParse() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	<cfset x=123/>"
                + "	<cfset var y=123/>\r\n" + "</cffunction>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("MISSING_VAR", result.get(0).getMessageCode());// Note
                                                                    // parsing
                                                                    // error
                                                                    // fixed

    }

    @Test
    public void testSimpleCFSCRIPTCommentInScript() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n" + "	<cfscript>\r\n"
                + "   // Set Rate Event Fields;\r\n" + "	var x={};\r\n" + "	</cfscript>\r\n"
                + "	<cfscript>\r\n" + "	x.xx=123;\r\n" + "	</cfscript>\r\n" + "</cffunction>\r\n"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals("" + result, 0, result.size());
    }

    @Test
    public void testSimpleCFQUERYNested() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfoutput query=\"q123\">\r\n" + "	<cfset var y=123/>\r\n"
                + "	<cfoutput>#y#</cfoutput>\r\n" + "	</cfoutput>\r\n" + "</cffunction>\r\n"
                + "/<cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("NESTED_CFOUTPUT", result.get(0).getMessageCode());
        assertEquals(5, result.get(0).getLine());
    }

    @Test
    public void testSimpleCFQUERYIssue30() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfoutput query=\"qryPopups\" group=\"id\">\n"
                + "            <cfif message NEQ \"\" and not(ListFind(arraytoList( arguments.quote.getErrorHandler().getShownErrors()),id))>\n"
                + "                <cfoutput>#message####id#|</cfoutput>\n"
                + "                <cfset arrayappend(arguments.quote.getErrorHandler().getShownErrors(),id)>\n"
                + "            </cfif>\n"
                + "            <cflog file=\"Quote_#quote.getOnlineNumber()#\" text=\"Popup:#message#,#id#\" type=\"informational\">\n"
                + "        </cfoutput>\r\n" + "</cffunction>\r\n" + "/<cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

    @Test
    public void testSimpleCFQUERYIssue30b_OK() throws CFLintScanException {
        final String cfcSrc = "	<cfoutput query=\"qryPopups\" group=\"id\">\n" + "	<cfoutput group=\"id2\">\n"
                + "            <cfif message NEQ \"\" and not(ListFind(arraytoList( arguments.quote.getErrorHandler().getShownErrors()),id))>\n"
                + "                <cfoutput>#message####id#|</cfoutput>\n"
                + "                <cfset arrayappend(arguments.quote.getErrorHandler().getShownErrors(),id)>\n"
                + "            </cfif>\n"
                + "            <cflog file=\"Quote_#quote.getOnlineNumber()#\" text=\"Popup:#message#,#id#\" type=\"informational\">\n"
                + "        </cfoutput>\r\n" + "        </cfoutput>\r\n";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

    @Test
    public void testSimpleCFQUERYIssue30bNotOK() throws CFLintScanException {
        final String cfcSrc = "	<cfoutput query=\"qryPopups\" group=\"id\">\n" + "	<cfoutput>\n"
                + "            <cfif message NEQ \"\" and not(ListFind(arraytoList( arguments.quote.getErrorHandler().getShownErrors()),id))>\n"
                + "                <cfoutput>#message####id#|</cfoutput>\n"
                + "                <cfset arrayappend(arguments.quote.getErrorHandler().getShownErrors(),id)>\n"
                + "            </cfif>\n"
                + "            <cflog file=\"Quote_#quote.getOnlineNumber()#\" text=\"Popup:#message#,#id#\" type=\"informational\">\n"
                + "        </cfoutput>\r\n" + "        </cfoutput>\r\n";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("NESTED_CFOUTPUT", result.get(0).getMessageCode());
        assertEquals(4, result.get(0).getLine());

    }

    @Test
    public void testSimpleCFQUERYNestedWithGroup() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\">\r\n"
                + "	<cfoutput query=\"q123\" group=\"x\">\r\n" + "	<cfset var y=123/>\r\n"
                + "	<cfoutput>#y#</cfoutput>\r\n" + "	</cfoutput>\r\n" + "</cffunction>\r\n"
                + "/<cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

    @Test
    public void testCFScriptCfc() throws CFLintScanException {
        final String cfcSrc = "/** \r\n" + "* Simple Component. \r\n" + "*/ \r\n" + "component { \r\n" + "/** \r\n"
                + "* Simple function. \r\n" + "*/ \r\n" + "public void function foo() { \r\n" + "xx=123; \r\n"
                + "} \r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("MISSING_VAR", result.get(0).getMessageCode());
        assertEquals(9, result.get(0).getLine());
    }

    @Test
    public void testCFScriptCfcArgOk() throws CFLintScanException {
        final String cfcSrc = "/** \r\n" + "* Simple Component. \r\n" + "*/ \r\n" + "component { \r\n" + "/** \r\n"
                + "* Simple function. \r\n" + "*/ \r\n" + "public void function foo(any arg1=\"\") { \r\n"
                + "arg1=123; \r\n" + "} \r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testCFScriptIf() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"rateBop\" >\r\n"
                + "<cfargument name=\"quote\">\r\n" + "\r\n" + "<cfscript>\r\n" + "if (xx) {\r\n" + "yy=123;\r\n"
                + "}else{\r\n" + "zz=123;\r\n" + "}\r\n" + "</cfscript>\r\n" + "</cffunction>" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
    }

    @Test
    public void testCFScriptFor() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"rateBop\" >\r\n"
                + "<cfargument name=\"quote\">\r\n" + "\r\n" + "<cfscript>\r\n" + "for (i = 0; i < 100; i + 5) {\r\n"
                + "xx=123;\r\n" + "}\r\n" + "</cfscript>\r\n" + "</cffunction>" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
    }

    @Test
    public void testCFScriptDOT() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"rateBop\" >\r\n" + "<cfscript>\r\n"
                + "request.yy=123;\r\n" + "</cfscript>\r\n" + "</cffunction>" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().get("GLOBAL_VAR");
        assertEquals(1, result.size());
        assertEquals("GLOBAL_VAR", result.get(0).getMessageCode());
    }

    @Test
    public void testCFScriptQueryNew() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"test\" >\r\n" + "</cffunction>\r\n"
                + "<cffunction name=\"rateBop\" >\r\n" + "<cfscript>\r\n" + "var qry = QueryNew('A,B');\r\n"
                + "</cfscript>\r\n" + "</cffunction>" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("QUERYNEW_DATATYPE", result.get(0).getMessageCode());
        assertEquals(6, result.get(0).getLine());
    }

    @Test
    public void testCFScriptForDeclare() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"func1\">\r\n" + "	<cfscript>\r\n"
                + "			for (var a=1;a LTE arraylen(d); a=a+1) {\r\n"
                + "				if(d[a].getName() EQ drawer){\r\n"
                + "					return d[a];\r\n" + "				}\r\n"
                + "			}\r\n" + "	</cfscript>\r\n" + "</cffunction>" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

    @Test
    public void testCFScriptForUnVarred() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"func1\">\r\n" + "	<cfscript>\r\n"
                + "			for (a=1;a LTE arraylen(d); a=a+1) {\r\n"
                + "				if(d[a].getName() EQ drawer){\r\n"
                + "					return d[a];\r\n" + "				}\r\n"
                + "			}\r\n" + "	</cfscript>\r\n" + "</cffunction>" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("MISSING_VAR", result.get(0).getMessageCode());
        assertEquals(4, result.get(0).getLine());
    }

    @Test
    public void testCFScriptAStruct() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"func1\">\r\n" + "	<cfscript>\r\n"
                + "		var a = {};\r\n"
                + "		a.response = processRequest(argumentCollection=arguments);\r\n" + "	</cfscript>\r\n"
                + "</cffunction>" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

    @Test
    public void testGlobalVarCheckerNPE() throws CFLintScanException {
        CFLintResult lintresult = cfBugs.scan("component {\r\n" + "public any function process(){\r\n" + "url.x=123;\r\n" + "}\r\n" + "}",
                "test");
        List<BugInfo> list = lintresult.getIssues().get("GLOBAL_VAR");
        assertEquals(list.toString(), 1, list.size());
        assertEquals("test", list.get(0).getFilename());
        assertEquals("process", list.get(0).getFunction());
        assertEquals(3, list.get(0).getLine());
    }

    @Test
    public void testVarScoper() throws CFLintScanException {
    	String src = "component {\r\n" + "public any function process(){\r\n" + "   x=123;\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(src, "test");
        List<BugInfo> list = lintresult.getIssues().get("MISSING_VAR");
        assertEquals(1, list.size());
        assertEquals("test", list.get(0).getFilename());
        assertEquals("process", list.get(0).getFunction());
        assertEquals(3, list.get(0).getLine());
        assertEquals(4, list.get(0).getColumn());
        assertEquals(48, src.indexOf("x"));
        assertEquals(48, list.get(0).getOffset());
    }

    @Test
    public void testMissingSemi() throws CFLintScanException {
        String src = "component {\n" + " function test() {\n" + "   name_1 \n" +  "    name2 = \"Smith\"\n" + 
                " last.name1 = \"Fred\"\n" + " }\n" + "}";
        CFLintResult lintresult = cfBugs.scan(src, "test");
        List<BugInfo> list = lintresult.getIssues().get("MISSING_SEMI");
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(3, list.get(0).getLine());
        assertEquals(8, list.get(0).getColumn());
        assertEquals(39, src.indexOf("name_1") + "name_1".length() -1 );
        assertEquals(39, list.get(0).getOffset());
    }

    @Test
    public void testMissingSemiTag() throws CFLintScanException {
        String src = "<cfcomponent>\n" + "<cfscript> function test() {\n" + " name_1 \n" +  "    name2 = \"Smith\"\n" + 
                " last.name1 = \"Fred\"\n" + " }\n" + "</cfscript> </component>";
        CFLintResult lintresult = cfBugs.scan(src, "test");
        List<BugInfo> list = lintresult.getIssues().get("MISSING_SEMI");
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(49, src.indexOf("name_1") + "name_1".length() -1 );
        assertEquals(49, list.get(0).getOffset());
        assertEquals(3, list.get(0).getLine());
        assertEquals(6, list.get(0).getColumn());
    }
    
    @Test
    public void testMissingSemiTag2() throws CFLintScanException {
        String src = "<cfscript>   name_1 \n" +  "    name2 = \"Smith\"\n</cfscript>";
        CFLintResult lintresult = cfBugs.scan(src, "test");
        List<BugInfo> list = lintresult.getIssues().get("MISSING_SEMI");
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(18, src.indexOf("name_1") + "name_1".length() -1 );
        assertEquals(18, list.get(0).getOffset());
        assertEquals(1, list.get(0).getLine());
        assertEquals(18, list.get(0).getColumn());
    }

}
