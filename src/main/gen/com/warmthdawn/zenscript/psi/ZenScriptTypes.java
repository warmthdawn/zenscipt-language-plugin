// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.warmthdawn.zenscript.psi.impl.*;

public interface ZenScriptTypes {

  IElementType ADD_EXPRESSION = new ZenScriptElementType("ADD_EXPRESSION");
  IElementType AND_AND_EXPRESSION = new ZenScriptElementType("AND_AND_EXPRESSION");
  IElementType AND_EXPRESSION = new ZenScriptElementType("AND_EXPRESSION");
  IElementType ARGUMENTS = new ZenScriptElementType("ARGUMENTS");
  IElementType ARRAY_INDEX_EXPRESSION = new ZenScriptElementType("ARRAY_INDEX_EXPRESSION");
  IElementType ARRAY_LITERAL = new ZenScriptElementType("ARRAY_LITERAL");
  IElementType ARRAY_TYPE_REF = new ZenScriptElementType("ARRAY_TYPE_REF");
  IElementType ASSIGNMENT_EXPRESSION = new ZenScriptElementType("ASSIGNMENT_EXPRESSION");
  IElementType BLOCK_STATEMENT = new ZenScriptElementType("BLOCK_STATEMENT");
  IElementType BRACKET_HANDLER_LITERAL = new ZenScriptElementType("BRACKET_HANDLER_LITERAL");
  IElementType BREAK_STATEMENT = new ZenScriptElementType("BREAK_STATEMENT");
  IElementType CALL_EXPRESSION = new ZenScriptElementType("CALL_EXPRESSION");
  IElementType CLASS_DECLARATION = new ZenScriptElementType("CLASS_DECLARATION");
  IElementType CLASS_TYPE_REF = new ZenScriptElementType("CLASS_TYPE_REF");
  IElementType COMPARE_EXPRESSION = new ZenScriptElementType("COMPARE_EXPRESSION");
  IElementType CONDITIONAL_EXPRESSION = new ZenScriptElementType("CONDITIONAL_EXPRESSION");
  IElementType CONSTRUCTOR_DECLARATION = new ZenScriptElementType("CONSTRUCTOR_DECLARATION");
  IElementType CONTINUE_STATEMENT = new ZenScriptElementType("CONTINUE_STATEMENT");
  IElementType EXPAND_FUNCTION_DECLARATION = new ZenScriptElementType("EXPAND_FUNCTION_DECLARATION");
  IElementType EXPRESSION = new ZenScriptElementType("EXPRESSION");
  IElementType EXPRESSION_STATEMENT = new ZenScriptElementType("EXPRESSION_STATEMENT");
  IElementType FOREACH_STATEMENT = new ZenScriptElementType("FOREACH_STATEMENT");
  IElementType FOREACH_VARIABLE_DECLARATION = new ZenScriptElementType("FOREACH_VARIABLE_DECLARATION");
  IElementType FUNCTION_BODY = new ZenScriptElementType("FUNCTION_BODY");
  IElementType FUNCTION_DECLARATION = new ZenScriptElementType("FUNCTION_DECLARATION");
  IElementType FUNCTION_LITERAL = new ZenScriptElementType("FUNCTION_LITERAL");
  IElementType FUNCTION_TYPE_REF = new ZenScriptElementType("FUNCTION_TYPE_REF");
  IElementType IDENTIFIER = new ZenScriptElementType("IDENTIFIER");
  IElementType IF_STATEMENT = new ZenScriptElementType("IF_STATEMENT");
  IElementType IMPORT_DECLARATION = new ZenScriptElementType("IMPORT_DECLARATION");
  IElementType IMPORT_LIST = new ZenScriptElementType("IMPORT_LIST");
  IElementType IMPORT_REFERENCE = new ZenScriptElementType("IMPORT_REFERENCE");
  IElementType INITIALIZER_OR_DEFAULT = new ZenScriptElementType("INITIALIZER_OR_DEFAULT");
  IElementType INSTANCE_OF_EXPRESSION = new ZenScriptElementType("INSTANCE_OF_EXPRESSION");
  IElementType LIST_TYPE_REF = new ZenScriptElementType("LIST_TYPE_REF");
  IElementType LITERAL_EXPRESSION = new ZenScriptElementType("LITERAL_EXPRESSION");
  IElementType LOCAL_ACCESS_EXPRESSION = new ZenScriptElementType("LOCAL_ACCESS_EXPRESSION");
  IElementType MAP_ENTRY = new ZenScriptElementType("MAP_ENTRY");
  IElementType MAP_LITERAL = new ZenScriptElementType("MAP_LITERAL");
  IElementType MAP_TYPE_REF = new ZenScriptElementType("MAP_TYPE_REF");
  IElementType MEMBER_ACCESS_EXPRESSION = new ZenScriptElementType("MEMBER_ACCESS_EXPRESSION");
  IElementType MEMBER_NAME = new ZenScriptElementType("MEMBER_NAME");
  IElementType MUL_EXPRESSION = new ZenScriptElementType("MUL_EXPRESSION");
  IElementType OR_EXPRESSION = new ZenScriptElementType("OR_EXPRESSION");
  IElementType OR_OR_EXPRESSION = new ZenScriptElementType("OR_OR_EXPRESSION");
  IElementType PARAMETER = new ZenScriptElementType("PARAMETER");
  IElementType PARAMETERS = new ZenScriptElementType("PARAMETERS");
  IElementType PAREN_EXPRESSION = new ZenScriptElementType("PAREN_EXPRESSION");
  IElementType POSTFIX_EXPRESSION = new ZenScriptElementType("POSTFIX_EXPRESSION");
  IElementType PRIMITIVE_LITERAL = new ZenScriptElementType("PRIMITIVE_LITERAL");
  IElementType PRIMITIVE_TYPE_REF = new ZenScriptElementType("PRIMITIVE_TYPE_REF");
  IElementType QUALIFIED_NAME = new ZenScriptElementType("QUALIFIED_NAME");
  IElementType QUALIFIER = new ZenScriptElementType("QUALIFIER");
  IElementType RANGE_EXPRESSION = new ZenScriptElementType("RANGE_EXPRESSION");
  IElementType RETURN_STATEMENT = new ZenScriptElementType("RETURN_STATEMENT");
  IElementType SCRIPT_BODY = new ZenScriptElementType("SCRIPT_BODY");
  IElementType STATEMENT = new ZenScriptElementType("STATEMENT");
  IElementType TYPE_CAST_EXPRESSION = new ZenScriptElementType("TYPE_CAST_EXPRESSION");
  IElementType TYPE_REF = new ZenScriptElementType("TYPE_REF");
  IElementType UNARY_EXPRESSION = new ZenScriptElementType("UNARY_EXPRESSION");
  IElementType VARIABLE_DECLARATION = new ZenScriptElementType("VARIABLE_DECLARATION");
  IElementType WHILE_STATEMENT = new ZenScriptElementType("WHILE_STATEMENT");
  IElementType XOR_EXPRESSION = new ZenScriptElementType("XOR_EXPRESSION");

  IElementType BLOCK_COMMENT = new ZenScriptTokenType("BLOCK_COMMENT");
  IElementType BRACE_CLOSE = new ZenScriptTokenType("}");
  IElementType BRACE_OPEN = new ZenScriptTokenType("{");
  IElementType BRACK_CLOSE = new ZenScriptTokenType("]");
  IElementType BRACK_OPEN = new ZenScriptTokenType("[");
  IElementType COMMA = new ZenScriptTokenType(",");
  IElementType DOC_COMMENT = new ZenScriptTokenType("DOC_COMMENT");
  IElementType DOT = new ZenScriptTokenType(".");
  IElementType DOUBLE_LITERAL = new ZenScriptTokenType("DOUBLE_LITERAL");
  IElementType FLOAT_LITERAL = new ZenScriptTokenType("FLOAT_LITERAL");
  IElementType ID = new ZenScriptTokenType("ID");
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
  IElementType LINE_COMMENT = new ZenScriptTokenType("LINE_COMMENT");
  IElementType LONG_LITERAL = new ZenScriptTokenType("LONG_LITERAL");
  IElementType NEW_LINE = new ZenScriptTokenType("<NEW_LINE>");
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
  IElementType PREPROCESSOR = new ZenScriptTokenType("PREPROCESSOR");
  IElementType SEMICOLON = new ZenScriptTokenType(";");
  IElementType STRING_LITERAL = new ZenScriptTokenType("STRING_LITERAL");

  interface TokenSets {
    TokenSet ADD_OP_TOKENS = TokenSet.create(OP_ADD, OP_CAT, OP_SUB);
    TokenSet ASSIGNMENT_OP_TOKENS = TokenSet.create(
        OP_ADD_ASSIGN, OP_AND_ASSIGN, OP_ASSIGN, OP_CAT_ASSIGN, 
        OP_DIV_ASSIGN, OP_MOD_ASSIGN, OP_MUL_ASSIGN, OP_OR_ASSIGN, 
        OP_SUB_ASSIGN, OP_XOR_ASSIGN
    );
    TokenSet COMPARE_OP_TOKENS = TokenSet.create(
        K_HAS, K_IN, OP_EQUAL, OP_GREATER, 
        OP_GREATER_EQUAL, OP_LESS, OP_LESS_EQUAL, OP_NOT_EQUAL
    );
    TokenSet EXPRESSION_START_TOKENS = TokenSet.create(
        BRACE_OPEN, BRACK_OPEN, DOUBLE_LITERAL, FLOAT_LITERAL, 
        ID, INT_LITERAL, K_ANY, K_BOOL, 
        K_BYTE, K_DOUBLE, K_FALSE, K_FLOAT, 
        K_FUNCTION, K_INT, K_LONG, K_NULL, 
        K_SHORT, K_STRING, K_TO, K_TRUE, 
        K_VOID, LONG_LITERAL, OP_LESS, OP_NOT, 
        OP_SUB, PAREN_OPEN, STRING_LITERAL
    );
    TokenSet MUL_OP_TOKENS = TokenSet.create(OP_DIV, OP_MOD, OP_MUL);
    TokenSet PRIMITIVE_LITERAL_TOKENS = TokenSet.create(
        DOUBLE_LITERAL, FLOAT_LITERAL, INT_LITERAL, K_FALSE, 
        K_NULL, K_TRUE, LONG_LITERAL, STRING_LITERAL
    );
    TokenSet PRIMITIVE_TYPE_REF_TOKENS = TokenSet.create(
        K_ANY, K_BOOL, K_BYTE, K_DOUBLE, 
        K_FLOAT, K_INT, K_LONG, K_SHORT, 
        K_STRING, K_VOID
    );
  }

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ADD_EXPRESSION) {
        return new ZenScriptAddExpressionImpl(node);
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
      else if (type == ARRAY_INDEX_EXPRESSION) {
        return new ZenScriptArrayIndexExpressionImpl(node);
      }
      else if (type == ARRAY_LITERAL) {
        return new ZenScriptArrayLiteralImpl(node);
      }
      else if (type == ARRAY_TYPE_REF) {
        return new ZenScriptArrayTypeRefImpl(node);
      }
      else if (type == ASSIGNMENT_EXPRESSION) {
        return new ZenScriptAssignmentExpressionImpl(node);
      }
      else if (type == BLOCK_STATEMENT) {
        return new ZenScriptBlockStatementImpl(node);
      }
      else if (type == BRACKET_HANDLER_LITERAL) {
        return new ZenScriptBracketHandlerLiteralImpl(node);
      }
      else if (type == BREAK_STATEMENT) {
        return new ZenScriptBreakStatementImpl(node);
      }
      else if (type == CALL_EXPRESSION) {
        return new ZenScriptCallExpressionImpl(node);
      }
      else if (type == CLASS_DECLARATION) {
        return new ZenScriptClassDeclarationImpl(node);
      }
      else if (type == CLASS_TYPE_REF) {
        return new ZenScriptClassTypeRefImpl(node);
      }
      else if (type == COMPARE_EXPRESSION) {
        return new ZenScriptCompareExpressionImpl(node);
      }
      else if (type == CONDITIONAL_EXPRESSION) {
        return new ZenScriptConditionalExpressionImpl(node);
      }
      else if (type == CONSTRUCTOR_DECLARATION) {
        return new ZenScriptConstructorDeclarationImpl(node);
      }
      else if (type == CONTINUE_STATEMENT) {
        return new ZenScriptContinueStatementImpl(node);
      }
      else if (type == EXPAND_FUNCTION_DECLARATION) {
        return new ZenScriptExpandFunctionDeclarationImpl(node);
      }
      else if (type == EXPRESSION) {
        return new ZenScriptExpressionImpl(node);
      }
      else if (type == EXPRESSION_STATEMENT) {
        return new ZenScriptExpressionStatementImpl(node);
      }
      else if (type == FOREACH_STATEMENT) {
        return new ZenScriptForeachStatementImpl(node);
      }
      else if (type == FOREACH_VARIABLE_DECLARATION) {
        return new ZenScriptForeachVariableDeclarationImpl(node);
      }
      else if (type == FUNCTION_BODY) {
        return new ZenScriptFunctionBodyImpl(node);
      }
      else if (type == FUNCTION_DECLARATION) {
        return new ZenScriptFunctionDeclarationImpl(node);
      }
      else if (type == FUNCTION_LITERAL) {
        return new ZenScriptFunctionLiteralImpl(node);
      }
      else if (type == FUNCTION_TYPE_REF) {
        return new ZenScriptFunctionTypeRefImpl(node);
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
      else if (type == IMPORT_REFERENCE) {
        return new ZenScriptImportReferenceImpl(node);
      }
      else if (type == INITIALIZER_OR_DEFAULT) {
        return new ZenScriptInitializerOrDefaultImpl(node);
      }
      else if (type == INSTANCE_OF_EXPRESSION) {
        return new ZenScriptInstanceOfExpressionImpl(node);
      }
      else if (type == LIST_TYPE_REF) {
        return new ZenScriptListTypeRefImpl(node);
      }
      else if (type == LOCAL_ACCESS_EXPRESSION) {
        return new ZenScriptLocalAccessExpressionImpl(node);
      }
      else if (type == MAP_ENTRY) {
        return new ZenScriptMapEntryImpl(node);
      }
      else if (type == MAP_LITERAL) {
        return new ZenScriptMapLiteralImpl(node);
      }
      else if (type == MAP_TYPE_REF) {
        return new ZenScriptMapTypeRefImpl(node);
      }
      else if (type == MEMBER_ACCESS_EXPRESSION) {
        return new ZenScriptMemberAccessExpressionImpl(node);
      }
      else if (type == MEMBER_NAME) {
        return new ZenScriptMemberNameImpl(node);
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
      else if (type == PARAMETERS) {
        return new ZenScriptParametersImpl(node);
      }
      else if (type == PAREN_EXPRESSION) {
        return new ZenScriptParenExpressionImpl(node);
      }
      else if (type == PRIMITIVE_LITERAL) {
        return new ZenScriptPrimitiveLiteralImpl(node);
      }
      else if (type == PRIMITIVE_TYPE_REF) {
        return new ZenScriptPrimitiveTypeRefImpl(node);
      }
      else if (type == QUALIFIED_NAME) {
        return new ZenScriptQualifiedNameImpl(node);
      }
      else if (type == QUALIFIER) {
        return new ZenScriptQualifierImpl(node);
      }
      else if (type == RANGE_EXPRESSION) {
        return new ZenScriptRangeExpressionImpl(node);
      }
      else if (type == RETURN_STATEMENT) {
        return new ZenScriptReturnStatementImpl(node);
      }
      else if (type == SCRIPT_BODY) {
        return new ZenScriptScriptBodyImpl(node);
      }
      else if (type == STATEMENT) {
        return new ZenScriptStatementImpl(node);
      }
      else if (type == TYPE_CAST_EXPRESSION) {
        return new ZenScriptTypeCastExpressionImpl(node);
      }
      else if (type == TYPE_REF) {
        return new ZenScriptTypeRefImpl(node);
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
