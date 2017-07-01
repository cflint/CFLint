package com.cflint.xml;

import java.io.Writer;

import com.cflint.BugList;
import com.cflint.CFLintStats;

public interface CFLintResultMarshaller {

    void output(BugList bugList, Writer writer, boolean showStats, CFLintStats stats) throws MarshallerException;

}
