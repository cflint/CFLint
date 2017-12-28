package com.cflint.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;

import com.cflint.exception.CFLintConfigurationException;

public class ConfigFileLoader {

    /**
     * Load the configuration from a file, ignore a null configuration filename.
     * bubble up any exceptions.
     *
     * @param configfile        the config file
     * @return                  the configuration
     * @throws CFLintConfigurationException exception thrown when there is an issue with the configuration
     */
    public static CFLintConfig loadConfig(final String configfile) throws CFLintConfigurationException {
        if (configfile != null) {
            try {
                CFLintPluginInfo pluginInfo = null;
                if (configfile.toLowerCase().endsWith(".xml")) {
                    @SuppressWarnings("deprecation")
                    final Object configObj = ConfigUtils.unmarshal(new File(configfile));
                    if (configObj instanceof CFLintPluginInfo) {
                        pluginInfo = (CFLintPluginInfo) configObj;
                    } else if (configObj instanceof CFLintConfig) {
                        return (CFLintConfig) configObj;
                    }
                } else {
                    pluginInfo = ConfigUtils.unmarshalJson(new FileInputStream(configfile), CFLintPluginInfo.class);
                }
                final CFLintConfig returnVal = new CFLintConfig();
                returnVal.setRules(pluginInfo.getRules());
                return returnVal;
            } catch (final Exception e) {
                throw new CFLintConfigurationException(e);
            }
        }
        return null;
    }

    /**
     * Load the configuration. If there is an exception print it on the provided
     * printstream, but return a null.
     *
     * @param configfile        the config file
     * @param printStreamErr    print stream to log exceptions
     * @return                  the configuration
     */
    public static CFLintConfig loadConfigQuietly(final String configfile, final PrintStream printStreamErr) {
        if (configfile != null) {
            try {
                return loadConfig(configfile);
            } catch (final CFLintConfigurationException e) {
                if (printStreamErr != null) {
                    printStreamErr.println("Unable to load config file. " + e.getMessage());
                }
            }
        }
        return null;
    }
}