import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MazeTest {
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
    public void MazeDetails() {
        assert (testMaze.getAuthor() == "Maze author") : testMaze.getAuthor() + " does not match " + "Maze author";
        assert (testMaze.getTitle() == "Maze title") : testMaze.getTitle() + " does not match " + "Maze title";
        assert (testMaze.getSizeX() == 3) : "Test maze of x size 3 does not match";
        assert (testMaze.getSizeY() == 1) : "Test maze of y size 1 does not match";
    }
    @Test
    public void ImageTooBig() {
        assertThrows(Exception.class, () -> {
            MazeImage mazeImage = new MazeImage(5, 5);
            testMaze.PlaceImage(0, 0, mazeImage);
        });}
    @Test
    public void DateLastEdited() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        testMaze.UpdateLastEdited();
        assert(testMaze.getDateLastEdited().getTime() > testMaze.getDateCreated().getTime()) : "Image last edited should be after date created";
    }
    @Test
    public void DateCreated() {
        Date now = new Date(System.currentTimeMillis());
        assert (now.getTime() - testMaze.getDateCreated().getTime() < 1000) : "Date created and now is not within 1 second";
    }
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
    public void Generate1000x1000() throws MazeException {
        testMaze = new Maze("Maze title", "Maze author", 1000, 1000);
        assert(testMaze.getSizeX() == 1000) : "X size " + testMaze.getSizeX() + " is not 1000";
        assert(testMaze.getSizeY() == 1000) : "Y size " + testMaze.getSizeY() + " is not 1000";
    }
    @Test
    public void GenerateTwice() throws MazeException {
        testMaze.GenerateMaze();
        testMaze.GenerateMaze();
    }
    @Test
    public void SolveSameStartEnd() throws MazeException {
        testMaze.GenerateMaze();
        testMaze.setStartCell(0, 0);
        testMaze.setEndCell(0, 0);
        testMaze.Solve();
    }
    @Test
    public void SolveBadIndex() throws MazeException {
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
    public void GenerateWithImage() throws MazeException {
        MazeImage mazeImage = new MazeImage(1, 1);
        testMaze.getImages().add(mazeImage);
        assert (testMaze.Solve() == null) : "Image should obstruct solution";
    }
    @Test
    public void GenerateWithStartAndEndImages() throws MazeException {
        MazeImage mazeImage = new MazeImage(1, 1);
        testMaze.setStartImage(mazeImage);
        testMaze.setEndImage(mazeImage);
        testMaze.GenerateMaze();
        assert (testMaze.Solve() != null) : "Start and end images should not obstruct solution";
    }
    @Test
    public void PlaceImage() throws MazeException {
        MazeImage mazeImage = new MazeImage(1, 1);
        testMaze.PlaceImage(1, 0, mazeImage);
        assert (testMaze.Solve() == null) : "Placed image should obstruct solution";}
    @Test
    public void SolutionPct() throws MazeException {
        testMaze.GenerateMaze();
        double solutionPct = testMaze.SolutionPct();
        assert (solutionPct == 1) : "Solution percentage " + solutionPct + " does not match 100%";
    }
    @Test
    public void DeadEndPct() throws MazeException {
        testMaze = new Maze("Maze title", "Maze author", 4, 1);
        testMaze.GenerateMaze();
        assert (testMaze.DeadEndPct() == 0.5) : "Dead end percentage " + testMaze.DeadEndPct() + " does not match 0.5";
    }
    @Test
    public void SerializeAndDeserializeMaze() throws IOException, ClassNotFoundException, MazeException {
        testMaze.GenerateMaze();
        Maze testMaze2 = Maze.ByteArrayToMaze(Maze.MazeToByteArray(testMaze));

        assert (testMaze != testMaze2) : "Maze objects should not have the same reference";
        assert (Objects.equals(testMaze.getTitle(), testMaze2.getTitle())) : testMaze2.getTitle() + " does not match " + testMaze.getTitle();
        assert (Objects.equals(testMaze.getAuthor(), testMaze2.getAuthor())) : testMaze2.getAuthor() + " does not match " + testMaze.getAuthor();
        assert (testMaze.getCells()[0][0] != testMaze2.getCells()[0][0]) : "Cell objects have the same reference";
        assert (Objects.equals(Arrays.toString(testMaze.Solve()), Arrays.toString(testMaze2.Solve()))) : "Solutions are not the same";
    }
}
