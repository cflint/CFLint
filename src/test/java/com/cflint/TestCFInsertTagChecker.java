package com.cflint;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.cflint.CFLint;
import com.cflint.StackHandler;
import com.cflint.plugins.core.CFInsertTagChecker;

import cfml.parsing.cfscript.ParseException;
import static org.junit.Assert.*;

public class TestCFInsertTagChecker {
	
	@Test
	public void test_BAD() throws ParseException, IOException {
		final String cfcSrc = "<cfinsert " +
			    "dataSource = \"data source name\" " + 
			    "tableName = \"table name\" " + 
			    "formFields = \"formfield1, formfield2, ...\" " + 
			    "password = \"password\" " + 
			    "tableOwner = \"owner\" " + 
			    "tableQualifier = \"table qualifier\" " +  
			    "username = \"user name\">";
		final CFLint cfBugs = new CFLint(new CFInsertTagChecker());
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().size());
	}

}
