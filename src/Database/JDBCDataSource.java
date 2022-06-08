package Database;

import java.sql.*;

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