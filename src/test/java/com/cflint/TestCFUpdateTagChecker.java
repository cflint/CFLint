package com.cflint;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFUpdateTagChecker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("AVOID_USING_CFUPDATE_TAG");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void test_BAD() throws CFLintScanException {
        final String cfcSrc = "<CFUPDATE DATASOURCE=\"ds_name\"" + "DBTYPE=\"type\"" + "DBSERVER=\"dbms\""
                + "DBNAME=\"database name\"" + "TABLENAME=\"table_name\"" + "TABLEOWNER=\"name\""
                + "TABLEQUALIFIER=\"qualifier\"" + "USERNAME=\"username\"" + "PASSWORD=\"password\""
                + "PROVIDER=\"COMProvider\"" + "PROVIDERDSN=\"datasource\"" + "FORMFIELDS=\"field_names\">";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(1, lintresult.getIssues().size());
    }

    @Test
    public void test_GOOD() throws CFLintScanException {
        final String cfcSrc = "<cfinsert " + "dataSource = \"data source name\" " + "tableName = \"table name\" "
                + "formFields = \"formfield1, formfield2, ...\" " + "password = \"password\" "
                + "tableOwner = \"owner\" " + "tableQualifier = \"table qualifier\" " + "username = \"user name\">";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        assertEquals(0, lintresult.getIssues().size());
    }

}
