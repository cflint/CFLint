package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintConfigurationException;
import com.cflint.exception.CFLintScanException;

public class TestFunctionLengthChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws CFLintConfigurationException {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("EXCESSIVE_FUNCTION_LENGTH");
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
        final String cfcSrc = "component name=\"EventQuery\" {\r\n"
                + "function getSourceTemplates (required sourceTemplateNum) {\r\n" + "var result = QueryExecute(\"\r\n"
                + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n" + "FROM tdc_doc_infocard\r\n"
                + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "var result = QueryExecute(\"\r\n" + "SELECT info_card_id, document_num, revision_nm, title_nm\r\n"
                + "FROM tdc_doc_infocard\r\n" + "WHERE info_card_id = :sourceTemplateNum\r\n"
                + "ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n"
                + "\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n"
                + "return result;\r\n" + "}\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("EXCESSIVE_FUNCTION_LENGTH", result.get(0).getMessageCode());
    }

}
