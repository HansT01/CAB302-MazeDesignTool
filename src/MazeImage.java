import javax.swing.*;

public class MazeImage {
    int imageData;
    int x;
    int y;
    int sizeX;
    int sizeY;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public MazeImage(int imageData, int sizeX, int sizeY)
    {
        this.imageData = imageData;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
}
