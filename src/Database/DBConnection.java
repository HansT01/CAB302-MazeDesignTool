package Database;
import Maze.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class DBConnection {
    private static Connection instance = null;
    private static String username;
    private static String password;

    private DBConnection() {
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream("db.props");
            props.load(in);
            in.close();

            // Register JDBC driver
            Class.forName("org.mariadb.jdbc.Driver");

            // specify the data source, username and password
            String url = props.getProperty("jdbc.url");
            String dbUser = props.getProperty("jdbc.username");
            String dbPassword = props.getProperty("jdbc.password");

            // get a connection
            instance = DriverManager.getConnection(url + "/", dbUser, dbPassword);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setUsername(String s) {
        username = s;
    }

    public static void setPassword(String s) {
        password = s;
    }

    public static Connection getInstance() {
        if (instance == null) {
            new DBConnection();
        }
        return instance;
    }


    //testing
    public static void main(String[] args) {
        Connection test = getInstance();
        System.out.print(test);
    }
}

