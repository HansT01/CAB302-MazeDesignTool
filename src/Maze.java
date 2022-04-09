import java.util.*;

public class Maze {
    private String title;
    private String author;
    private Date dateCreated;
    private Date dateLastEdited;

    private int sizeX;
    private int sizeY;
    private int area;
    private Cell[][] cells;
    private ArrayList<MazeImage> images;

    /**
     * Constructs and initialises a new Maze.
     * @param sizeX The width of the maze in cells.
     * @param sizeY The height of the maze in cells.
     */
    public Maze(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.area = sizeX * sizeY;
        cells = new Cell[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                cells[x][y] = new Cell(x, y);
            }
        }
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

    /**
     * Calculates the area of the maze.
     * @return The area of the maze.
     */
    public int getArea() {
        return area;
    }

    /**
     * Generates a maze.
     * TODO Does not implement image blocks
     */
    public void GenerateMaze() {
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
        int totalCells = getArea();

        Random r = new Random();
        Cell currentCell = cells[r.nextInt(sizeX)][r.nextInt(sizeY)];

        int visitedCells = 1;

        while (visitedCells < totalCells) {
            ArrayList<Cell> neighbourCells = currentCell.GetClosedNeighbours(this);

            if (neighbourCells.size() != 0)
            {
                Cell neighbourCell = neighbourCells.get(r.nextInt(neighbourCells.size()));

                // Positive x is EAST, negative x is WEST
                // Positive y is SOUTH, negative y is NORTH

                int currentX = currentCell.getX();
                int currentY = currentCell.getY();
                int neighbourX = neighbourCell.getX();
                int neighbourY = neighbourCell.getY();

                currentCell.RemoveWallX(neighbourX - currentX);
                currentCell.RemoveWallY(neighbourY - currentY);
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
     * Solves maze from the startCell to endCell using the AStar algorithm.
     * @param startX The x position of the starting cell.
     * @param startY The y position of the starting cell.
     * @param endX The x position of the ending cell.
     * @param endY The y position of the ending cell.
     * @return
     * An array of the cell path from startCell to endCell.
     * An empty Cell array if no path was found.
     * TODO Does not implement image blocks
     */
    public ArrayList<CellNode> Solve(int startX, int startY, int endX, int endY) {
        /*
        AStar pseudocode:

        create a CellPQueue priority queue containing only the StartCell
        create a VisitedCells array list
        while there are still nodes to be visited
          dequeue CellQueue as CurrentCell
          if CurrentCell is the EndCell
            trace back the parents of all cells from EndCell to StartCell
            store it in an array
            return array
          if not:
            put CurrentCell in VisitedCells and find its neighbours
            set NeighbourCells as the neighbours
            for each NeighbourCell in NeighbourCells
              if NeighbourCell is not in VisitedCells
                PathCost = NeighbourCell.parent.PathCost + 1
                CombinedCost = PathCost + NeighbourCell.DistanceTo(EndCell)
                if PathCost is less than the current path cost of NeighbourCell
                  set the path cost of NeighbourCell to PathCost
                  set the combined cost of NeighbourCell to CombinedCost
                  set NeighbourCell's parent as CurrentCell
                enqueue NeighbourCell
        return null
        */

        Cell startCell = cells[startX][startY];
        Cell endCell = cells[endX][endY];

        CellNode startNode = new CellNode(startCell);
        startNode.setParent(startNode);
        startNode.setPathCost(0);

        PriorityQueue<CellNode> cellPQueue = new PriorityQueue();
        cellPQueue.add(startNode);

        HashMap<String, CellNode> visitedNodes = new HashMap();

        while (visitedNodes.size() < area)
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
                visitedNodes.put(currentNode.toString(), currentNode);
                ArrayList<Cell> neighbourCells = currentNode.getCell().GetOpenNeighbours(this);
                ArrayList<CellNode> neighbourNodes = new ArrayList();
                for (int i = 0; i < neighbourCells.size(); i++) {
                    neighbourNodes.add(new CellNode(neighbourCells.get(i)));
                }

                for (int i = 0; i < neighbourNodes.size(); i++) {
                    CellNode neighbourNode = neighbourNodes.get(i);

                    if (visitedNodes.get(neighbourNode.toString()) == null)
                    {
                        int pathCost = currentNode.getPathCost() + 1;
                        double combinedCost = pathCost + neighbourNode.getCell().DistanceTo(endCell);

                        if (pathCost < neighbourNode.getPathCost())
                        {
                            neighbourNode.setPathCost(pathCost);
                            neighbourNode.setCombinedCost(combinedCost);
                            neighbourNode.setParent(currentNode);
                        }
                        cellPQueue.add(neighbourNode);

                        /*
                        for (CellNode item:cellPQueue)
                        {
                            System.out.format("%s ", item.getCell());
                            System.out.format("%,.2f ", item.getCombinedCost());
                        }
                        System.out.println();
                         */
                    }
                }
            }
        }
        return null;
    }

    /**
     * Adds an image to images field.
     * @param image object of class MazeImage.
     * @param x x location of image.
     * @param y y location of image.
     */
    public void AddImage(MazeImage image, int x, int y) {
        image.setX(x);
        image.setY(y);
        images.add(image);
    }

    /**
     * Prints the maze to console.
     */
    public void Print() {
        for (int y = 0; y < sizeY; y++)
        {
            StringBuilder str = new StringBuilder();
            for (int x = 0; x < sizeX; x++)
            {
                System.out.format("1  %d  1  ", cells[x][y].getWalls()[0]);
            }
            System.out.println();
            for (int x = 0; x < sizeX; x++)
            {
                System.out.format("%d     %d  ", cells[x][y].getWalls()[3], cells[x][y].getWalls()[1]);
            }
            System.out.println();
            for (int x = 0; x < sizeX; x++)
            {
                System.out.format("1  %d  1  ", cells[x][y].getWalls()[2]);
            }
            System.out.println();
        }
    }

    /**
     * Calculates the percentage of cells in maze covered by a solution.
     * @param solution Path solution.
     * @return Percentage of cells in maze covered by a solution.
     */
    public double SolutionPct(ArrayList<CellNode> solution)
    {
        return 0;
    }

    /**
     * Calculates the percentage of cells in maze that are dead ends.
     * @return Percentage of cells in maze that are dead ends.
     */
    public double DeadEndPct()
    {
        return 0;
    }
}
