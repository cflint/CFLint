package com.cflint.xml.stax;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.cflint.BugList;
import com.cflint.CFLintStats;
import com.cflint.xml.CFLintResultMarshaller;
import com.cflint.xml.MarshallerException;

public class FindBugsCFLintResultMarshaller implements CFLintResultMarshaller {

    @Override
    public void output(final BugList bugList, final Writer writer, final CFLintStats stats) throws MarshallerException {
        try {
            StringWriter sw = new StringWriter();
            DefaultCFlintResultMarshaller marshaller = new DefaultCFlintResultMarshaller();
            marshaller.output(bugList, sw, stats);
            sw.flush();

            final Transformer transformer = TransformerFactory.newInstance().newTransformer(
                    new StreamSource(getClass().getResourceAsStream("/findbugs/cflint-to-findbugs.xsl")));

            transformer.transform(new StreamSource(new StringReader(sw.toString())), new StreamResult(writer));

        } catch (TransformerException e) {
            throw new MarshallerException(e);
        }
    }

    public static String formatDate(String dateString) {
        return new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH).format(new Date(Integer.parseInt(dateString)));
    }
}
