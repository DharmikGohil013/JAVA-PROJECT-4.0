package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EditFacultyPoliciesSection {

    JFrame frame;
    JTextArea editTextArea;
    int policyId;  // Assuming you are editing a specific policy by ID

    public EditFacultyPoliciesSection(String initialPolicies, int policyId) {
        this.policyId = policyId;  // Store the policy ID

        frame = new JFrame("Edit Faculty Policies");
        
        // Create a panel with a layout for components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create a text area for editing policies
        editTextArea = new JTextArea(initialPolicies);  // Pre-fill with the current policies
        editTextArea.setLineWrap(true);
        editTextArea.setWrapStyleWord(true);
        
        // Create a scroll pane for the text area
        JScrollPane scrollPane = new JScrollPane(editTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Create buttons for "Save" and "Back"
        JButton saveButton = new JButton("Save");
        JButton backButton = new JButton("Back");

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);
        buttonPanel.add(saveButton);

        // Add components to the main panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to frame
        frame.add(panel);

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);  // Set frame size to something manageable
        frame.setVisible(true);

        // Action listener for the "Back" button
        backButton.addActionListener(e -> {
            frame.dispose();  // Close the current EditFacultyPoliciesSection window
            new FacultyPoliciesSection();  // Go back to the Faculty Policies section
        });

        // Action listener for the "Save" button
        saveButton.addActionListener(e -> {
            String updatedPolicies = editTextArea.getText();  // Get the updated policies from the text area
            updatePoliciesInDatabase(updatedPolicies);  // Update the policies in the database
        });
    }

    // Code to update policies in the database
    private void updatePoliciesInDatabase(String updatedPolicies) {
        String url = "jdbc:mysql://localhost:3306/test_db"; // Update with your database URL
        String user = "root"; // Update with your database username
        String password = "DHARMIKgohil@2006"; // Update with your database password

        String updateQuery = "UPDATE faculty_policies SET description = ? WHERE id = ?"; // Modify as needed

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            // Set the updated description and the policy ID
            pstmt.setString(1, updatedPolicies);
            pstmt.setInt(2, policyId); // Use the policy ID provided

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Policies updated successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "No policy found with the given ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error updating faculty policies", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // For testing purposes, you can pass some dummy policies to edit
        // Here, we pass a dummy policy text and an example policyId (1)
        new EditFacultyPoliciesSection("Faculty Policy 1: ...\nFaculty Policy 2: ...", 1);
    }
}
