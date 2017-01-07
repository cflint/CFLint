package com.cflint.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.CFLintScanner;

@XmlRootElement(name = "config")
public class CFLintConfig {

	List<ConfigOutput> output = new ArrayList<CFLintConfig.ConfigOutput>();
	List<CFLintPluginInfo.PluginInfoRule> rules = new ArrayList<CFLintPluginInfo.PluginInfoRule>();
	List<PluginMessage> excludes = new ArrayList<PluginMessage>();
	List<PluginMessage> includes = new ArrayList<PluginMessage>();

	private boolean inheritParent = true;
	
	public List<ConfigOutput> getOutput() {
		return output;
	}

	@XmlElement(name = "output")
	public void setOutput(final List<ConfigOutput> output) {
		this.output = output;
	}

	public List<CFLintPluginInfo.PluginInfoRule> getRules() {
		return rules;
	}

	@XmlElement(name = "rule")
	public void setRules(final List<CFLintPluginInfo.PluginInfoRule> rules) {
		this.rules = rules;
	}

	public List<PluginMessage> getExcludes() {
		return excludes;
	}

	@XmlElement(name = "excludes")
	public void setExcludes(final List<PluginMessage> excludes) {
		this.excludes = excludes;
	}

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
	public void setInheritParent(boolean inheritParent) {
		this.inheritParent = inheritParent;
	}

	public static class ConfigOutput {

		String name;
		OutputText text;
		OutputXML html;
		OutputXML xml;
		OutputText json;

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
			String file;

			public String getFile() {
				return file;
			}

			@XmlAttribute(name = "file")
			public void setFile(final String file) {
				this.file = file;
			}
		}

		public static class OutputXML extends OutputText {
			String style;

			public String getStyle() {
				return style;
			}

			@XmlAttribute(name = "style")
			public void setStyle(final String style) {
				this.style = style;
			}
		}
	}

	public boolean includes(PluginMessage pluginMessage) {
		return (getIncludes().isEmpty() || getIncludes().contains(pluginMessage));
	}

	public boolean excludes(PluginMessage pluginMessage) {
		return (!getExcludes().isEmpty() && getExcludes().contains(pluginMessage));
	}
	
	public PluginInfoRule getRuleByClass(final Class<?> clazz) {
		final String className = clazz.getSimpleName();
		for (final PluginInfoRule rule : getRules()) {
			if (rule.getName().equals(className) || className.equals(rule.getClassName())) {
				return rule;
			}
		}
		return null;
	}

	public PluginInfoRule getRuleForPlugin(final CFLintScanner plugin) {
		for (final PluginInfoRule rule : getRules()) {
			if (rule.getPluginInstance() == plugin) {
				return rule;
			}
		}
		return getRuleByClass(plugin.getClass());
	}
}
