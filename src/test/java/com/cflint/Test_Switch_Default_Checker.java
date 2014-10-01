package com.cflint;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cfml.parsing.cfscript.ParseException;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.ConfigRuntime;
import com.cflint.plugins.core.CFSwitchDefaultChecker;

public class Test_Switch_Default_Checker {

	private CFLint cfBugs;

	@Before
	public void setUp() {
		final ConfigRuntime conf = new ConfigRuntime();
		final PluginInfoRule pluginRule = new PluginInfoRule();
		pluginRule.setName("CFSwitchDefaultChecker");
		conf.getRules().add(pluginRule);
		final PluginMessage pluginMessage = new PluginMessage("NO_DEFAULT_INSIDE_SWITCH");
		pluginMessage.setSeverity("WARNING");
		pluginMessage.setMessageText("Not having a Default statement defined for a switch could pose potential issues");
		pluginRule.getMessages().add(pluginMessage);

		cfBugs = new CFLint(conf, new CFSwitchDefaultChecker());
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
		cfBugs.process(cfcSrc,"test");
		Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
		assertEquals(0, result.size());
	}
}
