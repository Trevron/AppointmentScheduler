package com.trevormetcalf.utility;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
    This is a utility class used for connecting to a remote database using JDBC.
 */

public class DBConnection {

    // The database information has been removed due to uploading to github
    private static final String dbName = "";
    private static final String dbURL = "" + dbName;
    private static final String dbUserName = "";
    private static final String dbPassword = "";
    private static final String driver = "com.mysql.jdbc.Driver";
    static Connection conn;

    public static void makeConnection() throws ClassNotFoundException, SQLException, Exception {
        Class.forName(driver);
        conn = (Connection) DriverManager.getConnection(dbURL, dbUserName, dbPassword);
        System.out.println("Connection successful!");

    }

    public static void closeConnection() throws ClassNotFoundException, SQLException, Exception {
        conn.close();
        System.out.println("Connection closed!");
    }
}
