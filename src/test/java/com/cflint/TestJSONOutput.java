package com.cflint;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cfml.parsing.reporting.ParseException;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.core.FunctionLengthChecker;
import com.cflint.config.ConfigRuntime;
import com.cflint.JSONOutput;
import java.util.Map;
import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.io.FileWriter;

public class TestJSONOutput {

	private CFLint cfBugs;
	private HashMap list;

	@Before
	public void setUp(){
		ConfigRuntime conf = new ConfigRuntime();
		PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("FunctionLengthChecker");
		conf.getRules().add(pluginRule);
		PluginMessage pluginMessage = new PluginMessage("EXCESSIVE_FUNCTION_LENGTH");
		pluginMessage.setSeverity("INFO");
		pluginMessage.setMessageText("Variable ${variable} is not declared with a var statement.");
		pluginRule.getMessages().add(pluginMessage);
		list = new HashMap();
		cfBugs = new CFLint(conf, new FunctionLengthChecker());
	}
	
	@Test
	public void testGoodFunction() throws ParseException, IOException{
		String cfcSrc = "component name=\"EventQuery\" {\r\n" +
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

		cfBugs.process(cfcSrc,"test");
		final List<BugInfo> bugInfo = cfBugs.getBugs().getBugList().values().iterator().next();
		JSONObject issue = new JSONObject();
		issue.put("severity", bugInfo.get(0).getSeverity());
		issue.put("id", bugInfo.get(0).getMessageCode());
		issue.put("message", bugInfo.get(0).getMessage());
		JSONObject details = new JSONObject();
		details.put("file", bugInfo.get(0).getFilename());
		// details.put("fileName", .substring(Math.max(bugInfo.get(0).getFilename().lastIndexOf('/'), bugInfo.get(0).getFilename().lastIndexOf('\\')));
		// details.put("line", Integer.valueOf(bugInfo.getColumn()).toString());

		issue.put("location", details);
		JSONObject output = new JSONOutput().output(cfBugs.getBugs(), new FileWriter("output"));
		
		// output.put("issue");
		assertEquals(1, output);
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
								"}";
		cfBugs.process(cfcSrc,"test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		assertEquals(1, result.size());
		assertEquals("EXCESSIVE_FUNCTION_LENGTH", result.get(0).getMessageCode());
	}
}
