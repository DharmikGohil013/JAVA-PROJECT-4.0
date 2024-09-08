package util;

import com.toedter.calendar.JCalendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventAttendanceSection {

    JFrame frame;
    String loggedInEmail;

    public EventAttendanceSection(String email) {
        loggedInEmail = email;  // Get email from the login section

        frame = new JFrame("Event Attendance");

        // Set the layout to null for manual positioning
        frame.setLayout(null);

        // Create Labels and TextFields for Event Attendance Form
        JLabel eventNameLabel = new JLabel("Event Name:");
        JTextField eventNameField = new JTextField();

        JLabel emailLabel = new JLabel("Participant Email:");
        JTextField emailField = new JTextField();

        JLabel startTimeLabel = new JLabel("Event Start Time (HH:MM:SS):");
        JTextField startTimeField = new JTextField();

        JLabel endTimeLabel = new JLabel("Event End Time (HH:MM:SS):");
        JTextField endTimeField = new JTextField();

        JLabel dateLabel = new JLabel("Event Date:");
        JCalendar calendar = new JCalendar();  // Using JCalendar to pick event date

        JButton submitButton = new JButton("Submit Attendance");
        JButton backButton = new JButton("Back");

        // Set component sizes, fonts, and positions
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        eventNameLabel.setFont(labelFont);
        emailLabel.setFont(labelFont);
        startTimeLabel.setFont(labelFont);
        endTimeLabel.setFont(labelFont);
        dateLabel.setFont(labelFont);

        // Set positions for each component
        int xStart = 300, yStart = 100, ySpacing = 50, labelWidth = 200, fieldWidth = 200;

        eventNameLabel.setBounds(xStart, yStart, labelWidth, 30);
        eventNameField.setBounds(xStart + labelWidth + 10, yStart, fieldWidth, 30);

        emailLabel.setBounds(xStart, yStart + ySpacing, labelWidth, 30);
        emailField.setBounds(xStart + labelWidth + 10, yStart + ySpacing, fieldWidth, 30);

        startTimeLabel.setBounds(xStart, yStart + ySpacing * 2, labelWidth, 30);
        startTimeField.setBounds(xStart + labelWidth + 10, yStart + ySpacing * 2, fieldWidth, 30);

        endTimeLabel.setBounds(xStart, yStart + ySpacing * 3, labelWidth, 30);
        endTimeField.setBounds(xStart + labelWidth + 10, yStart + ySpacing * 3, fieldWidth, 30);

        dateLabel.setBounds(xStart, yStart + ySpacing * 4, labelWidth, 30);
        calendar.setBounds(xStart + labelWidth + 10, yStart + ySpacing * 4, 250, 200);  // JCalendar for event date

        submitButton.setBounds(xStart, yStart + ySpacing * 7, fieldWidth, 30);
        backButton.setBounds(1600, 900, 100, 50);  // Back button at bottom-right corner

        // Add components to the frame
        frame.add(eventNameLabel);
        frame.add(eventNameField);
        frame.add(emailLabel);
        frame.add(emailField);
        frame.add(startTimeLabel);
        frame.add(startTimeField);
        frame.add(endTimeLabel);
        frame.add(endTimeField);
        frame.add(dateLabel);
        frame.add(calendar);  // Adding the JCalendar component
        frame.add(submitButton);
        frame.add(backButton);

        // Submit button action listener
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String eventName = eventNameField.getText();
                String participantEmail = emailField.getText();
                String startTime = startTimeField.getText();
                String endTime = endTimeField.getText();
                
                // Get the selected date from the JCalendar component
                Date selectedDate = calendar.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String eventDate = sdf.format(selectedDate);

                // Call method to save attendance to the database
                saveEventAttendance(eventName, participantEmail, startTime, endTime, eventDate, loggedInEmail);
            }
        });

        // Back button action listener
        backButton.addActionListener(e -> {
            frame.dispose();  // Close the current EventAttendanceSection window
            new CCSection(loggedInEmail);  // Return to the CC section
        });

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    // Method to save event attendance in the database
    private void saveEventAttendance(String eventName, String participantEmail, String startTime, String endTime, String eventDate, String ccEmail) {
        Connection conn = null;
        PreparedStatement ps = null;
    
        try {
            // Get the database connection
            conn = DBConnection.getConnection();
    
            // Validate input fields
            if (eventName.isEmpty() || participantEmail.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || eventDate.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required!", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
    
            // Check if time format is valid
            if (!startTime.matches("\\d{2}:\\d{2}:\\d{2}") || !endTime.matches("\\d{2}:\\d{2}:\\d{2}")) {
                JOptionPane.showMessageDialog(frame, "Invalid time format! Use HH:MM:SS.", "Time Format Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // SQL Insert query
            String query = "INSERT INTO event_attendance (event_name, email, event_start_time, event_end_time, event_date, cc_email) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
    
            // Set parameters
            ps.setString(1, eventName);
            ps.setString(2, participantEmail);
            ps.setString(3, startTime);
            ps.setString(4, endTime);
            ps.setString(5, eventDate);
            ps.setString(6, ccEmail);
    
            // Execute the query
            int result = ps.executeUpdate();
            if (result > 0) {
                // Success message
                JOptionPane.showMessageDialog(frame, "Attendance for " + eventName + " saved successfully!");
            } else {
                // No rows affected (unlikely in this case)
                JOptionPane.showMessageDialog(frame, "Failed to save attendance!", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
    
        } catch (SQLException ex) {
            // Detailed error handling for database exceptions
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
    
        } finally {
            // Close resources
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    

    public static void main(String[] args) {
        new EventAttendanceSection("cc_email@example.com");  // Assume the CC is logged in
    }
}
