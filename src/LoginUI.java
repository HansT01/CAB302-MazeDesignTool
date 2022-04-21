import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame implements Runnable{

    // Values used for default screen size
    private static final int width = 500;
    private static final int height = 300;

    // Used for opening application at location of mouse pointer on screen
    Point open_location = MouseInfo.getPointerInfo().getLocation();
    int open_x = (int) open_location.getX();
    int open_y = (int) open_location.getY();

    private void createGUI () {
        // Setting up main window
        setLocation(open_x, open_y); // Open window at location of mouse pointer
        setVisible(true);
        setTitle("Login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set so pressing x closes window
        setSize(width, height);
        setLayout(new BorderLayout());

        // Labels
        JLabel nameLabel = new JLabel("Name");
        JLabel passwordLabel = new JLabel("Password");
        // Text Areas
        JTextField nameText = new JTextField();
        JTextField passwordText = new JTextField();
        // Button
        JButton login = new JButton(); login.setText("Login");
        login.addActionListener(e -> SwingUtilities.invokeLater(new DataBaseUI())); // Placeholder; does not go to right spot

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
    public static void main(String[] args) {SwingUtilities.invokeLater(new LoginUI());} // for testing

}
