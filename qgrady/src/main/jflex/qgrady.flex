/*
 * Usercode Section
 */

package com.aidanogrady.qgrady.syntax;

import java_cup.runtime.*;


%%

/******************************************************************************/

/*
 * Options/Declarations
 */

/* The name of the class JFlex will create. */
%class Lexer

/* The current line and column. */
%line
%column

/* CUP compatibility mode */
%cup

/* Make public */
%public

/* Declarations */
%{

    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

/* Macro Declarations */
LineTerm    = \r|\n|\r\n
Space       = {LineTerm} | [ \t\f]
Digit       = [0-9]
Integer     = {Digit}+
Decimal     = ([0-9]*\.)?[0-9]+ | {Integer}
String      = [a-zA-Z$_] [a-zA-Z0-9$_]*

%%

/******************************************************************************/

/*
 * Lexical Rules
 */

<YYINITIAL> {

    "->"                { return symbol(sym.ARROW);         }
    ","                 { return symbol(sym.COMMA);         }
    "="                 { return symbol(sym.EQUALS);        }
    "input"             { return symbol(sym.INPUT);         }
    "["                 { return symbol(sym.LBRACKET);      }
    "output"            { return symbol(sym.OUTPUT);        }
    "range"             { return symbol(sym.RANGE);         }
    "]"                 { return symbol(sym.RBRACKET);      }
    ";"                 { return symbol(sym.SEMICOLON);     }


    {String}    { return symbol(sym.VAR, yytext());                 }
    {Integer}   { return symbol(sym.INT, new Integer(yytext()));    }
    {Decimal}   { return symbol(sym.NUM, new Double(yytext()));     }

    /* Don't do anything if whitespace is found */
    {Space} { /* just skip what was found, do nothing */ }
}


/* No token was found for the input so through an error.  Print out an
   Illegal character message with the illegal character that was found. */
[^]         { throw new Error("Illegal character <"+yytext()+">"); }
