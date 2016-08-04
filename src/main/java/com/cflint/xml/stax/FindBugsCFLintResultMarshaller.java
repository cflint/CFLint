package com.cflint.xml.stax;

import com.cflint.BugList;
import com.cflint.xml.CFLintResultMarshaller;
import com.cflint.xml.MarshallerException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public class FindBugsCFLintResultMarshaller implements CFLintResultMarshaller {

    @Override
    public void output(BugList bugList, Writer writer, boolean showStats) throws MarshallerException {
        try {
            StringWriter sw = new StringWriter();
            DefaultCFlintResultMarshaller marshaller = new DefaultCFlintResultMarshaller();
            marshaller.output(bugList, sw, showStats);
            sw.flush();

            final Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(getClass().getResourceAsStream("/findbugs/cflint-to-findbugs.xsl")));

            transformer.transform(new StreamSource(new StringReader(sw.toString())), new StreamResult(writer));

        } catch (TransformerException e) {
            throw new MarshallerException(e);
        }
    }
}
