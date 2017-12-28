package com.cflint.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginParameter;
import com.cflint.plugins.CFLintScanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

public class ConfigUtils {

	private static final String PLUGIN_PACKAGE = "com.cflint.plugins.core";

    private static final Logger log = LoggerFactory.getLogger(ConfigUtils.class);
    private static JAXBContext CFLintConfigContext = null;

    private ConfigUtils() {
        throw new IllegalStateException("ConfigUtils utility class");
    }

    @Deprecated
    public static Marshaller createMarshaller() throws JAXBException {
        if (CFLintConfigContext == null) {
            init();
        }
        return CFLintConfigContext.createMarshaller();
    }

    @Deprecated
    private static Unmarshaller createUnmarshaller() throws JAXBException {
        if (CFLintConfigContext == null) {
            init();
        }
        return CFLintConfigContext.createUnmarshaller();
    }

    @Deprecated
    protected static synchronized void init() throws JAXBException {
        CFLintConfigContext = JAXBContext.newInstance(CFLintPluginInfo.class, CFLintConfig.class);
    }

    @Deprecated
    public static String marshal(final Object obj) throws JAXBException {
        final StringWriter sw = new StringWriter();
        createMarshaller().marshal(obj, sw);
        return sw.toString();
    }

    @Deprecated
    public static String marshalQuietly(final Object obj) {
        try {
            return marshal(obj);
        } catch (final JAXBException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    public static <E> E unmarshal(final String xml, final Class<E> expectedClass) throws JAXBException {
        return (E) createUnmarshaller().unmarshal(new StringReader(xml));
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    public static <E> E unmarshal(final InputStream inputStream, final Class<E> expectedClass) throws JAXBException {
        return (E) createUnmarshaller().unmarshal(new InputStreamReader(inputStream));
    }
    
    @SuppressWarnings("unchecked")
    @Deprecated
    public static <E> E unmarshal(final File xmlFile, final Class<E> expectedClass) throws JAXBException, FileNotFoundException {
        System.err.println("XML configurations will be removed in the next release.  Convert " + xmlFile.getName() + " to json.");
        return (E) createUnmarshaller().unmarshal(new InputStreamReader(new FileInputStream(xmlFile)));
    }
    
    @Deprecated
    public static Object unmarshal(final File xmlFile) throws JAXBException, FileNotFoundException {
        System.err.println("XML configurations will be removed in the next release.  Convert " + xmlFile.getName() + " to json.");
        return createUnmarshaller().unmarshal(new InputStreamReader(new FileInputStream(xmlFile)));
    }

    public static String marshalJson(final Object obj)
            throws IOException {
        final StringWriter sw = new StringWriter();
        final ObjectMapper objectMapper = new ObjectMapper();
        final JaxbAnnotationModule module = new JaxbAnnotationModule();
        objectMapper.registerModule(module);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(sw, obj);
        return sw.toString();
    }

    public static <E> E unmarshalJson(final InputStream inputStream, final Class<E> expectedClass)
            throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final JaxbAnnotationModule module = new JaxbAnnotationModule();
        objectMapper.registerModule(module);
        // AnnotationIntrospector introspector = new
        // JaxbAnnotationIntrospector();
        // mapper.setAnnotationIntrospector(introspector);
        return objectMapper.readValue(inputStream, expectedClass);
    }

    public static <E> E unmarshalJson(final String input, final Class<E> expectedClass)
            throws IOException {
        return unmarshalJson(new StringReader(input), expectedClass);
    }

    public static <E> E unmarshalJson(final Reader reader, final Class<E> expectedClass)
            throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final JaxbAnnotationModule module = new JaxbAnnotationModule();
        objectMapper.registerModule(module);
        return objectMapper.readValue(reader, expectedClass);
    }

    /**
     * Load the plugin definitions. If it is available use the json definition
     * file first.
     *
     * @return CFLintPluginInfo instance of plugin definitions
     */
    public static CFLintPluginInfo loadDefaultPluginInfo() {
        final InputStream jsonInputStream = ConfigUtils.class.getResourceAsStream("/cflint.definition.json");
        if (jsonInputStream != null) {
            try {
                return unmarshalJson(jsonInputStream, CFLintPluginInfo.class);
            } catch (final IOException e) {
                log.error("Error loading default plugin json info", e);
            }
        }

        final InputStream inputStream = ConfigUtils.class.getResourceAsStream("/cflint.definition.xml");
        if (inputStream != null) {
            try {
                return unmarshal(inputStream, CFLintPluginInfo.class);
            } catch (final JAXBException e) {
                log.error("Error loading default plugin xml info", e);
            }
        }
        return new CFLintPluginInfo();
    }

    /**
     * Load the plugin description.
     *
     * @return Map&lt;String,String&gt; map of message codes to descriptions
     */
    public static Map<String, String> loadDescriptions() {
        final HashMap<String, String> descriptions = new HashMap<>();
        final InputStream inputStream = ConfigUtils.class.getResourceAsStream("/cflint.description.txt");

        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] parts = line.split(":");
                if (parts.length == 2) {
                    descriptions.put(parts[0], parts[1]);
                }
            }
        } catch (final Exception e) {
            log.error("Error loading descriptions", e);
        }

        return descriptions;
    }

    public static CFLintScanner loadPlugin(final PluginInfoRule ruleInfo) {
        final String shortClassName = ruleInfo.getClassName() != null && ruleInfo.getClassName().trim().length() > 0
                ? ruleInfo.getClassName() : ruleInfo.getName();
        final String className = PLUGIN_PACKAGE + "." + shortClassName.trim();
        try {
            final Class<?> pluginClass = Class.forName(className);
            final CFLintScanner plugin = (CFLintScanner) pluginClass.newInstance();
            for (final PluginParameter param : ruleInfo.getParameters()) {
                plugin.setParameter(param.getName(), param.getValue());
            }
            ruleInfo.setPluginInstance(plugin);
            return plugin;
        } catch (final Exception e) {
            log.error("Could not load plugin " + className, e);
        }
        return null;
    }

}
