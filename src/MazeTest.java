import org.junit.jupiter.api.*;

public class MazeTest {
    @BeforeEach
    public void ConstructMaze() {
        Maze m1 = new Maze("Maze title", "Maze author", 3, 1);
        Cell c1 = m1.getCells()[0][0];
        Cell c2 = m1.getCells()[1][0];
        Cell c3 = m1.getCells()[2][0];
    }
    public void MazeDetails() {}
    public void ImageTooBig() {}
    public void ImageBadIndex() {}
    public void DateLastEdited() {}
    public void DateCreated() {}
    public void Area() {}
    public void NegativeArea() {}
    public void Generate0x0() {}
    public void Generate1000x1000() {}
    public void GenerateTwice() {}
    public void SolveSameStartEnd() {}
    public void SolveBadIndex() {}
    public void SolveNoSolution() {}
    public void GenerateWithImage() {}
    public void GenerateWithTwoImages() {}
    public void PlaceImage() {}
    public void SolutionPct() {}
    public void DeadEndPct() {}
}
