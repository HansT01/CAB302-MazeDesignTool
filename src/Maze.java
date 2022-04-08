import java.util.*;

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
     * TODO Code is untested atm
     */
    public ArrayList<CellNode> Solve(Cell startCell, Cell endCell)
    {
        /*
        AStar pseudocode:


        create a CellPQueue priority queue containing only the StartCell
        create a VisitedCells array list
        while (the destination node has not been reached)
          dequeue CellQueue as CurrentCell
          if CurrentCell is the EndCell
            trace back the parents of all cells from EndCell to StartCell
            store it in an array
            return array
          if not:
            put CurrentCell in VisitedCells and find its neighbours
            set NeighbourCells as the neighbours
            for each NeighbourCell in NeighbourCells
              PathCost = NeighbourCell.parent.PathCost + 1
              CombinedCost = PathCost + NeighbourCell.DistanceTo(EndCell)
              if PathCost is less than the current path cost of NeighbourCell
                set the path cost of NeighbourCell to PathCost
                set the combined cost of NeighbourCell to CombinedCost
                set NeighbourCell's parent as CurrentCell
              if NeighbourCell is not in VisitedCells
                enqueue NeighbourCell
        */

        PriorityQueue<CellNode> cellPQueue = new PriorityQueue();
        cellPQueue.add(new CellNode(startCell));

        ArrayList<CellNode> visitedNodes = new ArrayList();

        while (true)
        {
            CellNode currentNode = cellPQueue.remove();
            if (currentNode.getCell() == endCell)
            {
                ArrayList<CellNode> solution = new ArrayList();
                solution.add(currentNode);
                while (currentNode.getCell() != startCell)
                {
                    currentNode = currentNode.getParent();
                    solution.add(currentNode);
                }
                Collections.reverse(solution);
                return solution;
            } else {
                visitedNodes.add(currentNode);
                ArrayList<Cell> neighbourCells = currentNode.getCell().GetOpenNeighbours(this);

                ArrayList<CellNode> neighbourNodes = new ArrayList();
                for (int i = 0; i < neighbourCells.size(); i++) {
                    neighbourNodes.add(new CellNode(neighbourCells.get(i)));
                }

                for (int i = 0; i < neighbourNodes.size(); i++) {
                    CellNode neighbourNode = neighbourNodes.get(i);
                    int pathCost = neighbourNode.getParent().getPathCost() + 1;
                    double combinedCost = pathCost + neighbourNode.getCell().DistanceTo(endCell);

                    if (pathCost < neighbourNode.getPathCost())
                    {
                        neighbourNode.setPathCost(pathCost);
                        neighbourNode.setCombinedCost(combinedCost);
                        neighbourNode.setParent(currentNode);
                    }
                    if (visitedNodes.contains(neighbourNode))
                    {
                        cellPQueue.add(neighbourNode);
                    }
                }
            }
        }
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
