import java.util.ArrayList;

public class Program {
    public static void main(String[] args)
    {
        int sizeX = 20;
        int sizeY = 10;
        long startTime;
        long endTime;

        startTime = System.nanoTime();
        Maze testMaze = new Maze(sizeX, sizeY);
        endTime = System.nanoTime();
        long constructTime = endTime - startTime;

        MazeImage testImage = new MazeImage(0, 3, 3);
        testMaze.PlaceImage(testImage, 2, 2);

        testMaze.Print();
        System.out.println();

        startTime = System.nanoTime();
        testMaze.GenerateMaze();
        endTime = System.nanoTime();
        long generateTime = endTime - startTime;

        testMaze.Print();
        System.out.println();

        startTime = System.nanoTime();
        CellNode[] solution = testMaze.Solve(0, 0, sizeX - 1, sizeY - 1);
        endTime = System.nanoTime();
        long solveTime = endTime - startTime;

        for (CellNode node:solution)
        {
            System.out.print(node.toString() + " ");
        }
        System.out.println();
        System.out.println();

        System.out.format("Construction time (ms): %f\n", constructTime/1000000.0);
        System.out.format("Generation time (ms): %f\n", generateTime/1000000.0);
        System.out.format("Solve time (ms): %f\n", solveTime/1000000.0);

    }
}
