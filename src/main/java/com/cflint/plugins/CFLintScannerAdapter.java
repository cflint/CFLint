package com.cflint.plugins;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cflint.BugList;

import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;
import net.htmlparser.jericho.Element;

/**
 * Lint Rule Plugins can extend this adapter instead of implementing all the
 * methods of CFLintScanner (and CFLintStructureListener)
 *
 * @author eberlyrh
 */
public class CFLintScannerAdapter implements CFLintScanner, CFLintStructureListener {

    private Map<String, Object> params = new HashMap<>();

    /**
     * Empty implementation
     */
    @Override
    public void expression(final CFExpression expression, final Context context, final BugList bugs) {
        //empty body for Adapter
    }

    /**
     * Empty implementation
     */
    @Override
    public void element(final Element element, final Context context, final BugList bugs) {
        //empty body for Adapter
    }

    /**
     * Empty implementation
     */
    @Override
    public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
        //empty body for Adapter
    }

    @Override
    public void setParameter(final String name, final Object value) {
        if (name != null) {
            params.put(name.toLowerCase(), value);
            params.put(name, value);
        }
    }

    /**
     * get the property from the configuration.
     * This can be overriden with -DcheckerClass.propertyname=value
     *
     * @param name
     * @return
     */
    public String getParameter(final String name) {
        final String propertyForName = System.getProperty(getClass().getSimpleName() + "." + name);
        if (propertyForName != null && propertyForName.trim().length() > 0) {
            return propertyForName;
        }
        if (name != null && params.get(name.toLowerCase()) != null) {
            return params.get(name.toLowerCase()).toString();
        }

        return null;
    }

    public String getParameterNotNull(final String name) {
        if (name != null) {
            Object retval = params.get(name.toLowerCase());
            if (retval != null) {
                return retval.toString();
            }
        }
        return "";
    }

    public <E> E getParameter(final String name, final Class<E> clazz) {
        final String propertyForName = System.getProperty(getClass().getSimpleName() + "." + name);
        if (propertyForName != null && propertyForName.trim().length() > 0) {
            if (clazz.equals(String.class)) {
                return (E) propertyForName;
            }
            if (List.class.isAssignableFrom(clazz)) {
                return (E) Arrays.asList(propertyForName.split(","));
            }
            System.err.println("Cannot associate property " + getClass().getSimpleName() + "." + name + " as a " + clazz.getName());
        }
        if (name != null && params.get(name.toLowerCase()) != null) {
            return (E) params.get(name.toLowerCase());
        }

        return null;
    }

    /**
     * Return parameter split by comma
     *
     * @param name
     * @return
     */
    public List<String> getParameterAsList(final String name) {
        return Arrays.asList(getParameterNotNull(name).split(","));
    }

    public int currentLine(final CFExpression expression, final Context context) {
        return expression.getLine() + context.startLine() - 1;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * Default implementation does nothing
     */
    @Override
    public void startFile(final String fileName, final BugList bugs) {
        //empty body for Adapter
    }

    /**
     * Default implementation does nothing
     */
    @Override
    public void endFile(final String fileName, final BugList bugs) {
        //empty body for Adapter
    }

    /**
     * Default implementation does nothing
     */
    @Override
    public void startComponent(final Context context, final BugList bugs) {
        //empty body for Adapter
    }

    /**
     * Default implementation does nothing
     */
    @Override
    public void endComponent(final Context context, final BugList bugs) {
        //empty body for Adapter
    }

    /**
     * Default implementation does nothing
     */
    @Override
    public void startFunction(final Context context, final BugList bugs) {
        //empty body for Adapter
    }

    /**
     * Default implementation does nothing
     */
    @Override
    public void endFunction(final Context context, final BugList bugs) {
        //empty body for Adapter
    }
}
