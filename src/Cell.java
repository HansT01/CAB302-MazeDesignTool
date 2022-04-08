import java.util.ArrayList;

public class Cell {
    private int x;
    private int y;
    private int[] walls = {1, 1, 1, 1};  // North, East, South, West

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
     * Calculates the distance between this and the target cell.
     * @param targetCell The target cell.
     * @return The distance between current cell and target cell in cells.
     */
    public double DistanceTo(Cell targetCell)
    {
        return 0;
    }

    /**
     * Checks if this cell has all walls intact.
     * @return True if all walls are intact.
     */
    public Boolean HasAllWalls()
    {
        return false;
    }

    /**
     * Finds all neighbouring cells.
     * @param maze The maze the cell is located inside.
     * @return An array of cells that are adjacent to this cell.
     */
    public ArrayList<Cell> GetNeighbours(Maze maze)
    {
        ArrayList<Cell> neighbours = new ArrayList<>();
        Cell[][] cells = maze.getCells();
        int sizeX = maze.getSizeX();
        int sizeY = maze.getSizeY();

        if (y > 0)
        {
            neighbours.add(cells[y-1][x]);
        }
        if (x < sizeX)
        {
            neighbours.add(cells[y][x+1]);
        }
        if (y < sizeY)
        {
            neighbours.add(cells[y+1][x]);
        }
        if (x > 0)
        {
            neighbours.add(cells[y][x-1]);
        }

        return neighbours;
    }

    public ArrayList<Cell> GetOpenNeighbours(Maze maze)
    {
        ArrayList<Cell> neighbours = new ArrayList<>();
        Cell[][] cells = maze.getCells();
        int sizeX = maze.getSizeX();
        int sizeY = maze.getSizeY();

        if (y > 0 && walls[0] == 0)
        {
            neighbours.add(cells[y-1][x]);
        }
        if (x < sizeX && walls[1] == 0)
        {
            neighbours.add(cells[y][x+1]);
        }
        if (y < sizeY && walls[2] == 0)
        {
            neighbours.add(cells[y+1][x]);
        }
        if (x > 0 && walls[3] == 0)
        {
            neighbours.add(cells[y][x-1]);
        }

        return neighbours;
    }


    /**
     * Removes a wall on the x-axis.
     * @param wall Wall to be removed: 1 is East wall, -1 is West wall.
     */
    public void RemoveWallX(int wall)
    {
        if (wall == 1)
        {
            walls[1] = 0;
        }
        else if (wall == -1)
        {
            walls[3] = 0;
        }
    }

    /**
     * Removes a wall on the y-axis.
     * @param wall Wall to be removed: 1 is South wall, -1 is North wall.
     */
    public void RemoveWallY(int wall)
    {
        if (wall == 1)
        {
            walls[2] = 0;
        }
        else if (wall == -1)
        {
            walls[0] = 0;
        }
    }

    public void Print()
    {
        System.out.format("1  %d  1\n", walls[0]);
        System.out.format("%d     %d\n", walls[3], walls[1]);
        System.out.format("1  %d  1\n", walls[2]);
    }
}
