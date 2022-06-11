import Database.DBConnection;
import Database.JDBCDataSource;
import Maze.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Database.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
        int id = data.AddMaze(testMaze);
        assert (id != 0) : "Problem has occurred in SQL query";

        // Retrieve maze from database
        Maze testMaze2 = data.GetMaze(id);
        byte[] ba2 = Maze.MazeToByteArray(testMaze2);

        assert(Arrays.equals(ba, ba2)) : "Byte arrays do not match";
    }

    @Test
    public void DeleteMaze() throws MazeException, IOException {
        Maze testMaze = new Maze("test-maze-title", "test-maze-author", 10,10, 16);
        byte[] ba = Maze.MazeToByteArray(testMaze);

        // Add maze to database
        int id = data.AddMaze(testMaze);
        assert (id != 0) : "Problem has occurred in SQL query";

        // Delete maze from database
        data.DeleteMaze(id);

        // Retrieve deleted maze from database
        Maze testMaze2 = data.GetMaze(id);
        assert (testMaze2 == null) : "Get maze should return null value";
    }


    @Test
    public void UpdateMaze() throws MazeException, IOException {
        Maze testMaze = new Maze("test-maze-title", "test-maze-author", 10,10, 16);
        byte[] ba = Maze.MazeToByteArray(testMaze);

        // Add maze to database
        int id = data.AddMaze(testMaze);
        assert (id != 0) : "Problem has occurred in SQL query";

        // Update maze in database
        testMaze.GenerateMaze();
        data.UpdateMaze(id, testMaze);

        // Retrieve deleted maze from database
        Maze testMaze2 = data.GetMaze(id);
        byte[] ba2 = Maze.MazeToByteArray(testMaze2);

        assert (!Arrays.equals(ba, ba2)) : "Byte arrays should no longer be equal";
    }

    @Test
    public void AddUser() throws NoSuchAlgorithmException, InvalidKeySpecException {
        DBConnection.setUsername("username");
        DBConnection.setPassword("password");

        data.AddUser();
        assert (data.VerifyUser()) : "User is unverified";
    }

    @Test
    public void PasswordHash() throws NoSuchAlgorithmException, InvalidKeySpecException {
        DBConnection.setUsername("username");
        DBConnection.setPassword("password");

        String hash = data.HashString(DBConnection.getPassword());

        assert (data.MatchHash(DBConnection.getPassword(), hash)) : "Hashes do not match";
    }

    @Test
    public void GetUserMaze() throws MazeException, IOException, SQLException {
        // Add user to database
        DBConnection.setUsername("USERNAME");
        DBConnection.setPassword("PASSWORD");
        data.AddUser();

        // Initialize test maze
        Maze testMaze = new Maze("test-maze-title", DBConnection.getUsername(), 10,10, 16);
        byte[] ba = Maze.MazeToByteArray(testMaze);

        // Add test maze to database
        data.AddMaze(testMaze);

        // Get user mazes
        ResultSet rs = data.GetUserMazes(false);
        rs.absolute(1);
        int id = rs.getInt("id");

        Maze maze = data.GetMaze(id);
        byte[] ba2 = Maze.MazeToByteArray(maze);

        assert (Arrays.equals(ba, ba2)) : "Byte arrays should be equal";
    }

    @Test
    public void GetAllMazes() throws IOException, MazeException, SQLException {
        DBConnection.setUsername("admin");
        DBConnection.setPassword("password");

        Maze testMaze = new Maze("test-maze-title", DBConnection.getUsername(), 10,10, 16);
        byte[] ba = Maze.MazeToByteArray(testMaze);
        int id1 = data.AddMaze(testMaze);

        ResultSet rs = data.GetUserMazes(false);
        rs.last();
        int id2 = rs.getInt("id");

        assert(id1 == id2) : "Maze id should be equal";
    }

    @Test
    public void ToggleCompleteMaze() throws MazeException, IOException, SQLException {
        // Add user to database
        DBConnection.setUsername("USERNAME");
        DBConnection.setPassword("PASSWORD");
        data.AddUser();

        // Initialize test maze
        Maze testMaze = new Maze("test-maze-title", DBConnection.getUsername(), 10,10, 16);

        // Add test maze to database
        int id = data.AddMaze(testMaze);

        // Get first complete value
        ResultSet rs1 = data.GetUserMazes(false);
        rs1.last();
        int c1 = rs1.getRow();

        // Get second complete value
        data.ToggleCompleteMaze(id);
        ResultSet rs2 = data.GetUserMazes(true);
        rs2.last();
        int c2 = rs2.getRow();

        // Get third complete value
        ResultSet rs3 = data.GetUserMazes(false);
        rs3.last();
        int c3 = rs3.getRow();

        assert (c1 == 1) : "There should be one maze in progress";
        assert (c2 == 1) : "There should be one complete maze";
        assert (c3 == 0) : "There should be no mazes in progress";
    }
}
