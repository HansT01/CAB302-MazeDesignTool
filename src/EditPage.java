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

public class EditPage extends JFrame implements Runnable {
    private final MazePanel mazePanel;
    private JToggleButton toggleSolution = new JToggleButton("Enable maze solution", true);
    private JToggleButton toggleRandomizeImages = new JToggleButton("Enable randomize images");
    private JButton importImage = new JButton("Import image");
    private JButton deleteImage = new JButton("Delete image");
    private JToggleButton placeImage = new JToggleButton("Place image");
    private JButton clearImages = new JButton("Clear images");
    private JButton generateMaze = new JButton("Generate maze");
    private JTextField imageWidth = new JTextField("1", 10);
    private JTextField imageHeight = new JTextField("1", 10);
    private JLabel solutionPct = new JLabel("0.00%", SwingConstants.LEFT);
    private JLabel deadEndsPct = new JLabel("0.00%", SwingConstants.LEFT);
    private JButton saveMaze = new JButton("Save maze");
    private JButton restoreMaze = new JButton("Restore maze");

    private byte[] saveState;

    private JTable imagesTable = new JTable(new DefaultTableModel(new String[][] {}, new String[] {"File name", "Width", "Height"})) {
        // make rows uneditable
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JPanel optionsPanel;

    final JFileChooser fc = new JFileChooser();

    private int innerPaddingSize = 5;
    private int outerPaddingSize = 20;

    /**
     * Constructs the edit page
     * @param maze maze object
     * @param cellSize size of each cell
     */
    public EditPage(Maze maze, int cellSize) throws IOException {
        fc.setFileFilter(new FileNameExtensionFilter("Image files", "jpeg", "jpg", "png", "gif"));
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));

        mazePanel = new MazePanel(maze, cellSize);
        saveState = Maze.MazeToByteArray(maze);
        UpdateTable();


        UpdateSolutionMetrics();

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
                mazePanel.setPlacingImage(!placeImage.isSelected());
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
                UpdateSolutionMetrics();
            }
        });

        toggleSolution.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mazePanel.setDrawSolution(!toggleSolution.isSelected());
                mazePanel.repaint();
            }
        });

        mazePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                UpdateSolutionMetrics();
            }
        });

        saveMaze.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    SaveMaze();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        restoreMaze.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    RestoreMaze();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Updates the labels for the maze metrics
     */
    private void UpdateSolutionMetrics() {
        Maze maze = mazePanel.getMaze();
        solutionPct.setText(String.format("%.2f%%%n", maze.SolutionPct(maze.Solve())*100));
        deadEndsPct.setText(String.format("%.2f%%%n", maze.DeadEndPct()*100));
    }

    /**
     * Regenerates maze by clearing and optionally placing images randomly
     */
    private void RegenerateMaze() {
        Maze maze = mazePanel.getMaze();
        maze.ClearMaze();
        if (toggleRandomizeImages.isSelected()) {
            maze.PlaceImagesRandom(50);
        }
        maze.GenerateMaze();
        maze.Solve();
        mazePanel.repaint();
    }

    private void ClearImages() {
        ArrayList<MazeImage> imagesList = mazePanel.getMaze().getImages();
        for (MazeImage image : imagesList) {
            mazePanel.getMaze().RemoveImage(image);
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
     * Updates maze saveState and database row
     * @throws IOException
     */
    private void SaveMaze() throws IOException {
        saveState = Maze.MazeToByteArray(mazePanel.getMaze());
        // update database row
    }

    /**
     * Restores maze object to save state
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void RestoreMaze() throws IOException, ClassNotFoundException {
        mazePanel.setMaze(Maze.ByteArrayToMaze(saveState));
        mazePanel.repaint();
        UpdateTable();
        UpdateSolutionMetrics();
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

        gbc.insets = new Insets((y==0) ? outerPaddingSize : 0, (x==0) ? outerPaddingSize : 0, outerPaddingSize, outerPaddingSize);
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

        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets((y!=0) ? innerPaddingSize : 0, (x!=0) ? innerPaddingSize : 0, 0, 0);
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

    private JPanel CreateImagesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Maze images editor"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );

        JScrollPane scrollPane = new JScrollPane(imagesTable);
        scrollPane.setPreferredSize(new Dimension(250, 100));

        GridBagConstraints gbc;
        int gridRow = 0;

        Color clr = new Color(255, 255, 255, 40);

        // Import image button
        gbc = CreateInnerGBC(0, gridRow);
        gbc.gridwidth = 1;
        panel.add(importImage, gbc);

        // Delete image button
        gbc = CreateInnerGBC(1, gridRow++);
        gbc.gridwidth = 1;
        panel.add(deleteImage, gbc);

        // Imported images table
        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        panel.add(scrollPane, gbc);

        // Place image buttons and options
        gbc = CreateInnerGBC(0, gridRow);
        panel.add(new Label("Image width:", Label.RIGHT), gbc);
        gbc = CreateInnerGBC(1, gridRow++);
        panel.add(imageWidth, gbc);
        gbc = CreateInnerGBC(0, gridRow);
        panel.add(new Label("Image height:", Label.RIGHT), gbc);
        gbc = CreateInnerGBC(1, gridRow++);
        panel.add(imageHeight, gbc);
        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        panel.add(placeImage, gbc);

        // Clear all images button
        gbc = CreateInnerGBC(0, gridRow);
        gbc.gridwidth = 2;
        panel.add(clearImages, gbc);

        return panel;
    }

    private JPanel CreateGeneratePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Maze options"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );

        GridBagConstraints gbc;
        int gridRow = 0;

        // Generate maze button
        gbc = CreateInnerGBC(0, gridRow++);
        panel.add(generateMaze, gbc);

        // Toggle randomize images
        gbc = CreateInnerGBC(0, gridRow);
        panel.add(toggleRandomizeImages, gbc);

        return panel;
    }

    private JPanel CreateSolutionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Solution options"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );

        GridBagConstraints gbc;
        int gridRow = 0;

        // toggle solution
        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        panel.add(toggleSolution, gbc);

        // solution percentage
        gbc = CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("Solution percentage: " , SwingConstants.LEFT), gbc);
        gbc = CreateInnerGBC(1, gridRow++);
        panel.add(solutionPct, gbc);

        // solution percentage
        gbc = CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("Dead ends: ", SwingConstants.LEFT), gbc);
        gbc = CreateInnerGBC(1, gridRow);
        panel.add(deadEndsPct, gbc);

        return panel;
    }

    private JPanel CreateSavePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Save options"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );

        GridBagConstraints gbc;
        int gridRow = 0;

        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(saveMaze, gbc);

        gbc = CreateInnerGBC(0, gridRow);
        gbc.gridwidth = 1;
        panel.add(restoreMaze, gbc);

        return panel;
    }

    private JPanel CreateOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc;
        int gridRow = 0;

        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(CreateImagesPanel(), gbc);

        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(CreateGeneratePanel(), gbc);

        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(CreateSolutionPanel(), gbc);

        gbc = CreateInnerGBC(0, gridRow);
        gbc.gridwidth = 1;
        panel.add(CreateSavePanel(), gbc);

        return panel;
    }

    public void CreateGUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        int gridRow = 0;

        // maze panel
        gbc = CreateOuterGBC(0, gridRow);
        gbc.anchor = GridBagConstraints.EAST;
        add(mazePanel, gbc);

        // options panel
        gbc = CreateOuterGBC(1, gridRow);
        gbc.anchor = GridBagConstraints.WEST;
        add(CreateOptionsPanel(), gbc);

        // resizes window to preferred dimensions
        pack();

        // centre to screen
        setLocationRelativeTo(null);

        // set defaults
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
    public static void main(String[] args) throws IOException {
        // Generate maze
        Maze testMaze = new Maze("Maze Title", "Maze Author", 80,80);
        testMaze.GenerateMaze();

        // Create page with panel
        EditPage testPage = new EditPage(testMaze, 8);

        // No idea what runnable is for, but here it is!
        SwingUtilities.invokeLater(testPage);
    }
}
