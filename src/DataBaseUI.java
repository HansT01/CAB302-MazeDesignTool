import DB.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Constructs window for viewing and interacting with mazes stored in database
 */
public class DataBaseUI extends JFrame implements Runnable {

    /** Used for getting location of mouse pointer */
     Point openLocation = MouseInfo.getPointerInfo().getLocation();

    /** Value of column of cell clicked on */
    int selectedColumn;
    /** Value of row of cell clicked on */
    int selectedRow;
    /** Data in cell clicked on */
    String selectedCellValue;

    /** JButton used for New */
    JButton b1 = new JButton();
    /** JButton used for Edit */
    JButton b2 = new JButton();
    /** JButton used for Delete */
    JButton b3 = new JButton();
    /** JButton used for Export */
    JButton b4 = new JButton();
    /** JTable used for displaying maze data */
    JTable table;

    /** Constructs DataBase JFrame */
    private void createGUI() {
        // Adjusting window
        setLocation(openLocation); // Open window at location of mouse pointer
        setVisible(true);
        setTitle("Maze DataBase");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set so pressing x closes window not whole program
        setSize(500, 500);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        // Centre to screen
        setLocationRelativeTo(null);

        // Table
        table = mazeTable();
        listenerSetup();
        JScrollPane scroller = new JScrollPane(table);

        // Panel for buttons and whatnot
        JPanel p1 = new JPanel();
        p1.setLayout((new GridLayout(4,1)));
        // Buttons
        b1.setText("New");
        b2.setText("Edit");
        b3.setText("Delete");
        b4.setText("Export");
        // Adding buttons to panel
        p1.add(b1);
        p1.add(b2);
        p1.add(b3);
        p1.add(b4);
        // Packing
        add(scroller); // Scroll panel for table
        add(p1);
        // Setup listeners
        buttonSetup();
    }

    /**
     * Constructs JTable used for displaying information stored in database
     * @return Table for displaying maze data
     */
    private JTable mazeTable() {
        Object[][] rowData = getData();
        String[] header = {"Author", "Date Create", "Last Edited", "SizeX", "SizeY"};
        JTable table = new JTable(rowData, header);
        table.setBounds(30,40,200,300);
        return table;
    }

    private Object[][] getData() {
        Object[][] data = null;
        try {
            Connection connection = DBConnection.getInstance();
            System.out.print(connection);
            final String GET_DATA = "SELECT * FROM mazeStorage";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(GET_DATA);

            int rowCount = getRowCount(rs); // Row Count
            int columnCount = getColumnCount(rs); // Column Count

            data = new Object[rowCount][columnCount];

            rs.beforeFirst();

            int i = 0;
                while (rs.next()) {
                    int j = 0;
                    data[i][j++] = rs.getString("author");
                    data[i][j++] = rs.getString("dateCreated");
                    data[i][j++] = rs.getString("dateLastEdited");
                    data[i][j++] = rs.getString("sizeX");
                    data[i][j++] = rs.getString("sizeY");

                    i++;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        return data;
    }

    // Method to get Row Count from ResultSet Object
    private int getRowCount(ResultSet rs) {

        try {

            if(rs != null) {

                rs.last();

                return rs.getRow();
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    // Method to get Column Count from ResultSet Object
    private int getColumnCount(ResultSet rs) {

        try {

            if(rs != null)
                return rs.getMetaData().getColumnCount();

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }



    /**
     * Adds listener for mousePressed event
     */
    private void listenerSetup(){
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                int selectedColumn =table.getSelectedColumn();
                String selectedCellValue = (String) table.getValueAt(table.getSelectedRow() , table.getSelectedColumn());
                System.out.println(selectedCellValue);
                System.out.println(selectedRow);
                System.out.println(selectedColumn);
            }
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    /**
     * Adds actionListeners for buttons
     */
    void buttonSetup() {
        b1.addActionListener(e -> {
            SwingUtilities.invokeLater(new CreatePage());
            dispose();
        });
        b2.addActionListener(e -> {
            // Create maze panel - This will later implement parameters from the database
            Maze testMaze = new Maze("Maze Title", "Maze Author", 80,50);
            testMaze.GenerateMaze();

            try {
                SwingUtilities.invokeLater(new EditPage(testMaze, 12));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        b3.addActionListener(e -> System.out.println("get pranked nerd"));
    }

    public void run() {createGUI();}

    public static void main(String[] args) {SwingUtilities.invokeLater(new DataBaseUI());}
}
