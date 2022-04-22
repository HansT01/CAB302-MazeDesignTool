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

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
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
     * Setter for the start cell.
     * @param startX x location of the start cell.
     * @param startY y location of the start cell.
     */
    public void setStartCell(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
    }

    /**
     * Setter for the end cell.
     * @param endX x location of the end cell.
     * @param endY y location of the end cell.
     */
    public void setEndCell(int endX, int endY) {
        this.endX = endX;
        this.endY = endY;
    }

    /**
     * Clears the maze to a state ready for the GenerateMaze method.
     */
    public void ClearMaze() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                cells[x][y].setWalls(new boolean[] {true, true, true, true});
            }
        }
    }

    /**
     * Generates a maze.
     */
    public void GenerateMaze() {
        /*
        Random DFS maze pseudocode:
        Modified from https://en.wikipedia.org/wiki/Depth-first_search

        let S be a stack
        let start be the starting cell
        S.push(start)
        while S is not empty do
            c = S.peek()
            for a random unmarked neighbour n of c
                mark n as discovered
                break wall between c and n
                S.push(n)
            else if none is found
                S.pop()
         */

        // Failing precondition
        if (cells[startX][startY].isCoveredByImage()) {
            throw new RuntimeException();
        }
        Random r = new Random();

        // let S be a stack
        // let start be the starting cell
        // S.push(start)
        Stack<Cell> cellStack = new Stack<>();
        cellStack.push(cells[startX][startY]);

        // while S is not empty do
        while (!cellStack.empty()) {
            // c = S.peek()
            Cell currentCell = cellStack.peek();

            // for a random unmarked neighbour n of c
            ArrayList<Cell> neighbourCells = currentCell.GetClosedNeighbours();
            if (neighbourCells.size() != 0)
            {
                Cell neighbourCell = neighbourCells.get(r.nextInt(neighbourCells.size()));

                // mark n as discovered
                // break wall between c and n

                // Positive x direction is EAST, negative x direction is WEST.
                // Positive y direction is SOUTH, negative y direction is NORTH.
                int xDiff = neighbourCell.getX() - currentCell.getX();
                int yDiff = neighbourCell.getY() - currentCell.getY();

                // Break down the walls between the current and neighbour cell.
                if (yDiff < 0) currentCell.RemoveWall(0);
                else if (xDiff > 0) currentCell.RemoveWall(1);
                else if (yDiff > 0) currentCell.RemoveWall(2);
                else currentCell.RemoveWall(3);

                // S.push(n)
                cellStack.add(neighbourCell);
            }
            // else if none is found
            else {
                // S.pop()
                cellStack.pop();
            }
        }
    }

    /**
     * Solves maze from the startX, startY to endX, endY using the AStar algorithm.
     * @return
     * An array of the cell path from startCell to endCell.
     * An empty Cell array if no path was found.
     */
    public ArrayList<CellNode> Solve() {
        /*
        AStar pathfinding pseudocode:
        Modified from https://en.wikipedia.org/wiki/A*_search_algorithm

        let P be a priority queue sorted by cost
        let M be an unordered map to mark visited cells
        let start be the start cell
        let end be the end cell

        P.push(start)
        while P is not empty do
            c = P.pop()
            M(n) = true
            if c == end
                return path from start
            for each unmarked neighbour n of c
                pathCost = c.pathCost + 1
                if pathCost < n.pathCost
                    n.pathCost = pathCost
                    n.combinedCost = pathCost + distanceTo(n, end)
                    n.parent = c
                P.push(n)
        return null
        */

        // let P be a priority queue sorted by cost
        // let M be an unordered map to mark visited cells
        HashMap<String, Boolean> visitedNodes = new HashMap<>();
        PriorityQueue<CellNode> priorityQueue = new PriorityQueue<>();

        // let start be the start cell
        // let end be the end cell
        Cell startCell = cells[startX][startY];
        Cell endCell = cells[endX][endY];
        CellNode startNode = new CellNode(startCell);
        startNode.setParent(startNode);
        startNode.setPathCost(0);

        // P.push(start)
        priorityQueue.add(startNode);

        // while P is not empty do
        while (!priorityQueue.isEmpty()) {
            // c = P.pop()
            CellNode currentNode = priorityQueue.poll();

            // M(n) = true
            visitedNodes.put(currentNode.toString(), true);

            // if c == end
            if (currentNode.getCell() == endCell) {
                // return path from start
                ArrayList<CellNode> solution = new ArrayList<>();
                while (currentNode.getCell() != startCell) {
                    solution.add(currentNode);
                    currentNode = currentNode.getParent();
                }
                solution.add(currentNode);
                Collections.reverse(solution);
                return solution;
            }

            // for each unmarked neighbour n of c
            ArrayList<CellNode> neighbourNodes = new ArrayList<>();
            for (Cell neighbourCell : currentNode.getCell().GetOpenNeighbours()) {
                neighbourNodes.add(new CellNode(neighbourCell));
            }
            for (CellNode neighbourNode : neighbourNodes) {
                if (!visitedNodes.containsKey(neighbourNode.toString())) {
                    // pathCost = c.pathCost + 1
                    int pathCost = currentNode.getPathCost() + 1;

                    // if pathCost < n.pathCost
                    if (pathCost < neighbourNode.getPathCost()) {
                        // n.pathCost = pathCost
                        // n.combinedCost = pathCost + distanceTo(n, end)
                        // n.parent = c
                        neighbourNode.setPathCost(pathCost);
                        neighbourNode.setCombinedCost(pathCost + neighbourNode.getCell().DistanceTo(endCell));
                        neighbourNode.setParent(currentNode);
                    }
                    // P.push(n)
                    priorityQueue.add(neighbourNode);
                }
            }
        }
        // return null
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
     */
    public double SolutionPct(CellNode[] solution) {
        if (solution != null) {
            return 1.0 * solution.length / area;
        }
        return 0.0;
    }

    /**
     * Calculates the percentage of cells in maze that are dead ends.
     * @return Percentage of cells in maze that are dead ends.
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

    public static void main(String[] args) {
        long startTime;
        long endTime;

        startTime = System.nanoTime();
        Maze testMaze = new Maze(20, 10);
        endTime = System.nanoTime();
        long constructTime = endTime - startTime;

        MazeImage testImage = new MazeImage(0, 3, 3);
        testMaze.PlaceImage(testImage, 2, 2);

        startTime = System.nanoTime();
        testMaze.GenerateMaze();
        endTime = System.nanoTime();
        long generateTime = endTime - startTime;

        testMaze.Print();
        System.out.println();

        startTime = System.nanoTime();
        ArrayList<CellNode> solution = testMaze.Solve();
        endTime = System.nanoTime();
        long solveTime = endTime - startTime;

        for (CellNode node:solution) {
            System.out.print(node.toString() + " ");
        }
        System.out.println();
        System.out.println();

        System.out.format("Construction time (ms): %f\n", constructTime/1000000.0);
        System.out.format("Generation time (ms): %f\n", generateTime/1000000.0);
        System.out.format("Solve time (ms): %f\n", solveTime/1000000.0);
    }
}
