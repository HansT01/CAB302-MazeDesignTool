import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Maze {
    private int sizeX;
    private int sizeY;
    private Cell[][] cells;

    /**
     * Constructs and initialises a new Maze.
     * @param sizeX The width of the maze in cells.
     * @param sizeY The height of the maze in cells.
     */
    public Maze(int sizeX, int sizeY)
    {

    }

    /**
     * Generates a maze.
     */
    public void GenerateMaze(Cell startCell)
    {
        /*
        Maze generation pseudocode:

        create a CellStack to hold a list of cell locations
        set TotalCells to the number of cells in grid
        choose a starting cell location and call it CurrentCell
        set VisitedCells as 1

        while VisitedCells < TotalCells
          find all neighbours of CurrentCell with all walls intact
          if at least one is found
            choose one at random
            knock down the wall between it and CurrentCell
            push CurrentCell location onto the CellStack
            set CurrentCell as the new cell
            add 1 to VisitedCells
          else
            pop CellStack
            set CurrentCell as the new cell
         */

        Stack<Cell> cellStack = new Stack<>();
        int totalCells = GetArea();

        Cell currentCell = startCell;
        int visitedCells = 1;

        while (visitedCells < totalCells) {
            ArrayList<Cell> neighbourCells = currentCell.GetNeighbours(this);
            if (neighbourCells.size() != 0)
            {
                Random r = new Random();
                int random = r.nextInt() % neighbourCells.size();
                Cell neighbourCell = neighbourCells.get(random);

                // Positive x is EAST, negative x is WEST
                // Positive y is SOUTH, negative y is NORTH

                int currentX = currentCell.getX();
                int currentY = currentCell.getY();
                int neighbourX = neighbourCell.getX();
                int neighbourY = neighbourCell.getY();

                currentCell.RemoveWallX(neighbourX - currentX);
                currentCell.RemoveWallX(neighbourY - currentY);
                neighbourCell.RemoveWallX(currentX - neighbourX);
                neighbourCell.RemoveWallY(currentY - neighbourY);

                cellStack.add(currentCell);
                currentCell = neighbourCell;
                visitedCells++;
            } else {
                currentCell = cellStack.pop();
            }
        }

    }

    /**
     * Prints the maze to console.
     */
    public void PrintMaze()
    {

    }

    /**
     * Solves maze from the startCell to endCell.
     * @param startCell The starting cell.
     * @param endCell The ending cell.
     * @return
     * An array of the cell path from startCell to endCell.
     * An empty Cell array if no path was found.
     */
    public Cell[] Solve(Cell startCell, Cell endCell)
    {
        return new Cell[GetArea()];
    }

    /**
     * Calculates the area of the maze.
     * @return The area of the maze.
     */
    public int GetArea()
    {
        return 0;
    }

    /**
     * Getter for 2D cell array.
     * @return The 2D cell array.
     */
    public Cell[][] getCells() {
        return cells;
    }

    /**
     * Getter for maze width.
     * @return maze width.
     */
    public int getSizeX() {
        return sizeX;
    }

    /**
     * Getter for maze height.
     * @return maze height.
     */
    public int getSizeY() {
        return sizeY;
    }
}
