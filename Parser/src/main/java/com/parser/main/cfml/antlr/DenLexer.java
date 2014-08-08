// $ANTLR 3.4 /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g 2012-10-27 03:10:06

package com.parser.main.cfml.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class DenLexer extends Lexer {
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

        boolean tagMode = false;


    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public DenLexer() {} 
    public DenLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public DenLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "/Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g"; }

    // $ANTLR start "TAG_START_OPEN"
    public final void mTAG_START_OPEN() throws RecognitionException {
        try {
            int _type = TAG_START_OPEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:11:16: ( '<' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:11:18: '<'
            {
            match('<'); 

             tagMode = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TAG_START_OPEN"

    // $ANTLR start "TAG_END_OPEN"
    public final void mTAG_END_OPEN() throws RecognitionException {
        try {
            int _type = TAG_END_OPEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:12:14: ( '</' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:12:16: '</'
            {
            match("</"); 



             tagMode = true; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TAG_END_OPEN"

    // $ANTLR start "TAG_CLOSE"
    public final void mTAG_CLOSE() throws RecognitionException {
        try {
            int _type = TAG_CLOSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:13:11: ({...}? => '>' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:13:13: {...}? => '>'
            {
            if ( !(( tagMode )) ) {
                throw new FailedPredicateException(input, "TAG_CLOSE", " tagMode ");
            }

            match('>'); 

             tagMode = false; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TAG_CLOSE"

    // $ANTLR start "TAG_EMPTY_CLOSE"
    public final void mTAG_EMPTY_CLOSE() throws RecognitionException {
        try {
            int _type = TAG_EMPTY_CLOSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:14:17: ({...}? => '/>' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:14:19: {...}? => '/>'
            {
            if ( !(( tagMode )) ) {
                throw new FailedPredicateException(input, "TAG_EMPTY_CLOSE", " tagMode ");
            }

            match("/>"); 



             tagMode = false; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TAG_EMPTY_CLOSE"

    // $ANTLR start "ATTR_EQ"
    public final void mATTR_EQ() throws RecognitionException {
        try {
            int _type = ATTR_EQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:16:9: ({...}? => '=' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:16:11: {...}? => '='
            {
            if ( !(( tagMode )) ) {
                throw new FailedPredicateException(input, "ATTR_EQ", " tagMode ");
            }

            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ATTR_EQ"

    // $ANTLR start "ATTR_VALUE"
    public final void mATTR_VALUE() throws RecognitionException {
        try {
            int _type = ATTR_VALUE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:18:12: ({...}? => ( '\"' (~ '\"' )* '\"' | '\\'' (~ '\\'' )* '\\'' ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:18:14: {...}? => ( '\"' (~ '\"' )* '\"' | '\\'' (~ '\\'' )* '\\'' )
            {
            if ( !(( tagMode )) ) {
                throw new FailedPredicateException(input, "ATTR_VALUE", " tagMode ");
            }

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:19:9: ( '\"' (~ '\"' )* '\"' | '\\'' (~ '\\'' )* '\\'' )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='\"') ) {
                alt3=1;
            }
            else if ( (LA3_0=='\'') ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;

            }
            switch (alt3) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:19:11: '\"' (~ '\"' )* '\"'
                    {
                    match('\"'); 

                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:19:15: (~ '\"' )*
                    loop1:
                    do {
                        int alt1=2;
                        int LA1_0 = input.LA(1);

                        if ( ((LA1_0 >= '\u0000' && LA1_0 <= '!')||(LA1_0 >= '#' && LA1_0 <= '\uFFFF')) ) {
                            alt1=1;
                        }


                        switch (alt1) {
                    	case 1 :
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:
                    	    {
                    	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '\uFFFF') ) {
                    	        input.consume();
                    	    }
                    	    else {
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


                    match('\"'); 

                    }
                    break;
                case 2 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:20:11: '\\'' (~ '\\'' )* '\\''
                    {
                    match('\''); 

                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:20:16: (~ '\\'' )*
                    loop2:
                    do {
                        int alt2=2;
                        int LA2_0 = input.LA(1);

                        if ( ((LA2_0 >= '\u0000' && LA2_0 <= '&')||(LA2_0 >= '(' && LA2_0 <= '\uFFFF')) ) {
                            alt2=1;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:
                    	    {
                    	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '\uFFFF') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop2;
                        }
                    } while (true);


                    match('\''); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ATTR_VALUE"

    // $ANTLR start "PCDATA"
    public final void mPCDATA() throws RecognitionException {
        try {
            int _type = PCDATA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:24:8: ({...}? => (~ '<' )+ )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:24:10: {...}? => (~ '<' )+
            {
            if ( !(( !tagMode )) ) {
                throw new FailedPredicateException(input, "PCDATA", " !tagMode ");
            }

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:24:26: (~ '<' )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0 >= '\u0000' && LA4_0 <= ';')||(LA4_0 >= '=' && LA4_0 <= '\uFFFF')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= ';')||(input.LA(1) >= '=' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PCDATA"

    // $ANTLR start "GENERIC_ID"
    public final void mGENERIC_ID() throws RecognitionException {
        try {
            int _type = GENERIC_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:27:5: ({...}? => ( LETTER | '_' | ':' ) ( NAMECHAR )* )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:27:7: {...}? => ( LETTER | '_' | ':' ) ( NAMECHAR )*
            {
            if ( !(( tagMode )) ) {
                throw new FailedPredicateException(input, "GENERIC_ID", " tagMode ");
            }

            if ( input.LA(1)==':'||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:28:29: ( NAMECHAR )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0 >= '-' && LA5_0 <= '.')||(LA5_0 >= '0' && LA5_0 <= ':')||(LA5_0 >= 'A' && LA5_0 <= 'Z')||LA5_0=='_'||(LA5_0 >= 'a' && LA5_0 <= 'z')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:
            	    {
            	    if ( (input.LA(1) >= '-' && input.LA(1) <= '.')||(input.LA(1) >= '0' && input.LA(1) <= ':')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop5;
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

    // $ANTLR start "NAMECHAR"
    public final void mNAMECHAR() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:32:5: ( LETTER | DIGIT | '.' | '-' | '_' | ':' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:
            {
            if ( (input.LA(1) >= '-' && input.LA(1) <= '.')||(input.LA(1) >= '0' && input.LA(1) <= ':')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
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
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:36:5: ( '0' .. '9' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                input.consume();
            }
            else {
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
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:40:5: ( 'a' .. 'z' | 'A' .. 'Z' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
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
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:44:5: ({...}? => ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:44:8: {...}? => ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )
            {
            if ( !(( tagMode )) ) {
                throw new FailedPredicateException(input, "WS", " tagMode ");
            }

            if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            _channel=99;

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
        // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:1:8: ( TAG_START_OPEN | TAG_END_OPEN | TAG_CLOSE | TAG_EMPTY_CLOSE | ATTR_EQ | ATTR_VALUE | PCDATA | GENERIC_ID | WS )
        int alt6=9;
        alt6 = dfa6.predict(input);
        switch (alt6) {
            case 1 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:1:10: TAG_START_OPEN
                {
                mTAG_START_OPEN(); 


                }
                break;
            case 2 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:1:25: TAG_END_OPEN
                {
                mTAG_END_OPEN(); 


                }
                break;
            case 3 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:1:38: TAG_CLOSE
                {
                mTAG_CLOSE(); 


                }
                break;
            case 4 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:1:48: TAG_EMPTY_CLOSE
                {
                mTAG_EMPTY_CLOSE(); 


                }
                break;
            case 5 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:1:64: ATTR_EQ
                {
                mATTR_EQ(); 


                }
                break;
            case 6 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:1:72: ATTR_VALUE
                {
                mATTR_VALUE(); 


                }
                break;
            case 7 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:1:83: PCDATA
                {
                mPCDATA(); 


                }
                break;
            case 8 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:1:90: GENERIC_ID
                {
                mGENERIC_ID(); 


                }
                break;
            case 9 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/DenLexer.g:1:101: WS
                {
                mWS(); 


                }
                break;

        }

    }


    protected DFA6 dfa6 = new DFA6(this);
    static final String DFA6_eotS =
        "\1\uffff\1\13\1\14\1\11\1\16\2\11\1\24\1\26\4\uffff\1\30\1\uffff"+
        "\1\11\1\32\1\uffff\1\11\1\32\1\uffff\1\24\10\uffff";
    static final String DFA6_eofS =
        "\36\uffff";
    static final String DFA6_minS =
        "\1\0\1\57\1\0\1\76\5\0\3\uffff\5\0\1\uffff\5\0\1\uffff\1\0\1\uffff"+
        "\1\0\3\uffff";
    static final String DFA6_maxS =
        "\1\uffff\1\57\1\uffff\1\76\5\uffff\3\uffff\1\0\1\uffff\1\0\2\uffff"+
        "\1\uffff\2\uffff\1\0\1\uffff\1\0\1\uffff\1\0\1\uffff\1\0\3\uffff";
    static final String DFA6_acceptS =
        "\11\uffff\1\7\1\2\1\1\5\uffff\1\6\5\uffff\1\3\1\uffff\1\5\1\uffff"+
        "\1\10\1\11\1\4";
    static final String DFA6_specialS =
        "\1\14\1\uffff\1\1\1\6\1\23\1\16\1\12\1\22\1\0\3\uffff\1\3\1\17\1"+
        "\10\1\13\1\15\1\uffff\1\2\1\21\1\7\1\20\1\5\1\uffff\1\4\1\uffff"+
        "\1\11\3\uffff}>";
    static final String[] DFA6_transitionS = {
            "\11\11\2\10\1\11\2\10\22\11\1\10\1\11\1\5\4\11\1\6\7\11\1\3"+
            "\12\11\1\7\1\11\1\1\1\4\1\2\2\11\32\7\4\11\1\7\1\11\32\7\uff85"+
            "\11",
            "\1\12",
            "\74\11\1\uffff\uffc3\11",
            "\1\15",
            "\74\11\1\uffff\uffc3\11",
            "\42\17\1\20\31\17\1\21\uffc3\17",
            "\47\22\1\23\24\22\1\21\uffc3\22",
            "\55\11\2\25\1\11\13\25\1\11\1\uffff\4\11\32\25\4\11\1\25\1"+
            "\11\32\25\uff85\11",
            "\74\11\1\uffff\uffc3\11",
            "",
            "",
            "",
            "\1\uffff",
            "\74\11\1\uffff\uffc3\11",
            "\1\uffff",
            "\42\17\1\20\31\17\1\21\uffc3\17",
            "\74\11\1\uffff\uffc3\11",
            "",
            "\47\22\1\23\24\22\1\21\uffc3\22",
            "\74\11\1\uffff\uffc3\11",
            "\1\uffff",
            "\55\11\2\25\1\11\13\25\1\11\1\uffff\4\11\32\25\4\11\1\25\1"+
            "\11\32\25\uff85\11",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "",
            ""
    };

    static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
    static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
    static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
    static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
    static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
    static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
    static final short[][] DFA6_transition;

    static {
        int numStates = DFA6_transitionS.length;
        DFA6_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
        }
    }

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = DFA6_eot;
            this.eof = DFA6_eof;
            this.min = DFA6_min;
            this.max = DFA6_max;
            this.accept = DFA6_accept;
            this.special = DFA6_special;
            this.transition = DFA6_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( TAG_START_OPEN | TAG_END_OPEN | TAG_CLOSE | TAG_EMPTY_CLOSE | ATTR_EQ | ATTR_VALUE | PCDATA | GENERIC_ID | WS );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA6_8 = input.LA(1);

                         
                        int index6_8 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA6_8 >= '\u0000' && LA6_8 <= ';')||(LA6_8 >= '=' && LA6_8 <= '\uFFFF')) && (( !tagMode ))) {s = 9;}

                        else s = 22;

                         
                        input.seek(index6_8);

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA6_2 = input.LA(1);

                         
                        int index6_2 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA6_2 >= '\u0000' && LA6_2 <= ';')||(LA6_2 >= '=' && LA6_2 <= '\uFFFF')) && (( !tagMode ))) {s = 9;}

                        else s = 12;

                         
                        input.seek(index6_2);

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA6_18 = input.LA(1);

                         
                        int index6_18 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA6_18=='\'') && ((( !tagMode )||( tagMode )))) {s = 19;}

                        else if ( ((LA6_18 >= '\u0000' && LA6_18 <= '&')||(LA6_18 >= '(' && LA6_18 <= ';')||(LA6_18 >= '=' && LA6_18 <= '\uFFFF')) && ((( !tagMode )||( tagMode )))) {s = 18;}

                        else if ( (LA6_18=='<') && (( tagMode ))) {s = 17;}

                        else s = 9;

                         
                        input.seek(index6_18);

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA6_12 = input.LA(1);

                         
                        int index6_12 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (( tagMode )) ) {s = 23;}

                        else if ( (( !tagMode )) ) {s = 9;}

                         
                        input.seek(index6_12);

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA6_24 = input.LA(1);

                         
                        int index6_24 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (( tagMode )) ) {s = 29;}

                        else if ( (( !tagMode )) ) {s = 9;}

                         
                        input.seek(index6_24);

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA6_22 = input.LA(1);

                         
                        int index6_22 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (( !tagMode )) ) {s = 9;}

                        else if ( (( tagMode )) ) {s = 28;}

                         
                        input.seek(index6_22);

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA6_3 = input.LA(1);

                         
                        int index6_3 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA6_3=='>') && ((( !tagMode )||( tagMode )))) {s = 13;}

                        else s = 9;

                         
                        input.seek(index6_3);

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA6_20 = input.LA(1);

                         
                        int index6_20 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (( !tagMode )) ) {s = 9;}

                        else if ( (( tagMode )) ) {s = 27;}

                         
                        input.seek(index6_20);

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA6_14 = input.LA(1);

                         
                        int index6_14 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (( tagMode )) ) {s = 25;}

                        else if ( (( !tagMode )) ) {s = 9;}

                         
                        input.seek(index6_14);

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA6_26 = input.LA(1);

                         
                        int index6_26 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (( tagMode )) ) {s = 17;}

                        else if ( (( !tagMode )) ) {s = 9;}

                         
                        input.seek(index6_26);

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA6_6 = input.LA(1);

                         
                        int index6_6 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA6_6 >= '\u0000' && LA6_6 <= '&')||(LA6_6 >= '(' && LA6_6 <= ';')||(LA6_6 >= '=' && LA6_6 <= '\uFFFF')) && ((( !tagMode )||( tagMode )))) {s = 18;}

                        else if ( (LA6_6=='\'') && ((( !tagMode )||( tagMode )))) {s = 19;}

                        else if ( (LA6_6=='<') && (( tagMode ))) {s = 17;}

                        else s = 9;

                         
                        input.seek(index6_6);

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA6_15 = input.LA(1);

                         
                        int index6_15 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA6_15=='\"') && ((( !tagMode )||( tagMode )))) {s = 16;}

                        else if ( ((LA6_15 >= '\u0000' && LA6_15 <= '!')||(LA6_15 >= '#' && LA6_15 <= ';')||(LA6_15 >= '=' && LA6_15 <= '\uFFFF')) && ((( !tagMode )||( tagMode )))) {s = 15;}

                        else if ( (LA6_15=='<') && (( tagMode ))) {s = 17;}

                        else s = 9;

                         
                        input.seek(index6_15);

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA6_0 = input.LA(1);

                         
                        int index6_0 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA6_0=='<') ) {s = 1;}

                        else if ( (LA6_0=='>') && ((( !tagMode )||( tagMode )))) {s = 2;}

                        else if ( (LA6_0=='/') && ((( !tagMode )||( tagMode )))) {s = 3;}

                        else if ( (LA6_0=='=') && ((( !tagMode )||( tagMode )))) {s = 4;}

                        else if ( (LA6_0=='\"') && ((( !tagMode )||( tagMode )))) {s = 5;}

                        else if ( (LA6_0=='\'') && ((( !tagMode )||( tagMode )))) {s = 6;}

                        else if ( (LA6_0==':'||(LA6_0 >= 'A' && LA6_0 <= 'Z')||LA6_0=='_'||(LA6_0 >= 'a' && LA6_0 <= 'z')) && ((( !tagMode )||( tagMode )))) {s = 7;}

                        else if ( ((LA6_0 >= '\t' && LA6_0 <= '\n')||(LA6_0 >= '\f' && LA6_0 <= '\r')||LA6_0==' ') && ((( !tagMode )||( tagMode )))) {s = 8;}

                        else if ( ((LA6_0 >= '\u0000' && LA6_0 <= '\b')||LA6_0=='\u000B'||(LA6_0 >= '\u000E' && LA6_0 <= '\u001F')||LA6_0=='!'||(LA6_0 >= '#' && LA6_0 <= '&')||(LA6_0 >= '(' && LA6_0 <= '.')||(LA6_0 >= '0' && LA6_0 <= '9')||LA6_0==';'||(LA6_0 >= '?' && LA6_0 <= '@')||(LA6_0 >= '[' && LA6_0 <= '^')||LA6_0=='`'||(LA6_0 >= '{' && LA6_0 <= '\uFFFF')) && (( !tagMode ))) {s = 9;}

                         
                        input.seek(index6_0);

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA6_16 = input.LA(1);

                         
                        int index6_16 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA6_16 >= '\u0000' && LA6_16 <= ';')||(LA6_16 >= '=' && LA6_16 <= '\uFFFF')) && (( !tagMode ))) {s = 9;}

                        else s = 26;

                         
                        input.seek(index6_16);

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA6_5 = input.LA(1);

                         
                        int index6_5 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA6_5 >= '\u0000' && LA6_5 <= '!')||(LA6_5 >= '#' && LA6_5 <= ';')||(LA6_5 >= '=' && LA6_5 <= '\uFFFF')) && ((( !tagMode )||( tagMode )))) {s = 15;}

                        else if ( (LA6_5=='\"') && ((( !tagMode )||( tagMode )))) {s = 16;}

                        else if ( (LA6_5=='<') && (( tagMode ))) {s = 17;}

                        else s = 9;

                         
                        input.seek(index6_5);

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA6_13 = input.LA(1);

                         
                        int index6_13 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA6_13 >= '\u0000' && LA6_13 <= ';')||(LA6_13 >= '=' && LA6_13 <= '\uFFFF')) && (( !tagMode ))) {s = 9;}

                        else s = 24;

                         
                        input.seek(index6_13);

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA6_21 = input.LA(1);

                         
                        int index6_21 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA6_21 >= '-' && LA6_21 <= '.')||(LA6_21 >= '0' && LA6_21 <= ':')||(LA6_21 >= 'A' && LA6_21 <= 'Z')||LA6_21=='_'||(LA6_21 >= 'a' && LA6_21 <= 'z')) && ((( !tagMode )||( tagMode )))) {s = 21;}

                        else if ( ((LA6_21 >= '\u0000' && LA6_21 <= ',')||LA6_21=='/'||LA6_21==';'||(LA6_21 >= '=' && LA6_21 <= '@')||(LA6_21 >= '[' && LA6_21 <= '^')||LA6_21=='`'||(LA6_21 >= '{' && LA6_21 <= '\uFFFF')) && (( !tagMode ))) {s = 9;}

                        else s = 20;

                         
                        input.seek(index6_21);

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA6_19 = input.LA(1);

                         
                        int index6_19 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA6_19 >= '\u0000' && LA6_19 <= ';')||(LA6_19 >= '=' && LA6_19 <= '\uFFFF')) && (( !tagMode ))) {s = 9;}

                        else s = 26;

                         
                        input.seek(index6_19);

                        if ( s>=0 ) return s;
                        break;

                    case 18 : 
                        int LA6_7 = input.LA(1);

                         
                        int index6_7 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA6_7 >= '-' && LA6_7 <= '.')||(LA6_7 >= '0' && LA6_7 <= ':')||(LA6_7 >= 'A' && LA6_7 <= 'Z')||LA6_7=='_'||(LA6_7 >= 'a' && LA6_7 <= 'z')) && ((( !tagMode )||( tagMode )))) {s = 21;}

                        else if ( ((LA6_7 >= '\u0000' && LA6_7 <= ',')||LA6_7=='/'||LA6_7==';'||(LA6_7 >= '=' && LA6_7 <= '@')||(LA6_7 >= '[' && LA6_7 <= '^')||LA6_7=='`'||(LA6_7 >= '{' && LA6_7 <= '\uFFFF')) && (( !tagMode ))) {s = 9;}

                        else s = 20;

                         
                        input.seek(index6_7);

                        if ( s>=0 ) return s;
                        break;

                    case 19 : 
                        int LA6_4 = input.LA(1);

                         
                        int index6_4 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA6_4 >= '\u0000' && LA6_4 <= ';')||(LA6_4 >= '=' && LA6_4 <= '\uFFFF')) && (( !tagMode ))) {s = 9;}

                        else s = 14;

                         
                        input.seek(index6_4);

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 6, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

}