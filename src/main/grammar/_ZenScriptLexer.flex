package com.warmthdawn.zenscript.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.warmthdawn.zenscript.psi.ZenScriptTypes.*;

%%

%{
  public _ZenScriptLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _ZenScriptLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

ID=[a-zA-Z_][a-zA-Z0-9_]*
STRING_LITERAL=('([^'\\]|\\.)*'|\"([^\"\\]|\\\"|\\'|\\)*\")
INT_LITERAL=(0|([1-9][0-9]*))
WHITE_SPACE=[ \r\t\f\n]+

%%
<YYINITIAL> {
  {WHITE_SPACE}         { return WHITE_SPACE; }

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
  "LINE_COMMENT"        { return LINE_COMMENT; }
  "BLOCK_COMMENT"       { return BLOCK_COMMENT; }
  "DOC_COMMENT"         { return DOC_COMMENT; }
  "PREPROCESSOR"        { return PREPROCESSOR; }
  "LONG_LITERAL"        { return LONG_LITERAL; }
  "FLOAT_LITERAL"       { return FLOAT_LITERAL; }
  "DOUBLE_LITERAL"      { return DOUBLE_LITERAL; }
  "ANY"                 { return ANY; }
  "BYTE"                { return BYTE; }
  "SHORT"               { return SHORT; }
  "INT"                 { return INT; }
  "LONG"                { return LONG; }
  "FLOAT"               { return FLOAT; }
  "DOUBLE"              { return DOUBLE; }
  "BOOL"                { return BOOL; }
  "VOID"                { return VOID; }
  "STRING"              { return STRING; }

  {ID}                  { return ID; }
  {STRING_LITERAL}      { return STRING_LITERAL; }
  {INT_LITERAL}         { return INT_LITERAL; }
  {WHITE_SPACE}         { return WHITE_SPACE; }

}

[^] { return BAD_CHARACTER; }
