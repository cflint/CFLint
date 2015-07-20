package com.cflint.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.cflint.BugInfo;

public class CFLintFilter {

	private JSONArray data = null;
	private List<String> includeCodes = null;
	boolean verbose = false;

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
	

	public static CFLintFilter createFilter(boolean verbose) {
		String data = null;
		try {
			final InputStream is = CFLintFilter.class.getResourceAsStream("/cflintexclude.json");
			if(is == null){
				if(verbose){
					System.out.println("No cflintexclude.json on classpath.");
				}
				return new CFLintFilter(null);
			}
			if(verbose){
				final URL url = CFLintFilter.class.getResource("/cflintexclude.json");
				System.out.println("Using exclude file " + url);
			}
			
			final byte b[] = new byte[is.available()];
			is.read(b);
			data = new String(b);
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
		final CFLintFilter filter = new CFLintFilter(data);
		filter.setVerbose(verbose);
		if (verbose){
			System.out.println("Exclude rule count : " + filter.data.size());
		}
		return filter;
	}

	public static CFLintFilter createFilter(final String excludeJSON,boolean verbose) {
		final CFLintFilter filter = new CFLintFilter(excludeJSON);
		filter.setVerbose(verbose);
		return filter;
	}
	public static CFLintFilter createFilter(final String excludeJSON) {
		final CFLintFilter filter = new CFLintFilter(excludeJSON);
		return filter;
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
					}else if (verbose){
						System.out.println("Exclude matched file " + bugInfo.getFilename());
					}
				}
				if (jsonObj.containsKey("code")) {
					if (!bugInfo.getMessageCode().matches(jsonObj.get("code").toString())) {
						continue;
					}else if (verbose){
						System.out.println("Exclude matched message code " + bugInfo.getMessageCode());
					}
				}
				if (jsonObj.containsKey("function")) {
					if (bugInfo.getFunction() == null
							|| !bugInfo.getFunction().matches(jsonObj.get("function").toString())) {
						continue;
					}else if (verbose){
						System.out.println("Exclude matched function name " + bugInfo.getFunction());
					}
				}
				if (jsonObj.containsKey("variable")) {
					if (bugInfo.getVariable() == null
							|| !bugInfo.getVariable().matches(jsonObj.get("variable").toString())) {
						continue;
					}else if (verbose){
						System.out.println("Exclude matched variable name " + bugInfo.getVariable());
					}
				}
				if (jsonObj.containsKey("line")) {
					if (bugInfo.getLine() > 0
							|| !new Integer(bugInfo.getLine()).toString().matches(jsonObj.get("line").toString())) {
						continue;
					}else if (verbose){
						System.out.println("Exclude matched line " + bugInfo.getLine());
					}
				}
				if (jsonObj.containsKey("expression")) {
					if (bugInfo.getExpression() == null
							|| !bugInfo.getExpression().matches(jsonObj.get("expression").toString())) {
						continue;
					}else if (verbose){
						System.out.println("Exclude matched expression " + bugInfo.getExpression());
					}
				}
				return false;
			}
		}
		return true;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
}
