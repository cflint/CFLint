package com.cflint.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import com.cflint.BugInfo;

public class CFLintFilter {

	private JSONArray data = null;
	private List<String> includeCodes = null;

	private CFLintFilter(final String data) {
		if (data != null && data.trim().length() > 0) {
			this.data = (JSONArray) JSONValue.parse(data);
		}else{
			this.data = new JSONArray();
		}
	}
	
	public void addFilter(final Map<String,String> filter){
		final JSONObject jsonObj = new JSONObject();
//		for(Entry<String, String> entry: filter.entrySet()){
			jsonObj.putAll(filter);
//		}
	}
	
	public void excludeCode(final String ... codes){
		if(codes.length == 0)
			return;
		Map<String, String> filter = new HashMap<String, String>();
		for(String code: codes){
			filter.put("code",code);
		}
		addFilter(filter);
	}
	public void includeCode(final String ... codes){
		if(codes.length == 0)
			return;
		if(includeCodes == null){
			includeCodes = new ArrayList<String>();
		}
		includeCodes.addAll(Arrays.asList(codes));
	}
	

	public static CFLintFilter createFilter() {
		String data = null;
		try {
			final InputStream is = CFLintFilter.class.getResourceAsStream("/cflintexclude.json");
			if(is == null){
				return new CFLintFilter(null);
			}
			final byte b[] = new byte[is.available()];
			is.read(b);
			data = new String(b);
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
		return new CFLintFilter(data);
	}

	public static CFLintFilter createFilter(final String excludeJSON) {
		return new CFLintFilter(excludeJSON);
	}
	
	public boolean include(final BugInfo bugInfo) {
		if (includeCodes != null && !includeCodes.contains(bugInfo.getMessageCode())){
			return false;
		}
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
