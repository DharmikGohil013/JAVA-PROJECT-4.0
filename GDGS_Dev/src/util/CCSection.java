package util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CCSection {

    JFrame frame;
    String loggedInEmail;

    public CCSection(String email) {
        loggedInEmail = email;  // Get email from login section

        frame = new JFrame("CC Section");

        // Set the layout to null for manual positioning
        frame.setLayout(null);

        // Create buttons
        JButton eventAttendanceButton = new JButton("Event Attendance");
        JButton eventAddButton = new JButton("Add Event");
        JButton backButton = new JButton("Back");

        // Set button size, font, and colors
        Dimension buttonSize = new Dimension(200, 50);
        Font buttonFont = new Font("Arial", Font.PLAIN, 16);
        Color buttonColor = new Color(51, 153, 255);  // Light blue color
        Color textColor = Color.WHITE;

        // Apply style to buttons
        JButton[] buttons = {eventAttendanceButton, eventAddButton, backButton};

        for (JButton button : buttons) {
            button.setPreferredSize(buttonSize);
            button.setFont(buttonFont);
            button.setBackground(buttonColor);
            button.setForeground(textColor);
            button.setFocusPainted(false); // Remove button border on click
        }

        // Set button positions
        int xStart = 300, yStart = 100, ySpacing = 70;
        eventAttendanceButton.setBounds(xStart, yStart, buttonSize.width, buttonSize.height);
        eventAddButton.setBounds(xStart, yStart + ySpacing, buttonSize.width, buttonSize.height);
        backButton.setBounds(1600, 900, 100, 50);  // Back button at bottom-right corner

        // Add buttons to the frame
        frame.add(eventAttendanceButton);
        frame.add(eventAddButton);
        frame.add(backButton);

        // Button action listeners
        // eventAttendanceButton.addActionListener(e -> new EventAttendanceSection(loggedInEmail));
        // eventAddButton.addActionListener(e -> new AddEventSection(loggedInEmail));
        backButton.addActionListener(e -> {
            frame.dispose();  // Close the current CCSection window
            new inStudentSection(loggedInEmail);  // Return to the student section
        });

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new CCSection("user_email@example.com");  // Assume the user is already logged in
    }
}
