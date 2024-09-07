package util;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClassUpdateEntryPage {

    private JFrame frame;
    private JTextField classNameField;
    private JTextArea updateDetailsArea;
    private JDateChooser updateDateChooser; // Ensure this import is correct

    public ClassUpdateEntryPage() {
        frame = new JFrame("Add Class Update");

        // Create components
        JLabel classNameLabel = new JLabel("Class Name:");
        classNameField = new JTextField(30);

        JLabel updateDetailsLabel = new JLabel("Update Details:");
        updateDetailsArea = new JTextArea(5, 30);
        updateDetailsArea.setLineWrap(true);
        updateDetailsArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(updateDetailsArea);

        JLabel updateDateLabel = new JLabel("Update Date:");
        updateDateChooser = new JDateChooser(); // Use JDateChooser

        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");

        // Set layout and add components
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(classNameLabel, gbc);

        gbc.gridx = 1;
        frame.add(classNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(updateDetailsLabel, gbc);

        gbc.gridx = 1;
        frame.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(updateDateLabel, gbc);

        gbc.gridx = 1;
        frame.add(updateDateChooser, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        frame.add(submitButton, gbc);

        gbc.gridx = 0;
        frame.add(backButton, gbc);

        // Set button actions
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitUpdate();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // Assuming you have a method to return to previous menu
                // Adjust to your context
            }
        });

        // Frame settings
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void submitUpdate() {
        String className = classNameField.getText();
        String updateDetails = updateDetailsArea.getText();
        java.sql.Date updateDate = new java.sql.Date(updateDateChooser.getDate().getTime());

        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO class_updates (class_name, update_details, update_date) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, className);
            stmt.setString(2, updateDetails);
            stmt.setDate(3, updateDate);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(frame, "Class update added successfully!");
                // Clear fields after successful insertion
                classNameField.setText("");
                updateDetailsArea.setText("");
                updateDateChooser.setDate(null);
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to add class update. Please try again.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new ClassUpdateEntryPage();
    }
}
