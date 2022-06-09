package Database;

import Maze.*;
import Pages.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;

public class DatabaseTest {
    Statement statement;
    JDBCDataSource data;

    @BeforeEach
    public void CreateDatabase() throws SQLException {
        statement = DBConnection.getInstance().createStatement();
        data = new JDBCDataSource("cab302test");
    }

    @AfterEach
    public void DropTable() throws SQLException {
        statement.execute("DROP DATABASE IF EXISTS cab302test;");
    }

    @Test
    public void AddMaze() throws MazeException, IOException {
        Maze testMaze = new Maze("test-maze-title", "test-maze-author", 10,10, 16);
        byte[] ba = Maze.MazeToByteArray(testMaze);

        // Add maze to database
        int id = data.addMaze(testMaze);
        assert (id != 0) : "Problem has occurred in SQL query";

        // Retrieve maze from database
        Maze testMaze2 = data.getMaze(id);
        byte[] ba2 = Maze.MazeToByteArray(testMaze2);

        assert(Arrays.equals(ba, ba2)) : "Byte arrays do not match";
    }

    @Test
    public void DeleteMaze() throws MazeException, IOException {
        Maze testMaze = new Maze("test-maze-title", "test-maze-author", 10,10, 16);
        byte[] ba = Maze.MazeToByteArray(testMaze);

        // Add maze to database
        int id = data.addMaze(testMaze);
        assert (id != 0) : "Problem has occurred in SQL query";

        // Delete maze from database
        data.deleteMaze(id);

        // Retrieve deleted maze from database
        Maze testMaze2 = data.getMaze(id);
        assert (testMaze2 == null) : "Get maze should return null value";
    }


    @Test
    public void UpdateMaze() throws MazeException, IOException {
        Maze testMaze = new Maze("test-maze-title", "test-maze-author", 10,10, 16);
        byte[] ba = Maze.MazeToByteArray(testMaze);

        // Add maze to database
        int id = data.addMaze(testMaze);
        assert (id != 0) : "Problem has occurred in SQL query";

        // Update maze in database
        testMaze.GenerateMaze();
        data.updateMaze(id, testMaze);

        // Retrieve deleted maze from database
        Maze testMaze2 = data.getMaze(id);
        byte[] ba2 = Maze.MazeToByteArray(testMaze2);

        assert (!Arrays.equals(ba, ba2)) : "Byte arrays should no longer be equal";
    }
}
