import javax.swing.*;
import java.awt.*;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import java.io.File;

public class MazeDisplay extends JFrame implements Runnable{
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
        //JMenuItem loginMenu = new JMenuItem("Author");
        //menuBar.add(loginMenu);

        //Database button
        //JMenuItem dbMenu = new JMenuItem("DataBase");
        //menuBar.add(dbMenu);

        //Home button
        JMenuItem homeBtn = new JMenuItem("Home");
        menuBar.add(homeBtn);

        setJMenuBar(menuBar);

        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(null);

            }
        });

        // Open LoginUI when 'Login' is pressed
        //loginMenu.addActionListener(e -> SwingUtilities.invokeLater(new CreateDialogue()));

        // Open DataBaseUI when 'DataBase' is pressed
        //dbMenu.addActionListener(e -> SwingUtilities.invokeLater(new DataBaseUI())); // DataBaseUI is currently just a blank window

        // Goes back to home
        homeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JMenuItem homeBtn = (JMenuItem)e.getSource();
                if (homeBtn.isArmed()) {
                    preGenerateUI();
                    initializeUI();
                    System.out.println("Home Button");
                }

            }
        });

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

        add(menuPanel);


        generate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("anyone listening?");

                menuPanel.removeAll();
                JToggleButton solution = new JToggleButton("Show maze solution");
                menuPanel.add(solution);

                if (easy.isSelected()) {
                    Maze testMaze = new Maze("Title A", "Author A",12, 8);
                    testMaze.GenerateMaze();
                    MazePanel mazePanel = new MazePanel(testMaze, 100);
                    menuPanel.add(mazePanel);
                    solution.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JToggleButton tBtn = (JToggleButton)e.getSource();
                            if (tBtn.isSelected()){
                                mazePanel.toggleSolution();
                            }
                            else {
                                mazePanel.toggleSolution();
                            }
                        }
                    });
                    System.out.println("Easy");
                }
                else if (medium.isSelected()) {
                    Maze testMaze = new Maze("Title B", "Author B",24, 16);
                    testMaze.GenerateMaze();
                    MazePanel mazePanel = new MazePanel(testMaze, 50);
                    menuPanel.add(mazePanel);
                    solution.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JToggleButton tBtn = (JToggleButton)e.getSource();
                            if (tBtn.isSelected()){
                                mazePanel.toggleSolution();
                            }
                            else {
                                mazePanel.toggleSolution();
                            }
                        }
                    });
                    System.out.println("Medium");
                }
                else if (hard.isSelected()) {
                    Maze testMaze = new Maze("Title C", "Author C",48, 32);
                    testMaze.GenerateMaze();
                    MazePanel mazePanel = new MazePanel(testMaze, 25);
                    menuPanel.add(mazePanel);
                    solution.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JToggleButton tBtn = (JToggleButton)e.getSource();
                            if (tBtn.isSelected()){
                                mazePanel.toggleSolution();
                            }
                            else {
                                mazePanel.toggleSolution();
                            }
                        }
                    });
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setPreferredSize(new Dimension(1400, 900)); // desired size of window
        // Compile and place window on screen
        menuBar();
        pack();
        setLocationRelativeTo(null);


        // Opens window at mouse pointer
        Point open_location = MouseInfo.getPointerInfo().getLocation();
        int open_x = (int) open_location.getX();
        int open_y = (int) open_location.getY();


    }
    private void postGenerationUI() {
        menuPanel.removeAll();
        // Show solved version button

    }

    private void createGUI() {

        initializeUI();
        preGenerateUI();
        setVisible(true);
        // Automatically centres content to frame

        setLayout(new GridBagLayout());

        // Resizes window to preferred dimensions
        pack();
        // Centre to screen
        setLocationRelativeTo(null);
    }

    public void run() {createGUI();}

    public static void main(String[] args) {SwingUtilities.invokeLater(new MazeDisplay());}

}
