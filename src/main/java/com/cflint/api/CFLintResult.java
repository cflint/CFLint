package com.cflint.api;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;

import com.cflint.BugInfo;
import com.cflint.CFLint;
import com.cflint.CFLintStats;
import com.cflint.HTMLOutput;
import com.cflint.JSONOutput;
import com.cflint.TextOutput;
import com.cflint.XMLOutput;
import com.cflint.xml.MarshallerException;
import com.cflint.xml.stax.DefaultCFlintResultMarshaller;

public class CFLintResult {

    final CFLint cflint;

    public CFLintResult(final CFLint cflint) {
        this.cflint = cflint;
    }

    public String getXml() throws MarshallerException {
        final StringWriter xmlwriter = new StringWriter();
        writeXml(xmlwriter);
        return xmlwriter.toString();
    }

    public void writeXml(final Writer xmlwriter) throws MarshallerException {
        new DefaultCFlintResultMarshaller().output(cflint.getBugs(), xmlwriter, cflint.getStats());
    }

    public String getFindBugsXml() throws MarshallerException, IOException, TransformerException {
        final StringWriter xmlwriter = new StringWriter();
        writeFindBugsXml(xmlwriter);
        return xmlwriter.toString();
    }

    public void writeFindBugsXml(final Writer xmlwriter) throws IOException, TransformerException {
        new XMLOutput().outputFindBugs(cflint.getBugs(), xmlwriter, cflint.getStats());
    }

    public String getText() throws IOException {
        final StringWriter xmlwriter = new StringWriter();
        writeText(xmlwriter);
        return xmlwriter.toString();
    }

    public void writeText(final Writer textwriter) throws IOException {
        new TextOutput().output(cflint.getBugs(), textwriter, cflint.getStats());
    }

    public String getHTML(final String htmlStyle) throws IOException {
        final StringWriter xmlwriter = new StringWriter();
        writeHTML(htmlStyle, xmlwriter);
        return xmlwriter.toString();
    }

    public void writeHTML(final String htmlStyle, final Writer htmlwriter) throws IOException {
        try {
            new HTMLOutput(htmlStyle).output(cflint.getBugs(), htmlwriter, cflint.getStats());
        } catch (final TransformerException e) {
            throw new IOException(e);
        }
    }

    public String getJSON() throws IOException {
        final StringWriter xmlwriter = new StringWriter();
        writeJSON(xmlwriter);
        return xmlwriter.toString();
    }

    public void writeJSON(final Writer jsonwriter) throws IOException {
        new JSONOutput().output(cflint.getBugs(), jsonwriter, cflint.getStats());
    }

    public CFLintStats getStats() {
        return cflint.getStats();
    }
    
    public Map<String, List<BugInfo>> getIssues(){
        return cflint.getBugs().getBugList();
    }
}