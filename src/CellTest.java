import org.junit.jupiter.api.*;

public class CellTest {
    @BeforeEach
    public void ConstructMaze() {
        Maze m1 = new Maze(3, 1);
        Cell c1 = m1.getCells()[0][0];
        Cell c2 = m1.getCells()[1][0];
        Cell c3 = m1.getCells()[2][0];
    }
    public void DistanceTo() {}
    public void HasAllWalls() {}
    public void GetClosedNeighboursNone() {}
    public void GetClosedNeighboursOne() {}
    public void GetClosedNeighboursTwo() {}
    public void GetOpenNeighboursNone() {}
    public void GetOpenNeighboursOne() {}
    public void GetOpenNeighboursTwo() {}
    public void RemoveWallX() {}
    public void RemoveWallY() {}
}
