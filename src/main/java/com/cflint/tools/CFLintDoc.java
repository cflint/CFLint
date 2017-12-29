package com.cflint.tools;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cflint.config.CFLintConfiguration;
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
        final List<String> diminishParms = Arrays.asList("UnusedLocalVarChecker","CFXTagChecker","FunctionXChecker");
        print.println("List of built-in rules and rule groups");
                print.println("======================================");
        print.println("## Rule Parameters ");
        for(PluginInfoRule ruleInfo: pluginInfo.getRules()){
            //Do not highlight specific parameters.
            if(!diminishParms.contains(ruleInfo.getClassName())){
                for(PluginParameter p: ruleInfo.getParameters()){
                    print.println("<br>" + ruleInfo.getName()+"." + p.getName() + " = *" + p.getValue() + "*");
                }
            }
        }
        print.println("## Built-in rules");
        
        for(PluginInfoRule ruleInfo: pluginInfo.getRules()){
            print.println("* "+ ruleInfo.getName());
            final String className = ruleInfo.getClassName()==null?ruleInfo.getName():ruleInfo.getClassName();
            final String fullClassName = className.contains(".")?className:
                "com.cflint.plugins.core." + className;
            //print.println("**Class:** "+fullClassName);
            if(!ruleInfo.getParameters().isEmpty()){
                print.println("    * Parameters");
                for(PluginParameter p: ruleInfo.getParameters()){
                    print.println("        * " + p.getName() + " = *" + p.getValue() + "*");
                }
            }
            int counter = 1;
            for(PluginMessage msg: ruleInfo.getMessages()){
                final String desc = descriptions.get(msg.getCode())!=null?
                        descriptions.get(msg.getCode()).replace(">", "&gt;").replace("<", "&lt;"):"";
                print.println("    * "+ msg.getCode() + " - " + desc+ "  *" + msg.getSeverity() + "*");
                print.println("        * "+ cleanUpMessage(msg,ruleInfo) );
            }
        }
        
        print.println("## Rule Groups");
        
        for (final RuleGroup ruleGroup : pluginInfo.getRuleGroups()) {
            print.println("### " + ruleGroup.getName());
            for (final PluginMessage msg : ruleGroup.getMessages()) {
                print.println(" * " + msg.getCode() + "   *" + msg.getSeverity() + "*");
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
