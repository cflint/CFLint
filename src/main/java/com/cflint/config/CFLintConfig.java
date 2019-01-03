package com.cflint.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.CFLintScanner;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement(name = "config")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CFLintConfig extends BaseConfig {

    private List<CFLintPluginInfo.PluginInfoRule> rules = new ArrayList<>();
    private List<PluginMessage> excludes = new ArrayList<>();
    private List<PluginMessage> includes = new ArrayList<>();
    private HashMap<String,Object> parameters = new HashMap<>();

    private boolean inheritParent = true;
    
    public HashMap<String,Object> getParameters() {
        return parameters;
    }

    @JsonAnyGetter
    @XmlElement(name = "parameters")
    public void setParameters(final HashMap<String,Object> parameters) {
        this.parameters = parameters;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cflint.config.CFLintConfiguration#getRules()
     */
    @Override
    public List<CFLintPluginInfo.PluginInfoRule> getRules() {
        return rules;
    }

    @XmlElement(name = "rule")
    public void setRules(final List<CFLintPluginInfo.PluginInfoRule> rules) {
        this.rules = rules;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cflint.config.CFLintConfiguration#getExcludes()
     */
    public List<PluginMessage> getExcludes() {
        return excludes;
    }

    @XmlElement(name = "excludes")
    public void setExcludes(final List<PluginMessage> excludes) {
        this.excludes = excludes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cflint.config.CFLintConfiguration#getIncludes()
     */
    public List<PluginMessage> getIncludes() {
        return includes;
    }

    @XmlElement(name = "includes")
    public void setIncludes(final List<PluginMessage> includes) {
        this.includes = includes;
    }

    public boolean isInheritParent() {
        return inheritParent;
    }

    @XmlAttribute(name = "inheritParent")
    public void setInheritParent(final boolean inheritParent) {
        this.inheritParent = inheritParent;
    }

    public static class ConfigOutput {

        private String name;
        private OutputText text;
        private OutputXML html;
        private OutputXML xml;
        private OutputText json;

        public String getName() {
            return name;
        }

        @XmlAttribute(name = "name")
        public void setName(final String name) {
            this.name = name;
        }

        public OutputText getText() {
            return text;
        }

        public OutputText getJSON() {
            return json;
        }

        @XmlElement(name = "text")
        public void setText(final OutputText text) {
            this.text = text;
        }

        @XmlElement(name = "json")
        public void setJSON(final OutputText json) {
            this.json = json;
        }

        public OutputXML getHtml() {
            return html;
        }

        @XmlElement(name = "html")
        public void setHtml(final OutputXML html) {
            this.html = html;
        }

        public OutputXML getXml() {
            return xml;
        }

        @XmlElement(name = "xml")
        public void setXml(final OutputXML xml) {
            this.xml = xml;
        }

        public static class OutputText {
            private String file;

            public String getFile() {
                return file;
            }

            @XmlAttribute(name = "file")
            public void setFile(final String file) {
                this.file = file;
            }
        }

        public static class OutputXML extends OutputText {
            private String style;

            public String getStyle() {
                return style;
            }

            @XmlAttribute(name = "style")
            public void setStyle(final String style) {
                this.style = style;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cflint.config.CFLintConfiguration#includes(com.cflint.config.
     * CFLintPluginInfo.PluginInfoRule.PluginMessage)
     */
    @Override
    public boolean includes(PluginMessage pluginMessage) {
        return (getIncludes().isEmpty() || getIncludes().contains(pluginMessage));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cflint.config.CFLintConfiguration#excludes(com.cflint.config.
     * CFLintPluginInfo.PluginInfoRule.PluginMessage)
     */
    @Override
    public boolean excludes(PluginMessage pluginMessage) {
        return (!getExcludes().isEmpty() && getExcludes().contains(pluginMessage));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cflint.config.CFLintConfiguration#getRuleByClass(java.lang.Class)
     */
    @Override
    public PluginInfoRule getRuleByClass(final Class<?> clazz) {
        final String className = clazz.getSimpleName();
        for (final PluginInfoRule rule : getRules()) {
            if (rule.getName().equals(className) || className.equals(rule.getClassName())) {
                return rule;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cflint.config.CFLintConfiguration#getRuleForPlugin(com.cflint.plugins
     * .CFLintScanner)
     */
    @Override
    public PluginInfoRule getRuleForPlugin(final CFLintScanner plugin) {
        for (final PluginInfoRule rule : getRules()) {
            if (rule.getPluginInstance() == plugin) {
                return rule;
            }
        }
        return getRuleByClass(plugin.getClass());
    }

    @Override
    public void addInclude(PluginMessage pluginMessage) {
        includes.add(pluginMessage);
    }

    @Override
    public void addExclude(PluginMessage pluginMessage) {
        excludes.add(pluginMessage);
    }

    public static CFLintConfiguration createDefault() {
        final CFLintPluginInfo pluginInfo = ConfigUtils.loadDefaultPluginInfo();
        CFLintConfig defaultConfig = new CFLintConfig();
        defaultConfig.setRules(pluginInfo.getRules());
        return defaultConfig;
    }

    @Override
    public Object getParameter(String name) {
        if(parameters!=null && name !=null && parameters.containsKey(name)){
            return parameters.get(name);
        }
        return null;
    }
}
