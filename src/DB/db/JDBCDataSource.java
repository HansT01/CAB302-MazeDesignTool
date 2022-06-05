package DB.db;

public class JDBCDataSource implements DBDataSource {

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS mazeStorage ("
                    + "author VARCHAR(50),"
                    + "dateCreated DATE,"
                    + "dateLastEdited DATE,"
                    + "width VARCHAR(5),"
                    + "height VARCHAR(5)" + ");";
}
