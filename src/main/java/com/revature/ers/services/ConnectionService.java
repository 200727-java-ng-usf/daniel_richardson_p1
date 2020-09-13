package com.revature.ers.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ConnectionService
 * uses properties file for postgresql db connection
 */

//singleton
public class ConnectionService{
    private static ConnectionService connFactory = new ConnectionService();

    private ConnectionService() {
        super();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream propsInput = loader.getResourceAsStream("application.properties");
            props.load(propsInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ConnectionService getInstance() {
        return connFactory;
    }
    private Properties props = new Properties();

    public Connection getConnection() {
        Connection conn = null;
        try {
            // Force the JVM to load the PostGreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(
                    props.getProperty("url"),
                    props.getProperty("username"),
                    props.getProperty("password")
            );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
//        if (conn == null) {
//            System.out.println("Failed to connect");
//        }
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(
                        System.getenv("url"),
                        System.getenv("username"),
                        System.getenv("password")
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

}
