package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.core.CFXTagChecker;

import cfml.parsing.reporting.ParseException;

public class TestCFUpdateTagChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() throws Exception{
		final CFLintConfig conf = new CFLintConfig();
		final PluginInfoRule pluginRuleX = new PluginInfoRule();
		pluginRuleX.setName("CFXTagChecker");
		pluginRuleX.addParameter("tagName", "cfupdate");
		conf.getRules().add(pluginRuleX);
		final PluginMessage pluginMessageX = new PluginMessage("AVOID_USING_CFUPDATE_TAG");
		pluginMessageX.setSeverity("WARNING");
		pluginMessageX
				.setMessageText("Avoid Leaving <${tagName}> tags in committed code. Debug information should be ommited from release code");
		pluginRuleX.getMessages().add(pluginMessageX);
		final CFXTagChecker checker = new CFXTagChecker();
		checker.setParameter("tagName", "cfupdate");
		cfBugs = new CFLint(conf, checker);
	}

	@Test
	public void test_BAD() throws ParseException, IOException {
		final String cfcSrc = "<CFUPDATE DATASOURCE=\"ds_name\"" + "DBTYPE=\"type\"" + "DBSERVER=\"dbms\""
				+ "DBNAME=\"database name\"" + "TABLENAME=\"table_name\"" + "TABLEOWNER=\"name\""
				+ "TABLEQUALIFIER=\"qualifier\"" + "USERNAME=\"username\"" + "PASSWORD=\"password\""
				+ "PROVIDER=\"COMProvider\"" + "PROVIDERDSN=\"datasource\"" + "FORMFIELDS=\"field_names\">";
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().size());
	}

	@Test
	public void test_GOOD() throws ParseException, IOException {
		final String cfcSrc = "<cfinsert " + "dataSource = \"data source name\" " + "tableName = \"table name\" "
				+ "formFields = \"formfield1, formfield2, ...\" " + "password = \"password\" "
				+ "tableOwner = \"owner\" " + "tableQualifier = \"table qualifier\" " + "username = \"user name\">";
		cfBugs.process(cfcSrc, "test");
		assertEquals(0, cfBugs.getBugs().getBugList().size());
	}

}
