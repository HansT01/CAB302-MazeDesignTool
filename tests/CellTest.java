import org.junit.jupiter.api.*;

public class CellTest {
    Maze testMaze;
    Cell testCell1;
    Cell testCell2;
    Cell testCell3;

    @BeforeEach
    public void ConstructMaze() throws MazeException {
        testMaze = new Maze("Maze title", "Maze author", 3, 1);
        testCell1 = testMaze.getCells()[0][0];
        testCell2 = testMaze.getCells()[1][0];
        testCell3 = testMaze.getCells()[2][0];
    }
    @Test
    public void DistanceTo() {
        double distance = testCell1.DistanceTo(testCell3);
        assert(distance == 2.0) : "Distance " + distance + " is not 2.0";
    }
    @Test
    public void HasAllWalls() {
        boolean[] cellWalls;
        cellWalls = testCell1.getWalls();
        for (int i = 0; i < cellWalls.length; i++) {
            assert (cellWalls[i]) : "Wall index " + i + " is not true";
        }
        cellWalls = testCell2.getWalls();
        for (int i = 0; i < cellWalls.length; i++) {
            assert (cellWalls[i]) : "Wall index " + i + " is not true";
        }
        cellWalls = testCell3.getWalls();
        for (int i = 0; i < cellWalls.length; i++) {
            assert (cellWalls[i]) : "Wall index " + i + " is not true";
        }
    }
    @Test
    public void GetClosedNeighboursNone() throws MazeException {
        testMaze.GenerateMaze();
        assert(testCell1.GetClosedNeighbours().size() == 0) : "Test cell 1 should not have a closed neighbour";
        assert(testCell2.GetClosedNeighbours().size() == 0) : "Test cell 2 should not have a closed neighbour";
        assert(testCell3.GetClosedNeighbours().size() == 0) : "Test cell 3 should not have a closed neighbour";
    }
    @Test
    public void GetClosedNeighboursOne() {
        assert(testCell1.GetClosedNeighbours().size() == 1) : "Test cell 1 should have 1 closed neighbour";
        assert(testCell3.GetClosedNeighbours().size() == 1) : "Test cell 3 should have 1 closed neighbour";
    }
    @Test
    public void GetClosedNeighboursTwo() {
        assert(testCell2.GetClosedNeighbours().size() == 2) : "Test cell 1 should have 2 closed neighbour";
    }
    @Test
    public void GetOpenNeighboursNone() {
        assert(testCell1.GetOpenNeighbours().size() == 0) : "Test cell 1 should not have an open neighbour";
        assert(testCell2.GetOpenNeighbours().size() == 0) : "Test cell 2 should not have an open neighbour";
        assert(testCell3.GetOpenNeighbours().size() == 0) : "Test cell 3 should not have an open neighbour";
    }
    @Test
    public void GetOpenNeighboursOne() throws MazeException {
        testMaze.GenerateMaze();
        assert(testCell1.GetOpenNeighbours().size() == 1) : "Test cell 1 should have 1 open neighbour";
        assert(testCell3.GetOpenNeighbours().size() == 1) : "Test cell 3 should have 1 open neighbour";
    }
    @Test
    public void GetOpenNeighboursTwo() throws MazeException {
        testMaze.GenerateMaze();
        assert(testCell2.GetOpenNeighbours().size() == 2) : "Test cell 2 should have 2 open neighbours";
    }
    @Test
    public void RemoveWallX() {
        testCell1.RemoveWall(1);
        assert(!testCell1.getWalls()[1]) : "Test cell 1 should have its east wall taken down";
        assert(!testCell2.getWalls()[3]) : "Test cell 3 should have its west wall taken down";
    }
    @Test
    public void RemoveWallY() throws MazeException {
        testMaze = new Maze("Maze title", "Maze author", 1, 3);
        testCell1 = testMaze.getCells()[0][0];
        testCell2 = testMaze.getCells()[0][1];
        testCell3 = testMaze.getCells()[0][2];
        testCell1.RemoveWall(2);
        assert(!testCell1.getWalls()[2]) : "Test cell 1 should have its south wall taken down";
        assert(!testCell2.getWalls()[0]) : "Test cell 3 should have its north wall taken down";
    }
}
