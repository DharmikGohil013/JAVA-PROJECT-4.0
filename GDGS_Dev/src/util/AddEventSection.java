package util;

import javax.swing.*;
import com.toedter.calendar.JCalendar;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class AddEventSection {

    JFrame frame;
    JTextField eventNameField;
    JTextArea eventDescriptionArea;
    JComboBox<String> departmentComboBox;
    JCalendar eventDatePicker;
    String loggedInEmail;

    public AddEventSection(String email) {
        loggedInEmail = email;

        frame = new JFrame("Add Event");
        frame.setLayout(null);

        // Labels
        JLabel eventNameLabel = new JLabel("Event Name:");
        JLabel eventDateLabel = new JLabel("Event Date:");
        JLabel eventDescriptionLabel = new JLabel("Event Description:");
        JLabel departmentLabel = new JLabel("Department:");

        // Set bounds for labels
        eventNameLabel.setBounds(100, 50, 200, 30);
        eventDateLabel.setBounds(100, 100, 200, 30);
        eventDescriptionLabel.setBounds(100, 150, 200, 30);
        departmentLabel.setBounds(100, 300, 200, 30);

        // Input fields
        eventNameField = new JTextField();
        eventDescriptionArea = new JTextArea();
        eventDescriptionArea.setLineWrap(true);
        eventDescriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(eventDescriptionArea);

        // Set input bounds
        eventNameField.setBounds(300, 50, 300, 30);
        eventDatePicker = new JCalendar(); // Date Picker
        eventDatePicker.setBounds(300, 100, 300, 200);
        descriptionScrollPane.setBounds(300, 150, 300, 100);

        // Department dropdown
        String[] departments = {"IT", "CS", "CE", "CD"};
        departmentComboBox = new JComboBox<>(departments);
        departmentComboBox.setBounds(300, 300, 300, 30);

        // Save and Back buttons
        JButton saveButton = new JButton("Save");
        JButton backButton = new JButton("Back");
        saveButton.setBounds(300, 400, 100, 30);
        backButton.setBounds(500, 400, 100, 30);

        // Add components to the frame
        frame.add(eventNameLabel);
        frame.add(eventDateLabel);
        frame.add(eventDescriptionLabel);
        frame.add(departmentLabel);
        frame.add(eventNameField);
        frame.add(eventDatePicker);
        frame.add(descriptionScrollPane);
        frame.add(departmentComboBox);
        frame.add(saveButton);
        frame.add(backButton);

        // Save button action listener
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveEvent();  // Save event details to the database
            }
        });

        // Back button action listener
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the current AddEventSection window
                new CCSection(loggedInEmail);  // Return to CC section
            }
        });

        // Frame settings
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Method to save event details in the database
    private void saveEvent() {
        String eventName = eventNameField.getText();
        String eventDescription = eventDescriptionArea.getText();
        String department = (String) departmentComboBox.getSelectedItem();
        
        // Format the selected date from JCalendar
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String eventDate = dateFormat.format(eventDatePicker.getDate());

        // Check if required fields are filled
        if (eventName.isEmpty() || eventDate.isEmpty() || department.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all required fields!", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // Get the database connection
            conn = DBConnection.getConnection();

            // SQL Insert query to save event
            String query = "INSERT INTO event (event_name, event_date, event_description, department) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, eventName);
            ps.setString(2, eventDate);
            ps.setString(3, eventDescription);
            ps.setString(4, department);

            // Execute the query
            int result = ps.executeUpdate();

            if (result > 0) {
                // Success message
                JOptionPane.showMessageDialog(frame, "Event added successfully!");
            } else {
                // Failure message
                JOptionPane.showMessageDialog(frame, "Failed to add event!", "Database Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            // Handle database errors
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
        new AddEventSection("user_email@example.com");  // Example of a logged-in user
    }
}
