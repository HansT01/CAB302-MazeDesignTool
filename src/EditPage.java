import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class EditPage extends JFrame implements Runnable{
    private final MazePanel mazePanel;
    private JToggleButton toggleSolution = new JToggleButton("Enable maze solution");
    private JToggleButton toggleRandomizeImages = new JToggleButton("Enable randomize images");
    private JToggleButton placeImage = new JToggleButton("Place image");
    private JButton importImage = new JButton("Import new image");
    private JButton clearImages = new JButton("Clear images");
    private JButton generateMaze = new JButton("Generate maze");
    private JTextField imageWidth = new JTextField(10);
    private JTextField imageHeight = new JTextField(10);

    private JTable imagesTable = new JTable(new DefaultTableModel());

    private GridBagConstraints gbc;
    private JPanel optionsPanel;

    final JFileChooser fc = new JFileChooser();

    private int paddingSize = 5;

    /**
     * Constructs the edit page with the maze panel
     * @param mazePanel Maze panel with maze object
     */
    public EditPage(MazePanel mazePanel) {
        this.mazePanel = mazePanel;
        UpdateTable();

        fc.setFileFilter(new FileNameExtensionFilter("Static image files", "jpeg", "jpg", "png"));
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));

        importImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Import image button clicked");
                ImportImage();
            }
        });

        placeImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                boolean selected = !placeImage.getModel().isSelected();
                mazePanel.setPlacingImage(selected);
            }
        });
    }

    private void ImportImage() {
        int returnVal = fc.showOpenDialog(this);

        // https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                System.out.println("Opening file " + file.getName());
                BufferedImage imageData = ImageIO.read(file);
                int width = Integer.parseInt(imageWidth.getText());
                int height = Integer.parseInt(imageHeight.getText());
                mazePanel.getMaze().getImages().add(new MazeImage(file.getName(), imageData, width, height));
                UpdateTable();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {
            System.out.println("Open command cancelled by user");
        }
    }

    /**
     * Creates a GridBagConstraints object for objects on the main frame
     * @param x x location of the grid bag layout
     * @param y y location of the grid bag layout
     * @return GridBagConstraints object
     */
    private GridBagConstraints CreateOuterGBC(int x, int y) {
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

    /**
     * Creates a GridBagConstraints object for objects inside a panel
     * @param x x location of the grid bag layout
     * @param y y location of the grid bag layout
     * @return GridBagConstraints object
     */
    private GridBagConstraints CreateInnerGBC(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        gbc.insets = new Insets((y!=0) ? paddingSize: 0, (x!=0) ? paddingSize: 0, 0, 0);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        return gbc;
    }

    public void UpdateTable() {
        ArrayList<MazeImage> imageList = mazePanel.getMaze().getImages();
        String[][] imageNames = new String[imageList.size()][1];
        for (int i = 0; i < imageList.size(); i++) {
            MazeImage image = imageList.get(i);
            imageNames[i][0] = image.getImageTitle();
        }
        System.out.println(Arrays.deepToString(imageNames));
        // TODO currently creating a new model everytime method is invoked
        imagesTable.setModel(new DefaultTableModel(imageNames, new String[] {"File name"}));
    }



    public void CreateGUI() {
        JScrollPane scrollPane = new JScrollPane(imagesTable);
        scrollPane.setPreferredSize(new Dimension(250, 100));

        // Options panel
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridBagLayout());

        // Import image button
        gbc = CreateInnerGBC(0, 0);
        gbc.gridwidth = 2;
        optionsPanel.add(importImage, gbc);

        // Imported images table
        gbc = CreateInnerGBC(0, 1);
        gbc.gridwidth = 2;
        optionsPanel.add(scrollPane, gbc);

        // Image place button and options
        gbc = CreateInnerGBC(0, 2);
        optionsPanel.add(new Label("Image width:", 2), gbc);
        gbc = CreateInnerGBC(1, 2);
        optionsPanel.add(imageWidth, gbc);
        gbc = CreateInnerGBC(0, 3);
        optionsPanel.add(new Label("Image height:", 2), gbc);
        gbc = CreateInnerGBC(1, 3);
        optionsPanel.add(imageHeight, gbc);
        gbc = CreateInnerGBC(0, 4);
        gbc.gridwidth = 2;
        optionsPanel.add(placeImage, gbc);

        // Remove all images button
        gbc = CreateInnerGBC(0, 5);
        gbc.gridwidth = 2;
        optionsPanel.add(clearImages, gbc);

        // Generate maze button
        gbc = CreateInnerGBC(0, 6);
        gbc.gridwidth = 2;
        optionsPanel.add(generateMaze, gbc);

        // Toggle solution
        gbc = CreateInnerGBC(0, 7);
        gbc.gridwidth = 2;
        optionsPanel.add(toggleSolution, gbc);

        // Toggle randomize images
        gbc = CreateInnerGBC(0, 8);
        gbc.gridwidth = 2;
        optionsPanel.add(toggleRandomizeImages, gbc);

        // Main panel
        setLayout(new GridBagLayout());
        gbc = CreateOuterGBC(0,0);
        gbc.gridheight = 2;
        add(mazePanel, gbc);
        gbc = CreateOuterGBC(1, 0);
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
        Maze testMaze = new Maze("Maze Title", "Maze Author", 5,5);
        testMaze.GenerateMaze();

        // Create panel with maze
        MazePanel testPanel = new MazePanel(testMaze, 64);

        // Create page with panel
        EditPage testPage = new EditPage(testPanel);

        // No idea what runnable is for, but here it is!
        SwingUtilities.invokeLater(testPage);
    }
}
