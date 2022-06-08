package DB.db;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DBConnection {
    private Connection connection;

    private static Connection instance = null;

    private DBConnection() {
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream("./src/DB/db/db.props");
            props.load(in);
            in.close();

            // specify the data source, username and password
            String url = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");
            String schema = props.getProperty("jdbc.schema");

            // get a connection
            instance = DriverManager.getConnection(url + "/", username,
                    password);
        } catch (SQLException sqle) {
            System.err.println(sqle);
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

