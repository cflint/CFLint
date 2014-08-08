/*
 [The "BSD licence"]
 Copyright (c) 2005-2007 Terence Parr
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/** ANTLR v3 grammar written in ANTLR v3 with AST construction */

/*
 Modified by Yang Jiang (yang.jiang.z@gmail.com) 
 Nov. 30, 2008.
  
 This file is originally to taken from antlr.org, http://www.antlr.org/grammar/ANTLR/ANTLRv3.g .
 Then I stripped off all the tree construction code.
 And change it to a rewrite grammar so it takes the Java.g file and adds tree construction code
 to it. 
 */
     

grammar ANTLRv3;

options {output=template; rewrite=true;}

tokens {
	DOC_COMMENT;
	PARSER;	
    LEXER;
    RULE;
    BLOCK;
    OPTIONAL;
    CLOSURE;
    POSITIVE_CLOSURE;
    SYNPRED;
    RANGE;
    CHAR_RANGE;
    EPSILON;
    ALT;
    EOR;
    EOB;
    EOA; // end of alt
    ID;
    ARG;
    ARGLIST;
    RET;
    LEXER_GRAMMAR;
    PARSER_GRAMMAR;
    TREE_GRAMMAR;
    COMBINED_GRAMMAR;
    INITACTION;
    LABEL; // $x used in rewrite rules
    TEMPLATE;
    SCOPE='scope';
    SEMPRED;
    GATED_SEMPRED; // {p}? =>
    SYN_SEMPRED; // (...) =>   it's a manually-specified synpred converted to sempred
    BACKTRACK_SEMPRED; // auto backtracking mode syn pred converted to sempred
    FRAGMENT='fragment';
    TREE_BEGIN='^(';
    ROOT='^';
    BANG='!';
    RANGE='..';
    REWRITE='->';
}

@parser::header {
package treetool;
}

@lexer::header {
package treetool;

}


@members{
    int count = 0;
}

grammarDef
    :   DOC_COMMENT?
    	(	'lexer'     // pure lexer
    	|   'parser'    // pure parser
    	|   'tree'      // a tree parser
    	|  // merged parser/lexer
    	) 'grammar' id ';' optionsSpec? tokensSpec? attrScope* action*
    	rule+
    	EOF
    ;

tokensSpec
	:	TOKENS tokenSpec+ '}'
	;

tokenSpec
	:	TOKEN_REF
		(	'=' ( STRING_LITERAL| CHAR_LITERAL)
		|
		)
		';'
	;

attrScope
	:	'scope' id ACTION
	;

/** Match stuff like @parser::members {int i;} */
action
	:	'@' (actionScopeName '::')? id ACTION
	;

/** Sometimes the scope names will collide with keywords; allow them as
 *  ids for action scopes.
 */
actionScopeName
	:	id
	| 'lexer'
    | 'parser'
	;

optionsSpec
	:	OPTIONS (option ';')+ '}'
	;

option
    :   id '=' optionValue
 	;
 	
optionValue
    :   id
    |   STRING_LITERAL
    |   CHAR_LITERAL
    |   INT
    | '*'  // used for k=*
    ;

rule
	:	DOC_COMMENT?
		( ('protected'|'public'|'private'|'fragment') )?
		id 	
		'!'?
		( ARG_ACTION )?
		( 'returns' ARG_ACTION  )?
		throwsSpec? optionsSpec? ruleScopeSpec? ruleAction*
		':' 	
		altList	';'
		exceptionGroup?
	;

/** Match stuff like @init {int i;} */
ruleAction
	:	'@' id ACTION
	;

throwsSpec
	:	'throws' id ( ',' id )*
	;

ruleScopeSpec
	:	'scope' ACTION
	|	'scope' id (',' id)* ';'
	|	'scope' ACTION
		'scope' id (',' id)* ';'
	;

block
    : '('
		( ( optionsSpec)? ':' )? alternative rewrite ( '|' alternative rewrite )* ')'
    ;

altList
    : alternative rewrite ( '|' alternative rewrite )*
    ;

alternative
    :   element+
    |
    ;

exceptionGroup
	:	( exceptionHandler )+ ( finallyClause )?
	|	finallyClause
    ;

exceptionHandler
    :    'catch' ARG_ACTION ACTION
    ;

finallyClause
    :    'finally' ACTION
    ;

element
	:	elementNoOptionSpec
	;

elementNoOptionSpec
	:	id ( '='| '+=') atom
		(	ebnfSuffix
		|
		)
	|	id ( '='| '+=') block
		(	ebnfSuffix
		|
		)
	|	atom
		(	ebnfSuffix
		|
		)
	|	ebnf
	|   ACTION
	|   SEMPRED ( '=>' | )
	|   treeSpec
		(	ebnfSuffix
		|
		)
	;

atom:   range ( ( '^'| '!') | )
    |   terminal
    |	notSet ( ( '^'| '!') | )
    |   RULE_REF
          {
            String tagName = "t"+count++;
            String content =
            "{T.pushTop();T.setCurrentParent(T.addNode(\""+$RULE_REF.text+"\"));} "+
            tagName + "=" +$RULE_REF.text+" "+
            "{T.popTop().setTextRange(\$"+tagName+".start, \$"+tagName+".stop);}";
            ((TokenRewriteStream) input).replace($RULE_REF.getTokenIndex(), $RULE_REF.getTokenIndex(), new StringTemplate(templateLib, content));
           }                
        ( ARG_ACTION )? ( ( '^'| '!') )?
    ;

notSet
	:	'~'
		(	notTerminal
		|	block
		)
	;

treeSpec
	:	'^(' element ( element )+ ')'
	;

/** Matches ENBF blocks (and token sets via block rule) */
ebnf
	:	block
		( '?'
		| '*'
		| '+'
		|   '=>'
        |
		)
	;

range
	: CHAR_LITERAL RANGE CHAR_LITERAL
	;

terminal
    :   (	CHAR_LITERAL
          {                    
            String s = $CHAR_LITERAL.text;
            s = s.substring(1,s.length());
            s = s.substring(0,s.length()-1);
            //s = s.replace("\%",("\\"+"\%"));
            //s = s.replace("'","\\'");
            s = s.replace("<", "\\<");
            String escapedName = "'"+s+"'";
            String content = 
            "("+escapedName + "{T.addLeaf(\"'"+s.replace("\%",("\\"+"\%"))+"'\",input.LT(-1));})";
            
            ((TokenRewriteStream) input).replace($CHAR_LITERAL.getTokenIndex(), $CHAR_LITERAL.getTokenIndex(), new StringTemplate(templateLib, content));
           }    
		|   TOKEN_REF
            {
                String name = $TOKEN_REF.text;
                String content =
                name + " {T.addLeaf(\""+name+"['\"+input.LT(-1).getText()+\"']\",input.LT(-1));}";
                ((TokenRewriteStream) input).replace($TOKEN_REF.getTokenIndex(), $TOKEN_REF.getTokenIndex(), new StringTemplate(templateLib, content));
            }		
			( ARG_ACTION
			|
			)
		|   STRING_LITERAL   
          {
            String s = $STRING_LITERAL.text;
            s = s.replace("\%",("\\"+"\%"));
            String content =
            $STRING_LITERAL.text + "{T.addLeaf(\""+s+"\",input.LT(-1));}";
            ((TokenRewriteStream) input).replace($STRING_LITERAL.getTokenIndex(), $STRING_LITERAL.getTokenIndex(), new StringTemplate(templateLib, content));
           }		
		|   '.'
		)	
		(	'^'
		|	'!'
		)?
	;

notTerminal
	:   CHAR_LITERAL
	|	TOKEN_REF
	|	STRING_LITERAL
	;
	
ebnfSuffix
	:	'?'
  	|	'*'
   	|	'+'
	;
	


// R E W R I T E  S Y N T A X

rewrite
	:	( '->' SEMPRED rewrite_alternative)* '->' rewrite_alternative
	|
	;

rewrite_alternative
	:	rewrite_template
	|	rewrite_tree_alternative
   	|
	;
	
rewrite_tree_block
    : '(' rewrite_tree_alternative ')'
    ;

rewrite_tree_alternative
    :	rewrite_tree_element+
    ;

rewrite_tree_element
	:	rewrite_tree_atom
	|	rewrite_tree_atom ebnfSuffix
	|   rewrite_tree
		(	ebnfSuffix
		|
		)
	|   rewrite_tree_ebnf
	;

rewrite_tree_atom
    :   CHAR_LITERAL
	|   TOKEN_REF ARG_ACTION? // for imaginary nodes
    |   RULE_REF
	|   STRING_LITERAL
	| '$' id // reference to a label in a rewrite rule
	|	ACTION
	;

rewrite_tree_ebnf
	:	rewrite_tree_block ebnfSuffix
	;
	
rewrite_tree
	:	'^(' rewrite_tree_atom rewrite_tree_element* ')'
	;

/** Build a tree for a template rewrite:
      ^(TEMPLATE (ID|ACTION) ^(ARGLIST ^(ARG ID ACTION) ...) )
    where ARGLIST is always there even if no args exist.
    ID can be "template" keyword.  If first child is ACTION then it's
    an indirect template ref

    -> foo(a={...}, b={...})
    -> ({string-e})(a={...}, b={...})  // e evaluates to template name
    -> {%{$ID.text}} // create literal template from string (done in ActionTranslator)
	-> {st-expr} // st-expr evaluates to ST
 */
rewrite_template
	:   // -> template(a={...},...) "..."    inline template
		id '(' rewrite_template_args	')'
		( DOUBLE_QUOTE_STRING_LITERAL | DOUBLE_ANGLE_STRING_LITERAL )

	|	// -> foo(a={...}, ...)
		rewrite_template_ref

	|	// -> ({expr})(a={...}, ...)
		rewrite_indirect_template_head

	|	// -> {...}
		ACTION
	;

/** -> foo(a={...}, ...) */
rewrite_template_ref
	:	id '(' rewrite_template_args	')'
	;

/** -> ({expr})(a={...}, ...) */
rewrite_indirect_template_head
	: '(' ACTION ')' '(' rewrite_template_args ')'
	;

rewrite_template_args
	:	rewrite_template_arg (',' rewrite_template_arg)*
	|
	;

rewrite_template_arg
	:   id '=' ACTION
	;

id	:	TOKEN_REF
	|	RULE_REF
	;

// L E X I C A L   R U L E S

SL_COMMENT
 	:	'//'
 	 	(	' $ANTLR ' SRC // src directive
 		|	~('\r'|'\n')*
		)
		'\r'? '\n'
		{$channel=HIDDEN;}
	;

ML_COMMENT
	:	'/*' {if (input.LA(1)=='*') $type=DOC_COMMENT; else $channel=HIDDEN;} .* '*/'
	;

CHAR_LITERAL
	:	'\'' LITERAL_CHAR '\''
	;

STRING_LITERAL
	:	'\'' LITERAL_CHAR LITERAL_CHAR* '\''
	;

fragment
LITERAL_CHAR
	:	ESC
	|	~('\''|'\\')
	;

DOUBLE_QUOTE_STRING_LITERAL
	:	'"' (ESC | ~('\\'|'"'))* '"'
	;

DOUBLE_ANGLE_STRING_LITERAL
	:	'<<' .* '>>'
	;

fragment
ESC	:	'\\'
		(	'n'
		|	'r'
		|	't'
		|	'b'
		|	'f'
		|	'"'
		|	'\''
		|	'\\'
		|	'>'
		|	'u' XDIGIT XDIGIT XDIGIT XDIGIT
		|	. // unknown, leave as it is
		)
	;

fragment
XDIGIT :
		'0' .. '9'
	|	'a' .. 'f'
	|	'A' .. 'F'
	;

INT	:	'0'..'9'+
	;

ARG_ACTION
	:	NESTED_ARG_ACTION
	;

fragment
NESTED_ARG_ACTION :
	'['
	(	options {greedy=false; k=1;}
	:	NESTED_ARG_ACTION
	|	ACTION_STRING_LITERAL
	|	ACTION_CHAR_LITERAL
	|	.
	)*
	']'
	{setText(getText().substring(1, getText().length()-1));}
	;

ACTION
	:	NESTED_ACTION ( '?' {$type = SEMPRED;} )?
	;

fragment
NESTED_ACTION :
	'{'
	(	options {greedy=false; k=2;}
	:	NESTED_ACTION
	|	SL_COMMENT
	|	ML_COMMENT
	|	ACTION_STRING_LITERAL
	|	ACTION_CHAR_LITERAL
	|	.
	)*
	'}'
   ;

fragment
ACTION_CHAR_LITERAL
	:	'\'' (ACTION_ESC|~('\\'|'\'')) '\''
	;

fragment
ACTION_STRING_LITERAL
	:	'"' (ACTION_ESC|~('\\'|'"'))* '"'
	;

fragment
ACTION_ESC
	:	'\\\''
	|	'\\' '"' // ANTLR doesn't like: '\\"'
	|	'\\' ~('\''|'"')
	;

TOKEN_REF
	:	'A'..'Z' ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*
	;

RULE_REF
	:	'a'..'z' ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*
	;

/** Match the start of an options section.  Don't allow normal
 *  action processing on the {...} as it's not a action.
 */
OPTIONS
	:	'options' WS_LOOP '{'
	;
	
TOKENS
	:	'tokens' WS_LOOP '{'
	;

/** Reset the file and line information; useful when the grammar
 *  has been generated so that errors are shown relative to the
 *  original file like the old C preprocessor used to do.
 */
fragment
SRC	:	'src' ' ' ACTION_STRING_LITERAL ' ' INT
	;

WS	:	(	' '
		|	'\t'
		|	'\r'? '\n'
		)+
		{$channel=HIDDEN;}
	;

fragment
WS_LOOP
	:	(	WS
		|	SL_COMMENT
		|	ML_COMMENT
		)*
	;


