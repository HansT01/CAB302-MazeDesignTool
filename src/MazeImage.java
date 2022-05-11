import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MazeImage {
    private final BufferedImage imageData;
    private int sizeX;
    private int sizeY;
    private int x;
    private int y;
    private boolean isPlaced = false;

    public boolean isPlaced() {
        return isPlaced;
    }

    public void setPlaced(boolean placed) {
        isPlaced = placed;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public BufferedImage getImageData() {
        return imageData;
    }

    public MazeImage(BufferedImage imageData, int sizeX, int sizeY)
    {
        this.imageData = imageData;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public static void main(String[] args) {
        // Generate maze panel
        Maze testMaze = new Maze("Maze Title", "Maze Author", 5,5);
        testMaze.GenerateMaze();
        MazePanel mazePanel = new MazePanel(testMaze, 64);

        // Create new frame
        JFrame frame = new JFrame();

        // Automatically centres content to frame
        frame.setLayout(new GridBagLayout());
        frame.add(mazePanel, new GridBagConstraints());

        // Resizes window to preferred dimensions
        frame.pack();

        // Centre to screen
        frame.setLocationRelativeTo(null);

        // Set defaults
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create an image
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Static image files", "jpeg", "jpg", "png"));
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));

        int returnVal = fc.showOpenDialog(frame);
        System.out.println("Import image button clicked");

        // https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            System.out.println("Opening: " + file.getName());

            try {
                BufferedImage imageData = ImageIO.read(file);
                MazeImage image = new MazeImage(imageData, 3, 3);
                testMaze.getImages().add(image);
                testMaze.setSelectedImage(image);
                testMaze.PlaceImage(1, 1);
                mazePanel.repaint();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {
            System.out.println("Open command cancelled by user");
        }
    }
}
