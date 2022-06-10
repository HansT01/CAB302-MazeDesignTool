package GUI;

import Database.JDBCDataSource;
import Maze.Maze;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.*;
import java.util.Date;


/**
 * Constructs window for viewing and interacting with mazes stored in database
 */
public class PageDatabase extends JFrame implements Runnable {
    JDBCDataSource data;
    ResultSet tableData;

    GridBagManager gbm = new GridBagManager();

    JButton refreshButton = new JButton("Refresh table");
    /** JButton used for New */
    JButton newButton = new JButton("New");
    /** JButton used for Edit */
    JButton editButton = new JButton("Edit");
    /** JButton used for Delete */
    JButton deleteButton = new JButton("Delete");
    /** JButton used for Export */
    JButton exportButton = new JButton("Export");

    private final JTable mazesTable = new JTable(new DefaultTableModel(new String[][] {}, new String[] {"Title", "Author", "Date created", "Last edited", "SizeX", "SizeY", "Cell Size"})) {
        // make rows uneditable
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public PageDatabase() {
        listenerSetup();
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

                Date dateCreated = new Date(tableData.getDate("dateCreated").getTime());
                Date dateLastEdited = new Date(tableData.getDate("dateLastEdited").getTime());
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
        /*panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("???"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );*/

        panel.setPreferredSize(new Dimension(150, 300));

        GridBagConstraints gbc;
        int gridRow = 0;

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(refreshButton, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(newButton, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(editButton, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(deleteButton, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow);
        gbc.gridwidth = 1;
        panel.add(exportButton, gbc);

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
        gbc.anchor = GridBagConstraints.EAST;
        add(CreateTablePanel(), gbc);

        // options panel
        gbc = gbm.CreateOuterGBC(1, gridRow);
        gbc.anchor = GridBagConstraints.WEST;
        add(CreateOptionsPanel(), gbc);

        // resizes window to preferred dimensions
        pack();

        // centre to screen
        setLocationRelativeTo(null);

        // set defaults
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Adds listener for mousePressed event
     */
    private void listenerSetup(){
        refreshButton.addActionListener(e -> {
            UpdateTable();
        });
        newButton.addActionListener(e -> {
            SwingUtilities.invokeLater(new PageCreate());
        });
        editButton.addActionListener(e -> {
            EditMaze();
        });
        deleteButton.addActionListener(e -> {
            DeleteMaze();
        });
    }


    public void EditMaze() {
        int selectedRow = mazesTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                tableData.absolute(selectedRow + 1);
                int id = tableData.getInt("id");
                Blob b = tableData.getBlob("serialization");
                Maze maze = Maze.ByteArrayToMaze(b.getBytes(1, (int) b.length()));
                SwingUtilities.invokeLater(new PageEdit(maze, id));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void DeleteMaze() {
        int selectedRow = mazesTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                tableData.absolute(selectedRow + 1);
                int id = tableData.getInt("id");
                data.DeleteMaze(id);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void run() {
        CreateGUI();
        data = new JDBCDataSource();
        UpdateTable();
    }

    public static void main(String[] args) {
        new JDBCDataSource();
        SwingUtilities.invokeLater(new PageDatabase());
    }
}
