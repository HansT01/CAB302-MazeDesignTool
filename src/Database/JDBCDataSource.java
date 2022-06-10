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

public class JDBCDataSource {
    public static final String CREATE_MAZE_TABLE =
            "CREATE TABLE IF NOT EXISTS mazeStorage ("
                    + "title VARCHAR(50) ,"
                    + "author VARCHAR(50) ,"
                    + "dateCreated DATETIME ,"
                    + "dateLastEdited DATETIME ,"
                    + "sizeX INT ,"
                    + "sizeY INT ,"
                    + "cellSize INT ,"
                    + "serialization MEDIUMBLOB NOT NULL,"
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
    private static final String GET_MAZES_BY_USER = "SELECT * FROM mazeStorage WHERE author = ?;";

    private static final String GET_HASH = "SELECT hash FROM users WHERE username = ?";
    private static final String INSERT_USER = "INSERT INTO users (username, hash) VALUES (?, ?);";
    private static final String DELETE_USER = "DELETE FROM users WHERE username = ?;";


    private PreparedStatement addMaze;
    private PreparedStatement deleteMaze;
    private PreparedStatement updateMaze;
    private PreparedStatement getMazeByID;
    private PreparedStatement getUserMazes;

    private PreparedStatement getHash;
    private PreparedStatement addUser;
    private PreparedStatement deleteUser;


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
            getUserMazes = connection.prepareStatement(GET_MAZES_BY_USER);

            getHash = connection.prepareStatement(GET_HASH);
            addUser = connection.prepareStatement(INSERT_USER);
            deleteUser = connection.prepareStatement(DELETE_USER);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int AddMaze(Maze maze) {
        try {
            addMaze.setString(1, maze.getTitle());
            addMaze.setString(2, maze.getAuthor());
            addMaze.setDate(3, new java.sql.Date(maze.getDateCreated().getTime()));
            addMaze.setDate(4, new java.sql.Date(maze.getDateLastEdited().getTime()));
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

    public void DeleteMaze(int id) {
        try {
            deleteMaze.setInt(1, id);
            deleteMaze.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void UpdateMaze(int id, Maze maze) {
        try {
            updateMaze.setString(1, maze.getTitle());
            updateMaze.setString(2, maze.getAuthor());
            updateMaze.setDate(3, new java.sql.Date(maze.getDateCreated().getTime()));
            updateMaze.setDate(4, new java.sql.Date(maze.getDateLastEdited().getTime()));
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

    public ResultSet GetUserMazes() {
        if (!VerifyUser()) {
            return null;
        }
        try {
            getUserMazes.setString(1, DBConnection.getUsername());
            return getUserMazes.executeQuery();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

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

    public void DeleteUser(String username) {
        try {
            deleteUser.setString(1, username);
            deleteUser.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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