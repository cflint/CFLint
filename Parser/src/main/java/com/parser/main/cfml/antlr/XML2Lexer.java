// $ANTLR 3.4 /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g 2012-10-27 03:10:11

package com.parser.main.cfml.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class XML2Lexer extends Lexer {
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

        boolean tagMode = false;


    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public XML2Lexer() {} 
    public XML2Lexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public XML2Lexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "/Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g"; }

    // $ANTLR start "ELEMENT"
    public final void mELEMENT() throws RecognitionException {
        try {
            int _type = ELEMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken t=null;

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:47:5: ( ( START_TAG ( ELEMENT |t= PCDATA |t= CDATA |t= COMMENT )* END_TAG ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:47:7: ( START_TAG ( ELEMENT |t= PCDATA |t= CDATA |t= COMMENT )* END_TAG )
            {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:47:7: ( START_TAG ( ELEMENT |t= PCDATA |t= CDATA |t= COMMENT )* END_TAG )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:47:9: START_TAG ( ELEMENT |t= PCDATA |t= CDATA |t= COMMENT )* END_TAG
            {
            mSTART_TAG(); 


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:48:13: ( ELEMENT |t= PCDATA |t= CDATA |t= COMMENT )*
            loop1:
            do {
                int alt1=5;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='<') ) {
                    int LA1_1 = input.LA(2);

                    if ( (LA1_1=='!') ) {
                        int LA1_4 = input.LA(3);

                        if ( (LA1_4=='[') ) {
                            alt1=3;
                        }
                        else if ( (LA1_4=='-') ) {
                            alt1=4;
                        }


                    }
                    else if ( ((LA1_1 >= '\t' && LA1_1 <= '\n')||LA1_1=='\r'||LA1_1==' '||LA1_1==':'||(LA1_1 >= 'A' && LA1_1 <= 'Z')||LA1_1=='_'||(LA1_1 >= 'a' && LA1_1 <= 'z')) ) {
                        alt1=1;
                    }


                }
                else if ( ((LA1_0 >= '\u0000' && LA1_0 <= ';')||(LA1_0 >= '=' && LA1_0 <= '\uFFFF')) ) {
                    alt1=2;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:48:14: ELEMENT
            	    {
            	    mELEMENT(); 


            	    }
            	    break;
            	case 2 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:49:15: t= PCDATA
            	    {
            	    int tStart62 = getCharIndex();
            	    int tStartLine62 = getLine();
            	    int tStartCharPos62 = getCharPositionInLine();
            	    mPCDATA(); 
            	    t = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, tStart62, getCharIndex()-1);
            	    t.setLine(tStartLine62);
            	    t.setCharPositionInLine(tStartCharPos62);


            	     System.out.println("PCDATA: \""+t.getText()+"\""); 

            	    }
            	    break;
            	case 3 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:51:15: t= CDATA
            	    {
            	    int tStart98 = getCharIndex();
            	    int tStartLine98 = getLine();
            	    int tStartCharPos98 = getCharPositionInLine();
            	    mCDATA(); 
            	    t = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, tStart98, getCharIndex()-1);
            	    t.setLine(tStartLine98);
            	    t.setCharPositionInLine(tStartCharPos98);


            	     System.out.println("CDATA: \""+t.getText()+"\""); 

            	    }
            	    break;
            	case 4 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:53:15: t= COMMENT
            	    {
            	    int tStart134 = getCharIndex();
            	    int tStartLine134 = getLine();
            	    int tStartCharPos134 = getCharPositionInLine();
            	    mCOMMENT(); 
            	    t = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, tStart134, getCharIndex()-1);
            	    t.setLine(tStartLine134);
            	    t.setCharPositionInLine(tStartCharPos134);


            	     System.out.println("Comment: \""+t.getText()+"\""); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            mEND_TAG(); 


            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ELEMENT"

    // $ANTLR start "START_TAG"
    public final void mSTART_TAG() throws RecognitionException {
        try {
            int _type = START_TAG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken name=null;

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:62:5: ( '<' ( WS )? name= GENERIC_ID ( WS )? ( ATTRIBUTE ( WS )? )* '>' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:62:7: '<' ( WS )? name= GENERIC_ID ( WS )? ( ATTRIBUTE ( WS )? )* '>'
            {
            match('<'); 

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:62:11: ( WS )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( ((LA2_0 >= '\t' && LA2_0 <= '\n')||LA2_0=='\r'||LA2_0==' ') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:62:11: WS
                    {
                    mWS(); 


                    }
                    break;

            }


            int nameStart224 = getCharIndex();
            int nameStartLine224 = getLine();
            int nameStartCharPos224 = getCharPositionInLine();
            mGENERIC_ID(); 
            name = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, nameStart224, getCharIndex()-1);
            name.setLine(nameStartLine224);
            name.setCharPositionInLine(nameStartCharPos224);


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:62:31: ( WS )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( ((LA3_0 >= '\t' && LA3_0 <= '\n')||LA3_0=='\r'||LA3_0==' ') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:62:31: WS
                    {
                    mWS(); 


                    }
                    break;

            }


             System.out.println("Start Tag: "+name.getText()); 

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:64:9: ( ATTRIBUTE ( WS )? )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==':'||(LA5_0 >= 'A' && LA5_0 <= 'Z')||LA5_0=='_'||(LA5_0 >= 'a' && LA5_0 <= 'z')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:64:11: ATTRIBUTE ( WS )?
            	    {
            	    mATTRIBUTE(); 


            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:64:21: ( WS )?
            	    int alt4=2;
            	    int LA4_0 = input.LA(1);

            	    if ( ((LA4_0 >= '\t' && LA4_0 <= '\n')||LA4_0=='\r'||LA4_0==' ') ) {
            	        alt4=1;
            	    }
            	    switch (alt4) {
            	        case 1 :
            	            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:64:21: WS
            	            {
            	            mWS(); 


            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "START_TAG"

    // $ANTLR start "EMPTY_ELEMENT"
    public final void mEMPTY_ELEMENT() throws RecognitionException {
        try {
            int _type = EMPTY_ELEMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken name=null;

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:68:5: ( '<' ( WS )? name= GENERIC_ID ( WS )? ( ATTRIBUTE ( WS )? )* '/>' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:68:7: '<' ( WS )? name= GENERIC_ID ( WS )? ( ATTRIBUTE ( WS )? )* '/>'
            {
            match('<'); 

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:68:11: ( WS )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( ((LA6_0 >= '\t' && LA6_0 <= '\n')||LA6_0=='\r'||LA6_0==' ') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:68:11: WS
                    {
                    mWS(); 


                    }
                    break;

            }


            int nameStart283 = getCharIndex();
            int nameStartLine283 = getLine();
            int nameStartCharPos283 = getCharPositionInLine();
            mGENERIC_ID(); 
            name = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, nameStart283, getCharIndex()-1);
            name.setLine(nameStartLine283);
            name.setCharPositionInLine(nameStartCharPos283);


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:68:31: ( WS )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( ((LA7_0 >= '\t' && LA7_0 <= '\n')||LA7_0=='\r'||LA7_0==' ') ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:68:31: WS
                    {
                    mWS(); 


                    }
                    break;

            }


             System.out.println("Empty Element: "+name.getText()); 

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:70:9: ( ATTRIBUTE ( WS )? )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==':'||(LA9_0 >= 'A' && LA9_0 <= 'Z')||LA9_0=='_'||(LA9_0 >= 'a' && LA9_0 <= 'z')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:70:11: ATTRIBUTE ( WS )?
            	    {
            	    mATTRIBUTE(); 


            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:70:21: ( WS )?
            	    int alt8=2;
            	    int LA8_0 = input.LA(1);

            	    if ( ((LA8_0 >= '\t' && LA8_0 <= '\n')||LA8_0=='\r'||LA8_0==' ') ) {
            	        alt8=1;
            	    }
            	    switch (alt8) {
            	        case 1 :
            	            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:70:21: WS
            	            {
            	            mWS(); 


            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            match("/>"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "EMPTY_ELEMENT"

    // $ANTLR start "ATTRIBUTE"
    public final void mATTRIBUTE() throws RecognitionException {
        try {
            int _type = ATTRIBUTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            CommonToken name=null;
            CommonToken value=null;

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:74:5: (name= GENERIC_ID ( WS )? '=' ( WS )? value= VALUE )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:74:7: name= GENERIC_ID ( WS )? '=' ( WS )? value= VALUE
            {
            int nameStart337 = getCharIndex();
            int nameStartLine337 = getLine();
            int nameStartCharPos337 = getCharPositionInLine();
            mGENERIC_ID(); 
            name = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, nameStart337, getCharIndex()-1);
            name.setLine(nameStartLine337);
            name.setCharPositionInLine(nameStartCharPos337);


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:74:23: ( WS )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( ((LA10_0 >= '\t' && LA10_0 <= '\n')||LA10_0=='\r'||LA10_0==' ') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:74:23: WS
                    {
                    mWS(); 


                    }
                    break;

            }


            match('='); 

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:74:31: ( WS )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( ((LA11_0 >= '\t' && LA11_0 <= '\n')||LA11_0=='\r'||LA11_0==' ') ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:74:31: WS
                    {
                    mWS(); 


                    }
                    break;

            }


            int valueStart349 = getCharIndex();
            int valueStartLine349 = getLine();
            int valueStartCharPos349 = getCharPositionInLine();
            mVALUE(); 
            value = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, valueStart349, getCharIndex()-1);
            value.setLine(valueStartLine349);
            value.setCharPositionInLine(valueStartCharPos349);


             System.out.println("Attr: "+name.getText()+"="+value.getText()); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ATTRIBUTE"

    // $ANTLR start "END_TAG"
    public final void mEND_TAG() throws RecognitionException {
        try {
            CommonToken name=null;

            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:79:5: ( '</' ( WS )? name= GENERIC_ID ( WS )? '>' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:79:7: '</' ( WS )? name= GENERIC_ID ( WS )? '>'
            {
            match("</"); 



            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:79:12: ( WS )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( ((LA12_0 >= '\t' && LA12_0 <= '\n')||LA12_0=='\r'||LA12_0==' ') ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:79:12: WS
                    {
                    mWS(); 


                    }
                    break;

            }


            int nameStart385 = getCharIndex();
            int nameStartLine385 = getLine();
            int nameStartCharPos385 = getCharPositionInLine();
            mGENERIC_ID(); 
            name = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL, nameStart385, getCharIndex()-1);
            name.setLine(nameStartLine385);
            name.setCharPositionInLine(nameStartCharPos385);


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:79:32: ( WS )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( ((LA13_0 >= '\t' && LA13_0 <= '\n')||LA13_0=='\r'||LA13_0==' ') ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:79:32: WS
                    {
                    mWS(); 


                    }
                    break;

            }


            match('>'); 

             System.out.println("End Tag: "+name.getText()); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "END_TAG"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:85:3: ( '<!--' ( options {greedy=false; } : . )* '-->' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:85:5: '<!--' ( options {greedy=false; } : . )* '-->'
            {
            match("<!--"); 



            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:85:12: ( options {greedy=false; } : . )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0=='-') ) {
                    int LA14_1 = input.LA(2);

                    if ( (LA14_1=='-') ) {
                        int LA14_3 = input.LA(3);

                        if ( (LA14_3=='>') ) {
                            alt14=2;
                        }
                        else if ( ((LA14_3 >= '\u0000' && LA14_3 <= '=')||(LA14_3 >= '?' && LA14_3 <= '\uFFFF')) ) {
                            alt14=1;
                        }


                    }
                    else if ( ((LA14_1 >= '\u0000' && LA14_1 <= ',')||(LA14_1 >= '.' && LA14_1 <= '\uFFFF')) ) {
                        alt14=1;
                    }


                }
                else if ( ((LA14_0 >= '\u0000' && LA14_0 <= ',')||(LA14_0 >= '.' && LA14_0 <= '\uFFFF')) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:85:39: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);


            match("-->"); 



            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "CDATA"
    public final void mCDATA() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:89:3: ( '<![CDATA[' ( options {greedy=false; } : . )* ']]>' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:89:5: '<![CDATA[' ( options {greedy=false; } : . )* ']]>'
            {
            match("<![CDATA["); 



            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:89:17: ( options {greedy=false; } : . )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==']') ) {
                    int LA15_1 = input.LA(2);

                    if ( (LA15_1==']') ) {
                        int LA15_3 = input.LA(3);

                        if ( (LA15_3=='>') ) {
                            alt15=2;
                        }
                        else if ( ((LA15_3 >= '\u0000' && LA15_3 <= '=')||(LA15_3 >= '?' && LA15_3 <= '\uFFFF')) ) {
                            alt15=1;
                        }


                    }
                    else if ( ((LA15_1 >= '\u0000' && LA15_1 <= '\\')||(LA15_1 >= '^' && LA15_1 <= '\uFFFF')) ) {
                        alt15=1;
                    }


                }
                else if ( ((LA15_0 >= '\u0000' && LA15_0 <= '\\')||(LA15_0 >= '^' && LA15_0 <= '\uFFFF')) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:89:44: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);


            match("]]>"); 



            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CDATA"

    // $ANTLR start "PCDATA"
    public final void mPCDATA() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:92:17: ( ( options {greedy=false; } : (~ '<' )+ ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:92:19: ( options {greedy=false; } : (~ '<' )+ )
            {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:92:19: ( options {greedy=false; } : (~ '<' )+ )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:92:46: (~ '<' )+
            {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:92:46: (~ '<' )+
            int cnt16=0;
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( ((LA16_0 >= '\u0000' && LA16_0 <= ';')||(LA16_0 >= '=' && LA16_0 <= '\uFFFF')) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:
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
            	    if ( cnt16 >= 1 ) break loop16;
                        EarlyExitException eee =
                            new EarlyExitException(16, input);
                        throw eee;
                }
                cnt16++;
            } while (true);


            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "PCDATA"

    // $ANTLR start "VALUE"
    public final void mVALUE() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:94:16: ( ( '\\\"' (~ '\\\"' )* '\\\"' | '\\'' (~ '\\'' )* '\\'' ) )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:95:9: ( '\\\"' (~ '\\\"' )* '\\\"' | '\\'' (~ '\\'' )* '\\'' )
            {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:95:9: ( '\\\"' (~ '\\\"' )* '\\\"' | '\\'' (~ '\\'' )* '\\'' )
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0=='\"') ) {
                alt19=1;
            }
            else if ( (LA19_0=='\'') ) {
                alt19=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;

            }
            switch (alt19) {
                case 1 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:95:11: '\\\"' (~ '\\\"' )* '\\\"'
                    {
                    match('\"'); 

                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:95:16: (~ '\\\"' )*
                    loop17:
                    do {
                        int alt17=2;
                        int LA17_0 = input.LA(1);

                        if ( ((LA17_0 >= '\u0000' && LA17_0 <= '!')||(LA17_0 >= '#' && LA17_0 <= '\uFFFF')) ) {
                            alt17=1;
                        }


                        switch (alt17) {
                    	case 1 :
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:
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
                    	    break loop17;
                        }
                    } while (true);


                    match('\"'); 

                    }
                    break;
                case 2 :
                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:96:11: '\\'' (~ '\\'' )* '\\''
                    {
                    match('\''); 

                    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:96:16: (~ '\\'' )*
                    loop18:
                    do {
                        int alt18=2;
                        int LA18_0 = input.LA(1);

                        if ( ((LA18_0 >= '\u0000' && LA18_0 <= '&')||(LA18_0 >= '(' && LA18_0 <= '\uFFFF')) ) {
                            alt18=1;
                        }


                        switch (alt18) {
                    	case 1 :
                    	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:
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
                    	    break loop18;
                        }
                    } while (true);


                    match('\''); 

                    }
                    break;

            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "VALUE"

    // $ANTLR start "GENERIC_ID"
    public final void mGENERIC_ID() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:101:5: ( ( LETTER | '_' | ':' ) ( options {greedy=true; } : LETTER | '0' .. '9' | '.' | '-' | '_' | ':' )* )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:101:7: ( LETTER | '_' | ':' ) ( options {greedy=true; } : LETTER | '0' .. '9' | '.' | '-' | '_' | ':' )*
            {
            if ( input.LA(1)==':'||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:102:9: ( options {greedy=true; } : LETTER | '0' .. '9' | '.' | '-' | '_' | ':' )*
            loop20:
            do {
                int alt20=7;
                switch ( input.LA(1) ) {
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
                    alt20=1;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    {
                    alt20=2;
                    }
                    break;
                case '.':
                    {
                    alt20=3;
                    }
                    break;
                case '-':
                    {
                    alt20=4;
                    }
                    break;
                case '_':
                    {
                    alt20=5;
                    }
                    break;
                case ':':
                    {
                    alt20=6;
                    }
                    break;

                }

                switch (alt20) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:102:36: LETTER
            	    {
            	    mLETTER(); 


            	    }
            	    break;
            	case 2 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:102:45: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;
            	case 3 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:102:56: '.'
            	    {
            	    match('.'); 

            	    }
            	    break;
            	case 4 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:102:62: '-'
            	    {
            	    match('-'); 

            	    }
            	    break;
            	case 5 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:102:68: '_'
            	    {
            	    match('_'); 

            	    }
            	    break;
            	case 6 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:102:74: ':'
            	    {
            	    match(':'); 

            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "GENERIC_ID"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:106:3: ( 'a' .. 'z' | 'A' .. 'Z' )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:
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
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:110:14: ( ( ' ' | '\\t' | ( '\\n' | '\\r\\n' | '\\r' ) )+ )
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:111:9: ( ' ' | '\\t' | ( '\\n' | '\\r\\n' | '\\r' ) )+
            {
            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:111:9: ( ' ' | '\\t' | ( '\\n' | '\\r\\n' | '\\r' ) )+
            int cnt22=0;
            loop22:
            do {
                int alt22=4;
                switch ( input.LA(1) ) {
                case ' ':
                    {
                    alt22=1;
                    }
                    break;
                case '\t':
                    {
                    alt22=2;
                    }
                    break;
                case '\n':
                case '\r':
                    {
                    alt22=3;
                    }
                    break;

                }

                switch (alt22) {
            	case 1 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:111:13: ' '
            	    {
            	    match(' '); 

            	    }
            	    break;
            	case 2 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:112:13: '\\t'
            	    {
            	    match('\t'); 

            	    }
            	    break;
            	case 3 :
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:113:12: ( '\\n' | '\\r\\n' | '\\r' )
            	    {
            	    // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:113:12: ( '\\n' | '\\r\\n' | '\\r' )
            	    int alt21=3;
            	    int LA21_0 = input.LA(1);

            	    if ( (LA21_0=='\n') ) {
            	        alt21=1;
            	    }
            	    else if ( (LA21_0=='\r') ) {
            	        int LA21_2 = input.LA(2);

            	        if ( (LA21_2=='\n') ) {
            	            alt21=2;
            	        }
            	        else {
            	            alt21=3;
            	        }
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 21, 0, input);

            	        throw nvae;

            	    }
            	    switch (alt21) {
            	        case 1 :
            	            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:113:14: '\\n'
            	            {
            	            match('\n'); 

            	            }
            	            break;
            	        case 2 :
            	            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:114:15: '\\r\\n'
            	            {
            	            match("\r\n"); 



            	            }
            	            break;
            	        case 3 :
            	            // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:115:15: '\\r'
            	            {
            	            match('\r'); 

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt22 >= 1 ) break loop22;
                        EarlyExitException eee =
                            new EarlyExitException(22, input);
                        throw eee;
                }
                cnt22++;
            } while (true);


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:1:8: ( ELEMENT | START_TAG | EMPTY_ELEMENT | ATTRIBUTE )
        int alt23=4;
        alt23 = dfa23.predict(input);
        switch (alt23) {
            case 1 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:1:10: ELEMENT
                {
                mELEMENT(); 


                }
                break;
            case 2 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:1:18: START_TAG
                {
                mSTART_TAG(); 


                }
                break;
            case 3 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:1:28: EMPTY_ELEMENT
                {
                mEMPTY_ELEMENT(); 


                }
                break;
            case 4 :
                // /Users/valliant/Projects/java/CFML/com.parser.main/antlr/concept/XML2.g:1:42: ATTRIBUTE
                {
                mATTRIBUTE(); 


                }
                break;

        }

    }


    protected DFA23 dfa23 = new DFA23(this);
    static final String DFA23_eotS =
        "\23\uffff\1\42\54\uffff";
    static final String DFA23_eofS =
        "\100\uffff";
    static final String DFA23_minS =
        "\1\72\1\11\1\uffff\20\11\1\0\1\uffff\15\11\2\uffff\5\11\2\0\13\11"+
        "\1\0\1\11\1\0\7\11";
    static final String DFA23_maxS =
        "\2\172\1\uffff\20\172\1\uffff\1\uffff\12\172\1\47\2\172\2\uffff"+
        "\1\172\4\47\2\uffff\6\172\4\75\1\47\1\uffff\1\172\1\uffff\1\172"+
        "\1\75\5\172";
    static final String DFA23_acceptS =
        "\2\uffff\1\4\21\uffff\1\3\15\uffff\1\2\1\1\34\uffff";
    static final String DFA23_specialS =
        "\23\uffff\1\3\25\uffff\1\0\1\4\13\uffff\1\2\1\uffff\1\1\7\uffff}>";
    static final String[] DFA23_transitionS = {
            "\1\2\1\uffff\1\1\4\uffff\32\2\4\uffff\1\2\1\uffff\32\2",
            "\1\4\1\5\2\uffff\1\6\22\uffff\1\3\31\uffff\1\7\6\uffff\32\7"+
            "\4\uffff\1\7\1\uffff\32\7",
            "",
            "\1\4\1\5\2\uffff\1\6\22\uffff\1\3\31\uffff\1\7\6\uffff\32\7"+
            "\4\uffff\1\7\1\uffff\32\7",
            "\1\4\1\5\2\uffff\1\6\22\uffff\1\3\31\uffff\1\7\6\uffff\32\7"+
            "\4\uffff\1\7\1\uffff\32\7",
            "\1\4\1\5\2\uffff\1\6\22\uffff\1\3\31\uffff\1\7\6\uffff\32\7"+
            "\4\uffff\1\7\1\uffff\32\7",
            "\1\4\1\10\2\uffff\1\6\22\uffff\1\3\31\uffff\1\7\6\uffff\32"+
            "\7\4\uffff\1\7\1\uffff\32\7",
            "\1\20\1\21\2\uffff\1\22\22\uffff\1\17\14\uffff\1\14\1\13\1"+
            "\24\12\12\1\16\3\uffff\1\23\2\uffff\32\11\4\uffff\1\15\1\uffff"+
            "\32\11",
            "\1\4\1\5\2\uffff\1\6\22\uffff\1\3\31\uffff\1\7\6\uffff\32\7"+
            "\4\uffff\1\7\1\uffff\32\7",
            "\1\26\1\27\2\uffff\1\30\22\uffff\1\25\14\uffff\1\35\1\34\1"+
            "\24\12\33\1\36\2\uffff\1\37\1\23\2\uffff\32\31\4\uffff\1\32"+
            "\1\uffff\32\31",
            "\1\20\1\21\2\uffff\1\22\22\uffff\1\17\14\uffff\1\14\1\13\1"+
            "\24\12\12\1\16\3\uffff\1\23\2\uffff\32\11\4\uffff\1\15\1\uffff"+
            "\32\11",
            "\1\20\1\21\2\uffff\1\22\22\uffff\1\17\14\uffff\1\14\1\13\1"+
            "\24\12\12\1\16\3\uffff\1\23\2\uffff\32\11\4\uffff\1\15\1\uffff"+
            "\32\11",
            "\1\20\1\21\2\uffff\1\22\22\uffff\1\17\14\uffff\1\14\1\13\1"+
            "\24\12\12\1\16\3\uffff\1\23\2\uffff\32\11\4\uffff\1\15\1\uffff"+
            "\32\11",
            "\1\26\1\27\2\uffff\1\30\22\uffff\1\25\14\uffff\1\35\1\34\1"+
            "\24\12\33\1\36\2\uffff\1\37\1\23\2\uffff\32\31\4\uffff\1\32"+
            "\1\uffff\32\31",
            "\1\26\1\27\2\uffff\1\30\22\uffff\1\25\14\uffff\1\35\1\34\1"+
            "\24\12\33\1\36\2\uffff\1\37\1\23\2\uffff\32\31\4\uffff\1\32"+
            "\1\uffff\32\31",
            "\1\20\1\21\2\uffff\1\22\22\uffff\1\17\16\uffff\1\24\12\uffff"+
            "\1\40\3\uffff\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\20\1\21\2\uffff\1\22\22\uffff\1\17\16\uffff\1\24\12\uffff"+
            "\1\40\3\uffff\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\20\1\21\2\uffff\1\22\22\uffff\1\17\16\uffff\1\24\12\uffff"+
            "\1\40\3\uffff\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\20\1\41\2\uffff\1\22\22\uffff\1\17\16\uffff\1\24\12\uffff"+
            "\1\40\3\uffff\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\0\43",
            "",
            "\1\26\1\27\2\uffff\1\30\22\uffff\1\25\16\uffff\1\24\12\uffff"+
            "\1\40\2\uffff\1\37\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32"+
            "\40",
            "\1\26\1\27\2\uffff\1\30\22\uffff\1\25\16\uffff\1\24\12\uffff"+
            "\1\40\2\uffff\1\37\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32"+
            "\40",
            "\1\26\1\27\2\uffff\1\30\22\uffff\1\25\16\uffff\1\24\12\uffff"+
            "\1\40\2\uffff\1\37\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32"+
            "\40",
            "\1\26\1\44\2\uffff\1\30\22\uffff\1\25\16\uffff\1\24\12\uffff"+
            "\1\40\2\uffff\1\37\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32"+
            "\40",
            "\1\26\1\27\2\uffff\1\30\22\uffff\1\25\14\uffff\1\35\1\34\1"+
            "\24\12\33\1\36\2\uffff\1\37\1\23\2\uffff\32\31\4\uffff\1\32"+
            "\1\uffff\32\31",
            "\1\26\1\27\2\uffff\1\30\22\uffff\1\25\14\uffff\1\35\1\34\1"+
            "\24\12\33\1\36\2\uffff\1\37\1\23\2\uffff\32\31\4\uffff\1\32"+
            "\1\uffff\32\31",
            "\1\26\1\27\2\uffff\1\30\22\uffff\1\25\14\uffff\1\35\1\34\1"+
            "\24\12\33\1\36\2\uffff\1\37\1\23\2\uffff\32\31\4\uffff\1\32"+
            "\1\uffff\32\31",
            "\1\26\1\27\2\uffff\1\30\22\uffff\1\25\14\uffff\1\35\1\34\1"+
            "\24\12\33\1\36\2\uffff\1\37\1\23\2\uffff\32\31\4\uffff\1\32"+
            "\1\uffff\32\31",
            "\1\26\1\27\2\uffff\1\30\22\uffff\1\25\14\uffff\1\35\1\34\1"+
            "\24\12\33\1\36\2\uffff\1\37\1\23\2\uffff\32\31\4\uffff\1\32"+
            "\1\uffff\32\31",
            "\1\26\1\27\2\uffff\1\30\22\uffff\1\25\14\uffff\1\35\1\34\1"+
            "\24\12\33\1\36\2\uffff\1\37\1\23\2\uffff\32\31\4\uffff\1\32"+
            "\1\uffff\32\31",
            "\1\46\1\47\2\uffff\1\50\22\uffff\1\45\1\uffff\1\51\4\uffff"+
            "\1\52",
            "\1\62\1\63\2\uffff\1\64\22\uffff\1\61\14\uffff\1\56\1\55\1"+
            "\uffff\12\54\1\60\2\uffff\1\37\3\uffff\32\53\4\uffff\1\57\1"+
            "\uffff\32\53",
            "\1\20\1\21\2\uffff\1\22\22\uffff\1\17\16\uffff\1\24\12\uffff"+
            "\1\40\3\uffff\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "",
            "",
            "\1\26\1\27\2\uffff\1\30\22\uffff\1\25\16\uffff\1\24\12\uffff"+
            "\1\40\2\uffff\1\37\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32"+
            "\40",
            "\1\46\1\47\2\uffff\1\50\22\uffff\1\45\1\uffff\1\51\4\uffff"+
            "\1\52",
            "\1\46\1\47\2\uffff\1\50\22\uffff\1\45\1\uffff\1\51\4\uffff"+
            "\1\52",
            "\1\46\1\47\2\uffff\1\50\22\uffff\1\45\1\uffff\1\51\4\uffff"+
            "\1\52",
            "\1\46\1\65\2\uffff\1\50\22\uffff\1\45\1\uffff\1\51\4\uffff"+
            "\1\52",
            "\42\66\1\67\uffdd\66",
            "\47\70\1\71\uffd8\70",
            "\1\62\1\63\2\uffff\1\64\22\uffff\1\61\14\uffff\1\56\1\55\1"+
            "\uffff\12\54\1\60\2\uffff\1\37\3\uffff\32\53\4\uffff\1\57\1"+
            "\uffff\32\53",
            "\1\62\1\63\2\uffff\1\64\22\uffff\1\61\14\uffff\1\56\1\55\1"+
            "\uffff\12\54\1\60\2\uffff\1\37\3\uffff\32\53\4\uffff\1\57\1"+
            "\uffff\32\53",
            "\1\62\1\63\2\uffff\1\64\22\uffff\1\61\14\uffff\1\56\1\55\1"+
            "\uffff\12\54\1\60\2\uffff\1\37\3\uffff\32\53\4\uffff\1\57\1"+
            "\uffff\32\53",
            "\1\62\1\63\2\uffff\1\64\22\uffff\1\61\14\uffff\1\56\1\55\1"+
            "\uffff\12\54\1\60\2\uffff\1\37\3\uffff\32\53\4\uffff\1\57\1"+
            "\uffff\32\53",
            "\1\62\1\63\2\uffff\1\64\22\uffff\1\61\14\uffff\1\56\1\55\1"+
            "\uffff\12\54\1\60\2\uffff\1\37\3\uffff\32\53\4\uffff\1\57\1"+
            "\uffff\32\53",
            "\1\62\1\63\2\uffff\1\64\22\uffff\1\61\14\uffff\1\56\1\55\1"+
            "\uffff\12\54\1\60\2\uffff\1\37\3\uffff\32\53\4\uffff\1\57\1"+
            "\uffff\32\53",
            "\1\62\1\63\2\uffff\1\64\22\uffff\1\61\34\uffff\1\37",
            "\1\62\1\63\2\uffff\1\64\22\uffff\1\61\34\uffff\1\37",
            "\1\62\1\63\2\uffff\1\64\22\uffff\1\61\34\uffff\1\37",
            "\1\62\1\72\2\uffff\1\64\22\uffff\1\61\34\uffff\1\37",
            "\1\46\1\47\2\uffff\1\50\22\uffff\1\45\1\uffff\1\51\4\uffff"+
            "\1\52",
            "\42\66\1\67\uffdd\66",
            "\1\74\1\75\2\uffff\1\76\22\uffff\1\73\16\uffff\1\24\12\uffff"+
            "\1\40\3\uffff\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\47\70\1\71\uffd8\70",
            "\1\74\1\75\2\uffff\1\76\22\uffff\1\73\16\uffff\1\24\12\uffff"+
            "\1\40\3\uffff\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\62\1\63\2\uffff\1\64\22\uffff\1\61\34\uffff\1\37",
            "\1\74\1\75\2\uffff\1\76\22\uffff\1\73\16\uffff\1\24\12\uffff"+
            "\1\40\3\uffff\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\74\1\75\2\uffff\1\76\22\uffff\1\73\16\uffff\1\24\12\uffff"+
            "\1\40\3\uffff\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\74\1\75\2\uffff\1\76\22\uffff\1\73\16\uffff\1\24\12\uffff"+
            "\1\40\3\uffff\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\74\1\77\2\uffff\1\76\22\uffff\1\73\16\uffff\1\24\12\uffff"+
            "\1\40\3\uffff\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\74\1\75\2\uffff\1\76\22\uffff\1\73\16\uffff\1\24\12\uffff"+
            "\1\40\3\uffff\1\23\2\uffff\32\40\4\uffff\1\40\1\uffff\32\40"
    };

    static final short[] DFA23_eot = DFA.unpackEncodedString(DFA23_eotS);
    static final short[] DFA23_eof = DFA.unpackEncodedString(DFA23_eofS);
    static final char[] DFA23_min = DFA.unpackEncodedStringToUnsignedChars(DFA23_minS);
    static final char[] DFA23_max = DFA.unpackEncodedStringToUnsignedChars(DFA23_maxS);
    static final short[] DFA23_accept = DFA.unpackEncodedString(DFA23_acceptS);
    static final short[] DFA23_special = DFA.unpackEncodedString(DFA23_specialS);
    static final short[][] DFA23_transition;

    static {
        int numStates = DFA23_transitionS.length;
        DFA23_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA23_transition[i] = DFA.unpackEncodedString(DFA23_transitionS[i]);
        }
    }

    class DFA23 extends DFA {

        public DFA23(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 23;
            this.eot = DFA23_eot;
            this.eof = DFA23_eof;
            this.min = DFA23_min;
            this.max = DFA23_max;
            this.accept = DFA23_accept;
            this.special = DFA23_special;
            this.transition = DFA23_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( ELEMENT | START_TAG | EMPTY_ELEMENT | ATTRIBUTE );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA23_41 = input.LA(1);

                        s = -1;
                        if ( ((LA23_41 >= '\u0000' && LA23_41 <= '!')||(LA23_41 >= '#' && LA23_41 <= '\uFFFF')) ) {s = 54;}

                        else if ( (LA23_41=='\"') ) {s = 55;}

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA23_56 = input.LA(1);

                        s = -1;
                        if ( (LA23_56=='\'') ) {s = 57;}

                        else if ( ((LA23_56 >= '\u0000' && LA23_56 <= '&')||(LA23_56 >= '(' && LA23_56 <= '\uFFFF')) ) {s = 56;}

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA23_54 = input.LA(1);

                        s = -1;
                        if ( (LA23_54=='\"') ) {s = 55;}

                        else if ( ((LA23_54 >= '\u0000' && LA23_54 <= '!')||(LA23_54 >= '#' && LA23_54 <= '\uFFFF')) ) {s = 54;}

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA23_19 = input.LA(1);

                        s = -1;
                        if ( ((LA23_19 >= '\u0000' && LA23_19 <= '\uFFFF')) ) {s = 35;}

                        else s = 34;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA23_42 = input.LA(1);

                        s = -1;
                        if ( ((LA23_42 >= '\u0000' && LA23_42 <= '&')||(LA23_42 >= '(' && LA23_42 <= '\uFFFF')) ) {s = 56;}

                        else if ( (LA23_42=='\'') ) {s = 57;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 23, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

}