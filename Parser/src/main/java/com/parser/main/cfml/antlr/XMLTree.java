// $ANTLR 3.4 /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLTree.g 2012-10-27 03:09:59

  package com.parser.main.cfml.antlr;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class XMLTree extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ATTRIBUTE", "CDATA", "COMMENT", "ELEMENT", "EMPTY_ELEMENT", "END_TAG", "GENERIC_ID", "LETTER", "PCDATA", "START_TAG", "VALUE", "WS", "ATTR_VALUE"
    };

    public static final int EOF=-1;
    public static final int ATTRIBUTE=4;
    public static final int CDATA=5;
    public static final int COMMENT=6;
    public static final int ELEMENT=7;
    public static final int EMPTY_ELEMENT=8;
    public static final int END_TAG=9;
    public static final int GENERIC_ID=10;
    public static final int LETTER=11;
    public static final int PCDATA=12;
    public static final int START_TAG=13;
    public static final int VALUE=14;
    public static final int WS=15;
    public static final int ATTR_VALUE=16;

    // delegates
    public TreeParser[] getDelegates() {
        return new TreeParser[] {};
    }

    // delegators


    public XMLTree(TreeNodeStream input) {
        this(input, new RecognizerSharedState());
    }
    public XMLTree(TreeNodeStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return XMLTree.tokenNames; }
    public String getGrammarFileName() { return "/Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLTree.g"; }



    // $ANTLR start "document"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLTree.g:12:1: document : element ;
    public final void document() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLTree.g:12:10: ( element )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLTree.g:12:12: element
            {
            pushFollow(FOLLOW_element_in_document37);
            element();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "document"



    // $ANTLR start "element"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLTree.g:14:1: element : ^( ELEMENT name= GENERIC_ID ( ^( ATTRIBUTE attrName= GENERIC_ID value= ATTR_VALUE ) )* ( element |text= PCDATA )* ) ;
    public final void element() throws RecognitionException {
        CommonTree name=null;
        CommonTree attrName=null;
        CommonTree value=null;
        CommonTree text=null;

        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLTree.g:15:5: ( ^( ELEMENT name= GENERIC_ID ( ^( ATTRIBUTE attrName= GENERIC_ID value= ATTR_VALUE ) )* ( element |text= PCDATA )* ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLTree.g:15:7: ^( ELEMENT name= GENERIC_ID ( ^( ATTRIBUTE attrName= GENERIC_ID value= ATTR_VALUE ) )* ( element |text= PCDATA )* )
            {
            match(input,ELEMENT,FOLLOW_ELEMENT_in_element52); 

            match(input, Token.DOWN, null); 
            name=(CommonTree)match(input,GENERIC_ID,FOLLOW_GENERIC_ID_in_element56); 

             System.out.print("<"+(name!=null?name.getText():null)); 

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLTree.g:17:13: ( ^( ATTRIBUTE attrName= GENERIC_ID value= ATTR_VALUE ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==ATTRIBUTE) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLTree.g:18:17: ^( ATTRIBUTE attrName= GENERIC_ID value= ATTR_VALUE )
            	    {
            	    match(input,ATTRIBUTE,FOLLOW_ATTRIBUTE_in_element103); 

            	    match(input, Token.DOWN, null); 
            	    attrName=(CommonTree)match(input,GENERIC_ID,FOLLOW_GENERIC_ID_in_element107); 

            	    value=(CommonTree)match(input,ATTR_VALUE,FOLLOW_ATTR_VALUE_in_element111); 

            	    match(input, Token.UP, null); 


            	     System.out.print(" "+(attrName!=null?attrName.getText():null)+"="+(value!=null?value.getText():null)); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


             System.out.println(">"); 

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLTree.g:22:13: ( element |text= PCDATA )*
            loop2:
            do {
                int alt2=3;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==ELEMENT) ) {
                    alt2=1;
                }
                else if ( (LA2_0==PCDATA) ) {
                    alt2=2;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLTree.g:22:14: element
            	    {
            	    pushFollow(FOLLOW_element_in_element174);
            	    element();

            	    state._fsp--;


            	    }
            	    break;
            	case 2 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLTree.g:23:15: text= PCDATA
            	    {
            	    text=(CommonTree)match(input,PCDATA,FOLLOW_PCDATA_in_element192); 

            	     System.out.print((text!=null?text.getText():null)); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


             System.out.println("</"+(name!=null?name.getText():null)+">"); 

            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "element"

    // Delegated rules


 

    public static final BitSet FOLLOW_element_in_document37 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ELEMENT_in_element52 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_GENERIC_ID_in_element56 = new BitSet(new long[]{0x0000000000001098L});
    public static final BitSet FOLLOW_ATTRIBUTE_in_element103 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_GENERIC_ID_in_element107 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_ATTR_VALUE_in_element111 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_element_in_element174 = new BitSet(new long[]{0x0000000000001088L});
    public static final BitSet FOLLOW_PCDATA_in_element192 = new BitSet(new long[]{0x0000000000001088L});

}