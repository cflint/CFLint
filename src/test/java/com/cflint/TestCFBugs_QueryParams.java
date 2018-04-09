package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFBugs_QueryParams {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("QUERYPARAM_REQ","CFQUERYPARAM_REQ");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testCFScript_QueryParams_OK() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"rateBop\" >\r\n"
                + "<cfquery name=\"LOCAL.categories\">\r\n" + "SELECT * FROM product_categories p\r\n"
                + "WHERE p.id = <cfqueryparam value=\"#LOCAL.id#\"/>\r\n"
                + "and p.name = <cfqueryparam value=\"abc\"/>\r\n" + "</cfquery>\r\n" + "</cffunction>"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

    @Test
    public void testCFScript_QueryParams_Script_OK() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"rateBop\" >\r\n" + "<cfscript>\r\n"
                + "local.query = new Query();" + "local.query.setSql(\"\r\n"
                + "    SELECT id from table where id = :id\");"
                + "local.query.addParam(name=\"id\", cfsqltype=\"CF_SQL_INTEGER\", value=arguments.id, maxlength=10);\r\n"
                + "</cfscript>\r\n" + "</cffunction>" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

    @Test
    public void testCFScript_QueryParams_2Hashes() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"rateBop\" >\r\n"
                + "<cfquery name=\"LOCAL.categories\">\r\n" + "SELECT * FROM product_categories p\r\n"
                + "WHERE p.id = #LOCAL.id#\r\n" + "and p.name = #LOCAL.abc#\r\n" + "</cfquery>\r\n" + "</cffunction>"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(2, result.size());
        assertEquals("CFQUERYPARAM_REQ", result.get(0).getMessageCode());
        assertEquals(5, result.get(0).getLine());
        assertEquals("LOCAL.id", result.get(0).getVariable());
        assertEquals("CFQUERYPARAM_REQ", result.get(1).getMessageCode());
        assertEquals(6, result.get(1).getLine());
    }

    @Test
    public void testCFScript_QueryParams_OutsideFunction() throws CFLintScanException {
        final String cfcSrc = "<cfquery name=\"LOCAL.categories\">\r\n" + "SELECT * FROM product_categories p\r\n"
                + "WHERE p.id = #LOCAL.id#\r\n" + "and p.name = #LOCAL.abc#\r\n" + "</cfquery>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().get("CFQUERYPARAM_REQ");
        assertEquals(2, result.size());
        assertEquals("CFQUERYPARAM_REQ", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("LOCAL.id", result.get(0).getVariable());
        assertEquals("CFQUERYPARAM_REQ", result.get(1).getMessageCode());
        assertEquals(4, result.get(1).getLine());
    }

    @Test
    public void testCFScript_QueryParams_Qoq() throws CFLintScanException {
        final String cfcSrc = "<cfquery name=\"outDocs\" dbtype=\"query\"> Select * From arguments.documents WHERE DocumentType = 'COLD' and TransactionType IN ('1','6') #orderBy# </cfquery> ";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

    @Test
    public void testCFScript_QueryParams_Script_Hashes() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"rateBop\" >\r\n" + "<cfscript>\r\n"
                + "local.query = new Query();" + "local.query.setSql(\"\r\n"
                + "    SELECT id from table where id = #arguments.id#\");" + "</cfscript>\r\n" + "</cffunction>"
                + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().get("QUERYPARAM_REQ");
        assertEquals(1, result.size());
        assertEquals("QUERYPARAM_REQ", result.get(0).getMessageCode());
        assertEquals(4, result.get(0).getLine());
        assertEquals("setSql", result.get(0).getVariable());
    }

    @Test
    public void testCFScript_QueryParams_EscapeHashes() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"rateBop\" >\r\n"
                + "<cfquery name=\"LOCAL.categories\">\r\n" + "SELECT * FROM product_categories p\r\n"
                + "WHERE p.##id = 1\r\n" + "</cfquery>\r\n" + "</cffunction>" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

    @Test
    public void testCFScript_QueryParams_DynamicTableName() throws CFLintScanException {
        final String cfcSrc = "<cfquery name=\"queryName\" datasource=\"#datasourceName#\">\n"
                + "    update #tableName#\n" + "    set fieldName = 'foo'\n" + "</cfquery>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("CFQUERYPARAM_REQ", result.get(0).getMessageCode());
        assertEquals("tableName", result.get(0).getVariable());
    }

    @Test
    public void testCFScript_QueryParams_ignore_offset() throws CFLintScanException {
        final String cfcSrc = "<cfcomponent>\n" + 
            "	<cffunction name=\"foo\">\n" + 
            "		<cfset var fooQry=\"\"/>\n" + 
            "        <cfquery name=\"fooQry\" datasource=\"#arguments.siteDomain#com\" cachedwithin=\"#createTimeSpan(0,0,5,0)#\">\n" + 
            "            SELECT\n" + 
            "                M.firstName\n" + 
            "                <!--- @CFLintIgnore CFQUERYPARAM_REQ --->\n" + 
            "            FROM #application.linkedServerName#.schema.dbo.Comment C WITH (NOLOCK)\n" + 
            "            LEFT OUTER JOIN something SM WITH (NOLOCK)\n" + 
            "                ON C.memberID = SM.memberID\n" + 
            "            INNER JOIN somethingelse m\n" + 
            "                ON m.memberID = sm.memberid\n" + 
            "            LEFT OUTER JOIN #application.linkedServerName#.schema.dbo.FooTable A WITH (NOLOCK)\n" + 
            "                ON C.aID = A.aID\n" + 
            "                AND C.bar = #magicVal# <!--- \n" + 
            "                		@CFLintIgnore CFQUERYPARAM_REQ --->\n" + 
            "            WHERE \n" + 
            "            <!---\n" + 
            "        @CFLintIgnore CFQUERYPARAM_REQ\n" + 
            "        --->\n" + 
            "                eID = #arguments.someNumber# AND\n" + 
            "                moderated = 1\n" + 
            "            ORDER BY\n" + 
            "                cID\n" + 
            "        </cfquery>\n" + 
            "\n" + 
            "	</cffunction>\n" + 
            "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("CFQUERYPARAM_REQ", result.get(0).getMessageCode());
        assertEquals("application.linkedServerName", result.get(0).getVariable());
        assertEquals(13, result.get(0).getLine());
        assertEquals(575, cfcSrc.indexOf("application.linkedServerName", cfcSrc.indexOf("application.linkedServerName") +1 )); // get the non-ignored one
        assertEquals(575, result.get(0).getOffset());
    }

    @Test
    public void testCFScript_QueryParams_queryExecute_OK() throws CFLintScanException {
        final String cfcSrc = "component {\r\n" 
            + "public void function getData() {\r\n"
            + "queryExecute(\"\r\n"
            + "   SELECT id from table where id = :id\r\n"
            + "\",{\r\n"
            + "   id = {cfsqltype=\"CF_SQL_INTEGER\", value=arguments.id, maxlength=10}\r\n"
            + "});\r\n}\r\n}";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final Map<String, List<BugInfo>> result = lintresult.getIssues();
        assertEquals(0, result.size());
    }

    @Test
    public void testCFScript_QueryParams_queryExecute_Hashes() throws CFLintScanException {
        final String cfcSrc = "component {\r\n" 
            + "public void function getData() {\r\n"
            + "queryExecute(\"\r\n"
            + "   SELECT id from table where id = #arguments.id#\r\n"
            + "\");\r\n}\r\n}";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().get("QUERYPARAM_REQ");
        assertEquals(1, result.size());
        assertEquals("QUERYPARAM_REQ", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
    }

}
