package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.QueryParamChecker;

import cfml.parsing.reporting.ParseException;

public class TestCFBugs_QueryParams {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("QueryParamChecker");
		conf.getRules().add(pluginRule);
		PluginMessage pluginMessage = new PluginMessage("QUERYPARAM_REQ");
		pluginMessage.setSeverity("WARNING");
		pluginMessage.setMessageText("setSql() statement should use .addParam() instead of #'s for security.");
		pluginRule.getMessages().add(pluginMessage);

		pluginRule = new PluginInfoRule();
		pluginRule.setName("QueryParamChecker");
		conf.getRules().add(pluginRule);
		pluginMessage = new PluginMessage("CFQUERYPARAM_REQ");
		pluginMessage.setSeverity("WARNING");
		pluginMessage.setMessageText("<${tag} name=\"${variable}\"> should use <cfqueryparam/> for security reasons.");
		pluginRule.getMessages().add(pluginMessage);

		cfBugs = new CFLint(conf, new QueryParamChecker());
	}

	@Test
	public void testCFScript_QueryParams_OK() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"rateBop\" >\r\n"
				+ "<cfquery name=\"LOCAL.categories\">\r\n" + "SELECT * FROM product_categories p\r\n"
				+ "WHERE p.id = <cfqueryparam value=\"#LOCAL.id#\"/>\r\n"
				+ "and p.name = <cfqueryparam value=\"abc\"/>\r\n" + "</cfquery>\r\n" + "</cffunction>"
				+ "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getFlatBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testCFScript_QueryParams_Script_OK() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n"
				+ "<cffunction name=\"rateBop\" >\r\n"
				+ "<cfscript>\r\n"
				+ "local.query = new Query();"
				+ "local.query.setSql(\"\r\n"
				+ "    SELECT id from table where id = :id\");"
				+ "local.query.addParam(name=\"id\", cfsqltype=\"CF_SQL_INTEGER\", value=arguments.id, maxlength=10);\r\n"
				+ "</cfscript>\r\n" + "</cffunction>" + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getFlatBugList();
		assertEquals(0, result.size());
	}

	@Test
	public void testCFScript_QueryParams_2Hashes() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"rateBop\" >\r\n"
				+ "<cfquery name=\"LOCAL.categories\">\r\n" + "SELECT * FROM product_categories p\r\n"
				+ "WHERE p.id = #LOCAL.id#\r\n" + "and p.name = #LOCAL.abc#\r\n" + "</cfquery>\r\n" + "</cffunction>"
				+ "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getFlatBugList();
		for (final BugInfo bi : result) {
			System.out.println(bi);
		}
		assertEquals(2, result.size());
		assertEquals("CFQUERYPARAM_REQ", result.get(0).getMessageCode());
		assertEquals(3, result.get(0).getLine());
		assertEquals("LOCAL.categories", result.get(0).getVariable());
		assertEquals("CFQUERYPARAM_REQ", result.get(1).getMessageCode());
		assertEquals(3, result.get(1).getLine());
	}

	@Test
	public void testCFScript_QueryParams_OutsideFunction() throws ParseException, IOException {
		final String cfcSrc = "<cfquery name=\"LOCAL.categories\">\r\n" + "SELECT * FROM product_categories p\r\n"
				+ "WHERE p.id = #LOCAL.id#\r\n" + "and p.name = #LOCAL.abc#\r\n" + "</cfquery>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getFlatBugList();
		for (final BugInfo bi : result) {
			System.out.println(bi);
		}
		assertEquals(2, result.size());
		assertEquals("CFQUERYPARAM_REQ", result.get(0).getMessageCode());
		assertEquals(1, result.get(0).getLine());
		assertEquals("LOCAL.categories", result.get(0).getVariable());
		assertEquals("CFQUERYPARAM_REQ", result.get(1).getMessageCode());
		assertEquals(1, result.get(1).getLine());
	}
	
	@Test
	public void testCFScript_QueryParams_Qoq() throws ParseException, IOException {
		final String cfcSrc = "<cfquery name=\"outDocs\" dbtype=\"query\"> Select * From arguments.documents WHERE DocumentType = 'COLD' and TransactionType IN ('1','6') #orderBy# </cfquery> ";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getFlatBugList();
		for (final BugInfo bi : result) {
			System.out.println(bi);
		}
		assertEquals(0, result.size());
	}

	@Test
	public void testCFScript_QueryParams_Script_Hashes() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"rateBop\" >\r\n" + "<cfscript>\r\n"
				+ "local.query = new Query();" + "local.query.setSql(\"\r\n"
				+ "    SELECT id from table where id = #arguments.id#\");" + "</cfscript>\r\n" + "</cffunction>"
				+ "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
		for (final BugInfo bi : result) {
			System.out.println(bi);
		}
		assertEquals(1, result.size());
		assertEquals("QUERYPARAM_REQ", result.get(0).getMessageCode());
		assertEquals(4, result.get(0).getLine());
		assertEquals("setSql", result.get(0).getVariable());
	}

	@Test
	public void testCFScript_QueryParams_EscapeHashes() throws ParseException, IOException {
		final String cfcSrc = "<cfcomponent>\r\n" + "<cffunction name=\"rateBop\" >\r\n"
				+ "<cfquery name=\"LOCAL.categories\">\r\n" + "SELECT * FROM product_categories p\r\n"
				+ "WHERE p.##id = 1\r\n" + "</cfquery>\r\n" + "</cffunction>" + "</cfcomponent>";
		cfBugs.process(cfcSrc, "test");
		final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
		assertEquals(0, result.size());
	}
	
	@Test
	public void testCFScript_QueryParams_DynamicTableName() throws ParseException, IOException {
		final String cfcSrc = "<cfquery name=\"queryName\" datasource=\"#datasourceName#\">\n" + 
				"    update #tableName#\n" + 
				"    set fieldName = 'foo';\n" + 
				"</cfquery>";
		cfBugs.process(cfcSrc, "test");
		final List<BugInfo> result = cfBugs.getBugs().getFlatBugList();
		for (final BugInfo bi : result) {
			System.out.println(bi);
		}
		assertEquals(1, result.size());
		assertEquals("CFQUERYPARAM_REQ", result.get(0).getMessageCode());
		assertEquals("queryName", result.get(0).getVariable());
		System.out.println(result.get(0).getExpression());
	}
}
