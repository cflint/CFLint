// $ANTLR 3.4 /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g 2012-10-27 03:09:55

package com.parser.main.cfscript;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class JavadocParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "END", "ID", "WS", "'@author'"
    };

    public static final int EOF=-1;
    public static final int T__7=7;
    public static final int END=4;
    public static final int ID=5;
    public static final int WS=6;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public JavadocParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public JavadocParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return JavadocParser.tokenNames; }
    public String getGrammarFileName() { return "/Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g"; }



    // $ANTLR start "comment"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:11:1: comment : ( author )* ;
    public final void comment() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:11:9: ( ( author )* )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:11:11: ( author )*
            {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:11:11: ( author )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==7) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:11:13: author
            	    {
            	    pushFollow(FOLLOW_author_in_comment30);
            	    author();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


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
    // $ANTLR end "comment"



    // $ANTLR start "author"
    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:13:1: author : '@author' ID ;
    public final void author() throws RecognitionException {
        Token ID1=null;

        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:13:8: ( '@author' ID )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:13:10: '@author' ID
            {
            match(input,7,FOLLOW_7_in_author42); 

            ID1=(Token)match(input,ID,FOLLOW_ID_in_author44); 

            System.out.println("author "+(ID1!=null?ID1.getText():null));

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
    // $ANTLR end "author"

    // Delegated rules


 

    public static final BitSet FOLLOW_author_in_comment30 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_7_in_author42 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_ID_in_author44 = new BitSet(new long[]{0x0000000000000002L});

}