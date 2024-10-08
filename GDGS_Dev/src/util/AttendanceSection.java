package util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AttendanceSection {
    JFrame frame;
    JTextField dateField;
    JButton checkButton, backButton;
    JLabel statusLabel;
    String loggedInEmail;

    public AttendanceSection(String email) {
        loggedInEmail = email;
        frame = new JFrame("Attendance Section");
        dateField = new JTextField();
        checkButton = new JButton("Check Attendance");
        backButton = new JButton("Back");
        statusLabel = new JLabel("Status: ");

        // Set layout and bounds
        frame.setLayout(null);
        dateField.setBounds(300, 200, 200, 30);
        checkButton.setBounds(300, 250, 200, 30);
        backButton.setBounds(100, 300, 100, 30);
        statusLabel.setBounds(300, 300, 400, 30);

        // Add components to the frame
        frame.add(dateField);
        frame.add(checkButton);
        frame.add(backButton);
        frame.add(statusLabel);

        // Check Button ActionListener
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredDate = dateField.getText();
                checkAttendance(loggedInEmail, enteredDate);
            }
        });

        // Back Button ActionListener
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new inStudentSection(loggedInEmail);  // Navigate back to the previous screen
            }
        });

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public void checkAttendance(String email, String date) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
    
        try {
            // Get database connection
            conn = DBConnection.getConnection();
    
            // Prepare SQL query
            String query = "SELECT status FROM attendance WHERE student_email = ? AND attendance_date = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, email.trim());
            ps.setString(2, date.trim());
    
            // Construct the final query string for debugging
            String finalQuery = "SELECT status FROM attendance WHERE student_email = '" + email.trim() + "' AND attendance_date = '" + date.trim() + "'";
            System.out.println("Final query for debugging: " + finalQuery);
    
            // Execute query
            rs = ps.executeQuery();
    
            // Display status
            if (rs.next()) {
                String status = rs.getString("status");
                statusLabel.setText("Status: " + status);
            } else {
                statusLabel.setText("No attendance record found for the given email and date.");
            }
    
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            statusLabel.setText("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error checking attendance.");
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    

    
    
}
