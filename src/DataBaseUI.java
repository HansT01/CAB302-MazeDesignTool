import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
        String[][] rowData = {
                {"Mr PlaceHolder", "The PlaceHold", "Some Time", "Ur Mum"},
                {"Mr PlaceHolder II", "something", "Some Time", "Ur Mum"}
        };
        String[] header = {"Title", "Author", "Data Created", "Last Edited"};
        JTable table = new JTable(rowData, header);
        table.setBounds(30,40,200,300);
        return table;
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
            SwingUtilities.invokeLater(new CreateDialogue());
            dispose();
        });
        b2.addActionListener(e -> {
            SwingUtilities.invokeLater(new MazeDisplay());
            dispose();
        });
        b3.addActionListener(e -> System.out.println("get pranked nerd"));
    }

    public void run() {createGUI();}

    public static void main(String[] args) {SwingUtilities.invokeLater(new DataBaseUI());}
}
