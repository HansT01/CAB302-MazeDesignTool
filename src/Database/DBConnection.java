package Database;
import GUI.InvalidInputException;
import Maze.*;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * DBConnection class to initialize and maintain a single connection with the database.
 * This class also stores the username and password of a given user.
 */
public class DBConnection {
    private static Connection instance = null;
    private static String schema;
    private static String username;
    private static String password;

    /**
     * Constructor for DBConnection. Takes in local db.props files and establishes a connection.
     */
    private DBConnection() throws InvalidInputException {
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream("db.props");
            props.load(in);
            in.close();

            // specify the data source, username and password
            String url = props.getProperty("jdbc.url");
            String user = props.getProperty("jdbc.username");
            String pass = props.getProperty("jdbc.password");
            schema = props.getProperty("jdbc.schema");

            // get a connection
            instance = DriverManager.getConnection(url + "/", user, pass);
        } catch (Exception e) {
            throw new InvalidInputException("Unable to create connection with db.props properties.");
        }
    }

    /**
     * Getter for username of current user.
     * @return Username string
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Getter for password of current user.
     * @return Password string
     */
    public static String getPassword() {
        return password;
    }

    /**
     * Getter for database schema.
     * @return Schema string
     */
    public static String getSchema() {
        return schema;
    }

    /**
     * Setter for username of current user.
     * @param s Username string
     */
    public static void setUsername(String s) {
        username = s;
    }

    /**
     * Setter for password of current password.
     * @param s Password string
     */
    public static void setPassword(String s) {
        password = s;
    }

    /**
     * Gets the current connection instance, if it exists. Otherwise, create a new connection.
     * @return Connection instance
     */
    public static Connection getInstance() {
        if (instance == null) {
            try {
                new DBConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        Connection test = getInstance();
        System.out.print(test);
    }
}

