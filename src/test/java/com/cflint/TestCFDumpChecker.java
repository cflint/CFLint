package com.cflint;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.cflint.CFLint;
import com.cflint.StackHandler;
import com.cflint.plugins.core.CFDumpChecker;

import cfml.parsing.cfscript.ParseException;
import static org.junit.Assert.*;

public class TestCFDumpChecker {

	@Test
	public void test_BAD() throws ParseException, IOException {
		final String cfcSrc = "<CFDUMP " +
				    " var = \"#variable#\">";
		final CFLint cfBugs = new CFLint(new CFDumpChecker());
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().size());
	}

}
