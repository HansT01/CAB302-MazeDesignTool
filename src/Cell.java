import java.util.ArrayList;

public class Cell {
    private final int x;
    private final int y;
    private boolean[] walls = {true, true, true, true};  // North, East, South, West

    /**
     * Constructs and initializes a cell object
     * @param x x coordinate of the cell
     * @param y y coordinate of the cell
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
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
     * Checks if this cell has all walls intact.
     * @return True if all walls are intact.
     */
    public boolean HasAllWalls() {
        boolean hasAllWalls = true;
        for (int i = 0; i < 4; i++)
        {
            if (!walls[i]) {
                hasAllWalls = false;
                break;
            }
        }
        return hasAllWalls;
    }

    /**
     * Sets all walls to false.
     */
    public void RemoveAllWalls() {
        walls = new boolean[] {false, false, false, false};
    }

    /**
     * Finds all neighbouring cells with all walls intact.
     * @param maze The maze the cell is located inside.
     * @return An array of all cells with intact walls.
     */
    public ArrayList<Cell> GetClosedNeighbours(Maze maze) {
        ArrayList<Cell> neighbours = new ArrayList<>();
        Cell[][] cells = maze.getCells();
        int sizeX = maze.getSizeX();
        int sizeY = maze.getSizeY();

        if (y > 0 && cells[x][y-1].HasAllWalls())
        {
            neighbours.add(cells[x][y-1]);
        }
        if (x < sizeX - 1 && cells[x+1][y].HasAllWalls())
        {
            neighbours.add(cells[x+1][y]);
        }
        if (y < sizeY - 1 && cells[x][y+1].HasAllWalls())
        {
            neighbours.add(cells[x][y+1]);
        }
        if (x > 0 && cells[x-1][y].HasAllWalls())
        {
            neighbours.add(cells[x-1][y]);
        }

        return neighbours;
    }

    /**
     * Finds all neighbours that are open to this cell.
     * @param maze The maze the cell is located inside.
     * @return ArrayList of open cell neighbours.
     */
    public ArrayList<Cell> GetOpenNeighbours(Maze maze) {
        ArrayList<Cell> neighbours = new ArrayList<>();
        Cell[][] cells = maze.getCells();
        int sizeX = maze.getSizeX();
        int sizeY = maze.getSizeY();

        if (y > 0 && !walls[0])
        {
            neighbours.add(cells[x][y-1]);
        }
        if (x < sizeX && !walls[1])
        {
            neighbours.add(cells[x+1][y]);
        }
        if (y < sizeY && !walls[2])
        {
            neighbours.add(cells[x][y+1]);
        }
        if (x > 0 && !walls[3])
        {
            neighbours.add(cells[x-1][y]);
        }

        return neighbours;
    }

    /**
     * Removes a wall on the x-axis.
     * @param wall Wall to be removed: 1 is East wall, -1 is West wall.
     */
    public void RemoveWallX(int wall) {
        if (wall == 1) {
            walls[1] = false;
        } else if (wall == -1) {
            walls[3] = false;
        }
    }

    /**
     * Removes a wall on the y-axis.
     * @param wall Wall to be removed: 1 is South wall, -1 is North wall.
     */
    public void RemoveWallY(int wall) {
        if (wall == 1) {
            walls[2] = false;
        } else if (wall == -1) {
            walls[0] = false;
        }
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
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x && y == cell.y;
    }
}
