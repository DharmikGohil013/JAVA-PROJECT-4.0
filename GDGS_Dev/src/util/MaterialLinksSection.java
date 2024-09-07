package util;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MaterialLinksSection {
    JFrame frame;
    JTextArea materialsArea;
    JButton backButton;

    public MaterialLinksSection(String email) {
        frame = new JFrame("Material Links");
        frame.setLayout(null);

        // TextArea for displaying materials
        materialsArea = new JTextArea();
        materialsArea.setFont(new Font("Arial", Font.PLAIN, 18));
        materialsArea.setEditable(false); // Make it read-only
        JScrollPane scrollPane = new JScrollPane(materialsArea);
        scrollPane.setBounds(50, 50, 1200, 600); // Set size for full screen

        // Back Button
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setBounds(1650, 850, 150, 50);  // Position bottom-right
        backButton.setBackground(new Color(51, 153, 255)); // Button color
        backButton.setForeground(Color.WHITE);

        // Add action listener to the Back button
        backButton.addActionListener(e -> {
            frame.dispose();  // Close current window
            // You can add the action to return to the previous page (e.g., new inStudentSection(email))
        });

        frame.add(scrollPane);
        frame.add(backButton);

        // Fetch and display materials based on the user's department
        String department = getDepartmentByEmail(email);
        if (department != null) {
            fetchMaterials(department);
        } else {
            materialsArea.setText("No department information found for the email: " + email);
        }

        // Frame settings: Full-screen, layout, etc.
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set the frame to fullscreen
        frame.setVisible(true);
    }

    // Method to fetch student's department using their email
    private String getDepartmentByEmail(String email) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String[] tables = {"student_it", "student_ce", "student_cs", "student_cd"};
        String department = null;

        try {
            conn = DBConnection.getConnection();

            // Loop through each table to find the student's department
            for (String table : tables) {
                String query = "SELECT * FROM " + table + " WHERE email = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, email);
                rs = ps.executeQuery();

                if (rs.next()) {
                    // If a record is found, the department is based on the table name
                    department = table.split("_")[1].toUpperCase();  // Extracts IT, CE, CS, CD
                    break;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return department;
    }

    // Method to fetch materials based on the student's department
    private void fetchMaterials(String department) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String query = "SELECT * FROM materials WHERE class_name = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, department);
            rs = ps.executeQuery();

            // Build the materials text
            StringBuilder materialsText = new StringBuilder();
            while (rs.next()) {
                String title = rs.getString("title");
                String link = rs.getString("link");
                String uploadDate = rs.getString("upload_date");
                materialsText.append("Title: ").append(title).append("\n");
                materialsText.append("Link: ").append(link).append("\n");
                materialsText.append("Uploaded on: ").append(uploadDate).append("\n\n");
            }

            // If no materials are found, show a message
            if (materialsText.length() == 0) {
                materialsArea.setText("No materials available for your department.");
            } else {
                materialsArea.setText(materialsText.toString());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new MaterialLinksSection("user_email@example.com");  // Test with a user's email
    }
}
