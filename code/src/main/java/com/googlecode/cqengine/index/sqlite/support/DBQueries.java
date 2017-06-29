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
package com.googlecode.cqengine.index.sqlite.support;

import com.googlecode.concurrenttrees.common.CharSequences;
import com.googlecode.cqengine.index.sqlite.SQLiteIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.simple.*;
import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Database (SQLite) query executor used by the {@link SQLiteIndex}.
 *
 * @author Silvano Riz
 */
public class DBQueries {

    /**
     * Represents a table row (objectId, value).
     *
     * @param <K> The type of the objectId.
     * @param <A> The type of the value.
     */
    public static class Row<K, A>{
        private final K objectKey;
        private final A value;

        public Row(K objectKey, A value) {
            this.objectKey = objectKey;
            this.value = value;
        }

        public K getObjectKey() {
            return objectKey;
        }

        public A getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Row row = (Row) o;

            if (!objectKey.equals(row.objectKey)) return false;
            if (!value.equals(row.value)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = objectKey.hashCode();
            result = 31 * result + value.hashCode();
            return result;
        }
    }

    public static <K, A> void createIndexTable(final String tableName, final Class<K> objectKeyClass, final Class<A> valueClass, final Connection connection){

        final String objectKeySQLiteType = DBUtils.getDBTypeForClass(objectKeyClass);
        final String objectValueSQLiteType = DBUtils.getDBTypeForClass(valueClass);

        final String sqlCreateTable = String.format(
                "CREATE TABLE IF NOT EXISTS cqtbl_%s (objectKey %s, value %s, PRIMARY KEY (objectKey, value)) WITHOUT ROWID;",
                tableName,
                objectKeySQLiteType,
                objectValueSQLiteType);

        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sqlCreateTable);
        }catch (SQLException e){
            throw new IllegalStateException("Unable to create index table: " + tableName, e);
        }finally {
            DBUtils.closeQuietly(statement);
        }
    }

    public static boolean indexTableExists(final String tableName, final Connection connection) {
        final String selectSql = String.format("SELECT 1 FROM sqlite_master WHERE type='table' AND name='cqtbl_%s';", tableName);
        Statement statement = null;
        try{
            statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery(selectSql);
            return resultSet.next();
        }catch(Exception e){
            throw new IllegalStateException("Unable to determine if table exists: " + tableName, e);
        }
        finally {
            DBUtils.closeQuietly(statement);
        }
    }

    public static void createIndexOnTable(final String tableName, final Connection connection){
        final String sqlCreateIndex = String.format(
                "CREATE INDEX IF NOT EXISTS cqidx_%s_value ON cqtbl_%s (value);",
                tableName,
                tableName);
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sqlCreateIndex);
        }catch (SQLException e){
            throw new IllegalStateException("Unable to add index on table: " + tableName, e);
        }finally {
            DBUtils.closeQuietly(statement);
        }
    }

    public static void suspendSyncAndJournaling(final Connection connection){
        setSyncAndJournaling(connection, SQLiteConfig.SynchronousMode.OFF, SQLiteConfig.JournalMode.OFF);
    }

    public static void setSyncAndJournaling(final Connection connection, final SQLiteConfig.SynchronousMode pragmaSynchronous, final SQLiteConfig.JournalMode pragmaJournalMode){
        Statement statement = null;
        try {
            final boolean autoCommit = DBUtils.setAutoCommit(connection, true);
            statement = connection.createStatement();
            statement.execute("PRAGMA synchronous = " + pragmaSynchronous.getValue());

            // This little transaction will also cause a wanted fsync on the OS to flush the data still in the OS cache to disc.
            statement.execute("PRAGMA journal_mode = " + pragmaJournalMode.getValue());

            DBUtils.setAutoCommit(connection, autoCommit);
        }catch (SQLException e){
            throw new IllegalStateException("Unable to set the 'synchronous' and 'journal_mode' pragmas", e);
        }finally{
            DBUtils.closeQuietly(statement);
        }
    }

    public static SQLiteConfig.SynchronousMode getPragmaSynchronousOrNull(final Connection connection){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("PRAGMA synchronous;");
            if (resultSet.next()){
                final int syncPragmaId = resultSet.getInt(1);
                if (!resultSet.wasNull()) {
                    switch (syncPragmaId){
                        case 0: return SQLiteConfig.SynchronousMode.OFF;
                        case 1: return SQLiteConfig.SynchronousMode.NORMAL;
                        case 2: return SQLiteConfig.SynchronousMode.FULL;
                        default: return null;
                    }
                }
            }
            return null;
        }catch (Exception e){
            return null;
        }finally{
            DBUtils.closeQuietly(statement);
        }
    }

    public static SQLiteConfig.JournalMode getPragmaJournalModeOrNull(final Connection connection){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("PRAGMA journal_mode;");
            if (resultSet.next()){
                final String journalMode = resultSet.getString(1);
                return journalMode != null ? SQLiteConfig.JournalMode.valueOf(journalMode.toUpperCase()) : null;
            }
            return null;
        }catch (Exception e){
            return null;
        }finally{
            DBUtils.closeQuietly(statement);
        }
    }

    public static void dropIndexOnTable(final String tableName, final Connection connection){
        final String sqlDropIndex = String.format("DROP INDEX IF EXISTS cqidx_%s_value;",tableName);
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sqlDropIndex);
        }catch (SQLException e){
            throw new IllegalStateException("Unable to drop index on table: " + tableName, e);
        }finally {
            DBUtils.closeQuietly(statement);
        }
    }

    public static void dropIndexTable(final String tableName, final Connection connection){
        final String sqlDropIndex = String.format("DROP INDEX IF EXISTS cqidx_%s_value;",tableName);
        final String sqlDropTable = String.format("DROP TABLE IF EXISTS cqtbl_%s;", tableName);
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sqlDropIndex);
            statement.executeUpdate(sqlDropTable);
        }catch (SQLException e){
            throw new IllegalStateException("Unable to drop index table: "+ tableName, e);
        }finally{
            DBUtils.closeQuietly(statement);
        }
    }

    public static void clearIndexTable(final String tableName, final Connection connection){
        final String clearTable = String.format("DELETE FROM cqtbl_%s;",tableName);
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(clearTable);
        }catch (SQLException e){
            throw new IllegalStateException("Unable to clear index table: " + tableName, e);
        }finally{
            DBUtils.closeQuietly(statement);
        }
    }

    public static void compactDatabase(final Connection connection){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute("VACUUM;");
        }catch (SQLException e){
            throw new IllegalStateException("Unable to compact database", e);
        }finally{
            DBUtils.closeQuietly(statement);
        }
    }

    public static void expandDatabase(final Connection connection, long numBytes) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS cq_expansion (val);");
            statement.execute("INSERT INTO cq_expansion VALUES (zeroblob(" + numBytes + "));");
            statement.execute("DROP TABLE cq_expansion;");

        }catch (SQLException e){
            throw new IllegalStateException("Unable to expand database by bytes: " + numBytes, e);
        }finally{
            DBUtils.closeQuietly(statement);
        }
    }

    public static long getDatabaseSize(final Connection connection){
        long pageCount = readPragmaLong(connection, "PRAGMA page_count;");
        long pageSize = readPragmaLong(connection, "PRAGMA page_size;");
        return pageCount * pageSize;
    }

    static long readPragmaLong(final Connection connection, String query) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()){
                throw new IllegalStateException("Unable to read long from pragma query. The ResultSet returned no row. Query: " + query);
            }
            return resultSet.getLong(1);
        }catch (SQLException e){
            throw new IllegalStateException("Unable to read long from pragma query", e);
        }finally{
            DBUtils.closeQuietly(statement);
        }
    }

    public static <K,A> int bulkAdd(Iterable<Row<K, A>> rows, final String tableName, final Connection connection){
        final String sql = String.format("INSERT OR IGNORE INTO cqtbl_%s values(?, ?);", tableName);
        PreparedStatement statement = null;
        int totalRowsModified = 0;
        try {
            statement = connection.prepareStatement(sql);

            for (Row<K, A> row : rows) {
                statement.setObject(1, row.getObjectKey());
                statement.setObject(2, row.getValue());
                statement.addBatch();
            }
            int[] rowsModified = statement.executeBatch();
            for (int m : rowsModified) {
                ensureNotNegative(m);
                totalRowsModified += m;
            }
            return totalRowsModified;
        }
        catch (NullPointerException e) {
            // Note: here we catch a and rethrow NullPointerException,
            // to allow compatibility with Java Collections Framework,
            // which requires NPE to be thrown for null arguments...
            boolean rolledBack = DBUtils.rollback(connection);
            NullPointerException npe = new NullPointerException("Unable to bulk add rows containing a null object to the index table: "+ tableName + ". Rolled back: " + rolledBack);
            npe.initCause(e);
            throw npe;
        }
        catch (Exception e){
            boolean rolledBack = DBUtils.rollback(connection);
            throw new IllegalStateException("Unable to bulk add rows to the index table: "+ tableName + ". Rolled back: " + rolledBack, e);
        }finally {
            DBUtils.closeQuietly(statement);
        }
    }

    public static <K> int bulkRemove(Iterable<K> objectKeys, final String tableName, final Connection connection){
        final String sql = String.format("DELETE FROM cqtbl_%s WHERE objectKey = ?;", tableName);
        PreparedStatement statement = null;
        Boolean previousAutocommit = null;
        int totalRowsModified = 0;
        try{
            previousAutocommit = DBUtils.setAutoCommit(connection, false);
            statement = connection.prepareStatement(sql);
            for(K objectKey: objectKeys) {
                statement.setObject(1, objectKey);
                statement.addBatch();
            }
            int[] rowsModified = statement.executeBatch();
            for (int m : rowsModified) {
                ensureNotNegative(m);
                totalRowsModified += m;
            }
            DBUtils.commit(connection);
            return totalRowsModified;
        }
        catch (NullPointerException e) {
            // Note: here we catch a and rethrow NullPointerException,
            // to allow compatibility with Java Collections Framework,
            // which requires NPE to be thrown for null arguments...
            boolean rolledBack = DBUtils.rollback(connection);
            NullPointerException npe = new NullPointerException("Unable to bulk remove rows containing a null object from the index table: "+ tableName + ". Rolled back: " + rolledBack);
            npe.initCause(e);
            throw npe;
        }
        catch (Exception e){
            boolean rolledBack = DBUtils.rollback(connection);
            throw new IllegalStateException("Unable to remove rows from the index table: " + tableName + ". Rolled back: " + rolledBack, e);
        }finally{
            DBUtils.closeQuietly(statement);
            if (previousAutocommit != null)
                DBUtils.setAutoCommit(connection, previousAutocommit);
        }
    }

    static class WhereClause{
        final String whereClause;
        final Object objectToBind;
        WhereClause(String whereClause, Object objectToBind){
            this.whereClause = whereClause;
            this.objectToBind = objectToBind;
        }
    }

    static <O, A> PreparedStatement createAndBindSelectPreparedStatement(final String selectPrefix,
                                                                         final String groupingAndSorting,
                                                                         final List<WhereClause> additionalWhereClauses,
                                                                         final Query<O> query,
                                                                         final Connection connection) throws SQLException {

        int bindingIndex = 1;
        StringBuilder stringBuilder = new StringBuilder(selectPrefix).append(' ');
        StringBuilder suffix = new StringBuilder();
        final Class queryClass = query.getClass();
        PreparedStatement statement;

        if (queryClass == Has.class){
            // Has is a special case, because there is no WHERE clause by default.
            if (additionalWhereClauses.isEmpty()){
                suffix.append(groupingAndSorting);
                suffix.append(';');
            }else{
                stringBuilder.append("WHERE ");
                for (Iterator<WhereClause> iterator = additionalWhereClauses.iterator(); iterator.hasNext(); ) {
                    WhereClause additionalWhereClause = iterator.next();
                    suffix.append(additionalWhereClause.whereClause);
                    if (iterator.hasNext()) {
                        suffix.append(" AND ");
                    }
                }
                suffix.append(groupingAndSorting);
                suffix.append(';');
            }
            stringBuilder.append(suffix);
            statement = connection.prepareStatement(stringBuilder.toString());

        }else {
            // Other queries have a WHERE clause by default.
            if (additionalWhereClauses.isEmpty()){
                suffix.append(groupingAndSorting);
                suffix.append(';');
            }else{
                for (WhereClause additionalWhereClause : additionalWhereClauses){
                    suffix.append(" AND ").append(additionalWhereClause.whereClause);
                }
                suffix.append(groupingAndSorting);
                suffix.append(';');
            }
            if (queryClass == Equal.class) {
                @SuppressWarnings("unchecked")
                final Equal<O, A> equal = (Equal<O, A>) query;
                stringBuilder.append("WHERE value = ?").append(suffix);
                statement = connection.prepareStatement(stringBuilder.toString());
                DBUtils.setValueToPreparedStatement(bindingIndex++, statement, equal.getValue());
            } else if (queryClass == In.class){
                @SuppressWarnings("unchecked")
                final In<O, A> in = (In<O, A>) query;
                Set<A> values = in.getValues();
                stringBuilder.append("WHERE value IN ( ");
                for (int i=0; i<values.size(); i++){
                    if (i > 0){
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append("?");
                }
                stringBuilder.append(")").append(suffix);
                statement = connection.prepareStatement(stringBuilder.toString());
                bindingIndex = DBUtils.setValuesToPreparedStatement(bindingIndex, statement, values);
            } else if (queryClass == LessThan.class) {
                @SuppressWarnings("unchecked")
                final LessThan<O, ? extends Comparable<A>> lessThan = (LessThan<O, ? extends Comparable<A>>) query;
                boolean isValueInclusive = lessThan.isValueInclusive();
                if (isValueInclusive) {
                    stringBuilder.append("WHERE value <= ?").append(suffix);
                } else {
                    stringBuilder.append("WHERE value < ?").append(suffix);
                }
                statement = connection.prepareStatement(stringBuilder.toString());
                DBUtils.setValueToPreparedStatement(bindingIndex++, statement, lessThan.getValue());

            } else if (queryClass == StringStartsWith.class) {
                final StringStartsWith<O, ? extends CharSequence> stringStartsWith = (StringStartsWith<O, ? extends CharSequence>) query;
                stringBuilder.append("WHERE value >= ? AND value < ?").append(suffix);
                final String lowerBoundInclusive = CharSequences.toString(stringStartsWith.getValue());
                final int len = lowerBoundInclusive.length();
                final String allButLast = lowerBoundInclusive.substring(0, len - 1);
                final String upperBoundExclusive = allButLast + Character.toChars(lowerBoundInclusive.charAt(len - 1) + 1)[0];

                statement = connection.prepareStatement(stringBuilder.toString());
                DBUtils.setValueToPreparedStatement(bindingIndex++, statement, lowerBoundInclusive);
                DBUtils.setValueToPreparedStatement(bindingIndex++, statement, upperBoundExclusive);

            } else if (queryClass == GreaterThan.class) {
                @SuppressWarnings("unchecked")
                final GreaterThan<O, ? extends Comparable<A>> greaterThan = (GreaterThan<O, ? extends Comparable<A>>) query;
                boolean isValueInclusive = greaterThan.isValueInclusive();
                if (isValueInclusive) {
                    stringBuilder.append("WHERE value >= ?").append(suffix);
                } else {
                    stringBuilder.append("WHERE value > ?").append(suffix);
                }
                statement = connection.prepareStatement(stringBuilder.toString());
                DBUtils.setValueToPreparedStatement(bindingIndex++, statement, greaterThan.getValue());

            } else if (queryClass == Between.class) {
                @SuppressWarnings("unchecked")
                final Between<O, ? extends Comparable<A>> between = (Between<O, ? extends Comparable<A>>) query;
                if (between.isLowerInclusive()) {
                    stringBuilder.append("WHERE value >= ?");
                } else {
                    stringBuilder.append("WHERE value > ?");
                }
                if (between.isUpperInclusive()) {
                    stringBuilder.append(" AND value <= ?");
                } else {
                    stringBuilder.append(" AND value < ?");
                }
                stringBuilder.append(suffix);
                statement = connection.prepareStatement(stringBuilder.toString());
                DBUtils.setValueToPreparedStatement(bindingIndex++, statement, between.getLowerValue());
                DBUtils.setValueToPreparedStatement(bindingIndex++, statement, between.getUpperValue());

            } else {
                throw new IllegalStateException("Query " + queryClass + " not supported.");
            }
        }

        for (WhereClause additionalWhereClause : additionalWhereClauses){
            DBUtils.setValueToPreparedStatement(bindingIndex++, statement, additionalWhereClause.objectToBind);
        }

        return statement;
    }

    public static <O> int count(final Query<O> query, final String tableName, final Connection connection) {

        final String selectSql = String.format("SELECT COUNT(objectKey) FROM cqtbl_%s", tableName);
        PreparedStatement statement = null;
        try {
            statement = createAndBindSelectPreparedStatement(selectSql, "", Collections.<WhereClause>emptyList(), query, connection);
            java.sql.ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                throw new IllegalStateException("Unable to execute count. The ResultSet returned no row. Query: " + query);
            }

            return resultSet.getInt(1);
        }
        catch (Exception e) {
            throw new IllegalStateException("Unable to execute count. Query: " + query, e);
        }
        finally {
            DBUtils.closeQuietly(statement);
        }
    }

    public static <O> int countDistinct(final Query<O> query, final String tableName, final Connection connection){

        // NOTE: Using GROUP BY is much faster than using SELECT DISTINCT in SQLite for deduplication
        final String selectSql = String.format("SELECT COUNT(1) AS countDistinct FROM (SELECT objectKey FROM cqtbl_%s", tableName);
        PreparedStatement statement = null;
        try{
            statement = createAndBindSelectPreparedStatement(selectSql, " GROUP BY objectKey)", Collections.<WhereClause>emptyList(), query, connection);
            java.sql.ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()){
                throw new IllegalStateException("Unable to execute count. The ResultSet returned no row. Query: " + query);
            }

            return resultSet.getInt(1);
        }catch(Exception e){
            throw new IllegalStateException("Unable to execute count. Query: " + query, e);
        }finally {
            DBUtils.closeQuietly(statement);
        }

    }

    public static <O> java.sql.ResultSet search(final Query<O> query, final String tableName, final Connection connection){
        final String selectSql = String.format("SELECT DISTINCT objectKey FROM cqtbl_%s",tableName);
        PreparedStatement statement = null;
        try{
            statement = createAndBindSelectPreparedStatement(selectSql, "", Collections.<WhereClause>emptyList(), query, connection);
            return statement.executeQuery();
        }catch(Exception e){
            DBUtils.closeQuietly(statement);
            throw new IllegalStateException("Unable to execute search. Query: " + query, e);
        }
        // In case of success we leave the statement and result-set open because the iteration of an Index ResultSet is lazy.

    }

    public static <O> java.sql.ResultSet getDistinctKeys(final Query<O> query, boolean descending, final String tableName, final Connection connection){
        final String selectSql = String.format("SELECT DISTINCT value FROM cqtbl_%s",tableName);
        PreparedStatement statement = null;
        try{
            String orderByClause = descending ? " ORDER BY value DESC" : " ORDER BY value ASC";
            statement = createAndBindSelectPreparedStatement(selectSql, orderByClause, Collections.<WhereClause>emptyList(), query, connection);
            return statement.executeQuery();
        }catch(Exception e){
            DBUtils.closeQuietly(statement);
            throw new IllegalStateException("Unable to look up keys. Query: " + query, e);
        }
        // In case of success we leave the statement and result-set open because the iteration of an Index ResultSet is lazy.

    }

    public static <O> java.sql.ResultSet getKeysAndValues(final Query<O> query, boolean descending, final String tableName, final Connection connection){
        final String selectSql = String.format("SELECT objectKey, value FROM cqtbl_%s",tableName);
        PreparedStatement statement = null;
        try{
            String orderByClause = descending ? " ORDER BY value DESC" : " ORDER BY value ASC";
            statement = createAndBindSelectPreparedStatement(selectSql, orderByClause, Collections.<WhereClause>emptyList(), query, connection);
            return statement.executeQuery();
        }catch(Exception e){
            DBUtils.closeQuietly(statement);
            throw new IllegalStateException("Unable to look up keys and values. Query: " + query, e);
        }
        // In case of success we leave the statement and result-set open because the iteration of an Index ResultSet is lazy.

    }

    public static int getCountOfDistinctKeys(final String tableName, final Connection connection){
        final String selectSql = String.format("SELECT COUNT(DISTINCT value) FROM cqtbl_%s",tableName);
        Statement statement = null;
        try{
            statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery(selectSql);
            if (!resultSet.next()){
                throw new IllegalStateException("Unable to execute count. The ResultSet returned no row. Query: " + selectSql);
            }

            return resultSet.getInt(1);
        }catch(Exception e){
            DBUtils.closeQuietly(statement);
            throw new IllegalStateException("Unable to count distinct keys.", e);
        }
    }

    public static java.sql.ResultSet getDistinctKeysAndCounts(boolean sortByKeyDescending, final String tableName, final Connection connection){
        final String selectSql = String.format("SELECT DISTINCT value, COUNT(value) AS valueCount FROM cqtbl_%s GROUP BY (value) %s", tableName, sortByKeyDescending ? "ORDER BY value DESC" : "");
        Statement statement = null;
        try{
            statement = connection.createStatement();
            return statement.executeQuery(selectSql);
        }catch(Exception e){
            DBUtils.closeQuietly(statement);
            throw new IllegalStateException("Unable to look up index entries and counts.", e);
        }
        // In case of success we leave the statement and result-set open because the iteration of an Index ResultSet is lazy.
    }

    public static java.sql.ResultSet getAllIndexEntries(final String tableName, final Connection connection){
        final String selectSql = String.format("SELECT objectKey, value FROM cqtbl_%s ORDER BY objectKey;",tableName);
        Statement statement = null;
        try{
            statement = connection.createStatement();
            return statement.executeQuery(selectSql);
        }catch(Exception e){
            DBUtils.closeQuietly(statement);
            throw new IllegalStateException("Unable to look up index entries.", e);
        }
        // In case of success we leave the statement and result-set open because the iteration of an Index ResultSet is lazy.

    }

    public static <K> java.sql.ResultSet getIndexEntryByObjectKey(final K key , final String tableName, final Connection connection){
        final String selectSql = String.format("SELECT objectKey, value FROM cqtbl_%s WHERE objectKey = ?",tableName);
        PreparedStatement statement = null;
        try{
            statement = connection.prepareStatement(selectSql);
            DBUtils.setValueToPreparedStatement(1, statement, key);
            return statement.executeQuery();
        }catch(Exception e){
            DBUtils.closeQuietly(statement);
            throw new IllegalStateException("Unable to look up index entries.", e);
        }
        // In case of success we leave the statement and result-set open because the iteration of an Index ResultSet is lazy.

    }

    public static <K, O> boolean contains(final K objectKey, final Query<O> query, final String tableName, final Connection connection){
        final String selectSql = String.format("SELECT objectKey FROM cqtbl_%s", tableName);
        PreparedStatement statement = null;
        try{
            List<WhereClause> additionalWhereClauses = Collections.singletonList(new WhereClause("objectKey = ?", objectKey));
            statement = createAndBindSelectPreparedStatement(selectSql, " LIMIT 1", additionalWhereClauses, query, connection);
            java.sql.ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            throw new IllegalStateException("Unable to execute contains. Query: " + query, e);
        }finally{
            DBUtils.closeQuietly(statement);
        }
    }

    static void ensureNotNegative(int value) {
        if (value < 0) throw new IllegalStateException("Update returned error code: " + value);
    }
}
