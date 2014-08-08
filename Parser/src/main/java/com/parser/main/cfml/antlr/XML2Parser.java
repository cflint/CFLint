// $ANTLR 3.4 /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g 2012-10-27 03:10:10

package com.parser.main.cfml.antlr;

import java.util.LinkedList;
import com.parser.main.treetool.TreeBuilder;
import javax.swing.tree.DefaultMutableTreeNode;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class XML2Parser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ATTRIBUTE", "CDATA", "COMMENT", "ELEMENT", "EMPTY_ELEMENT", "END_TAG", "GENERIC_ID", "LETTER", "PCDATA", "START_TAG", "VALUE", "WS"
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

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators

    protected static class ElementScope_scope {
        String currentElementName;
    }
    protected Stack ElementScope_stack = new Stack();



    public XML2Parser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public XML2Parser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return XML2Parser.tokenNames; }
    public String getGrammarFileName() { return "/Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g"; }


        private TreeBuilder T;
       // private DefaultMutableTreeNode oldNode;
        public void setTreeBuilder(TreeBuilder T){
            this.T = T;
        }


    public static class compilationUnit_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "compilationUnit"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:41:1: compilationUnit : tag ;
    public final XML2Parser.compilationUnit_return compilationUnit() throws RecognitionException {
        XML2Parser.compilationUnit_return retval = new XML2Parser.compilationUnit_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        XML2Parser.tag_return tag1 =null;



        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:41:17: ( tag )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:41:19: tag
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_tag_in_compilationUnit89);
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
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:43:1: tag : ELEMENT -> ( ^( ELEMENT ) )* ;
    public final XML2Parser.tag_return tag() throws RecognitionException {
        XML2Parser.tag_return retval = new XML2Parser.tag_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token ELEMENT2=null;

        CommonTree ELEMENT2_tree=null;
        RewriteRuleTokenStream stream_ELEMENT=new RewriteRuleTokenStream(adaptor,"token ELEMENT");

        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:43:4: ( ELEMENT -> ( ^( ELEMENT ) )* )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:43:6: ELEMENT
            {
            ELEMENT2=(Token)match(input,ELEMENT,FOLLOW_ELEMENT_in_tag96);  
            stream_ELEMENT.add(ELEMENT2);


            // AST REWRITE
            // elements: ELEMENT
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 43:14: -> ( ^( ELEMENT ) )*
            {
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:43:17: ( ^( ELEMENT ) )*
                while ( stream_ELEMENT.hasNext() ) {
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:43:17: ^( ELEMENT )
                    {
                    CommonTree root_1 = (CommonTree)adaptor.nil();
                    root_1 = (CommonTree)adaptor.becomeRoot(
                    stream_ELEMENT.nextNode()
                    , root_1);

                    adaptor.addChild(root_0, root_1);
                    }

                }
                stream_ELEMENT.reset();

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
    // $ANTLR end "tag"

    // Delegated rules


 

    public static final BitSet FOLLOW_tag_in_compilationUnit89 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ELEMENT_in_tag96 = new BitSet(new long[]{0x0000000000000002L});

}