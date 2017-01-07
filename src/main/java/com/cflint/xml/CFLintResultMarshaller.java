package com.cflint.xml;

import java.io.Writer;

import com.cflint.BugList;

public interface CFLintResultMarshaller {

    void output(BugList bugList, Writer writer, boolean showStats) throws MarshallerException;

}
