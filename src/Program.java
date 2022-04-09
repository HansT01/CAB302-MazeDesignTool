import java.util.ArrayList;

public class Program {
    public static void main(String[] args)
    {
        int sizeX = 10;
        int sizeY = 6;
        long startTime;
        long endTime;

        startTime = System.nanoTime();
        Maze testMaze = new Maze(sizeX, sizeY);
        endTime = System.nanoTime();
        long constructTime = endTime - startTime;

        testMaze.Print();
        System.out.println();

        startTime = System.nanoTime();
        testMaze.GenerateMaze();
        endTime = System.nanoTime();
        long generateTime = endTime - startTime;

        testMaze.Print();
        System.out.println();

        startTime = System.nanoTime();
        ArrayList<CellNode> solution = testMaze.Solve(0, 0, sizeX - 1, 0);
        endTime = System.nanoTime();
        long solveTime = endTime - startTime;

        for (CellNode node:solution)
        {
            System.out.print(node.toString() + " ");
        }
        System.out.println();
        System.out.println();

        System.out.format("Construction time: %f\n", constructTime/1000000.0);
        System.out.format("Generation time: %f\n", generateTime/1000000.0);
        System.out.format("Solve time: %f\n", solveTime/1000000.0);

    }
}
