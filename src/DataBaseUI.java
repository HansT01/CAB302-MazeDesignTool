import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class DataBaseUI extends JFrame implements Runnable {

    // Values used for default screen size
    private static final int width = 500;
    private static final int height = 500;

    // Used for opening application at location of mouse pointer on screen
    Point open_location = MouseInfo.getPointerInfo().getLocation();
    int open_x = (int) open_location.getX();
    int open_y = (int) open_location.getY();

    // For getting rows and columns
    int selectedColumn;
    int selectedRow;
    String selectedCellValue;

    JButton b1 = new JButton();
    JButton b2 = new JButton();
    JButton b3 = new JButton();
    JButton b4 = new JButton();
    JTable table;


    private void createGUI() {
        // Adjusting window
        setLocation(open_x, open_y); // Open window at location of mouse pointer
        setVisible(true);
        setTitle("Maze DataBase");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set so pressing x closes window not whole program
        setSize(width, height);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

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

    void buttonSetup() {
        b1.addActionListener(e -> SwingUtilities.invokeLater(new CreateDialogue()));
        b2.addActionListener(e -> SwingUtilities.invokeLater(new MazeDisplay()));
        b3.addActionListener(e -> System.out.println("get pranked nerd"));
    }

    public void run() {createGUI();}

    public static void main(String[] args) {SwingUtilities.invokeLater(new DataBaseUI());}
}
