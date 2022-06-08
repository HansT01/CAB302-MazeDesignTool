package Maze;

import javax.swing.*;
import java.awt.*;

public class MazeException extends Exception {
    public MazeException(String message) {
        System.out.println(message);
    }
    public MazeException() {
        System.out.println("Maze.Maze exception has occurred");
    }
}