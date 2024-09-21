package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class infacultysection extends JFrame {
    private Image backgroundImage;

    public infacultysection() {
        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("D:\\Git Hub\\JAVA-PROJECT-4.0\\GDGS_Dev\\src\\util\\faculty1.png"));  // Absolute path
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a custom JPanel for the background
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        panel.setLayout(null); // Set layout to null for manual button positioning

        // Create 30 buttons for the Faculty Section
        JButton[] buttons = new JButton[30];
        String[] buttonNames = {
                "Profile", "Attendance", "Grades", "Time Table", "Meetings", "Notices",
                "Research", "Publications", "Faculty Events", "Schedules", "Salary Details", "Leaves",
                "Department Info", "Upload Material", "Assignments", "Lectures", "Feedback", "Students",
                "University Updates", "Workshops", "Seminars", "Projects", "Logout", "Exit",
                "Course Plan", "Supervisions", "Exams", "Grading System", "Classroom Info", "Help Desk"
        };

        // Set button size, font, and colors
        Dimension buttonSize = new Dimension(200, 50);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Color buttonColorPrimary = new Color(70, 130, 180);  // Steel blue
        Color buttonColorSecondary = new Color(60, 179, 113); // Medium sea green
        Color textColor = Color.WHITE;

        // Initialize each button and apply styles
        for (int i = 0; i < 30; i++) {
            buttons[i] = new JButton(buttonNames[i]);
            buttons[i].setPreferredSize(buttonSize);
            buttons[i].setFont(buttonFont);
            buttons[i].setForeground(textColor);
            buttons[i].setFocusPainted(false); // Remove button border on click
            buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor
            if (i < 15) {
                buttons[i].setBackground(buttonColorPrimary); // Primary color for the first 15 buttons
            } else {
                buttons[i].setBackground(buttonColorSecondary); // Secondary color for the rest
            }
        }

        // Position buttons: 5 rows, 6 buttons per row
        int xStart = 200, yStart = 100, xSpacing = 250, ySpacing = 100;
        int row = 0, col = 0;

        for (int i = 0; i < buttons.length; i++) {
            JButton button = buttons[i];
            button.setBounds(xStart + (col * xSpacing), yStart + (row * ySpacing), buttonSize.width, buttonSize.height);
            panel.add(button); // Add buttons to the panel

            col++;
            if (col == 6) {  // Move to the next row after 6 buttons
                col = 0;
                row++;
            }
        }

        // Add the panel to the frame
        add(panel);

        // Frame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window
        setVisible(true);
    }

    public static void main(String[] args) {
        new infacultysection();  // Initialize the Faculty Section
    }
}
