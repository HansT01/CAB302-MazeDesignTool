import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MazeTest {
    Maze testMaze;
    Cell testCell1;
    Cell testCell2;
    Cell testCell3;

    @BeforeEach
    public void ConstructMaze() {
        testMaze = new Maze("Maze title", "Maze author", 3, 1);
        testCell1 = testMaze.getCells()[0][0];
        testCell2 = testMaze.getCells()[1][0];
        testCell3 = testMaze.getCells()[2][0];
    }
    @Test
    public void MazeDetails() {
        assert (testMaze.getAuthor() == "Maze author") : testMaze.getAuthor() + " does not match " + "Maze author";
        assert (testMaze.getTitle() == "Maze title") : testMaze.getTitle() + " does not match " + "Maze title";
        assert (testMaze.getSizeX() == 3) : "Test maze of x size 3 does not match";
        assert (testMaze.getSizeY() == 1) : "Test maze of y size 1 does not match";
    }
    @Test
    public void ImageTooBig() {
        MazeImage testImage = new MazeImage(new BufferedImage(1, 1, 1), 2, 2);
        assertThrows(Exception.class, () -> {
        });}
    @Test
    public void ImageBadIndex() {
        MazeImage testImage = new MazeImage(new BufferedImage(1, 1, 1), 1, 1);
        assertThrows(Exception.class, () -> {
        });
    }
    @Test
    public void DateLastEdited() {}
    @Test
    public void DateCreated() {}
    @Test
    public void Area() {
        assert (testMaze.getArea() == testMaze.getSizeX() * testMaze.getSizeY()) : "Area does not match X * Y";
    }
    @Test
    public void NegativeDimensions() {
        assertThrows(Exception.class, () -> {
            testMaze = new Maze("Maze title", "Maze author", -1, 1);
        });
        assertThrows(Exception.class, () -> {
            testMaze = new Maze("Maze title", "Maze author", 1, -1);
        });
    }
    @Test
    public void GenerateZeroDimensions() {
        assertThrows(Exception.class, () -> {
            testMaze = new Maze("Maze title", "Maze author", 0, 1);
        });
        assertThrows(Exception.class, () -> {
            testMaze = new Maze("Maze title", "Maze author", 1, 0);
        });
    }
    @Test
    public void Generate1000x1000() {
        testMaze = new Maze("Maze title", "Maze author", 1000, 1000);
        assert(testMaze.getSizeX() == 1000) : "X size " + testMaze.getSizeX() + " is not 1000";
        assert(testMaze.getSizeY() == 1000) : "Y size " + testMaze.getSizeY() + " is not 1000";
    }
    @Test
    public void GenerateTwice() {
        testMaze.GenerateMaze();
        testMaze.GenerateMaze();
    }
    @Test
    public void SolveSameStartEnd() {
        testMaze.GenerateMaze();
        testMaze.setStartCell(0, 0);
        testMaze.setEndCell(0, 0);
        testMaze.Solve();
    }
    @Test
    public void SolveBadIndex() {
        testMaze.GenerateMaze();
        testMaze.setStartCell(-1, 0);
        assertThrows(Exception.class, () -> {
            testMaze.Solve();
        });
    }
    @Test
    public void SolveNoSolution() {
        assert (testMaze.Solve() == null) : "Solution " + testMaze.Solve() + " is not null";
    }
    @Test
    public void GenerateWithImage() {}
    @Test
    public void GenerateWithTwoImages() {}
    @Test
    public void PlaceImage() {}
    @Test
    public void SolutionPct() {
        testMaze.GenerateMaze();
        double solutionPct = testMaze.SolutionPct(testMaze.Solve());
        assert (solutionPct == 1) : "Solution percentage " + solutionPct + " does not match 100%";
    }
    @Test
    public void DeadEndPct() {
        testMaze = new Maze("Maze title", "Maze author", 4, 1);
        testMaze.GenerateMaze();
        assert (testMaze.DeadEndPct() == 0.5) : "Dead end percentage " + testMaze.DeadEndPct() + " does not match 0.5";
    }
}
