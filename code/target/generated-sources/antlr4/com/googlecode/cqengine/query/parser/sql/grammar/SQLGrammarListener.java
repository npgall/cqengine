// Generated from com\googlecode\cqengine\query\parser\sql\grammar\SQLGrammar.g4 by ANTLR 4.7
package com.googlecode.cqengine.query.parser.sql.grammar;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SQLGrammarParser}.
 */
public interface SQLGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(SQLGrammarParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(SQLGrammarParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#indexedCollection}.
	 * @param ctx the parse tree
	 */
	void enterIndexedCollection(SQLGrammarParser.IndexedCollectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#indexedCollection}.
	 * @param ctx the parse tree
	 */
	void exitIndexedCollection(SQLGrammarParser.IndexedCollectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void enterWhereClause(SQLGrammarParser.WhereClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void exitWhereClause(SQLGrammarParser.WhereClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void enterOrderByClause(SQLGrammarParser.OrderByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void exitOrderByClause(SQLGrammarParser.OrderByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(SQLGrammarParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(SQLGrammarParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#logicalQuery}.
	 * @param ctx the parse tree
	 */
	void enterLogicalQuery(SQLGrammarParser.LogicalQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#logicalQuery}.
	 * @param ctx the parse tree
	 */
	void exitLogicalQuery(SQLGrammarParser.LogicalQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#andQuery}.
	 * @param ctx the parse tree
	 */
	void enterAndQuery(SQLGrammarParser.AndQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#andQuery}.
	 * @param ctx the parse tree
	 */
	void exitAndQuery(SQLGrammarParser.AndQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#orQuery}.
	 * @param ctx the parse tree
	 */
	void enterOrQuery(SQLGrammarParser.OrQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#orQuery}.
	 * @param ctx the parse tree
	 */
	void exitOrQuery(SQLGrammarParser.OrQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#notQuery}.
	 * @param ctx the parse tree
	 */
	void enterNotQuery(SQLGrammarParser.NotQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#notQuery}.
	 * @param ctx the parse tree
	 */
	void exitNotQuery(SQLGrammarParser.NotQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#simpleQuery}.
	 * @param ctx the parse tree
	 */
	void enterSimpleQuery(SQLGrammarParser.SimpleQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#simpleQuery}.
	 * @param ctx the parse tree
	 */
	void exitSimpleQuery(SQLGrammarParser.SimpleQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#equalQuery}.
	 * @param ctx the parse tree
	 */
	void enterEqualQuery(SQLGrammarParser.EqualQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#equalQuery}.
	 * @param ctx the parse tree
	 */
	void exitEqualQuery(SQLGrammarParser.EqualQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#notEqualQuery}.
	 * @param ctx the parse tree
	 */
	void enterNotEqualQuery(SQLGrammarParser.NotEqualQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#notEqualQuery}.
	 * @param ctx the parse tree
	 */
	void exitNotEqualQuery(SQLGrammarParser.NotEqualQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#lessThanOrEqualToQuery}.
	 * @param ctx the parse tree
	 */
	void enterLessThanOrEqualToQuery(SQLGrammarParser.LessThanOrEqualToQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#lessThanOrEqualToQuery}.
	 * @param ctx the parse tree
	 */
	void exitLessThanOrEqualToQuery(SQLGrammarParser.LessThanOrEqualToQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#lessThanQuery}.
	 * @param ctx the parse tree
	 */
	void enterLessThanQuery(SQLGrammarParser.LessThanQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#lessThanQuery}.
	 * @param ctx the parse tree
	 */
	void exitLessThanQuery(SQLGrammarParser.LessThanQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#greaterThanOrEqualToQuery}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanOrEqualToQuery(SQLGrammarParser.GreaterThanOrEqualToQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#greaterThanOrEqualToQuery}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanOrEqualToQuery(SQLGrammarParser.GreaterThanOrEqualToQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#greaterThanQuery}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanQuery(SQLGrammarParser.GreaterThanQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#greaterThanQuery}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanQuery(SQLGrammarParser.GreaterThanQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#betweenQuery}.
	 * @param ctx the parse tree
	 */
	void enterBetweenQuery(SQLGrammarParser.BetweenQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#betweenQuery}.
	 * @param ctx the parse tree
	 */
	void exitBetweenQuery(SQLGrammarParser.BetweenQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#notBetweenQuery}.
	 * @param ctx the parse tree
	 */
	void enterNotBetweenQuery(SQLGrammarParser.NotBetweenQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#notBetweenQuery}.
	 * @param ctx the parse tree
	 */
	void exitNotBetweenQuery(SQLGrammarParser.NotBetweenQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#inQuery}.
	 * @param ctx the parse tree
	 */
	void enterInQuery(SQLGrammarParser.InQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#inQuery}.
	 * @param ctx the parse tree
	 */
	void exitInQuery(SQLGrammarParser.InQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#notInQuery}.
	 * @param ctx the parse tree
	 */
	void enterNotInQuery(SQLGrammarParser.NotInQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#notInQuery}.
	 * @param ctx the parse tree
	 */
	void exitNotInQuery(SQLGrammarParser.NotInQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#startsWithQuery}.
	 * @param ctx the parse tree
	 */
	void enterStartsWithQuery(SQLGrammarParser.StartsWithQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#startsWithQuery}.
	 * @param ctx the parse tree
	 */
	void exitStartsWithQuery(SQLGrammarParser.StartsWithQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#endsWithQuery}.
	 * @param ctx the parse tree
	 */
	void enterEndsWithQuery(SQLGrammarParser.EndsWithQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#endsWithQuery}.
	 * @param ctx the parse tree
	 */
	void exitEndsWithQuery(SQLGrammarParser.EndsWithQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#containsQuery}.
	 * @param ctx the parse tree
	 */
	void enterContainsQuery(SQLGrammarParser.ContainsQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#containsQuery}.
	 * @param ctx the parse tree
	 */
	void exitContainsQuery(SQLGrammarParser.ContainsQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#hasQuery}.
	 * @param ctx the parse tree
	 */
	void enterHasQuery(SQLGrammarParser.HasQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#hasQuery}.
	 * @param ctx the parse tree
	 */
	void exitHasQuery(SQLGrammarParser.HasQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#notHasQuery}.
	 * @param ctx the parse tree
	 */
	void enterNotHasQuery(SQLGrammarParser.NotHasQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#notHasQuery}.
	 * @param ctx the parse tree
	 */
	void exitNotHasQuery(SQLGrammarParser.NotHasQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#isPrefixOfQuery}.
	 * @param ctx the parse tree
	 */
	void enterIsPrefixOfQuery(SQLGrammarParser.IsPrefixOfQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#isPrefixOfQuery}.
	 * @param ctx the parse tree
	 */
	void exitIsPrefixOfQuery(SQLGrammarParser.IsPrefixOfQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void enterAttributeName(SQLGrammarParser.AttributeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void exitAttributeName(SQLGrammarParser.AttributeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#queryParameterTrailingPercent}.
	 * @param ctx the parse tree
	 */
	void enterQueryParameterTrailingPercent(SQLGrammarParser.QueryParameterTrailingPercentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#queryParameterTrailingPercent}.
	 * @param ctx the parse tree
	 */
	void exitQueryParameterTrailingPercent(SQLGrammarParser.QueryParameterTrailingPercentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#queryParameterLeadingPercent}.
	 * @param ctx the parse tree
	 */
	void enterQueryParameterLeadingPercent(SQLGrammarParser.QueryParameterLeadingPercentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#queryParameterLeadingPercent}.
	 * @param ctx the parse tree
	 */
	void exitQueryParameterLeadingPercent(SQLGrammarParser.QueryParameterLeadingPercentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#queryParameterLeadingAndTrailingPercent}.
	 * @param ctx the parse tree
	 */
	void enterQueryParameterLeadingAndTrailingPercent(SQLGrammarParser.QueryParameterLeadingAndTrailingPercentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#queryParameterLeadingAndTrailingPercent}.
	 * @param ctx the parse tree
	 */
	void exitQueryParameterLeadingAndTrailingPercent(SQLGrammarParser.QueryParameterLeadingAndTrailingPercentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#queryParameter}.
	 * @param ctx the parse tree
	 */
	void enterQueryParameter(SQLGrammarParser.QueryParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#queryParameter}.
	 * @param ctx the parse tree
	 */
	void exitQueryParameter(SQLGrammarParser.QueryParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#attributeOrder}.
	 * @param ctx the parse tree
	 */
	void enterAttributeOrder(SQLGrammarParser.AttributeOrderContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#attributeOrder}.
	 * @param ctx the parse tree
	 */
	void exitAttributeOrder(SQLGrammarParser.AttributeOrderContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#direction}.
	 * @param ctx the parse tree
	 */
	void enterDirection(SQLGrammarParser.DirectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#direction}.
	 * @param ctx the parse tree
	 */
	void exitDirection(SQLGrammarParser.DirectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#parse}.
	 * @param ctx the parse tree
	 */
	void enterParse(SQLGrammarParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(SQLGrammarParser.ParseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#error}.
	 * @param ctx the parse tree
	 */
	void enterError(SQLGrammarParser.ErrorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#error}.
	 * @param ctx the parse tree
	 */
	void exitError(SQLGrammarParser.ErrorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#sql_stmt_list}.
	 * @param ctx the parse tree
	 */
	void enterSql_stmt_list(SQLGrammarParser.Sql_stmt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#sql_stmt_list}.
	 * @param ctx the parse tree
	 */
	void exitSql_stmt_list(SQLGrammarParser.Sql_stmt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#sql_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSql_stmt(SQLGrammarParser.Sql_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#sql_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSql_stmt(SQLGrammarParser.Sql_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#alter_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAlter_table_stmt(SQLGrammarParser.Alter_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#alter_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAlter_table_stmt(SQLGrammarParser.Alter_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#analyze_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAnalyze_stmt(SQLGrammarParser.Analyze_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#analyze_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAnalyze_stmt(SQLGrammarParser.Analyze_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#attach_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAttach_stmt(SQLGrammarParser.Attach_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#attach_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAttach_stmt(SQLGrammarParser.Attach_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#begin_stmt}.
	 * @param ctx the parse tree
	 */
	void enterBegin_stmt(SQLGrammarParser.Begin_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#begin_stmt}.
	 * @param ctx the parse tree
	 */
	void exitBegin_stmt(SQLGrammarParser.Begin_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#commit_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCommit_stmt(SQLGrammarParser.Commit_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#commit_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCommit_stmt(SQLGrammarParser.Commit_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#compound_select_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCompound_select_stmt(SQLGrammarParser.Compound_select_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#compound_select_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCompound_select_stmt(SQLGrammarParser.Compound_select_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#create_index_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_index_stmt(SQLGrammarParser.Create_index_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#create_index_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_index_stmt(SQLGrammarParser.Create_index_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#create_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table_stmt(SQLGrammarParser.Create_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#create_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table_stmt(SQLGrammarParser.Create_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#create_trigger_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_trigger_stmt(SQLGrammarParser.Create_trigger_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#create_trigger_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_trigger_stmt(SQLGrammarParser.Create_trigger_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#create_view_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_view_stmt(SQLGrammarParser.Create_view_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#create_view_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_view_stmt(SQLGrammarParser.Create_view_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#create_virtual_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_virtual_table_stmt(SQLGrammarParser.Create_virtual_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#create_virtual_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_virtual_table_stmt(SQLGrammarParser.Create_virtual_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#delete_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDelete_stmt(SQLGrammarParser.Delete_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#delete_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDelete_stmt(SQLGrammarParser.Delete_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#delete_stmt_limited}.
	 * @param ctx the parse tree
	 */
	void enterDelete_stmt_limited(SQLGrammarParser.Delete_stmt_limitedContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#delete_stmt_limited}.
	 * @param ctx the parse tree
	 */
	void exitDelete_stmt_limited(SQLGrammarParser.Delete_stmt_limitedContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#detach_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDetach_stmt(SQLGrammarParser.Detach_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#detach_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDetach_stmt(SQLGrammarParser.Detach_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#drop_index_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDrop_index_stmt(SQLGrammarParser.Drop_index_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#drop_index_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDrop_index_stmt(SQLGrammarParser.Drop_index_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#drop_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDrop_table_stmt(SQLGrammarParser.Drop_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#drop_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDrop_table_stmt(SQLGrammarParser.Drop_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#drop_trigger_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDrop_trigger_stmt(SQLGrammarParser.Drop_trigger_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#drop_trigger_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDrop_trigger_stmt(SQLGrammarParser.Drop_trigger_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#drop_view_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDrop_view_stmt(SQLGrammarParser.Drop_view_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#drop_view_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDrop_view_stmt(SQLGrammarParser.Drop_view_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#factored_select_stmt}.
	 * @param ctx the parse tree
	 */
	void enterFactored_select_stmt(SQLGrammarParser.Factored_select_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#factored_select_stmt}.
	 * @param ctx the parse tree
	 */
	void exitFactored_select_stmt(SQLGrammarParser.Factored_select_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#insert_stmt}.
	 * @param ctx the parse tree
	 */
	void enterInsert_stmt(SQLGrammarParser.Insert_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#insert_stmt}.
	 * @param ctx the parse tree
	 */
	void exitInsert_stmt(SQLGrammarParser.Insert_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#pragma_stmt}.
	 * @param ctx the parse tree
	 */
	void enterPragma_stmt(SQLGrammarParser.Pragma_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#pragma_stmt}.
	 * @param ctx the parse tree
	 */
	void exitPragma_stmt(SQLGrammarParser.Pragma_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#reindex_stmt}.
	 * @param ctx the parse tree
	 */
	void enterReindex_stmt(SQLGrammarParser.Reindex_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#reindex_stmt}.
	 * @param ctx the parse tree
	 */
	void exitReindex_stmt(SQLGrammarParser.Reindex_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#release_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRelease_stmt(SQLGrammarParser.Release_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#release_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRelease_stmt(SQLGrammarParser.Release_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#rollback_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRollback_stmt(SQLGrammarParser.Rollback_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#rollback_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRollback_stmt(SQLGrammarParser.Rollback_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#savepoint_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSavepoint_stmt(SQLGrammarParser.Savepoint_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#savepoint_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSavepoint_stmt(SQLGrammarParser.Savepoint_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#simple_select_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSimple_select_stmt(SQLGrammarParser.Simple_select_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#simple_select_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSimple_select_stmt(SQLGrammarParser.Simple_select_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#select_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSelect_stmt(SQLGrammarParser.Select_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#select_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSelect_stmt(SQLGrammarParser.Select_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#select_or_values}.
	 * @param ctx the parse tree
	 */
	void enterSelect_or_values(SQLGrammarParser.Select_or_valuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#select_or_values}.
	 * @param ctx the parse tree
	 */
	void exitSelect_or_values(SQLGrammarParser.Select_or_valuesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#update_stmt}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_stmt(SQLGrammarParser.Update_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#update_stmt}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_stmt(SQLGrammarParser.Update_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#update_stmt_limited}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_stmt_limited(SQLGrammarParser.Update_stmt_limitedContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#update_stmt_limited}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_stmt_limited(SQLGrammarParser.Update_stmt_limitedContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#vacuum_stmt}.
	 * @param ctx the parse tree
	 */
	void enterVacuum_stmt(SQLGrammarParser.Vacuum_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#vacuum_stmt}.
	 * @param ctx the parse tree
	 */
	void exitVacuum_stmt(SQLGrammarParser.Vacuum_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#column_def}.
	 * @param ctx the parse tree
	 */
	void enterColumn_def(SQLGrammarParser.Column_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#column_def}.
	 * @param ctx the parse tree
	 */
	void exitColumn_def(SQLGrammarParser.Column_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#type_name}.
	 * @param ctx the parse tree
	 */
	void enterType_name(SQLGrammarParser.Type_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#type_name}.
	 * @param ctx the parse tree
	 */
	void exitType_name(SQLGrammarParser.Type_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#column_constraint}.
	 * @param ctx the parse tree
	 */
	void enterColumn_constraint(SQLGrammarParser.Column_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#column_constraint}.
	 * @param ctx the parse tree
	 */
	void exitColumn_constraint(SQLGrammarParser.Column_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#conflict_clause}.
	 * @param ctx the parse tree
	 */
	void enterConflict_clause(SQLGrammarParser.Conflict_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#conflict_clause}.
	 * @param ctx the parse tree
	 */
	void exitConflict_clause(SQLGrammarParser.Conflict_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(SQLGrammarParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(SQLGrammarParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#foreign_key_clause}.
	 * @param ctx the parse tree
	 */
	void enterForeign_key_clause(SQLGrammarParser.Foreign_key_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#foreign_key_clause}.
	 * @param ctx the parse tree
	 */
	void exitForeign_key_clause(SQLGrammarParser.Foreign_key_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#raise_function}.
	 * @param ctx the parse tree
	 */
	void enterRaise_function(SQLGrammarParser.Raise_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#raise_function}.
	 * @param ctx the parse tree
	 */
	void exitRaise_function(SQLGrammarParser.Raise_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#indexed_column}.
	 * @param ctx the parse tree
	 */
	void enterIndexed_column(SQLGrammarParser.Indexed_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#indexed_column}.
	 * @param ctx the parse tree
	 */
	void exitIndexed_column(SQLGrammarParser.Indexed_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#table_constraint}.
	 * @param ctx the parse tree
	 */
	void enterTable_constraint(SQLGrammarParser.Table_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#table_constraint}.
	 * @param ctx the parse tree
	 */
	void exitTable_constraint(SQLGrammarParser.Table_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#with_clause}.
	 * @param ctx the parse tree
	 */
	void enterWith_clause(SQLGrammarParser.With_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#with_clause}.
	 * @param ctx the parse tree
	 */
	void exitWith_clause(SQLGrammarParser.With_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#qualified_table_name}.
	 * @param ctx the parse tree
	 */
	void enterQualified_table_name(SQLGrammarParser.Qualified_table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#qualified_table_name}.
	 * @param ctx the parse tree
	 */
	void exitQualified_table_name(SQLGrammarParser.Qualified_table_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#ordering_term}.
	 * @param ctx the parse tree
	 */
	void enterOrdering_term(SQLGrammarParser.Ordering_termContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#ordering_term}.
	 * @param ctx the parse tree
	 */
	void exitOrdering_term(SQLGrammarParser.Ordering_termContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#pragma_value}.
	 * @param ctx the parse tree
	 */
	void enterPragma_value(SQLGrammarParser.Pragma_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#pragma_value}.
	 * @param ctx the parse tree
	 */
	void exitPragma_value(SQLGrammarParser.Pragma_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#common_table_expression}.
	 * @param ctx the parse tree
	 */
	void enterCommon_table_expression(SQLGrammarParser.Common_table_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#common_table_expression}.
	 * @param ctx the parse tree
	 */
	void exitCommon_table_expression(SQLGrammarParser.Common_table_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#result_column}.
	 * @param ctx the parse tree
	 */
	void enterResult_column(SQLGrammarParser.Result_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#result_column}.
	 * @param ctx the parse tree
	 */
	void exitResult_column(SQLGrammarParser.Result_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#table_or_subquery}.
	 * @param ctx the parse tree
	 */
	void enterTable_or_subquery(SQLGrammarParser.Table_or_subqueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#table_or_subquery}.
	 * @param ctx the parse tree
	 */
	void exitTable_or_subquery(SQLGrammarParser.Table_or_subqueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#join_clause}.
	 * @param ctx the parse tree
	 */
	void enterJoin_clause(SQLGrammarParser.Join_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#join_clause}.
	 * @param ctx the parse tree
	 */
	void exitJoin_clause(SQLGrammarParser.Join_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#join_operator}.
	 * @param ctx the parse tree
	 */
	void enterJoin_operator(SQLGrammarParser.Join_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#join_operator}.
	 * @param ctx the parse tree
	 */
	void exitJoin_operator(SQLGrammarParser.Join_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#join_constraint}.
	 * @param ctx the parse tree
	 */
	void enterJoin_constraint(SQLGrammarParser.Join_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#join_constraint}.
	 * @param ctx the parse tree
	 */
	void exitJoin_constraint(SQLGrammarParser.Join_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#select_core}.
	 * @param ctx the parse tree
	 */
	void enterSelect_core(SQLGrammarParser.Select_coreContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#select_core}.
	 * @param ctx the parse tree
	 */
	void exitSelect_core(SQLGrammarParser.Select_coreContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#compound_operator}.
	 * @param ctx the parse tree
	 */
	void enterCompound_operator(SQLGrammarParser.Compound_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#compound_operator}.
	 * @param ctx the parse tree
	 */
	void exitCompound_operator(SQLGrammarParser.Compound_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#cte_table_name}.
	 * @param ctx the parse tree
	 */
	void enterCte_table_name(SQLGrammarParser.Cte_table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#cte_table_name}.
	 * @param ctx the parse tree
	 */
	void exitCte_table_name(SQLGrammarParser.Cte_table_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#signed_number}.
	 * @param ctx the parse tree
	 */
	void enterSigned_number(SQLGrammarParser.Signed_numberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#signed_number}.
	 * @param ctx the parse tree
	 */
	void exitSigned_number(SQLGrammarParser.Signed_numberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#literal_value}.
	 * @param ctx the parse tree
	 */
	void enterLiteral_value(SQLGrammarParser.Literal_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#literal_value}.
	 * @param ctx the parse tree
	 */
	void exitLiteral_value(SQLGrammarParser.Literal_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#unary_operator}.
	 * @param ctx the parse tree
	 */
	void enterUnary_operator(SQLGrammarParser.Unary_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#unary_operator}.
	 * @param ctx the parse tree
	 */
	void exitUnary_operator(SQLGrammarParser.Unary_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#error_message}.
	 * @param ctx the parse tree
	 */
	void enterError_message(SQLGrammarParser.Error_messageContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#error_message}.
	 * @param ctx the parse tree
	 */
	void exitError_message(SQLGrammarParser.Error_messageContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#module_argument}.
	 * @param ctx the parse tree
	 */
	void enterModule_argument(SQLGrammarParser.Module_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#module_argument}.
	 * @param ctx the parse tree
	 */
	void exitModule_argument(SQLGrammarParser.Module_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#column_alias}.
	 * @param ctx the parse tree
	 */
	void enterColumn_alias(SQLGrammarParser.Column_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#column_alias}.
	 * @param ctx the parse tree
	 */
	void exitColumn_alias(SQLGrammarParser.Column_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#keyword}.
	 * @param ctx the parse tree
	 */
	void enterKeyword(SQLGrammarParser.KeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#keyword}.
	 * @param ctx the parse tree
	 */
	void exitKeyword(SQLGrammarParser.KeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(SQLGrammarParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(SQLGrammarParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#function_name}.
	 * @param ctx the parse tree
	 */
	void enterFunction_name(SQLGrammarParser.Function_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#function_name}.
	 * @param ctx the parse tree
	 */
	void exitFunction_name(SQLGrammarParser.Function_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#database_name}.
	 * @param ctx the parse tree
	 */
	void enterDatabase_name(SQLGrammarParser.Database_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#database_name}.
	 * @param ctx the parse tree
	 */
	void exitDatabase_name(SQLGrammarParser.Database_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#table_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_name(SQLGrammarParser.Table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#table_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_name(SQLGrammarParser.Table_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#table_or_index_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_or_index_name(SQLGrammarParser.Table_or_index_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#table_or_index_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_or_index_name(SQLGrammarParser.Table_or_index_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#new_table_name}.
	 * @param ctx the parse tree
	 */
	void enterNew_table_name(SQLGrammarParser.New_table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#new_table_name}.
	 * @param ctx the parse tree
	 */
	void exitNew_table_name(SQLGrammarParser.New_table_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#column_name}.
	 * @param ctx the parse tree
	 */
	void enterColumn_name(SQLGrammarParser.Column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#column_name}.
	 * @param ctx the parse tree
	 */
	void exitColumn_name(SQLGrammarParser.Column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#collation_name}.
	 * @param ctx the parse tree
	 */
	void enterCollation_name(SQLGrammarParser.Collation_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#collation_name}.
	 * @param ctx the parse tree
	 */
	void exitCollation_name(SQLGrammarParser.Collation_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#foreign_table}.
	 * @param ctx the parse tree
	 */
	void enterForeign_table(SQLGrammarParser.Foreign_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#foreign_table}.
	 * @param ctx the parse tree
	 */
	void exitForeign_table(SQLGrammarParser.Foreign_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#index_name}.
	 * @param ctx the parse tree
	 */
	void enterIndex_name(SQLGrammarParser.Index_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#index_name}.
	 * @param ctx the parse tree
	 */
	void exitIndex_name(SQLGrammarParser.Index_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#trigger_name}.
	 * @param ctx the parse tree
	 */
	void enterTrigger_name(SQLGrammarParser.Trigger_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#trigger_name}.
	 * @param ctx the parse tree
	 */
	void exitTrigger_name(SQLGrammarParser.Trigger_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#view_name}.
	 * @param ctx the parse tree
	 */
	void enterView_name(SQLGrammarParser.View_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#view_name}.
	 * @param ctx the parse tree
	 */
	void exitView_name(SQLGrammarParser.View_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#module_name}.
	 * @param ctx the parse tree
	 */
	void enterModule_name(SQLGrammarParser.Module_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#module_name}.
	 * @param ctx the parse tree
	 */
	void exitModule_name(SQLGrammarParser.Module_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#pragma_name}.
	 * @param ctx the parse tree
	 */
	void enterPragma_name(SQLGrammarParser.Pragma_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#pragma_name}.
	 * @param ctx the parse tree
	 */
	void exitPragma_name(SQLGrammarParser.Pragma_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#savepoint_name}.
	 * @param ctx the parse tree
	 */
	void enterSavepoint_name(SQLGrammarParser.Savepoint_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#savepoint_name}.
	 * @param ctx the parse tree
	 */
	void exitSavepoint_name(SQLGrammarParser.Savepoint_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#table_alias}.
	 * @param ctx the parse tree
	 */
	void enterTable_alias(SQLGrammarParser.Table_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#table_alias}.
	 * @param ctx the parse tree
	 */
	void exitTable_alias(SQLGrammarParser.Table_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#transaction_name}.
	 * @param ctx the parse tree
	 */
	void enterTransaction_name(SQLGrammarParser.Transaction_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#transaction_name}.
	 * @param ctx the parse tree
	 */
	void exitTransaction_name(SQLGrammarParser.Transaction_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLGrammarParser#any_name}.
	 * @param ctx the parse tree
	 */
	void enterAny_name(SQLGrammarParser.Any_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLGrammarParser#any_name}.
	 * @param ctx the parse tree
	 */
	void exitAny_name(SQLGrammarParser.Any_nameContext ctx);
}