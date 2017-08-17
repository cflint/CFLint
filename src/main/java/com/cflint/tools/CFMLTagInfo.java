package com.cflint.tools;

import cfml.dictionary.Parameter;
import cfml.dictionary.Return;
import cfml.dictionary.SyntaxDictionary;
import cfml.dictionary.Tag;
import net.htmlparser.jericho.Element;

public class CFMLTagInfo {

    final SyntaxDictionary dictionary;

    public CFMLTagInfo(final SyntaxDictionary dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * 
     * @param elem
     * @param attributeName
     * @return true when the tag/attribute combination represents a variable
     *         assignment.
     */
    public boolean isAssignmentAttribute(final Element elem, final String attributeName) {
        if (elem != null) {
            return isAssignmentAttribute(elem.getName(), attributeName);
        }
        return false;
    }

    /**
     * 
     * @param elementName
     * @param attributeName
     * @return true when the tag/attribute combination represents a variable
     *         assignment.
     */
    public boolean isAssignmentAttribute(final String elementName, final String attributeName) {
        if ((elementName != null) && (attributeName != null)) {
            // Hardcoded exceptions to the dictionary
            if (elementName.toLowerCase().equals("cfprocparam")) {
                return attributeName.equalsIgnoreCase("variable");
            }
            final Tag tag = dictionary.getTag(elementName.toLowerCase());
            if (tag != null) {
                for (final Object retObj : tag.getReturns()) {
                    final Return ret = (Return) retObj;
                    if (attributeName.toLowerCase().equals(ret.getParameterName())) {
                        return true;
                    }
                }
                for (final Object paramObj : tag.getParameters()) {
                    final Parameter param = (Parameter) paramObj;
                    if (attributeName.toLowerCase().equals(param.getName())) {
                        return "variablename".equalsIgnoreCase(param.getReturnVarType());
                    }
                }
            }
        }
        return false;
    }

    /**
     * 
     * @param elem
     * @param attributeName
     * @return true when the tag/attribute combination represents a variable
     *         refernce.
     */
    public boolean isExpressionAttribute(final Element elem, final String attributeName) {
        if (elem != null) {
            return isExpressionAttribute(elem.getName(), attributeName);
        }
        return false;
    }

    /**
     * 
     * @param elementName
     * @param attributeName
     * @return true when the tag/attribute combination represents a variable
     *         refernce.
     */
    public boolean isExpressionAttribute(final String elementName, final String attributeName) {
        if (isAssignmentAttribute(elementName, attributeName)) {
            return true;
        }
        if ((elementName != null) && (attributeName != null)) {
            final Tag tag = dictionary.getTag(elementName.toLowerCase());
            if (tag != null) {
                for (final Object paramObj : tag.getParameters()) {
                    final Parameter param = (Parameter) paramObj;
                    if (attributeName.toLowerCase().equals(param.getName())) {
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
