package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintConfigurationException;
import com.cflint.exception.CFLintScanException;

public class Test_Switch_Default_Checker {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws IOException, CFLintConfigurationException {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("NO_DEFAULT_INSIDE_SWITCH");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void goodCFML() throws CFLintScanException {

        final String cfcSrc = "<cfswitch expression=\"#Trim(Department)#\">/n" + "	<cfcase value=\"Sales\">/n"
                + "		#FirstName# #LastName# is in <b>sales</b><br><br>" + "	</cfcase>"
                + "	<cfcase value=\"Accounting\">" + "    	#FirstName# #LastName# is in <b>accounting</b><br><br>"
                + "	</cfcase> <cfcase value=\"Administration\">"
                + "    	#FirstName# #LastName# is in <b>administration</b><br><br>" + "	</cfcase>" + "	<cfdefaultcase>"
                + "    	#FirstName# #LastName# is not in Sales, Accounting, or" + "        	Administration.<br><br>"
                + "	</cfdefaultcase>" + "</cfswitch> ";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(0, result.size());
    }

    @Test
    public void badCFML() throws CFLintScanException {
        final String cfcSrc = "<cfswitch expression=\"#Trim(Department)#\">/n" + "	<cfcase value=\"Sales\">/n"
                + "		#FirstName# #LastName# is in <b>sales</b><br><br>" + "	</cfcase>"
                + "	<cfcase value=\"Accounting\">" + "    	#FirstName# #LastName# is in <b>accounting</b><br><br>"
                + "	</cfcase> <cfcase value=\"Administration\">"
                + "    	#FirstName# #LastName# is in <b>administration</b><br><br>" + "	</cfcase>" + "</cfswitch> ";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(1, result.size());
    }

    @Test
    public void goodCFScript() throws CFLintScanException {
        final String cfcSrc = "switch(rejectCode[applicant]) {" + "	case \"score\": "
                + "		WriteOutput(\"Reject reason: Score was too low.<br>\"); " + "		break; "
                + "	case \"late\": " + "		WriteOutput(\"Reject reason: Application was late.<br>\"); "
                + "		break; " + "	default: "
                + "		WriteOutput(\"Rejected with invalid reason code.<br>\"); " + "}";
        CFLintResult lintresult = cfBugs.scan(cfcSrc, "test");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(0, result.size());
    }
}
