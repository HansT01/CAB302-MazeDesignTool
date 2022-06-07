import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Constructs window for authenticating user
 */
public class PageLogin extends JFrame implements ActionListener, Runnable {
    GridBagManager gbm = new GridBagManager();

    // Labels
    JLabel nameLabel = new JLabel("Name");
    JLabel passwordLabel = new JLabel("Password");
    // Text Areas
    JTextField nameText = new JTextField();
    JTextField passwordText = new JTextField();
    // Button
    JButton login = new JButton("Login");

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
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        // Authenticate user
        if (e.getSource() == login) {
            String userTxt = nameText.getText();
            String passTxt = passwordText.getText();
            if (userTxt.equalsIgnoreCase("Riley") && passTxt.equalsIgnoreCase("1234")) {
                login.addActionListener(f -> {
                    SwingUtilities.invokeLater(new PageDatabase());
                    dispose();
                });
            }
            else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials");
            }
        }

    }

    @Override
    public void run() {
        CreateGUI();}
    public static void main(String[] args) {SwingUtilities.invokeLater(new PageLogin());} // for testing
}