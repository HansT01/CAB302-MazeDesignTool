package Database;

import java.sql.*;

public class JDBCDataSource implements DBDataSource {

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS mazeStorage ("
                    + "title VARCHAR(50),"
                    + "author VARCHAR(50),"
                    + "dateCreated INT,"
                    + "dateLastEdited INT,"
                    + "sizeX INT,"
                    + "sizeY INT,"
                    + "cellSize INT,"
                    + "serialization MEDIUMBLOB"+ ");";


    private Connection connection;
    private PreparedStatement getData;

    public JDBCDataSource() {
        connection = DBConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute("CREATE DATABASE IF NOT EXISTS cab302;");
            st.execute("use cab302;");
            st.execute(CREATE_TABLE);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(JDBCDataSource args) {
    }


}