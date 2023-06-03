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
    create_token_set_(ARRAY_TYPE, CLASS_TYPE, FUNCTION_TYPE, LIST_TYPE,
      MAP_TYPE, PRIMITIVE_TYPE, QUALIFIED_CLASS_TYPE, TYPE),
    create_token_set_(ADD_EXPRESSION, AND_AND_EXPRESSION, AND_EXPRESSION, ARRAY_INDEX_EXPRESSION,
      ARRAY_LITERAL, ASSIGNMENT_EXPRESSION, BRACKET_HANDLER_LITERAL, CALL_EXPRESSION,
      COMPARE_EXPRESSION, CONDITIONAL_EXPRESSION, EXPRESSION, FUNCTION_LITERAL,
      INSTANCE_OF_EXPRESSION, LITERAL_EXPRESSION, MAP_LITERAL, MEMBER_ACCESS_EXPRESSION,
      MUL_EXPRESSION, OR_EXPRESSION, OR_OR_EXPRESSION, PAREN_EXPRESSION,
      PRIMITIVE_LITERAL, RANGE_EXPRESSION, REFERENCE_EXPRESSION, TYPE_CAST_EXPRESSION,
      UNARY_EXPRESSION, XOR_EXPRESSION),
  };

  /* ********************************************************** */
  // expression (',' expression)*
  static boolean argumentList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argumentList")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expression(b, l + 1, -1);
    r = r && argumentList_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' expression)*
  private static boolean argumentList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argumentList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!argumentList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "argumentList_1", c)) break;
    }
    return true;
  }

  // ',' expression
  private static boolean argumentList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argumentList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = ','
    r = r && expression(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '(' argumentList? ')'
  public static boolean arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments")) return false;
    if (!nextTokenIs(b, PAREN_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARGUMENTS, null);
    r = consumeToken(b, PAREN_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, arguments_1(b, l + 1));
    r = p && consumeToken(b, PAREN_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // argumentList?
  private static boolean arguments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arguments_1")) return false;
    argumentList(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '[' expression (',' (expression | &']'))* ']'
  public static boolean arrayLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayLiteral")) return false;
    if (!nextTokenIs(b, BRACK_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARRAY_LITERAL, null);
    r = consumeToken(b, BRACK_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, expression(b, l + 1, -1));
    r = p && report_error_(b, arrayLiteral_2(b, l + 1)) && r;
    r = p && consumeToken(b, BRACK_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' (expression | &']'))*
  private static boolean arrayLiteral_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayLiteral_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!arrayLiteral_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "arrayLiteral_2", c)) break;
    }
    return true;
  }

  // ',' (expression | &']')
  private static boolean arrayLiteral_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayLiteral_2_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && arrayLiteral_2_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // expression | &']'
  private static boolean arrayLiteral_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayLiteral_2_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expression(b, l + 1, -1);
    if (!r) r = arrayLiteral_2_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &']'
  private static boolean arrayLiteral_2_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayLiteral_2_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, BRACK_CLOSE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '{' statement* '}'
  public static boolean blockStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "blockStatement")) return false;
    if (!nextTokenIs(b, BRACE_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, BLOCK_STATEMENT, null);
    r = consumeToken(b, BRACE_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, blockStatement_1(b, l + 1));
    r = p && consumeToken(b, BRACE_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
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
  // '<' (!'>')* '>'
  public static boolean bracketHandlerLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracketHandlerLiteral")) return false;
    if (!nextTokenIs(b, OP_LESS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, BRACKET_HANDLER_LITERAL, null);
    r = consumeToken(b, OP_LESS);
    p = r; // pin = 1
    r = r && report_error_(b, bracketHandlerLiteral_1(b, l + 1));
    r = p && consumeToken(b, OP_GREATER) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (!'>')*
  private static boolean bracketHandlerLiteral_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracketHandlerLiteral_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!bracketHandlerLiteral_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bracketHandlerLiteral_1", c)) break;
    }
    return true;
  }

  // !'>'
  private static boolean bracketHandlerLiteral_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracketHandlerLiteral_1_0")) return false;
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
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, BREAK_STATEMENT, null);
    r = consumeTokens(b, 1, K_BREAK, SEMICOLON);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'zenClass' qualifiedName '{' (variableDeclaration | constructorDeclaration | functionDeclaration)* '}'
  public static boolean classDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classDeclaration")) return false;
    if (!nextTokenIs(b, K_ZEN_CLASS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CLASS_DECLARATION, null);
    r = consumeToken(b, K_ZEN_CLASS);
    p = r; // pin = 1
    r = r && report_error_(b, qualifiedName(b, l + 1));
    r = p && report_error_(b, consumeToken(b, BRACE_OPEN)) && r;
    r = p && report_error_(b, classDeclaration_3(b, l + 1)) && r;
    r = p && consumeToken(b, BRACE_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
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
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONSTRUCTOR_BODY, null);
    r = consumeToken(b, BRACE_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, constructorBody_1(b, l + 1));
    r = p && consumeToken(b, BRACE_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
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
  // 'zenConstructor' parameters constructorBody
  public static boolean constructorDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructorDeclaration")) return false;
    if (!nextTokenIs(b, K_ZEN_CONSTRUCTOR)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONSTRUCTOR_DECLARATION, null);
    r = consumeToken(b, K_ZEN_CONSTRUCTOR);
    p = r; // pin = 1
    r = r && report_error_(b, parameters(b, l + 1));
    r = p && constructorBody(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'continue' ';'
  public static boolean continueStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "continueStatement")) return false;
    if (!nextTokenIs(b, K_CONTINUE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONTINUE_STATEMENT, null);
    r = consumeTokens(b, 1, K_CONTINUE, SEMICOLON);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // statement
  static boolean elseBody(PsiBuilder b, int l) {
    return statement(b, l + 1);
  }

  /* ********************************************************** */
  // '$expand' type '$' identifier parameters ('as' type)? functionBody
  public static boolean expandFunctionDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandFunctionDeclaration")) return false;
    if (!nextTokenIs(b, K_EXPAND)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, EXPAND_FUNCTION_DECLARATION, null);
    r = consumeToken(b, K_EXPAND);
    p = r; // pin = 1
    r = r && report_error_(b, type(b, l + 1, -1));
    r = p && report_error_(b, consumeToken(b, OP_DOLLAR)) && r;
    r = p && report_error_(b, identifier(b, l + 1)) && r;
    r = p && report_error_(b, parameters(b, l + 1)) && r;
    r = p && report_error_(b, expandFunctionDeclaration_5(b, l + 1)) && r;
    r = p && functionBody(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ('as' type)?
  private static boolean expandFunctionDeclaration_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandFunctionDeclaration_5")) return false;
    expandFunctionDeclaration_5_0(b, l + 1);
    return true;
  }

  // 'as' type
  private static boolean expandFunctionDeclaration_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandFunctionDeclaration_5_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, K_AS);
    p = r; // pin = 1
    r = r && type(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // expression ';'
  public static boolean expressionStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expressionStatement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, EXPRESSION_STATEMENT, "<expression statement>");
    r = expression(b, l + 1, -1);
    p = r; // pin = 1
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, p, null);
    return r || p;
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
  // '(' foreachEntryNoParen ')'
  static boolean forEachEntryWithParen(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "forEachEntryWithParen")) return false;
    if (!nextTokenIs(b, PAREN_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, PAREN_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, foreachEntryNoParen(b, l + 1));
    r = p && consumeToken(b, PAREN_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '{' statement* '}'
  public static boolean foreachBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachBody")) return false;
    if (!nextTokenIs(b, BRACE_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FOREACH_BODY, null);
    r = consumeToken(b, BRACE_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, foreachBody_1(b, l + 1));
    r = p && consumeToken(b, BRACE_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
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
  // forEachEntryWithParen | foreachEntryNoParen
  static boolean foreachEntry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachEntry")) return false;
    boolean r;
    r = forEachEntryWithParen(b, l + 1);
    if (!r) r = foreachEntryNoParen(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // simpleVariable (',' simpleVariable)*
  static boolean foreachEntryNoParen(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachEntryNoParen")) return false;
    if (!nextTokenIs(b, "", ID, K_TO)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = simpleVariable(b, l + 1);
    p = r; // pin = 1
    r = r && foreachEntryNoParen_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' simpleVariable)*
  private static boolean foreachEntryNoParen_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachEntryNoParen_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!foreachEntryNoParen_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "foreachEntryNoParen_1", c)) break;
    }
    return true;
  }

  // ',' simpleVariable
  private static boolean foreachEntryNoParen_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachEntryNoParen_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && simpleVariable(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'for' foreachEntry 'in' expression foreachBody
  public static boolean foreachStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachStatement")) return false;
    if (!nextTokenIs(b, K_FOR)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FOREACH_STATEMENT, null);
    r = consumeToken(b, K_FOR);
    p = r; // pin = 1
    r = r && report_error_(b, foreachEntry(b, l + 1));
    r = p && report_error_(b, consumeToken(b, K_IN)) && r;
    r = p && report_error_(b, expression(b, l + 1, -1)) && r;
    r = p && foreachBody(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '{' statement* '}'
  public static boolean functionBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionBody")) return false;
    if (!nextTokenIs(b, BRACE_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_BODY, null);
    r = consumeToken(b, BRACE_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, functionBody_1(b, l + 1));
    r = p && consumeToken(b, BRACE_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
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
  // ('static')? 'function' identifier parameters ('as' type)? functionBody
  public static boolean functionDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclaration")) return false;
    if (!nextTokenIs(b, "<function declaration>", K_FUNCTION, K_STATIC)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_DECLARATION, "<function declaration>");
    r = functionDeclaration_0(b, l + 1);
    r = r && consumeToken(b, K_FUNCTION);
    r = r && identifier(b, l + 1);
    p = r; // pin = 3
    r = r && report_error_(b, parameters(b, l + 1));
    r = p && report_error_(b, functionDeclaration_4(b, l + 1)) && r;
    r = p && functionBody(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ('static')?
  private static boolean functionDeclaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclaration_0")) return false;
    consumeToken(b, K_STATIC);
    return true;
  }

  // ('as' type)?
  private static boolean functionDeclaration_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclaration_4")) return false;
    functionDeclaration_4_0(b, l + 1);
    return true;
  }

  // 'as' type
  private static boolean functionDeclaration_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclaration_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, K_AS);
    r = r && type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'function' parameters ('as' type)? functionBody
  public static boolean functionLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionLiteral")) return false;
    if (!nextTokenIs(b, K_FUNCTION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_LITERAL, null);
    r = consumeToken(b, K_FUNCTION);
    r = r && parameters(b, l + 1);
    p = r; // pin = 2
    r = r && report_error_(b, functionLiteral_2(b, l + 1));
    r = p && functionBody(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ('as' type)?
  private static boolean functionLiteral_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionLiteral_2")) return false;
    functionLiteral_2_0(b, l + 1);
    return true;
  }

  // 'as' type
  private static boolean functionLiteral_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionLiteral_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, K_AS);
    r = r && type(b, l + 1, -1);
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
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IF_STATEMENT, null);
    r = consumeToken(b, K_IF);
    p = r; // pin = 1
    r = r && report_error_(b, expression(b, l + 1, -1));
    r = p && report_error_(b, thenBody(b, l + 1)) && r;
    r = p && ifStatement_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
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
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, K_ELSE);
    p = r; // pin = 1
    r = r && elseBody(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'import' qualifiedName ('as' identifier)? ';'
  public static boolean importDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importDeclaration")) return false;
    if (!nextTokenIs(b, K_IMPORT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_DECLARATION, null);
    r = consumeToken(b, K_IMPORT);
    p = r; // pin = 1
    r = r && report_error_(b, qualifiedName(b, l + 1));
    r = p && report_error_(b, importDeclaration_2(b, l + 1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ('as' identifier)?
  private static boolean importDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importDeclaration_2")) return false;
    importDeclaration_2_0(b, l + 1);
    return true;
  }

  // 'as' identifier
  private static boolean importDeclaration_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importDeclaration_2_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, K_AS);
    p = r; // pin = 1
    r = r && identifier(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
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
  // expression ':' expression
  public static boolean mapEntry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapEntry")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MAP_ENTRY, "<map entry>");
    r = expression(b, l + 1, -1);
    r = r && consumeToken(b, OP_COLON);
    p = r; // pin = 2
    r = r && expression(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '{' mapEntry (',' (mapEntry | &'}'))* '}'
  public static boolean mapLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapLiteral")) return false;
    if (!nextTokenIs(b, BRACE_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MAP_LITERAL, null);
    r = consumeToken(b, BRACE_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, mapEntry(b, l + 1));
    r = p && report_error_(b, mapLiteral_2(b, l + 1)) && r;
    r = p && consumeToken(b, BRACE_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' (mapEntry | &'}'))*
  private static boolean mapLiteral_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapLiteral_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!mapLiteral_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "mapLiteral_2", c)) break;
    }
    return true;
  }

  // ',' (mapEntry | &'}')
  private static boolean mapLiteral_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapLiteral_2_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && mapLiteral_2_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // mapEntry | &'}'
  private static boolean mapLiteral_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapLiteral_2_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = mapEntry(b, l + 1);
    if (!r) r = mapLiteral_2_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &'}'
  private static boolean mapLiteral_2_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapLiteral_2_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, BRACE_CLOSE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // identifier | 'string'
  static boolean memberName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "memberName")) return false;
    boolean r;
    r = identifier(b, l + 1);
    if (!r) r = consumeToken(b, K_STRING);
    return r;
  }

  /* ********************************************************** */
  // identifier ('as' type)? ('=' expression)?
  public static boolean parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter")) return false;
    if (!nextTokenIs(b, "<parameter>", ID, K_TO)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PARAMETER, "<parameter>");
    r = identifier(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, parameter_1(b, l + 1));
    r = p && parameter_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ('as' type)?
  private static boolean parameter_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_1")) return false;
    parameter_1_0(b, l + 1);
    return true;
  }

  // 'as' type
  private static boolean parameter_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, K_AS);
    p = r; // pin = 1
    r = r && type(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ('=' expression)?
  private static boolean parameter_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_2")) return false;
    parameter_2_0(b, l + 1);
    return true;
  }

  // '=' expression
  private static boolean parameter_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_2_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, OP_ASSIGN);
    p = r; // pin = 1
    r = r && expression(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '(' parametersList? ')'
  public static boolean parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameters")) return false;
    if (!nextTokenIs(b, PAREN_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PARAMETERS, null);
    r = consumeToken(b, PAREN_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, parameters_1(b, l + 1));
    r = p && consumeToken(b, PAREN_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // parametersList?
  private static boolean parameters_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameters_1")) return false;
    parametersList(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // parameter (',' parameter)*
  static boolean parametersList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parametersList")) return false;
    if (!nextTokenIs(b, "", ID, K_TO)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter(b, l + 1);
    r = r && parametersList_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' parameter)*
  private static boolean parametersList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parametersList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!parametersList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "parametersList_1", c)) break;
    }
    return true;
  }

  // ',' parameter
  private static boolean parametersList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parametersList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = ','
    r = r && parameter(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // INT_LITERAL
  //     | LONG_LITERAL
  //     | FLOAT_LITERAL
  //     | DOUBLE_LITERAL
  //     | STRING_LITERAL
  //     | 'true'
  //     | 'false'
  //     | 'null'
  public static boolean primitiveLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primitiveLiteral")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PRIMITIVE_LITERAL, "<primitive literal>");
    r = consumeToken(b, INT_LITERAL);
    if (!r) r = consumeToken(b, LONG_LITERAL);
    if (!r) r = consumeToken(b, FLOAT_LITERAL);
    if (!r) r = consumeToken(b, DOUBLE_LITERAL);
    if (!r) r = consumeToken(b, STRING_LITERAL);
    if (!r) r = consumeToken(b, K_TRUE);
    if (!r) r = consumeToken(b, K_FALSE);
    if (!r) r = consumeToken(b, K_NULL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '.' identifier
  public static boolean qualifiedClassType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qualifiedClassType")) return false;
    if (!nextTokenIs(b, DOT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _LEFT_, QUALIFIED_CLASS_TYPE, null);
    r = consumeToken(b, DOT);
    p = r; // pin = 1
    r = r && identifier(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // qualifier? identifier
  public static boolean qualifiedName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qualifiedName")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, QUALIFIED_NAME, "<qualified name>");
    r = qualifiedName_0(b, l + 1);
    p = r; // pin = 1
    r = r && identifier(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // qualifier?
  private static boolean qualifiedName_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qualifiedName_0")) return false;
    qualifier(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (identifier '.')+
  public static boolean qualifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qualifier")) return false;
    if (!nextTokenIs(b, "<qualifier>", ID, K_TO)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, QUALIFIER, "<qualifier>");
    r = qualifier_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!qualifier_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "qualifier", c)) break;
    }
    exit_section_(b, l, m, r, false, null);
    return r;
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
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RETURN_STATEMENT, null);
    r = consumeToken(b, K_RETURN);
    p = r; // pin = 1
    r = r && report_error_(b, returnStatement_1(b, l + 1));
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
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
  static boolean simpleVariable(PsiBuilder b, int l) {
    return identifier(b, l + 1);
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
  static boolean thenBody(PsiBuilder b, int l) {
    return statement(b, l + 1);
  }

  /* ********************************************************** */
  // ('var' | 'val' | 'static' | 'global') identifier ('as' type)? ('=' expression)? ';'
  public static boolean variableDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, VARIABLE_DECLARATION, "<variable declaration>");
    r = variableDeclaration_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, identifier(b, l + 1));
    r = p && report_error_(b, variableDeclaration_2(b, l + 1)) && r;
    r = p && report_error_(b, variableDeclaration_3(b, l + 1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
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

  // ('as' type)?
  private static boolean variableDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableDeclaration_2")) return false;
    variableDeclaration_2_0(b, l + 1);
    return true;
  }

  // 'as' type
  private static boolean variableDeclaration_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableDeclaration_2_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, K_AS);
    p = r; // pin = 1
    r = r && type(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ('=' expression)?
  private static boolean variableDeclaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableDeclaration_3")) return false;
    variableDeclaration_3_0(b, l + 1);
    return true;
  }

  // '=' expression
  private static boolean variableDeclaration_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableDeclaration_3_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, OP_ASSIGN);
    p = r; // pin = 1
    r = r && expression(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'while' '(' expression ')' statement
  public static boolean whileStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "whileStatement")) return false;
    if (!nextTokenIs(b, K_WHILE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, WHILE_STATEMENT, null);
    r = consumeTokens(b, 1, K_WHILE, PAREN_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, expression(b, l + 1, -1));
    r = p && report_error_(b, consumeToken(b, PAREN_CLOSE)) && r;
    r = p && statement(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // Expression root: expression
  // Operator priority table:
  // 0: BINARY(assignmentExpression)
  // 1: BINARY(conditionalExpression)
  // 2: BINARY(orOrExpression)
  // 3: BINARY(andAndExpression)
  // 4: BINARY(orExpression)
  // 5: BINARY(xorExpression)
  // 6: BINARY(andExpression)
  // 7: BINARY(compareExpression)
  // 8: BINARY(addExpression)
  // 9: BINARY(mulExpression)
  // 10: ATOM(unaryExpression)
  // 11: BINARY(rangeExpression)
  // 12: POSTFIX(memberAccessExpression) BINARY(arrayIndexExpression) POSTFIX(callExpression) POSTFIX(typeCastExpression)
  //    POSTFIX(instanceOfExpression) ATOM(referenceExpression) ATOM(literalExpression)
  // 13: ATOM(parenExpression)
  public static boolean expression(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expression")) return false;
    addVariant(b, "<expression>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<expression>");
    r = unaryExpression(b, l + 1);
    if (!r) r = referenceExpression(b, l + 1);
    if (!r) r = literalExpression(b, l + 1);
    if (!r) r = parenExpression(b, l + 1);
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
      if (g < 0 && assignmentExpression_0(b, l + 1)) {
        r = expression(b, l, -1);
        exit_section_(b, l, m, ASSIGNMENT_EXPRESSION, r, true, null);
      }
      else if (g < 1 && consumeTokenSmart(b, OP_QUEST)) {
        r = report_error_(b, expression(b, l, 0));
        r = conditionalExpression_1(b, l + 1) && r;
        exit_section_(b, l, m, CONDITIONAL_EXPRESSION, r, true, null);
      }
      else if (g < 2 && consumeTokenSmart(b, OP_OR_OR)) {
        r = expression(b, l, 2);
        exit_section_(b, l, m, OR_OR_EXPRESSION, r, true, null);
      }
      else if (g < 3 && consumeTokenSmart(b, OP_AND_AND)) {
        r = expression(b, l, 3);
        exit_section_(b, l, m, AND_AND_EXPRESSION, r, true, null);
      }
      else if (g < 4 && consumeTokenSmart(b, OP_OR)) {
        r = expression(b, l, 4);
        exit_section_(b, l, m, OR_EXPRESSION, r, true, null);
      }
      else if (g < 5 && consumeTokenSmart(b, OP_XOR)) {
        r = expression(b, l, 5);
        exit_section_(b, l, m, XOR_EXPRESSION, r, true, null);
      }
      else if (g < 6 && consumeTokenSmart(b, OP_AND)) {
        r = expression(b, l, 6);
        exit_section_(b, l, m, AND_EXPRESSION, r, true, null);
      }
      else if (g < 7 && compareExpression_0(b, l + 1)) {
        r = expression(b, l, 6);
        exit_section_(b, l, m, COMPARE_EXPRESSION, r, true, null);
      }
      else if (g < 8 && addExpression_0(b, l + 1)) {
        r = expression(b, l, 8);
        exit_section_(b, l, m, ADD_EXPRESSION, r, true, null);
      }
      else if (g < 9 && mulExpression_0(b, l + 1)) {
        r = expression(b, l, 9);
        exit_section_(b, l, m, MUL_EXPRESSION, r, true, null);
      }
      else if (g < 11 && rangeExpression_0(b, l + 1)) {
        r = expression(b, l, 11);
        exit_section_(b, l, m, RANGE_EXPRESSION, r, true, null);
      }
      else if (g < 12 && memberAccessExpression_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, MEMBER_ACCESS_EXPRESSION, r, true, null);
      }
      else if (g < 12 && consumeTokenSmart(b, BRACK_OPEN)) {
        r = report_error_(b, expression(b, l, 12));
        r = consumeToken(b, BRACK_CLOSE) && r;
        exit_section_(b, l, m, ARRAY_INDEX_EXPRESSION, r, true, null);
      }
      else if (g < 12 && arguments(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, CALL_EXPRESSION, r, true, null);
      }
      else if (g < 12 && typeCastExpression_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, TYPE_CAST_EXPRESSION, r, true, null);
      }
      else if (g < 12 && instanceOfExpression_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, INSTANCE_OF_EXPRESSION, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
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

  // '+' | '-' | '~'
  private static boolean addExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "addExpression_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, OP_ADD);
    if (!r) r = consumeTokenSmart(b, OP_SUB);
    if (!r) r = consumeTokenSmart(b, OP_CAT);
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

  // ('!' | '-') expression
  public static boolean unaryExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unaryExpression")) return false;
    if (!nextTokenIsSmart(b, OP_NOT, OP_SUB)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _COLLAPSE_, UNARY_EXPRESSION, "<unary expression>");
    r = unaryExpression_0(b, l + 1);
    p = r; // pin = 1
    r = r && expression(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
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

  // 'to' | '..'
  private static boolean rangeExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rangeExpression_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, K_TO);
    if (!r) r = consumeTokenSmart(b, OP_DOT_DOT);
    return r;
  }

  // '.' memberName
  private static boolean memberAccessExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "memberAccessExpression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, DOT);
    r = r && memberName(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'as' type
  private static boolean typeCastExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeCastExpression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, K_AS);
    r = r && type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'instanceof' type
  private static boolean instanceOfExpression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instanceOfExpression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, K_INSTANCEOF);
    r = r && type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // identifier | primitiveType
  public static boolean referenceExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "referenceExpression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, REFERENCE_EXPRESSION, "<reference expression>");
    r = identifier(b, l + 1);
    if (!r) r = primitiveType(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // arrayLiteral | mapLiteral | functionLiteral | bracketHandlerLiteral | primitiveLiteral
  public static boolean literalExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literalExpression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, LITERAL_EXPRESSION, "<literal expression>");
    r = arrayLiteral(b, l + 1);
    if (!r) r = mapLiteral(b, l + 1);
    if (!r) r = functionLiteral(b, l + 1);
    if (!r) r = bracketHandlerLiteral(b, l + 1);
    if (!r) r = primitiveLiteral(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '(' expression ')'
  public static boolean parenExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parenExpression")) return false;
    if (!nextTokenIsSmart(b, PAREN_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PAREN_EXPRESSION, null);
    r = consumeTokenSmart(b, PAREN_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, expression(b, l + 1, -1));
    r = p && consumeToken(b, PAREN_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // Expression root: type
  // Operator priority table:
  // 0: ATOM(primitiveType)
  // 1: POSTFIX(arrayType)
  // 2: BINARY(mapType)
  // 3: ATOM(listType)
  // 4: ATOM(classType)
  // 5: ATOM(functionType)
  public static boolean type(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "type")) return false;
    addVariant(b, "<type>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<type>");
    r = primitiveType(b, l + 1);
    if (!r) r = listType(b, l + 1);
    if (!r) r = classType(b, l + 1);
    if (!r) r = functionType(b, l + 1);
    p = r;
    r = r && type_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean type_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "type_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 1 && parseTokensSmart(b, 0, BRACK_OPEN, BRACK_CLOSE)) {
        r = true;
        exit_section_(b, l, m, ARRAY_TYPE, r, true, null);
      }
      else if (g < 2 && consumeTokenSmart(b, BRACK_OPEN)) {
        r = report_error_(b, type(b, l, 2));
        r = consumeToken(b, BRACK_CLOSE) && r;
        exit_section_(b, l, m, MAP_TYPE, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // 'any'
  //   | 'byte'
  //   | 'short'
  //   | 'int'
  //   | 'long'
  //   | 'float'
  //   | 'double'
  //   | 'bool'
  //   | 'void'
  //   | 'string'
  public static boolean primitiveType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primitiveType")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PRIMITIVE_TYPE, "<primitive type>");
    r = consumeTokenSmart(b, K_ANY);
    if (!r) r = consumeTokenSmart(b, K_BYTE);
    if (!r) r = consumeTokenSmart(b, K_SHORT);
    if (!r) r = consumeTokenSmart(b, K_INT);
    if (!r) r = consumeTokenSmart(b, K_LONG);
    if (!r) r = consumeTokenSmart(b, K_FLOAT);
    if (!r) r = consumeTokenSmart(b, K_DOUBLE);
    if (!r) r = consumeTokenSmart(b, K_BOOL);
    if (!r) r = consumeTokenSmart(b, K_VOID);
    if (!r) r = consumeTokenSmart(b, K_STRING);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '[' type ']'
  public static boolean listType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "listType")) return false;
    if (!nextTokenIsSmart(b, BRACK_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LIST_TYPE, null);
    r = consumeTokenSmart(b, BRACK_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, type(b, l + 1, -1));
    r = p && consumeToken(b, BRACK_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // identifier (qualifiedClassType)*
  public static boolean classType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classType")) return false;
    if (!nextTokenIsSmart(b, ID, K_TO)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, CLASS_TYPE, "<class type>");
    r = identifier(b, l + 1);
    r = r && classType_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (qualifiedClassType)*
  private static boolean classType_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classType_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!classType_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "classType_1", c)) break;
    }
    return true;
  }

  // (qualifiedClassType)
  private static boolean classType_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classType_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = qualifiedClassType(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'function' '(' (type (',' type)*)? ')' type
  public static boolean functionType(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionType")) return false;
    if (!nextTokenIsSmart(b, K_FUNCTION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_TYPE, null);
    r = consumeTokensSmart(b, 1, K_FUNCTION, PAREN_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, functionType_2(b, l + 1));
    r = p && report_error_(b, consumeToken(b, PAREN_CLOSE)) && r;
    r = p && type(b, l + 1, -1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (type (',' type)*)?
  private static boolean functionType_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionType_2")) return false;
    functionType_2_0(b, l + 1);
    return true;
  }

  // type (',' type)*
  private static boolean functionType_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionType_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type(b, l + 1, -1);
    r = r && functionType_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' type)*
  private static boolean functionType_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionType_2_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!functionType_2_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "functionType_2_0_1", c)) break;
    }
    return true;
  }

  // ',' type
  private static boolean functionType_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionType_2_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COMMA);
    r = r && type(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

}
