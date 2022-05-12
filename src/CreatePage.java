import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Constructs window for entering information required to create new maze
 */
public class CreatePage extends JFrame implements Runnable {
    private final JTextField mazeTitle = new JTextField();
    private final JTextField mazeWidth = new JTextField();
    private final JTextField mazeHeight = new JTextField();
    private final JTextField cellSizeInput = new JTextField();

    private final JTextField startCellXField = new JTextField();
    private final JTextField startCellYField = new JTextField();
    private final JTextField endCellXField = new JTextField();
    private final JTextField endCellYField = new JTextField();

    private final JLabel startImageLabel = new JLabel("No image selected", SwingUtilities.LEFT);
    private final JLabel endImageLabel = new JLabel("No image selected", SwingUtilities.LEFT);
    private final JButton importStartImage = new JButton("Import image");
    private final JButton importEndImage = new JButton("Import image");
    private final JTextField startImageWidthField = new JTextField();
    private final JTextField startImageHeightField = new JTextField();
    private final JTextField endImageWidthField = new JTextField();
    private final JTextField endImageHeightField = new JTextField();

    private final JButton createMaze = new JButton("Create maze");

    private MazeImage startImage;
    private MazeImage endImage;

    final JFileChooser fc = new JFileChooser();

    public CreatePage() {
        fc.setFileFilter(new FileNameExtensionFilter("Image files", "jpeg", "jpg", "png", "gif"));
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));

        importStartImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startImage = ImportImage();
                UpdateLabels();
            }
        });
        importEndImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                endImage = ImportImage();
                UpdateLabels();
            }
        });
        createMaze.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    CreateMaze();
                } catch (IOException | InvalidInputException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void CreateMaze() throws IOException, InvalidInputException {
        if (mazeTitle.getText().isEmpty()) {
            throw new InvalidInputException("Please input a title", this);
        }
        int sizeX;
        int sizeY;
        int cellSize;
        try {
            sizeX = Integer.parseInt(mazeWidth.getText());
            sizeY = Integer.parseInt(mazeHeight.getText());
            cellSize = Integer.parseInt(cellSizeInput.getText());
        }
        catch (Exception e) {
            throw new InvalidInputException("Please input a number for width, height, and cell size", this);
        }

        if (sizeX <= 0 || sizeY <= 0 || cellSize <= 0) {
            throw new InvalidInputException("Maze dimensions cannot be zero or negative", this);
        }

        // TODO change author to use database username
        Maze maze = new Maze(mazeTitle.getText(), "STEVE", sizeX, sizeY);

        int startX;
        int startY;
        int endX;
        int endY;
        try {
            startX = Integer.parseInt(startCellXField.getText()) - 1;
            startY = Integer.parseInt(startCellYField.getText()) - 1;
            endX = Integer.parseInt(endCellXField.getText()) - 1;
            endY = Integer.parseInt(endCellYField.getText()) - 1;
        }
        catch (Exception e) {
            throw new InvalidInputException("Please input a number for start and end locations");
        }

        if (!maze.CheckInBounds(startX, startY) || !maze.CheckInBounds(endX, endY)) {
            throw new InvalidInputException("Start and end locations must be within bounds");
        }

        MazeImage startMazeImage;
        MazeImage endMazeImage;
        if (startImage != null) {
            int startImageWidth;
            int startImageHeight;
            try {
                startImageWidth = Integer.parseInt(startImageWidthField.getText());
                startImageHeight = Integer.parseInt(startImageHeightField.getText());
            }
            catch (Exception e) {
                throw new InvalidInputException("Please input a number for start image dimensions");
            }

            startMazeImage = new MazeImage(startImage.getImageTitle(), startImage.getImageData().getImage(), startImageWidth, startImageHeight);
            if (!maze.CheckInBounds(startX, startY, startMazeImage)) {
                throw new InvalidInputException("Start image is out of bounds");
            }
            maze.setStartImage(startMazeImage);
        }
        if (endImage != null) {
            int endImageWidth;
            int endImageHeight;
            try {
                endImageWidth = Integer.parseInt(endImageWidthField.getText());
                endImageHeight = Integer.parseInt(endImageHeightField.getText());
            }
            catch (Exception e) {
                throw new InvalidInputException("Please input a number for end image dimensions");
            }

            endMazeImage = new MazeImage(endImage.getImageTitle(), endImage.getImageData().getImage(), endImageWidth, endImageHeight);
            if (!maze.CheckInBounds(startX, startY, endMazeImage)) {
                throw new InvalidInputException("Start image is out of bounds");
            }
            maze.setEndImage(endMazeImage);
        }

        EditPage editPage = new EditPage(maze, cellSize);
        SwingUtilities.invokeLater(editPage);
        dispose();
    }

    private void UpdateLabels() {
        if (startImage != null) {
            startImageLabel.setText(startImage.getImageTitle());
        }
        else {
            startImageLabel.setText("No image selected");
        }

        if (endImage != null) {
            endImageLabel.setText(endImage.getImageTitle());
        }
        else {
            endImageLabel.setText("No image selected");
        }
    }

    private MazeImage ImportImage() {
        int returnVal = fc.showOpenDialog(this);

        // https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                System.out.println("Opening file " + file.getName());
                BufferedImage image = ImageIO.read(file);
                return new MazeImage(file.getName(), image);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Open command cancelled by user");
        }
        return null;
    }

    private JPanel CreateMazePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Maze properties"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );

        GridBagConstraints gbc;
        int gridRow = 0;

        // input title
        gbc = CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("Maze Title: ", SwingUtilities.LEFT), gbc);
        gbc = CreateInnerGBC(1, gridRow++);
        System.out.println(gbc.insets.toString());
        panel.add(mazeTitle, gbc);

        // input width
        gbc = CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("Width: ", SwingUtilities.LEFT), gbc);
        gbc = CreateInnerGBC(1, gridRow++);
        panel.add(mazeWidth, gbc);

        // input height
        gbc = CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("Height: ", SwingUtilities.LEFT), gbc);
        gbc = CreateInnerGBC(1, gridRow++);
        panel.add(mazeHeight, gbc);

        // input cell size
        gbc = CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("Cell size (pixels): ", SwingUtilities.LEFT), gbc);
        gbc = CreateInnerGBC(1, gridRow);
        panel.add(cellSizeInput, gbc);

        return panel;
    }

    private JPanel CreateStartEndPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Solution properties"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );

        GridBagConstraints gbc;
        int gridRow = 0;

        // start cell label
        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        panel.add(new JLabel("Start cell", SwingUtilities.LEFT), gbc);

        // start x input
        gbc = CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("x: ", SwingUtilities.LEFT), gbc);
        gbc = CreateInnerGBC(1, gridRow++);
        panel.add(startCellXField, gbc);

        // start y input
        gbc = CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("y: ", SwingUtilities.LEFT), gbc);
        gbc = CreateInnerGBC(1, gridRow++);
        panel.add(startCellYField, gbc);

        // end cell label
        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        panel.add(new JLabel("End cell", SwingUtilities.LEFT), gbc);

        // end x input
        gbc = CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("x: ", SwingUtilities.LEFT), gbc);
        gbc = CreateInnerGBC(1, gridRow++);
        panel.add(endCellXField, gbc);

        // end y input
        gbc = CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("y: ", SwingUtilities.LEFT), gbc);
        gbc = CreateInnerGBC(1, gridRow);
        panel.add(endCellYField, gbc);

        return panel;
    }

    private JPanel CreateStartImagePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Start image (optional)"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );

        GridBagConstraints gbc;
        int gridRow = 0;

        // start image label
        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        panel.add(startImageLabel, gbc);

        // import start image
        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        panel.add(importStartImage, gbc);

        // start image width
        gbc = CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("Width: ", SwingUtilities.LEFT), gbc);
        gbc = CreateInnerGBC(1, gridRow++);
        panel.add(startImageWidthField, gbc);

        // start image height
        gbc = CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("Height: ", SwingUtilities.LEFT), gbc);
        gbc = CreateInnerGBC(1, gridRow++);
        panel.add(startImageHeightField, gbc);

        return panel;
    }

    private JPanel CreateEndImagePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("End image (optional)"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );

        GridBagConstraints gbc;
        int gridRow = 0;

        // end image label
        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        panel.add(endImageLabel, gbc);

        // import end image
        gbc = CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 2;
        panel.add(importEndImage, gbc);

        // end image width
        gbc = CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("Width: ", SwingUtilities.LEFT), gbc);
        gbc = CreateInnerGBC(1, gridRow++);
        panel.add(endImageWidthField, gbc);

        // end image height
        gbc = CreateInnerGBC(0, gridRow);
        gbc.weightx = 0;
        panel.add(new JLabel("Height: ", SwingUtilities.LEFT), gbc);
        gbc = CreateInnerGBC(1, gridRow);
        panel.add(endImageHeightField, gbc);

        return panel;
    }

    private JPanel CreateMainPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(500,630));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc;
        int gridRow = 0;

        // maze panel
        gbc = CreateInnerGBC(0, gridRow++);
        panel.add(CreateMazePanel(), gbc);

        // start-end panel
        gbc = CreateInnerGBC(0, gridRow++);
        panel.add(CreateStartEndPanel(), gbc);

        // images panel
        gbc = CreateInnerGBC(0, gridRow++);
        panel.add(CreateStartImagePanel(), gbc);
        gbc = CreateInnerGBC(0, gridRow++);
        panel.add(CreateEndImagePanel(), gbc);

        // create maze button
        gbc = CreateInnerGBC(0, gridRow++);
        panel.add(createMaze, gbc);
        return panel;
    }


    private void CreateGUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc;

        gbc = CreateOuterGBC(0, 0);
        add(CreateMainPanel(), gbc);

        // resizes window to preferred dimensions
        pack();

        // centre to screen
        setLocationRelativeTo(null);

        // set defaults
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        int innerPaddingSize = 5;
        gbc.insets = new Insets((y!=0) ? innerPaddingSize : 0, (x!=0) ? innerPaddingSize : 0, 0, 0);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        return gbc;
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

        int outerPaddingSize = 20;
        gbc.insets = new Insets((y==0) ? outerPaddingSize : 0, (x==0) ? outerPaddingSize : 0, outerPaddingSize, outerPaddingSize);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        return gbc;
    }

    @Override
    public void run() {
        CreateGUI();
    }

    /**
     * Main test method for testing this edit page
     */
    public static void main(String[] args) throws IOException {
        // test page
        CreatePage testPage = new CreatePage();
        SwingUtilities.invokeLater(testPage);
    }
}
