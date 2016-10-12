package com.cflint.xml;

import com.cflint.BugList;

import java.io.Writer;

public interface CFLintResultMarshaller {

    void output(BugList bugList, Writer writer, boolean showStats) throws MarshallerException;

}
