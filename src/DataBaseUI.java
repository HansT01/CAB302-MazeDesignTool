import javax.swing.*;
import java.awt.*;
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


        // Packing
        add(scroller); // Scroll panel for table
        add(p1);


    }

    private JTable mazeTable() {
        String[][] rowData = {
                {"Mr PlaceHolder", "The PlaceHold", "Some Time", "Ur Mum"},
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
                String selectedCellValue = (String) table.getValueAt(table.getSelectedRow() , table.getSelectedColumn());
                System.out.println(selectedCellValue);
            }
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }



    public void run() {createGUI();}

    public static void main(String[] args) {SwingUtilities.invokeLater(new DataBaseUI());}
}
