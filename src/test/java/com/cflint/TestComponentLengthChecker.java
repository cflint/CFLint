package com.cflint;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cfml.parsing.reporting.ParseException;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.core.ComponentLengthChecker;
import com.cflint.config.ConfigRuntime;
import java.util.Map;
import java.util.HashMap;

public class TestComponentLengthChecker {

	private CFLint cfBugs;
	private HashMap list;
	
	@Before
	public void setUp(){
		ConfigRuntime conf = new ConfigRuntime();
		PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("ComponentLengthChecker");
		conf.getRules().add(pluginRule);
		PluginMessage pluginMessage = new PluginMessage("EXCESSIVE_COMPONENT_LENGTH");
		pluginMessage.setSeverity("INFO");
		pluginMessage.setMessageText("Component is too long.");
		pluginRule.getMessages().add(pluginMessage);
		list = new HashMap();
		cfBugs = new CFLint(conf, new ComponentLengthChecker());
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
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("EXCESSIVE_COMPONENT_LENGTH", result.get(0).getMessageCode());
	}


}
