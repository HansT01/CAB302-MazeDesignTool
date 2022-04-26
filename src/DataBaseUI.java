import javax.swing.*;
import java.awt.*;


public class DataBaseUI extends JFrame implements Runnable{

    // Values used for default screen size
    private static final int width = 500;
    private static final int height = 500;

    // Used for opening application at location of mouse pointer on screen
    Point open_location = MouseInfo.getPointerInfo().getLocation();
    int open_x = (int) open_location.getX();
    int open_y = (int) open_location.getY();


    private void createGUI() {
        // Adjusting window
        setLocation(open_x, open_y); // Open window at location of mouse pointer
        setVisible(true);
        setTitle("Maze DataBase");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set so pressing x closes window not whole program
        setSize(width, height);


        add(mazeTable());




    }


    private JScrollPane scrollPane() {
        JScrollPane scroller = new JScrollPane();
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setMinimumSize(new Dimension(200, 100));
        scroller.setPreferredSize(new Dimension(250, 150));
        scroller.setMaximumSize(new Dimension(300, 200));

        return scroller;
    }

    private JTable mazeTable() {
        String[][] data = {{"ur","a","nerd"}};
        String[] columns = {"test","test","test"};
        JTable table = new JTable(data, columns);
        table.setBounds(10,10,100,200);



        return table;
    }



    public void run() {createGUI();}

    public static void main(String[] args) {SwingUtilities.invokeLater(new DataBaseUI());}
}
