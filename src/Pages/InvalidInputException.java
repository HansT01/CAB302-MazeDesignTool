package Pages;

import javax.swing.*;
import java.awt.*;

public class InvalidInputException extends Exception {
    public InvalidInputException(String message, Component parentComponent) {
        JOptionPane.showMessageDialog(parentComponent, message);
    }
    public InvalidInputException(String message) {
        System.out.println(message);
    }
    public InvalidInputException() {
        System.out.println("Invalid input exception has occurred");
    }
}
