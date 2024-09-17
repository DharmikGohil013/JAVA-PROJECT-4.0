package util;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class FacultyPoliciesSection {

    JFrame frame;
    JTextArea policiesTextArea;

    public FacultyPoliciesSection() {
        frame = new JFrame("Faculty Policies");

        // Create a panel with a layout for components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create a text area for displaying policies
        policiesTextArea = new JTextArea();
        policiesTextArea.setEditable(false);  // Initially, the text area is not editable
        policiesTextArea.setLineWrap(true);  // Enable word wrapping
        policiesTextArea.setWrapStyleWord(true);

        // Create a scroll pane to make the text area scrollable
        JScrollPane scrollPane = new JScrollPane(policiesTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Create buttons for "Back" and "Edit"
        JButton backButton = new JButton("Back");
        JButton editButton = new JButton("Edit");

        // Panel for buttons at the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);
        buttonPanel.add(editButton);

        // Add components to the main panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to frame
        frame.add(panel);

        // Fetch policies from the database and display them
        fetchAndDisplayPolicies();

        // Action listeners for buttons
        backButton.addActionListener(e -> {
            frame.dispose();  // Close the current FacultyPoliciesSection window // Return to the admin section (replace with actual email)
        });

        editButton.addActionListener(e -> {
            frame.dispose();  // Close the current FacultyPoliciesSection window
           // Assuming you have a policyId already
int policyId = 1; // Replace this with the actual ID of the policy you want to edit

// Call the constructor with both the policy text and the policy ID
new EditFacultyPoliciesSection(policiesTextArea.getText(), policyId);
// Open the edit section with current policies
        });

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private void fetchAndDisplayPolicies() {
        String url = "jdbc:mysql://localhost:3306/test_db"; // Update with your database URL
        String user = "root"; // Update with your database username
        String password = "DHARMIKgohil@2006"; // Update with your database password

        String query = "SELECT title, description FROM faculty_policies";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            StringBuilder policies = new StringBuilder();

            // Loop through the result set and append policies to the text area
            while (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");

                policies.append(title).append(":\n").append(description).append("\n\n");
            }

            // Set the text in the text area
            policiesTextArea.setText(policies.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error fetching faculty policies", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new FacultyPoliciesSection();  // Show faculty policies
    }
}
