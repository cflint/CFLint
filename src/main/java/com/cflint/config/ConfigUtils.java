package com.cflint.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginParameter;
import com.cflint.plugins.CFLintScanner;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

public class ConfigUtils {

	static JAXBContext CFLintConfigContext = null;

	public static Marshaller createMarshaller() throws JAXBException {
		if (CFLintConfigContext == null) {
			init();
		}
		return CFLintConfigContext.createMarshaller();
	}

	private static Unmarshaller createUnmarshaller() throws JAXBException {
		if (CFLintConfigContext == null) {
			init();
		}
		return CFLintConfigContext.createUnmarshaller();
	}

	protected static synchronized void init() throws JAXBException {
		CFLintConfigContext = JAXBContext.newInstance(CFLintPluginInfo.class,
				CFLintConfig.class);
	}

	public static String marshal(Object obj) throws JAXBException {
		StringWriter sw = new StringWriter();
		createMarshaller().marshal(obj, sw);
		return sw.toString();
	}

	public static String marshalQuietly(Object obj) {
		try {
			return marshal(obj);
		} catch (JAXBException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <E> E unmarshal(String xml, Class<E> expectedClass)
			throws JAXBException {
		return (E) createUnmarshaller().unmarshal(new StringReader(xml));
	}

	@SuppressWarnings("unchecked")
	public static <E> E unmarshal(InputStream inputStream,
			Class<E> expectedClass) throws JAXBException {
		return (E) createUnmarshaller().unmarshal(
				new InputStreamReader(inputStream));
	}
	
	public static String marshalJson(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		StringWriter sw = new StringWriter();
		ObjectMapper objectMapper = new ObjectMapper();
		JaxbAnnotationModule module = new JaxbAnnotationModule();
		objectMapper.registerModule(module);
		objectMapper.writeValue(sw, obj);
		return sw.toString();
	}

	@SuppressWarnings("unchecked")
	public static <E> E unmarshalJson(InputStream inputStream,
			Class<E> expectedClass) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JaxbAnnotationModule module = new JaxbAnnotationModule();
		objectMapper.registerModule(module);
//		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
//		mapper.setAnnotationIntrospector(introspector);
		return objectMapper.readValue(inputStream, expectedClass);
	}
	
	@SuppressWarnings("unchecked")
	public static <E> E unmarshalJson(Reader reader,
			Class<E> expectedClass) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JaxbAnnotationModule module = new JaxbAnnotationModule();
		objectMapper.registerModule(module);
		return objectMapper.readValue(reader, expectedClass);
	}

	/**
	 * Load the plugin definitions.
	 * If it is available use the json definition file first.
	 * 
	 * @return CFLintPluginInfo instance of plugin definitions
	 */
	public static CFLintPluginInfo loadDefaultPluginInfo() {
		final InputStream jsonInputStream = ConfigUtils.class
				.getResourceAsStream("/cflint.definition.json");
		if (jsonInputStream != null) {
			try {
				return unmarshalJson(jsonInputStream, CFLintPluginInfo.class);
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}
		
		final InputStream inputStream = ConfigUtils.class
				.getResourceAsStream("/cflint.definition.xml");
		if (inputStream != null) {
			try {
				return unmarshal(inputStream, CFLintPluginInfo.class);
			} catch (JAXBException e) {
				e.printStackTrace(System.err);
			}
		}
		return new CFLintPluginInfo();
	}

	/**
	 * Load the plugin description.
	 * 
	 * @return MapList&lt;String,String&gt; map of message codes to descriptions
	 */
	public static HashMap loadDescriptions() {
		HashMap descriptions = new HashMap();
		InputStream inputStream = ConfigUtils.class
				.getResourceAsStream("/cflint.description.txt");

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
		    while ((line = reader.readLine()) != null) {
		      	String[] parts = line.split(":");
		      	if (parts.length == 2) {
		      		descriptions.put(parts[0], parts[1]);
		      	}
		    }
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
		return descriptions;
	}

	static final String PLUGIN_PACKAGE = "com.cflint.plugins.core";

	public static CFLintScanner loadPlugin(final PluginInfoRule ruleInfo) {
		final String shortClassName = ruleInfo.getClassName() != null
				&& ruleInfo.getClassName().trim().length() > 0 ? ruleInfo
				.getClassName() : ruleInfo.getName();
		final String className = PLUGIN_PACKAGE + "." + shortClassName.trim();
		try {
			final Class<?> pluginClass = Class.forName(className);
			final CFLintScanner plugin = (CFLintScanner) pluginClass
					.newInstance();
			for (PluginParameter param : ruleInfo.getParameters()) {
				plugin.setParameter(param.getName(), param.getValue());
			}
			ruleInfo.setPluginInstance(plugin);
			return plugin;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
