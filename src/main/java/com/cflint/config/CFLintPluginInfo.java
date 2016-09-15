package com.cflint.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.cflint.plugins.CFLintScanner;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement(name = "CFLint-Plugin")
@JsonInclude(Include.NON_NULL)
public class CFLintPluginInfo {

	List<PluginInfoRule> rules = new ArrayList<CFLintPluginInfo.PluginInfoRule>();

	public List<PluginInfoRule> getRules() {
		return rules;
	}

	@XmlElement(name = "ruleImpl")
	public void setRules(final List<PluginInfoRule> rules) {
		this.rules = rules;
	}

	public PluginInfoRule getRuleByName(final String ruleName) {
		for (final PluginInfoRule rule : rules) {
			if (ruleName != null && ruleName.equals(rule.getName())) {
				return rule;
			}
		}
		return null;
	}

	@JsonInclude(Include.NON_NULL)
	public static class PluginInfoRule {

		String name;
		String className;
		List<PluginMessage> messages = new ArrayList<PluginMessage>();
		List<PluginParameter> parameters = new ArrayList<PluginParameter>();
		// Associate the pluginInstance with the rule that created it
		CFLintScanner pluginInstance;

		public CFLintScanner getPluginInstance() {
			return pluginInstance;
		}

		@XmlTransient
		public void setPluginInstance(final CFLintScanner pluginInstance) {
			this.pluginInstance = pluginInstance;
		}

		public String getClassName() {
			return className;
		}

		@XmlAttribute(name = "className")
		public void setClassName(final String className) {
			this.className = className;
		}

		public List<PluginParameter> getParameters() {
			return parameters;
		}

		@XmlElement(name = "parameter")
		public void setParameters(final List<PluginParameter> parameters) {
			this.parameters = parameters;
		}

		public String getName() {
			return name;
		}

		public void addParameter(final String name, final String value) {
			final PluginParameter p = new PluginParameter();
			p.setName(name);
			p.setValue(value);
			parameters.add(p);
		}

		@XmlAttribute(name = "name")
		public void setName(final String name) {
			this.name = name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final PluginInfoRule other = (PluginInfoRule) obj;
			if (name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!name.equals(other.name)) {
				return false;
			}
			return true;
		}

		public List<PluginMessage> getMessages() {
			return messages;
		}

		@XmlElement(name = "message")
		public void setMessages(final List<PluginMessage> messages) {
			this.messages = messages;
		}

		public PluginMessage getMessageByCode(final String messageCode) {
			for (final PluginMessage message : messages) {
				if (messageCode != null && messageCode.equals(message.getCode())) {
					return message;
				}
			}
			return null;
		}

		public static class PluginParameter {
			String name;
			String value;

			public String getName() {
				return name;
			}

			@XmlAttribute(name = "name")
			public void setName(final String name) {
				this.name = name;
			}

			public String getValue() {
				return value;
			}

			@XmlAttribute(name = "value")
			public void setValue(final String value) {
				this.value = value;
			}
		}

		public static class PluginMessage {
			String code;

			public PluginMessage(final String code) {
				super();
				this.code = code;
			}

			public PluginMessage() {
				super();
			}

			String messageText;
			String severity;

			public String getCode() {
				return code;
			}

			@XmlAttribute(name = "code")
			public void setCode(final String code) {
				this.code = code;
			}

			public String getMessageText() {
				return messageText;
			}

			@XmlElement(name = "messageText")
			public void setMessageText(final String messageText) {
				this.messageText = messageText;
			}

			public String getSeverity() {
				return severity;
			}

			@XmlElement(name = "severity")
			public void setSeverity(final String severity) {
				this.severity = severity;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + ((code == null) ? 0 : code.hashCode());
				return result;
			}

			@Override
			public boolean equals(final Object obj) {
				if (this == obj) {
					return true;
				}
				if (obj == null) {
					return false;
				}
				if (getClass() != obj.getClass()) {
					return false;
				}
				final PluginMessage other = (PluginMessage) obj;
				if (code == null) {
					if (other.code != null) {
						return false;
					}
				} else if (!code.equals(other.code)) {
					return false;
				}
				return true;
			}
		}
	}
}
