import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Cell implements Serializable {
    /** The maze that is containing this cell */
    private final Maze parentMaze;
    private final int x;
    private final int y;

    /**
     * The status of the walls of this cell.
     * 0 - North, 1 - East, 2 - South, 3 - West.
     */
    private boolean[] walls = {true, true, true, true};
    private boolean coveredByImage = false;

    /**
     * Constructs and initializes a cell object
     * @param x x coordinate of the cell
     * @param y y coordinate of the cell
     */
    public Cell(int x, int y, Maze maze) {
        this.x = x;
        this.y = y;
        this.parentMaze = maze;
    }

    /**
     * Getter for x.
     * @return x.
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for y.
     * @return y.
     */
    public int getY() {
        return y;
    }

    /**
     * Getter for walls.
     * @return walls.
     */
    public boolean[] getWalls() {
        return walls;
    }

    /**
     * Setter for walls.
     * @param walls walls array of length 4 in order: North, East, South, West
     */
    public void setWalls(boolean[] walls) {
        this.walls = walls;
    }

    /**
     * Getter for coveredByImage.
     * @return True if covered by image.
     */
    public boolean isCoveredByImage() {
        return coveredByImage;
    }

    /**
     * Setter for coveredByImage.
     * @param coveredByImage Covered by image boolean value.
     */
    public void setCoveredByImage(boolean coveredByImage) {
        this.coveredByImage = coveredByImage;
    }

    /**
     * Calculates the distance between this and the target cell.
     * @param targetCell The target cell.
     * @return The distance between current cell and target cell in cells.
     */
    public double DistanceTo(Cell targetCell) {
        double xDiff = targetCell.getX()-x;
        double yDiff = targetCell.getY()-y;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    /**
     * Checks each wall and returns the number of intact walls.
     * @return Number of intact walls.
     */
    public int CountWalls() {
        int counter = 0;
        for (int i = 0; i < 4; i++) {
            if (walls[i]) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Toggles wall between this cell and its corresponding neighbour.
     * @param i Index of wall in order: North, East, South, West.
     */
    public void ToggleWall(int i) {
        Cell[][] cells = parentMaze.getCells();
        int sizeX = parentMaze.getSizeX();
        int sizeY = parentMaze.getSizeY();

        // Toggle this wall
        walls[i] = !walls[i];

        // Toggle neighbouring wall
        if (i == 0 && y > 0) cells[x][y-1].walls[2] = !cells[x][y-1].walls[2];
        if (i == 1 && x < sizeX - 1) cells[x+1][y].walls[3] = !cells[x+1][y].walls[3];
        if (i == 2 && y < sizeY - 1) cells[x][y+1].walls[0] = !cells[x][y+1].walls[0];
        if (i == 3 && x > 0) cells[x-1][y].walls[1] = !cells[x-1][y].walls[1];
    }

    /**
     * Removes wall between this cell and its corresponding neighbour.
     * @param i Index of wall in order: North, East, South, West.
     */
    public void RemoveWall(int i) {
        Cell[][] cells = parentMaze.getCells();
        int sizeX = parentMaze.getSizeX();
        int sizeY = parentMaze.getSizeY();

        // Toggle this wall
        walls[i] = false;

        if (i == 0 && y > 0) cells[x][y-1].walls[2] = false;
        if (i == 1 && x < sizeX - 1) cells[x+1][y].walls[3] = false;
        if (i == 2 && y < sizeY - 1) cells[x][y+1].walls[0] = false;
        if (i == 3 && x > 0) cells[x-1][y].walls[1] = false;
    }

    /**
     * Finds all neighbouring cells with all walls intact.
     * @return An array of all cells with intact walls.
     */
    public ArrayList<Cell> GetClosedNeighbours() {
        ArrayList<Cell> neighbours = new ArrayList<>(4);
        Cell[][] cells = parentMaze.getCells();
        int sizeX = parentMaze.getSizeX();
        int sizeY = parentMaze.getSizeY();

        if (y > 0 && cells[x][y-1].CountWalls()==4 && !cells[x][y-1].isCoveredByImage())
        {
            neighbours.add(cells[x][y-1]);
        }
        if (x < sizeX - 1 && cells[x+1][y].CountWalls()==4 && !cells[x+1][y].isCoveredByImage())
        {
            neighbours.add(cells[x+1][y]);
        }
        if (y < sizeY - 1 && cells[x][y+1].CountWalls()==4 && !cells[x][y+1].isCoveredByImage())
        {
            neighbours.add(cells[x][y+1]);
        }
        if (x > 0 && cells[x-1][y].CountWalls()==4 && !cells[x-1][y].isCoveredByImage())
        {
            neighbours.add(cells[x-1][y]);
        }

        return neighbours;
    }

    /**
     * Finds all neighbours that this cell is open to.
     * @return ArrayList of open cell neighbours.
     */
    public ArrayList<Cell> GetOpenNeighbours() {
        ArrayList<Cell> neighbours = new ArrayList<>(4);
        Cell[][] cells = parentMaze.getCells();
        int sizeX = parentMaze.getSizeX();
        int sizeY = parentMaze.getSizeY();

        if (y > 0 && !walls[0] && !cells[x][y-1].walls[2])
        {
            neighbours.add(cells[x][y-1]);
        }
        if (x < sizeX - 1 && !walls[1] && !cells[x+1][y].walls[3])
        {
            neighbours.add(cells[x+1][y]);
        }
        if (y < sizeY - 1 && !walls[2] && !cells[x][y+1].walls[0])
        {
            neighbours.add(cells[x][y+1]);
        }
        if (x > 0 && !walls[3] && !cells[x-1][y].walls[1])
        {
            neighbours.add(cells[x-1][y]);
        }

        return neighbours;
    }

    /**
     * String representation of a cell.
     * @return the x and y coordinates of the cell.
     */
    @Override
    public String toString() {
        return String.format("(%s,%s)", x, y);
    }

    /**
     * Equals method.
     * @param o comparison object
     * @return true if both cells have same coordinates
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return false;
    }
}
