// $ANTLR 3.4 /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g 2012-10-27 03:10:02

package com.parser.main.cfml.antlr;

import java.util.LinkedList;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class XMLParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ATTR", "ATTR_EQ", "ATTR_VALUE", "CFTAG_ID", "DIGIT", "DoubleStringCharacter", "GENERIC_ID", "ID", "LETTER", "NAMECHAR", "PCDATA", "STRING_LITERAL", "SingleStringCharacter", "TAG_CLOSE", "TAG_EMPTY_CLOSE", "TAG_END_OPEN", "TAG_START_OPEN", "TRY", "WS", "ASSIGN", "ATTRIBUTE", "ATTRIBUTENAME", "CFMLTAG", "ELEMENT", "TAG", "TAGNAME"
    };

    public static final int EOF=-1;
    public static final int ATTR=4;
    public static final int ATTR_EQ=5;
    public static final int ATTR_VALUE=6;
    public static final int CFTAG_ID=7;
    public static final int DIGIT=8;
    public static final int DoubleStringCharacter=9;
    public static final int GENERIC_ID=10;
    public static final int ID=11;
    public static final int LETTER=12;
    public static final int NAMECHAR=13;
    public static final int PCDATA=14;
    public static final int STRING_LITERAL=15;
    public static final int SingleStringCharacter=16;
    public static final int TAG_CLOSE=17;
    public static final int TAG_EMPTY_CLOSE=18;
    public static final int TAG_END_OPEN=19;
    public static final int TAG_START_OPEN=20;
    public static final int TRY=21;
    public static final int WS=22;
    public static final int ASSIGN=23;
    public static final int ATTRIBUTE=24;
    public static final int ATTRIBUTENAME=25;
    public static final int CFMLTAG=26;
    public static final int ELEMENT=27;
    public static final int TAG=28;
    public static final int TAGNAME=29;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators

    protected static class ElementScope_scope {
        String currentElementName;
        int closerLine;
        int closerPosInLine;
    }
    protected Stack ElementScope_stack = new Stack();



    public XMLParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public XMLParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return XMLParser.tokenNames; }
    public String getGrammarFileName() { return "/Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g"; }


      protected boolean isColdFusionTag(String name)
      {   
        boolean isColdfusion = name.toLowerCase().startsWith("cf");
        System.out.println("isColdFusion: " + name + " : " + isColdfusion);
        return isColdfusion;
      }

      protected boolean isAssignmentTag(String name)
      {   
        boolean isSingle = name.toLowerCase().startsWith("cfset");
        System.out.println("isColdFusion: " + name + " : " + isSingle);
        return isSingle;
      }

      protected boolean isSingleTag(String name)
      {   
        boolean isSingle = name.toLowerCase().startsWith("cf");
        System.out.println("isColdFusion: " + name + " : " + isSingle);
        return isSingle;
      }




    public static class compilationUnit_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "compilationUnit"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:57:1: compilationUnit : tag ;
    public final XMLParser.compilationUnit_return compilationUnit() throws RecognitionException {
        XMLParser.compilationUnit_return retval = new XMLParser.compilationUnit_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        XMLParser.tag_return tag1 =null;



        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:57:17: ( tag )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:57:19: tag
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_tag_in_compilationUnit105);
            tag1=tag();

            state._fsp--;

            adaptor.addChild(root_0, tag1.getTree());

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
    // $ANTLR end "compilationUnit"


    public static class tag_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "tag"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:59:1: tag : ( element )* ;
    public final XMLParser.tag_return tag() throws RecognitionException {
        XMLParser.tag_return retval = new XMLParser.tag_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        XMLParser.element_return element2 =null;



        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:59:4: ( ( element )* )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:59:6: ( element )*
            {
            root_0 = (CommonTree)adaptor.nil();


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:59:6: ( element )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==TAG_START_OPEN) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:59:6: element
            	    {
            	    pushFollow(FOLLOW_element_in_tag112);
            	    element2=element();

            	    state._fsp--;

            	    adaptor.addChild(root_0, element2.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


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
    // $ANTLR end "tag"


    public static class element_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "element"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:61:1: element : ( ( startTag ^) ( element | PCDATA )* endTag !| emptyElement ) ;
    public final XMLParser.element_return element() throws RecognitionException {
        ElementScope_stack.push(new ElementScope_scope());

        XMLParser.element_return retval = new XMLParser.element_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token PCDATA5=null;
        XMLParser.startTag_return startTag3 =null;

        XMLParser.element_return element4 =null;

        XMLParser.endTag_return endTag6 =null;

        XMLParser.emptyElement_return emptyElement7 =null;


        CommonTree PCDATA5_tree=null;

        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:63:5: ( ( ( startTag ^) ( element | PCDATA )* endTag !| emptyElement ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:63:7: ( ( startTag ^) ( element | PCDATA )* endTag !| emptyElement )
            {
            root_0 = (CommonTree)adaptor.nil();


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:63:7: ( ( startTag ^) ( element | PCDATA )* endTag !| emptyElement )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==TAG_START_OPEN) ) {
                int LA3_1 = input.LA(2);

                if ( (LA3_1==GENERIC_ID) ) {
                    int LA3_2 = input.LA(3);

                    if ( (LA3_2==ATTR_EQ) ) {
                        alt3=2;
                    }
                    else if ( (LA3_2==GENERIC_ID||LA3_2==TAG_CLOSE) ) {
                        alt3=1;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 3, 2, input);

                        throw nvae;

                    }
                }
                else if ( (LA3_1==TAG_EMPTY_CLOSE) ) {
                    alt3=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 3, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;

            }
            switch (alt3) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:63:9: ( startTag ^) ( element | PCDATA )* endTag !
                    {
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:63:9: ( startTag ^)
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:63:10: startTag ^
                    {
                    pushFollow(FOLLOW_startTag_in_element133);
                    startTag3=startTag();

                    state._fsp--;

                    root_0 = (CommonTree)adaptor.becomeRoot(startTag3.getTree(), root_0);

                    }


                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:64:13: ( element | PCDATA )*
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
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:64:14: element
                    	    {
                    	    pushFollow(FOLLOW_element_in_element150);
                    	    element4=element();

                    	    state._fsp--;

                    	    adaptor.addChild(root_0, element4.getTree());

                    	    }
                    	    break;
                    	case 2 :
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:65:15: PCDATA
                    	    {
                    	    PCDATA5=(Token)match(input,PCDATA,FOLLOW_PCDATA_in_element166); 
                    	    PCDATA5_tree = 
                    	    (CommonTree)adaptor.create(PCDATA5)
                    	    ;
                    	    adaptor.addChild(root_0, PCDATA5_tree);


                    	    }
                    	    break;

                    	default :
                    	    break loop2;
                        }
                    } while (true);


                    pushFollow(FOLLOW_endTag_in_element195);
                    endTag6=endTag();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:68:11: emptyElement
                    {
                    pushFollow(FOLLOW_emptyElement_in_element208);
                    emptyElement7=emptyElement();

                    state._fsp--;

                    adaptor.addChild(root_0, emptyElement7.getTree());

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


    public static class startTag_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "startTag"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:72:1: startTag : el= TAG_START_OPEN name= GENERIC_ID ( attribute )* TAG_CLOSE -> ^( ELEMENT[$name] ( attribute )* ) ;
    public final XMLParser.startTag_return startTag() throws RecognitionException {
        XMLParser.startTag_return retval = new XMLParser.startTag_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token el=null;
        Token name=null;
        Token TAG_CLOSE9=null;
        XMLParser.attribute_return attribute8 =null;


        CommonTree el_tree=null;
        CommonTree name_tree=null;
        CommonTree TAG_CLOSE9_tree=null;
        RewriteRuleTokenStream stream_TAG_CLOSE=new RewriteRuleTokenStream(adaptor,"token TAG_CLOSE");
        RewriteRuleTokenStream stream_TAG_START_OPEN=new RewriteRuleTokenStream(adaptor,"token TAG_START_OPEN");
        RewriteRuleTokenStream stream_GENERIC_ID=new RewriteRuleTokenStream(adaptor,"token GENERIC_ID");
        RewriteRuleSubtreeStream stream_attribute=new RewriteRuleSubtreeStream(adaptor,"rule attribute");
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:73:5: (el= TAG_START_OPEN name= GENERIC_ID ( attribute )* TAG_CLOSE -> ^( ELEMENT[$name] ( attribute )* ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:73:7: el= TAG_START_OPEN name= GENERIC_ID ( attribute )* TAG_CLOSE
            {
            el=(Token)match(input,TAG_START_OPEN,FOLLOW_TAG_START_OPEN_in_startTag237);  
            stream_TAG_START_OPEN.add(el);


            name=(Token)match(input,GENERIC_ID,FOLLOW_GENERIC_ID_in_startTag241);  
            stream_GENERIC_ID.add(name);


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:73:41: ( attribute )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==GENERIC_ID) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:73:41: attribute
            	    {
            	    pushFollow(FOLLOW_attribute_in_startTag243);
            	    attribute8=attribute();

            	    state._fsp--;

            	    stream_attribute.add(attribute8.getTree());

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            TAG_CLOSE9=(Token)match(input,TAG_CLOSE,FOLLOW_TAG_CLOSE_in_startTag246);  
            stream_TAG_CLOSE.add(TAG_CLOSE9);


            ((ElementScope_scope)ElementScope_stack.peek()).currentElementName = (name!=null?name.getText():null);

             System.out.println("current Tag:"+(name!=null?name.getText():null)); 

            // AST REWRITE
            // elements: attribute
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 76:9: -> ^( ELEMENT[$name] ( attribute )* )
            {
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:76:12: ^( ELEMENT[$name] ( attribute )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(ELEMENT, name)
                , root_1);

                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:76:29: ( attribute )*
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
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:80:1: attribute : aname= GENERIC_ID ATTR_EQ ATTR_VALUE -> ^( ATTRIBUTE[$aname] ATTRIBUTENAME[$aname] ATTR_VALUE ) ;
    public final XMLParser.attribute_return attribute() throws RecognitionException {
        XMLParser.attribute_return retval = new XMLParser.attribute_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token aname=null;
        Token ATTR_EQ10=null;
        Token ATTR_VALUE11=null;

        CommonTree aname_tree=null;
        CommonTree ATTR_EQ10_tree=null;
        CommonTree ATTR_VALUE11_tree=null;
        RewriteRuleTokenStream stream_ATTR_EQ=new RewriteRuleTokenStream(adaptor,"token ATTR_EQ");
        RewriteRuleTokenStream stream_ATTR_VALUE=new RewriteRuleTokenStream(adaptor,"token ATTR_VALUE");
        RewriteRuleTokenStream stream_GENERIC_ID=new RewriteRuleTokenStream(adaptor,"token GENERIC_ID");

        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:80:11: (aname= GENERIC_ID ATTR_EQ ATTR_VALUE -> ^( ATTRIBUTE[$aname] ATTRIBUTENAME[$aname] ATTR_VALUE ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:80:13: aname= GENERIC_ID ATTR_EQ ATTR_VALUE
            {
            aname=(Token)match(input,GENERIC_ID,FOLLOW_GENERIC_ID_in_attribute309);  
            stream_GENERIC_ID.add(aname);


            ATTR_EQ10=(Token)match(input,ATTR_EQ,FOLLOW_ATTR_EQ_in_attribute311);  
            stream_ATTR_EQ.add(ATTR_EQ10);


            ATTR_VALUE11=(Token)match(input,ATTR_VALUE,FOLLOW_ATTR_VALUE_in_attribute313);  
            stream_ATTR_VALUE.add(ATTR_VALUE11);


            // AST REWRITE
            // elements: ATTR_VALUE
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 80:49: -> ^( ATTRIBUTE[$aname] ATTRIBUTENAME[$aname] ATTR_VALUE )
            {
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:80:52: ^( ATTRIBUTE[$aname] ATTRIBUTENAME[$aname] ATTR_VALUE )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(ATTRIBUTE, aname)
                , root_1);

                adaptor.addChild(root_1, 
                (CommonTree)adaptor.create(ATTRIBUTENAME, aname)
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
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:82:1: endTag :{...}? TAG_END_OPEN GENERIC_ID ;
    public final XMLParser.endTag_return endTag() throws RecognitionException {
        XMLParser.endTag_return retval = new XMLParser.endTag_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token TAG_END_OPEN12=null;
        Token GENERIC_ID13=null;

        CommonTree TAG_END_OPEN12_tree=null;
        CommonTree GENERIC_ID13_tree=null;

        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:84:5: ({...}? TAG_END_OPEN GENERIC_ID )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:84:7: {...}? TAG_END_OPEN GENERIC_ID
            {
            root_0 = (CommonTree)adaptor.nil();



                System.out.println(input.LT(1).getText() + input.LT(1).getType());
                System.out.println(input.LT(2).getText()+ input.LT(2).getType());
                System.out.println(input.LT(3).getText()+ input.LT(3).getType());

            if ( !(( ((ElementScope_scope)ElementScope_stack.peek()).currentElementName.equals(input.LT(2).getText()))) ) {
                throw new FailedPredicateException(input, "endTag", " $ElementScope::currentElementName.equals(input.LT(2).getText())");
            }

            TAG_END_OPEN12=(Token)match(input,TAG_END_OPEN,FOLLOW_TAG_END_OPEN_in_endTag363); 
            TAG_END_OPEN12_tree = 
            (CommonTree)adaptor.create(TAG_END_OPEN12)
            ;
            adaptor.addChild(root_0, TAG_END_OPEN12_tree);


            GENERIC_ID13=(Token)match(input,GENERIC_ID,FOLLOW_GENERIC_ID_in_endTag365); 
            GENERIC_ID13_tree = 
            (CommonTree)adaptor.create(GENERIC_ID13)
            ;
            adaptor.addChild(root_0, GENERIC_ID13_tree);


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
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:102:1: emptyElement : el= TAG_START_OPEN ( attribute )* TAG_EMPTY_CLOSE -> ^( ELEMENT[$el] ( attribute )* ) ;
    public final XMLParser.emptyElement_return emptyElement() throws RecognitionException {
        XMLParser.emptyElement_return retval = new XMLParser.emptyElement_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token el=null;
        Token TAG_EMPTY_CLOSE15=null;
        XMLParser.attribute_return attribute14 =null;


        CommonTree el_tree=null;
        CommonTree TAG_EMPTY_CLOSE15_tree=null;
        RewriteRuleTokenStream stream_TAG_EMPTY_CLOSE=new RewriteRuleTokenStream(adaptor,"token TAG_EMPTY_CLOSE");
        RewriteRuleTokenStream stream_TAG_START_OPEN=new RewriteRuleTokenStream(adaptor,"token TAG_START_OPEN");
        RewriteRuleSubtreeStream stream_attribute=new RewriteRuleSubtreeStream(adaptor,"rule attribute");
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:102:14: (el= TAG_START_OPEN ( attribute )* TAG_EMPTY_CLOSE -> ^( ELEMENT[$el] ( attribute )* ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:102:16: el= TAG_START_OPEN ( attribute )* TAG_EMPTY_CLOSE
            {
            el=(Token)match(input,TAG_START_OPEN,FOLLOW_TAG_START_OPEN_in_emptyElement386);  
            stream_TAG_START_OPEN.add(el);


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:102:34: ( attribute )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==GENERIC_ID) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:102:34: attribute
            	    {
            	    pushFollow(FOLLOW_attribute_in_emptyElement388);
            	    attribute14=attribute();

            	    state._fsp--;

            	    stream_attribute.add(attribute14.getTree());

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            TAG_EMPTY_CLOSE15=(Token)match(input,TAG_EMPTY_CLOSE,FOLLOW_TAG_EMPTY_CLOSE_in_emptyElement391);  
            stream_TAG_EMPTY_CLOSE.add(TAG_EMPTY_CLOSE15);


            // AST REWRITE
            // elements: attribute
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 103:9: -> ^( ELEMENT[$el] ( attribute )* )
            {
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:103:12: ^( ELEMENT[$el] ( attribute )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(ELEMENT, el)
                , root_1);

                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLParser.g:103:27: ( attribute )*
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


 

    public static final BitSet FOLLOW_tag_in_compilationUnit105 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_element_in_tag112 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_startTag_in_element133 = new BitSet(new long[]{0x0000000000184000L});
    public static final BitSet FOLLOW_element_in_element150 = new BitSet(new long[]{0x0000000000184000L});
    public static final BitSet FOLLOW_PCDATA_in_element166 = new BitSet(new long[]{0x0000000000184000L});
    public static final BitSet FOLLOW_endTag_in_element195 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_emptyElement_in_element208 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAG_START_OPEN_in_startTag237 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_GENERIC_ID_in_startTag241 = new BitSet(new long[]{0x0000000000020400L});
    public static final BitSet FOLLOW_attribute_in_startTag243 = new BitSet(new long[]{0x0000000000020400L});
    public static final BitSet FOLLOW_TAG_CLOSE_in_startTag246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GENERIC_ID_in_attribute309 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_ATTR_EQ_in_attribute311 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_ATTR_VALUE_in_attribute313 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAG_END_OPEN_in_endTag363 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_GENERIC_ID_in_endTag365 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAG_START_OPEN_in_emptyElement386 = new BitSet(new long[]{0x0000000000040400L});
    public static final BitSet FOLLOW_attribute_in_emptyElement388 = new BitSet(new long[]{0x0000000000040400L});
    public static final BitSet FOLLOW_TAG_EMPTY_CLOSE_in_emptyElement391 = new BitSet(new long[]{0x0000000000000002L});

}