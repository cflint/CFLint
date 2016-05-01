package com.cflint.plugins.core;

import java.util.Arrays;
import java.util.Collection;

public class CFScopes {

	final static Collection<String> scopes = Arrays.asList("url", "form",
			"cookie", "cgi", "server", "application", "session", "client",
			"request", "arguments", "variables", "this", "local", "cfcatch");

	protected String[] parts(final String variable) {
		return variable.toLowerCase().split("\\.|\\[|\\]");
	}

	public boolean isCFScoped(final String variable) {
		String[] parts = parts(variable);
		return scopes.contains(parts[0]);
	}

	public boolean isScoped(final String variable, final String scope) {
		String[] parts = parts(variable);
		return parts[0].equals(scope);
	}

	public boolean isLocalScoped(final String variable) {
		return isScoped(variable, "local");
	}

	public boolean isFunctionScoped(final String variable) {
		return isScoped(variable, "local") || isScoped(variable, "variables")
				|| isScoped(variable, "arguments");
	}

}