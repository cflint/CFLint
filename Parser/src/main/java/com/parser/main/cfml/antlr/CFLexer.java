// $ANTLR 3.4 /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g 2012-10-27 03:10:04

package com.parser.main.cfml.antlr;

import java.util.Set;

import com.dictionary.main.DictionaryManager;
import com.dictionary.main.SyntaxDictionary;
import com.dictionary.main.Tag;
import com.dictionary.main.preferences.DictionaryPreferences;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class CFLexer extends Lexer {
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

      private static final int TEXT_CHANNEL = 89;
    boolean tagMode = false;
    boolean internalTagMode = false;
      private static int NONE_MODE = 0;
      private static int ENDTAG_MODE = 1;
      private static int STARTTAG_MODE = 2;
      private static int DOUBLE_QUOTE_STRING_MODE = 3;
      private static int SINGLE_QUOTE_STRING_MODE = 4;
      private static int HASH_CFML_MODE = 5;
      //private static SyntaxDictionary cfdic;

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

    public CFLexer() {} 
    public CFLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public CFLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "/Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g"; }

    // $ANTLR start "TAG_START_OPEN"
    public final void mTAG_START_OPEN() throws RecognitionException {
        try {
            int _type = TAG_START_OPEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken name=null;

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:78:16: ( ( '<' CFTAG_ID name= ID ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:78:18: ( '<' CFTAG_ID name= ID )
            {
              
            //DictionaryManager.initDictionaries();
            //cfdic = DictionaryManager.getDictionary("CF_DICTIONARY");
            //Set<Tag> cfTags = cfdic.getAllTags();


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:82:2: ( '<' CFTAG_ID name= ID )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:82:3: '<' CFTAG_ID name= ID
            {
            match('<'); 

            mCFTAG_ID(); 


            int nameStart31 = getCharIndex();
            int nameStartLine31 = getLine();
            int nameStartCharPos31 = getCharPositionInLine();
            mID(); 
            name = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, nameStart31, getCharIndex()-1);
            name.setLine(nameStartLine31);
            name.setCharPositionInLine(nameStartCharPos31);


             tagMode = true; 

            }


             
            if((name!=null?name.getText():null).equals("set")) {
            	_type=SINGLETAG;
            }
            if((name!=null?name.getText():null).equals("if")) {
            	//_type=CFIFTAG;
            	System.out.println("iftag:"+(name!=null?name.getText():null)+ " type:"+(name!=null?name.getType():0));
            }

            System.out.println("PCDATA:"+(name!=null?name.getText():null)+ " type:"+(name!=null?name.getType():0)); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TAG_START_OPEN"

    // $ANTLR start "ATTR_EQ"
    public final void mATTR_EQ() throws RecognitionException {
        try {
            int _type = ATTR_EQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:94:9: ({...}? => '=' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:94:11: {...}? => '='
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
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:95:21: ({...}? => STRING_LITERAL )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:95:23: {...}? => STRING_LITERAL
            {
            if ( !(( tagMode )) ) {
                throw new FailedPredicateException(input, "ATTR_VALUE", " tagMode ");
            }

            mSTRING_LITERAL(); 


             System.out.println("PCDATA: string"); 

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
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:100:17: ({...}? => '/' CLOSE_CHEVRON )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:100:19: {...}? => '/' CLOSE_CHEVRON
            {
            if ( !(( tagMode )) ) {
                throw new FailedPredicateException(input, "TAG_EMPTY_CLOSE", " tagMode ");
            }

            match('/'); 

            mCLOSE_CHEVRON(); 


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

    // $ANTLR start "TAG_CLOSE"
    public final void mTAG_CLOSE() throws RecognitionException {
        try {
            int _type = TAG_CLOSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:101:11: ({...}? => CLOSE_CHEVRON )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:101:13: {...}? => CLOSE_CHEVRON
            {
            if ( !(( tagMode )) ) {
                throw new FailedPredicateException(input, "TAG_CLOSE", " tagMode ");
            }

            mCLOSE_CHEVRON(); 


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

    // $ANTLR start "TAG_END_OPEN"
    public final void mTAG_END_OPEN() throws RecognitionException {
        try {
            int _type = TAG_END_OPEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken name=null;

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:102:14: ( ( OPEN_CHEVRON '/' CFTAG_ID name= ID ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:102:16: ( OPEN_CHEVRON '/' CFTAG_ID name= ID )
            {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:102:16: ( OPEN_CHEVRON '/' CFTAG_ID name= ID )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:102:17: OPEN_CHEVRON '/' CFTAG_ID name= ID
            {
            mOPEN_CHEVRON(); 


            match('/'); 

            mCFTAG_ID(); 


            int nameStart135 = getCharIndex();
            int nameStartLine135 = getLine();
            int nameStartCharPos135 = getCharPositionInLine();
            mID(); 
            name = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, nameStart135, getCharIndex()-1);
            name.setLine(nameStartLine135);
            name.setCharPositionInLine(nameStartCharPos135);


             tagMode = true; 

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
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:105:5: ({...}? => ( LETTER | '_' | ':' ) ( NAMECHAR )* )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:105:7: {...}? => ( LETTER | '_' | ':' ) ( NAMECHAR )*
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


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:106:29: ( NAMECHAR )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= '-' && LA1_0 <= '.')||(LA1_0 >= '0' && LA1_0 <= ':')||(LA1_0 >= 'A' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:
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
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:110:3: ({...}? => ( '\"' ( DoubleStringCharacter )* '\"' | '\\'' ( SingleStringCharacter )* '\\'' ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:110:4: {...}? => ( '\"' ( DoubleStringCharacter )* '\"' | '\\'' ( SingleStringCharacter )* '\\'' )
            {
            if ( !(( tagMode )) ) {
                throw new FailedPredicateException(input, "STRING_LITERAL", " tagMode ");
            }

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:110:19: ( '\"' ( DoubleStringCharacter )* '\"' | '\\'' ( SingleStringCharacter )* '\\'' )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='\"') ) {
                alt4=1;
            }
            else if ( (LA4_0=='\'') ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }
            switch (alt4) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:110:21: '\"' ( DoubleStringCharacter )* '\"'
                    {
                    match('\"'); 

                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:110:25: ( DoubleStringCharacter )*
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
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:110:25: DoubleStringCharacter
                    	    {
                    	    mDoubleStringCharacter(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop2;
                        }
                    } while (true);


                    match('\"'); 

                    }
                    break;
                case 2 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:111:5: '\\'' ( SingleStringCharacter )* '\\''
                    {
                    match('\''); 

                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:111:10: ( SingleStringCharacter )*
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
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:111:10: SingleStringCharacter
                    	    {
                    	    mSingleStringCharacter(); 


                    	    }
                    	    break;

                    	default :
                    	    break loop3;
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
    // $ANTLR end "STRING_LITERAL"

    // $ANTLR start "DoubleStringCharacter"
    public final void mDoubleStringCharacter() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:115:3: (~ ( '\"' ) | '\"\"' )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( ((LA5_0 >= '\u0000' && LA5_0 <= '!')||(LA5_0 >= '#' && LA5_0 <= '\uFFFF')) ) {
                alt5=1;
            }
            else if ( (LA5_0=='\"') ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:115:5: ~ ( '\"' )
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
                case 2 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:116:5: '\"\"'
                    {
                    match("\"\""); 



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
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:120:3: (~ ( '\\'' ) | '\\'\\'' )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( ((LA6_0 >= '\u0000' && LA6_0 <= '&')||(LA6_0 >= '(' && LA6_0 <= '\uFFFF')) ) {
                alt6=1;
            }
            else if ( (LA6_0=='\'') ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }
            switch (alt6) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:120:5: ~ ( '\\'' )
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
                case 2 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:121:5: '\\'\\''
                    {
                    match("''"); 



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
            int _type = PCDATA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:125:8: ({...}? => (~ '<' )+ )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:125:10: {...}? => (~ '<' )+
            {
            if ( !(( !tagMode )) ) {
                throw new FailedPredicateException(input, "PCDATA", " !tagMode ");
            }

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:125:26: (~ '<' )+
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
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:
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
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
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

    // $ANTLR start "CFSETDATA"
    public final void mCFSETDATA() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:126:20: ({...}? => (~ '>' )+ )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:126:22: {...}? => (~ '>' )+
            {
            if ( !(( tagMode )) ) {
                throw new FailedPredicateException(input, "CFSETDATA", " tagMode ");
            }

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:126:37: (~ '>' )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0 >= '\u0000' && LA8_0 <= '=')||(LA8_0 >= '?' && LA8_0 <= '\uFFFF')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '=')||(input.LA(1) >= '?' && input.LA(1) <= '\uFFFF') ) {
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
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CFSETDATA"

    // $ANTLR start "CFTAG_ID"
    public final void mCFTAG_ID() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:129:5: ( ( 'c' | 'C' ) ( 'f' | 'F' ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:130:7: ( 'c' | 'C' ) ( 'f' | 'F' )
            {
            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
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
    // $ANTLR end "CFTAG_ID"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:135:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:135:9: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:135:33: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0 >= '0' && LA9_0 <= '9')||(LA9_0 >= 'A' && LA9_0 <= 'Z')||LA9_0=='_'||(LA9_0 >= 'a' && LA9_0 <= 'z')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
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
            	    break loop9;
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
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:138:5: ( 'try' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:138:8: 'try'
            {
            match("try"); 



            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TRY"

    // $ANTLR start "OPEN_CHEVRON"
    public final void mOPEN_CHEVRON() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:142:13: ( '<' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:142:16: '<'
            {
            match('<'); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "OPEN_CHEVRON"

    // $ANTLR start "CLOSE_CHEVRON"
    public final void mCLOSE_CHEVRON() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:146:14: ( '>' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:146:17: '>'
            {
            match('>'); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CLOSE_CHEVRON"

    // $ANTLR start "SINGLETAG"
    public final void mSINGLETAG() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:150:10: ( 'placeholder' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:150:13: 'placeholder'
            {
            match("placeholder"); 



            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SINGLETAG"

    // $ANTLR start "CFIFTAG"
    public final void mCFIFTAG() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:154:8: ( 'placeholder' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:154:11: 'placeholder'
            {
            match("placeholder"); 



            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CFIFTAG"

    // $ANTLR start "CFELSETAG"
    public final void mCFELSETAG() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:158:10: ( 'placeholder' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:158:13: 'placeholder'
            {
            match("placeholder"); 



            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CFELSETAG"

    // $ANTLR start "NAMECHAR"
    public final void mNAMECHAR() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:161:5: ( LETTER | DIGIT | '.' | '-' | '_' | ':' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:
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
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:165:5: ( '0' .. '9' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:
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
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:169:5: ( ( 'a' .. 'z' | 'A' .. 'Z' ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:
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
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:172:5: ({...}? => ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:172:8: {...}? => ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )
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

    // $ANTLR start "OTHER"
    public final void mOTHER() throws RecognitionException {
        try {
            int _type = OTHER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:176:3: ({...}? => ( options {greedy=false; } : . ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:177:3: {...}? => ( options {greedy=false; } : . )
            {
            if ( !(( !tagMode )) ) {
                throw new FailedPredicateException(input, "OTHER", " !tagMode ");
            }

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:178:3: ( options {greedy=false; } : . )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:178:30: .
            {
            matchAny(); 

            }



                _channel=TEXT_CHANNEL; //test is on a seperate channel, in case you want it
              

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "OTHER"

    public void mTokens() throws RecognitionException {
        // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:1:8: ( TAG_START_OPEN | ATTR_EQ | TAG_EMPTY_CLOSE | TAG_CLOSE | TAG_END_OPEN | GENERIC_ID | STRING_LITERAL | PCDATA | WS | OTHER )
        int alt10=10;
        alt10 = dfa10.predict(input);
        switch (alt10) {
            case 1 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:1:10: TAG_START_OPEN
                {
                mTAG_START_OPEN(); 


                }
                break;
            case 2 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:1:25: ATTR_EQ
                {
                mATTR_EQ(); 


                }
                break;
            case 3 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:1:33: TAG_EMPTY_CLOSE
                {
                mTAG_EMPTY_CLOSE(); 


                }
                break;
            case 4 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:1:49: TAG_CLOSE
                {
                mTAG_CLOSE(); 


                }
                break;
            case 5 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:1:59: TAG_END_OPEN
                {
                mTAG_END_OPEN(); 


                }
                break;
            case 6 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:1:72: GENERIC_ID
                {
                mGENERIC_ID(); 


                }
                break;
            case 7 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:1:83: STRING_LITERAL
                {
                mSTRING_LITERAL(); 


                }
                break;
            case 8 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:1:98: PCDATA
                {
                mPCDATA(); 


                }
                break;
            case 9 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:1:105: WS
                {
                mWS(); 


                }
                break;
            case 10 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/CF.g:1:108: OTHER
                {
                mOTHER(); 


                }
                break;

        }

    }


    protected DFA10 dfa10 = new DFA10(this);
    static final String DFA10_eotS =
        "\1\uffff\1\13\1\15\1\20\1\21\1\23\2\20\1\31\1\20\5\uffff\1\33\2"+
        "\uffff\1\35\1\uffff\1\16\1\40\1\uffff\1\16\1\40\6\uffff\1\16\1\uffff"+
        "\1\16\2\uffff";
    static final String DFA10_eofS =
        "\44\uffff";
    static final String DFA10_minS =
        "\1\0\1\57\10\0\3\uffff\1\0\1\uffff\7\0\1\uffff\3\0\1\uffff\1\0\1"+
        "\uffff\1\0\1\uffff\3\0\2\uffff";
    static final String DFA10_maxS =
        "\1\uffff\1\143\10\uffff\3\uffff\1\0\1\uffff\1\uffff\2\0\1\uffff"+
        "\1\0\2\uffff\1\uffff\2\uffff\1\0\1\uffff\1\0\1\uffff\1\0\1\uffff"+
        "\1\uffff\1\0\1\uffff\2\uffff";
    static final String DFA10_acceptS =
        "\12\uffff\1\1\1\12\1\5\1\uffff\1\10\7\uffff\1\7\3\uffff\1\2\1\uffff"+
        "\1\4\1\uffff\1\6\3\uffff\1\11\1\3";
    static final String DFA10_specialS =
        "\1\15\1\31\1\5\1\24\1\30\1\16\1\17\1\3\1\14\1\25\3\uffff\1\11\1"+
        "\uffff\1\22\1\6\1\13\1\26\1\0\1\23\1\21\1\uffff\1\1\1\27\1\4\1\uffff"+
        "\1\12\1\uffff\1\10\1\uffff\1\20\1\7\1\2\2\uffff}>";
    static final String[] DFA10_transitionS = {
            "\11\11\2\10\1\11\2\10\22\11\1\10\1\11\1\6\4\11\1\7\7\11\1\3"+
            "\12\11\1\5\1\11\1\1\1\2\1\4\2\11\32\5\4\11\1\5\1\11\32\5\uff85"+
            "\11",
            "\1\14\23\uffff\1\12\37\uffff\1\12",
            "\74\16\1\uffff\uffc3\16",
            "\74\16\1\uffff\1\16\1\17\uffc1\16",
            "\74\16\1\uffff\uffc3\16",
            "\55\16\2\22\1\16\13\22\1\16\1\uffff\4\16\32\22\4\16\1\22\1"+
            "\16\32\22\uff85\16",
            "\42\24\1\25\31\24\1\26\uffc3\24",
            "\47\27\1\30\24\27\1\26\uffc3\27",
            "\74\16\1\uffff\uffc3\16",
            "\74\16\1\uffff\uffc3\16",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "\74\16\1\uffff\uffc3\16",
            "\1\uffff",
            "\1\uffff",
            "\55\16\2\22\1\16\13\22\1\16\1\uffff\4\16\32\22\4\16\1\22\1"+
            "\16\32\22\uff85\16",
            "\1\uffff",
            "\42\24\1\25\31\24\1\26\uffc3\24",
            "\42\16\1\37\31\16\1\uffff\uffc3\16",
            "",
            "\47\27\1\30\24\27\1\26\uffc3\27",
            "\47\16\1\41\24\16\1\uffff\uffc3\16",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "\42\24\1\25\31\24\1\26\uffc3\24",
            "\1\uffff",
            "\47\27\1\30\24\27\1\26\uffc3\27",
            "",
            ""
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( TAG_START_OPEN | ATTR_EQ | TAG_EMPTY_CLOSE | TAG_CLOSE | TAG_END_OPEN | GENERIC_ID | STRING_LITERAL | PCDATA | WS | OTHER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA10_19 = input.LA(1);

                         
                        int index10_19 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (( tagMode )) ) {s = 30;}

                        else if ( (( !tagMode )) ) {s = 14;}

                        else if ( (( !tagMode )) ) {s = 11;}

                         
                        input.seek(index10_19);

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA10_23 = input.LA(1);

                         
                        int index10_23 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA10_23=='\'') && ((( !tagMode )||( tagMode )))) {s = 24;}

                        else if ( ((LA10_23 >= '\u0000' && LA10_23 <= '&')||(LA10_23 >= '(' && LA10_23 <= ';')||(LA10_23 >= '=' && LA10_23 <= '\uFFFF')) && ((( !tagMode )||( tagMode )))) {s = 23;}

                        else if ( (LA10_23=='<') && (( tagMode ))) {s = 22;}

                        else s = 14;

                         
                        input.seek(index10_23);

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA10_33 = input.LA(1);

                         
                        int index10_33 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA10_33=='\'') && ((( !tagMode )||( tagMode )))) {s = 24;}

                        else if ( ((LA10_33 >= '\u0000' && LA10_33 <= '&')||(LA10_33 >= '(' && LA10_33 <= ';')||(LA10_33 >= '=' && LA10_33 <= '\uFFFF')) && ((( !tagMode )||( tagMode )))) {s = 23;}

                        else if ( (LA10_33=='<') && (( tagMode ))) {s = 22;}

                        else s = 14;

                         
                        input.seek(index10_33);

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA10_7 = input.LA(1);

                         
                        int index10_7 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA10_7 >= '\u0000' && LA10_7 <= '&')||(LA10_7 >= '(' && LA10_7 <= ';')||(LA10_7 >= '=' && LA10_7 <= '\uFFFF')) && ((( !tagMode )||( tagMode )))) {s = 23;}

                        else if ( (LA10_7=='\'') && ((( !tagMode )||( tagMode )))) {s = 24;}

                        else if ( (LA10_7=='<') && (( tagMode ))) {s = 22;}

                        else s = 16;

                         
                        input.seek(index10_7);

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA10_25 = input.LA(1);

                         
                        int index10_25 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (( !tagMode )) ) {s = 14;}

                        else if ( (( tagMode )) ) {s = 34;}

                        else if ( (( !tagMode )) ) {s = 11;}

                         
                        input.seek(index10_25);

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA10_2 = input.LA(1);

                         
                        int index10_2 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA10_2 >= '\u0000' && LA10_2 <= ';')||(LA10_2 >= '=' && LA10_2 <= '\uFFFF')) && (( !tagMode ))) {s = 14;}

                        else s = 13;

                         
                        input.seek(index10_2);

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA10_16 = input.LA(1);

                         
                        int index10_16 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (( !tagMode )) ) {s = 14;}

                        else if ( (( !tagMode )) ) {s = 11;}

                         
                        input.seek(index10_16);

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA10_32 = input.LA(1);

                         
                        int index10_32 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (( tagMode )) ) {s = 22;}

                        else if ( (( !tagMode )) ) {s = 14;}

                         
                        input.seek(index10_32);

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA10_29 = input.LA(1);

                         
                        int index10_29 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (( tagMode )) ) {s = 30;}

                        else if ( (( !tagMode )) ) {s = 14;}

                         
                        input.seek(index10_29);

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA10_13 = input.LA(1);

                         
                        int index10_13 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (( tagMode )) ) {s = 26;}

                        else if ( (( !tagMode )) ) {s = 14;}

                        else if ( (( !tagMode )) ) {s = 11;}

                         
                        input.seek(index10_13);

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA10_27 = input.LA(1);

                         
                        int index10_27 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (( tagMode )) ) {s = 35;}

                        else if ( (( !tagMode )) ) {s = 14;}

                         
                        input.seek(index10_27);

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA10_17 = input.LA(1);

                         
                        int index10_17 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (( tagMode )) ) {s = 28;}

                        else if ( (( !tagMode )) ) {s = 14;}

                        else if ( (( !tagMode )) ) {s = 11;}

                         
                        input.seek(index10_17);

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA10_8 = input.LA(1);

                         
                        int index10_8 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA10_8 >= '\u0000' && LA10_8 <= ';')||(LA10_8 >= '=' && LA10_8 <= '\uFFFF')) && (( !tagMode ))) {s = 14;}

                        else s = 25;

                         
                        input.seek(index10_8);

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA10_0 = input.LA(1);

                         
                        int index10_0 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA10_0=='<') ) {s = 1;}

                        else if ( (LA10_0=='=') && ((( !tagMode )||( tagMode )))) {s = 2;}

                        else if ( (LA10_0=='/') && ((( !tagMode )||( tagMode )))) {s = 3;}

                        else if ( (LA10_0=='>') && ((( !tagMode )||( tagMode )))) {s = 4;}

                        else if ( (LA10_0==':'||(LA10_0 >= 'A' && LA10_0 <= 'Z')||LA10_0=='_'||(LA10_0 >= 'a' && LA10_0 <= 'z')) && ((( !tagMode )||( tagMode )))) {s = 5;}

                        else if ( (LA10_0=='\"') && ((( !tagMode )||( tagMode )))) {s = 6;}

                        else if ( (LA10_0=='\'') && ((( !tagMode )||( tagMode )))) {s = 7;}

                        else if ( ((LA10_0 >= '\t' && LA10_0 <= '\n')||(LA10_0 >= '\f' && LA10_0 <= '\r')||LA10_0==' ') && ((( !tagMode )||( tagMode )))) {s = 8;}

                        else if ( ((LA10_0 >= '\u0000' && LA10_0 <= '\b')||LA10_0=='\u000B'||(LA10_0 >= '\u000E' && LA10_0 <= '\u001F')||LA10_0=='!'||(LA10_0 >= '#' && LA10_0 <= '&')||(LA10_0 >= '(' && LA10_0 <= '.')||(LA10_0 >= '0' && LA10_0 <= '9')||LA10_0==';'||(LA10_0 >= '?' && LA10_0 <= '@')||(LA10_0 >= '[' && LA10_0 <= '^')||LA10_0=='`'||(LA10_0 >= '{' && LA10_0 <= '\uFFFF')) && (( !tagMode ))) {s = 9;}

                         
                        input.seek(index10_0);

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA10_5 = input.LA(1);

                         
                        int index10_5 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA10_5 >= '-' && LA10_5 <= '.')||(LA10_5 >= '0' && LA10_5 <= ':')||(LA10_5 >= 'A' && LA10_5 <= 'Z')||LA10_5=='_'||(LA10_5 >= 'a' && LA10_5 <= 'z')) && ((( !tagMode )||( tagMode )))) {s = 18;}

                        else if ( ((LA10_5 >= '\u0000' && LA10_5 <= ',')||LA10_5=='/'||LA10_5==';'||(LA10_5 >= '=' && LA10_5 <= '@')||(LA10_5 >= '[' && LA10_5 <= '^')||LA10_5=='`'||(LA10_5 >= '{' && LA10_5 <= '\uFFFF')) && (( !tagMode ))) {s = 14;}

                        else s = 19;

                         
                        input.seek(index10_5);

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA10_6 = input.LA(1);

                         
                        int index10_6 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA10_6 >= '\u0000' && LA10_6 <= '!')||(LA10_6 >= '#' && LA10_6 <= ';')||(LA10_6 >= '=' && LA10_6 <= '\uFFFF')) && ((( !tagMode )||( tagMode )))) {s = 20;}

                        else if ( (LA10_6=='\"') && ((( !tagMode )||( tagMode )))) {s = 21;}

                        else if ( (LA10_6=='<') && (( tagMode ))) {s = 22;}

                        else s = 16;

                         
                        input.seek(index10_6);

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA10_31 = input.LA(1);

                         
                        int index10_31 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA10_31=='\"') && ((( !tagMode )||( tagMode )))) {s = 21;}

                        else if ( ((LA10_31 >= '\u0000' && LA10_31 <= '!')||(LA10_31 >= '#' && LA10_31 <= ';')||(LA10_31 >= '=' && LA10_31 <= '\uFFFF')) && ((( !tagMode )||( tagMode )))) {s = 20;}

                        else if ( (LA10_31=='<') && (( tagMode ))) {s = 22;}

                        else s = 14;

                         
                        input.seek(index10_31);

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA10_21 = input.LA(1);

                         
                        int index10_21 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA10_21=='\"') && ((( !tagMode )||( tagMode )))) {s = 31;}

                        else if ( ((LA10_21 >= '\u0000' && LA10_21 <= '!')||(LA10_21 >= '#' && LA10_21 <= ';')||(LA10_21 >= '=' && LA10_21 <= '\uFFFF')) && (( !tagMode ))) {s = 14;}

                        else s = 32;

                         
                        input.seek(index10_21);

                        if ( s>=0 ) return s;
                        break;

                    case 18 : 
                        int LA10_15 = input.LA(1);

                         
                        int index10_15 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA10_15 >= '\u0000' && LA10_15 <= ';')||(LA10_15 >= '=' && LA10_15 <= '\uFFFF')) && (( !tagMode ))) {s = 14;}

                        else s = 27;

                         
                        input.seek(index10_15);

                        if ( s>=0 ) return s;
                        break;

                    case 19 : 
                        int LA10_20 = input.LA(1);

                         
                        int index10_20 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA10_20=='\"') && ((( !tagMode )||( tagMode )))) {s = 21;}

                        else if ( ((LA10_20 >= '\u0000' && LA10_20 <= '!')||(LA10_20 >= '#' && LA10_20 <= ';')||(LA10_20 >= '=' && LA10_20 <= '\uFFFF')) && ((( !tagMode )||( tagMode )))) {s = 20;}

                        else if ( (LA10_20=='<') && (( tagMode ))) {s = 22;}

                        else s = 14;

                         
                        input.seek(index10_20);

                        if ( s>=0 ) return s;
                        break;

                    case 20 : 
                        int LA10_3 = input.LA(1);

                         
                        int index10_3 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA10_3=='>') && ((( !tagMode )||( tagMode )))) {s = 15;}

                        else if ( ((LA10_3 >= '\u0000' && LA10_3 <= ';')||LA10_3=='='||(LA10_3 >= '?' && LA10_3 <= '\uFFFF')) && (( !tagMode ))) {s = 14;}

                        else s = 16;

                         
                        input.seek(index10_3);

                        if ( s>=0 ) return s;
                        break;

                    case 21 : 
                        int LA10_9 = input.LA(1);

                         
                        int index10_9 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA10_9 >= '\u0000' && LA10_9 <= ';')||(LA10_9 >= '=' && LA10_9 <= '\uFFFF')) && (( !tagMode ))) {s = 14;}

                        else s = 16;

                         
                        input.seek(index10_9);

                        if ( s>=0 ) return s;
                        break;

                    case 22 : 
                        int LA10_18 = input.LA(1);

                         
                        int index10_18 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA10_18 >= '-' && LA10_18 <= '.')||(LA10_18 >= '0' && LA10_18 <= ':')||(LA10_18 >= 'A' && LA10_18 <= 'Z')||LA10_18=='_'||(LA10_18 >= 'a' && LA10_18 <= 'z')) && ((( !tagMode )||( tagMode )))) {s = 18;}

                        else if ( ((LA10_18 >= '\u0000' && LA10_18 <= ',')||LA10_18=='/'||LA10_18==';'||(LA10_18 >= '=' && LA10_18 <= '@')||(LA10_18 >= '[' && LA10_18 <= '^')||LA10_18=='`'||(LA10_18 >= '{' && LA10_18 <= '\uFFFF')) && (( !tagMode ))) {s = 14;}

                        else s = 29;

                         
                        input.seek(index10_18);

                        if ( s>=0 ) return s;
                        break;

                    case 23 : 
                        int LA10_24 = input.LA(1);

                         
                        int index10_24 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA10_24=='\'') && ((( !tagMode )||( tagMode )))) {s = 33;}

                        else if ( ((LA10_24 >= '\u0000' && LA10_24 <= '&')||(LA10_24 >= '(' && LA10_24 <= ';')||(LA10_24 >= '=' && LA10_24 <= '\uFFFF')) && (( !tagMode ))) {s = 14;}

                        else s = 32;

                         
                        input.seek(index10_24);

                        if ( s>=0 ) return s;
                        break;

                    case 24 : 
                        int LA10_4 = input.LA(1);

                         
                        int index10_4 = input.index();
                        input.rewind();

                        s = -1;
                        if ( ((LA10_4 >= '\u0000' && LA10_4 <= ';')||(LA10_4 >= '=' && LA10_4 <= '\uFFFF')) && (( !tagMode ))) {s = 14;}

                        else s = 17;

                         
                        input.seek(index10_4);

                        if ( s>=0 ) return s;
                        break;

                    case 25 : 
                        int LA10_1 = input.LA(1);

                         
                        int index10_1 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (LA10_1=='C'||LA10_1=='c') ) {s = 10;}

                        else if ( (LA10_1=='/') ) {s = 12;}

                        else s = 11;

                         
                        input.seek(index10_1);

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 10, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

}