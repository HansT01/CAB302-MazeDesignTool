import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LaunchScreen_redundant extends JFrame implements Runnable {
    // Values used for default screen size
    private static final int width = 500;
    private static final int height = 300;

    // Used for opening application at location of mouse pointer on screen
    Point open_location = MouseInfo.getPointerInfo().getLocation();
    int open_x = (int) open_location.getX();
    int open_y = (int) open_location.getY();



    private void createGUI () {
        setLocation(open_x, open_y); // Open window at location of mouse pointer
        setVisible(true);
        setTitle("Computer-Assisted Maze Design Tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set so pressing x closes window

        JPanel btnPanel = new JPanel();
        BoxLayout boxlayout = new BoxLayout(btnPanel, BoxLayout.PAGE_AXIS);
        btnPanel.setBorder(new EmptyBorder(new Insets(height/2, width/2, height/2, width/2)));
        btnPanel.setLayout(boxlayout);




        // Maze button
        JButton maze = new JButton(); maze.setText("Maze");
        maze.addActionListener(e -> System.out.print("maze")); // testing
        btnPanel.add(maze);
        // Database button
        JButton db = new JButton(); db.setText("Database"); // testing
        db.addActionListener(e -> SwingUtilities.invokeLater(new DataBaseUI()));
        btnPanel.add(db);


        add(btnPanel);
        pack();
    }


    @Override
    public void run() {createGUI();}

    public static void main(String[] args) {SwingUtilities.invokeLater(new LaunchScreen_redundant());}

}
