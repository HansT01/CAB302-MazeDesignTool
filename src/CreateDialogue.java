import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreateDialogue extends JFrame implements Runnable {

    // Values used for default screen size
    private static final int width = 500;
    private static final int height = 300;

    // Used for opening application at location of mouse pointer on screen
    Point open_location = MouseInfo.getPointerInfo().getLocation();
    int open_x = (int) open_location.getX();
    int open_y = (int) open_location.getY();

    private void createGUI () {
        // Setting up main window
        setLocation(open_x, open_y); // Open window at location of mouse pointer
        setVisible(true);
        setTitle("Create Maze");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set so pressing x closes window
        setSize(width, height);
        setLayout(new BorderLayout());

        // Labels
        JLabel authorLabel = new JLabel("Author");
        JLabel titleLabel = new JLabel("Maze Title");
        JLabel xLabel = new JLabel("Width");
        JLabel yLabel = new JLabel("Height");
        JLabel cellLabel = new JLabel("Cell Size");
        // Text Areas
        JTextField authorText = new JTextField();
        JTextField titleText = new JTextField();
        JTextField xText = new JTextField();
        JTextField yText = new JTextField();
        JTextField cellText = new JTextField();
        // Button
        JButton create = new JButton(); create.setText("Create");
        create.addActionListener(e ->SwingUtilities.invokeLater(new MazeDisplay()));

        // Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(11,1));
        panel.setBorder(new EmptyBorder(10,10,10,10));
        add(panel, BorderLayout.CENTER);

        // Packing
        panel.add(authorLabel);
        panel.add(authorText);
        panel.add(titleLabel);
        panel.add(titleText);
        panel.add(xLabel);
        panel.add(xText);
        panel.add(yLabel);
        panel.add(yText);
        panel.add(cellLabel);
        panel.add(cellText);
        panel.add(create);

    }




    @Override
    public void run() {createGUI();}
}
