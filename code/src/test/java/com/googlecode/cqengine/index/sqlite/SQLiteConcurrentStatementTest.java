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
package com.googlecode.cqengine.index.sqlite;

import com.googlecode.cqengine.index.sqlite.TemporaryDatabase.TemporaryInMemoryDatabase;
import com.googlecode.cqengine.index.sqlite.support.DBUtils;
import com.googlecode.cqengine.query.QueryFactory;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.googlecode.cqengine.query.QueryFactory.noQueryOptions;

/**
 * Tests that we can open more than one JDBC Statement and ResultSet per Connection, and use them both at the same time
 * (from the same thread). This may be a support occurrence in CQEngine when multiple SQL queries may be open from the
 * same SQLite database simultaneously, but accessed lazily, to evaluate a single request.
 *
 * @author niall.gallagher
 */
public class SQLiteConcurrentStatementTest {

    @Rule
    public TemporaryInMemoryDatabase temporaryDatabase = new TemporaryInMemoryDatabase();

    @Test
    public void testConcurrentStatements() throws SQLException {
        Connection connection = temporaryDatabase.getConnectionManager(true).getConnection(null, noQueryOptions());
        final int NUM_ROWS = 10;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS test_table (column1 INTEGER, column2 BLOB, PRIMARY KEY (column1, column2)) WITHOUT ROWID;");
            stmt.executeUpdate("CREATE INDEX IF NOT EXISTS test_index ON test_table (column1);");
            stmt.close();
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO test_table (column1, column2) VALUES (?, ?);");
            for (int i = 0; i < NUM_ROWS; i++) {
                pstmt.setObject(1, i);
                pstmt.setObject(2, createBytes(i));
                pstmt.executeUpdate();
                pstmt.clearParameters();
            }
            pstmt.close();

            PreparedStatement rstmt1 = connection.prepareStatement("SELECT * FROM test_table WHERE column1 >= 3 AND column1 <= 5;");
            ResultSet rs1 = rstmt1.executeQuery();
            Map<Integer, Integer> results = new LinkedHashMap<Integer, Integer>();
            while (rs1.next()) {
                Integer column1 = rs1.getInt(1);
                byte[] column2 = rs1.getBytes(2);
                PreparedStatement rstmt2 = connection.prepareStatement("SELECT COUNT(column2) FROM test_table WHERE column1 >= 3 AND column1 <= 5 AND column2 = ?;");
                rstmt2.setBytes(1, column2);
                ResultSet rs2 = rstmt2.executeQuery();
                Assert.assertTrue(rs2.next());
                Integer count = rs2.getInt(1);
                rs2.close();
                rstmt2.close();
                results.put(column1, count);
            }
            rs1.close();
            rstmt1.close();
            Assert.assertEquals(3, results.size());
            Assert.assertEquals(Integer.valueOf(1), results.get(3));
            Assert.assertEquals(Integer.valueOf(1), results.get(4));
            Assert.assertEquals(Integer.valueOf(1), results.get(5));
        }
        finally {
            DBUtils.closeQuietly(connection);
        }
    }

    static byte[] createBytes(int rowNumber) {
        final int NUM_BYTES = 50;
        byte[] result = new byte[50];
        for (int i = 0; i < NUM_BYTES; i++) {
            result[i] = (byte) (rowNumber + i);
        }
        return result;
    }
}
