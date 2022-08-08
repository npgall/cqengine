// Generated from com\googlecode\cqengine\query\parser\cqn\grammar\CQNGrammar.g4 by ANTLR 4.7
package com.googlecode.cqengine.query.parser.cqn.grammar;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CQNGrammarParser}.
 */
public interface CQNGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(CQNGrammarParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(CQNGrammarParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(CQNGrammarParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(CQNGrammarParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#logicalQuery}.
	 * @param ctx the parse tree
	 */
	void enterLogicalQuery(CQNGrammarParser.LogicalQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#logicalQuery}.
	 * @param ctx the parse tree
	 */
	void exitLogicalQuery(CQNGrammarParser.LogicalQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#andQuery}.
	 * @param ctx the parse tree
	 */
	void enterAndQuery(CQNGrammarParser.AndQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#andQuery}.
	 * @param ctx the parse tree
	 */
	void exitAndQuery(CQNGrammarParser.AndQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#orQuery}.
	 * @param ctx the parse tree
	 */
	void enterOrQuery(CQNGrammarParser.OrQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#orQuery}.
	 * @param ctx the parse tree
	 */
	void exitOrQuery(CQNGrammarParser.OrQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#notQuery}.
	 * @param ctx the parse tree
	 */
	void enterNotQuery(CQNGrammarParser.NotQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#notQuery}.
	 * @param ctx the parse tree
	 */
	void exitNotQuery(CQNGrammarParser.NotQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#simpleQuery}.
	 * @param ctx the parse tree
	 */
	void enterSimpleQuery(CQNGrammarParser.SimpleQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#simpleQuery}.
	 * @param ctx the parse tree
	 */
	void exitSimpleQuery(CQNGrammarParser.SimpleQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#equalQuery}.
	 * @param ctx the parse tree
	 */
	void enterEqualQuery(CQNGrammarParser.EqualQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#equalQuery}.
	 * @param ctx the parse tree
	 */
	void exitEqualQuery(CQNGrammarParser.EqualQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#lessThanOrEqualToQuery}.
	 * @param ctx the parse tree
	 */
	void enterLessThanOrEqualToQuery(CQNGrammarParser.LessThanOrEqualToQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#lessThanOrEqualToQuery}.
	 * @param ctx the parse tree
	 */
	void exitLessThanOrEqualToQuery(CQNGrammarParser.LessThanOrEqualToQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#lessThanQuery}.
	 * @param ctx the parse tree
	 */
	void enterLessThanQuery(CQNGrammarParser.LessThanQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#lessThanQuery}.
	 * @param ctx the parse tree
	 */
	void exitLessThanQuery(CQNGrammarParser.LessThanQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#greaterThanOrEqualToQuery}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanOrEqualToQuery(CQNGrammarParser.GreaterThanOrEqualToQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#greaterThanOrEqualToQuery}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanOrEqualToQuery(CQNGrammarParser.GreaterThanOrEqualToQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#greaterThanQuery}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanQuery(CQNGrammarParser.GreaterThanQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#greaterThanQuery}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanQuery(CQNGrammarParser.GreaterThanQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#verboseBetweenQuery}.
	 * @param ctx the parse tree
	 */
	void enterVerboseBetweenQuery(CQNGrammarParser.VerboseBetweenQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#verboseBetweenQuery}.
	 * @param ctx the parse tree
	 */
	void exitVerboseBetweenQuery(CQNGrammarParser.VerboseBetweenQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#betweenQuery}.
	 * @param ctx the parse tree
	 */
	void enterBetweenQuery(CQNGrammarParser.BetweenQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#betweenQuery}.
	 * @param ctx the parse tree
	 */
	void exitBetweenQuery(CQNGrammarParser.BetweenQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#inQuery}.
	 * @param ctx the parse tree
	 */
	void enterInQuery(CQNGrammarParser.InQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#inQuery}.
	 * @param ctx the parse tree
	 */
	void exitInQuery(CQNGrammarParser.InQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#startsWithQuery}.
	 * @param ctx the parse tree
	 */
	void enterStartsWithQuery(CQNGrammarParser.StartsWithQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#startsWithQuery}.
	 * @param ctx the parse tree
	 */
	void exitStartsWithQuery(CQNGrammarParser.StartsWithQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#endsWithQuery}.
	 * @param ctx the parse tree
	 */
	void enterEndsWithQuery(CQNGrammarParser.EndsWithQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#endsWithQuery}.
	 * @param ctx the parse tree
	 */
	void exitEndsWithQuery(CQNGrammarParser.EndsWithQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#containsQuery}.
	 * @param ctx the parse tree
	 */
	void enterContainsQuery(CQNGrammarParser.ContainsQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#containsQuery}.
	 * @param ctx the parse tree
	 */
	void exitContainsQuery(CQNGrammarParser.ContainsQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#isContainedInQuery}.
	 * @param ctx the parse tree
	 */
	void enterIsContainedInQuery(CQNGrammarParser.IsContainedInQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#isContainedInQuery}.
	 * @param ctx the parse tree
	 */
	void exitIsContainedInQuery(CQNGrammarParser.IsContainedInQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#matchesRegexQuery}.
	 * @param ctx the parse tree
	 */
	void enterMatchesRegexQuery(CQNGrammarParser.MatchesRegexQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#matchesRegexQuery}.
	 * @param ctx the parse tree
	 */
	void exitMatchesRegexQuery(CQNGrammarParser.MatchesRegexQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#hasQuery}.
	 * @param ctx the parse tree
	 */
	void enterHasQuery(CQNGrammarParser.HasQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#hasQuery}.
	 * @param ctx the parse tree
	 */
	void exitHasQuery(CQNGrammarParser.HasQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#allQuery}.
	 * @param ctx the parse tree
	 */
	void enterAllQuery(CQNGrammarParser.AllQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#allQuery}.
	 * @param ctx the parse tree
	 */
	void exitAllQuery(CQNGrammarParser.AllQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#noneQuery}.
	 * @param ctx the parse tree
	 */
	void enterNoneQuery(CQNGrammarParser.NoneQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#noneQuery}.
	 * @param ctx the parse tree
	 */
	void exitNoneQuery(CQNGrammarParser.NoneQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#longestPrefixQuery}.
	 * @param ctx the parse tree
	 */
	void enterLongestPrefixQuery(CQNGrammarParser.LongestPrefixQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#longestPrefixQuery}.
	 * @param ctx the parse tree
	 */
	void exitLongestPrefixQuery(CQNGrammarParser.LongestPrefixQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#isPrefixOfQuery}.
	 * @param ctx the parse tree
	 */
	void enterIsPrefixOfQuery(CQNGrammarParser.IsPrefixOfQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#isPrefixOfQuery}.
	 * @param ctx the parse tree
	 */
	void exitIsPrefixOfQuery(CQNGrammarParser.IsPrefixOfQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#objectType}.
	 * @param ctx the parse tree
	 */
	void enterObjectType(CQNGrammarParser.ObjectTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#objectType}.
	 * @param ctx the parse tree
	 */
	void exitObjectType(CQNGrammarParser.ObjectTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void enterAttributeName(CQNGrammarParser.AttributeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void exitAttributeName(CQNGrammarParser.AttributeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#queryParameter}.
	 * @param ctx the parse tree
	 */
	void enterQueryParameter(CQNGrammarParser.QueryParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#queryParameter}.
	 * @param ctx the parse tree
	 */
	void exitQueryParameter(CQNGrammarParser.QueryParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#stringQueryParameter}.
	 * @param ctx the parse tree
	 */
	void enterStringQueryParameter(CQNGrammarParser.StringQueryParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#stringQueryParameter}.
	 * @param ctx the parse tree
	 */
	void exitStringQueryParameter(CQNGrammarParser.StringQueryParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#queryOptions}.
	 * @param ctx the parse tree
	 */
	void enterQueryOptions(CQNGrammarParser.QueryOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#queryOptions}.
	 * @param ctx the parse tree
	 */
	void exitQueryOptions(CQNGrammarParser.QueryOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#queryOption}.
	 * @param ctx the parse tree
	 */
	void enterQueryOption(CQNGrammarParser.QueryOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#queryOption}.
	 * @param ctx the parse tree
	 */
	void exitQueryOption(CQNGrammarParser.QueryOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#orderByOption}.
	 * @param ctx the parse tree
	 */
	void enterOrderByOption(CQNGrammarParser.OrderByOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#orderByOption}.
	 * @param ctx the parse tree
	 */
	void exitOrderByOption(CQNGrammarParser.OrderByOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#attributeOrder}.
	 * @param ctx the parse tree
	 */
	void enterAttributeOrder(CQNGrammarParser.AttributeOrderContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#attributeOrder}.
	 * @param ctx the parse tree
	 */
	void exitAttributeOrder(CQNGrammarParser.AttributeOrderContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#direction}.
	 * @param ctx the parse tree
	 */
	void enterDirection(CQNGrammarParser.DirectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#direction}.
	 * @param ctx the parse tree
	 */
	void exitDirection(CQNGrammarParser.DirectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(CQNGrammarParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(CQNGrammarParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void enterCompilationUnit(CQNGrammarParser.CompilationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void exitCompilationUnit(CQNGrammarParser.CompilationUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#packageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterPackageDeclaration(CQNGrammarParser.PackageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#packageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitPackageDeclaration(CQNGrammarParser.PackageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterImportDeclaration(CQNGrammarParser.ImportDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitImportDeclaration(CQNGrammarParser.ImportDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTypeDeclaration(CQNGrammarParser.TypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTypeDeclaration(CQNGrammarParser.TypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#modifier}.
	 * @param ctx the parse tree
	 */
	void enterModifier(CQNGrammarParser.ModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#modifier}.
	 * @param ctx the parse tree
	 */
	void exitModifier(CQNGrammarParser.ModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#classOrInterfaceModifier}.
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceModifier(CQNGrammarParser.ClassOrInterfaceModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#classOrInterfaceModifier}.
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceModifier(CQNGrammarParser.ClassOrInterfaceModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#variableModifier}.
	 * @param ctx the parse tree
	 */
	void enterVariableModifier(CQNGrammarParser.VariableModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#variableModifier}.
	 * @param ctx the parse tree
	 */
	void exitVariableModifier(CQNGrammarParser.VariableModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(CQNGrammarParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(CQNGrammarParser.ClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#typeParameters}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameters(CQNGrammarParser.TypeParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#typeParameters}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameters(CQNGrammarParser.TypeParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameter(CQNGrammarParser.TypeParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameter(CQNGrammarParser.TypeParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#typeBound}.
	 * @param ctx the parse tree
	 */
	void enterTypeBound(CQNGrammarParser.TypeBoundContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#typeBound}.
	 * @param ctx the parse tree
	 */
	void exitTypeBound(CQNGrammarParser.TypeBoundContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterEnumDeclaration(CQNGrammarParser.EnumDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitEnumDeclaration(CQNGrammarParser.EnumDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#enumConstants}.
	 * @param ctx the parse tree
	 */
	void enterEnumConstants(CQNGrammarParser.EnumConstantsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#enumConstants}.
	 * @param ctx the parse tree
	 */
	void exitEnumConstants(CQNGrammarParser.EnumConstantsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#enumConstant}.
	 * @param ctx the parse tree
	 */
	void enterEnumConstant(CQNGrammarParser.EnumConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#enumConstant}.
	 * @param ctx the parse tree
	 */
	void exitEnumConstant(CQNGrammarParser.EnumConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#enumBodyDeclarations}.
	 * @param ctx the parse tree
	 */
	void enterEnumBodyDeclarations(CQNGrammarParser.EnumBodyDeclarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#enumBodyDeclarations}.
	 * @param ctx the parse tree
	 */
	void exitEnumBodyDeclarations(CQNGrammarParser.EnumBodyDeclarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceDeclaration(CQNGrammarParser.InterfaceDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceDeclaration(CQNGrammarParser.InterfaceDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#typeList}.
	 * @param ctx the parse tree
	 */
	void enterTypeList(CQNGrammarParser.TypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#typeList}.
	 * @param ctx the parse tree
	 */
	void exitTypeList(CQNGrammarParser.TypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#classBody}.
	 * @param ctx the parse tree
	 */
	void enterClassBody(CQNGrammarParser.ClassBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#classBody}.
	 * @param ctx the parse tree
	 */
	void exitClassBody(CQNGrammarParser.ClassBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#interfaceBody}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceBody(CQNGrammarParser.InterfaceBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#interfaceBody}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceBody(CQNGrammarParser.InterfaceBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassBodyDeclaration(CQNGrammarParser.ClassBodyDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassBodyDeclaration(CQNGrammarParser.ClassBodyDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#memberDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMemberDeclaration(CQNGrammarParser.MemberDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#memberDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMemberDeclaration(CQNGrammarParser.MemberDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(CQNGrammarParser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(CQNGrammarParser.MethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#genericMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterGenericMethodDeclaration(CQNGrammarParser.GenericMethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#genericMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitGenericMethodDeclaration(CQNGrammarParser.GenericMethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstructorDeclaration(CQNGrammarParser.ConstructorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstructorDeclaration(CQNGrammarParser.ConstructorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#genericConstructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterGenericConstructorDeclaration(CQNGrammarParser.GenericConstructorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#genericConstructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitGenericConstructorDeclaration(CQNGrammarParser.GenericConstructorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFieldDeclaration(CQNGrammarParser.FieldDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFieldDeclaration(CQNGrammarParser.FieldDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#interfaceBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceBodyDeclaration(CQNGrammarParser.InterfaceBodyDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#interfaceBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceBodyDeclaration(CQNGrammarParser.InterfaceBodyDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#interfaceMemberDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMemberDeclaration(CQNGrammarParser.InterfaceMemberDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#interfaceMemberDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMemberDeclaration(CQNGrammarParser.InterfaceMemberDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#constDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstDeclaration(CQNGrammarParser.ConstDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#constDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstDeclaration(CQNGrammarParser.ConstDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#constantDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterConstantDeclarator(CQNGrammarParser.ConstantDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#constantDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitConstantDeclarator(CQNGrammarParser.ConstantDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#interfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMethodDeclaration(CQNGrammarParser.InterfaceMethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#interfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMethodDeclaration(CQNGrammarParser.InterfaceMethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#genericInterfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterGenericInterfaceMethodDeclaration(CQNGrammarParser.GenericInterfaceMethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#genericInterfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitGenericInterfaceMethodDeclaration(CQNGrammarParser.GenericInterfaceMethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#variableDeclarators}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarators(CQNGrammarParser.VariableDeclaratorsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#variableDeclarators}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarators(CQNGrammarParser.VariableDeclaratorsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarator(CQNGrammarParser.VariableDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarator(CQNGrammarParser.VariableDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaratorId(CQNGrammarParser.VariableDeclaratorIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaratorId(CQNGrammarParser.VariableDeclaratorIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void enterVariableInitializer(CQNGrammarParser.VariableInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void exitVariableInitializer(CQNGrammarParser.VariableInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void enterArrayInitializer(CQNGrammarParser.ArrayInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void exitArrayInitializer(CQNGrammarParser.ArrayInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#enumConstantName}.
	 * @param ctx the parse tree
	 */
	void enterEnumConstantName(CQNGrammarParser.EnumConstantNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#enumConstantName}.
	 * @param ctx the parse tree
	 */
	void exitEnumConstantName(CQNGrammarParser.EnumConstantNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(CQNGrammarParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(CQNGrammarParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceType(CQNGrammarParser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceType(CQNGrammarParser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(CQNGrammarParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(CQNGrammarParser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void enterTypeArguments(CQNGrammarParser.TypeArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void exitTypeArguments(CQNGrammarParser.TypeArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#typeArgument}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgument(CQNGrammarParser.TypeArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#typeArgument}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgument(CQNGrammarParser.TypeArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#qualifiedNameList}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedNameList(CQNGrammarParser.QualifiedNameListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#qualifiedNameList}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedNameList(CQNGrammarParser.QualifiedNameListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#formalParameters}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameters(CQNGrammarParser.FormalParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#formalParameters}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameters(CQNGrammarParser.FormalParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterList(CQNGrammarParser.FormalParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterList(CQNGrammarParser.FormalParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#formalParameter}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameter(CQNGrammarParser.FormalParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#formalParameter}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameter(CQNGrammarParser.FormalParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#lastFormalParameter}.
	 * @param ctx the parse tree
	 */
	void enterLastFormalParameter(CQNGrammarParser.LastFormalParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#lastFormalParameter}.
	 * @param ctx the parse tree
	 */
	void exitLastFormalParameter(CQNGrammarParser.LastFormalParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#methodBody}.
	 * @param ctx the parse tree
	 */
	void enterMethodBody(CQNGrammarParser.MethodBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#methodBody}.
	 * @param ctx the parse tree
	 */
	void exitMethodBody(CQNGrammarParser.MethodBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#constructorBody}.
	 * @param ctx the parse tree
	 */
	void enterConstructorBody(CQNGrammarParser.ConstructorBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#constructorBody}.
	 * @param ctx the parse tree
	 */
	void exitConstructorBody(CQNGrammarParser.ConstructorBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(CQNGrammarParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(CQNGrammarParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation(CQNGrammarParser.AnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation(CQNGrammarParser.AnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#annotationName}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationName(CQNGrammarParser.AnnotationNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#annotationName}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationName(CQNGrammarParser.AnnotationNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#elementValuePairs}.
	 * @param ctx the parse tree
	 */
	void enterElementValuePairs(CQNGrammarParser.ElementValuePairsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#elementValuePairs}.
	 * @param ctx the parse tree
	 */
	void exitElementValuePairs(CQNGrammarParser.ElementValuePairsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#elementValuePair}.
	 * @param ctx the parse tree
	 */
	void enterElementValuePair(CQNGrammarParser.ElementValuePairContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#elementValuePair}.
	 * @param ctx the parse tree
	 */
	void exitElementValuePair(CQNGrammarParser.ElementValuePairContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#elementValue}.
	 * @param ctx the parse tree
	 */
	void enterElementValue(CQNGrammarParser.ElementValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#elementValue}.
	 * @param ctx the parse tree
	 */
	void exitElementValue(CQNGrammarParser.ElementValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 */
	void enterElementValueArrayInitializer(CQNGrammarParser.ElementValueArrayInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 */
	void exitElementValueArrayInitializer(CQNGrammarParser.ElementValueArrayInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#annotationTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeDeclaration(CQNGrammarParser.AnnotationTypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#annotationTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeDeclaration(CQNGrammarParser.AnnotationTypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#annotationTypeBody}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeBody(CQNGrammarParser.AnnotationTypeBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#annotationTypeBody}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeBody(CQNGrammarParser.AnnotationTypeBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#annotationTypeElementDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeElementDeclaration(CQNGrammarParser.AnnotationTypeElementDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#annotationTypeElementDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeElementDeclaration(CQNGrammarParser.AnnotationTypeElementDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#annotationTypeElementRest}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeElementRest(CQNGrammarParser.AnnotationTypeElementRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#annotationTypeElementRest}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeElementRest(CQNGrammarParser.AnnotationTypeElementRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#annotationMethodOrConstantRest}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationMethodOrConstantRest(CQNGrammarParser.AnnotationMethodOrConstantRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#annotationMethodOrConstantRest}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationMethodOrConstantRest(CQNGrammarParser.AnnotationMethodOrConstantRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#annotationMethodRest}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationMethodRest(CQNGrammarParser.AnnotationMethodRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#annotationMethodRest}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationMethodRest(CQNGrammarParser.AnnotationMethodRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#annotationConstantRest}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationConstantRest(CQNGrammarParser.AnnotationConstantRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#annotationConstantRest}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationConstantRest(CQNGrammarParser.AnnotationConstantRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void enterDefaultValue(CQNGrammarParser.DefaultValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void exitDefaultValue(CQNGrammarParser.DefaultValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(CQNGrammarParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(CQNGrammarParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(CQNGrammarParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(CQNGrammarParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#localVariableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclarationStatement(CQNGrammarParser.LocalVariableDeclarationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#localVariableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclarationStatement(CQNGrammarParser.LocalVariableDeclarationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclaration(CQNGrammarParser.LocalVariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclaration(CQNGrammarParser.LocalVariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(CQNGrammarParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(CQNGrammarParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#catchClause}.
	 * @param ctx the parse tree
	 */
	void enterCatchClause(CQNGrammarParser.CatchClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#catchClause}.
	 * @param ctx the parse tree
	 */
	void exitCatchClause(CQNGrammarParser.CatchClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#catchType}.
	 * @param ctx the parse tree
	 */
	void enterCatchType(CQNGrammarParser.CatchTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#catchType}.
	 * @param ctx the parse tree
	 */
	void exitCatchType(CQNGrammarParser.CatchTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#finallyBlock}.
	 * @param ctx the parse tree
	 */
	void enterFinallyBlock(CQNGrammarParser.FinallyBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#finallyBlock}.
	 * @param ctx the parse tree
	 */
	void exitFinallyBlock(CQNGrammarParser.FinallyBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#resourceSpecification}.
	 * @param ctx the parse tree
	 */
	void enterResourceSpecification(CQNGrammarParser.ResourceSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#resourceSpecification}.
	 * @param ctx the parse tree
	 */
	void exitResourceSpecification(CQNGrammarParser.ResourceSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#resources}.
	 * @param ctx the parse tree
	 */
	void enterResources(CQNGrammarParser.ResourcesContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#resources}.
	 * @param ctx the parse tree
	 */
	void exitResources(CQNGrammarParser.ResourcesContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#resource}.
	 * @param ctx the parse tree
	 */
	void enterResource(CQNGrammarParser.ResourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#resource}.
	 * @param ctx the parse tree
	 */
	void exitResource(CQNGrammarParser.ResourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void enterSwitchBlockStatementGroup(CQNGrammarParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void exitSwitchBlockStatementGroup(CQNGrammarParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void enterSwitchLabel(CQNGrammarParser.SwitchLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void exitSwitchLabel(CQNGrammarParser.SwitchLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#forControl}.
	 * @param ctx the parse tree
	 */
	void enterForControl(CQNGrammarParser.ForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#forControl}.
	 * @param ctx the parse tree
	 */
	void exitForControl(CQNGrammarParser.ForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(CQNGrammarParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(CQNGrammarParser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#enhancedForControl}.
	 * @param ctx the parse tree
	 */
	void enterEnhancedForControl(CQNGrammarParser.EnhancedForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#enhancedForControl}.
	 * @param ctx the parse tree
	 */
	void exitEnhancedForControl(CQNGrammarParser.EnhancedForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void enterForUpdate(CQNGrammarParser.ForUpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void exitForUpdate(CQNGrammarParser.ForUpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#parExpression}.
	 * @param ctx the parse tree
	 */
	void enterParExpression(CQNGrammarParser.ParExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#parExpression}.
	 * @param ctx the parse tree
	 */
	void exitParExpression(CQNGrammarParser.ParExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(CQNGrammarParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(CQNGrammarParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#statementExpression}.
	 * @param ctx the parse tree
	 */
	void enterStatementExpression(CQNGrammarParser.StatementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#statementExpression}.
	 * @param ctx the parse tree
	 */
	void exitStatementExpression(CQNGrammarParser.StatementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpression(CQNGrammarParser.ConstantExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpression(CQNGrammarParser.ConstantExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(CQNGrammarParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(CQNGrammarParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(CQNGrammarParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(CQNGrammarParser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterCreator(CQNGrammarParser.CreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitCreator(CQNGrammarParser.CreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#createdName}.
	 * @param ctx the parse tree
	 */
	void enterCreatedName(CQNGrammarParser.CreatedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#createdName}.
	 * @param ctx the parse tree
	 */
	void exitCreatedName(CQNGrammarParser.CreatedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#innerCreator}.
	 * @param ctx the parse tree
	 */
	void enterInnerCreator(CQNGrammarParser.InnerCreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#innerCreator}.
	 * @param ctx the parse tree
	 */
	void exitInnerCreator(CQNGrammarParser.InnerCreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreatorRest(CQNGrammarParser.ArrayCreatorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreatorRest(CQNGrammarParser.ArrayCreatorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#classCreatorRest}.
	 * @param ctx the parse tree
	 */
	void enterClassCreatorRest(CQNGrammarParser.ClassCreatorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#classCreatorRest}.
	 * @param ctx the parse tree
	 */
	void exitClassCreatorRest(CQNGrammarParser.ClassCreatorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#explicitGenericInvocation}.
	 * @param ctx the parse tree
	 */
	void enterExplicitGenericInvocation(CQNGrammarParser.ExplicitGenericInvocationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#explicitGenericInvocation}.
	 * @param ctx the parse tree
	 */
	void exitExplicitGenericInvocation(CQNGrammarParser.ExplicitGenericInvocationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#nonWildcardTypeArguments}.
	 * @param ctx the parse tree
	 */
	void enterNonWildcardTypeArguments(CQNGrammarParser.NonWildcardTypeArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#nonWildcardTypeArguments}.
	 * @param ctx the parse tree
	 */
	void exitNonWildcardTypeArguments(CQNGrammarParser.NonWildcardTypeArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#typeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgumentsOrDiamond(CQNGrammarParser.TypeArgumentsOrDiamondContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#typeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgumentsOrDiamond(CQNGrammarParser.TypeArgumentsOrDiamondContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#nonWildcardTypeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void enterNonWildcardTypeArgumentsOrDiamond(CQNGrammarParser.NonWildcardTypeArgumentsOrDiamondContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#nonWildcardTypeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void exitNonWildcardTypeArgumentsOrDiamond(CQNGrammarParser.NonWildcardTypeArgumentsOrDiamondContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#superSuffix}.
	 * @param ctx the parse tree
	 */
	void enterSuperSuffix(CQNGrammarParser.SuperSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#superSuffix}.
	 * @param ctx the parse tree
	 */
	void exitSuperSuffix(CQNGrammarParser.SuperSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#explicitGenericInvocationSuffix}.
	 * @param ctx the parse tree
	 */
	void enterExplicitGenericInvocationSuffix(CQNGrammarParser.ExplicitGenericInvocationSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#explicitGenericInvocationSuffix}.
	 * @param ctx the parse tree
	 */
	void exitExplicitGenericInvocationSuffix(CQNGrammarParser.ExplicitGenericInvocationSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link CQNGrammarParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(CQNGrammarParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CQNGrammarParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(CQNGrammarParser.ArgumentsContext ctx);
}