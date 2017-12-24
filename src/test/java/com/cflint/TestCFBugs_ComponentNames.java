package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.api.CFLintAPI;
import com.cflint.api.CFLintResult;
import com.cflint.config.ConfigBuilder;
import com.cflint.exception.CFLintScanException;

public class TestCFBugs_ComponentNames {

    private CFLintAPI cfBugs;

    @Before
    public void setUp() throws Exception {
        final ConfigBuilder configBuilder = new ConfigBuilder().include("COMPONENT_INVALID_NAME","COMPONENT_ALLCAPS_NAME","COMPONENT_TOO_SHORT","COMPONENT_TOO_LONG","COMPONENT_TOO_WORDY","COMPONENT_IS_TEMPORARY","COMPONENT_HAS_PREFIX_OR_POSTFIX");
        cfBugs = new CFLintAPI(configBuilder.build());
    }

    @Test
    public void testValidNamesTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "NicComponentNm.cfc");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(lintresult.getIssues().values().toString(), 0, result.size());
    }

    @Test
    public void testUpercaseNameTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "UPPERCASE.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_ALLCAPS_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void testLowercaseNameTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "lowercase.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void invalidCharsInNameTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "Last$name.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameEndsInNumberTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "Component23.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameTooShortTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "A.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_TOO_SHORT", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameTooLongTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "Acomponentwithaveryverylongcomponentname.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_TOO_LONG", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameTooWordyTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "NameIsTooWordy.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_TOO_WORDY", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameIsTemporyTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "Temp.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_IS_TEMPORARY", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameHasPrefixOrPostfixTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        CFLintResult lintresult = cfBugs.scan(tagSrc, "MyComp.cfc");
        final List<BugInfo> result = lintresult.getIssues().get("COMPONENT_HAS_PREFIX_OR_POSTFIX");
        assertEquals(1, result.size());
        assertEquals("COMPONENT_HAS_PREFIX_OR_POSTFIX", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void testValidNamesScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "NicComponentNm.cfc");
        Collection<List<BugInfo>> result = lintresult.getIssues().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testLowercaseNameScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "lowercase.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void testUpercaseNameScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "UPPERCASE.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_ALLCAPS_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void invalidCharsInNameScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "Component$Name.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameEndsInNumberScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "Component23.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameTooShortScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "A.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_TOO_SHORT", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameTooLongScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "Componenthasaverylongcomponentname.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_TOO_LONG", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameTooWordyScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "NameIsTooWordy.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_TOO_WORDY", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameIsTemporyScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "Temp.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_IS_TEMPORARY", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameHasPrefixOrPostfixScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        CFLintResult lintresult = cfBugs.scan(scriptSrc, "ThisComponent.cfc");
        final List<BugInfo> result = lintresult.getIssues().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_HAS_PREFIX_OR_POSTFIX", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

}
