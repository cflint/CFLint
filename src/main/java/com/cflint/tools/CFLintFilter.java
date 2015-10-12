package com.cflint.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cflint.BugInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CFLintFilter {

	private ArrayList<Map<String,?>> data = null;
	private List<String> includeCodes = null;
	boolean verbose = false;

	@SuppressWarnings("unchecked")
	private CFLintFilter(final String data) throws IOException {
		if (data != null && data.trim().length() > 0) {
			ObjectMapper mapper = new ObjectMapper();
			this.data = mapper.readValue(data, ArrayList.class);
		}else{
			this.data = new ArrayList<Map<String,?>>();
		}
	}
	
	public void addFilter(final Map<String,String> filter){
		data.add(filter);
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
	

	public static CFLintFilter createFilter(boolean verbose) throws IOException {
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

	public static CFLintFilter createFilter(final String excludeJSON,boolean verbose) throws IOException {
		final CFLintFilter filter = new CFLintFilter(excludeJSON);
		filter.setVerbose(verbose);
		return filter;
	}
	public static CFLintFilter createFilter(final String excludeJSON) throws IOException {
		final CFLintFilter filter = new CFLintFilter(excludeJSON);
		return filter;
	}
	
	public boolean include(final BugInfo bugInfo) {
		if (includeCodes != null && !includeCodes.contains(bugInfo.getMessageCode())){
			return false;
		}
		if (data != null) {
			for (final Map<String,?> item : data) {
				
				if (item.containsKey("file")) {
					if (!bugInfo.getFilename().matches(item.get("file").toString())) {
						continue;
					}else if (verbose){
						System.out.println("Exclude matched file " + bugInfo.getFilename());
					}
				}
				if (item.containsKey("code")) {
					if (!bugInfo.getMessageCode().matches(item.get("code").toString())) {
						continue;
					}else if (verbose){
						System.out.println("Exclude matched message code " + bugInfo.getMessageCode());
					}
				}
				if (item.containsKey("function")) {
					if (bugInfo.getFunction() == null
							|| !bugInfo.getFunction().matches(item.get("function").toString())) {
						continue;
					}else if (verbose){
						System.out.println("Exclude matched function name " + bugInfo.getFunction());
					}
				}
				if (item.containsKey("variable")) {
					if (bugInfo.getVariable() == null
							|| !bugInfo.getVariable().matches(item.get("variable").toString())) {
						continue;
					}else if (verbose){
						System.out.println("Exclude matched variable name " + bugInfo.getVariable());
					}
				}
				if (item.containsKey("line")) {
					if (bugInfo.getLine() > 0
							|| !new Integer(bugInfo.getLine()).toString().matches(item.get("line").toString())) {
						continue;
					}else if (verbose){
						System.out.println("Exclude matched line " + bugInfo.getLine());
					}
				}
				if (item.containsKey("expression")) {
					if (bugInfo.getExpression() == null
							|| !bugInfo.getExpression().matches(item.get("expression").toString())) {
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
