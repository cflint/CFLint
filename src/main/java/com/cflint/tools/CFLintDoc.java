package com.cflint.tools;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cflint.config.CFLintPluginInfo;
import com.cflint.config.ConfigUtils;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.RuleGroup;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginParameter;

public class CFLintDoc {

    public static void generateRuleGroup(final CFLintPluginInfo pluginInfo, final PrintWriter print){
        final Map<String, PluginMessage> allCodes = new LinkedHashMap<>();
        for (final PluginInfoRule rule : pluginInfo.getRules()) {
            for (final PluginMessage msg : rule.getMessages()) {
                allCodes.put(msg.getCode(), msg);
            }
        }
        for (final RuleGroup ruleGroup : pluginInfo.getRuleGroups()) {
            print.println("Rule Group : " + ruleGroup.getName());
            for (final PluginMessage msg : ruleGroup.getMessages()) {
                print.println("\t" + msg.getCode() + " : " + msg.getSeverity());
                allCodes.remove(msg.getCode());
            }
        }
        if (!allCodes.isEmpty()) {
            print.println("Rule Group : UNASSIGNED");
            for (final PluginMessage msg : allCodes.values()) {
                print.println("\t" + msg.getCode() + " : " + msg.getSeverity());
            }
        }
    }

    public static void generateRuleMarkDown(final CFLintPluginInfo pluginInfo, final PrintWriter print){
        final Map<String, String> descriptions = ConfigUtils.loadDescriptions();
        final List<String> diminishParams = Arrays.asList("UnusedLocalVarChecker","CFXTagChecker","FunctionXChecker");
        print.println("# Built-in Rules");
        print.println("");
        print.println("## Rule List");

        for (PluginInfoRule ruleInfo: pluginInfo.getRules()) {
            print.println("");
            print.println("### " + ruleInfo.getName());

            for (PluginMessage msg: ruleInfo.getMessages()) {
                final String desc = descriptions.get(msg.getCode()) != null ?
                        descriptions.get(msg.getCode()).replace(">", "&gt;").replace("<", "&lt;") : "";
                print.println("");
                print.println("#### "+ msg.getCode());
                if (desc.length() > 0) {
                    print.println("");
                    print.println(desc);
                }
                if (msg.getSeverity().toString().length() > 0) {
                    print.println("");
                    print.println("**Severity**: " + msg.getSeverity());
                }
                final String messageText = cleanUpMessage(msg,ruleInfo);
                if (messageText.length() > 0) {
                    print.println("");
                    print.println("**Message**: " + messageText);
                }
            }

            if (!ruleInfo.getParameters().isEmpty() && !diminishParams.contains(ruleInfo.getClassName())) {
                print.println("");
                print.println("#### " + ruleInfo.getName() + " Parameters");
                for (PluginParameter p: ruleInfo.getParameters()) {
                    print.println("");
                    print.println("* " + p.getName() + " = *" + p.getValue() + "*");
                }
            }
        }

        print.println("");
        print.println("## Rule Groups");
        for (final RuleGroup ruleGroup : pluginInfo.getRuleGroups()) {
            print.println("");
            print.println("### " + ruleGroup.getName());
            for (final PluginMessage msg : ruleGroup.getMessages()) {
                print.println("");
                print.println("* [" + msg.getCode() + "](#" + msg.getCode().toLowerCase() + ")");
            }
        }
    }

    private static String cleanUpMessage(PluginMessage msg, PluginInfoRule ruleInfo) {
        String retval = msg.getMessageText();
        for(PluginParameter p:ruleInfo.getParameters()){
            if(p.getValue()!=null)
                retval=retval.replace("${" + p.getName() + "}", p.getValue().toString());
        }
        return retval.replaceAll("\\$\\{","*").replaceAll("\\}","*").replaceAll(">","&gt;").replaceAll("<","&lt;");
    }
}
