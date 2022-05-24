import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MazePanel extends JPanel {
    private Maze maze;
    private final int cellSize;
    private boolean drawSolution = true;
    private boolean placingImage = false;

    public void setDrawSolution(boolean drawSolution) {
        this.drawSolution = drawSolution;
    }

    public Maze getMaze() {
        return maze;
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public void setPlacingImage(boolean placingImage) {
        this.placingImage = placingImage;
    }

    public void paint(Graphics g) {
        Cell[][] cells = maze.getCells();
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        ArrayList<MazeImage> images = maze.getImages();
        if (maze.getStartImage() != null) {
            MazeImage image = maze.getStartImage();
            g2d.drawImage(image.getImageData().getImage(), image.getX() * cellSize, image.getY() * cellSize,
                    image.getSizeX() * cellSize, image.getSizeY() * cellSize, null);
        }
        if (maze.getEndImage() != null) {
            MazeImage image = maze.getEndImage();
            g2d.drawImage(image.getImageData().getImage(), image.getX() * cellSize, image.getY() * cellSize,
                    image.getSizeX() * cellSize, image.getSizeY() * cellSize, null);
        }
        for (MazeImage image : images) {
            if (image.isPlaced()) {
                g2d.drawImage(image.getImageData().getImage(), image.getX() * cellSize, image.getY() * cellSize,
                        image.getSizeX() * cellSize, image.getSizeY() * cellSize, null);
            }
        }

        if (drawSolution) {
            int offset = cellSize / 2;
            Cell[] solution = maze.Solve();

            if (solution != null) {
                g2d.setColor(Color.RED);
                int xOld = solution[0].getX() * cellSize + offset;
                int yOld = solution[0].getY() * cellSize + offset;
                for (int i = 1; i < solution.length; i++) {
                    int x = solution[i].getX() * cellSize + offset;
                    int y = solution[i].getY() * cellSize + offset;

                    g2d.drawLine(xOld, yOld, x, y);
                    xOld = x;
                    yOld = y;
                }
            }
        }

        g2d.setColor(Color.BLACK);
        for (int i = 0; i < maze.getSizeX(); i++) {
            int x = i * cellSize;
            for (int j = 0; j < maze.getSizeY(); j++) {
                int y = j * cellSize;
                boolean[] walls = cells[i][j].getWalls();

                if (walls[0]) {
                    // North wall
                    g2d.drawLine(x, y, x + cellSize, y);
                }
                if (walls[1]) {
                    // East wall
                    g2d.drawLine(x + cellSize, y, x + cellSize, y + cellSize);
                }
                if (walls[2]) {
                    // South wall
                    g2d.drawLine(x, y + cellSize, x + cellSize, y + cellSize);
                }
                if (walls[3]) {
                    // West wall
                    g2d.drawLine(x, y, x, y + cellSize);
                }
            }
        }
    }

    /**
     * Prints the current panel to a BufferedImage object
     * @return BufferedImage object
     */
    public BufferedImage PrintToImage() {
        // https://stackoverflow.com/questions/1349220/convert-jpanel-to-image
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        paint(g);
        g.dispose();
        return image;
    }

    /**
     * Exports this maze panel to a png file named after the maze's title
     * @throws IOException
     */
    public void ExportToFile() throws IOException {
        File outputFile = new File(maze.getTitle() + ".png");
        ImageIO.write(PrintToImage(), "png", outputFile);
    }

    /**
     * Constructs the maze panel.
     * @param maze The Maze object to be rendered.
     * @param cellSize The size of each cell in the maze. The size should be even for proper rendering.
     */
    public MazePanel(Maze maze, int cellSize) {
        super();
        this.maze = maze;
        this.cellSize = cellSize;

        setPreferredSize(new Dimension(maze.getSizeX()*cellSize + 1, maze.getSizeY()*cellSize + 1));
        setBackground(Color.white);

        // Sourced from: https://stackhowto.com/how-to-get-mouse-position-on-click-relative-to-jframe/
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                HandleClickEvent(e);
            }
        });
    }




    /**
     * Toggles a wall near the input x, y coordinate.
     * @param e MouseEvent click event.
     */
    private void HandleClickEvent(MouseEvent e) {
        Cell[][] cells = maze.getCells();
        if (placingImage) {
            int x = e.getX() / cellSize;
            int y = e.getY() / cellSize;

            int xPos = (x == maze.getSizeX()) ? x - 1 : x;
            int yPos = (y == maze.getSizeY()) ? y - 1 : y;

            // If x is out of bounds
            maze.PlaceImage(xPos, yPos);
            repaint();
        }
        else {
            // Round to every half cell index to determine which wall is clicked
            int x2 = (int) Math.round(2.0 * e.getX() / cellSize);
            int y2 = (int) Math.round(2.0 * e.getY() / cellSize);
            boolean xSelect = (x2 % 2 == 1);
            boolean ySelect = (y2 % 2 == 1);

            // If a wall is clicked
            if (xSelect ^ ySelect) {
                // If x is out of bounds
                if (x2 / 2 == maze.getSizeX()) {
                    cells[x2 / 2 - 1][y2 / 2].ToggleWall(1);
                }
                // If y is out of bounds
                else if (y2 / 2 == maze.getSizeY()) {
                    cells[x2 / 2][y2 / 2 - 1].ToggleWall(2);
                }
                // If x is selected
                else if (xSelect) {
                    cells[x2 / 2][y2 / 2].ToggleWall(0);
                }
                // If y is selected
                else {
                    cells[x2 / 2][y2 / 2].ToggleWall(3);
                }
                repaint();
            }
        }
    }

    /**
     * Gets the cell indices at an x, y coordinate.
     * @param xPx x coordinate in pixels.
     * @param yPx y coordinate in pixels.
     * @return x and y indices for a cell in this maze.
     */
    public int[] TranslateToIndex(int xPx, int yPx) {
        return new int[] {xPx / maze.getSizeX(), yPx / maze.getSizeY()};
    }

    // For testing maze panel, do not remove!
    public static void main(String[] args) {
        // Generate maze panel
        Maze testMaze = new Maze("Maze Title", "Maze Author", 6,4);
        testMaze.GenerateMaze();
        MazePanel mazePanel = new MazePanel(testMaze, 64);

        // Create new frame
        JFrame frame = new JFrame();

        // Automatically centres content to frame
        frame.setLayout(new GridBagLayout());
        frame.add(mazePanel, new GridBagConstraints());

        // Resizes window to preferred dimensions
        frame.pack();

        // Centre to screen
        frame.setLocationRelativeTo(null);

        // Set defaults
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
