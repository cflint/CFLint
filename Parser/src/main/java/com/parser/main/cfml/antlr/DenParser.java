// $ANTLR 3.4 /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g 2012-10-27 03:10:08

package com.parser.main.cfml.antlr;

import java.util.LinkedList;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class DenParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ATTR_EQ", "ATTR_VALUE", "DIGIT", "GENERIC_ID", "LETTER", "NAMECHAR", "PCDATA", "TAG_CLOSE", "TAG_EMPTY_CLOSE", "TAG_END_OPEN", "TAG_START_OPEN", "WS", "ATTRIBUTE", "CFIF", "ELEMENT"
    };

    public static final int EOF=-1;
    public static final int ATTR_EQ=4;
    public static final int ATTR_VALUE=5;
    public static final int DIGIT=6;
    public static final int GENERIC_ID=7;
    public static final int LETTER=8;
    public static final int NAMECHAR=9;
    public static final int PCDATA=10;
    public static final int TAG_CLOSE=11;
    public static final int TAG_EMPTY_CLOSE=12;
    public static final int TAG_END_OPEN=13;
    public static final int TAG_START_OPEN=14;
    public static final int WS=15;
    public static final int ATTRIBUTE=16;
    public static final int CFIF=17;
    public static final int ELEMENT=18;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators

    protected static class ElementScope_scope {
        String currentElementName;
    }
    protected Stack ElementScope_stack = new Stack();



    public DenParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public DenParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return DenParser.tokenNames; }
    public String getGrammarFileName() { return "/Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g"; }


    public static class document_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "document"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:24:1: document : tags ;
    public final DenParser.document_return document() throws RecognitionException {
        DenParser.document_return retval = new DenParser.document_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        DenParser.tags_return tags1 =null;



        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:24:10: ( tags )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:24:12: tags
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_tags_in_document74);
            tags1=tags();

            state._fsp--;

            adaptor.addChild(root_0, tags1.getTree());

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "document"


    public static class tags_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "tags"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:26:1: tags : ( ifStatement | element ) ;
    public final DenParser.tags_return tags() throws RecognitionException {
        DenParser.tags_return retval = new DenParser.tags_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        DenParser.ifStatement_return ifStatement2 =null;

        DenParser.element_return element3 =null;



        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:26:6: ( ( ifStatement | element ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:26:8: ( ifStatement | element )
            {
            root_0 = (CommonTree)adaptor.nil();


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:26:8: ( ifStatement | element )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==TAG_START_OPEN) ) {
                int LA1_1 = input.LA(2);

                if ( (LA1_1==CFIF) ) {
                    alt1=1;
                }
                else if ( (LA1_1==GENERIC_ID) ) {
                    alt1=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;

            }
            switch (alt1) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:26:9: ifStatement
                    {
                    pushFollow(FOLLOW_ifStatement_in_tags84);
                    ifStatement2=ifStatement();

                    state._fsp--;

                    adaptor.addChild(root_0, ifStatement2.getTree());

                    }
                    break;
                case 2 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:26:23: element
                    {
                    pushFollow(FOLLOW_element_in_tags88);
                    element3=element();

                    state._fsp--;

                    adaptor.addChild(root_0, element3.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "tags"


    public static class element_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "element"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:28:1: element : ( startTag ^ ( element | PCDATA )* endTag !| emptyElement ) ;
    public final DenParser.element_return element() throws RecognitionException {
        ElementScope_stack.push(new ElementScope_scope());

        DenParser.element_return retval = new DenParser.element_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token PCDATA6=null;
        DenParser.startTag_return startTag4 =null;

        DenParser.element_return element5 =null;

        DenParser.endTag_return endTag7 =null;

        DenParser.emptyElement_return emptyElement8 =null;


        CommonTree PCDATA6_tree=null;

        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:30:5: ( ( startTag ^ ( element | PCDATA )* endTag !| emptyElement ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:30:7: ( startTag ^ ( element | PCDATA )* endTag !| emptyElement )
            {
            root_0 = (CommonTree)adaptor.nil();


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:30:7: ( startTag ^ ( element | PCDATA )* endTag !| emptyElement )
            int alt3=2;
            alt3 = dfa3.predict(input);
            switch (alt3) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:30:9: startTag ^ ( element | PCDATA )* endTag !
                    {
                    pushFollow(FOLLOW_startTag_in_element108);
                    startTag4=startTag();

                    state._fsp--;

                    root_0 = (CommonTree)adaptor.becomeRoot(startTag4.getTree(), root_0);

                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:31:13: ( element | PCDATA )*
                    loop2:
                    do {
                        int alt2=3;
                        int LA2_0 = input.LA(1);

                        if ( (LA2_0==TAG_START_OPEN) ) {
                            alt2=1;
                        }
                        else if ( (LA2_0==PCDATA) ) {
                            alt2=2;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:31:14: element
                    	    {
                    	    pushFollow(FOLLOW_element_in_element124);
                    	    element5=element();

                    	    state._fsp--;

                    	    adaptor.addChild(root_0, element5.getTree());

                    	    }
                    	    break;
                    	case 2 :
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:32:15: PCDATA
                    	    {
                    	    PCDATA6=(Token)match(input,PCDATA,FOLLOW_PCDATA_in_element140); 
                    	    PCDATA6_tree = 
                    	    (CommonTree)adaptor.create(PCDATA6)
                    	    ;
                    	    adaptor.addChild(root_0, PCDATA6_tree);


                    	    }
                    	    break;

                    	default :
                    	    break loop2;
                        }
                    } while (true);


                    pushFollow(FOLLOW_endTag_in_element169);
                    endTag7=endTag();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:35:11: emptyElement
                    {
                    pushFollow(FOLLOW_emptyElement_in_element182);
                    emptyElement8=emptyElement();

                    state._fsp--;

                    adaptor.addChild(root_0, emptyElement8.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
            ElementScope_stack.pop();

        }
        return retval;
    }
    // $ANTLR end "element"


    public static class ifStatement_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "ifStatement"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:39:1: ifStatement : TAG_START_OPEN CFIF ( attribute )* TAG_CLOSE -> ^( ELEMENT CFIF ( attribute )* ) ;
    public final DenParser.ifStatement_return ifStatement() throws RecognitionException {
        DenParser.ifStatement_return retval = new DenParser.ifStatement_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token TAG_START_OPEN9=null;
        Token CFIF10=null;
        Token TAG_CLOSE12=null;
        DenParser.attribute_return attribute11 =null;


        CommonTree TAG_START_OPEN9_tree=null;
        CommonTree CFIF10_tree=null;
        CommonTree TAG_CLOSE12_tree=null;
        RewriteRuleTokenStream stream_CFIF=new RewriteRuleTokenStream(adaptor,"token CFIF");
        RewriteRuleTokenStream stream_TAG_CLOSE=new RewriteRuleTokenStream(adaptor,"token TAG_CLOSE");
        RewriteRuleTokenStream stream_TAG_START_OPEN=new RewriteRuleTokenStream(adaptor,"token TAG_START_OPEN");
        RewriteRuleSubtreeStream stream_attribute=new RewriteRuleSubtreeStream(adaptor,"rule attribute");
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:40:5: ( TAG_START_OPEN CFIF ( attribute )* TAG_CLOSE -> ^( ELEMENT CFIF ( attribute )* ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:40:7: TAG_START_OPEN CFIF ( attribute )* TAG_CLOSE
            {
            TAG_START_OPEN9=(Token)match(input,TAG_START_OPEN,FOLLOW_TAG_START_OPEN_in_ifStatement209);  
            stream_TAG_START_OPEN.add(TAG_START_OPEN9);


            CFIF10=(Token)match(input,CFIF,FOLLOW_CFIF_in_ifStatement211);  
            stream_CFIF.add(CFIF10);


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:40:27: ( attribute )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==GENERIC_ID) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:40:27: attribute
            	    {
            	    pushFollow(FOLLOW_attribute_in_ifStatement213);
            	    attribute11=attribute();

            	    state._fsp--;

            	    stream_attribute.add(attribute11.getTree());

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            TAG_CLOSE12=(Token)match(input,TAG_CLOSE,FOLLOW_TAG_CLOSE_in_ifStatement216);  
            stream_TAG_CLOSE.add(TAG_CLOSE12);


            ((ElementScope_scope)ElementScope_stack.peek()).currentElementName = (CFIF10!=null?CFIF10.getText():null); 

            // AST REWRITE
            // elements: CFIF, attribute
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 42:9: -> ^( ELEMENT CFIF ( attribute )* )
            {
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:42:12: ^( ELEMENT CFIF ( attribute )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(ELEMENT, "ELEMENT")
                , root_1);

                adaptor.addChild(root_1, 
                stream_CFIF.nextNode()
                );

                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:42:27: ( attribute )*
                while ( stream_attribute.hasNext() ) {
                    adaptor.addChild(root_1, stream_attribute.nextTree());

                }
                stream_attribute.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "ifStatement"


    public static class startTag_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "startTag"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:46:1: startTag : TAG_START_OPEN GENERIC_ID ( attribute )* TAG_CLOSE -> ^( ELEMENT GENERIC_ID ( attribute )* ) ;
    public final DenParser.startTag_return startTag() throws RecognitionException {
        DenParser.startTag_return retval = new DenParser.startTag_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token TAG_START_OPEN13=null;
        Token GENERIC_ID14=null;
        Token TAG_CLOSE16=null;
        DenParser.attribute_return attribute15 =null;


        CommonTree TAG_START_OPEN13_tree=null;
        CommonTree GENERIC_ID14_tree=null;
        CommonTree TAG_CLOSE16_tree=null;
        RewriteRuleTokenStream stream_TAG_CLOSE=new RewriteRuleTokenStream(adaptor,"token TAG_CLOSE");
        RewriteRuleTokenStream stream_TAG_START_OPEN=new RewriteRuleTokenStream(adaptor,"token TAG_START_OPEN");
        RewriteRuleTokenStream stream_GENERIC_ID=new RewriteRuleTokenStream(adaptor,"token GENERIC_ID");
        RewriteRuleSubtreeStream stream_attribute=new RewriteRuleSubtreeStream(adaptor,"rule attribute");
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:47:5: ( TAG_START_OPEN GENERIC_ID ( attribute )* TAG_CLOSE -> ^( ELEMENT GENERIC_ID ( attribute )* ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:47:7: TAG_START_OPEN GENERIC_ID ( attribute )* TAG_CLOSE
            {
            TAG_START_OPEN13=(Token)match(input,TAG_START_OPEN,FOLLOW_TAG_START_OPEN_in_startTag268);  
            stream_TAG_START_OPEN.add(TAG_START_OPEN13);


            GENERIC_ID14=(Token)match(input,GENERIC_ID,FOLLOW_GENERIC_ID_in_startTag270);  
            stream_GENERIC_ID.add(GENERIC_ID14);


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:47:33: ( attribute )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==GENERIC_ID) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:47:33: attribute
            	    {
            	    pushFollow(FOLLOW_attribute_in_startTag272);
            	    attribute15=attribute();

            	    state._fsp--;

            	    stream_attribute.add(attribute15.getTree());

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            TAG_CLOSE16=(Token)match(input,TAG_CLOSE,FOLLOW_TAG_CLOSE_in_startTag275);  
            stream_TAG_CLOSE.add(TAG_CLOSE16);


            ((ElementScope_scope)ElementScope_stack.peek()).currentElementName = (GENERIC_ID14!=null?GENERIC_ID14.getText():null); 

            // AST REWRITE
            // elements: GENERIC_ID, attribute
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 49:9: -> ^( ELEMENT GENERIC_ID ( attribute )* )
            {
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:49:12: ^( ELEMENT GENERIC_ID ( attribute )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(ELEMENT, "ELEMENT")
                , root_1);

                adaptor.addChild(root_1, 
                stream_GENERIC_ID.nextNode()
                );

                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:49:33: ( attribute )*
                while ( stream_attribute.hasNext() ) {
                    adaptor.addChild(root_1, stream_attribute.nextTree());

                }
                stream_attribute.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "startTag"


    public static class attribute_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "attribute"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:52:1: attribute : GENERIC_ID ATTR_EQ ATTR_VALUE -> ^( ATTRIBUTE GENERIC_ID ATTR_VALUE ) ;
    public final DenParser.attribute_return attribute() throws RecognitionException {
        DenParser.attribute_return retval = new DenParser.attribute_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token GENERIC_ID17=null;
        Token ATTR_EQ18=null;
        Token ATTR_VALUE19=null;

        CommonTree GENERIC_ID17_tree=null;
        CommonTree ATTR_EQ18_tree=null;
        CommonTree ATTR_VALUE19_tree=null;
        RewriteRuleTokenStream stream_ATTR_EQ=new RewriteRuleTokenStream(adaptor,"token ATTR_EQ");
        RewriteRuleTokenStream stream_ATTR_VALUE=new RewriteRuleTokenStream(adaptor,"token ATTR_VALUE");
        RewriteRuleTokenStream stream_GENERIC_ID=new RewriteRuleTokenStream(adaptor,"token GENERIC_ID");

        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:52:11: ( GENERIC_ID ATTR_EQ ATTR_VALUE -> ^( ATTRIBUTE GENERIC_ID ATTR_VALUE ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:52:13: GENERIC_ID ATTR_EQ ATTR_VALUE
            {
            GENERIC_ID17=(Token)match(input,GENERIC_ID,FOLLOW_GENERIC_ID_in_attribute322);  
            stream_GENERIC_ID.add(GENERIC_ID17);


            ATTR_EQ18=(Token)match(input,ATTR_EQ,FOLLOW_ATTR_EQ_in_attribute324);  
            stream_ATTR_EQ.add(ATTR_EQ18);


            ATTR_VALUE19=(Token)match(input,ATTR_VALUE,FOLLOW_ATTR_VALUE_in_attribute326);  
            stream_ATTR_VALUE.add(ATTR_VALUE19);


            // AST REWRITE
            // elements: ATTR_VALUE, GENERIC_ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 52:43: -> ^( ATTRIBUTE GENERIC_ID ATTR_VALUE )
            {
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:52:46: ^( ATTRIBUTE GENERIC_ID ATTR_VALUE )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(ATTRIBUTE, "ATTRIBUTE")
                , root_1);

                adaptor.addChild(root_1, 
                stream_GENERIC_ID.nextNode()
                );

                adaptor.addChild(root_1, 
                stream_ATTR_VALUE.nextNode()
                );

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "attribute"


    public static class endTag_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "endTag"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:54:1: endTag :{...}? TAG_END_OPEN GENERIC_ID TAG_CLOSE ;
    public final DenParser.endTag_return endTag() throws RecognitionException {
        DenParser.endTag_return retval = new DenParser.endTag_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token TAG_END_OPEN20=null;
        Token GENERIC_ID21=null;
        Token TAG_CLOSE22=null;

        CommonTree TAG_END_OPEN20_tree=null;
        CommonTree GENERIC_ID21_tree=null;
        CommonTree TAG_CLOSE22_tree=null;

        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:55:5: ({...}? TAG_END_OPEN GENERIC_ID TAG_CLOSE )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:55:7: {...}? TAG_END_OPEN GENERIC_ID TAG_CLOSE
            {
            root_0 = (CommonTree)adaptor.nil();


            if ( !(( ((ElementScope_scope)ElementScope_stack.peek()).currentElementName.equals(input.LT(2).getText()) )) ) {
                throw new FailedPredicateException(input, "endTag", " $ElementScope::currentElementName.equals(input.LT(2).getText()) ");
            }

            TAG_END_OPEN20=(Token)match(input,TAG_END_OPEN,FOLLOW_TAG_END_OPEN_in_endTag358); 
            TAG_END_OPEN20_tree = 
            (CommonTree)adaptor.create(TAG_END_OPEN20)
            ;
            adaptor.addChild(root_0, TAG_END_OPEN20_tree);


            GENERIC_ID21=(Token)match(input,GENERIC_ID,FOLLOW_GENERIC_ID_in_endTag360); 
            GENERIC_ID21_tree = 
            (CommonTree)adaptor.create(GENERIC_ID21)
            ;
            adaptor.addChild(root_0, GENERIC_ID21_tree);


            TAG_CLOSE22=(Token)match(input,TAG_CLOSE,FOLLOW_TAG_CLOSE_in_endTag362); 
            TAG_CLOSE22_tree = 
            (CommonTree)adaptor.create(TAG_CLOSE22)
            ;
            adaptor.addChild(root_0, TAG_CLOSE22_tree);


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (FailedPredicateException fpe) {

                String hdr = getErrorHeader(fpe);
                String msg = "end tag (" + input.LT(2).getText() +
                             ") does not match start tag (" +
                             ((ElementScope_scope)ElementScope_stack.peek()).currentElementName +
                             ") currently open, closing it anyway";
                emitErrorMessage(hdr+" "+msg);
                consumeUntil(input, TAG_CLOSE);
                input.consume();

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "endTag"


    public static class emptyElement_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "emptyElement"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:69:1: emptyElement : TAG_START_OPEN GENERIC_ID ( attribute )* TAG_EMPTY_CLOSE -> ^( ELEMENT GENERIC_ID ( attribute )* ) ;
    public final DenParser.emptyElement_return emptyElement() throws RecognitionException {
        DenParser.emptyElement_return retval = new DenParser.emptyElement_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token TAG_START_OPEN23=null;
        Token GENERIC_ID24=null;
        Token TAG_EMPTY_CLOSE26=null;
        DenParser.attribute_return attribute25 =null;


        CommonTree TAG_START_OPEN23_tree=null;
        CommonTree GENERIC_ID24_tree=null;
        CommonTree TAG_EMPTY_CLOSE26_tree=null;
        RewriteRuleTokenStream stream_TAG_EMPTY_CLOSE=new RewriteRuleTokenStream(adaptor,"token TAG_EMPTY_CLOSE");
        RewriteRuleTokenStream stream_TAG_START_OPEN=new RewriteRuleTokenStream(adaptor,"token TAG_START_OPEN");
        RewriteRuleTokenStream stream_GENERIC_ID=new RewriteRuleTokenStream(adaptor,"token GENERIC_ID");
        RewriteRuleSubtreeStream stream_attribute=new RewriteRuleSubtreeStream(adaptor,"rule attribute");
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:69:14: ( TAG_START_OPEN GENERIC_ID ( attribute )* TAG_EMPTY_CLOSE -> ^( ELEMENT GENERIC_ID ( attribute )* ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:69:16: TAG_START_OPEN GENERIC_ID ( attribute )* TAG_EMPTY_CLOSE
            {
            TAG_START_OPEN23=(Token)match(input,TAG_START_OPEN,FOLLOW_TAG_START_OPEN_in_emptyElement381);  
            stream_TAG_START_OPEN.add(TAG_START_OPEN23);


            GENERIC_ID24=(Token)match(input,GENERIC_ID,FOLLOW_GENERIC_ID_in_emptyElement383);  
            stream_GENERIC_ID.add(GENERIC_ID24);


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:69:42: ( attribute )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==GENERIC_ID) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:69:42: attribute
            	    {
            	    pushFollow(FOLLOW_attribute_in_emptyElement385);
            	    attribute25=attribute();

            	    state._fsp--;

            	    stream_attribute.add(attribute25.getTree());

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            TAG_EMPTY_CLOSE26=(Token)match(input,TAG_EMPTY_CLOSE,FOLLOW_TAG_EMPTY_CLOSE_in_emptyElement388);  
            stream_TAG_EMPTY_CLOSE.add(TAG_EMPTY_CLOSE26);


            // AST REWRITE
            // elements: GENERIC_ID, attribute
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 70:9: -> ^( ELEMENT GENERIC_ID ( attribute )* )
            {
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:70:12: ^( ELEMENT GENERIC_ID ( attribute )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(ELEMENT, "ELEMENT")
                , root_1);

                adaptor.addChild(root_1, 
                stream_GENERIC_ID.nextNode()
                );

                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenParser.g:70:33: ( attribute )*
                while ( stream_attribute.hasNext() ) {
                    adaptor.addChild(root_1, stream_attribute.nextTree());

                }
                stream_attribute.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "emptyElement"

    // Delegated rules


    protected DFA3 dfa3 = new DFA3(this);
    static final String DFA3_eotS =
        "\10\uffff";
    static final String DFA3_eofS =
        "\10\uffff";
    static final String DFA3_minS =
        "\1\16\2\7\1\4\2\uffff\1\5\1\7";
    static final String DFA3_maxS =
        "\1\16\1\7\1\14\1\4\2\uffff\1\5\1\14";
    static final String DFA3_acceptS =
        "\4\uffff\1\1\1\2\2\uffff";
    static final String DFA3_specialS =
        "\10\uffff}>";
    static final String[] DFA3_transitionS = {
            "\1\1",
            "\1\2",
            "\1\3\3\uffff\1\4\1\5",
            "\1\6",
            "",
            "",
            "\1\7",
            "\1\3\3\uffff\1\4\1\5"
    };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        public String getDescription() {
            return "30:7: ( startTag ^ ( element | PCDATA )* endTag !| emptyElement )";
        }
    }
 

    public static final BitSet FOLLOW_tags_in_document74 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifStatement_in_tags84 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_element_in_tags88 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_startTag_in_element108 = new BitSet(new long[]{0x0000000000006400L});
    public static final BitSet FOLLOW_element_in_element124 = new BitSet(new long[]{0x0000000000006400L});
    public static final BitSet FOLLOW_PCDATA_in_element140 = new BitSet(new long[]{0x0000000000006400L});
    public static final BitSet FOLLOW_endTag_in_element169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_emptyElement_in_element182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAG_START_OPEN_in_ifStatement209 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_CFIF_in_ifStatement211 = new BitSet(new long[]{0x0000000000000880L});
    public static final BitSet FOLLOW_attribute_in_ifStatement213 = new BitSet(new long[]{0x0000000000000880L});
    public static final BitSet FOLLOW_TAG_CLOSE_in_ifStatement216 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAG_START_OPEN_in_startTag268 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_GENERIC_ID_in_startTag270 = new BitSet(new long[]{0x0000000000000880L});
    public static final BitSet FOLLOW_attribute_in_startTag272 = new BitSet(new long[]{0x0000000000000880L});
    public static final BitSet FOLLOW_TAG_CLOSE_in_startTag275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GENERIC_ID_in_attribute322 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ATTR_EQ_in_attribute324 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_ATTR_VALUE_in_attribute326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAG_END_OPEN_in_endTag358 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_GENERIC_ID_in_endTag360 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_TAG_CLOSE_in_endTag362 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAG_START_OPEN_in_emptyElement381 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_GENERIC_ID_in_emptyElement383 = new BitSet(new long[]{0x0000000000001080L});
    public static final BitSet FOLLOW_attribute_in_emptyElement385 = new BitSet(new long[]{0x0000000000001080L});
    public static final BitSet FOLLOW_TAG_EMPTY_CLOSE_in_emptyElement388 = new BitSet(new long[]{0x0000000000000002L});

}