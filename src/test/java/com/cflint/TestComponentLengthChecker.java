package com.cflint;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;

import cfml.parsing.reporting.ParseException;

public class TestComponentLengthChecker {

	private CFLint cfBugs;
	
	@Before
	public void setUp() throws Exception{
		final com.cflint.config.CFLintConfiguration conf = CFLintConfig.createDefaultLimited("ComponentLengthChecker");
		cfBugs = new CFLint(conf);
	}
	
	@Test
	public void testGoodFunction() throws ParseException, IOException{
		final String cfcSrc = "component name=\"EventQuery\" {\r\n" +
									"function getSourceTemplates (required sourceTemplateNum) {\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"return result;\r\n" +
									"}\r\n" +
								"}";
		assertEquals(true, cfBugs.getBugs().getBugList().isEmpty());
	}

	@Test
	public void testBadFunction() throws ParseException, IOException{
		final String cfcSrc = "component name=\"EventQuery\" {\r\n" +
									"function getSourceTemplates (required sourceTemplateNum) {\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"return result;\r\n" +
									"}\r\n" +
									"function getSourceTemplates2 (required sourceTemplateNum) {\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"return result;\r\n" +
									"}\r\n" +	
									"function getSourceTemplates3 (required sourceTemplateNum) {\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"return result;\r\n" +
									"}\r\n" +
									"function getSourceTemplates4 (required sourceTemplateNum) {\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"return result;\r\n" +
										"function getSourceTemplates5 (required sourceTemplateNum) {\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"var result = QueryExecute(\"\r\n" +
											"SELECT info_card_id, document_num, revision_nm, title_nm\r\n" +
											"FROM tdc_doc_infocard\r\n" +
											"WHERE info_card_id = :sourceTemplateNum\r\n" +
											"ORDER BY UPPER(document_num) ASC, revision_seq DESC\r\n" +
										"\", { sourceTemplateNum = { value=\"#sourceTemplateNum#\", cfsqltype=\"cf_sql_varchar\" }});\r\n" +
										"return result;\r\n" +
									"}\r\n" +
									"}\r\n" +						
								"}";
		cfBugs.process(cfcSrc,"test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().get("EXCESSIVE_COMPONENT_LENGTH");
		assertEquals(1, result.size());
		assertEquals("EXCESSIVE_COMPONENT_LENGTH", result.get(0).getMessageCode());
	}


}
