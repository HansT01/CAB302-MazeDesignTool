import org.junit.jupiter.api.*;

public class MazeTest {
    @BeforeEach
    public void ConstructMaze() {
        Maze m1 = new Maze(3, 1);
        Cell c1 = m1.getCells()[0][0];
        Cell c2 = m1.getCells()[1][0];
        Cell c3 = m1.getCells()[2][0];
    }
    public void TestMazeDetails() {}
    public void TestImageTooBig() {}
    public void TestImageBadIndex() {}
    public void TestDateLastEdited() {}
    public void TestDateCreated() {}
    public void TestArea() {}
    public void TestNegativeArea() {}
    public void TestGenerate0x0() {}
    public void TestGenerate1000x1000() {}
    public void TestGenerateTwice() {}
    public void TestSolveSameStartEnd() {}
    public void TestSolveBadIndex() {}
    public void TestSolveNoSolution() {}
    public void TestGenerateWithImage() {}
    public void TestGenerateWithTwoImages() {}
    public void TestPlaceImage() {}
    public void TestSolutionPct() {}
    public void TestDeadEndPct() {}
}
