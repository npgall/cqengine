package com.googlecode.cqengine.index.offheap.support;

import com.googlecode.concurrenttrees.common.CharSequences;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.simple.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Database (SQLite) query executor used by the {@link com.googlecode.cqengine.index.offheap.OffHeapIndex}.
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

        final String sqlCreateIndex = String.format(
                "CREATE INDEX IF NOT EXISTS cqidx_%s_value ON cqtbl_%s (value);",
                tableName,
                tableName);

        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sqlCreateTable);
            statement.executeUpdate(sqlCreateIndex);
        }catch (SQLException e){
            throw new IllegalStateException("Unable to create index table: " + tableName, e);
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

    public static <K,A> void bulkAdd(Iterable<Row<K, A>> rows, final String tableName, final Connection connection){
        final String sql = String.format("INSERT OR REPLACE INTO cqtbl_%s values(?, ?);", tableName);
        PreparedStatement statement = null;
        Boolean previousAutocommit = null;
        try {
            previousAutocommit = DBUtils.setAutoCommit(connection, false);
            statement = connection.prepareStatement(sql);

            for (Row<K, A> row : rows) {
                statement.setObject(1, row.getObjectKey());
                statement.setObject(2, row.getValue());
                statement.addBatch();
            }
            statement.executeBatch();
            DBUtils.commit(connection);
        }catch (Exception e){
            boolean rolledBack = DBUtils.rollback(connection);
            throw new IllegalStateException("Unable to bulk add rows to the index table: "+ tableName + ". Rolled back: " + rolledBack, e);
        }finally {
            DBUtils.closeQuietly(statement);
            if (previousAutocommit != null)
                DBUtils.setAutoCommit(connection, previousAutocommit);
        }
    }

    public static <K> void bulkRemove(Iterable<K> objectKeys, final String tableName, final Connection connection){
        final String sql = String.format("DELETE FROM cqtbl_%s WHERE objectKey = ?;", tableName);
        PreparedStatement statement = null;
        Boolean previousAutocommit = null;
        try{
            previousAutocommit = DBUtils.setAutoCommit(connection, false);
            statement = connection.prepareStatement(sql);
            for(K objectKey: objectKeys) {
                statement.setObject(1, objectKey);
                statement.addBatch();
            }
            statement.executeBatch();
            DBUtils.commit(connection);
        }catch (Exception e){
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
                                                                         final List<WhereClause> additionalWhereClauses,
                                                                         final Query<O> query,
                                                                         final Connection connection) throws SQLException {

        int bindingIndex = 1;
        StringBuilder stringBuilder = new StringBuilder(selectPrefix).append(' ');
        StringBuilder suffix = new StringBuilder();
        if (additionalWhereClauses.size() == 0){
            suffix.append(';');
        }else{
            for (WhereClause additionalWhereClause : additionalWhereClauses){
                suffix.append(' ').append(additionalWhereClause.whereClause);
            }
            suffix.append(';');
        }

        final Class queryClass = query.getClass();
        PreparedStatement statement;

        if (queryClass == All.class){
            stringBuilder.append(suffix);
            statement = connection.prepareStatement(stringBuilder.toString());

        }else if (queryClass == Equal.class){
            final Equal<O, A> equal =(Equal<O, A>) query;
            stringBuilder.append("WHERE value = ?").append(suffix);
            statement = connection.prepareStatement(stringBuilder.toString());
            DBUtils.setValueToPreparedStatement(bindingIndex++, statement, equal.getValue());

        }else if (queryClass == LessThan.class){
            final LessThan<O, ? extends Comparable<A>> lessThan =(LessThan<O, ? extends Comparable<A>>) query;
            boolean isValueInclusive = lessThan.isValueInclusive();
            if (isValueInclusive){
                stringBuilder.append("WHERE value <= ?").append(suffix);
            }else{
                stringBuilder.append("WHERE value < ?").append(suffix);
            }
            statement = connection.prepareStatement(stringBuilder.toString());
            DBUtils.setValueToPreparedStatement(bindingIndex++, statement, lessThan.getValue());

        }else if (queryClass == StringStartsWith.class){
            final StringStartsWith<O, ? extends CharSequence> stringStartsWith =(StringStartsWith<O, ? extends CharSequence>) query;
            stringBuilder.append("WHERE value LIKE ?").append(suffix);
            final String prefix = CharSequences.toString(stringStartsWith.getValue());
            statement = connection.prepareStatement(stringBuilder.toString());
            DBUtils.setValueToPreparedStatement(bindingIndex++, statement, prefix + '%');

        }else if (queryClass == GreaterThan.class){
            final GreaterThan<O, ? extends Comparable<A>> greaterThan = (GreaterThan<O, ? extends Comparable<A>>)query;
            boolean isValueInclusive = greaterThan.isValueInclusive();
            if (isValueInclusive){
                stringBuilder.append("WHERE value >= ?").append(suffix);
            }else{
                stringBuilder.append("WHERE value > ?").append(suffix);
            }
            statement = connection.prepareStatement(stringBuilder.toString());
            DBUtils.setValueToPreparedStatement(bindingIndex++, statement, greaterThan.getValue());

        }else if (queryClass == Between.class){
            final Between<O, ? extends Comparable<A>> between = (Between<O, ? extends Comparable<A>>)query;
            if (between.isLowerInclusive()){
                stringBuilder.append("WHERE value >= ?");
            }else{
                stringBuilder.append("WHERE value > ?");
            }
            if (between.isUpperInclusive()){
                stringBuilder.append(" AND value <= ?");
            }else{
                stringBuilder.append(" AND value < ?");
            }
            stringBuilder.append(suffix);
            statement = connection.prepareStatement(stringBuilder.toString());
            DBUtils.setValueToPreparedStatement(bindingIndex++, statement, between.getLowerValue());
            DBUtils.setValueToPreparedStatement(bindingIndex++, statement, between.getUpperValue());

        }else{
            throw new IllegalStateException("Query " + queryClass + " not supported.");
        }

        for (WhereClause additionalWhereClause : additionalWhereClauses){
            DBUtils.setValueToPreparedStatement(bindingIndex++, statement, additionalWhereClause.objectToBind);
        }

        return statement;
    }

    public static <O> int count(final Query<O> query, final String tableName, final Connection connection){

        final String selectSql = String.format("SELECT COUNT(objectKey) FROM cqtbl_%s", tableName);
        PreparedStatement statement = null;
        try{
            statement = createAndBindSelectPreparedStatement(selectSql, Collections.<WhereClause>emptyList(), query, connection);
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
        final String selectSql = String.format("SELECT objectKey, value FROM cqtbl_%s",tableName);
        PreparedStatement statement = null;
        try{
            statement = createAndBindSelectPreparedStatement(selectSql, Collections.<WhereClause>emptyList(), query, connection);
            return statement.executeQuery();
        }catch(Exception e){
            DBUtils.closeQuietly(statement);
            throw new IllegalStateException("Unable to execute search. Query: " + query, e);
        }
        // In case of success we leave the statement and result-set open because the iteration of an Index ResultSet is lazy.

    }

    public static <K, O> boolean contains(final K objectKey, final Query<O> query, final String tableName, final Connection connection){
        final String selectSql = String.format("SELECT COUNT(objectKey) FROM cqtbl_%s", tableName);
        PreparedStatement statement = null;
        try{
            List<WhereClause> additionalWhereClauses = Arrays.asList(new WhereClause("AND objectKey = ?", objectKey));
            statement = createAndBindSelectPreparedStatement(selectSql, additionalWhereClauses, query, connection);
            java.sql.ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()){
                throw new IllegalStateException("Unable to execute contains. The ResultSet returned no row. Query: " + query);
            }

            return resultSet.getInt(1) > 0;
        }catch (SQLException e){
            throw new IllegalStateException("Unable to execute contains. Query: " + query, e);
        }finally{
            DBUtils.closeQuietly(statement);
        }
    }

}
