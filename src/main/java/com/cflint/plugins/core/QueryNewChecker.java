package com.cflint.plugins.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import ro.fortsoft.pf4j.Extension;

/**
 * Avoid using function X checker.
 */
@Extension
public class QueryNewChecker extends CFLintScannerAdapter {

	/**
	 * Check query new for unique columns listed in parm 1.
	 * @param expression expresison to check.
	 * @param context    expresion context.
	 * @param bugs       list of bugs.
	 */
	@Override
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFunctionExpression) {
			final CFFunctionExpression functionExpression = (CFFunctionExpression) expression;
			if (functionExpression.getName().equalsIgnoreCase("querynew") && functionExpression.getArgs().size() > 0) {
				final CFExpression arg = functionExpression.getArgs().get(0);
				final String query = arg.Decompile(0);
				//Build the list, then get unique with a set, pull them out of the list, anything 
				// left is a duplicate.
				final List<String> cols = new ArrayList<String>(Arrays.asList(query.toLowerCase().split("\\s*,\\s*")));
				final Set<String> cols_unique = new HashSet<String>(cols);
				for(final String x:cols_unique) {
					cols.remove(x);
				}
				if (cols.size() > 0) {
					context.addMessage("QUERYNEW_DUPLICATE_COLUMNS", cols.get(0));
				}
			}
		}
	}

}
