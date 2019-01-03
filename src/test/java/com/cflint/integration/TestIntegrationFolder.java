package com.cflint.integration;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;

import com.cflint.cli.CFLintCLI;

public class TestIntegrationFolder {

    @Test
    @Ignore
    public void testFolder() throws Exception {
        CFLintCLI.main(new String[] { "--folder", "src/test/resources/com/cflint/integration", "--json", "--jsonfile",
                "src/test/resources/com/cflint/integration/output.json" });
        final String expected = loadFile(new File("src/test/resources/com/cflint/integration/output.expected.json"));
        final String actual = loadFile(new File("src/test/resources/com/cflint/integration/output.json"));
        assertEquals(
                expected.replaceAll("\\\\", "/").replaceAll("/+", "/").replaceAll("\\R", "\n")
                        .replaceAll("\"file\".+\\R\\s*", "").replaceAll("\"timestamp\" : \\d+", "\"timestamp\" : 0"),
                actual.replaceAll("\\\\", "/").replaceAll("/+", "/").replaceAll("\\R", "\n")
                        .replaceAll("\"file\".+\\R\\s*", "").replaceAll("\"timestamp\" : \\d+", "\"timestamp\" : 0"));
    }

    @Test
    public void testRuleGroupFolder() throws Exception {
        CFLintCLI.main(new String[] { "--folder", "src/test/resources/com/cflint/integration", "--json", "--jsonfile",
                "src/test/resources/com/cflint/integration/output.rulegroup.json", "--rulegroups", "Naming" });
        final String expected = loadFile(
                new File("src/test/resources/com/cflint/integration/output.rulegroup.expected.json"));
        final String actual = loadFile(new File("src/test/resources/com/cflint/integration/output.rulegroup.json"));
        assertEquals(
                expected.replaceAll("\\\\", "/").replaceAll("/+", "/").replaceAll("\\R", "\n")
                        .replaceAll("\"file\".+\\R\\s*", "").replaceAll("\"timestamp\" : \\d+", "\"timestamp\" : 0"),
                actual.replaceAll("\\\\", "/").replaceAll("/+", "/").replaceAll("\\R", "\n")
                        .replaceAll("\"file\".+\\R\\s*", "").replaceAll("\"timestamp\" : \\d+", "\"timestamp\" : 0"));
    }

    @Test
    public void test_194() throws Exception {
        File tempFile = File.createTempFile("test_194_", "json");
        tempFile.deleteOnExit();
        CFLintCLI.main(new String[] { "--folder", "src/test/resources/com/cflint/integration", "-includeRule",
                "COMPONENT_INVALID_NAME", "--json", "--jsonfile", tempFile.getAbsolutePath() });
        final String expected = loadFile(
                new File("src/test/resources/com/cflint/integration/output_194.expected.json"));
        final String actual = loadFile(tempFile);
        tempFile.delete();
        assertEquals(
                expected.replaceAll("\\\\", "/").replaceAll("/+", "/").replaceAll("\\R", "\n")
                        .replaceAll("\"file\".+\\R\\s*", "").replaceAll("\"timestamp\" : \\d+", "\"timestamp\" : 0"),
                actual.replaceAll("\\\\", "/").replaceAll("/+", "/").replaceAll("\\R", "\n")
                        .replaceAll("\"file\".+\\R\\s*", "").replaceAll("\"timestamp\" : \\d+", "\"timestamp\" : 0"));
    }

    public static String loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        byte[] b = new byte[is.available()];
        is.read(b);
        is.close();
        return new String(b);
    }
}
