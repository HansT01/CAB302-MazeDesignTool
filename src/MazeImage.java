import javax.swing.*;

public class MazeImage {
    private int imageData;
    private int x;
    private int y;
    private int sizeX;
    private int sizeY;

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

    public int getSizeY() {
        return sizeY;
    }

    public MazeImage(int imageData, int sizeX, int sizeY)
    {
        this.imageData = imageData;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
}
