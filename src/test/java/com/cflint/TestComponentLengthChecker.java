package com.cflint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestComponentLengthChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("EXCESSIVE_COMPONENT_LENGTH");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testGoodFunction() throws CFLintScanException {
        final String cfcSrc = "component name=\"EventQuery\" {\r\n"
                + "function getSourceTemplates (required sourceTemplateNum) {\r\n" + "var result = QueryExecute(\"\r\n"
                + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n" + "FROM tdc_doc_infocard\r\n"
                + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "return result;\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(true, lintresult.getIssues().isEmpty());
    }

    @Test
    public void testBadFunction() throws CFLintScanException {
        final StringBuilder cfcSrc = new StringBuilder("component name=\"EventQuery\" {\r\n");
        for(int i=0;i<505;i++){
            cfcSrc.append("  function getSourceTemplates (required sourceTemplateNum) {\r\n" + 
                        "    return 123;\r\n" +
                        "  }\r\n");
        }
        cfcSrc.append("}");
        CFLintResult lintresult = cfBugs.scan(cfcSrc.toString(), "test");
        final List<BugInfo> result = lintresult.getIssues().get("EXCESSIVE_COMPONENT_LENGTH");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("EXCESSIVE_COMPONENT_LENGTH", result.get(0).getMessageCode());
    }
    @Test
    public void testBadFunctionCFML() throws CFLintScanException {
        final StringBuilder cfcSrc = new StringBuilder("<cfcomponent> {\r\n");
        for(int i=0;i<505;i++){
            cfcSrc.append("<cffunction>\r\n" + 
                        "    <cfreturn 123/>\r\n" +
                        " </cffunction>\r\n");
        }
        cfcSrc.append("</cfcomponent>");
        CFLintResult lintresult = cfBugs.scan(cfcSrc.toString(), "test");
        final List<BugInfo> result = lintresult.getIssues().get("EXCESSIVE_COMPONENT_LENGTH");
        assertEquals(1, result.size());
        assertEquals("EXCESSIVE_COMPONENT_LENGTH", result.get(0).getMessageCode());
    }

}
