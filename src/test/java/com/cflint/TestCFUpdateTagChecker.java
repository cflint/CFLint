package com.cflint;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.cflint.CFLint;
import com.cflint.StackHandler;
import com.cflint.plugins.core.CFUpdateTagChecker;

import cfml.parsing.cfscript.ParseException;
import static org.junit.Assert.*;

public class TestCFUpdateTagChecker {

	@Test
	public void test_BAD() throws ParseException, IOException {
		final String cfcSrc = "<CFUPDATE DATASOURCE=\"ds_name\"" +
			    "DBTYPE=\"type\"" +
			    "DBSERVER=\"dbms\"" +
			    "DBNAME=\"database name\"" +
			    "TABLENAME=\"table_name\"" +
			    "TABLEOWNER=\"name\"" +
			    "TABLEQUALIFIER=\"qualifier\"" +
			    "USERNAME=\"username\"" +
			    "PASSWORD=\"password\"" +
			    "PROVIDER=\"COMProvider\"" + 
			    "PROVIDERDSN=\"datasource\"" + 
			    "FORMFIELDS=\"field_names\">";
		final CFLint cfBugs = new CFLint(new CFUpdateTagChecker());
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().size());
	}

}
