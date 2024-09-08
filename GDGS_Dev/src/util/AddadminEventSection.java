package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import com.toedter.calendar.JDateChooser;

public class AddadminEventSection {

    private JFrame frame;
    private JTextField eventNameField;
    private JTextArea eventDescriptionArea;
    private JDateChooser eventDateChooser;
    private JComboBox<String> departmentComboBox;

    public AddadminEventSection() {
        frame = new JFrame("Add Event");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create and add components
        JLabel nameLabel = new JLabel("Event Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(nameLabel, gbc);

        eventNameField = new JTextField(20);
        gbc.gridx = 1;
        frame.add(eventNameField, gbc);

        JLabel dateLabel = new JLabel("Event Date:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(dateLabel, gbc);

        eventDateChooser = new JDateChooser();
        eventDateChooser.setDateFormatString("yyyy-MM-dd");
        gbc.gridx = 1;
        frame.add(eventDateChooser, gbc);

        JLabel descriptionLabel = new JLabel("Event Description:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(descriptionLabel, gbc);

        eventDescriptionArea = new JTextArea(5, 20);
        eventDescriptionArea.setLineWrap(true);
        eventDescriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(eventDescriptionArea);
        gbc.gridx = 1;
        frame.add(scrollPane, gbc);

        JLabel departmentLabel = new JLabel("Department:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(departmentLabel, gbc);

        String[] departments = {"IT", "CE", "CS", "CD", "None"};
        departmentComboBox = new JComboBox<>(departments);
        gbc.gridx = 1;
        frame.add(departmentComboBox, gbc);

        JButton submitButton = new JButton("Add Event");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(submitButton, gbc);

        JButton backButton = new JButton("Back");
        gbc.gridy = 5;
        frame.add(backButton, gbc);

        // Add action listeners
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEvent();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current frame
                // Optionally open the previous section, e.g., new AdminSection();
            }
        });

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addEvent() {
        String eventName = eventNameField.getText();
        java.util.Date selectedDate = eventDateChooser.getDate();
        String eventDate = selectedDate != null ? new java.sql.Date(selectedDate.getTime()).toString() : "";
        String eventDescription = eventDescriptionArea.getText();
        String department = (String) departmentComboBox.getSelectedItem();

        // Validate inputs
        if (eventName.isEmpty() || eventDate.isEmpty() || eventDescription.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert into database
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password")) {
            String query = "INSERT INTO event (event_name, event_date, event_description, department) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, eventName);
            statement.setString(2, eventDate);
            statement.setString(3, eventDescription);
            statement.setString(4, department);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(frame, "Event added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Error adding event", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AddadminEventSection(); // Display the add event section
    }
}
