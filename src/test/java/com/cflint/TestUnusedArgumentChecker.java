package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;

import cfml.parsing.reporting.ParseException;

public class TestUnusedArgumentChecker {

    private CFLint cfBugs;

    @Before
    public void setUp() throws Exception{
        final com.cflint.config.CFLintConfiguration conf = CFLintConfig.createDefaultLimited("UnusedArgumentChecker");
        cfBugs = new CFLint(conf);
    }

    @Test
    public void testNoArgumentsInScript() throws ParseException, IOException {
        final String scriptSrc = "<cfscript>\r\n"
            + "component {\r\n"
            + "function dummyFunction() {\r\n"
            + "}\r\n"
            + "}\r\n"
            + "</cfscript>";
            
        cfBugs.process(scriptSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testArgumentsUsedInScript() throws ParseException, IOException {
        final String scriptSrc = "<cfscript>\r\n"
            + "component {\r\n"
            + "function sum(a,b) {\r\n"
            + "return a + b;\r\n"
            + "}\r\n"
            + "}\r\n"
            + "</cfscript>";
            
        cfBugs.process(scriptSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testArgumentsNotUsedInScript() throws ParseException, IOException {
        final String scriptSrc = "<cfscript>\r\n"
            + "component {\r\n"
            + "function sum(a,b) {\r\n"
            + "c = a + b;"
            + "return c;\r\n"
            + "}\r\n"
            + "}\r\n"
            + "</cfscript>";
            
        cfBugs.process(scriptSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testArgumentsNotUsedInMethodScript() throws ParseException, IOException {
        final String scriptSrc = "<cfscript>\r\n"
            + "component {\r\n"
            + "function sum(a,b) {\r\n"
            + "return other(a,b);\r\n"
            + "}\r\n"
            + "}\r\n"
            + "</cfscript>";
            
        cfBugs.process(scriptSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testArgumentsNotUsedInReturnScript() throws ParseException, IOException {
        final String scriptSrc = "<cfscript>\r\n"
            + "component {\r\n"
            + "function sum(a,b) {\r\n"
            + "return a;\r\n"
            + "}\r\n"
            + "}\r\n"
            + "</cfscript>";
            
        cfBugs.process(scriptSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("UNUSED_METHOD_ARGUMENT", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
    }

    @Test
    public void testMultipleArgumentsNotUsedScript() throws ParseException, IOException {
        final String scriptSrc = "<cfscript>\r\n"
            + "component {\r\n"
            + "function sum(a,b,c,d) {\r\n"
            + "return a;\r\n"
            + "}\r\n"
            + "}\r\n"
            + "</cfscript>";
            
        cfBugs.process(scriptSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(3, result.size());
        assertEquals("UNUSED_METHOD_ARGUMENT", result.get(0).getMessageCode());
        assertEquals(3, result.get(0).getLine());
        assertEquals("UNUSED_METHOD_ARGUMENT", result.get(1).getMessageCode());
        assertEquals(3, result.get(1).getLine());
        assertEquals("UNUSED_METHOD_ARGUMENT", result.get(2).getMessageCode());
        assertEquals(3, result.get(2).getLine());
    }

    @Test
    public void testArgumentsUsedInScriptWithArgumentsScope() throws ParseException, IOException {
        final String scriptSrc = "<cfscript>\r\n"
            + "component {\r\n"
            + "function sum(a,b) {\r\n"
            + "return arguments.a + arguments.b;\r\n"
            + "}\r\n"
            + "}\r\n"
            + "</cfscript>";
            
        cfBugs.process(scriptSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testNoArgumentsInTag() throws ParseException, IOException {
        final String tagSrc = "<cfcomponent>\r\n"
            + "<cffunction name=\"dummyFunction\">\r\n"
            + "</cffunction>\r\n"
            + "</cfcomponent>\r\n";
            
        cfBugs.process(tagSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testArgumentsUsedInTag() throws ParseException, IOException {
        final String tagSrc = "<cfcomponent>\r\n"
            + "<cffunction name=\"sum\">\r\n"
            + "<cfargument name=\"a\">\r\n"
            + "<cfargument name=\"b\">\r\n"
            + "<cfreturn a + b>\r\n"
            + "</cffunction>\r\n"
            + "</cfcomponent>\r\n";
            
        cfBugs.process(tagSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testArgumentsNotUsedInTag() throws ParseException, IOException {
        final String tagSrc = "<cfcomponent>\r\n"
            + "<cffunction name=\"sum\">\r\n"
            + "<cfargument name=\"a\">\r\n"
            + "<cfargument name=\"b\">\r\n"
            + "<cfset c = a + b>"
            + "<cfreturn c>\r\n"
            + "</cffunction>\r\n"
            + "</cfcomponent>\r\n";
            
        cfBugs.process(tagSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testArgumentsNotUsedInMethodTag() throws ParseException, IOException {
        final String tagSrc = "<cfcomponent>\r\n"
            + "<cffunction name=\"sum\">\r\n"
            + "<cfargument name=\"a\">\r\n"
            + "<cfargument name=\"b\">\r\n"
            + "<cfreturn other(a,b)>\r\n"
            + "</cffunction>\r\n"
            + "</cfcomponent>\r\n";
            
        cfBugs.process(tagSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Test
    public void testArgumentsNotUsedInReturnTag() throws ParseException, IOException {
        final String tagSrc = "<cfcomponent>\r\n"
            + "<cffunction name=\"sum\">\r\n"
            + "<cfargument name=\"a\">\r\n"
            + "<cfargument name=\"b\">\r\n"
            + "<cfreturn a>\r\n"
            + "</cffunction>\r\n"
            + "</cfcomponent>\r\n";
            
        cfBugs.process(tagSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("UNUSED_METHOD_ARGUMENT", result.get(0).getMessageCode());
        assertEquals(4, result.get(0).getLine());
    }

    @Test
    public void testMultipleArgumentsNotUsedTag() throws ParseException, IOException {
        final String tagSrc = "<cfcomponent>\r\n"
            + "<cffunction name=\"sum\">\r\n"
            + "<cfargument name=\"a\">\r\n"
            + "<cfargument name=\"b\">\r\n"
            + "<cfargument name=\"c\">\r\n"
            + "<cfargument name=\"d\">\r\n"
            + "<cfreturn a>\r\n"
            + "</cffunction>\r\n"
            + "</cfcomponent>\r\n";
            
        cfBugs.process(tagSrc, "test");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(3, result.size());
        assertEquals("UNUSED_METHOD_ARGUMENT", result.get(0).getMessageCode());
        assertEquals(4, result.get(0).getLine());
        assertEquals("UNUSED_METHOD_ARGUMENT", result.get(1).getMessageCode());
        assertEquals(5, result.get(1).getLine());
        assertEquals("UNUSED_METHOD_ARGUMENT", result.get(2).getMessageCode());
        assertEquals(6, result.get(2).getLine());       
    }

    @Test
    public void testArgumentsUsedInTagWithArgumentsScope() throws ParseException, IOException {
        final String tagSrc = "<cfcomponent>\r\n"
            + "<cffunction name=\"sum\">\r\n"
            + "<cfargument name=\"a\">\r\n"
            + "<cfargument name=\"b\">\r\n"
            + "<cfreturn arguments.a + arguments.b>\r\n"
            + "</cffunction>\r\n"
            + "</cfcomponent>\r\n";
            
        cfBugs.process(tagSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }
    
    @Test
    public void testArgumentsUsedInQuerygWithArgumentsScope() throws ParseException, IOException {
        final String tagSrc = "<cfcomponent>\r\n"
            + "<cffunction name=\"getProduct\">\r\n"
            + "<cfargument name=\"productid\">\r\n"
            + "<cfargument name=\"b\">\r\n"
            + "<cfset var qryProduct = \"\" />\r\n"
            + "<cfquery name=\"qryProduct\">\r\n"
            + "SET @productId = <cfqueryparam value=\"#arguments.productid#\" cfsqltype=\"cf_sql_integer\" />\r\n"
            + "SELECT @productId as productId\r\n"
            + "</cfquery>\r\n"
            + "<cfreturn arguments.a + arguments.b>\r\n"
            + "</cffunction>\r\n"
            + "</cfcomponent>\r\n";
        
        cfBugs.process(tagSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }
    
    
    @Test
    public void testArgumentsUsedInTagWithArgumentsScope2() throws ParseException, IOException {
        final String tagSrc = "<cfcomponent displayame=\"CustomOptInGateway\" output=\"false\" initmethod=\"init\" accessors=\"true\" extends=\"_BaseGateway\">\r\n"
                + "\r\n"
                + "<cffunction name=\"registerMemberCustomOptIn\" access=\"public\" returntype=\"void\" output=\"false\" hint=\"Run insert or update query to save custom member opt in information\">\r\n"
                + "<cfargument name=\"memberId\" type=\"string\" required=\"true\" />\r\n"
                + "<cfargument name=\"customOptInKey\" type=\"string\" required=\"false\" default=\"\" />\r\n"
                + "<cfargument name=\"partnerId\" type=\"string\" required=\"false\" default=\"0\" />\r\n"
                + "\r\n"
                + "<cfquery>\r\n"
                + "DECLARE @MemberID int,\r\n"
                + "@CustomOptInKey varchar(100),\r\n"
                + "@CustomOptInID int,\r\n"
                + "@PartnerID int\r\n"
                + "\r\n"
                + "SET @MemberID = <cfqueryparam value=\"#arguments.memberId#\" cfsqltype=\"cf_sql_varchar\" />\r\n"
                + "SET @CustomOptInKey = <cfqueryparam value=\"#arguments.customOptInKey#\" cfsqltype=\"cf_sql_varchar\" />\r\n"
                + "SET @PartnerID = <cfqueryparam value=\"#arguments.partnerId#\" cfsqltype=\"cf_sql_varchar\" />\r\n"
                + "\r\n"
                + "SET @CustomOptInID = ISNULL((SELECT TOP 1 CustomOptInID FROM CustomOptIn WITH(NOLOCK) WHERE CustomOptInKey = @CustomOptInKey),0)\r\n"
                + "\r\n"
                + "IF (@CustomOptInID > 0)\r\n"
                + "BEGIN\r\n"
                + "IF NOT EXISTS(SELECT CustomOptInID FROM CustomOptInMember WITH(NOLOCK) WHERE MemberID = @MemberID AND CustomOptInID = @CustomOptInID)\r\n"
                + "BEGIN\r\n"
                + "INSERT INTO CustomOptInMember (MemberID, CustomOptInID, PartnerID)\r\n"
                + "VALUES (@MemberID, @CustomOptInID, @PartnerID)\r\n"
                + "END\r\n"
                + "ELSE\r\n"
                + "BEGIN\r\n"
                + "UPDATE CustomOptInMember\r\n"
                + "SET IsActive = 1,\r\n"
                + "PartnerID = @PartnerID,\r\n"
                + "UpdateDate = getDate()\r\n"
                + "WHERE MemberID = @MemberID\r\n"
                + "AND CustomOptInID = @CustomOptInID\r\n"
                + "END\r\n"
                + "END\r\n"
                + "</cfquery>\r\n"
                + "</cffunction>\r\n"
                + "\r\n"
                + "</cfcomponent>";
            
        cfBugs.process(tagSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }
    
    @Test
    public void testArgumentsUsedInTagWithArgumentsScope3() throws ParseException, IOException {
        final String tagSrc = "<cffunction name=\"newContentBlock\" access=\"public\" output=\"false\" returntype=\"ContentBlockBean\">\r\n"
                + "<cfargument name=\"ContentID\" type=\"numeric\" required=\"true\" />\r\n"
                + "<cfargument name=\"ContentName\" type=\"string\" required=\"false\" />\r\n"
                + "<cfargument name=\"PageName\" type=\"string\" required=\"false\" />\r\n"
                + "<cfargument name=\"ContentOrder\" type=\"numeric\" required=\"false\" />\r\n"
                + "<cfargument name=\"IsActive\" type=\"boolean\" required=\"false\" />\r\n"
                + "<cfargument name=\"ContentObject\" type=\"string\" required=\"false\" />\r\n"
                + "<cfargument name=\"IsPartnerInfoPage\" type=\"boolean\" required=\"false\" />\r\n"
                + "<cfargument name=\"ContentHeroObject\" type=\"string\" required=\"false\" />\r\n"
                + "<cfargument name=\"BackgroundImageID\" type=\"numeric\" required=\"false\" />\r\n"
                + "<cfargument name=\"FollowTabAllowed\" type=\"boolean\" required=\"false\" />\r\n"
                + "\r\n"
                + "<cfreturn getContentBlockGateway().new(argumentCollection = arguments ) />\r\n"
                + "</cffunction>";
            
        cfBugs.process(tagSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }
    
    @Test
    public void testArgumentsUsedInTagWithArgumentsScope4() throws ParseException, IOException {
        final String tagSrc = "component displayname=\"ServerErrorController\" accessors=\"true\" extends=\"_BaseController\"\r\n"
                + "{\r\n"
                + "// ------------------------ DEPENDENCY INJECTION ------------------------ //\r\n"
                + "property name=\"Config\";\r\n"
                + "property name=\"fw\";\r\n"
                + "property name=\"ExceptionHandler\";\r\n"
                + "\r\n"
                + "\r\n"
                + "public void function internalError(required struct rc){\r\n"
                + "request.exception[\"failedaction\"] = request.failedaction;\r\n"
                + "request.exception[\"failedcfcname\"] = request.failedcfcname;\r\n"
                + "request.exception[\"failedmethod\"] = request.failedmethod;\r\n"
                + "request.exception[\"rootURL\"] = arguments.rc.rootURL;\r\n"
                + "request.exception[\"currentURL\"] = arguments.rc.currentURL;\r\n"
                + "request.exception[\"current35URL\"] = arguments.rc[\"currentURL\"];\r\n"
                + "request.exception[\"fw1Trace\"] = getFramework().getFrameworkTrace();\r\n"
                + "request.exception[\"requestContext\"] = arguments.rc;\r\n"
                + "if(getFramework().getEnvironment() != \"Development\"){\r\n"
                + "getExceptionHandler().track(request.exception);\r\n"
                + "}\r\n"
                + "}\r\n"
                + "}";
            
        cfBugs.process(tagSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }
    
    @Test
    public void testArgumentsUsedInTagWithArgumentsScope5() throws ParseException, IOException {
        final String tagSrc = "component displayname=\"ServerErrorController\" accessors=\"true\" extends=\"_BaseController\"\r\n"
                + "{\r\n"
                + "// ------------------------ DEPENDENCY INJECTION ------------------------ //\r\n"
                + "property name=\"Config\";\r\n"
                + "property name=\"fw\";\r\n"
                + "property name=\"ExceptionHandler\";\r\n"
                + "\r\n"
                + "\r\n"
                + "public void function internalError(required struct rc){\r\n"
                + "getExceptionHandler().track(argumentCollection=arguments);\r\n"
                + "}\r\n"
                + "}\r\n"
                + "}";
            
        cfBugs.process(tagSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }

    @Ignore("Not fixed yet")
    public void testArgumentsUsedInQueryParam() throws ParseException, IOException {
        final String tagSrc = "<cffunction name=\"lookup\" access=\"public\" output=\"false\" returntype=\"void\">\r\n"
                + "<cfargument name=\"domain\" type=\"string\" required=\"true\" />\r\n"
                + "<cfset var details = \"\">\r\n"
          		+ "<cfquery name=\"details\" datasource=\"#request.datasource#\" cachedwithin=\"#request.cachetime#\">\r\n"
                + "select *\r\n"
                + "from domains\r\n"
                + "where domain = <cfqueryparam value=\"#domain#\" cfsqltype=\"CF_SQL_VARCHAR\">\r\n"
          		+ "</cfquery>\r\n"
                + "</cffunction>";

        cfBugs.process(tagSrc, "test");
        final Map<String, List<BugInfo>> result = cfBugs.getBugs().getBugList();
        assertEquals(0, result.size());
    }
}
