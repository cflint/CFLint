package com.cflint;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.cflint.CFLint;
import com.cflint.StackHandler;
import com.cflint.plugins.core.CFModuleTagChecker;

import cfml.parsing.cfscript.ParseException;
import static org.junit.Assert.*;

public class TestCFModuleTagChecker {

	@Test
	public void test_BAD() throws ParseException, IOException {
		final String cfcSrc = "<cfmodule template=\"tagsExchRateCalculator.cfm\">";
		final CFLint cfBugs = new CFLint(new CFModuleTagChecker());
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().size());
	}

}
