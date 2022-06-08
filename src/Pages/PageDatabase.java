package Pages;

import Database.DBConnection;
import Database.JDBCDataSource;
import Maze.Maze;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Constructs window for viewing and interacting with mazes stored in database
 */
public class PageDatabase extends JFrame implements Runnable {

    /** Used for getting location of mouse pointer */
    Point openLocation = MouseInfo.getPointerInfo().getLocation();

    GridBagManager gbm = new GridBagManager();

    /** Value of column of cell clicked on */
    int selectedColumn;
    /** Value of row of cell clicked on */
    int selectedRow;
    /** Data in cell clicked on */
    String selectedCellValue;

    /** JButton used for New */
    JButton newButton = new JButton("New");
    /** JButton used for Edit */
    JButton editButton = new JButton("Edit");
    /** JButton used for Delete */
    JButton deleteButton = new JButton("Delete");
    /** JButton used for Export */
    JButton exportButton = new JButton("Export");

    JButton loginButton = new JButton("Login");

    private final JTable mazesTable = new JTable(new DefaultTableModel(new String[][] {}, new String[] {"Title", "Author", "Date created", "Last edited", "SizeX", "SizeY"})) {
        // make rows uneditable
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public PageDatabase() {
        listenerSetup();
        UpdateTable();
    }


    public void UpdateTable() {
        // get current model
        DefaultTableModel tm = (DefaultTableModel) mazesTable.getModel();
        tm.setRowCount(0);

        // create new data array
        Object[][] data = getData();
        for (Object[] row : data) {
            tm.addRow(row);
        }

        // update table model
        mazesTable.setModel(tm);
        tm.fireTableDataChanged();
    }

    private JPanel CreateTablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        JScrollPane scrollPane = new JScrollPane(mazesTable);
        mazesTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        mazesTable.getColumnModel().getColumn(1).setPreferredWidth(75);
        mazesTable.getColumnModel().getColumn(2).setPreferredWidth(75);

        GridBagConstraints gbc;
        int gridRow = 0;

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(scrollPane, gbc);

        return panel;
    }

    private JPanel CreateOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("???"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );

        GridBagConstraints gbc;
        int gridRow = 0;

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(newButton, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(editButton, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(deleteButton, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(exportButton, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(loginButton, gbc);

        return panel;
    }

    /**
     * Main create GUI method. Calls other create panel methods and adds them to the main frame.
     */
    public void CreateGUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        int gridRow = 0;

        // table panel
        gbc = gbm.CreateOuterGBC(0, gridRow);
        add(CreateTablePanel(), gbc);

        // options panel
        gbc = gbm.CreateOuterGBC(1, gridRow);
        add(CreateOptionsPanel(), gbc);

        // resizes window to preferred dimensions
        pack();

        // centre to screen
        setLocationRelativeTo(null);

        // set defaults
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private Object[][] getData() {
        Object[][] data = null;
        try {
            Connection connection = DBConnection.getInstance();
            final String GET_DATA = "SELECT * FROM mazeStorage";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(GET_DATA);

            int rowCount = getRowCount(rs);         // Row Count
            int columnCount = getColumnCount(rs);   // Column Count

            data = new Object[rowCount][columnCount];

            rs.beforeFirst();

            int i = 0;
            while (rs.next()) {
                int j = 0;
                data[i][j++] = rs.getString("title");
                data[i][j++] = rs.getString("author");
                data[i][j++] = rs.getString("dateCreated");
                data[i][j++] = rs.getString("dateLastEdited");
                data[i][j++] = rs.getString("sizeX");
                data[i][j] = rs.getString("sizeY");
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
            if (rs != null) return rs.getMetaData().getColumnCount();
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
        mazesTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = mazesTable.getSelectedRow();
                // int selectedColumn = mazesTable.getSelectedColumn();
                // String selectedCellValue = (String) mazesTable.getValueAt(mazesTable.getSelectedRow(), mazesTable.getSelectedColumn());

            }
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        newButton.addActionListener(e -> {
            SwingUtilities.invokeLater(new PageCreate());
            dispose();
        });
        editButton.addActionListener(e -> {
            // Create maze panel - This will later implement parameters from the database
            try {
                SwingUtilities.invokeLater(new PageCreate());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        deleteButton.addActionListener(e -> {
            String title = mazesTable.getModel().getValueAt(selectedRow, 0).toString();
            String author = mazesTable.getModel().getValueAt(selectedRow, 1).toString();

            try {
                Connection connection = DBConnection.getInstance();
                final String DELETE = "DELETE FROM mazeStorage WHERE title="+ "'" + title + "'" + " AND author=" + "'" + author + "'";

                Statement statement = connection.createStatement();
                statement.executeQuery(DELETE);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            UpdateTable();

        });
        loginButton.addActionListener(e -> {
            try {
                SwingUtilities.invokeLater(new PageLogin());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void run() {CreateGUI();}

    public static void main(String[] args) {
        new JDBCDataSource();
        PageDatabase page = new PageDatabase();
        SwingUtilities.invokeLater(page);
    }
}
