import GUI.PageLogin;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static final String DB_URL = "jdbc:mariadb://localhost:3306";
    public static final String USER = "root";
    public static final String PASS = "root";


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new PageLogin());

        // Open a connection
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = conn.createStatement();
        ) {
            String create = "CREATE DATABASE IF NOT EXISTS cab302";
            statement.executeUpdate(create);
            System.out.println("Database created successfully...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
