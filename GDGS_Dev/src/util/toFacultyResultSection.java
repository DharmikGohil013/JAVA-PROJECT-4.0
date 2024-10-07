package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class toFacultyResultSection extends JFrame {
    // GUI components
    private JTextField emailField, semesterField, resultField;
    private JButton addResultButton, backButton;

    // Constructor
    public toFacultyResultSection() {
        // Initialize GUI components
        initializeComponents();
    }

    private void initializeComponents() {
        // Set frame title
        setTitle("Add Result");

        // Create labels
        JLabel emailLabel = new JLabel("Email ID:");
        JLabel semesterLabel = new JLabel("Semester:");
        JLabel resultLabel = new JLabel("Result:");

        // Create text fields
        emailField = new JTextField(20);
        semesterField = new JTextField(20);
        resultField = new JTextField(20);

        // Create buttons
        addResultButton = createLargeButton("Add Result");
        backButton = createLargeButton("Back");

        // Action listener for adding results
        addResultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addResult();
            }
        });

        // Action listener for back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the window
            }
        });

        // Layout setup
        setLayout(new GridLayout(4, 2, 10, 10)); // 4 rows, 2 columns with gaps
        add(emailLabel);
        add(emailField);
        add(semesterLabel);
        add(semesterField);
        add(resultLabel);
        add(resultField);
        add(addResultButton);
        add(backButton);

        // Frame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    // Method to create a large button with max width
    private JButton createLargeButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18)); // Bigger font for buttons
        return button;
    }

    // Method to add a result to the database
    private void addResult() {
        String email = emailField.getText();
        String semester = semesterField.getText();
        String result = resultField.getText();

        // Input validation
        if (email.isEmpty() || semester.isEmpty() || result.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.");
            return;
        }

        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/your_database_name"; // Change to your database name
        String user = "your_username"; // Change to your username
        String password = "DHARMIKgohil@2006"; // Change to your password

        // SQL query to insert result
        String sql = "INSERT INTO results (email_id, semester, result) VALUES (?, ?, ?)";

        // Connecting to the database and executing the query
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setInt(2, Integer.parseInt(semester)); // Convert semester to int
            pstmt.setString(3, result);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Result added successfully!");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding result: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Semester must be a valid integer.");
        }
    }

    // Method to clear input fields
    private void clearFields() {
        emailField.setText("");
        semesterField.setText("");
        resultField.setText("");
    }

    public static void main(String[] args) {
        // Create and show the Faculty Result section window
        new toFacultyResultSection();
    }
}
