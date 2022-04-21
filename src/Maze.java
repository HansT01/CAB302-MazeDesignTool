import java.util.*;

public class Maze {
    private String title;
    private String author;
    private Date dateCreated;
    private Date dateLastEdited;

    private final int sizeX;
    private final int sizeY;
    private final int area;
    private final Cell[][] cells;
    private ArrayList<MazeImage> images = new ArrayList<>();
    private int imageCells = 0;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

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

        this.startX = 0;
        this.startY = 0;
        this.endX = sizeX - 1;
        this.endY = sizeY - 1;

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                cells[x][y] = new Cell(x, y, this);
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
     * Getter for title.
     * @return Maze title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for author.
     * @return Maze author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Getter for date created.
     * @return Maze date creation time.
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Getter for date last edited
     * @return Maze date last edited.
     */
    public Date getDateLastEdited() {
        return dateLastEdited;
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
     */
    public void GenerateMaze() {
        /*
        Recursive Backtracking algorithm pseudocode:

        create a cell stack for backtracking
        create a visited cells count variable
        pick a random starting cell as the current cell

        while there are still cells to be visited
            find current cell neighbours with all walls intact
            if at least one is found
                choose one neighbour cell at random
                knock down the wall between it and current cell
                push current cell onto the cell stack
                increment visited cells
                set current cell as the neighbour cell
            else
                pop the cell stack and set it as current cell
         */

        // Create cell stack.
        Stack<Cell> cellStack = new Stack<>();
        
        // Construct random object.
        Random r = new Random();

        // Visited cells include cells covered by an image.
        int visitedCellsCount = 1 + imageCells;
        int totalCells = getArea();

        Cell currentCell = cells[startX][startY];

        if (currentCell.isCoveredByImage()) {
            throw new RuntimeException();
        }

        // Loop until all cells have been visited.
        while (visitedCellsCount < totalCells) {
            // Find all neighbours of current cell with all walls intact.
            ArrayList<Cell> neighbourCells = currentCell.GetClosedNeighbours();

            // If any neighbour cells are available.
            if (neighbourCells.size() != 0)
            {
                // Pick a random neighbour cell
                Cell neighbourCell = neighbourCells.get(r.nextInt(neighbourCells.size()));

                // Positive x direction is EAST, negative x direction is WEST.
                // Positive y direction is SOUTH, negative y direction is NORTH.
                int xDiff = neighbourCell.getX() - currentCell.getX();
                int yDiff = neighbourCell.getY() - currentCell.getY();

                // Break down the walls between the current and neighbour cell.
                if (yDiff < 0) {
                    currentCell.RemoveWall(0);
                }
                else if (xDiff > 0) {
                    currentCell.RemoveWall(1);
                }
                else if (yDiff > 0) {
                    currentCell.RemoveWall(2);
                }
                else {
                    currentCell.RemoveWall(3);
                }

                // Push the current cell onto the stack and increment visited cells count.
                cellStack.add(currentCell);
                visitedCellsCount++;

                // Set the current cell as the neighbour cell.
                currentCell = neighbourCell;
            } else {
                // Pop the cell stack as the current cell.
                currentCell = cellStack.pop();
            }
        }

    }

    /**
     * Solves maze from the startX, startY to endX, endY using the AStar algorithm.
     * @return
     * An array of the cell path from startCell to endCell.
     * An empty Cell array if no path was found.
     */
    public CellNode[] Solve() {
        /*
        AStar pathfinding pseudocode:

        create a priority queue that sorts by the combined cost
        create a visited cells hash map
        enqueue the start cell to the priority queue
        while there are still nodes to be visited
            dequeue priority queue as current cell
            if current cell is the end cell
                trace back the parents of all cells from end to start cell
                store it in an array and reverse it
                return array
            else
                put current cell in visited cells
                find current cell neighbours as neighbour cells
                for each neighbour cell
                    if the neighbour cell is not in visited cells
                        calculate path and combined cost of the neighbour cell
                        if the path cost is less than the neighbour cell's path cost
                            update the neighbour cell's path and combined cost
                            set the neighbour cell's parent as the current cell
                        enqueue the neighbour cell to the priority queue
        return null
        */

        // Start and end cells.
        Cell startCell = cells[startX][startY];
        Cell endCell = cells[endX][endY];

        // Construct a node on the start cell.
        CellNode startNode = new CellNode(startCell);
        startNode.setParent(startNode);
        startNode.setPathCost(0);

        // Create a priority queue, sorted by the combined cost of a node.
        // The combined cost of the node is its displacement to the end node added to its path cost.
        PriorityQueue<CellNode> cellPQueue = new PriorityQueue<>();
        cellPQueue.add(startNode);

        // Create a hash map to store visited nodes.
        HashMap<String, CellNode> visitedNodes = new HashMap<>();

        // Loop until all nodes have been checked.
        while (visitedNodes.size() < area)
        {
            // Dequeue first node in queue as current node.
            CellNode currentNode = cellPQueue.poll();

            // Break queue if all accessible nodes have been checked.
            if (currentNode == null) {
                break;
            }

            // Check if cell in the node is the end cell.
            if (currentNode.getCell() == endCell) {
                // Generate and return the solution.
                ArrayList<CellNode> solution = new ArrayList<>();
                solution.add(currentNode);
                while (currentNode.getCell() != startCell) {
                    currentNode = currentNode.getParent();
                    solution.add(currentNode);
                }
                Collections.reverse(solution);
                return solution.toArray(new CellNode[0]);
            } else {
                // Mark current node as complete by putting it in the hash map.
                visitedNodes.put(currentNode.toString(), currentNode);

                // Get all neighbour nodes of current node.
                ArrayList<Cell> neighbourCells = currentNode.getCell().GetOpenNeighbours();
                ArrayList<CellNode> neighbourNodes = new ArrayList<>();
                for (Cell neighbourCell : neighbourCells) {
                    neighbourNodes.add(new CellNode(neighbourCell));
                }

                for (CellNode neighbourNode : neighbourNodes) {
                    // Skip visited neighbour nodes by checking hash map.
                    if (visitedNodes.get(neighbourNode.toString()) == null) {
                        // Calculate path and combined cost of the neighbour node.
                        int pathCost = currentNode.getPathCost() + 1;
                        double combinedCost = pathCost + neighbourNode.getCell().DistanceTo(endCell);

                        // If the cost is lower than its current cost, update it and
                        // set its parent to the current node.
                        if (pathCost < neighbourNode.getPathCost()) {
                            neighbourNode.setPathCost(pathCost);
                            neighbourNode.setCombinedCost(combinedCost);
                            neighbourNode.setParent(currentNode);
                        }
                        // Add the neighbour node to the priority queue.
                        cellPQueue.add(neighbourNode);
                    }
                }
            }
        }
        // No solution was found.
        return null;
    }

    /**
     * Adds an image to images field, increments the imageCells, removes all walls under image cells
     * @param image object of class MazeImage.
     * @param xPos x position of image.
     * @param yPos y position of image.
     */
    public void PlaceImage(MazeImage image, int xPos, int yPos) {
        int imageSizeX = image.getSizeX();
        int imageSizeY = image.getSizeY();
        boolean fitsX = (xPos >= 0 && xPos + imageSizeX <= sizeX);
        boolean fitsY = (yPos >= 0 && yPos + imageSizeY <= sizeY);
        if (fitsX && fitsY)
        {
            image.setX(xPos);
            image.setY(yPos);

            images.add(image);
            imageCells += imageSizeX * imageSizeY;
            for (int x = xPos; x < xPos + imageSizeX; x++) {
                for (int y = yPos; y < yPos + imageSizeY; y++) {
                    cells[x][y].setCoveredByImage(true);
                }
            }

            for (int x = xPos; x < xPos + imageSizeX - 1; x++) {
                for (int y = yPos; y < yPos + imageSizeY; y++) {
                    cells[x][y].RemoveWall(1);
                }
            }

            for (int x = xPos; x < xPos + imageSizeX; x++) {
                for (int y = yPos; y < yPos + imageSizeY - 1; y++) {
                    cells[x][y].RemoveWall(2);
                }
            }
        }
    }

    /**
     * Prints the maze to console.
     */
    public void Print() {
        for (int y = 0; y < sizeY; y++)
        {
            for (int x = 0; x < sizeX; x++)
            {
                System.out.format("1  %s  1  ", cells[x][y].getWalls()[0] ? "1" : " ");
            }
            System.out.println();
            for (int x = 0; x < sizeX; x++)
            {
                System.out.format("%s     %s  ", cells[x][y].getWalls()[3] ? "1" : " ", cells[x][y].getWalls()[1] ? "1" : " ");
            }
            System.out.println();
            for (int x = 0; x < sizeX; x++)
            {
                System.out.format("1  %s  1  ", cells[x][y].getWalls()[2] ? "1" : " ");
            }
            System.out.println();
        }
    }

    /**
     * Calculates the percentage of cells in maze covered by a solution.
     * @param solution Path solution.
     * @return Percentage of cells in maze covered by a solution.
     * TODO untested
     */
    public double SolutionPct(ArrayList<CellNode> solution) {
        return 1.0 * solution.size() / area;
    }

    /**
     * Calculates the percentage of cells in maze that are dead ends.
     * @return Percentage of cells in maze that are dead ends.
     * TODO untested
     */
    public double DeadEndPct() {
        // A cell is a dead end if it has only one opening
        int deadEndCount = 0;
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (cells[x][y].CountWalls() == 3) {
                    deadEndCount++;
                }
            }
        }
        return deadEndCount;
    }
}
