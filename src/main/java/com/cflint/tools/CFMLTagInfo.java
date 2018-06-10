package com.cflint.tools;

import cfml.dictionary.Parameter;
import cfml.dictionary.Return;
import cfml.dictionary.SyntaxDictionary;
import cfml.dictionary.Tag;
import com.cflint.CF;
import net.htmlparser.jericho.Element;

public class CFMLTagInfo {

    private final SyntaxDictionary dictionary;

    public CFMLTagInfo(final SyntaxDictionary dictionary) {
        this.dictionary = dictionary;
    }
    
    public boolean isTag(String elementName){
        final Tag tag = dictionary.getTag(elementName.toLowerCase());
        return tag != null;
    }

    /**
     * 
     * @param element           the element
     * @param attributeName     the attribute name
     * @return true when the tag/attribute combination represents a variable
     *         assignment.
     */
    public boolean isAssignmentAttribute(final Element element, final String attributeName) {
        if (element != null) {
            return isAssignmentAttribute(element.getName(), attributeName);
        }
        return false;
    }

    /**
     * 
     * @param elementName       the element
     * @param attributeName     the attribute name
     * @return true when the tag/attribute combination represents a variable
     *         assignment.
     */
    public boolean isAssignmentAttribute(final String elementName, final String attributeName) {
        if (elementName != null && attributeName != null) {
            // Hardcoded exceptions to the dictionary
            if (elementName.equalsIgnoreCase(CF.CFPROCPARAM)) {
                return "variable".equalsIgnoreCase(attributeName);
            }
            if (elementName.equalsIgnoreCase(CF.CFCOOKIE) && "name".equalsIgnoreCase(attributeName)) {
                return false;
            }
            final Tag tag = dictionary.getTag(elementName.toLowerCase());
            if (tag != null) {
                for (final Object retObj : tag.getReturns()) {
                    final Return ret = (Return) retObj;
                    if (attributeName.equalsIgnoreCase(ret.getParameterName())) {
                        return true;
                    }
                }
                for (final Object paramObj : tag.getParameters()) {
                    final Parameter param = (Parameter) paramObj;
                    if (attributeName.equalsIgnoreCase(param.getName())) {
                        return "variablename".equalsIgnoreCase(param.getReturnVarType());
                    }
                }
            }
        }
        return false;
    }

    /**
     * 
     * @param element           the element
     * @param attributeName     the attribute name
     * @return true when the tag/attribute combination represents a variable
     *         reference.
     */
    public boolean isExpressionAttribute(final Element element, final String attributeName) {
        if (element != null) {
            return isExpressionAttribute(element.getName(), attributeName);
        }
        return false;
    }

    /**
     * 
     * @param elementName       the element
     * @param attributeName     the attribute name
     * @return true when the tag/attribute combination represents a variable
     *         reference.
     */
    public boolean isExpressionAttribute(final String elementName, final String attributeName) {
        if("cfdump".equalsIgnoreCase(elementName))
            return false;
        if (isAssignmentAttribute(elementName, attributeName)) {
            return true;
        }
        if ((elementName != null) && (attributeName != null)) {
            final Tag tag = dictionary.getTag(elementName.toLowerCase());
            if (tag != null) {
                for (final Object paramObj : tag.getParameters()) {
                    final Parameter param = (Parameter) paramObj;
                    if (attributeName.equalsIgnoreCase(param.getName())) {
                        return "query".equalsIgnoreCase(param.getType())
                                || "variableName".equalsIgnoreCase(param.getType())
                                || "array".equalsIgnoreCase(param.getType())
                                || "struct".equalsIgnoreCase(param.getType())
                                || "any".equalsIgnoreCase(param.getType());
                    }
                }
            }
        }
        return false;
    }
}
