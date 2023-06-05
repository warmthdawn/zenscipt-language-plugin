// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.warmthdawn.zenscript.lexer;

import com.intellij.psi.tree.IElementType;
import com.intellij.lexer.FlexLexer;
import static com.warmthdawn.zenscript.psi.ZenScriptTypes.*;
import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;

%%

%{
  public ZenScriptLexer() {
    this((java.io.Reader)null);
  }
%}


%class ZenScriptLexer
%implements FlexLexer
%unicode
%public
%function advance
%type IElementType

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*
Preprocessor           = "#" {InputCharacter}* {LineTerminator}?


STR =      "\""
STR2 =      "'"
String = {STR} ( [^\"\\\n\r] | "\\" ("\\" | {STR} | {ESCAPES} | [0-8xuU] ) )* {STR}?
String2 = {STR2} ( [^\"\\\n\r] | "\\" ("\\" | {STR2} | {ESCAPES} | [0-8xuU] ) )* {STR2}?
ESCAPES = [abfnrtv]

Digit = [0-9]
Digits = {Digit}+


HexDigits = {HexDigit}+
HexDigit = [0-9a-fA-F]

LetterOrDigit = {Letter} | [0-9]

Letter = [a-zA-Z_]


Identifier = {Letter} {LetterOrDigit}*

%%

<YYINITIAL> {
  "var"                 { return K_VAR; }
  "val"                 { return K_VAL; }
  "global"              { return K_GLOBAL; }
  "static"              { return K_STATIC; }
  "import"              { return K_IMPORT; }
  "function"            { return K_FUNCTION; }
  "as"                  { return K_AS; }
  "to"                  { return K_TO; }
  "in"                  { return K_IN; }
  "has"                 { return K_HAS; }
  "instanceof"          { return K_INSTANCEOF; }
  "this"                { return K_THIS; }
  "super"               { return K_SUPER; }
  "any"                 { return K_ANY; }
  "byte"                { return K_BYTE; }
  "short"               { return K_SHORT; }
  "int"                 { return K_INT; }
  "long"                { return K_LONG; }
  "float"               { return K_FLOAT; }
  "double"              { return K_DOUBLE; }
  "bool"                { return K_BOOL; }
  "void"                { return K_VOID; }
  "string"              { return K_STRING; }
  "true"                { return K_TRUE; }
  "false"               { return K_FALSE; }
  "null"                { return K_NULL; }
  "if"                  { return K_IF; }
  "else"                { return K_ELSE; }
  "for"                 { return K_FOR; }
  "do"                  { return K_DO; }
  "while"               { return K_WHILE; }
  "break"               { return K_BREAK; }
  "continue"            { return K_CONTINUE; }
  "return"              { return K_RETURN; }
  "zenClass"            { return K_ZEN_CLASS; }
  "zenConstructor"      { return K_ZEN_CONSTRUCTOR; }
  "$expand"             { return K_EXPAND; }
  "("                   { return PAREN_OPEN; }
  ")"                   { return PAREN_CLOSE; }
  "["                   { return BRACK_OPEN; }
  "]"                   { return BRACK_CLOSE; }
  "{"                   { return BRACE_OPEN; }
  "}"                   { return BRACE_CLOSE; }
  ","                   { return COMMA; }
  "."                   { return DOT; }
  ";"                   { return SEMICOLON; }
  "+"                   { return OP_ADD; }
  "-"                   { return OP_SUB; }
  "*"                   { return OP_MUL; }
  "/"                   { return OP_DIV; }
  "%"                   { return OP_MOD; }
  "~"                   { return OP_CAT; }
  "!"                   { return OP_NOT; }
  "<"                   { return OP_LESS; }
  ">"                   { return OP_GREATER; }
  "^"                   { return OP_XOR; }
  ":"                   { return OP_COLON; }
  "?"                   { return OP_QUEST; }
  "`"                   { return OP_BACKTICK; }
  "$"                   { return OP_DOLLAR; }
  "&"                   { return OP_AND; }
  "|"                   { return OP_OR; }
  "="                   { return OP_ASSIGN; }
  "&&"                  { return OP_AND_AND; }
  "||"                  { return OP_OR_OR; }
  "=="                  { return OP_EQUAL; }
  "!="                  { return OP_NOT_EQUAL; }
  "<="                  { return OP_LESS_EQUAL; }
  ">="                  { return OP_GREATER_EQUAL; }
  "+="                  { return OP_ADD_ASSIGN; }
  "-="                  { return OP_SUB_ASSIGN; }
  "*="                  { return OP_MUL_ASSIGN; }
  "/="                  { return OP_DIV_ASSIGN; }
  "%="                  { return OP_MOD_ASSIGN; }
  "^="                  { return OP_XOR_ASSIGN; }
  "&="                  { return OP_AND_ASSIGN; }
  "|="                  { return OP_OR_ASSIGN; }
  "~="                  { return OP_CAT_ASSIGN; }
  ".."                  { return OP_DOT_DOT; }

  {Identifier}          { return ID; }
  {String}              { return STRING_LITERAL;}
  {String2}             { return STRING_LITERAL;}


  {EndOfLineComment}             { return LINE_COMMENT;}
  {DocumentationComment}         { return DOC_COMMENT;}
  {TraditionalComment}           { return BLOCK_COMMENT;}
  {Preprocessor}                 { return PREPROCESSOR;}

  (0 | ([1-9] {Digit}*))                           { return INT_LITERAL; }
  0 [xX] {HexDigits}                           { return INT_LITERAL; }
  (0 | ([1-9] {Digit}*)) [lL]                   { return LONG_LITERAL; }
  0 [xX] {HexDigits} [lL]                     { return LONG_LITERAL; }
  (0 | ([1-9] {Digit}*)) '.' {Digits} ([eE] {Digits})? [fF]    { return FLOAT_LITERAL; }
  (0 | ([1-9] {Digit}*)) '.' {Digits} ([eE] {Digits})? [dD]?   { return DOUBLE_LITERAL; }

  {LineTerminator}        { return NEW_LINE; }
  {WhiteSpace}            { return WHITE_SPACE; }
}


[^] { return BAD_CHARACTER; }

