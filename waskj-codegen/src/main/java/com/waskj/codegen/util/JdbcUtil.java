package com.waskj.codegen.util;

import com.waskj.codegen.config.JdbcConfig;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by poet on 2016/10/27.
 */
public abstract class JdbcUtil {

    public static Connection getConnection(JdbcConfig config){
        Connection result = null;
        try {
            Class.forName(config.getDriverClassName());
            result = DriverManager.getConnection(config.getJdbcUrl(),config.getUsername(),config.getPassword());
        } catch (Exception e) {
           wrapException(e);
        }
        return result;
    }

    public static Collection<String> getTables(JdbcConfig config){
        Connection connection = getConnection(config);
        DatabaseMetaData metaData = getDatabaseMetaData(connection);
        Collection<String> ret = getTables(metaData);

        closeConnectionQuietly(connection);
        return ret;
    }

    public static void closeConnectionQuietly(Connection connection){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseMetaData getDatabaseMetaData(Connection connection){
        try {
            return connection.getMetaData();
        } catch (SQLException e) {
            wrapException(e);
        }
        return null;
    }

    public static Collection<String> getTables(DatabaseMetaData metaData){
        Set<String> result = new HashSet<>();

        String types[] = new String[]{"TABLE"};
        try {
            Connection conn = metaData.getConnection();

            ResultSet rs = metaData.getTables(conn.getCatalog(), conn.getSchema(),null, types);
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                result.add(tableName);
            }
        } catch (SQLException e) {
            wrapException(e);
        }

        return result;
    }

    private static void wrapException(Exception e){
        throw new RuntimeException(e);
    }


}
