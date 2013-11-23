package com.cfbugs.tools;

import java.io.IOException;
import java.io.InputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.cfbugs.BugInfo;

public class CFBugsFilter {

	private JSONArray data = null;

	private CFBugsFilter(final String data) {
		if (data != null && data.trim().length() > 0) {
			this.data = (JSONArray) JSONValue.parse(data);
		}
	}

	public static CFBugsFilter createFilter() {
		String data = null;
		try {
			final InputStream is = CFBugsFilter.class.getResourceAsStream("/cfbugsexclude.json");
			if(is == null){
				System.out.println("No filter cfbugsexclude.json found.");
				return new CFBugsFilter(null);
			}
			final byte b[] = new byte[is.available()];
			is.read(b);
			data = new String(b);
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
		return new CFBugsFilter(data);
	}

	public static CFBugsFilter createFilter(final String excludeJSON) {
		return new CFBugsFilter(excludeJSON);
	}

	public boolean include(final BugInfo bugInfo) {
		if (data != null) {
			for (final Object item : data) {
				final JSONObject jsonObj = (JSONObject) item;
				if (jsonObj.containsKey("file")) {
					if (!bugInfo.getFilename().matches(jsonObj.get("file").toString())) {
						continue;
					}
				}
				if (jsonObj.containsKey("code")) {
					if (!bugInfo.getMessageCode().matches(jsonObj.get("code").toString())) {
						continue;
					}
				}
				if (jsonObj.containsKey("function")) {
					if (bugInfo.getFunction() == null
							|| !bugInfo.getFunction().matches(jsonObj.get("function").toString())) {
						continue;
					}
				}
				if (jsonObj.containsKey("variable")) {
					if (bugInfo.getVariable() == null
							|| !bugInfo.getFunction().matches(jsonObj.get("function").toString())) {
						continue;
					}
				}
				if (jsonObj.containsKey("line")) {
					if (bugInfo.getLine() > 0
							|| !new Integer(bugInfo.getLine()).toString().matches(jsonObj.get("line").toString())) {
						continue;
					}
				}
				return false;
			}
		}
		return true;
	}
}
