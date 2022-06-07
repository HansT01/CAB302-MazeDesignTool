import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Constructs window for authenticating user
 */
public class LoginUI extends JFrame implements ActionListener, Runnable {

    // Used for opening application at location of mouse pointer on screen
    Point openLocation = MouseInfo.getPointerInfo().getLocation();
    // Labels
    JLabel nameLabel = new JLabel("Name");
    JLabel passwordLabel = new JLabel("Password");
    // Text Areas
    JTextField nameText = new JTextField();
    JTextField passwordText = new JTextField();
    // Button
    JButton login = new JButton("Login");

    public void createGUI () {
        // Setting up main window
        setLocation(openLocation); // Open window at location of mouse pointer
        setVisible(true);
        setTitle("Login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set so pressing x closes window
        setSize(500, 300);
        setLayout(new BorderLayout());
        // Centre to screen
        setLocationRelativeTo(null);

        // Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5,1));
        panel.setBorder(new EmptyBorder(10,10,10,10));
        add(panel, BorderLayout.CENTER);

        // Packing
        panel.add(nameLabel);
        panel.add(nameText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(login);

        // Login Button Action
        login.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Authenticate user
        if (e.getSource() == login) {
            String userTxt;
            String passTxt;
            userTxt = nameText.getText();
            passTxt = passwordText.getText();
            if (userTxt.equalsIgnoreCase("Riley") && passTxt.equalsIgnoreCase("1234")) {
                login.addActionListener(f -> {
                    SwingUtilities.invokeLater(new DataBaseUI());
                    dispose();
                });
            }
            else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials");
            }
        }

    }

    @Override
    public void run() {createGUI();}
    public static void main(String[] args) {SwingUtilities.invokeLater(new LoginUI());} // for testing
}