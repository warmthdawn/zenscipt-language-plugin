// This is a generated file. Not intended for manual editing.
package com.warmthdawn.zenscript.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.warmthdawn.zenscript.psi.ZenScriptTypes.*;
import static com.warmthdawn.zenscript.psi.ZenScriptTypes.TokenSets.*;
import static com.warmthdawn.zenscript.parser.ZenScriptParserUtil.*;
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
    create_token_set_(IDENTIFIER, MEMBER_NAME),
    create_token_set_(ARRAY_TYPE_REF, CLASS_TYPE_REF, FUNCTION_TYPE_REF, LIST_TYPE_REF,
      MAP_TYPE_REF, PRIMITIVE_TYPE_REF, TYPE_REF),
    create_token_set_(BLOCK_STATEMENT, BREAK_STATEMENT, CONTINUE_STATEMENT, EXPRESSION_STATEMENT,
      FOREACH_STATEMENT, IF_STATEMENT, RETURN_STATEMENT, STATEMENT,
      VARIABLE_DECLARATION, WHILE_STATEMENT),
    create_token_set_(ADD_EXPRESSION, AND_AND_EXPRESSION, AND_EXPRESSION, ARRAY_INDEX_EXPRESSION,
      ARRAY_LITERAL, ASSIGNMENT_EXPRESSION, BRACKET_HANDLER_LITERAL, CALL_EXPRESSION,
      COMPARE_EXPRESSION, CONDITIONAL_EXPRESSION, EXPRESSION, FUNCTION_LITERAL,
      INSTANCE_OF_EXPRESSION, LITERAL_EXPRESSION, LOCAL_ACCESS_EXPRESSION, MAP_LITERAL,
      MEMBER_ACCESS_EXPRESSION, MUL_EXPRESSION, OR_EXPRESSION, OR_OR_EXPRESSION,
      PAREN_EXPRESSION, POSTFIX_EXPRESSION, PRIMITIVE_LITERAL, RANGE_EXPRESSION,
      TYPE_CAST_EXPRESSION, UNARY_EXPRESSION, XOR_EXPRESSION),
  };

  /* ********************************************************** */
  // '+' | '-' | '~'
  static boolean addOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "addOp")) return false;
    boolean r;
    r = consumeToken(b, ADD_OP_TOKENS);
    return r;
  }

  /* ********************************************************** */
  // binaryOP | unaryOp | ':' | '.' '?'
  static boolean allOP(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "allOP")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = binaryOP(b, l + 1);
    if (!r) r = unaryOp(b, l + 1);
    if (!r) r = consumeToken(b, OP_COLON);
    if (!r) r = parseTokens(b, 0, DOT, OP_QUEST);
    exit_section_(b, m, null, r);
    return r;
  }

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
  // '[' expression ']'
  public static boolean arrayIndexExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayIndexExpression")) return false;
    if (!nextTokenIs(b, BRACK_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _LEFT_, ARRAY_INDEX_EXPRESSION, null);
    r = consumeToken(b, BRACK_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, expression(b, l + 1, -1));
    r = p && consumeToken(b, BRACK_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '[' (!']' expression (',' (expression | &']'))*)? ']'
  public static boolean arrayLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayLiteral")) return false;
    if (!nextTokenIs(b, BRACK_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARRAY_LITERAL, null);
    r = consumeToken(b, BRACK_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, arrayLiteral_1(b, l + 1));
    r = p && consumeToken(b, BRACK_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (!']' expression (',' (expression | &']'))*)?
  private static boolean arrayLiteral_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayLiteral_1")) return false;
    arrayLiteral_1_0(b, l + 1);
    return true;
  }

  // !']' expression (',' (expression | &']'))*
  private static boolean arrayLiteral_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayLiteral_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = arrayLiteral_1_0_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, expression(b, l + 1, -1));
    r = p && arrayLiteral_1_0_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // !']'
  private static boolean arrayLiteral_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayLiteral_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, BRACK_CLOSE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (',' (expression | &']'))*
  private static boolean arrayLiteral_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayLiteral_1_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!arrayLiteral_1_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "arrayLiteral_1_0_2", c)) break;
    }
    return true;
  }

  // ',' (expression | &']')
  private static boolean arrayLiteral_1_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayLiteral_1_0_2_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && arrayLiteral_1_0_2_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // expression | &']'
  private static boolean arrayLiteral_1_0_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayLiteral_1_0_2_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expression(b, l + 1, -1);
    if (!r) r = arrayLiteral_1_0_2_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &']'
  private static boolean arrayLiteral_1_0_2_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arrayLiteral_1_0_2_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, BRACK_CLOSE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '=' | '+=' | '-=' | '*=' | '/=' | '%=' | '~=' | '&=' | '|=' | '^='
  static boolean assignmentOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignmentOp")) return false;
    boolean r;
    r = consumeToken(b, ASSIGNMENT_OP_TOKENS);
    return r;
  }

  /* ********************************************************** */
  // rangeOp | mulOp |  addOp | compareOP | assignmentOp | '&' | '^' | '|' |  '&&' |  '||'
  static boolean binaryOP(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binaryOP")) return false;
    boolean r;
    r = rangeOp(b, l + 1);
    if (!r) r = mulOp(b, l + 1);
    if (!r) r = addOp(b, l + 1);
    if (!r) r = compareOP(b, l + 1);
    if (!r) r = assignmentOp(b, l + 1);
    if (!r) r = consumeToken(b, OP_AND);
    if (!r) r = consumeToken(b, OP_XOR);
    if (!r) r = consumeToken(b, OP_OR);
    if (!r) r = consumeToken(b, OP_AND_AND);
    if (!r) r = consumeToken(b, OP_OR_OR);
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
  // '<' (!'>' <<any>>)+ '>'
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

  // (!'>' <<any>>)+
  private static boolean bracketHandlerLiteral_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracketHandlerLiteral_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = bracketHandlerLiteral_1_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!bracketHandlerLiteral_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bracketHandlerLiteral_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // !'>' <<any>>
  private static boolean bracketHandlerLiteral_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracketHandlerLiteral_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = bracketHandlerLiteral_1_0_0(b, l + 1);
    p = r; // pin = 1
    r = r && any(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // !'>'
  private static boolean bracketHandlerLiteral_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bracketHandlerLiteral_1_0_0")) return false;
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
  // arguments
  public static boolean callExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "callExpression")) return false;
    if (!nextTokenIs(b, PAREN_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, CALL_EXPRESSION, null);
    r = arguments(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // variableDeclaration | constructorDeclaration | functionDeclaration
  static boolean classBodyItem(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classBodyItem")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = variableDeclaration(b, l + 1);
    if (!r) r = constructorDeclaration(b, l + 1);
    if (!r) r = functionDeclaration(b, l + 1);
    exit_section_(b, l, m, r, false, ZenScriptParser::class_body_recover);
    return r;
  }

  /* ********************************************************** */
  // 'zenClass' qualifiedName '{' classBodyItem* '}'
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

  // classBodyItem*
  private static boolean classDeclaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classDeclaration_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!classBodyItem(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "classDeclaration_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !('}' | 'function' | 'global' | 'static' | 'val' | 'var' | 'zenConstructor')
  static boolean class_body_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !class_body_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '}' | 'function' | 'global' | 'static' | 'val' | 'var' | 'zenConstructor'
  private static boolean class_body_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_body_recover_0")) return false;
    boolean r;
    r = consumeToken(b, BRACE_CLOSE);
    if (!r) r = consumeToken(b, K_FUNCTION);
    if (!r) r = consumeToken(b, K_GLOBAL);
    if (!r) r = consumeToken(b, K_STATIC);
    if (!r) r = consumeToken(b, K_VAL);
    if (!r) r = consumeToken(b, K_VAR);
    if (!r) r = consumeToken(b, K_ZEN_CONSTRUCTOR);
    return r;
  }

  /* ********************************************************** */
  // '==' | '!=' | '<' | '<=' | '>' | '>=' | 'in' | 'has'
  static boolean compareOP(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compareOP")) return false;
    boolean r;
    r = consumeToken(b, COMPARE_OP_TOKENS);
    return r;
  }

  /* ********************************************************** */
  // '?' expression ':' expression
  static boolean conditionalTail(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conditionalTail")) return false;
    if (!nextTokenIs(b, OP_QUEST)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, OP_QUEST);
    p = r; // pin = 1
    r = r && report_error_(b, expression(b, l + 1, -1));
    r = p && report_error_(b, consumeToken(b, OP_COLON)) && r;
    r = p && expression(b, l + 1, -1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'zenConstructor' parameters functionBody
  public static boolean constructorDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructorDeclaration")) return false;
    if (!nextTokenIs(b, K_ZEN_CONSTRUCTOR)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONSTRUCTOR_DECLARATION, null);
    r = consumeToken(b, K_ZEN_CONSTRUCTOR);
    p = r; // pin = 1
    r = r && report_error_(b, parameters(b, l + 1));
    r = p && functionBody(b, l + 1) && r;
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
  // '$expand' typeRef '$' identifier parameters (funcReturnTypeDef | &'{') functionBody
  public static boolean expandFunctionDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandFunctionDeclaration")) return false;
    if (!nextTokenIs(b, K_EXPAND)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, EXPAND_FUNCTION_DECLARATION, null);
    r = consumeToken(b, K_EXPAND);
    p = r; // pin = 1
    r = r && report_error_(b, typeRef(b, l + 1, -1));
    r = p && report_error_(b, consumeToken(b, OP_DOLLAR)) && r;
    r = p && report_error_(b, identifier(b, l + 1)) && r;
    r = p && report_error_(b, parameters(b, l + 1)) && r;
    r = p && report_error_(b, expandFunctionDeclaration_5(b, l + 1)) && r;
    r = p && functionBody(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // funcReturnTypeDef | &'{'
  private static boolean expandFunctionDeclaration_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandFunctionDeclaration_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = funcReturnTypeDef(b, l + 1);
    if (!r) r = expandFunctionDeclaration_5_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &'{'
  private static boolean expandFunctionDeclaration_5_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandFunctionDeclaration_5_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, BRACE_OPEN);
    exit_section_(b, l, m, r, false, null);
    return r;
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
  // !(allOP | statement_start | ')' | ',' | ':' | ';'| ']' | 'as' | 'else' | 'instanceof' | '}')
  static boolean expression_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !expression_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // allOP | statement_start | ')' | ',' | ':' | ';'| ']' | 'as' | 'else' | 'instanceof' | '}'
  private static boolean expression_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_recover_0")) return false;
    boolean r;
    r = allOP(b, l + 1);
    if (!r) r = statement_start(b, l + 1);
    if (!r) r = consumeToken(b, PAREN_CLOSE);
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, OP_COLON);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, BRACK_CLOSE);
    if (!r) r = consumeToken(b, K_AS);
    if (!r) r = consumeToken(b, K_ELSE);
    if (!r) r = consumeToken(b, K_INSTANCEOF);
    if (!r) r = consumeToken(b, BRACE_CLOSE);
    return r;
  }

  /* ********************************************************** */
  // '!' | '(' | '-' | '<' | '[' | 'any' | 'bool' | 'byte' | 'double' | 'false' | 'float' | 'function' | 'int' | 'long' | 'null' | 'short' | 'string' | 'to' | 'true' | 'void' | '{' | DOUBLE_LITERAL | FLOAT_LITERAL | ID | INT_LITERAL | LONG_LITERAL | STRING_LITERAL
  static boolean expression_start(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_start")) return false;
    boolean r;
    r = consumeToken(b, EXPRESSION_START_TOKENS);
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
  // !(',' | 'in' | '{' | ';')
  static boolean for_each_entry_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_each_entry_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !for_each_entry_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ',' | 'in' | '{' | ';'
  private static boolean for_each_entry_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_each_entry_recover_0")) return false;
    boolean r;
    r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, K_IN);
    if (!r) r = consumeToken(b, BRACE_OPEN);
    if (!r) r = consumeToken(b, SEMICOLON);
    return r;
  }

  /* ********************************************************** */
  // foreachVariableDeclaration (',' foreachVariableDeclaration)*
  static boolean foreachEntry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachEntry")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = foreachVariableDeclaration(b, l + 1);
    p = r; // pin = 1
    r = r && foreachEntry_1(b, l + 1);
    exit_section_(b, l, m, r, p, ZenScriptParser::for_each_entry_recover);
    return r || p;
  }

  // (',' foreachVariableDeclaration)*
  private static boolean foreachEntry_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachEntry_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!foreachEntry_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "foreachEntry_1", c)) break;
    }
    return true;
  }

  // ',' foreachVariableDeclaration
  private static boolean foreachEntry_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachEntry_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && foreachVariableDeclaration(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'for' foreachEntry 'in' expression statement
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
    r = p && statement(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // identifier
  public static boolean foreachVariableDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreachVariableDeclaration")) return false;
    if (!nextTokenIs(b, "<foreach variable declaration>", ID, K_TO)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FOREACH_VARIABLE_DECLARATION, "<foreach variable declaration>");
    r = identifier(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'as' typeRef
  static boolean funcReturnTypeDef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "funcReturnTypeDef")) return false;
    if (!nextTokenIs(b, K_AS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, K_AS);
    p = r; // pin = 1
    r = r && typeRef(b, l + 1, -1);
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
  // ('static')? 'function' !'(' identifier parameters (funcReturnTypeDef | &'{') functionBody
  public static boolean functionDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclaration")) return false;
    if (!nextTokenIs(b, "<function declaration>", K_FUNCTION, K_STATIC)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_DECLARATION, "<function declaration>");
    r = functionDeclaration_0(b, l + 1);
    r = r && consumeToken(b, K_FUNCTION);
    r = r && functionDeclaration_2(b, l + 1);
    p = r; // pin = 3
    r = r && report_error_(b, identifier(b, l + 1));
    r = p && report_error_(b, parameters(b, l + 1)) && r;
    r = p && report_error_(b, functionDeclaration_5(b, l + 1)) && r;
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

  // !'('
  private static boolean functionDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclaration_2")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, PAREN_OPEN);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // funcReturnTypeDef | &'{'
  private static boolean functionDeclaration_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclaration_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = funcReturnTypeDef(b, l + 1);
    if (!r) r = functionDeclaration_5_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &'{'
  private static boolean functionDeclaration_5_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionDeclaration_5_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, BRACE_OPEN);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'function' parameters funcReturnTypeDef? functionBody
  public static boolean functionLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionLiteral")) return false;
    if (!nextTokenIs(b, K_FUNCTION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_LITERAL, null);
    r = consumeToken(b, K_FUNCTION);
    p = r; // pin = 1
    r = r && report_error_(b, parameters(b, l + 1));
    r = p && report_error_(b, functionLiteral_2(b, l + 1)) && r;
    r = p && functionBody(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // funcReturnTypeDef?
  private static boolean functionLiteral_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionLiteral_2")) return false;
    funcReturnTypeDef(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // typeRef (',' typeRef)*
  static boolean functionParamsTypeRef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionParamsTypeRef")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = typeRef(b, l + 1, -1);
    p = r; // pin = 1
    r = r && functionParamsTypeRef_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' typeRef)*
  private static boolean functionParamsTypeRef_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionParamsTypeRef_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!functionParamsTypeRef_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "functionParamsTypeRef_1", c)) break;
    }
    return true;
  }

  // ',' typeRef
  private static boolean functionParamsTypeRef_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionParamsTypeRef_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && typeRef(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
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
  // 'if' expression statement ('else' statement)?
  public static boolean ifStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifStatement")) return false;
    if (!nextTokenIs(b, K_IF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IF_STATEMENT, null);
    r = consumeToken(b, K_IF);
    p = r; // pin = 1
    r = r && report_error_(b, expression(b, l + 1, -1));
    r = p && report_error_(b, statement(b, l + 1)) && r;
    r = p && ifStatement_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ('else' statement)?
  private static boolean ifStatement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifStatement_3")) return false;
    ifStatement_3_0(b, l + 1);
    return true;
  }

  // 'else' statement
  private static boolean ifStatement_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ifStatement_3_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, K_ELSE);
    p = r; // pin = 1
    r = r && statement(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'import' qualifiedName ('as' identifier)? ';'
  public static boolean importDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_DECLARATION, "<import declaration>");
    r = consumeToken(b, K_IMPORT);
    p = r; // pin = 1
    r = r && report_error_(b, qualifiedName(b, l + 1));
    r = p && report_error_(b, importDeclaration_2(b, l + 1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, ZenScriptParser::import_recover);
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
  // importDeclaration*
  public static boolean importList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importList")) return false;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_LIST, "<import list>");
    while (true) {
      int c = current_position_(b);
      if (!importDeclaration(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "importList", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // !('$expand' | 'function' | 'import' | 'static' | 'zenClass' | ';' | statement_start)
  static boolean import_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !import_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '$expand' | 'function' | 'import' | 'static' | 'zenClass' | ';' | statement_start
  private static boolean import_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_recover_0")) return false;
    boolean r;
    r = consumeToken(b, K_EXPAND);
    if (!r) r = consumeToken(b, K_FUNCTION);
    if (!r) r = consumeToken(b, K_IMPORT);
    if (!r) r = consumeToken(b, K_STATIC);
    if (!r) r = consumeToken(b, K_ZEN_CLASS);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = statement_start(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // expression
  public static boolean initializerOrDefault(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "initializerOrDefault")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INITIALIZER_OR_DEFAULT, "<initializer or default>");
    r = expression(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'instanceof' typeRef
  public static boolean instanceOfExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instanceOfExpression")) return false;
    if (!nextTokenIs(b, K_INSTANCEOF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _LEFT_, INSTANCE_OF_EXPRESSION, null);
    r = consumeToken(b, K_INSTANCEOF);
    p = r; // pin = 1
    r = r && typeRef(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
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

  /* ********************************************************** */
  // identifier | primitiveTypeRef
  public static boolean localAccessExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "localAccessExpression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LOCAL_ACCESS_EXPRESSION, "<local access expression>");
    r = identifier(b, l + 1);
    if (!r) r = primitiveTypeRef(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // expression ':' expression
  public static boolean mapEntry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapEntry")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MAP_ENTRY, "<map entry>");
    r = expression(b, l + 1, -1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, OP_COLON));
    r = p && expression(b, l + 1, -1) && r;
    exit_section_(b, l, m, r, p, ZenScriptParser::map_literal_recover);
    return r || p;
  }

  /* ********************************************************** */
  // '{' (!'}' mapEntry (',' (mapEntry | &'}'))*)? '}'
  public static boolean mapLiteral(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapLiteral")) return false;
    if (!nextTokenIs(b, BRACE_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MAP_LITERAL, null);
    r = consumeToken(b, BRACE_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, mapLiteral_1(b, l + 1));
    r = p && consumeToken(b, BRACE_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (!'}' mapEntry (',' (mapEntry | &'}'))*)?
  private static boolean mapLiteral_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapLiteral_1")) return false;
    mapLiteral_1_0(b, l + 1);
    return true;
  }

  // !'}' mapEntry (',' (mapEntry | &'}'))*
  private static boolean mapLiteral_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapLiteral_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = mapLiteral_1_0_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, mapEntry(b, l + 1));
    r = p && mapLiteral_1_0_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // !'}'
  private static boolean mapLiteral_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapLiteral_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, BRACE_CLOSE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (',' (mapEntry | &'}'))*
  private static boolean mapLiteral_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapLiteral_1_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!mapLiteral_1_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "mapLiteral_1_0_2", c)) break;
    }
    return true;
  }

  // ',' (mapEntry | &'}')
  private static boolean mapLiteral_1_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapLiteral_1_0_2_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && mapLiteral_1_0_2_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // mapEntry | &'}'
  private static boolean mapLiteral_1_0_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapLiteral_1_0_2_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = mapEntry(b, l + 1);
    if (!r) r = mapLiteral_1_0_2_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &'}'
  private static boolean mapLiteral_1_0_2_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mapLiteral_1_0_2_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, BRACE_CLOSE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(',' | '}') & statement_recover
  static boolean map_literal_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_literal_recover")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = map_literal_recover_0(b, l + 1);
    r = r && map_literal_recover_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !(',' | '}')
  private static boolean map_literal_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_literal_recover_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !map_literal_recover_0_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ',' | '}'
  private static boolean map_literal_recover_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_literal_recover_0_0")) return false;
    boolean r;
    r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, BRACE_CLOSE);
    return r;
  }

  // & statement_recover
  private static boolean map_literal_recover_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_literal_recover_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = statement_recover(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '.' memberName
  public static boolean memberAccessExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "memberAccessExpression")) return false;
    if (!nextTokenIs(b, DOT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _LEFT_, MEMBER_ACCESS_EXPRESSION, null);
    r = consumeToken(b, DOT);
    p = r; // pin = 1
    r = r && memberName(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // identifier | 'string'
  public static boolean memberName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "memberName")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, MEMBER_NAME, "<member name>");
    r = identifier(b, l + 1);
    if (!r) r = consumeToken(b, K_STRING);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '*' | '/' | '%'
  static boolean mulOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mulOp")) return false;
    boolean r;
    r = consumeToken(b, MUL_OP_TOKENS);
    return r;
  }

  /* ********************************************************** */
  // identifier ('as' typeRef)? ('=' initializerOrDefault)?
  public static boolean parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PARAMETER, "<parameter>");
    r = identifier(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, parameter_1(b, l + 1));
    r = p && parameter_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, ZenScriptParser::parameter_recover);
    return r || p;
  }

  // ('as' typeRef)?
  private static boolean parameter_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_1")) return false;
    parameter_1_0(b, l + 1);
    return true;
  }

  // 'as' typeRef
  private static boolean parameter_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, K_AS);
    p = r; // pin = 1
    r = r && typeRef(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ('=' initializerOrDefault)?
  private static boolean parameter_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_2")) return false;
    parameter_2_0(b, l + 1);
    return true;
  }

  // '=' initializerOrDefault
  private static boolean parameter_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_2_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, OP_ASSIGN);
    p = r; // pin = 1
    r = r && initializerOrDefault(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // !(',' | ')' | 'as' | '{' | '}') & script_body_recover
  static boolean parameter_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_recover")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter_recover_0(b, l + 1);
    r = r && parameter_recover_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !(',' | ')' | 'as' | '{' | '}')
  private static boolean parameter_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_recover_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !parameter_recover_0_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ',' | ')' | 'as' | '{' | '}'
  private static boolean parameter_recover_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_recover_0_0")) return false;
    boolean r;
    r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, PAREN_CLOSE);
    if (!r) r = consumeToken(b, K_AS);
    if (!r) r = consumeToken(b, BRACE_OPEN);
    if (!r) r = consumeToken(b, BRACE_CLOSE);
    return r;
  }

  // & script_body_recover
  private static boolean parameter_recover_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_recover_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = script_body_recover(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '(' (!')' parametersList)? ')'
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

  // (!')' parametersList)?
  private static boolean parameters_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameters_1")) return false;
    parameters_1_0(b, l + 1);
    return true;
  }

  // !')' parametersList
  private static boolean parameters_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameters_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = parameters_1_0_0(b, l + 1);
    p = r; // pin = 1
    r = r && parametersList(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // !')'
  private static boolean parameters_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameters_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, PAREN_CLOSE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // parameter (',' parameter)*
  static boolean parametersList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parametersList")) return false;
    if (!nextTokenIs(b, "", ID, K_TO)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = parameter(b, l + 1);
    p = r; // pin = 1
    r = r && parametersList_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
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
    p = r; // pin = 1
    r = r && parameter(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '(' expression ')'
  public static boolean parenExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parenExpression")) return false;
    if (!nextTokenIs(b, PAREN_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PAREN_EXPRESSION, null);
    r = consumeToken(b, PAREN_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, expression(b, l + 1, -1));
    r = p && consumeToken(b, PAREN_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // literalExpression | localAccessExpression | parenExpression
  static boolean primaryExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primaryExpression")) return false;
    boolean r;
    r = literalExpression(b, l + 1);
    if (!r) r = localAccessExpression(b, l + 1);
    if (!r) r = parenExpression(b, l + 1);
    return r;
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
    r = consumeToken(b, PRIMITIVE_LITERAL_TOKENS);
    exit_section_(b, l, m, r, false, null);
    return r;
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
  // 'to' | '..'
  static boolean rangeOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rangeOp")) return false;
    if (!nextTokenIs(b, "", K_TO, OP_DOT_DOT)) return false;
    boolean r;
    r = consumeToken(b, K_TO);
    if (!r) r = consumeToken(b, OP_DOT_DOT);
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
  // topLevelStatement*
  public static boolean scriptBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scriptBody")) return false;
    Marker m = enter_section_(b, l, _NONE_, SCRIPT_BODY, "<script body>");
    while (true) {
      int c = current_position_(b);
      if (!topLevelStatement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scriptBody", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // !('$expand' | 'function' | 'static' | 'zenClass' | statement_start)
  static boolean script_body_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_body_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !script_body_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '$expand' | 'function' | 'static' | 'zenClass' | statement_start
  private static boolean script_body_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_body_recover_0")) return false;
    boolean r;
    r = consumeToken(b, K_EXPAND);
    if (!r) r = consumeToken(b, K_FUNCTION);
    if (!r) r = consumeToken(b, K_STATIC);
    if (!r) r = consumeToken(b, K_ZEN_CLASS);
    if (!r) r = statement_start(b, l + 1);
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
  //     | ';'
  public static boolean statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, STATEMENT, "<statement>");
    r = blockStatement(b, l + 1);
    if (!r) r = returnStatement(b, l + 1);
    if (!r) r = breakStatement(b, l + 1);
    if (!r) r = continueStatement(b, l + 1);
    if (!r) r = ifStatement(b, l + 1);
    if (!r) r = foreachStatement(b, l + 1);
    if (!r) r = whileStatement(b, l + 1);
    if (!r) r = variableDeclaration(b, l + 1);
    if (!r) r = expressionStatement(b, l + 1);
    if (!r) r = consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, false, ZenScriptParser::statement_recover);
    return r;
  }

  /* ********************************************************** */
  // !('}' | 'else' | ';' | '$expand' | 'function' | 'static' | 'zenClass' | statement_start)
  static boolean statement_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !statement_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '}' | 'else' | ';' | '$expand' | 'function' | 'static' | 'zenClass' | statement_start
  private static boolean statement_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_recover_0")) return false;
    boolean r;
    r = consumeToken(b, BRACE_CLOSE);
    if (!r) r = consumeToken(b, K_ELSE);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, K_EXPAND);
    if (!r) r = consumeToken(b, K_FUNCTION);
    if (!r) r = consumeToken(b, K_STATIC);
    if (!r) r = consumeToken(b, K_ZEN_CLASS);
    if (!r) r = statement_start(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // 'break' | 'continue' | 'for' | 'global' | 'if' | 'return' | 'static' | 'val' | 'var' | 'while' | '{' | ';' | expression_start
  static boolean statement_start(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_start")) return false;
    boolean r;
    r = consumeToken(b, K_BREAK);
    if (!r) r = consumeToken(b, K_CONTINUE);
    if (!r) r = consumeToken(b, K_FOR);
    if (!r) r = consumeToken(b, K_GLOBAL);
    if (!r) r = consumeToken(b, K_IF);
    if (!r) r = consumeToken(b, K_RETURN);
    if (!r) r = consumeToken(b, K_STATIC);
    if (!r) r = consumeToken(b, K_VAL);
    if (!r) r = consumeToken(b, K_VAR);
    if (!r) r = consumeToken(b, K_WHILE);
    if (!r) r = consumeToken(b, BRACE_OPEN);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = expression_start(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // functionDeclaration | expandFunctionDeclaration | classDeclaration | statement
  static boolean topLevelStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "topLevelStatement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = functionDeclaration(b, l + 1);
    if (!r) r = expandFunctionDeclaration(b, l + 1);
    if (!r) r = classDeclaration(b, l + 1);
    if (!r) r = statement(b, l + 1);
    exit_section_(b, l, m, r, false, ZenScriptParser::script_body_recover);
    return r;
  }

  /* ********************************************************** */
  // 'as' typeRef
  public static boolean typeCastExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typeCastExpression")) return false;
    if (!nextTokenIs(b, K_AS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _LEFT_, TYPE_CAST_EXPRESSION, null);
    r = consumeToken(b, K_AS);
    p = r; // pin = 1
    r = r && typeRef(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // !('$' | '=' | '[' | ']' | ',' | 'to' | ID | 'function' | primitiveTypeRef) & expression_recover & statement_recover
  static boolean type_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_recover")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_recover_0(b, l + 1);
    r = r && type_recover_1(b, l + 1);
    r = r && type_recover_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !('$' | '=' | '[' | ']' | ',' | 'to' | ID | 'function' | primitiveTypeRef)
  private static boolean type_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_recover_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !type_recover_0_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '$' | '=' | '[' | ']' | ',' | 'to' | ID | 'function' | primitiveTypeRef
  private static boolean type_recover_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_recover_0_0")) return false;
    boolean r;
    r = consumeToken(b, OP_DOLLAR);
    if (!r) r = consumeToken(b, OP_ASSIGN);
    if (!r) r = consumeToken(b, BRACK_OPEN);
    if (!r) r = consumeToken(b, BRACK_CLOSE);
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, K_TO);
    if (!r) r = consumeToken(b, ID);
    if (!r) r = consumeToken(b, K_FUNCTION);
    if (!r) r = primitiveTypeRef(b, l + 1);
    return r;
  }

  // & expression_recover
  private static boolean type_recover_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_recover_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = expression_recover(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // & statement_recover
  private static boolean type_recover_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_recover_2")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = statement_recover(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '!' | '-'
  static boolean unaryOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unaryOp")) return false;
    if (!nextTokenIs(b, "", OP_NOT, OP_SUB)) return false;
    boolean r;
    r = consumeToken(b, OP_NOT);
    if (!r) r = consumeToken(b, OP_SUB);
    return r;
  }

  /* ********************************************************** */
  // ('var' | 'val' | 'static' | 'global') identifier ('as' typeRef)? ('=' initializerOrDefault)? ';'
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

  // ('as' typeRef)?
  private static boolean variableDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableDeclaration_2")) return false;
    variableDeclaration_2_0(b, l + 1);
    return true;
  }

  // 'as' typeRef
  private static boolean variableDeclaration_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableDeclaration_2_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, K_AS);
    p = r; // pin = 1
    r = r && typeRef(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ('=' initializerOrDefault)?
  private static boolean variableDeclaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableDeclaration_3")) return false;
    variableDeclaration_3_0(b, l + 1);
    return true;
  }

  // '=' initializerOrDefault
  private static boolean variableDeclaration_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variableDeclaration_3_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, OP_ASSIGN);
    p = r; // pin = 1
    r = r && initializerOrDefault(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'while' expression statement
  public static boolean whileStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "whileStatement")) return false;
    if (!nextTokenIs(b, K_WHILE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, WHILE_STATEMENT, null);
    r = consumeToken(b, K_WHILE);
    p = r; // pin = 1
    r = r && report_error_(b, expression(b, l + 1, -1));
    r = p && statement(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // Expression root: expression
  // Operator priority table:
  // 0: BINARY(assignmentExpression)
  // 1: POSTFIX(conditionalExpression)
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
  // 12: ATOM(postfixExpression)
  public static boolean expression(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expression")) return false;
    addVariant(b, "<expression>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<expression>");
    r = unaryExpression(b, l + 1);
    if (!r) r = postfixExpression(b, l + 1);
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
      if (g < 0 && assignmentOp(b, l + 1)) {
        r = expression(b, l, -1);
        exit_section_(b, l, m, ASSIGNMENT_EXPRESSION, r, true, null);
      }
      else if (g < 1 && conditionalTail(b, l + 1)) {
        r = true;
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
      else if (g < 7 && compareOP(b, l + 1)) {
        r = expression(b, l, 6);
        exit_section_(b, l, m, COMPARE_EXPRESSION, r, true, null);
      }
      else if (g < 8 && addOp(b, l + 1)) {
        r = expression(b, l, 8);
        exit_section_(b, l, m, ADD_EXPRESSION, r, true, null);
      }
      else if (g < 9 && mulOp(b, l + 1)) {
        r = expression(b, l, 9);
        exit_section_(b, l, m, MUL_EXPRESSION, r, true, null);
      }
      else if (g < 11 && rangeOp(b, l + 1)) {
        r = expression(b, l, 11);
        exit_section_(b, l, m, RANGE_EXPRESSION, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // unaryOp expression
  public static boolean unaryExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unaryExpression")) return false;
    if (!nextTokenIsSmart(b, OP_NOT, OP_SUB)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _COLLAPSE_, UNARY_EXPRESSION, "<unary expression>");
    r = unaryOp(b, l + 1);
    p = r; // pin = 1
    r = r && expression(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // primaryExpression (
  //       memberAccessExpression
  //     | arrayIndexExpression
  //     | callExpression
  //     | typeCastExpression
  //     | instanceOfExpression
  // )*
  public static boolean postfixExpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfixExpression")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _COLLAPSE_, POSTFIX_EXPRESSION, "<postfix expression>");
    r = primaryExpression(b, l + 1);
    p = r; // pin = 1
    r = r && postfixExpression_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (
  //       memberAccessExpression
  //     | arrayIndexExpression
  //     | callExpression
  //     | typeCastExpression
  //     | instanceOfExpression
  // )*
  private static boolean postfixExpression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfixExpression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!postfixExpression_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "postfixExpression_1", c)) break;
    }
    return true;
  }

  // memberAccessExpression
  //     | arrayIndexExpression
  //     | callExpression
  //     | typeCastExpression
  //     | instanceOfExpression
  private static boolean postfixExpression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfixExpression_1_0")) return false;
    boolean r;
    r = memberAccessExpression(b, l + 1);
    if (!r) r = arrayIndexExpression(b, l + 1);
    if (!r) r = callExpression(b, l + 1);
    if (!r) r = typeCastExpression(b, l + 1);
    if (!r) r = instanceOfExpression(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // Expression root: typeRef
  // Operator priority table:
  // 0: ATOM(primitiveTypeRef)
  // 1: POSTFIX(arrayTypeRef) BINARY(mapTypeRef)
  // 2: ATOM(listTypeRef)
  // 3: ATOM(classTypeRef)
  // 4: ATOM(functionTypeRef)
  public static boolean typeRef(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "typeRef")) return false;
    addVariant(b, "<type ref>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<type ref>");
    r = primitiveTypeRef(b, l + 1);
    if (!r) r = listTypeRef(b, l + 1);
    if (!r) r = classTypeRef(b, l + 1);
    if (!r) r = functionTypeRef(b, l + 1);
    p = r;
    r = r && typeRef_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean typeRef_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "typeRef_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 1 && parseTokensSmart(b, 0, BRACK_OPEN, BRACK_CLOSE)) {
        r = true;
        exit_section_(b, l, m, ARRAY_TYPE_REF, r, true, null);
      }
      else if (g < 1 && consumeTokenSmart(b, BRACK_OPEN)) {
        r = report_error_(b, typeRef(b, l, 0));
        r = consumeToken(b, BRACK_CLOSE) && r;
        exit_section_(b, l, m, MAP_TYPE_REF, r, true, null);
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
  public static boolean primitiveTypeRef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primitiveTypeRef")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PRIMITIVE_TYPE_REF, "<primitive type ref>");
    r = consumeTokenSmart(b, PRIMITIVE_TYPE_REF_TOKENS);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '[' typeRef ']'
  public static boolean listTypeRef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "listTypeRef")) return false;
    if (!nextTokenIsSmart(b, BRACK_OPEN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LIST_TYPE_REF, null);
    r = consumeTokenSmart(b, BRACK_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, typeRef(b, l + 1, -1));
    r = p && consumeToken(b, BRACK_CLOSE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // qualifiedName
  public static boolean classTypeRef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "classTypeRef")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CLASS_TYPE_REF, "<class type ref>");
    r = qualifiedName(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // 'function' '(' functionParamsTypeRef? ')' typeRef
  public static boolean functionTypeRef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionTypeRef")) return false;
    if (!nextTokenIsSmart(b, K_FUNCTION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_TYPE_REF, null);
    r = consumeTokensSmart(b, 1, K_FUNCTION, PAREN_OPEN);
    p = r; // pin = 1
    r = r && report_error_(b, functionTypeRef_2(b, l + 1));
    r = p && report_error_(b, consumeToken(b, PAREN_CLOSE)) && r;
    r = p && typeRef(b, l + 1, -1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // functionParamsTypeRef?
  private static boolean functionTypeRef_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "functionTypeRef_2")) return false;
    functionParamsTypeRef(b, l + 1);
    return true;
  }

}
