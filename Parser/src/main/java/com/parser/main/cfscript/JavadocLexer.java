// $ANTLR 3.4 /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g 2012-10-27 03:09:55

package com.parser.main.cfscript;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class JavadocLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__7=7;
    public static final int END=4;
    public static final int ID=5;
    public static final int WS=6;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public JavadocLexer() {} 
    public JavadocLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public JavadocLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "/Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g"; }

    // $ANTLR start "T__7"
    public final void mT__7() throws RecognitionException {
        try {
            int _type = T__7;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:6:6: ( '@author' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:6:8: '@author'
            {
            match("@author"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__7"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:15:4: ( ( 'a' .. 'z' | 'A' .. 'Z' )+ )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:15:6: ( 'a' .. 'z' | 'A' .. 'Z' )+
            {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:15:6: ( 'a' .. 'z' | 'A' .. 'Z' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= 'A' && LA1_0 <= 'Z')||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:
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
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "END"
    public final void mEND() throws RecognitionException {
        try {
            int _type = END;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:31:5: ( '*/' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:31:7: '*/'
            {
            match("*/"); 



            emit(Token.EOF_TOKEN);

            System.out.println("exit javadoc");

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "END"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:29:4: ( ( ' ' | '\\t' | '\\n' )+ )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:29:6: ( ' ' | '\\t' | '\\n' )+
            {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:29:6: ( ' ' | '\\t' | '\\n' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0 >= '\t' && LA2_0 <= '\n')||LA2_0==' ') ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:
            	    {
            	    if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)==' ' ) {
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
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            _channel=HIDDEN;

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
        // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:1:8: ( T__7 | ID | END | WS )
        int alt3=4;
        switch ( input.LA(1) ) {
        case '@':
            {
            alt3=1;
            }
            break;
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case 'a':
        case 'b':
        case 'c':
        case 'd':
        case 'e':
        case 'f':
        case 'g':
        case 'h':
        case 'i':
        case 'j':
        case 'k':
        case 'l':
        case 'm':
        case 'n':
        case 'o':
        case 'p':
        case 'q':
        case 'r':
        case 's':
        case 't':
        case 'u':
        case 'v':
        case 'w':
        case 'x':
        case 'y':
        case 'z':
            {
            alt3=2;
            }
            break;
        case '*':
            {
            alt3=3;
            }
            break;
        case '\t':
        case '\n':
        case ' ':
            {
            alt3=4;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 3, 0, input);

            throw nvae;

        }

        switch (alt3) {
            case 1 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:1:10: T__7
                {
                mT__7(); 


                }
                break;
            case 2 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:1:15: ID
                {
                mID(); 


                }
                break;
            case 3 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:1:18: END
                {
                mEND(); 


                }
                break;
            case 4 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/Javadoc.g:1:22: WS
                {
                mWS(); 


                }
                break;

        }

    }


 

}