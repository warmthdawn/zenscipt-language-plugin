// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.warmthdawn.zenscript.psi.impl.*;

public interface ZenScriptTypes {

  IElementType ADD_EXPRESSION = new ZenScriptElementType("ADD_EXPRESSION");
  IElementType ALIAS = new ZenScriptElementType("ALIAS");
  IElementType AND_AND_EXPRESSION = new ZenScriptElementType("AND_AND_EXPRESSION");
  IElementType AND_EXPRESSION = new ZenScriptElementType("AND_EXPRESSION");
  IElementType ARGUMENTS = new ZenScriptElementType("ARGUMENTS");
  IElementType ARRAY_SUFFIX = new ZenScriptElementType("ARRAY_SUFFIX");
  IElementType ASSIGNMENT_EXPRESSION = new ZenScriptElementType("ASSIGNMENT_EXPRESSION");
  IElementType BLOCK_STATEMENT = new ZenScriptElementType("BLOCK_STATEMENT");
  IElementType BRACKET_HANDLER = new ZenScriptElementType("BRACKET_HANDLER");
  IElementType BREAK_STATEMENT = new ZenScriptElementType("BREAK_STATEMENT");
  IElementType CLASS_DECLARATION = new ZenScriptElementType("CLASS_DECLARATION");
  IElementType COMPARE_EXPRESSION = new ZenScriptElementType("COMPARE_EXPRESSION");
  IElementType CONDITIONAL_EXPRESSION = new ZenScriptElementType("CONDITIONAL_EXPRESSION");
  IElementType CONSTRUCTOR_BODY = new ZenScriptElementType("CONSTRUCTOR_BODY");
  IElementType CONSTRUCTOR_DECLARATION = new ZenScriptElementType("CONSTRUCTOR_DECLARATION");
  IElementType CONTINUE_STATEMENT = new ZenScriptElementType("CONTINUE_STATEMENT");
  IElementType DEFAULT_VALUE = new ZenScriptElementType("DEFAULT_VALUE");
  IElementType ELSE_BODY = new ZenScriptElementType("ELSE_BODY");
  IElementType EXPAND_FUNCTION_DECLARATION = new ZenScriptElementType("EXPAND_FUNCTION_DECLARATION");
  IElementType EXPRESSION = new ZenScriptElementType("EXPRESSION");
  IElementType EXPRESSION_STATEMENT = new ZenScriptElementType("EXPRESSION_STATEMENT");
  IElementType FOREACH_BODY = new ZenScriptElementType("FOREACH_BODY");
  IElementType FOREACH_STATEMENT = new ZenScriptElementType("FOREACH_STATEMENT");
  IElementType FUNCTION_BODY = new ZenScriptElementType("FUNCTION_BODY");
  IElementType FUNCTION_DECLARATION = new ZenScriptElementType("FUNCTION_DECLARATION");
  IElementType IDENTIFIER = new ZenScriptElementType("IDENTIFIER");
  IElementType IF_STATEMENT = new ZenScriptElementType("IF_STATEMENT");
  IElementType IMPORT_DECLARATION = new ZenScriptElementType("IMPORT_DECLARATION");
  IElementType IMPORT_LIST = new ZenScriptElementType("IMPORT_LIST");
  IElementType INITIALIZER = new ZenScriptElementType("INITIALIZER");
  IElementType MAP_ENTRY = new ZenScriptElementType("MAP_ENTRY");
  IElementType MUL_EXPRESSION = new ZenScriptElementType("MUL_EXPRESSION");
  IElementType OR_EXPRESSION = new ZenScriptElementType("OR_EXPRESSION");
  IElementType OR_OR_EXPRESSION = new ZenScriptElementType("OR_OR_EXPRESSION");
  IElementType PARAMETER = new ZenScriptElementType("PARAMETER");
  IElementType POSTFIX_EXPRESSION = new ZenScriptElementType("POSTFIX_EXPRESSION");
  IElementType PRIMARY_EXPRESSION = new ZenScriptElementType("PRIMARY_EXPRESSION");
  IElementType PRIMITIVE_TYPE = new ZenScriptElementType("PRIMITIVE_TYPE");
  IElementType QUALIFIED_NAME = new ZenScriptElementType("QUALIFIED_NAME");
  IElementType QUALIFIER = new ZenScriptElementType("QUALIFIER");
  IElementType RETURN_STATEMENT = new ZenScriptElementType("RETURN_STATEMENT");
  IElementType SCRIPT_BODY = new ZenScriptElementType("SCRIPT_BODY");
  IElementType SIMPLE_VARIABLE = new ZenScriptElementType("SIMPLE_VARIABLE");
  IElementType STATEMENT = new ZenScriptElementType("STATEMENT");
  IElementType THEN_BODY = new ZenScriptElementType("THEN_BODY");
  IElementType TYPE_LITERAL = new ZenScriptElementType("TYPE_LITERAL");
  IElementType UNARY_EXPRESSION = new ZenScriptElementType("UNARY_EXPRESSION");
  IElementType VARIABLE_DECLARATION = new ZenScriptElementType("VARIABLE_DECLARATION");
  IElementType WHILE_STATEMENT = new ZenScriptElementType("WHILE_STATEMENT");
  IElementType XOR_EXPRESSION = new ZenScriptElementType("XOR_EXPRESSION");

  IElementType ANY = new ZenScriptTokenType("ANY");
  IElementType BOOL = new ZenScriptTokenType("BOOL");
  IElementType BRACE_CLOSE = new ZenScriptTokenType("}");
  IElementType BRACE_OPEN = new ZenScriptTokenType("{");
  IElementType BRACK_CLOSE = new ZenScriptTokenType("]");
  IElementType BRACK_OPEN = new ZenScriptTokenType("[");
  IElementType BYTE = new ZenScriptTokenType("BYTE");
  IElementType COMMA = new ZenScriptTokenType(",");
  IElementType COMMENT = new ZenScriptTokenType("COMMENT");
  IElementType DOT = new ZenScriptTokenType(".");
  IElementType DOUBLE = new ZenScriptTokenType("DOUBLE");
  IElementType DOUBLE_LITERAL = new ZenScriptTokenType("DOUBLE_LITERAL");
  IElementType EOF = new ZenScriptTokenType("<<EOF>>");
  IElementType FLOAT = new ZenScriptTokenType("FLOAT");
  IElementType FLOAT_LITERAL = new ZenScriptTokenType("FLOAT_LITERAL");
  IElementType ID = new ZenScriptTokenType("ID");
  IElementType INT = new ZenScriptTokenType("INT");
  IElementType INT_LITERAL = new ZenScriptTokenType("INT_LITERAL");
  IElementType K_ANY = new ZenScriptTokenType("any");
  IElementType K_AS = new ZenScriptTokenType("as");
  IElementType K_BOOL = new ZenScriptTokenType("bool");
  IElementType K_BREAK = new ZenScriptTokenType("break");
  IElementType K_BYTE = new ZenScriptTokenType("byte");
  IElementType K_CONTINUE = new ZenScriptTokenType("continue");
  IElementType K_DO = new ZenScriptTokenType("do");
  IElementType K_DOUBLE = new ZenScriptTokenType("double");
  IElementType K_ELSE = new ZenScriptTokenType("else");
  IElementType K_EXPAND = new ZenScriptTokenType("$expand");
  IElementType K_FALSE = new ZenScriptTokenType("false");
  IElementType K_FLOAT = new ZenScriptTokenType("float");
  IElementType K_FOR = new ZenScriptTokenType("for");
  IElementType K_FUNCTION = new ZenScriptTokenType("function");
  IElementType K_GLOBAL = new ZenScriptTokenType("global");
  IElementType K_HAS = new ZenScriptTokenType("has");
  IElementType K_IF = new ZenScriptTokenType("if");
  IElementType K_IMPORT = new ZenScriptTokenType("import");
  IElementType K_IN = new ZenScriptTokenType("in");
  IElementType K_INSTANCEOF = new ZenScriptTokenType("instanceof");
  IElementType K_INT = new ZenScriptTokenType("int");
  IElementType K_LONG = new ZenScriptTokenType("long");
  IElementType K_NULL = new ZenScriptTokenType("null");
  IElementType K_RETURN = new ZenScriptTokenType("return");
  IElementType K_SHORT = new ZenScriptTokenType("short");
  IElementType K_STATIC = new ZenScriptTokenType("static");
  IElementType K_STRING = new ZenScriptTokenType("string");
  IElementType K_SUPER = new ZenScriptTokenType("super");
  IElementType K_THIS = new ZenScriptTokenType("this");
  IElementType K_TO = new ZenScriptTokenType("to");
  IElementType K_TRUE = new ZenScriptTokenType("true");
  IElementType K_VAL = new ZenScriptTokenType("val");
  IElementType K_VAR = new ZenScriptTokenType("var");
  IElementType K_VOID = new ZenScriptTokenType("void");
  IElementType K_WHILE = new ZenScriptTokenType("while");
  IElementType K_ZEN_CLASS = new ZenScriptTokenType("zenClass");
  IElementType K_ZEN_CONSTRUCTOR = new ZenScriptTokenType("zenConstructor");
  IElementType LONG = new ZenScriptTokenType("LONG");
  IElementType LONG_LITERAL = new ZenScriptTokenType("LONG_LITERAL");
  IElementType OP_ADD = new ZenScriptTokenType("+");
  IElementType OP_ADD_ASSIGN = new ZenScriptTokenType("+=");
  IElementType OP_AND = new ZenScriptTokenType("&");
  IElementType OP_AND_AND = new ZenScriptTokenType("&&");
  IElementType OP_AND_ASSIGN = new ZenScriptTokenType("&=");
  IElementType OP_ASSIGN = new ZenScriptTokenType("=");
  IElementType OP_BACKTICK = new ZenScriptTokenType("`");
  IElementType OP_CAT = new ZenScriptTokenType("~");
  IElementType OP_CAT_ASSIGN = new ZenScriptTokenType("~=");
  IElementType OP_COLON = new ZenScriptTokenType(":");
  IElementType OP_DIV = new ZenScriptTokenType("/");
  IElementType OP_DIV_ASSIGN = new ZenScriptTokenType("/=");
  IElementType OP_DOLLAR = new ZenScriptTokenType("$");
  IElementType OP_DOT_DOT = new ZenScriptTokenType("..");
  IElementType OP_EQUAL = new ZenScriptTokenType("==");
  IElementType OP_GREATER = new ZenScriptTokenType(">");
  IElementType OP_GREATER_EQUAL = new ZenScriptTokenType(">=");
  IElementType OP_LESS = new ZenScriptTokenType("<");
  IElementType OP_LESS_EQUAL = new ZenScriptTokenType("<=");
  IElementType OP_MOD = new ZenScriptTokenType("%");
  IElementType OP_MOD_ASSIGN = new ZenScriptTokenType("%=");
  IElementType OP_MUL = new ZenScriptTokenType("*");
  IElementType OP_MUL_ASSIGN = new ZenScriptTokenType("*=");
  IElementType OP_NOT = new ZenScriptTokenType("!");
  IElementType OP_NOT_EQUAL = new ZenScriptTokenType("!=");
  IElementType OP_OR = new ZenScriptTokenType("|");
  IElementType OP_OR_ASSIGN = new ZenScriptTokenType("|=");
  IElementType OP_OR_OR = new ZenScriptTokenType("||");
  IElementType OP_QUEST = new ZenScriptTokenType("?");
  IElementType OP_SUB = new ZenScriptTokenType("-");
  IElementType OP_SUB_ASSIGN = new ZenScriptTokenType("-=");
  IElementType OP_XOR = new ZenScriptTokenType("^");
  IElementType OP_XOR_ASSIGN = new ZenScriptTokenType("^=");
  IElementType PAREN_CLOSE = new ZenScriptTokenType(")");
  IElementType PAREN_OPEN = new ZenScriptTokenType("(");
  IElementType SEMICOLON = new ZenScriptTokenType(";");
  IElementType SHORT = new ZenScriptTokenType("SHORT");
  IElementType STRING = new ZenScriptTokenType("STRING");
  IElementType STRING_LITERAL = new ZenScriptTokenType("STRING_LITERAL");
  IElementType TRUE_LITERAL = new ZenScriptTokenType("TRUE_LITERAL");
  IElementType VOID = new ZenScriptTokenType("VOID");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ADD_EXPRESSION) {
        return new ZenScriptAddExpressionImpl(node);
      }
      else if (type == ALIAS) {
        return new ZenScriptAliasImpl(node);
      }
      else if (type == AND_AND_EXPRESSION) {
        return new ZenScriptAndAndExpressionImpl(node);
      }
      else if (type == AND_EXPRESSION) {
        return new ZenScriptAndExpressionImpl(node);
      }
      else if (type == ARGUMENTS) {
        return new ZenScriptArgumentsImpl(node);
      }
      else if (type == ARRAY_SUFFIX) {
        return new ZenScriptArraySuffixImpl(node);
      }
      else if (type == ASSIGNMENT_EXPRESSION) {
        return new ZenScriptAssignmentExpressionImpl(node);
      }
      else if (type == BLOCK_STATEMENT) {
        return new ZenScriptBlockStatementImpl(node);
      }
      else if (type == BRACKET_HANDLER) {
        return new ZenScriptBracketHandlerImpl(node);
      }
      else if (type == BREAK_STATEMENT) {
        return new ZenScriptBreakStatementImpl(node);
      }
      else if (type == CLASS_DECLARATION) {
        return new ZenScriptClassDeclarationImpl(node);
      }
      else if (type == COMPARE_EXPRESSION) {
        return new ZenScriptCompareExpressionImpl(node);
      }
      else if (type == CONDITIONAL_EXPRESSION) {
        return new ZenScriptConditionalExpressionImpl(node);
      }
      else if (type == CONSTRUCTOR_BODY) {
        return new ZenScriptConstructorBodyImpl(node);
      }
      else if (type == CONSTRUCTOR_DECLARATION) {
        return new ZenScriptConstructorDeclarationImpl(node);
      }
      else if (type == CONTINUE_STATEMENT) {
        return new ZenScriptContinueStatementImpl(node);
      }
      else if (type == DEFAULT_VALUE) {
        return new ZenScriptDefaultValueImpl(node);
      }
      else if (type == ELSE_BODY) {
        return new ZenScriptElseBodyImpl(node);
      }
      else if (type == EXPAND_FUNCTION_DECLARATION) {
        return new ZenScriptExpandFunctionDeclarationImpl(node);
      }
      else if (type == EXPRESSION_STATEMENT) {
        return new ZenScriptExpressionStatementImpl(node);
      }
      else if (type == FOREACH_BODY) {
        return new ZenScriptForeachBodyImpl(node);
      }
      else if (type == FOREACH_STATEMENT) {
        return new ZenScriptForeachStatementImpl(node);
      }
      else if (type == FUNCTION_BODY) {
        return new ZenScriptFunctionBodyImpl(node);
      }
      else if (type == FUNCTION_DECLARATION) {
        return new ZenScriptFunctionDeclarationImpl(node);
      }
      else if (type == IDENTIFIER) {
        return new ZenScriptIdentifierImpl(node);
      }
      else if (type == IF_STATEMENT) {
        return new ZenScriptIfStatementImpl(node);
      }
      else if (type == IMPORT_DECLARATION) {
        return new ZenScriptImportDeclarationImpl(node);
      }
      else if (type == IMPORT_LIST) {
        return new ZenScriptImportListImpl(node);
      }
      else if (type == INITIALIZER) {
        return new ZenScriptInitializerImpl(node);
      }
      else if (type == MAP_ENTRY) {
        return new ZenScriptMapEntryImpl(node);
      }
      else if (type == MUL_EXPRESSION) {
        return new ZenScriptMulExpressionImpl(node);
      }
      else if (type == OR_EXPRESSION) {
        return new ZenScriptOrExpressionImpl(node);
      }
      else if (type == OR_OR_EXPRESSION) {
        return new ZenScriptOrOrExpressionImpl(node);
      }
      else if (type == PARAMETER) {
        return new ZenScriptParameterImpl(node);
      }
      else if (type == POSTFIX_EXPRESSION) {
        return new ZenScriptPostfixExpressionImpl(node);
      }
      else if (type == PRIMARY_EXPRESSION) {
        return new ZenScriptPrimaryExpressionImpl(node);
      }
      else if (type == PRIMITIVE_TYPE) {
        return new ZenScriptPrimitiveTypeImpl(node);
      }
      else if (type == QUALIFIED_NAME) {
        return new ZenScriptQualifiedNameImpl(node);
      }
      else if (type == QUALIFIER) {
        return new ZenScriptQualifierImpl(node);
      }
      else if (type == RETURN_STATEMENT) {
        return new ZenScriptReturnStatementImpl(node);
      }
      else if (type == SCRIPT_BODY) {
        return new ZenScriptScriptBodyImpl(node);
      }
      else if (type == SIMPLE_VARIABLE) {
        return new ZenScriptSimpleVariableImpl(node);
      }
      else if (type == STATEMENT) {
        return new ZenScriptStatementImpl(node);
      }
      else if (type == THEN_BODY) {
        return new ZenScriptThenBodyImpl(node);
      }
      else if (type == TYPE_LITERAL) {
        return new ZenScriptTypeLiteralImpl(node);
      }
      else if (type == UNARY_EXPRESSION) {
        return new ZenScriptUnaryExpressionImpl(node);
      }
      else if (type == VARIABLE_DECLARATION) {
        return new ZenScriptVariableDeclarationImpl(node);
      }
      else if (type == WHILE_STATEMENT) {
        return new ZenScriptWhileStatementImpl(node);
      }
      else if (type == XOR_EXPRESSION) {
        return new ZenScriptXorExpressionImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
