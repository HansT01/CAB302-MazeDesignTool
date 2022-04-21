import javax.swing.*;
import java.awt.*;


public class DataBaseUI extends JFrame implements Runnable{

    // Values used for default screen size
    private static final int width = 500;
    private static final int height = 300;

    // Used for opening application at location of mouse pointer on screen
    Point open_location = MouseInfo.getPointerInfo().getLocation();
    int open_x = (int) open_location.getX();
    int open_y = (int) open_location.getY();


    private void createGUI() {
        setLocation(open_x, open_y); // Open window at location of mouse pointer
        setVisible(true);
        setTitle("Maze DataBase");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set so pressing x closes program
        setSize(WIDTH, HEIGHT);
    }






    public void run() {createGUI();}
}
