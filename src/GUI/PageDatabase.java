package GUI;

import Database.JDBCDataSource;
import Maze.Maze;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Constructs window for viewing and interacting with mazes stored in database
 */
public class PageDatabase extends JFrame implements Runnable {
    JDBCDataSource data;
    ResultSet dataInProgress;
    ResultSet dataComplete;
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
    JButton completeButton = new JButton("Toggle complete");

    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    private final JTable mazesInProgress = new JTable(new DefaultTableModel(new String[][] {}, new String[] {"Title", "Author", "Date created", "Last edited", "SizeX", "SizeY", "Cell Size"})) {
        // make rows uneditable
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable mazesComplete = new JTable(new DefaultTableModel(new String[][] {}, new String[] {"Title", "Author", "Date created", "Last edited", "SizeX", "SizeY", "Cell Size"})) {
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
            DefaultTableModel tm1 = (DefaultTableModel) mazesInProgress.getModel();
            DefaultTableModel tm2 = (DefaultTableModel) mazesComplete.getModel();

            // clear rows and append new data
            tm1.setRowCount(0);
            tm2.setRowCount(0);

            dataInProgress = data.GetUserMazes(false);
            ArrayList<String[]> rowsInProgress = ParseData(dataInProgress);
            for (String[] row : rowsInProgress) {
                tm1.addRow(row);
            }

            dataComplete = data.GetUserMazes(true);
            ArrayList<String[]> rowsComplete = ParseData(dataComplete);
            for (String[] row : rowsComplete) {
                tm2.addRow(row);
            }


            // update table model
            mazesInProgress.setModel(tm1);
            mazesComplete.setModel(tm2);
            tm1.fireTableDataChanged();
            tm2.fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses ResultSet object from tableData into strings
     * @return 2D array of strings to be displayed on a JTable
     */
    private ArrayList<String[]> ParseData(ResultSet rs) throws InvalidInputException {
        try {
            // Get column count
            rs.last();
            int colCount = rs.getMetaData().getColumnCount();
            ArrayList<String[]> result = new ArrayList<>();

            // Iterate through result set
            rs.beforeFirst();
            while(rs.next()) {
                int j = 0;
                String[] row = new String[colCount];
                row[j++] = rs.getString("title");
                row[j++] = rs.getString("author");

                DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                row[j++] = df.format(rs.getTimestamp("dateCreated").getTime());
                row[j++] = df.format(rs.getTimestamp("dateLastEdited").getTime());

                row[j++] = rs.getString("sizeX");
                row[j++] = rs.getString("sizeY");
                row[j] = rs.getString("cellSize");

                // Add row to ArrayList
                result.add(row);
            }
            // return ArrayList
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
        panel.add(completeButton, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(exportButton, gbc);

        gbc = gbm.CreateInnerGBC(0, gridRow++);
        gbc.gridwidth = 1;
        panel.add(exportSolutionButton, gbc);

        return panel;
    }

    private JPanel CreateTabbedPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        tabbedPane.addTab("In Progress", CreateTablePanel(mazesInProgress));
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        tabbedPane.addTab("Complete", CreateTablePanel(mazesComplete));
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        GridBagConstraints gbc;
        int gridRow = 0;

        gbc = gbm.CreateInnerGBC(0, gridRow);
        gbc.gridwidth = 1;
        panel.add(tabbedPane, gbc);

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
        add(CreateTabbedPanel(), gbc);

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
        tabbedPane.addChangeListener(e -> {
            UpdateTable();
        });
    }

    private int GetSelectedRow() {
        int progressRow = mazesInProgress.getSelectedRow();
        if (progressRow != -1) {
            return progressRow;
        }
        int completeRow = mazesComplete.getSelectedRow();
        if (completeRow != -1) {
            return completeRow + mazesInProgress.getRowCount();
        }
        return -1;
    }

    private int[] GetSelectedRows() {
        int[] progressRows = mazesInProgress.getSelectedRows();
        if (progressRows.length != 0) {
            return progressRows;
        }
        int[] completeRows = mazesComplete.getSelectedRows();
        if (completeRows.length != 0) {
            int rc = mazesInProgress.getRowCount();
            for (int i = 0; i < completeRows.length; i++) {
                completeRows[i] += rc;
            }
            return completeRows;
        }
        return new int[] {};
    }

    /**
     * Complete button event handler.
     */
    private void CompleteMaze() {
        int srIP = mazesInProgress.getSelectedRow();
        int srC = mazesComplete.getSelectedRow();

        if (srIP != -1) {
            try {
                dataInProgress.absolute(srIP + 1);
                int id = dataInProgress.getInt("id");
                data.ToggleCompleteMaze(id);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (srC != -1) {
            try {
                dataComplete.absolute(srC + 1);
                int id = dataComplete.getInt("id");
                data.ToggleCompleteMaze(id);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        UpdateTable();
    }

    /**
     * Edit button event handler.
     */
    public void EditMaze() throws InvalidInputException {
        int srIP = mazesInProgress.getSelectedRow();
        int srC = mazesComplete.getSelectedRow();

        if (srIP != -1) {
            try {
                dataInProgress.absolute(srIP + 1);
                int id = dataInProgress.getInt("id");
                Maze maze = data.GetMaze(id);
                SwingUtilities.invokeLater(new PageEdit(maze, id));
            } catch (Exception e) {
                throw new InvalidInputException(e.toString(), this);
            }
        }

        if (srC != -1) {
            try {
                dataComplete.absolute(srC + 1);
                int id = dataComplete.getInt("id");
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
        int srIP = mazesInProgress.getSelectedRow();
        int srC = mazesComplete.getSelectedRow();

        if (srIP != -1) {
            try {
                dataInProgress.absolute(srIP + 1);
                int id = dataInProgress.getInt("id");
                data.DeleteMaze(id);
                UpdateTable();
            } catch (Exception ex) {
                throw new InvalidInputException(ex.toString(), this);
            }
        }

        if (srC != -1) {
            try {
                dataComplete.absolute(srC + 1);
                int id = dataComplete.getInt("id");
                data.DeleteMaze(id);
                UpdateTable();
            } catch (Exception ex) {
                throw new InvalidInputException(ex.toString(), this);
            }
        }
    }

    /**
     * Export selected mazes to PNG file without solution
     * @throws InvalidInputException
     */
    public void ExportMazes() throws InvalidInputException {
        int[] srIP = mazesInProgress.getSelectedRows();
        int[] srC = mazesComplete.getSelectedRows();

        if (srIP.length != 0) {
            try {
                for (int row : srIP) {
                    dataInProgress.absolute(row + 1);
                    int id = dataInProgress.getInt("id");
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

        if (srC.length != 0) {
            try {
                for (int row : srC) {
                    dataComplete.absolute(row + 1);
                    int id = dataComplete.getInt("id");
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
        int[] srIP = mazesInProgress.getSelectedRows();
        int[] srC = mazesComplete.getSelectedRows();

        if (srIP.length != 0) {
            try {
                for (int row : srIP) {
                    dataInProgress.absolute(row + 1);
                    int id = dataInProgress.getInt("id");
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

        if (srC.length != 0) {
            try {
                for (int row : srC) {
                    dataComplete.absolute(row + 1);
                    int id = dataComplete.getInt("id");
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
