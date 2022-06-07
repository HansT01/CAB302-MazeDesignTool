package DB.db;

import java.sql.*;
import java.util.Set;
import java.util.TreeSet;

public class JDBCDataSource implements DBDataSource {

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS mazeStorage ("
                    + "author VARCHAR(50),"
                    + "dateCreated DATE,"
                    + "dateLastEdited DATE,"
                    + "sizeX VARCHAR(5),"
                    + "sizeY VARCHAR(5)" + ");";





    public Object[][] data;
    private Connection connection;
    private static final String GET_DATA = "SELECT * FROM mazeStorage";
    private PreparedStatement getData;

    public JDBCDataSource() {
        connection = DBConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_TABLE);
            getData = connection.prepareStatement(GET_DATA);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public Object[][] getData() throws SQLException {
        ResultSet rs = getData.executeQuery();;
        int i = 0;
        try {
            while (rs.next()) {
                int j = 0;
                data[i][j++] = rs.getString("author");
                data[i][j++] = rs.getDate("dateCreated");
                data[i][j++] = rs.getDate("dateLastEdited");
                data[i][j++] = rs.getInt("sizeX");
                data[i][j++] = rs.getInt("sizeY");

                i++;

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return data;
    }



}


