// $ANTLR 3.4 /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g 2012-10-27 03:10:13

package com.parser.main.cfml.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class XMLLexer extends Lexer {
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

    boolean tagMode = false;
    boolean internalTagMode = false;
      private static int NONE_MODE = 0;
      private static int ENDTAG_MODE = 1;
      private static int STARTTAG_MODE = 2;
      private static int DOUBLE_QUOTE_STRING_MODE = 3;
      private static int SINGLE_QUOTE_STRING_MODE = 4;
      private static int HASH_CFML_MODE = 5;

      private int mode;
      
      private int getMode()
      {
        return mode;
      }
      
      private void setMode(int mode)
      {
        this.mode = mode;
      }


    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public XMLLexer() {} 
    public XMLLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public XMLLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "/Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g"; }

    public Token nextToken() {
        while (true) {
            if ( input.LA(1)==CharStream.EOF ) {
                Token eof = new CommonToken((CharStream)input,Token.EOF,
                                            Token.DEFAULT_CHANNEL,
                                            input.index(),input.index());
                eof.setLine(getLine());
                eof.setCharPositionInLine(getCharPositionInLine());
                return eof;
            }
            state.token = null;
    	state.channel = Token.DEFAULT_CHANNEL;
            state.tokenStartCharIndex = input.index();
            state.tokenStartCharPositionInLine = input.getCharPositionInLine();
            state.tokenStartLine = input.getLine();
    	state.text = null;
            try {
                int m = input.mark();
                state.backtracking=1; 
                state.failed=false;
                mTokens();
                state.backtracking=0;
                if ( state.failed ) {
                    input.rewind(m);
                    input.consume(); 
                }
                else {
                    emit();
                    return state.token;
                }
            }
            catch (RecognitionException re) {
                // shouldn't happen in backtracking mode, but...
                reportError(re);
                recover(re);
            }
        }
    }

    public void memoize(IntStream input,
    		int ruleIndex,
    		int ruleStartIndex)
    {
    if ( state.backtracking>1 ) super.memoize(input, ruleIndex, ruleStartIndex);
    }

    public boolean alreadyParsedRule(IntStream input, int ruleIndex) {
    if ( state.backtracking>1 ) return super.alreadyParsedRule(input, ruleIndex);
    return false;
    }
    // $ANTLR start "TAG_START_OPEN"
    public final void mTAG_START_OPEN() throws RecognitionException {
        try {
            int _type = TAG_START_OPEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken name=null;

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:36:16: ( ( '<' name= CFTAG_ID ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:36:18: ( '<' name= CFTAG_ID )
            {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:36:18: ( '<' name= CFTAG_ID )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:36:19: '<' name= CFTAG_ID
            {
            match('<'); if (state.failed) return ;

            int nameStart50 = getCharIndex();
            int nameStartLine50 = getLine();
            int nameStartCharPos50 = getCharPositionInLine();
            mCFTAG_ID(); if (state.failed) return ;
            name = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, nameStart50, getCharIndex()-1);
            name.setLine(nameStartLine50);
            name.setCharPositionInLine(nameStartCharPos50);


            if ( state.backtracking==1 ) { tagMode = true; }

            }


            if ( state.backtracking==1 ) { System.out.println("PCDATA:"+(name!=null?name.getText():null)+ " type:"+(name!=null?name.getType():0)); }

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TAG_START_OPEN"

    // $ANTLR start "ATTR"
    public final void mATTR() throws RecognitionException {
        try {
            CommonToken at=null;
            CommonToken av=null;

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:38:15: ( (at= GENERIC_ID ATTR_EQ av= ATTR_VALUE ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:38:17: (at= GENERIC_ID ATTR_EQ av= ATTR_VALUE )
            {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:38:17: (at= GENERIC_ID ATTR_EQ av= ATTR_VALUE )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:38:18: at= GENERIC_ID ATTR_EQ av= ATTR_VALUE
            {
            int atStart68 = getCharIndex();
            int atStartLine68 = getLine();
            int atStartCharPos68 = getCharPositionInLine();
            mGENERIC_ID(); if (state.failed) return ;
            at = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, atStart68, getCharIndex()-1);
            at.setLine(atStartLine68);
            at.setCharPositionInLine(atStartCharPos68);


            mATTR_EQ(); if (state.failed) return ;


            int avStart74 = getCharIndex();
            int avStartLine74 = getLine();
            int avStartCharPos74 = getCharPositionInLine();
            mATTR_VALUE(); if (state.failed) return ;
            av = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, avStart74, getCharIndex()-1);
            av.setLine(avStartLine74);
            av.setCharPositionInLine(avStartCharPos74);


            }


            if ( state.backtracking==1 ) { System.out.println("PCDATA:"+(at!=null?at.getText():null)+" "+(av!=null?av.getText():null)+" type:" + (av!=null?av.getType():0)); }

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ATTR"

    // $ANTLR start "ATTR_EQ"
    public final void mATTR_EQ() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:40:18: ({...}? => '=' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:40:20: {...}? => '='
            {
            if ( !(( tagMode )) ) {
                if (state.backtracking>0) {state.failed=true; return ;}
                throw new FailedPredicateException(input, "ATTR_EQ", " tagMode ");
            }

            match('='); if (state.failed) return ;

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ATTR_EQ"

    // $ANTLR start "ATTR_VALUE"
    public final void mATTR_VALUE() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:41:21: ({...}? => STRING_LITERAL )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:41:23: {...}? => STRING_LITERAL
            {
            if ( !(( tagMode )) ) {
                if (state.backtracking>0) {state.failed=true; return ;}
                throw new FailedPredicateException(input, "ATTR_VALUE", " tagMode ");
            }

            mSTRING_LITERAL(); if (state.failed) return ;


            if ( state.backtracking==1 ) { System.out.println("PCDATA: string"); }

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ATTR_VALUE"

    // $ANTLR start "TAG_EMPTY_CLOSE"
    public final void mTAG_EMPTY_CLOSE() throws RecognitionException {
        try {
            int _type = TAG_EMPTY_CLOSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:46:17: ({...}? => '/>' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:46:19: {...}? => '/>'
            {
            if ( !(( tagMode )) ) {
                if (state.backtracking>0) {state.failed=true; return ;}
                throw new FailedPredicateException(input, "TAG_EMPTY_CLOSE", " tagMode ");
            }

            match("/>"); if (state.failed) return ;



            if ( state.backtracking==1 ) { tagMode = false; }

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TAG_EMPTY_CLOSE"

    // $ANTLR start "TAG_CLOSE"
    public final void mTAG_CLOSE() throws RecognitionException {
        try {
            int _type = TAG_CLOSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:47:11: ({...}? => '>' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:47:13: {...}? => '>'
            {
            if ( !(( tagMode )) ) {
                if (state.backtracking>0) {state.failed=true; return ;}
                throw new FailedPredicateException(input, "TAG_CLOSE", " tagMode ");
            }

            match('>'); if (state.failed) return ;

            if ( state.backtracking==1 ) { tagMode = false; }

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TAG_CLOSE"

    // $ANTLR start "TAG_END_OPEN"
    public final void mTAG_END_OPEN() throws RecognitionException {
        try {
            int _type = TAG_END_OPEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:48:14: ( ( '</' CFTAG_ID ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:48:16: ( '</' CFTAG_ID )
            {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:48:16: ( '</' CFTAG_ID )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:48:17: '</' CFTAG_ID
            {
            match("</"); if (state.failed) return ;



            mCFTAG_ID(); if (state.failed) return ;


            if ( state.backtracking==1 ) { tagMode = true; }

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TAG_END_OPEN"

    // $ANTLR start "GENERIC_ID"
    public final void mGENERIC_ID() throws RecognitionException {
        try {
            int _type = GENERIC_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:51:5: ({...}? => ( LETTER | '_' | ':' ) ( NAMECHAR )* )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:51:7: {...}? => ( LETTER | '_' | ':' ) ( NAMECHAR )*
            {
            if ( !(( tagMode )) ) {
                if (state.backtracking>0) {state.failed=true; return ;}
                throw new FailedPredicateException(input, "GENERIC_ID", " tagMode ");
            }

            if ( input.LA(1)==':'||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:52:29: ( NAMECHAR )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= '-' && LA1_0 <= '.')||(LA1_0 >= '0' && LA1_0 <= ':')||(LA1_0 >= 'A' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:
            	    {
            	    if ( (input.LA(1) >= '-' && input.LA(1) <= '.')||(input.LA(1) >= '0' && input.LA(1) <= ':')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	        state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "GENERIC_ID"

    // $ANTLR start "STRING_LITERAL"
    public final void mSTRING_LITERAL() throws RecognitionException {
        try {
            int _type = STRING_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:56:3: ( '\"' ( DoubleStringCharacter )* '\"' | '\\'' ( SingleStringCharacter )* '\\'' )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='\"') ) {
                alt4=1;
            }
            else if ( (LA4_0=='\'') ) {
                alt4=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }
            switch (alt4) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:56:5: '\"' ( DoubleStringCharacter )* '\"'
                    {
                    match('\"'); if (state.failed) return ;

                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:56:9: ( DoubleStringCharacter )*
                    loop2:
                    do {
                        int alt2=2;
                        int LA2_0 = input.LA(1);

                        if ( (LA2_0=='\"') ) {
                            int LA2_1 = input.LA(2);

                            if ( (LA2_1=='\"') ) {
                                alt2=1;
                            }


                        }
                        else if ( ((LA2_0 >= '\u0000' && LA2_0 <= '!')||(LA2_0 >= '#' && LA2_0 <= '\uFFFF')) ) {
                            alt2=1;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:56:9: DoubleStringCharacter
                    	    {
                    	    mDoubleStringCharacter(); if (state.failed) return ;


                    	    }
                    	    break;

                    	default :
                    	    break loop2;
                        }
                    } while (true);


                    match('\"'); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:57:5: '\\'' ( SingleStringCharacter )* '\\''
                    {
                    match('\''); if (state.failed) return ;

                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:57:10: ( SingleStringCharacter )*
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( (LA3_0=='\'') ) {
                            int LA3_1 = input.LA(2);

                            if ( (LA3_1=='\'') ) {
                                alt3=1;
                            }


                        }
                        else if ( ((LA3_0 >= '\u0000' && LA3_0 <= '&')||(LA3_0 >= '(' && LA3_0 <= '\uFFFF')) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:57:10: SingleStringCharacter
                    	    {
                    	    mSingleStringCharacter(); if (state.failed) return ;


                    	    }
                    	    break;

                    	default :
                    	    break loop3;
                        }
                    } while (true);


                    match('\''); if (state.failed) return ;

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STRING_LITERAL"

    // $ANTLR start "DoubleStringCharacter"
    public final void mDoubleStringCharacter() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:61:3: (~ ( '\"' ) | '\"\"' )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( ((LA5_0 >= '\u0000' && LA5_0 <= '!')||(LA5_0 >= '#' && LA5_0 <= '\uFFFF')) ) {
                alt5=1;
            }
            else if ( (LA5_0=='\"') ) {
                alt5=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:61:5: ~ ( '\"' )
                    {
                    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '\uFFFF') ) {
                        input.consume();
                        state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:62:5: '\"\"'
                    {
                    match("\"\""); if (state.failed) return ;



                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DoubleStringCharacter"

    // $ANTLR start "SingleStringCharacter"
    public final void mSingleStringCharacter() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:66:3: (~ ( '\\'' ) | '\\'\\'' )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( ((LA6_0 >= '\u0000' && LA6_0 <= '&')||(LA6_0 >= '(' && LA6_0 <= '\uFFFF')) ) {
                alt6=1;
            }
            else if ( (LA6_0=='\'') ) {
                alt6=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }
            switch (alt6) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:66:5: ~ ( '\\'' )
                    {
                    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '\uFFFF') ) {
                        input.consume();
                        state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:67:5: '\\'\\''
                    {
                    match("''"); if (state.failed) return ;



                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SingleStringCharacter"

    // $ANTLR start "PCDATA"
    public final void mPCDATA() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:71:17: ({...}? => (~ '<' )+ )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:71:19: {...}? => (~ '<' )+
            {
            if ( !(( !tagMode )) ) {
                if (state.backtracking>0) {state.failed=true; return ;}
                throw new FailedPredicateException(input, "PCDATA", " !tagMode ");
            }

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:71:35: (~ '<' )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0 >= '\u0000' && LA7_0 <= ';')||(LA7_0 >= '=' && LA7_0 <= '\uFFFF')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= ';')||(input.LA(1) >= '=' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	        state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
            	    if (state.backtracking>0) {state.failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PCDATA"

    // $ANTLR start "CFTAG_ID"
    public final void mCFTAG_ID() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:74:5: ( ( 'c' | 'C' ) ( 'f' | 'F' ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:75:7: ( 'c' | 'C' ) ( 'f' | 'F' )
            {
            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CFTAG_ID"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:79:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:79:9: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:79:33: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0 >= '0' && LA8_0 <= '9')||(LA8_0 >= 'A' && LA8_0 <= 'Z')||LA8_0=='_'||(LA8_0 >= 'a' && LA8_0 <= 'z')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	        state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "TRY"
    public final void mTRY() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:82:5: ( 'try' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:82:8: 'try'
            {
            match("try"); if (state.failed) return ;



            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TRY"

    // $ANTLR start "NAMECHAR"
    public final void mNAMECHAR() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:87:5: ( LETTER | DIGIT | '.' | '-' | '_' | ':' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:
            {
            if ( (input.LA(1) >= '-' && input.LA(1) <= '.')||(input.LA(1) >= '0' && input.LA(1) <= ':')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NAMECHAR"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:91:5: ( '0' .. '9' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:95:5: ( ( 'a' .. 'z' | 'A' .. 'Z' ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LETTER"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:98:5: ({...}? => ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:98:8: {...}? => ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )
            {
            if ( !(( tagMode )) ) {
                if (state.backtracking>0) {state.failed=true; return ;}
                throw new FailedPredicateException(input, "WS", " tagMode ");
            }

            if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
                input.consume();
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( state.backtracking==1 ) {_channel=99;}

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:39: ( TAG_START_OPEN | TAG_EMPTY_CLOSE | TAG_CLOSE | TAG_END_OPEN | GENERIC_ID | STRING_LITERAL | WS )
        int alt9=7;
        int LA9_0 = input.LA(1);

        if ( (LA9_0=='<') ) {
            int LA9_1 = input.LA(2);

            if ( (synpred1_XMLLexer()) ) {
                alt9=1;
            }
            else if ( (synpred4_XMLLexer()) ) {
                alt9=4;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 1, input);

                throw nvae;

            }
        }
        else if ( (LA9_0=='/') && (( tagMode ))) {
            alt9=2;
        }
        else if ( (LA9_0=='>') && (( tagMode ))) {
            alt9=3;
        }
        else if ( (LA9_0==':'||(LA9_0 >= 'A' && LA9_0 <= 'Z')||LA9_0=='_'||(LA9_0 >= 'a' && LA9_0 <= 'z')) && (( tagMode ))) {
            alt9=5;
        }
        else if ( (LA9_0=='\"'||LA9_0=='\'') ) {
            alt9=6;
        }
        else if ( ((LA9_0 >= '\t' && LA9_0 <= '\n')||(LA9_0 >= '\f' && LA9_0 <= '\r')||LA9_0==' ') && (( tagMode ))) {
            alt9=7;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 9, 0, input);

            throw nvae;

        }
        switch (alt9) {
            case 1 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:41: TAG_START_OPEN
                {
                mTAG_START_OPEN(); if (state.failed) return ;


                }
                break;
            case 2 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:56: TAG_EMPTY_CLOSE
                {
                mTAG_EMPTY_CLOSE(); if (state.failed) return ;


                }
                break;
            case 3 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:72: TAG_CLOSE
                {
                mTAG_CLOSE(); if (state.failed) return ;


                }
                break;
            case 4 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:82: TAG_END_OPEN
                {
                mTAG_END_OPEN(); if (state.failed) return ;


                }
                break;
            case 5 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:95: GENERIC_ID
                {
                mGENERIC_ID(); if (state.failed) return ;


                }
                break;
            case 6 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:106: STRING_LITERAL
                {
                mSTRING_LITERAL(); if (state.failed) return ;


                }
                break;
            case 7 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:121: WS
                {
                mWS(); if (state.failed) return ;


                }
                break;

        }

    }

    // $ANTLR start synpred1_XMLLexer
    public final void synpred1_XMLLexer_fragment() throws RecognitionException {
        // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:41: ( TAG_START_OPEN )
        // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:41: TAG_START_OPEN
        {
        mTAG_START_OPEN(); if (state.failed) return ;


        }

    }
    // $ANTLR end synpred1_XMLLexer

    // $ANTLR start synpred2_XMLLexer
    public final void synpred2_XMLLexer_fragment() throws RecognitionException {
        // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:56: ( TAG_EMPTY_CLOSE )
        // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:56: TAG_EMPTY_CLOSE
        {
        mTAG_EMPTY_CLOSE(); if (state.failed) return ;


        }

    }
    // $ANTLR end synpred2_XMLLexer

    // $ANTLR start synpred3_XMLLexer
    public final void synpred3_XMLLexer_fragment() throws RecognitionException {
        // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:72: ( TAG_CLOSE )
        // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:72: TAG_CLOSE
        {
        mTAG_CLOSE(); if (state.failed) return ;


        }

    }
    // $ANTLR end synpred3_XMLLexer

    // $ANTLR start synpred4_XMLLexer
    public final void synpred4_XMLLexer_fragment() throws RecognitionException {
        // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:82: ( TAG_END_OPEN )
        // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:82: TAG_END_OPEN
        {
        mTAG_END_OPEN(); if (state.failed) return ;


        }

    }
    // $ANTLR end synpred4_XMLLexer

    // $ANTLR start synpred5_XMLLexer
    public final void synpred5_XMLLexer_fragment() throws RecognitionException {
        // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:95: ( GENERIC_ID )
        // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XMLLexer.g:1:95: GENERIC_ID
        {
        mGENERIC_ID(); if (state.failed) return ;


        }

    }
    // $ANTLR end synpred5_XMLLexer

    public final boolean synpred3_XMLLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_XMLLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred1_XMLLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_XMLLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred4_XMLLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred4_XMLLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred5_XMLLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_XMLLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_XMLLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_XMLLexer_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


 

}