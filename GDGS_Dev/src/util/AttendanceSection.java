package util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AttendanceSection {
    JFrame frame;
    JTextField emailField, dateField;
    JButton checkButton, backButton;
    JLabel statusLabel;

    public AttendanceSection(String email) {
        frame = new JFrame("Attendance Section");
        emailField = new JTextField();
        dateField = new JTextField();
        checkButton = new JButton("Check Attendance");
        backButton = new JButton("Back");
        statusLabel = new JLabel("Status: ");

        // Set layout and bounds
        frame.setLayout(null);
        emailField.setBounds(300, 150, 200, 30);
        dateField.setBounds(300, 200, 200, 30);
        checkButton.setBounds(300, 250, 200, 30);
        backButton.setBounds(100, 300, 100, 30);
        statusLabel.setBounds(300, 300, 400, 30);

        // Add components to the frame
        frame.add(emailField);
        frame.add(dateField);
        frame.add(checkButton);
        frame.add(backButton);
        frame.add(statusLabel);

        // Check Button ActionListener
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredEmail = emailField.getText();
                String enteredDate = dateField.getText();
                checkAttendance(enteredEmail, enteredDate);
            }
        });

        // Back Button ActionListener
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new inStudentSection(email);  // Navigate back to the previous screen
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
            ps.setString(1, email);
            ps.setString(2, date);

            // Execute query
            rs = ps.executeQuery();

            // Display status
            if (rs.next()) {
                String status = rs.getString("status");
                statusLabel.setText("Status: " + status);
            } else {
                statusLabel.setText("No attendance record found for the given email and date.");
            }

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
