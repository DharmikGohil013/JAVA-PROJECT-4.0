package util;

import javax.swing.*;
import java.awt.*;

public class infacultysection {
    JFrame frame;

    public infacultysection() {
        // Initialize the frame
        frame = new JFrame("Faculty Section");

        // Set layout to null for manual button positioning
        frame.setLayout(null);

        // Create 24 buttons for the Faculty Section
        JButton[] buttons = new JButton[24];
        String[] buttonNames = {
                "Profile", "Attendance", "Grades", "Time Table", "Meetings", "Notices",
                "Research", "Publications", "Faculty Events", "Schedules", "Salary Details", "Leaves",
                "Department Info", "Upload Material", "Assignments", "Lectures", "Feedback", "Students",
                "University Updates", "Workshops", "Seminars", "Projects", "Logout", "Exit"
        };

        // Set button size, font, and colors
        Dimension buttonSize = new Dimension(200, 50);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Color buttonColorPrimary = new Color(70, 130, 180);  // Steel blue
        Color buttonColorSecondary = new Color(60, 179, 113); // Medium sea green
        Color textColor = Color.WHITE;

        // Initialize each button and apply styles
        for (int i = 0; i < 24; i++) {
            buttons[i] = new JButton(buttonNames[i]);
            buttons[i].setPreferredSize(buttonSize);
            buttons[i].setFont(buttonFont);
            buttons[i].setForeground(textColor);
            buttons[i].setFocusPainted(false); // Remove button border on click
            buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor
            if (i < 12) {
                buttons[i].setBackground(buttonColorPrimary); // Primary color for the first 12 buttons
            } else {
                buttons[i].setBackground(buttonColorSecondary); // Secondary color for the rest
            }
        }

        // Position buttons: 4 rows, 6 buttons per row
        int xStart = 200, yStart = 100, xSpacing = 250, ySpacing = 100;
        int row = 0, col = 0;

        for (int i = 0; i < buttons.length; i++) {
            JButton button = buttons[i];
            button.setBounds(xStart + (col * xSpacing), yStart + (row * ySpacing), buttonSize.width, buttonSize.height);
            frame.add(button);

            col++;
            if (col == 6) {  // Move to the next row after 6 buttons
                col = 0;
                row++;
            }
        }

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new infacultysection();  // Initialize the Faculty Section
    }
}
