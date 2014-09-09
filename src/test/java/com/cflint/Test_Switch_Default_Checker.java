package com.cflint;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.BugInfo;
import com.cflint.CFLint;
import com.cflint.StackHandler;
import com.cflint.plugins.core.CFSwitchDefaultChecker;

import cfml.parsing.CFMLParser;
import cfml.parsing.CFMLSource;
import cfml.parsing.cfscript.ParseException;
import static org.junit.Assert.*;

public class Test_Switch_Default_Checker {

	StackHandler handler = null;
	
	@Before
	public void setUp(){
		handler = new StackHandler();
	}
	
	@Test
	public void goodCFML() throws ParseException, IOException {
		final String cfcSrc = 	"<cfswitch expression=\"#Trim(Department)#\">/n" +
    							"	<cfcase value=\"Sales\">/n" +
        						"		#FirstName# #LastName# is in <b>sales</b><br><br>" +
    							"	</cfcase>" +
    							"	<cfcase value=\"Accounting\">" +
								"    	#FirstName# #LastName# is in <b>accounting</b><br><br>" +
								"	</cfcase> <cfcase value=\"Administration\">" +
								"    	#FirstName# #LastName# is in <b>administration</b><br><br>" +
								"	</cfcase>" +
								"	<cfdefaultcase>" +
								"    	#FirstName# #LastName# is not in Sales, Accounting, or" +
								"        	Administration.<br><br>" +
								"	</cfdefaultcase>" +
								"</cfswitch> ";
		CFLint cfBugs = new CFLint(new CFSwitchDefaultChecker());
		cfBugs.process(cfcSrc,"test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0, result.size());
	}
	
	@Test
	public void badCFML() throws ParseException, IOException {
		final String cfcSrc = 	"<cfswitch expression=\"#Trim(Department)#\">/n" +
    							"	<cfcase value=\"Sales\">/n" +
        						"		#FirstName# #LastName# is in <b>sales</b><br><br>" +
    							"	</cfcase>" +
    							"	<cfcase value=\"Accounting\">" +
								"    	#FirstName# #LastName# is in <b>accounting</b><br><br>" +
								"	</cfcase> <cfcase value=\"Administration\">" +
								"    	#FirstName# #LastName# is in <b>administration</b><br><br>" +
								"	</cfcase>" +
								"</cfswitch> ";
		CFLint cfBugs = new CFLint(new CFSwitchDefaultChecker());
		cfBugs.process(cfcSrc,"test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(1, result.size());
	}
	
	@Test
	public void goodCFScript() throws ParseException, IOException{
		final String cfcSrc=	"switch(rejectCode[applicant]) {" +
			    				"	case \"score\": " +
			    				"		WriteOutput(\"Reject reason: Score was too low.<br>\"); " +
			        			"		break; " +
			    				"	case \"late\": " +
			    				"		WriteOutput(\"Reject reason: Application was late.<br>\"); " +
			        			"		break; " +
			    				"	default: " +
			        			"		WriteOutput(\"Rejected with invalid reason code.<br>\"); " +
			        			"}";
		CFLint cfBugs = new CFLint(new CFSwitchDefaultChecker());
		cfBugs.process(cfcSrc,"test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0, result.size());
	}
}
