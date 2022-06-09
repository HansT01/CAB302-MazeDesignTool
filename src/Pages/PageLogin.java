package Pages;

import Database.DBConnection;
import Database.JDBCDataSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Constructs window for authenticating user
 */
public class PageLogin extends JFrame implements Runnable {
    GridBagManager gbm = new GridBagManager();

    // Labels
    JLabel nameLabel = new JLabel("Name");
    JLabel passwordLabel = new JLabel("Password");
    // Text Areas
    JTextField nameText = new JTextField();
    JTextField passwordText = new JTextField();
    // Button
    JButton login = new JButton("Login");
    // Associate user who logs in as the author
    public static String author;
    /**
     * Creates 'Login' JPanel object
     * @return JPanel object
     */
    private JPanel CreateLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );
        panel.setPreferredSize(new Dimension(300, 150));

        GridBagConstraints gbc;
        int gridRow = 0;

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(nameLabel, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(nameText, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(passwordLabel, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(passwordText, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow);
        gbc.gridwidth = 1;
        panel.add(login, gbc);

        return panel;
    }


    /**
     * Main create GUI method. Calls other create panel methods and adds them to the main frame.
     */
    public void CreateGUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        int gridRow = 0;

        // maze panel
        gbc = gbm.CreateOuterGBC(0, gridRow);
        add(CreateLoginPanel(), gbc);

        // resizes window to preferred dimensions
        pack();

        // centre to screen
        setLocationRelativeTo(null);

        // set defaults
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Login Button Action Event
        login.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                // Authenticate user
                if (e.getSource() == login) {
                    String userTxt = nameText.getText();
                    author = nameText.getText();
                    String passTxt = passwordText.getText();
                    try {
                        Connection connection = DBConnection.getInstance();
                        PreparedStatement auth = (PreparedStatement) connection.prepareStatement("Select name, password from user where name=? and password=?");

                        auth.setString(1, userTxt);
                        auth.setString(2, passTxt);
                        ResultSet rs = auth.executeQuery();
                        if (rs.next()) {
                            dispose();
                            JOptionPane.showMessageDialog(login, "Valid Credentials");
                            SwingUtilities.invokeLater(new PageDatabase());
                        }
                        else {
                            JOptionPane.showMessageDialog(login, "Invalid Credentials");
                        }
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void run() {
        CreateGUI();
    }
    public static void main(String[] args) throws SQLException {

        // Setup Login DB
        Connection connection = DBConnection.getInstance();
        Statement statement = connection.createStatement();
        statement.execute("CREATE DATABASE IF NOT EXISTS cab302;");
        statement.execute("use cab302;");
        statement.execute("DROP TABLE IF EXISTS user;");
        statement.execute("CREATE TABLE user ( name varchar(45) NOT NULL," +
                "password varchar(45) NOT NULL," +
                "PRIMARY KEY (`name`));");
        // Enter Accounts
        statement.execute("INSERT IGNORE INTO user VALUES('Test', '1234');");
        statement.execute("INSERT IGNORE INTO user VALUES('Test123', '1234');");
        statement.execute("INSERT IGNORE INTO user VALUES('Test321', '1234');");
        JDBCDataSource.main(new JDBCDataSource());
        SwingUtilities.invokeLater(new PageLogin());
    }
}