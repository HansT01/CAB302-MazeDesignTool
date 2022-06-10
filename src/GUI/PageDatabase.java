package GUI;

import Database.DBConnection;
import Database.JDBCDataSource;
import Maze.Maze;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Constructs window for viewing and interacting with mazes stored in database
 */
public class PageDatabase extends JFrame implements Runnable {
    JDBCDataSource data;
    ResultSet tableData;
    public boolean complete;

    GridBagManager gbm = new GridBagManager();

    JButton refreshButton = new JButton("Refresh table");
    /** JButton used for New */
    JButton newButton = new JButton("Create new maze");
    /** JButton used for Edit */
    JButton editButton = new JButton("Edit maze");
    /** JButton used for Delete */
    JButton deleteButton = new JButton("Delete maze");
    /** JButton used for Export */
    JButton exportButton = new JButton("Export to PNG");
    JButton exportSolutionButton = new JButton("Export with solution");
    /** JButton used for Complete */
    JButton completeButton = new JButton("Mark Complete");

    private final JTable mazesTable = new JTable(new DefaultTableModel(new String[][] {}, new String[] {"Title", "Author", "Date created", "Last edited", "SizeX", "SizeY", "Cell Size"})) {
        // make rows uneditable
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable mazesTable2 = new JTable(new DefaultTableModel(new String[][] {}, new String[] {"Title", "Author", "Date created", "Last edited", "SizeX", "SizeY", "Cell Size"})) {
        // make rows uneditable
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    /**
     * Default constructor for PageDatabase.
     */
    public PageDatabase() {
        listenerSetup();
    }

    /**
     * Updates the table with new data
     */
    public void UpdateTable() {
        try {
            // get current model
            DefaultTableModel tm = (DefaultTableModel) mazesTable.getModel();
            DefaultTableModel tm2 = (DefaultTableModel) mazesTable2.getModel();

            // clear rows and append new data
            tm.setRowCount(0);
            tm2.setRowCount(0);
            tableData = data.GetUserMazes();
            String[][] rows = ParseData();
            for (String[] row : rows) {
                tm.addRow(row);
                tm2.addRow(row);
            }

            // update table model
            mazesTable.setModel(tm);
            mazesTable2.setModel(tm2);
            tm.fireTableDataChanged();
            tm2.fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses ResultSet object from tableData into strings
     * @return 2D array of strings to be displayed on a JTable
     */
    private String[][] ParseData() throws InvalidInputException {
        try {
            Connection connection = DBConnection.getInstance();
            Statement st = connection.createStatement();
            if (complete == true) {
                tableData = st.executeQuery("SELECT * FROM mazeStorage WHERE complete IS true;");
            }
            else {
                tableData = st.executeQuery("SELECT * FROM mazeStorage WHERE complete IS false;");
            }
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

                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                result[i][j++] = df.format(tableData.getDate("dateCreated").getTime());
                result[i][j++] = df.format(tableData.getDate("dateLastEdited").getTime());

                result[i][j++] = tableData.getString("sizeX");
                result[i][j++] = tableData.getString("sizeY");
                result[i][j] = tableData.getString("cellSize");
                i++;
            }
            return result;
        } catch (Exception ex) {
            throw new InvalidInputException(ex.toString(), this);
        }
    }

    /**
     * Creates panel for the mazes table.
     * @return JPanel object
     */
    private JPanel CreateTablePanel(JTable jTable) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(800, 500));

        JScrollPane scrollPane = new JScrollPane(jTable);

        TableColumnModel tcm = jTable.getColumnModel();

        tcm.getColumn(0).setPreferredWidth(200);
        tcm.getColumn(1).setPreferredWidth(200);
        tcm.getColumn(2).setPreferredWidth(200);
        tcm.getColumn(3).setPreferredWidth(200);
        tcm.getColumn(4).setPreferredWidth(100);
        tcm.getColumn(5).setPreferredWidth(100);
        tcm.getColumn(6).setPreferredWidth(100);

        GridBagConstraints gbc;
        int gridRow = 0;

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(scrollPane, gbc);

        return panel;
    }

    /**
     * Creates panel for button options
     * @return JPanel object
     */
    private JPanel CreateOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

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

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(exportButton, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(exportSolutionButton, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow);
        gbc.gridwidth = 1;
        panel.add(completeButton, gbc);

        return panel;
    }

    private JPanel TabbedPane() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        tabbedPane.addTab("In Progress", CreateTablePanel(mazesTable));
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        tabbedPane.addTab("Complete", CreateTablePanel(mazesTable2));
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        GridBagConstraints gbc;
        int gridRow = 0;

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(tabbedPane, gbc);

        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));
                if (sourceTabbedPane.getTitleAt(index) == "Complete") {
                    complete = true;
                    UpdateTable();
                    System.out.println(complete);
                } else {
                    complete = false;
                    UpdateTable();
                    System.out.println(complete);
                }
            }
        };
        tabbedPane.addChangeListener(changeListener);

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
        add(TabbedPane(), gbc);

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
            try {
                EditMaze();
            } catch (InvalidInputException ex) {
                ex.printStackTrace();
            }
        });
        deleteButton.addActionListener(e -> {
            try {
                DeleteMaze();
            } catch (InvalidInputException ex) {
                ex.printStackTrace();
            }
        });
        exportButton.addActionListener(e -> {
            try {
                ExportMazes();
            } catch (InvalidInputException ex) {
                ex.printStackTrace();
            }
        });
        exportSolutionButton.addActionListener(e -> {
            try {
                ExportMazesWithSolution();
            } catch (InvalidInputException ex) {
                ex.printStackTrace();
            }
        });
        completeButton.addActionListener(e -> {
            CompleteMaze();
        });
    }

    /**
     * Complete button event handler.
     */
    private void CompleteMaze() {
        int selectedRow = mazesTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                tableData.absolute(selectedRow + 1);
                int id = tableData.getInt("id");
                data.CompleteMaze(id);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Edit button event handler.
     */
    public void EditMaze() throws InvalidInputException {
        int selectedRow = mazesTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                tableData.absolute(selectedRow + 1);
                int id = tableData.getInt("id");
                Maze maze = data.GetMaze(id);
                SwingUtilities.invokeLater(new PageEdit(maze, id));
            } catch (Exception e) {
                throw new InvalidInputException(e.toString(), this);
            }
        }
    }

    /**
     * Delete button event handler.
     */
    public void DeleteMaze() throws InvalidInputException {
        if (complete == false) {
            int selectedRow = mazesTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    tableData.absolute(selectedRow + 1);
                    int id = tableData.getInt("id");
                    data.DeleteMaze(id);
                    UpdateTable();
                } catch (Exception ex) {
                    throw new InvalidInputException(ex.toString(), this);
                }
            }
        }
        else {
            int selectedRow = mazesTable2.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    tableData.absolute(selectedRow + 1);
                    int id = tableData.getInt("id");
                    data.DeleteMaze(id);
                    UpdateTable();
                } catch (Exception ex) {
                    throw new InvalidInputException(ex.toString(), this);
                }
            }
        }
    }

    /**
     * Export selected mazes to PNG file without solution
     * @throws InvalidInputException
     */
    public void ExportMazes() throws InvalidInputException {
        int[] selectedRows = mazesTable.getSelectedRows();
        if (selectedRows.length != 0) {
            try {
                for (int row : selectedRows) {
                    tableData.absolute(row + 1);
                    int id = tableData.getInt("id");
                    Maze maze = data.GetMaze(id);
                    MazePanel mp = new MazePanel(maze);
                    mp.setDrawSolution(false);
                    mp.repaint();
                    mp.ExportToFile();
                }
            } catch (Exception e) {
                throw new InvalidInputException(e.toString(), this);
            }
        }
    }

    /**
     * Export selected mazes to PNG file with solution
     * @throws InvalidInputException
     */
    public void ExportMazesWithSolution() throws InvalidInputException {
        int[] selectedRows = mazesTable.getSelectedRows();
        if (selectedRows.length != 0) {
            try {
                for (int row : selectedRows) {
                    tableData.absolute(row + 1);
                    int id = tableData.getInt("id");
                    Maze maze = data.GetMaze(id);
                    MazePanel mp = new MazePanel(maze);
                    mp.setDrawSolution(true);
                    mp.repaint();
                    mp.ExportToFile();
                }
            } catch (Exception e) {
                throw new InvalidInputException(e.toString(), this);
            }
        }
    }

    @Override
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
