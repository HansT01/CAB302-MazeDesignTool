package Database;

import Maze.*;
import Pages.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.*;

public class DatabaseTest {
    Connection connection;
    Statement statement;

    @BeforeEach
    public void CreateDatabase() throws SQLException, MazeException {
        connection = DBConnection.getInstance();
        statement = connection.createStatement();
        statement.execute("CREATE DATABASE IF NOT EXISTS cab302test;");
        statement.execute("USE cab302test;");
        JDBCDataSource.main(new JDBCDataSource());
    }

    @AfterEach
    public void DropTable() throws SQLException {
        statement.execute("DROP DATABASE IF EXISTS cab302test;");
    }

    @Test
    public void AddMaze() throws MazeException, IOException, SQLException {
        Maze testMaze = new Maze("test-maze-title", "test-maze-author", 10,10);
        byte[] ba = Maze.MazeToByteArray(testMaze);
        // TODO method to add maze row
        statement.execute(String.format("INSERT INTO `mazeStorage` (serialization)", ));


        // Get maze from database
        ResultSet rs = statement.executeQuery("SELECT * FROM mazeStorage");
        Blob b = rs.getBlob("serialization");
        byte[] ba2 = b.getBytes(1, (int) b.length());

        assert(ba == ba2) : "Byte arrays do not match";
    }
}
