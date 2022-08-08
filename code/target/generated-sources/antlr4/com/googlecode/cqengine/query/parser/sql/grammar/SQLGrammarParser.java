// Generated from com\googlecode\cqengine\query\parser\sql\grammar\SQLGrammar.g4 by ANTLR 4.7
package com.googlecode.cqengine.query.parser.sql.grammar;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SQLGrammarParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		STRING_LITERAL_WITH_TRAILING_PERCENT=1, STRING_LITERAL_WITH_LEADING_PERCENT=2, 
		STRING_LITERAL_WITH_LEADING_AND_TRAILING_PERCENT=3, BOOLEAN_LITERAL=4, 
		SCOL=5, DOT=6, OPEN_PAR=7, CLOSE_PAR=8, COMMA=9, ASSIGN=10, STAR=11, PLUS=12, 
		MINUS=13, TILDE=14, PIPE2=15, DIV=16, MOD=17, LT2=18, GT2=19, AMP=20, 
		PIPE=21, LT=22, LT_EQ=23, GT=24, GT_EQ=25, EQ=26, NOT_EQ1=27, NOT_EQ2=28, 
		K_ABORT=29, K_ACTION=30, K_ADD=31, K_AFTER=32, K_ALL=33, K_ALTER=34, K_ANALYZE=35, 
		K_AND=36, K_AS=37, K_ASC=38, K_ATTACH=39, K_AUTOINCREMENT=40, K_BEFORE=41, 
		K_BEGIN=42, K_BETWEEN=43, K_BY=44, K_CASCADE=45, K_CASE=46, K_CAST=47, 
		K_CHECK=48, K_COLLATE=49, K_COLUMN=50, K_COMMIT=51, K_CONFLICT=52, K_CONSTRAINT=53, 
		K_CREATE=54, K_CROSS=55, K_CURRENT_DATE=56, K_CURRENT_TIME=57, K_CURRENT_TIMESTAMP=58, 
		K_DATABASE=59, K_DEFAULT=60, K_DEFERRABLE=61, K_DEFERRED=62, K_DELETE=63, 
		K_DESC=64, K_DETACH=65, K_DISTINCT=66, K_DROP=67, K_EACH=68, K_ELSE=69, 
		K_END=70, K_ESCAPE=71, K_EXCEPT=72, K_EXCLUSIVE=73, K_EXISTS=74, K_EXPLAIN=75, 
		K_FAIL=76, K_FOR=77, K_FOREIGN=78, K_FROM=79, K_FULL=80, K_GLOB=81, K_GROUP=82, 
		K_HAVING=83, K_IF=84, K_IGNORE=85, K_IMMEDIATE=86, K_IN=87, K_INDEX=88, 
		K_INDEXED=89, K_INITIALLY=90, K_INNER=91, K_INSERT=92, K_INSTEAD=93, K_INTERSECT=94, 
		K_INTO=95, K_IS=96, K_ISNULL=97, K_JOIN=98, K_KEY=99, K_LEFT=100, K_LIKE=101, 
		K_LIMIT=102, K_MATCH=103, K_NATURAL=104, K_NO=105, K_NOT=106, K_NOTNULL=107, 
		K_NULL=108, K_OF=109, K_OFFSET=110, K_ON=111, K_OR=112, K_ORDER=113, K_OUTER=114, 
		K_PLAN=115, K_PRAGMA=116, K_PRIMARY=117, K_QUERY=118, K_RAISE=119, K_RECURSIVE=120, 
		K_REFERENCES=121, K_REGEXP=122, K_REINDEX=123, K_RELEASE=124, K_RENAME=125, 
		K_REPLACE=126, K_RESTRICT=127, K_RIGHT=128, K_ROLLBACK=129, K_ROW=130, 
		K_SAVEPOINT=131, K_SELECT=132, K_SET=133, K_TABLE=134, K_TEMP=135, K_TEMPORARY=136, 
		K_THEN=137, K_TO=138, K_TRANSACTION=139, K_TRIGGER=140, K_UNION=141, K_UNIQUE=142, 
		K_UPDATE=143, K_USING=144, K_VACUUM=145, K_VALUES=146, K_VIEW=147, K_VIRTUAL=148, 
		K_WHEN=149, K_WHERE=150, K_WITH=151, K_WITHOUT=152, IDENTIFIER=153, NUMERIC_LITERAL=154, 
		BIND_PARAMETER=155, STRING_LITERAL=156, BLOB_LITERAL=157, SINGLE_LINE_COMMENT=158, 
		MULTILINE_COMMENT=159, SPACES=160, UNEXPECTED_CHAR=161;
	public static final int
		RULE_start = 0, RULE_indexedCollection = 1, RULE_whereClause = 2, RULE_orderByClause = 3, 
		RULE_query = 4, RULE_logicalQuery = 5, RULE_andQuery = 6, RULE_orQuery = 7, 
		RULE_notQuery = 8, RULE_simpleQuery = 9, RULE_equalQuery = 10, RULE_notEqualQuery = 11, 
		RULE_lessThanOrEqualToQuery = 12, RULE_lessThanQuery = 13, RULE_greaterThanOrEqualToQuery = 14, 
		RULE_greaterThanQuery = 15, RULE_betweenQuery = 16, RULE_notBetweenQuery = 17, 
		RULE_inQuery = 18, RULE_notInQuery = 19, RULE_startsWithQuery = 20, RULE_endsWithQuery = 21, 
		RULE_containsQuery = 22, RULE_hasQuery = 23, RULE_notHasQuery = 24, RULE_isPrefixOfQuery = 25, 
		RULE_attributeName = 26, RULE_queryParameterTrailingPercent = 27, RULE_queryParameterLeadingPercent = 28, 
		RULE_queryParameterLeadingAndTrailingPercent = 29, RULE_queryParameter = 30, 
		RULE_attributeOrder = 31, RULE_direction = 32, RULE_parse = 33, RULE_error = 34, 
		RULE_sql_stmt_list = 35, RULE_sql_stmt = 36, RULE_alter_table_stmt = 37, 
		RULE_analyze_stmt = 38, RULE_attach_stmt = 39, RULE_begin_stmt = 40, RULE_commit_stmt = 41, 
		RULE_compound_select_stmt = 42, RULE_create_index_stmt = 43, RULE_create_table_stmt = 44, 
		RULE_create_trigger_stmt = 45, RULE_create_view_stmt = 46, RULE_create_virtual_table_stmt = 47, 
		RULE_delete_stmt = 48, RULE_delete_stmt_limited = 49, RULE_detach_stmt = 50, 
		RULE_drop_index_stmt = 51, RULE_drop_table_stmt = 52, RULE_drop_trigger_stmt = 53, 
		RULE_drop_view_stmt = 54, RULE_factored_select_stmt = 55, RULE_insert_stmt = 56, 
		RULE_pragma_stmt = 57, RULE_reindex_stmt = 58, RULE_release_stmt = 59, 
		RULE_rollback_stmt = 60, RULE_savepoint_stmt = 61, RULE_simple_select_stmt = 62, 
		RULE_select_stmt = 63, RULE_select_or_values = 64, RULE_update_stmt = 65, 
		RULE_update_stmt_limited = 66, RULE_vacuum_stmt = 67, RULE_column_def = 68, 
		RULE_type_name = 69, RULE_column_constraint = 70, RULE_conflict_clause = 71, 
		RULE_expr = 72, RULE_foreign_key_clause = 73, RULE_raise_function = 74, 
		RULE_indexed_column = 75, RULE_table_constraint = 76, RULE_with_clause = 77, 
		RULE_qualified_table_name = 78, RULE_ordering_term = 79, RULE_pragma_value = 80, 
		RULE_common_table_expression = 81, RULE_result_column = 82, RULE_table_or_subquery = 83, 
		RULE_join_clause = 84, RULE_join_operator = 85, RULE_join_constraint = 86, 
		RULE_select_core = 87, RULE_compound_operator = 88, RULE_cte_table_name = 89, 
		RULE_signed_number = 90, RULE_literal_value = 91, RULE_unary_operator = 92, 
		RULE_error_message = 93, RULE_module_argument = 94, RULE_column_alias = 95, 
		RULE_keyword = 96, RULE_name = 97, RULE_function_name = 98, RULE_database_name = 99, 
		RULE_table_name = 100, RULE_table_or_index_name = 101, RULE_new_table_name = 102, 
		RULE_column_name = 103, RULE_collation_name = 104, RULE_foreign_table = 105, 
		RULE_index_name = 106, RULE_trigger_name = 107, RULE_view_name = 108, 
		RULE_module_name = 109, RULE_pragma_name = 110, RULE_savepoint_name = 111, 
		RULE_table_alias = 112, RULE_transaction_name = 113, RULE_any_name = 114;
	public static final String[] ruleNames = {
		"start", "indexedCollection", "whereClause", "orderByClause", "query", 
		"logicalQuery", "andQuery", "orQuery", "notQuery", "simpleQuery", "equalQuery", 
		"notEqualQuery", "lessThanOrEqualToQuery", "lessThanQuery", "greaterThanOrEqualToQuery", 
		"greaterThanQuery", "betweenQuery", "notBetweenQuery", "inQuery", "notInQuery", 
		"startsWithQuery", "endsWithQuery", "containsQuery", "hasQuery", "notHasQuery", 
		"isPrefixOfQuery", "attributeName", "queryParameterTrailingPercent", "queryParameterLeadingPercent", 
		"queryParameterLeadingAndTrailingPercent", "queryParameter", "attributeOrder", 
		"direction", "parse", "error", "sql_stmt_list", "sql_stmt", "alter_table_stmt", 
		"analyze_stmt", "attach_stmt", "begin_stmt", "commit_stmt", "compound_select_stmt", 
		"create_index_stmt", "create_table_stmt", "create_trigger_stmt", "create_view_stmt", 
		"create_virtual_table_stmt", "delete_stmt", "delete_stmt_limited", "detach_stmt", 
		"drop_index_stmt", "drop_table_stmt", "drop_trigger_stmt", "drop_view_stmt", 
		"factored_select_stmt", "insert_stmt", "pragma_stmt", "reindex_stmt", 
		"release_stmt", "rollback_stmt", "savepoint_stmt", "simple_select_stmt", 
		"select_stmt", "select_or_values", "update_stmt", "update_stmt_limited", 
		"vacuum_stmt", "column_def", "type_name", "column_constraint", "conflict_clause", 
		"expr", "foreign_key_clause", "raise_function", "indexed_column", "table_constraint", 
		"with_clause", "qualified_table_name", "ordering_term", "pragma_value", 
		"common_table_expression", "result_column", "table_or_subquery", "join_clause", 
		"join_operator", "join_constraint", "select_core", "compound_operator", 
		"cte_table_name", "signed_number", "literal_value", "unary_operator", 
		"error_message", "module_argument", "column_alias", "keyword", "name", 
		"function_name", "database_name", "table_name", "table_or_index_name", 
		"new_table_name", "column_name", "collation_name", "foreign_table", "index_name", 
		"trigger_name", "view_name", "module_name", "pragma_name", "savepoint_name", 
		"table_alias", "transaction_name", "any_name"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, null, null, "';'", "'.'", "'('", "')'", "','", "'='", 
		"'*'", "'+'", "'-'", "'~'", "'||'", "'/'", "'%'", "'<<'", "'>>'", "'&'", 
		"'|'", "'<'", "'<='", "'>'", "'>='", "'=='", "'!='", "'<>'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "STRING_LITERAL_WITH_TRAILING_PERCENT", "STRING_LITERAL_WITH_LEADING_PERCENT", 
		"STRING_LITERAL_WITH_LEADING_AND_TRAILING_PERCENT", "BOOLEAN_LITERAL", 
		"SCOL", "DOT", "OPEN_PAR", "CLOSE_PAR", "COMMA", "ASSIGN", "STAR", "PLUS", 
		"MINUS", "TILDE", "PIPE2", "DIV", "MOD", "LT2", "GT2", "AMP", "PIPE", 
		"LT", "LT_EQ", "GT", "GT_EQ", "EQ", "NOT_EQ1", "NOT_EQ2", "K_ABORT", "K_ACTION", 
		"K_ADD", "K_AFTER", "K_ALL", "K_ALTER", "K_ANALYZE", "K_AND", "K_AS", 
		"K_ASC", "K_ATTACH", "K_AUTOINCREMENT", "K_BEFORE", "K_BEGIN", "K_BETWEEN", 
		"K_BY", "K_CASCADE", "K_CASE", "K_CAST", "K_CHECK", "K_COLLATE", "K_COLUMN", 
		"K_COMMIT", "K_CONFLICT", "K_CONSTRAINT", "K_CREATE", "K_CROSS", "K_CURRENT_DATE", 
		"K_CURRENT_TIME", "K_CURRENT_TIMESTAMP", "K_DATABASE", "K_DEFAULT", "K_DEFERRABLE", 
		"K_DEFERRED", "K_DELETE", "K_DESC", "K_DETACH", "K_DISTINCT", "K_DROP", 
		"K_EACH", "K_ELSE", "K_END", "K_ESCAPE", "K_EXCEPT", "K_EXCLUSIVE", "K_EXISTS", 
		"K_EXPLAIN", "K_FAIL", "K_FOR", "K_FOREIGN", "K_FROM", "K_FULL", "K_GLOB", 
		"K_GROUP", "K_HAVING", "K_IF", "K_IGNORE", "K_IMMEDIATE", "K_IN", "K_INDEX", 
		"K_INDEXED", "K_INITIALLY", "K_INNER", "K_INSERT", "K_INSTEAD", "K_INTERSECT", 
		"K_INTO", "K_IS", "K_ISNULL", "K_JOIN", "K_KEY", "K_LEFT", "K_LIKE", "K_LIMIT", 
		"K_MATCH", "K_NATURAL", "K_NO", "K_NOT", "K_NOTNULL", "K_NULL", "K_OF", 
		"K_OFFSET", "K_ON", "K_OR", "K_ORDER", "K_OUTER", "K_PLAN", "K_PRAGMA", 
		"K_PRIMARY", "K_QUERY", "K_RAISE", "K_RECURSIVE", "K_REFERENCES", "K_REGEXP", 
		"K_REINDEX", "K_RELEASE", "K_RENAME", "K_REPLACE", "K_RESTRICT", "K_RIGHT", 
		"K_ROLLBACK", "K_ROW", "K_SAVEPOINT", "K_SELECT", "K_SET", "K_TABLE", 
		"K_TEMP", "K_TEMPORARY", "K_THEN", "K_TO", "K_TRANSACTION", "K_TRIGGER", 
		"K_UNION", "K_UNIQUE", "K_UPDATE", "K_USING", "K_VACUUM", "K_VALUES", 
		"K_VIEW", "K_VIRTUAL", "K_WHEN", "K_WHERE", "K_WITH", "K_WITHOUT", "IDENTIFIER", 
		"NUMERIC_LITERAL", "BIND_PARAMETER", "STRING_LITERAL", "BLOB_LITERAL", 
		"SINGLE_LINE_COMMENT", "MULTILINE_COMMENT", "SPACES", "UNEXPECTED_CHAR"
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
	public String getGrammarFileName() { return "SQLGrammar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SQLGrammarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class StartContext extends ParserRuleContext {
		public TerminalNode K_SELECT() { return getToken(SQLGrammarParser.K_SELECT, 0); }
		public TerminalNode STAR() { return getToken(SQLGrammarParser.STAR, 0); }
		public TerminalNode K_FROM() { return getToken(SQLGrammarParser.K_FROM, 0); }
		public IndexedCollectionContext indexedCollection() {
			return getRuleContext(IndexedCollectionContext.class,0);
		}
		public TerminalNode EOF() { return getToken(SQLGrammarParser.EOF, 0); }
		public WhereClauseContext whereClause() {
			return getRuleContext(WhereClauseContext.class,0);
		}
		public OrderByClauseContext orderByClause() {
			return getRuleContext(OrderByClauseContext.class,0);
		}
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitStart(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_start);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			match(K_SELECT);
			setState(231);
			match(STAR);
			setState(232);
			match(K_FROM);
			setState(233);
			indexedCollection();
			setState(235);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WHERE) {
				{
				setState(234);
				whereClause();
				}
			}

			setState(238);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_ORDER) {
				{
				setState(237);
				orderByClause();
				}
			}

			setState(240);
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

	public static class IndexedCollectionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(SQLGrammarParser.IDENTIFIER, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(SQLGrammarParser.STRING_LITERAL, 0); }
		public IndexedCollectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_indexedCollection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterIndexedCollection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitIndexedCollection(this);
		}
	}

	public final IndexedCollectionContext indexedCollection() throws RecognitionException {
		IndexedCollectionContext _localctx = new IndexedCollectionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_indexedCollection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(242);
			_la = _input.LA(1);
			if ( !(_la==IDENTIFIER || _la==STRING_LITERAL) ) {
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

	public static class WhereClauseContext extends ParserRuleContext {
		public TerminalNode K_WHERE() { return getToken(SQLGrammarParser.K_WHERE, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public WhereClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whereClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterWhereClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitWhereClause(this);
		}
	}

	public final WhereClauseContext whereClause() throws RecognitionException {
		WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_whereClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
			match(K_WHERE);
			setState(245);
			query();
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

	public static class OrderByClauseContext extends ParserRuleContext {
		public TerminalNode K_ORDER() { return getToken(SQLGrammarParser.K_ORDER, 0); }
		public TerminalNode K_BY() { return getToken(SQLGrammarParser.K_BY, 0); }
		public List<AttributeOrderContext> attributeOrder() {
			return getRuleContexts(AttributeOrderContext.class);
		}
		public AttributeOrderContext attributeOrder(int i) {
			return getRuleContext(AttributeOrderContext.class,i);
		}
		public OrderByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderByClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterOrderByClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitOrderByClause(this);
		}
	}

	public final OrderByClauseContext orderByClause() throws RecognitionException {
		OrderByClauseContext _localctx = new OrderByClauseContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_orderByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(247);
			match(K_ORDER);
			setState(248);
			match(K_BY);
			setState(249);
			attributeOrder();
			setState(254);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(250);
				match(COMMA);
				setState(251);
				attributeOrder();
				}
				}
				setState(256);
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
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitQuery(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_query);
		try {
			setState(259);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(257);
				logicalQuery();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(258);
				simpleQuery();
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
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterLogicalQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitLogicalQuery(this);
		}
	}

	public final LogicalQueryContext logicalQuery() throws RecognitionException {
		LogicalQueryContext _localctx = new LogicalQueryContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_logicalQuery);
		try {
			setState(264);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(261);
				andQuery();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(262);
				orQuery();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(263);
				notQuery();
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

	public static class AndQueryContext extends ParserRuleContext {
		public TerminalNode OPEN_PAR() { return getToken(SQLGrammarParser.OPEN_PAR, 0); }
		public List<QueryContext> query() {
			return getRuleContexts(QueryContext.class);
		}
		public QueryContext query(int i) {
			return getRuleContext(QueryContext.class,i);
		}
		public List<TerminalNode> K_AND() { return getTokens(SQLGrammarParser.K_AND); }
		public TerminalNode K_AND(int i) {
			return getToken(SQLGrammarParser.K_AND, i);
		}
		public TerminalNode CLOSE_PAR() { return getToken(SQLGrammarParser.CLOSE_PAR, 0); }
		public AndQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterAndQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitAndQuery(this);
		}
	}

	public final AndQueryContext andQuery() throws RecognitionException {
		AndQueryContext _localctx = new AndQueryContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_andQuery);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(266);
			match(OPEN_PAR);
			setState(267);
			query();
			setState(268);
			match(K_AND);
			setState(269);
			query();
			setState(274);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==K_AND) {
				{
				{
				setState(270);
				match(K_AND);
				setState(271);
				query();
				}
				}
				setState(276);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(277);
			match(CLOSE_PAR);
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
		public TerminalNode OPEN_PAR() { return getToken(SQLGrammarParser.OPEN_PAR, 0); }
		public List<QueryContext> query() {
			return getRuleContexts(QueryContext.class);
		}
		public QueryContext query(int i) {
			return getRuleContext(QueryContext.class,i);
		}
		public List<TerminalNode> K_OR() { return getTokens(SQLGrammarParser.K_OR); }
		public TerminalNode K_OR(int i) {
			return getToken(SQLGrammarParser.K_OR, i);
		}
		public TerminalNode CLOSE_PAR() { return getToken(SQLGrammarParser.CLOSE_PAR, 0); }
		public OrQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterOrQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitOrQuery(this);
		}
	}

	public final OrQueryContext orQuery() throws RecognitionException {
		OrQueryContext _localctx = new OrQueryContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_orQuery);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
			match(OPEN_PAR);
			setState(280);
			query();
			setState(281);
			match(K_OR);
			setState(282);
			query();
			setState(287);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==K_OR) {
				{
				{
				setState(283);
				match(K_OR);
				setState(284);
				query();
				}
				}
				setState(289);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(290);
			match(CLOSE_PAR);
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
		public TerminalNode K_NOT() { return getToken(SQLGrammarParser.K_NOT, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TerminalNode OPEN_PAR() { return getToken(SQLGrammarParser.OPEN_PAR, 0); }
		public TerminalNode CLOSE_PAR() { return getToken(SQLGrammarParser.CLOSE_PAR, 0); }
		public NotQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterNotQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitNotQuery(this);
		}
	}

	public final NotQueryContext notQuery() throws RecognitionException {
		NotQueryContext _localctx = new NotQueryContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_notQuery);
		try {
			setState(299);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_NOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(292);
				match(K_NOT);
				setState(293);
				query();
				}
				break;
			case OPEN_PAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(294);
				match(OPEN_PAR);
				setState(295);
				match(K_NOT);
				setState(296);
				query();
				setState(297);
				match(CLOSE_PAR);
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

	public static class SimpleQueryContext extends ParserRuleContext {
		public EqualQueryContext equalQuery() {
			return getRuleContext(EqualQueryContext.class,0);
		}
		public NotEqualQueryContext notEqualQuery() {
			return getRuleContext(NotEqualQueryContext.class,0);
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
		public BetweenQueryContext betweenQuery() {
			return getRuleContext(BetweenQueryContext.class,0);
		}
		public NotBetweenQueryContext notBetweenQuery() {
			return getRuleContext(NotBetweenQueryContext.class,0);
		}
		public InQueryContext inQuery() {
			return getRuleContext(InQueryContext.class,0);
		}
		public NotInQueryContext notInQuery() {
			return getRuleContext(NotInQueryContext.class,0);
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
		public HasQueryContext hasQuery() {
			return getRuleContext(HasQueryContext.class,0);
		}
		public NotHasQueryContext notHasQuery() {
			return getRuleContext(NotHasQueryContext.class,0);
		}
		public IsPrefixOfQueryContext isPrefixOfQuery() {
			return getRuleContext(IsPrefixOfQueryContext.class,0);
		}
		public TerminalNode OPEN_PAR() { return getToken(SQLGrammarParser.OPEN_PAR, 0); }
		public SimpleQueryContext simpleQuery() {
			return getRuleContext(SimpleQueryContext.class,0);
		}
		public TerminalNode CLOSE_PAR() { return getToken(SQLGrammarParser.CLOSE_PAR, 0); }
		public SimpleQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterSimpleQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitSimpleQuery(this);
		}
	}

	public final SimpleQueryContext simpleQuery() throws RecognitionException {
		SimpleQueryContext _localctx = new SimpleQueryContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_simpleQuery);
		try {
			setState(321);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(301);
				equalQuery();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(302);
				notEqualQuery();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(303);
				lessThanOrEqualToQuery();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(304);
				lessThanQuery();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(305);
				greaterThanOrEqualToQuery();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(306);
				greaterThanQuery();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(307);
				betweenQuery();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(308);
				notBetweenQuery();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(309);
				inQuery();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(310);
				notInQuery();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(311);
				startsWithQuery();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(312);
				endsWithQuery();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(313);
				containsQuery();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(314);
				hasQuery();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(315);
				notHasQuery();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(316);
				isPrefixOfQuery();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(317);
				match(OPEN_PAR);
				setState(318);
				simpleQuery();
				setState(319);
				match(CLOSE_PAR);
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
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(SQLGrammarParser.ASSIGN, 0); }
		public QueryParameterContext queryParameter() {
			return getRuleContext(QueryParameterContext.class,0);
		}
		public EqualQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equalQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterEqualQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitEqualQuery(this);
		}
	}

	public final EqualQueryContext equalQuery() throws RecognitionException {
		EqualQueryContext _localctx = new EqualQueryContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_equalQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(323);
			attributeName();
			setState(324);
			match(ASSIGN);
			setState(325);
			queryParameter();
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

	public static class NotEqualQueryContext extends ParserRuleContext {
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode NOT_EQ2() { return getToken(SQLGrammarParser.NOT_EQ2, 0); }
		public QueryParameterContext queryParameter() {
			return getRuleContext(QueryParameterContext.class,0);
		}
		public NotEqualQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notEqualQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterNotEqualQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitNotEqualQuery(this);
		}
	}

	public final NotEqualQueryContext notEqualQuery() throws RecognitionException {
		NotEqualQueryContext _localctx = new NotEqualQueryContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_notEqualQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(327);
			attributeName();
			setState(328);
			match(NOT_EQ2);
			setState(329);
			queryParameter();
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
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode LT_EQ() { return getToken(SQLGrammarParser.LT_EQ, 0); }
		public QueryParameterContext queryParameter() {
			return getRuleContext(QueryParameterContext.class,0);
		}
		public LessThanOrEqualToQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lessThanOrEqualToQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterLessThanOrEqualToQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitLessThanOrEqualToQuery(this);
		}
	}

	public final LessThanOrEqualToQueryContext lessThanOrEqualToQuery() throws RecognitionException {
		LessThanOrEqualToQueryContext _localctx = new LessThanOrEqualToQueryContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_lessThanOrEqualToQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(331);
			attributeName();
			setState(332);
			match(LT_EQ);
			setState(333);
			queryParameter();
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
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode LT() { return getToken(SQLGrammarParser.LT, 0); }
		public QueryParameterContext queryParameter() {
			return getRuleContext(QueryParameterContext.class,0);
		}
		public LessThanQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lessThanQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterLessThanQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitLessThanQuery(this);
		}
	}

	public final LessThanQueryContext lessThanQuery() throws RecognitionException {
		LessThanQueryContext _localctx = new LessThanQueryContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_lessThanQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			attributeName();
			setState(336);
			match(LT);
			setState(337);
			queryParameter();
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
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode GT_EQ() { return getToken(SQLGrammarParser.GT_EQ, 0); }
		public QueryParameterContext queryParameter() {
			return getRuleContext(QueryParameterContext.class,0);
		}
		public GreaterThanOrEqualToQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_greaterThanOrEqualToQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterGreaterThanOrEqualToQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitGreaterThanOrEqualToQuery(this);
		}
	}

	public final GreaterThanOrEqualToQueryContext greaterThanOrEqualToQuery() throws RecognitionException {
		GreaterThanOrEqualToQueryContext _localctx = new GreaterThanOrEqualToQueryContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_greaterThanOrEqualToQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(339);
			attributeName();
			setState(340);
			match(GT_EQ);
			setState(341);
			queryParameter();
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
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode GT() { return getToken(SQLGrammarParser.GT, 0); }
		public QueryParameterContext queryParameter() {
			return getRuleContext(QueryParameterContext.class,0);
		}
		public GreaterThanQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_greaterThanQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterGreaterThanQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitGreaterThanQuery(this);
		}
	}

	public final GreaterThanQueryContext greaterThanQuery() throws RecognitionException {
		GreaterThanQueryContext _localctx = new GreaterThanQueryContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_greaterThanQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(343);
			attributeName();
			setState(344);
			match(GT);
			setState(345);
			queryParameter();
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
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode K_BETWEEN() { return getToken(SQLGrammarParser.K_BETWEEN, 0); }
		public List<QueryParameterContext> queryParameter() {
			return getRuleContexts(QueryParameterContext.class);
		}
		public QueryParameterContext queryParameter(int i) {
			return getRuleContext(QueryParameterContext.class,i);
		}
		public TerminalNode K_AND() { return getToken(SQLGrammarParser.K_AND, 0); }
		public BetweenQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_betweenQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterBetweenQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitBetweenQuery(this);
		}
	}

	public final BetweenQueryContext betweenQuery() throws RecognitionException {
		BetweenQueryContext _localctx = new BetweenQueryContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_betweenQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(347);
			attributeName();
			setState(348);
			match(K_BETWEEN);
			setState(349);
			queryParameter();
			setState(350);
			match(K_AND);
			setState(351);
			queryParameter();
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

	public static class NotBetweenQueryContext extends ParserRuleContext {
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode K_NOT() { return getToken(SQLGrammarParser.K_NOT, 0); }
		public TerminalNode K_BETWEEN() { return getToken(SQLGrammarParser.K_BETWEEN, 0); }
		public List<QueryParameterContext> queryParameter() {
			return getRuleContexts(QueryParameterContext.class);
		}
		public QueryParameterContext queryParameter(int i) {
			return getRuleContext(QueryParameterContext.class,i);
		}
		public TerminalNode K_AND() { return getToken(SQLGrammarParser.K_AND, 0); }
		public NotBetweenQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notBetweenQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterNotBetweenQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitNotBetweenQuery(this);
		}
	}

	public final NotBetweenQueryContext notBetweenQuery() throws RecognitionException {
		NotBetweenQueryContext _localctx = new NotBetweenQueryContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_notBetweenQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(353);
			attributeName();
			setState(354);
			match(K_NOT);
			setState(355);
			match(K_BETWEEN);
			setState(356);
			queryParameter();
			setState(357);
			match(K_AND);
			setState(358);
			queryParameter();
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
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode K_IN() { return getToken(SQLGrammarParser.K_IN, 0); }
		public TerminalNode OPEN_PAR() { return getToken(SQLGrammarParser.OPEN_PAR, 0); }
		public List<QueryParameterContext> queryParameter() {
			return getRuleContexts(QueryParameterContext.class);
		}
		public QueryParameterContext queryParameter(int i) {
			return getRuleContext(QueryParameterContext.class,i);
		}
		public TerminalNode CLOSE_PAR() { return getToken(SQLGrammarParser.CLOSE_PAR, 0); }
		public InQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterInQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitInQuery(this);
		}
	}

	public final InQueryContext inQuery() throws RecognitionException {
		InQueryContext _localctx = new InQueryContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_inQuery);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(360);
			attributeName();
			setState(361);
			match(K_IN);
			setState(362);
			match(OPEN_PAR);
			setState(363);
			queryParameter();
			setState(368);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(364);
				match(COMMA);
				setState(365);
				queryParameter();
				}
				}
				setState(370);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(371);
			match(CLOSE_PAR);
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

	public static class NotInQueryContext extends ParserRuleContext {
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode K_NOT() { return getToken(SQLGrammarParser.K_NOT, 0); }
		public TerminalNode K_IN() { return getToken(SQLGrammarParser.K_IN, 0); }
		public TerminalNode OPEN_PAR() { return getToken(SQLGrammarParser.OPEN_PAR, 0); }
		public List<QueryParameterContext> queryParameter() {
			return getRuleContexts(QueryParameterContext.class);
		}
		public QueryParameterContext queryParameter(int i) {
			return getRuleContext(QueryParameterContext.class,i);
		}
		public TerminalNode CLOSE_PAR() { return getToken(SQLGrammarParser.CLOSE_PAR, 0); }
		public NotInQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notInQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterNotInQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitNotInQuery(this);
		}
	}

	public final NotInQueryContext notInQuery() throws RecognitionException {
		NotInQueryContext _localctx = new NotInQueryContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_notInQuery);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(373);
			attributeName();
			setState(374);
			match(K_NOT);
			setState(375);
			match(K_IN);
			setState(376);
			match(OPEN_PAR);
			setState(377);
			queryParameter();
			setState(382);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(378);
				match(COMMA);
				setState(379);
				queryParameter();
				}
				}
				setState(384);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(385);
			match(CLOSE_PAR);
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
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode K_LIKE() { return getToken(SQLGrammarParser.K_LIKE, 0); }
		public QueryParameterTrailingPercentContext queryParameterTrailingPercent() {
			return getRuleContext(QueryParameterTrailingPercentContext.class,0);
		}
		public StartsWithQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_startsWithQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterStartsWithQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitStartsWithQuery(this);
		}
	}

	public final StartsWithQueryContext startsWithQuery() throws RecognitionException {
		StartsWithQueryContext _localctx = new StartsWithQueryContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_startsWithQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
			attributeName();
			setState(388);
			match(K_LIKE);
			setState(389);
			queryParameterTrailingPercent();
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
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode K_LIKE() { return getToken(SQLGrammarParser.K_LIKE, 0); }
		public QueryParameterLeadingPercentContext queryParameterLeadingPercent() {
			return getRuleContext(QueryParameterLeadingPercentContext.class,0);
		}
		public EndsWithQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endsWithQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterEndsWithQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitEndsWithQuery(this);
		}
	}

	public final EndsWithQueryContext endsWithQuery() throws RecognitionException {
		EndsWithQueryContext _localctx = new EndsWithQueryContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_endsWithQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(391);
			attributeName();
			setState(392);
			match(K_LIKE);
			setState(393);
			queryParameterLeadingPercent();
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
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode K_LIKE() { return getToken(SQLGrammarParser.K_LIKE, 0); }
		public QueryParameterLeadingAndTrailingPercentContext queryParameterLeadingAndTrailingPercent() {
			return getRuleContext(QueryParameterLeadingAndTrailingPercentContext.class,0);
		}
		public ContainsQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_containsQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterContainsQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitContainsQuery(this);
		}
	}

	public final ContainsQueryContext containsQuery() throws RecognitionException {
		ContainsQueryContext _localctx = new ContainsQueryContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_containsQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(395);
			attributeName();
			setState(396);
			match(K_LIKE);
			setState(397);
			queryParameterLeadingAndTrailingPercent();
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
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode K_IS() { return getToken(SQLGrammarParser.K_IS, 0); }
		public TerminalNode K_NOT() { return getToken(SQLGrammarParser.K_NOT, 0); }
		public TerminalNode K_NULL() { return getToken(SQLGrammarParser.K_NULL, 0); }
		public HasQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hasQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterHasQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitHasQuery(this);
		}
	}

	public final HasQueryContext hasQuery() throws RecognitionException {
		HasQueryContext _localctx = new HasQueryContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_hasQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(399);
			attributeName();
			setState(400);
			match(K_IS);
			setState(401);
			match(K_NOT);
			setState(402);
			match(K_NULL);
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

	public static class NotHasQueryContext extends ParserRuleContext {
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode K_IS() { return getToken(SQLGrammarParser.K_IS, 0); }
		public TerminalNode K_NULL() { return getToken(SQLGrammarParser.K_NULL, 0); }
		public NotHasQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notHasQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterNotHasQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitNotHasQuery(this);
		}
	}

	public final NotHasQueryContext notHasQuery() throws RecognitionException {
		NotHasQueryContext _localctx = new NotHasQueryContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_notHasQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(404);
			attributeName();
			setState(405);
			match(K_IS);
			setState(406);
			match(K_NULL);
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
		public QueryParameterContext queryParameter() {
			return getRuleContext(QueryParameterContext.class,0);
		}
		public TerminalNode K_LIKE() { return getToken(SQLGrammarParser.K_LIKE, 0); }
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public TerminalNode PIPE2() { return getToken(SQLGrammarParser.PIPE2, 0); }
		public QueryParameterTrailingPercentContext queryParameterTrailingPercent() {
			return getRuleContext(QueryParameterTrailingPercentContext.class,0);
		}
		public IsPrefixOfQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_isPrefixOfQuery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterIsPrefixOfQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitIsPrefixOfQuery(this);
		}
	}

	public final IsPrefixOfQueryContext isPrefixOfQuery() throws RecognitionException {
		IsPrefixOfQueryContext _localctx = new IsPrefixOfQueryContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_isPrefixOfQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(408);
			queryParameter();
			setState(409);
			match(K_LIKE);
			setState(410);
			attributeName();
			setState(411);
			match(PIPE2);
			setState(412);
			queryParameterTrailingPercent();
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
		public TerminalNode IDENTIFIER() { return getToken(SQLGrammarParser.IDENTIFIER, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(SQLGrammarParser.STRING_LITERAL, 0); }
		public AttributeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterAttributeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitAttributeName(this);
		}
	}

	public final AttributeNameContext attributeName() throws RecognitionException {
		AttributeNameContext _localctx = new AttributeNameContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_attributeName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(414);
			_la = _input.LA(1);
			if ( !(_la==IDENTIFIER || _la==STRING_LITERAL) ) {
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

	public static class QueryParameterTrailingPercentContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL_WITH_TRAILING_PERCENT() { return getToken(SQLGrammarParser.STRING_LITERAL_WITH_TRAILING_PERCENT, 0); }
		public QueryParameterTrailingPercentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryParameterTrailingPercent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterQueryParameterTrailingPercent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitQueryParameterTrailingPercent(this);
		}
	}

	public final QueryParameterTrailingPercentContext queryParameterTrailingPercent() throws RecognitionException {
		QueryParameterTrailingPercentContext _localctx = new QueryParameterTrailingPercentContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_queryParameterTrailingPercent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(416);
			match(STRING_LITERAL_WITH_TRAILING_PERCENT);
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

	public static class QueryParameterLeadingPercentContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL_WITH_LEADING_PERCENT() { return getToken(SQLGrammarParser.STRING_LITERAL_WITH_LEADING_PERCENT, 0); }
		public QueryParameterLeadingPercentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryParameterLeadingPercent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterQueryParameterLeadingPercent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitQueryParameterLeadingPercent(this);
		}
	}

	public final QueryParameterLeadingPercentContext queryParameterLeadingPercent() throws RecognitionException {
		QueryParameterLeadingPercentContext _localctx = new QueryParameterLeadingPercentContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_queryParameterLeadingPercent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(418);
			match(STRING_LITERAL_WITH_LEADING_PERCENT);
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

	public static class QueryParameterLeadingAndTrailingPercentContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL_WITH_LEADING_AND_TRAILING_PERCENT() { return getToken(SQLGrammarParser.STRING_LITERAL_WITH_LEADING_AND_TRAILING_PERCENT, 0); }
		public QueryParameterLeadingAndTrailingPercentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryParameterLeadingAndTrailingPercent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterQueryParameterLeadingAndTrailingPercent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitQueryParameterLeadingAndTrailingPercent(this);
		}
	}

	public final QueryParameterLeadingAndTrailingPercentContext queryParameterLeadingAndTrailingPercent() throws RecognitionException {
		QueryParameterLeadingAndTrailingPercentContext _localctx = new QueryParameterLeadingAndTrailingPercentContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_queryParameterLeadingAndTrailingPercent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(420);
			match(STRING_LITERAL_WITH_LEADING_AND_TRAILING_PERCENT);
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
		public TerminalNode NUMERIC_LITERAL() { return getToken(SQLGrammarParser.NUMERIC_LITERAL, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(SQLGrammarParser.STRING_LITERAL, 0); }
		public TerminalNode BOOLEAN_LITERAL() { return getToken(SQLGrammarParser.BOOLEAN_LITERAL, 0); }
		public QueryParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterQueryParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitQueryParameter(this);
		}
	}

	public final QueryParameterContext queryParameter() throws RecognitionException {
		QueryParameterContext _localctx = new QueryParameterContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_queryParameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(422);
			_la = _input.LA(1);
			if ( !(_la==BOOLEAN_LITERAL || _la==NUMERIC_LITERAL || _la==STRING_LITERAL) ) {
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

	public static class AttributeOrderContext extends ParserRuleContext {
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public DirectionContext direction() {
			return getRuleContext(DirectionContext.class,0);
		}
		public AttributeOrderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeOrder; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterAttributeOrder(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitAttributeOrder(this);
		}
	}

	public final AttributeOrderContext attributeOrder() throws RecognitionException {
		AttributeOrderContext _localctx = new AttributeOrderContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_attributeOrder);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(424);
			attributeName();
			setState(426);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_ASC || _la==K_DESC) {
				{
				setState(425);
				direction();
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

	public static class DirectionContext extends ParserRuleContext {
		public TerminalNode K_ASC() { return getToken(SQLGrammarParser.K_ASC, 0); }
		public TerminalNode K_DESC() { return getToken(SQLGrammarParser.K_DESC, 0); }
		public DirectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_direction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterDirection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitDirection(this);
		}
	}

	public final DirectionContext direction() throws RecognitionException {
		DirectionContext _localctx = new DirectionContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_direction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(428);
			_la = _input.LA(1);
			if ( !(_la==K_ASC || _la==K_DESC) ) {
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

	public static class ParseContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(SQLGrammarParser.EOF, 0); }
		public List<Sql_stmt_listContext> sql_stmt_list() {
			return getRuleContexts(Sql_stmt_listContext.class);
		}
		public Sql_stmt_listContext sql_stmt_list(int i) {
			return getRuleContext(Sql_stmt_listContext.class,i);
		}
		public List<ErrorContext> error() {
			return getRuleContexts(ErrorContext.class);
		}
		public ErrorContext error(int i) {
			return getRuleContext(ErrorContext.class,i);
		}
		public ParseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parse; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterParse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitParse(this);
		}
	}

	public final ParseContext parse() throws RecognitionException {
		ParseContext _localctx = new ParseContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_parse);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(434);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SCOL) | (1L << K_ALTER) | (1L << K_ANALYZE) | (1L << K_ATTACH) | (1L << K_BEGIN) | (1L << K_COMMIT) | (1L << K_CREATE) | (1L << K_DELETE))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (K_DETACH - 65)) | (1L << (K_DROP - 65)) | (1L << (K_END - 65)) | (1L << (K_EXPLAIN - 65)) | (1L << (K_INSERT - 65)) | (1L << (K_PRAGMA - 65)) | (1L << (K_REINDEX - 65)) | (1L << (K_RELEASE - 65)) | (1L << (K_REPLACE - 65)))) != 0) || ((((_la - 129)) & ~0x3f) == 0 && ((1L << (_la - 129)) & ((1L << (K_ROLLBACK - 129)) | (1L << (K_SAVEPOINT - 129)) | (1L << (K_SELECT - 129)) | (1L << (K_UPDATE - 129)) | (1L << (K_VACUUM - 129)) | (1L << (K_VALUES - 129)) | (1L << (K_WITH - 129)) | (1L << (UNEXPECTED_CHAR - 129)))) != 0)) {
				{
				setState(432);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case SCOL:
				case K_ALTER:
				case K_ANALYZE:
				case K_ATTACH:
				case K_BEGIN:
				case K_COMMIT:
				case K_CREATE:
				case K_DELETE:
				case K_DETACH:
				case K_DROP:
				case K_END:
				case K_EXPLAIN:
				case K_INSERT:
				case K_PRAGMA:
				case K_REINDEX:
				case K_RELEASE:
				case K_REPLACE:
				case K_ROLLBACK:
				case K_SAVEPOINT:
				case K_SELECT:
				case K_UPDATE:
				case K_VACUUM:
				case K_VALUES:
				case K_WITH:
					{
					setState(430);
					sql_stmt_list();
					}
					break;
				case UNEXPECTED_CHAR:
					{
					setState(431);
					error();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(436);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(437);
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

	public static class ErrorContext extends ParserRuleContext {
		public Token UNEXPECTED_CHAR;
		public TerminalNode UNEXPECTED_CHAR() { return getToken(SQLGrammarParser.UNEXPECTED_CHAR, 0); }
		public ErrorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_error; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterError(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitError(this);
		}
	}

	public final ErrorContext error() throws RecognitionException {
		ErrorContext _localctx = new ErrorContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_error);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(439);
			((ErrorContext)_localctx).UNEXPECTED_CHAR = match(UNEXPECTED_CHAR);
			 
			     throw new RuntimeException("UNEXPECTED_CHAR=" + (((ErrorContext)_localctx).UNEXPECTED_CHAR!=null?((ErrorContext)_localctx).UNEXPECTED_CHAR.getText():null)); 
			   
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

	public static class Sql_stmt_listContext extends ParserRuleContext {
		public List<Sql_stmtContext> sql_stmt() {
			return getRuleContexts(Sql_stmtContext.class);
		}
		public Sql_stmtContext sql_stmt(int i) {
			return getRuleContext(Sql_stmtContext.class,i);
		}
		public Sql_stmt_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sql_stmt_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterSql_stmt_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitSql_stmt_list(this);
		}
	}

	public final Sql_stmt_listContext sql_stmt_list() throws RecognitionException {
		Sql_stmt_listContext _localctx = new Sql_stmt_listContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_sql_stmt_list);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(445);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SCOL) {
				{
				{
				setState(442);
				match(SCOL);
				}
				}
				setState(447);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(448);
			sql_stmt();
			setState(457);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(450); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(449);
						match(SCOL);
						}
						}
						setState(452); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==SCOL );
					setState(454);
					sql_stmt();
					}
					} 
				}
				setState(459);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			}
			setState(463);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(460);
					match(SCOL);
					}
					} 
				}
				setState(465);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
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

	public static class Sql_stmtContext extends ParserRuleContext {
		public Alter_table_stmtContext alter_table_stmt() {
			return getRuleContext(Alter_table_stmtContext.class,0);
		}
		public Analyze_stmtContext analyze_stmt() {
			return getRuleContext(Analyze_stmtContext.class,0);
		}
		public Attach_stmtContext attach_stmt() {
			return getRuleContext(Attach_stmtContext.class,0);
		}
		public Begin_stmtContext begin_stmt() {
			return getRuleContext(Begin_stmtContext.class,0);
		}
		public Commit_stmtContext commit_stmt() {
			return getRuleContext(Commit_stmtContext.class,0);
		}
		public Compound_select_stmtContext compound_select_stmt() {
			return getRuleContext(Compound_select_stmtContext.class,0);
		}
		public Create_index_stmtContext create_index_stmt() {
			return getRuleContext(Create_index_stmtContext.class,0);
		}
		public Create_table_stmtContext create_table_stmt() {
			return getRuleContext(Create_table_stmtContext.class,0);
		}
		public Create_trigger_stmtContext create_trigger_stmt() {
			return getRuleContext(Create_trigger_stmtContext.class,0);
		}
		public Create_view_stmtContext create_view_stmt() {
			return getRuleContext(Create_view_stmtContext.class,0);
		}
		public Create_virtual_table_stmtContext create_virtual_table_stmt() {
			return getRuleContext(Create_virtual_table_stmtContext.class,0);
		}
		public Delete_stmtContext delete_stmt() {
			return getRuleContext(Delete_stmtContext.class,0);
		}
		public Delete_stmt_limitedContext delete_stmt_limited() {
			return getRuleContext(Delete_stmt_limitedContext.class,0);
		}
		public Detach_stmtContext detach_stmt() {
			return getRuleContext(Detach_stmtContext.class,0);
		}
		public Drop_index_stmtContext drop_index_stmt() {
			return getRuleContext(Drop_index_stmtContext.class,0);
		}
		public Drop_table_stmtContext drop_table_stmt() {
			return getRuleContext(Drop_table_stmtContext.class,0);
		}
		public Drop_trigger_stmtContext drop_trigger_stmt() {
			return getRuleContext(Drop_trigger_stmtContext.class,0);
		}
		public Drop_view_stmtContext drop_view_stmt() {
			return getRuleContext(Drop_view_stmtContext.class,0);
		}
		public Factored_select_stmtContext factored_select_stmt() {
			return getRuleContext(Factored_select_stmtContext.class,0);
		}
		public Insert_stmtContext insert_stmt() {
			return getRuleContext(Insert_stmtContext.class,0);
		}
		public Pragma_stmtContext pragma_stmt() {
			return getRuleContext(Pragma_stmtContext.class,0);
		}
		public Reindex_stmtContext reindex_stmt() {
			return getRuleContext(Reindex_stmtContext.class,0);
		}
		public Release_stmtContext release_stmt() {
			return getRuleContext(Release_stmtContext.class,0);
		}
		public Rollback_stmtContext rollback_stmt() {
			return getRuleContext(Rollback_stmtContext.class,0);
		}
		public Savepoint_stmtContext savepoint_stmt() {
			return getRuleContext(Savepoint_stmtContext.class,0);
		}
		public Simple_select_stmtContext simple_select_stmt() {
			return getRuleContext(Simple_select_stmtContext.class,0);
		}
		public Select_stmtContext select_stmt() {
			return getRuleContext(Select_stmtContext.class,0);
		}
		public Update_stmtContext update_stmt() {
			return getRuleContext(Update_stmtContext.class,0);
		}
		public Update_stmt_limitedContext update_stmt_limited() {
			return getRuleContext(Update_stmt_limitedContext.class,0);
		}
		public Vacuum_stmtContext vacuum_stmt() {
			return getRuleContext(Vacuum_stmtContext.class,0);
		}
		public TerminalNode K_EXPLAIN() { return getToken(SQLGrammarParser.K_EXPLAIN, 0); }
		public TerminalNode K_QUERY() { return getToken(SQLGrammarParser.K_QUERY, 0); }
		public TerminalNode K_PLAN() { return getToken(SQLGrammarParser.K_PLAN, 0); }
		public Sql_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sql_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterSql_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitSql_stmt(this);
		}
	}

	public final Sql_stmtContext sql_stmt() throws RecognitionException {
		Sql_stmtContext _localctx = new Sql_stmtContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_sql_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(471);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_EXPLAIN) {
				{
				setState(466);
				match(K_EXPLAIN);
				setState(469);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_QUERY) {
					{
					setState(467);
					match(K_QUERY);
					setState(468);
					match(K_PLAN);
					}
				}

				}
			}

			setState(503);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				setState(473);
				alter_table_stmt();
				}
				break;
			case 2:
				{
				setState(474);
				analyze_stmt();
				}
				break;
			case 3:
				{
				setState(475);
				attach_stmt();
				}
				break;
			case 4:
				{
				setState(476);
				begin_stmt();
				}
				break;
			case 5:
				{
				setState(477);
				commit_stmt();
				}
				break;
			case 6:
				{
				setState(478);
				compound_select_stmt();
				}
				break;
			case 7:
				{
				setState(479);
				create_index_stmt();
				}
				break;
			case 8:
				{
				setState(480);
				create_table_stmt();
				}
				break;
			case 9:
				{
				setState(481);
				create_trigger_stmt();
				}
				break;
			case 10:
				{
				setState(482);
				create_view_stmt();
				}
				break;
			case 11:
				{
				setState(483);
				create_virtual_table_stmt();
				}
				break;
			case 12:
				{
				setState(484);
				delete_stmt();
				}
				break;
			case 13:
				{
				setState(485);
				delete_stmt_limited();
				}
				break;
			case 14:
				{
				setState(486);
				detach_stmt();
				}
				break;
			case 15:
				{
				setState(487);
				drop_index_stmt();
				}
				break;
			case 16:
				{
				setState(488);
				drop_table_stmt();
				}
				break;
			case 17:
				{
				setState(489);
				drop_trigger_stmt();
				}
				break;
			case 18:
				{
				setState(490);
				drop_view_stmt();
				}
				break;
			case 19:
				{
				setState(491);
				factored_select_stmt();
				}
				break;
			case 20:
				{
				setState(492);
				insert_stmt();
				}
				break;
			case 21:
				{
				setState(493);
				pragma_stmt();
				}
				break;
			case 22:
				{
				setState(494);
				reindex_stmt();
				}
				break;
			case 23:
				{
				setState(495);
				release_stmt();
				}
				break;
			case 24:
				{
				setState(496);
				rollback_stmt();
				}
				break;
			case 25:
				{
				setState(497);
				savepoint_stmt();
				}
				break;
			case 26:
				{
				setState(498);
				simple_select_stmt();
				}
				break;
			case 27:
				{
				setState(499);
				select_stmt();
				}
				break;
			case 28:
				{
				setState(500);
				update_stmt();
				}
				break;
			case 29:
				{
				setState(501);
				update_stmt_limited();
				}
				break;
			case 30:
				{
				setState(502);
				vacuum_stmt();
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

	public static class Alter_table_stmtContext extends ParserRuleContext {
		public TerminalNode K_ALTER() { return getToken(SQLGrammarParser.K_ALTER, 0); }
		public TerminalNode K_TABLE() { return getToken(SQLGrammarParser.K_TABLE, 0); }
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public TerminalNode K_RENAME() { return getToken(SQLGrammarParser.K_RENAME, 0); }
		public TerminalNode K_TO() { return getToken(SQLGrammarParser.K_TO, 0); }
		public New_table_nameContext new_table_name() {
			return getRuleContext(New_table_nameContext.class,0);
		}
		public TerminalNode K_ADD() { return getToken(SQLGrammarParser.K_ADD, 0); }
		public Column_defContext column_def() {
			return getRuleContext(Column_defContext.class,0);
		}
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public TerminalNode K_COLUMN() { return getToken(SQLGrammarParser.K_COLUMN, 0); }
		public Alter_table_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alter_table_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterAlter_table_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitAlter_table_stmt(this);
		}
	}

	public final Alter_table_stmtContext alter_table_stmt() throws RecognitionException {
		Alter_table_stmtContext _localctx = new Alter_table_stmtContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_alter_table_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(505);
			match(K_ALTER);
			setState(506);
			match(K_TABLE);
			setState(510);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(507);
				database_name();
				setState(508);
				match(DOT);
				}
				break;
			}
			setState(512);
			table_name();
			setState(521);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_RENAME:
				{
				setState(513);
				match(K_RENAME);
				setState(514);
				match(K_TO);
				setState(515);
				new_table_name();
				}
				break;
			case K_ADD:
				{
				setState(516);
				match(K_ADD);
				setState(518);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
				case 1:
					{
					setState(517);
					match(K_COLUMN);
					}
					break;
				}
				setState(520);
				column_def();
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

	public static class Analyze_stmtContext extends ParserRuleContext {
		public TerminalNode K_ANALYZE() { return getToken(SQLGrammarParser.K_ANALYZE, 0); }
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public Table_or_index_nameContext table_or_index_name() {
			return getRuleContext(Table_or_index_nameContext.class,0);
		}
		public Analyze_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_analyze_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterAnalyze_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitAnalyze_stmt(this);
		}
	}

	public final Analyze_stmtContext analyze_stmt() throws RecognitionException {
		Analyze_stmtContext _localctx = new Analyze_stmtContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_analyze_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(523);
			match(K_ANALYZE);
			setState(530);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				setState(524);
				database_name();
				}
				break;
			case 2:
				{
				setState(525);
				table_or_index_name();
				}
				break;
			case 3:
				{
				setState(526);
				database_name();
				setState(527);
				match(DOT);
				setState(528);
				table_or_index_name();
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

	public static class Attach_stmtContext extends ParserRuleContext {
		public TerminalNode K_ATTACH() { return getToken(SQLGrammarParser.K_ATTACH, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode K_AS() { return getToken(SQLGrammarParser.K_AS, 0); }
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public TerminalNode K_DATABASE() { return getToken(SQLGrammarParser.K_DATABASE, 0); }
		public Attach_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attach_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterAttach_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitAttach_stmt(this);
		}
	}

	public final Attach_stmtContext attach_stmt() throws RecognitionException {
		Attach_stmtContext _localctx = new Attach_stmtContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_attach_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(532);
			match(K_ATTACH);
			setState(534);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				{
				setState(533);
				match(K_DATABASE);
				}
				break;
			}
			setState(536);
			expr(0);
			setState(537);
			match(K_AS);
			setState(538);
			database_name();
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

	public static class Begin_stmtContext extends ParserRuleContext {
		public TerminalNode K_BEGIN() { return getToken(SQLGrammarParser.K_BEGIN, 0); }
		public TerminalNode K_TRANSACTION() { return getToken(SQLGrammarParser.K_TRANSACTION, 0); }
		public TerminalNode K_DEFERRED() { return getToken(SQLGrammarParser.K_DEFERRED, 0); }
		public TerminalNode K_IMMEDIATE() { return getToken(SQLGrammarParser.K_IMMEDIATE, 0); }
		public TerminalNode K_EXCLUSIVE() { return getToken(SQLGrammarParser.K_EXCLUSIVE, 0); }
		public Transaction_nameContext transaction_name() {
			return getRuleContext(Transaction_nameContext.class,0);
		}
		public Begin_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_begin_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterBegin_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitBegin_stmt(this);
		}
	}

	public final Begin_stmtContext begin_stmt() throws RecognitionException {
		Begin_stmtContext _localctx = new Begin_stmtContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_begin_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(540);
			match(K_BEGIN);
			setState(542);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 62)) & ~0x3f) == 0 && ((1L << (_la - 62)) & ((1L << (K_DEFERRED - 62)) | (1L << (K_EXCLUSIVE - 62)) | (1L << (K_IMMEDIATE - 62)))) != 0)) {
				{
				setState(541);
				_la = _input.LA(1);
				if ( !(((((_la - 62)) & ~0x3f) == 0 && ((1L << (_la - 62)) & ((1L << (K_DEFERRED - 62)) | (1L << (K_EXCLUSIVE - 62)) | (1L << (K_IMMEDIATE - 62)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(548);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_TRANSACTION) {
				{
				setState(544);
				match(K_TRANSACTION);
				setState(546);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
				case 1:
					{
					setState(545);
					transaction_name();
					}
					break;
				}
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

	public static class Commit_stmtContext extends ParserRuleContext {
		public TerminalNode K_COMMIT() { return getToken(SQLGrammarParser.K_COMMIT, 0); }
		public TerminalNode K_END() { return getToken(SQLGrammarParser.K_END, 0); }
		public TerminalNode K_TRANSACTION() { return getToken(SQLGrammarParser.K_TRANSACTION, 0); }
		public Transaction_nameContext transaction_name() {
			return getRuleContext(Transaction_nameContext.class,0);
		}
		public Commit_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commit_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterCommit_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitCommit_stmt(this);
		}
	}

	public final Commit_stmtContext commit_stmt() throws RecognitionException {
		Commit_stmtContext _localctx = new Commit_stmtContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_commit_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(550);
			_la = _input.LA(1);
			if ( !(_la==K_COMMIT || _la==K_END) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(555);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_TRANSACTION) {
				{
				setState(551);
				match(K_TRANSACTION);
				setState(553);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
				case 1:
					{
					setState(552);
					transaction_name();
					}
					break;
				}
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

	public static class Compound_select_stmtContext extends ParserRuleContext {
		public List<Select_coreContext> select_core() {
			return getRuleContexts(Select_coreContext.class);
		}
		public Select_coreContext select_core(int i) {
			return getRuleContext(Select_coreContext.class,i);
		}
		public TerminalNode K_WITH() { return getToken(SQLGrammarParser.K_WITH, 0); }
		public List<Common_table_expressionContext> common_table_expression() {
			return getRuleContexts(Common_table_expressionContext.class);
		}
		public Common_table_expressionContext common_table_expression(int i) {
			return getRuleContext(Common_table_expressionContext.class,i);
		}
		public TerminalNode K_ORDER() { return getToken(SQLGrammarParser.K_ORDER, 0); }
		public TerminalNode K_BY() { return getToken(SQLGrammarParser.K_BY, 0); }
		public List<Ordering_termContext> ordering_term() {
			return getRuleContexts(Ordering_termContext.class);
		}
		public Ordering_termContext ordering_term(int i) {
			return getRuleContext(Ordering_termContext.class,i);
		}
		public TerminalNode K_LIMIT() { return getToken(SQLGrammarParser.K_LIMIT, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> K_UNION() { return getTokens(SQLGrammarParser.K_UNION); }
		public TerminalNode K_UNION(int i) {
			return getToken(SQLGrammarParser.K_UNION, i);
		}
		public List<TerminalNode> K_INTERSECT() { return getTokens(SQLGrammarParser.K_INTERSECT); }
		public TerminalNode K_INTERSECT(int i) {
			return getToken(SQLGrammarParser.K_INTERSECT, i);
		}
		public List<TerminalNode> K_EXCEPT() { return getTokens(SQLGrammarParser.K_EXCEPT); }
		public TerminalNode K_EXCEPT(int i) {
			return getToken(SQLGrammarParser.K_EXCEPT, i);
		}
		public TerminalNode K_RECURSIVE() { return getToken(SQLGrammarParser.K_RECURSIVE, 0); }
		public TerminalNode K_OFFSET() { return getToken(SQLGrammarParser.K_OFFSET, 0); }
		public List<TerminalNode> K_ALL() { return getTokens(SQLGrammarParser.K_ALL); }
		public TerminalNode K_ALL(int i) {
			return getToken(SQLGrammarParser.K_ALL, i);
		}
		public Compound_select_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compound_select_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterCompound_select_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitCompound_select_stmt(this);
		}
	}

	public final Compound_select_stmtContext compound_select_stmt() throws RecognitionException {
		Compound_select_stmtContext _localctx = new Compound_select_stmtContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_compound_select_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(569);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WITH) {
				{
				setState(557);
				match(K_WITH);
				setState(559);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
				case 1:
					{
					setState(558);
					match(K_RECURSIVE);
					}
					break;
				}
				setState(561);
				common_table_expression();
				setState(566);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(562);
					match(COMMA);
					setState(563);
					common_table_expression();
					}
					}
					setState(568);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(571);
			select_core();
			setState(581); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(578);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case K_UNION:
					{
					setState(572);
					match(K_UNION);
					setState(574);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==K_ALL) {
						{
						setState(573);
						match(K_ALL);
						}
					}

					}
					break;
				case K_INTERSECT:
					{
					setState(576);
					match(K_INTERSECT);
					}
					break;
				case K_EXCEPT:
					{
					setState(577);
					match(K_EXCEPT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(580);
				select_core();
				}
				}
				setState(583); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==K_EXCEPT || _la==K_INTERSECT || _la==K_UNION );
			setState(595);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_ORDER) {
				{
				setState(585);
				match(K_ORDER);
				setState(586);
				match(K_BY);
				setState(587);
				ordering_term();
				setState(592);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(588);
					match(COMMA);
					setState(589);
					ordering_term();
					}
					}
					setState(594);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(603);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_LIMIT) {
				{
				setState(597);
				match(K_LIMIT);
				setState(598);
				expr(0);
				setState(601);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA || _la==K_OFFSET) {
					{
					setState(599);
					_la = _input.LA(1);
					if ( !(_la==COMMA || _la==K_OFFSET) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(600);
					expr(0);
					}
				}

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

	public static class Create_index_stmtContext extends ParserRuleContext {
		public TerminalNode K_CREATE() { return getToken(SQLGrammarParser.K_CREATE, 0); }
		public TerminalNode K_INDEX() { return getToken(SQLGrammarParser.K_INDEX, 0); }
		public Index_nameContext index_name() {
			return getRuleContext(Index_nameContext.class,0);
		}
		public TerminalNode K_ON() { return getToken(SQLGrammarParser.K_ON, 0); }
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public List<Indexed_columnContext> indexed_column() {
			return getRuleContexts(Indexed_columnContext.class);
		}
		public Indexed_columnContext indexed_column(int i) {
			return getRuleContext(Indexed_columnContext.class,i);
		}
		public TerminalNode K_UNIQUE() { return getToken(SQLGrammarParser.K_UNIQUE, 0); }
		public TerminalNode K_IF() { return getToken(SQLGrammarParser.K_IF, 0); }
		public TerminalNode K_NOT() { return getToken(SQLGrammarParser.K_NOT, 0); }
		public TerminalNode K_EXISTS() { return getToken(SQLGrammarParser.K_EXISTS, 0); }
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public TerminalNode K_WHERE() { return getToken(SQLGrammarParser.K_WHERE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Create_index_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_create_index_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterCreate_index_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitCreate_index_stmt(this);
		}
	}

	public final Create_index_stmtContext create_index_stmt() throws RecognitionException {
		Create_index_stmtContext _localctx = new Create_index_stmtContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_create_index_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(605);
			match(K_CREATE);
			setState(607);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_UNIQUE) {
				{
				setState(606);
				match(K_UNIQUE);
				}
			}

			setState(609);
			match(K_INDEX);
			setState(613);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				{
				setState(610);
				match(K_IF);
				setState(611);
				match(K_NOT);
				setState(612);
				match(K_EXISTS);
				}
				break;
			}
			setState(618);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				{
				setState(615);
				database_name();
				setState(616);
				match(DOT);
				}
				break;
			}
			setState(620);
			index_name();
			setState(621);
			match(K_ON);
			setState(622);
			table_name();
			setState(623);
			match(OPEN_PAR);
			setState(624);
			indexed_column();
			setState(629);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(625);
				match(COMMA);
				setState(626);
				indexed_column();
				}
				}
				setState(631);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(632);
			match(CLOSE_PAR);
			setState(635);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WHERE) {
				{
				setState(633);
				match(K_WHERE);
				setState(634);
				expr(0);
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

	public static class Create_table_stmtContext extends ParserRuleContext {
		public TerminalNode K_CREATE() { return getToken(SQLGrammarParser.K_CREATE, 0); }
		public TerminalNode K_TABLE() { return getToken(SQLGrammarParser.K_TABLE, 0); }
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public List<Column_defContext> column_def() {
			return getRuleContexts(Column_defContext.class);
		}
		public Column_defContext column_def(int i) {
			return getRuleContext(Column_defContext.class,i);
		}
		public TerminalNode K_AS() { return getToken(SQLGrammarParser.K_AS, 0); }
		public Select_stmtContext select_stmt() {
			return getRuleContext(Select_stmtContext.class,0);
		}
		public TerminalNode K_IF() { return getToken(SQLGrammarParser.K_IF, 0); }
		public TerminalNode K_NOT() { return getToken(SQLGrammarParser.K_NOT, 0); }
		public TerminalNode K_EXISTS() { return getToken(SQLGrammarParser.K_EXISTS, 0); }
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public TerminalNode K_TEMP() { return getToken(SQLGrammarParser.K_TEMP, 0); }
		public TerminalNode K_TEMPORARY() { return getToken(SQLGrammarParser.K_TEMPORARY, 0); }
		public List<Table_constraintContext> table_constraint() {
			return getRuleContexts(Table_constraintContext.class);
		}
		public Table_constraintContext table_constraint(int i) {
			return getRuleContext(Table_constraintContext.class,i);
		}
		public TerminalNode K_WITHOUT() { return getToken(SQLGrammarParser.K_WITHOUT, 0); }
		public TerminalNode IDENTIFIER() { return getToken(SQLGrammarParser.IDENTIFIER, 0); }
		public Create_table_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_create_table_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterCreate_table_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitCreate_table_stmt(this);
		}
	}

	public final Create_table_stmtContext create_table_stmt() throws RecognitionException {
		Create_table_stmtContext _localctx = new Create_table_stmtContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_create_table_stmt);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(637);
			match(K_CREATE);
			setState(639);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_TEMP || _la==K_TEMPORARY) {
				{
				setState(638);
				_la = _input.LA(1);
				if ( !(_la==K_TEMP || _la==K_TEMPORARY) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(641);
			match(K_TABLE);
			setState(645);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				setState(642);
				match(K_IF);
				setState(643);
				match(K_NOT);
				setState(644);
				match(K_EXISTS);
				}
				break;
			}
			setState(650);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				{
				setState(647);
				database_name();
				setState(648);
				match(DOT);
				}
				break;
			}
			setState(652);
			table_name();
			setState(676);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OPEN_PAR:
				{
				setState(653);
				match(OPEN_PAR);
				setState(654);
				column_def();
				setState(659);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(655);
						match(COMMA);
						setState(656);
						column_def();
						}
						} 
					}
					setState(661);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
				}
				setState(666);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(662);
					match(COMMA);
					setState(663);
					table_constraint();
					}
					}
					setState(668);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(669);
				match(CLOSE_PAR);
				setState(672);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_WITHOUT) {
					{
					setState(670);
					match(K_WITHOUT);
					setState(671);
					match(IDENTIFIER);
					}
				}

				}
				break;
			case K_AS:
				{
				setState(674);
				match(K_AS);
				setState(675);
				select_stmt();
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

	public static class Create_trigger_stmtContext extends ParserRuleContext {
		public TerminalNode K_CREATE() { return getToken(SQLGrammarParser.K_CREATE, 0); }
		public TerminalNode K_TRIGGER() { return getToken(SQLGrammarParser.K_TRIGGER, 0); }
		public Trigger_nameContext trigger_name() {
			return getRuleContext(Trigger_nameContext.class,0);
		}
		public TerminalNode K_ON() { return getToken(SQLGrammarParser.K_ON, 0); }
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public TerminalNode K_BEGIN() { return getToken(SQLGrammarParser.K_BEGIN, 0); }
		public TerminalNode K_END() { return getToken(SQLGrammarParser.K_END, 0); }
		public TerminalNode K_DELETE() { return getToken(SQLGrammarParser.K_DELETE, 0); }
		public TerminalNode K_INSERT() { return getToken(SQLGrammarParser.K_INSERT, 0); }
		public TerminalNode K_UPDATE() { return getToken(SQLGrammarParser.K_UPDATE, 0); }
		public TerminalNode K_IF() { return getToken(SQLGrammarParser.K_IF, 0); }
		public TerminalNode K_NOT() { return getToken(SQLGrammarParser.K_NOT, 0); }
		public TerminalNode K_EXISTS() { return getToken(SQLGrammarParser.K_EXISTS, 0); }
		public List<Database_nameContext> database_name() {
			return getRuleContexts(Database_nameContext.class);
		}
		public Database_nameContext database_name(int i) {
			return getRuleContext(Database_nameContext.class,i);
		}
		public TerminalNode K_BEFORE() { return getToken(SQLGrammarParser.K_BEFORE, 0); }
		public TerminalNode K_AFTER() { return getToken(SQLGrammarParser.K_AFTER, 0); }
		public TerminalNode K_INSTEAD() { return getToken(SQLGrammarParser.K_INSTEAD, 0); }
		public List<TerminalNode> K_OF() { return getTokens(SQLGrammarParser.K_OF); }
		public TerminalNode K_OF(int i) {
			return getToken(SQLGrammarParser.K_OF, i);
		}
		public TerminalNode K_FOR() { return getToken(SQLGrammarParser.K_FOR, 0); }
		public TerminalNode K_EACH() { return getToken(SQLGrammarParser.K_EACH, 0); }
		public TerminalNode K_ROW() { return getToken(SQLGrammarParser.K_ROW, 0); }
		public TerminalNode K_WHEN() { return getToken(SQLGrammarParser.K_WHEN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode K_TEMP() { return getToken(SQLGrammarParser.K_TEMP, 0); }
		public TerminalNode K_TEMPORARY() { return getToken(SQLGrammarParser.K_TEMPORARY, 0); }
		public List<Column_nameContext> column_name() {
			return getRuleContexts(Column_nameContext.class);
		}
		public Column_nameContext column_name(int i) {
			return getRuleContext(Column_nameContext.class,i);
		}
		public List<Update_stmtContext> update_stmt() {
			return getRuleContexts(Update_stmtContext.class);
		}
		public Update_stmtContext update_stmt(int i) {
			return getRuleContext(Update_stmtContext.class,i);
		}
		public List<Insert_stmtContext> insert_stmt() {
			return getRuleContexts(Insert_stmtContext.class);
		}
		public Insert_stmtContext insert_stmt(int i) {
			return getRuleContext(Insert_stmtContext.class,i);
		}
		public List<Delete_stmtContext> delete_stmt() {
			return getRuleContexts(Delete_stmtContext.class);
		}
		public Delete_stmtContext delete_stmt(int i) {
			return getRuleContext(Delete_stmtContext.class,i);
		}
		public List<Select_stmtContext> select_stmt() {
			return getRuleContexts(Select_stmtContext.class);
		}
		public Select_stmtContext select_stmt(int i) {
			return getRuleContext(Select_stmtContext.class,i);
		}
		public Create_trigger_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_create_trigger_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterCreate_trigger_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitCreate_trigger_stmt(this);
		}
	}

	public final Create_trigger_stmtContext create_trigger_stmt() throws RecognitionException {
		Create_trigger_stmtContext _localctx = new Create_trigger_stmtContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_create_trigger_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(678);
			match(K_CREATE);
			setState(680);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_TEMP || _la==K_TEMPORARY) {
				{
				setState(679);
				_la = _input.LA(1);
				if ( !(_la==K_TEMP || _la==K_TEMPORARY) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(682);
			match(K_TRIGGER);
			setState(686);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				{
				setState(683);
				match(K_IF);
				setState(684);
				match(K_NOT);
				setState(685);
				match(K_EXISTS);
				}
				break;
			}
			setState(691);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				{
				setState(688);
				database_name();
				setState(689);
				match(DOT);
				}
				break;
			}
			setState(693);
			trigger_name();
			setState(698);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_BEFORE:
				{
				setState(694);
				match(K_BEFORE);
				}
				break;
			case K_AFTER:
				{
				setState(695);
				match(K_AFTER);
				}
				break;
			case K_INSTEAD:
				{
				setState(696);
				match(K_INSTEAD);
				setState(697);
				match(K_OF);
				}
				break;
			case K_DELETE:
			case K_INSERT:
			case K_UPDATE:
				break;
			default:
				break;
			}
			setState(714);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_DELETE:
				{
				setState(700);
				match(K_DELETE);
				}
				break;
			case K_INSERT:
				{
				setState(701);
				match(K_INSERT);
				}
				break;
			case K_UPDATE:
				{
				setState(702);
				match(K_UPDATE);
				setState(712);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_OF) {
					{
					setState(703);
					match(K_OF);
					setState(704);
					column_name();
					setState(709);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(705);
						match(COMMA);
						setState(706);
						column_name();
						}
						}
						setState(711);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(716);
			match(K_ON);
			setState(720);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,60,_ctx) ) {
			case 1:
				{
				setState(717);
				database_name();
				setState(718);
				match(DOT);
				}
				break;
			}
			setState(722);
			table_name();
			setState(726);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_FOR) {
				{
				setState(723);
				match(K_FOR);
				setState(724);
				match(K_EACH);
				setState(725);
				match(K_ROW);
				}
			}

			setState(730);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WHEN) {
				{
				setState(728);
				match(K_WHEN);
				setState(729);
				expr(0);
				}
			}

			setState(732);
			match(K_BEGIN);
			setState(741); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(737);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
				case 1:
					{
					setState(733);
					update_stmt();
					}
					break;
				case 2:
					{
					setState(734);
					insert_stmt();
					}
					break;
				case 3:
					{
					setState(735);
					delete_stmt();
					}
					break;
				case 4:
					{
					setState(736);
					select_stmt();
					}
					break;
				}
				setState(739);
				match(SCOL);
				}
				}
				setState(743); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 63)) & ~0x3f) == 0 && ((1L << (_la - 63)) & ((1L << (K_DELETE - 63)) | (1L << (K_INSERT - 63)) | (1L << (K_REPLACE - 63)))) != 0) || ((((_la - 132)) & ~0x3f) == 0 && ((1L << (_la - 132)) & ((1L << (K_SELECT - 132)) | (1L << (K_UPDATE - 132)) | (1L << (K_VALUES - 132)) | (1L << (K_WITH - 132)))) != 0) );
			setState(745);
			match(K_END);
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

	public static class Create_view_stmtContext extends ParserRuleContext {
		public TerminalNode K_CREATE() { return getToken(SQLGrammarParser.K_CREATE, 0); }
		public TerminalNode K_VIEW() { return getToken(SQLGrammarParser.K_VIEW, 0); }
		public View_nameContext view_name() {
			return getRuleContext(View_nameContext.class,0);
		}
		public TerminalNode K_AS() { return getToken(SQLGrammarParser.K_AS, 0); }
		public Select_stmtContext select_stmt() {
			return getRuleContext(Select_stmtContext.class,0);
		}
		public TerminalNode K_IF() { return getToken(SQLGrammarParser.K_IF, 0); }
		public TerminalNode K_NOT() { return getToken(SQLGrammarParser.K_NOT, 0); }
		public TerminalNode K_EXISTS() { return getToken(SQLGrammarParser.K_EXISTS, 0); }
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public TerminalNode K_TEMP() { return getToken(SQLGrammarParser.K_TEMP, 0); }
		public TerminalNode K_TEMPORARY() { return getToken(SQLGrammarParser.K_TEMPORARY, 0); }
		public Create_view_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_create_view_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterCreate_view_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitCreate_view_stmt(this);
		}
	}

	public final Create_view_stmtContext create_view_stmt() throws RecognitionException {
		Create_view_stmtContext _localctx = new Create_view_stmtContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_create_view_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(747);
			match(K_CREATE);
			setState(749);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_TEMP || _la==K_TEMPORARY) {
				{
				setState(748);
				_la = _input.LA(1);
				if ( !(_la==K_TEMP || _la==K_TEMPORARY) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(751);
			match(K_VIEW);
			setState(755);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,66,_ctx) ) {
			case 1:
				{
				setState(752);
				match(K_IF);
				setState(753);
				match(K_NOT);
				setState(754);
				match(K_EXISTS);
				}
				break;
			}
			setState(760);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,67,_ctx) ) {
			case 1:
				{
				setState(757);
				database_name();
				setState(758);
				match(DOT);
				}
				break;
			}
			setState(762);
			view_name();
			setState(763);
			match(K_AS);
			setState(764);
			select_stmt();
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

	public static class Create_virtual_table_stmtContext extends ParserRuleContext {
		public TerminalNode K_CREATE() { return getToken(SQLGrammarParser.K_CREATE, 0); }
		public TerminalNode K_VIRTUAL() { return getToken(SQLGrammarParser.K_VIRTUAL, 0); }
		public TerminalNode K_TABLE() { return getToken(SQLGrammarParser.K_TABLE, 0); }
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public TerminalNode K_USING() { return getToken(SQLGrammarParser.K_USING, 0); }
		public Module_nameContext module_name() {
			return getRuleContext(Module_nameContext.class,0);
		}
		public TerminalNode K_IF() { return getToken(SQLGrammarParser.K_IF, 0); }
		public TerminalNode K_NOT() { return getToken(SQLGrammarParser.K_NOT, 0); }
		public TerminalNode K_EXISTS() { return getToken(SQLGrammarParser.K_EXISTS, 0); }
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public List<Module_argumentContext> module_argument() {
			return getRuleContexts(Module_argumentContext.class);
		}
		public Module_argumentContext module_argument(int i) {
			return getRuleContext(Module_argumentContext.class,i);
		}
		public Create_virtual_table_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_create_virtual_table_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterCreate_virtual_table_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitCreate_virtual_table_stmt(this);
		}
	}

	public final Create_virtual_table_stmtContext create_virtual_table_stmt() throws RecognitionException {
		Create_virtual_table_stmtContext _localctx = new Create_virtual_table_stmtContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_create_virtual_table_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(766);
			match(K_CREATE);
			setState(767);
			match(K_VIRTUAL);
			setState(768);
			match(K_TABLE);
			setState(772);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
			case 1:
				{
				setState(769);
				match(K_IF);
				setState(770);
				match(K_NOT);
				setState(771);
				match(K_EXISTS);
				}
				break;
			}
			setState(777);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,69,_ctx) ) {
			case 1:
				{
				setState(774);
				database_name();
				setState(775);
				match(DOT);
				}
				break;
			}
			setState(779);
			table_name();
			setState(780);
			match(K_USING);
			setState(781);
			module_name();
			setState(793);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPEN_PAR) {
				{
				setState(782);
				match(OPEN_PAR);
				setState(783);
				module_argument();
				setState(788);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(784);
					match(COMMA);
					setState(785);
					module_argument();
					}
					}
					setState(790);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(791);
				match(CLOSE_PAR);
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

	public static class Delete_stmtContext extends ParserRuleContext {
		public TerminalNode K_DELETE() { return getToken(SQLGrammarParser.K_DELETE, 0); }
		public TerminalNode K_FROM() { return getToken(SQLGrammarParser.K_FROM, 0); }
		public Qualified_table_nameContext qualified_table_name() {
			return getRuleContext(Qualified_table_nameContext.class,0);
		}
		public With_clauseContext with_clause() {
			return getRuleContext(With_clauseContext.class,0);
		}
		public TerminalNode K_WHERE() { return getToken(SQLGrammarParser.K_WHERE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Delete_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_delete_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterDelete_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitDelete_stmt(this);
		}
	}

	public final Delete_stmtContext delete_stmt() throws RecognitionException {
		Delete_stmtContext _localctx = new Delete_stmtContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_delete_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(796);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WITH) {
				{
				setState(795);
				with_clause();
				}
			}

			setState(798);
			match(K_DELETE);
			setState(799);
			match(K_FROM);
			setState(800);
			qualified_table_name();
			setState(803);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WHERE) {
				{
				setState(801);
				match(K_WHERE);
				setState(802);
				expr(0);
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

	public static class Delete_stmt_limitedContext extends ParserRuleContext {
		public TerminalNode K_DELETE() { return getToken(SQLGrammarParser.K_DELETE, 0); }
		public TerminalNode K_FROM() { return getToken(SQLGrammarParser.K_FROM, 0); }
		public Qualified_table_nameContext qualified_table_name() {
			return getRuleContext(Qualified_table_nameContext.class,0);
		}
		public With_clauseContext with_clause() {
			return getRuleContext(With_clauseContext.class,0);
		}
		public TerminalNode K_WHERE() { return getToken(SQLGrammarParser.K_WHERE, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode K_LIMIT() { return getToken(SQLGrammarParser.K_LIMIT, 0); }
		public TerminalNode K_ORDER() { return getToken(SQLGrammarParser.K_ORDER, 0); }
		public TerminalNode K_BY() { return getToken(SQLGrammarParser.K_BY, 0); }
		public List<Ordering_termContext> ordering_term() {
			return getRuleContexts(Ordering_termContext.class);
		}
		public Ordering_termContext ordering_term(int i) {
			return getRuleContext(Ordering_termContext.class,i);
		}
		public TerminalNode K_OFFSET() { return getToken(SQLGrammarParser.K_OFFSET, 0); }
		public Delete_stmt_limitedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_delete_stmt_limited; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterDelete_stmt_limited(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitDelete_stmt_limited(this);
		}
	}

	public final Delete_stmt_limitedContext delete_stmt_limited() throws RecognitionException {
		Delete_stmt_limitedContext _localctx = new Delete_stmt_limitedContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_delete_stmt_limited);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(806);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WITH) {
				{
				setState(805);
				with_clause();
				}
			}

			setState(808);
			match(K_DELETE);
			setState(809);
			match(K_FROM);
			setState(810);
			qualified_table_name();
			setState(813);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WHERE) {
				{
				setState(811);
				match(K_WHERE);
				setState(812);
				expr(0);
				}
			}

			setState(833);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_LIMIT || _la==K_ORDER) {
				{
				setState(825);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_ORDER) {
					{
					setState(815);
					match(K_ORDER);
					setState(816);
					match(K_BY);
					setState(817);
					ordering_term();
					setState(822);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(818);
						match(COMMA);
						setState(819);
						ordering_term();
						}
						}
						setState(824);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(827);
				match(K_LIMIT);
				setState(828);
				expr(0);
				setState(831);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA || _la==K_OFFSET) {
					{
					setState(829);
					_la = _input.LA(1);
					if ( !(_la==COMMA || _la==K_OFFSET) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(830);
					expr(0);
					}
				}

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

	public static class Detach_stmtContext extends ParserRuleContext {
		public TerminalNode K_DETACH() { return getToken(SQLGrammarParser.K_DETACH, 0); }
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public TerminalNode K_DATABASE() { return getToken(SQLGrammarParser.K_DATABASE, 0); }
		public Detach_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_detach_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterDetach_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitDetach_stmt(this);
		}
	}

	public final Detach_stmtContext detach_stmt() throws RecognitionException {
		Detach_stmtContext _localctx = new Detach_stmtContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_detach_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(835);
			match(K_DETACH);
			setState(837);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,80,_ctx) ) {
			case 1:
				{
				setState(836);
				match(K_DATABASE);
				}
				break;
			}
			setState(839);
			database_name();
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

	public static class Drop_index_stmtContext extends ParserRuleContext {
		public TerminalNode K_DROP() { return getToken(SQLGrammarParser.K_DROP, 0); }
		public TerminalNode K_INDEX() { return getToken(SQLGrammarParser.K_INDEX, 0); }
		public Index_nameContext index_name() {
			return getRuleContext(Index_nameContext.class,0);
		}
		public TerminalNode K_IF() { return getToken(SQLGrammarParser.K_IF, 0); }
		public TerminalNode K_EXISTS() { return getToken(SQLGrammarParser.K_EXISTS, 0); }
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public Drop_index_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_drop_index_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterDrop_index_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitDrop_index_stmt(this);
		}
	}

	public final Drop_index_stmtContext drop_index_stmt() throws RecognitionException {
		Drop_index_stmtContext _localctx = new Drop_index_stmtContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_drop_index_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(841);
			match(K_DROP);
			setState(842);
			match(K_INDEX);
			setState(845);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
			case 1:
				{
				setState(843);
				match(K_IF);
				setState(844);
				match(K_EXISTS);
				}
				break;
			}
			setState(850);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,82,_ctx) ) {
			case 1:
				{
				setState(847);
				database_name();
				setState(848);
				match(DOT);
				}
				break;
			}
			setState(852);
			index_name();
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

	public static class Drop_table_stmtContext extends ParserRuleContext {
		public TerminalNode K_DROP() { return getToken(SQLGrammarParser.K_DROP, 0); }
		public TerminalNode K_TABLE() { return getToken(SQLGrammarParser.K_TABLE, 0); }
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public TerminalNode K_IF() { return getToken(SQLGrammarParser.K_IF, 0); }
		public TerminalNode K_EXISTS() { return getToken(SQLGrammarParser.K_EXISTS, 0); }
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public Drop_table_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_drop_table_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterDrop_table_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitDrop_table_stmt(this);
		}
	}

	public final Drop_table_stmtContext drop_table_stmt() throws RecognitionException {
		Drop_table_stmtContext _localctx = new Drop_table_stmtContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_drop_table_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(854);
			match(K_DROP);
			setState(855);
			match(K_TABLE);
			setState(858);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,83,_ctx) ) {
			case 1:
				{
				setState(856);
				match(K_IF);
				setState(857);
				match(K_EXISTS);
				}
				break;
			}
			setState(863);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,84,_ctx) ) {
			case 1:
				{
				setState(860);
				database_name();
				setState(861);
				match(DOT);
				}
				break;
			}
			setState(865);
			table_name();
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

	public static class Drop_trigger_stmtContext extends ParserRuleContext {
		public TerminalNode K_DROP() { return getToken(SQLGrammarParser.K_DROP, 0); }
		public TerminalNode K_TRIGGER() { return getToken(SQLGrammarParser.K_TRIGGER, 0); }
		public Trigger_nameContext trigger_name() {
			return getRuleContext(Trigger_nameContext.class,0);
		}
		public TerminalNode K_IF() { return getToken(SQLGrammarParser.K_IF, 0); }
		public TerminalNode K_EXISTS() { return getToken(SQLGrammarParser.K_EXISTS, 0); }
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public Drop_trigger_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_drop_trigger_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterDrop_trigger_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitDrop_trigger_stmt(this);
		}
	}

	public final Drop_trigger_stmtContext drop_trigger_stmt() throws RecognitionException {
		Drop_trigger_stmtContext _localctx = new Drop_trigger_stmtContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_drop_trigger_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(867);
			match(K_DROP);
			setState(868);
			match(K_TRIGGER);
			setState(871);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				{
				setState(869);
				match(K_IF);
				setState(870);
				match(K_EXISTS);
				}
				break;
			}
			setState(876);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,86,_ctx) ) {
			case 1:
				{
				setState(873);
				database_name();
				setState(874);
				match(DOT);
				}
				break;
			}
			setState(878);
			trigger_name();
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

	public static class Drop_view_stmtContext extends ParserRuleContext {
		public TerminalNode K_DROP() { return getToken(SQLGrammarParser.K_DROP, 0); }
		public TerminalNode K_VIEW() { return getToken(SQLGrammarParser.K_VIEW, 0); }
		public View_nameContext view_name() {
			return getRuleContext(View_nameContext.class,0);
		}
		public TerminalNode K_IF() { return getToken(SQLGrammarParser.K_IF, 0); }
		public TerminalNode K_EXISTS() { return getToken(SQLGrammarParser.K_EXISTS, 0); }
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public Drop_view_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_drop_view_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterDrop_view_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitDrop_view_stmt(this);
		}
	}

	public final Drop_view_stmtContext drop_view_stmt() throws RecognitionException {
		Drop_view_stmtContext _localctx = new Drop_view_stmtContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_drop_view_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(880);
			match(K_DROP);
			setState(881);
			match(K_VIEW);
			setState(884);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,87,_ctx) ) {
			case 1:
				{
				setState(882);
				match(K_IF);
				setState(883);
				match(K_EXISTS);
				}
				break;
			}
			setState(889);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
			case 1:
				{
				setState(886);
				database_name();
				setState(887);
				match(DOT);
				}
				break;
			}
			setState(891);
			view_name();
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

	public static class Factored_select_stmtContext extends ParserRuleContext {
		public List<Select_coreContext> select_core() {
			return getRuleContexts(Select_coreContext.class);
		}
		public Select_coreContext select_core(int i) {
			return getRuleContext(Select_coreContext.class,i);
		}
		public TerminalNode K_WITH() { return getToken(SQLGrammarParser.K_WITH, 0); }
		public List<Common_table_expressionContext> common_table_expression() {
			return getRuleContexts(Common_table_expressionContext.class);
		}
		public Common_table_expressionContext common_table_expression(int i) {
			return getRuleContext(Common_table_expressionContext.class,i);
		}
		public List<Compound_operatorContext> compound_operator() {
			return getRuleContexts(Compound_operatorContext.class);
		}
		public Compound_operatorContext compound_operator(int i) {
			return getRuleContext(Compound_operatorContext.class,i);
		}
		public TerminalNode K_ORDER() { return getToken(SQLGrammarParser.K_ORDER, 0); }
		public TerminalNode K_BY() { return getToken(SQLGrammarParser.K_BY, 0); }
		public List<Ordering_termContext> ordering_term() {
			return getRuleContexts(Ordering_termContext.class);
		}
		public Ordering_termContext ordering_term(int i) {
			return getRuleContext(Ordering_termContext.class,i);
		}
		public TerminalNode K_LIMIT() { return getToken(SQLGrammarParser.K_LIMIT, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode K_RECURSIVE() { return getToken(SQLGrammarParser.K_RECURSIVE, 0); }
		public TerminalNode K_OFFSET() { return getToken(SQLGrammarParser.K_OFFSET, 0); }
		public Factored_select_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factored_select_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterFactored_select_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitFactored_select_stmt(this);
		}
	}

	public final Factored_select_stmtContext factored_select_stmt() throws RecognitionException {
		Factored_select_stmtContext _localctx = new Factored_select_stmtContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_factored_select_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(905);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WITH) {
				{
				setState(893);
				match(K_WITH);
				setState(895);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,89,_ctx) ) {
				case 1:
					{
					setState(894);
					match(K_RECURSIVE);
					}
					break;
				}
				setState(897);
				common_table_expression();
				setState(902);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(898);
					match(COMMA);
					setState(899);
					common_table_expression();
					}
					}
					setState(904);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(907);
			select_core();
			setState(913);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==K_EXCEPT || _la==K_INTERSECT || _la==K_UNION) {
				{
				{
				setState(908);
				compound_operator();
				setState(909);
				select_core();
				}
				}
				setState(915);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(926);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_ORDER) {
				{
				setState(916);
				match(K_ORDER);
				setState(917);
				match(K_BY);
				setState(918);
				ordering_term();
				setState(923);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(919);
					match(COMMA);
					setState(920);
					ordering_term();
					}
					}
					setState(925);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(934);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_LIMIT) {
				{
				setState(928);
				match(K_LIMIT);
				setState(929);
				expr(0);
				setState(932);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA || _la==K_OFFSET) {
					{
					setState(930);
					_la = _input.LA(1);
					if ( !(_la==COMMA || _la==K_OFFSET) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(931);
					expr(0);
					}
				}

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

	public static class Insert_stmtContext extends ParserRuleContext {
		public TerminalNode K_INTO() { return getToken(SQLGrammarParser.K_INTO, 0); }
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public TerminalNode K_INSERT() { return getToken(SQLGrammarParser.K_INSERT, 0); }
		public TerminalNode K_REPLACE() { return getToken(SQLGrammarParser.K_REPLACE, 0); }
		public TerminalNode K_OR() { return getToken(SQLGrammarParser.K_OR, 0); }
		public TerminalNode K_ROLLBACK() { return getToken(SQLGrammarParser.K_ROLLBACK, 0); }
		public TerminalNode K_ABORT() { return getToken(SQLGrammarParser.K_ABORT, 0); }
		public TerminalNode K_FAIL() { return getToken(SQLGrammarParser.K_FAIL, 0); }
		public TerminalNode K_IGNORE() { return getToken(SQLGrammarParser.K_IGNORE, 0); }
		public TerminalNode K_VALUES() { return getToken(SQLGrammarParser.K_VALUES, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public Select_stmtContext select_stmt() {
			return getRuleContext(Select_stmtContext.class,0);
		}
		public TerminalNode K_DEFAULT() { return getToken(SQLGrammarParser.K_DEFAULT, 0); }
		public With_clauseContext with_clause() {
			return getRuleContext(With_clauseContext.class,0);
		}
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public List<Column_nameContext> column_name() {
			return getRuleContexts(Column_nameContext.class);
		}
		public Column_nameContext column_name(int i) {
			return getRuleContext(Column_nameContext.class,i);
		}
		public Insert_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insert_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterInsert_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitInsert_stmt(this);
		}
	}

	public final Insert_stmtContext insert_stmt() throws RecognitionException {
		Insert_stmtContext _localctx = new Insert_stmtContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_insert_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(937);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WITH) {
				{
				setState(936);
				with_clause();
				}
			}

			setState(956);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,98,_ctx) ) {
			case 1:
				{
				setState(939);
				match(K_INSERT);
				}
				break;
			case 2:
				{
				setState(940);
				match(K_REPLACE);
				}
				break;
			case 3:
				{
				setState(941);
				match(K_INSERT);
				setState(942);
				match(K_OR);
				setState(943);
				match(K_REPLACE);
				}
				break;
			case 4:
				{
				setState(944);
				match(K_INSERT);
				setState(945);
				match(K_OR);
				setState(946);
				match(K_ROLLBACK);
				}
				break;
			case 5:
				{
				setState(947);
				match(K_INSERT);
				setState(948);
				match(K_OR);
				setState(949);
				match(K_ABORT);
				}
				break;
			case 6:
				{
				setState(950);
				match(K_INSERT);
				setState(951);
				match(K_OR);
				setState(952);
				match(K_FAIL);
				}
				break;
			case 7:
				{
				setState(953);
				match(K_INSERT);
				setState(954);
				match(K_OR);
				setState(955);
				match(K_IGNORE);
				}
				break;
			}
			setState(958);
			match(K_INTO);
			setState(962);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,99,_ctx) ) {
			case 1:
				{
				setState(959);
				database_name();
				setState(960);
				match(DOT);
				}
				break;
			}
			setState(964);
			table_name();
			setState(976);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPEN_PAR) {
				{
				setState(965);
				match(OPEN_PAR);
				setState(966);
				column_name();
				setState(971);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(967);
					match(COMMA);
					setState(968);
					column_name();
					}
					}
					setState(973);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(974);
				match(CLOSE_PAR);
				}
			}

			setState(1009);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,105,_ctx) ) {
			case 1:
				{
				setState(978);
				match(K_VALUES);
				setState(979);
				match(OPEN_PAR);
				setState(980);
				expr(0);
				setState(985);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(981);
					match(COMMA);
					setState(982);
					expr(0);
					}
					}
					setState(987);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(988);
				match(CLOSE_PAR);
				setState(1003);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(989);
					match(COMMA);
					setState(990);
					match(OPEN_PAR);
					setState(991);
					expr(0);
					setState(996);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(992);
						match(COMMA);
						setState(993);
						expr(0);
						}
						}
						setState(998);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(999);
					match(CLOSE_PAR);
					}
					}
					setState(1005);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 2:
				{
				setState(1006);
				select_stmt();
				}
				break;
			case 3:
				{
				setState(1007);
				match(K_DEFAULT);
				setState(1008);
				match(K_VALUES);
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

	public static class Pragma_stmtContext extends ParserRuleContext {
		public TerminalNode K_PRAGMA() { return getToken(SQLGrammarParser.K_PRAGMA, 0); }
		public Pragma_nameContext pragma_name() {
			return getRuleContext(Pragma_nameContext.class,0);
		}
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public Pragma_valueContext pragma_value() {
			return getRuleContext(Pragma_valueContext.class,0);
		}
		public Pragma_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pragma_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterPragma_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitPragma_stmt(this);
		}
	}

	public final Pragma_stmtContext pragma_stmt() throws RecognitionException {
		Pragma_stmtContext _localctx = new Pragma_stmtContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_pragma_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1011);
			match(K_PRAGMA);
			setState(1015);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
			case 1:
				{
				setState(1012);
				database_name();
				setState(1013);
				match(DOT);
				}
				break;
			}
			setState(1017);
			pragma_name();
			setState(1024);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ASSIGN:
				{
				setState(1018);
				match(ASSIGN);
				setState(1019);
				pragma_value();
				}
				break;
			case OPEN_PAR:
				{
				setState(1020);
				match(OPEN_PAR);
				setState(1021);
				pragma_value();
				setState(1022);
				match(CLOSE_PAR);
				}
				break;
			case EOF:
			case SCOL:
			case K_ALTER:
			case K_ANALYZE:
			case K_ATTACH:
			case K_BEGIN:
			case K_COMMIT:
			case K_CREATE:
			case K_DELETE:
			case K_DETACH:
			case K_DROP:
			case K_END:
			case K_EXPLAIN:
			case K_INSERT:
			case K_PRAGMA:
			case K_REINDEX:
			case K_RELEASE:
			case K_REPLACE:
			case K_ROLLBACK:
			case K_SAVEPOINT:
			case K_SELECT:
			case K_UPDATE:
			case K_VACUUM:
			case K_VALUES:
			case K_WITH:
			case UNEXPECTED_CHAR:
				break;
			default:
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

	public static class Reindex_stmtContext extends ParserRuleContext {
		public TerminalNode K_REINDEX() { return getToken(SQLGrammarParser.K_REINDEX, 0); }
		public Collation_nameContext collation_name() {
			return getRuleContext(Collation_nameContext.class,0);
		}
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public Index_nameContext index_name() {
			return getRuleContext(Index_nameContext.class,0);
		}
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public Reindex_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reindex_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterReindex_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitReindex_stmt(this);
		}
	}

	public final Reindex_stmtContext reindex_stmt() throws RecognitionException {
		Reindex_stmtContext _localctx = new Reindex_stmtContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_reindex_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1026);
			match(K_REINDEX);
			setState(1037);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,110,_ctx) ) {
			case 1:
				{
				setState(1027);
				collation_name();
				}
				break;
			case 2:
				{
				setState(1031);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,108,_ctx) ) {
				case 1:
					{
					setState(1028);
					database_name();
					setState(1029);
					match(DOT);
					}
					break;
				}
				setState(1035);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
				case 1:
					{
					setState(1033);
					table_name();
					}
					break;
				case 2:
					{
					setState(1034);
					index_name();
					}
					break;
				}
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

	public static class Release_stmtContext extends ParserRuleContext {
		public TerminalNode K_RELEASE() { return getToken(SQLGrammarParser.K_RELEASE, 0); }
		public Savepoint_nameContext savepoint_name() {
			return getRuleContext(Savepoint_nameContext.class,0);
		}
		public TerminalNode K_SAVEPOINT() { return getToken(SQLGrammarParser.K_SAVEPOINT, 0); }
		public Release_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_release_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterRelease_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitRelease_stmt(this);
		}
	}

	public final Release_stmtContext release_stmt() throws RecognitionException {
		Release_stmtContext _localctx = new Release_stmtContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_release_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1039);
			match(K_RELEASE);
			setState(1041);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,111,_ctx) ) {
			case 1:
				{
				setState(1040);
				match(K_SAVEPOINT);
				}
				break;
			}
			setState(1043);
			savepoint_name();
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

	public static class Rollback_stmtContext extends ParserRuleContext {
		public TerminalNode K_ROLLBACK() { return getToken(SQLGrammarParser.K_ROLLBACK, 0); }
		public TerminalNode K_TRANSACTION() { return getToken(SQLGrammarParser.K_TRANSACTION, 0); }
		public TerminalNode K_TO() { return getToken(SQLGrammarParser.K_TO, 0); }
		public Savepoint_nameContext savepoint_name() {
			return getRuleContext(Savepoint_nameContext.class,0);
		}
		public Transaction_nameContext transaction_name() {
			return getRuleContext(Transaction_nameContext.class,0);
		}
		public TerminalNode K_SAVEPOINT() { return getToken(SQLGrammarParser.K_SAVEPOINT, 0); }
		public Rollback_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rollback_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterRollback_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitRollback_stmt(this);
		}
	}

	public final Rollback_stmtContext rollback_stmt() throws RecognitionException {
		Rollback_stmtContext _localctx = new Rollback_stmtContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_rollback_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1045);
			match(K_ROLLBACK);
			setState(1050);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_TRANSACTION) {
				{
				setState(1046);
				match(K_TRANSACTION);
				setState(1048);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,112,_ctx) ) {
				case 1:
					{
					setState(1047);
					transaction_name();
					}
					break;
				}
				}
			}

			setState(1057);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_TO) {
				{
				setState(1052);
				match(K_TO);
				setState(1054);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,114,_ctx) ) {
				case 1:
					{
					setState(1053);
					match(K_SAVEPOINT);
					}
					break;
				}
				setState(1056);
				savepoint_name();
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

	public static class Savepoint_stmtContext extends ParserRuleContext {
		public TerminalNode K_SAVEPOINT() { return getToken(SQLGrammarParser.K_SAVEPOINT, 0); }
		public Savepoint_nameContext savepoint_name() {
			return getRuleContext(Savepoint_nameContext.class,0);
		}
		public Savepoint_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_savepoint_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterSavepoint_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitSavepoint_stmt(this);
		}
	}

	public final Savepoint_stmtContext savepoint_stmt() throws RecognitionException {
		Savepoint_stmtContext _localctx = new Savepoint_stmtContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_savepoint_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1059);
			match(K_SAVEPOINT);
			setState(1060);
			savepoint_name();
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

	public static class Simple_select_stmtContext extends ParserRuleContext {
		public Select_coreContext select_core() {
			return getRuleContext(Select_coreContext.class,0);
		}
		public TerminalNode K_WITH() { return getToken(SQLGrammarParser.K_WITH, 0); }
		public List<Common_table_expressionContext> common_table_expression() {
			return getRuleContexts(Common_table_expressionContext.class);
		}
		public Common_table_expressionContext common_table_expression(int i) {
			return getRuleContext(Common_table_expressionContext.class,i);
		}
		public TerminalNode K_ORDER() { return getToken(SQLGrammarParser.K_ORDER, 0); }
		public TerminalNode K_BY() { return getToken(SQLGrammarParser.K_BY, 0); }
		public List<Ordering_termContext> ordering_term() {
			return getRuleContexts(Ordering_termContext.class);
		}
		public Ordering_termContext ordering_term(int i) {
			return getRuleContext(Ordering_termContext.class,i);
		}
		public TerminalNode K_LIMIT() { return getToken(SQLGrammarParser.K_LIMIT, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode K_RECURSIVE() { return getToken(SQLGrammarParser.K_RECURSIVE, 0); }
		public TerminalNode K_OFFSET() { return getToken(SQLGrammarParser.K_OFFSET, 0); }
		public Simple_select_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_select_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterSimple_select_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitSimple_select_stmt(this);
		}
	}

	public final Simple_select_stmtContext simple_select_stmt() throws RecognitionException {
		Simple_select_stmtContext _localctx = new Simple_select_stmtContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_simple_select_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1074);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WITH) {
				{
				setState(1062);
				match(K_WITH);
				setState(1064);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,116,_ctx) ) {
				case 1:
					{
					setState(1063);
					match(K_RECURSIVE);
					}
					break;
				}
				setState(1066);
				common_table_expression();
				setState(1071);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1067);
					match(COMMA);
					setState(1068);
					common_table_expression();
					}
					}
					setState(1073);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1076);
			select_core();
			setState(1087);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_ORDER) {
				{
				setState(1077);
				match(K_ORDER);
				setState(1078);
				match(K_BY);
				setState(1079);
				ordering_term();
				setState(1084);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1080);
					match(COMMA);
					setState(1081);
					ordering_term();
					}
					}
					setState(1086);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1095);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_LIMIT) {
				{
				setState(1089);
				match(K_LIMIT);
				setState(1090);
				expr(0);
				setState(1093);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA || _la==K_OFFSET) {
					{
					setState(1091);
					_la = _input.LA(1);
					if ( !(_la==COMMA || _la==K_OFFSET) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1092);
					expr(0);
					}
				}

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

	public static class Select_stmtContext extends ParserRuleContext {
		public List<Select_or_valuesContext> select_or_values() {
			return getRuleContexts(Select_or_valuesContext.class);
		}
		public Select_or_valuesContext select_or_values(int i) {
			return getRuleContext(Select_or_valuesContext.class,i);
		}
		public TerminalNode K_WITH() { return getToken(SQLGrammarParser.K_WITH, 0); }
		public List<Common_table_expressionContext> common_table_expression() {
			return getRuleContexts(Common_table_expressionContext.class);
		}
		public Common_table_expressionContext common_table_expression(int i) {
			return getRuleContext(Common_table_expressionContext.class,i);
		}
		public List<Compound_operatorContext> compound_operator() {
			return getRuleContexts(Compound_operatorContext.class);
		}
		public Compound_operatorContext compound_operator(int i) {
			return getRuleContext(Compound_operatorContext.class,i);
		}
		public TerminalNode K_ORDER() { return getToken(SQLGrammarParser.K_ORDER, 0); }
		public TerminalNode K_BY() { return getToken(SQLGrammarParser.K_BY, 0); }
		public List<Ordering_termContext> ordering_term() {
			return getRuleContexts(Ordering_termContext.class);
		}
		public Ordering_termContext ordering_term(int i) {
			return getRuleContext(Ordering_termContext.class,i);
		}
		public TerminalNode K_LIMIT() { return getToken(SQLGrammarParser.K_LIMIT, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode K_RECURSIVE() { return getToken(SQLGrammarParser.K_RECURSIVE, 0); }
		public TerminalNode K_OFFSET() { return getToken(SQLGrammarParser.K_OFFSET, 0); }
		public Select_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_select_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterSelect_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitSelect_stmt(this);
		}
	}

	public final Select_stmtContext select_stmt() throws RecognitionException {
		Select_stmtContext _localctx = new Select_stmtContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_select_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1109);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WITH) {
				{
				setState(1097);
				match(K_WITH);
				setState(1099);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,123,_ctx) ) {
				case 1:
					{
					setState(1098);
					match(K_RECURSIVE);
					}
					break;
				}
				setState(1101);
				common_table_expression();
				setState(1106);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1102);
					match(COMMA);
					setState(1103);
					common_table_expression();
					}
					}
					setState(1108);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1111);
			select_or_values();
			setState(1117);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==K_EXCEPT || _la==K_INTERSECT || _la==K_UNION) {
				{
				{
				setState(1112);
				compound_operator();
				setState(1113);
				select_or_values();
				}
				}
				setState(1119);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1130);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_ORDER) {
				{
				setState(1120);
				match(K_ORDER);
				setState(1121);
				match(K_BY);
				setState(1122);
				ordering_term();
				setState(1127);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1123);
					match(COMMA);
					setState(1124);
					ordering_term();
					}
					}
					setState(1129);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(1138);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_LIMIT) {
				{
				setState(1132);
				match(K_LIMIT);
				setState(1133);
				expr(0);
				setState(1136);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA || _la==K_OFFSET) {
					{
					setState(1134);
					_la = _input.LA(1);
					if ( !(_la==COMMA || _la==K_OFFSET) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1135);
					expr(0);
					}
				}

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

	public static class Select_or_valuesContext extends ParserRuleContext {
		public TerminalNode K_SELECT() { return getToken(SQLGrammarParser.K_SELECT, 0); }
		public List<Result_columnContext> result_column() {
			return getRuleContexts(Result_columnContext.class);
		}
		public Result_columnContext result_column(int i) {
			return getRuleContext(Result_columnContext.class,i);
		}
		public TerminalNode K_FROM() { return getToken(SQLGrammarParser.K_FROM, 0); }
		public TerminalNode K_WHERE() { return getToken(SQLGrammarParser.K_WHERE, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode K_GROUP() { return getToken(SQLGrammarParser.K_GROUP, 0); }
		public TerminalNode K_BY() { return getToken(SQLGrammarParser.K_BY, 0); }
		public TerminalNode K_DISTINCT() { return getToken(SQLGrammarParser.K_DISTINCT, 0); }
		public TerminalNode K_ALL() { return getToken(SQLGrammarParser.K_ALL, 0); }
		public List<Table_or_subqueryContext> table_or_subquery() {
			return getRuleContexts(Table_or_subqueryContext.class);
		}
		public Table_or_subqueryContext table_or_subquery(int i) {
			return getRuleContext(Table_or_subqueryContext.class,i);
		}
		public Join_clauseContext join_clause() {
			return getRuleContext(Join_clauseContext.class,0);
		}
		public TerminalNode K_HAVING() { return getToken(SQLGrammarParser.K_HAVING, 0); }
		public TerminalNode K_VALUES() { return getToken(SQLGrammarParser.K_VALUES, 0); }
		public Select_or_valuesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_select_or_values; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterSelect_or_values(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitSelect_or_values(this);
		}
	}

	public final Select_or_valuesContext select_or_values() throws RecognitionException {
		Select_or_valuesContext _localctx = new Select_or_valuesContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_select_or_values);
		int _la;
		try {
			setState(1214);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_SELECT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1140);
				match(K_SELECT);
				setState(1142);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,131,_ctx) ) {
				case 1:
					{
					setState(1141);
					_la = _input.LA(1);
					if ( !(_la==K_ALL || _la==K_DISTINCT) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					break;
				}
				setState(1144);
				result_column();
				setState(1149);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1145);
					match(COMMA);
					setState(1146);
					result_column();
					}
					}
					setState(1151);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1164);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_FROM) {
					{
					setState(1152);
					match(K_FROM);
					setState(1162);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
					case 1:
						{
						setState(1153);
						table_or_subquery();
						setState(1158);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==COMMA) {
							{
							{
							setState(1154);
							match(COMMA);
							setState(1155);
							table_or_subquery();
							}
							}
							setState(1160);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					case 2:
						{
						setState(1161);
						join_clause();
						}
						break;
					}
					}
				}

				setState(1168);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_WHERE) {
					{
					setState(1166);
					match(K_WHERE);
					setState(1167);
					expr(0);
					}
				}

				setState(1184);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_GROUP) {
					{
					setState(1170);
					match(K_GROUP);
					setState(1171);
					match(K_BY);
					setState(1172);
					expr(0);
					setState(1177);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1173);
						match(COMMA);
						setState(1174);
						expr(0);
						}
						}
						setState(1179);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(1182);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==K_HAVING) {
						{
						setState(1180);
						match(K_HAVING);
						setState(1181);
						expr(0);
						}
					}

					}
				}

				}
				break;
			case K_VALUES:
				enterOuterAlt(_localctx, 2);
				{
				setState(1186);
				match(K_VALUES);
				setState(1187);
				match(OPEN_PAR);
				setState(1188);
				expr(0);
				setState(1193);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1189);
					match(COMMA);
					setState(1190);
					expr(0);
					}
					}
					setState(1195);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1196);
				match(CLOSE_PAR);
				setState(1211);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1197);
					match(COMMA);
					setState(1198);
					match(OPEN_PAR);
					setState(1199);
					expr(0);
					setState(1204);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1200);
						match(COMMA);
						setState(1201);
						expr(0);
						}
						}
						setState(1206);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(1207);
					match(CLOSE_PAR);
					}
					}
					setState(1213);
					_errHandler.sync(this);
					_la = _input.LA(1);
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

	public static class Update_stmtContext extends ParserRuleContext {
		public TerminalNode K_UPDATE() { return getToken(SQLGrammarParser.K_UPDATE, 0); }
		public Qualified_table_nameContext qualified_table_name() {
			return getRuleContext(Qualified_table_nameContext.class,0);
		}
		public TerminalNode K_SET() { return getToken(SQLGrammarParser.K_SET, 0); }
		public List<Column_nameContext> column_name() {
			return getRuleContexts(Column_nameContext.class);
		}
		public Column_nameContext column_name(int i) {
			return getRuleContext(Column_nameContext.class,i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public With_clauseContext with_clause() {
			return getRuleContext(With_clauseContext.class,0);
		}
		public TerminalNode K_OR() { return getToken(SQLGrammarParser.K_OR, 0); }
		public TerminalNode K_ROLLBACK() { return getToken(SQLGrammarParser.K_ROLLBACK, 0); }
		public TerminalNode K_ABORT() { return getToken(SQLGrammarParser.K_ABORT, 0); }
		public TerminalNode K_REPLACE() { return getToken(SQLGrammarParser.K_REPLACE, 0); }
		public TerminalNode K_FAIL() { return getToken(SQLGrammarParser.K_FAIL, 0); }
		public TerminalNode K_IGNORE() { return getToken(SQLGrammarParser.K_IGNORE, 0); }
		public TerminalNode K_WHERE() { return getToken(SQLGrammarParser.K_WHERE, 0); }
		public Update_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_update_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterUpdate_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitUpdate_stmt(this);
		}
	}

	public final Update_stmtContext update_stmt() throws RecognitionException {
		Update_stmtContext _localctx = new Update_stmtContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_update_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1217);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WITH) {
				{
				setState(1216);
				with_clause();
				}
			}

			setState(1219);
			match(K_UPDATE);
			setState(1230);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,145,_ctx) ) {
			case 1:
				{
				setState(1220);
				match(K_OR);
				setState(1221);
				match(K_ROLLBACK);
				}
				break;
			case 2:
				{
				setState(1222);
				match(K_OR);
				setState(1223);
				match(K_ABORT);
				}
				break;
			case 3:
				{
				setState(1224);
				match(K_OR);
				setState(1225);
				match(K_REPLACE);
				}
				break;
			case 4:
				{
				setState(1226);
				match(K_OR);
				setState(1227);
				match(K_FAIL);
				}
				break;
			case 5:
				{
				setState(1228);
				match(K_OR);
				setState(1229);
				match(K_IGNORE);
				}
				break;
			}
			setState(1232);
			qualified_table_name();
			setState(1233);
			match(K_SET);
			setState(1234);
			column_name();
			setState(1235);
			match(ASSIGN);
			setState(1236);
			expr(0);
			setState(1244);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1237);
				match(COMMA);
				setState(1238);
				column_name();
				setState(1239);
				match(ASSIGN);
				setState(1240);
				expr(0);
				}
				}
				setState(1246);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1249);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WHERE) {
				{
				setState(1247);
				match(K_WHERE);
				setState(1248);
				expr(0);
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

	public static class Update_stmt_limitedContext extends ParserRuleContext {
		public TerminalNode K_UPDATE() { return getToken(SQLGrammarParser.K_UPDATE, 0); }
		public Qualified_table_nameContext qualified_table_name() {
			return getRuleContext(Qualified_table_nameContext.class,0);
		}
		public TerminalNode K_SET() { return getToken(SQLGrammarParser.K_SET, 0); }
		public List<Column_nameContext> column_name() {
			return getRuleContexts(Column_nameContext.class);
		}
		public Column_nameContext column_name(int i) {
			return getRuleContext(Column_nameContext.class,i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public With_clauseContext with_clause() {
			return getRuleContext(With_clauseContext.class,0);
		}
		public TerminalNode K_OR() { return getToken(SQLGrammarParser.K_OR, 0); }
		public TerminalNode K_ROLLBACK() { return getToken(SQLGrammarParser.K_ROLLBACK, 0); }
		public TerminalNode K_ABORT() { return getToken(SQLGrammarParser.K_ABORT, 0); }
		public TerminalNode K_REPLACE() { return getToken(SQLGrammarParser.K_REPLACE, 0); }
		public TerminalNode K_FAIL() { return getToken(SQLGrammarParser.K_FAIL, 0); }
		public TerminalNode K_IGNORE() { return getToken(SQLGrammarParser.K_IGNORE, 0); }
		public TerminalNode K_WHERE() { return getToken(SQLGrammarParser.K_WHERE, 0); }
		public TerminalNode K_LIMIT() { return getToken(SQLGrammarParser.K_LIMIT, 0); }
		public TerminalNode K_ORDER() { return getToken(SQLGrammarParser.K_ORDER, 0); }
		public TerminalNode K_BY() { return getToken(SQLGrammarParser.K_BY, 0); }
		public List<Ordering_termContext> ordering_term() {
			return getRuleContexts(Ordering_termContext.class);
		}
		public Ordering_termContext ordering_term(int i) {
			return getRuleContext(Ordering_termContext.class,i);
		}
		public TerminalNode K_OFFSET() { return getToken(SQLGrammarParser.K_OFFSET, 0); }
		public Update_stmt_limitedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_update_stmt_limited; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterUpdate_stmt_limited(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitUpdate_stmt_limited(this);
		}
	}

	public final Update_stmt_limitedContext update_stmt_limited() throws RecognitionException {
		Update_stmt_limitedContext _localctx = new Update_stmt_limitedContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_update_stmt_limited);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1252);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WITH) {
				{
				setState(1251);
				with_clause();
				}
			}

			setState(1254);
			match(K_UPDATE);
			setState(1265);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,149,_ctx) ) {
			case 1:
				{
				setState(1255);
				match(K_OR);
				setState(1256);
				match(K_ROLLBACK);
				}
				break;
			case 2:
				{
				setState(1257);
				match(K_OR);
				setState(1258);
				match(K_ABORT);
				}
				break;
			case 3:
				{
				setState(1259);
				match(K_OR);
				setState(1260);
				match(K_REPLACE);
				}
				break;
			case 4:
				{
				setState(1261);
				match(K_OR);
				setState(1262);
				match(K_FAIL);
				}
				break;
			case 5:
				{
				setState(1263);
				match(K_OR);
				setState(1264);
				match(K_IGNORE);
				}
				break;
			}
			setState(1267);
			qualified_table_name();
			setState(1268);
			match(K_SET);
			setState(1269);
			column_name();
			setState(1270);
			match(ASSIGN);
			setState(1271);
			expr(0);
			setState(1279);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1272);
				match(COMMA);
				setState(1273);
				column_name();
				setState(1274);
				match(ASSIGN);
				setState(1275);
				expr(0);
				}
				}
				setState(1281);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1284);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WHERE) {
				{
				setState(1282);
				match(K_WHERE);
				setState(1283);
				expr(0);
				}
			}

			setState(1304);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_LIMIT || _la==K_ORDER) {
				{
				setState(1296);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_ORDER) {
					{
					setState(1286);
					match(K_ORDER);
					setState(1287);
					match(K_BY);
					setState(1288);
					ordering_term();
					setState(1293);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1289);
						match(COMMA);
						setState(1290);
						ordering_term();
						}
						}
						setState(1295);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(1298);
				match(K_LIMIT);
				setState(1299);
				expr(0);
				setState(1302);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA || _la==K_OFFSET) {
					{
					setState(1300);
					_la = _input.LA(1);
					if ( !(_la==COMMA || _la==K_OFFSET) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1301);
					expr(0);
					}
				}

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

	public static class Vacuum_stmtContext extends ParserRuleContext {
		public TerminalNode K_VACUUM() { return getToken(SQLGrammarParser.K_VACUUM, 0); }
		public Vacuum_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vacuum_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterVacuum_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitVacuum_stmt(this);
		}
	}

	public final Vacuum_stmtContext vacuum_stmt() throws RecognitionException {
		Vacuum_stmtContext _localctx = new Vacuum_stmtContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_vacuum_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1306);
			match(K_VACUUM);
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

	public static class Column_defContext extends ParserRuleContext {
		public Column_nameContext column_name() {
			return getRuleContext(Column_nameContext.class,0);
		}
		public Type_nameContext type_name() {
			return getRuleContext(Type_nameContext.class,0);
		}
		public List<Column_constraintContext> column_constraint() {
			return getRuleContexts(Column_constraintContext.class);
		}
		public Column_constraintContext column_constraint(int i) {
			return getRuleContext(Column_constraintContext.class,i);
		}
		public Column_defContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column_def; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterColumn_def(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitColumn_def(this);
		}
	}

	public final Column_defContext column_def() throws RecognitionException {
		Column_defContext _localctx = new Column_defContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_column_def);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1308);
			column_name();
			setState(1310);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,156,_ctx) ) {
			case 1:
				{
				setState(1309);
				type_name();
				}
				break;
			}
			setState(1315);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << K_CHECK) | (1L << K_COLLATE) | (1L << K_CONSTRAINT) | (1L << K_DEFAULT))) != 0) || ((((_la - 106)) & ~0x3f) == 0 && ((1L << (_la - 106)) & ((1L << (K_NOT - 106)) | (1L << (K_NULL - 106)) | (1L << (K_PRIMARY - 106)) | (1L << (K_REFERENCES - 106)) | (1L << (K_UNIQUE - 106)))) != 0)) {
				{
				{
				setState(1312);
				column_constraint();
				}
				}
				setState(1317);
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

	public static class Type_nameContext extends ParserRuleContext {
		public List<NameContext> name() {
			return getRuleContexts(NameContext.class);
		}
		public NameContext name(int i) {
			return getRuleContext(NameContext.class,i);
		}
		public List<Signed_numberContext> signed_number() {
			return getRuleContexts(Signed_numberContext.class);
		}
		public Signed_numberContext signed_number(int i) {
			return getRuleContext(Signed_numberContext.class,i);
		}
		public Type_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterType_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitType_name(this);
		}
	}

	public final Type_nameContext type_name() throws RecognitionException {
		Type_nameContext _localctx = new Type_nameContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_type_name);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1319); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1318);
					name();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1321); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,158,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(1333);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,159,_ctx) ) {
			case 1:
				{
				setState(1323);
				match(OPEN_PAR);
				setState(1324);
				signed_number();
				setState(1325);
				match(CLOSE_PAR);
				}
				break;
			case 2:
				{
				setState(1327);
				match(OPEN_PAR);
				setState(1328);
				signed_number();
				setState(1329);
				match(COMMA);
				setState(1330);
				signed_number();
				setState(1331);
				match(CLOSE_PAR);
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

	public static class Column_constraintContext extends ParserRuleContext {
		public TerminalNode K_PRIMARY() { return getToken(SQLGrammarParser.K_PRIMARY, 0); }
		public TerminalNode K_KEY() { return getToken(SQLGrammarParser.K_KEY, 0); }
		public Conflict_clauseContext conflict_clause() {
			return getRuleContext(Conflict_clauseContext.class,0);
		}
		public TerminalNode K_NULL() { return getToken(SQLGrammarParser.K_NULL, 0); }
		public TerminalNode K_UNIQUE() { return getToken(SQLGrammarParser.K_UNIQUE, 0); }
		public TerminalNode K_CHECK() { return getToken(SQLGrammarParser.K_CHECK, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode K_DEFAULT() { return getToken(SQLGrammarParser.K_DEFAULT, 0); }
		public TerminalNode K_COLLATE() { return getToken(SQLGrammarParser.K_COLLATE, 0); }
		public Collation_nameContext collation_name() {
			return getRuleContext(Collation_nameContext.class,0);
		}
		public Foreign_key_clauseContext foreign_key_clause() {
			return getRuleContext(Foreign_key_clauseContext.class,0);
		}
		public TerminalNode K_CONSTRAINT() { return getToken(SQLGrammarParser.K_CONSTRAINT, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public Signed_numberContext signed_number() {
			return getRuleContext(Signed_numberContext.class,0);
		}
		public Literal_valueContext literal_value() {
			return getRuleContext(Literal_valueContext.class,0);
		}
		public TerminalNode K_AUTOINCREMENT() { return getToken(SQLGrammarParser.K_AUTOINCREMENT, 0); }
		public TerminalNode K_NOT() { return getToken(SQLGrammarParser.K_NOT, 0); }
		public TerminalNode K_ASC() { return getToken(SQLGrammarParser.K_ASC, 0); }
		public TerminalNode K_DESC() { return getToken(SQLGrammarParser.K_DESC, 0); }
		public Column_constraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column_constraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterColumn_constraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitColumn_constraint(this);
		}
	}

	public final Column_constraintContext column_constraint() throws RecognitionException {
		Column_constraintContext _localctx = new Column_constraintContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_column_constraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1337);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_CONSTRAINT) {
				{
				setState(1335);
				match(K_CONSTRAINT);
				setState(1336);
				name();
				}
			}

			setState(1372);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_PRIMARY:
				{
				setState(1339);
				match(K_PRIMARY);
				setState(1340);
				match(K_KEY);
				setState(1342);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_ASC || _la==K_DESC) {
					{
					setState(1341);
					_la = _input.LA(1);
					if ( !(_la==K_ASC || _la==K_DESC) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
				}

				setState(1344);
				conflict_clause();
				setState(1346);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_AUTOINCREMENT) {
					{
					setState(1345);
					match(K_AUTOINCREMENT);
					}
				}

				}
				break;
			case K_NOT:
			case K_NULL:
				{
				setState(1349);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_NOT) {
					{
					setState(1348);
					match(K_NOT);
					}
				}

				setState(1351);
				match(K_NULL);
				setState(1352);
				conflict_clause();
				}
				break;
			case K_UNIQUE:
				{
				setState(1353);
				match(K_UNIQUE);
				setState(1354);
				conflict_clause();
				}
				break;
			case K_CHECK:
				{
				setState(1355);
				match(K_CHECK);
				setState(1356);
				match(OPEN_PAR);
				setState(1357);
				expr(0);
				setState(1358);
				match(CLOSE_PAR);
				}
				break;
			case K_DEFAULT:
				{
				setState(1360);
				match(K_DEFAULT);
				setState(1367);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,164,_ctx) ) {
				case 1:
					{
					setState(1361);
					signed_number();
					}
					break;
				case 2:
					{
					setState(1362);
					literal_value();
					}
					break;
				case 3:
					{
					setState(1363);
					match(OPEN_PAR);
					setState(1364);
					expr(0);
					setState(1365);
					match(CLOSE_PAR);
					}
					break;
				}
				}
				break;
			case K_COLLATE:
				{
				setState(1369);
				match(K_COLLATE);
				setState(1370);
				collation_name();
				}
				break;
			case K_REFERENCES:
				{
				setState(1371);
				foreign_key_clause();
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

	public static class Conflict_clauseContext extends ParserRuleContext {
		public TerminalNode K_ON() { return getToken(SQLGrammarParser.K_ON, 0); }
		public TerminalNode K_CONFLICT() { return getToken(SQLGrammarParser.K_CONFLICT, 0); }
		public TerminalNode K_ROLLBACK() { return getToken(SQLGrammarParser.K_ROLLBACK, 0); }
		public TerminalNode K_ABORT() { return getToken(SQLGrammarParser.K_ABORT, 0); }
		public TerminalNode K_FAIL() { return getToken(SQLGrammarParser.K_FAIL, 0); }
		public TerminalNode K_IGNORE() { return getToken(SQLGrammarParser.K_IGNORE, 0); }
		public TerminalNode K_REPLACE() { return getToken(SQLGrammarParser.K_REPLACE, 0); }
		public Conflict_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conflict_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterConflict_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitConflict_clause(this);
		}
	}

	public final Conflict_clauseContext conflict_clause() throws RecognitionException {
		Conflict_clauseContext _localctx = new Conflict_clauseContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_conflict_clause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1377);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_ON) {
				{
				setState(1374);
				match(K_ON);
				setState(1375);
				match(K_CONFLICT);
				setState(1376);
				_la = _input.LA(1);
				if ( !(_la==K_ABORT || ((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & ((1L << (K_FAIL - 76)) | (1L << (K_IGNORE - 76)) | (1L << (K_REPLACE - 76)) | (1L << (K_ROLLBACK - 76)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
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

	public static class ExprContext extends ParserRuleContext {
		public Literal_valueContext literal_value() {
			return getRuleContext(Literal_valueContext.class,0);
		}
		public TerminalNode BIND_PARAMETER() { return getToken(SQLGrammarParser.BIND_PARAMETER, 0); }
		public Column_nameContext column_name() {
			return getRuleContext(Column_nameContext.class,0);
		}
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public Unary_operatorContext unary_operator() {
			return getRuleContext(Unary_operatorContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public Function_nameContext function_name() {
			return getRuleContext(Function_nameContext.class,0);
		}
		public TerminalNode K_DISTINCT() { return getToken(SQLGrammarParser.K_DISTINCT, 0); }
		public TerminalNode K_CAST() { return getToken(SQLGrammarParser.K_CAST, 0); }
		public TerminalNode K_AS() { return getToken(SQLGrammarParser.K_AS, 0); }
		public Type_nameContext type_name() {
			return getRuleContext(Type_nameContext.class,0);
		}
		public Select_stmtContext select_stmt() {
			return getRuleContext(Select_stmtContext.class,0);
		}
		public TerminalNode K_EXISTS() { return getToken(SQLGrammarParser.K_EXISTS, 0); }
		public TerminalNode K_NOT() { return getToken(SQLGrammarParser.K_NOT, 0); }
		public TerminalNode K_CASE() { return getToken(SQLGrammarParser.K_CASE, 0); }
		public TerminalNode K_END() { return getToken(SQLGrammarParser.K_END, 0); }
		public List<TerminalNode> K_WHEN() { return getTokens(SQLGrammarParser.K_WHEN); }
		public TerminalNode K_WHEN(int i) {
			return getToken(SQLGrammarParser.K_WHEN, i);
		}
		public List<TerminalNode> K_THEN() { return getTokens(SQLGrammarParser.K_THEN); }
		public TerminalNode K_THEN(int i) {
			return getToken(SQLGrammarParser.K_THEN, i);
		}
		public TerminalNode K_ELSE() { return getToken(SQLGrammarParser.K_ELSE, 0); }
		public Raise_functionContext raise_function() {
			return getRuleContext(Raise_functionContext.class,0);
		}
		public TerminalNode K_IS() { return getToken(SQLGrammarParser.K_IS, 0); }
		public TerminalNode K_IN() { return getToken(SQLGrammarParser.K_IN, 0); }
		public TerminalNode K_LIKE() { return getToken(SQLGrammarParser.K_LIKE, 0); }
		public TerminalNode K_GLOB() { return getToken(SQLGrammarParser.K_GLOB, 0); }
		public TerminalNode K_MATCH() { return getToken(SQLGrammarParser.K_MATCH, 0); }
		public TerminalNode K_REGEXP() { return getToken(SQLGrammarParser.K_REGEXP, 0); }
		public TerminalNode K_AND() { return getToken(SQLGrammarParser.K_AND, 0); }
		public TerminalNode K_OR() { return getToken(SQLGrammarParser.K_OR, 0); }
		public TerminalNode K_BETWEEN() { return getToken(SQLGrammarParser.K_BETWEEN, 0); }
		public TerminalNode K_COLLATE() { return getToken(SQLGrammarParser.K_COLLATE, 0); }
		public Collation_nameContext collation_name() {
			return getRuleContext(Collation_nameContext.class,0);
		}
		public TerminalNode K_ESCAPE() { return getToken(SQLGrammarParser.K_ESCAPE, 0); }
		public TerminalNode K_ISNULL() { return getToken(SQLGrammarParser.K_ISNULL, 0); }
		public TerminalNode K_NOTNULL() { return getToken(SQLGrammarParser.K_NOTNULL, 0); }
		public TerminalNode K_NULL() { return getToken(SQLGrammarParser.K_NULL, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 144;
		enterRecursionRule(_localctx, 144, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1455);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,177,_ctx) ) {
			case 1:
				{
				setState(1380);
				literal_value();
				}
				break;
			case 2:
				{
				setState(1381);
				match(BIND_PARAMETER);
				}
				break;
			case 3:
				{
				setState(1390);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,168,_ctx) ) {
				case 1:
					{
					setState(1385);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,167,_ctx) ) {
					case 1:
						{
						setState(1382);
						database_name();
						setState(1383);
						match(DOT);
						}
						break;
					}
					setState(1387);
					table_name();
					setState(1388);
					match(DOT);
					}
					break;
				}
				setState(1392);
				column_name();
				}
				break;
			case 4:
				{
				setState(1393);
				unary_operator();
				setState(1394);
				expr(21);
				}
				break;
			case 5:
				{
				setState(1396);
				function_name();
				setState(1397);
				match(OPEN_PAR);
				setState(1410);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case OPEN_PAR:
				case PLUS:
				case MINUS:
				case TILDE:
				case K_ABORT:
				case K_ACTION:
				case K_ADD:
				case K_AFTER:
				case K_ALL:
				case K_ALTER:
				case K_ANALYZE:
				case K_AND:
				case K_AS:
				case K_ASC:
				case K_ATTACH:
				case K_AUTOINCREMENT:
				case K_BEFORE:
				case K_BEGIN:
				case K_BETWEEN:
				case K_BY:
				case K_CASCADE:
				case K_CASE:
				case K_CAST:
				case K_CHECK:
				case K_COLLATE:
				case K_COLUMN:
				case K_COMMIT:
				case K_CONFLICT:
				case K_CONSTRAINT:
				case K_CREATE:
				case K_CROSS:
				case K_CURRENT_DATE:
				case K_CURRENT_TIME:
				case K_CURRENT_TIMESTAMP:
				case K_DATABASE:
				case K_DEFAULT:
				case K_DEFERRABLE:
				case K_DEFERRED:
				case K_DELETE:
				case K_DESC:
				case K_DETACH:
				case K_DISTINCT:
				case K_DROP:
				case K_EACH:
				case K_ELSE:
				case K_END:
				case K_ESCAPE:
				case K_EXCEPT:
				case K_EXCLUSIVE:
				case K_EXISTS:
				case K_EXPLAIN:
				case K_FAIL:
				case K_FOR:
				case K_FOREIGN:
				case K_FROM:
				case K_FULL:
				case K_GLOB:
				case K_GROUP:
				case K_HAVING:
				case K_IF:
				case K_IGNORE:
				case K_IMMEDIATE:
				case K_IN:
				case K_INDEX:
				case K_INDEXED:
				case K_INITIALLY:
				case K_INNER:
				case K_INSERT:
				case K_INSTEAD:
				case K_INTERSECT:
				case K_INTO:
				case K_IS:
				case K_ISNULL:
				case K_JOIN:
				case K_KEY:
				case K_LEFT:
				case K_LIKE:
				case K_LIMIT:
				case K_MATCH:
				case K_NATURAL:
				case K_NO:
				case K_NOT:
				case K_NOTNULL:
				case K_NULL:
				case K_OF:
				case K_OFFSET:
				case K_ON:
				case K_OR:
				case K_ORDER:
				case K_OUTER:
				case K_PLAN:
				case K_PRAGMA:
				case K_PRIMARY:
				case K_QUERY:
				case K_RAISE:
				case K_RECURSIVE:
				case K_REFERENCES:
				case K_REGEXP:
				case K_REINDEX:
				case K_RELEASE:
				case K_RENAME:
				case K_REPLACE:
				case K_RESTRICT:
				case K_RIGHT:
				case K_ROLLBACK:
				case K_ROW:
				case K_SAVEPOINT:
				case K_SELECT:
				case K_SET:
				case K_TABLE:
				case K_TEMP:
				case K_TEMPORARY:
				case K_THEN:
				case K_TO:
				case K_TRANSACTION:
				case K_TRIGGER:
				case K_UNION:
				case K_UNIQUE:
				case K_UPDATE:
				case K_USING:
				case K_VACUUM:
				case K_VALUES:
				case K_VIEW:
				case K_VIRTUAL:
				case K_WHEN:
				case K_WHERE:
				case K_WITH:
				case K_WITHOUT:
				case IDENTIFIER:
				case NUMERIC_LITERAL:
				case BIND_PARAMETER:
				case STRING_LITERAL:
				case BLOB_LITERAL:
					{
					setState(1399);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,169,_ctx) ) {
					case 1:
						{
						setState(1398);
						match(K_DISTINCT);
						}
						break;
					}
					setState(1401);
					expr(0);
					setState(1406);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1402);
						match(COMMA);
						setState(1403);
						expr(0);
						}
						}
						setState(1408);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
					break;
				case STAR:
					{
					setState(1409);
					match(STAR);
					}
					break;
				case CLOSE_PAR:
					break;
				default:
					break;
				}
				setState(1412);
				match(CLOSE_PAR);
				}
				break;
			case 6:
				{
				setState(1414);
				match(OPEN_PAR);
				setState(1415);
				expr(0);
				setState(1416);
				match(CLOSE_PAR);
				}
				break;
			case 7:
				{
				setState(1418);
				match(K_CAST);
				setState(1419);
				match(OPEN_PAR);
				setState(1420);
				expr(0);
				setState(1421);
				match(K_AS);
				setState(1422);
				type_name();
				setState(1423);
				match(CLOSE_PAR);
				}
				break;
			case 8:
				{
				setState(1429);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_EXISTS || _la==K_NOT) {
					{
					setState(1426);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==K_NOT) {
						{
						setState(1425);
						match(K_NOT);
						}
					}

					setState(1428);
					match(K_EXISTS);
					}
				}

				setState(1431);
				match(OPEN_PAR);
				setState(1432);
				select_stmt();
				setState(1433);
				match(CLOSE_PAR);
				}
				break;
			case 9:
				{
				setState(1435);
				match(K_CASE);
				setState(1437);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,174,_ctx) ) {
				case 1:
					{
					setState(1436);
					expr(0);
					}
					break;
				}
				setState(1444); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(1439);
					match(K_WHEN);
					setState(1440);
					expr(0);
					setState(1441);
					match(K_THEN);
					setState(1442);
					expr(0);
					}
					}
					setState(1446); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==K_WHEN );
				setState(1450);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_ELSE) {
					{
					setState(1448);
					match(K_ELSE);
					setState(1449);
					expr(0);
					}
				}

				setState(1452);
				match(K_END);
				}
				break;
			case 10:
				{
				setState(1454);
				raise_function();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1557);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,190,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(1555);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,189,_ctx) ) {
					case 1:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(1457);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(1458);
						match(PIPE2);
						setState(1459);
						expr(21);
						}
						break;
					case 2:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(1460);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						setState(1461);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STAR) | (1L << DIV) | (1L << MOD))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1462);
						expr(20);
						}
						break;
					case 3:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(1463);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(1464);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1465);
						expr(19);
						}
						break;
					case 4:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(1466);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(1467);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT2) | (1L << GT2) | (1L << AMP) | (1L << PIPE))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1468);
						expr(18);
						}
						break;
					case 5:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(1469);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(1470);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT) | (1L << LT_EQ) | (1L << GT) | (1L << GT_EQ))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1471);
						expr(17);
						}
						break;
					case 6:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(1472);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(1485);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,178,_ctx) ) {
						case 1:
							{
							setState(1473);
							match(ASSIGN);
							}
							break;
						case 2:
							{
							setState(1474);
							match(EQ);
							}
							break;
						case 3:
							{
							setState(1475);
							match(NOT_EQ1);
							}
							break;
						case 4:
							{
							setState(1476);
							match(NOT_EQ2);
							}
							break;
						case 5:
							{
							setState(1477);
							match(K_IS);
							}
							break;
						case 6:
							{
							setState(1478);
							match(K_IS);
							setState(1479);
							match(K_NOT);
							}
							break;
						case 7:
							{
							setState(1480);
							match(K_IN);
							}
							break;
						case 8:
							{
							setState(1481);
							match(K_LIKE);
							}
							break;
						case 9:
							{
							setState(1482);
							match(K_GLOB);
							}
							break;
						case 10:
							{
							setState(1483);
							match(K_MATCH);
							}
							break;
						case 11:
							{
							setState(1484);
							match(K_REGEXP);
							}
							break;
						}
						setState(1487);
						expr(16);
						}
						break;
					case 7:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(1488);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(1489);
						match(K_AND);
						setState(1490);
						expr(15);
						}
						break;
					case 8:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(1491);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(1492);
						match(K_OR);
						setState(1493);
						expr(14);
						}
						break;
					case 9:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(1494);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(1495);
						match(K_IS);
						setState(1497);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,179,_ctx) ) {
						case 1:
							{
							setState(1496);
							match(K_NOT);
							}
							break;
						}
						setState(1499);
						expr(7);
						}
						break;
					case 10:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(1500);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(1502);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==K_NOT) {
							{
							setState(1501);
							match(K_NOT);
							}
						}

						setState(1504);
						match(K_BETWEEN);
						setState(1505);
						expr(0);
						setState(1506);
						match(K_AND);
						setState(1507);
						expr(6);
						}
						break;
					case 11:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(1509);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(1510);
						match(K_COLLATE);
						setState(1511);
						collation_name();
						}
						break;
					case 12:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(1512);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(1514);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==K_NOT) {
							{
							setState(1513);
							match(K_NOT);
							}
						}

						setState(1516);
						_la = _input.LA(1);
						if ( !(((((_la - 81)) & ~0x3f) == 0 && ((1L << (_la - 81)) & ((1L << (K_GLOB - 81)) | (1L << (K_LIKE - 81)) | (1L << (K_MATCH - 81)) | (1L << (K_REGEXP - 81)))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(1517);
						expr(0);
						setState(1520);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,182,_ctx) ) {
						case 1:
							{
							setState(1518);
							match(K_ESCAPE);
							setState(1519);
							expr(0);
							}
							break;
						}
						}
						break;
					case 13:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(1522);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(1527);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case K_ISNULL:
							{
							setState(1523);
							match(K_ISNULL);
							}
							break;
						case K_NOTNULL:
							{
							setState(1524);
							match(K_NOTNULL);
							}
							break;
						case K_NOT:
							{
							setState(1525);
							match(K_NOT);
							setState(1526);
							match(K_NULL);
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						}
						break;
					case 14:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(1529);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(1531);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==K_NOT) {
							{
							setState(1530);
							match(K_NOT);
							}
						}

						setState(1533);
						match(K_IN);
						setState(1553);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,188,_ctx) ) {
						case 1:
							{
							setState(1534);
							match(OPEN_PAR);
							setState(1544);
							_errHandler.sync(this);
							switch ( getInterpreter().adaptivePredict(_input,186,_ctx) ) {
							case 1:
								{
								setState(1535);
								select_stmt();
								}
								break;
							case 2:
								{
								setState(1536);
								expr(0);
								setState(1541);
								_errHandler.sync(this);
								_la = _input.LA(1);
								while (_la==COMMA) {
									{
									{
									setState(1537);
									match(COMMA);
									setState(1538);
									expr(0);
									}
									}
									setState(1543);
									_errHandler.sync(this);
									_la = _input.LA(1);
								}
								}
								break;
							}
							setState(1546);
							match(CLOSE_PAR);
							}
							break;
						case 2:
							{
							setState(1550);
							_errHandler.sync(this);
							switch ( getInterpreter().adaptivePredict(_input,187,_ctx) ) {
							case 1:
								{
								setState(1547);
								database_name();
								setState(1548);
								match(DOT);
								}
								break;
							}
							setState(1552);
							table_name();
							}
							break;
						}
						}
						break;
					}
					} 
				}
				setState(1559);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,190,_ctx);
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

	public static class Foreign_key_clauseContext extends ParserRuleContext {
		public TerminalNode K_REFERENCES() { return getToken(SQLGrammarParser.K_REFERENCES, 0); }
		public Foreign_tableContext foreign_table() {
			return getRuleContext(Foreign_tableContext.class,0);
		}
		public List<Column_nameContext> column_name() {
			return getRuleContexts(Column_nameContext.class);
		}
		public Column_nameContext column_name(int i) {
			return getRuleContext(Column_nameContext.class,i);
		}
		public TerminalNode K_DEFERRABLE() { return getToken(SQLGrammarParser.K_DEFERRABLE, 0); }
		public List<TerminalNode> K_ON() { return getTokens(SQLGrammarParser.K_ON); }
		public TerminalNode K_ON(int i) {
			return getToken(SQLGrammarParser.K_ON, i);
		}
		public List<TerminalNode> K_MATCH() { return getTokens(SQLGrammarParser.K_MATCH); }
		public TerminalNode K_MATCH(int i) {
			return getToken(SQLGrammarParser.K_MATCH, i);
		}
		public List<NameContext> name() {
			return getRuleContexts(NameContext.class);
		}
		public NameContext name(int i) {
			return getRuleContext(NameContext.class,i);
		}
		public List<TerminalNode> K_DELETE() { return getTokens(SQLGrammarParser.K_DELETE); }
		public TerminalNode K_DELETE(int i) {
			return getToken(SQLGrammarParser.K_DELETE, i);
		}
		public List<TerminalNode> K_UPDATE() { return getTokens(SQLGrammarParser.K_UPDATE); }
		public TerminalNode K_UPDATE(int i) {
			return getToken(SQLGrammarParser.K_UPDATE, i);
		}
		public TerminalNode K_NOT() { return getToken(SQLGrammarParser.K_NOT, 0); }
		public TerminalNode K_INITIALLY() { return getToken(SQLGrammarParser.K_INITIALLY, 0); }
		public TerminalNode K_DEFERRED() { return getToken(SQLGrammarParser.K_DEFERRED, 0); }
		public TerminalNode K_IMMEDIATE() { return getToken(SQLGrammarParser.K_IMMEDIATE, 0); }
		public List<TerminalNode> K_SET() { return getTokens(SQLGrammarParser.K_SET); }
		public TerminalNode K_SET(int i) {
			return getToken(SQLGrammarParser.K_SET, i);
		}
		public List<TerminalNode> K_NULL() { return getTokens(SQLGrammarParser.K_NULL); }
		public TerminalNode K_NULL(int i) {
			return getToken(SQLGrammarParser.K_NULL, i);
		}
		public List<TerminalNode> K_DEFAULT() { return getTokens(SQLGrammarParser.K_DEFAULT); }
		public TerminalNode K_DEFAULT(int i) {
			return getToken(SQLGrammarParser.K_DEFAULT, i);
		}
		public List<TerminalNode> K_CASCADE() { return getTokens(SQLGrammarParser.K_CASCADE); }
		public TerminalNode K_CASCADE(int i) {
			return getToken(SQLGrammarParser.K_CASCADE, i);
		}
		public List<TerminalNode> K_RESTRICT() { return getTokens(SQLGrammarParser.K_RESTRICT); }
		public TerminalNode K_RESTRICT(int i) {
			return getToken(SQLGrammarParser.K_RESTRICT, i);
		}
		public List<TerminalNode> K_NO() { return getTokens(SQLGrammarParser.K_NO); }
		public TerminalNode K_NO(int i) {
			return getToken(SQLGrammarParser.K_NO, i);
		}
		public List<TerminalNode> K_ACTION() { return getTokens(SQLGrammarParser.K_ACTION); }
		public TerminalNode K_ACTION(int i) {
			return getToken(SQLGrammarParser.K_ACTION, i);
		}
		public Foreign_key_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_foreign_key_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterForeign_key_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitForeign_key_clause(this);
		}
	}

	public final Foreign_key_clauseContext foreign_key_clause() throws RecognitionException {
		Foreign_key_clauseContext _localctx = new Foreign_key_clauseContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_foreign_key_clause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1560);
			match(K_REFERENCES);
			setState(1561);
			foreign_table();
			setState(1573);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPEN_PAR) {
				{
				setState(1562);
				match(OPEN_PAR);
				setState(1563);
				column_name();
				setState(1568);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1564);
					match(COMMA);
					setState(1565);
					column_name();
					}
					}
					setState(1570);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1571);
				match(CLOSE_PAR);
				}
			}

			setState(1593);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==K_MATCH || _la==K_ON) {
				{
				{
				setState(1589);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case K_ON:
					{
					setState(1575);
					match(K_ON);
					setState(1576);
					_la = _input.LA(1);
					if ( !(_la==K_DELETE || _la==K_UPDATE) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(1585);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,193,_ctx) ) {
					case 1:
						{
						setState(1577);
						match(K_SET);
						setState(1578);
						match(K_NULL);
						}
						break;
					case 2:
						{
						setState(1579);
						match(K_SET);
						setState(1580);
						match(K_DEFAULT);
						}
						break;
					case 3:
						{
						setState(1581);
						match(K_CASCADE);
						}
						break;
					case 4:
						{
						setState(1582);
						match(K_RESTRICT);
						}
						break;
					case 5:
						{
						setState(1583);
						match(K_NO);
						setState(1584);
						match(K_ACTION);
						}
						break;
					}
					}
					break;
				case K_MATCH:
					{
					setState(1587);
					match(K_MATCH);
					setState(1588);
					name();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(1595);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1606);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,198,_ctx) ) {
			case 1:
				{
				setState(1597);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_NOT) {
					{
					setState(1596);
					match(K_NOT);
					}
				}

				setState(1599);
				match(K_DEFERRABLE);
				setState(1604);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,197,_ctx) ) {
				case 1:
					{
					setState(1600);
					match(K_INITIALLY);
					setState(1601);
					match(K_DEFERRED);
					}
					break;
				case 2:
					{
					setState(1602);
					match(K_INITIALLY);
					setState(1603);
					match(K_IMMEDIATE);
					}
					break;
				}
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

	public static class Raise_functionContext extends ParserRuleContext {
		public TerminalNode K_RAISE() { return getToken(SQLGrammarParser.K_RAISE, 0); }
		public TerminalNode K_IGNORE() { return getToken(SQLGrammarParser.K_IGNORE, 0); }
		public Error_messageContext error_message() {
			return getRuleContext(Error_messageContext.class,0);
		}
		public TerminalNode K_ROLLBACK() { return getToken(SQLGrammarParser.K_ROLLBACK, 0); }
		public TerminalNode K_ABORT() { return getToken(SQLGrammarParser.K_ABORT, 0); }
		public TerminalNode K_FAIL() { return getToken(SQLGrammarParser.K_FAIL, 0); }
		public Raise_functionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_raise_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterRaise_function(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitRaise_function(this);
		}
	}

	public final Raise_functionContext raise_function() throws RecognitionException {
		Raise_functionContext _localctx = new Raise_functionContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_raise_function);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1608);
			match(K_RAISE);
			setState(1609);
			match(OPEN_PAR);
			setState(1614);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_IGNORE:
				{
				setState(1610);
				match(K_IGNORE);
				}
				break;
			case K_ABORT:
			case K_FAIL:
			case K_ROLLBACK:
				{
				setState(1611);
				_la = _input.LA(1);
				if ( !(_la==K_ABORT || _la==K_FAIL || _la==K_ROLLBACK) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1612);
				match(COMMA);
				setState(1613);
				error_message();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1616);
			match(CLOSE_PAR);
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

	public static class Indexed_columnContext extends ParserRuleContext {
		public Column_nameContext column_name() {
			return getRuleContext(Column_nameContext.class,0);
		}
		public TerminalNode K_COLLATE() { return getToken(SQLGrammarParser.K_COLLATE, 0); }
		public Collation_nameContext collation_name() {
			return getRuleContext(Collation_nameContext.class,0);
		}
		public TerminalNode K_ASC() { return getToken(SQLGrammarParser.K_ASC, 0); }
		public TerminalNode K_DESC() { return getToken(SQLGrammarParser.K_DESC, 0); }
		public Indexed_columnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_indexed_column; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterIndexed_column(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitIndexed_column(this);
		}
	}

	public final Indexed_columnContext indexed_column() throws RecognitionException {
		Indexed_columnContext _localctx = new Indexed_columnContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_indexed_column);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1618);
			column_name();
			setState(1621);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_COLLATE) {
				{
				setState(1619);
				match(K_COLLATE);
				setState(1620);
				collation_name();
				}
			}

			setState(1624);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_ASC || _la==K_DESC) {
				{
				setState(1623);
				_la = _input.LA(1);
				if ( !(_la==K_ASC || _la==K_DESC) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
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

	public static class Table_constraintContext extends ParserRuleContext {
		public List<Indexed_columnContext> indexed_column() {
			return getRuleContexts(Indexed_columnContext.class);
		}
		public Indexed_columnContext indexed_column(int i) {
			return getRuleContext(Indexed_columnContext.class,i);
		}
		public Conflict_clauseContext conflict_clause() {
			return getRuleContext(Conflict_clauseContext.class,0);
		}
		public TerminalNode K_CHECK() { return getToken(SQLGrammarParser.K_CHECK, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode K_FOREIGN() { return getToken(SQLGrammarParser.K_FOREIGN, 0); }
		public TerminalNode K_KEY() { return getToken(SQLGrammarParser.K_KEY, 0); }
		public List<Column_nameContext> column_name() {
			return getRuleContexts(Column_nameContext.class);
		}
		public Column_nameContext column_name(int i) {
			return getRuleContext(Column_nameContext.class,i);
		}
		public Foreign_key_clauseContext foreign_key_clause() {
			return getRuleContext(Foreign_key_clauseContext.class,0);
		}
		public TerminalNode K_CONSTRAINT() { return getToken(SQLGrammarParser.K_CONSTRAINT, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode K_PRIMARY() { return getToken(SQLGrammarParser.K_PRIMARY, 0); }
		public TerminalNode K_UNIQUE() { return getToken(SQLGrammarParser.K_UNIQUE, 0); }
		public Table_constraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_table_constraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterTable_constraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitTable_constraint(this);
		}
	}

	public final Table_constraintContext table_constraint() throws RecognitionException {
		Table_constraintContext _localctx = new Table_constraintContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_table_constraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1628);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_CONSTRAINT) {
				{
				setState(1626);
				match(K_CONSTRAINT);
				setState(1627);
				name();
				}
			}

			setState(1666);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_PRIMARY:
			case K_UNIQUE:
				{
				setState(1633);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case K_PRIMARY:
					{
					setState(1630);
					match(K_PRIMARY);
					setState(1631);
					match(K_KEY);
					}
					break;
				case K_UNIQUE:
					{
					setState(1632);
					match(K_UNIQUE);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1635);
				match(OPEN_PAR);
				setState(1636);
				indexed_column();
				setState(1641);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1637);
					match(COMMA);
					setState(1638);
					indexed_column();
					}
					}
					setState(1643);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1644);
				match(CLOSE_PAR);
				setState(1645);
				conflict_clause();
				}
				break;
			case K_CHECK:
				{
				setState(1647);
				match(K_CHECK);
				setState(1648);
				match(OPEN_PAR);
				setState(1649);
				expr(0);
				setState(1650);
				match(CLOSE_PAR);
				}
				break;
			case K_FOREIGN:
				{
				setState(1652);
				match(K_FOREIGN);
				setState(1653);
				match(K_KEY);
				setState(1654);
				match(OPEN_PAR);
				setState(1655);
				column_name();
				setState(1660);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1656);
					match(COMMA);
					setState(1657);
					column_name();
					}
					}
					setState(1662);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1663);
				match(CLOSE_PAR);
				setState(1664);
				foreign_key_clause();
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

	public static class With_clauseContext extends ParserRuleContext {
		public TerminalNode K_WITH() { return getToken(SQLGrammarParser.K_WITH, 0); }
		public List<Cte_table_nameContext> cte_table_name() {
			return getRuleContexts(Cte_table_nameContext.class);
		}
		public Cte_table_nameContext cte_table_name(int i) {
			return getRuleContext(Cte_table_nameContext.class,i);
		}
		public List<TerminalNode> K_AS() { return getTokens(SQLGrammarParser.K_AS); }
		public TerminalNode K_AS(int i) {
			return getToken(SQLGrammarParser.K_AS, i);
		}
		public List<Select_stmtContext> select_stmt() {
			return getRuleContexts(Select_stmtContext.class);
		}
		public Select_stmtContext select_stmt(int i) {
			return getRuleContext(Select_stmtContext.class,i);
		}
		public TerminalNode K_RECURSIVE() { return getToken(SQLGrammarParser.K_RECURSIVE, 0); }
		public With_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_with_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterWith_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitWith_clause(this);
		}
	}

	public final With_clauseContext with_clause() throws RecognitionException {
		With_clauseContext _localctx = new With_clauseContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_with_clause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1668);
			match(K_WITH);
			setState(1670);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,207,_ctx) ) {
			case 1:
				{
				setState(1669);
				match(K_RECURSIVE);
				}
				break;
			}
			setState(1672);
			cte_table_name();
			setState(1673);
			match(K_AS);
			setState(1674);
			match(OPEN_PAR);
			setState(1675);
			select_stmt();
			setState(1676);
			match(CLOSE_PAR);
			setState(1686);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(1677);
				match(COMMA);
				setState(1678);
				cte_table_name();
				setState(1679);
				match(K_AS);
				setState(1680);
				match(OPEN_PAR);
				setState(1681);
				select_stmt();
				setState(1682);
				match(CLOSE_PAR);
				}
				}
				setState(1688);
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

	public static class Qualified_table_nameContext extends ParserRuleContext {
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public TerminalNode K_INDEXED() { return getToken(SQLGrammarParser.K_INDEXED, 0); }
		public TerminalNode K_BY() { return getToken(SQLGrammarParser.K_BY, 0); }
		public Index_nameContext index_name() {
			return getRuleContext(Index_nameContext.class,0);
		}
		public TerminalNode K_NOT() { return getToken(SQLGrammarParser.K_NOT, 0); }
		public Qualified_table_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualified_table_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterQualified_table_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitQualified_table_name(this);
		}
	}

	public final Qualified_table_nameContext qualified_table_name() throws RecognitionException {
		Qualified_table_nameContext _localctx = new Qualified_table_nameContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_qualified_table_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1692);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,209,_ctx) ) {
			case 1:
				{
				setState(1689);
				database_name();
				setState(1690);
				match(DOT);
				}
				break;
			}
			setState(1694);
			table_name();
			setState(1700);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_INDEXED:
				{
				setState(1695);
				match(K_INDEXED);
				setState(1696);
				match(K_BY);
				setState(1697);
				index_name();
				}
				break;
			case K_NOT:
				{
				setState(1698);
				match(K_NOT);
				setState(1699);
				match(K_INDEXED);
				}
				break;
			case EOF:
			case SCOL:
			case K_ALTER:
			case K_ANALYZE:
			case K_ATTACH:
			case K_BEGIN:
			case K_COMMIT:
			case K_CREATE:
			case K_DELETE:
			case K_DETACH:
			case K_DROP:
			case K_END:
			case K_EXPLAIN:
			case K_INSERT:
			case K_LIMIT:
			case K_ORDER:
			case K_PRAGMA:
			case K_REINDEX:
			case K_RELEASE:
			case K_REPLACE:
			case K_ROLLBACK:
			case K_SAVEPOINT:
			case K_SELECT:
			case K_SET:
			case K_UPDATE:
			case K_VACUUM:
			case K_VALUES:
			case K_WHERE:
			case K_WITH:
			case UNEXPECTED_CHAR:
				break;
			default:
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

	public static class Ordering_termContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode K_COLLATE() { return getToken(SQLGrammarParser.K_COLLATE, 0); }
		public Collation_nameContext collation_name() {
			return getRuleContext(Collation_nameContext.class,0);
		}
		public TerminalNode K_ASC() { return getToken(SQLGrammarParser.K_ASC, 0); }
		public TerminalNode K_DESC() { return getToken(SQLGrammarParser.K_DESC, 0); }
		public Ordering_termContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ordering_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterOrdering_term(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitOrdering_term(this);
		}
	}

	public final Ordering_termContext ordering_term() throws RecognitionException {
		Ordering_termContext _localctx = new Ordering_termContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_ordering_term);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1702);
			expr(0);
			setState(1705);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_COLLATE) {
				{
				setState(1703);
				match(K_COLLATE);
				setState(1704);
				collation_name();
				}
			}

			setState(1708);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_ASC || _la==K_DESC) {
				{
				setState(1707);
				_la = _input.LA(1);
				if ( !(_la==K_ASC || _la==K_DESC) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
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

	public static class Pragma_valueContext extends ParserRuleContext {
		public Signed_numberContext signed_number() {
			return getRuleContext(Signed_numberContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public TerminalNode STRING_LITERAL() { return getToken(SQLGrammarParser.STRING_LITERAL, 0); }
		public Pragma_valueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pragma_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterPragma_value(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitPragma_value(this);
		}
	}

	public final Pragma_valueContext pragma_value() throws RecognitionException {
		Pragma_valueContext _localctx = new Pragma_valueContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_pragma_value);
		try {
			setState(1713);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,213,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1710);
				signed_number();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1711);
				name();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1712);
				match(STRING_LITERAL);
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

	public static class Common_table_expressionContext extends ParserRuleContext {
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public TerminalNode K_AS() { return getToken(SQLGrammarParser.K_AS, 0); }
		public Select_stmtContext select_stmt() {
			return getRuleContext(Select_stmtContext.class,0);
		}
		public List<Column_nameContext> column_name() {
			return getRuleContexts(Column_nameContext.class);
		}
		public Column_nameContext column_name(int i) {
			return getRuleContext(Column_nameContext.class,i);
		}
		public Common_table_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_common_table_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterCommon_table_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitCommon_table_expression(this);
		}
	}

	public final Common_table_expressionContext common_table_expression() throws RecognitionException {
		Common_table_expressionContext _localctx = new Common_table_expressionContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_common_table_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1715);
			table_name();
			setState(1727);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPEN_PAR) {
				{
				setState(1716);
				match(OPEN_PAR);
				setState(1717);
				column_name();
				setState(1722);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1718);
					match(COMMA);
					setState(1719);
					column_name();
					}
					}
					setState(1724);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1725);
				match(CLOSE_PAR);
				}
			}

			setState(1729);
			match(K_AS);
			setState(1730);
			match(OPEN_PAR);
			setState(1731);
			select_stmt();
			setState(1732);
			match(CLOSE_PAR);
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

	public static class Result_columnContext extends ParserRuleContext {
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Column_aliasContext column_alias() {
			return getRuleContext(Column_aliasContext.class,0);
		}
		public TerminalNode K_AS() { return getToken(SQLGrammarParser.K_AS, 0); }
		public Result_columnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_result_column; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterResult_column(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitResult_column(this);
		}
	}

	public final Result_columnContext result_column() throws RecognitionException {
		Result_columnContext _localctx = new Result_columnContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_result_column);
		int _la;
		try {
			setState(1746);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,218,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1734);
				match(STAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1735);
				table_name();
				setState(1736);
				match(DOT);
				setState(1737);
				match(STAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1739);
				expr(0);
				setState(1744);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_AS || _la==IDENTIFIER || _la==STRING_LITERAL) {
					{
					setState(1741);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==K_AS) {
						{
						setState(1740);
						match(K_AS);
						}
					}

					setState(1743);
					column_alias();
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

	public static class Table_or_subqueryContext extends ParserRuleContext {
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public Database_nameContext database_name() {
			return getRuleContext(Database_nameContext.class,0);
		}
		public Table_aliasContext table_alias() {
			return getRuleContext(Table_aliasContext.class,0);
		}
		public TerminalNode K_INDEXED() { return getToken(SQLGrammarParser.K_INDEXED, 0); }
		public TerminalNode K_BY() { return getToken(SQLGrammarParser.K_BY, 0); }
		public Index_nameContext index_name() {
			return getRuleContext(Index_nameContext.class,0);
		}
		public TerminalNode K_NOT() { return getToken(SQLGrammarParser.K_NOT, 0); }
		public TerminalNode K_AS() { return getToken(SQLGrammarParser.K_AS, 0); }
		public List<Table_or_subqueryContext> table_or_subquery() {
			return getRuleContexts(Table_or_subqueryContext.class);
		}
		public Table_or_subqueryContext table_or_subquery(int i) {
			return getRuleContext(Table_or_subqueryContext.class,i);
		}
		public Join_clauseContext join_clause() {
			return getRuleContext(Join_clauseContext.class,0);
		}
		public Select_stmtContext select_stmt() {
			return getRuleContext(Select_stmtContext.class,0);
		}
		public Table_or_subqueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_table_or_subquery; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterTable_or_subquery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitTable_or_subquery(this);
		}
	}

	public final Table_or_subqueryContext table_or_subquery() throws RecognitionException {
		Table_or_subqueryContext _localctx = new Table_or_subqueryContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_table_or_subquery);
		int _la;
		try {
			setState(1795);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,229,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1751);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,219,_ctx) ) {
				case 1:
					{
					setState(1748);
					database_name();
					setState(1749);
					match(DOT);
					}
					break;
				}
				setState(1753);
				table_name();
				setState(1758);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,221,_ctx) ) {
				case 1:
					{
					setState(1755);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,220,_ctx) ) {
					case 1:
						{
						setState(1754);
						match(K_AS);
						}
						break;
					}
					setState(1757);
					table_alias();
					}
					break;
				}
				setState(1765);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case K_INDEXED:
					{
					setState(1760);
					match(K_INDEXED);
					setState(1761);
					match(K_BY);
					setState(1762);
					index_name();
					}
					break;
				case K_NOT:
					{
					setState(1763);
					match(K_NOT);
					setState(1764);
					match(K_INDEXED);
					}
					break;
				case EOF:
				case SCOL:
				case CLOSE_PAR:
				case COMMA:
				case K_ALTER:
				case K_ANALYZE:
				case K_ATTACH:
				case K_BEGIN:
				case K_COMMIT:
				case K_CREATE:
				case K_CROSS:
				case K_DELETE:
				case K_DETACH:
				case K_DROP:
				case K_END:
				case K_EXCEPT:
				case K_EXPLAIN:
				case K_GROUP:
				case K_INNER:
				case K_INSERT:
				case K_INTERSECT:
				case K_JOIN:
				case K_LEFT:
				case K_LIMIT:
				case K_NATURAL:
				case K_ON:
				case K_ORDER:
				case K_PRAGMA:
				case K_REINDEX:
				case K_RELEASE:
				case K_REPLACE:
				case K_ROLLBACK:
				case K_SAVEPOINT:
				case K_SELECT:
				case K_UNION:
				case K_UPDATE:
				case K_USING:
				case K_VACUUM:
				case K_VALUES:
				case K_WHERE:
				case K_WITH:
				case UNEXPECTED_CHAR:
					break;
				default:
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1767);
				match(OPEN_PAR);
				setState(1777);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,224,_ctx) ) {
				case 1:
					{
					setState(1768);
					table_or_subquery();
					setState(1773);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1769);
						match(COMMA);
						setState(1770);
						table_or_subquery();
						}
						}
						setState(1775);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
					break;
				case 2:
					{
					setState(1776);
					join_clause();
					}
					break;
				}
				setState(1779);
				match(CLOSE_PAR);
				setState(1784);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,226,_ctx) ) {
				case 1:
					{
					setState(1781);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,225,_ctx) ) {
					case 1:
						{
						setState(1780);
						match(K_AS);
						}
						break;
					}
					setState(1783);
					table_alias();
					}
					break;
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1786);
				match(OPEN_PAR);
				setState(1787);
				select_stmt();
				setState(1788);
				match(CLOSE_PAR);
				setState(1793);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,228,_ctx) ) {
				case 1:
					{
					setState(1790);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,227,_ctx) ) {
					case 1:
						{
						setState(1789);
						match(K_AS);
						}
						break;
					}
					setState(1792);
					table_alias();
					}
					break;
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

	public static class Join_clauseContext extends ParserRuleContext {
		public List<Table_or_subqueryContext> table_or_subquery() {
			return getRuleContexts(Table_or_subqueryContext.class);
		}
		public Table_or_subqueryContext table_or_subquery(int i) {
			return getRuleContext(Table_or_subqueryContext.class,i);
		}
		public List<Join_operatorContext> join_operator() {
			return getRuleContexts(Join_operatorContext.class);
		}
		public Join_operatorContext join_operator(int i) {
			return getRuleContext(Join_operatorContext.class,i);
		}
		public List<Join_constraintContext> join_constraint() {
			return getRuleContexts(Join_constraintContext.class);
		}
		public Join_constraintContext join_constraint(int i) {
			return getRuleContext(Join_constraintContext.class,i);
		}
		public Join_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_join_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterJoin_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitJoin_clause(this);
		}
	}

	public final Join_clauseContext join_clause() throws RecognitionException {
		Join_clauseContext _localctx = new Join_clauseContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_join_clause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1797);
			table_or_subquery();
			setState(1804);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA || _la==K_CROSS || ((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & ((1L << (K_INNER - 91)) | (1L << (K_JOIN - 91)) | (1L << (K_LEFT - 91)) | (1L << (K_NATURAL - 91)))) != 0)) {
				{
				{
				setState(1798);
				join_operator();
				setState(1799);
				table_or_subquery();
				setState(1800);
				join_constraint();
				}
				}
				setState(1806);
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

	public static class Join_operatorContext extends ParserRuleContext {
		public TerminalNode K_JOIN() { return getToken(SQLGrammarParser.K_JOIN, 0); }
		public TerminalNode K_NATURAL() { return getToken(SQLGrammarParser.K_NATURAL, 0); }
		public TerminalNode K_LEFT() { return getToken(SQLGrammarParser.K_LEFT, 0); }
		public TerminalNode K_INNER() { return getToken(SQLGrammarParser.K_INNER, 0); }
		public TerminalNode K_CROSS() { return getToken(SQLGrammarParser.K_CROSS, 0); }
		public TerminalNode K_OUTER() { return getToken(SQLGrammarParser.K_OUTER, 0); }
		public Join_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_join_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterJoin_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitJoin_operator(this);
		}
	}

	public final Join_operatorContext join_operator() throws RecognitionException {
		Join_operatorContext _localctx = new Join_operatorContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_join_operator);
		int _la;
		try {
			setState(1820);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case COMMA:
				enterOuterAlt(_localctx, 1);
				{
				setState(1807);
				match(COMMA);
				}
				break;
			case K_CROSS:
			case K_INNER:
			case K_JOIN:
			case K_LEFT:
			case K_NATURAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(1809);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_NATURAL) {
					{
					setState(1808);
					match(K_NATURAL);
					}
				}

				setState(1817);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case K_LEFT:
					{
					setState(1811);
					match(K_LEFT);
					setState(1813);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==K_OUTER) {
						{
						setState(1812);
						match(K_OUTER);
						}
					}

					}
					break;
				case K_INNER:
					{
					setState(1815);
					match(K_INNER);
					}
					break;
				case K_CROSS:
					{
					setState(1816);
					match(K_CROSS);
					}
					break;
				case K_JOIN:
					break;
				default:
					break;
				}
				setState(1819);
				match(K_JOIN);
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

	public static class Join_constraintContext extends ParserRuleContext {
		public TerminalNode K_ON() { return getToken(SQLGrammarParser.K_ON, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode K_USING() { return getToken(SQLGrammarParser.K_USING, 0); }
		public List<Column_nameContext> column_name() {
			return getRuleContexts(Column_nameContext.class);
		}
		public Column_nameContext column_name(int i) {
			return getRuleContext(Column_nameContext.class,i);
		}
		public Join_constraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_join_constraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterJoin_constraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitJoin_constraint(this);
		}
	}

	public final Join_constraintContext join_constraint() throws RecognitionException {
		Join_constraintContext _localctx = new Join_constraintContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_join_constraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1836);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_ON:
				{
				setState(1822);
				match(K_ON);
				setState(1823);
				expr(0);
				}
				break;
			case K_USING:
				{
				setState(1824);
				match(K_USING);
				setState(1825);
				match(OPEN_PAR);
				setState(1826);
				column_name();
				setState(1831);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1827);
					match(COMMA);
					setState(1828);
					column_name();
					}
					}
					setState(1833);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1834);
				match(CLOSE_PAR);
				}
				break;
			case EOF:
			case SCOL:
			case CLOSE_PAR:
			case COMMA:
			case K_ALTER:
			case K_ANALYZE:
			case K_ATTACH:
			case K_BEGIN:
			case K_COMMIT:
			case K_CREATE:
			case K_CROSS:
			case K_DELETE:
			case K_DETACH:
			case K_DROP:
			case K_END:
			case K_EXCEPT:
			case K_EXPLAIN:
			case K_GROUP:
			case K_INNER:
			case K_INSERT:
			case K_INTERSECT:
			case K_JOIN:
			case K_LEFT:
			case K_LIMIT:
			case K_NATURAL:
			case K_ORDER:
			case K_PRAGMA:
			case K_REINDEX:
			case K_RELEASE:
			case K_REPLACE:
			case K_ROLLBACK:
			case K_SAVEPOINT:
			case K_SELECT:
			case K_UNION:
			case K_UPDATE:
			case K_VACUUM:
			case K_VALUES:
			case K_WHERE:
			case K_WITH:
			case UNEXPECTED_CHAR:
				break;
			default:
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

	public static class Select_coreContext extends ParserRuleContext {
		public TerminalNode K_SELECT() { return getToken(SQLGrammarParser.K_SELECT, 0); }
		public List<Result_columnContext> result_column() {
			return getRuleContexts(Result_columnContext.class);
		}
		public Result_columnContext result_column(int i) {
			return getRuleContext(Result_columnContext.class,i);
		}
		public TerminalNode K_FROM() { return getToken(SQLGrammarParser.K_FROM, 0); }
		public TerminalNode K_WHERE() { return getToken(SQLGrammarParser.K_WHERE, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode K_GROUP() { return getToken(SQLGrammarParser.K_GROUP, 0); }
		public TerminalNode K_BY() { return getToken(SQLGrammarParser.K_BY, 0); }
		public TerminalNode K_DISTINCT() { return getToken(SQLGrammarParser.K_DISTINCT, 0); }
		public TerminalNode K_ALL() { return getToken(SQLGrammarParser.K_ALL, 0); }
		public List<Table_or_subqueryContext> table_or_subquery() {
			return getRuleContexts(Table_or_subqueryContext.class);
		}
		public Table_or_subqueryContext table_or_subquery(int i) {
			return getRuleContext(Table_or_subqueryContext.class,i);
		}
		public Join_clauseContext join_clause() {
			return getRuleContext(Join_clauseContext.class,0);
		}
		public TerminalNode K_HAVING() { return getToken(SQLGrammarParser.K_HAVING, 0); }
		public TerminalNode K_VALUES() { return getToken(SQLGrammarParser.K_VALUES, 0); }
		public Select_coreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_select_core; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterSelect_core(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitSelect_core(this);
		}
	}

	public final Select_coreContext select_core() throws RecognitionException {
		Select_coreContext _localctx = new Select_coreContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_select_core);
		int _la;
		try {
			setState(1912);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_SELECT:
				enterOuterAlt(_localctx, 1);
				{
				setState(1838);
				match(K_SELECT);
				setState(1840);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,237,_ctx) ) {
				case 1:
					{
					setState(1839);
					_la = _input.LA(1);
					if ( !(_la==K_ALL || _la==K_DISTINCT) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					break;
				}
				setState(1842);
				result_column();
				setState(1847);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1843);
					match(COMMA);
					setState(1844);
					result_column();
					}
					}
					setState(1849);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1862);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_FROM) {
					{
					setState(1850);
					match(K_FROM);
					setState(1860);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,240,_ctx) ) {
					case 1:
						{
						setState(1851);
						table_or_subquery();
						setState(1856);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==COMMA) {
							{
							{
							setState(1852);
							match(COMMA);
							setState(1853);
							table_or_subquery();
							}
							}
							setState(1858);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						}
						break;
					case 2:
						{
						setState(1859);
						join_clause();
						}
						break;
					}
					}
				}

				setState(1866);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_WHERE) {
					{
					setState(1864);
					match(K_WHERE);
					setState(1865);
					expr(0);
					}
				}

				setState(1882);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==K_GROUP) {
					{
					setState(1868);
					match(K_GROUP);
					setState(1869);
					match(K_BY);
					setState(1870);
					expr(0);
					setState(1875);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1871);
						match(COMMA);
						setState(1872);
						expr(0);
						}
						}
						setState(1877);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(1880);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==K_HAVING) {
						{
						setState(1878);
						match(K_HAVING);
						setState(1879);
						expr(0);
						}
					}

					}
				}

				}
				break;
			case K_VALUES:
				enterOuterAlt(_localctx, 2);
				{
				setState(1884);
				match(K_VALUES);
				setState(1885);
				match(OPEN_PAR);
				setState(1886);
				expr(0);
				setState(1891);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1887);
					match(COMMA);
					setState(1888);
					expr(0);
					}
					}
					setState(1893);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1894);
				match(CLOSE_PAR);
				setState(1909);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1895);
					match(COMMA);
					setState(1896);
					match(OPEN_PAR);
					setState(1897);
					expr(0);
					setState(1902);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(1898);
						match(COMMA);
						setState(1899);
						expr(0);
						}
						}
						setState(1904);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(1905);
					match(CLOSE_PAR);
					}
					}
					setState(1911);
					_errHandler.sync(this);
					_la = _input.LA(1);
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

	public static class Compound_operatorContext extends ParserRuleContext {
		public TerminalNode K_UNION() { return getToken(SQLGrammarParser.K_UNION, 0); }
		public TerminalNode K_ALL() { return getToken(SQLGrammarParser.K_ALL, 0); }
		public TerminalNode K_INTERSECT() { return getToken(SQLGrammarParser.K_INTERSECT, 0); }
		public TerminalNode K_EXCEPT() { return getToken(SQLGrammarParser.K_EXCEPT, 0); }
		public Compound_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compound_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterCompound_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitCompound_operator(this);
		}
	}

	public final Compound_operatorContext compound_operator() throws RecognitionException {
		Compound_operatorContext _localctx = new Compound_operatorContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_compound_operator);
		try {
			setState(1919);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,250,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1914);
				match(K_UNION);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1915);
				match(K_UNION);
				setState(1916);
				match(K_ALL);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1917);
				match(K_INTERSECT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1918);
				match(K_EXCEPT);
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

	public static class Cte_table_nameContext extends ParserRuleContext {
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public List<Column_nameContext> column_name() {
			return getRuleContexts(Column_nameContext.class);
		}
		public Column_nameContext column_name(int i) {
			return getRuleContext(Column_nameContext.class,i);
		}
		public Cte_table_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cte_table_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterCte_table_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitCte_table_name(this);
		}
	}

	public final Cte_table_nameContext cte_table_name() throws RecognitionException {
		Cte_table_nameContext _localctx = new Cte_table_nameContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_cte_table_name);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1921);
			table_name();
			setState(1933);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPEN_PAR) {
				{
				setState(1922);
				match(OPEN_PAR);
				setState(1923);
				column_name();
				setState(1928);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(1924);
					match(COMMA);
					setState(1925);
					column_name();
					}
					}
					setState(1930);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1931);
				match(CLOSE_PAR);
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

	public static class Signed_numberContext extends ParserRuleContext {
		public TerminalNode NUMERIC_LITERAL() { return getToken(SQLGrammarParser.NUMERIC_LITERAL, 0); }
		public Signed_numberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_signed_number; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterSigned_number(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitSigned_number(this);
		}
	}

	public final Signed_numberContext signed_number() throws RecognitionException {
		Signed_numberContext _localctx = new Signed_numberContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_signed_number);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1936);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PLUS || _la==MINUS) {
				{
				setState(1935);
				_la = _input.LA(1);
				if ( !(_la==PLUS || _la==MINUS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(1938);
			match(NUMERIC_LITERAL);
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

	public static class Literal_valueContext extends ParserRuleContext {
		public TerminalNode NUMERIC_LITERAL() { return getToken(SQLGrammarParser.NUMERIC_LITERAL, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(SQLGrammarParser.STRING_LITERAL, 0); }
		public TerminalNode BLOB_LITERAL() { return getToken(SQLGrammarParser.BLOB_LITERAL, 0); }
		public TerminalNode K_NULL() { return getToken(SQLGrammarParser.K_NULL, 0); }
		public TerminalNode K_CURRENT_TIME() { return getToken(SQLGrammarParser.K_CURRENT_TIME, 0); }
		public TerminalNode K_CURRENT_DATE() { return getToken(SQLGrammarParser.K_CURRENT_DATE, 0); }
		public TerminalNode K_CURRENT_TIMESTAMP() { return getToken(SQLGrammarParser.K_CURRENT_TIMESTAMP, 0); }
		public Literal_valueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterLiteral_value(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitLiteral_value(this);
		}
	}

	public final Literal_valueContext literal_value() throws RecognitionException {
		Literal_valueContext _localctx = new Literal_valueContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_literal_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1940);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << K_CURRENT_DATE) | (1L << K_CURRENT_TIME) | (1L << K_CURRENT_TIMESTAMP))) != 0) || ((((_la - 108)) & ~0x3f) == 0 && ((1L << (_la - 108)) & ((1L << (K_NULL - 108)) | (1L << (NUMERIC_LITERAL - 108)) | (1L << (STRING_LITERAL - 108)) | (1L << (BLOB_LITERAL - 108)))) != 0)) ) {
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

	public static class Unary_operatorContext extends ParserRuleContext {
		public TerminalNode K_NOT() { return getToken(SQLGrammarParser.K_NOT, 0); }
		public Unary_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterUnary_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitUnary_operator(this);
		}
	}

	public final Unary_operatorContext unary_operator() throws RecognitionException {
		Unary_operatorContext _localctx = new Unary_operatorContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_unary_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1942);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PLUS) | (1L << MINUS) | (1L << TILDE))) != 0) || _la==K_NOT) ) {
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

	public static class Error_messageContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL() { return getToken(SQLGrammarParser.STRING_LITERAL, 0); }
		public Error_messageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_error_message; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterError_message(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitError_message(this);
		}
	}

	public final Error_messageContext error_message() throws RecognitionException {
		Error_messageContext _localctx = new Error_messageContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_error_message);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1944);
			match(STRING_LITERAL);
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

	public static class Module_argumentContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Column_defContext column_def() {
			return getRuleContext(Column_defContext.class,0);
		}
		public Module_argumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterModule_argument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitModule_argument(this);
		}
	}

	public final Module_argumentContext module_argument() throws RecognitionException {
		Module_argumentContext _localctx = new Module_argumentContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_module_argument);
		try {
			setState(1948);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,254,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1946);
				expr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1947);
				column_def();
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

	public static class Column_aliasContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(SQLGrammarParser.IDENTIFIER, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(SQLGrammarParser.STRING_LITERAL, 0); }
		public Column_aliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column_alias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterColumn_alias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitColumn_alias(this);
		}
	}

	public final Column_aliasContext column_alias() throws RecognitionException {
		Column_aliasContext _localctx = new Column_aliasContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_column_alias);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1950);
			_la = _input.LA(1);
			if ( !(_la==IDENTIFIER || _la==STRING_LITERAL) ) {
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

	public static class KeywordContext extends ParserRuleContext {
		public TerminalNode K_ABORT() { return getToken(SQLGrammarParser.K_ABORT, 0); }
		public TerminalNode K_ACTION() { return getToken(SQLGrammarParser.K_ACTION, 0); }
		public TerminalNode K_ADD() { return getToken(SQLGrammarParser.K_ADD, 0); }
		public TerminalNode K_AFTER() { return getToken(SQLGrammarParser.K_AFTER, 0); }
		public TerminalNode K_ALL() { return getToken(SQLGrammarParser.K_ALL, 0); }
		public TerminalNode K_ALTER() { return getToken(SQLGrammarParser.K_ALTER, 0); }
		public TerminalNode K_ANALYZE() { return getToken(SQLGrammarParser.K_ANALYZE, 0); }
		public TerminalNode K_AND() { return getToken(SQLGrammarParser.K_AND, 0); }
		public TerminalNode K_AS() { return getToken(SQLGrammarParser.K_AS, 0); }
		public TerminalNode K_ASC() { return getToken(SQLGrammarParser.K_ASC, 0); }
		public TerminalNode K_ATTACH() { return getToken(SQLGrammarParser.K_ATTACH, 0); }
		public TerminalNode K_AUTOINCREMENT() { return getToken(SQLGrammarParser.K_AUTOINCREMENT, 0); }
		public TerminalNode K_BEFORE() { return getToken(SQLGrammarParser.K_BEFORE, 0); }
		public TerminalNode K_BEGIN() { return getToken(SQLGrammarParser.K_BEGIN, 0); }
		public TerminalNode K_BETWEEN() { return getToken(SQLGrammarParser.K_BETWEEN, 0); }
		public TerminalNode K_BY() { return getToken(SQLGrammarParser.K_BY, 0); }
		public TerminalNode K_CASCADE() { return getToken(SQLGrammarParser.K_CASCADE, 0); }
		public TerminalNode K_CASE() { return getToken(SQLGrammarParser.K_CASE, 0); }
		public TerminalNode K_CAST() { return getToken(SQLGrammarParser.K_CAST, 0); }
		public TerminalNode K_CHECK() { return getToken(SQLGrammarParser.K_CHECK, 0); }
		public TerminalNode K_COLLATE() { return getToken(SQLGrammarParser.K_COLLATE, 0); }
		public TerminalNode K_COLUMN() { return getToken(SQLGrammarParser.K_COLUMN, 0); }
		public TerminalNode K_COMMIT() { return getToken(SQLGrammarParser.K_COMMIT, 0); }
		public TerminalNode K_CONFLICT() { return getToken(SQLGrammarParser.K_CONFLICT, 0); }
		public TerminalNode K_CONSTRAINT() { return getToken(SQLGrammarParser.K_CONSTRAINT, 0); }
		public TerminalNode K_CREATE() { return getToken(SQLGrammarParser.K_CREATE, 0); }
		public TerminalNode K_CROSS() { return getToken(SQLGrammarParser.K_CROSS, 0); }
		public TerminalNode K_CURRENT_DATE() { return getToken(SQLGrammarParser.K_CURRENT_DATE, 0); }
		public TerminalNode K_CURRENT_TIME() { return getToken(SQLGrammarParser.K_CURRENT_TIME, 0); }
		public TerminalNode K_CURRENT_TIMESTAMP() { return getToken(SQLGrammarParser.K_CURRENT_TIMESTAMP, 0); }
		public TerminalNode K_DATABASE() { return getToken(SQLGrammarParser.K_DATABASE, 0); }
		public TerminalNode K_DEFAULT() { return getToken(SQLGrammarParser.K_DEFAULT, 0); }
		public TerminalNode K_DEFERRABLE() { return getToken(SQLGrammarParser.K_DEFERRABLE, 0); }
		public TerminalNode K_DEFERRED() { return getToken(SQLGrammarParser.K_DEFERRED, 0); }
		public TerminalNode K_DELETE() { return getToken(SQLGrammarParser.K_DELETE, 0); }
		public TerminalNode K_DESC() { return getToken(SQLGrammarParser.K_DESC, 0); }
		public TerminalNode K_DETACH() { return getToken(SQLGrammarParser.K_DETACH, 0); }
		public TerminalNode K_DISTINCT() { return getToken(SQLGrammarParser.K_DISTINCT, 0); }
		public TerminalNode K_DROP() { return getToken(SQLGrammarParser.K_DROP, 0); }
		public TerminalNode K_EACH() { return getToken(SQLGrammarParser.K_EACH, 0); }
		public TerminalNode K_ELSE() { return getToken(SQLGrammarParser.K_ELSE, 0); }
		public TerminalNode K_END() { return getToken(SQLGrammarParser.K_END, 0); }
		public TerminalNode K_ESCAPE() { return getToken(SQLGrammarParser.K_ESCAPE, 0); }
		public TerminalNode K_EXCEPT() { return getToken(SQLGrammarParser.K_EXCEPT, 0); }
		public TerminalNode K_EXCLUSIVE() { return getToken(SQLGrammarParser.K_EXCLUSIVE, 0); }
		public TerminalNode K_EXISTS() { return getToken(SQLGrammarParser.K_EXISTS, 0); }
		public TerminalNode K_EXPLAIN() { return getToken(SQLGrammarParser.K_EXPLAIN, 0); }
		public TerminalNode K_FAIL() { return getToken(SQLGrammarParser.K_FAIL, 0); }
		public TerminalNode K_FOR() { return getToken(SQLGrammarParser.K_FOR, 0); }
		public TerminalNode K_FOREIGN() { return getToken(SQLGrammarParser.K_FOREIGN, 0); }
		public TerminalNode K_FROM() { return getToken(SQLGrammarParser.K_FROM, 0); }
		public TerminalNode K_FULL() { return getToken(SQLGrammarParser.K_FULL, 0); }
		public TerminalNode K_GLOB() { return getToken(SQLGrammarParser.K_GLOB, 0); }
		public TerminalNode K_GROUP() { return getToken(SQLGrammarParser.K_GROUP, 0); }
		public TerminalNode K_HAVING() { return getToken(SQLGrammarParser.K_HAVING, 0); }
		public TerminalNode K_IF() { return getToken(SQLGrammarParser.K_IF, 0); }
		public TerminalNode K_IGNORE() { return getToken(SQLGrammarParser.K_IGNORE, 0); }
		public TerminalNode K_IMMEDIATE() { return getToken(SQLGrammarParser.K_IMMEDIATE, 0); }
		public TerminalNode K_IN() { return getToken(SQLGrammarParser.K_IN, 0); }
		public TerminalNode K_INDEX() { return getToken(SQLGrammarParser.K_INDEX, 0); }
		public TerminalNode K_INDEXED() { return getToken(SQLGrammarParser.K_INDEXED, 0); }
		public TerminalNode K_INITIALLY() { return getToken(SQLGrammarParser.K_INITIALLY, 0); }
		public TerminalNode K_INNER() { return getToken(SQLGrammarParser.K_INNER, 0); }
		public TerminalNode K_INSERT() { return getToken(SQLGrammarParser.K_INSERT, 0); }
		public TerminalNode K_INSTEAD() { return getToken(SQLGrammarParser.K_INSTEAD, 0); }
		public TerminalNode K_INTERSECT() { return getToken(SQLGrammarParser.K_INTERSECT, 0); }
		public TerminalNode K_INTO() { return getToken(SQLGrammarParser.K_INTO, 0); }
		public TerminalNode K_IS() { return getToken(SQLGrammarParser.K_IS, 0); }
		public TerminalNode K_ISNULL() { return getToken(SQLGrammarParser.K_ISNULL, 0); }
		public TerminalNode K_JOIN() { return getToken(SQLGrammarParser.K_JOIN, 0); }
		public TerminalNode K_KEY() { return getToken(SQLGrammarParser.K_KEY, 0); }
		public TerminalNode K_LEFT() { return getToken(SQLGrammarParser.K_LEFT, 0); }
		public TerminalNode K_LIKE() { return getToken(SQLGrammarParser.K_LIKE, 0); }
		public TerminalNode K_LIMIT() { return getToken(SQLGrammarParser.K_LIMIT, 0); }
		public TerminalNode K_MATCH() { return getToken(SQLGrammarParser.K_MATCH, 0); }
		public TerminalNode K_NATURAL() { return getToken(SQLGrammarParser.K_NATURAL, 0); }
		public TerminalNode K_NO() { return getToken(SQLGrammarParser.K_NO, 0); }
		public TerminalNode K_NOT() { return getToken(SQLGrammarParser.K_NOT, 0); }
		public TerminalNode K_NOTNULL() { return getToken(SQLGrammarParser.K_NOTNULL, 0); }
		public TerminalNode K_NULL() { return getToken(SQLGrammarParser.K_NULL, 0); }
		public TerminalNode K_OF() { return getToken(SQLGrammarParser.K_OF, 0); }
		public TerminalNode K_OFFSET() { return getToken(SQLGrammarParser.K_OFFSET, 0); }
		public TerminalNode K_ON() { return getToken(SQLGrammarParser.K_ON, 0); }
		public TerminalNode K_OR() { return getToken(SQLGrammarParser.K_OR, 0); }
		public TerminalNode K_ORDER() { return getToken(SQLGrammarParser.K_ORDER, 0); }
		public TerminalNode K_OUTER() { return getToken(SQLGrammarParser.K_OUTER, 0); }
		public TerminalNode K_PLAN() { return getToken(SQLGrammarParser.K_PLAN, 0); }
		public TerminalNode K_PRAGMA() { return getToken(SQLGrammarParser.K_PRAGMA, 0); }
		public TerminalNode K_PRIMARY() { return getToken(SQLGrammarParser.K_PRIMARY, 0); }
		public TerminalNode K_QUERY() { return getToken(SQLGrammarParser.K_QUERY, 0); }
		public TerminalNode K_RAISE() { return getToken(SQLGrammarParser.K_RAISE, 0); }
		public TerminalNode K_RECURSIVE() { return getToken(SQLGrammarParser.K_RECURSIVE, 0); }
		public TerminalNode K_REFERENCES() { return getToken(SQLGrammarParser.K_REFERENCES, 0); }
		public TerminalNode K_REGEXP() { return getToken(SQLGrammarParser.K_REGEXP, 0); }
		public TerminalNode K_REINDEX() { return getToken(SQLGrammarParser.K_REINDEX, 0); }
		public TerminalNode K_RELEASE() { return getToken(SQLGrammarParser.K_RELEASE, 0); }
		public TerminalNode K_RENAME() { return getToken(SQLGrammarParser.K_RENAME, 0); }
		public TerminalNode K_REPLACE() { return getToken(SQLGrammarParser.K_REPLACE, 0); }
		public TerminalNode K_RESTRICT() { return getToken(SQLGrammarParser.K_RESTRICT, 0); }
		public TerminalNode K_RIGHT() { return getToken(SQLGrammarParser.K_RIGHT, 0); }
		public TerminalNode K_ROLLBACK() { return getToken(SQLGrammarParser.K_ROLLBACK, 0); }
		public TerminalNode K_ROW() { return getToken(SQLGrammarParser.K_ROW, 0); }
		public TerminalNode K_SAVEPOINT() { return getToken(SQLGrammarParser.K_SAVEPOINT, 0); }
		public TerminalNode K_SELECT() { return getToken(SQLGrammarParser.K_SELECT, 0); }
		public TerminalNode K_SET() { return getToken(SQLGrammarParser.K_SET, 0); }
		public TerminalNode K_TABLE() { return getToken(SQLGrammarParser.K_TABLE, 0); }
		public TerminalNode K_TEMP() { return getToken(SQLGrammarParser.K_TEMP, 0); }
		public TerminalNode K_TEMPORARY() { return getToken(SQLGrammarParser.K_TEMPORARY, 0); }
		public TerminalNode K_THEN() { return getToken(SQLGrammarParser.K_THEN, 0); }
		public TerminalNode K_TO() { return getToken(SQLGrammarParser.K_TO, 0); }
		public TerminalNode K_TRANSACTION() { return getToken(SQLGrammarParser.K_TRANSACTION, 0); }
		public TerminalNode K_TRIGGER() { return getToken(SQLGrammarParser.K_TRIGGER, 0); }
		public TerminalNode K_UNION() { return getToken(SQLGrammarParser.K_UNION, 0); }
		public TerminalNode K_UNIQUE() { return getToken(SQLGrammarParser.K_UNIQUE, 0); }
		public TerminalNode K_UPDATE() { return getToken(SQLGrammarParser.K_UPDATE, 0); }
		public TerminalNode K_USING() { return getToken(SQLGrammarParser.K_USING, 0); }
		public TerminalNode K_VACUUM() { return getToken(SQLGrammarParser.K_VACUUM, 0); }
		public TerminalNode K_VALUES() { return getToken(SQLGrammarParser.K_VALUES, 0); }
		public TerminalNode K_VIEW() { return getToken(SQLGrammarParser.K_VIEW, 0); }
		public TerminalNode K_VIRTUAL() { return getToken(SQLGrammarParser.K_VIRTUAL, 0); }
		public TerminalNode K_WHEN() { return getToken(SQLGrammarParser.K_WHEN, 0); }
		public TerminalNode K_WHERE() { return getToken(SQLGrammarParser.K_WHERE, 0); }
		public TerminalNode K_WITH() { return getToken(SQLGrammarParser.K_WITH, 0); }
		public TerminalNode K_WITHOUT() { return getToken(SQLGrammarParser.K_WITHOUT, 0); }
		public KeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyword; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterKeyword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitKeyword(this);
		}
	}

	public final KeywordContext keyword() throws RecognitionException {
		KeywordContext _localctx = new KeywordContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_keyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1952);
			_la = _input.LA(1);
			if ( !(((((_la - 29)) & ~0x3f) == 0 && ((1L << (_la - 29)) & ((1L << (K_ABORT - 29)) | (1L << (K_ACTION - 29)) | (1L << (K_ADD - 29)) | (1L << (K_AFTER - 29)) | (1L << (K_ALL - 29)) | (1L << (K_ALTER - 29)) | (1L << (K_ANALYZE - 29)) | (1L << (K_AND - 29)) | (1L << (K_AS - 29)) | (1L << (K_ASC - 29)) | (1L << (K_ATTACH - 29)) | (1L << (K_AUTOINCREMENT - 29)) | (1L << (K_BEFORE - 29)) | (1L << (K_BEGIN - 29)) | (1L << (K_BETWEEN - 29)) | (1L << (K_BY - 29)) | (1L << (K_CASCADE - 29)) | (1L << (K_CASE - 29)) | (1L << (K_CAST - 29)) | (1L << (K_CHECK - 29)) | (1L << (K_COLLATE - 29)) | (1L << (K_COLUMN - 29)) | (1L << (K_COMMIT - 29)) | (1L << (K_CONFLICT - 29)) | (1L << (K_CONSTRAINT - 29)) | (1L << (K_CREATE - 29)) | (1L << (K_CROSS - 29)) | (1L << (K_CURRENT_DATE - 29)) | (1L << (K_CURRENT_TIME - 29)) | (1L << (K_CURRENT_TIMESTAMP - 29)) | (1L << (K_DATABASE - 29)) | (1L << (K_DEFAULT - 29)) | (1L << (K_DEFERRABLE - 29)) | (1L << (K_DEFERRED - 29)) | (1L << (K_DELETE - 29)) | (1L << (K_DESC - 29)) | (1L << (K_DETACH - 29)) | (1L << (K_DISTINCT - 29)) | (1L << (K_DROP - 29)) | (1L << (K_EACH - 29)) | (1L << (K_ELSE - 29)) | (1L << (K_END - 29)) | (1L << (K_ESCAPE - 29)) | (1L << (K_EXCEPT - 29)) | (1L << (K_EXCLUSIVE - 29)) | (1L << (K_EXISTS - 29)) | (1L << (K_EXPLAIN - 29)) | (1L << (K_FAIL - 29)) | (1L << (K_FOR - 29)) | (1L << (K_FOREIGN - 29)) | (1L << (K_FROM - 29)) | (1L << (K_FULL - 29)) | (1L << (K_GLOB - 29)) | (1L << (K_GROUP - 29)) | (1L << (K_HAVING - 29)) | (1L << (K_IF - 29)) | (1L << (K_IGNORE - 29)) | (1L << (K_IMMEDIATE - 29)) | (1L << (K_IN - 29)) | (1L << (K_INDEX - 29)) | (1L << (K_INDEXED - 29)) | (1L << (K_INITIALLY - 29)) | (1L << (K_INNER - 29)) | (1L << (K_INSERT - 29)))) != 0) || ((((_la - 93)) & ~0x3f) == 0 && ((1L << (_la - 93)) & ((1L << (K_INSTEAD - 93)) | (1L << (K_INTERSECT - 93)) | (1L << (K_INTO - 93)) | (1L << (K_IS - 93)) | (1L << (K_ISNULL - 93)) | (1L << (K_JOIN - 93)) | (1L << (K_KEY - 93)) | (1L << (K_LEFT - 93)) | (1L << (K_LIKE - 93)) | (1L << (K_LIMIT - 93)) | (1L << (K_MATCH - 93)) | (1L << (K_NATURAL - 93)) | (1L << (K_NO - 93)) | (1L << (K_NOT - 93)) | (1L << (K_NOTNULL - 93)) | (1L << (K_NULL - 93)) | (1L << (K_OF - 93)) | (1L << (K_OFFSET - 93)) | (1L << (K_ON - 93)) | (1L << (K_OR - 93)) | (1L << (K_ORDER - 93)) | (1L << (K_OUTER - 93)) | (1L << (K_PLAN - 93)) | (1L << (K_PRAGMA - 93)) | (1L << (K_PRIMARY - 93)) | (1L << (K_QUERY - 93)) | (1L << (K_RAISE - 93)) | (1L << (K_RECURSIVE - 93)) | (1L << (K_REFERENCES - 93)) | (1L << (K_REGEXP - 93)) | (1L << (K_REINDEX - 93)) | (1L << (K_RELEASE - 93)) | (1L << (K_RENAME - 93)) | (1L << (K_REPLACE - 93)) | (1L << (K_RESTRICT - 93)) | (1L << (K_RIGHT - 93)) | (1L << (K_ROLLBACK - 93)) | (1L << (K_ROW - 93)) | (1L << (K_SAVEPOINT - 93)) | (1L << (K_SELECT - 93)) | (1L << (K_SET - 93)) | (1L << (K_TABLE - 93)) | (1L << (K_TEMP - 93)) | (1L << (K_TEMPORARY - 93)) | (1L << (K_THEN - 93)) | (1L << (K_TO - 93)) | (1L << (K_TRANSACTION - 93)) | (1L << (K_TRIGGER - 93)) | (1L << (K_UNION - 93)) | (1L << (K_UNIQUE - 93)) | (1L << (K_UPDATE - 93)) | (1L << (K_USING - 93)) | (1L << (K_VACUUM - 93)) | (1L << (K_VALUES - 93)) | (1L << (K_VIEW - 93)) | (1L << (K_VIRTUAL - 93)) | (1L << (K_WHEN - 93)) | (1L << (K_WHERE - 93)) | (1L << (K_WITH - 93)) | (1L << (K_WITHOUT - 93)))) != 0)) ) {
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

	public static class NameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitName(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1954);
			any_name();
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

	public static class Function_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Function_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterFunction_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitFunction_name(this);
		}
	}

	public final Function_nameContext function_name() throws RecognitionException {
		Function_nameContext _localctx = new Function_nameContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_function_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1956);
			any_name();
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

	public static class Database_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Database_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_database_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterDatabase_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitDatabase_name(this);
		}
	}

	public final Database_nameContext database_name() throws RecognitionException {
		Database_nameContext _localctx = new Database_nameContext(_ctx, getState());
		enterRule(_localctx, 198, RULE_database_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1958);
			any_name();
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

	public static class Table_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Table_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_table_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterTable_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitTable_name(this);
		}
	}

	public final Table_nameContext table_name() throws RecognitionException {
		Table_nameContext _localctx = new Table_nameContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_table_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1960);
			any_name();
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

	public static class Table_or_index_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Table_or_index_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_table_or_index_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterTable_or_index_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitTable_or_index_name(this);
		}
	}

	public final Table_or_index_nameContext table_or_index_name() throws RecognitionException {
		Table_or_index_nameContext _localctx = new Table_or_index_nameContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_table_or_index_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1962);
			any_name();
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

	public static class New_table_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public New_table_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_new_table_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterNew_table_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitNew_table_name(this);
		}
	}

	public final New_table_nameContext new_table_name() throws RecognitionException {
		New_table_nameContext _localctx = new New_table_nameContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_new_table_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1964);
			any_name();
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

	public static class Column_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Column_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterColumn_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitColumn_name(this);
		}
	}

	public final Column_nameContext column_name() throws RecognitionException {
		Column_nameContext _localctx = new Column_nameContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_column_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1966);
			any_name();
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

	public static class Collation_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Collation_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_collation_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterCollation_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitCollation_name(this);
		}
	}

	public final Collation_nameContext collation_name() throws RecognitionException {
		Collation_nameContext _localctx = new Collation_nameContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_collation_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1968);
			any_name();
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

	public static class Foreign_tableContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Foreign_tableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_foreign_table; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterForeign_table(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitForeign_table(this);
		}
	}

	public final Foreign_tableContext foreign_table() throws RecognitionException {
		Foreign_tableContext _localctx = new Foreign_tableContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_foreign_table);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1970);
			any_name();
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

	public static class Index_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Index_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_index_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterIndex_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitIndex_name(this);
		}
	}

	public final Index_nameContext index_name() throws RecognitionException {
		Index_nameContext _localctx = new Index_nameContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_index_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1972);
			any_name();
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

	public static class Trigger_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Trigger_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_trigger_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterTrigger_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitTrigger_name(this);
		}
	}

	public final Trigger_nameContext trigger_name() throws RecognitionException {
		Trigger_nameContext _localctx = new Trigger_nameContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_trigger_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1974);
			any_name();
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

	public static class View_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public View_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_view_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterView_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitView_name(this);
		}
	}

	public final View_nameContext view_name() throws RecognitionException {
		View_nameContext _localctx = new View_nameContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_view_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1976);
			any_name();
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

	public static class Module_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Module_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterModule_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitModule_name(this);
		}
	}

	public final Module_nameContext module_name() throws RecognitionException {
		Module_nameContext _localctx = new Module_nameContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_module_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1978);
			any_name();
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

	public static class Pragma_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Pragma_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pragma_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterPragma_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitPragma_name(this);
		}
	}

	public final Pragma_nameContext pragma_name() throws RecognitionException {
		Pragma_nameContext _localctx = new Pragma_nameContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_pragma_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1980);
			any_name();
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

	public static class Savepoint_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Savepoint_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_savepoint_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterSavepoint_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitSavepoint_name(this);
		}
	}

	public final Savepoint_nameContext savepoint_name() throws RecognitionException {
		Savepoint_nameContext _localctx = new Savepoint_nameContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_savepoint_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1982);
			any_name();
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

	public static class Table_aliasContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Table_aliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_table_alias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterTable_alias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitTable_alias(this);
		}
	}

	public final Table_aliasContext table_alias() throws RecognitionException {
		Table_aliasContext _localctx = new Table_aliasContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_table_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1984);
			any_name();
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

	public static class Transaction_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Transaction_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transaction_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterTransaction_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitTransaction_name(this);
		}
	}

	public final Transaction_nameContext transaction_name() throws RecognitionException {
		Transaction_nameContext _localctx = new Transaction_nameContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_transaction_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1986);
			any_name();
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

	public static class Any_nameContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(SQLGrammarParser.IDENTIFIER, 0); }
		public KeywordContext keyword() {
			return getRuleContext(KeywordContext.class,0);
		}
		public TerminalNode STRING_LITERAL() { return getToken(SQLGrammarParser.STRING_LITERAL, 0); }
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Any_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_any_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).enterAny_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SQLGrammarListener ) ((SQLGrammarListener)listener).exitAny_name(this);
		}
	}

	public final Any_nameContext any_name() throws RecognitionException {
		Any_nameContext _localctx = new Any_nameContext(_ctx, getState());
		enterRule(_localctx, 228, RULE_any_name);
		try {
			setState(1995);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(1988);
				match(IDENTIFIER);
				}
				break;
			case K_ABORT:
			case K_ACTION:
			case K_ADD:
			case K_AFTER:
			case K_ALL:
			case K_ALTER:
			case K_ANALYZE:
			case K_AND:
			case K_AS:
			case K_ASC:
			case K_ATTACH:
			case K_AUTOINCREMENT:
			case K_BEFORE:
			case K_BEGIN:
			case K_BETWEEN:
			case K_BY:
			case K_CASCADE:
			case K_CASE:
			case K_CAST:
			case K_CHECK:
			case K_COLLATE:
			case K_COLUMN:
			case K_COMMIT:
			case K_CONFLICT:
			case K_CONSTRAINT:
			case K_CREATE:
			case K_CROSS:
			case K_CURRENT_DATE:
			case K_CURRENT_TIME:
			case K_CURRENT_TIMESTAMP:
			case K_DATABASE:
			case K_DEFAULT:
			case K_DEFERRABLE:
			case K_DEFERRED:
			case K_DELETE:
			case K_DESC:
			case K_DETACH:
			case K_DISTINCT:
			case K_DROP:
			case K_EACH:
			case K_ELSE:
			case K_END:
			case K_ESCAPE:
			case K_EXCEPT:
			case K_EXCLUSIVE:
			case K_EXISTS:
			case K_EXPLAIN:
			case K_FAIL:
			case K_FOR:
			case K_FOREIGN:
			case K_FROM:
			case K_FULL:
			case K_GLOB:
			case K_GROUP:
			case K_HAVING:
			case K_IF:
			case K_IGNORE:
			case K_IMMEDIATE:
			case K_IN:
			case K_INDEX:
			case K_INDEXED:
			case K_INITIALLY:
			case K_INNER:
			case K_INSERT:
			case K_INSTEAD:
			case K_INTERSECT:
			case K_INTO:
			case K_IS:
			case K_ISNULL:
			case K_JOIN:
			case K_KEY:
			case K_LEFT:
			case K_LIKE:
			case K_LIMIT:
			case K_MATCH:
			case K_NATURAL:
			case K_NO:
			case K_NOT:
			case K_NOTNULL:
			case K_NULL:
			case K_OF:
			case K_OFFSET:
			case K_ON:
			case K_OR:
			case K_ORDER:
			case K_OUTER:
			case K_PLAN:
			case K_PRAGMA:
			case K_PRIMARY:
			case K_QUERY:
			case K_RAISE:
			case K_RECURSIVE:
			case K_REFERENCES:
			case K_REGEXP:
			case K_REINDEX:
			case K_RELEASE:
			case K_RENAME:
			case K_REPLACE:
			case K_RESTRICT:
			case K_RIGHT:
			case K_ROLLBACK:
			case K_ROW:
			case K_SAVEPOINT:
			case K_SELECT:
			case K_SET:
			case K_TABLE:
			case K_TEMP:
			case K_TEMPORARY:
			case K_THEN:
			case K_TO:
			case K_TRANSACTION:
			case K_TRIGGER:
			case K_UNION:
			case K_UNIQUE:
			case K_UPDATE:
			case K_USING:
			case K_VACUUM:
			case K_VALUES:
			case K_VIEW:
			case K_VIRTUAL:
			case K_WHEN:
			case K_WHERE:
			case K_WITH:
			case K_WITHOUT:
				enterOuterAlt(_localctx, 2);
				{
				setState(1989);
				keyword();
				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 3);
				{
				setState(1990);
				match(STRING_LITERAL);
				}
				break;
			case OPEN_PAR:
				enterOuterAlt(_localctx, 4);
				{
				setState(1991);
				match(OPEN_PAR);
				setState(1992);
				any_name();
				setState(1993);
				match(CLOSE_PAR);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 72:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 20);
		case 1:
			return precpred(_ctx, 19);
		case 2:
			return precpred(_ctx, 18);
		case 3:
			return precpred(_ctx, 17);
		case 4:
			return precpred(_ctx, 16);
		case 5:
			return precpred(_ctx, 15);
		case 6:
			return precpred(_ctx, 14);
		case 7:
			return precpred(_ctx, 13);
		case 8:
			return precpred(_ctx, 6);
		case 9:
			return precpred(_ctx, 5);
		case 10:
			return precpred(_ctx, 9);
		case 11:
			return precpred(_ctx, 8);
		case 12:
			return precpred(_ctx, 7);
		case 13:
			return precpred(_ctx, 4);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u00a3\u07d0\4\2\t"+
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
		"k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\3\2\3\2\3\2\3"+
		"\2\3\2\5\2\u00ee\n\2\3\2\5\2\u00f1\n\2\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\5"+
		"\3\5\3\5\3\5\3\5\7\5\u00ff\n\5\f\5\16\5\u0102\13\5\3\6\3\6\5\6\u0106\n"+
		"\6\3\7\3\7\3\7\5\7\u010b\n\7\3\b\3\b\3\b\3\b\3\b\3\b\7\b\u0113\n\b\f\b"+
		"\16\b\u0116\13\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\7\t\u0120\n\t\f\t\16"+
		"\t\u0123\13\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u012e\n\n\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\5\13\u0144\n\13\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3"+
		"\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3"+
		"\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3"+
		"\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\7\24\u0171\n\24\f\24\16\24\u0174"+
		"\13\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\7\25\u017f\n\25\f"+
		"\25\16\25\u0182\13\25\3\25\3\25\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27"+
		"\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\33"+
		"\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3"+
		" \3!\3!\5!\u01ad\n!\3\"\3\"\3#\3#\7#\u01b3\n#\f#\16#\u01b6\13#\3#\3#\3"+
		"$\3$\3$\3%\7%\u01be\n%\f%\16%\u01c1\13%\3%\3%\6%\u01c5\n%\r%\16%\u01c6"+
		"\3%\7%\u01ca\n%\f%\16%\u01cd\13%\3%\7%\u01d0\n%\f%\16%\u01d3\13%\3&\3"+
		"&\3&\5&\u01d8\n&\5&\u01da\n&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3"+
		"&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\5&\u01fa\n&\3\'\3\'"+
		"\3\'\3\'\3\'\5\'\u0201\n\'\3\'\3\'\3\'\3\'\3\'\3\'\5\'\u0209\n\'\3\'\5"+
		"\'\u020c\n\'\3(\3(\3(\3(\3(\3(\3(\5(\u0215\n(\3)\3)\5)\u0219\n)\3)\3)"+
		"\3)\3)\3*\3*\5*\u0221\n*\3*\3*\5*\u0225\n*\5*\u0227\n*\3+\3+\3+\5+\u022c"+
		"\n+\5+\u022e\n+\3,\3,\5,\u0232\n,\3,\3,\3,\7,\u0237\n,\f,\16,\u023a\13"+
		",\5,\u023c\n,\3,\3,\3,\5,\u0241\n,\3,\3,\5,\u0245\n,\3,\6,\u0248\n,\r"+
		",\16,\u0249\3,\3,\3,\3,\3,\7,\u0251\n,\f,\16,\u0254\13,\5,\u0256\n,\3"+
		",\3,\3,\3,\5,\u025c\n,\5,\u025e\n,\3-\3-\5-\u0262\n-\3-\3-\3-\3-\5-\u0268"+
		"\n-\3-\3-\3-\5-\u026d\n-\3-\3-\3-\3-\3-\3-\3-\7-\u0276\n-\f-\16-\u0279"+
		"\13-\3-\3-\3-\5-\u027e\n-\3.\3.\5.\u0282\n.\3.\3.\3.\3.\5.\u0288\n.\3"+
		".\3.\3.\5.\u028d\n.\3.\3.\3.\3.\3.\7.\u0294\n.\f.\16.\u0297\13.\3.\3."+
		"\7.\u029b\n.\f.\16.\u029e\13.\3.\3.\3.\5.\u02a3\n.\3.\3.\5.\u02a7\n.\3"+
		"/\3/\5/\u02ab\n/\3/\3/\3/\3/\5/\u02b1\n/\3/\3/\3/\5/\u02b6\n/\3/\3/\3"+
		"/\3/\3/\5/\u02bd\n/\3/\3/\3/\3/\3/\3/\3/\7/\u02c6\n/\f/\16/\u02c9\13/"+
		"\5/\u02cb\n/\5/\u02cd\n/\3/\3/\3/\3/\5/\u02d3\n/\3/\3/\3/\3/\5/\u02d9"+
		"\n/\3/\3/\5/\u02dd\n/\3/\3/\3/\3/\3/\5/\u02e4\n/\3/\3/\6/\u02e8\n/\r/"+
		"\16/\u02e9\3/\3/\3\60\3\60\5\60\u02f0\n\60\3\60\3\60\3\60\3\60\5\60\u02f6"+
		"\n\60\3\60\3\60\3\60\5\60\u02fb\n\60\3\60\3\60\3\60\3\60\3\61\3\61\3\61"+
		"\3\61\3\61\3\61\5\61\u0307\n\61\3\61\3\61\3\61\5\61\u030c\n\61\3\61\3"+
		"\61\3\61\3\61\3\61\3\61\3\61\7\61\u0315\n\61\f\61\16\61\u0318\13\61\3"+
		"\61\3\61\5\61\u031c\n\61\3\62\5\62\u031f\n\62\3\62\3\62\3\62\3\62\3\62"+
		"\5\62\u0326\n\62\3\63\5\63\u0329\n\63\3\63\3\63\3\63\3\63\3\63\5\63\u0330"+
		"\n\63\3\63\3\63\3\63\3\63\3\63\7\63\u0337\n\63\f\63\16\63\u033a\13\63"+
		"\5\63\u033c\n\63\3\63\3\63\3\63\3\63\5\63\u0342\n\63\5\63\u0344\n\63\3"+
		"\64\3\64\5\64\u0348\n\64\3\64\3\64\3\65\3\65\3\65\3\65\5\65\u0350\n\65"+
		"\3\65\3\65\3\65\5\65\u0355\n\65\3\65\3\65\3\66\3\66\3\66\3\66\5\66\u035d"+
		"\n\66\3\66\3\66\3\66\5\66\u0362\n\66\3\66\3\66\3\67\3\67\3\67\3\67\5\67"+
		"\u036a\n\67\3\67\3\67\3\67\5\67\u036f\n\67\3\67\3\67\38\38\38\38\58\u0377"+
		"\n8\38\38\38\58\u037c\n8\38\38\39\39\59\u0382\n9\39\39\39\79\u0387\n9"+
		"\f9\169\u038a\139\59\u038c\n9\39\39\39\39\79\u0392\n9\f9\169\u0395\13"+
		"9\39\39\39\39\39\79\u039c\n9\f9\169\u039f\139\59\u03a1\n9\39\39\39\39"+
		"\59\u03a7\n9\59\u03a9\n9\3:\5:\u03ac\n:\3:\3:\3:\3:\3:\3:\3:\3:\3:\3:"+
		"\3:\3:\3:\3:\3:\3:\3:\5:\u03bf\n:\3:\3:\3:\3:\5:\u03c5\n:\3:\3:\3:\3:"+
		"\3:\7:\u03cc\n:\f:\16:\u03cf\13:\3:\3:\5:\u03d3\n:\3:\3:\3:\3:\3:\7:\u03da"+
		"\n:\f:\16:\u03dd\13:\3:\3:\3:\3:\3:\3:\7:\u03e5\n:\f:\16:\u03e8\13:\3"+
		":\3:\7:\u03ec\n:\f:\16:\u03ef\13:\3:\3:\3:\5:\u03f4\n:\3;\3;\3;\3;\5;"+
		"\u03fa\n;\3;\3;\3;\3;\3;\3;\3;\5;\u0403\n;\3<\3<\3<\3<\3<\5<\u040a\n<"+
		"\3<\3<\5<\u040e\n<\5<\u0410\n<\3=\3=\5=\u0414\n=\3=\3=\3>\3>\3>\5>\u041b"+
		"\n>\5>\u041d\n>\3>\3>\5>\u0421\n>\3>\5>\u0424\n>\3?\3?\3?\3@\3@\5@\u042b"+
		"\n@\3@\3@\3@\7@\u0430\n@\f@\16@\u0433\13@\5@\u0435\n@\3@\3@\3@\3@\3@\3"+
		"@\7@\u043d\n@\f@\16@\u0440\13@\5@\u0442\n@\3@\3@\3@\3@\5@\u0448\n@\5@"+
		"\u044a\n@\3A\3A\5A\u044e\nA\3A\3A\3A\7A\u0453\nA\fA\16A\u0456\13A\5A\u0458"+
		"\nA\3A\3A\3A\3A\7A\u045e\nA\fA\16A\u0461\13A\3A\3A\3A\3A\3A\7A\u0468\n"+
		"A\fA\16A\u046b\13A\5A\u046d\nA\3A\3A\3A\3A\5A\u0473\nA\5A\u0475\nA\3B"+
		"\3B\5B\u0479\nB\3B\3B\3B\7B\u047e\nB\fB\16B\u0481\13B\3B\3B\3B\3B\7B\u0487"+
		"\nB\fB\16B\u048a\13B\3B\5B\u048d\nB\5B\u048f\nB\3B\3B\5B\u0493\nB\3B\3"+
		"B\3B\3B\3B\7B\u049a\nB\fB\16B\u049d\13B\3B\3B\5B\u04a1\nB\5B\u04a3\nB"+
		"\3B\3B\3B\3B\3B\7B\u04aa\nB\fB\16B\u04ad\13B\3B\3B\3B\3B\3B\3B\7B\u04b5"+
		"\nB\fB\16B\u04b8\13B\3B\3B\7B\u04bc\nB\fB\16B\u04bf\13B\5B\u04c1\nB\3"+
		"C\5C\u04c4\nC\3C\3C\3C\3C\3C\3C\3C\3C\3C\3C\3C\5C\u04d1\nC\3C\3C\3C\3"+
		"C\3C\3C\3C\3C\3C\3C\7C\u04dd\nC\fC\16C\u04e0\13C\3C\3C\5C\u04e4\nC\3D"+
		"\5D\u04e7\nD\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\5D\u04f4\nD\3D\3D\3D\3D"+
		"\3D\3D\3D\3D\3D\3D\7D\u0500\nD\fD\16D\u0503\13D\3D\3D\5D\u0507\nD\3D\3"+
		"D\3D\3D\3D\7D\u050e\nD\fD\16D\u0511\13D\5D\u0513\nD\3D\3D\3D\3D\5D\u0519"+
		"\nD\5D\u051b\nD\3E\3E\3F\3F\5F\u0521\nF\3F\7F\u0524\nF\fF\16F\u0527\13"+
		"F\3G\6G\u052a\nG\rG\16G\u052b\3G\3G\3G\3G\3G\3G\3G\3G\3G\3G\5G\u0538\n"+
		"G\3H\3H\5H\u053c\nH\3H\3H\3H\5H\u0541\nH\3H\3H\5H\u0545\nH\3H\5H\u0548"+
		"\nH\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\5H\u055a\nH\3H\3H"+
		"\3H\5H\u055f\nH\3I\3I\3I\5I\u0564\nI\3J\3J\3J\3J\3J\3J\5J\u056c\nJ\3J"+
		"\3J\3J\5J\u0571\nJ\3J\3J\3J\3J\3J\3J\3J\5J\u057a\nJ\3J\3J\3J\7J\u057f"+
		"\nJ\fJ\16J\u0582\13J\3J\5J\u0585\nJ\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3"+
		"J\3J\3J\5J\u0595\nJ\3J\5J\u0598\nJ\3J\3J\3J\3J\3J\3J\5J\u05a0\nJ\3J\3"+
		"J\3J\3J\3J\6J\u05a7\nJ\rJ\16J\u05a8\3J\3J\5J\u05ad\nJ\3J\3J\3J\5J\u05b2"+
		"\nJ\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J"+
		"\3J\3J\3J\3J\3J\3J\5J\u05d0\nJ\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\5J\u05dc"+
		"\nJ\3J\3J\3J\5J\u05e1\nJ\3J\3J\3J\3J\3J\3J\3J\3J\3J\3J\5J\u05ed\nJ\3J"+
		"\3J\3J\3J\5J\u05f3\nJ\3J\3J\3J\3J\3J\5J\u05fa\nJ\3J\3J\5J\u05fe\nJ\3J"+
		"\3J\3J\3J\3J\3J\7J\u0606\nJ\fJ\16J\u0609\13J\5J\u060b\nJ\3J\3J\3J\3J\5"+
		"J\u0611\nJ\3J\5J\u0614\nJ\7J\u0616\nJ\fJ\16J\u0619\13J\3K\3K\3K\3K\3K"+
		"\3K\7K\u0621\nK\fK\16K\u0624\13K\3K\3K\5K\u0628\nK\3K\3K\3K\3K\3K\3K\3"+
		"K\3K\3K\3K\5K\u0634\nK\3K\3K\5K\u0638\nK\7K\u063a\nK\fK\16K\u063d\13K"+
		"\3K\5K\u0640\nK\3K\3K\3K\3K\3K\5K\u0647\nK\5K\u0649\nK\3L\3L\3L\3L\3L"+
		"\3L\5L\u0651\nL\3L\3L\3M\3M\3M\5M\u0658\nM\3M\5M\u065b\nM\3N\3N\5N\u065f"+
		"\nN\3N\3N\3N\5N\u0664\nN\3N\3N\3N\3N\7N\u066a\nN\fN\16N\u066d\13N\3N\3"+
		"N\3N\3N\3N\3N\3N\3N\3N\3N\3N\3N\3N\3N\7N\u067d\nN\fN\16N\u0680\13N\3N"+
		"\3N\3N\5N\u0685\nN\3O\3O\5O\u0689\nO\3O\3O\3O\3O\3O\3O\3O\3O\3O\3O\3O"+
		"\3O\7O\u0697\nO\fO\16O\u069a\13O\3P\3P\3P\5P\u069f\nP\3P\3P\3P\3P\3P\3"+
		"P\5P\u06a7\nP\3Q\3Q\3Q\5Q\u06ac\nQ\3Q\5Q\u06af\nQ\3R\3R\3R\5R\u06b4\n"+
		"R\3S\3S\3S\3S\3S\7S\u06bb\nS\fS\16S\u06be\13S\3S\3S\5S\u06c2\nS\3S\3S"+
		"\3S\3S\3S\3T\3T\3T\3T\3T\3T\3T\5T\u06d0\nT\3T\5T\u06d3\nT\5T\u06d5\nT"+
		"\3U\3U\3U\5U\u06da\nU\3U\3U\5U\u06de\nU\3U\5U\u06e1\nU\3U\3U\3U\3U\3U"+
		"\5U\u06e8\nU\3U\3U\3U\3U\7U\u06ee\nU\fU\16U\u06f1\13U\3U\5U\u06f4\nU\3"+
		"U\3U\5U\u06f8\nU\3U\5U\u06fb\nU\3U\3U\3U\3U\5U\u0701\nU\3U\5U\u0704\n"+
		"U\5U\u0706\nU\3V\3V\3V\3V\3V\7V\u070d\nV\fV\16V\u0710\13V\3W\3W\5W\u0714"+
		"\nW\3W\3W\5W\u0718\nW\3W\3W\5W\u071c\nW\3W\5W\u071f\nW\3X\3X\3X\3X\3X"+
		"\3X\3X\7X\u0728\nX\fX\16X\u072b\13X\3X\3X\5X\u072f\nX\3Y\3Y\5Y\u0733\n"+
		"Y\3Y\3Y\3Y\7Y\u0738\nY\fY\16Y\u073b\13Y\3Y\3Y\3Y\3Y\7Y\u0741\nY\fY\16"+
		"Y\u0744\13Y\3Y\5Y\u0747\nY\5Y\u0749\nY\3Y\3Y\5Y\u074d\nY\3Y\3Y\3Y\3Y\3"+
		"Y\7Y\u0754\nY\fY\16Y\u0757\13Y\3Y\3Y\5Y\u075b\nY\5Y\u075d\nY\3Y\3Y\3Y"+
		"\3Y\3Y\7Y\u0764\nY\fY\16Y\u0767\13Y\3Y\3Y\3Y\3Y\3Y\3Y\7Y\u076f\nY\fY\16"+
		"Y\u0772\13Y\3Y\3Y\7Y\u0776\nY\fY\16Y\u0779\13Y\5Y\u077b\nY\3Z\3Z\3Z\3"+
		"Z\3Z\5Z\u0782\nZ\3[\3[\3[\3[\3[\7[\u0789\n[\f[\16[\u078c\13[\3[\3[\5["+
		"\u0790\n[\3\\\5\\\u0793\n\\\3\\\3\\\3]\3]\3^\3^\3_\3_\3`\3`\5`\u079f\n"+
		"`\3a\3a\3b\3b\3c\3c\3d\3d\3e\3e\3f\3f\3g\3g\3h\3h\3i\3i\3j\3j\3k\3k\3"+
		"l\3l\3m\3m\3n\3n\3o\3o\3p\3p\3q\3q\3r\3r\3s\3s\3t\3t\3t\3t\3t\3t\3t\5"+
		"t\u07ce\nt\3t\2\3\u0092u\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&("+
		"*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084"+
		"\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c"+
		"\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4"+
		"\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc"+
		"\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4"+
		"\u00e6\2\25\4\2\u009b\u009b\u009e\u009e\5\2\6\6\u009c\u009c\u009e\u009e"+
		"\4\2((BB\5\2@@KKXX\4\2\65\65HH\4\2\13\13pp\3\2\u0089\u008a\4\2##DD\7\2"+
		"\37\37NNWW\u0080\u0080\u0083\u0083\4\2\r\r\22\23\3\2\16\17\3\2\24\27\3"+
		"\2\30\33\6\2SSggii||\4\2AA\u0091\u0091\5\2\37\37NN\u0083\u0083\6\2:<n"+
		"n\u009c\u009c\u009e\u009f\4\2\16\20ll\3\2\37\u009a\2\u08d8\2\u00e8\3\2"+
		"\2\2\4\u00f4\3\2\2\2\6\u00f6\3\2\2\2\b\u00f9\3\2\2\2\n\u0105\3\2\2\2\f"+
		"\u010a\3\2\2\2\16\u010c\3\2\2\2\20\u0119\3\2\2\2\22\u012d\3\2\2\2\24\u0143"+
		"\3\2\2\2\26\u0145\3\2\2\2\30\u0149\3\2\2\2\32\u014d\3\2\2\2\34\u0151\3"+
		"\2\2\2\36\u0155\3\2\2\2 \u0159\3\2\2\2\"\u015d\3\2\2\2$\u0163\3\2\2\2"+
		"&\u016a\3\2\2\2(\u0177\3\2\2\2*\u0185\3\2\2\2,\u0189\3\2\2\2.\u018d\3"+
		"\2\2\2\60\u0191\3\2\2\2\62\u0196\3\2\2\2\64\u019a\3\2\2\2\66\u01a0\3\2"+
		"\2\28\u01a2\3\2\2\2:\u01a4\3\2\2\2<\u01a6\3\2\2\2>\u01a8\3\2\2\2@\u01aa"+
		"\3\2\2\2B\u01ae\3\2\2\2D\u01b4\3\2\2\2F\u01b9\3\2\2\2H\u01bf\3\2\2\2J"+
		"\u01d9\3\2\2\2L\u01fb\3\2\2\2N\u020d\3\2\2\2P\u0216\3\2\2\2R\u021e\3\2"+
		"\2\2T\u0228\3\2\2\2V\u023b\3\2\2\2X\u025f\3\2\2\2Z\u027f\3\2\2\2\\\u02a8"+
		"\3\2\2\2^\u02ed\3\2\2\2`\u0300\3\2\2\2b\u031e\3\2\2\2d\u0328\3\2\2\2f"+
		"\u0345\3\2\2\2h\u034b\3\2\2\2j\u0358\3\2\2\2l\u0365\3\2\2\2n\u0372\3\2"+
		"\2\2p\u038b\3\2\2\2r\u03ab\3\2\2\2t\u03f5\3\2\2\2v\u0404\3\2\2\2x\u0411"+
		"\3\2\2\2z\u0417\3\2\2\2|\u0425\3\2\2\2~\u0434\3\2\2\2\u0080\u0457\3\2"+
		"\2\2\u0082\u04c0\3\2\2\2\u0084\u04c3\3\2\2\2\u0086\u04e6\3\2\2\2\u0088"+
		"\u051c\3\2\2\2\u008a\u051e\3\2\2\2\u008c\u0529\3\2\2\2\u008e\u053b\3\2"+
		"\2\2\u0090\u0563\3\2\2\2\u0092\u05b1\3\2\2\2\u0094\u061a\3\2\2\2\u0096"+
		"\u064a\3\2\2\2\u0098\u0654\3\2\2\2\u009a\u065e\3\2\2\2\u009c\u0686\3\2"+
		"\2\2\u009e\u069e\3\2\2\2\u00a0\u06a8\3\2\2\2\u00a2\u06b3\3\2\2\2\u00a4"+
		"\u06b5\3\2\2\2\u00a6\u06d4\3\2\2\2\u00a8\u0705\3\2\2\2\u00aa\u0707\3\2"+
		"\2\2\u00ac\u071e\3\2\2\2\u00ae\u072e\3\2\2\2\u00b0\u077a\3\2\2\2\u00b2"+
		"\u0781\3\2\2\2\u00b4\u0783\3\2\2\2\u00b6\u0792\3\2\2\2\u00b8\u0796\3\2"+
		"\2\2\u00ba\u0798\3\2\2\2\u00bc\u079a\3\2\2\2\u00be\u079e\3\2\2\2\u00c0"+
		"\u07a0\3\2\2\2\u00c2\u07a2\3\2\2\2\u00c4\u07a4\3\2\2\2\u00c6\u07a6\3\2"+
		"\2\2\u00c8\u07a8\3\2\2\2\u00ca\u07aa\3\2\2\2\u00cc\u07ac\3\2\2\2\u00ce"+
		"\u07ae\3\2\2\2\u00d0\u07b0\3\2\2\2\u00d2\u07b2\3\2\2\2\u00d4\u07b4\3\2"+
		"\2\2\u00d6\u07b6\3\2\2\2\u00d8\u07b8\3\2\2\2\u00da\u07ba\3\2\2\2\u00dc"+
		"\u07bc\3\2\2\2\u00de\u07be\3\2\2\2\u00e0\u07c0\3\2\2\2\u00e2\u07c2\3\2"+
		"\2\2\u00e4\u07c4\3\2\2\2\u00e6\u07cd\3\2\2\2\u00e8\u00e9\7\u0086\2\2\u00e9"+
		"\u00ea\7\r\2\2\u00ea\u00eb\7Q\2\2\u00eb\u00ed\5\4\3\2\u00ec\u00ee\5\6"+
		"\4\2\u00ed\u00ec\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee\u00f0\3\2\2\2\u00ef"+
		"\u00f1\5\b\5\2\u00f0\u00ef\3\2\2\2\u00f0\u00f1\3\2\2\2\u00f1\u00f2\3\2"+
		"\2\2\u00f2\u00f3\7\2\2\3\u00f3\3\3\2\2\2\u00f4\u00f5\t\2\2\2\u00f5\5\3"+
		"\2\2\2\u00f6\u00f7\7\u0098\2\2\u00f7\u00f8\5\n\6\2\u00f8\7\3\2\2\2\u00f9"+
		"\u00fa\7s\2\2\u00fa\u00fb\7.\2\2\u00fb\u0100\5@!\2\u00fc\u00fd\7\13\2"+
		"\2\u00fd\u00ff\5@!\2\u00fe\u00fc\3\2\2\2\u00ff\u0102\3\2\2\2\u0100\u00fe"+
		"\3\2\2\2\u0100\u0101\3\2\2\2\u0101\t\3\2\2\2\u0102\u0100\3\2\2\2\u0103"+
		"\u0106\5\f\7\2\u0104\u0106\5\24\13\2\u0105\u0103\3\2\2\2\u0105\u0104\3"+
		"\2\2\2\u0106\13\3\2\2\2\u0107\u010b\5\16\b\2\u0108\u010b\5\20\t\2\u0109"+
		"\u010b\5\22\n\2\u010a\u0107\3\2\2\2\u010a\u0108\3\2\2\2\u010a\u0109\3"+
		"\2\2\2\u010b\r\3\2\2\2\u010c\u010d\7\t\2\2\u010d\u010e\5\n\6\2\u010e\u010f"+
		"\7&\2\2\u010f\u0114\5\n\6\2\u0110\u0111\7&\2\2\u0111\u0113\5\n\6\2\u0112"+
		"\u0110\3\2\2\2\u0113\u0116\3\2\2\2\u0114\u0112\3\2\2\2\u0114\u0115\3\2"+
		"\2\2\u0115\u0117\3\2\2\2\u0116\u0114\3\2\2\2\u0117\u0118\7\n\2\2\u0118"+
		"\17\3\2\2\2\u0119\u011a\7\t\2\2\u011a\u011b\5\n\6\2\u011b\u011c\7r\2\2"+
		"\u011c\u0121\5\n\6\2\u011d\u011e\7r\2\2\u011e\u0120\5\n\6\2\u011f\u011d"+
		"\3\2\2\2\u0120\u0123\3\2\2\2\u0121\u011f\3\2\2\2\u0121\u0122\3\2\2\2\u0122"+
		"\u0124\3\2\2\2\u0123\u0121\3\2\2\2\u0124\u0125\7\n\2\2\u0125\21\3\2\2"+
		"\2\u0126\u0127\7l\2\2\u0127\u012e\5\n\6\2\u0128\u0129\7\t\2\2\u0129\u012a"+
		"\7l\2\2\u012a\u012b\5\n\6\2\u012b\u012c\7\n\2\2\u012c\u012e\3\2\2\2\u012d"+
		"\u0126\3\2\2\2\u012d\u0128\3\2\2\2\u012e\23\3\2\2\2\u012f\u0144\5\26\f"+
		"\2\u0130\u0144\5\30\r\2\u0131\u0144\5\32\16\2\u0132\u0144\5\34\17\2\u0133"+
		"\u0144\5\36\20\2\u0134\u0144\5 \21\2\u0135\u0144\5\"\22\2\u0136\u0144"+
		"\5$\23\2\u0137\u0144\5&\24\2\u0138\u0144\5(\25\2\u0139\u0144\5*\26\2\u013a"+
		"\u0144\5,\27\2\u013b\u0144\5.\30\2\u013c\u0144\5\60\31\2\u013d\u0144\5"+
		"\62\32\2\u013e\u0144\5\64\33\2\u013f\u0140\7\t\2\2\u0140\u0141\5\24\13"+
		"\2\u0141\u0142\7\n\2\2\u0142\u0144\3\2\2\2\u0143\u012f\3\2\2\2\u0143\u0130"+
		"\3\2\2\2\u0143\u0131\3\2\2\2\u0143\u0132\3\2\2\2\u0143\u0133\3\2\2\2\u0143"+
		"\u0134\3\2\2\2\u0143\u0135\3\2\2\2\u0143\u0136\3\2\2\2\u0143\u0137\3\2"+
		"\2\2\u0143\u0138\3\2\2\2\u0143\u0139\3\2\2\2\u0143\u013a\3\2\2\2\u0143"+
		"\u013b\3\2\2\2\u0143\u013c\3\2\2\2\u0143\u013d\3\2\2\2\u0143\u013e\3\2"+
		"\2\2\u0143\u013f\3\2\2\2\u0144\25\3\2\2\2\u0145\u0146\5\66\34\2\u0146"+
		"\u0147\7\f\2\2\u0147\u0148\5> \2\u0148\27\3\2\2\2\u0149\u014a\5\66\34"+
		"\2\u014a\u014b\7\36\2\2\u014b\u014c\5> \2\u014c\31\3\2\2\2\u014d\u014e"+
		"\5\66\34\2\u014e\u014f\7\31\2\2\u014f\u0150\5> \2\u0150\33\3\2\2\2\u0151"+
		"\u0152\5\66\34\2\u0152\u0153\7\30\2\2\u0153\u0154\5> \2\u0154\35\3\2\2"+
		"\2\u0155\u0156\5\66\34\2\u0156\u0157\7\33\2\2\u0157\u0158\5> \2\u0158"+
		"\37\3\2\2\2\u0159\u015a\5\66\34\2\u015a\u015b\7\32\2\2\u015b\u015c\5>"+
		" \2\u015c!\3\2\2\2\u015d\u015e\5\66\34\2\u015e\u015f\7-\2\2\u015f\u0160"+
		"\5> \2\u0160\u0161\7&\2\2\u0161\u0162\5> \2\u0162#\3\2\2\2\u0163\u0164"+
		"\5\66\34\2\u0164\u0165\7l\2\2\u0165\u0166\7-\2\2\u0166\u0167\5> \2\u0167"+
		"\u0168\7&\2\2\u0168\u0169\5> \2\u0169%\3\2\2\2\u016a\u016b\5\66\34\2\u016b"+
		"\u016c\7Y\2\2\u016c\u016d\7\t\2\2\u016d\u0172\5> \2\u016e\u016f\7\13\2"+
		"\2\u016f\u0171\5> \2\u0170\u016e\3\2\2\2\u0171\u0174\3\2\2\2\u0172\u0170"+
		"\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u0175\3\2\2\2\u0174\u0172\3\2\2\2\u0175"+
		"\u0176\7\n\2\2\u0176\'\3\2\2\2\u0177\u0178\5\66\34\2\u0178\u0179\7l\2"+
		"\2\u0179\u017a\7Y\2\2\u017a\u017b\7\t\2\2\u017b\u0180\5> \2\u017c\u017d"+
		"\7\13\2\2\u017d\u017f\5> \2\u017e\u017c\3\2\2\2\u017f\u0182\3\2\2\2\u0180"+
		"\u017e\3\2\2\2\u0180\u0181\3\2\2\2\u0181\u0183\3\2\2\2\u0182\u0180\3\2"+
		"\2\2\u0183\u0184\7\n\2\2\u0184)\3\2\2\2\u0185\u0186\5\66\34\2\u0186\u0187"+
		"\7g\2\2\u0187\u0188\58\35\2\u0188+\3\2\2\2\u0189\u018a\5\66\34\2\u018a"+
		"\u018b\7g\2\2\u018b\u018c\5:\36\2\u018c-\3\2\2\2\u018d\u018e\5\66\34\2"+
		"\u018e\u018f\7g\2\2\u018f\u0190\5<\37\2\u0190/\3\2\2\2\u0191\u0192\5\66"+
		"\34\2\u0192\u0193\7b\2\2\u0193\u0194\7l\2\2\u0194\u0195\7n\2\2\u0195\61"+
		"\3\2\2\2\u0196\u0197\5\66\34\2\u0197\u0198\7b\2\2\u0198\u0199\7n\2\2\u0199"+
		"\63\3\2\2\2\u019a\u019b\5> \2\u019b\u019c\7g\2\2\u019c\u019d\5\66\34\2"+
		"\u019d\u019e\7\21\2\2\u019e\u019f\58\35\2\u019f\65\3\2\2\2\u01a0\u01a1"+
		"\t\2\2\2\u01a1\67\3\2\2\2\u01a2\u01a3\7\3\2\2\u01a39\3\2\2\2\u01a4\u01a5"+
		"\7\4\2\2\u01a5;\3\2\2\2\u01a6\u01a7\7\5\2\2\u01a7=\3\2\2\2\u01a8\u01a9"+
		"\t\3\2\2\u01a9?\3\2\2\2\u01aa\u01ac\5\66\34\2\u01ab\u01ad\5B\"\2\u01ac"+
		"\u01ab\3\2\2\2\u01ac\u01ad\3\2\2\2\u01adA\3\2\2\2\u01ae\u01af\t\4\2\2"+
		"\u01afC\3\2\2\2\u01b0\u01b3\5H%\2\u01b1\u01b3\5F$\2\u01b2\u01b0\3\2\2"+
		"\2\u01b2\u01b1\3\2\2\2\u01b3\u01b6\3\2\2\2\u01b4\u01b2\3\2\2\2\u01b4\u01b5"+
		"\3\2\2\2\u01b5\u01b7\3\2\2\2\u01b6\u01b4\3\2\2\2\u01b7\u01b8\7\2\2\3\u01b8"+
		"E\3\2\2\2\u01b9\u01ba\7\u00a3\2\2\u01ba\u01bb\b$\1\2\u01bbG\3\2\2\2\u01bc"+
		"\u01be\7\7\2\2\u01bd\u01bc\3\2\2\2\u01be\u01c1\3\2\2\2\u01bf\u01bd\3\2"+
		"\2\2\u01bf\u01c0\3\2\2\2\u01c0\u01c2\3\2\2\2\u01c1\u01bf\3\2\2\2\u01c2"+
		"\u01cb\5J&\2\u01c3\u01c5\7\7\2\2\u01c4\u01c3\3\2\2\2\u01c5\u01c6\3\2\2"+
		"\2\u01c6\u01c4\3\2\2\2\u01c6\u01c7\3\2\2\2\u01c7\u01c8\3\2\2\2\u01c8\u01ca"+
		"\5J&\2\u01c9\u01c4\3\2\2\2\u01ca\u01cd\3\2\2\2\u01cb\u01c9\3\2\2\2\u01cb"+
		"\u01cc\3\2\2\2\u01cc\u01d1\3\2\2\2\u01cd\u01cb\3\2\2\2\u01ce\u01d0\7\7"+
		"\2\2\u01cf\u01ce\3\2\2\2\u01d0\u01d3\3\2\2\2\u01d1\u01cf\3\2\2\2\u01d1"+
		"\u01d2\3\2\2\2\u01d2I\3\2\2\2\u01d3\u01d1\3\2\2\2\u01d4\u01d7\7M\2\2\u01d5"+
		"\u01d6\7x\2\2\u01d6\u01d8\7u\2\2\u01d7\u01d5\3\2\2\2\u01d7\u01d8\3\2\2"+
		"\2\u01d8\u01da\3\2\2\2\u01d9\u01d4\3\2\2\2\u01d9\u01da\3\2\2\2\u01da\u01f9"+
		"\3\2\2\2\u01db\u01fa\5L\'\2\u01dc\u01fa\5N(\2\u01dd\u01fa\5P)\2\u01de"+
		"\u01fa\5R*\2\u01df\u01fa\5T+\2\u01e0\u01fa\5V,\2\u01e1\u01fa\5X-\2\u01e2"+
		"\u01fa\5Z.\2\u01e3\u01fa\5\\/\2\u01e4\u01fa\5^\60\2\u01e5\u01fa\5`\61"+
		"\2\u01e6\u01fa\5b\62\2\u01e7\u01fa\5d\63\2\u01e8\u01fa\5f\64\2\u01e9\u01fa"+
		"\5h\65\2\u01ea\u01fa\5j\66\2\u01eb\u01fa\5l\67\2\u01ec\u01fa\5n8\2\u01ed"+
		"\u01fa\5p9\2\u01ee\u01fa\5r:\2\u01ef\u01fa\5t;\2\u01f0\u01fa\5v<\2\u01f1"+
		"\u01fa\5x=\2\u01f2\u01fa\5z>\2\u01f3\u01fa\5|?\2\u01f4\u01fa\5~@\2\u01f5"+
		"\u01fa\5\u0080A\2\u01f6\u01fa\5\u0084C\2\u01f7\u01fa\5\u0086D\2\u01f8"+
		"\u01fa\5\u0088E\2\u01f9\u01db\3\2\2\2\u01f9\u01dc\3\2\2\2\u01f9\u01dd"+
		"\3\2\2\2\u01f9\u01de\3\2\2\2\u01f9\u01df\3\2\2\2\u01f9\u01e0\3\2\2\2\u01f9"+
		"\u01e1\3\2\2\2\u01f9\u01e2\3\2\2\2\u01f9\u01e3\3\2\2\2\u01f9\u01e4\3\2"+
		"\2\2\u01f9\u01e5\3\2\2\2\u01f9\u01e6\3\2\2\2\u01f9\u01e7\3\2\2\2\u01f9"+
		"\u01e8\3\2\2\2\u01f9\u01e9\3\2\2\2\u01f9\u01ea\3\2\2\2\u01f9\u01eb\3\2"+
		"\2\2\u01f9\u01ec\3\2\2\2\u01f9\u01ed\3\2\2\2\u01f9\u01ee\3\2\2\2\u01f9"+
		"\u01ef\3\2\2\2\u01f9\u01f0\3\2\2\2\u01f9\u01f1\3\2\2\2\u01f9\u01f2\3\2"+
		"\2\2\u01f9\u01f3\3\2\2\2\u01f9\u01f4\3\2\2\2\u01f9\u01f5\3\2\2\2\u01f9"+
		"\u01f6\3\2\2\2\u01f9\u01f7\3\2\2\2\u01f9\u01f8\3\2\2\2\u01faK\3\2\2\2"+
		"\u01fb\u01fc\7$\2\2\u01fc\u0200\7\u0088\2\2\u01fd\u01fe\5\u00c8e\2\u01fe"+
		"\u01ff\7\b\2\2\u01ff\u0201\3\2\2\2\u0200\u01fd\3\2\2\2\u0200\u0201\3\2"+
		"\2\2\u0201\u0202\3\2\2\2\u0202\u020b\5\u00caf\2\u0203\u0204\7\177\2\2"+
		"\u0204\u0205\7\u008c\2\2\u0205\u020c\5\u00ceh\2\u0206\u0208\7!\2\2\u0207"+
		"\u0209\7\64\2\2\u0208\u0207\3\2\2\2\u0208\u0209\3\2\2\2\u0209\u020a\3"+
		"\2\2\2\u020a\u020c\5\u008aF\2\u020b\u0203\3\2\2\2\u020b\u0206\3\2\2\2"+
		"\u020cM\3\2\2\2\u020d\u0214\7%\2\2\u020e\u0215\5\u00c8e\2\u020f\u0215"+
		"\5\u00ccg\2\u0210\u0211\5\u00c8e\2\u0211\u0212\7\b\2\2\u0212\u0213\5\u00cc"+
		"g\2\u0213\u0215\3\2\2\2\u0214\u020e\3\2\2\2\u0214\u020f\3\2\2\2\u0214"+
		"\u0210\3\2\2\2\u0214\u0215\3\2\2\2\u0215O\3\2\2\2\u0216\u0218\7)\2\2\u0217"+
		"\u0219\7=\2\2\u0218\u0217\3\2\2\2\u0218\u0219\3\2\2\2\u0219\u021a\3\2"+
		"\2\2\u021a\u021b\5\u0092J\2\u021b\u021c\7\'\2\2\u021c\u021d\5\u00c8e\2"+
		"\u021dQ\3\2\2\2\u021e\u0220\7,\2\2\u021f\u0221\t\5\2\2\u0220\u021f\3\2"+
		"\2\2\u0220\u0221\3\2\2\2\u0221\u0226\3\2\2\2\u0222\u0224\7\u008d\2\2\u0223"+
		"\u0225\5\u00e4s\2\u0224\u0223\3\2\2\2\u0224\u0225\3\2\2\2\u0225\u0227"+
		"\3\2\2\2\u0226\u0222\3\2\2\2\u0226\u0227\3\2\2\2\u0227S\3\2\2\2\u0228"+
		"\u022d\t\6\2\2\u0229\u022b\7\u008d\2\2\u022a\u022c\5\u00e4s\2\u022b\u022a"+
		"\3\2\2\2\u022b\u022c\3\2\2\2\u022c\u022e\3\2\2\2\u022d\u0229\3\2\2\2\u022d"+
		"\u022e\3\2\2\2\u022eU\3\2\2\2\u022f\u0231\7\u0099\2\2\u0230\u0232\7z\2"+
		"\2\u0231\u0230\3\2\2\2\u0231\u0232\3\2\2\2\u0232\u0233\3\2\2\2\u0233\u0238"+
		"\5\u00a4S\2\u0234\u0235\7\13\2\2\u0235\u0237\5\u00a4S\2\u0236\u0234\3"+
		"\2\2\2\u0237\u023a\3\2\2\2\u0238\u0236\3\2\2\2\u0238\u0239\3\2\2\2\u0239"+
		"\u023c\3\2\2\2\u023a\u0238\3\2\2\2\u023b\u022f\3\2\2\2\u023b\u023c\3\2"+
		"\2\2\u023c\u023d\3\2\2\2\u023d\u0247\5\u00b0Y\2\u023e\u0240\7\u008f\2"+
		"\2\u023f\u0241\7#\2\2\u0240\u023f\3\2\2\2\u0240\u0241\3\2\2\2\u0241\u0245"+
		"\3\2\2\2\u0242\u0245\7`\2\2\u0243\u0245\7J\2\2\u0244\u023e\3\2\2\2\u0244"+
		"\u0242\3\2\2\2\u0244\u0243\3\2\2\2\u0245\u0246\3\2\2\2\u0246\u0248\5\u00b0"+
		"Y\2\u0247\u0244\3\2\2\2\u0248\u0249\3\2\2\2\u0249\u0247\3\2\2\2\u0249"+
		"\u024a\3\2\2\2\u024a\u0255\3\2\2\2\u024b\u024c\7s\2\2\u024c\u024d\7.\2"+
		"\2\u024d\u0252\5\u00a0Q\2\u024e\u024f\7\13\2\2\u024f\u0251\5\u00a0Q\2"+
		"\u0250\u024e\3\2\2\2\u0251\u0254\3\2\2\2\u0252\u0250\3\2\2\2\u0252\u0253"+
		"\3\2\2\2\u0253\u0256\3\2\2\2\u0254\u0252\3\2\2\2\u0255\u024b\3\2\2\2\u0255"+
		"\u0256\3\2\2\2\u0256\u025d\3\2\2\2\u0257\u0258\7h\2\2\u0258\u025b\5\u0092"+
		"J\2\u0259\u025a\t\7\2\2\u025a\u025c\5\u0092J\2\u025b\u0259\3\2\2\2\u025b"+
		"\u025c\3\2\2\2\u025c\u025e\3\2\2\2\u025d\u0257\3\2\2\2\u025d\u025e\3\2"+
		"\2\2\u025eW\3\2\2\2\u025f\u0261\78\2\2\u0260\u0262\7\u0090\2\2\u0261\u0260"+
		"\3\2\2\2\u0261\u0262\3\2\2\2\u0262\u0263\3\2\2\2\u0263\u0267\7Z\2\2\u0264"+
		"\u0265\7V\2\2\u0265\u0266\7l\2\2\u0266\u0268\7L\2\2\u0267\u0264\3\2\2"+
		"\2\u0267\u0268\3\2\2\2\u0268\u026c\3\2\2\2\u0269\u026a\5\u00c8e\2\u026a"+
		"\u026b\7\b\2\2\u026b\u026d\3\2\2\2\u026c\u0269\3\2\2\2\u026c\u026d\3\2"+
		"\2\2\u026d\u026e\3\2\2\2\u026e\u026f\5\u00d6l\2\u026f\u0270\7q\2\2\u0270"+
		"\u0271\5\u00caf\2\u0271\u0272\7\t\2\2\u0272\u0277\5\u0098M\2\u0273\u0274"+
		"\7\13\2\2\u0274\u0276\5\u0098M\2\u0275\u0273\3\2\2\2\u0276\u0279\3\2\2"+
		"\2\u0277\u0275\3\2\2\2\u0277\u0278\3\2\2\2\u0278\u027a\3\2\2\2\u0279\u0277"+
		"\3\2\2\2\u027a\u027d\7\n\2\2\u027b\u027c\7\u0098\2\2\u027c\u027e\5\u0092"+
		"J\2\u027d\u027b\3\2\2\2\u027d\u027e\3\2\2\2\u027eY\3\2\2\2\u027f\u0281"+
		"\78\2\2\u0280\u0282\t\b\2\2\u0281\u0280\3\2\2\2\u0281\u0282\3\2\2\2\u0282"+
		"\u0283\3\2\2\2\u0283\u0287\7\u0088\2\2\u0284\u0285\7V\2\2\u0285\u0286"+
		"\7l\2\2\u0286\u0288\7L\2\2\u0287\u0284\3\2\2\2\u0287\u0288\3\2\2\2\u0288"+
		"\u028c\3\2\2\2\u0289\u028a\5\u00c8e\2\u028a\u028b\7\b\2\2\u028b\u028d"+
		"\3\2\2\2\u028c\u0289\3\2\2\2\u028c\u028d\3\2\2\2\u028d\u028e\3\2\2\2\u028e"+
		"\u02a6\5\u00caf\2\u028f\u0290\7\t\2\2\u0290\u0295\5\u008aF\2\u0291\u0292"+
		"\7\13\2\2\u0292\u0294\5\u008aF\2\u0293\u0291\3\2\2\2\u0294\u0297\3\2\2"+
		"\2\u0295\u0293\3\2\2\2\u0295\u0296\3\2\2\2\u0296\u029c\3\2\2\2\u0297\u0295"+
		"\3\2\2\2\u0298\u0299\7\13\2\2\u0299\u029b\5\u009aN\2\u029a\u0298\3\2\2"+
		"\2\u029b\u029e\3\2\2\2\u029c\u029a\3\2\2\2\u029c\u029d\3\2\2\2\u029d\u029f"+
		"\3\2\2\2\u029e\u029c\3\2\2\2\u029f\u02a2\7\n\2\2\u02a0\u02a1\7\u009a\2"+
		"\2\u02a1\u02a3\7\u009b\2\2\u02a2\u02a0\3\2\2\2\u02a2\u02a3\3\2\2\2\u02a3"+
		"\u02a7\3\2\2\2\u02a4\u02a5\7\'\2\2\u02a5\u02a7\5\u0080A\2\u02a6\u028f"+
		"\3\2\2\2\u02a6\u02a4\3\2\2\2\u02a7[\3\2\2\2\u02a8\u02aa\78\2\2\u02a9\u02ab"+
		"\t\b\2\2\u02aa\u02a9\3\2\2\2\u02aa\u02ab\3\2\2\2\u02ab\u02ac\3\2\2\2\u02ac"+
		"\u02b0\7\u008e\2\2\u02ad\u02ae\7V\2\2\u02ae\u02af\7l\2\2\u02af\u02b1\7"+
		"L\2\2\u02b0\u02ad\3\2\2\2\u02b0\u02b1\3\2\2\2\u02b1\u02b5\3\2\2\2\u02b2"+
		"\u02b3\5\u00c8e\2\u02b3\u02b4\7\b\2\2\u02b4\u02b6\3\2\2\2\u02b5\u02b2"+
		"\3\2\2\2\u02b5\u02b6\3\2\2\2\u02b6\u02b7\3\2\2\2\u02b7\u02bc\5\u00d8m"+
		"\2\u02b8\u02bd\7+\2\2\u02b9\u02bd\7\"\2\2\u02ba\u02bb\7_\2\2\u02bb\u02bd"+
		"\7o\2\2\u02bc\u02b8\3\2\2\2\u02bc\u02b9\3\2\2\2\u02bc\u02ba\3\2\2\2\u02bc"+
		"\u02bd\3\2\2\2\u02bd\u02cc\3\2\2\2\u02be\u02cd\7A\2\2\u02bf\u02cd\7^\2"+
		"\2\u02c0\u02ca\7\u0091\2\2\u02c1\u02c2\7o\2\2\u02c2\u02c7\5\u00d0i\2\u02c3"+
		"\u02c4\7\13\2\2\u02c4\u02c6\5\u00d0i\2\u02c5\u02c3\3\2\2\2\u02c6\u02c9"+
		"\3\2\2\2\u02c7\u02c5\3\2\2\2\u02c7\u02c8\3\2\2\2\u02c8\u02cb\3\2\2\2\u02c9"+
		"\u02c7\3\2\2\2\u02ca\u02c1\3\2\2\2\u02ca\u02cb\3\2\2\2\u02cb\u02cd\3\2"+
		"\2\2\u02cc\u02be\3\2\2\2\u02cc\u02bf\3\2\2\2\u02cc\u02c0\3\2\2\2\u02cd"+
		"\u02ce\3\2\2\2\u02ce\u02d2\7q\2\2\u02cf\u02d0\5\u00c8e\2\u02d0\u02d1\7"+
		"\b\2\2\u02d1\u02d3\3\2\2\2\u02d2\u02cf\3\2\2\2\u02d2\u02d3\3\2\2\2\u02d3"+
		"\u02d4\3\2\2\2\u02d4\u02d8\5\u00caf\2\u02d5\u02d6\7O\2\2\u02d6\u02d7\7"+
		"F\2\2\u02d7\u02d9\7\u0084\2\2\u02d8\u02d5\3\2\2\2\u02d8\u02d9\3\2\2\2"+
		"\u02d9\u02dc\3\2\2\2\u02da\u02db\7\u0097\2\2\u02db\u02dd\5\u0092J\2\u02dc"+
		"\u02da\3\2\2\2\u02dc\u02dd\3\2\2\2\u02dd\u02de\3\2\2\2\u02de\u02e7\7,"+
		"\2\2\u02df\u02e4\5\u0084C\2\u02e0\u02e4\5r:\2\u02e1\u02e4\5b\62\2\u02e2"+
		"\u02e4\5\u0080A\2\u02e3\u02df\3\2\2\2\u02e3\u02e0\3\2\2\2\u02e3\u02e1"+
		"\3\2\2\2\u02e3\u02e2\3\2\2\2\u02e4\u02e5\3\2\2\2\u02e5\u02e6\7\7\2\2\u02e6"+
		"\u02e8\3\2\2\2\u02e7\u02e3\3\2\2\2\u02e8\u02e9\3\2\2\2\u02e9\u02e7\3\2"+
		"\2\2\u02e9\u02ea\3\2\2\2\u02ea\u02eb\3\2\2\2\u02eb\u02ec\7H\2\2\u02ec"+
		"]\3\2\2\2\u02ed\u02ef\78\2\2\u02ee\u02f0\t\b\2\2\u02ef\u02ee\3\2\2\2\u02ef"+
		"\u02f0\3\2\2\2\u02f0\u02f1\3\2\2\2\u02f1\u02f5\7\u0095\2\2\u02f2\u02f3"+
		"\7V\2\2\u02f3\u02f4\7l\2\2\u02f4\u02f6\7L\2\2\u02f5\u02f2\3\2\2\2\u02f5"+
		"\u02f6\3\2\2\2\u02f6\u02fa\3\2\2\2\u02f7\u02f8\5\u00c8e\2\u02f8\u02f9"+
		"\7\b\2\2\u02f9\u02fb\3\2\2\2\u02fa\u02f7\3\2\2\2\u02fa\u02fb\3\2\2\2\u02fb"+
		"\u02fc\3\2\2\2\u02fc\u02fd\5\u00dan\2\u02fd\u02fe\7\'\2\2\u02fe\u02ff"+
		"\5\u0080A\2\u02ff_\3\2\2\2\u0300\u0301\78\2\2\u0301\u0302\7\u0096\2\2"+
		"\u0302\u0306\7\u0088\2\2\u0303\u0304\7V\2\2\u0304\u0305\7l\2\2\u0305\u0307"+
		"\7L\2\2\u0306\u0303\3\2\2\2\u0306\u0307\3\2\2\2\u0307\u030b\3\2\2\2\u0308"+
		"\u0309\5\u00c8e\2\u0309\u030a\7\b\2\2\u030a\u030c\3\2\2\2\u030b\u0308"+
		"\3\2\2\2\u030b\u030c\3\2\2\2\u030c\u030d\3\2\2\2\u030d\u030e\5\u00caf"+
		"\2\u030e\u030f\7\u0092\2\2\u030f\u031b\5\u00dco\2\u0310\u0311\7\t\2\2"+
		"\u0311\u0316\5\u00be`\2\u0312\u0313\7\13\2\2\u0313\u0315\5\u00be`\2\u0314"+
		"\u0312\3\2\2\2\u0315\u0318\3\2\2\2\u0316\u0314\3\2\2\2\u0316\u0317\3\2"+
		"\2\2\u0317\u0319\3\2\2\2\u0318\u0316\3\2\2\2\u0319\u031a\7\n\2\2\u031a"+
		"\u031c\3\2\2\2\u031b\u0310\3\2\2\2\u031b\u031c\3\2\2\2\u031ca\3\2\2\2"+
		"\u031d\u031f\5\u009cO\2\u031e\u031d\3\2\2\2\u031e\u031f\3\2\2\2\u031f"+
		"\u0320\3\2\2\2\u0320\u0321\7A\2\2\u0321\u0322\7Q\2\2\u0322\u0325\5\u009e"+
		"P\2\u0323\u0324\7\u0098\2\2\u0324\u0326\5\u0092J\2\u0325\u0323\3\2\2\2"+
		"\u0325\u0326\3\2\2\2\u0326c\3\2\2\2\u0327\u0329\5\u009cO\2\u0328\u0327"+
		"\3\2\2\2\u0328\u0329\3\2\2\2\u0329\u032a\3\2\2\2\u032a\u032b\7A\2\2\u032b"+
		"\u032c\7Q\2\2\u032c\u032f\5\u009eP\2\u032d\u032e\7\u0098\2\2\u032e\u0330"+
		"\5\u0092J\2\u032f\u032d\3\2\2\2\u032f\u0330\3\2\2\2\u0330\u0343\3\2\2"+
		"\2\u0331\u0332\7s\2\2\u0332\u0333\7.\2\2\u0333\u0338\5\u00a0Q\2\u0334"+
		"\u0335\7\13\2\2\u0335\u0337\5\u00a0Q\2\u0336\u0334\3\2\2\2\u0337\u033a"+
		"\3\2\2\2\u0338\u0336\3\2\2\2\u0338\u0339\3\2\2\2\u0339\u033c\3\2\2\2\u033a"+
		"\u0338\3\2\2\2\u033b\u0331\3\2\2\2\u033b\u033c\3\2\2\2\u033c\u033d\3\2"+
		"\2\2\u033d\u033e\7h\2\2\u033e\u0341\5\u0092J\2\u033f\u0340\t\7\2\2\u0340"+
		"\u0342\5\u0092J\2\u0341\u033f\3\2\2\2\u0341\u0342\3\2\2\2\u0342\u0344"+
		"\3\2\2\2\u0343\u033b\3\2\2\2\u0343\u0344\3\2\2\2\u0344e\3\2\2\2\u0345"+
		"\u0347\7C\2\2\u0346\u0348\7=\2\2\u0347\u0346\3\2\2\2\u0347\u0348\3\2\2"+
		"\2\u0348\u0349\3\2\2\2\u0349\u034a\5\u00c8e\2\u034ag\3\2\2\2\u034b\u034c"+
		"\7E\2\2\u034c\u034f\7Z\2\2\u034d\u034e\7V\2\2\u034e\u0350\7L\2\2\u034f"+
		"\u034d\3\2\2\2\u034f\u0350\3\2\2\2\u0350\u0354\3\2\2\2\u0351\u0352\5\u00c8"+
		"e\2\u0352\u0353\7\b\2\2\u0353\u0355\3\2\2\2\u0354\u0351\3\2\2\2\u0354"+
		"\u0355\3\2\2\2\u0355\u0356\3\2\2\2\u0356\u0357\5\u00d6l\2\u0357i\3\2\2"+
		"\2\u0358\u0359\7E\2\2\u0359\u035c\7\u0088\2\2\u035a\u035b\7V\2\2\u035b"+
		"\u035d\7L\2\2\u035c\u035a\3\2\2\2\u035c\u035d\3\2\2\2\u035d\u0361\3\2"+
		"\2\2\u035e\u035f\5\u00c8e\2\u035f\u0360\7\b\2\2\u0360\u0362\3\2\2\2\u0361"+
		"\u035e\3\2\2\2\u0361\u0362\3\2\2\2\u0362\u0363\3\2\2\2\u0363\u0364\5\u00ca"+
		"f\2\u0364k\3\2\2\2\u0365\u0366\7E\2\2\u0366\u0369\7\u008e\2\2\u0367\u0368"+
		"\7V\2\2\u0368\u036a\7L\2\2\u0369\u0367\3\2\2\2\u0369\u036a\3\2\2\2\u036a"+
		"\u036e\3\2\2\2\u036b\u036c\5\u00c8e\2\u036c\u036d\7\b\2\2\u036d\u036f"+
		"\3\2\2\2\u036e\u036b\3\2\2\2\u036e\u036f\3\2\2\2\u036f\u0370\3\2\2\2\u0370"+
		"\u0371\5\u00d8m\2\u0371m\3\2\2\2\u0372\u0373\7E\2\2\u0373\u0376\7\u0095"+
		"\2\2\u0374\u0375\7V\2\2\u0375\u0377\7L\2\2\u0376\u0374\3\2\2\2\u0376\u0377"+
		"\3\2\2\2\u0377\u037b\3\2\2\2\u0378\u0379\5\u00c8e\2\u0379\u037a\7\b\2"+
		"\2\u037a\u037c\3\2\2\2\u037b\u0378\3\2\2\2\u037b\u037c\3\2\2\2\u037c\u037d"+
		"\3\2\2\2\u037d\u037e\5\u00dan\2\u037eo\3\2\2\2\u037f\u0381\7\u0099\2\2"+
		"\u0380\u0382\7z\2\2\u0381\u0380\3\2\2\2\u0381\u0382\3\2\2\2\u0382\u0383"+
		"\3\2\2\2\u0383\u0388\5\u00a4S\2\u0384\u0385\7\13\2\2\u0385\u0387\5\u00a4"+
		"S\2\u0386\u0384\3\2\2\2\u0387\u038a\3\2\2\2\u0388\u0386\3\2\2\2\u0388"+
		"\u0389\3\2\2\2\u0389\u038c\3\2\2\2\u038a\u0388\3\2\2\2\u038b\u037f\3\2"+
		"\2\2\u038b\u038c\3\2\2\2\u038c\u038d\3\2\2\2\u038d\u0393\5\u00b0Y\2\u038e"+
		"\u038f\5\u00b2Z\2\u038f\u0390\5\u00b0Y\2\u0390\u0392\3\2\2\2\u0391\u038e"+
		"\3\2\2\2\u0392\u0395\3\2\2\2\u0393\u0391\3\2\2\2\u0393\u0394\3\2\2\2\u0394"+
		"\u03a0\3\2\2\2\u0395\u0393\3\2\2\2\u0396\u0397\7s\2\2\u0397\u0398\7.\2"+
		"\2\u0398\u039d\5\u00a0Q\2\u0399\u039a\7\13\2\2\u039a\u039c\5\u00a0Q\2"+
		"\u039b\u0399\3\2\2\2\u039c\u039f\3\2\2\2\u039d\u039b\3\2\2\2\u039d\u039e"+
		"\3\2\2\2\u039e\u03a1\3\2\2\2\u039f\u039d\3\2\2\2\u03a0\u0396\3\2\2\2\u03a0"+
		"\u03a1\3\2\2\2\u03a1\u03a8\3\2\2\2\u03a2\u03a3\7h\2\2\u03a3\u03a6\5\u0092"+
		"J\2\u03a4\u03a5\t\7\2\2\u03a5\u03a7\5\u0092J\2\u03a6\u03a4\3\2\2\2\u03a6"+
		"\u03a7\3\2\2\2\u03a7\u03a9\3\2\2\2\u03a8\u03a2\3\2\2\2\u03a8\u03a9\3\2"+
		"\2\2\u03a9q\3\2\2\2\u03aa\u03ac\5\u009cO\2\u03ab\u03aa\3\2\2\2\u03ab\u03ac"+
		"\3\2\2\2\u03ac\u03be\3\2\2\2\u03ad\u03bf\7^\2\2\u03ae\u03bf\7\u0080\2"+
		"\2\u03af\u03b0\7^\2\2\u03b0\u03b1\7r\2\2\u03b1\u03bf\7\u0080\2\2\u03b2"+
		"\u03b3\7^\2\2\u03b3\u03b4\7r\2\2\u03b4\u03bf\7\u0083\2\2\u03b5\u03b6\7"+
		"^\2\2\u03b6\u03b7\7r\2\2\u03b7\u03bf\7\37\2\2\u03b8\u03b9\7^\2\2\u03b9"+
		"\u03ba\7r\2\2\u03ba\u03bf\7N\2\2\u03bb\u03bc\7^\2\2\u03bc\u03bd\7r\2\2"+
		"\u03bd\u03bf\7W\2\2\u03be\u03ad\3\2\2\2\u03be\u03ae\3\2\2\2\u03be\u03af"+
		"\3\2\2\2\u03be\u03b2\3\2\2\2\u03be\u03b5\3\2\2\2\u03be\u03b8\3\2\2\2\u03be"+
		"\u03bb\3\2\2\2\u03bf\u03c0\3\2\2\2\u03c0\u03c4\7a\2\2\u03c1\u03c2\5\u00c8"+
		"e\2\u03c2\u03c3\7\b\2\2\u03c3\u03c5\3\2\2\2\u03c4\u03c1\3\2\2\2\u03c4"+
		"\u03c5\3\2\2\2\u03c5\u03c6\3\2\2\2\u03c6\u03d2\5\u00caf\2\u03c7\u03c8"+
		"\7\t\2\2\u03c8\u03cd\5\u00d0i\2\u03c9\u03ca\7\13\2\2\u03ca\u03cc\5\u00d0"+
		"i\2\u03cb\u03c9\3\2\2\2\u03cc\u03cf\3\2\2\2\u03cd\u03cb\3\2\2\2\u03cd"+
		"\u03ce\3\2\2\2\u03ce\u03d0\3\2\2\2\u03cf\u03cd\3\2\2\2\u03d0\u03d1\7\n"+
		"\2\2\u03d1\u03d3\3\2\2\2\u03d2\u03c7\3\2\2\2\u03d2\u03d3\3\2\2\2\u03d3"+
		"\u03f3\3\2\2\2\u03d4\u03d5\7\u0094\2\2\u03d5\u03d6\7\t\2\2\u03d6\u03db"+
		"\5\u0092J\2\u03d7\u03d8\7\13\2\2\u03d8\u03da\5\u0092J\2\u03d9\u03d7\3"+
		"\2\2\2\u03da\u03dd\3\2\2\2\u03db\u03d9\3\2\2\2\u03db\u03dc\3\2\2\2\u03dc"+
		"\u03de\3\2\2\2\u03dd\u03db\3\2\2\2\u03de\u03ed\7\n\2\2\u03df\u03e0\7\13"+
		"\2\2\u03e0\u03e1\7\t\2\2\u03e1\u03e6\5\u0092J\2\u03e2\u03e3\7\13\2\2\u03e3"+
		"\u03e5\5\u0092J\2\u03e4\u03e2\3\2\2\2\u03e5\u03e8\3\2\2\2\u03e6\u03e4"+
		"\3\2\2\2\u03e6\u03e7\3\2\2\2\u03e7\u03e9\3\2\2\2\u03e8\u03e6\3\2\2\2\u03e9"+
		"\u03ea\7\n\2\2\u03ea\u03ec\3\2\2\2\u03eb\u03df\3\2\2\2\u03ec\u03ef\3\2"+
		"\2\2\u03ed\u03eb\3\2\2\2\u03ed\u03ee\3\2\2\2\u03ee\u03f4\3\2\2\2\u03ef"+
		"\u03ed\3\2\2\2\u03f0\u03f4\5\u0080A\2\u03f1\u03f2\7>\2\2\u03f2\u03f4\7"+
		"\u0094\2\2\u03f3\u03d4\3\2\2\2\u03f3\u03f0\3\2\2\2\u03f3\u03f1\3\2\2\2"+
		"\u03f4s\3\2\2\2\u03f5\u03f9\7v\2\2\u03f6\u03f7\5\u00c8e\2\u03f7\u03f8"+
		"\7\b\2\2\u03f8\u03fa\3\2\2\2\u03f9\u03f6\3\2\2\2\u03f9\u03fa\3\2\2\2\u03fa"+
		"\u03fb\3\2\2\2\u03fb\u0402\5\u00dep\2\u03fc\u03fd\7\f\2\2\u03fd\u0403"+
		"\5\u00a2R\2\u03fe\u03ff\7\t\2\2\u03ff\u0400\5\u00a2R\2\u0400\u0401\7\n"+
		"\2\2\u0401\u0403\3\2\2\2\u0402\u03fc\3\2\2\2\u0402\u03fe\3\2\2\2\u0402"+
		"\u0403\3\2\2\2\u0403u\3\2\2\2\u0404\u040f\7}\2\2\u0405\u0410\5\u00d2j"+
		"\2\u0406\u0407\5\u00c8e\2\u0407\u0408\7\b\2\2\u0408\u040a\3\2\2\2\u0409"+
		"\u0406\3\2\2\2\u0409\u040a\3\2\2\2\u040a\u040d\3\2\2\2\u040b\u040e\5\u00ca"+
		"f\2\u040c\u040e\5\u00d6l\2\u040d\u040b\3\2\2\2\u040d\u040c\3\2\2\2\u040e"+
		"\u0410\3\2\2\2\u040f\u0405\3\2\2\2\u040f\u0409\3\2\2\2\u040f\u0410\3\2"+
		"\2\2\u0410w\3\2\2\2\u0411\u0413\7~\2\2\u0412\u0414\7\u0085\2\2\u0413\u0412"+
		"\3\2\2\2\u0413\u0414\3\2\2\2\u0414\u0415\3\2\2\2\u0415\u0416\5\u00e0q"+
		"\2\u0416y\3\2\2\2\u0417\u041c\7\u0083\2\2\u0418\u041a\7\u008d\2\2\u0419"+
		"\u041b\5\u00e4s\2\u041a\u0419\3\2\2\2\u041a\u041b\3\2\2\2\u041b\u041d"+
		"\3\2\2\2\u041c\u0418\3\2\2\2\u041c\u041d\3\2\2\2\u041d\u0423\3\2\2\2\u041e"+
		"\u0420\7\u008c\2\2\u041f\u0421\7\u0085\2\2\u0420\u041f\3\2\2\2\u0420\u0421"+
		"\3\2\2\2\u0421\u0422\3\2\2\2\u0422\u0424\5\u00e0q\2\u0423\u041e\3\2\2"+
		"\2\u0423\u0424\3\2\2\2\u0424{\3\2\2\2\u0425\u0426\7\u0085\2\2\u0426\u0427"+
		"\5\u00e0q\2\u0427}\3\2\2\2\u0428\u042a\7\u0099\2\2\u0429\u042b\7z\2\2"+
		"\u042a\u0429\3\2\2\2\u042a\u042b\3\2\2\2\u042b\u042c\3\2\2\2\u042c\u0431"+
		"\5\u00a4S\2\u042d\u042e\7\13\2\2\u042e\u0430\5\u00a4S\2\u042f\u042d\3"+
		"\2\2\2\u0430\u0433\3\2\2\2\u0431\u042f\3\2\2\2\u0431\u0432\3\2\2\2\u0432"+
		"\u0435\3\2\2\2\u0433\u0431\3\2\2\2\u0434\u0428\3\2\2\2\u0434\u0435\3\2"+
		"\2\2\u0435\u0436\3\2\2\2\u0436\u0441\5\u00b0Y\2\u0437\u0438\7s\2\2\u0438"+
		"\u0439\7.\2\2\u0439\u043e\5\u00a0Q\2\u043a\u043b\7\13\2\2\u043b\u043d"+
		"\5\u00a0Q\2\u043c\u043a\3\2\2\2\u043d\u0440\3\2\2\2\u043e\u043c\3\2\2"+
		"\2\u043e\u043f\3\2\2\2\u043f\u0442\3\2\2\2\u0440\u043e\3\2\2\2\u0441\u0437"+
		"\3\2\2\2\u0441\u0442\3\2\2\2\u0442\u0449\3\2\2\2\u0443\u0444\7h\2\2\u0444"+
		"\u0447\5\u0092J\2\u0445\u0446\t\7\2\2\u0446\u0448\5\u0092J\2\u0447\u0445"+
		"\3\2\2\2\u0447\u0448\3\2\2\2\u0448\u044a\3\2\2\2\u0449\u0443\3\2\2\2\u0449"+
		"\u044a\3\2\2\2\u044a\177\3\2\2\2\u044b\u044d\7\u0099\2\2\u044c\u044e\7"+
		"z\2\2\u044d\u044c\3\2\2\2\u044d\u044e\3\2\2\2\u044e\u044f\3\2\2\2\u044f"+
		"\u0454\5\u00a4S\2\u0450\u0451\7\13\2\2\u0451\u0453\5\u00a4S\2\u0452\u0450"+
		"\3\2\2\2\u0453\u0456\3\2\2\2\u0454\u0452\3\2\2\2\u0454\u0455\3\2\2\2\u0455"+
		"\u0458\3\2\2\2\u0456\u0454\3\2\2\2\u0457\u044b\3\2\2\2\u0457\u0458\3\2"+
		"\2\2\u0458\u0459\3\2\2\2\u0459\u045f\5\u0082B\2\u045a\u045b\5\u00b2Z\2"+
		"\u045b\u045c\5\u0082B\2\u045c\u045e\3\2\2\2\u045d\u045a\3\2\2\2\u045e"+
		"\u0461\3\2\2\2\u045f\u045d\3\2\2\2\u045f\u0460\3\2\2\2\u0460\u046c\3\2"+
		"\2\2\u0461\u045f\3\2\2\2\u0462\u0463\7s\2\2\u0463\u0464\7.\2\2\u0464\u0469"+
		"\5\u00a0Q\2\u0465\u0466\7\13\2\2\u0466\u0468\5\u00a0Q\2\u0467\u0465\3"+
		"\2\2\2\u0468\u046b\3\2\2\2\u0469\u0467\3\2\2\2\u0469\u046a\3\2\2\2\u046a"+
		"\u046d\3\2\2\2\u046b\u0469\3\2\2\2\u046c\u0462\3\2\2\2\u046c\u046d\3\2"+
		"\2\2\u046d\u0474\3\2\2\2\u046e\u046f\7h\2\2\u046f\u0472\5\u0092J\2\u0470"+
		"\u0471\t\7\2\2\u0471\u0473\5\u0092J\2\u0472\u0470\3\2\2\2\u0472\u0473"+
		"\3\2\2\2\u0473\u0475\3\2\2\2\u0474\u046e\3\2\2\2\u0474\u0475\3\2\2\2\u0475"+
		"\u0081\3\2\2\2\u0476\u0478\7\u0086\2\2\u0477\u0479\t\t\2\2\u0478\u0477"+
		"\3\2\2\2\u0478\u0479\3\2\2\2\u0479\u047a\3\2\2\2\u047a\u047f\5\u00a6T"+
		"\2\u047b\u047c\7\13\2\2\u047c\u047e\5\u00a6T\2\u047d\u047b\3\2\2\2\u047e"+
		"\u0481\3\2\2\2\u047f\u047d\3\2\2\2\u047f\u0480\3\2\2\2\u0480\u048e\3\2"+
		"\2\2\u0481\u047f\3\2\2\2\u0482\u048c\7Q\2\2\u0483\u0488\5\u00a8U\2\u0484"+
		"\u0485\7\13\2\2\u0485\u0487\5\u00a8U\2\u0486\u0484\3\2\2\2\u0487\u048a"+
		"\3\2\2\2\u0488\u0486\3\2\2\2\u0488\u0489\3\2\2\2\u0489\u048d\3\2\2\2\u048a"+
		"\u0488\3\2\2\2\u048b\u048d\5\u00aaV\2\u048c\u0483\3\2\2\2\u048c\u048b"+
		"\3\2\2\2\u048d\u048f\3\2\2\2\u048e\u0482\3\2\2\2\u048e\u048f\3\2\2\2\u048f"+
		"\u0492\3\2\2\2\u0490\u0491\7\u0098\2\2\u0491\u0493\5\u0092J\2\u0492\u0490"+
		"\3\2\2\2\u0492\u0493\3\2\2\2\u0493\u04a2\3\2\2\2\u0494\u0495\7T\2\2\u0495"+
		"\u0496\7.\2\2\u0496\u049b\5\u0092J\2\u0497\u0498\7\13\2\2\u0498\u049a"+
		"\5\u0092J\2\u0499\u0497\3\2\2\2\u049a\u049d\3\2\2\2\u049b\u0499\3\2\2"+
		"\2\u049b\u049c\3\2\2\2\u049c\u04a0\3\2\2\2\u049d\u049b\3\2\2\2\u049e\u049f"+
		"\7U\2\2\u049f\u04a1\5\u0092J\2\u04a0\u049e\3\2\2\2\u04a0\u04a1\3\2\2\2"+
		"\u04a1\u04a3\3\2\2\2\u04a2\u0494\3\2\2\2\u04a2\u04a3\3\2\2\2\u04a3\u04c1"+
		"\3\2\2\2\u04a4\u04a5\7\u0094\2\2\u04a5\u04a6\7\t\2\2\u04a6\u04ab\5\u0092"+
		"J\2\u04a7\u04a8\7\13\2\2\u04a8\u04aa\5\u0092J\2\u04a9\u04a7\3\2\2\2\u04aa"+
		"\u04ad\3\2\2\2\u04ab\u04a9\3\2\2\2\u04ab\u04ac\3\2\2\2\u04ac\u04ae\3\2"+
		"\2\2\u04ad\u04ab\3\2\2\2\u04ae\u04bd\7\n\2\2\u04af\u04b0\7\13\2\2\u04b0"+
		"\u04b1\7\t\2\2\u04b1\u04b6\5\u0092J\2\u04b2\u04b3\7\13\2\2\u04b3\u04b5"+
		"\5\u0092J\2\u04b4\u04b2\3\2\2\2\u04b5\u04b8\3\2\2\2\u04b6\u04b4\3\2\2"+
		"\2\u04b6\u04b7\3\2\2\2\u04b7\u04b9\3\2\2\2\u04b8\u04b6\3\2\2\2\u04b9\u04ba"+
		"\7\n\2\2\u04ba\u04bc\3\2\2\2\u04bb\u04af\3\2\2\2\u04bc\u04bf\3\2\2\2\u04bd"+
		"\u04bb\3\2\2\2\u04bd\u04be\3\2\2\2\u04be\u04c1\3\2\2\2\u04bf\u04bd\3\2"+
		"\2\2\u04c0\u0476\3\2\2\2\u04c0\u04a4\3\2\2\2\u04c1\u0083\3\2\2\2\u04c2"+
		"\u04c4\5\u009cO\2\u04c3\u04c2\3\2\2\2\u04c3\u04c4\3\2\2\2\u04c4\u04c5"+
		"\3\2\2\2\u04c5\u04d0\7\u0091\2\2\u04c6\u04c7\7r\2\2\u04c7\u04d1\7\u0083"+
		"\2\2\u04c8\u04c9\7r\2\2\u04c9\u04d1\7\37\2\2\u04ca\u04cb\7r\2\2\u04cb"+
		"\u04d1\7\u0080\2\2\u04cc\u04cd\7r\2\2\u04cd\u04d1\7N\2\2\u04ce\u04cf\7"+
		"r\2\2\u04cf\u04d1\7W\2\2\u04d0\u04c6\3\2\2\2\u04d0\u04c8\3\2\2\2\u04d0"+
		"\u04ca\3\2\2\2\u04d0\u04cc\3\2\2\2\u04d0\u04ce\3\2\2\2\u04d0\u04d1\3\2"+
		"\2\2\u04d1\u04d2\3\2\2\2\u04d2\u04d3\5\u009eP\2\u04d3\u04d4\7\u0087\2"+
		"\2\u04d4\u04d5\5\u00d0i\2\u04d5\u04d6\7\f\2\2\u04d6\u04de\5\u0092J\2\u04d7"+
		"\u04d8\7\13\2\2\u04d8\u04d9\5\u00d0i\2\u04d9\u04da\7\f\2\2\u04da\u04db"+
		"\5\u0092J\2\u04db\u04dd\3\2\2\2\u04dc\u04d7\3\2\2\2\u04dd\u04e0\3\2\2"+
		"\2\u04de\u04dc\3\2\2\2\u04de\u04df\3\2\2\2\u04df\u04e3\3\2\2\2\u04e0\u04de"+
		"\3\2\2\2\u04e1\u04e2\7\u0098\2\2\u04e2\u04e4\5\u0092J\2\u04e3\u04e1\3"+
		"\2\2\2\u04e3\u04e4\3\2\2\2\u04e4\u0085\3\2\2\2\u04e5\u04e7\5\u009cO\2"+
		"\u04e6\u04e5\3\2\2\2\u04e6\u04e7\3\2\2\2\u04e7\u04e8\3\2\2\2\u04e8\u04f3"+
		"\7\u0091\2\2\u04e9\u04ea\7r\2\2\u04ea\u04f4\7\u0083\2\2\u04eb\u04ec\7"+
		"r\2\2\u04ec\u04f4\7\37\2\2\u04ed\u04ee\7r\2\2\u04ee\u04f4\7\u0080\2\2"+
		"\u04ef\u04f0\7r\2\2\u04f0\u04f4\7N\2\2\u04f1\u04f2\7r\2\2\u04f2\u04f4"+
		"\7W\2\2\u04f3\u04e9\3\2\2\2\u04f3\u04eb\3\2\2\2\u04f3\u04ed\3\2\2\2\u04f3"+
		"\u04ef\3\2\2\2\u04f3\u04f1\3\2\2\2\u04f3\u04f4\3\2\2\2\u04f4\u04f5\3\2"+
		"\2\2\u04f5\u04f6\5\u009eP\2\u04f6\u04f7\7\u0087\2\2\u04f7\u04f8\5\u00d0"+
		"i\2\u04f8\u04f9\7\f\2\2\u04f9\u0501\5\u0092J\2\u04fa\u04fb\7\13\2\2\u04fb"+
		"\u04fc\5\u00d0i\2\u04fc\u04fd\7\f\2\2\u04fd\u04fe\5\u0092J\2\u04fe\u0500"+
		"\3\2\2\2\u04ff\u04fa\3\2\2\2\u0500\u0503\3\2\2\2\u0501\u04ff\3\2\2\2\u0501"+
		"\u0502\3\2\2\2\u0502\u0506\3\2\2\2\u0503\u0501\3\2\2\2\u0504\u0505\7\u0098"+
		"\2\2\u0505\u0507\5\u0092J\2\u0506\u0504\3\2\2\2\u0506\u0507\3\2\2\2\u0507"+
		"\u051a\3\2\2\2\u0508\u0509\7s\2\2\u0509\u050a\7.\2\2\u050a\u050f\5\u00a0"+
		"Q\2\u050b\u050c\7\13\2\2\u050c\u050e\5\u00a0Q\2\u050d\u050b\3\2\2\2\u050e"+
		"\u0511\3\2\2\2\u050f\u050d\3\2\2\2\u050f\u0510\3\2\2\2\u0510\u0513\3\2"+
		"\2\2\u0511\u050f\3\2\2\2\u0512\u0508\3\2\2\2\u0512\u0513\3\2\2\2\u0513"+
		"\u0514\3\2\2\2\u0514\u0515\7h\2\2\u0515\u0518\5\u0092J\2\u0516\u0517\t"+
		"\7\2\2\u0517\u0519\5\u0092J\2\u0518\u0516\3\2\2\2\u0518\u0519\3\2\2\2"+
		"\u0519\u051b\3\2\2\2\u051a\u0512\3\2\2\2\u051a\u051b\3\2\2\2\u051b\u0087"+
		"\3\2\2\2\u051c\u051d\7\u0093\2\2\u051d\u0089\3\2\2\2\u051e\u0520\5\u00d0"+
		"i\2\u051f\u0521\5\u008cG\2\u0520\u051f\3\2\2\2\u0520\u0521\3\2\2\2\u0521"+
		"\u0525\3\2\2\2\u0522\u0524\5\u008eH\2\u0523\u0522\3\2\2\2\u0524\u0527"+
		"\3\2\2\2\u0525\u0523\3\2\2\2\u0525\u0526\3\2\2\2\u0526\u008b\3\2\2\2\u0527"+
		"\u0525\3\2\2\2\u0528\u052a\5\u00c4c\2\u0529\u0528\3\2\2\2\u052a\u052b"+
		"\3\2\2\2\u052b\u0529\3\2\2\2\u052b\u052c\3\2\2\2\u052c\u0537\3\2\2\2\u052d"+
		"\u052e\7\t\2\2\u052e\u052f\5\u00b6\\\2\u052f\u0530\7\n\2\2\u0530\u0538"+
		"\3\2\2\2\u0531\u0532\7\t\2\2\u0532\u0533\5\u00b6\\\2\u0533\u0534\7\13"+
		"\2\2\u0534\u0535\5\u00b6\\\2\u0535\u0536\7\n\2\2\u0536\u0538\3\2\2\2\u0537"+
		"\u052d\3\2\2\2\u0537\u0531\3\2\2\2\u0537\u0538\3\2\2\2\u0538\u008d\3\2"+
		"\2\2\u0539\u053a\7\67\2\2\u053a\u053c\5\u00c4c\2\u053b\u0539\3\2\2\2\u053b"+
		"\u053c\3\2\2\2\u053c\u055e\3\2\2\2\u053d\u053e\7w\2\2\u053e\u0540\7e\2"+
		"\2\u053f\u0541\t\4\2\2\u0540\u053f\3\2\2\2\u0540\u0541\3\2\2\2\u0541\u0542"+
		"\3\2\2\2\u0542\u0544\5\u0090I\2\u0543\u0545\7*\2\2\u0544\u0543\3\2\2\2"+
		"\u0544\u0545\3\2\2\2\u0545\u055f\3\2\2\2\u0546\u0548\7l\2\2\u0547\u0546"+
		"\3\2\2\2\u0547\u0548\3\2\2\2\u0548\u0549\3\2\2\2\u0549\u054a\7n\2\2\u054a"+
		"\u055f\5\u0090I\2\u054b\u054c\7\u0090\2\2\u054c\u055f\5\u0090I\2\u054d"+
		"\u054e\7\62\2\2\u054e\u054f\7\t\2\2\u054f\u0550\5\u0092J\2\u0550\u0551"+
		"\7\n\2\2\u0551\u055f\3\2\2\2\u0552\u0559\7>\2\2\u0553\u055a\5\u00b6\\"+
		"\2\u0554\u055a\5\u00b8]\2\u0555\u0556\7\t\2\2\u0556\u0557\5\u0092J\2\u0557"+
		"\u0558\7\n\2\2\u0558\u055a\3\2\2\2\u0559\u0553\3\2\2\2\u0559\u0554\3\2"+
		"\2\2\u0559\u0555\3\2\2\2\u055a\u055f\3\2\2\2\u055b\u055c\7\63\2\2\u055c"+
		"\u055f\5\u00d2j\2\u055d\u055f\5\u0094K\2\u055e\u053d\3\2\2\2\u055e\u0547"+
		"\3\2\2\2\u055e\u054b\3\2\2\2\u055e\u054d\3\2\2\2\u055e\u0552\3\2\2\2\u055e"+
		"\u055b\3\2\2\2\u055e\u055d\3\2\2\2\u055f\u008f\3\2\2\2\u0560\u0561\7q"+
		"\2\2\u0561\u0562\7\66\2\2\u0562\u0564\t\n\2\2\u0563\u0560\3\2\2\2\u0563"+
		"\u0564\3\2\2\2\u0564\u0091\3\2\2\2\u0565\u0566\bJ\1\2\u0566\u05b2\5\u00b8"+
		"]\2\u0567\u05b2\7\u009d\2\2\u0568\u0569\5\u00c8e\2\u0569\u056a\7\b\2\2"+
		"\u056a\u056c\3\2\2\2\u056b\u0568\3\2\2\2\u056b\u056c\3\2\2\2\u056c\u056d"+
		"\3\2\2\2\u056d\u056e\5\u00caf\2\u056e\u056f\7\b\2\2\u056f\u0571\3\2\2"+
		"\2\u0570\u056b\3\2\2\2\u0570\u0571\3\2\2\2\u0571\u0572\3\2\2\2\u0572\u05b2"+
		"\5\u00d0i\2\u0573\u0574\5\u00ba^\2\u0574\u0575\5\u0092J\27\u0575\u05b2"+
		"\3\2\2\2\u0576\u0577\5\u00c6d\2\u0577\u0584\7\t\2\2\u0578\u057a\7D\2\2"+
		"\u0579\u0578\3\2\2\2\u0579\u057a\3\2\2\2\u057a\u057b\3\2\2\2\u057b\u0580"+
		"\5\u0092J\2\u057c\u057d\7\13\2\2\u057d\u057f\5\u0092J\2\u057e\u057c\3"+
		"\2\2\2\u057f\u0582\3\2\2\2\u0580\u057e\3\2\2\2\u0580\u0581\3\2\2\2\u0581"+
		"\u0585\3\2\2\2\u0582\u0580\3\2\2\2\u0583\u0585\7\r\2\2\u0584\u0579\3\2"+
		"\2\2\u0584\u0583\3\2\2\2\u0584\u0585\3\2\2\2\u0585\u0586\3\2\2\2\u0586"+
		"\u0587\7\n\2\2\u0587\u05b2\3\2\2\2\u0588\u0589\7\t\2\2\u0589\u058a\5\u0092"+
		"J\2\u058a\u058b\7\n\2\2\u058b\u05b2\3\2\2\2\u058c\u058d\7\61\2\2\u058d"+
		"\u058e\7\t\2\2\u058e\u058f\5\u0092J\2\u058f\u0590\7\'\2\2\u0590\u0591"+
		"\5\u008cG\2\u0591\u0592\7\n\2\2\u0592\u05b2\3\2\2\2\u0593\u0595\7l\2\2"+
		"\u0594\u0593\3\2\2\2\u0594\u0595\3\2\2\2\u0595\u0596\3\2\2\2\u0596\u0598"+
		"\7L\2\2\u0597\u0594\3\2\2\2\u0597\u0598\3\2\2\2\u0598\u0599\3\2\2\2\u0599"+
		"\u059a\7\t\2\2\u059a\u059b\5\u0080A\2\u059b\u059c\7\n\2\2\u059c\u05b2"+
		"\3\2\2\2\u059d\u059f\7\60\2\2\u059e\u05a0\5\u0092J\2\u059f\u059e\3\2\2"+
		"\2\u059f\u05a0\3\2\2\2\u05a0\u05a6\3\2\2\2\u05a1\u05a2\7\u0097\2\2\u05a2"+
		"\u05a3\5\u0092J\2\u05a3\u05a4\7\u008b\2\2\u05a4\u05a5\5\u0092J\2\u05a5"+
		"\u05a7\3\2\2\2\u05a6\u05a1\3\2\2\2\u05a7\u05a8\3\2\2\2\u05a8\u05a6\3\2"+
		"\2\2\u05a8\u05a9\3\2\2\2\u05a9\u05ac\3\2\2\2\u05aa\u05ab\7G\2\2\u05ab"+
		"\u05ad\5\u0092J\2\u05ac\u05aa\3\2\2\2\u05ac\u05ad\3\2\2\2\u05ad\u05ae"+
		"\3\2\2\2\u05ae\u05af\7H\2\2\u05af\u05b2\3\2\2\2\u05b0\u05b2\5\u0096L\2"+
		"\u05b1\u0565\3\2\2\2\u05b1\u0567\3\2\2\2\u05b1\u0570\3\2\2\2\u05b1\u0573"+
		"\3\2\2\2\u05b1\u0576\3\2\2\2\u05b1\u0588\3\2\2\2\u05b1\u058c\3\2\2\2\u05b1"+
		"\u0597\3\2\2\2\u05b1\u059d\3\2\2\2\u05b1\u05b0\3\2\2\2\u05b2\u0617\3\2"+
		"\2\2\u05b3\u05b4\f\26\2\2\u05b4\u05b5\7\21\2\2\u05b5\u0616\5\u0092J\27"+
		"\u05b6\u05b7\f\25\2\2\u05b7\u05b8\t\13\2\2\u05b8\u0616\5\u0092J\26\u05b9"+
		"\u05ba\f\24\2\2\u05ba\u05bb\t\f\2\2\u05bb\u0616\5\u0092J\25\u05bc\u05bd"+
		"\f\23\2\2\u05bd\u05be\t\r\2\2\u05be\u0616\5\u0092J\24\u05bf\u05c0\f\22"+
		"\2\2\u05c0\u05c1\t\16\2\2\u05c1\u0616\5\u0092J\23\u05c2\u05cf\f\21\2\2"+
		"\u05c3\u05d0\7\f\2\2\u05c4\u05d0\7\34\2\2\u05c5\u05d0\7\35\2\2\u05c6\u05d0"+
		"\7\36\2\2\u05c7\u05d0\7b\2\2\u05c8\u05c9\7b\2\2\u05c9\u05d0\7l\2\2\u05ca"+
		"\u05d0\7Y\2\2\u05cb\u05d0\7g\2\2\u05cc\u05d0\7S\2\2\u05cd\u05d0\7i\2\2"+
		"\u05ce\u05d0\7|\2\2\u05cf\u05c3\3\2\2\2\u05cf\u05c4\3\2\2\2\u05cf\u05c5"+
		"\3\2\2\2\u05cf\u05c6\3\2\2\2\u05cf\u05c7\3\2\2\2\u05cf\u05c8\3\2\2\2\u05cf"+
		"\u05ca\3\2\2\2\u05cf\u05cb\3\2\2\2\u05cf\u05cc\3\2\2\2\u05cf\u05cd\3\2"+
		"\2\2\u05cf\u05ce\3\2\2\2\u05d0\u05d1\3\2\2\2\u05d1\u0616\5\u0092J\22\u05d2"+
		"\u05d3\f\20\2\2\u05d3\u05d4\7&\2\2\u05d4\u0616\5\u0092J\21\u05d5\u05d6"+
		"\f\17\2\2\u05d6\u05d7\7r\2\2\u05d7\u0616\5\u0092J\20\u05d8\u05d9\f\b\2"+
		"\2\u05d9\u05db\7b\2\2\u05da\u05dc\7l\2\2\u05db\u05da\3\2\2\2\u05db\u05dc"+
		"\3\2\2\2\u05dc\u05dd\3\2\2\2\u05dd\u0616\5\u0092J\t\u05de\u05e0\f\7\2"+
		"\2\u05df\u05e1\7l\2\2\u05e0\u05df\3\2\2\2\u05e0\u05e1\3\2\2\2\u05e1\u05e2"+
		"\3\2\2\2\u05e2\u05e3\7-\2\2\u05e3\u05e4\5\u0092J\2\u05e4\u05e5\7&\2\2"+
		"\u05e5\u05e6\5\u0092J\b\u05e6\u0616\3\2\2\2\u05e7\u05e8\f\13\2\2\u05e8"+
		"\u05e9\7\63\2\2\u05e9\u0616\5\u00d2j\2\u05ea\u05ec\f\n\2\2\u05eb\u05ed"+
		"\7l\2\2\u05ec\u05eb\3\2\2\2\u05ec\u05ed\3\2\2\2\u05ed\u05ee\3\2\2\2\u05ee"+
		"\u05ef\t\17\2\2\u05ef\u05f2\5\u0092J\2\u05f0\u05f1\7I\2\2\u05f1\u05f3"+
		"\5\u0092J\2\u05f2\u05f0\3\2\2\2\u05f2\u05f3\3\2\2\2\u05f3\u0616\3\2\2"+
		"\2\u05f4\u05f9\f\t\2\2\u05f5\u05fa\7c\2\2\u05f6\u05fa\7m\2\2\u05f7\u05f8"+
		"\7l\2\2\u05f8\u05fa\7n\2\2\u05f9\u05f5\3\2\2\2\u05f9\u05f6\3\2\2\2\u05f9"+
		"\u05f7\3\2\2\2\u05fa\u0616\3\2\2\2\u05fb\u05fd\f\6\2\2\u05fc\u05fe\7l"+
		"\2\2\u05fd\u05fc\3\2\2\2\u05fd\u05fe\3\2\2\2\u05fe\u05ff\3\2\2\2\u05ff"+
		"\u0613\7Y\2\2\u0600\u060a\7\t\2\2\u0601\u060b\5\u0080A\2\u0602\u0607\5"+
		"\u0092J\2\u0603\u0604\7\13\2\2\u0604\u0606\5\u0092J\2\u0605\u0603\3\2"+
		"\2\2\u0606\u0609\3\2\2\2\u0607\u0605\3\2\2\2\u0607\u0608\3\2\2\2\u0608"+
		"\u060b\3\2\2\2\u0609\u0607\3\2\2\2\u060a\u0601\3\2\2\2\u060a\u0602\3\2"+
		"\2\2\u060a\u060b\3\2\2\2\u060b\u060c\3\2\2\2\u060c\u0614\7\n\2\2\u060d"+
		"\u060e\5\u00c8e\2\u060e\u060f\7\b\2\2\u060f\u0611\3\2\2\2\u0610\u060d"+
		"\3\2\2\2\u0610\u0611\3\2\2\2\u0611\u0612\3\2\2\2\u0612\u0614\5\u00caf"+
		"\2\u0613\u0600\3\2\2\2\u0613\u0610\3\2\2\2\u0614\u0616\3\2\2\2\u0615\u05b3"+
		"\3\2\2\2\u0615\u05b6\3\2\2\2\u0615\u05b9\3\2\2\2\u0615\u05bc\3\2\2\2\u0615"+
		"\u05bf\3\2\2\2\u0615\u05c2\3\2\2\2\u0615\u05d2\3\2\2\2\u0615\u05d5\3\2"+
		"\2\2\u0615\u05d8\3\2\2\2\u0615\u05de\3\2\2\2\u0615\u05e7\3\2\2\2\u0615"+
		"\u05ea\3\2\2\2\u0615\u05f4\3\2\2\2\u0615\u05fb\3\2\2\2\u0616\u0619\3\2"+
		"\2\2\u0617\u0615\3\2\2\2\u0617\u0618\3\2\2\2\u0618\u0093\3\2\2\2\u0619"+
		"\u0617\3\2\2\2\u061a\u061b\7{\2\2\u061b\u0627\5\u00d4k\2\u061c\u061d\7"+
		"\t\2\2\u061d\u0622\5\u00d0i\2\u061e\u061f\7\13\2\2\u061f\u0621\5\u00d0"+
		"i\2\u0620\u061e\3\2\2\2\u0621\u0624\3\2\2\2\u0622\u0620\3\2\2\2\u0622"+
		"\u0623\3\2\2\2\u0623\u0625\3\2\2\2\u0624\u0622\3\2\2\2\u0625\u0626\7\n"+
		"\2\2\u0626\u0628\3\2\2\2\u0627\u061c\3\2\2\2\u0627\u0628\3\2\2\2\u0628"+
		"\u063b\3\2\2\2\u0629\u062a\7q\2\2\u062a\u0633\t\20\2\2\u062b\u062c\7\u0087"+
		"\2\2\u062c\u0634\7n\2\2\u062d\u062e\7\u0087\2\2\u062e\u0634\7>\2\2\u062f"+
		"\u0634\7/\2\2\u0630\u0634\7\u0081\2\2\u0631\u0632\7k\2\2\u0632\u0634\7"+
		" \2\2\u0633\u062b\3\2\2\2\u0633\u062d\3\2\2\2\u0633\u062f\3\2\2\2\u0633"+
		"\u0630\3\2\2\2\u0633\u0631\3\2\2\2\u0634\u0638\3\2\2\2\u0635\u0636\7i"+
		"\2\2\u0636\u0638\5\u00c4c\2\u0637\u0629\3\2\2\2\u0637\u0635\3\2\2\2\u0638"+
		"\u063a\3\2\2\2\u0639\u0637\3\2\2\2\u063a\u063d\3\2\2\2\u063b\u0639\3\2"+
		"\2\2\u063b\u063c\3\2\2\2\u063c\u0648\3\2\2\2\u063d\u063b\3\2\2\2\u063e"+
		"\u0640\7l\2\2\u063f\u063e\3\2\2\2\u063f\u0640\3\2\2\2\u0640\u0641\3\2"+
		"\2\2\u0641\u0646\7?\2\2\u0642\u0643\7\\\2\2\u0643\u0647\7@\2\2\u0644\u0645"+
		"\7\\\2\2\u0645\u0647\7X\2\2\u0646\u0642\3\2\2\2\u0646\u0644\3\2\2\2\u0646"+
		"\u0647\3\2\2\2\u0647\u0649\3\2\2\2\u0648\u063f\3\2\2\2\u0648\u0649\3\2"+
		"\2\2\u0649\u0095\3\2\2\2\u064a\u064b\7y\2\2\u064b\u0650\7\t\2\2\u064c"+
		"\u0651\7W\2\2\u064d\u064e\t\21\2\2\u064e\u064f\7\13\2\2\u064f\u0651\5"+
		"\u00bc_\2\u0650\u064c\3\2\2\2\u0650\u064d\3\2\2\2\u0651\u0652\3\2\2\2"+
		"\u0652\u0653\7\n\2\2\u0653\u0097\3\2\2\2\u0654\u0657\5\u00d0i\2\u0655"+
		"\u0656\7\63\2\2\u0656\u0658\5\u00d2j\2\u0657\u0655\3\2\2\2\u0657\u0658"+
		"\3\2\2\2\u0658\u065a\3\2\2\2\u0659\u065b\t\4\2\2\u065a\u0659\3\2\2\2\u065a"+
		"\u065b\3\2\2\2\u065b\u0099\3\2\2\2\u065c\u065d\7\67\2\2\u065d\u065f\5"+
		"\u00c4c\2\u065e\u065c\3\2\2\2\u065e\u065f\3\2\2\2\u065f\u0684\3\2\2\2"+
		"\u0660\u0661\7w\2\2\u0661\u0664\7e\2\2\u0662\u0664\7\u0090\2\2\u0663\u0660"+
		"\3\2\2\2\u0663\u0662\3\2\2\2\u0664\u0665\3\2\2\2\u0665\u0666\7\t\2\2\u0666"+
		"\u066b\5\u0098M\2\u0667\u0668\7\13\2\2\u0668\u066a\5\u0098M\2\u0669\u0667"+
		"\3\2\2\2\u066a\u066d\3\2\2\2\u066b\u0669\3\2\2\2\u066b\u066c\3\2\2\2\u066c"+
		"\u066e\3\2\2\2\u066d\u066b\3\2\2\2\u066e\u066f\7\n\2\2\u066f\u0670\5\u0090"+
		"I\2\u0670\u0685\3\2\2\2\u0671\u0672\7\62\2\2\u0672\u0673\7\t\2\2\u0673"+
		"\u0674\5\u0092J\2\u0674\u0675\7\n\2\2\u0675\u0685\3\2\2\2\u0676\u0677"+
		"\7P\2\2\u0677\u0678\7e\2\2\u0678\u0679\7\t\2\2\u0679\u067e\5\u00d0i\2"+
		"\u067a\u067b\7\13\2\2\u067b\u067d\5\u00d0i\2\u067c\u067a\3\2\2\2\u067d"+
		"\u0680\3\2\2\2\u067e\u067c\3\2\2\2\u067e\u067f\3\2\2\2\u067f\u0681\3\2"+
		"\2\2\u0680\u067e\3\2\2\2\u0681\u0682\7\n\2\2\u0682\u0683\5\u0094K\2\u0683"+
		"\u0685\3\2\2\2\u0684\u0663\3\2\2\2\u0684\u0671\3\2\2\2\u0684\u0676\3\2"+
		"\2\2\u0685\u009b\3\2\2\2\u0686\u0688\7\u0099\2\2\u0687\u0689\7z\2\2\u0688"+
		"\u0687\3\2\2\2\u0688\u0689\3\2\2\2\u0689\u068a\3\2\2\2\u068a\u068b\5\u00b4"+
		"[\2\u068b\u068c\7\'\2\2\u068c\u068d\7\t\2\2\u068d\u068e\5\u0080A\2\u068e"+
		"\u0698\7\n\2\2\u068f\u0690\7\13\2\2\u0690\u0691\5\u00b4[\2\u0691\u0692"+
		"\7\'\2\2\u0692\u0693\7\t\2\2\u0693\u0694\5\u0080A\2\u0694\u0695\7\n\2"+
		"\2\u0695\u0697\3\2\2\2\u0696\u068f\3\2\2\2\u0697\u069a\3\2\2\2\u0698\u0696"+
		"\3\2\2\2\u0698\u0699\3\2\2\2\u0699\u009d\3\2\2\2\u069a\u0698\3\2\2\2\u069b"+
		"\u069c\5\u00c8e\2\u069c\u069d\7\b\2\2\u069d\u069f\3\2\2\2\u069e\u069b"+
		"\3\2\2\2\u069e\u069f\3\2\2\2\u069f\u06a0\3\2\2\2\u06a0\u06a6\5\u00caf"+
		"\2\u06a1\u06a2\7[\2\2\u06a2\u06a3\7.\2\2\u06a3\u06a7\5\u00d6l\2\u06a4"+
		"\u06a5\7l\2\2\u06a5\u06a7\7[\2\2\u06a6\u06a1\3\2\2\2\u06a6\u06a4\3\2\2"+
		"\2\u06a6\u06a7\3\2\2\2\u06a7\u009f\3\2\2\2\u06a8\u06ab\5\u0092J\2\u06a9"+
		"\u06aa\7\63\2\2\u06aa\u06ac\5\u00d2j\2\u06ab\u06a9\3\2\2\2\u06ab\u06ac"+
		"\3\2\2\2\u06ac\u06ae\3\2\2\2\u06ad\u06af\t\4\2\2\u06ae\u06ad\3\2\2\2\u06ae"+
		"\u06af\3\2\2\2\u06af\u00a1\3\2\2\2\u06b0\u06b4\5\u00b6\\\2\u06b1\u06b4"+
		"\5\u00c4c\2\u06b2\u06b4\7\u009e\2\2\u06b3\u06b0\3\2\2\2\u06b3\u06b1\3"+
		"\2\2\2\u06b3\u06b2\3\2\2\2\u06b4\u00a3\3\2\2\2\u06b5\u06c1\5\u00caf\2"+
		"\u06b6\u06b7\7\t\2\2\u06b7\u06bc\5\u00d0i\2\u06b8\u06b9\7\13\2\2\u06b9"+
		"\u06bb\5\u00d0i\2\u06ba\u06b8\3\2\2\2\u06bb\u06be\3\2\2\2\u06bc\u06ba"+
		"\3\2\2\2\u06bc\u06bd\3\2\2\2\u06bd\u06bf\3\2\2\2\u06be\u06bc\3\2\2\2\u06bf"+
		"\u06c0\7\n\2\2\u06c0\u06c2\3\2\2\2\u06c1\u06b6\3\2\2\2\u06c1\u06c2\3\2"+
		"\2\2\u06c2\u06c3\3\2\2\2\u06c3\u06c4\7\'\2\2\u06c4\u06c5\7\t\2\2\u06c5"+
		"\u06c6\5\u0080A\2\u06c6\u06c7\7\n\2\2\u06c7\u00a5\3\2\2\2\u06c8\u06d5"+
		"\7\r\2\2\u06c9\u06ca\5\u00caf\2\u06ca\u06cb\7\b\2\2\u06cb\u06cc\7\r\2"+
		"\2\u06cc\u06d5\3\2\2\2\u06cd\u06d2\5\u0092J\2\u06ce\u06d0\7\'\2\2\u06cf"+
		"\u06ce\3\2\2\2\u06cf\u06d0\3\2\2\2\u06d0\u06d1\3\2\2\2\u06d1\u06d3\5\u00c0"+
		"a\2\u06d2\u06cf\3\2\2\2\u06d2\u06d3\3\2\2\2\u06d3\u06d5\3\2\2\2\u06d4"+
		"\u06c8\3\2\2\2\u06d4\u06c9\3\2\2\2\u06d4\u06cd\3\2\2\2\u06d5\u00a7\3\2"+
		"\2\2\u06d6\u06d7\5\u00c8e\2\u06d7\u06d8\7\b\2\2\u06d8\u06da\3\2\2\2\u06d9"+
		"\u06d6\3\2\2\2\u06d9\u06da\3\2\2\2\u06da\u06db\3\2\2\2\u06db\u06e0\5\u00ca"+
		"f\2\u06dc\u06de\7\'\2\2\u06dd\u06dc\3\2\2\2\u06dd\u06de\3\2\2\2\u06de"+
		"\u06df\3\2\2\2\u06df\u06e1\5\u00e2r\2\u06e0\u06dd\3\2\2\2\u06e0\u06e1"+
		"\3\2\2\2\u06e1\u06e7\3\2\2\2\u06e2\u06e3\7[\2\2\u06e3\u06e4\7.\2\2\u06e4"+
		"\u06e8\5\u00d6l\2\u06e5\u06e6\7l\2\2\u06e6\u06e8\7[\2\2\u06e7\u06e2\3"+
		"\2\2\2\u06e7\u06e5\3\2\2\2\u06e7\u06e8\3\2\2\2\u06e8\u0706\3\2\2\2\u06e9"+
		"\u06f3\7\t\2\2\u06ea\u06ef\5\u00a8U\2\u06eb\u06ec\7\13\2\2\u06ec\u06ee"+
		"\5\u00a8U\2\u06ed\u06eb\3\2\2\2\u06ee\u06f1\3\2\2\2\u06ef\u06ed\3\2\2"+
		"\2\u06ef\u06f0\3\2\2\2\u06f0\u06f4\3\2\2\2\u06f1\u06ef\3\2\2\2\u06f2\u06f4"+
		"\5\u00aaV\2\u06f3\u06ea\3\2\2\2\u06f3\u06f2\3\2\2\2\u06f4\u06f5\3\2\2"+
		"\2\u06f5\u06fa\7\n\2\2\u06f6\u06f8\7\'\2\2\u06f7\u06f6\3\2\2\2\u06f7\u06f8"+
		"\3\2\2\2\u06f8\u06f9\3\2\2\2\u06f9\u06fb\5\u00e2r\2\u06fa\u06f7\3\2\2"+
		"\2\u06fa\u06fb\3\2\2\2\u06fb\u0706\3\2\2\2\u06fc\u06fd\7\t\2\2\u06fd\u06fe"+
		"\5\u0080A\2\u06fe\u0703\7\n\2\2\u06ff\u0701\7\'\2\2\u0700\u06ff\3\2\2"+
		"\2\u0700\u0701\3\2\2\2\u0701\u0702\3\2\2\2\u0702\u0704\5\u00e2r\2\u0703"+
		"\u0700\3\2\2\2\u0703\u0704\3\2\2\2\u0704\u0706\3\2\2\2\u0705\u06d9\3\2"+
		"\2\2\u0705\u06e9\3\2\2\2\u0705\u06fc\3\2\2\2\u0706\u00a9\3\2\2\2\u0707"+
		"\u070e\5\u00a8U\2\u0708\u0709\5\u00acW\2\u0709\u070a\5\u00a8U\2\u070a"+
		"\u070b\5\u00aeX\2\u070b\u070d\3\2\2\2\u070c\u0708\3\2\2\2\u070d\u0710"+
		"\3\2\2\2\u070e\u070c\3\2\2\2\u070e\u070f\3\2\2\2\u070f\u00ab\3\2\2\2\u0710"+
		"\u070e\3\2\2\2\u0711\u071f\7\13\2\2\u0712\u0714\7j\2\2\u0713\u0712\3\2"+
		"\2\2\u0713\u0714\3\2\2\2\u0714\u071b\3\2\2\2\u0715\u0717\7f\2\2\u0716"+
		"\u0718\7t\2\2\u0717\u0716\3\2\2\2\u0717\u0718\3\2\2\2\u0718\u071c\3\2"+
		"\2\2\u0719\u071c\7]\2\2\u071a\u071c\79\2\2\u071b\u0715\3\2\2\2\u071b\u0719"+
		"\3\2\2\2\u071b\u071a\3\2\2\2\u071b\u071c\3\2\2\2\u071c\u071d\3\2\2\2\u071d"+
		"\u071f\7d\2\2\u071e\u0711\3\2\2\2\u071e\u0713\3\2\2\2\u071f\u00ad\3\2"+
		"\2\2\u0720\u0721\7q\2\2\u0721\u072f\5\u0092J\2\u0722\u0723\7\u0092\2\2"+
		"\u0723\u0724\7\t\2\2\u0724\u0729\5\u00d0i\2\u0725\u0726\7\13\2\2\u0726"+
		"\u0728\5\u00d0i\2\u0727\u0725\3\2\2\2\u0728\u072b\3\2\2\2\u0729\u0727"+
		"\3\2\2\2\u0729\u072a\3\2\2\2\u072a\u072c\3\2\2\2\u072b\u0729\3\2\2\2\u072c"+
		"\u072d\7\n\2\2\u072d\u072f\3\2\2\2\u072e\u0720\3\2\2\2\u072e\u0722\3\2"+
		"\2\2\u072e\u072f\3\2\2\2\u072f\u00af\3\2\2\2\u0730\u0732\7\u0086\2\2\u0731"+
		"\u0733\t\t\2\2\u0732\u0731\3\2\2\2\u0732\u0733\3\2\2\2\u0733\u0734\3\2"+
		"\2\2\u0734\u0739\5\u00a6T\2\u0735\u0736\7\13\2\2\u0736\u0738\5\u00a6T"+
		"\2\u0737\u0735\3\2\2\2\u0738\u073b\3\2\2\2\u0739\u0737\3\2\2\2\u0739\u073a"+
		"\3\2\2\2\u073a\u0748\3\2\2\2\u073b\u0739\3\2\2\2\u073c\u0746\7Q\2\2\u073d"+
		"\u0742\5\u00a8U\2\u073e\u073f\7\13\2\2\u073f\u0741\5\u00a8U\2\u0740\u073e"+
		"\3\2\2\2\u0741\u0744\3\2\2\2\u0742\u0740\3\2\2\2\u0742\u0743\3\2\2\2\u0743"+
		"\u0747\3\2\2\2\u0744\u0742\3\2\2\2\u0745\u0747\5\u00aaV\2\u0746\u073d"+
		"\3\2\2\2\u0746\u0745\3\2\2\2\u0747\u0749\3\2\2\2\u0748\u073c\3\2\2\2\u0748"+
		"\u0749\3\2\2\2\u0749\u074c\3\2\2\2\u074a\u074b\7\u0098\2\2\u074b\u074d"+
		"\5\u0092J\2\u074c\u074a\3\2\2\2\u074c\u074d\3\2\2\2\u074d\u075c\3\2\2"+
		"\2\u074e\u074f\7T\2\2\u074f\u0750\7.\2\2\u0750\u0755\5\u0092J\2\u0751"+
		"\u0752\7\13\2\2\u0752\u0754\5\u0092J\2\u0753\u0751\3\2\2\2\u0754\u0757"+
		"\3\2\2\2\u0755\u0753\3\2\2\2\u0755\u0756\3\2\2\2\u0756\u075a\3\2\2\2\u0757"+
		"\u0755\3\2\2\2\u0758\u0759\7U\2\2\u0759\u075b\5\u0092J\2\u075a\u0758\3"+
		"\2\2\2\u075a\u075b\3\2\2\2\u075b\u075d\3\2\2\2\u075c\u074e\3\2\2\2\u075c"+
		"\u075d\3\2\2\2\u075d\u077b\3\2\2\2\u075e\u075f\7\u0094\2\2\u075f\u0760"+
		"\7\t\2\2\u0760\u0765\5\u0092J\2\u0761\u0762\7\13\2\2\u0762\u0764\5\u0092"+
		"J\2\u0763\u0761\3\2\2\2\u0764\u0767\3\2\2\2\u0765\u0763\3\2\2\2\u0765"+
		"\u0766\3\2\2\2\u0766\u0768\3\2\2\2\u0767\u0765\3\2\2\2\u0768\u0777\7\n"+
		"\2\2\u0769\u076a\7\13\2\2\u076a\u076b\7\t\2\2\u076b\u0770\5\u0092J\2\u076c"+
		"\u076d\7\13\2\2\u076d\u076f\5\u0092J\2\u076e\u076c\3\2\2\2\u076f\u0772"+
		"\3\2\2\2\u0770\u076e\3\2\2\2\u0770\u0771\3\2\2\2\u0771\u0773\3\2\2\2\u0772"+
		"\u0770\3\2\2\2\u0773\u0774\7\n\2\2\u0774\u0776\3\2\2\2\u0775\u0769\3\2"+
		"\2\2\u0776\u0779\3\2\2\2\u0777\u0775\3\2\2\2\u0777\u0778\3\2\2\2\u0778"+
		"\u077b\3\2\2\2\u0779\u0777\3\2\2\2\u077a\u0730\3\2\2\2\u077a\u075e\3\2"+
		"\2\2\u077b\u00b1\3\2\2\2\u077c\u0782\7\u008f\2\2\u077d\u077e\7\u008f\2"+
		"\2\u077e\u0782\7#\2\2\u077f\u0782\7`\2\2\u0780\u0782\7J\2\2\u0781\u077c"+
		"\3\2\2\2\u0781\u077d\3\2\2\2\u0781\u077f\3\2\2\2\u0781\u0780\3\2\2\2\u0782"+
		"\u00b3\3\2\2\2\u0783\u078f\5\u00caf\2\u0784\u0785\7\t\2\2\u0785\u078a"+
		"\5\u00d0i\2\u0786\u0787\7\13\2\2\u0787\u0789\5\u00d0i\2\u0788\u0786\3"+
		"\2\2\2\u0789\u078c\3\2\2\2\u078a\u0788\3\2\2\2\u078a\u078b\3\2\2\2\u078b"+
		"\u078d\3\2\2\2\u078c\u078a\3\2\2\2\u078d\u078e\7\n\2\2\u078e\u0790\3\2"+
		"\2\2\u078f\u0784\3\2\2\2\u078f\u0790\3\2\2\2\u0790\u00b5\3\2\2\2\u0791"+
		"\u0793\t\f\2\2\u0792\u0791\3\2\2\2\u0792\u0793\3\2\2\2\u0793\u0794\3\2"+
		"\2\2\u0794\u0795\7\u009c\2\2\u0795\u00b7\3\2\2\2\u0796\u0797\t\22\2\2"+
		"\u0797\u00b9\3\2\2\2\u0798\u0799\t\23\2\2\u0799\u00bb\3\2\2\2\u079a\u079b"+
		"\7\u009e\2\2\u079b\u00bd\3\2\2\2\u079c\u079f\5\u0092J\2\u079d\u079f\5"+
		"\u008aF\2\u079e\u079c\3\2\2\2\u079e\u079d\3\2\2\2\u079f\u00bf\3\2\2\2"+
		"\u07a0\u07a1\t\2\2\2\u07a1\u00c1\3\2\2\2\u07a2\u07a3\t\24\2\2\u07a3\u00c3"+
		"\3\2\2\2\u07a4\u07a5\5\u00e6t\2\u07a5\u00c5\3\2\2\2\u07a6\u07a7\5\u00e6"+
		"t\2\u07a7\u00c7\3\2\2\2\u07a8\u07a9\5\u00e6t\2\u07a9\u00c9\3\2\2\2\u07aa"+
		"\u07ab\5\u00e6t\2\u07ab\u00cb\3\2\2\2\u07ac\u07ad\5\u00e6t\2\u07ad\u00cd"+
		"\3\2\2\2\u07ae\u07af\5\u00e6t\2\u07af\u00cf\3\2\2\2\u07b0\u07b1\5\u00e6"+
		"t\2\u07b1\u00d1\3\2\2\2\u07b2\u07b3\5\u00e6t\2\u07b3\u00d3\3\2\2\2\u07b4"+
		"\u07b5\5\u00e6t\2\u07b5\u00d5\3\2\2\2\u07b6\u07b7\5\u00e6t\2\u07b7\u00d7"+
		"\3\2\2\2\u07b8\u07b9\5\u00e6t\2\u07b9\u00d9\3\2\2\2\u07ba\u07bb\5\u00e6"+
		"t\2\u07bb\u00db\3\2\2\2\u07bc\u07bd\5\u00e6t\2\u07bd\u00dd\3\2\2\2\u07be"+
		"\u07bf\5\u00e6t\2\u07bf\u00df\3\2\2\2\u07c0\u07c1\5\u00e6t\2\u07c1\u00e1"+
		"\3\2\2\2\u07c2\u07c3\5\u00e6t\2\u07c3\u00e3\3\2\2\2\u07c4\u07c5\5\u00e6"+
		"t\2\u07c5\u00e5\3\2\2\2\u07c6\u07ce\7\u009b\2\2\u07c7\u07ce\5\u00c2b\2"+
		"\u07c8\u07ce\7\u009e\2\2\u07c9\u07ca\7\t\2\2\u07ca\u07cb\5\u00e6t\2\u07cb"+
		"\u07cc\7\n\2\2\u07cc\u07ce\3\2\2\2\u07cd\u07c6\3\2\2\2\u07cd\u07c7\3\2"+
		"\2\2\u07cd\u07c8\3\2\2\2\u07cd\u07c9\3\2\2\2\u07ce\u00e7\3\2\2\2\u0102"+
		"\u00ed\u00f0\u0100\u0105\u010a\u0114\u0121\u012d\u0143\u0172\u0180\u01ac"+
		"\u01b2\u01b4\u01bf\u01c6\u01cb\u01d1\u01d7\u01d9\u01f9\u0200\u0208\u020b"+
		"\u0214\u0218\u0220\u0224\u0226\u022b\u022d\u0231\u0238\u023b\u0240\u0244"+
		"\u0249\u0252\u0255\u025b\u025d\u0261\u0267\u026c\u0277\u027d\u0281\u0287"+
		"\u028c\u0295\u029c\u02a2\u02a6\u02aa\u02b0\u02b5\u02bc\u02c7\u02ca\u02cc"+
		"\u02d2\u02d8\u02dc\u02e3\u02e9\u02ef\u02f5\u02fa\u0306\u030b\u0316\u031b"+
		"\u031e\u0325\u0328\u032f\u0338\u033b\u0341\u0343\u0347\u034f\u0354\u035c"+
		"\u0361\u0369\u036e\u0376\u037b\u0381\u0388\u038b\u0393\u039d\u03a0\u03a6"+
		"\u03a8\u03ab\u03be\u03c4\u03cd\u03d2\u03db\u03e6\u03ed\u03f3\u03f9\u0402"+
		"\u0409\u040d\u040f\u0413\u041a\u041c\u0420\u0423\u042a\u0431\u0434\u043e"+
		"\u0441\u0447\u0449\u044d\u0454\u0457\u045f\u0469\u046c\u0472\u0474\u0478"+
		"\u047f\u0488\u048c\u048e\u0492\u049b\u04a0\u04a2\u04ab\u04b6\u04bd\u04c0"+
		"\u04c3\u04d0\u04de\u04e3\u04e6\u04f3\u0501\u0506\u050f\u0512\u0518\u051a"+
		"\u0520\u0525\u052b\u0537\u053b\u0540\u0544\u0547\u0559\u055e\u0563\u056b"+
		"\u0570\u0579\u0580\u0584\u0594\u0597\u059f\u05a8\u05ac\u05b1\u05cf\u05db"+
		"\u05e0\u05ec\u05f2\u05f9\u05fd\u0607\u060a\u0610\u0613\u0615\u0617\u0622"+
		"\u0627\u0633\u0637\u063b\u063f\u0646\u0648\u0650\u0657\u065a\u065e\u0663"+
		"\u066b\u067e\u0684\u0688\u0698\u069e\u06a6\u06ab\u06ae\u06b3\u06bc\u06c1"+
		"\u06cf\u06d2\u06d4\u06d9\u06dd\u06e0\u06e7\u06ef\u06f3\u06f7\u06fa\u0700"+
		"\u0703\u0705\u070e\u0713\u0717\u071b\u071e\u0729\u072e\u0732\u0739\u0742"+
		"\u0746\u0748\u074c\u0755\u075a\u075c\u0765\u0770\u0777\u077a\u0781\u078a"+
		"\u078f\u0792\u079e\u07cd";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}