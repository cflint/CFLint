package com.cflint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Version {

	public static String getVersion() {
		final InputStream is = Version.class.getResourceAsStream("/META-INF/maven/com.cflint/CFLint/pom.properties");
		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = reader.readLine();
			while (line != null && !line.startsWith("version=")) {
				line = reader.readLine();
			}
			if (line != null) {
				return line.replaceAll("version=", "");
			}
		} catch (final Exception e) {
			try {
				is.close();
			} catch (final IOException e1) {
				e1.printStackTrace();
			}
		}
		return "";
	}
}
