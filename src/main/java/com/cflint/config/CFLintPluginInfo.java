package com.cflint.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.cflint.Levels;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.CFLintScanner;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@XmlRootElement(name = "CFLint-Plugin")
@JsonInclude(Include.NON_NULL)
public class CFLintPluginInfo {

    private List<PluginInfoRule> rules = new ArrayList<>();
    private List<RuleGroup> ruleGroups = new ArrayList<>();

    public List<PluginInfoRule> getRules() {
        return rules;
    }

    @XmlElement(name = "ruleImpl")
    public void setRules(final List<PluginInfoRule> rules) {
        this.rules = rules;
    }

    public List<RuleGroup> getRuleGroups() {
        return ruleGroups;
    }

    @XmlElement(name = "ruleGroup")
    public void setRuleGroups(final List<RuleGroup> ruleGroups) {
        this.ruleGroups = ruleGroups;
    }

    public PluginInfoRule getRuleByName(final String ruleName) {
        for (final PluginInfoRule rule : rules) {
            if (ruleName != null && ruleName.equals(rule.getName())) {
                return rule;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CFLintPluginInfo[");
        sb.append(rules);
        sb.append("]");
        return sb.toString();
    }

    @JsonInclude(Include.NON_NULL)
    public static class RuleGroup {

        private String name;
        private List<PluginMessage> messages = new ArrayList<>();
        private Levels defaultSeverity;

        public RuleGroup(String name) {
            super();
            this.name = name;
        }

        public RuleGroup() {
            super();
        }

        public Levels getDefaultSeverity() {
            return defaultSeverity;
        }

        @XmlAttribute(name = "defaultSeverity")
        public void setDefaultSeverity(String defaultSeverity) {
            this.defaultSeverity = Levels.fromString(defaultSeverity);
        }

        public void setDefaultSeverity(Levels defaultSeverity) {
            this.defaultSeverity = defaultSeverity;
        }
        
        public String getName() {
            return name;
        }

        @XmlAttribute(name = "name")
        public void setName(String name) {
            this.name = name;
        }

        public List<PluginMessage> getMessages() {
            return messages;
        }

        @XmlElement(name = "message")
        public void setMessages(final List<PluginMessage> messages) {
            this.messages = messages;
        }
    }

    // @JsonInclude(Include.NON_NULL)
    public static class PluginInfoRule {

        private String name;
        private String className;
        private List<PluginMessage> messages = new ArrayList<>();
        private List<PluginParameter> parameters = new ArrayList<>();
        // Associate the pluginInstance with the rule that created it
        private CFLintScanner pluginInstance;

        public CFLintScanner getPluginInstance() {
            return pluginInstance;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("PluginInfoRule[");
            sb.append("name=");
            sb.append(name);
            if (className != null && !className.isEmpty()) {
                sb.append(", class=");
                sb.append(className);
            }
            sb.append("]");
            return sb.toString();
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
            public PluginParameter() {
                super();
            }
            public PluginParameter(String name, Object value) {
                super();
                this.name = name;
                this.value = value;
            }

            private String name;
            private Object value;

            public String getName() {
                return name;
            }

            @XmlAttribute(name = "name")
            public void setName(final String name) {
                this.name = name;
            }

            public Object getValue() {
                return value;
            }

            @JsonProperty(value="value")
            public void setValue(final Object value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return "PluginParameter [name=" + name + ", value=" + value + "]";
            }
            
        }

        @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "code")
        public static class PluginMessage {
            private String code;
            private String messageText;
            private Levels severity;

            public PluginMessage(final String code) {
                super();
                this.code = code;
            }

            public PluginMessage() {
                super();
            }

            
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

            public Levels getSeverity() {
                return severity;
            }

            @XmlElement(name = "severity")
            public void setSeverity(final String severity) {
                this.severity = Levels.fromString(severity);
            }

            public void setSeverity(final Levels severity) {
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

            @Override
            public String toString() {
                return "PluginMessage [code=" + code + ", messageText=" + messageText + "]";
            }
        }
    }
}
