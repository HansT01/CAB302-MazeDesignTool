import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MazePanel extends JPanel {
    private Maze maze;
    private Cell[][] cells;
    private int cellSize;
    private boolean drawSolution = true;


    public MazePanel(Maze maze, int cellSize) {
        super();
        this.maze = maze;
        this.cells = maze.getCells();
        this.cellSize = cellSize;

        setPreferredSize(new Dimension(maze.getSizeX()*cellSize + 1, maze.getSizeY()*cellSize + 1));

        setBackground(Color.white);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ToggleWall(e.getX(), e.getY());
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.blue);


        Insets insets = getInsets();
        g2d.clearRect(0, 0, maze.getSizeX() * cellSize + 1, maze.getSizeY() * cellSize + 1);
        // setSize(maze.getSizeX() * cellSize + insets.left + insets.right + 1, maze.getSizeY() * cellSize + insets.top + insets.bottom + 1);

        if (drawSolution) {
            int offset = cellSize / 2;
            CellNode[] solution = maze.Solve();

            if (solution != null) {
                g2d.setColor(Color.RED);
                int xOld = solution[0].getCell().getX() * cellSize + insets.left + offset;
                int yOld = solution[0].getCell().getY() * cellSize + insets.top + offset;
                for (int i = 1; i < solution.length; i++) {
                    int x = solution[i].getCell().getX() * cellSize + insets.left + offset;
                    int y = solution[i].getCell().getY() * cellSize + insets.top + offset;

                    g2d.drawLine(xOld, yOld, x, y);
                    xOld = x;
                    yOld = y;
                }
                g2d.setColor(Color.BLUE);
            }
        }

        for (int i = 0; i < maze.getSizeX(); i++) {
            int x = i * cellSize + insets.left;
            for (int j = 0; j < maze.getSizeY(); j++) {
                int y = j * cellSize + insets.top;
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
     * Toggles a wall near the input x, y coordinate.
     * @param xPx x coordinate in pixels.
     * @param yPx y coordinate in pixels.
     */
    public void ToggleWall(int xPx, int yPx) {
        // Get every half cell index to determine which wall is clicked
        int x2 = (int) Math.round(2.0 * xPx / cellSize);
        int y2 = (int) Math.round(2.0 * yPx / cellSize);

        // Limit to one wall at a time
        boolean xSelect = (x2 % 2 == 1);
        boolean ySelect = (y2 % 2 == 1);
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

    /**
     * Gets the cell indices at an x, y coordinate.
     * @param xPx x coordinate in pixels.
     * @param yPx y coordinate in pixels.
     * @return x and y indices for a cell in this maze.
     */
    public int[] TranslateToIndex(int xPx, int yPx) {
        return new int[] {xPx / maze.getSizeX(), yPx / maze.getSizeY()};
    }

    public static void main(String[] args) {
        Maze testMaze = new Maze(32,32);
        testMaze.GenerateMaze();
        testMaze.Solve();
        MazePanel testPanel = new MazePanel(testMaze, 16);

        JFrame testWindow = new JFrame();
        testWindow.add(testPanel);
        testWindow.pack();

        testWindow.setLayout(null);
        testWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testWindow.setVisible(true);
    }
}
