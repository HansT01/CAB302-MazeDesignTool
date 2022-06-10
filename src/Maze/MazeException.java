package Maze;

import javax.swing.*;
import java.awt.*;

public class MazeException extends Exception {
    /**
     * Main constructor for MazeException. Prints out the input message to the console.
     * @param message Error message.
     */
    public MazeException(String message) {
        System.out.println(message);
    }

    /**
     * Alternative constructor when no error message is provided.
     */
    public MazeException() {
        System.out.println("Maze exception has occurred");
    }
}