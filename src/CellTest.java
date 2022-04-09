import org.junit.jupiter.api.*;

public class CellTest {
    @BeforeEach
    public void ConstructMaze() {
        Maze m1 = new Maze(3, 1);
        Cell c1 = m1.getCells()[0][0];
        Cell c2 = m1.getCells()[1][0];
        Cell c3 = m1.getCells()[2][0];
    }
    public void TestDistanceTo() {}
    public void TestHasAllWalls() {}
    public void TestGetClosedNeighboursNone() {}
    public void TestGetClosedNeighboursOne() {}
    public void TestGetClosedNeighboursTwo() {}
    public void TestGetOpenNeighboursNone() {}
    public void TestGetOpenNeighboursOne() {}
    public void TestGetOpenNeighboursTwo() {}
    public void TestRemoveWallX() {}
    public void TestRemoveWallY() {}
}
