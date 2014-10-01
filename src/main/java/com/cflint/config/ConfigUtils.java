package com.cflint.config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginParameter;
import com.cflint.plugins.CFLintScanner;

public class ConfigUtils {

	static JAXBContext CFLintConfigContext = null;
	
	public static Marshaller createMarshaller() throws JAXBException{
		if(CFLintConfigContext == null){
			init();
		}
		return CFLintConfigContext.createMarshaller();
	}
	public static Unmarshaller createUnmarshaller() throws JAXBException{
		if(CFLintConfigContext == null){
			init();
		}
		return CFLintConfigContext.createUnmarshaller();
	}
	protected static synchronized void init() throws JAXBException {
		CFLintConfigContext = JAXBContext.newInstance(CFLintPluginInfo.class,CFLintConfig.class);
	}
	
	public static String marshal(Object obj) throws JAXBException{
		StringWriter sw = new StringWriter();
		createMarshaller().marshal(obj, sw);
		return sw.toString();
	}
	public static String marshalQuietly(Object obj){
		try{
			return marshal(obj);
		}catch(JAXBException e){return null;}
	}
	@SuppressWarnings("unchecked")
	public static <E> E unmarshal(String xml,Class<E> expectedClass) throws JAXBException{
		StringWriter sw = new StringWriter();
		return (E) createUnmarshaller().unmarshal(new StringReader(xml));
	}
	@SuppressWarnings("unchecked")
	public static <E> E unmarshal(InputStream inputStream,Class<E> expectedClass) throws JAXBException{
		StringWriter sw = new StringWriter();
		return (E) createUnmarshaller().unmarshal(new InputStreamReader(inputStream));
	}
	
	public static CFLintPluginInfo loadDefaultPluginInfo(){
		final InputStream inputStream = ConfigUtils.class.getResourceAsStream("/cflint.definition.xml");
		if(inputStream != null){
			try{
				return unmarshal(inputStream,CFLintPluginInfo.class);
			}catch(JAXBException e){
				e.printStackTrace(System.err);
			}
		}
		return new CFLintPluginInfo();
	}
	
	static final String PLUGIN_PACKAGE = "com.cflint.plugins.core";
	public static CFLintScanner loadPlugin(final PluginInfoRule ruleInfo) {
		final String shortClassName = ruleInfo.getClassName() != null && ruleInfo.getClassName().trim().length() > 0?
				ruleInfo.getClassName():ruleInfo.getName();
		final String className = PLUGIN_PACKAGE + "." + shortClassName.trim();
		try {
			final Class<?> pluginClass = Class.forName(className);
			final CFLintScanner plugin= (CFLintScanner)pluginClass.newInstance();
			for(PluginParameter param: ruleInfo.getParameters()){
				plugin.setParameter(param.getName(), param.getValue());
			}
			ruleInfo.setPluginInstance(plugin);
			return plugin;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
