package GUI;

import Database.DBConnection;
import Database.JDBCDataSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

/**
 * Constructs window for authenticating user
 */
public class PageLogin extends JFrame implements Runnable {
    GridBagManager gbm = new GridBagManager();
    JDBCDataSource data;

    // Labels
    JLabel nameLabel = new JLabel("Username");
    JLabel passwordLabel = new JLabel("Password");
    // Text Areas
    JTextField usernameText = new JTextField();
    JTextField passwordText = new JTextField();
    // Button
    JButton login = new JButton("Login");
    JButton register = new JButton("Register");

    public PageLogin() {
        SetupListeners();
    }

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
        panel.add(usernameText, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(passwordLabel, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(passwordText, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(login, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow);
        gbc.gridwidth = 1;
        panel.add(register, gbc);

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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Sets up listener events for all buttons
     */
    public void SetupListeners() {
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    LoginHandler();
                } catch (InvalidInputException ex) {
                    ex.printStackTrace();
                }
            }
        });
        register.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    RegisterHandler();
                } catch (InvalidInputException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Login button event handler.
     * @throws InvalidInputException
     */
    public void LoginHandler() throws InvalidInputException {
        DBConnection.setUsername(usernameText.getText());
        DBConnection.setPassword(passwordText.getText());
        if (data.VerifyUser()) {
            SwingUtilities.invokeLater(new PageDatabase());
            dispose();
        } else {
            throw new InvalidInputException("Login credentials are wrong!", this);
        }
    }

    /**
     * Register button event handler.
     */
    public void RegisterHandler() throws InvalidInputException {
        DBConnection.setUsername(usernameText.getText());
        DBConnection.setPassword(passwordText.getText());
        System.out.println("1");
        if (data.AddUser()) {
            System.out.println("2");
            SwingUtilities.invokeLater(new PageDatabase());
            dispose();
        } else {
            System.out.println("3");
            throw new InvalidInputException("Failed to register account...", this);
        }
    }

    @Override
    public void run() {
        CreateGUI();
        data = new JDBCDataSource();
    }

    public static void main(String[] args) throws SQLException {
        SwingUtilities.invokeLater(new PageLogin());

        JDBCDataSource data = new JDBCDataSource();
        data.DeleteUser("Test");
        DBConnection.setUsername("Test");
        DBConnection.setPassword("1234");
        data.AddUser();
    }
}