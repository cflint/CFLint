package com.cflint.config;

import java.util.Collection;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginMessage;
import com.cflint.plugins.CFLintScanner;

public interface CFLintConfiguration {

    boolean includes(PluginMessage pluginMessage);

    boolean excludes(PluginMessage pluginMessage);

    PluginInfoRule getRuleByClass(Class<?> clazz);

    PluginInfoRule getRuleForPlugin(CFLintScanner plugin);

    void addInclude(PluginMessage pluginMessage);

    void addExclude(PluginMessage pluginMessage);

    Collection<CFLintPluginInfo.PluginInfoRule> getRules();
    
    /**
     * get the property from the configuration.
     * This can be overriden with -DcheckerClass.propertyname=value
     *
     * @param name      the name of the parameter
     * @return          the value of the parameter
     */
    public String getParameter(CFLintScanner linter, final String name);

    public String getParameterNotNull(CFLintScanner linter, final String name);

    public <E> E getParameter(CFLintScanner linter, final String name, final Class<E> clazz);

    public Object getParameter(final String name);
}
