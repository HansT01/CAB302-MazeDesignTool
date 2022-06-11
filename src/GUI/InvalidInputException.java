package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Handles exceptions raised in the GUI.
 */
public class InvalidInputException extends Exception {
    /**
     * Main constructor for InvalidInputException. If a valid Component is passed,
     * a message dialogue will be used to notify the user of the error message.
     * @param message Error message
     * @param parentComponent Component object
     */
    public InvalidInputException(String message, Component parentComponent) {
        JOptionPane.showMessageDialog(parentComponent, message);
    }

    /**
     * Alternative constructor to print message to console.
     * @param message Error message
     */
    public InvalidInputException(String message) {
        System.out.println(message);
    }

    /**
     * Alternative constructor if no parameters are passed.
     */
    public InvalidInputException() {
        System.out.println("Invalid input exception has occurred");
    }
}
