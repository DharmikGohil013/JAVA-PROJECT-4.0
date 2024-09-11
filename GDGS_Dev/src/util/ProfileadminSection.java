package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class ProfileadminSection {

    JFrame frame;
    String loggedInEmail;  // Store the logged-in admin's email
    Image backgroundImage; // Background image

    public ProfileadminSection(String email) {
        loggedInEmail = email;  // Get email from login section

        frame = new JFrame("Admin Profile");

        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("D:\\Git Hub\\JAVA-PROJECT-4.0\\GDGS_Dev\\src\\util\\Profile.png"));  // Update with your image path
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a custom JPanel to display the background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        panel.setLayout(null);  // Use null layout for manual component placement
        frame.setContentPane(panel);

        // Create labels and text fields
        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel phoneLabel = new JLabel("Phone Number:");
        JLabel passwordLabel = new JLabel("Password:");

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        // Set text fields as uneditable
        nameField.setEditable(false);
        emailField.setEditable(false);
        phoneField.setEditable(false);
        passwordField.setEditable(false);

        // Set component bounds
        int labelWidth = 150;
        int fieldWidth = 300;
        int xStart = 100, yStart = 100, ySpacing = 40;

        nameLabel.setBounds(xStart, yStart, labelWidth, 30);
        nameField.setBounds(xStart + labelWidth, yStart, fieldWidth, 30);
        emailLabel.setBounds(xStart, yStart + ySpacing, labelWidth, 30);
        emailField.setBounds(xStart + labelWidth, yStart + ySpacing, fieldWidth, 30);
        phoneLabel.setBounds(xStart, yStart + ySpacing * 2, labelWidth, 30);
        phoneField.setBounds(xStart + labelWidth, yStart + ySpacing * 2, fieldWidth, 30);
        passwordLabel.setBounds(xStart, yStart + ySpacing * 3, labelWidth, 30);
        passwordField.setBounds(xStart + labelWidth, yStart + ySpacing * 3, fieldWidth, 30);

        // Create and position the back button
        JButton backButton = new JButton("Back");
        backButton.setBounds(1200, 900, 100, 50);  // Bottom-right corner

        // Add components to the panel
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(backButton);

        // Fetch and display admin profile information
        fetchProfileInfo(nameField, emailField, phoneField, passwordField);

        // Button action listeners
        backButton.addActionListener(e -> {
            frame.dispose();  // Close the current ProfileSection window
            new inAdminSection(loggedInEmail);  // Return to the admin section
        });

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private void fetchProfileInfo(JTextField nameField, JTextField emailField, JTextField phoneField, JPasswordField passwordField) {
        String url = "jdbc:mysql://localhost:3306/test_db"; // Update with your database URL
        String user = "root"; // Update with your database username
        String password = "DHARMIKgohil@2006"; // Update with your database password

        String query = "SELECT name, email, mo_number, password FROM admin WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, loggedInEmail);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("mo_number"));
                passwordField.setText(rs.getString("password"));
            } else {
                JOptionPane.showMessageDialog(frame, "No profile found for this email.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error fetching profile information", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ProfileadminSection("admin1@xyz.edu.in");  // Assume the user is already logged in as an admin
    }
}
