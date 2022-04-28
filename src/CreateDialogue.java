import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreateDialogue extends JFrame implements Runnable {

    // Used for opening application at location of mouse pointer on screen
    Point openLocation = MouseInfo.getPointerInfo().getLocation();

    private void createGUI () {
        // Setting up main window
        setLocation(openLocation); // Open window at location of mouse pointer
        setVisible(true);
        setTitle("Create Maze");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set so pressing x closes window
        setSize(500, 300);
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
        create.addActionListener(e -> {
            SwingUtilities.invokeLater(new MazeDisplay());
            dispose();
        });

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
