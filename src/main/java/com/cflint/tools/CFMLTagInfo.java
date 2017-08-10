package com.cflint.tools;

import java.util.Map;

import cfml.dictionary.SyntaxDictionary;
import net.htmlparser.jericho.Element;

public class CFMLTagInfo {
    
   
    /**
     * 
     * @param elem
     * @param attributeName
     * @return true when the tag/attribute combination represents a variable assignment.
     */
    public boolean isAssignmentAttribute(final Element elem, final String attributeName) {
        if(elem != null){
            return isAssignmentAttribute(elem.getName(),attributeName);
        }
        return false;
    }
    
    /**
     * 
     * @param elementName
     * @param attributeName
     * @return true when the tag/attribute combination represents a variable assignment.
     */
    public boolean isAssignmentAttribute(final String elementName, final String attributeName) {
        if(elementName!=null && attributeName != null){
            switch(elementName.toLowerCase()){
                case "cfloop":
                    return attributeName.equalsIgnoreCase("index");
                case "cffeed":
                    return attributeName.equalsIgnoreCase("query");
                case "cfprocparam":
                    return attributeName.equalsIgnoreCase("variable");
                    
            }
        }
        return false;
    }
}
