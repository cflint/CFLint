package com.cflint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

public class TestXMLOutput {

    private XMLOutput outputer;
    private BugList bugList;
    private Writer writer;

    @Before
    public void setUp() {
        outputer = new XMLOutput();
        bugList = new BugList(null);
        writer = new StringWriter();
    }

    @Test
    public void testOutput() throws IOException {
        BugInfo bugInfo = new BugInfo.BugInfoBuilder().setFunction("testf").setMessageCode("PARSE_ERROR")
                .setFilename("c:\\temp\\test.cfc").build();
        bugList.add(bugInfo);
        CFLintStats stats = new CFLintStats(123456L, 1, new BigInteger("545454"));
        outputer.output(bugList, writer, stats);
        String expectedText = "<issues version=\"" + Version.getVersion() + "\" timestamp=\"123456\">\n"
                + "<issue severity=\"\" id=\"PARSE_ERROR\" message=\"PARSE_ERROR\" category=\"CFLint\" abbrev=\"PE\"><location file=\"c:\\temp\\test.cfc\" fileName=\"test.cfc\" function=\"testf\" column=\"1\" line=\"1\" message=\"\" variable=\"\"><Expression><![CDATA[]]></Expression></location>\n"
                + "</issue>\n" + "<counts totalfiles=\"1\" totallines=\"545454\">\n" + "</counts>" + "</issues>";
        // remove the version
        assertEquals(expectedText.replace("\n", "").replace("\r", ""),
                writer.toString().replace("\n", "").replace("\r", ""));
    }

    @Test
    public void testStats() throws IOException {
        BugInfo bugInfo = new BugInfo.BugInfoBuilder().setFunction("testf").setMessageCode("PARSE_ERROR")
                .setFilename("c:\\temp\\test.cfc").build();
        bugList.add(bugInfo);
        BugCounts counts = new BugCounts();
        counts.add("PARSE_ERROR", null);
        CFLintStats stats = new CFLintStats(123456L, 1, new BigInteger("545454"), counts);
        outputer.output(bugList, writer, stats);

        String expectedText = "<issues version=\"" + Version.getVersion() + "\" timestamp=\"123456\">\n"
                + "<issue severity=\"\" id=\"PARSE_ERROR\" message=\"PARSE_ERROR\" category=\"CFLint\" abbrev=\"PE\"><location file=\"c:\\temp\\test.cfc\" fileName=\"test.cfc\" function=\"testf\" column=\"1\" line=\"1\" message=\"\" variable=\"\"><Expression><![CDATA[]]></Expression></location>\n"
                + "</issue>\n" + "<counts totalfiles=\"1\" totallines=\"545454\">\n"
                + "<count code=\"PARSE_ERROR\" count=\"1\" />\n" + "</counts>" + "</issues>";
        assertEquals(expectedText.replace("\n", "").replace("\r", ""),
                writer.toString().replace("\n", "").replace("\r", ""));
    }

    @Test
    public void testStatsAndSeverity() throws IOException {
        BugInfo bugInfo = new BugInfo.BugInfoBuilder().setFunction("testf").setMessageCode("PARSE_ERROR")
                .setSeverity(Levels.ERROR).setFilename("c:\\temp\\test.cfc").build();
        bugList.add(bugInfo);
        BugCounts counts = new BugCounts();
        counts.add("PARSE_ERROR", Levels.ERROR);
        CFLintStats stats = new CFLintStats(123456L, 1, new BigInteger("545454"), counts);
        outputer.output(bugList, writer, stats);

        String expectedText = "<issues version=\"" + Version.getVersion() + "\" timestamp=\"123456\">\n"
                + "<issue severity=\"ERROR\" id=\"PARSE_ERROR\" message=\"PARSE_ERROR\" category=\"CFLint\" abbrev=\"PE\"><location file=\"c:\\temp\\test.cfc\" fileName=\"test.cfc\" function=\"testf\" column=\"1\" line=\"1\" message=\"\" variable=\"\"><Expression><![CDATA[]]></Expression></location>\n"
                + "</issue>\n" + "<counts totalfiles=\"1\" totallines=\"545454\">\n"
                + "<count code=\"PARSE_ERROR\" count=\"1\" />\n" + "<count severity=\"ERROR\" count=\"1\" />"
                + "</counts>" + "</issues>";
        assertEquals(expectedText.replace("\n", "").replace("\r", ""),
                writer.toString().replace("\n", "").replace("\r", ""));
    }

}
