package com.cflint.xml;

import java.io.Writer;

import com.cflint.BugList;
import com.cflint.CFLintStats;

public interface CFLintResultMarshaller {

    public void output(final BugList bugList, final Writer writer, final CFLintStats stats) throws MarshallerException;

}
