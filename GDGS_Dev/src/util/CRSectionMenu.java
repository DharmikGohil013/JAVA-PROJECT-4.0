package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CRSectionMenu {

    JFrame frame;
    String loggedInEmail;

    public CRSectionMenu(String email) {
        loggedInEmail = email;
        
        // Check if the user has CR role
        if (!isCRRole(loggedInEmail)) {
            JOptionPane.showMessageDialog(null, "You are not authorized to access this section.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }

        frame = new JFrame("CR Section Menu");
        frame.setLayout(new BorderLayout());  // Use BorderLayout for main frame

        // Create buttons for CR options
        JButton addClassUpdateButton = new JButton("Add Class Update");
        JButton addMaterialLinksButton = new JButton("Add Material Links");

        // Set button properties
        Dimension buttonSize = new Dimension(300, 60);
        Font buttonFont = new Font("Arial", Font.PLAIN, 18);
        Color buttonColor = new Color(51, 153, 255); // Light blue color
        Color textColor = Color.WHITE;

        JButton[] buttons = {addClassUpdateButton, addMaterialLinksButton};

        for (JButton button : buttons) {
            button.setPreferredSize(buttonSize);
            button.setFont(buttonFont);
            button.setBackground(buttonColor);
            button.setForeground(textColor);
            button.setFocusPainted(false); // Remove button border on click
        }

        // Set button positions
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Padding around buttons
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(addClassUpdateButton, gbc);
        gbc.gridy = 1;
        buttonPanel.add(addMaterialLinksButton, gbc);

        // Add buttons to the frame
        frame.add(buttonPanel, BorderLayout.CENTER);

        // Add Back button
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBackground(new Color(51, 153, 255));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false); // Remove button border on click

        // Add action listener for Back button
        backButton.addActionListener(e -> {
            frame.dispose(); // Close the current window
             // Replace with the actual previous window class
        });

        JPanel backButtonPanel = new JPanel();
        backButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 20)); // Align to the right with padding
        backButtonPanel.add(backButton);

        // Add Back button to the frame
        frame.add(backButtonPanel, BorderLayout.SOUTH);
        addClassUpdateButton.addActionListener(e-> new ClassUpdateEntryPage());
        addMaterialLinksButton.addActionListener(e-> new AddMaterialLinkPage());

        // Frame settings
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        frame.setVisible(true);
    }

    // Method to check if the user has CR role
    private boolean isCRRole(String email) {
        boolean isCR = false;
        String[] tables = {"student_it", "student_cd", "student_ce", "student_cs"};
        
        try (Connection conn = DBConnection.getConnection()) {
            for (String table : tables) {
                String query = "SELECT rol FROM " + table + " WHERE email = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
    
                if (rs.next()) {
                    String role = rs.getString("rol");
                    if ("CR".equalsIgnoreCase(role)) {
                        isCR = true;
                        break;  // Exit loop once CR role is found
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return isCR;
    }
    

    public static void main(String[] args) {
        new CRSectionMenu("23dit013@xyz.edu.in"); // Test with a specific email
    }
}
