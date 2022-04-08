import javax.swing.*;
import java.awt.*;

public class MazeDisplay {

    /**
     * Exports maze to image file
     */
    public void exportButton() {}

    public static void main(String args[]) {

        // Creating and displaying the program
        JFrame window = new JFrame("Maze Generator");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setPreferredSize(new Dimension(400, 400)); // desired size of window

        // Setting up the menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu exportMenu = new JMenu("Export to image file");
        exportMenu.add("With Solution");
        exportMenu.add("Without Solution");
        menuBar.add(exportMenu);
        window.setJMenuBar(menuBar);

        // Compile and place window on screen
        window.pack();
        window.setLocationRelativeTo(null);
    }

}
