/**
 * Copyright 2012-2015 Niall Gallagher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
grammar CQNGrammar;
import Java;

start : query EOF;

query : logicalQuery | simpleQuery ;

logicalQuery : andQuery | orQuery | notQuery ;

andQuery : 'and' LPAREN query (',' query)+ RPAREN ;
orQuery : 'or' LPAREN query (',' query)+ RPAREN ;
notQuery : 'not' LPAREN query RPAREN ;

simpleQuery : equalQuery
            | lessThanOrEqualToQuery
            | lessThanQuery
            | greaterThanOrEqualToQuery
            | greaterThanQuery
            | verboseBetweenQuery
            | betweenQuery
            | inQuery
            | startsWithQuery
            | endsWithQuery
            | containsQuery
            | isContainedInQuery
            | matchesRegexQuery
            | hasQuery
            | allQuery
            | noneQuery
            ;

equalQuery : 'equal' LPAREN attributeName ',' queryParameter RPAREN ;
lessThanOrEqualToQuery : 'lessThanOrEqualTo' LPAREN attributeName ',' queryParameter RPAREN ;
lessThanQuery : 'lessThan' LPAREN attributeName ',' queryParameter RPAREN ;
greaterThanOrEqualToQuery : 'greaterThanOrEqualTo' LPAREN attributeName ',' queryParameter RPAREN ;
greaterThanQuery : 'greaterThan' LPAREN attributeName ',' queryParameter RPAREN ;
verboseBetweenQuery : 'between' LPAREN attributeName ',' queryParameter ',' BooleanLiteral ',' queryParameter ',' BooleanLiteral RPAREN ;
betweenQuery : 'between' LPAREN attributeName ',' queryParameter ',' queryParameter RPAREN ;
inQuery : 'in' LPAREN attributeName ',' queryParameter (',' queryParameter)* RPAREN ;
startsWithQuery : 'startsWith' LPAREN attributeName ',' stringQueryParameter RPAREN ;
endsWithQuery : 'endsWith' LPAREN attributeName ',' stringQueryParameter RPAREN ;
containsQuery : 'contains' LPAREN attributeName ',' stringQueryParameter RPAREN ;
isContainedInQuery : 'isContainedIn' LPAREN attributeName ',' stringQueryParameter RPAREN ;
matchesRegexQuery : 'matchesRegex' LPAREN attributeName ',' stringQueryParameter RPAREN ;
hasQuery : 'has' LPAREN attributeName RPAREN ;
allQuery : 'all' LPAREN objectType '.class' RPAREN ;
noneQuery : 'none' LPAREN objectType '.class' RPAREN ;

objectType : Identifier ;
attributeName : StringLiteral ;
queryParameter : literal | Identifier;
stringQueryParameter : StringLiteral ;
