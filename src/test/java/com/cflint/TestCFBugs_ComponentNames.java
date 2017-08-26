package com.cflint;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.config.CFLintConfig;
import com.cflint.exception.CFLintScanException;

public class TestCFBugs_ComponentNames {

    private CFLint cfBugs;

    @Before
    public void setUp() throws Exception {
        final com.cflint.config.CFLintConfiguration conf = CFLintConfig.createDefaultLimited("ComponentNameChecker");
        cfBugs = new CFLint(conf);
    }

    @Test
    public void testValidNamesTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        cfBugs.process(tagSrc, "NicComponentNm.cfc");
        Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
        assertEquals(cfBugs.getBugs().getBugList().values().toString(), 0, result.size());
    }

    @Test
    public void testUpercaseNameTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        cfBugs.process(tagSrc, "UPPERCASE.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_ALLCAPS_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void testLowercaseNameTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        cfBugs.process(tagSrc, "lowercase.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void invalidCharsInNameTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        cfBugs.process(tagSrc, "Last$name.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameEndsInNumberTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        cfBugs.process(tagSrc, "Component23.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameTooShortTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        cfBugs.process(tagSrc, "A.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_TOO_SHORT", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameTooLongTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        cfBugs.process(tagSrc, "Acomponentwithaveryverylongcomponentname.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_TOO_LONG", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameTooWordyTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        cfBugs.process(tagSrc, "NameIsTooWordy.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_TOO_WORDY", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameIsTemporyTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        cfBugs.process(tagSrc, "Temp.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_IS_TEMPORARY", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameHasPrefixOrPostfixTag() throws CFLintScanException {
        final String tagSrc = "<cfcomponent>\r\n" + "</cfcomponent>";
        cfBugs.process(tagSrc, "MyComp.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().get("COMPONENT_HAS_PREFIX_OR_POSTFIX");
        assertEquals(1, result.size());
        assertEquals("COMPONENT_HAS_PREFIX_OR_POSTFIX", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void testValidNamesScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        cfBugs.process(scriptSrc, "NicComponentNm.cfc");
        Collection<List<BugInfo>> result = cfBugs.getBugs().getBugList().values();
        assertEquals(0, result.size());
    }

    @Test
    public void testLowercaseNameScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        cfBugs.process(scriptSrc, "lowercase.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void testUpercaseNameScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        cfBugs.process(scriptSrc, "UPPERCASE.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_ALLCAPS_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void invalidCharsInNameScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        cfBugs.process(scriptSrc, "Component$Name.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameEndsInNumberScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        cfBugs.process(scriptSrc, "Component23.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_INVALID_NAME", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameTooShortScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        cfBugs.process(scriptSrc, "A.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_TOO_SHORT", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameTooLongScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        cfBugs.process(scriptSrc, "Componenthasaverylongcomponentname.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_TOO_LONG", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameTooWordyScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        cfBugs.process(scriptSrc, "NameIsTooWordy.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_TOO_WORDY", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameIsTemporyScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        cfBugs.process(scriptSrc, "Temp.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_IS_TEMPORARY", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

    @Test
    public void nameHasPrefixOrPostfixScript() throws CFLintScanException {
        final String scriptSrc = "component {\r\n" + "}";
        cfBugs.process(scriptSrc, "ThisComponent.cfc");
        final List<BugInfo> result = cfBugs.getBugs().getBugList().values().iterator().next();
        assertEquals(1, result.size());
        assertEquals("COMPONENT_HAS_PREFIX_OR_POSTFIX", result.get(0).getMessageCode());
        assertEquals(1, result.get(0).getLine());
    }

}
