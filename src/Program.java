import java.util.ArrayList;
import java.util.PriorityQueue;

public class Program {
    public static void main(String[] args)
    {
        System.out.println();

        int sizeX = 10;
        int sizeY = 5;
        Maze testMaze = new Maze(sizeX, sizeY);
        testMaze.Print();

        System.out.println();

        testMaze.GenerateMaze();
        testMaze.Print();

        System.out.println();

        ArrayList<CellNode> solution = testMaze.Solve(0, 0, sizeX - 1, 0);
        for (CellNode node:solution)
        {
            System.out.print(node.toString() + " ");
        }
        System.out.println();

    }
}
