package DB.db;

import java.sql.*;
import java.util.Set;
import java.util.TreeSet;

public class JDBCDataSource implements DBDataSource {

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS mazeStorage ("
                    + "author VARCHAR(50),"
                    + "dateCreated VARCHAR(50),"
                    + "dateLastEdited VARCHAR(50),"
                    + "sizeX VARCHAR(5),"
                    + "sizeY VARCHAR(5)" + ");";




    private Connection connection;
    private PreparedStatement getData;

    public JDBCDataSource() {
        connection = DBConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_TABLE);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new JDBCDataSource();
    }


}


