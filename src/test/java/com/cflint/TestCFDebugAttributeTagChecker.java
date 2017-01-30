package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;

import cfml.parsing.reporting.ParseException;

public class TestCFDebugAttributeTagChecker {

	private CFLint cfBugs;

	@Before
	public void setUp() throws Exception{
		final com.cflint.config.CFLintConfiguration conf = CFLintConfig.createDefaultLimited("CFDebugAttributeChecker");
		cfBugs = new CFLint(conf);
	}

	@Test
	public void test_debugNoVal() throws ParseException, IOException {
		final String cfcSrc = "<cfquery name=\"TestQuery\" datasource=\"cfdocexamples\" debug> \n" + 
				"    SELECT * FROM TestTable \n" + 
				"</cfquery>";
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().get("AVOID_USING_DEBUG_ATTR").size());
	}

	@Test
	public void test_debugNoValCASE() throws ParseException, IOException {
		final String cfcSrc = "<cfquery name=\"TestQuery\" datasource=\"cfdocexamples\" DEBUG> \n" + 
				"    SELECT * FROM TestTable \n" + 
				"</cfquery>";
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().get("AVOID_USING_DEBUG_ATTR").size());
	}

	@Test
	public void test_debugWithVal() throws ParseException, IOException {
		final String cfcSrc = "<cfquery name=\"TestQuery\" datasource=\"cfdocexamples\" debug=false> \n" + 
				"    SELECT * FROM TestTable \n" + 
				"</cfquery>";
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().get("AVOID_USING_DEBUG_ATTR").size());
	}

	@Test
	public void test_debugWithQuotedVal() throws ParseException, IOException {
		final String cfcSrc = "<cfquery name=\"TestQuery\" datasource=\"cfdocexamples\" debug=\"false\"> \n" + 
				"    SELECT * FROM TestTable \n" + 
				"</cfquery>";
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().get("AVOID_USING_DEBUG_ATTR").size());
	}
	

	@Test
	public void test_cfsetting_debug_no() throws ParseException, IOException {
		final String cfcSrc = "<cfsetting showDebugOutput=\"No\">";
		cfBugs.process(cfcSrc, "test");
		assertEquals(0, cfBugs.getBugs().getBugList().size());
	}

	@Test
	public void test_cfsetting_debug_yes() throws ParseException, IOException {
		final String cfcSrc = "<cfsetting showDebugOutput=\"Yes\">";
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().get("AVOID_USING_CFSETTING_DEBUG").size());
	}


	@Test
	public void test_cfsetting_no_attributes() throws ParseException, IOException {
		final String cfcSrc = "<cfset var a = 1>";
		cfBugs.process(cfcSrc, "test");
		assertEquals(0, cfBugs.getBugs().getBugList().size());
	}
}
