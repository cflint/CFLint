package com.cflint.config;

import java.util.Arrays;
import java.util.List;

import com.cflint.config.CFLintPluginInfo.PluginInfoRule;
import com.cflint.config.CFLintPluginInfo.PluginInfoRule.PluginParameter;
import com.cflint.plugins.CFLintScanner;

public abstract class BaseConfig implements CFLintConfiguration{

    /**
     * get the string property from the configuration.
     * This can be overriden with -DcheckerClass.propertyname=value
     *
     * @param linter    instance of current CFLintScanner plugin
     * @param name      the name of the parameter
     * @return          the value of the parameter (may be null)
     */
    @Override
    public String getParameter(final CFLintScanner linter, final String name) {
        final String propertyForName = System.getProperty(linter.getClass().getSimpleName() + "." + name);
        if (propertyForName != null && propertyForName.trim().length() > 0) {
            return propertyForName;
        }
        //Return the property from the config.parameters if available.
        final Object overrideProperty = getParameter(linter.getClass().getSimpleName() + "." + name);
        if(overrideProperty != null){
            return overrideProperty.toString();
        }
        final PluginInfoRule relativePlugin = getRuleForPlugin(linter);
        if(name != null && relativePlugin != null && relativePlugin.getParameters()!=null){
            for(PluginParameter contextParm : relativePlugin.getParameters()){
                if(name.equalsIgnoreCase(contextParm.getName()) && contextParm.getValue()!=null){
                    return contextParm.getValue().toString();
                }
            }
        }

        return null;
    }

    /**
     * get the string property from the configuration.  If it is null return empty string.
     * This can be overriden with -DcheckerClass.propertyname=value
     *
     * @param linter    instance of current CFLintScanner plugin
     * @param name      the name of the parameter
     * @return          the value of the parameter (will not be null)
     */
    @Override
    public String getParameterNotNull(final CFLintScanner linter, final String name) {
        final String retval = getParameter(linter, name);
        if(retval != null){
            return retval;
        }
        return "";
    }

    /**
     * get the property from the configuration.
     * This can be overriden with -DcheckerClass.propertyname=value
     *
     * @param linter    instance of current CFLintScanner plugin
     * @param name      the name of the parameter
     * @param clazz     the desired class of the returned value.
     * @return          the value of the parameter
     */
    @SuppressWarnings("unchecked")
    @Override
    public <E> E getParameter(final CFLintScanner linter, final String name, final Class<E> clazz) {
        final String propertyForName = System.getProperty(linter.getClass().getSimpleName() + "." + name);
        if (propertyForName != null && propertyForName.trim().length() > 0) {
            if (clazz.equals(String.class)) {
                return (E) propertyForName;
            }
            if (List.class.isAssignableFrom(clazz)) {
                return (E) Arrays.asList(propertyForName.split(","));
            }
            System.err.println("Cannot associate property " + linter.getClass().getSimpleName() + "." + name + " as a " + clazz.getName());
        }
        //Return the property from the config.parameters if available.
        final Object overrideProperty = getParameter(linter.getClass().getSimpleName() + "." + name);
        if(overrideProperty != null){
            if (List.class.isAssignableFrom(clazz) && overrideProperty instanceof String) {
                return (E) Arrays.asList(overrideProperty.toString().split(","));
            }
            return (E) overrideProperty;
        }
        final PluginInfoRule relativePlugin = getRuleForPlugin(linter);
        if(name != null && relativePlugin != null && relativePlugin.getParameters()!=null){
            for(PluginParameter contextParm : relativePlugin.getParameters()){
                if(name.equalsIgnoreCase(contextParm.getName()) && contextParm.getValue()!=null){
                    return (E) contextParm.getValue();
                }
            }
        }
        return null;
    }
}