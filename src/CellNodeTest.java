import org.junit.jupiter.api.*;

public class CellNodeTest {
    Maze testMaze;
    Cell testCell1;
    Cell testCell2;
    Cell testCell3;
    CellNode testNode1;
    CellNode testNode2;
    CellNode testNode3;

    @BeforeEach
    public void ConstructMaze() {
        testMaze = new Maze("Maze title", "Maze author", 3, 1);
        testCell1 = testMaze.getCells()[0][0];
        testCell2 = testMaze.getCells()[1][0];
        testCell3 = testMaze.getCells()[2][0];
        testNode1 = new CellNode(testCell1);
        testNode2 = new CellNode(testCell2);
        testNode3 = new CellNode(testCell3);
    }
    @Test
    public void DefaultFields() {
        assert (testNode1.getCell() == testCell1) : "Cell node 1 should have cell 1 in its fields";
        assert (testNode1.getParent() == null) : "Cell node 1 should not have a null parent node";
        assert (testNode1.getCombinedCost() == Double.MAX_VALUE) : "Combined cost " + testNode1.getCombinedCost() + " should be " + Double.MAX_VALUE;
        assert (testNode1.getPathCost() == Integer.MAX_VALUE) : "Path cost " + testNode1.getPathCost() + " should be " + Integer.MAX_VALUE;
    }
    @Test
    public void CompareTo() {
        testNode1.setCombinedCost(1);
        testNode2.setCombinedCost(10);
        assert (testNode1.compareTo(testNode2) == -1) : "A lower combined cost should have -1 compareTo value";
        assert (testNode2.compareTo(testNode1) == 1) : "A higher combined cost should have 1 compareTo value";
        assert (testNode1.compareTo(testNode1) == 0) : "The same combined cost should have 0 compareTo value";
    }
}
