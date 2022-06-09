package Pages;

import Database.JDBCDataSource;
import Maze.Maze;
import Maze.MazeException;
import Maze.MazeImage;
import Maze.MazePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PageEdit extends JFrame implements Runnable {
    private final MazePanel mazePanel;
    private final GridBagManager gbm = new GridBagManager();
    private final JToggleButton toggleSolution = new JToggleButton("Enable maze solution", true);
    private final JToggleButton toggleRandomizeImages = new JToggleButton("Enable randomize images");
    private final JButton importImage = new JButton("Import image");
    private final JButton deleteImage = new JButton("Delete image");
    private final JToggleButton placeImage = new JToggleButton("Place image");
    private final JButton clearImages = new JButton("Clear images");
    private final JButton generateMaze = new JButton("Generate maze");
    private final JTextField imageWidth = new JTextField("1", 10);
    private final JTextField imageHeight = new JTextField("1", 10);
    private final JLabel solutionPct = new JLabel("0.00%", SwingConstants.LEFT);
    private final JLabel deadEndsPct = new JLabel("0.00%", SwingConstants.LEFT);
    private final JButton saveMaze = new JButton("Save maze");
    private final JButton restoreMaze = new JButton("Restore maze");
    private final JButton exportMaze = new JButton("Export to PNG");

    private byte[] saveState;
    private int mazeID;

    private final JTable imagesTable = new JTable(new DefaultTableModel(new String[][] {}, new String[] {"File name", "Width", "Height"})) {
        // make rows uneditable
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    final JFileChooser fc = new JFileChooser();

    /**
     * Constructs the edit page
     * @param maze maze object
     */
    public PageEdit(Maze maze, int id) throws IOException {
        fc.setFileFilter(new FileNameExtensionFilter("Image Files", "jpeg", "jpg", "png", "gif"));
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));

        mazePanel = new MazePanel(maze);
        saveState = Maze.MazeToByteArray(maze);
        mazeID = id;

        UpdateTable();
        MazeUpdatedEvent();
        SetupListenerEvents();
    }

    /**
     * Constructs the edit page
     * @param maze maze object
     */
    public PageEdit(Maze maze) throws IOException {
        fc.setFileFilter(new FileNameExtensionFilter("Image Files", "jpeg", "jpg", "png", "gif"));
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));

        mazePanel = new MazePanel(maze);
        saveState = Maze.MazeToByteArray(maze);
        mazeID = -1;

        UpdateTable();
        MazeUpdatedEvent();
        SetupListenerEvents();
    }

    private void SetupListenerEvents() {
        importImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    ImportImage();
                } catch (InvalidInputException ex) {
                    ex.printStackTrace();
                }
            }
        });

        deleteImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Maze maze = mazePanel.getMaze();
                MazeImage mazeImage = GetSelectedImage();
                if (mazeImage == null) try {
                    throw new InvalidInputException("Please select an image", PageEdit.this);
                } catch (InvalidInputException ex) {
                    ex.printStackTrace();
                }
                else {
                    maze.RemoveImage(mazeImage);
                    maze.getImages().remove(mazeImage);
                    UpdateTable();
                    MazeUpdatedEvent();
                }
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
        imagesTable.getSelectionModel().addListSelectionListener(e -> mazePanel.getMaze().setSelectedImage(GetSelectedImage()));

        generateMaze.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    RegenerateMaze();
                } catch (MazeException | InvalidInputException ex) {
                    ex.printStackTrace();
                }
            }
        });

        toggleSolution.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mazePanel.setDrawSolution(!toggleSolution.isSelected());
                MazeUpdatedEvent();
            }
        });

        mazePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MazeUpdatedEvent();
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
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        exportMaze.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    ExportToPNG();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Updates the labels for the maze metrics and repaints the maze.
     */
    private void MazeUpdatedEvent() {
        Maze maze = mazePanel.getMaze();
        solutionPct.setText(String.format("%.2f%%%n", maze.SolutionPct()*100));
        deadEndsPct.setText(String.format("%.2f%%%n", maze.DeadEndPct()*100));
        mazePanel.repaint();
    }

    /**
     * Regenerates maze by clearing and optionally placing images randomly
     * @throws MazeException if PlaceImage method fails to place image
     * @throws InvalidInputException if PlaceImagesRandom runs out of iterations
     */
    private void RegenerateMaze() throws MazeException, InvalidInputException {
        Maze maze = mazePanel.getMaze();
        if (toggleRandomizeImages.isSelected()) {
            if (!maze.PlaceImagesRandom(50)) {
                throw new InvalidInputException("Ran out of iterations, unable to generate maze with given images");
            }
        }
        else {
            for (MazeImage mazeImage : maze.getImages()) {
                if (mazeImage.isPlaced()) {
                    maze.PlaceImage(mazeImage.getX(), mazeImage.getY(), mazeImage);
                }
            }
        }
        maze.GenerateMaze();
        MazeUpdatedEvent();
    }

    private void ClearImages() {
        ArrayList<MazeImage> imagesList = mazePanel.getMaze().getImages();
        for (MazeImage image : imagesList) {
            mazePanel.getMaze().RemoveImage(image);
        }
        MazeUpdatedEvent();
    }

    /**
     * Opens a file chooser dialogue and adds image to maze images
     */
    private void ImportImage() throws InvalidInputException {
        int returnVal = fc.showOpenDialog(this);

        // https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            BufferedImage imageData;
            try {
                imageData = ImageIO.read(file);
                if (imageData == null) throw new Exception();
            }
            catch (Exception e) {
                throw new InvalidInputException("Please select an image file", this);
            }

            int width;
            int height;
            try {
                width = Integer.parseInt(imageWidth.getText());
                height = Integer.parseInt(imageHeight.getText());
                if (width <= 0 || height <= 0) throw new Exception();
            }
            catch (Exception e) {
                throw new InvalidInputException("Image dimensions must be a non-zero positive integer", this);
            }


            mazePanel.getMaze().getImages().add(new MazeImage(file.getName(), imageData, width, height));
            UpdateTable();
        }
    }

    /**
     * Updates maze saveState and database row
     * @throws IOException IOException
     */
    private void SaveMaze() throws IOException {
        Maze maze = mazePanel.getMaze();
        maze.UpdateLastEdited();
        saveState = Maze.MazeToByteArray(maze);

        JDBCDataSource ds = new JDBCDataSource();
        if (mazeID == -1) {
            mazeID = ds.AddMaze(maze);
        } else {
            ds.UpdateMaze(mazeID, maze);
        }
    }

    /**
     * Restores maze object to save state
     * @throws IOException Exception
     * @throws ClassNotFoundException Exception
     */
    private void RestoreMaze() throws IOException, ClassNotFoundException {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to restore the maze to the last saved state?\n" +
                        "This will remove all changes made since the last save.",
                "Confirm restore maze",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            mazePanel.setMaze(Maze.ByteArrayToMaze(saveState));
            UpdateTable();
            MazeUpdatedEvent();
        }
    }

    /**
     * Exports the current maze panel to png file
     * @throws IOException Exception
     */
    private void ExportToPNG() throws IOException {
        mazePanel.ExportToFile();
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
        for (MazeImage mazeImage : imageList) {
            String[] data = new String[3];
            data[0] = mazeImage.getImageTitle();
            data[1] = String.format("%s", mazeImage.getSizeX());
            data[2] = String.format("%s", mazeImage.getSizeY());
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

    /**
     * Creates 'images' JPanel object
     * @return JPanel object
     */
    private JPanel CreateImagesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Maze images editor"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );

        imagesTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        imagesTable.getColumnModel().getColumn(1).setPreferredWidth(75);
        imagesTable.getColumnModel().getColumn(2).setPreferredWidth(75);

        JScrollPane scrollPane = new JScrollPane(imagesTable);
        scrollPane.setPreferredSize(new Dimension(250, 100));

        GridBagConstraints gbc;
        int gridRow = 0;

        // import image button
        gbc = gbm.CreateInnerGBC(0, gridRow);
        gbc.gridwidth = 1;
        panel.add(importImage, gbc);

        // delete image button
        gbc = gbm.CreateInnerGBC(1, gridRow++);
        gbc.gridwidth = 1;
        panel.add(deleteImage, gbc);

        // import image options
        gbc = gbm.CreateInnerGBC(0, gridRow);
        panel.add(new Label("Image width:", Label.RIGHT), gbc);
        gbc = gbm.CreateInnerGBC(1, gridRow++);
        panel.add(imageWidth, gbc);
        gbc = gbm.CreateInnerGBC(0, gridRow);
        panel.add(new Label("Image height:", Label.RIGHT), gbc);
        gbc = gbm.CreateInnerGBC(1, gridRow++);
        panel.add(imageHeight, gbc);

        // imported images table
        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        panel.add(scrollPane, gbc);

        // place image button
        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        panel.add(placeImage, gbc);

        // clear all images button
        gbc = gbm.CreateInnerGBC(0, gridRow);
        gbc.gridwidth = 2;
        panel.add(clearImages, gbc);

        return panel;
    }

    /**
     * Creates 'generate' JPanel object
     * @return JPanel object
     */
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
        gbc = gbm.CreateInnerGBC(0, gridRow++);
        panel.add(generateMaze, gbc);

        // Toggle randomize images
        gbc = gbm.CreateInnerGBC(0, gridRow);
        panel.add(toggleRandomizeImages, gbc);

        return panel;
    }

    /**
     * Creates 'solution' JPanel object
     * @return JPanel object
     */
    private JPanel CreateSolutionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Maze metrics"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );

        GridBagConstraints gbc;
        int gridRow = 0;

        Maze maze = mazePanel.getMaze();
        int cellSize = maze.getCellSize();
        int mazeWidth = maze.getSizeX();
        int mazeHeight = maze.getSizeY();

        // toggle solution
        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        panel.add(toggleSolution, gbc);

        // solution percentage
        gbc = gbm.CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("Pixels per cell: " , SwingConstants.LEFT), gbc);
        gbc = gbm.CreateInnerGBC(1, gridRow++);
        panel.add(new JLabel(String.format("%s", cellSize), SwingConstants.LEFT), gbc);

        // solution percentage
        gbc = gbm.CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("Maze dimensions (cells): " , SwingConstants.LEFT), gbc);
        gbc = gbm.CreateInnerGBC(1, gridRow++);
        panel.add(new JLabel(String.format("%sx%s", mazeWidth, mazeHeight), SwingConstants.LEFT), gbc);

        // solution percentage
        gbc = gbm.CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("Maze dimensions (pixels): " , SwingConstants.LEFT), gbc);
        gbc = gbm.CreateInnerGBC(1, gridRow++);
        panel.add(new JLabel(String.format("%sx%s", mazeWidth * cellSize, mazeHeight * cellSize), SwingConstants.LEFT), gbc);

        // solution percentage
        gbc = gbm.CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("Solution percentage: " , SwingConstants.LEFT), gbc);
        gbc = gbm.CreateInnerGBC(1, gridRow++);
        panel.add(solutionPct, gbc);

        // solution percentage
        gbc = gbm.CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("Dead ends: ", SwingConstants.LEFT), gbc);
        gbc = gbm.CreateInnerGBC(1, gridRow);
        panel.add(deadEndsPct, gbc);

        return panel;
    }

    /**
     * Creates 'save' JPanel object
     * @return JPanel object
     */
    private JPanel CreateSavePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Save options"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );

        GridBagConstraints gbc;
        int gridRow = 0;

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(saveMaze, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(restoreMaze, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow);
        gbc.gridwidth = 1;
        panel.add(exportMaze, gbc);


        return panel;
    }

    /**
     * Creates 'options' JPanel object
     * @return JPanel object
     */
    private JPanel CreateOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc;
        int gridRow = 0;

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(CreateImagesPanel(), gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(CreateGeneratePanel(), gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(CreateSolutionPanel(), gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow);
        gbc.gridwidth = 1;
        panel.add(CreateSavePanel(), gbc);

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
        gbc.anchor = GridBagConstraints.EAST;
        add(mazePanel, gbc);

        // options panel
        gbc = gbm.CreateOuterGBC(1, gridRow);
        gbc.anchor = GridBagConstraints.WEST;
        add(CreateOptionsPanel(), gbc);

        // resizes window to preferred dimensions
        pack();

        // centre to screen
        setLocationRelativeTo(null);

        // set defaults
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void run() {
        CreateGUI();
    }

    /**
     * Main test method for testing this edit page
     */
    public static void main(String[] args) throws IOException, MazeException {
        // Generate maze
        Maze testMaze = new Maze("test-maze-title", "test-maze-author", 10,5, 32);

        // Create page with panel
        PageEdit testPage = new PageEdit(testMaze);
        SwingUtilities.invokeLater(testPage);
    }
}
