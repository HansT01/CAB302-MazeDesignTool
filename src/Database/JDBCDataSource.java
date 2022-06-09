package Database;

import Maze.Maze;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;

public class JDBCDataSource implements DBDataSource {
    public static final String CREATE_MAZE_TABLE =
            "CREATE TABLE IF NOT EXISTS mazeStorage ("
                    + "title VARCHAR(50) ,"
                    + "author VARCHAR(50) ,"
                    + "dateCreated LONG ,"
                    + "dateLastEdited LONG ,"
                    + "sizeX INT ,"
                    + "sizeY INT ,"
                    + "cellSize INT ,"
                    + "serialization MEDIUMBLOB NOT NULL,"
                    + "id int NOT NULL AUTO_INCREMENT,"
                    + "PRIMARY KEY (id)"
                    + ");";
    public static final String CREATE_USERS_TABLE =
            "CREATE TABLE IF NOT EXISTS user (" +
            "username varchar(255) NOT NULL," +
            "password varchar(255) NOT NULL," +
            "PRIMARY KEY (`name`));";
    private static final String INSERT_MAZE =
            "INSERT INTO mazeStorage " +
            "(title, author, dateCreated, dateLastEdited, sizeX, sizeY, cellSize, serialization) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String DELETE_MAZE = "DELETE FROM mazeStorage WHERE id = ?;";
    private static final String UPDATE_MAZE =
            "UPDATE mazeStorage SET " +
            "title = ?," +
            "author = ?," +
            "dateCreated = ?," +
            "dateLastEdited = ?," +
            "sizeX = ?," +
            "sizeY = ?," +
            "cellSize = ?," +
            "serialization = ?" +
            "WHERE id = ?;";
    private static final String GET_MAZE_BY_ID = "SELECT * FROM mazeStorage WHERE id = ?;";
    private static final String GET_ALL_MAZES = "SELECT * FROM mazeStorage;";
    private static final String GET_USERNAME_PASSWORD = "SELECT name, password FROM user WHERE name=? and password=?;";


    private PreparedStatement addMaze;
    private PreparedStatement deleteMaze;
    private PreparedStatement updateMaze;
    private PreparedStatement getMazeByID;
    private PreparedStatement getAllMazes;
    private PreparedStatement getUser;


    private Connection connection;

    /**
     * Default constructor using database named cab302.
     */
    public JDBCDataSource() {
        SetupConnection("cab302");
    }

    /**
     * Alternative constructor for custom database name
     * @param database Name of new database
     */
    public JDBCDataSource(String database) {
        SetupConnection(database);
    }

    /**
     * Sets up connection with database. Will create database if it does not exist.
     * @param database Input database name
     */
    public void SetupConnection(String database) {
        connection = DBConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute("CREATE DATABASE IF NOT EXISTS " + database + ";");
            st.execute("use " + database + ";");

            st.execute(CREATE_MAZE_TABLE);
            st.execute(CREATE_USERS_TABLE);

            addMaze = connection.prepareStatement(INSERT_MAZE, Statement.RETURN_GENERATED_KEYS);
            deleteMaze = connection.prepareStatement(DELETE_MAZE);
            updateMaze = connection.prepareStatement(UPDATE_MAZE);
            getMazeByID = connection.prepareStatement(GET_MAZE_BY_ID);
            getAllMazes = connection.prepareStatement(GET_ALL_MAZES);
            getUser = connection.prepareStatement(GET_USERNAME_PASSWORD);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int addMaze(Maze maze) {
        try {
            addMaze.setString(1, maze.getTitle());
            addMaze.setString(2, maze.getAuthor());
            addMaze.setLong(3, maze.getDateCreated().getTime());
            addMaze.setLong(4, maze.getDateLastEdited().getTime());
            addMaze.setInt(5, maze.getSizeX());
            addMaze.setInt(6, maze.getSizeY());
            addMaze.setInt(7, maze.getCellSize());
            addMaze.setBinaryStream(8, new ByteArrayInputStream(Maze.MazeToByteArray(maze)));
            addMaze.execute();

            ResultSet rs = addMaze.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteMaze(int id) {
        try {
            deleteMaze.setInt(1, id);
            deleteMaze.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMaze(int id, Maze maze) {
        try {
            updateMaze.setString(1, maze.getTitle());
            updateMaze.setString(2, maze.getAuthor());
            updateMaze.setLong(3, maze.getDateCreated().getTime());
            updateMaze.setLong(4, maze.getDateLastEdited().getTime());
            updateMaze.setInt(5, maze.getSizeX());
            updateMaze.setInt(6, maze.getSizeY());
            updateMaze.setInt(7, maze.getCellSize());
            updateMaze.setBinaryStream(8, new ByteArrayInputStream(Maze.MazeToByteArray(maze)));
            updateMaze.setInt(9, id);
            updateMaze.execute();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public Maze getMaze(int id) {
        try {
            getMazeByID.setInt(1, id);
            ResultSet rs = getMazeByID.executeQuery();
            rs.beforeFirst();
            if (rs.next()) {
                Blob b = rs.getBlob("serialization");
                byte[] ba = b.getBytes(1, (int) b.length());
                return Maze.ByteArrayToMaze(ba);
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet GetAllDocuments() {
        try {
            return getAllMazes.executeQuery();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean VerifyUser() {
        try {
            getUser.setString(1, DBConnection.getUsername());
            getUser.setString(2, DBConnection.getPassword());
            ResultSet rs = getUser.executeQuery();
            rs.beforeFirst();
            if (rs.next()) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void main(JDBCDataSource args) {
    }
}