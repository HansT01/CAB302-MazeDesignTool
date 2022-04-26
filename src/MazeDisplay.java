import javax.swing.*;
import java.awt.*;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MazeDisplay {

    static JFrame window = new JFrame("Maze Generator");

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
        JMenuItem loginMenu = new JMenuItem("Author");
        menuBar.add(loginMenu);

        //Database button
        JMenuItem dbMenu = new JMenuItem("DataBase");
        menuBar.add(dbMenu);

        //Back button
        JMenuItem backBtn = new JMenuItem("Back");
        menuBar.add(backBtn);

        window.setJMenuBar(menuBar);

        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!userStatus) {
                    System.out.println("pls login");
                    SwingUtilities.invokeLater(new CreateDialogue()); // Open LoginUI when 'Open File' is pressed and userStatus if false

//                    JFrame frame = new JFrame("JOptionPane showMessageDialog component example");
//                    JOptionPane.showMessageDialog(frame, window);
                }
                else {

                }
            }
        });

        // Open LoginUI when 'Login' is pressed
        loginMenu.addActionListener(e -> SwingUtilities.invokeLater(new CreateDialogue()));

        // Open DataBaseUI when 'DataBase' is pressed
        dbMenu.addActionListener(e -> SwingUtilities.invokeLater(new DataBaseUI())); // DataBaseUI is currently just a blank window

        // Goes back to home (work in progress)
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JMenuItem backBtn = (JMenuItem)e.getSource();
                if (backBtn.isArmed()) {
                    System.out.println("Back");
                    preGenerateUI();
                    MazeDisplay UI = new MazeDisplay();
                    UI.initializeUI();
                }

            }
        });

    }


    private void setVisible(boolean b) {
    }


    private void preGenerateUI() {
        menuPanel.removeAll();
        // Generate Button
        Button generate = new Button("Generate!");



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


        generate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("anyone listening?");


                postGenerationUI();
                if (easy.isSelected()) {
                    Maze testMaze = new Maze(12, 8);
                    testMaze.GenerateMaze();
                    MazePanel mazePanel = new MazePanel(testMaze, 100);
                    menuPanel.add(mazePanel);
                    System.out.println("Easy");
                }
                else if (medium.isSelected()) {
                    Maze testMaze = new Maze(24, 16);
                    testMaze.GenerateMaze();
                    MazePanel mazePanel = new MazePanel(testMaze, 50);
                    menuPanel.add(mazePanel);
                    System.out.println("Medium");

                }
                else if (hard.isSelected()) {
                    Maze testMaze = new Maze(48, 32);
                    testMaze.GenerateMaze();
                    MazePanel mazePanel = new MazePanel(testMaze, 25);

                    menuPanel.add(mazePanel);
                    System.out.println("Hard");
                }
                else {
                    preGenerateUI();
                }
                initializeUI();
            }
        });
    }


    private void initializeUI() {
        // Creating and displaying the program
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setPreferredSize(new Dimension(1400, 900)); // desired size of window
        // Compile and place window on screen
        menuBar();
        window.pack();
        window.setLocationRelativeTo(null);


        // Opens window at mouse pointer
        Point open_location = MouseInfo.getPointerInfo().getLocation();
        int open_x = (int) open_location.getX();
        int open_y = (int) open_location.getY();


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
            // Automatically centres content to frame

            window.setLayout(new GridBagLayout());

            // Resizes window to preferred dimensions
            window.pack();
            // Centre to screen
            window.setLocationRelativeTo(null);

            // Set defaults
            window.setVisible(true);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }


}
