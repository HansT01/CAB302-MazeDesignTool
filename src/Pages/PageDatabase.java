package Pages;

import Database.DBConnection;
import Database.JDBCDataSource;
import Maze.Maze;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;


/**
 * Constructs window for viewing and interacting with mazes stored in database
 */
public class PageDatabase extends JFrame implements Runnable {

    /** Used for getting location of mouse pointer */
    Point openLocation = MouseInfo.getPointerInfo().getLocation();
    JDBCDataSource data;
    ResultSet tableData;

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

    private final JTable mazesTable = new JTable(new DefaultTableModel(new String[][] {}, new String[] {"Title", "Author", "Date created", "Last edited", "SizeX", "SizeY", "Cell Size"})) {
        // make rows uneditable
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public PageDatabase() {
        data = new JDBCDataSource();
        listenerSetup();
        UpdateTable();
    }


    public void UpdateTable() {
        // get current model
        DefaultTableModel tm = (DefaultTableModel) mazesTable.getModel();
        tm.setRowCount(0);

        tableData = data.GetAllDocuments();
        String[][] rows = ParseData();

        if (rows == null) return;
        for (String[] row : rows) {
            tm.addRow(row);
        }

        // update table model
        mazesTable.setModel(tm);
        tm.fireTableDataChanged();
    }

    private String[][] ParseData() {
        try {
            tableData.last();
            int rowCount = tableData.getRow();
            int colCount = tableData.getMetaData().getColumnCount();

            String[][] result = new String[rowCount][colCount];

            int i = 0;
            tableData.beforeFirst();
            while(tableData.next()) {
                int j = 0;
                result[i][j++] = tableData.getString("title");
                result[i][j++] = tableData.getString("author");

                Date dateCreated = new Date(tableData.getLong("dateCreated"));
                Date dateLastEdited = new Date(tableData.getLong("dateLastEdited"));
                result[i][j++] = dateCreated.toString();
                result[i][j++] = dateLastEdited.toString();

                result[i][j++] = tableData.getString("sizeX");
                result[i][j++] = tableData.getString("sizeY");
                result[i][j] = tableData.getString("cellSize");
                i++;
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private JPanel CreateTablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        JScrollPane scrollPane = new JScrollPane(mazesTable);

        TableColumnModel tcm = mazesTable.getColumnModel();

        tcm.getColumn(0).setPreferredWidth(200);
        tcm.getColumn(1).setPreferredWidth(100);
        tcm.getColumn(2).setPreferredWidth(100);
        tcm.getColumn(3).setPreferredWidth(100);
        tcm.getColumn(4).setPreferredWidth(50);
        tcm.getColumn(5).setPreferredWidth(50);
        tcm.getColumn(6).setPreferredWidth(50);

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

        panel.setPreferredSize(new Dimension(150, 300));

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

        gbc = gbm.CreateInnerGBC(0, gridRow);
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
                int selectedColumn = mazesTable.getSelectedColumn();
                String selectedCellValue = (String) mazesTable.getValueAt(mazesTable.getSelectedRow(), mazesTable.getSelectedColumn());
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
                Maze testMaze = new Maze("Maze Title", "Maze Author", 80,50, 16);
                testMaze.GenerateMaze();
                SwingUtilities.invokeLater(new PageEdit(testMaze));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        deleteButton.addActionListener(e -> System.out.println("get pranked nerd"));
    }

    public void run() {CreateGUI();}

    public static void main(String[] args) {
        new JDBCDataSource();
        PageDatabase page = new PageDatabase();
        SwingUtilities.invokeLater(page);
    }
}
