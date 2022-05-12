import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
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
    private JButton importImage = new JButton("Import image");
    private JButton deleteImage = new JButton("Delete image");
    private JToggleButton placeImage = new JToggleButton("Place image");
    private JButton clearImages = new JButton("Clear images");
    private JButton generateMaze = new JButton("Generate maze");
    private JTextField imageWidth = new JTextField("1", 10);
    private JTextField imageHeight = new JTextField("1", 10);

    private JTable imagesTable = new JTable(new DefaultTableModel(new String[][] {}, new String[] {"File name", "Width", "Height"})) {
        // make rows uneditable
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

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
                ImportImage();
            }
        });

        deleteImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mazePanel.getMaze().getImages().remove(GetSelectedImage());
                mazePanel.repaint();
                UpdateTable();
            }
        });

        placeImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mazePanel.setPlacingImage(!placeImage.getModel().isSelected());
            }
        });

        clearImages.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ClearImages();
            }
        });

        // event listener for row selection
        imagesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                mazePanel.getMaze().setSelectedImage(GetSelectedImage());
            }
        });

        generateMaze.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                RegenerateMaze();
            }
        });
    }

    private void RegenerateMaze() {
        Maze maze = mazePanel.getMaze();
        maze.ClearMaze();
        maze.PlaceImagesRandom(50);
        maze.GenerateMaze();
        mazePanel.repaint();
    }

    private void ClearImages() {
        ArrayList<MazeImage> imagesList = mazePanel.getMaze().getImages();
        for (MazeImage image : imagesList) {
            image.setPlaced(false);
        }
        mazePanel.repaint();
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

    /**
     * Gets the current table model and updates the data
     */
    public void UpdateTable() {
        // get current model
        DefaultTableModel tm = (DefaultTableModel) imagesTable.getModel();
        tm.setRowCount(0);

        // create new data array
        ArrayList<MazeImage> imageList = mazePanel.getMaze().getImages();
        for (int i = 0; i < imageList.size(); i++) {
            String[] data = new String[3];
            MazeImage image = imageList.get(i);
            data[0] = image.getImageTitle();
            data[1] = String.format("%s", image.getSizeX());
            data[2] = String.format("%s", image.getSizeY());
            System.out.println(Arrays.deepToString(data));
            tm.addRow(data);
        }

        // update table model
        imagesTable.setModel(tm);
        tm.fireTableDataChanged();
    }

    /**
     * Gets the image of the first selected row
     * @return Image of the first selected row
     */
    public MazeImage GetSelectedImage() {
        int selectedRow = imagesTable.getSelectedRow();
        if (selectedRow != -1) {
            return mazePanel.getMaze().getImages().get(selectedRow);
        }
        return null;
    }

    public void CreateGUI() {
        JScrollPane scrollPane = new JScrollPane(imagesTable);
        scrollPane.setPreferredSize(new Dimension(250, 100));

        // Options panel
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridBagLayout());

        int gridRow = 0;

        // Import image button
        gbc = CreateInnerGBC(0, gridRow);
        gbc.gridwidth = 1;
        optionsPanel.add(importImage, gbc);

        // Delete image button
        gbc = CreateInnerGBC(1, gridRow++);
        gbc.gridwidth = 1;
        optionsPanel.add(deleteImage, gbc);

        // Imported images table
        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        optionsPanel.add(scrollPane, gbc);

        // Place image buttons and options
        gbc = CreateInnerGBC(0, gridRow);
        optionsPanel.add(new Label("Image width:", 2), gbc);
        gbc = CreateInnerGBC(1, gridRow++);
        optionsPanel.add(imageWidth, gbc);
        gbc = CreateInnerGBC(0, gridRow);
        optionsPanel.add(new Label("Image height:", 2), gbc);
        gbc = CreateInnerGBC(1, gridRow++);
        optionsPanel.add(imageHeight, gbc);
        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        optionsPanel.add(placeImage, gbc);

        // Clear all images button
        // This button will set all images to not isPlaced
        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        optionsPanel.add(clearImages, gbc);

        // Generate maze button
        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        optionsPanel.add(generateMaze, gbc);

        // Toggle solution
        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        optionsPanel.add(toggleSolution, gbc);

        // Toggle randomize images
        gbc = CreateInnerGBC(0, gridRow++);
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
        Maze testMaze = new Maze("Maze Title", "Maze Author", 12,12);
        testMaze.GenerateMaze();

        // Create panel with maze
        MazePanel testPanel = new MazePanel(testMaze, 32);

        // Create page with panel
        EditPage testPage = new EditPage(testPanel);

        // No idea what runnable is for, but here it is!
        SwingUtilities.invokeLater(testPage);
    }
}
