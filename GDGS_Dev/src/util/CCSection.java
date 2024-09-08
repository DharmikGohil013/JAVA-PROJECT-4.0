package util;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CCSection {

    JFrame frame;
    String loggedInEmail;

    public CCSection(String email) {
        loggedInEmail = email;  // Get email from login section

        // Check if the user is CC
        if (!isUserCC(loggedInEmail)) {
            JOptionPane.showMessageDialog(null, "Access Denied. Only CCs can access this section.");
            new inStudentSection(loggedInEmail);  // Redirect to student section
            return;  // Exit the constructor
        }

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
        eventAttendanceButton.addActionListener(e -> new EventAttendanceSection(email));
        eventAddButton.addActionListener(e -> new AddEventSection(email));
        backButton.addActionListener(e -> {
            frame.dispose();  // Close the current CCSection window  // Return to the student section
        });

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    /**
     * Method to check if the logged-in user is a CC.
     * It checks the user's role in the student tables: student_it, student_ce, student_cd, student_cs.
     *
     * @param email The email of the logged-in user.
     * @return True if the user has the CC role, otherwise false.
     */
    private boolean isUserCC(String email) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
    
        String[] tables = {"student_it", "student_ce", "student_cd", "student_cs"};  // List of tables to check
    
        try {
            // Get database connection
            conn = DBConnection.getConnection();
    
            // Loop through each table to check if the user has the "CC" role
            for (String table : tables) {
                String query = "SELECT rol FROM " + table + " WHERE email = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, email);
    
                // Execute the query
                rs = ps.executeQuery();
    
                // If a matching record is found in the current table
                if (rs.next()) {
                    String role = rs.getString("rol");
                    if ("CC".equals(role)) {
                        return true;  // The user has the "CC" role
                    }
                }
            }
    
            // If no matching record with the "CC" role is found
            return false;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;  // Return false if any error occurs
    
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
