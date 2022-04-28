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
public class LoginUI extends JFrame implements Runnable{

    // Used for opening application at location of mouse pointer on screen
    Point openLocation = MouseInfo.getPointerInfo().getLocation();

    private void createGUI () {
        // Setting up main window
        setLocation(openLocation); // Open window at location of mouse pointer
        setVisible(true);
        setTitle("Login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set so pressing x closes window
        setSize(500, 300);
        setLayout(new BorderLayout());
        // Centre to screen
        setLocationRelativeTo(null);
        
        // Labels
        JLabel nameLabel = new JLabel("Name");
        JLabel passwordLabel = new JLabel("Password");
        // Text Areas
        JTextField nameText = new JTextField();
        JTextField passwordText = new JTextField();
        // Button
        JButton login = new JButton(); login.setText("Login");
        login.addActionListener(e -> {
            SwingUtilities.invokeLater(new DataBaseUI());
            dispose();
        });

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

    }

    @Override
    public void run() {createGUI();}


}