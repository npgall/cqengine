// Generated from com\googlecode\cqengine\query\parser\cqn\grammar\CQNGrammar.g4 by ANTLR 4.7
package com.googlecode.cqengine.query.parser.cqn.grammar;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CQNGrammarParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, IntegerLiteral=26, DecimalIntegerLiteral=27, FloatingPointLiteral=28, 
		DecimalFloatingPointLiteral=29, ABSTRACT=30, ASSERT=31, BOOLEAN=32, BREAK=33, 
		BYTE=34, CASE=35, CATCH=36, CHAR=37, CLASS=38, CONST=39, CONTINUE=40, 
		DEFAULT=41, DO=42, DOUBLE=43, ELSE=44, ENUM=45, EXTENDS=46, FINAL=47, 
		FINALLY=48, FLOAT=49, FOR=50, IF=51, GOTO=52, IMPLEMENTS=53, IMPORT=54, 
		INSTANCEOF=55, INT=56, INTERFACE=57, LONG=58, NATIVE=59, NEW=60, PACKAGE=61, 
		PRIVATE=62, PROTECTED=63, PUBLIC=64, RETURN=65, SHORT=66, STATIC=67, STRICTFP=68, 
		SUPER=69, SWITCH=70, SYNCHRONIZED=71, THIS=72, THROW=73, THROWS=74, TRANSIENT=75, 
		TRY=76, VOID=77, VOLATILE=78, WHILE=79, BooleanLiteral=80, CharacterLiteral=81, 
		StringLiteral=82, NullLiteral=83, LPAREN=84, RPAREN=85, LBRACE=86, RBRACE=87, 
		LBRACK=88, RBRACK=89, SEMI=90, COMMA=91, DOT=92, ASSIGN=93, GT=94, LT=95, 
		BANG=96, TILDE=97, QUESTION=98, COLON=99, EQUAL=100, LE=101, GE=102, NOTEQUAL=103, 
		AND=104, OR=105, INC=106, DEC=107, ADD=108, SUB=109, MUL=110, DIV=111, 
		BITAND=112, BITOR=113, CARET=114, MOD=115, ADD_ASSIGN=116, SUB_ASSIGN=117, 
		MUL_ASSIGN=118, DIV_ASSIGN=119, AND_ASSIGN=120, OR_ASSIGN=121, XOR_ASSIGN=122, 
		MOD_ASSIGN=123, LSHIFT_ASSIGN=124, RSHIFT_ASSIGN=125, URSHIFT_ASSIGN=126, 
		Identifier=127, AT=128, ELLIPSIS=129, WS=130, COMMENT=131, LINE_COMMENT=132;
	public static final int
		RULE_start = 0, RULE_query = 1, RULE_logicalQuery = 2, RULE_andQuery = 3, 
		RULE_orQuery = 4, RULE_notQuery = 5, RULE_simpleQuery = 6, RULE_equalQuery = 7, 
		RULE_lessThanOrEqualToQuery = 8, RULE_lessThanQuery = 9, RULE_greaterThanOrEqualToQuery = 10, 
		RULE_greaterThanQuery = 11, RULE_verboseBetweenQuery = 12, RULE_betweenQuery = 13, 
		RULE_inQuery = 14, RULE_startsWithQuery = 15, RULE_endsWithQuery = 16, 
		RULE_containsQuery = 17, RULE_isContainedInQuery = 18, RULE_matchesRegexQuery = 19, 
		RULE_hasQuery = 20, RULE_allQuery = 21, RULE_noneQuery = 22, RULE_longestPrefixQuery = 23, 
		RULE_isPrefixOfQuery = 24, RULE_objectType = 25, RULE_attributeName = 26, 
		RULE_queryParameter = 27, RULE_stringQueryParameter = 28, RULE_queryOptions = 29, 
		RULE_queryOption = 30, RULE_orderByOption = 31, RULE_attributeOrder = 32, 
		RULE_direction = 33, RULE_literal = 34, RULE_compilationUnit = 35, RULE_packageDeclaration = 36, 
		RULE_importDeclaration = 37, RULE_typeDeclaration = 38, RULE_modifier = 39, 
		RULE_classOrInterfaceModifier = 40, RULE_variableModifier = 41, RULE_classDeclaration = 42, 
		RULE_typeParameters = 43, RULE_typeParameter = 44, RULE_typeBound = 45, 
		RULE_enumDeclaration = 46, RULE_enumConstants = 47, RULE_enumConstant = 48, 
		RULE_enumBodyDeclarations = 49, RULE_interfaceDeclaration = 50, RULE_typeList = 51, 
		RULE_classBody = 52, RULE_interfaceBody = 53, RULE_classBodyDeclaration = 54, 
		RULE_memberDeclaration = 55, RULE_methodDeclaration = 56, RULE_genericMethodDeclaration = 57, 
		RULE_constructorDeclaration = 58, RULE_genericConstructorDeclaration = 59, 
		RULE_fieldDeclaration = 60, RULE_interfaceBodyDeclaration = 61, RULE_interfaceMemberDeclaration = 62, 
		RULE_constDeclaration = 63, RULE_constantDeclarator = 64, RULE_interfaceMethodDeclaration = 65, 
		RULE_genericInterfaceMethodDeclaration = 66, RULE_variableDeclarators = 67, 
		RULE_variableDeclarator = 68, RULE_variableDeclaratorId = 69, RULE_variableInitializer = 70, 
		RULE_arrayInitializer = 71, RULE_enumConstantName = 72, RULE_type = 73, 
		RULE_classOrInterfaceType = 74, RULE_primitiveType = 75, RULE_typeArguments = 76, 
		RULE_typeArgument = 77, RULE_qualifiedNameList = 78, RULE_formalParameters = 79, 
		RULE_formalParameterList = 80, RULE_formalParameter = 81, RULE_lastFormalParameter = 82, 
		RULE_methodBody = 83, RULE_constructorBody = 84, RULE_qualifiedName = 85, 
		RULE_annotation = 86, RULE_annotationName = 87, RULE_elementValuePairs = 88, 
		RULE_elementValuePair = 89, RULE_elementValue = 90, RULE_elementValueArrayInitializer = 91, 
		RULE_annotationTypeDeclaration = 92, RULE_annotationTypeBody = 93, RULE_annotationTypeElementDeclaration = 94, 
		RULE_annotationTypeElementRest = 95, RULE_annotationMethodOrConstantRest = 96, 
		RULE_annotationMethodRest = 97, RULE_annotationConstantRest = 98, RULE_defaultValue = 99, 
		RULE_block = 100, RULE_blockStatement = 101, RULE_localVariableDeclarationStatement = 102, 
		RULE_localVariableDeclaration = 103, RULE_statement = 104, RULE_catchClause = 105, 
		RULE_catchType = 106, RULE_finallyBlock = 107, RULE_resourceSpecification = 108, 
		RULE_resources = 109, RULE_resource = 110, RULE_switchBlockStatementGroup = 111, 
		RULE_switchLabel = 112, RULE_forControl = 113, RULE_forInit = 114, RULE_enhancedForControl = 115, 
		RULE_forUpdate = 116, RULE_parExpression = 117, RULE_expressionList = 118, 
		RULE_statementExpression = 119, RULE_constantExpression = 120, RULE_expression = 121, 
		RULE_primary = 122, RULE_creator = 123, RULE_createdName = 124, RULE_innerCreator = 125, 
		RULE_arrayCreatorRest = 126, RULE_classCreatorRest = 127, RULE_explicitGenericInvocation = 128, 
		RULE_nonWildcardTypeArguments = 129, RULE_typeArgumentsOrDiamond = 130, 
		RULE_nonWildcardTypeArgumentsOrDiamond = 131, RULE_superSuffix = 132, 
		RULE_explicitGenericInvocationSuffix = 133, RULE_arguments = 134;
	public static final String[] ruleNames = {
		"start", "query", "logicalQuery", "andQuery", "orQuery", "notQuery", "simpleQuery", 
		"equalQuery", "lessThanOrEqualToQuery", "lessThanQuery", "greaterThanOrEqualToQuery", 
		"greaterThanQuery", "verboseBetweenQuery", "betweenQuery", "inQuery", 
		"startsWithQuery", "endsWithQuery", "containsQuery", "isContainedInQuery", 
		"matchesRegexQuery", "hasQuery", "allQuery", "noneQuery", "longestPrefixQuery", 
		"isPrefixOfQuery", "objectType", "attributeName", "queryParameter", "stringQueryParameter", 
		"queryOptions", "queryOption", "orderByOption", "attributeOrder", "direction", 
		"literal", "compilationUnit", "packageDeclaration", "importDeclaration", 
		"typeDeclaration", "modifier", "classOrInterfaceModifier", "variableModifier", 
		"classDeclaration", "typeParameters", "typeParameter", "typeBound", "enumDeclaration", 
		"enumConstants", "enumConstant", "enumBodyDeclarations", "interfaceDeclaration", 
		"typeList", "classBody", "interfaceBody", "classBodyDeclaration", "memberDeclaration", 
		"methodDeclaration", "genericMethodDeclaration", "constructorDeclaration", 
		"genericConstructorDeclaration", "fieldDeclaration", "interfaceBodyDeclaration", 
		"interfaceMemberDeclaration", "constDeclaration", "constantDeclarator", 
		"interfaceMethodDeclaration", "genericInterfaceMethodDeclaration", "variableDeclarators", 
		"variableDeclarator", "variableDeclaratorId", "variableInitializer", "arrayInitializer", 
		"enumConstantName", "type", "classOrInterfaceType", "primitiveType", "typeArguments", 
		"typeArgument", "qualifiedNameList", "formalParameters", "formalParameterList", 
		"formalParameter", "lastFormalParameter", "methodBody", "constructorBody", 
		"qualifiedName", "annotation", "annotationName", "elementValuePairs", 
		"elementValuePair", "elementValue", "elementValueArrayInitializer", "annotationTypeDeclaration", 
		"annotationTypeBody", "annotationTypeElementDeclaration", "annotationTypeElementRest", 
		"annotationMethodOrConstantRest", "annotationMethodRest", "annotationConstantRest", 
		"defaultValue", "block", "blockStatement", "localVariableDeclarationStatement", 
		"localVariableDeclaration", "statement", "catchClause", "catchType", "finallyBlock", 
		"resourceSpecification", "resources", "resource", "switchBlockStatementGroup", 
		"switchLabel", "forControl", "forInit", "enhancedForControl", "forUpdate", 
		"parExpression", "expressionList", "statementExpression", "constantExpression", 
		"expression", "primary", "creator", "createdName", "innerCreator", "arrayCreatorRest", 
		"classCreatorRest", "explicitGenericInvocation", "nonWildcardTypeArguments", 
		"typeArgumentsOrDiamond", "nonWildcardTypeArgumentsOrDiamond", "superSuffix", 
		"explicitGenericInvocationSuffix", "arguments"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'and'", "'or'", "'not'", "'equal'", "'lessThanOrEqualTo'", "'lessThan'", 
		"'greaterThanOrEqualTo'", "'greaterThan'", "'between'", "'in'", "'startsWith'", 
		"'endsWith'", "'contains'", "'isContainedIn'", "'matchesRegex'", "'has'", 
		"'all'", "'.class'", "'none'", "'longestPrefix'", "'isPrefixOf'", "'queryOptions'", 
		"'orderBy'", "'ascending'", "'descending'", null, null, null, null, "'abstract'", 
		"'assert'", "'boolean'", "'break'", "'byte'", "'case'", "'catch'", "'char'", 
		"'class'", "'const'", "'continue'", "'default'", "'do'", "'double'", "'else'", 
		"'enum'", "'extends'", "'final'", "'finally'", "'float'", "'for'", "'if'", 
		"'goto'", "'implements'", "'import'", "'instanceof'", "'int'", "'interface'", 
		"'long'", "'native'", "'new'", "'package'", "'private'", "'protected'", 
		"'public'", "'return'", "'short'", "'static'", "'strictfp'", "'super'", 
		"'switch'", "'synchronized'", "'this'", "'throw'", "'throws'", "'transient'", 
		"'try'", "'void'", "'volatile'", "'while'", null, null, null, "'null'", 
		"'('", "')'", "'{'", "'}'", "'['", "']'", "';'", "','", "'.'", "'='", 
		"'>'", "'<'", "'!'", "'~'", "'?'", "':'", "'=='", "'<='", "'>='", "'!='", 
		"'&&'", "'||'", "'++'", "'--'", "'+'", "'-'", "'*'", "'/'", "'&'", "'|'", 
		"'^'", "'%'", "'+='", "'-='", "'*='", "'/='", "'&='", "'|='", "'^='", 
		"'%='", "'<<='", "'>>='", "'>>>='", null, "'@'", "'...'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, "IntegerLiteral", "DecimalIntegerLiteral", "FloatingPointLiteral", 
		"DecimalFloatingPointLiteral", "ABSTRACT", "ASSERT", "BOOLEAN", "BREAK", 
		"BYTE", "CASE", "CATCH", "CHAR", "CLASS", "CONST", "CONTINUE", "DEFAULT", 
		"DO", "DOUBLE", "ELSE", "ENUM", "EXTENDS", "FINAL", "FINALLY", "FLOAT", 
		"FOR", "IF", "GOTO", "IMPLEMENTS", "IMPORT", "INSTANCEOF", "INT", "INTERFACE", 
		"LONG", "NATIVE", "NEW", "PACKAGE", "PRIVATE", "PROTECTED", "PUBLIC", 
		"RETURN", "SHORT", "STATIC", "STRICTFP", "SUPER", "SWITCH", "SYNCHRONIZED", 
		"THIS", "THROW", "THROWS", "TRANSIENT", "TRY", "VOID", "VOLATILE", "WHILE", 
		"BooleanLiteral", "CharacterLiteral", "StringLiteral", "NullLiteral", 
		"LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", "RBRACK", "SEMI", "COMMA", 
		"DOT", "ASSIGN", "GT", "LT", "BANG", "TILDE", "QUESTION", "COLON", "EQUAL", 
		"LE", "GE", "NOTEQUAL", "AND", "OR", "INC", "DEC", "ADD", "SUB", "MUL", 
		"DIV", "BITAND", "BITOR", "CARET", "MOD", "ADD_ASSIGN", "SUB_ASSIGN", 
		"MUL_ASSIGN", "DIV_ASSIGN", "AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", "MOD_ASSIGN", 
		"LSHIFT_ASSIGN", "RSHIFT_ASSIGN", "URSHIFT_ASSIGN", "Identifier", "AT", 
		"ELLIPSIS", "WS", "COMMENT", "LINE_COMMENT"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "CQNGrammar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CQNGrammarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class StartContext extends ParserRuleContext {
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TerminalNode EOF() { return getToken(CQNGrammarParser.EOF, 0); }
		public QueryOptionsContext queryOptions() {
			return getRuleContext(QueryOptionsContext.class,0);
		}
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitStart(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_start);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(270);
			query();
			setState(273);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(271);
				match(COMMA);
				setState(272);
				queryOptions();
				}
			}

			setState(275);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QueryContext extends ParserRuleContext {
		public LogicalQueryContext logicalQuery() {
			return getRuleContext(LogicalQueryContext.class,0);
		}
		public SimpleQueryContext simpleQuery() {
			return getRuleContext(SimpleQueryContext.class,0);
		}
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitQuery(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_query);
		try {
			setState(279);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
			case T__1:
			case T__2:
				enterOuterAlt(_localctx, 1);
				{
				setState(277);
				logicalQuery();
				}
				break;
			case T__3:
			case T__4:
			case T__5:
			case T__6:
			case T__7:
			case T__8:
			case T__9:
			case T__10:
			case T__11:
			case T__12:
			case T__13:
			case T__14:
			case T__15:
			case T__16:
			case T__18:
			case T__19:
			case T__20:
				enterOuterAlt(_localctx, 2);
				{
				setState(278);
				simpleQuery();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LogicalQueryContext extends ParserRuleContext {
		public AndQueryContext andQuery() {
			return getRuleContext(AndQueryContext.class,0);
		}
		public OrQueryContext orQuery() {
			return getRuleContext(OrQueryContext.class,0);
		}
		public NotQueryContext notQuery() {
			return getRuleContext(NotQueryContext.class,0);
		}
		public LogicalQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterLogicalQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitLogicalQuery(this);
		}
	}

	public final LogicalQueryContext logicalQuery() throws RecognitionException {
		LogicalQueryContext _localctx = new LogicalQueryContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_logicalQuery);
		try {
			setState(284);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1);
				{
				setState(281);
				andQuery();
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 2);
				{
				setState(282);
				orQuery();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 3);
				{
				setState(283);
				notQuery();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AndQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public List<QueryContext> query() {
			return getRuleContexts(QueryContext.class);
		}
		public QueryContext query(int i) {
			return getRuleContext(QueryContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public AndQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterAndQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitAndQuery(this);
		}
	}

	public final AndQueryContext andQuery() throws RecognitionException {
		AndQueryContext _localctx = new AndQueryContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_andQuery);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(286);
			match(T__0);
			setState(287);
			match(LPAREN);
			setState(288);
			query();
			setState(291); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(289);
				match(COMMA);
				setState(290);
				query();
				}
				}
				setState(293); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==COMMA );
			setState(295);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OrQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public List<QueryContext> query() {
			return getRuleContexts(QueryContext.class);
		}
		public QueryContext query(int i) {
			return getRuleContext(QueryContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public OrQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterOrQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitOrQuery(this);
		}
	}

	public final OrQueryContext orQuery() throws RecognitionException {
		OrQueryContext _localctx = new OrQueryContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_orQuery);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(297);
			match(T__1);
			setState(298);
			match(LPAREN);
			setState(299);
			query();
			setState(302); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(300);
				match(COMMA);
				setState(301);
				query();
				}
				}
				setState(304); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==COMMA );
			setState(306);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NotQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public NotQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterNotQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitNotQuery(this);
		}
	}

	public final NotQueryContext notQuery() throws RecognitionException {
		NotQueryContext _localctx = new NotQueryContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_notQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(308);
			match(T__2);
			setState(309);
			match(LPAREN);
			setState(310);
			query();
			setState(311);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SimpleQueryContext extends ParserRuleContext {
		public EqualQueryContext equalQuery() {
			return getRuleContext(EqualQueryContext.class,0);
		}
		public LessThanOrEqualToQueryContext lessThanOrEqualToQuery() {
			return getRuleContext(LessThanOrEqualToQueryContext.class,0);
		}
		public LessThanQueryContext lessThanQuery() {
			return getRuleContext(LessThanQueryContext.class,0);
		}
		public GreaterThanOrEqualToQueryContext greaterThanOrEqualToQuery() {
			return getRuleContext(GreaterThanOrEqualToQueryContext.class,0);
		}
		public GreaterThanQueryContext greaterThanQuery() {
			return getRuleContext(GreaterThanQueryContext.class,0);
		}
		public VerboseBetweenQueryContext verboseBetweenQuery() {
			return getRuleContext(VerboseBetweenQueryContext.class,0);
		}
		public BetweenQueryContext betweenQuery() {
			return getRuleContext(BetweenQueryContext.class,0);
		}
		public InQueryContext inQuery() {
			return getRuleContext(InQueryContext.class,0);
		}
		public StartsWithQueryContext startsWithQuery() {
			return getRuleContext(StartsWithQueryContext.class,0);
		}
		public EndsWithQueryContext endsWithQuery() {
			return getRuleContext(EndsWithQueryContext.class,0);
		}
		public ContainsQueryContext containsQuery() {
			return getRuleContext(ContainsQueryContext.class,0);
		}
		public IsContainedInQueryContext isContainedInQuery() {
			return getRuleContext(IsContainedInQueryContext.class,0);
		}
		public MatchesRegexQueryContext matchesRegexQuery() {
			return getRuleContext(MatchesRegexQueryContext.class,0);
		}
		public HasQueryContext hasQuery() {
			return getRuleContext(HasQueryContext.class,0);
		}
		public AllQueryContext allQuery() {
			return getRuleContext(AllQueryContext.class,0);
		}
		public NoneQueryContext noneQuery() {
			return getRuleContext(NoneQueryContext.class,0);
		}
		public LongestPrefixQueryContext longestPrefixQuery() {
			return getRuleContext(LongestPrefixQueryContext.class,0);
		}
		public IsPrefixOfQueryContext isPrefixOfQuery() {
			return getRuleContext(IsPrefixOfQueryContext.class,0);
		}
		public SimpleQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterSimpleQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitSimpleQuery(this);
		}
	}

	public final SimpleQueryContext simpleQuery() throws RecognitionException {
		SimpleQueryContext _localctx = new SimpleQueryContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_simpleQuery);
		try {
			setState(331);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(313);
				equalQuery();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(314);
				lessThanOrEqualToQuery();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(315);
				lessThanQuery();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(316);
				greaterThanOrEqualToQuery();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(317);
				greaterThanQuery();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(318);
				verboseBetweenQuery();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(319);
				betweenQuery();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(320);
				inQuery();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(321);
				startsWithQuery();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(322);
				endsWithQuery();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(323);
				containsQuery();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(324);
				isContainedInQuery();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(325);
				matchesRegexQuery();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(326);
				hasQuery();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(327);
				allQuery();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(328);
				noneQuery();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(329);
				longestPrefixQuery();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(330);
				isPrefixOfQuery();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EqualQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public QueryParameterContext queryParameter() {
			return getRuleContext(QueryParameterContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public EqualQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equalQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterEqualQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitEqualQuery(this);
		}
	}

	public final EqualQueryContext equalQuery() throws RecognitionException {
		EqualQueryContext _localctx = new EqualQueryContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_equalQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(333);
			match(T__3);
			setState(334);
			match(LPAREN);
			setState(335);
			attributeName();
			setState(336);
			match(COMMA);
			setState(337);
			queryParameter();
			setState(338);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LessThanOrEqualToQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public QueryParameterContext queryParameter() {
			return getRuleContext(QueryParameterContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public LessThanOrEqualToQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lessThanOrEqualToQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterLessThanOrEqualToQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitLessThanOrEqualToQuery(this);
		}
	}

	public final LessThanOrEqualToQueryContext lessThanOrEqualToQuery() throws RecognitionException {
		LessThanOrEqualToQueryContext _localctx = new LessThanOrEqualToQueryContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_lessThanOrEqualToQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(340);
			match(T__4);
			setState(341);
			match(LPAREN);
			setState(342);
			attributeName();
			setState(343);
			match(COMMA);
			setState(344);
			queryParameter();
			setState(345);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LessThanQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public QueryParameterContext queryParameter() {
			return getRuleContext(QueryParameterContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public LessThanQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lessThanQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterLessThanQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitLessThanQuery(this);
		}
	}

	public final LessThanQueryContext lessThanQuery() throws RecognitionException {
		LessThanQueryContext _localctx = new LessThanQueryContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_lessThanQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(347);
			match(T__5);
			setState(348);
			match(LPAREN);
			setState(349);
			attributeName();
			setState(350);
			match(COMMA);
			setState(351);
			queryParameter();
			setState(352);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GreaterThanOrEqualToQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public QueryParameterContext queryParameter() {
			return getRuleContext(QueryParameterContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public GreaterThanOrEqualToQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_greaterThanOrEqualToQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterGreaterThanOrEqualToQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitGreaterThanOrEqualToQuery(this);
		}
	}

	public final GreaterThanOrEqualToQueryContext greaterThanOrEqualToQuery() throws RecognitionException {
		GreaterThanOrEqualToQueryContext _localctx = new GreaterThanOrEqualToQueryContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_greaterThanOrEqualToQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(354);
			match(T__6);
			setState(355);
			match(LPAREN);
			setState(356);
			attributeName();
			setState(357);
			match(COMMA);
			setState(358);
			queryParameter();
			setState(359);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GreaterThanQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public QueryParameterContext queryParameter() {
			return getRuleContext(QueryParameterContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public GreaterThanQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_greaterThanQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterGreaterThanQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitGreaterThanQuery(this);
		}
	}

	public final GreaterThanQueryContext greaterThanQuery() throws RecognitionException {
		GreaterThanQueryContext _localctx = new GreaterThanQueryContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_greaterThanQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(361);
			match(T__7);
			setState(362);
			match(LPAREN);
			setState(363);
			attributeName();
			setState(364);
			match(COMMA);
			setState(365);
			queryParameter();
			setState(366);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VerboseBetweenQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public List<QueryParameterContext> queryParameter() {
			return getRuleContexts(QueryParameterContext.class);
		}
		public QueryParameterContext queryParameter(int i) {
			return getRuleContext(QueryParameterContext.class,i);
		}
		public List<TerminalNode> BooleanLiteral() { return getTokens(CQNGrammarParser.BooleanLiteral); }
		public TerminalNode BooleanLiteral(int i) {
			return getToken(CQNGrammarParser.BooleanLiteral, i);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public VerboseBetweenQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_verboseBetweenQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterVerboseBetweenQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitVerboseBetweenQuery(this);
		}
	}

	public final VerboseBetweenQueryContext verboseBetweenQuery() throws RecognitionException {
		VerboseBetweenQueryContext _localctx = new VerboseBetweenQueryContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_verboseBetweenQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(368);
			match(T__8);
			setState(369);
			match(LPAREN);
			setState(370);
			attributeName();
			setState(371);
			match(COMMA);
			setState(372);
			queryParameter();
			setState(373);
			match(COMMA);
			setState(374);
			match(BooleanLiteral);
			setState(375);
			match(COMMA);
			setState(376);
			queryParameter();
			setState(377);
			match(COMMA);
			setState(378);
			match(BooleanLiteral);
			setState(379);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BetweenQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public List<QueryParameterContext> queryParameter() {
			return getRuleContexts(QueryParameterContext.class);
		}
		public QueryParameterContext queryParameter(int i) {
			return getRuleContext(QueryParameterContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public BetweenQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_betweenQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterBetweenQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitBetweenQuery(this);
		}
	}

	public final BetweenQueryContext betweenQuery() throws RecognitionException {
		BetweenQueryContext _localctx = new BetweenQueryContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_betweenQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(381);
			match(T__8);
			setState(382);
			match(LPAREN);
			setState(383);
			attributeName();
			setState(384);
			match(COMMA);
			setState(385);
			queryParameter();
			setState(386);
			match(COMMA);
			setState(387);
			queryParameter();
			setState(388);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public List<QueryParameterContext> queryParameter() {
			return getRuleContexts(QueryParameterContext.class);
		}
		public QueryParameterContext queryParameter(int i) {
			return getRuleContext(QueryParameterContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public InQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterInQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitInQuery(this);
		}
	}

	public final InQueryContext inQuery() throws RecognitionException {
		InQueryContext _localctx = new InQueryContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_inQuery);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(390);
			match(T__9);
			setState(391);
			match(LPAREN);
			setState(392);
			attributeName();
			setState(393);
			match(COMMA);
			setState(394);
			queryParameter();
			setState(399);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(395);
				match(COMMA);
				setState(396);
				queryParameter();
				}
				}
				setState(401);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(402);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StartsWithQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public StringQueryParameterContext stringQueryParameter() {
			return getRuleContext(StringQueryParameterContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public StartsWithQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_startsWithQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterStartsWithQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitStartsWithQuery(this);
		}
	}

	public final StartsWithQueryContext startsWithQuery() throws RecognitionException {
		StartsWithQueryContext _localctx = new StartsWithQueryContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_startsWithQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(404);
			match(T__10);
			setState(405);
			match(LPAREN);
			setState(406);
			attributeName();
			setState(407);
			match(COMMA);
			setState(408);
			stringQueryParameter();
			setState(409);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EndsWithQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public StringQueryParameterContext stringQueryParameter() {
			return getRuleContext(StringQueryParameterContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public EndsWithQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endsWithQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterEndsWithQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitEndsWithQuery(this);
		}
	}

	public final EndsWithQueryContext endsWithQuery() throws RecognitionException {
		EndsWithQueryContext _localctx = new EndsWithQueryContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_endsWithQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			match(T__11);
			setState(412);
			match(LPAREN);
			setState(413);
			attributeName();
			setState(414);
			match(COMMA);
			setState(415);
			stringQueryParameter();
			setState(416);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ContainsQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public StringQueryParameterContext stringQueryParameter() {
			return getRuleContext(StringQueryParameterContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public ContainsQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_containsQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterContainsQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitContainsQuery(this);
		}
	}

	public final ContainsQueryContext containsQuery() throws RecognitionException {
		ContainsQueryContext _localctx = new ContainsQueryContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_containsQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(418);
			match(T__12);
			setState(419);
			match(LPAREN);
			setState(420);
			attributeName();
			setState(421);
			match(COMMA);
			setState(422);
			stringQueryParameter();
			setState(423);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IsContainedInQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public StringQueryParameterContext stringQueryParameter() {
			return getRuleContext(StringQueryParameterContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public IsContainedInQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_isContainedInQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterIsContainedInQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitIsContainedInQuery(this);
		}
	}

	public final IsContainedInQueryContext isContainedInQuery() throws RecognitionException {
		IsContainedInQueryContext _localctx = new IsContainedInQueryContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_isContainedInQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(425);
			match(T__13);
			setState(426);
			match(LPAREN);
			setState(427);
			attributeName();
			setState(428);
			match(COMMA);
			setState(429);
			stringQueryParameter();
			setState(430);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MatchesRegexQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public StringQueryParameterContext stringQueryParameter() {
			return getRuleContext(StringQueryParameterContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public MatchesRegexQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matchesRegexQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterMatchesRegexQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitMatchesRegexQuery(this);
		}
	}

	public final MatchesRegexQueryContext matchesRegexQuery() throws RecognitionException {
		MatchesRegexQueryContext _localctx = new MatchesRegexQueryContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_matchesRegexQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(432);
			match(T__14);
			setState(433);
			match(LPAREN);
			setState(434);
			attributeName();
			setState(435);
			match(COMMA);
			setState(436);
			stringQueryParameter();
			setState(437);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HasQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public HasQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hasQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterHasQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitHasQuery(this);
		}
	}

	public final HasQueryContext hasQuery() throws RecognitionException {
		HasQueryContext _localctx = new HasQueryContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_hasQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(439);
			match(T__15);
			setState(440);
			match(LPAREN);
			setState(441);
			attributeName();
			setState(442);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AllQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public ObjectTypeContext objectType() {
			return getRuleContext(ObjectTypeContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public AllQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_allQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterAllQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitAllQuery(this);
		}
	}

	public final AllQueryContext allQuery() throws RecognitionException {
		AllQueryContext _localctx = new AllQueryContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_allQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(444);
			match(T__16);
			setState(445);
			match(LPAREN);
			setState(446);
			objectType();
			setState(447);
			match(T__17);
			setState(448);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NoneQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public ObjectTypeContext objectType() {
			return getRuleContext(ObjectTypeContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public NoneQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noneQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterNoneQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitNoneQuery(this);
		}
	}

	public final NoneQueryContext noneQuery() throws RecognitionException {
		NoneQueryContext _localctx = new NoneQueryContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_noneQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(450);
			match(T__18);
			setState(451);
			match(LPAREN);
			setState(452);
			objectType();
			setState(453);
			match(T__17);
			setState(454);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LongestPrefixQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public StringQueryParameterContext stringQueryParameter() {
			return getRuleContext(StringQueryParameterContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public LongestPrefixQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_longestPrefixQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterLongestPrefixQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitLongestPrefixQuery(this);
		}
	}

	public final LongestPrefixQueryContext longestPrefixQuery() throws RecognitionException {
		LongestPrefixQueryContext _localctx = new LongestPrefixQueryContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_longestPrefixQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(456);
			match(T__19);
			setState(457);
			match(LPAREN);
			setState(458);
			attributeName();
			setState(459);
			match(COMMA);
			setState(460);
			stringQueryParameter();
			setState(461);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IsPrefixOfQueryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public StringQueryParameterContext stringQueryParameter() {
			return getRuleContext(StringQueryParameterContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public IsPrefixOfQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_isPrefixOfQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterIsPrefixOfQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitIsPrefixOfQuery(this);
		}
	}

	public final IsPrefixOfQueryContext isPrefixOfQuery() throws RecognitionException {
		IsPrefixOfQueryContext _localctx = new IsPrefixOfQueryContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_isPrefixOfQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(463);
			match(T__20);
			setState(464);
			match(LPAREN);
			setState(465);
			attributeName();
			setState(466);
			match(COMMA);
			setState(467);
			stringQueryParameter();
			setState(468);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectTypeContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public ObjectTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterObjectType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitObjectType(this);
		}
	}

	public final ObjectTypeContext objectType() throws RecognitionException {
		ObjectTypeContext _localctx = new ObjectTypeContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_objectType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(470);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttributeNameContext extends ParserRuleContext {
		public TerminalNode StringLiteral() { return getToken(CQNGrammarParser.StringLiteral, 0); }
		public AttributeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterAttributeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitAttributeName(this);
		}
	}

	public final AttributeNameContext attributeName() throws RecognitionException {
		AttributeNameContext _localctx = new AttributeNameContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_attributeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(472);
			match(StringLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QueryParameterContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public QueryParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterQueryParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitQueryParameter(this);
		}
	}

	public final QueryParameterContext queryParameter() throws RecognitionException {
		QueryParameterContext _localctx = new QueryParameterContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_queryParameter);
		try {
			setState(476);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerLiteral:
			case FloatingPointLiteral:
			case BooleanLiteral:
			case CharacterLiteral:
			case StringLiteral:
			case NullLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(474);
				literal();
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(475);
				match(Identifier);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringQueryParameterContext extends ParserRuleContext {
		public TerminalNode StringLiteral() { return getToken(CQNGrammarParser.StringLiteral, 0); }
		public StringQueryParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringQueryParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterStringQueryParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitStringQueryParameter(this);
		}
	}

	public final StringQueryParameterContext stringQueryParameter() throws RecognitionException {
		StringQueryParameterContext _localctx = new StringQueryParameterContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_stringQueryParameter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(478);
			match(StringLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QueryOptionsContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public List<QueryOptionContext> queryOption() {
			return getRuleContexts(QueryOptionContext.class);
		}
		public QueryOptionContext queryOption(int i) {
			return getRuleContext(QueryOptionContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public QueryOptionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryOptions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterQueryOptions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitQueryOptions(this);
		}
	}

	public final QueryOptionsContext queryOptions() throws RecognitionException {
		QueryOptionsContext _localctx = new QueryOptionsContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_queryOptions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(480);
			match(T__21);
			setState(481);
			match(LPAREN);
			setState(482);
			queryOption();
			setState(487);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(483);
				match(COMMA);
				setState(484);
				queryOption();
				}
				}
				setState(489);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(490);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QueryOptionContext extends ParserRuleContext {
		public OrderByOptionContext orderByOption() {
			return getRuleContext(OrderByOptionContext.class,0);
		}
		public QueryOptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryOption; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterQueryOption(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitQueryOption(this);
		}
	}

	public final QueryOptionContext queryOption() throws RecognitionException {
		QueryOptionContext _localctx = new QueryOptionContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_queryOption);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(492);
			orderByOption();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OrderByOptionContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public List<AttributeOrderContext> attributeOrder() {
			return getRuleContexts(AttributeOrderContext.class);
		}
		public AttributeOrderContext attributeOrder(int i) {
			return getRuleContext(AttributeOrderContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public OrderByOptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderByOption; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterOrderByOption(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitOrderByOption(this);
		}
	}

	public final OrderByOptionContext orderByOption() throws RecognitionException {
		OrderByOptionContext _localctx = new OrderByOptionContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_orderByOption);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(494);
			match(T__22);
			setState(495);
			match(LPAREN);
			setState(496);
			attributeOrder();
			setState(501);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(497);
				match(COMMA);
				setState(498);
				attributeOrder();
				}
				}
				setState(503);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(504);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttributeOrderContext extends ParserRuleContext {
		public DirectionContext direction() {
			return getRuleContext(DirectionContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(CQNGrammarParser.LPAREN, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CQNGrammarParser.RPAREN, 0); }
		public AttributeOrderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeOrder; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterAttributeOrder(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitAttributeOrder(this);
		}
	}

	public final AttributeOrderContext attributeOrder() throws RecognitionException {
		AttributeOrderContext _localctx = new AttributeOrderContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_attributeOrder);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(506);
			direction();
			setState(507);
			match(LPAREN);
			setState(508);
			attributeName();
			setState(509);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DirectionContext extends ParserRuleContext {
		public DirectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_direction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterDirection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitDirection(this);
		}
	}

	public final DirectionContext direction() throws RecognitionException {
		DirectionContext _localctx = new DirectionContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_direction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(511);
			_la = _input.LA(1);
			if ( !(_la==T__23 || _la==T__24) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode IntegerLiteral() { return getToken(CQNGrammarParser.IntegerLiteral, 0); }
		public TerminalNode FloatingPointLiteral() { return getToken(CQNGrammarParser.FloatingPointLiteral, 0); }
		public TerminalNode CharacterLiteral() { return getToken(CQNGrammarParser.CharacterLiteral, 0); }
		public TerminalNode StringLiteral() { return getToken(CQNGrammarParser.StringLiteral, 0); }
		public TerminalNode BooleanLiteral() { return getToken(CQNGrammarParser.BooleanLiteral, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitLiteral(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(513);
			_la = _input.LA(1);
			if ( !(((((_la - 26)) & ~0x3f) == 0 && ((1L << (_la - 26)) & ((1L << (IntegerLiteral - 26)) | (1L << (FloatingPointLiteral - 26)) | (1L << (BooleanLiteral - 26)) | (1L << (CharacterLiteral - 26)) | (1L << (StringLiteral - 26)) | (1L << (NullLiteral - 26)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CompilationUnitContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(CQNGrammarParser.EOF, 0); }
		public PackageDeclarationContext packageDeclaration() {
			return getRuleContext(PackageDeclarationContext.class,0);
		}
		public List<ImportDeclarationContext> importDeclaration() {
			return getRuleContexts(ImportDeclarationContext.class);
		}
		public ImportDeclarationContext importDeclaration(int i) {
			return getRuleContext(ImportDeclarationContext.class,i);
		}
		public List<TypeDeclarationContext> typeDeclaration() {
			return getRuleContexts(TypeDeclarationContext.class);
		}
		public TypeDeclarationContext typeDeclaration(int i) {
			return getRuleContext(TypeDeclarationContext.class,i);
		}
		public CompilationUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compilationUnit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterCompilationUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitCompilationUnit(this);
		}
	}

	public final CompilationUnitContext compilationUnit() throws RecognitionException {
		CompilationUnitContext _localctx = new CompilationUnitContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_compilationUnit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(516);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(515);
				packageDeclaration();
				}
				break;
			}
			setState(521);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(518);
				importDeclaration();
				}
				}
				setState(523);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(527);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (ABSTRACT - 30)) | (1L << (CLASS - 30)) | (1L << (ENUM - 30)) | (1L << (FINAL - 30)) | (1L << (INTERFACE - 30)) | (1L << (PRIVATE - 30)) | (1L << (PROTECTED - 30)) | (1L << (PUBLIC - 30)) | (1L << (STATIC - 30)) | (1L << (STRICTFP - 30)) | (1L << (SEMI - 30)))) != 0) || _la==AT) {
				{
				{
				setState(524);
				typeDeclaration();
				}
				}
				setState(529);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(530);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PackageDeclarationContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public PackageDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packageDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterPackageDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitPackageDeclaration(this);
		}
	}

	public final PackageDeclarationContext packageDeclaration() throws RecognitionException {
		PackageDeclarationContext _localctx = new PackageDeclarationContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_packageDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(535);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AT) {
				{
				{
				setState(532);
				annotation();
				}
				}
				setState(537);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(538);
			match(PACKAGE);
			setState(539);
			qualifiedName();
			setState(540);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImportDeclarationContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public ImportDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterImportDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitImportDeclaration(this);
		}
	}

	public final ImportDeclarationContext importDeclaration() throws RecognitionException {
		ImportDeclarationContext _localctx = new ImportDeclarationContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_importDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(542);
			match(IMPORT);
			setState(544);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STATIC) {
				{
				setState(543);
				match(STATIC);
				}
			}

			setState(546);
			qualifiedName();
			setState(549);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOT) {
				{
				setState(547);
				match(DOT);
				setState(548);
				match(MUL);
				}
			}

			setState(551);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeDeclarationContext extends ParserRuleContext {
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public List<ClassOrInterfaceModifierContext> classOrInterfaceModifier() {
			return getRuleContexts(ClassOrInterfaceModifierContext.class);
		}
		public ClassOrInterfaceModifierContext classOrInterfaceModifier(int i) {
			return getRuleContext(ClassOrInterfaceModifierContext.class,i);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public InterfaceDeclarationContext interfaceDeclaration() {
			return getRuleContext(InterfaceDeclarationContext.class,0);
		}
		public AnnotationTypeDeclarationContext annotationTypeDeclaration() {
			return getRuleContext(AnnotationTypeDeclarationContext.class,0);
		}
		public TypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitTypeDeclaration(this);
		}
	}

	public final TypeDeclarationContext typeDeclaration() throws RecognitionException {
		TypeDeclarationContext _localctx = new TypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_typeDeclaration);
		int _la;
		try {
			int _alt;
			setState(582);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(556);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (ABSTRACT - 30)) | (1L << (FINAL - 30)) | (1L << (PRIVATE - 30)) | (1L << (PROTECTED - 30)) | (1L << (PUBLIC - 30)) | (1L << (STATIC - 30)) | (1L << (STRICTFP - 30)))) != 0) || _la==AT) {
					{
					{
					setState(553);
					classOrInterfaceModifier();
					}
					}
					setState(558);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(559);
				classDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(563);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (ABSTRACT - 30)) | (1L << (FINAL - 30)) | (1L << (PRIVATE - 30)) | (1L << (PROTECTED - 30)) | (1L << (PUBLIC - 30)) | (1L << (STATIC - 30)) | (1L << (STRICTFP - 30)))) != 0) || _la==AT) {
					{
					{
					setState(560);
					classOrInterfaceModifier();
					}
					}
					setState(565);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(566);
				enumDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(570);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (ABSTRACT - 30)) | (1L << (FINAL - 30)) | (1L << (PRIVATE - 30)) | (1L << (PROTECTED - 30)) | (1L << (PUBLIC - 30)) | (1L << (STATIC - 30)) | (1L << (STRICTFP - 30)))) != 0) || _la==AT) {
					{
					{
					setState(567);
					classOrInterfaceModifier();
					}
					}
					setState(572);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(573);
				interfaceDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(577);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(574);
						classOrInterfaceModifier();
						}
						} 
					}
					setState(579);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				}
				setState(580);
				annotationTypeDeclaration();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(581);
				match(SEMI);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModifierContext extends ParserRuleContext {
		public ClassOrInterfaceModifierContext classOrInterfaceModifier() {
			return getRuleContext(ClassOrInterfaceModifierContext.class,0);
		}
		public ModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitModifier(this);
		}
	}

	public final ModifierContext modifier() throws RecognitionException {
		ModifierContext _localctx = new ModifierContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_modifier);
		int _la;
		try {
			setState(586);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ABSTRACT:
			case FINAL:
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case STATIC:
			case STRICTFP:
			case AT:
				enterOuterAlt(_localctx, 1);
				{
				setState(584);
				classOrInterfaceModifier();
				}
				break;
			case NATIVE:
			case SYNCHRONIZED:
			case TRANSIENT:
			case VOLATILE:
				enterOuterAlt(_localctx, 2);
				{
				setState(585);
				_la = _input.LA(1);
				if ( !(((((_la - 59)) & ~0x3f) == 0 && ((1L << (_la - 59)) & ((1L << (NATIVE - 59)) | (1L << (SYNCHRONIZED - 59)) | (1L << (TRANSIENT - 59)) | (1L << (VOLATILE - 59)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassOrInterfaceModifierContext extends ParserRuleContext {
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public ClassOrInterfaceModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classOrInterfaceModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterClassOrInterfaceModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitClassOrInterfaceModifier(this);
		}
	}

	public final ClassOrInterfaceModifierContext classOrInterfaceModifier() throws RecognitionException {
		ClassOrInterfaceModifierContext _localctx = new ClassOrInterfaceModifierContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_classOrInterfaceModifier);
		int _la;
		try {
			setState(590);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case AT:
				enterOuterAlt(_localctx, 1);
				{
				setState(588);
				annotation();
				}
				break;
			case ABSTRACT:
			case FINAL:
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case STATIC:
			case STRICTFP:
				enterOuterAlt(_localctx, 2);
				{
				setState(589);
				_la = _input.LA(1);
				if ( !(((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (ABSTRACT - 30)) | (1L << (FINAL - 30)) | (1L << (PRIVATE - 30)) | (1L << (PROTECTED - 30)) | (1L << (PUBLIC - 30)) | (1L << (STATIC - 30)) | (1L << (STRICTFP - 30)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableModifierContext extends ParserRuleContext {
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public VariableModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterVariableModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitVariableModifier(this);
		}
	}

	public final VariableModifierContext variableModifier() throws RecognitionException {
		VariableModifierContext _localctx = new VariableModifierContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_variableModifier);
		try {
			setState(594);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FINAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(592);
				match(FINAL);
				}
				break;
			case AT:
				enterOuterAlt(_localctx, 2);
				{
				setState(593);
				annotation();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassDeclarationContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public ClassBodyContext classBody() {
			return getRuleContext(ClassBodyContext.class,0);
		}
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public ClassDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterClassDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitClassDeclaration(this);
		}
	}

	public final ClassDeclarationContext classDeclaration() throws RecognitionException {
		ClassDeclarationContext _localctx = new ClassDeclarationContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_classDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(596);
			match(CLASS);
			setState(597);
			match(Identifier);
			setState(599);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(598);
				typeParameters();
				}
			}

			setState(603);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(601);
				match(EXTENDS);
				setState(602);
				type();
				}
			}

			setState(607);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IMPLEMENTS) {
				{
				setState(605);
				match(IMPLEMENTS);
				setState(606);
				typeList();
				}
			}

			setState(609);
			classBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeParametersContext extends ParserRuleContext {
		public List<TypeParameterContext> typeParameter() {
			return getRuleContexts(TypeParameterContext.class);
		}
		public TypeParameterContext typeParameter(int i) {
			return getRuleContext(TypeParameterContext.class,i);
		}
		public TypeParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterTypeParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitTypeParameters(this);
		}
	}

	public final TypeParametersContext typeParameters() throws RecognitionException {
		TypeParametersContext _localctx = new TypeParametersContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_typeParameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(611);
			match(LT);
			setState(612);
			typeParameter();
			setState(617);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(613);
				match(COMMA);
				setState(614);
				typeParameter();
				}
				}
				setState(619);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(620);
			match(GT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeParameterContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public TypeBoundContext typeBound() {
			return getRuleContext(TypeBoundContext.class,0);
		}
		public TypeParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterTypeParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitTypeParameter(this);
		}
	}

	public final TypeParameterContext typeParameter() throws RecognitionException {
		TypeParameterContext _localctx = new TypeParameterContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_typeParameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(622);
			match(Identifier);
			setState(625);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(623);
				match(EXTENDS);
				setState(624);
				typeBound();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeBoundContext extends ParserRuleContext {
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public TypeBoundContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeBound; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterTypeBound(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitTypeBound(this);
		}
	}

	public final TypeBoundContext typeBound() throws RecognitionException {
		TypeBoundContext _localctx = new TypeBoundContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_typeBound);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(627);
			type();
			setState(632);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BITAND) {
				{
				{
				setState(628);
				match(BITAND);
				setState(629);
				type();
				}
				}
				setState(634);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumDeclarationContext extends ParserRuleContext {
		public TerminalNode ENUM() { return getToken(CQNGrammarParser.ENUM, 0); }
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public EnumConstantsContext enumConstants() {
			return getRuleContext(EnumConstantsContext.class,0);
		}
		public EnumBodyDeclarationsContext enumBodyDeclarations() {
			return getRuleContext(EnumBodyDeclarationsContext.class,0);
		}
		public EnumDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterEnumDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitEnumDeclaration(this);
		}
	}

	public final EnumDeclarationContext enumDeclaration() throws RecognitionException {
		EnumDeclarationContext _localctx = new EnumDeclarationContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_enumDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(635);
			match(ENUM);
			setState(636);
			match(Identifier);
			setState(639);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IMPLEMENTS) {
				{
				setState(637);
				match(IMPLEMENTS);
				setState(638);
				typeList();
				}
			}

			setState(641);
			match(LBRACE);
			setState(643);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Identifier || _la==AT) {
				{
				setState(642);
				enumConstants();
				}
			}

			setState(646);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(645);
				match(COMMA);
				}
			}

			setState(649);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEMI) {
				{
				setState(648);
				enumBodyDeclarations();
				}
			}

			setState(651);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumConstantsContext extends ParserRuleContext {
		public List<EnumConstantContext> enumConstant() {
			return getRuleContexts(EnumConstantContext.class);
		}
		public EnumConstantContext enumConstant(int i) {
			return getRuleContext(EnumConstantContext.class,i);
		}
		public EnumConstantsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumConstants; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterEnumConstants(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitEnumConstants(this);
		}
	}

	public final EnumConstantsContext enumConstants() throws RecognitionException {
		EnumConstantsContext _localctx = new EnumConstantsContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_enumConstants);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(653);
			enumConstant();
			setState(658);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(654);
					match(COMMA);
					setState(655);
					enumConstant();
					}
					} 
				}
				setState(660);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumConstantContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public ClassBodyContext classBody() {
			return getRuleContext(ClassBodyContext.class,0);
		}
		public EnumConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumConstant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterEnumConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitEnumConstant(this);
		}
	}

	public final EnumConstantContext enumConstant() throws RecognitionException {
		EnumConstantContext _localctx = new EnumConstantContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_enumConstant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(664);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AT) {
				{
				{
				setState(661);
				annotation();
				}
				}
				setState(666);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(667);
			match(Identifier);
			setState(669);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(668);
				arguments();
				}
			}

			setState(672);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACE) {
				{
				setState(671);
				classBody();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumBodyDeclarationsContext extends ParserRuleContext {
		public List<ClassBodyDeclarationContext> classBodyDeclaration() {
			return getRuleContexts(ClassBodyDeclarationContext.class);
		}
		public ClassBodyDeclarationContext classBodyDeclaration(int i) {
			return getRuleContext(ClassBodyDeclarationContext.class,i);
		}
		public EnumBodyDeclarationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumBodyDeclarations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterEnumBodyDeclarations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitEnumBodyDeclarations(this);
		}
	}

	public final EnumBodyDeclarationsContext enumBodyDeclarations() throws RecognitionException {
		EnumBodyDeclarationsContext _localctx = new EnumBodyDeclarationsContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_enumBodyDeclarations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(674);
			match(SEMI);
			setState(678);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (ABSTRACT - 30)) | (1L << (BOOLEAN - 30)) | (1L << (BYTE - 30)) | (1L << (CHAR - 30)) | (1L << (CLASS - 30)) | (1L << (DOUBLE - 30)) | (1L << (ENUM - 30)) | (1L << (FINAL - 30)) | (1L << (FLOAT - 30)) | (1L << (INT - 30)) | (1L << (INTERFACE - 30)) | (1L << (LONG - 30)) | (1L << (NATIVE - 30)) | (1L << (PRIVATE - 30)) | (1L << (PROTECTED - 30)) | (1L << (PUBLIC - 30)) | (1L << (SHORT - 30)) | (1L << (STATIC - 30)) | (1L << (STRICTFP - 30)) | (1L << (SYNCHRONIZED - 30)) | (1L << (TRANSIENT - 30)) | (1L << (VOID - 30)) | (1L << (VOLATILE - 30)) | (1L << (LBRACE - 30)) | (1L << (SEMI - 30)))) != 0) || ((((_la - 95)) & ~0x3f) == 0 && ((1L << (_la - 95)) & ((1L << (LT - 95)) | (1L << (Identifier - 95)) | (1L << (AT - 95)))) != 0)) {
				{
				{
				setState(675);
				classBodyDeclaration();
				}
				}
				setState(680);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfaceDeclarationContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public InterfaceBodyContext interfaceBody() {
			return getRuleContext(InterfaceBodyContext.class,0);
		}
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public InterfaceDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterInterfaceDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitInterfaceDeclaration(this);
		}
	}

	public final InterfaceDeclarationContext interfaceDeclaration() throws RecognitionException {
		InterfaceDeclarationContext _localctx = new InterfaceDeclarationContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_interfaceDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(681);
			match(INTERFACE);
			setState(682);
			match(Identifier);
			setState(684);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(683);
				typeParameters();
				}
			}

			setState(688);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(686);
				match(EXTENDS);
				setState(687);
				typeList();
				}
			}

			setState(690);
			interfaceBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeListContext extends ParserRuleContext {
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public TypeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterTypeList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitTypeList(this);
		}
	}

	public final TypeListContext typeList() throws RecognitionException {
		TypeListContext _localctx = new TypeListContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_typeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(692);
			type();
			setState(697);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(693);
				match(COMMA);
				setState(694);
				type();
				}
				}
				setState(699);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassBodyContext extends ParserRuleContext {
		public List<ClassBodyDeclarationContext> classBodyDeclaration() {
			return getRuleContexts(ClassBodyDeclarationContext.class);
		}
		public ClassBodyDeclarationContext classBodyDeclaration(int i) {
			return getRuleContext(ClassBodyDeclarationContext.class,i);
		}
		public ClassBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterClassBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitClassBody(this);
		}
	}

	public final ClassBodyContext classBody() throws RecognitionException {
		ClassBodyContext _localctx = new ClassBodyContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_classBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(700);
			match(LBRACE);
			setState(704);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (ABSTRACT - 30)) | (1L << (BOOLEAN - 30)) | (1L << (BYTE - 30)) | (1L << (CHAR - 30)) | (1L << (CLASS - 30)) | (1L << (DOUBLE - 30)) | (1L << (ENUM - 30)) | (1L << (FINAL - 30)) | (1L << (FLOAT - 30)) | (1L << (INT - 30)) | (1L << (INTERFACE - 30)) | (1L << (LONG - 30)) | (1L << (NATIVE - 30)) | (1L << (PRIVATE - 30)) | (1L << (PROTECTED - 30)) | (1L << (PUBLIC - 30)) | (1L << (SHORT - 30)) | (1L << (STATIC - 30)) | (1L << (STRICTFP - 30)) | (1L << (SYNCHRONIZED - 30)) | (1L << (TRANSIENT - 30)) | (1L << (VOID - 30)) | (1L << (VOLATILE - 30)) | (1L << (LBRACE - 30)) | (1L << (SEMI - 30)))) != 0) || ((((_la - 95)) & ~0x3f) == 0 && ((1L << (_la - 95)) & ((1L << (LT - 95)) | (1L << (Identifier - 95)) | (1L << (AT - 95)))) != 0)) {
				{
				{
				setState(701);
				classBodyDeclaration();
				}
				}
				setState(706);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(707);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfaceBodyContext extends ParserRuleContext {
		public List<InterfaceBodyDeclarationContext> interfaceBodyDeclaration() {
			return getRuleContexts(InterfaceBodyDeclarationContext.class);
		}
		public InterfaceBodyDeclarationContext interfaceBodyDeclaration(int i) {
			return getRuleContext(InterfaceBodyDeclarationContext.class,i);
		}
		public InterfaceBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterInterfaceBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitInterfaceBody(this);
		}
	}

	public final InterfaceBodyContext interfaceBody() throws RecognitionException {
		InterfaceBodyContext _localctx = new InterfaceBodyContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_interfaceBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(709);
			match(LBRACE);
			setState(713);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (ABSTRACT - 30)) | (1L << (BOOLEAN - 30)) | (1L << (BYTE - 30)) | (1L << (CHAR - 30)) | (1L << (CLASS - 30)) | (1L << (DOUBLE - 30)) | (1L << (ENUM - 30)) | (1L << (FINAL - 30)) | (1L << (FLOAT - 30)) | (1L << (INT - 30)) | (1L << (INTERFACE - 30)) | (1L << (LONG - 30)) | (1L << (NATIVE - 30)) | (1L << (PRIVATE - 30)) | (1L << (PROTECTED - 30)) | (1L << (PUBLIC - 30)) | (1L << (SHORT - 30)) | (1L << (STATIC - 30)) | (1L << (STRICTFP - 30)) | (1L << (SYNCHRONIZED - 30)) | (1L << (TRANSIENT - 30)) | (1L << (VOID - 30)) | (1L << (VOLATILE - 30)) | (1L << (SEMI - 30)))) != 0) || ((((_la - 95)) & ~0x3f) == 0 && ((1L << (_la - 95)) & ((1L << (LT - 95)) | (1L << (Identifier - 95)) | (1L << (AT - 95)))) != 0)) {
				{
				{
				setState(710);
				interfaceBodyDeclaration();
				}
				}
				setState(715);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(716);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassBodyDeclarationContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public MemberDeclarationContext memberDeclaration() {
			return getRuleContext(MemberDeclarationContext.class,0);
		}
		public List<ModifierContext> modifier() {
			return getRuleContexts(ModifierContext.class);
		}
		public ModifierContext modifier(int i) {
			return getRuleContext(ModifierContext.class,i);
		}
		public ClassBodyDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classBodyDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterClassBodyDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitClassBodyDeclaration(this);
		}
	}

	public final ClassBodyDeclarationContext classBodyDeclaration() throws RecognitionException {
		ClassBodyDeclarationContext _localctx = new ClassBodyDeclarationContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_classBodyDeclaration);
		int _la;
		try {
			int _alt;
			setState(730);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,46,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(718);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(720);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==STATIC) {
					{
					setState(719);
					match(STATIC);
					}
				}

				setState(722);
				block();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(726);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,45,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(723);
						modifier();
						}
						} 
					}
					setState(728);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,45,_ctx);
				}
				setState(729);
				memberDeclaration();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MemberDeclarationContext extends ParserRuleContext {
		public MethodDeclarationContext methodDeclaration() {
			return getRuleContext(MethodDeclarationContext.class,0);
		}
		public GenericMethodDeclarationContext genericMethodDeclaration() {
			return getRuleContext(GenericMethodDeclarationContext.class,0);
		}
		public FieldDeclarationContext fieldDeclaration() {
			return getRuleContext(FieldDeclarationContext.class,0);
		}
		public ConstructorDeclarationContext constructorDeclaration() {
			return getRuleContext(ConstructorDeclarationContext.class,0);
		}
		public GenericConstructorDeclarationContext genericConstructorDeclaration() {
			return getRuleContext(GenericConstructorDeclarationContext.class,0);
		}
		public InterfaceDeclarationContext interfaceDeclaration() {
			return getRuleContext(InterfaceDeclarationContext.class,0);
		}
		public AnnotationTypeDeclarationContext annotationTypeDeclaration() {
			return getRuleContext(AnnotationTypeDeclarationContext.class,0);
		}
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public MemberDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterMemberDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitMemberDeclaration(this);
		}
	}

	public final MemberDeclarationContext memberDeclaration() throws RecognitionException {
		MemberDeclarationContext _localctx = new MemberDeclarationContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_memberDeclaration);
		try {
			setState(741);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(732);
				methodDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(733);
				genericMethodDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(734);
				fieldDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(735);
				constructorDeclaration();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(736);
				genericConstructorDeclaration();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(737);
				interfaceDeclaration();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(738);
				annotationTypeDeclaration();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(739);
				classDeclaration();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(740);
				enumDeclaration();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodDeclarationContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public FormalParametersContext formalParameters() {
			return getRuleContext(FormalParametersContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public MethodBodyContext methodBody() {
			return getRuleContext(MethodBodyContext.class,0);
		}
		public QualifiedNameListContext qualifiedNameList() {
			return getRuleContext(QualifiedNameListContext.class,0);
		}
		public MethodDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterMethodDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitMethodDeclaration(this);
		}
	}

	public final MethodDeclarationContext methodDeclaration() throws RecognitionException {
		MethodDeclarationContext _localctx = new MethodDeclarationContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_methodDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(745);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
			case Identifier:
				{
				setState(743);
				type();
				}
				break;
			case VOID:
				{
				setState(744);
				match(VOID);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(747);
			match(Identifier);
			setState(748);
			formalParameters();
			setState(753);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(749);
				match(LBRACK);
				setState(750);
				match(RBRACK);
				}
				}
				setState(755);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(758);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==THROWS) {
				{
				setState(756);
				match(THROWS);
				setState(757);
				qualifiedNameList();
				}
			}

			setState(762);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRACE:
				{
				setState(760);
				methodBody();
				}
				break;
			case SEMI:
				{
				setState(761);
				match(SEMI);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GenericMethodDeclarationContext extends ParserRuleContext {
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public MethodDeclarationContext methodDeclaration() {
			return getRuleContext(MethodDeclarationContext.class,0);
		}
		public GenericMethodDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericMethodDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterGenericMethodDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitGenericMethodDeclaration(this);
		}
	}

	public final GenericMethodDeclarationContext genericMethodDeclaration() throws RecognitionException {
		GenericMethodDeclarationContext _localctx = new GenericMethodDeclarationContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_genericMethodDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(764);
			typeParameters();
			setState(765);
			methodDeclaration();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstructorDeclarationContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public FormalParametersContext formalParameters() {
			return getRuleContext(FormalParametersContext.class,0);
		}
		public ConstructorBodyContext constructorBody() {
			return getRuleContext(ConstructorBodyContext.class,0);
		}
		public QualifiedNameListContext qualifiedNameList() {
			return getRuleContext(QualifiedNameListContext.class,0);
		}
		public ConstructorDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructorDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterConstructorDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitConstructorDeclaration(this);
		}
	}

	public final ConstructorDeclarationContext constructorDeclaration() throws RecognitionException {
		ConstructorDeclarationContext _localctx = new ConstructorDeclarationContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_constructorDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(767);
			match(Identifier);
			setState(768);
			formalParameters();
			setState(771);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==THROWS) {
				{
				setState(769);
				match(THROWS);
				setState(770);
				qualifiedNameList();
				}
			}

			setState(773);
			constructorBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GenericConstructorDeclarationContext extends ParserRuleContext {
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public ConstructorDeclarationContext constructorDeclaration() {
			return getRuleContext(ConstructorDeclarationContext.class,0);
		}
		public GenericConstructorDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericConstructorDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterGenericConstructorDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitGenericConstructorDeclaration(this);
		}
	}

	public final GenericConstructorDeclarationContext genericConstructorDeclaration() throws RecognitionException {
		GenericConstructorDeclarationContext _localctx = new GenericConstructorDeclarationContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_genericConstructorDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(775);
			typeParameters();
			setState(776);
			constructorDeclaration();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldDeclarationContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public VariableDeclaratorsContext variableDeclarators() {
			return getRuleContext(VariableDeclaratorsContext.class,0);
		}
		public FieldDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterFieldDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitFieldDeclaration(this);
		}
	}

	public final FieldDeclarationContext fieldDeclaration() throws RecognitionException {
		FieldDeclarationContext _localctx = new FieldDeclarationContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_fieldDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(778);
			type();
			setState(779);
			variableDeclarators();
			setState(780);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfaceBodyDeclarationContext extends ParserRuleContext {
		public InterfaceMemberDeclarationContext interfaceMemberDeclaration() {
			return getRuleContext(InterfaceMemberDeclarationContext.class,0);
		}
		public List<ModifierContext> modifier() {
			return getRuleContexts(ModifierContext.class);
		}
		public ModifierContext modifier(int i) {
			return getRuleContext(ModifierContext.class,i);
		}
		public InterfaceBodyDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceBodyDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterInterfaceBodyDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitInterfaceBodyDeclaration(this);
		}
	}

	public final InterfaceBodyDeclarationContext interfaceBodyDeclaration() throws RecognitionException {
		InterfaceBodyDeclarationContext _localctx = new InterfaceBodyDeclarationContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_interfaceBodyDeclaration);
		try {
			int _alt;
			setState(790);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ABSTRACT:
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case CLASS:
			case DOUBLE:
			case ENUM:
			case FINAL:
			case FLOAT:
			case INT:
			case INTERFACE:
			case LONG:
			case NATIVE:
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case SHORT:
			case STATIC:
			case STRICTFP:
			case SYNCHRONIZED:
			case TRANSIENT:
			case VOID:
			case VOLATILE:
			case LT:
			case Identifier:
			case AT:
				enterOuterAlt(_localctx, 1);
				{
				setState(785);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(782);
						modifier();
						}
						} 
					}
					setState(787);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
				}
				setState(788);
				interfaceMemberDeclaration();
				}
				break;
			case SEMI:
				enterOuterAlt(_localctx, 2);
				{
				setState(789);
				match(SEMI);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfaceMemberDeclarationContext extends ParserRuleContext {
		public ConstDeclarationContext constDeclaration() {
			return getRuleContext(ConstDeclarationContext.class,0);
		}
		public InterfaceMethodDeclarationContext interfaceMethodDeclaration() {
			return getRuleContext(InterfaceMethodDeclarationContext.class,0);
		}
		public GenericInterfaceMethodDeclarationContext genericInterfaceMethodDeclaration() {
			return getRuleContext(GenericInterfaceMethodDeclarationContext.class,0);
		}
		public InterfaceDeclarationContext interfaceDeclaration() {
			return getRuleContext(InterfaceDeclarationContext.class,0);
		}
		public AnnotationTypeDeclarationContext annotationTypeDeclaration() {
			return getRuleContext(AnnotationTypeDeclarationContext.class,0);
		}
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public InterfaceMemberDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceMemberDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterInterfaceMemberDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitInterfaceMemberDeclaration(this);
		}
	}

	public final InterfaceMemberDeclarationContext interfaceMemberDeclaration() throws RecognitionException {
		InterfaceMemberDeclarationContext _localctx = new InterfaceMemberDeclarationContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_interfaceMemberDeclaration);
		try {
			setState(799);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(792);
				constDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(793);
				interfaceMethodDeclaration();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(794);
				genericInterfaceMethodDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(795);
				interfaceDeclaration();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(796);
				annotationTypeDeclaration();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(797);
				classDeclaration();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(798);
				enumDeclaration();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstDeclarationContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public List<ConstantDeclaratorContext> constantDeclarator() {
			return getRuleContexts(ConstantDeclaratorContext.class);
		}
		public ConstantDeclaratorContext constantDeclarator(int i) {
			return getRuleContext(ConstantDeclaratorContext.class,i);
		}
		public ConstDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterConstDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitConstDeclaration(this);
		}
	}

	public final ConstDeclarationContext constDeclaration() throws RecognitionException {
		ConstDeclarationContext _localctx = new ConstDeclarationContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_constDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(801);
			type();
			setState(802);
			constantDeclarator();
			setState(807);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(803);
				match(COMMA);
				setState(804);
				constantDeclarator();
				}
				}
				setState(809);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(810);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantDeclaratorContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public VariableInitializerContext variableInitializer() {
			return getRuleContext(VariableInitializerContext.class,0);
		}
		public ConstantDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constantDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterConstantDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitConstantDeclarator(this);
		}
	}

	public final ConstantDeclaratorContext constantDeclarator() throws RecognitionException {
		ConstantDeclaratorContext _localctx = new ConstantDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_constantDeclarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(812);
			match(Identifier);
			setState(817);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(813);
				match(LBRACK);
				setState(814);
				match(RBRACK);
				}
				}
				setState(819);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(820);
			match(ASSIGN);
			setState(821);
			variableInitializer();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfaceMethodDeclarationContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public FormalParametersContext formalParameters() {
			return getRuleContext(FormalParametersContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public QualifiedNameListContext qualifiedNameList() {
			return getRuleContext(QualifiedNameListContext.class,0);
		}
		public InterfaceMethodDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceMethodDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterInterfaceMethodDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitInterfaceMethodDeclaration(this);
		}
	}

	public final InterfaceMethodDeclarationContext interfaceMethodDeclaration() throws RecognitionException {
		InterfaceMethodDeclarationContext _localctx = new InterfaceMethodDeclarationContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_interfaceMethodDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(825);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
			case Identifier:
				{
				setState(823);
				type();
				}
				break;
			case VOID:
				{
				setState(824);
				match(VOID);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(827);
			match(Identifier);
			setState(828);
			formalParameters();
			setState(833);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(829);
				match(LBRACK);
				setState(830);
				match(RBRACK);
				}
				}
				setState(835);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(838);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==THROWS) {
				{
				setState(836);
				match(THROWS);
				setState(837);
				qualifiedNameList();
				}
			}

			setState(840);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GenericInterfaceMethodDeclarationContext extends ParserRuleContext {
		public TypeParametersContext typeParameters() {
			return getRuleContext(TypeParametersContext.class,0);
		}
		public InterfaceMethodDeclarationContext interfaceMethodDeclaration() {
			return getRuleContext(InterfaceMethodDeclarationContext.class,0);
		}
		public GenericInterfaceMethodDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericInterfaceMethodDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterGenericInterfaceMethodDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitGenericInterfaceMethodDeclaration(this);
		}
	}

	public final GenericInterfaceMethodDeclarationContext genericInterfaceMethodDeclaration() throws RecognitionException {
		GenericInterfaceMethodDeclarationContext _localctx = new GenericInterfaceMethodDeclarationContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_genericInterfaceMethodDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(842);
			typeParameters();
			setState(843);
			interfaceMethodDeclaration();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableDeclaratorsContext extends ParserRuleContext {
		public List<VariableDeclaratorContext> variableDeclarator() {
			return getRuleContexts(VariableDeclaratorContext.class);
		}
		public VariableDeclaratorContext variableDeclarator(int i) {
			return getRuleContext(VariableDeclaratorContext.class,i);
		}
		public VariableDeclaratorsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclarators; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterVariableDeclarators(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitVariableDeclarators(this);
		}
	}

	public final VariableDeclaratorsContext variableDeclarators() throws RecognitionException {
		VariableDeclaratorsContext _localctx = new VariableDeclaratorsContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_variableDeclarators);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(845);
			variableDeclarator();
			setState(850);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(846);
				match(COMMA);
				setState(847);
				variableDeclarator();
				}
				}
				setState(852);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableDeclaratorContext extends ParserRuleContext {
		public VariableDeclaratorIdContext variableDeclaratorId() {
			return getRuleContext(VariableDeclaratorIdContext.class,0);
		}
		public VariableInitializerContext variableInitializer() {
			return getRuleContext(VariableInitializerContext.class,0);
		}
		public VariableDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterVariableDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitVariableDeclarator(this);
		}
	}

	public final VariableDeclaratorContext variableDeclarator() throws RecognitionException {
		VariableDeclaratorContext _localctx = new VariableDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_variableDeclarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(853);
			variableDeclaratorId();
			setState(856);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(854);
				match(ASSIGN);
				setState(855);
				variableInitializer();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableDeclaratorIdContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public VariableDeclaratorIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclaratorId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterVariableDeclaratorId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitVariableDeclaratorId(this);
		}
	}

	public final VariableDeclaratorIdContext variableDeclaratorId() throws RecognitionException {
		VariableDeclaratorIdContext _localctx = new VariableDeclaratorIdContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_variableDeclaratorId);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(858);
			match(Identifier);
			setState(863);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(859);
				match(LBRACK);
				setState(860);
				match(RBRACK);
				}
				}
				setState(865);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableInitializerContext extends ParserRuleContext {
		public ArrayInitializerContext arrayInitializer() {
			return getRuleContext(ArrayInitializerContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterVariableInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitVariableInitializer(this);
		}
	}

	public final VariableInitializerContext variableInitializer() throws RecognitionException {
		VariableInitializerContext _localctx = new VariableInitializerContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_variableInitializer);
		try {
			setState(868);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(866);
				arrayInitializer();
				}
				break;
			case IntegerLiteral:
			case FloatingPointLiteral:
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case NEW:
			case SHORT:
			case SUPER:
			case THIS:
			case VOID:
			case BooleanLiteral:
			case CharacterLiteral:
			case StringLiteral:
			case NullLiteral:
			case LPAREN:
			case LT:
			case BANG:
			case TILDE:
			case INC:
			case DEC:
			case ADD:
			case SUB:
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(867);
				expression(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayInitializerContext extends ParserRuleContext {
		public List<VariableInitializerContext> variableInitializer() {
			return getRuleContexts(VariableInitializerContext.class);
		}
		public VariableInitializerContext variableInitializer(int i) {
			return getRuleContext(VariableInitializerContext.class,i);
		}
		public ArrayInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterArrayInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitArrayInitializer(this);
		}
	}

	public final ArrayInitializerContext arrayInitializer() throws RecognitionException {
		ArrayInitializerContext _localctx = new ArrayInitializerContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_arrayInitializer);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(870);
			match(LBRACE);
			setState(882);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (SHORT - 66)) | (1L << (SUPER - 66)) | (1L << (THIS - 66)) | (1L << (VOID - 66)) | (1L << (BooleanLiteral - 66)) | (1L << (CharacterLiteral - 66)) | (1L << (StringLiteral - 66)) | (1L << (NullLiteral - 66)) | (1L << (LPAREN - 66)) | (1L << (LBRACE - 66)) | (1L << (LT - 66)) | (1L << (BANG - 66)) | (1L << (TILDE - 66)) | (1L << (INC - 66)) | (1L << (DEC - 66)) | (1L << (ADD - 66)) | (1L << (SUB - 66)) | (1L << (Identifier - 66)))) != 0)) {
				{
				setState(871);
				variableInitializer();
				setState(876);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(872);
						match(COMMA);
						setState(873);
						variableInitializer();
						}
						} 
					}
					setState(878);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
				}
				setState(880);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(879);
					match(COMMA);
					}
				}

				}
			}

			setState(884);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumConstantNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public EnumConstantNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumConstantName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterEnumConstantName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitEnumConstantName(this);
		}
	}

	public final EnumConstantNameContext enumConstantName() throws RecognitionException {
		EnumConstantNameContext _localctx = new EnumConstantNameContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_enumConstantName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(886);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public ClassOrInterfaceTypeContext classOrInterfaceType() {
			return getRuleContext(ClassOrInterfaceTypeContext.class,0);
		}
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitType(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_type);
		try {
			int _alt;
			setState(904);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(888);
				classOrInterfaceType();
				setState(893);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,68,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(889);
						match(LBRACK);
						setState(890);
						match(RBRACK);
						}
						} 
					}
					setState(895);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,68,_ctx);
				}
				}
				break;
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
				enterOuterAlt(_localctx, 2);
				{
				setState(896);
				primitiveType();
				setState(901);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(897);
						match(LBRACK);
						setState(898);
						match(RBRACK);
						}
						} 
					}
					setState(903);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,69,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassOrInterfaceTypeContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() { return getTokens(CQNGrammarParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(CQNGrammarParser.Identifier, i);
		}
		public List<TypeArgumentsContext> typeArguments() {
			return getRuleContexts(TypeArgumentsContext.class);
		}
		public TypeArgumentsContext typeArguments(int i) {
			return getRuleContext(TypeArgumentsContext.class,i);
		}
		public ClassOrInterfaceTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classOrInterfaceType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterClassOrInterfaceType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitClassOrInterfaceType(this);
		}
	}

	public final ClassOrInterfaceTypeContext classOrInterfaceType() throws RecognitionException {
		ClassOrInterfaceTypeContext _localctx = new ClassOrInterfaceTypeContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_classOrInterfaceType);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(906);
			match(Identifier);
			setState(908);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
			case 1:
				{
				setState(907);
				typeArguments();
				}
				break;
			}
			setState(917);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,73,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(910);
					match(DOT);
					setState(911);
					match(Identifier);
					setState(913);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,72,_ctx) ) {
					case 1:
						{
						setState(912);
						typeArguments();
						}
						break;
					}
					}
					} 
				}
				setState(919);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,73,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrimitiveTypeContext extends ParserRuleContext {
		public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterPrimitiveType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitPrimitiveType(this);
		}
	}

	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(920);
			_la = _input.LA(1);
			if ( !(((((_la - 32)) & ~0x3f) == 0 && ((1L << (_la - 32)) & ((1L << (BOOLEAN - 32)) | (1L << (BYTE - 32)) | (1L << (CHAR - 32)) | (1L << (DOUBLE - 32)) | (1L << (FLOAT - 32)) | (1L << (INT - 32)) | (1L << (LONG - 32)) | (1L << (SHORT - 32)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeArgumentsContext extends ParserRuleContext {
		public List<TypeArgumentContext> typeArgument() {
			return getRuleContexts(TypeArgumentContext.class);
		}
		public TypeArgumentContext typeArgument(int i) {
			return getRuleContext(TypeArgumentContext.class,i);
		}
		public TypeArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeArguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterTypeArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitTypeArguments(this);
		}
	}

	public final TypeArgumentsContext typeArguments() throws RecognitionException {
		TypeArgumentsContext _localctx = new TypeArgumentsContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_typeArguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(922);
			match(LT);
			setState(923);
			typeArgument();
			setState(928);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(924);
				match(COMMA);
				setState(925);
				typeArgument();
				}
				}
				setState(930);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(931);
			match(GT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeArgumentContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TypeArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeArgument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterTypeArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitTypeArgument(this);
		}
	}

	public final TypeArgumentContext typeArgument() throws RecognitionException {
		TypeArgumentContext _localctx = new TypeArgumentContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_typeArgument);
		int _la;
		try {
			setState(939);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(933);
				type();
				}
				break;
			case QUESTION:
				enterOuterAlt(_localctx, 2);
				{
				setState(934);
				match(QUESTION);
				setState(937);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==EXTENDS || _la==SUPER) {
					{
					setState(935);
					_la = _input.LA(1);
					if ( !(_la==EXTENDS || _la==SUPER) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(936);
					type();
					}
				}

				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QualifiedNameListContext extends ParserRuleContext {
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
		}
		public QualifiedNameListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedNameList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterQualifiedNameList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitQualifiedNameList(this);
		}
	}

	public final QualifiedNameListContext qualifiedNameList() throws RecognitionException {
		QualifiedNameListContext _localctx = new QualifiedNameListContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_qualifiedNameList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(941);
			qualifiedName();
			setState(946);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(942);
				match(COMMA);
				setState(943);
				qualifiedName();
				}
				}
				setState(948);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormalParametersContext extends ParserRuleContext {
		public FormalParameterListContext formalParameterList() {
			return getRuleContext(FormalParameterListContext.class,0);
		}
		public FormalParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterFormalParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitFormalParameters(this);
		}
	}

	public final FormalParametersContext formalParameters() throws RecognitionException {
		FormalParametersContext _localctx = new FormalParametersContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_formalParameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(949);
			match(LPAREN);
			setState(951);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << LONG))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (SHORT - 66)) | (1L << (Identifier - 66)) | (1L << (AT - 66)))) != 0)) {
				{
				setState(950);
				formalParameterList();
				}
			}

			setState(953);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormalParameterListContext extends ParserRuleContext {
		public List<FormalParameterContext> formalParameter() {
			return getRuleContexts(FormalParameterContext.class);
		}
		public FormalParameterContext formalParameter(int i) {
			return getRuleContext(FormalParameterContext.class,i);
		}
		public LastFormalParameterContext lastFormalParameter() {
			return getRuleContext(LastFormalParameterContext.class,0);
		}
		public FormalParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalParameterList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterFormalParameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitFormalParameterList(this);
		}
	}

	public final FormalParameterListContext formalParameterList() throws RecognitionException {
		FormalParameterListContext _localctx = new FormalParameterListContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_formalParameterList);
		int _la;
		try {
			int _alt;
			setState(968);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(955);
				formalParameter();
				setState(960);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,79,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(956);
						match(COMMA);
						setState(957);
						formalParameter();
						}
						} 
					}
					setState(962);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,79,_ctx);
				}
				setState(965);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(963);
					match(COMMA);
					setState(964);
					lastFormalParameter();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(967);
				lastFormalParameter();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormalParameterContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public VariableDeclaratorIdContext variableDeclaratorId() {
			return getRuleContext(VariableDeclaratorIdContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public FormalParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterFormalParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitFormalParameter(this);
		}
	}

	public final FormalParameterContext formalParameter() throws RecognitionException {
		FormalParameterContext _localctx = new FormalParameterContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_formalParameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(973);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FINAL || _la==AT) {
				{
				{
				setState(970);
				variableModifier();
				}
				}
				setState(975);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(976);
			type();
			setState(977);
			variableDeclaratorId();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LastFormalParameterContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public VariableDeclaratorIdContext variableDeclaratorId() {
			return getRuleContext(VariableDeclaratorIdContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public LastFormalParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lastFormalParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterLastFormalParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitLastFormalParameter(this);
		}
	}

	public final LastFormalParameterContext lastFormalParameter() throws RecognitionException {
		LastFormalParameterContext _localctx = new LastFormalParameterContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_lastFormalParameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(982);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FINAL || _la==AT) {
				{
				{
				setState(979);
				variableModifier();
				}
				}
				setState(984);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(985);
			type();
			setState(986);
			match(ELLIPSIS);
			setState(987);
			variableDeclaratorId();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodBodyContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public MethodBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterMethodBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitMethodBody(this);
		}
	}

	public final MethodBodyContext methodBody() throws RecognitionException {
		MethodBodyContext _localctx = new MethodBodyContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_methodBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(989);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstructorBodyContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ConstructorBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructorBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterConstructorBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitConstructorBody(this);
		}
	}

	public final ConstructorBodyContext constructorBody() throws RecognitionException {
		ConstructorBodyContext _localctx = new ConstructorBodyContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_constructorBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(991);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QualifiedNameContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() { return getTokens(CQNGrammarParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(CQNGrammarParser.Identifier, i);
		}
		public QualifiedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterQualifiedName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitQualifiedName(this);
		}
	}

	public final QualifiedNameContext qualifiedName() throws RecognitionException {
		QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_qualifiedName);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(993);
			match(Identifier);
			setState(998);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,84,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(994);
					match(DOT);
					setState(995);
					match(Identifier);
					}
					} 
				}
				setState(1000);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,84,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationContext extends ParserRuleContext {
		public AnnotationNameContext annotationName() {
			return getRuleContext(AnnotationNameContext.class,0);
		}
		public ElementValuePairsContext elementValuePairs() {
			return getRuleContext(ElementValuePairsContext.class,0);
		}
		public ElementValueContext elementValue() {
			return getRuleContext(ElementValueContext.class,0);
		}
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitAnnotation(this);
		}
	}

	public final AnnotationContext annotation() throws RecognitionException {
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_annotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1001);
			match(AT);
			setState(1002);
			annotationName();
			setState(1009);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(1003);
				match(LPAREN);
				setState(1006);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
				case 1:
					{
					setState(1004);
					elementValuePairs();
					}
					break;
				case 2:
					{
					setState(1005);
					elementValue();
					}
					break;
				}
				setState(1008);
				match(RPAREN);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationNameContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public AnnotationNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterAnnotationName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitAnnotationName(this);
		}
	}

	public final AnnotationNameContext annotationName() throws RecognitionException {
		AnnotationNameContext _localctx = new AnnotationNameContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_annotationName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1011);
			qualifiedName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElementValuePairsContext extends ParserRuleContext {
		public List<ElementValuePairContext> elementValuePair() {
			return getRuleContexts(ElementValuePairContext.class);
		}
		public ElementValuePairContext elementValuePair(int i) {
			return getRuleContext(ElementValuePairContext.class,i);
		}
		public ElementValuePairsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValuePairs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterElementValuePairs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitElementValuePairs(this);
		}
	}

	public final ElementValuePairsContext elementValuePairs() throws RecognitionException {
		ElementValuePairsContext _localctx = new ElementValuePairsContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_elementValuePairs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1013);
			elementValuePair();
			setState(1018);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1014);
				match(COMMA);
				setState(1015);
				elementValuePair();
				}
				}
				setState(1020);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElementValuePairContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public ElementValueContext elementValue() {
			return getRuleContext(ElementValueContext.class,0);
		}
		public ElementValuePairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValuePair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterElementValuePair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitElementValuePair(this);
		}
	}

	public final ElementValuePairContext elementValuePair() throws RecognitionException {
		ElementValuePairContext _localctx = new ElementValuePairContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_elementValuePair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1021);
			match(Identifier);
			setState(1022);
			match(ASSIGN);
			setState(1023);
			elementValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElementValueContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public ElementValueArrayInitializerContext elementValueArrayInitializer() {
			return getRuleContext(ElementValueArrayInitializerContext.class,0);
		}
		public ElementValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterElementValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitElementValue(this);
		}
	}

	public final ElementValueContext elementValue() throws RecognitionException {
		ElementValueContext _localctx = new ElementValueContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_elementValue);
		try {
			setState(1028);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerLiteral:
			case FloatingPointLiteral:
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case NEW:
			case SHORT:
			case SUPER:
			case THIS:
			case VOID:
			case BooleanLiteral:
			case CharacterLiteral:
			case StringLiteral:
			case NullLiteral:
			case LPAREN:
			case LT:
			case BANG:
			case TILDE:
			case INC:
			case DEC:
			case ADD:
			case SUB:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(1025);
				expression(0);
				}
				break;
			case AT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1026);
				annotation();
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 3);
				{
				setState(1027);
				elementValueArrayInitializer();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElementValueArrayInitializerContext extends ParserRuleContext {
		public List<ElementValueContext> elementValue() {
			return getRuleContexts(ElementValueContext.class);
		}
		public ElementValueContext elementValue(int i) {
			return getRuleContext(ElementValueContext.class,i);
		}
		public ElementValueArrayInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValueArrayInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterElementValueArrayInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitElementValueArrayInitializer(this);
		}
	}

	public final ElementValueArrayInitializerContext elementValueArrayInitializer() throws RecognitionException {
		ElementValueArrayInitializerContext _localctx = new ElementValueArrayInitializerContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_elementValueArrayInitializer);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1030);
			match(LBRACE);
			setState(1039);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (SHORT - 66)) | (1L << (SUPER - 66)) | (1L << (THIS - 66)) | (1L << (VOID - 66)) | (1L << (BooleanLiteral - 66)) | (1L << (CharacterLiteral - 66)) | (1L << (StringLiteral - 66)) | (1L << (NullLiteral - 66)) | (1L << (LPAREN - 66)) | (1L << (LBRACE - 66)) | (1L << (LT - 66)) | (1L << (BANG - 66)) | (1L << (TILDE - 66)) | (1L << (INC - 66)) | (1L << (DEC - 66)) | (1L << (ADD - 66)) | (1L << (SUB - 66)) | (1L << (Identifier - 66)) | (1L << (AT - 66)))) != 0)) {
				{
				setState(1031);
				elementValue();
				setState(1036);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,89,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1032);
						match(COMMA);
						setState(1033);
						elementValue();
						}
						} 
					}
					setState(1038);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,89,_ctx);
				}
				}
			}

			setState(1042);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(1041);
				match(COMMA);
				}
			}

			setState(1044);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationTypeDeclarationContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public AnnotationTypeBodyContext annotationTypeBody() {
			return getRuleContext(AnnotationTypeBodyContext.class,0);
		}
		public AnnotationTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationTypeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterAnnotationTypeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitAnnotationTypeDeclaration(this);
		}
	}

	public final AnnotationTypeDeclarationContext annotationTypeDeclaration() throws RecognitionException {
		AnnotationTypeDeclarationContext _localctx = new AnnotationTypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_annotationTypeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1046);
			match(AT);
			setState(1047);
			match(INTERFACE);
			setState(1048);
			match(Identifier);
			setState(1049);
			annotationTypeBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationTypeBodyContext extends ParserRuleContext {
		public List<AnnotationTypeElementDeclarationContext> annotationTypeElementDeclaration() {
			return getRuleContexts(AnnotationTypeElementDeclarationContext.class);
		}
		public AnnotationTypeElementDeclarationContext annotationTypeElementDeclaration(int i) {
			return getRuleContext(AnnotationTypeElementDeclarationContext.class,i);
		}
		public AnnotationTypeBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationTypeBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterAnnotationTypeBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitAnnotationTypeBody(this);
		}
	}

	public final AnnotationTypeBodyContext annotationTypeBody() throws RecognitionException {
		AnnotationTypeBodyContext _localctx = new AnnotationTypeBodyContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_annotationTypeBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1051);
			match(LBRACE);
			setState(1055);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 30)) & ~0x3f) == 0 && ((1L << (_la - 30)) & ((1L << (ABSTRACT - 30)) | (1L << (BOOLEAN - 30)) | (1L << (BYTE - 30)) | (1L << (CHAR - 30)) | (1L << (CLASS - 30)) | (1L << (DOUBLE - 30)) | (1L << (ENUM - 30)) | (1L << (FINAL - 30)) | (1L << (FLOAT - 30)) | (1L << (INT - 30)) | (1L << (INTERFACE - 30)) | (1L << (LONG - 30)) | (1L << (NATIVE - 30)) | (1L << (PRIVATE - 30)) | (1L << (PROTECTED - 30)) | (1L << (PUBLIC - 30)) | (1L << (SHORT - 30)) | (1L << (STATIC - 30)) | (1L << (STRICTFP - 30)) | (1L << (SYNCHRONIZED - 30)) | (1L << (TRANSIENT - 30)) | (1L << (VOLATILE - 30)) | (1L << (SEMI - 30)))) != 0) || _la==Identifier || _la==AT) {
				{
				{
				setState(1052);
				annotationTypeElementDeclaration();
				}
				}
				setState(1057);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1058);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationTypeElementDeclarationContext extends ParserRuleContext {
		public AnnotationTypeElementRestContext annotationTypeElementRest() {
			return getRuleContext(AnnotationTypeElementRestContext.class,0);
		}
		public List<ModifierContext> modifier() {
			return getRuleContexts(ModifierContext.class);
		}
		public ModifierContext modifier(int i) {
			return getRuleContext(ModifierContext.class,i);
		}
		public AnnotationTypeElementDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationTypeElementDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterAnnotationTypeElementDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitAnnotationTypeElementDeclaration(this);
		}
	}

	public final AnnotationTypeElementDeclarationContext annotationTypeElementDeclaration() throws RecognitionException {
		AnnotationTypeElementDeclarationContext _localctx = new AnnotationTypeElementDeclarationContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_annotationTypeElementDeclaration);
		try {
			int _alt;
			setState(1068);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ABSTRACT:
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case CLASS:
			case DOUBLE:
			case ENUM:
			case FINAL:
			case FLOAT:
			case INT:
			case INTERFACE:
			case LONG:
			case NATIVE:
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case SHORT:
			case STATIC:
			case STRICTFP:
			case SYNCHRONIZED:
			case TRANSIENT:
			case VOLATILE:
			case Identifier:
			case AT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1063);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,93,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1060);
						modifier();
						}
						} 
					}
					setState(1065);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,93,_ctx);
				}
				setState(1066);
				annotationTypeElementRest();
				}
				break;
			case SEMI:
				enterOuterAlt(_localctx, 2);
				{
				setState(1067);
				match(SEMI);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationTypeElementRestContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public AnnotationMethodOrConstantRestContext annotationMethodOrConstantRest() {
			return getRuleContext(AnnotationMethodOrConstantRestContext.class,0);
		}
		public ClassDeclarationContext classDeclaration() {
			return getRuleContext(ClassDeclarationContext.class,0);
		}
		public InterfaceDeclarationContext interfaceDeclaration() {
			return getRuleContext(InterfaceDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public AnnotationTypeDeclarationContext annotationTypeDeclaration() {
			return getRuleContext(AnnotationTypeDeclarationContext.class,0);
		}
		public AnnotationTypeElementRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationTypeElementRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterAnnotationTypeElementRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitAnnotationTypeElementRest(this);
		}
	}

	public final AnnotationTypeElementRestContext annotationTypeElementRest() throws RecognitionException {
		AnnotationTypeElementRestContext _localctx = new AnnotationTypeElementRestContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_annotationTypeElementRest);
		try {
			setState(1090);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(1070);
				type();
				setState(1071);
				annotationMethodOrConstantRest();
				setState(1072);
				match(SEMI);
				}
				break;
			case CLASS:
				enterOuterAlt(_localctx, 2);
				{
				setState(1074);
				classDeclaration();
				setState(1076);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,95,_ctx) ) {
				case 1:
					{
					setState(1075);
					match(SEMI);
					}
					break;
				}
				}
				break;
			case INTERFACE:
				enterOuterAlt(_localctx, 3);
				{
				setState(1078);
				interfaceDeclaration();
				setState(1080);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,96,_ctx) ) {
				case 1:
					{
					setState(1079);
					match(SEMI);
					}
					break;
				}
				}
				break;
			case ENUM:
				enterOuterAlt(_localctx, 4);
				{
				setState(1082);
				enumDeclaration();
				setState(1084);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
				case 1:
					{
					setState(1083);
					match(SEMI);
					}
					break;
				}
				}
				break;
			case AT:
				enterOuterAlt(_localctx, 5);
				{
				setState(1086);
				annotationTypeDeclaration();
				setState(1088);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,98,_ctx) ) {
				case 1:
					{
					setState(1087);
					match(SEMI);
					}
					break;
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationMethodOrConstantRestContext extends ParserRuleContext {
		public AnnotationMethodRestContext annotationMethodRest() {
			return getRuleContext(AnnotationMethodRestContext.class,0);
		}
		public AnnotationConstantRestContext annotationConstantRest() {
			return getRuleContext(AnnotationConstantRestContext.class,0);
		}
		public AnnotationMethodOrConstantRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationMethodOrConstantRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterAnnotationMethodOrConstantRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitAnnotationMethodOrConstantRest(this);
		}
	}

	public final AnnotationMethodOrConstantRestContext annotationMethodOrConstantRest() throws RecognitionException {
		AnnotationMethodOrConstantRestContext _localctx = new AnnotationMethodOrConstantRestContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_annotationMethodOrConstantRest);
		try {
			setState(1094);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,100,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1092);
				annotationMethodRest();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1093);
				annotationConstantRest();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationMethodRestContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public DefaultValueContext defaultValue() {
			return getRuleContext(DefaultValueContext.class,0);
		}
		public AnnotationMethodRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationMethodRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterAnnotationMethodRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitAnnotationMethodRest(this);
		}
	}

	public final AnnotationMethodRestContext annotationMethodRest() throws RecognitionException {
		AnnotationMethodRestContext _localctx = new AnnotationMethodRestContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_annotationMethodRest);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1096);
			match(Identifier);
			setState(1097);
			match(LPAREN);
			setState(1098);
			match(RPAREN);
			setState(1100);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DEFAULT) {
				{
				setState(1099);
				defaultValue();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationConstantRestContext extends ParserRuleContext {
		public VariableDeclaratorsContext variableDeclarators() {
			return getRuleContext(VariableDeclaratorsContext.class,0);
		}
		public AnnotationConstantRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationConstantRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterAnnotationConstantRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitAnnotationConstantRest(this);
		}
	}

	public final AnnotationConstantRestContext annotationConstantRest() throws RecognitionException {
		AnnotationConstantRestContext _localctx = new AnnotationConstantRestContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_annotationConstantRest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1102);
			variableDeclarators();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DefaultValueContext extends ParserRuleContext {
		public ElementValueContext elementValue() {
			return getRuleContext(ElementValueContext.class,0);
		}
		public DefaultValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defaultValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterDefaultValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitDefaultValue(this);
		}
	}

	public final DefaultValueContext defaultValue() throws RecognitionException {
		DefaultValueContext _localctx = new DefaultValueContext(_ctx, getState());
		enterRule(_localctx, 198, RULE_defaultValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1104);
			match(DEFAULT);
			setState(1105);
			elementValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockContext extends ParserRuleContext {
		public List<BlockStatementContext> blockStatement() {
			return getRuleContexts(BlockStatementContext.class);
		}
		public BlockStatementContext blockStatement(int i) {
			return getRuleContext(BlockStatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitBlock(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1107);
			match(LBRACE);
			setState(1111);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 26)) & ~0x3f) == 0 && ((1L << (_la - 26)) & ((1L << (IntegerLiteral - 26)) | (1L << (FloatingPointLiteral - 26)) | (1L << (ABSTRACT - 26)) | (1L << (ASSERT - 26)) | (1L << (BOOLEAN - 26)) | (1L << (BREAK - 26)) | (1L << (BYTE - 26)) | (1L << (CHAR - 26)) | (1L << (CLASS - 26)) | (1L << (CONTINUE - 26)) | (1L << (DO - 26)) | (1L << (DOUBLE - 26)) | (1L << (ENUM - 26)) | (1L << (FINAL - 26)) | (1L << (FLOAT - 26)) | (1L << (FOR - 26)) | (1L << (IF - 26)) | (1L << (INT - 26)) | (1L << (INTERFACE - 26)) | (1L << (LONG - 26)) | (1L << (NEW - 26)) | (1L << (PRIVATE - 26)) | (1L << (PROTECTED - 26)) | (1L << (PUBLIC - 26)) | (1L << (RETURN - 26)) | (1L << (SHORT - 26)) | (1L << (STATIC - 26)) | (1L << (STRICTFP - 26)) | (1L << (SUPER - 26)) | (1L << (SWITCH - 26)) | (1L << (SYNCHRONIZED - 26)) | (1L << (THIS - 26)) | (1L << (THROW - 26)) | (1L << (TRY - 26)) | (1L << (VOID - 26)) | (1L << (WHILE - 26)) | (1L << (BooleanLiteral - 26)) | (1L << (CharacterLiteral - 26)) | (1L << (StringLiteral - 26)) | (1L << (NullLiteral - 26)) | (1L << (LPAREN - 26)) | (1L << (LBRACE - 26)))) != 0) || ((((_la - 90)) & ~0x3f) == 0 && ((1L << (_la - 90)) & ((1L << (SEMI - 90)) | (1L << (LT - 90)) | (1L << (BANG - 90)) | (1L << (TILDE - 90)) | (1L << (INC - 90)) | (1L << (DEC - 90)) | (1L << (ADD - 90)) | (1L << (SUB - 90)) | (1L << (Identifier - 90)) | (1L << (AT - 90)))) != 0)) {
				{
				{
				setState(1108);
				blockStatement();
				}
				}
				setState(1113);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1114);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockStatementContext extends ParserRuleContext {
		public LocalVariableDeclarationStatementContext localVariableDeclarationStatement() {
			return getRuleContext(LocalVariableDeclarationStatementContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TypeDeclarationContext typeDeclaration() {
			return getRuleContext(TypeDeclarationContext.class,0);
		}
		public BlockStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterBlockStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitBlockStatement(this);
		}
	}

	public final BlockStatementContext blockStatement() throws RecognitionException {
		BlockStatementContext _localctx = new BlockStatementContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_blockStatement);
		try {
			setState(1119);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,103,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1116);
				localVariableDeclarationStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1117);
				statement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1118);
				typeDeclaration();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LocalVariableDeclarationStatementContext extends ParserRuleContext {
		public LocalVariableDeclarationContext localVariableDeclaration() {
			return getRuleContext(LocalVariableDeclarationContext.class,0);
		}
		public LocalVariableDeclarationStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_localVariableDeclarationStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterLocalVariableDeclarationStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitLocalVariableDeclarationStatement(this);
		}
	}

	public final LocalVariableDeclarationStatementContext localVariableDeclarationStatement() throws RecognitionException {
		LocalVariableDeclarationStatementContext _localctx = new LocalVariableDeclarationStatementContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_localVariableDeclarationStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1121);
			localVariableDeclaration();
			setState(1122);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LocalVariableDeclarationContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public VariableDeclaratorsContext variableDeclarators() {
			return getRuleContext(VariableDeclaratorsContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public LocalVariableDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_localVariableDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterLocalVariableDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitLocalVariableDeclaration(this);
		}
	}

	public final LocalVariableDeclarationContext localVariableDeclaration() throws RecognitionException {
		LocalVariableDeclarationContext _localctx = new LocalVariableDeclarationContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_localVariableDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1127);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FINAL || _la==AT) {
				{
				{
				setState(1124);
				variableModifier();
				}
				}
				setState(1129);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1130);
			type();
			setState(1131);
			variableDeclarators();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode ASSERT() { return getToken(CQNGrammarParser.ASSERT, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ParExpressionContext parExpression() {
			return getRuleContext(ParExpressionContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public ForControlContext forControl() {
			return getRuleContext(ForControlContext.class,0);
		}
		public FinallyBlockContext finallyBlock() {
			return getRuleContext(FinallyBlockContext.class,0);
		}
		public List<CatchClauseContext> catchClause() {
			return getRuleContexts(CatchClauseContext.class);
		}
		public CatchClauseContext catchClause(int i) {
			return getRuleContext(CatchClauseContext.class,i);
		}
		public ResourceSpecificationContext resourceSpecification() {
			return getRuleContext(ResourceSpecificationContext.class,0);
		}
		public List<SwitchBlockStatementGroupContext> switchBlockStatementGroup() {
			return getRuleContexts(SwitchBlockStatementGroupContext.class);
		}
		public SwitchBlockStatementGroupContext switchBlockStatementGroup(int i) {
			return getRuleContext(SwitchBlockStatementGroupContext.class,i);
		}
		public List<SwitchLabelContext> switchLabel() {
			return getRuleContexts(SwitchLabelContext.class);
		}
		public SwitchLabelContext switchLabel(int i) {
			return getRuleContext(SwitchLabelContext.class,i);
		}
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public StatementExpressionContext statementExpression() {
			return getRuleContext(StatementExpressionContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitStatement(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_statement);
		int _la;
		try {
			int _alt;
			setState(1237);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,117,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1133);
				block();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1134);
				match(ASSERT);
				setState(1135);
				expression(0);
				setState(1138);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COLON) {
					{
					setState(1136);
					match(COLON);
					setState(1137);
					expression(0);
					}
				}

				setState(1140);
				match(SEMI);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1142);
				match(IF);
				setState(1143);
				parExpression();
				setState(1144);
				statement();
				setState(1147);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
				case 1:
					{
					setState(1145);
					match(ELSE);
					setState(1146);
					statement();
					}
					break;
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1149);
				match(FOR);
				setState(1150);
				match(LPAREN);
				setState(1151);
				forControl();
				setState(1152);
				match(RPAREN);
				setState(1153);
				statement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1155);
				match(WHILE);
				setState(1156);
				parExpression();
				setState(1157);
				statement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1159);
				match(DO);
				setState(1160);
				statement();
				setState(1161);
				match(WHILE);
				setState(1162);
				parExpression();
				setState(1163);
				match(SEMI);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1165);
				match(TRY);
				setState(1166);
				block();
				setState(1176);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case CATCH:
					{
					setState(1168); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(1167);
						catchClause();
						}
						}
						setState(1170); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==CATCH );
					setState(1173);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==FINALLY) {
						{
						setState(1172);
						finallyBlock();
						}
					}

					}
					break;
				case FINALLY:
					{
					setState(1175);
					finallyBlock();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1178);
				match(TRY);
				setState(1179);
				resourceSpecification();
				setState(1180);
				block();
				setState(1184);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==CATCH) {
					{
					{
					setState(1181);
					catchClause();
					}
					}
					setState(1186);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1188);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==FINALLY) {
					{
					setState(1187);
					finallyBlock();
					}
				}

				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1190);
				match(SWITCH);
				setState(1191);
				parExpression();
				setState(1192);
				match(LBRACE);
				setState(1196);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,112,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1193);
						switchBlockStatementGroup();
						}
						} 
					}
					setState(1198);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,112,_ctx);
				}
				setState(1202);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==CASE || _la==DEFAULT) {
					{
					{
					setState(1199);
					switchLabel();
					}
					}
					setState(1204);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1205);
				match(RBRACE);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(1207);
				match(SYNCHRONIZED);
				setState(1208);
				parExpression();
				setState(1209);
				block();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(1211);
				match(RETURN);
				setState(1213);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (SHORT - 66)) | (1L << (SUPER - 66)) | (1L << (THIS - 66)) | (1L << (VOID - 66)) | (1L << (BooleanLiteral - 66)) | (1L << (CharacterLiteral - 66)) | (1L << (StringLiteral - 66)) | (1L << (NullLiteral - 66)) | (1L << (LPAREN - 66)) | (1L << (LT - 66)) | (1L << (BANG - 66)) | (1L << (TILDE - 66)) | (1L << (INC - 66)) | (1L << (DEC - 66)) | (1L << (ADD - 66)) | (1L << (SUB - 66)) | (1L << (Identifier - 66)))) != 0)) {
					{
					setState(1212);
					expression(0);
					}
				}

				setState(1215);
				match(SEMI);
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(1216);
				match(THROW);
				setState(1217);
				expression(0);
				setState(1218);
				match(SEMI);
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(1220);
				match(BREAK);
				setState(1222);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Identifier) {
					{
					setState(1221);
					match(Identifier);
					}
				}

				setState(1224);
				match(SEMI);
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(1225);
				match(CONTINUE);
				setState(1227);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Identifier) {
					{
					setState(1226);
					match(Identifier);
					}
				}

				setState(1229);
				match(SEMI);
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(1230);
				match(SEMI);
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(1231);
				statementExpression();
				setState(1232);
				match(SEMI);
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(1234);
				match(Identifier);
				setState(1235);
				match(COLON);
				setState(1236);
				statement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CatchClauseContext extends ParserRuleContext {
		public CatchTypeContext catchType() {
			return getRuleContext(CatchTypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public CatchClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterCatchClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitCatchClause(this);
		}
	}

	public final CatchClauseContext catchClause() throws RecognitionException {
		CatchClauseContext _localctx = new CatchClauseContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_catchClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1239);
			match(CATCH);
			setState(1240);
			match(LPAREN);
			setState(1244);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FINAL || _la==AT) {
				{
				{
				setState(1241);
				variableModifier();
				}
				}
				setState(1246);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1247);
			catchType();
			setState(1248);
			match(Identifier);
			setState(1249);
			match(RPAREN);
			setState(1250);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CatchTypeContext extends ParserRuleContext {
		public List<QualifiedNameContext> qualifiedName() {
			return getRuleContexts(QualifiedNameContext.class);
		}
		public QualifiedNameContext qualifiedName(int i) {
			return getRuleContext(QualifiedNameContext.class,i);
		}
		public CatchTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterCatchType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitCatchType(this);
		}
	}

	public final CatchTypeContext catchType() throws RecognitionException {
		CatchTypeContext _localctx = new CatchTypeContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_catchType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1252);
			qualifiedName();
			setState(1257);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BITOR) {
				{
				{
				setState(1253);
				match(BITOR);
				setState(1254);
				qualifiedName();
				}
				}
				setState(1259);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FinallyBlockContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public FinallyBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_finallyBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterFinallyBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitFinallyBlock(this);
		}
	}

	public final FinallyBlockContext finallyBlock() throws RecognitionException {
		FinallyBlockContext _localctx = new FinallyBlockContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_finallyBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1260);
			match(FINALLY);
			setState(1261);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ResourceSpecificationContext extends ParserRuleContext {
		public ResourcesContext resources() {
			return getRuleContext(ResourcesContext.class,0);
		}
		public ResourceSpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resourceSpecification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterResourceSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitResourceSpecification(this);
		}
	}

	public final ResourceSpecificationContext resourceSpecification() throws RecognitionException {
		ResourceSpecificationContext _localctx = new ResourceSpecificationContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_resourceSpecification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1263);
			match(LPAREN);
			setState(1264);
			resources();
			setState(1266);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEMI) {
				{
				setState(1265);
				match(SEMI);
				}
			}

			setState(1268);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ResourcesContext extends ParserRuleContext {
		public List<ResourceContext> resource() {
			return getRuleContexts(ResourceContext.class);
		}
		public ResourceContext resource(int i) {
			return getRuleContext(ResourceContext.class,i);
		}
		public ResourcesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resources; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterResources(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitResources(this);
		}
	}

	public final ResourcesContext resources() throws RecognitionException {
		ResourcesContext _localctx = new ResourcesContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_resources);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1270);
			resource();
			setState(1275);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,121,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1271);
					match(SEMI);
					setState(1272);
					resource();
					}
					} 
				}
				setState(1277);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,121,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ResourceContext extends ParserRuleContext {
		public ClassOrInterfaceTypeContext classOrInterfaceType() {
			return getRuleContext(ClassOrInterfaceTypeContext.class,0);
		}
		public VariableDeclaratorIdContext variableDeclaratorId() {
			return getRuleContext(VariableDeclaratorIdContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public ResourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resource; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterResource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitResource(this);
		}
	}

	public final ResourceContext resource() throws RecognitionException {
		ResourceContext _localctx = new ResourceContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_resource);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1281);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FINAL || _la==AT) {
				{
				{
				setState(1278);
				variableModifier();
				}
				}
				setState(1283);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1284);
			classOrInterfaceType();
			setState(1285);
			variableDeclaratorId();
			setState(1286);
			match(ASSIGN);
			setState(1287);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SwitchBlockStatementGroupContext extends ParserRuleContext {
		public List<SwitchLabelContext> switchLabel() {
			return getRuleContexts(SwitchLabelContext.class);
		}
		public SwitchLabelContext switchLabel(int i) {
			return getRuleContext(SwitchLabelContext.class,i);
		}
		public List<BlockStatementContext> blockStatement() {
			return getRuleContexts(BlockStatementContext.class);
		}
		public BlockStatementContext blockStatement(int i) {
			return getRuleContext(BlockStatementContext.class,i);
		}
		public SwitchBlockStatementGroupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchBlockStatementGroup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterSwitchBlockStatementGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitSwitchBlockStatementGroup(this);
		}
	}

	public final SwitchBlockStatementGroupContext switchBlockStatementGroup() throws RecognitionException {
		SwitchBlockStatementGroupContext _localctx = new SwitchBlockStatementGroupContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_switchBlockStatementGroup);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1290); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1289);
				switchLabel();
				}
				}
				setState(1292); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==CASE || _la==DEFAULT );
			setState(1295); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1294);
				blockStatement();
				}
				}
				setState(1297); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 26)) & ~0x3f) == 0 && ((1L << (_la - 26)) & ((1L << (IntegerLiteral - 26)) | (1L << (FloatingPointLiteral - 26)) | (1L << (ABSTRACT - 26)) | (1L << (ASSERT - 26)) | (1L << (BOOLEAN - 26)) | (1L << (BREAK - 26)) | (1L << (BYTE - 26)) | (1L << (CHAR - 26)) | (1L << (CLASS - 26)) | (1L << (CONTINUE - 26)) | (1L << (DO - 26)) | (1L << (DOUBLE - 26)) | (1L << (ENUM - 26)) | (1L << (FINAL - 26)) | (1L << (FLOAT - 26)) | (1L << (FOR - 26)) | (1L << (IF - 26)) | (1L << (INT - 26)) | (1L << (INTERFACE - 26)) | (1L << (LONG - 26)) | (1L << (NEW - 26)) | (1L << (PRIVATE - 26)) | (1L << (PROTECTED - 26)) | (1L << (PUBLIC - 26)) | (1L << (RETURN - 26)) | (1L << (SHORT - 26)) | (1L << (STATIC - 26)) | (1L << (STRICTFP - 26)) | (1L << (SUPER - 26)) | (1L << (SWITCH - 26)) | (1L << (SYNCHRONIZED - 26)) | (1L << (THIS - 26)) | (1L << (THROW - 26)) | (1L << (TRY - 26)) | (1L << (VOID - 26)) | (1L << (WHILE - 26)) | (1L << (BooleanLiteral - 26)) | (1L << (CharacterLiteral - 26)) | (1L << (StringLiteral - 26)) | (1L << (NullLiteral - 26)) | (1L << (LPAREN - 26)) | (1L << (LBRACE - 26)))) != 0) || ((((_la - 90)) & ~0x3f) == 0 && ((1L << (_la - 90)) & ((1L << (SEMI - 90)) | (1L << (LT - 90)) | (1L << (BANG - 90)) | (1L << (TILDE - 90)) | (1L << (INC - 90)) | (1L << (DEC - 90)) | (1L << (ADD - 90)) | (1L << (SUB - 90)) | (1L << (Identifier - 90)) | (1L << (AT - 90)))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SwitchLabelContext extends ParserRuleContext {
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public EnumConstantNameContext enumConstantName() {
			return getRuleContext(EnumConstantNameContext.class,0);
		}
		public SwitchLabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchLabel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterSwitchLabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitSwitchLabel(this);
		}
	}

	public final SwitchLabelContext switchLabel() throws RecognitionException {
		SwitchLabelContext _localctx = new SwitchLabelContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_switchLabel);
		try {
			setState(1309);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,125,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1299);
				match(CASE);
				setState(1300);
				constantExpression();
				setState(1301);
				match(COLON);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1303);
				match(CASE);
				setState(1304);
				enumConstantName();
				setState(1305);
				match(COLON);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1307);
				match(DEFAULT);
				setState(1308);
				match(COLON);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForControlContext extends ParserRuleContext {
		public EnhancedForControlContext enhancedForControl() {
			return getRuleContext(EnhancedForControlContext.class,0);
		}
		public ForInitContext forInit() {
			return getRuleContext(ForInitContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ForUpdateContext forUpdate() {
			return getRuleContext(ForUpdateContext.class,0);
		}
		public ForControlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forControl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterForControl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitForControl(this);
		}
	}

	public final ForControlContext forControl() throws RecognitionException {
		ForControlContext _localctx = new ForControlContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_forControl);
		int _la;
		try {
			setState(1323);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,129,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1311);
				enhancedForControl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1313);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FINAL) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (SHORT - 66)) | (1L << (SUPER - 66)) | (1L << (THIS - 66)) | (1L << (VOID - 66)) | (1L << (BooleanLiteral - 66)) | (1L << (CharacterLiteral - 66)) | (1L << (StringLiteral - 66)) | (1L << (NullLiteral - 66)) | (1L << (LPAREN - 66)) | (1L << (LT - 66)) | (1L << (BANG - 66)) | (1L << (TILDE - 66)) | (1L << (INC - 66)) | (1L << (DEC - 66)) | (1L << (ADD - 66)) | (1L << (SUB - 66)) | (1L << (Identifier - 66)) | (1L << (AT - 66)))) != 0)) {
					{
					setState(1312);
					forInit();
					}
				}

				setState(1315);
				match(SEMI);
				setState(1317);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (SHORT - 66)) | (1L << (SUPER - 66)) | (1L << (THIS - 66)) | (1L << (VOID - 66)) | (1L << (BooleanLiteral - 66)) | (1L << (CharacterLiteral - 66)) | (1L << (StringLiteral - 66)) | (1L << (NullLiteral - 66)) | (1L << (LPAREN - 66)) | (1L << (LT - 66)) | (1L << (BANG - 66)) | (1L << (TILDE - 66)) | (1L << (INC - 66)) | (1L << (DEC - 66)) | (1L << (ADD - 66)) | (1L << (SUB - 66)) | (1L << (Identifier - 66)))) != 0)) {
					{
					setState(1316);
					expression(0);
					}
				}

				setState(1319);
				match(SEMI);
				setState(1321);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (SHORT - 66)) | (1L << (SUPER - 66)) | (1L << (THIS - 66)) | (1L << (VOID - 66)) | (1L << (BooleanLiteral - 66)) | (1L << (CharacterLiteral - 66)) | (1L << (StringLiteral - 66)) | (1L << (NullLiteral - 66)) | (1L << (LPAREN - 66)) | (1L << (LT - 66)) | (1L << (BANG - 66)) | (1L << (TILDE - 66)) | (1L << (INC - 66)) | (1L << (DEC - 66)) | (1L << (ADD - 66)) | (1L << (SUB - 66)) | (1L << (Identifier - 66)))) != 0)) {
					{
					setState(1320);
					forUpdate();
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForInitContext extends ParserRuleContext {
		public LocalVariableDeclarationContext localVariableDeclaration() {
			return getRuleContext(LocalVariableDeclarationContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ForInitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forInit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterForInit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitForInit(this);
		}
	}

	public final ForInitContext forInit() throws RecognitionException {
		ForInitContext _localctx = new ForInitContext(_ctx, getState());
		enterRule(_localctx, 228, RULE_forInit);
		try {
			setState(1327);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,130,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1325);
				localVariableDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1326);
				expressionList();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnhancedForControlContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public VariableDeclaratorIdContext variableDeclaratorId() {
			return getRuleContext(VariableDeclaratorIdContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<VariableModifierContext> variableModifier() {
			return getRuleContexts(VariableModifierContext.class);
		}
		public VariableModifierContext variableModifier(int i) {
			return getRuleContext(VariableModifierContext.class,i);
		}
		public EnhancedForControlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enhancedForControl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterEnhancedForControl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitEnhancedForControl(this);
		}
	}

	public final EnhancedForControlContext enhancedForControl() throws RecognitionException {
		EnhancedForControlContext _localctx = new EnhancedForControlContext(_ctx, getState());
		enterRule(_localctx, 230, RULE_enhancedForControl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1332);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FINAL || _la==AT) {
				{
				{
				setState(1329);
				variableModifier();
				}
				}
				setState(1334);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1335);
			type();
			setState(1336);
			variableDeclaratorId();
			setState(1337);
			match(COLON);
			setState(1338);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForUpdateContext extends ParserRuleContext {
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ForUpdateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forUpdate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterForUpdate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitForUpdate(this);
		}
	}

	public final ForUpdateContext forUpdate() throws RecognitionException {
		ForUpdateContext _localctx = new ForUpdateContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_forUpdate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1340);
			expressionList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParExpressionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ParExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterParExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitParExpression(this);
		}
	}

	public final ParExpressionContext parExpression() throws RecognitionException {
		ParExpressionContext _localctx = new ParExpressionContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_parExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1342);
			match(LPAREN);
			setState(1343);
			expression(0);
			setState(1344);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitExpressionList(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_expressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1346);
			expression(0);
			setState(1351);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1347);
				match(COMMA);
				setState(1348);
				expression(0);
				}
				}
				setState(1353);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementExpressionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StatementExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statementExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterStatementExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitStatementExpression(this);
		}
	}

	public final StatementExpressionContext statementExpression() throws RecognitionException {
		StatementExpressionContext _localctx = new StatementExpressionContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_statementExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1354);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantExpressionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ConstantExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constantExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterConstantExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitConstantExpression(this);
		}
	}

	public final ConstantExpressionContext constantExpression() throws RecognitionException {
		ConstantExpressionContext _localctx = new ConstantExpressionContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_constantExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1356);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public CreatorContext creator() {
			return getRuleContext(CreatorContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public InnerCreatorContext innerCreator() {
			return getRuleContext(InnerCreatorContext.class,0);
		}
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public SuperSuffixContext superSuffix() {
			return getRuleContext(SuperSuffixContext.class,0);
		}
		public ExplicitGenericInvocationContext explicitGenericInvocation() {
			return getRuleContext(ExplicitGenericInvocationContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitExpression(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 242;
		enterRecursionRule(_localctx, 242, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1371);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,133,_ctx) ) {
			case 1:
				{
				setState(1359);
				primary();
				}
				break;
			case 2:
				{
				setState(1360);
				match(NEW);
				setState(1361);
				creator();
				}
				break;
			case 3:
				{
				setState(1362);
				match(LPAREN);
				setState(1363);
				type();
				setState(1364);
				match(RPAREN);
				setState(1365);
				expression(17);
				}
				break;
			case 4:
				{
				setState(1367);
				_la = _input.LA(1);
				if ( !(((((_la - 106)) & ~0x3f) == 0 && ((1L << (_la - 106)) & ((1L << (INC - 106)) | (1L << (DEC - 106)) | (1L << (ADD - 106)) | (1L << (SUB - 106)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1368);
				expression(15);
				}
				break;
			case 5:
				{
				setState(1369);
				_la = _input.LA(1);
				if ( !(_la==BANG || _la==TILDE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1370);
				expression(14);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1458);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,138,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1456);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,137,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1373);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(1374);
						_la = _input.LA(1);
						if ( !(((((_la - 110)) & ~0x3f) == 0 && ((1L << (_la - 110)) & ((1L << (MUL - 110)) | (1L << (DIV - 110)) | (1L << (MOD - 110)))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1375);
						expression(14);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1376);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(1377);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1378);
						expression(13);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1379);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(1387);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
						case 1:
							{
							setState(1380);
							match(LT);
							setState(1381);
							match(LT);
							}
							break;
						case 2:
							{
							setState(1382);
							match(GT);
							setState(1383);
							match(GT);
							setState(1384);
							match(GT);
							}
							break;
						case 3:
							{
							setState(1385);
							match(GT);
							setState(1386);
							match(GT);
							}
							break;
						}
						setState(1389);
						expression(12);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1390);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(1391);
						_la = _input.LA(1);
						if ( !(((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & ((1L << (GT - 94)) | (1L << (LT - 94)) | (1L << (LE - 94)) | (1L << (GE - 94)))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1392);
						expression(11);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1393);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(1394);
						_la = _input.LA(1);
						if ( !(_la==EQUAL || _la==NOTEQUAL) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1395);
						expression(9);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1396);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(1397);
						match(BITAND);
						setState(1398);
						expression(8);
						}
						break;
					case 7:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1399);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(1400);
						match(CARET);
						setState(1401);
						expression(7);
						}
						break;
					case 8:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1402);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(1403);
						match(BITOR);
						setState(1404);
						expression(6);
						}
						break;
					case 9:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1405);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(1406);
						match(AND);
						setState(1407);
						expression(5);
						}
						break;
					case 10:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1408);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(1409);
						match(OR);
						setState(1410);
						expression(4);
						}
						break;
					case 11:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1411);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(1412);
						match(QUESTION);
						setState(1413);
						expression(0);
						setState(1414);
						match(COLON);
						setState(1415);
						expression(3);
						}
						break;
					case 12:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1417);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(1418);
						_la = _input.LA(1);
						if ( !(((((_la - 93)) & ~0x3f) == 0 && ((1L << (_la - 93)) & ((1L << (ASSIGN - 93)) | (1L << (ADD_ASSIGN - 93)) | (1L << (SUB_ASSIGN - 93)) | (1L << (MUL_ASSIGN - 93)) | (1L << (DIV_ASSIGN - 93)) | (1L << (AND_ASSIGN - 93)) | (1L << (OR_ASSIGN - 93)) | (1L << (XOR_ASSIGN - 93)) | (1L << (MOD_ASSIGN - 93)) | (1L << (LSHIFT_ASSIGN - 93)) | (1L << (RSHIFT_ASSIGN - 93)) | (1L << (URSHIFT_ASSIGN - 93)))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1419);
						expression(1);
						}
						break;
					case 13:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1420);
						if (!(precpred(_ctx, 25))) throw new FailedPredicateException(this, "precpred(_ctx, 25)");
						setState(1421);
						match(DOT);
						setState(1422);
						match(Identifier);
						}
						break;
					case 14:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1423);
						if (!(precpred(_ctx, 24))) throw new FailedPredicateException(this, "precpred(_ctx, 24)");
						setState(1424);
						match(DOT);
						setState(1425);
						match(THIS);
						}
						break;
					case 15:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1426);
						if (!(precpred(_ctx, 23))) throw new FailedPredicateException(this, "precpred(_ctx, 23)");
						setState(1427);
						match(DOT);
						setState(1428);
						match(NEW);
						setState(1430);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==LT) {
							{
							setState(1429);
							nonWildcardTypeArguments();
							}
						}

						setState(1432);
						innerCreator();
						}
						break;
					case 16:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1433);
						if (!(precpred(_ctx, 22))) throw new FailedPredicateException(this, "precpred(_ctx, 22)");
						setState(1434);
						match(DOT);
						setState(1435);
						match(SUPER);
						setState(1436);
						superSuffix();
						}
						break;
					case 17:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1437);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(1438);
						match(DOT);
						setState(1439);
						explicitGenericInvocation();
						}
						break;
					case 18:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1440);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(1441);
						match(LBRACK);
						setState(1442);
						expression(0);
						setState(1443);
						match(RBRACK);
						}
						break;
					case 19:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1445);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						setState(1446);
						match(LPAREN);
						setState(1448);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (SHORT - 66)) | (1L << (SUPER - 66)) | (1L << (THIS - 66)) | (1L << (VOID - 66)) | (1L << (BooleanLiteral - 66)) | (1L << (CharacterLiteral - 66)) | (1L << (StringLiteral - 66)) | (1L << (NullLiteral - 66)) | (1L << (LPAREN - 66)) | (1L << (LT - 66)) | (1L << (BANG - 66)) | (1L << (TILDE - 66)) | (1L << (INC - 66)) | (1L << (DEC - 66)) | (1L << (ADD - 66)) | (1L << (SUB - 66)) | (1L << (Identifier - 66)))) != 0)) {
							{
							setState(1447);
							expressionList();
							}
						}

						setState(1450);
						match(RPAREN);
						}
						break;
					case 20:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1451);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(1452);
						_la = _input.LA(1);
						if ( !(_la==INC || _la==DEC) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					case 21:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(1453);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(1454);
						match(INSTANCEOF);
						setState(1455);
						type();
						}
						break;
					}
					} 
				}
				setState(1460);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,138,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class PrimaryContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() {
			return getRuleContext(ExplicitGenericInvocationSuffixContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public PrimaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterPrimary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitPrimary(this);
		}
	}

	public final PrimaryContext primary() throws RecognitionException {
		PrimaryContext _localctx = new PrimaryContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_primary);
		try {
			setState(1482);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,140,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1461);
				match(LPAREN);
				setState(1462);
				expression(0);
				setState(1463);
				match(RPAREN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1465);
				match(THIS);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1466);
				match(SUPER);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1467);
				literal();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1468);
				match(Identifier);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1469);
				type();
				setState(1470);
				match(DOT);
				setState(1471);
				match(CLASS);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1473);
				match(VOID);
				setState(1474);
				match(DOT);
				setState(1475);
				match(CLASS);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1476);
				nonWildcardTypeArguments();
				setState(1480);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case SUPER:
				case Identifier:
					{
					setState(1477);
					explicitGenericInvocationSuffix();
					}
					break;
				case THIS:
					{
					setState(1478);
					match(THIS);
					setState(1479);
					arguments();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CreatorContext extends ParserRuleContext {
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public CreatedNameContext createdName() {
			return getRuleContext(CreatedNameContext.class,0);
		}
		public ClassCreatorRestContext classCreatorRest() {
			return getRuleContext(ClassCreatorRestContext.class,0);
		}
		public ArrayCreatorRestContext arrayCreatorRest() {
			return getRuleContext(ArrayCreatorRestContext.class,0);
		}
		public CreatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_creator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterCreator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitCreator(this);
		}
	}

	public final CreatorContext creator() throws RecognitionException {
		CreatorContext _localctx = new CreatorContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_creator);
		try {
			setState(1493);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1484);
				nonWildcardTypeArguments();
				setState(1485);
				createdName();
				setState(1486);
				classCreatorRest();
				}
				break;
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(1488);
				createdName();
				setState(1491);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case LBRACK:
					{
					setState(1489);
					arrayCreatorRest();
					}
					break;
				case LPAREN:
					{
					setState(1490);
					classCreatorRest();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CreatedNameContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() { return getTokens(CQNGrammarParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(CQNGrammarParser.Identifier, i);
		}
		public List<TypeArgumentsOrDiamondContext> typeArgumentsOrDiamond() {
			return getRuleContexts(TypeArgumentsOrDiamondContext.class);
		}
		public TypeArgumentsOrDiamondContext typeArgumentsOrDiamond(int i) {
			return getRuleContext(TypeArgumentsOrDiamondContext.class,i);
		}
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public CreatedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createdName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterCreatedName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitCreatedName(this);
		}
	}

	public final CreatedNameContext createdName() throws RecognitionException {
		CreatedNameContext _localctx = new CreatedNameContext(_ctx, getState());
		enterRule(_localctx, 248, RULE_createdName);
		int _la;
		try {
			setState(1510);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(1495);
				match(Identifier);
				setState(1497);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LT) {
					{
					setState(1496);
					typeArgumentsOrDiamond();
					}
				}

				setState(1506);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==DOT) {
					{
					{
					setState(1499);
					match(DOT);
					setState(1500);
					match(Identifier);
					setState(1502);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LT) {
						{
						setState(1501);
						typeArgumentsOrDiamond();
						}
					}

					}
					}
					setState(1508);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1509);
				primitiveType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InnerCreatorContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public ClassCreatorRestContext classCreatorRest() {
			return getRuleContext(ClassCreatorRestContext.class,0);
		}
		public NonWildcardTypeArgumentsOrDiamondContext nonWildcardTypeArgumentsOrDiamond() {
			return getRuleContext(NonWildcardTypeArgumentsOrDiamondContext.class,0);
		}
		public InnerCreatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_innerCreator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterInnerCreator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitInnerCreator(this);
		}
	}

	public final InnerCreatorContext innerCreator() throws RecognitionException {
		InnerCreatorContext _localctx = new InnerCreatorContext(_ctx, getState());
		enterRule(_localctx, 250, RULE_innerCreator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1512);
			match(Identifier);
			setState(1514);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(1513);
				nonWildcardTypeArgumentsOrDiamond();
				}
			}

			setState(1516);
			classCreatorRest();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayCreatorRestContext extends ParserRuleContext {
		public ArrayInitializerContext arrayInitializer() {
			return getRuleContext(ArrayInitializerContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArrayCreatorRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayCreatorRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterArrayCreatorRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitArrayCreatorRest(this);
		}
	}

	public final ArrayCreatorRestContext arrayCreatorRest() throws RecognitionException {
		ArrayCreatorRestContext _localctx = new ArrayCreatorRestContext(_ctx, getState());
		enterRule(_localctx, 252, RULE_arrayCreatorRest);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1518);
			match(LBRACK);
			setState(1546);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case RBRACK:
				{
				setState(1519);
				match(RBRACK);
				setState(1524);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LBRACK) {
					{
					{
					setState(1520);
					match(LBRACK);
					setState(1521);
					match(RBRACK);
					}
					}
					setState(1526);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1527);
				arrayInitializer();
				}
				break;
			case IntegerLiteral:
			case FloatingPointLiteral:
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case NEW:
			case SHORT:
			case SUPER:
			case THIS:
			case VOID:
			case BooleanLiteral:
			case CharacterLiteral:
			case StringLiteral:
			case NullLiteral:
			case LPAREN:
			case LT:
			case BANG:
			case TILDE:
			case INC:
			case DEC:
			case ADD:
			case SUB:
			case Identifier:
				{
				setState(1528);
				expression(0);
				setState(1529);
				match(RBRACK);
				setState(1536);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,149,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1530);
						match(LBRACK);
						setState(1531);
						expression(0);
						setState(1532);
						match(RBRACK);
						}
						} 
					}
					setState(1538);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,149,_ctx);
				}
				setState(1543);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,150,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(1539);
						match(LBRACK);
						setState(1540);
						match(RBRACK);
						}
						} 
					}
					setState(1545);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,150,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassCreatorRestContext extends ParserRuleContext {
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public ClassBodyContext classBody() {
			return getRuleContext(ClassBodyContext.class,0);
		}
		public ClassCreatorRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classCreatorRest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterClassCreatorRest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitClassCreatorRest(this);
		}
	}

	public final ClassCreatorRestContext classCreatorRest() throws RecognitionException {
		ClassCreatorRestContext _localctx = new ClassCreatorRestContext(_ctx, getState());
		enterRule(_localctx, 254, RULE_classCreatorRest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1548);
			arguments();
			setState(1550);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,152,_ctx) ) {
			case 1:
				{
				setState(1549);
				classBody();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExplicitGenericInvocationContext extends ParserRuleContext {
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() {
			return getRuleContext(ExplicitGenericInvocationSuffixContext.class,0);
		}
		public ExplicitGenericInvocationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explicitGenericInvocation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterExplicitGenericInvocation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitExplicitGenericInvocation(this);
		}
	}

	public final ExplicitGenericInvocationContext explicitGenericInvocation() throws RecognitionException {
		ExplicitGenericInvocationContext _localctx = new ExplicitGenericInvocationContext(_ctx, getState());
		enterRule(_localctx, 256, RULE_explicitGenericInvocation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1552);
			nonWildcardTypeArguments();
			setState(1553);
			explicitGenericInvocationSuffix();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NonWildcardTypeArgumentsContext extends ParserRuleContext {
		public TypeListContext typeList() {
			return getRuleContext(TypeListContext.class,0);
		}
		public NonWildcardTypeArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonWildcardTypeArguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterNonWildcardTypeArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitNonWildcardTypeArguments(this);
		}
	}

	public final NonWildcardTypeArgumentsContext nonWildcardTypeArguments() throws RecognitionException {
		NonWildcardTypeArgumentsContext _localctx = new NonWildcardTypeArgumentsContext(_ctx, getState());
		enterRule(_localctx, 258, RULE_nonWildcardTypeArguments);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1555);
			match(LT);
			setState(1556);
			typeList();
			setState(1557);
			match(GT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeArgumentsOrDiamondContext extends ParserRuleContext {
		public TypeArgumentsContext typeArguments() {
			return getRuleContext(TypeArgumentsContext.class,0);
		}
		public TypeArgumentsOrDiamondContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeArgumentsOrDiamond; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterTypeArgumentsOrDiamond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitTypeArgumentsOrDiamond(this);
		}
	}

	public final TypeArgumentsOrDiamondContext typeArgumentsOrDiamond() throws RecognitionException {
		TypeArgumentsOrDiamondContext _localctx = new TypeArgumentsOrDiamondContext(_ctx, getState());
		enterRule(_localctx, 260, RULE_typeArgumentsOrDiamond);
		try {
			setState(1562);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,153,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1559);
				match(LT);
				setState(1560);
				match(GT);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1561);
				typeArguments();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NonWildcardTypeArgumentsOrDiamondContext extends ParserRuleContext {
		public NonWildcardTypeArgumentsContext nonWildcardTypeArguments() {
			return getRuleContext(NonWildcardTypeArgumentsContext.class,0);
		}
		public NonWildcardTypeArgumentsOrDiamondContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonWildcardTypeArgumentsOrDiamond; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterNonWildcardTypeArgumentsOrDiamond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitNonWildcardTypeArgumentsOrDiamond(this);
		}
	}

	public final NonWildcardTypeArgumentsOrDiamondContext nonWildcardTypeArgumentsOrDiamond() throws RecognitionException {
		NonWildcardTypeArgumentsOrDiamondContext _localctx = new NonWildcardTypeArgumentsOrDiamondContext(_ctx, getState());
		enterRule(_localctx, 262, RULE_nonWildcardTypeArgumentsOrDiamond);
		try {
			setState(1567);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,154,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1564);
				match(LT);
				setState(1565);
				match(GT);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1566);
				nonWildcardTypeArguments();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SuperSuffixContext extends ParserRuleContext {
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public SuperSuffixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_superSuffix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterSuperSuffix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitSuperSuffix(this);
		}
	}

	public final SuperSuffixContext superSuffix() throws RecognitionException {
		SuperSuffixContext _localctx = new SuperSuffixContext(_ctx, getState());
		enterRule(_localctx, 264, RULE_superSuffix);
		try {
			setState(1575);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(1569);
				arguments();
				}
				break;
			case DOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1570);
				match(DOT);
				setState(1571);
				match(Identifier);
				setState(1573);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,155,_ctx) ) {
				case 1:
					{
					setState(1572);
					arguments();
					}
					break;
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExplicitGenericInvocationSuffixContext extends ParserRuleContext {
		public SuperSuffixContext superSuffix() {
			return getRuleContext(SuperSuffixContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CQNGrammarParser.Identifier, 0); }
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public ExplicitGenericInvocationSuffixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explicitGenericInvocationSuffix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterExplicitGenericInvocationSuffix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitExplicitGenericInvocationSuffix(this);
		}
	}

	public final ExplicitGenericInvocationSuffixContext explicitGenericInvocationSuffix() throws RecognitionException {
		ExplicitGenericInvocationSuffixContext _localctx = new ExplicitGenericInvocationSuffixContext(_ctx, getState());
		enterRule(_localctx, 266, RULE_explicitGenericInvocationSuffix);
		try {
			setState(1581);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SUPER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1577);
				match(SUPER);
				setState(1578);
				superSuffix();
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(1579);
				match(Identifier);
				setState(1580);
				arguments();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentsContext extends ParserRuleContext {
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).enterArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CQNGrammarListener ) ((CQNGrammarListener)listener).exitArguments(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 268, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1583);
			match(LPAREN);
			setState(1585);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IntegerLiteral) | (1L << FloatingPointLiteral) | (1L << BOOLEAN) | (1L << BYTE) | (1L << CHAR) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG) | (1L << NEW))) != 0) || ((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (SHORT - 66)) | (1L << (SUPER - 66)) | (1L << (THIS - 66)) | (1L << (VOID - 66)) | (1L << (BooleanLiteral - 66)) | (1L << (CharacterLiteral - 66)) | (1L << (StringLiteral - 66)) | (1L << (NullLiteral - 66)) | (1L << (LPAREN - 66)) | (1L << (LT - 66)) | (1L << (BANG - 66)) | (1L << (TILDE - 66)) | (1L << (INC - 66)) | (1L << (DEC - 66)) | (1L << (ADD - 66)) | (1L << (SUB - 66)) | (1L << (Identifier - 66)))) != 0)) {
				{
				setState(1584);
				expressionList();
				}
			}

			setState(1587);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 121:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 13);
		case 1:
			return precpred(_ctx, 12);
		case 2:
			return precpred(_ctx, 11);
		case 3:
			return precpred(_ctx, 10);
		case 4:
			return precpred(_ctx, 8);
		case 5:
			return precpred(_ctx, 7);
		case 6:
			return precpred(_ctx, 6);
		case 7:
			return precpred(_ctx, 5);
		case 8:
			return precpred(_ctx, 4);
		case 9:
			return precpred(_ctx, 3);
		case 10:
			return precpred(_ctx, 2);
		case 11:
			return precpred(_ctx, 1);
		case 12:
			return precpred(_ctx, 25);
		case 13:
			return precpred(_ctx, 24);
		case 14:
			return precpred(_ctx, 23);
		case 15:
			return precpred(_ctx, 22);
		case 16:
			return precpred(_ctx, 21);
		case 17:
			return precpred(_ctx, 20);
		case 18:
			return precpred(_ctx, 19);
		case 19:
			return precpred(_ctx, 16);
		case 20:
			return precpred(_ctx, 9);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0086\u0638\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"+
		"`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\4k\t"+
		"k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\4u\tu\4v\tv\4"+
		"w\tw\4x\tx\4y\ty\4z\tz\4{\t{\4|\t|\4}\t}\4~\t~\4\177\t\177\4\u0080\t\u0080"+
		"\4\u0081\t\u0081\4\u0082\t\u0082\4\u0083\t\u0083\4\u0084\t\u0084\4\u0085"+
		"\t\u0085\4\u0086\t\u0086\4\u0087\t\u0087\4\u0088\t\u0088\3\2\3\2\3\2\5"+
		"\2\u0114\n\2\3\2\3\2\3\3\3\3\5\3\u011a\n\3\3\4\3\4\3\4\5\4\u011f\n\4\3"+
		"\5\3\5\3\5\3\5\3\5\6\5\u0126\n\5\r\5\16\5\u0127\3\5\3\5\3\6\3\6\3\6\3"+
		"\6\3\6\6\6\u0131\n\6\r\6\16\6\u0132\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\b"+
		"\u014e\n\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\7\20\u0190\n\20\f\20\16\20\u0193\13\20\3\20\3\20"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\27\3\27"+
		"\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\34\3\34"+
		"\3\35\3\35\5\35\u01df\n\35\3\36\3\36\3\37\3\37\3\37\3\37\3\37\7\37\u01e8"+
		"\n\37\f\37\16\37\u01eb\13\37\3\37\3\37\3 \3 \3!\3!\3!\3!\3!\7!\u01f6\n"+
		"!\f!\16!\u01f9\13!\3!\3!\3\"\3\"\3\"\3\"\3\"\3#\3#\3$\3$\3%\5%\u0207\n"+
		"%\3%\7%\u020a\n%\f%\16%\u020d\13%\3%\7%\u0210\n%\f%\16%\u0213\13%\3%\3"+
		"%\3&\7&\u0218\n&\f&\16&\u021b\13&\3&\3&\3&\3&\3\'\3\'\5\'\u0223\n\'\3"+
		"\'\3\'\3\'\5\'\u0228\n\'\3\'\3\'\3(\7(\u022d\n(\f(\16(\u0230\13(\3(\3"+
		"(\7(\u0234\n(\f(\16(\u0237\13(\3(\3(\7(\u023b\n(\f(\16(\u023e\13(\3(\3"+
		"(\7(\u0242\n(\f(\16(\u0245\13(\3(\3(\5(\u0249\n(\3)\3)\5)\u024d\n)\3*"+
		"\3*\5*\u0251\n*\3+\3+\5+\u0255\n+\3,\3,\3,\5,\u025a\n,\3,\3,\5,\u025e"+
		"\n,\3,\3,\5,\u0262\n,\3,\3,\3-\3-\3-\3-\7-\u026a\n-\f-\16-\u026d\13-\3"+
		"-\3-\3.\3.\3.\5.\u0274\n.\3/\3/\3/\7/\u0279\n/\f/\16/\u027c\13/\3\60\3"+
		"\60\3\60\3\60\5\60\u0282\n\60\3\60\3\60\5\60\u0286\n\60\3\60\5\60\u0289"+
		"\n\60\3\60\5\60\u028c\n\60\3\60\3\60\3\61\3\61\3\61\7\61\u0293\n\61\f"+
		"\61\16\61\u0296\13\61\3\62\7\62\u0299\n\62\f\62\16\62\u029c\13\62\3\62"+
		"\3\62\5\62\u02a0\n\62\3\62\5\62\u02a3\n\62\3\63\3\63\7\63\u02a7\n\63\f"+
		"\63\16\63\u02aa\13\63\3\64\3\64\3\64\5\64\u02af\n\64\3\64\3\64\5\64\u02b3"+
		"\n\64\3\64\3\64\3\65\3\65\3\65\7\65\u02ba\n\65\f\65\16\65\u02bd\13\65"+
		"\3\66\3\66\7\66\u02c1\n\66\f\66\16\66\u02c4\13\66\3\66\3\66\3\67\3\67"+
		"\7\67\u02ca\n\67\f\67\16\67\u02cd\13\67\3\67\3\67\38\38\58\u02d3\n8\3"+
		"8\38\78\u02d7\n8\f8\168\u02da\138\38\58\u02dd\n8\39\39\39\39\39\39\39"+
		"\39\39\59\u02e8\n9\3:\3:\5:\u02ec\n:\3:\3:\3:\3:\7:\u02f2\n:\f:\16:\u02f5"+
		"\13:\3:\3:\5:\u02f9\n:\3:\3:\5:\u02fd\n:\3;\3;\3;\3<\3<\3<\3<\5<\u0306"+
		"\n<\3<\3<\3=\3=\3=\3>\3>\3>\3>\3?\7?\u0312\n?\f?\16?\u0315\13?\3?\3?\5"+
		"?\u0319\n?\3@\3@\3@\3@\3@\3@\3@\5@\u0322\n@\3A\3A\3A\3A\7A\u0328\nA\f"+
		"A\16A\u032b\13A\3A\3A\3B\3B\3B\7B\u0332\nB\fB\16B\u0335\13B\3B\3B\3B\3"+
		"C\3C\5C\u033c\nC\3C\3C\3C\3C\7C\u0342\nC\fC\16C\u0345\13C\3C\3C\5C\u0349"+
		"\nC\3C\3C\3D\3D\3D\3E\3E\3E\7E\u0353\nE\fE\16E\u0356\13E\3F\3F\3F\5F\u035b"+
		"\nF\3G\3G\3G\7G\u0360\nG\fG\16G\u0363\13G\3H\3H\5H\u0367\nH\3I\3I\3I\3"+
		"I\7I\u036d\nI\fI\16I\u0370\13I\3I\5I\u0373\nI\5I\u0375\nI\3I\3I\3J\3J"+
		"\3K\3K\3K\7K\u037e\nK\fK\16K\u0381\13K\3K\3K\3K\7K\u0386\nK\fK\16K\u0389"+
		"\13K\5K\u038b\nK\3L\3L\5L\u038f\nL\3L\3L\3L\5L\u0394\nL\7L\u0396\nL\f"+
		"L\16L\u0399\13L\3M\3M\3N\3N\3N\3N\7N\u03a1\nN\fN\16N\u03a4\13N\3N\3N\3"+
		"O\3O\3O\3O\5O\u03ac\nO\5O\u03ae\nO\3P\3P\3P\7P\u03b3\nP\fP\16P\u03b6\13"+
		"P\3Q\3Q\5Q\u03ba\nQ\3Q\3Q\3R\3R\3R\7R\u03c1\nR\fR\16R\u03c4\13R\3R\3R"+
		"\5R\u03c8\nR\3R\5R\u03cb\nR\3S\7S\u03ce\nS\fS\16S\u03d1\13S\3S\3S\3S\3"+
		"T\7T\u03d7\nT\fT\16T\u03da\13T\3T\3T\3T\3T\3U\3U\3V\3V\3W\3W\3W\7W\u03e7"+
		"\nW\fW\16W\u03ea\13W\3X\3X\3X\3X\3X\5X\u03f1\nX\3X\5X\u03f4\nX\3Y\3Y\3"+
		"Z\3Z\3Z\7Z\u03fb\nZ\fZ\16Z\u03fe\13Z\3[\3[\3[\3[\3\\\3\\\3\\\5\\\u0407"+
		"\n\\\3]\3]\3]\3]\7]\u040d\n]\f]\16]\u0410\13]\5]\u0412\n]\3]\5]\u0415"+
		"\n]\3]\3]\3^\3^\3^\3^\3^\3_\3_\7_\u0420\n_\f_\16_\u0423\13_\3_\3_\3`\7"+
		"`\u0428\n`\f`\16`\u042b\13`\3`\3`\5`\u042f\n`\3a\3a\3a\3a\3a\3a\5a\u0437"+
		"\na\3a\3a\5a\u043b\na\3a\3a\5a\u043f\na\3a\3a\5a\u0443\na\5a\u0445\na"+
		"\3b\3b\5b\u0449\nb\3c\3c\3c\3c\5c\u044f\nc\3d\3d\3e\3e\3e\3f\3f\7f\u0458"+
		"\nf\ff\16f\u045b\13f\3f\3f\3g\3g\3g\5g\u0462\ng\3h\3h\3h\3i\7i\u0468\n"+
		"i\fi\16i\u046b\13i\3i\3i\3i\3j\3j\3j\3j\3j\5j\u0475\nj\3j\3j\3j\3j\3j"+
		"\3j\3j\5j\u047e\nj\3j\3j\3j\3j\3j\3j\3j\3j\3j\3j\3j\3j\3j\3j\3j\3j\3j"+
		"\3j\3j\6j\u0493\nj\rj\16j\u0494\3j\5j\u0498\nj\3j\5j\u049b\nj\3j\3j\3"+
		"j\3j\7j\u04a1\nj\fj\16j\u04a4\13j\3j\5j\u04a7\nj\3j\3j\3j\3j\7j\u04ad"+
		"\nj\fj\16j\u04b0\13j\3j\7j\u04b3\nj\fj\16j\u04b6\13j\3j\3j\3j\3j\3j\3"+
		"j\3j\3j\5j\u04c0\nj\3j\3j\3j\3j\3j\3j\3j\5j\u04c9\nj\3j\3j\3j\5j\u04ce"+
		"\nj\3j\3j\3j\3j\3j\3j\3j\3j\5j\u04d8\nj\3k\3k\3k\7k\u04dd\nk\fk\16k\u04e0"+
		"\13k\3k\3k\3k\3k\3k\3l\3l\3l\7l\u04ea\nl\fl\16l\u04ed\13l\3m\3m\3m\3n"+
		"\3n\3n\5n\u04f5\nn\3n\3n\3o\3o\3o\7o\u04fc\no\fo\16o\u04ff\13o\3p\7p\u0502"+
		"\np\fp\16p\u0505\13p\3p\3p\3p\3p\3p\3q\6q\u050d\nq\rq\16q\u050e\3q\6q"+
		"\u0512\nq\rq\16q\u0513\3r\3r\3r\3r\3r\3r\3r\3r\3r\3r\5r\u0520\nr\3s\3"+
		"s\5s\u0524\ns\3s\3s\5s\u0528\ns\3s\3s\5s\u052c\ns\5s\u052e\ns\3t\3t\5"+
		"t\u0532\nt\3u\7u\u0535\nu\fu\16u\u0538\13u\3u\3u\3u\3u\3u\3v\3v\3w\3w"+
		"\3w\3w\3x\3x\3x\7x\u0548\nx\fx\16x\u054b\13x\3y\3y\3z\3z\3{\3{\3{\3{\3"+
		"{\3{\3{\3{\3{\3{\3{\3{\3{\5{\u055e\n{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3"+
		"{\3{\3{\3{\5{\u056e\n{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3"+
		"{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3"+
		"{\3{\3{\5{\u0599\n{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\5"+
		"{\u05ab\n{\3{\3{\3{\3{\3{\3{\7{\u05b3\n{\f{\16{\u05b6\13{\3|\3|\3|\3|"+
		"\3|\3|\3|\3|\3|\3|\3|\3|\3|\3|\3|\3|\3|\3|\3|\5|\u05cb\n|\5|\u05cd\n|"+
		"\3}\3}\3}\3}\3}\3}\3}\5}\u05d6\n}\5}\u05d8\n}\3~\3~\5~\u05dc\n~\3~\3~"+
		"\3~\5~\u05e1\n~\7~\u05e3\n~\f~\16~\u05e6\13~\3~\5~\u05e9\n~\3\177\3\177"+
		"\5\177\u05ed\n\177\3\177\3\177\3\u0080\3\u0080\3\u0080\3\u0080\7\u0080"+
		"\u05f5\n\u0080\f\u0080\16\u0080\u05f8\13\u0080\3\u0080\3\u0080\3\u0080"+
		"\3\u0080\3\u0080\3\u0080\3\u0080\7\u0080\u0601\n\u0080\f\u0080\16\u0080"+
		"\u0604\13\u0080\3\u0080\3\u0080\7\u0080\u0608\n\u0080\f\u0080\16\u0080"+
		"\u060b\13\u0080\5\u0080\u060d\n\u0080\3\u0081\3\u0081\5\u0081\u0611\n"+
		"\u0081\3\u0082\3\u0082\3\u0082\3\u0083\3\u0083\3\u0083\3\u0083\3\u0084"+
		"\3\u0084\3\u0084\5\u0084\u061d\n\u0084\3\u0085\3\u0085\3\u0085\5\u0085"+
		"\u0622\n\u0085\3\u0086\3\u0086\3\u0086\3\u0086\5\u0086\u0628\n\u0086\5"+
		"\u0086\u062a\n\u0086\3\u0087\3\u0087\3\u0087\3\u0087\5\u0087\u0630\n\u0087"+
		"\3\u0088\3\u0088\5\u0088\u0634\n\u0088\3\u0088\3\u0088\3\u0088\2\3\u00f4"+
		"\u0089\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<"+
		">@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a"+
		"\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2"+
		"\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba"+
		"\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2"+
		"\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea"+
		"\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102"+
		"\u0104\u0106\u0108\u010a\u010c\u010e\2\20\3\2\32\33\5\2\34\34\36\36RU"+
		"\6\2==IIMMPP\6\2  \61\61@BEF\n\2\"\"$$\'\'--\63\63::<<DD\4\2\60\60GG\3"+
		"\2lo\3\2bc\4\2pquu\3\2no\4\2`agh\4\2ffii\4\2__v\u0080\3\2lm\2\u06a3\2"+
		"\u0110\3\2\2\2\4\u0119\3\2\2\2\6\u011e\3\2\2\2\b\u0120\3\2\2\2\n\u012b"+
		"\3\2\2\2\f\u0136\3\2\2\2\16\u014d\3\2\2\2\20\u014f\3\2\2\2\22\u0156\3"+
		"\2\2\2\24\u015d\3\2\2\2\26\u0164\3\2\2\2\30\u016b\3\2\2\2\32\u0172\3\2"+
		"\2\2\34\u017f\3\2\2\2\36\u0188\3\2\2\2 \u0196\3\2\2\2\"\u019d\3\2\2\2"+
		"$\u01a4\3\2\2\2&\u01ab\3\2\2\2(\u01b2\3\2\2\2*\u01b9\3\2\2\2,\u01be\3"+
		"\2\2\2.\u01c4\3\2\2\2\60\u01ca\3\2\2\2\62\u01d1\3\2\2\2\64\u01d8\3\2\2"+
		"\2\66\u01da\3\2\2\28\u01de\3\2\2\2:\u01e0\3\2\2\2<\u01e2\3\2\2\2>\u01ee"+
		"\3\2\2\2@\u01f0\3\2\2\2B\u01fc\3\2\2\2D\u0201\3\2\2\2F\u0203\3\2\2\2H"+
		"\u0206\3\2\2\2J\u0219\3\2\2\2L\u0220\3\2\2\2N\u0248\3\2\2\2P\u024c\3\2"+
		"\2\2R\u0250\3\2\2\2T\u0254\3\2\2\2V\u0256\3\2\2\2X\u0265\3\2\2\2Z\u0270"+
		"\3\2\2\2\\\u0275\3\2\2\2^\u027d\3\2\2\2`\u028f\3\2\2\2b\u029a\3\2\2\2"+
		"d\u02a4\3\2\2\2f\u02ab\3\2\2\2h\u02b6\3\2\2\2j\u02be\3\2\2\2l\u02c7\3"+
		"\2\2\2n\u02dc\3\2\2\2p\u02e7\3\2\2\2r\u02eb\3\2\2\2t\u02fe\3\2\2\2v\u0301"+
		"\3\2\2\2x\u0309\3\2\2\2z\u030c\3\2\2\2|\u0318\3\2\2\2~\u0321\3\2\2\2\u0080"+
		"\u0323\3\2\2\2\u0082\u032e\3\2\2\2\u0084\u033b\3\2\2\2\u0086\u034c\3\2"+
		"\2\2\u0088\u034f\3\2\2\2\u008a\u0357\3\2\2\2\u008c\u035c\3\2\2\2\u008e"+
		"\u0366\3\2\2\2\u0090\u0368\3\2\2\2\u0092\u0378\3\2\2\2\u0094\u038a\3\2"+
		"\2\2\u0096\u038c\3\2\2\2\u0098\u039a\3\2\2\2\u009a\u039c\3\2\2\2\u009c"+
		"\u03ad\3\2\2\2\u009e\u03af\3\2\2\2\u00a0\u03b7\3\2\2\2\u00a2\u03ca\3\2"+
		"\2\2\u00a4\u03cf\3\2\2\2\u00a6\u03d8\3\2\2\2\u00a8\u03df\3\2\2\2\u00aa"+
		"\u03e1\3\2\2\2\u00ac\u03e3\3\2\2\2\u00ae\u03eb\3\2\2\2\u00b0\u03f5\3\2"+
		"\2\2\u00b2\u03f7\3\2\2\2\u00b4\u03ff\3\2\2\2\u00b6\u0406\3\2\2\2\u00b8"+
		"\u0408\3\2\2\2\u00ba\u0418\3\2\2\2\u00bc\u041d\3\2\2\2\u00be\u042e\3\2"+
		"\2\2\u00c0\u0444\3\2\2\2\u00c2\u0448\3\2\2\2\u00c4\u044a\3\2\2\2\u00c6"+
		"\u0450\3\2\2\2\u00c8\u0452\3\2\2\2\u00ca\u0455\3\2\2\2\u00cc\u0461\3\2"+
		"\2\2\u00ce\u0463\3\2\2\2\u00d0\u0469\3\2\2\2\u00d2\u04d7\3\2\2\2\u00d4"+
		"\u04d9\3\2\2\2\u00d6\u04e6\3\2\2\2\u00d8\u04ee\3\2\2\2\u00da\u04f1\3\2"+
		"\2\2\u00dc\u04f8\3\2\2\2\u00de\u0503\3\2\2\2\u00e0\u050c\3\2\2\2\u00e2"+
		"\u051f\3\2\2\2\u00e4\u052d\3\2\2\2\u00e6\u0531\3\2\2\2\u00e8\u0536\3\2"+
		"\2\2\u00ea\u053e\3\2\2\2\u00ec\u0540\3\2\2\2\u00ee\u0544\3\2\2\2\u00f0"+
		"\u054c\3\2\2\2\u00f2\u054e\3\2\2\2\u00f4\u055d\3\2\2\2\u00f6\u05cc\3\2"+
		"\2\2\u00f8\u05d7\3\2\2\2\u00fa\u05e8\3\2\2\2\u00fc\u05ea\3\2\2\2\u00fe"+
		"\u05f0\3\2\2\2\u0100\u060e\3\2\2\2\u0102\u0612\3\2\2\2\u0104\u0615\3\2"+
		"\2\2\u0106\u061c\3\2\2\2\u0108\u0621\3\2\2\2\u010a\u0629\3\2\2\2\u010c"+
		"\u062f\3\2\2\2\u010e\u0631\3\2\2\2\u0110\u0113\5\4\3\2\u0111\u0112\7]"+
		"\2\2\u0112\u0114\5<\37\2\u0113\u0111\3\2\2\2\u0113\u0114\3\2\2\2\u0114"+
		"\u0115\3\2\2\2\u0115\u0116\7\2\2\3\u0116\3\3\2\2\2\u0117\u011a\5\6\4\2"+
		"\u0118\u011a\5\16\b\2\u0119\u0117\3\2\2\2\u0119\u0118\3\2\2\2\u011a\5"+
		"\3\2\2\2\u011b\u011f\5\b\5\2\u011c\u011f\5\n\6\2\u011d\u011f\5\f\7\2\u011e"+
		"\u011b\3\2\2\2\u011e\u011c\3\2\2\2\u011e\u011d\3\2\2\2\u011f\7\3\2\2\2"+
		"\u0120\u0121\7\3\2\2\u0121\u0122\7V\2\2\u0122\u0125\5\4\3\2\u0123\u0124"+
		"\7]\2\2\u0124\u0126\5\4\3\2\u0125\u0123\3\2\2\2\u0126\u0127\3\2\2\2\u0127"+
		"\u0125\3\2\2\2\u0127\u0128\3\2\2\2\u0128\u0129\3\2\2\2\u0129\u012a\7W"+
		"\2\2\u012a\t\3\2\2\2\u012b\u012c\7\4\2\2\u012c\u012d\7V\2\2\u012d\u0130"+
		"\5\4\3\2\u012e\u012f\7]\2\2\u012f\u0131\5\4\3\2\u0130\u012e\3\2\2\2\u0131"+
		"\u0132\3\2\2\2\u0132\u0130\3\2\2\2\u0132\u0133\3\2\2\2\u0133\u0134\3\2"+
		"\2\2\u0134\u0135\7W\2\2\u0135\13\3\2\2\2\u0136\u0137\7\5\2\2\u0137\u0138"+
		"\7V\2\2\u0138\u0139\5\4\3\2\u0139\u013a\7W\2\2\u013a\r\3\2\2\2\u013b\u014e"+
		"\5\20\t\2\u013c\u014e\5\22\n\2\u013d\u014e\5\24\13\2\u013e\u014e\5\26"+
		"\f\2\u013f\u014e\5\30\r\2\u0140\u014e\5\32\16\2\u0141\u014e\5\34\17\2"+
		"\u0142\u014e\5\36\20\2\u0143\u014e\5 \21\2\u0144\u014e\5\"\22\2\u0145"+
		"\u014e\5$\23\2\u0146\u014e\5&\24\2\u0147\u014e\5(\25\2\u0148\u014e\5*"+
		"\26\2\u0149\u014e\5,\27\2\u014a\u014e\5.\30\2\u014b\u014e\5\60\31\2\u014c"+
		"\u014e\5\62\32\2\u014d\u013b\3\2\2\2\u014d\u013c\3\2\2\2\u014d\u013d\3"+
		"\2\2\2\u014d\u013e\3\2\2\2\u014d\u013f\3\2\2\2\u014d\u0140\3\2\2\2\u014d"+
		"\u0141\3\2\2\2\u014d\u0142\3\2\2\2\u014d\u0143\3\2\2\2\u014d\u0144\3\2"+
		"\2\2\u014d\u0145\3\2\2\2\u014d\u0146\3\2\2\2\u014d\u0147\3\2\2\2\u014d"+
		"\u0148\3\2\2\2\u014d\u0149\3\2\2\2\u014d\u014a\3\2\2\2\u014d\u014b\3\2"+
		"\2\2\u014d\u014c\3\2\2\2\u014e\17\3\2\2\2\u014f\u0150\7\6\2\2\u0150\u0151"+
		"\7V\2\2\u0151\u0152\5\66\34\2\u0152\u0153\7]\2\2\u0153\u0154\58\35\2\u0154"+
		"\u0155\7W\2\2\u0155\21\3\2\2\2\u0156\u0157\7\7\2\2\u0157\u0158\7V\2\2"+
		"\u0158\u0159\5\66\34\2\u0159\u015a\7]\2\2\u015a\u015b\58\35\2\u015b\u015c"+
		"\7W\2\2\u015c\23\3\2\2\2\u015d\u015e\7\b\2\2\u015e\u015f\7V\2\2\u015f"+
		"\u0160\5\66\34\2\u0160\u0161\7]\2\2\u0161\u0162\58\35\2\u0162\u0163\7"+
		"W\2\2\u0163\25\3\2\2\2\u0164\u0165\7\t\2\2\u0165\u0166\7V\2\2\u0166\u0167"+
		"\5\66\34\2\u0167\u0168\7]\2\2\u0168\u0169\58\35\2\u0169\u016a\7W\2\2\u016a"+
		"\27\3\2\2\2\u016b\u016c\7\n\2\2\u016c\u016d\7V\2\2\u016d\u016e\5\66\34"+
		"\2\u016e\u016f\7]\2\2\u016f\u0170\58\35\2\u0170\u0171\7W\2\2\u0171\31"+
		"\3\2\2\2\u0172\u0173\7\13\2\2\u0173\u0174\7V\2\2\u0174\u0175\5\66\34\2"+
		"\u0175\u0176\7]\2\2\u0176\u0177\58\35\2\u0177\u0178\7]\2\2\u0178\u0179"+
		"\7R\2\2\u0179\u017a\7]\2\2\u017a\u017b\58\35\2\u017b\u017c\7]\2\2\u017c"+
		"\u017d\7R\2\2\u017d\u017e\7W\2\2\u017e\33\3\2\2\2\u017f\u0180\7\13\2\2"+
		"\u0180\u0181\7V\2\2\u0181\u0182\5\66\34\2\u0182\u0183\7]\2\2\u0183\u0184"+
		"\58\35\2\u0184\u0185\7]\2\2\u0185\u0186\58\35\2\u0186\u0187\7W\2\2\u0187"+
		"\35\3\2\2\2\u0188\u0189\7\f\2\2\u0189\u018a\7V\2\2\u018a\u018b\5\66\34"+
		"\2\u018b\u018c\7]\2\2\u018c\u0191\58\35\2\u018d\u018e\7]\2\2\u018e\u0190"+
		"\58\35\2\u018f\u018d\3\2\2\2\u0190\u0193\3\2\2\2\u0191\u018f\3\2\2\2\u0191"+
		"\u0192\3\2\2\2\u0192\u0194\3\2\2\2\u0193\u0191\3\2\2\2\u0194\u0195\7W"+
		"\2\2\u0195\37\3\2\2\2\u0196\u0197\7\r\2\2\u0197\u0198\7V\2\2\u0198\u0199"+
		"\5\66\34\2\u0199\u019a\7]\2\2\u019a\u019b\5:\36\2\u019b\u019c\7W\2\2\u019c"+
		"!\3\2\2\2\u019d\u019e\7\16\2\2\u019e\u019f\7V\2\2\u019f\u01a0\5\66\34"+
		"\2\u01a0\u01a1\7]\2\2\u01a1\u01a2\5:\36\2\u01a2\u01a3\7W\2\2\u01a3#\3"+
		"\2\2\2\u01a4\u01a5\7\17\2\2\u01a5\u01a6\7V\2\2\u01a6\u01a7\5\66\34\2\u01a7"+
		"\u01a8\7]\2\2\u01a8\u01a9\5:\36\2\u01a9\u01aa\7W\2\2\u01aa%\3\2\2\2\u01ab"+
		"\u01ac\7\20\2\2\u01ac\u01ad\7V\2\2\u01ad\u01ae\5\66\34\2\u01ae\u01af\7"+
		"]\2\2\u01af\u01b0\5:\36\2\u01b0\u01b1\7W\2\2\u01b1\'\3\2\2\2\u01b2\u01b3"+
		"\7\21\2\2\u01b3\u01b4\7V\2\2\u01b4\u01b5\5\66\34\2\u01b5\u01b6\7]\2\2"+
		"\u01b6\u01b7\5:\36\2\u01b7\u01b8\7W\2\2\u01b8)\3\2\2\2\u01b9\u01ba\7\22"+
		"\2\2\u01ba\u01bb\7V\2\2\u01bb\u01bc\5\66\34\2\u01bc\u01bd\7W\2\2\u01bd"+
		"+\3\2\2\2\u01be\u01bf\7\23\2\2\u01bf\u01c0\7V\2\2\u01c0\u01c1\5\64\33"+
		"\2\u01c1\u01c2\7\24\2\2\u01c2\u01c3\7W\2\2\u01c3-\3\2\2\2\u01c4\u01c5"+
		"\7\25\2\2\u01c5\u01c6\7V\2\2\u01c6\u01c7\5\64\33\2\u01c7\u01c8\7\24\2"+
		"\2\u01c8\u01c9\7W\2\2\u01c9/\3\2\2\2\u01ca\u01cb\7\26\2\2\u01cb\u01cc"+
		"\7V\2\2\u01cc\u01cd\5\66\34\2\u01cd\u01ce\7]\2\2\u01ce\u01cf\5:\36\2\u01cf"+
		"\u01d0\7W\2\2\u01d0\61\3\2\2\2\u01d1\u01d2\7\27\2\2\u01d2\u01d3\7V\2\2"+
		"\u01d3\u01d4\5\66\34\2\u01d4\u01d5\7]\2\2\u01d5\u01d6\5:\36\2\u01d6\u01d7"+
		"\7W\2\2\u01d7\63\3\2\2\2\u01d8\u01d9\7\u0081\2\2\u01d9\65\3\2\2\2\u01da"+
		"\u01db\7T\2\2\u01db\67\3\2\2\2\u01dc\u01df\5F$\2\u01dd\u01df\7\u0081\2"+
		"\2\u01de\u01dc\3\2\2\2\u01de\u01dd\3\2\2\2\u01df9\3\2\2\2\u01e0\u01e1"+
		"\7T\2\2\u01e1;\3\2\2\2\u01e2\u01e3\7\30\2\2\u01e3\u01e4\7V\2\2\u01e4\u01e9"+
		"\5> \2\u01e5\u01e6\7]\2\2\u01e6\u01e8\5> \2\u01e7\u01e5\3\2\2\2\u01e8"+
		"\u01eb\3\2\2\2\u01e9\u01e7\3\2\2\2\u01e9\u01ea\3\2\2\2\u01ea\u01ec\3\2"+
		"\2\2\u01eb\u01e9\3\2\2\2\u01ec\u01ed\7W\2\2\u01ed=\3\2\2\2\u01ee\u01ef"+
		"\5@!\2\u01ef?\3\2\2\2\u01f0\u01f1\7\31\2\2\u01f1\u01f2\7V\2\2\u01f2\u01f7"+
		"\5B\"\2\u01f3\u01f4\7]\2\2\u01f4\u01f6\5B\"\2\u01f5\u01f3\3\2\2\2\u01f6"+
		"\u01f9\3\2\2\2\u01f7\u01f5\3\2\2\2\u01f7\u01f8\3\2\2\2\u01f8\u01fa\3\2"+
		"\2\2\u01f9\u01f7\3\2\2\2\u01fa\u01fb\7W\2\2\u01fbA\3\2\2\2\u01fc\u01fd"+
		"\5D#\2\u01fd\u01fe\7V\2\2\u01fe\u01ff\5\66\34\2\u01ff\u0200\7W\2\2\u0200"+
		"C\3\2\2\2\u0201\u0202\t\2\2\2\u0202E\3\2\2\2\u0203\u0204\t\3\2\2\u0204"+
		"G\3\2\2\2\u0205\u0207\5J&\2\u0206\u0205\3\2\2\2\u0206\u0207\3\2\2\2\u0207"+
		"\u020b\3\2\2\2\u0208\u020a\5L\'\2\u0209\u0208\3\2\2\2\u020a\u020d\3\2"+
		"\2\2\u020b\u0209\3\2\2\2\u020b\u020c\3\2\2\2\u020c\u0211\3\2\2\2\u020d"+
		"\u020b\3\2\2\2\u020e\u0210\5N(\2\u020f\u020e\3\2\2\2\u0210\u0213\3\2\2"+
		"\2\u0211\u020f\3\2\2\2\u0211\u0212\3\2\2\2\u0212\u0214\3\2\2\2\u0213\u0211"+
		"\3\2\2\2\u0214\u0215\7\2\2\3\u0215I\3\2\2\2\u0216\u0218\5\u00aeX\2\u0217"+
		"\u0216\3\2\2\2\u0218\u021b\3\2\2\2\u0219\u0217\3\2\2\2\u0219\u021a\3\2"+
		"\2\2\u021a\u021c\3\2\2\2\u021b\u0219\3\2\2\2\u021c\u021d\7?\2\2\u021d"+
		"\u021e\5\u00acW\2\u021e\u021f\7\\\2\2\u021fK\3\2\2\2\u0220\u0222\78\2"+
		"\2\u0221\u0223\7E\2\2\u0222\u0221\3\2\2\2\u0222\u0223\3\2\2\2\u0223\u0224"+
		"\3\2\2\2\u0224\u0227\5\u00acW\2\u0225\u0226\7^\2\2\u0226\u0228\7p\2\2"+
		"\u0227\u0225\3\2\2\2\u0227\u0228\3\2\2\2\u0228\u0229\3\2\2\2\u0229\u022a"+
		"\7\\\2\2\u022aM\3\2\2\2\u022b\u022d\5R*\2\u022c\u022b\3\2\2\2\u022d\u0230"+
		"\3\2\2\2\u022e\u022c\3\2\2\2\u022e\u022f\3\2\2\2\u022f\u0231\3\2\2\2\u0230"+
		"\u022e\3\2\2\2\u0231\u0249\5V,\2\u0232\u0234\5R*\2\u0233\u0232\3\2\2\2"+
		"\u0234\u0237\3\2\2\2\u0235\u0233\3\2\2\2\u0235\u0236\3\2\2\2\u0236\u0238"+
		"\3\2\2\2\u0237\u0235\3\2\2\2\u0238\u0249\5^\60\2\u0239\u023b\5R*\2\u023a"+
		"\u0239\3\2\2\2\u023b\u023e\3\2\2\2\u023c\u023a\3\2\2\2\u023c\u023d\3\2"+
		"\2\2\u023d\u023f\3\2\2\2\u023e\u023c\3\2\2\2\u023f\u0249\5f\64\2\u0240"+
		"\u0242\5R*\2\u0241\u0240\3\2\2\2\u0242\u0245\3\2\2\2\u0243\u0241\3\2\2"+
		"\2\u0243\u0244\3\2\2\2\u0244\u0246\3\2\2\2\u0245\u0243\3\2\2\2\u0246\u0249"+
		"\5\u00ba^\2\u0247\u0249\7\\\2\2\u0248\u022e\3\2\2\2\u0248\u0235\3\2\2"+
		"\2\u0248\u023c\3\2\2\2\u0248\u0243\3\2\2\2\u0248\u0247\3\2\2\2\u0249O"+
		"\3\2\2\2\u024a\u024d\5R*\2\u024b\u024d\t\4\2\2\u024c\u024a\3\2\2\2\u024c"+
		"\u024b\3\2\2\2\u024dQ\3\2\2\2\u024e\u0251\5\u00aeX\2\u024f\u0251\t\5\2"+
		"\2\u0250\u024e\3\2\2\2\u0250\u024f\3\2\2\2\u0251S\3\2\2\2\u0252\u0255"+
		"\7\61\2\2\u0253\u0255\5\u00aeX\2\u0254\u0252\3\2\2\2\u0254\u0253\3\2\2"+
		"\2\u0255U\3\2\2\2\u0256\u0257\7(\2\2\u0257\u0259\7\u0081\2\2\u0258\u025a"+
		"\5X-\2\u0259\u0258\3\2\2\2\u0259\u025a\3\2\2\2\u025a\u025d\3\2\2\2\u025b"+
		"\u025c\7\60\2\2\u025c\u025e\5\u0094K\2\u025d\u025b\3\2\2\2\u025d\u025e"+
		"\3\2\2\2\u025e\u0261\3\2\2\2\u025f\u0260\7\67\2\2\u0260\u0262\5h\65\2"+
		"\u0261\u025f\3\2\2\2\u0261\u0262\3\2\2\2\u0262\u0263\3\2\2\2\u0263\u0264"+
		"\5j\66\2\u0264W\3\2\2\2\u0265\u0266\7a\2\2\u0266\u026b\5Z.\2\u0267\u0268"+
		"\7]\2\2\u0268\u026a\5Z.\2\u0269\u0267\3\2\2\2\u026a\u026d\3\2\2\2\u026b"+
		"\u0269\3\2\2\2\u026b\u026c\3\2\2\2\u026c\u026e\3\2\2\2\u026d\u026b\3\2"+
		"\2\2\u026e\u026f\7`\2\2\u026fY\3\2\2\2\u0270\u0273\7\u0081\2\2\u0271\u0272"+
		"\7\60\2\2\u0272\u0274\5\\/\2\u0273\u0271\3\2\2\2\u0273\u0274\3\2\2\2\u0274"+
		"[\3\2\2\2\u0275\u027a\5\u0094K\2\u0276\u0277\7r\2\2\u0277\u0279\5\u0094"+
		"K\2\u0278\u0276\3\2\2\2\u0279\u027c\3\2\2\2\u027a\u0278\3\2\2\2\u027a"+
		"\u027b\3\2\2\2\u027b]\3\2\2\2\u027c\u027a\3\2\2\2\u027d\u027e\7/\2\2\u027e"+
		"\u0281\7\u0081\2\2\u027f\u0280\7\67\2\2\u0280\u0282\5h\65\2\u0281\u027f"+
		"\3\2\2\2\u0281\u0282\3\2\2\2\u0282\u0283\3\2\2\2\u0283\u0285\7X\2\2\u0284"+
		"\u0286\5`\61\2\u0285\u0284\3\2\2\2\u0285\u0286\3\2\2\2\u0286\u0288\3\2"+
		"\2\2\u0287\u0289\7]\2\2\u0288\u0287\3\2\2\2\u0288\u0289\3\2\2\2\u0289"+
		"\u028b\3\2\2\2\u028a\u028c\5d\63\2\u028b\u028a\3\2\2\2\u028b\u028c\3\2"+
		"\2\2\u028c\u028d\3\2\2\2\u028d\u028e\7Y\2\2\u028e_\3\2\2\2\u028f\u0294"+
		"\5b\62\2\u0290\u0291\7]\2\2\u0291\u0293\5b\62\2\u0292\u0290\3\2\2\2\u0293"+
		"\u0296\3\2\2\2\u0294\u0292\3\2\2\2\u0294\u0295\3\2\2\2\u0295a\3\2\2\2"+
		"\u0296\u0294\3\2\2\2\u0297\u0299\5\u00aeX\2\u0298\u0297\3\2\2\2\u0299"+
		"\u029c\3\2\2\2\u029a\u0298\3\2\2\2\u029a\u029b\3\2\2\2\u029b\u029d\3\2"+
		"\2\2\u029c\u029a\3\2\2\2\u029d\u029f\7\u0081\2\2\u029e\u02a0\5\u010e\u0088"+
		"\2\u029f\u029e\3\2\2\2\u029f\u02a0\3\2\2\2\u02a0\u02a2\3\2\2\2\u02a1\u02a3"+
		"\5j\66\2\u02a2\u02a1\3\2\2\2\u02a2\u02a3\3\2\2\2\u02a3c\3\2\2\2\u02a4"+
		"\u02a8\7\\\2\2\u02a5\u02a7\5n8\2\u02a6\u02a5\3\2\2\2\u02a7\u02aa\3\2\2"+
		"\2\u02a8\u02a6\3\2\2\2\u02a8\u02a9\3\2\2\2\u02a9e\3\2\2\2\u02aa\u02a8"+
		"\3\2\2\2\u02ab\u02ac\7;\2\2\u02ac\u02ae\7\u0081\2\2\u02ad\u02af\5X-\2"+
		"\u02ae\u02ad\3\2\2\2\u02ae\u02af\3\2\2\2\u02af\u02b2\3\2\2\2\u02b0\u02b1"+
		"\7\60\2\2\u02b1\u02b3\5h\65\2\u02b2\u02b0\3\2\2\2\u02b2\u02b3\3\2\2\2"+
		"\u02b3\u02b4\3\2\2\2\u02b4\u02b5\5l\67\2\u02b5g\3\2\2\2\u02b6\u02bb\5"+
		"\u0094K\2\u02b7\u02b8\7]\2\2\u02b8\u02ba\5\u0094K\2\u02b9\u02b7\3\2\2"+
		"\2\u02ba\u02bd\3\2\2\2\u02bb\u02b9\3\2\2\2\u02bb\u02bc\3\2\2\2\u02bci"+
		"\3\2\2\2\u02bd\u02bb\3\2\2\2\u02be\u02c2\7X\2\2\u02bf\u02c1\5n8\2\u02c0"+
		"\u02bf\3\2\2\2\u02c1\u02c4\3\2\2\2\u02c2\u02c0\3\2\2\2\u02c2\u02c3\3\2"+
		"\2\2\u02c3\u02c5\3\2\2\2\u02c4\u02c2\3\2\2\2\u02c5\u02c6\7Y\2\2\u02c6"+
		"k\3\2\2\2\u02c7\u02cb\7X\2\2\u02c8\u02ca\5|?\2\u02c9\u02c8\3\2\2\2\u02ca"+
		"\u02cd\3\2\2\2\u02cb\u02c9\3\2\2\2\u02cb\u02cc\3\2\2\2\u02cc\u02ce\3\2"+
		"\2\2\u02cd\u02cb\3\2\2\2\u02ce\u02cf\7Y\2\2\u02cfm\3\2\2\2\u02d0\u02dd"+
		"\7\\\2\2\u02d1\u02d3\7E\2\2\u02d2\u02d1\3\2\2\2\u02d2\u02d3\3\2\2\2\u02d3"+
		"\u02d4\3\2\2\2\u02d4\u02dd\5\u00caf\2\u02d5\u02d7\5P)\2\u02d6\u02d5\3"+
		"\2\2\2\u02d7\u02da\3\2\2\2\u02d8\u02d6\3\2\2\2\u02d8\u02d9\3\2\2\2\u02d9"+
		"\u02db\3\2\2\2\u02da\u02d8\3\2\2\2\u02db\u02dd\5p9\2\u02dc\u02d0\3\2\2"+
		"\2\u02dc\u02d2\3\2\2\2\u02dc\u02d8\3\2\2\2\u02ddo\3\2\2\2\u02de\u02e8"+
		"\5r:\2\u02df\u02e8\5t;\2\u02e0\u02e8\5z>\2\u02e1\u02e8\5v<\2\u02e2\u02e8"+
		"\5x=\2\u02e3\u02e8\5f\64\2\u02e4\u02e8\5\u00ba^\2\u02e5\u02e8\5V,\2\u02e6"+
		"\u02e8\5^\60\2\u02e7\u02de\3\2\2\2\u02e7\u02df\3\2\2\2\u02e7\u02e0\3\2"+
		"\2\2\u02e7\u02e1\3\2\2\2\u02e7\u02e2\3\2\2\2\u02e7\u02e3\3\2\2\2\u02e7"+
		"\u02e4\3\2\2\2\u02e7\u02e5\3\2\2\2\u02e7\u02e6\3\2\2\2\u02e8q\3\2\2\2"+
		"\u02e9\u02ec\5\u0094K\2\u02ea\u02ec\7O\2\2\u02eb\u02e9\3\2\2\2\u02eb\u02ea"+
		"\3\2\2\2\u02ec\u02ed\3\2\2\2\u02ed\u02ee\7\u0081\2\2\u02ee\u02f3\5\u00a0"+
		"Q\2\u02ef\u02f0\7Z\2\2\u02f0\u02f2\7[\2\2\u02f1\u02ef\3\2\2\2\u02f2\u02f5"+
		"\3\2\2\2\u02f3\u02f1\3\2\2\2\u02f3\u02f4\3\2\2\2\u02f4\u02f8\3\2\2\2\u02f5"+
		"\u02f3\3\2\2\2\u02f6\u02f7\7L\2\2\u02f7\u02f9\5\u009eP\2\u02f8\u02f6\3"+
		"\2\2\2\u02f8\u02f9\3\2\2\2\u02f9\u02fc\3\2\2\2\u02fa\u02fd\5\u00a8U\2"+
		"\u02fb\u02fd\7\\\2\2\u02fc\u02fa\3\2\2\2\u02fc\u02fb\3\2\2\2\u02fds\3"+
		"\2\2\2\u02fe\u02ff\5X-\2\u02ff\u0300\5r:\2\u0300u\3\2\2\2\u0301\u0302"+
		"\7\u0081\2\2\u0302\u0305\5\u00a0Q\2\u0303\u0304\7L\2\2\u0304\u0306\5\u009e"+
		"P\2\u0305\u0303\3\2\2\2\u0305\u0306\3\2\2\2\u0306\u0307\3\2\2\2\u0307"+
		"\u0308\5\u00aaV\2\u0308w\3\2\2\2\u0309\u030a\5X-\2\u030a\u030b\5v<\2\u030b"+
		"y\3\2\2\2\u030c\u030d\5\u0094K\2\u030d\u030e\5\u0088E\2\u030e\u030f\7"+
		"\\\2\2\u030f{\3\2\2\2\u0310\u0312\5P)\2\u0311\u0310\3\2\2\2\u0312\u0315"+
		"\3\2\2\2\u0313\u0311\3\2\2\2\u0313\u0314\3\2\2\2\u0314\u0316\3\2\2\2\u0315"+
		"\u0313\3\2\2\2\u0316\u0319\5~@\2\u0317\u0319\7\\\2\2\u0318\u0313\3\2\2"+
		"\2\u0318\u0317\3\2\2\2\u0319}\3\2\2\2\u031a\u0322\5\u0080A\2\u031b\u0322"+
		"\5\u0084C\2\u031c\u0322\5\u0086D\2\u031d\u0322\5f\64\2\u031e\u0322\5\u00ba"+
		"^\2\u031f\u0322\5V,\2\u0320\u0322\5^\60\2\u0321\u031a\3\2\2\2\u0321\u031b"+
		"\3\2\2\2\u0321\u031c\3\2\2\2\u0321\u031d\3\2\2\2\u0321\u031e\3\2\2\2\u0321"+
		"\u031f\3\2\2\2\u0321\u0320\3\2\2\2\u0322\177\3\2\2\2\u0323\u0324\5\u0094"+
		"K\2\u0324\u0329\5\u0082B\2\u0325\u0326\7]\2\2\u0326\u0328\5\u0082B\2\u0327"+
		"\u0325\3\2\2\2\u0328\u032b\3\2\2\2\u0329\u0327\3\2\2\2\u0329\u032a\3\2"+
		"\2\2\u032a\u032c\3\2\2\2\u032b\u0329\3\2\2\2\u032c\u032d\7\\\2\2\u032d"+
		"\u0081\3\2\2\2\u032e\u0333\7\u0081\2\2\u032f\u0330\7Z\2\2\u0330\u0332"+
		"\7[\2\2\u0331\u032f\3\2\2\2\u0332\u0335\3\2\2\2\u0333\u0331\3\2\2\2\u0333"+
		"\u0334\3\2\2\2\u0334\u0336\3\2\2\2\u0335\u0333\3\2\2\2\u0336\u0337\7_"+
		"\2\2\u0337\u0338\5\u008eH\2\u0338\u0083\3\2\2\2\u0339\u033c\5\u0094K\2"+
		"\u033a\u033c\7O\2\2\u033b\u0339\3\2\2\2\u033b\u033a\3\2\2\2\u033c\u033d"+
		"\3\2\2\2\u033d\u033e\7\u0081\2\2\u033e\u0343\5\u00a0Q\2\u033f\u0340\7"+
		"Z\2\2\u0340\u0342\7[\2\2\u0341\u033f\3\2\2\2\u0342\u0345\3\2\2\2\u0343"+
		"\u0341\3\2\2\2\u0343\u0344\3\2\2\2\u0344\u0348\3\2\2\2\u0345\u0343\3\2"+
		"\2\2\u0346\u0347\7L\2\2\u0347\u0349\5\u009eP\2\u0348\u0346\3\2\2\2\u0348"+
		"\u0349\3\2\2\2\u0349\u034a\3\2\2\2\u034a\u034b\7\\\2\2\u034b\u0085\3\2"+
		"\2\2\u034c\u034d\5X-\2\u034d\u034e\5\u0084C\2\u034e\u0087\3\2\2\2\u034f"+
		"\u0354\5\u008aF\2\u0350\u0351\7]\2\2\u0351\u0353\5\u008aF\2\u0352\u0350"+
		"\3\2\2\2\u0353\u0356\3\2\2\2\u0354\u0352\3\2\2\2\u0354\u0355\3\2\2\2\u0355"+
		"\u0089\3\2\2\2\u0356\u0354\3\2\2\2\u0357\u035a\5\u008cG\2\u0358\u0359"+
		"\7_\2\2\u0359\u035b\5\u008eH\2\u035a\u0358\3\2\2\2\u035a\u035b\3\2\2\2"+
		"\u035b\u008b\3\2\2\2\u035c\u0361\7\u0081\2\2\u035d\u035e\7Z\2\2\u035e"+
		"\u0360\7[\2\2\u035f\u035d\3\2\2\2\u0360\u0363\3\2\2\2\u0361\u035f\3\2"+
		"\2\2\u0361\u0362\3\2\2\2\u0362\u008d\3\2\2\2\u0363\u0361\3\2\2\2\u0364"+
		"\u0367\5\u0090I\2\u0365\u0367\5\u00f4{\2\u0366\u0364\3\2\2\2\u0366\u0365"+
		"\3\2\2\2\u0367\u008f\3\2\2\2\u0368\u0374\7X\2\2\u0369\u036e\5\u008eH\2"+
		"\u036a\u036b\7]\2\2\u036b\u036d\5\u008eH\2\u036c\u036a\3\2\2\2\u036d\u0370"+
		"\3\2\2\2\u036e\u036c\3\2\2\2\u036e\u036f\3\2\2\2\u036f\u0372\3\2\2\2\u0370"+
		"\u036e\3\2\2\2\u0371\u0373\7]\2\2\u0372\u0371\3\2\2\2\u0372\u0373\3\2"+
		"\2\2\u0373\u0375\3\2\2\2\u0374\u0369\3\2\2\2\u0374\u0375\3\2\2\2\u0375"+
		"\u0376\3\2\2\2\u0376\u0377\7Y\2\2\u0377\u0091\3\2\2\2\u0378\u0379\7\u0081"+
		"\2\2\u0379\u0093\3\2\2\2\u037a\u037f\5\u0096L\2\u037b\u037c\7Z\2\2\u037c"+
		"\u037e\7[\2\2\u037d\u037b\3\2\2\2\u037e\u0381\3\2\2\2\u037f\u037d\3\2"+
		"\2\2\u037f\u0380\3\2\2\2\u0380\u038b\3\2\2\2\u0381\u037f\3\2\2\2\u0382"+
		"\u0387\5\u0098M\2\u0383\u0384\7Z\2\2\u0384\u0386\7[\2\2\u0385\u0383\3"+
		"\2\2\2\u0386\u0389\3\2\2\2\u0387\u0385\3\2\2\2\u0387\u0388\3\2\2\2\u0388"+
		"\u038b\3\2\2\2\u0389\u0387\3\2\2\2\u038a\u037a\3\2\2\2\u038a\u0382\3\2"+
		"\2\2\u038b\u0095\3\2\2\2\u038c\u038e\7\u0081\2\2\u038d\u038f\5\u009aN"+
		"\2\u038e\u038d\3\2\2\2\u038e\u038f\3\2\2\2\u038f\u0397\3\2\2\2\u0390\u0391"+
		"\7^\2\2\u0391\u0393\7\u0081\2\2\u0392\u0394\5\u009aN\2\u0393\u0392\3\2"+
		"\2\2\u0393\u0394\3\2\2\2\u0394\u0396\3\2\2\2\u0395\u0390\3\2\2\2\u0396"+
		"\u0399\3\2\2\2\u0397\u0395\3\2\2\2\u0397\u0398\3\2\2\2\u0398\u0097\3\2"+
		"\2\2\u0399\u0397\3\2\2\2\u039a\u039b\t\6\2\2\u039b\u0099\3\2\2\2\u039c"+
		"\u039d\7a\2\2\u039d\u03a2\5\u009cO\2\u039e\u039f\7]\2\2\u039f\u03a1\5"+
		"\u009cO\2\u03a0\u039e\3\2\2\2\u03a1\u03a4\3\2\2\2\u03a2\u03a0\3\2\2\2"+
		"\u03a2\u03a3\3\2\2\2\u03a3\u03a5\3\2\2\2\u03a4\u03a2\3\2\2\2\u03a5\u03a6"+
		"\7`\2\2\u03a6\u009b\3\2\2\2\u03a7\u03ae\5\u0094K\2\u03a8\u03ab\7d\2\2"+
		"\u03a9\u03aa\t\7\2\2\u03aa\u03ac\5\u0094K\2\u03ab\u03a9\3\2\2\2\u03ab"+
		"\u03ac\3\2\2\2\u03ac\u03ae\3\2\2\2\u03ad\u03a7\3\2\2\2\u03ad\u03a8\3\2"+
		"\2\2\u03ae\u009d\3\2\2\2\u03af\u03b4\5\u00acW\2\u03b0\u03b1\7]\2\2\u03b1"+
		"\u03b3\5\u00acW\2\u03b2\u03b0\3\2\2\2\u03b3\u03b6\3\2\2\2\u03b4\u03b2"+
		"\3\2\2\2\u03b4\u03b5\3\2\2\2\u03b5\u009f\3\2\2\2\u03b6\u03b4\3\2\2\2\u03b7"+
		"\u03b9\7V\2\2\u03b8\u03ba\5\u00a2R\2\u03b9\u03b8\3\2\2\2\u03b9\u03ba\3"+
		"\2\2\2\u03ba\u03bb\3\2\2\2\u03bb\u03bc\7W\2\2\u03bc\u00a1\3\2\2\2\u03bd"+
		"\u03c2\5\u00a4S\2\u03be\u03bf\7]\2\2\u03bf\u03c1\5\u00a4S\2\u03c0\u03be"+
		"\3\2\2\2\u03c1\u03c4\3\2\2\2\u03c2\u03c0\3\2\2\2\u03c2\u03c3\3\2\2\2\u03c3"+
		"\u03c7\3\2\2\2\u03c4\u03c2\3\2\2\2\u03c5\u03c6\7]\2\2\u03c6\u03c8\5\u00a6"+
		"T\2\u03c7\u03c5\3\2\2\2\u03c7\u03c8\3\2\2\2\u03c8\u03cb\3\2\2\2\u03c9"+
		"\u03cb\5\u00a6T\2\u03ca\u03bd\3\2\2\2\u03ca\u03c9\3\2\2\2\u03cb\u00a3"+
		"\3\2\2\2\u03cc\u03ce\5T+\2\u03cd\u03cc\3\2\2\2\u03ce\u03d1\3\2\2\2\u03cf"+
		"\u03cd\3\2\2\2\u03cf\u03d0\3\2\2\2\u03d0\u03d2\3\2\2\2\u03d1\u03cf\3\2"+
		"\2\2\u03d2\u03d3\5\u0094K\2\u03d3\u03d4\5\u008cG\2\u03d4\u00a5\3\2\2\2"+
		"\u03d5\u03d7\5T+\2\u03d6\u03d5\3\2\2\2\u03d7\u03da\3\2\2\2\u03d8\u03d6"+
		"\3\2\2\2\u03d8\u03d9\3\2\2\2\u03d9\u03db\3\2\2\2\u03da\u03d8\3\2\2\2\u03db"+
		"\u03dc\5\u0094K\2\u03dc\u03dd\7\u0083\2\2\u03dd\u03de\5\u008cG\2\u03de"+
		"\u00a7\3\2\2\2\u03df\u03e0\5\u00caf\2\u03e0\u00a9\3\2\2\2\u03e1\u03e2"+
		"\5\u00caf\2\u03e2\u00ab\3\2\2\2\u03e3\u03e8\7\u0081\2\2\u03e4\u03e5\7"+
		"^\2\2\u03e5\u03e7\7\u0081\2\2\u03e6\u03e4\3\2\2\2\u03e7\u03ea\3\2\2\2"+
		"\u03e8\u03e6\3\2\2\2\u03e8\u03e9\3\2\2\2\u03e9\u00ad\3\2\2\2\u03ea\u03e8"+
		"\3\2\2\2\u03eb\u03ec\7\u0082\2\2\u03ec\u03f3\5\u00b0Y\2\u03ed\u03f0\7"+
		"V\2\2\u03ee\u03f1\5\u00b2Z\2\u03ef\u03f1\5\u00b6\\\2\u03f0\u03ee\3\2\2"+
		"\2\u03f0\u03ef\3\2\2\2\u03f0\u03f1\3\2\2\2\u03f1\u03f2\3\2\2\2\u03f2\u03f4"+
		"\7W\2\2\u03f3\u03ed\3\2\2\2\u03f3\u03f4\3\2\2\2\u03f4\u00af\3\2\2\2\u03f5"+
		"\u03f6\5\u00acW\2\u03f6\u00b1\3\2\2\2\u03f7\u03fc\5\u00b4[\2\u03f8\u03f9"+
		"\7]\2\2\u03f9\u03fb\5\u00b4[\2\u03fa\u03f8\3\2\2\2\u03fb\u03fe\3\2\2\2"+
		"\u03fc\u03fa\3\2\2\2\u03fc\u03fd\3\2\2\2\u03fd\u00b3\3\2\2\2\u03fe\u03fc"+
		"\3\2\2\2\u03ff\u0400\7\u0081\2\2\u0400\u0401\7_\2\2\u0401\u0402\5\u00b6"+
		"\\\2\u0402\u00b5\3\2\2\2\u0403\u0407\5\u00f4{\2\u0404\u0407\5\u00aeX\2"+
		"\u0405\u0407\5\u00b8]\2\u0406\u0403\3\2\2\2\u0406\u0404\3\2\2\2\u0406"+
		"\u0405\3\2\2\2\u0407\u00b7\3\2\2\2\u0408\u0411\7X\2\2\u0409\u040e\5\u00b6"+
		"\\\2\u040a\u040b\7]\2\2\u040b\u040d\5\u00b6\\\2\u040c\u040a\3\2\2\2\u040d"+
		"\u0410\3\2\2\2\u040e\u040c\3\2\2\2\u040e\u040f\3\2\2\2\u040f\u0412\3\2"+
		"\2\2\u0410\u040e\3\2\2\2\u0411\u0409\3\2\2\2\u0411\u0412\3\2\2\2\u0412"+
		"\u0414\3\2\2\2\u0413\u0415\7]\2\2\u0414\u0413\3\2\2\2\u0414\u0415\3\2"+
		"\2\2\u0415\u0416\3\2\2\2\u0416\u0417\7Y\2\2\u0417\u00b9\3\2\2\2\u0418"+
		"\u0419\7\u0082\2\2\u0419\u041a\7;\2\2\u041a\u041b\7\u0081\2\2\u041b\u041c"+
		"\5\u00bc_\2\u041c\u00bb\3\2\2\2\u041d\u0421\7X\2\2\u041e\u0420\5\u00be"+
		"`\2\u041f\u041e\3\2\2\2\u0420\u0423\3\2\2\2\u0421\u041f\3\2\2\2\u0421"+
		"\u0422\3\2\2\2\u0422\u0424\3\2\2\2\u0423\u0421\3\2\2\2\u0424\u0425\7Y"+
		"\2\2\u0425\u00bd\3\2\2\2\u0426\u0428\5P)\2\u0427\u0426\3\2\2\2\u0428\u042b"+
		"\3\2\2\2\u0429\u0427\3\2\2\2\u0429\u042a\3\2\2\2\u042a\u042c\3\2\2\2\u042b"+
		"\u0429\3\2\2\2\u042c\u042f\5\u00c0a\2\u042d\u042f\7\\\2\2\u042e\u0429"+
		"\3\2\2\2\u042e\u042d\3\2\2\2\u042f\u00bf\3\2\2\2\u0430\u0431\5\u0094K"+
		"\2\u0431\u0432\5\u00c2b\2\u0432\u0433\7\\\2\2\u0433\u0445\3\2\2\2\u0434"+
		"\u0436\5V,\2\u0435\u0437\7\\\2\2\u0436\u0435\3\2\2\2\u0436\u0437\3\2\2"+
		"\2\u0437\u0445\3\2\2\2\u0438\u043a\5f\64\2\u0439\u043b\7\\\2\2\u043a\u0439"+
		"\3\2\2\2\u043a\u043b\3\2\2\2\u043b\u0445\3\2\2\2\u043c\u043e\5^\60\2\u043d"+
		"\u043f\7\\\2\2\u043e\u043d\3\2\2\2\u043e\u043f\3\2\2\2\u043f\u0445\3\2"+
		"\2\2\u0440\u0442\5\u00ba^\2\u0441\u0443\7\\\2\2\u0442\u0441\3\2\2\2\u0442"+
		"\u0443\3\2\2\2\u0443\u0445\3\2\2\2\u0444\u0430\3\2\2\2\u0444\u0434\3\2"+
		"\2\2\u0444\u0438\3\2\2\2\u0444\u043c\3\2\2\2\u0444\u0440\3\2\2\2\u0445"+
		"\u00c1\3\2\2\2\u0446\u0449\5\u00c4c\2\u0447\u0449\5\u00c6d\2\u0448\u0446"+
		"\3\2\2\2\u0448\u0447\3\2\2\2\u0449\u00c3\3\2\2\2\u044a\u044b\7\u0081\2"+
		"\2\u044b\u044c\7V\2\2\u044c\u044e\7W\2\2\u044d\u044f\5\u00c8e\2\u044e"+
		"\u044d\3\2\2\2\u044e\u044f\3\2\2\2\u044f\u00c5\3\2\2\2\u0450\u0451\5\u0088"+
		"E\2\u0451\u00c7\3\2\2\2\u0452\u0453\7+\2\2\u0453\u0454\5\u00b6\\\2\u0454"+
		"\u00c9\3\2\2\2\u0455\u0459\7X\2\2\u0456\u0458\5\u00ccg\2\u0457\u0456\3"+
		"\2\2\2\u0458\u045b\3\2\2\2\u0459\u0457\3\2\2\2\u0459\u045a\3\2\2\2\u045a"+
		"\u045c\3\2\2\2\u045b\u0459\3\2\2\2\u045c\u045d\7Y\2\2\u045d\u00cb\3\2"+
		"\2\2\u045e\u0462\5\u00ceh\2\u045f\u0462\5\u00d2j\2\u0460\u0462\5N(\2\u0461"+
		"\u045e\3\2\2\2\u0461\u045f\3\2\2\2\u0461\u0460\3\2\2\2\u0462\u00cd\3\2"+
		"\2\2\u0463\u0464\5\u00d0i\2\u0464\u0465\7\\\2\2\u0465\u00cf\3\2\2\2\u0466"+
		"\u0468\5T+\2\u0467\u0466\3\2\2\2\u0468\u046b\3\2\2\2\u0469\u0467\3\2\2"+
		"\2\u0469\u046a\3\2\2\2\u046a\u046c\3\2\2\2\u046b\u0469\3\2\2\2\u046c\u046d"+
		"\5\u0094K\2\u046d\u046e\5\u0088E\2\u046e\u00d1\3\2\2\2\u046f\u04d8\5\u00ca"+
		"f\2\u0470\u0471\7!\2\2\u0471\u0474\5\u00f4{\2\u0472\u0473\7e\2\2\u0473"+
		"\u0475\5\u00f4{\2\u0474\u0472\3\2\2\2\u0474\u0475\3\2\2\2\u0475\u0476"+
		"\3\2\2\2\u0476\u0477\7\\\2\2\u0477\u04d8\3\2\2\2\u0478\u0479\7\65\2\2"+
		"\u0479\u047a\5\u00ecw\2\u047a\u047d\5\u00d2j\2\u047b\u047c\7.\2\2\u047c"+
		"\u047e\5\u00d2j\2\u047d\u047b\3\2\2\2\u047d\u047e\3\2\2\2\u047e\u04d8"+
		"\3\2\2\2\u047f\u0480\7\64\2\2\u0480\u0481\7V\2\2\u0481\u0482\5\u00e4s"+
		"\2\u0482\u0483\7W\2\2\u0483\u0484\5\u00d2j\2\u0484\u04d8\3\2\2\2\u0485"+
		"\u0486\7Q\2\2\u0486\u0487\5\u00ecw\2\u0487\u0488\5\u00d2j\2\u0488\u04d8"+
		"\3\2\2\2\u0489\u048a\7,\2\2\u048a\u048b\5\u00d2j\2\u048b\u048c\7Q\2\2"+
		"\u048c\u048d\5\u00ecw\2\u048d\u048e\7\\\2\2\u048e\u04d8\3\2\2\2\u048f"+
		"\u0490\7N\2\2\u0490\u049a\5\u00caf\2\u0491\u0493\5\u00d4k\2\u0492\u0491"+
		"\3\2\2\2\u0493\u0494\3\2\2\2\u0494\u0492\3\2\2\2\u0494\u0495\3\2\2\2\u0495"+
		"\u0497\3\2\2\2\u0496\u0498\5\u00d8m\2\u0497\u0496\3\2\2\2\u0497\u0498"+
		"\3\2\2\2\u0498\u049b\3\2\2\2\u0499\u049b\5\u00d8m\2\u049a\u0492\3\2\2"+
		"\2\u049a\u0499\3\2\2\2\u049b\u04d8\3\2\2\2\u049c\u049d\7N\2\2\u049d\u049e"+
		"\5\u00dan\2\u049e\u04a2\5\u00caf\2\u049f\u04a1\5\u00d4k\2\u04a0\u049f"+
		"\3\2\2\2\u04a1\u04a4\3\2\2\2\u04a2\u04a0\3\2\2\2\u04a2\u04a3\3\2\2\2\u04a3"+
		"\u04a6\3\2\2\2\u04a4\u04a2\3\2\2\2\u04a5\u04a7\5\u00d8m\2\u04a6\u04a5"+
		"\3\2\2\2\u04a6\u04a7\3\2\2\2\u04a7\u04d8\3\2\2\2\u04a8\u04a9\7H\2\2\u04a9"+
		"\u04aa\5\u00ecw\2\u04aa\u04ae\7X\2\2\u04ab\u04ad\5\u00e0q\2\u04ac\u04ab"+
		"\3\2\2\2\u04ad\u04b0\3\2\2\2\u04ae\u04ac\3\2\2\2\u04ae\u04af\3\2\2\2\u04af"+
		"\u04b4\3\2\2\2\u04b0\u04ae\3\2\2\2\u04b1\u04b3\5\u00e2r\2\u04b2\u04b1"+
		"\3\2\2\2\u04b3\u04b6\3\2\2\2\u04b4\u04b2\3\2\2\2\u04b4\u04b5\3\2\2\2\u04b5"+
		"\u04b7\3\2\2\2\u04b6\u04b4\3\2\2\2\u04b7\u04b8\7Y\2\2\u04b8\u04d8\3\2"+
		"\2\2\u04b9\u04ba\7I\2\2\u04ba\u04bb\5\u00ecw\2\u04bb\u04bc\5\u00caf\2"+
		"\u04bc\u04d8\3\2\2\2\u04bd\u04bf\7C\2\2\u04be\u04c0\5\u00f4{\2\u04bf\u04be"+
		"\3\2\2\2\u04bf\u04c0\3\2\2\2\u04c0\u04c1\3\2\2\2\u04c1\u04d8\7\\\2\2\u04c2"+
		"\u04c3\7K\2\2\u04c3\u04c4\5\u00f4{\2\u04c4\u04c5\7\\\2\2\u04c5\u04d8\3"+
		"\2\2\2\u04c6\u04c8\7#\2\2\u04c7\u04c9\7\u0081\2\2\u04c8\u04c7\3\2\2\2"+
		"\u04c8\u04c9\3\2\2\2\u04c9\u04ca\3\2\2\2\u04ca\u04d8\7\\\2\2\u04cb\u04cd"+
		"\7*\2\2\u04cc\u04ce\7\u0081\2\2\u04cd\u04cc\3\2\2\2\u04cd\u04ce\3\2\2"+
		"\2\u04ce\u04cf\3\2\2\2\u04cf\u04d8\7\\\2\2\u04d0\u04d8\7\\\2\2\u04d1\u04d2"+
		"\5\u00f0y\2\u04d2\u04d3\7\\\2\2\u04d3\u04d8\3\2\2\2\u04d4\u04d5\7\u0081"+
		"\2\2\u04d5\u04d6\7e\2\2\u04d6\u04d8\5\u00d2j\2\u04d7\u046f\3\2\2\2\u04d7"+
		"\u0470\3\2\2\2\u04d7\u0478\3\2\2\2\u04d7\u047f\3\2\2\2\u04d7\u0485\3\2"+
		"\2\2\u04d7\u0489\3\2\2\2\u04d7\u048f\3\2\2\2\u04d7\u049c\3\2\2\2\u04d7"+
		"\u04a8\3\2\2\2\u04d7\u04b9\3\2\2\2\u04d7\u04bd\3\2\2\2\u04d7\u04c2\3\2"+
		"\2\2\u04d7\u04c6\3\2\2\2\u04d7\u04cb\3\2\2\2\u04d7\u04d0\3\2\2\2\u04d7"+
		"\u04d1\3\2\2\2\u04d7\u04d4\3\2\2\2\u04d8\u00d3\3\2\2\2\u04d9\u04da\7&"+
		"\2\2\u04da\u04de\7V\2\2\u04db\u04dd\5T+\2\u04dc\u04db\3\2\2\2\u04dd\u04e0"+
		"\3\2\2\2\u04de\u04dc\3\2\2\2\u04de\u04df\3\2\2\2\u04df\u04e1\3\2\2\2\u04e0"+
		"\u04de\3\2\2\2\u04e1\u04e2\5\u00d6l\2\u04e2\u04e3\7\u0081\2\2\u04e3\u04e4"+
		"\7W\2\2\u04e4\u04e5\5\u00caf\2\u04e5\u00d5\3\2\2\2\u04e6\u04eb\5\u00ac"+
		"W\2\u04e7\u04e8\7s\2\2\u04e8\u04ea\5\u00acW\2\u04e9\u04e7\3\2\2\2\u04ea"+
		"\u04ed\3\2\2\2\u04eb\u04e9\3\2\2\2\u04eb\u04ec\3\2\2\2\u04ec\u00d7\3\2"+
		"\2\2\u04ed\u04eb\3\2\2\2\u04ee\u04ef\7\62\2\2\u04ef\u04f0\5\u00caf\2\u04f0"+
		"\u00d9\3\2\2\2\u04f1\u04f2\7V\2\2\u04f2\u04f4\5\u00dco\2\u04f3\u04f5\7"+
		"\\\2\2\u04f4\u04f3\3\2\2\2\u04f4\u04f5\3\2\2\2\u04f5\u04f6\3\2\2\2\u04f6"+
		"\u04f7\7W\2\2\u04f7\u00db\3\2\2\2\u04f8\u04fd\5\u00dep\2\u04f9\u04fa\7"+
		"\\\2\2\u04fa\u04fc\5\u00dep\2\u04fb\u04f9\3\2\2\2\u04fc\u04ff\3\2\2\2"+
		"\u04fd\u04fb\3\2\2\2\u04fd\u04fe\3\2\2\2\u04fe\u00dd\3\2\2\2\u04ff\u04fd"+
		"\3\2\2\2\u0500\u0502\5T+\2\u0501\u0500\3\2\2\2\u0502\u0505\3\2\2\2\u0503"+
		"\u0501\3\2\2\2\u0503\u0504\3\2\2\2\u0504\u0506\3\2\2\2\u0505\u0503\3\2"+
		"\2\2\u0506\u0507\5\u0096L\2\u0507\u0508\5\u008cG\2\u0508\u0509\7_\2\2"+
		"\u0509\u050a\5\u00f4{\2\u050a\u00df\3\2\2\2\u050b\u050d\5\u00e2r\2\u050c"+
		"\u050b\3\2\2\2\u050d\u050e\3\2\2\2\u050e\u050c\3\2\2\2\u050e\u050f\3\2"+
		"\2\2\u050f\u0511\3\2\2\2\u0510\u0512\5\u00ccg\2\u0511\u0510\3\2\2\2\u0512"+
		"\u0513\3\2\2\2\u0513\u0511\3\2\2\2\u0513\u0514\3\2\2\2\u0514\u00e1\3\2"+
		"\2\2\u0515\u0516\7%\2\2\u0516\u0517\5\u00f2z\2\u0517\u0518\7e\2\2\u0518"+
		"\u0520\3\2\2\2\u0519\u051a\7%\2\2\u051a\u051b\5\u0092J\2\u051b\u051c\7"+
		"e\2\2\u051c\u0520\3\2\2\2\u051d\u051e\7+\2\2\u051e\u0520\7e\2\2\u051f"+
		"\u0515\3\2\2\2\u051f\u0519\3\2\2\2\u051f\u051d\3\2\2\2\u0520\u00e3\3\2"+
		"\2\2\u0521\u052e\5\u00e8u\2\u0522\u0524\5\u00e6t\2\u0523\u0522\3\2\2\2"+
		"\u0523\u0524\3\2\2\2\u0524\u0525\3\2\2\2\u0525\u0527\7\\\2\2\u0526\u0528"+
		"\5\u00f4{\2\u0527\u0526\3\2\2\2\u0527\u0528\3\2\2\2\u0528\u0529\3\2\2"+
		"\2\u0529\u052b\7\\\2\2\u052a\u052c\5\u00eav\2\u052b\u052a\3\2\2\2\u052b"+
		"\u052c\3\2\2\2\u052c\u052e\3\2\2\2\u052d\u0521\3\2\2\2\u052d\u0523\3\2"+
		"\2\2\u052e\u00e5\3\2\2\2\u052f\u0532\5\u00d0i\2\u0530\u0532\5\u00eex\2"+
		"\u0531\u052f\3\2\2\2\u0531\u0530\3\2\2\2\u0532\u00e7\3\2\2\2\u0533\u0535"+
		"\5T+\2\u0534\u0533\3\2\2\2\u0535\u0538\3\2\2\2\u0536\u0534\3\2\2\2\u0536"+
		"\u0537\3\2\2\2\u0537\u0539\3\2\2\2\u0538\u0536\3\2\2\2\u0539\u053a\5\u0094"+
		"K\2\u053a\u053b\5\u008cG\2\u053b\u053c\7e\2\2\u053c\u053d\5\u00f4{\2\u053d"+
		"\u00e9\3\2\2\2\u053e\u053f\5\u00eex\2\u053f\u00eb\3\2\2\2\u0540\u0541"+
		"\7V\2\2\u0541\u0542\5\u00f4{\2\u0542\u0543\7W\2\2\u0543\u00ed\3\2\2\2"+
		"\u0544\u0549\5\u00f4{\2\u0545\u0546\7]\2\2\u0546\u0548\5\u00f4{\2\u0547"+
		"\u0545\3\2\2\2\u0548\u054b\3\2\2\2\u0549\u0547\3\2\2\2\u0549\u054a\3\2"+
		"\2\2\u054a\u00ef\3\2\2\2\u054b\u0549\3\2\2\2\u054c\u054d\5\u00f4{\2\u054d"+
		"\u00f1\3\2\2\2\u054e\u054f\5\u00f4{\2\u054f\u00f3\3\2\2\2\u0550\u0551"+
		"\b{\1\2\u0551\u055e\5\u00f6|\2\u0552\u0553\7>\2\2\u0553\u055e\5\u00f8"+
		"}\2\u0554\u0555\7V\2\2\u0555\u0556\5\u0094K\2\u0556\u0557\7W\2\2\u0557"+
		"\u0558\5\u00f4{\23\u0558\u055e\3\2\2\2\u0559\u055a\t\b\2\2\u055a\u055e"+
		"\5\u00f4{\21\u055b\u055c\t\t\2\2\u055c\u055e\5\u00f4{\20\u055d\u0550\3"+
		"\2\2\2\u055d\u0552\3\2\2\2\u055d\u0554\3\2\2\2\u055d\u0559\3\2\2\2\u055d"+
		"\u055b\3\2\2\2\u055e\u05b4\3\2\2\2\u055f\u0560\f\17\2\2\u0560\u0561\t"+
		"\n\2\2\u0561\u05b3\5\u00f4{\20\u0562\u0563\f\16\2\2\u0563\u0564\t\13\2"+
		"\2\u0564\u05b3\5\u00f4{\17\u0565\u056d\f\r\2\2\u0566\u0567\7a\2\2\u0567"+
		"\u056e\7a\2\2\u0568\u0569\7`\2\2\u0569\u056a\7`\2\2\u056a\u056e\7`\2\2"+
		"\u056b\u056c\7`\2\2\u056c\u056e\7`\2\2\u056d\u0566\3\2\2\2\u056d\u0568"+
		"\3\2\2\2\u056d\u056b\3\2\2\2\u056e\u056f\3\2\2\2\u056f\u05b3\5\u00f4{"+
		"\16\u0570\u0571\f\f\2\2\u0571\u0572\t\f\2\2\u0572\u05b3\5\u00f4{\r\u0573"+
		"\u0574\f\n\2\2\u0574\u0575\t\r\2\2\u0575\u05b3\5\u00f4{\13\u0576\u0577"+
		"\f\t\2\2\u0577\u0578\7r\2\2\u0578\u05b3\5\u00f4{\n\u0579\u057a\f\b\2\2"+
		"\u057a\u057b\7t\2\2\u057b\u05b3\5\u00f4{\t\u057c\u057d\f\7\2\2\u057d\u057e"+
		"\7s\2\2\u057e\u05b3\5\u00f4{\b\u057f\u0580\f\6\2\2\u0580\u0581\7j\2\2"+
		"\u0581\u05b3\5\u00f4{\7\u0582\u0583\f\5\2\2\u0583\u0584\7k\2\2\u0584\u05b3"+
		"\5\u00f4{\6\u0585\u0586\f\4\2\2\u0586\u0587\7d\2\2\u0587\u0588\5\u00f4"+
		"{\2\u0588\u0589\7e\2\2\u0589\u058a\5\u00f4{\5\u058a\u05b3\3\2\2\2\u058b"+
		"\u058c\f\3\2\2\u058c\u058d\t\16\2\2\u058d\u05b3\5\u00f4{\3\u058e\u058f"+
		"\f\33\2\2\u058f\u0590\7^\2\2\u0590\u05b3\7\u0081\2\2\u0591\u0592\f\32"+
		"\2\2\u0592\u0593\7^\2\2\u0593\u05b3\7J\2\2\u0594\u0595\f\31\2\2\u0595"+
		"\u0596\7^\2\2\u0596\u0598\7>\2\2\u0597\u0599\5\u0104\u0083\2\u0598\u0597"+
		"\3\2\2\2\u0598\u0599\3\2\2\2\u0599\u059a\3\2\2\2\u059a\u05b3\5\u00fc\177"+
		"\2\u059b\u059c\f\30\2\2\u059c\u059d\7^\2\2\u059d\u059e\7G\2\2\u059e\u05b3"+
		"\5\u010a\u0086\2\u059f\u05a0\f\27\2\2\u05a0\u05a1\7^\2\2\u05a1\u05b3\5"+
		"\u0102\u0082\2\u05a2\u05a3\f\26\2\2\u05a3\u05a4\7Z\2\2\u05a4\u05a5\5\u00f4"+
		"{\2\u05a5\u05a6\7[\2\2\u05a6\u05b3\3\2\2\2\u05a7\u05a8\f\25\2\2\u05a8"+
		"\u05aa\7V\2\2\u05a9\u05ab\5\u00eex\2\u05aa\u05a9\3\2\2\2\u05aa\u05ab\3"+
		"\2\2\2\u05ab\u05ac\3\2\2\2\u05ac\u05b3\7W\2\2\u05ad\u05ae\f\22\2\2\u05ae"+
		"\u05b3\t\17\2\2\u05af\u05b0\f\13\2\2\u05b0\u05b1\79\2\2\u05b1\u05b3\5"+
		"\u0094K\2\u05b2\u055f\3\2\2\2\u05b2\u0562\3\2\2\2\u05b2\u0565\3\2\2\2"+
		"\u05b2\u0570\3\2\2\2\u05b2\u0573\3\2\2\2\u05b2\u0576\3\2\2\2\u05b2\u0579"+
		"\3\2\2\2\u05b2\u057c\3\2\2\2\u05b2\u057f\3\2\2\2\u05b2\u0582\3\2\2\2\u05b2"+
		"\u0585\3\2\2\2\u05b2\u058b\3\2\2\2\u05b2\u058e\3\2\2\2\u05b2\u0591\3\2"+
		"\2\2\u05b2\u0594\3\2\2\2\u05b2\u059b\3\2\2\2\u05b2\u059f\3\2\2\2\u05b2"+
		"\u05a2\3\2\2\2\u05b2\u05a7\3\2\2\2\u05b2\u05ad\3\2\2\2\u05b2\u05af\3\2"+
		"\2\2\u05b3\u05b6\3\2\2\2\u05b4\u05b2\3\2\2\2\u05b4\u05b5\3\2\2\2\u05b5"+
		"\u00f5\3\2\2\2\u05b6\u05b4\3\2\2\2\u05b7\u05b8\7V\2\2\u05b8\u05b9\5\u00f4"+
		"{\2\u05b9\u05ba\7W\2\2\u05ba\u05cd\3\2\2\2\u05bb\u05cd\7J\2\2\u05bc\u05cd"+
		"\7G\2\2\u05bd\u05cd\5F$\2\u05be\u05cd\7\u0081\2\2\u05bf\u05c0\5\u0094"+
		"K\2\u05c0\u05c1\7^\2\2\u05c1\u05c2\7(\2\2\u05c2\u05cd\3\2\2\2\u05c3\u05c4"+
		"\7O\2\2\u05c4\u05c5\7^\2\2\u05c5\u05cd\7(\2\2\u05c6\u05ca\5\u0104\u0083"+
		"\2\u05c7\u05cb\5\u010c\u0087\2\u05c8\u05c9\7J\2\2\u05c9\u05cb\5\u010e"+
		"\u0088\2\u05ca\u05c7\3\2\2\2\u05ca\u05c8\3\2\2\2\u05cb\u05cd\3\2\2\2\u05cc"+
		"\u05b7\3\2\2\2\u05cc\u05bb\3\2\2\2\u05cc\u05bc\3\2\2\2\u05cc\u05bd\3\2"+
		"\2\2\u05cc\u05be\3\2\2\2\u05cc\u05bf\3\2\2\2\u05cc\u05c3\3\2\2\2\u05cc"+
		"\u05c6\3\2\2\2\u05cd\u00f7\3\2\2\2\u05ce\u05cf\5\u0104\u0083\2\u05cf\u05d0"+
		"\5\u00fa~\2\u05d0\u05d1\5\u0100\u0081\2\u05d1\u05d8\3\2\2\2\u05d2\u05d5"+
		"\5\u00fa~\2\u05d3\u05d6\5\u00fe\u0080\2\u05d4\u05d6\5\u0100\u0081\2\u05d5"+
		"\u05d3\3\2\2\2\u05d5\u05d4\3\2\2\2\u05d6\u05d8\3\2\2\2\u05d7\u05ce\3\2"+
		"\2\2\u05d7\u05d2\3\2\2\2\u05d8\u00f9\3\2\2\2\u05d9\u05db\7\u0081\2\2\u05da"+
		"\u05dc\5\u0106\u0084\2\u05db\u05da\3\2\2\2\u05db\u05dc\3\2\2\2\u05dc\u05e4"+
		"\3\2\2\2\u05dd\u05de\7^\2\2\u05de\u05e0\7\u0081\2\2\u05df\u05e1\5\u0106"+
		"\u0084\2\u05e0\u05df\3\2\2\2\u05e0\u05e1\3\2\2\2\u05e1\u05e3\3\2\2\2\u05e2"+
		"\u05dd\3\2\2\2\u05e3\u05e6\3\2\2\2\u05e4\u05e2\3\2\2\2\u05e4\u05e5\3\2"+
		"\2\2\u05e5\u05e9\3\2\2\2\u05e6\u05e4\3\2\2\2\u05e7\u05e9\5\u0098M\2\u05e8"+
		"\u05d9\3\2\2\2\u05e8\u05e7\3\2\2\2\u05e9\u00fb\3\2\2\2\u05ea\u05ec\7\u0081"+
		"\2\2\u05eb\u05ed\5\u0108\u0085\2\u05ec\u05eb\3\2\2\2\u05ec\u05ed\3\2\2"+
		"\2\u05ed\u05ee\3\2\2\2\u05ee\u05ef\5\u0100\u0081\2\u05ef\u00fd\3\2\2\2"+
		"\u05f0\u060c\7Z\2\2\u05f1\u05f6\7[\2\2\u05f2\u05f3\7Z\2\2\u05f3\u05f5"+
		"\7[\2\2\u05f4\u05f2\3\2\2\2\u05f5\u05f8\3\2\2\2\u05f6\u05f4\3\2\2\2\u05f6"+
		"\u05f7\3\2\2\2\u05f7\u05f9\3\2\2\2\u05f8\u05f6\3\2\2\2\u05f9\u060d\5\u0090"+
		"I\2\u05fa\u05fb\5\u00f4{\2\u05fb\u0602\7[\2\2\u05fc\u05fd\7Z\2\2\u05fd"+
		"\u05fe\5\u00f4{\2\u05fe\u05ff\7[\2\2\u05ff\u0601\3\2\2\2\u0600\u05fc\3"+
		"\2\2\2\u0601\u0604\3\2\2\2\u0602\u0600\3\2\2\2\u0602\u0603\3\2\2\2\u0603"+
		"\u0609\3\2\2\2\u0604\u0602\3\2\2\2\u0605\u0606\7Z\2\2\u0606\u0608\7[\2"+
		"\2\u0607\u0605\3\2\2\2\u0608\u060b\3\2\2\2\u0609\u0607\3\2\2\2\u0609\u060a"+
		"\3\2\2\2\u060a\u060d\3\2\2\2\u060b\u0609\3\2\2\2\u060c\u05f1\3\2\2\2\u060c"+
		"\u05fa\3\2\2\2\u060d\u00ff\3\2\2\2\u060e\u0610\5\u010e\u0088\2\u060f\u0611"+
		"\5j\66\2\u0610\u060f\3\2\2\2\u0610\u0611\3\2\2\2\u0611\u0101\3\2\2\2\u0612"+
		"\u0613\5\u0104\u0083\2\u0613\u0614\5\u010c\u0087\2\u0614\u0103\3\2\2\2"+
		"\u0615\u0616\7a\2\2\u0616\u0617\5h\65\2\u0617\u0618\7`\2\2\u0618\u0105"+
		"\3\2\2\2\u0619\u061a\7a\2\2\u061a\u061d\7`\2\2\u061b\u061d\5\u009aN\2"+
		"\u061c\u0619\3\2\2\2\u061c\u061b\3\2\2\2\u061d\u0107\3\2\2\2\u061e\u061f"+
		"\7a\2\2\u061f\u0622\7`\2\2\u0620\u0622\5\u0104\u0083\2\u0621\u061e\3\2"+
		"\2\2\u0621\u0620\3\2\2\2\u0622\u0109\3\2\2\2\u0623\u062a\5\u010e\u0088"+
		"\2\u0624\u0625\7^\2\2\u0625\u0627\7\u0081\2\2\u0626\u0628\5\u010e\u0088"+
		"\2\u0627\u0626\3\2\2\2\u0627\u0628\3\2\2\2\u0628\u062a\3\2\2\2\u0629\u0623"+
		"\3\2\2\2\u0629\u0624\3\2\2\2\u062a\u010b\3\2\2\2\u062b\u062c\7G\2\2\u062c"+
		"\u0630\5\u010a\u0086\2\u062d\u062e\7\u0081\2\2\u062e\u0630\5\u010e\u0088"+
		"\2\u062f\u062b\3\2\2\2\u062f\u062d\3\2\2\2\u0630\u010d\3\2\2\2\u0631\u0633"+
		"\7V\2\2\u0632\u0634\5\u00eex\2\u0633\u0632\3\2\2\2\u0633\u0634\3\2\2\2"+
		"\u0634\u0635\3\2\2\2\u0635\u0636\7W\2\2\u0636\u010f\3\2\2\2\u00a1\u0113"+
		"\u0119\u011e\u0127\u0132\u014d\u0191\u01de\u01e9\u01f7\u0206\u020b\u0211"+
		"\u0219\u0222\u0227\u022e\u0235\u023c\u0243\u0248\u024c\u0250\u0254\u0259"+
		"\u025d\u0261\u026b\u0273\u027a\u0281\u0285\u0288\u028b\u0294\u029a\u029f"+
		"\u02a2\u02a8\u02ae\u02b2\u02bb\u02c2\u02cb\u02d2\u02d8\u02dc\u02e7\u02eb"+
		"\u02f3\u02f8\u02fc\u0305\u0313\u0318\u0321\u0329\u0333\u033b\u0343\u0348"+
		"\u0354\u035a\u0361\u0366\u036e\u0372\u0374\u037f\u0387\u038a\u038e\u0393"+
		"\u0397\u03a2\u03ab\u03ad\u03b4\u03b9\u03c2\u03c7\u03ca\u03cf\u03d8\u03e8"+
		"\u03f0\u03f3\u03fc\u0406\u040e\u0411\u0414\u0421\u0429\u042e\u0436\u043a"+
		"\u043e\u0442\u0444\u0448\u044e\u0459\u0461\u0469\u0474\u047d\u0494\u0497"+
		"\u049a\u04a2\u04a6\u04ae\u04b4\u04bf\u04c8\u04cd\u04d7\u04de\u04eb\u04f4"+
		"\u04fd\u0503\u050e\u0513\u051f\u0523\u0527\u052b\u052d\u0531\u0536\u0549"+
		"\u055d\u056d\u0598\u05aa\u05b2\u05b4\u05ca\u05cc\u05d5\u05d7\u05db\u05e0"+
		"\u05e4\u05e8\u05ec\u05f6\u0602\u0609\u060c\u0610\u061c\u0621\u0627\u0629"+
		"\u062f\u0633";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}