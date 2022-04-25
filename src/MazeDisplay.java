import javax.swing.*;
import java.awt.*;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MazeDisplay {

    JFrame window = new JFrame("Maze Generator");
    JPanel menuPanel = new JPanel();
    boolean userStatus = false;

    private void menuBar() {
        // Setting up the menu bar
        JMenuBar menuBar = new JMenuBar();

        // Exporting buttons
        JMenu exportMenu = new JMenu("Export to image file");
        exportMenu.add("With Solution");
        exportMenu.add("Without Solution");
        menuBar.add(exportMenu);

        // Open Button
        JMenuItem openButton = new JMenuItem("Open File");
        menuBar.add(openButton);

        // Login Button
        JMenuItem loginMenu = new JMenuItem("Login");
        menuBar.add(loginMenu);

        //Database button
        JMenuItem dbMenu = new JMenuItem("DataBase");
        menuBar.add(dbMenu);

        window.setJMenuBar(menuBar);

        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!userStatus) {
                    System.out.println("pls login");
                    SwingUtilities.invokeLater(new LoginUI()); // Open LoginUI when 'Open File' is pressed and userStatus if false

//                    JFrame frame = new JFrame("JOptionPane showMessageDialog component example");
//                    JOptionPane.showMessageDialog(frame, window);
                }
                else {

                }
            }
        });

        // Open LoginUI when 'Login' is pressed
        loginMenu.addActionListener(e -> SwingUtilities.invokeLater(new LoginUI()));

        // Open DataBaseUI when 'DataBase' is pressed
        dbMenu.addActionListener(e -> SwingUtilities.invokeLater(new DataBaseUI())); // DataBaseUI is currently just a blank window
    }

    private void initializeUI() {
        // Creating and displaying the program
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setPreferredSize(new Dimension(400, 400)); // desired size of window
        // Compile and place window on screen
        menuBar();
        window.pack();
        window.setLocationRelativeTo(null);

        // Opens window at mouse pointer
        Point open_location = MouseInfo.getPointerInfo().getLocation();
        int open_x = (int) open_location.getX();
        int open_y = (int) open_location.getY();
        window.setLocation(open_x, open_y); // Open window at location of mouse pointer
    }

    private void setVisible(boolean b) {
    }


    private void preGenerateUI() {
        menuPanel.removeAll();
        // Generate Button
        Button generate = new Button("Generate!");

        generate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("anyone listening?");
                postGenerationUI();
                initializeUI();
            }
        });

        // Radio buttons for choosing difficulty
        Label difficultyLabel = new Label("Choose Maze Difficulty:");

        ButtonGroup difficultyGroup = new ButtonGroup();
        JRadioButton easy = new JRadioButton("Easy");
        JRadioButton medium = new JRadioButton("Medium");
        JRadioButton hard = new JRadioButton("Hard");
//        easy.setBounds(275,25,100,30);
//        medium.setBounds(275,50,100,30);
//        hard.setBounds(275,1175,100,30);

//        dbar = new JLabel("Selected: ");

//        difficultyPanel.setLayout(new BoxLayout(difficultyPanel, BoxLayout.Y_AXIS));
//        difficultyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        difficultyGroup.add(easy);
        difficultyGroup.add(medium);
        difficultyGroup.add(hard);

        menuPanel.add(difficultyLabel);
        menuPanel.add(easy);
        menuPanel.add(medium);
        menuPanel.add(hard);
        menuPanel.add(generate);

        window.add(menuPanel);
    }

    private void postGenerationUI() {
        menuPanel.removeAll();
        // Show solved version button
        Button solution = new Button("Show maze solution");

        menuPanel.add(solution);
    }

    public static void main(String args[]) {

        EventQueue.invokeLater(() -> {
            MazeDisplay UI = new MazeDisplay();
            UI.initializeUI();
            UI.preGenerateUI();
            UI.setVisible(true);
        });
    }


}
