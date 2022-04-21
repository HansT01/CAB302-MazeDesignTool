import javax.swing.*;
import java.awt.*;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle;
import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MazeDisplay {

    /**
     * Exports maze to image file
     */
    private JLabel dbar;

    public void itemStateChanged(ItemEvent e) {

        int sel = e.getStateChange();

        if (sel == ItemEvent.SELECTED) {

            JRadioButton button = (JRadioButton) e.getSource();
            String text = button.getText();

            StringBuilder sb = new StringBuilder("Selected: ");
            sb.append(text);

            dbar.setText(sb.toString());
        }
    }

    private void initializeUI() {
        // Creating and displaying the program
        JFrame window = new JFrame("Maze Generator");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setPreferredSize(new Dimension(400, 400)); // desired size of window

        // Setting up the menu bar
        JMenuBar menuBar = new JMenuBar();

        // Exporting buttons
        JMenu exportMenu = new JMenu("Export to image file");
        exportMenu.add("With Solution");
        exportMenu.add("Without Solution");
        menuBar.add(exportMenu);

        // Open Button
        JMenu openMenu = new JMenu("Open File");
        menuBar.add(openMenu);

        // Login Button
        JMenu loginMenu = new JMenu("Login");
        menuBar.add(loginMenu);

        window.setJMenuBar(menuBar);

        // Radio buttons for choosing difficulty
        JPanel difficultyPanel = new JPanel();
        JLabel lbl = new JLabel("Difficulty:");

        ButtonGroup difficultyGroup = new ButtonGroup();
        JRadioButton easy = new JRadioButton("Easy");
        JRadioButton medium = new JRadioButton("Medium");
        JRadioButton hard = new JRadioButton("Hard");

        difficultyGroup.add(easy);
        difficultyGroup.add(medium);
        difficultyGroup.add(hard);
        window.add(difficultyPanel);
        easy.setBounds(75,50,100,30);
        medium.setBounds(75,75,100,30);
        hard.setBounds(75,100,100,30);
        lbl.setBounds(75,450,100,30);



        dbar = new JLabel("Selected: ");


        window.add(easy);
        window.add(medium);
        window.add(hard);
        window.add(dbar);




        // Compile and place window on screen
        window.pack();
        window.setLocationRelativeTo(null);
    }

    private void setVisible(boolean b) {
    }

    public void difficultyButtonStatus(ItemEvent e) {

        int sel = e.getStateChange();

        if (sel == ItemEvent.SELECTED) {

            JRadioButton button = (JRadioButton) e.getSource();
            String text = button.getText();

            StringBuilder sb = new StringBuilder("Selected: ");
            sb.append(text);

            dbar.setText(sb.toString());
        }
    }

    public static void main(String args[]) {

        EventQueue.invokeLater(() -> {
            MazeDisplay UI = new MazeDisplay();
            UI.initializeUI();
            UI.setVisible(true);
        });
    }


}
