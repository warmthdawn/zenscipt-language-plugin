// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.warmthdawn.zenscript.psi.ZenScriptTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class ZenScriptParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return file(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(ADD_EXPRESSION, AND_AND_EXPRESSION, AND_EXPRESSION, ARRAY_INDEX_EXPRESSION,
      ASSIGNMENT_EXPRESSION, CALL_EXPRESSION, COMPARE_EXPRESSION, CONDITIONAL_EXPRESSION,
      EXPRESSION, INSTANCE_OF_EXPRESSION, MEMBER_ACCESS_EXPRESSION, MUL_EXPRESSION,
      OR_EXPRESSION, OR_OR_EXPRESSION, PRIMARY_EXPRESSION, RANGE_EXPRESSION,
      TYPE_CAST_EXPRESSION, UNARY_EXPRESSION, XOR_EXPRESSION),
  };

  /* ********************************************************** */
  // identifier
  public static boolean alias(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "alias")) return false;
    if (!nextTokenIs(b, "<alias>", ID, K_TO)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ALIAS, "<alias>");
    r = identifier(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (expression (',' expression)*)?
  public static boolean arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments")) return false;
    Marker m = enter_section_(b, l, _NONE_, ARGUMENTS, "<arguments>");
    arguments_0(b, l + 1);
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // expression (',' expression)*
  private static boolean arguments_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expression(b, l + 1, -1);
    r = r && arguments_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' expression)*
  private static boolean arguments_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!arguments_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "arguments_0_1", c)) break;
    }
    return true;
  }

  // ',' expression
  private static boolean arguments_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ('[' ']')*
  public static boolean arraySuffix(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arraySuffix")) return false;
    Marker m = enter_section_(b, l, _NONE_, ARRAY_SUFFIX, "<array suffix>");
    while (true) {
      int c = current_position_(b);
      if (!arraySuffix_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "arraySuffix", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // '[' ']'
  private static boolean arraySuffix_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arraySuffix_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, BRACK_OPEN, BRACK_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '{' statement* '}'
  public static boolean blockStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "blockStatement")) return false;
    if (!nextTokenIs(b, BRACE_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACE_OPEN);
    r = r && blockStatement_1(b, l + 1);
    r = r && consumeToken(b, BRACE_CLOSE);
    exit_section_(b, m, BLOCK_STATEMENT, r);
    return r;
  }

  // statement*
  private static boolean blockStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "blockStatement_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!statement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "blockStatement_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // '<' {(!'>')*} '>'
  public static boolean bracketHandler(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracketHandler")) return false;
    if (!nextTokenIs(b, OP_LESS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_LESS);
    r = r && bracketHandler_1(b, l + 1);
    r = r && consumeToken(b, OP_GREATER);
    exit_section_(b, m, BRACKET_HANDLER, r);
    return r;
  }

  // (!'>')*
  private static boolean bracketHandler_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracketHandler_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!bracketHandler_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bracketHandler_1", c)) break;
    }
    return true;
  }

  // !'>'
  private static boolean bracketHandler_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracketHandler_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, OP_GREATER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'break' ';'
  public static boolean breakStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "breakStatement")) return false;
    if (!nextTokenIs(b, K_BREAK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, K_BREAK, SEMICOLON);
    exit_section_(b, m, BREAK_STATEMENT, r);
    return r;
  }

  /* ********************************************************** */
  // 'zenClass' qualifiedName '{' (variableDeclaration | constructorDeclaration | functionDeclaration)* '}'
  public static boolean classDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classDeclaration")) return false;
    if (!nextTokenIs(b, K_ZEN_CLASS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, K_ZEN_CLASS);
    r = r && qualifiedName(b, l + 1);
    r = r && consumeToken(b, BRACE_OPEN);
    r = r && classDeclaration_3(b, l + 1);
    r = r && consumeToken(b, BRACE_CLOSE);
    exit_section_(b, m, CLASS_DECLARATION, r);
    return r;
  }

  // (variableDeclaration | constructorDeclaration | functionDeclaration)*
  private static boolean classDeclaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classDeclaration_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!classDeclaration_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "classDeclaration_3", c)) break;
    }
    return true;
  }

  // variableDeclaration | constructorDeclaration | functionDeclaration
  private static boolean classDeclaration_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classDeclaration_3_0")) return false;
    boolean r;
    r = variableDeclaration(b, l + 1);
    if (!r) r = constructorDeclaration(b, l + 1);
    if (!r) r = functionDeclaration(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // '{' statement* '}'
  public static boolean constructorBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructorBody")) return false;
    if (!nextTokenIs(b, BRACE_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACE_OPEN);
    r = r && constructorBody_1(b, l + 1);
    r = r && consumeToken(b, BRACE_CLOSE);
    exit_section_(b, m, CONSTRUCTOR_BODY, r);
    return r;
  }

  // statement*
  private static boolean constructorBody_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructorBody_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!statement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "constructorBody_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // 'zenConstructor' '(' (parameter (',' parameter)*)? ')' constructorBody
  public static boolean constructorDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructorDeclaration")) return false;
    if (!nextTokenIs(b, K_ZEN_CONSTRUCTOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, K_ZEN_CONSTRUCTOR, PAREN_OPEN);
    r = r && constructorDeclaration_2(b, l + 1);
    r = r && consumeToken(b, PAREN_CLOSE);
    r = r && constructorBody(b, l + 1);
    exit_section_(b, m, CONSTRUCTOR_DECLARATION, r);
    return r;
  }

  // (parameter (',' parameter)*)?
  private static boolean constructorDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructorDeclaration_2")) return false;
    constructorDeclaration_2_0(b, l + 1);
    return true;
  }

  // parameter (',' parameter)*
  private static boolean constructorDeclaration_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructorDeclaration_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter(b, l + 1);
    r = r && constructorDeclaration_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' parameter)*
  private static boolean constructorDeclaration_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructorDeclaration_2_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!constructorDeclaration_2_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "constructorDeclaration_2_0_1", c)) break;
    }
    return true;
  }

  // ',' parameter
  private static boolean constructorDeclaration_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructorDeclaration_2_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'continue' ';'
  public static boolean continueStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "continueStatement")) return false;
    if (!nextTokenIs(b, K_CONTINUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, K_CONTINUE, SEMICOLON);
    exit_section_(b, m, CONTINUE_STATEMENT, r);
    return r;
  }

  /* ********************************************************** */
  // expression
  public static boolean defaultValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defaultValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DEFAULT_VALUE, "<default value>");
    r = expression(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // statement
  public static boolean elseBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elseBody")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ELSE_BODY, "<else body>");
    r = statement(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '$expand' typeLiteral '$' identifier '(' (parameter (',' parameter)*)? ')' ('as' typeLiteral)? functionBody
  public static boolean expandFunctionDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandFunctionDeclaration")) return false;
    if (!nextTokenIs(b, K_EXPAND)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, K_EXPAND);
    r = r && typeLiteral(b, l + 1);
    r = r && consumeToken(b, OP_DOLLAR);
    r = r && identifier(b, l + 1);
    r = r && consumeToken(b, PAREN_OPEN);
    r = r && expandFunctionDeclaration_5(b, l + 1);
    r = r && consumeToken(b, PAREN_CLOSE);
    r = r && expandFunctionDeclaration_7(b, l + 1);
    r = r && functionBody(b, l + 1);
    exit_section_(b, m, EXPAND_FUNCTION_DECLARATION, r);
    return r;
  }

  // (parameter (',' parameter)*)?
  private static boolean expandFunctionDeclaration_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandFunctionDeclaration_5")) return false;
    expandFunctionDeclaration_5_0(b, l + 1);
    return true;
  }

  // parameter (',' parameter)*
  private static boolean expandFunctionDeclaration_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandFunctionDeclaration_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter(b, l + 1);
    r = r && expandFunctionDeclaration_5_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' parameter)*
  private static boolean expandFunctionDeclaration_5_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandFunctionDeclaration_5_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expandFunctionDeclaration_5_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expandFunctionDeclaration_5_0_1", c)) break;
    }
    return true;
  }

  // ',' parameter
  private static boolean expandFunctionDeclaration_5_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandFunctionDeclaration_5_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ('as' typeLiteral)?
  private static boolean expandFunctionDeclaration_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandFunctionDeclaration_7")) return false;
    expandFunctionDeclaration_7_0(b, l + 1);
    return true;
  }

  // 'as' typeLiteral
  private static boolean expandFunctionDeclaration_7_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandFunctionDeclaration_7_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, K_AS);
    r = r && typeLiteral(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expression ';'
  public static boolean expressionStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expressionStatement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPRESSION_STATEMENT, "<expression statement>");
    r = expression(b, l + 1, -1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // importList scriptBody
  static boolean file(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = importList(b, l + 1);
    r = r && scriptBody(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '{' statement* '}'
  public static boolean foreachBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachBody")) return false;
    if (!nextTokenIs(b, BRACE_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACE_OPEN);
    r = r && foreachBody_1(b, l + 1);
    r = r && consumeToken(b, BRACE_CLOSE);
    exit_section_(b, m, FOREACH_BODY, r);
    return r;
  }

  // statement*
  private static boolean foreachBody_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachBody_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!statement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "foreachBody_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // 'for' simpleVariable (',' simpleVariable)* 'in' expression foreachBody
  public static boolean foreachStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachStatement")) return false;
    if (!nextTokenIs(b, K_FOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, K_FOR);
    r = r && simpleVariable(b, l + 1);
    r = r && foreachStatement_2(b, l + 1);
    r = r && consumeToken(b, K_IN);
    r = r && expression(b, l + 1, -1);
    r = r && foreachBody(b, l + 1);
    exit_section_(b, m, FOREACH_STATEMENT, r);
    return r;
  }

  // (',' simpleVariable)*
  private static boolean foreachStatement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachStatement_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!foreachStatement_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "foreachStatement_2", c)) break;
    }
    return true;
  }

  // ',' simpleVariable
  private static boolean foreachStatement_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachStatement_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && simpleVariable(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '{' statement* '}'
  public static boolean functionBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionBody")) return false;
    if (!nextTokenIs(b, BRACE_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACE_OPEN);
    r = r && functionBody_1(b, l + 1);
    r = r && consumeToken(b, BRACE_CLOSE);
    exit_section_(b, m, FUNCTION_BODY, r);
    return r;
  }

  // statement*
  private static boolean functionBody_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionBody_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!statement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "functionBody_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ('static')? 'function' identifier '(' (parameter (',' parameter)*)? ')' ('as' typeLiteral)? functionBody
  public static boolean functionDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclaration")) return false;
    if (!nextTokenIs(b, "<function declaration>", K_FUNCTION, K_STATIC)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_DECLARATION, "<function declaration>");
    r = functionDeclaration_0(b, l + 1);
    r = r && consumeToken(b, K_FUNCTION);
    r = r && identifier(b, l + 1);
    r = r && consumeToken(b, PAREN_OPEN);
    r = r && functionDeclaration_4(b, l + 1);
    r = r && consumeToken(b, PAREN_CLOSE);
    r = r && functionDeclaration_6(b, l + 1);
    r = r && functionBody(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ('static')?
  private static boolean functionDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclaration_0")) return false;
    consumeToken(b, K_STATIC);
    return true;
  }

  // (parameter (',' parameter)*)?
  private static boolean functionDeclaration_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclaration_4")) return false;
    functionDeclaration_4_0(b, l + 1);
    return true;
  }

  // parameter (',' parameter)*
  private static boolean functionDeclaration_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclaration_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter(b, l + 1);
    r = r && functionDeclaration_4_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' parameter)*
  private static boolean functionDeclaration_4_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclaration_4_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!functionDeclaration_4_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "functionDeclaration_4_0_1", c)) break;
    }
    return true;
  }

  // ',' parameter
  private static boolean functionDeclaration_4_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclaration_4_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ('as' typeLiteral)?
  private static boolean functionDeclaration_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclaration_6")) return false;
    functionDeclaration_6_0(b, l + 1);
    return true;
  }

  // 'as' typeLiteral
  private static boolean functionDeclaration_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclaration_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, K_AS);
    r = r && typeLiteral(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ID | 'to'
  public static boolean identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "identifier")) return false;
    if (!nextTokenIs(b, "<identifier>", ID, K_TO)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IDENTIFIER, "<identifier>");
    r = consumeToken(b, ID);
    if (!r) r = consumeToken(b, K_TO);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'if' expression thenBody ('else' elseBody)?
  public static boolean ifStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifStatement")) return false;
    if (!nextTokenIs(b, K_IF)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, K_IF);
    r = r && expression(b, l + 1, -1);
    r = r && thenBody(b, l + 1);
    r = r && ifStatement_3(b, l + 1);
    exit_section_(b, m, IF_STATEMENT, r);
    return r;
  }

  // ('else' elseBody)?
  private static boolean ifStatement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifStatement_3")) return false;
    ifStatement_3_0(b, l + 1);
    return true;
  }

  // 'else' elseBody
  private static boolean ifStatement_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifStatement_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, K_ELSE);
    r = r && elseBody(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'import' qualifiedName ('as' alias)? ';'
  public static boolean importDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importDeclaration")) return false;
    if (!nextTokenIs(b, K_IMPORT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, K_IMPORT);
    r = r && qualifiedName(b, l + 1);
    r = r && importDeclaration_2(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, IMPORT_DECLARATION, r);
    return r;
  }

  // ('as' alias)?
  private static boolean importDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importDeclaration_2")) return false;
    importDeclaration_2_0(b, l + 1);
    return true;
  }

  // 'as' alias
  private static boolean importDeclaration_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importDeclaration_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, K_AS);
    r = r && alias(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (importDeclaration)*
  public static boolean importList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importList")) return false;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_LIST, "<import list>");
    while (true) {
      int c = current_position_(b);
      if (!importList_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "importList", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // (importDeclaration)
  private static boolean importList_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importList_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = importDeclaration(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expression
  public static boolean initializer(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "initializer")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INITIALIZER, "<initializer>");
    r = expression(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // expression ':' expression
  public static boolean mapEntry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapEntry")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MAP_ENTRY, "<map entry>");
    r = expression(b, l + 1, -1);
    r = r && consumeToken(b, OP_COLON);
    r = r && expression(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // identifier ('as' typeLiteral)? ('=' defaultValue)?
  public static boolean parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter")) return false;
    if (!nextTokenIs(b, "<parameter>", ID, K_TO)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PARAMETER, "<parameter>");
    r = identifier(b, l + 1);
    r = r && parameter_1(b, l + 1);
    r = r && parameter_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ('as' typeLiteral)?
  private static boolean parameter_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_1")) return false;
    parameter_1_0(b, l + 1);
    return true;
  }

  // 'as' typeLiteral
  private static boolean parameter_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, K_AS);
    r = r && typeLiteral(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ('=' defaultValue)?
  private static boolean parameter_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_2")) return false;
    parameter_2_0(b, l + 1);
    return true;
  }

  // '=' defaultValue
  private static boolean parameter_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_ASSIGN);
    r = r && defaultValue(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ANY
  //   | BYTE
  //   | SHORT
  //   | INT
  //   | LONG
  //   | FLOAT
  //   | DOUBLE
  //   | BOOL
  //   | VOID
  //   | STRING
  public static boolean primitiveType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primitiveType")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PRIMITIVE_TYPE, "<primitive type>");
    r = consumeToken(b, ANY);
    if (!r) r = consumeToken(b, BYTE);
    if (!r) r = consumeToken(b, SHORT);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, LONG);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, DOUBLE);
    if (!r) r = consumeToken(b, BOOL);
    if (!r) r = consumeToken(b, VOID);
    if (!r) r = consumeToken(b, STRING);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // qualifier identifier
  public static boolean qualifiedName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qualifiedName")) return false;
    if (!nextTokenIs(b, "<qualified name>", ID, K_TO)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, QUALIFIED_NAME, "<qualified name>");
    r = qualifier(b, l + 1);
    r = r && identifier(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (identifier '.')*
  public static boolean qualifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qualifier")) return false;
    Marker m = enter_section_(b, l, _NONE_, QUALIFIER, "<qualifier>");
    while (true) {
      int c = current_position_(b);
      if (!qualifier_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "qualifier", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // identifier '.'
  private static boolean qualifier_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qualifier_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = identifier(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'return' expression? ';'
  public static boolean returnStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "returnStatement")) return false;
    if (!nextTokenIs(b, K_RETURN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, K_RETURN);
    r = r && returnStatement_1(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, RETURN_STATEMENT, r);
    return r;
  }

  // expression?
  private static boolean returnStatement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "returnStatement_1")) return false;
    expression(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // (functionDeclaration | expandFunctionDeclaration | classDeclaration | statement)*
  public static boolean scriptBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scriptBody")) return false;
    Marker m = enter_section_(b, l, _NONE_, SCRIPT_BODY, "<script body>");
    while (true) {
      int c = current_position_(b);
      if (!scriptBody_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scriptBody", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // functionDeclaration | expandFunctionDeclaration | classDeclaration | statement
  private static boolean scriptBody_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scriptBody_0")) return false;
    boolean r;
    r = functionDeclaration(b, l + 1);
    if (!r) r = expandFunctionDeclaration(b, l + 1);
    if (!r) r = classDeclaration(b, l + 1);
    if (!r) r = statement(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // identifier
  public static boolean simpleVariable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simpleVariable")) return false;
    if (!nextTokenIs(b, "<simple variable>", ID, K_TO)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SIMPLE_VARIABLE, "<simple variable>");
    r = identifier(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // blockStatement
  //     | returnStatement
  //     | breakStatement
  //     | continueStatement
  //     | ifStatement
  //     | foreachStatement
  //     | whileStatement
  //     | variableDeclaration
  //     | expressionStatement
  public static boolean statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STATEMENT, "<statement>");
    r = blockStatement(b, l + 1);
    if (!r) r = returnStatement(b, l + 1);
    if (!r) r = breakStatement(b, l + 1);
    if (!r) r = continueStatement(b, l + 1);
    if (!r) r = ifStatement(b, l + 1);
    if (!r) r = foreachStatement(b, l + 1);
    if (!r) r = whileStatement(b, l + 1);
    if (!r) r = variableDeclaration(b, l + 1);
    if (!r) r = expressionStatement(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // statement
  public static boolean thenBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "thenBody")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, THEN_BODY, "<then body>");
    r = statement(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // qualifiedName arraySuffix
  //   | 'function' '(' (typeLiteral (',' typeLiteral)*)? ')' typeLiteral arraySuffix
  //   | '[' typeLiteral ']' arraySuffix
  //   | primitiveType arraySuffix
  public static boolean typeLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeLiteral")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_LITERAL, "<type literal>");
    r = typeLiteral_0(b, l + 1);
    if (!r) r = typeLiteral_1(b, l + 1);
    if (!r) r = typeLiteral_2(b, l + 1);
    if (!r) r = typeLiteral_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // qualifiedName arraySuffix
  private static boolean typeLiteral_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeLiteral_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = qualifiedName(b, l + 1);
    r = r && arraySuffix(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'function' '(' (typeLiteral (',' typeLiteral)*)? ')' typeLiteral arraySuffix
  private static boolean typeLiteral_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeLiteral_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, K_FUNCTION, PAREN_OPEN);
    r = r && typeLiteral_1_2(b, l + 1);
    r = r && consumeToken(b, PAREN_CLOSE);
    r = r && typeLiteral(b, l + 1);
    r = r && arraySuffix(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (typeLiteral (',' typeLiteral)*)?
  private static boolean typeLiteral_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeLiteral_1_2")) return false;
    typeLiteral_1_2_0(b, l + 1);
    return true;
  }

  // typeLiteral (',' typeLiteral)*
  private static boolean typeLiteral_1_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeLiteral_1_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = typeLiteral(b, l + 1);
    r = r && typeLiteral_1_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' typeLiteral)*
  private static boolean typeLiteral_1_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeLiteral_1_2_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!typeLiteral_1_2_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "typeLiteral_1_2_0_1", c)) break;
    }
    return true;
  }

  // ',' typeLiteral
  private static boolean typeLiteral_1_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeLiteral_1_2_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && typeLiteral(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '[' typeLiteral ']' arraySuffix
  private static boolean typeLiteral_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeLiteral_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACK_OPEN);
    r = r && typeLiteral(b, l + 1);
    r = r && consumeToken(b, BRACK_CLOSE);
    r = r && arraySuffix(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // primitiveType arraySuffix
  private static boolean typeLiteral_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeLiteral_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = primitiveType(b, l + 1);
    r = r && arraySuffix(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ('var' | 'val' | 'static' | 'global') identifier ('as' typeLiteral)? ('=' initializer)? ';'
  public static boolean variableDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VARIABLE_DECLARATION, "<variable declaration>");
    r = variableDeclaration_0(b, l + 1);
    r = r && identifier(b, l + 1);
    r = r && variableDeclaration_2(b, l + 1);
    r = r && variableDeclaration_3(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // 'var' | 'val' | 'static' | 'global'
  private static boolean variableDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableDeclaration_0")) return false;
    boolean r;
    r = consumeToken(b, K_VAR);
    if (!r) r = consumeToken(b, K_VAL);
    if (!r) r = consumeToken(b, K_STATIC);
    if (!r) r = consumeToken(b, K_GLOBAL);
    return r;
  }

  // ('as' typeLiteral)?
  private static boolean variableDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableDeclaration_2")) return false;
    variableDeclaration_2_0(b, l + 1);
    return true;
  }

  // 'as' typeLiteral
  private static boolean variableDeclaration_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableDeclaration_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, K_AS);
    r = r && typeLiteral(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ('=' initializer)?
  private static boolean variableDeclaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableDeclaration_3")) return false;
    variableDeclaration_3_0(b, l + 1);
    return true;
  }

  // '=' initializer
  private static boolean variableDeclaration_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableDeclaration_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_ASSIGN);
    r = r && initializer(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'while' '(' expression ')' statement
  public static boolean whileStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "whileStatement")) return false;
    if (!nextTokenIs(b, K_WHILE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, K_WHILE, PAREN_OPEN);
    r = r && expression(b, l + 1, -1);
    r = r && consumeToken(b, PAREN_CLOSE);
    r = r && statement(b, l + 1);
    exit_section_(b, m, WHILE_STATEMENT, r);
    return r;
  }

  /* ********************************************************** */
  // Expression root: expression
  // Operator priority table:
  // 0: POSTFIX(memberAccessExpression) POSTFIX(arrayIndexExpression) POSTFIX(callExpression) POSTFIX(typeCastExpression)
  //    BINARY(instanceOfExpression) ATOM(primaryExpression)
  // 1: BINARY(rangeExpression)
  // 2: PREFIX(unaryExpression)
  // 3: BINARY(mulExpression)
  // 4: BINARY(addExpression)
  // 5: BINARY(compareExpression)
  // 6: BINARY(andExpression)
  // 7: BINARY(xorExpression)
  // 8: BINARY(orExpression)
  // 9: BINARY(andAndExpression)
  // 10: BINARY(orOrExpression)
  // 11: BINARY(conditionalExpression)
  // 12: BINARY(assignmentExpression)
  public static boolean expression(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expression")) return false;
    addVariant(b, "<expression>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<expression>");
    r = primaryExpression(b, l + 1);
    if (!r) r = unaryExpression(b, l + 1);
    p = r;
    r = r && expression_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean expression_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expression_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 0 && memberAccessExpression_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, MEMBER_ACCESS_EXPRESSION, r, true, null);
      }
      else if (g < 0 && arrayIndexExpression_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, ARRAY_INDEX_EXPRESSION, r, true, null);
      }
      else if (g < 0 && callExpression_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, CALL_EXPRESSION, r, true, null);
      }
      else if (g < 0 && typeCastExpression_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, TYPE_CAST_EXPRESSION, r, true, null);
      }
      else if (g < 0 && consumeTokenSmart(b, K_INSTANCEOF)) {
        r = expression(b, l, 0);
        exit_section_(b, l, m, INSTANCE_OF_EXPRESSION, r, true, null);
      }
      else if (g < 1 && rangeExpression_0(b, l + 1)) {
        r = expression(b, l, 1);
        exit_section_(b, l, m, RANGE_EXPRESSION, r, true, null);
      }
      else if (g < 3 && mulExpression_0(b, l + 1)) {
        r = expression(b, l, 3);
        exit_section_(b, l, m, MUL_EXPRESSION, r, true, null);
      }
      else if (g < 4 && addExpression_0(b, l + 1)) {
        r = expression(b, l, 4);
        exit_section_(b, l, m, ADD_EXPRESSION, r, true, null);
      }
      else if (g < 5 && compareExpression_0(b, l + 1)) {
        r = expression(b, l, 4);
        exit_section_(b, l, m, COMPARE_EXPRESSION, r, true, null);
      }
      else if (g < 6 && consumeTokenSmart(b, OP_AND)) {
        r = expression(b, l, 6);
        exit_section_(b, l, m, AND_EXPRESSION, r, true, null);
      }
      else if (g < 7 && consumeTokenSmart(b, OP_XOR)) {
        r = expression(b, l, 7);
        exit_section_(b, l, m, XOR_EXPRESSION, r, true, null);
      }
      else if (g < 8 && consumeTokenSmart(b, OP_OR)) {
        r = expression(b, l, 8);
        exit_section_(b, l, m, OR_EXPRESSION, r, true, null);
      }
      else if (g < 9 && consumeTokenSmart(b, OP_AND_AND)) {
        r = expression(b, l, 9);
        exit_section_(b, l, m, AND_AND_EXPRESSION, r, true, null);
      }
      else if (g < 10 && consumeTokenSmart(b, OP_OR_OR)) {
        r = expression(b, l, 10);
        exit_section_(b, l, m, OR_OR_EXPRESSION, r, true, null);
      }
      else if (g < 11 && consumeTokenSmart(b, OP_QUEST)) {
        r = report_error_(b, expression(b, l, 10));
        r = conditionalExpression_1(b, l + 1) && r;
        exit_section_(b, l, m, CONDITIONAL_EXPRESSION, r, true, null);
      }
      else if (g < 12 && assignmentExpression_0(b, l + 1)) {
        r = expression(b, l, 11);
        exit_section_(b, l, m, ASSIGNMENT_EXPRESSION, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // '.' identifier
  private static boolean memberAccessExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "memberAccessExpression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, DOT);
    r = r && identifier(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '[' identifier ']'
  private static boolean arrayIndexExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayIndexExpression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, BRACK_OPEN);
    r = r && identifier(b, l + 1);
    r = r && consumeToken(b, BRACK_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  // '(' arguments ')'
  private static boolean callExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "callExpression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, PAREN_OPEN);
    r = r && arguments(b, l + 1);
    r = r && consumeToken(b, PAREN_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'as' typeLiteral
  private static boolean typeCastExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeCastExpression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, K_AS);
    r = r && typeLiteral(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // INT_LITERAL
  //     | LONG_LITERAL
  //     | FLOAT_LITERAL
  //     | DOUBLE_LITERAL
  //     | STRING_LITERAL
  //     | 'true'
  //     | 'false'
  //     | 'null'
  //     | identifier
  //     | 'function' '(' (parameter (',' parameter)*)? ')' ('as' typeLiteral)? functionBody
  //     | bracketHandler
  //     | '[' (expression (',' expression)*)? ','? ']'
  //     | '{' (mapEntry (',' mapEntry)*)? ','? '}'
  //     | '(' expression ')'
  public static boolean primaryExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PRIMARY_EXPRESSION, "<primary expression>");
    r = consumeTokenSmart(b, INT_LITERAL);
    if (!r) r = consumeTokenSmart(b, LONG_LITERAL);
    if (!r) r = consumeTokenSmart(b, FLOAT_LITERAL);
    if (!r) r = consumeTokenSmart(b, DOUBLE_LITERAL);
    if (!r) r = consumeTokenSmart(b, STRING_LITERAL);
    if (!r) r = consumeTokenSmart(b, K_TRUE);
    if (!r) r = consumeTokenSmart(b, K_FALSE);
    if (!r) r = consumeTokenSmart(b, K_NULL);
    if (!r) r = identifier(b, l + 1);
    if (!r) r = primaryExpression_9(b, l + 1);
    if (!r) r = bracketHandler(b, l + 1);
    if (!r) r = primaryExpression_11(b, l + 1);
    if (!r) r = primaryExpression_12(b, l + 1);
    if (!r) r = primaryExpression_13(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // 'function' '(' (parameter (',' parameter)*)? ')' ('as' typeLiteral)? functionBody
  private static boolean primaryExpression_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_9")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokensSmart(b, 0, K_FUNCTION, PAREN_OPEN);
    r = r && primaryExpression_9_2(b, l + 1);
    r = r && consumeToken(b, PAREN_CLOSE);
    r = r && primaryExpression_9_4(b, l + 1);
    r = r && functionBody(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (parameter (',' parameter)*)?
  private static boolean primaryExpression_9_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_9_2")) return false;
    primaryExpression_9_2_0(b, l + 1);
    return true;
  }

  // parameter (',' parameter)*
  private static boolean primaryExpression_9_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_9_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter(b, l + 1);
    r = r && primaryExpression_9_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' parameter)*
  private static boolean primaryExpression_9_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_9_2_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!primaryExpression_9_2_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "primaryExpression_9_2_0_1", c)) break;
    }
    return true;
  }

  // ',' parameter
  private static boolean primaryExpression_9_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_9_2_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COMMA);
    r = r && parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ('as' typeLiteral)?
  private static boolean primaryExpression_9_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_9_4")) return false;
    primaryExpression_9_4_0(b, l + 1);
    return true;
  }

  // 'as' typeLiteral
  private static boolean primaryExpression_9_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_9_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, K_AS);
    r = r && typeLiteral(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '[' (expression (',' expression)*)? ','? ']'
  private static boolean primaryExpression_11(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_11")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, BRACK_OPEN);
    r = r && primaryExpression_11_1(b, l + 1);
    r = r && primaryExpression_11_2(b, l + 1);
    r = r && consumeToken(b, BRACK_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  // (expression (',' expression)*)?
  private static boolean primaryExpression_11_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_11_1")) return false;
    primaryExpression_11_1_0(b, l + 1);
    return true;
  }

  // expression (',' expression)*
  private static boolean primaryExpression_11_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_11_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expression(b, l + 1, -1);
    r = r && primaryExpression_11_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' expression)*
  private static boolean primaryExpression_11_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_11_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!primaryExpression_11_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "primaryExpression_11_1_0_1", c)) break;
    }
    return true;
  }

  // ',' expression
  private static boolean primaryExpression_11_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_11_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COMMA);
    r = r && expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ','?
  private static boolean primaryExpression_11_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_11_2")) return false;
    consumeTokenSmart(b, COMMA);
    return true;
  }

  // '{' (mapEntry (',' mapEntry)*)? ','? '}'
  private static boolean primaryExpression_12(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_12")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, BRACE_OPEN);
    r = r && primaryExpression_12_1(b, l + 1);
    r = r && primaryExpression_12_2(b, l + 1);
    r = r && consumeToken(b, BRACE_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  // (mapEntry (',' mapEntry)*)?
  private static boolean primaryExpression_12_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_12_1")) return false;
    primaryExpression_12_1_0(b, l + 1);
    return true;
  }

  // mapEntry (',' mapEntry)*
  private static boolean primaryExpression_12_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_12_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = mapEntry(b, l + 1);
    r = r && primaryExpression_12_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' mapEntry)*
  private static boolean primaryExpression_12_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_12_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!primaryExpression_12_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "primaryExpression_12_1_0_1", c)) break;
    }
    return true;
  }

  // ',' mapEntry
  private static boolean primaryExpression_12_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_12_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COMMA);
    r = r && mapEntry(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ','?
  private static boolean primaryExpression_12_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_12_2")) return false;
    consumeTokenSmart(b, COMMA);
    return true;
  }

  // '(' expression ')'
  private static boolean primaryExpression_13(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression_13")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, PAREN_OPEN);
    r = r && expression(b, l + 1, -1);
    r = r && consumeToken(b, PAREN_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'to' | '..'
  private static boolean rangeExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rangeExpression_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, K_TO);
    if (!r) r = consumeTokenSmart(b, OP_DOT_DOT);
    return r;
  }

  public static boolean unaryExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unaryExpression")) return false;
    if (!nextTokenIsSmart(b, OP_NOT, OP_SUB)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = unaryExpression_0(b, l + 1);
    p = r;
    r = p && expression(b, l, 2);
    exit_section_(b, l, m, UNARY_EXPRESSION, r, p, null);
    return r || p;
  }

  // '!' | '-'
  private static boolean unaryExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unaryExpression_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, OP_NOT);
    if (!r) r = consumeTokenSmart(b, OP_SUB);
    return r;
  }

  // '*' | '/' | '%'
  private static boolean mulExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mulExpression_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, OP_MUL);
    if (!r) r = consumeTokenSmart(b, OP_DIV);
    if (!r) r = consumeTokenSmart(b, OP_MOD);
    return r;
  }

  // '+' | '-' | '~'
  private static boolean addExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "addExpression_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, OP_ADD);
    if (!r) r = consumeTokenSmart(b, OP_SUB);
    if (!r) r = consumeTokenSmart(b, OP_CAT);
    return r;
  }

  // '==' | '!=' | '<' | '<=' | '>' | '>=' | 'in' | 'has'
  private static boolean compareExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compareExpression_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, OP_EQUAL);
    if (!r) r = consumeTokenSmart(b, OP_NOT_EQUAL);
    if (!r) r = consumeTokenSmart(b, OP_LESS);
    if (!r) r = consumeTokenSmart(b, OP_LESS_EQUAL);
    if (!r) r = consumeTokenSmart(b, OP_GREATER);
    if (!r) r = consumeTokenSmart(b, OP_GREATER_EQUAL);
    if (!r) r = consumeTokenSmart(b, K_IN);
    if (!r) r = consumeTokenSmart(b, K_HAS);
    return r;
  }

  // ':' expression
  private static boolean conditionalExpression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conditionalExpression_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_COLON);
    r = r && expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '=' | '+=' | '-=' | '*=' | '/=' | '%=' | '~=' | '&=' | '|=' | '^='
  private static boolean assignmentExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignmentExpression_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, OP_ASSIGN);
    if (!r) r = consumeTokenSmart(b, OP_ADD_ASSIGN);
    if (!r) r = consumeTokenSmart(b, OP_SUB_ASSIGN);
    if (!r) r = consumeTokenSmart(b, OP_MUL_ASSIGN);
    if (!r) r = consumeTokenSmart(b, OP_DIV_ASSIGN);
    if (!r) r = consumeTokenSmart(b, OP_MOD_ASSIGN);
    if (!r) r = consumeTokenSmart(b, OP_CAT_ASSIGN);
    if (!r) r = consumeTokenSmart(b, OP_AND_ASSIGN);
    if (!r) r = consumeTokenSmart(b, OP_OR_ASSIGN);
    if (!r) r = consumeTokenSmart(b, OP_XOR_ASSIGN);
    return r;
  }

}
