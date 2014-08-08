// $ANTLR 3.4 /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g 2012-10-27 03:10:04

package com.parser.main.cfml.antlr;
import java.util.LinkedList;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class CFParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ASSIGN", "ATTRIBUTE", "ATTRIBUTENAME", "ATTR_EQ", "ATTR_VALUE", "CFELSETAG", "CFIFTAG", "CFMLTAG", "CFSETDATA", "CFSETTAG", "CFTAG_ID", "CLOSE_CHEVRON", "DIGIT", "DoubleStringCharacter", "ELEMENT", "GENERIC_ID", "ID", "LETTER", "NAMECHAR", "OPEN_CHEVRON", "OTHER", "PCDATA", "SINGLETAG", "STRING_LITERAL", "SingleStringCharacter", "TAG", "TAGNAME", "TAG_CLOSE", "TAG_EMPTY_CLOSE", "TAG_END_OPEN", "TAG_START_OPEN", "TRY", "WS"
    };

    public static final int EOF=-1;
    public static final int ASSIGN=4;
    public static final int ATTRIBUTE=5;
    public static final int ATTRIBUTENAME=6;
    public static final int ATTR_EQ=7;
    public static final int ATTR_VALUE=8;
    public static final int CFELSETAG=9;
    public static final int CFIFTAG=10;
    public static final int CFMLTAG=11;
    public static final int CFSETDATA=12;
    public static final int CFSETTAG=13;
    public static final int CFTAG_ID=14;
    public static final int CLOSE_CHEVRON=15;
    public static final int DIGIT=16;
    public static final int DoubleStringCharacter=17;
    public static final int ELEMENT=18;
    public static final int GENERIC_ID=19;
    public static final int ID=20;
    public static final int LETTER=21;
    public static final int NAMECHAR=22;
    public static final int OPEN_CHEVRON=23;
    public static final int OTHER=24;
    public static final int PCDATA=25;
    public static final int SINGLETAG=26;
    public static final int STRING_LITERAL=27;
    public static final int SingleStringCharacter=28;
    public static final int TAG=29;
    public static final int TAGNAME=30;
    public static final int TAG_CLOSE=31;
    public static final int TAG_EMPTY_CLOSE=32;
    public static final int TAG_END_OPEN=33;
    public static final int TAG_START_OPEN=34;
    public static final int TRY=35;
    public static final int WS=36;

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



    public CFParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public CFParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return CFParser.tokenNames; }
    public String getGrammarFileName() { return "/Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g"; }


    public static class compilationUnit_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "compilationUnit"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:185:1: compilationUnit : tag ;
    public final CFParser.compilationUnit_return compilationUnit() throws RecognitionException {
        CFParser.compilationUnit_return retval = new CFParser.compilationUnit_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        CFParser.tag_return tag1 =null;



        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:185:17: ( tag )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:185:19: tag
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_tag_in_compilationUnit745);
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
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:187:1: tag : ( ( element )* | singleTag ( singleTag | PCDATA | element )* );
    public final CFParser.tag_return tag() throws RecognitionException {
        CFParser.tag_return retval = new CFParser.tag_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token PCDATA5=null;
        CFParser.element_return element2 =null;

        CFParser.singleTag_return singleTag3 =null;

        CFParser.singleTag_return singleTag4 =null;

        CFParser.element_return element6 =null;


        CommonTree PCDATA5_tree=null;

        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:187:4: ( ( element )* | singleTag ( singleTag | PCDATA | element )* )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==EOF||LA3_0==TAG_START_OPEN) ) {
                alt3=1;
            }
            else if ( (LA3_0==SINGLETAG) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;

            }
            switch (alt3) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:187:6: ( element )*
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:187:6: ( element )*
                    loop1:
                    do {
                        int alt1=2;
                        int LA1_0 = input.LA(1);

                        if ( (LA1_0==TAG_START_OPEN) ) {
                            alt1=1;
                        }


                        switch (alt1) {
                    	case 1 :
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:187:6: element
                    	    {
                    	    pushFollow(FOLLOW_element_in_tag752);
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
                    break;
                case 2 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:187:17: singleTag ( singleTag | PCDATA | element )*
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_singleTag_in_tag757);
                    singleTag3=singleTag();

                    state._fsp--;

                    adaptor.addChild(root_0, singleTag3.getTree());

                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:187:27: ( singleTag | PCDATA | element )*
                    loop2:
                    do {
                        int alt2=4;
                        switch ( input.LA(1) ) {
                        case SINGLETAG:
                            {
                            alt2=1;
                            }
                            break;
                        case PCDATA:
                            {
                            alt2=2;
                            }
                            break;
                        case TAG_START_OPEN:
                            {
                            alt2=3;
                            }
                            break;

                        }

                        switch (alt2) {
                    	case 1 :
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:187:28: singleTag
                    	    {
                    	    pushFollow(FOLLOW_singleTag_in_tag760);
                    	    singleTag4=singleTag();

                    	    state._fsp--;

                    	    adaptor.addChild(root_0, singleTag4.getTree());

                    	    }
                    	    break;
                    	case 2 :
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:187:38: PCDATA
                    	    {
                    	    PCDATA5=(Token)match(input,PCDATA,FOLLOW_PCDATA_in_tag762); 
                    	    PCDATA5_tree = 
                    	    (CommonTree)adaptor.create(PCDATA5)
                    	    ;
                    	    adaptor.addChild(root_0, PCDATA5_tree);


                    	    }
                    	    break;
                    	case 3 :
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:187:45: element
                    	    {
                    	    pushFollow(FOLLOW_element_in_tag764);
                    	    element6=element();

                    	    state._fsp--;

                    	    adaptor.addChild(root_0, element6.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop2;
                        }
                    } while (true);


                    }
                    break;

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
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:189:1: element : ( startTag ^ ( element | PCDATA )* endTag !| emptyElement );
    public final CFParser.element_return element() throws RecognitionException {
        ElementScope_stack.push(new ElementScope_scope());

        CFParser.element_return retval = new CFParser.element_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token PCDATA9=null;
        CFParser.startTag_return startTag7 =null;

        CFParser.element_return element8 =null;

        CFParser.endTag_return endTag10 =null;

        CFParser.emptyElement_return emptyElement11 =null;


        CommonTree PCDATA9_tree=null;

        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:191:5: ( startTag ^ ( element | PCDATA )* endTag !| emptyElement )
            int alt5=2;
            alt5 = dfa5.predict(input);
            switch (alt5) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:191:9: startTag ^ ( element | PCDATA )* endTag !
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_startTag_in_element785);
                    startTag7=startTag();

                    state._fsp--;

                    root_0 = (CommonTree)adaptor.becomeRoot(startTag7.getTree(), root_0);

                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:192:13: ( element | PCDATA )*
                    loop4:
                    do {
                        int alt4=3;
                        int LA4_0 = input.LA(1);

                        if ( (LA4_0==TAG_START_OPEN) ) {
                            alt4=1;
                        }
                        else if ( (LA4_0==PCDATA) ) {
                            alt4=2;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:192:14: element
                    	    {
                    	    pushFollow(FOLLOW_element_in_element801);
                    	    element8=element();

                    	    state._fsp--;

                    	    adaptor.addChild(root_0, element8.getTree());

                    	    }
                    	    break;
                    	case 2 :
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:193:15: PCDATA
                    	    {
                    	    PCDATA9=(Token)match(input,PCDATA,FOLLOW_PCDATA_in_element817); 
                    	    PCDATA9_tree = 
                    	    (CommonTree)adaptor.create(PCDATA9)
                    	    ;
                    	    adaptor.addChild(root_0, PCDATA9_tree);


                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);


                    pushFollow(FOLLOW_endTag_in_element846);
                    endTag10=endTag();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:196:11: emptyElement
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_emptyElement_in_element859);
                    emptyElement11=emptyElement();

                    state._fsp--;

                    adaptor.addChild(root_0, emptyElement11.getTree());

                    }
                    break;

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
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:202:1: startTag : (name= TAG_START_OPEN ( attribute )* TAG_CLOSE -> ^( ELEMENT[$name] ( attribute )* ) ) ;
    public final CFParser.startTag_return startTag() throws RecognitionException {
        CFParser.startTag_return retval = new CFParser.startTag_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token name=null;
        Token TAG_CLOSE13=null;
        CFParser.attribute_return attribute12 =null;


        CommonTree name_tree=null;
        CommonTree TAG_CLOSE13_tree=null;
        RewriteRuleTokenStream stream_TAG_CLOSE=new RewriteRuleTokenStream(adaptor,"token TAG_CLOSE");
        RewriteRuleTokenStream stream_TAG_START_OPEN=new RewriteRuleTokenStream(adaptor,"token TAG_START_OPEN");
        RewriteRuleSubtreeStream stream_attribute=new RewriteRuleSubtreeStream(adaptor,"rule attribute");
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:203:5: ( (name= TAG_START_OPEN ( attribute )* TAG_CLOSE -> ^( ELEMENT[$name] ( attribute )* ) ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:204:5: (name= TAG_START_OPEN ( attribute )* TAG_CLOSE -> ^( ELEMENT[$name] ( attribute )* ) )
            {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:204:5: (name= TAG_START_OPEN ( attribute )* TAG_CLOSE -> ^( ELEMENT[$name] ( attribute )* ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:204:6: name= TAG_START_OPEN ( attribute )* TAG_CLOSE
            {
            name=(Token)match(input,TAG_START_OPEN,FOLLOW_TAG_START_OPEN_in_startTag895);  
            stream_TAG_START_OPEN.add(name);


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:204:26: ( attribute )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==GENERIC_ID) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:204:26: attribute
            	    {
            	    pushFollow(FOLLOW_attribute_in_startTag897);
            	    attribute12=attribute();

            	    state._fsp--;

            	    stream_attribute.add(attribute12.getTree());

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            TAG_CLOSE13=(Token)match(input,TAG_CLOSE,FOLLOW_TAG_CLOSE_in_startTag900);  
            stream_TAG_CLOSE.add(TAG_CLOSE13);



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
            // 209:9: -> ^( ELEMENT[$name] ( attribute )* )
            {
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:209:12: ^( ELEMENT[$name] ( attribute )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(ELEMENT, name)
                , root_1);

                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:209:29: ( attribute )*
                while ( stream_attribute.hasNext() ) {
                    adaptor.addChild(root_1, stream_attribute.nextTree());

                }
                stream_attribute.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

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
    // $ANTLR end "startTag"


    public static class singleTag_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "singleTag"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:213:1: singleTag : (name= SINGLETAG ( attribute )* ( TAG_CLOSE | TAG_EMPTY_CLOSE ) -> ^( SINGLETAG[$name] ( attribute )* ) ) ;
    public final CFParser.singleTag_return singleTag() throws RecognitionException {
        CFParser.singleTag_return retval = new CFParser.singleTag_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token name=null;
        Token TAG_CLOSE15=null;
        Token TAG_EMPTY_CLOSE16=null;
        CFParser.attribute_return attribute14 =null;


        CommonTree name_tree=null;
        CommonTree TAG_CLOSE15_tree=null;
        CommonTree TAG_EMPTY_CLOSE16_tree=null;
        RewriteRuleTokenStream stream_SINGLETAG=new RewriteRuleTokenStream(adaptor,"token SINGLETAG");
        RewriteRuleTokenStream stream_TAG_EMPTY_CLOSE=new RewriteRuleTokenStream(adaptor,"token TAG_EMPTY_CLOSE");
        RewriteRuleTokenStream stream_TAG_CLOSE=new RewriteRuleTokenStream(adaptor,"token TAG_CLOSE");
        RewriteRuleSubtreeStream stream_attribute=new RewriteRuleSubtreeStream(adaptor,"rule attribute");
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:214:5: ( (name= SINGLETAG ( attribute )* ( TAG_CLOSE | TAG_EMPTY_CLOSE ) -> ^( SINGLETAG[$name] ( attribute )* ) ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:215:5: (name= SINGLETAG ( attribute )* ( TAG_CLOSE | TAG_EMPTY_CLOSE ) -> ^( SINGLETAG[$name] ( attribute )* ) )
            {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:215:5: (name= SINGLETAG ( attribute )* ( TAG_CLOSE | TAG_EMPTY_CLOSE ) -> ^( SINGLETAG[$name] ( attribute )* ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:215:6: name= SINGLETAG ( attribute )* ( TAG_CLOSE | TAG_EMPTY_CLOSE )
            {
            name=(Token)match(input,SINGLETAG,FOLLOW_SINGLETAG_in_singleTag960);  
            stream_SINGLETAG.add(name);


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:215:21: ( attribute )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==GENERIC_ID) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:215:21: attribute
            	    {
            	    pushFollow(FOLLOW_attribute_in_singleTag962);
            	    attribute14=attribute();

            	    state._fsp--;

            	    stream_attribute.add(attribute14.getTree());

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:215:32: ( TAG_CLOSE | TAG_EMPTY_CLOSE )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==TAG_CLOSE) ) {
                alt8=1;
            }
            else if ( (LA8_0==TAG_EMPTY_CLOSE) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }
            switch (alt8) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:215:33: TAG_CLOSE
                    {
                    TAG_CLOSE15=(Token)match(input,TAG_CLOSE,FOLLOW_TAG_CLOSE_in_singleTag966);  
                    stream_TAG_CLOSE.add(TAG_CLOSE15);


                    }
                    break;
                case 2 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:215:44: TAG_EMPTY_CLOSE
                    {
                    TAG_EMPTY_CLOSE16=(Token)match(input,TAG_EMPTY_CLOSE,FOLLOW_TAG_EMPTY_CLOSE_in_singleTag969);  
                    stream_TAG_EMPTY_CLOSE.add(TAG_EMPTY_CLOSE16);


                    }
                    break;

            }



                        System.out.println("current single Tag:"+(name!=null?name.getText():null));
                        

            // AST REWRITE
            // elements: attribute, SINGLETAG
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 219:9: -> ^( SINGLETAG[$name] ( attribute )* )
            {
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:219:12: ^( SINGLETAG[$name] ( attribute )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(SINGLETAG, name)
                , root_1);

                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:219:31: ( attribute )*
                while ( stream_attribute.hasNext() ) {
                    adaptor.addChild(root_1, stream_attribute.nextTree());

                }
                stream_attribute.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

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
    // $ANTLR end "singleTag"


    public static class cfifTag_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "cfifTag"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:222:1: cfifTag : (name= CFIFTAG ( ID )* TAG_CLOSE ( CFELSETAG )? endTag -> ^( CFIFTAG[$name] ( CFELSETAG )? ) ) ;
    public final CFParser.cfifTag_return cfifTag() throws RecognitionException {
        CFParser.cfifTag_return retval = new CFParser.cfifTag_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token name=null;
        Token ID17=null;
        Token TAG_CLOSE18=null;
        Token CFELSETAG19=null;
        CFParser.endTag_return endTag20 =null;


        CommonTree name_tree=null;
        CommonTree ID17_tree=null;
        CommonTree TAG_CLOSE18_tree=null;
        CommonTree CFELSETAG19_tree=null;
        RewriteRuleTokenStream stream_CFIFTAG=new RewriteRuleTokenStream(adaptor,"token CFIFTAG");
        RewriteRuleTokenStream stream_TAG_CLOSE=new RewriteRuleTokenStream(adaptor,"token TAG_CLOSE");
        RewriteRuleTokenStream stream_CFELSETAG=new RewriteRuleTokenStream(adaptor,"token CFELSETAG");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_endTag=new RewriteRuleSubtreeStream(adaptor,"rule endTag");
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:223:5: ( (name= CFIFTAG ( ID )* TAG_CLOSE ( CFELSETAG )? endTag -> ^( CFIFTAG[$name] ( CFELSETAG )? ) ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:224:5: (name= CFIFTAG ( ID )* TAG_CLOSE ( CFELSETAG )? endTag -> ^( CFIFTAG[$name] ( CFELSETAG )? ) )
            {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:224:5: (name= CFIFTAG ( ID )* TAG_CLOSE ( CFELSETAG )? endTag -> ^( CFIFTAG[$name] ( CFELSETAG )? ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:224:6: name= CFIFTAG ( ID )* TAG_CLOSE ( CFELSETAG )? endTag
            {
            name=(Token)match(input,CFIFTAG,FOLLOW_CFIFTAG_in_cfifTag1024);  
            stream_CFIFTAG.add(name);


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:224:19: ( ID )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==ID) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:224:19: ID
            	    {
            	    ID17=(Token)match(input,ID,FOLLOW_ID_in_cfifTag1026);  
            	    stream_ID.add(ID17);


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            TAG_CLOSE18=(Token)match(input,TAG_CLOSE,FOLLOW_TAG_CLOSE_in_cfifTag1029);  
            stream_TAG_CLOSE.add(TAG_CLOSE18);


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:224:33: ( CFELSETAG )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==CFELSETAG) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:224:33: CFELSETAG
                    {
                    CFELSETAG19=(Token)match(input,CFELSETAG,FOLLOW_CFELSETAG_in_cfifTag1031);  
                    stream_CFELSETAG.add(CFELSETAG19);


                    }
                    break;

            }


            pushFollow(FOLLOW_endTag_in_cfifTag1034);
            endTag20=endTag();

            state._fsp--;

            stream_endTag.add(endTag20.getTree());


                        System.out.println("current single Tag:"+(name!=null?name.getText():null));
                        

            // AST REWRITE
            // elements: CFELSETAG, CFIFTAG
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 228:9: -> ^( CFIFTAG[$name] ( CFELSETAG )? )
            {
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:228:12: ^( CFIFTAG[$name] ( CFELSETAG )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(CFIFTAG, name)
                , root_1);

                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:228:29: ( CFELSETAG )?
                if ( stream_CFELSETAG.hasNext() ) {
                    adaptor.addChild(root_1, 
                    stream_CFELSETAG.nextNode()
                    );

                }
                stream_CFELSETAG.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

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
    // $ANTLR end "cfifTag"


    public static class stringLiteral_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "stringLiteral"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:231:1: stringLiteral : STRING_LITERAL ;
    public final CFParser.stringLiteral_return stringLiteral() throws RecognitionException {
        CFParser.stringLiteral_return retval = new CFParser.stringLiteral_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token STRING_LITERAL21=null;

        CommonTree STRING_LITERAL21_tree=null;

        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:232:3: ( STRING_LITERAL )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:232:6: STRING_LITERAL
            {
            root_0 = (CommonTree)adaptor.nil();


            STRING_LITERAL21=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_stringLiteral1080); 
            STRING_LITERAL21_tree = 
            (CommonTree)adaptor.create(STRING_LITERAL21)
            ;
            adaptor.addChild(root_0, STRING_LITERAL21_tree);


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
    // $ANTLR end "stringLiteral"


    public static class attribute_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "attribute"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:234:1: attribute : aname= GENERIC_ID ATTR_EQ stringLiteral -> ^( ATTRIBUTE[$aname] ATTRIBUTENAME[$aname] stringLiteral ) ;
    public final CFParser.attribute_return attribute() throws RecognitionException {
        CFParser.attribute_return retval = new CFParser.attribute_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token aname=null;
        Token ATTR_EQ22=null;
        CFParser.stringLiteral_return stringLiteral23 =null;


        CommonTree aname_tree=null;
        CommonTree ATTR_EQ22_tree=null;
        RewriteRuleTokenStream stream_ATTR_EQ=new RewriteRuleTokenStream(adaptor,"token ATTR_EQ");
        RewriteRuleTokenStream stream_GENERIC_ID=new RewriteRuleTokenStream(adaptor,"token GENERIC_ID");
        RewriteRuleSubtreeStream stream_stringLiteral=new RewriteRuleSubtreeStream(adaptor,"rule stringLiteral");
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:234:11: (aname= GENERIC_ID ATTR_EQ stringLiteral -> ^( ATTRIBUTE[$aname] ATTRIBUTENAME[$aname] stringLiteral ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:234:13: aname= GENERIC_ID ATTR_EQ stringLiteral
            {
            aname=(Token)match(input,GENERIC_ID,FOLLOW_GENERIC_ID_in_attribute1090);  
            stream_GENERIC_ID.add(aname);


            ATTR_EQ22=(Token)match(input,ATTR_EQ,FOLLOW_ATTR_EQ_in_attribute1092);  
            stream_ATTR_EQ.add(ATTR_EQ22);


            pushFollow(FOLLOW_stringLiteral_in_attribute1094);
            stringLiteral23=stringLiteral();

            state._fsp--;

            stream_stringLiteral.add(stringLiteral23.getTree());

            // AST REWRITE
            // elements: stringLiteral
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 234:52: -> ^( ATTRIBUTE[$aname] ATTRIBUTENAME[$aname] stringLiteral )
            {
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:234:55: ^( ATTRIBUTE[$aname] ATTRIBUTENAME[$aname] stringLiteral )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(ATTRIBUTE, aname)
                , root_1);

                adaptor.addChild(root_1, 
                (CommonTree)adaptor.create(ATTRIBUTENAME, aname)
                );

                adaptor.addChild(root_1, stream_stringLiteral.nextTree());

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
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:236:1: endTag :{...}? ( TAG_END_OPEN TAG_CLOSE ) ;
    public final CFParser.endTag_return endTag() throws RecognitionException {
        CFParser.endTag_return retval = new CFParser.endTag_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token TAG_END_OPEN24=null;
        Token TAG_CLOSE25=null;

        CommonTree TAG_END_OPEN24_tree=null;
        CommonTree TAG_CLOSE25_tree=null;

        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:238:5: ({...}? ( TAG_END_OPEN TAG_CLOSE ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:238:7: {...}? ( TAG_END_OPEN TAG_CLOSE )
            {
            root_0 = (CommonTree)adaptor.nil();


            if ( !(( ((ElementScope_scope)ElementScope_stack.peek()).currentElementName.equals(input.LT(2).getText()))) ) {
                throw new FailedPredicateException(input, "endTag", " $ElementScope::currentElementName.equals(input.LT(2).getText())");
            }

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:239:7: ( TAG_END_OPEN TAG_CLOSE )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:239:8: TAG_END_OPEN TAG_CLOSE
            {
            TAG_END_OPEN24=(Token)match(input,TAG_END_OPEN,FOLLOW_TAG_END_OPEN_in_endTag1136); 
            TAG_END_OPEN24_tree = 
            (CommonTree)adaptor.create(TAG_END_OPEN24)
            ;
            adaptor.addChild(root_0, TAG_END_OPEN24_tree);


            TAG_CLOSE25=(Token)match(input,TAG_CLOSE,FOLLOW_TAG_CLOSE_in_endTag1138); 
            TAG_CLOSE25_tree = 
            (CommonTree)adaptor.create(TAG_CLOSE25)
            ;
            adaptor.addChild(root_0, TAG_CLOSE25_tree);


            }


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
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:252:1: emptyElement : el= TAG_START_OPEN ( attribute )* TAG_EMPTY_CLOSE -> ^( ELEMENT[$el] ( attribute )* ) ;
    public final CFParser.emptyElement_return emptyElement() throws RecognitionException {
        CFParser.emptyElement_return retval = new CFParser.emptyElement_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token el=null;
        Token TAG_EMPTY_CLOSE27=null;
        CFParser.attribute_return attribute26 =null;


        CommonTree el_tree=null;
        CommonTree TAG_EMPTY_CLOSE27_tree=null;
        RewriteRuleTokenStream stream_TAG_EMPTY_CLOSE=new RewriteRuleTokenStream(adaptor,"token TAG_EMPTY_CLOSE");
        RewriteRuleTokenStream stream_TAG_START_OPEN=new RewriteRuleTokenStream(adaptor,"token TAG_START_OPEN");
        RewriteRuleSubtreeStream stream_attribute=new RewriteRuleSubtreeStream(adaptor,"rule attribute");
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:252:14: (el= TAG_START_OPEN ( attribute )* TAG_EMPTY_CLOSE -> ^( ELEMENT[$el] ( attribute )* ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:252:16: el= TAG_START_OPEN ( attribute )* TAG_EMPTY_CLOSE
            {
            el=(Token)match(input,TAG_START_OPEN,FOLLOW_TAG_START_OPEN_in_emptyElement1160);  
            stream_TAG_START_OPEN.add(el);


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:252:34: ( attribute )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==GENERIC_ID) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:252:34: attribute
            	    {
            	    pushFollow(FOLLOW_attribute_in_emptyElement1162);
            	    attribute26=attribute();

            	    state._fsp--;

            	    stream_attribute.add(attribute26.getTree());

            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);


            TAG_EMPTY_CLOSE27=(Token)match(input,TAG_EMPTY_CLOSE,FOLLOW_TAG_EMPTY_CLOSE_in_emptyElement1165);  
            stream_TAG_EMPTY_CLOSE.add(TAG_EMPTY_CLOSE27);


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
            // 253:9: -> ^( ELEMENT[$el] ( attribute )* )
            {
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:253:12: ^( ELEMENT[$el] ( attribute )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(ELEMENT, el)
                , root_1);

                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:253:27: ( attribute )*
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


    protected DFA5 dfa5 = new DFA5(this);
    static final String DFA5_eotS =
        "\7\uffff";
    static final String DFA5_eofS =
        "\7\uffff";
    static final String DFA5_minS =
        "\1\42\1\23\1\7\2\uffff\1\33\1\23";
    static final String DFA5_maxS =
        "\1\42\1\40\1\7\2\uffff\1\33\1\40";
    static final String DFA5_acceptS =
        "\3\uffff\1\1\1\2\2\uffff";
    static final String DFA5_specialS =
        "\7\uffff}>";
    static final String[] DFA5_transitionS = {
            "\1\1",
            "\1\2\13\uffff\1\3\1\4",
            "\1\5",
            "",
            "",
            "\1\6",
            "\1\2\13\uffff\1\3\1\4"
    };

    static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
    static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
    static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
    static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
    static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
    static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
    static final short[][] DFA5_transition;

    static {
        int numStates = DFA5_transitionS.length;
        DFA5_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
        }
    }

    class DFA5 extends DFA {

        public DFA5(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 5;
            this.eot = DFA5_eot;
            this.eof = DFA5_eof;
            this.min = DFA5_min;
            this.max = DFA5_max;
            this.accept = DFA5_accept;
            this.special = DFA5_special;
            this.transition = DFA5_transition;
        }
        public String getDescription() {
            return "189:1: element : ( startTag ^ ( element | PCDATA )* endTag !| emptyElement );";
        }
    }
 

    public static final BitSet FOLLOW_tag_in_compilationUnit745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_element_in_tag752 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_singleTag_in_tag757 = new BitSet(new long[]{0x0000000406000002L});
    public static final BitSet FOLLOW_singleTag_in_tag760 = new BitSet(new long[]{0x0000000406000002L});
    public static final BitSet FOLLOW_PCDATA_in_tag762 = new BitSet(new long[]{0x0000000406000002L});
    public static final BitSet FOLLOW_element_in_tag764 = new BitSet(new long[]{0x0000000406000002L});
    public static final BitSet FOLLOW_startTag_in_element785 = new BitSet(new long[]{0x0000000602000000L});
    public static final BitSet FOLLOW_element_in_element801 = new BitSet(new long[]{0x0000000602000000L});
    public static final BitSet FOLLOW_PCDATA_in_element817 = new BitSet(new long[]{0x0000000602000000L});
    public static final BitSet FOLLOW_endTag_in_element846 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_emptyElement_in_element859 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAG_START_OPEN_in_startTag895 = new BitSet(new long[]{0x0000000080080000L});
    public static final BitSet FOLLOW_attribute_in_startTag897 = new BitSet(new long[]{0x0000000080080000L});
    public static final BitSet FOLLOW_TAG_CLOSE_in_startTag900 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SINGLETAG_in_singleTag960 = new BitSet(new long[]{0x0000000180080000L});
    public static final BitSet FOLLOW_attribute_in_singleTag962 = new BitSet(new long[]{0x0000000180080000L});
    public static final BitSet FOLLOW_TAG_CLOSE_in_singleTag966 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAG_EMPTY_CLOSE_in_singleTag969 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CFIFTAG_in_cfifTag1024 = new BitSet(new long[]{0x0000000080100000L});
    public static final BitSet FOLLOW_ID_in_cfifTag1026 = new BitSet(new long[]{0x0000000080100000L});
    public static final BitSet FOLLOW_TAG_CLOSE_in_cfifTag1029 = new BitSet(new long[]{0x0000000200000200L});
    public static final BitSet FOLLOW_CFELSETAG_in_cfifTag1031 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_endTag_in_cfifTag1034 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_stringLiteral1080 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GENERIC_ID_in_attribute1090 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ATTR_EQ_in_attribute1092 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_stringLiteral_in_attribute1094 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAG_END_OPEN_in_endTag1136 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_TAG_CLOSE_in_endTag1138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAG_START_OPEN_in_emptyElement1160 = new BitSet(new long[]{0x0000000100080000L});
    public static final BitSet FOLLOW_attribute_in_emptyElement1162 = new BitSet(new long[]{0x0000000100080000L});
    public static final BitSet FOLLOW_TAG_EMPTY_CLOSE_in_emptyElement1165 = new BitSet(new long[]{0x0000000000000002L});

}