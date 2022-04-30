import javax.swing.*;
import java.awt.*;

public class EditPage extends JFrame implements Runnable{
    private final MazePanel mazePanel;
    private JToggleButton toggleSolution = new JToggleButton("Enable maze solution");
    private JToggleButton toggleRandomizeImages = new JToggleButton("Enable randomize images");
    private JToggleButton placeImage = new JToggleButton("Place image");
    private JButton importImage = new JButton("Import new image");
    private JButton clearImages = new JButton("Clear images");
    private JButton generateMaze = new JButton("Generate maze");
    private JTextField imagePxWidth = new JTextField(10);
    private JTextField imagePxHeight = new JTextField(10);
    private JTable imagesTable;
    private GridBagConstraints gbc;
    private JPanel optionsPanel;

    private int paddingSize = 10;

    /**
     * Constructs the edit page with the maze panel
     * @param mazePanel Maze panel with maze object
     */
    public EditPage(MazePanel mazePanel) {
        this.mazePanel = mazePanel;
    }

    /**
     * Creates a GridBagConstraints object with 1 grid width and height at x and y.
     * Will generate automatic insets for padding.
     * @param x x location of the grid bag layout
     * @param y y location of the grid bag layout
     * @return GridBagConstraints object
     */
    private GridBagConstraints CreateGBC(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        gbc.insets = new Insets((y==0) ? paddingSize: 0, (x==0) ? paddingSize: 0, paddingSize, paddingSize);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        return gbc;
    }

    public void CreateGUI() {

        // JTable config
        // TODO move to its own method
        String[] columnNames = {"File name"};
        String[][] rowData = {
                {"logo.png"},
                {"start-icon.png"},
                {"end-icon.png"}
        };

        imagesTable = new JTable(rowData, columnNames);
        JScrollPane scrollPane = new JScrollPane(imagesTable);
        scrollPane.setPreferredSize(new Dimension(250, 100));

        // Options panel
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridBagLayout());

        // Import image button
        gbc = CreateGBC(0, 0);
        gbc.gridwidth = 2;
        optionsPanel.add(importImage, gbc);

        // Imported images table
        gbc = CreateGBC(0, 1);
        gbc.gridwidth = 2;
        optionsPanel.add(scrollPane, gbc);

        // Image place button and options
        gbc = CreateGBC(0, 2);
        optionsPanel.add(new Label("Image width:", 2), gbc);
        gbc = CreateGBC(1, 2);
        optionsPanel.add(imagePxWidth, gbc);
        gbc = CreateGBC(0, 3);
        optionsPanel.add(new Label("Image height:", 2), gbc);
        gbc = CreateGBC(1, 3);
        optionsPanel.add(imagePxHeight, gbc);
        gbc = CreateGBC(0, 4);
        gbc.gridwidth = 2;
        optionsPanel.add(placeImage, gbc);

        // Remove all images button
        gbc = CreateGBC(0, 5);
        gbc.gridwidth = 2;
        optionsPanel.add(clearImages, gbc);

        // Generate maze button
        gbc = CreateGBC(0, 6);
        gbc.gridwidth = 2;
        optionsPanel.add(generateMaze, gbc);

        // Toggle solution
        gbc = CreateGBC(0, 7);
        gbc.gridwidth = 2;
        optionsPanel.add(toggleSolution, gbc);

        // Toggle randomize images
        gbc = CreateGBC(0, 8);
        gbc.gridwidth = 2;
        optionsPanel.add(toggleRandomizeImages, gbc);

        // Main panel
        setLayout(new GridBagLayout());
        gbc = CreateGBC(0,0);
        gbc.gridheight = 2;
        add(mazePanel, gbc);
        gbc = CreateGBC(1, 0);
        add(optionsPanel, gbc);

        // Resizes window to preferred dimensions
        pack();

        // Centre to screen
        setLocationRelativeTo(null);

        // Set defaults
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void run() {
        CreateGUI();
    }

    /**
     * Main test method for testing this edit page
     */
    public static void main(String[] args) {
        // Generate maze
        Maze testMaze = new Maze("Maze Title", "Maze Author", 80,50);
        testMaze.GenerateMaze();

        // Create panel with maze
        MazePanel testPanel = new MazePanel(testMaze, 12);

        // Create page with panel
        EditPage testPage = new EditPage(testPanel);

        // No idea what runnable is for, but here it is!
        SwingUtilities.invokeLater(testPage);
    }
}
