package Database;

import Maze.Maze;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Base64;

/**
 * JDBCDataSource is a class for handling all database queries.
 */
public class JDBCDataSource {
    public static final String CREATE_MAZE_TABLE =
            "CREATE TABLE IF NOT EXISTS mazeStorage ("
                    + "title VARCHAR(50) ,"
                    + "author VARCHAR(50) ,"
                    + "dateCreated TIMESTAMP ,"
                    + "dateLastEdited TIMESTAMP ,"
                    + "sizeX INT ,"
                    + "sizeY INT ,"
                    + "cellSize INT ,"
                    + "serialization MEDIUMBLOB NOT NULL,"
                    + "complete BOOLEAN,"
                    + "id int NOT NULL AUTO_INCREMENT,"
                    + "PRIMARY KEY (id)"
                    + ");";
    public static final String CREATE_USERS_TABLE =
            "CREATE TABLE IF NOT EXISTS users (" +
            "username varchar(255) NOT NULL," +
            "hash varchar(255) NOT NULL," +
            "PRIMARY KEY (`username`));";
    private static final String INSERT_MAZE =
            "INSERT INTO mazeStorage " +
            "(title, author, dateCreated, dateLastEdited, sizeX, sizeY, cellSize, serialization, complete) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, false);";
    private static final String DELETE_MAZE = "DELETE FROM mazeStorage WHERE id = ?;";
    private static final String TOGGLE_COMPLETE_MAZE = "UPDATE mazeStorage SET complete = !complete WHERE id = ? ;";
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
    private static final String GET_MAZE_BY_ID = "SELECT (serialization) FROM mazeStorage WHERE id = ?;";
    private static final String GET_MAZES_BY_USER = "SELECT title, author, dateCreated, dateLastEdited, sizeX, sizeY, cellSize, id " +
            "FROM mazeStorage " +
            "WHERE complete = ? AND author = ? " +
            "ORDER BY dateLastEdited DESC;";
    private static final String GET_ALL_MAZES = "SELECT title, author, dateCreated, dateLastEdited, sizeX, sizeY, cellSize, id " +
            "FROM mazeStorage " +
            "WHERE complete = ? " +
            "ORDER BY dateLastEdited DESC;";

    private static final String GET_HASH = "SELECT hash FROM users WHERE username = ?;";
    private static final String INSERT_USER = "INSERT INTO users (username, hash) VALUES (?, ?);";
    private static final String DELETE_USER = "DELETE FROM users WHERE username = ?;";


    private PreparedStatement addMaze;
    private PreparedStatement completeMaze;
    private PreparedStatement deleteMaze;
    private PreparedStatement updateMaze;
    private PreparedStatement getMazeByID;
    private PreparedStatement getUserMazes;
    private PreparedStatement getAllMazes;

    private PreparedStatement getHash;
    private PreparedStatement addUser;
    private PreparedStatement deleteUser;


    private Connection connection;

    /**
     * Default constructor using database schema from db.props
     */
    public JDBCDataSource() {
        connection = DBConnection.getInstance();
        SetupConnection(DBConnection.getSchema());
    }

    /**
     * Alternative constructor for custom database schema name
     * @param schema Name of new database
     */
    public JDBCDataSource(String schema) {
        connection = DBConnection.getInstance();
        SetupConnection(schema);
    }

    /**
     * Sets up connection with database. Will create database if it does not exist.
     * @param database Input database name
     */
    public void SetupConnection(String database) {
        try {
            Statement st = connection.createStatement();

            // Create databases and tables (if not exists)
            st.execute("CREATE DATABASE IF NOT EXISTS " + database + ";");
            st.execute("USE " + database + ";");
            st.execute(CREATE_MAZE_TABLE);
            st.execute(CREATE_USERS_TABLE);

            // Set up prepared statements
            addMaze = connection.prepareStatement(INSERT_MAZE, Statement.RETURN_GENERATED_KEYS);
            completeMaze = connection.prepareStatement(TOGGLE_COMPLETE_MAZE);
            deleteMaze = connection.prepareStatement(DELETE_MAZE);
            updateMaze = connection.prepareStatement(UPDATE_MAZE);
            getMazeByID = connection.prepareStatement(GET_MAZE_BY_ID);
            getUserMazes = connection.prepareStatement(GET_MAZES_BY_USER);
            getAllMazes = connection.prepareStatement(GET_ALL_MAZES);

            getHash = connection.prepareStatement(GET_HASH);
            addUser = connection.prepareStatement(INSERT_USER);
            deleteUser = connection.prepareStatement(DELETE_USER);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Adds given maze to the database.
     * @param maze Maze object
     * @return The unique id of the new maze
     */
    public int AddMaze(Maze maze) {
        try {
            addMaze.setString(1, maze.getTitle());
            addMaze.setString(2, maze.getAuthor());
            addMaze.setTimestamp(3, new java.sql.Timestamp(maze.getDateCreated().getTime()));
            addMaze.setTimestamp(4, new java.sql.Timestamp(maze.getDateLastEdited().getTime()));
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

    /**
     * Toggles the complete status of a maze with a given unique id.
     * @param id Unique id of the maze
     */
    public void ToggleCompleteMaze(int id) {
        try {
            completeMaze.setInt(1, id);
            completeMaze.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes maze from the database given a unique id.
     * @param id Unique id of the maze
     */
    public void DeleteMaze(int id) {
        try {
            deleteMaze.setInt(1, id);
            deleteMaze.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a maze in the database with a given unique id.
     * @param id Unique id of the maze
     * @param maze New maze to replace existing maze
     */
    public void UpdateMaze(int id, Maze maze) {
        try {
            updateMaze.setString(1, maze.getTitle());
            updateMaze.setString(2, maze.getAuthor());
            updateMaze.setTimestamp(3, new java.sql.Timestamp(maze.getDateCreated().getTime()));
            updateMaze.setTimestamp(4, new java.sql.Timestamp(maze.getDateLastEdited().getTime()));
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

    /**
     * Gets the maze object from the database with a given unique id.
     * @param id Unique id of the maze
     * @return the Maze object
     */
    public Maze GetMaze(int id) {
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

    /**
     * Gets all the mazes belonging to the current user specified in DBConnection.
     * The method will perform a verification first before making an SQL query.
     * @param complete Complete status of the returned mazes.
     * @return ResultSet of all the maze rows, without the Maze Blob.
     */
    public ResultSet GetUserMazes(boolean complete) {
        if (!VerifyUser()) {
            return null;
        }
        try {
            if (DBConnection.getUsername().equals("admin")) {
                getAllMazes.setBoolean(1, complete);
                return getAllMazes.executeQuery();
            } else {
                getUserMazes.setBoolean(1, complete);
                getUserMazes.setString(2, DBConnection.getUsername());
                return getUserMazes.executeQuery();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Adds the current user specified in DBConnection to the database.
     * @return True if user was successfully added.
     */
    public boolean AddUser() {
        try {
            String username = DBConnection.getUsername();
            String password = DBConnection.getPassword();
            String hash = HashString(password);

            addUser.setString(1, username);
            addUser.setString(2, hash);
            addUser.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes the user with the username parameter.
     * @param username Username string
     */
    public void DeleteUser(String username) {
        try {
            deleteUser.setString(1, username);
            deleteUser.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Compares the password specified in DBConnection with the password with the same username in the database.
     * @return True if matching hash.
     */
    public boolean VerifyUser() {
        try {
            getHash.setString(1, DBConnection.getUsername());
            ResultSet rs = getHash.executeQuery();
            rs.beforeFirst();
            if (rs.next()) {
                String hash = rs.getString(1);
                return MatchHash(DBConnection.getPassword(), hash);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Hashes a string using PBKDF2, with 16 bytes of salt
     * @param str String to be hashed
     * @return Encoded string with the salt appended before the hash.
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public String HashString(String str) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
        SecureRandom r = new SecureRandom();

        // Salt rounds
        byte[] salt = new byte[16];
        r.nextBytes(salt);

        // Generate byte array hash with salt rounds
        KeySpec spec = new PBEKeySpec(str.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = f.generateSecret(spec).getEncoded();

        // Convert hash back to string
        Base64.Encoder enc = Base64.getEncoder();
        return enc.encodeToString(salt) + ":" + enc.encodeToString(hash);
    }

    /**
     * Compares a string with a hash from the HashString method.
     * @param str String to compare
     * @param hash Hash to be compared with
     * @return True if string matches the hash
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public boolean MatchHash(String str, String hash) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Base64.Encoder enc = Base64.getEncoder();
        Base64.Decoder dec = Base64.getDecoder();

        // Split
        String[] hSplit = hash.split(":", 2);
        String salt = hSplit[0];
        String hash1 = hSplit[1];

        // Generate byte array hash with salt rounds
        KeySpec spec = new PBEKeySpec(str.toCharArray(), dec.decode(salt), 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] strHash = f.generateSecret(spec).getEncoded();

        String hash2 = enc.encodeToString(strHash);
        return hash1.equals(hash2);
    }

    public static void main(JDBCDataSource args) {
    }
}