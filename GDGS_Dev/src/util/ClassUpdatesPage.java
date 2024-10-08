package util;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClassUpdatesPage {
    JFrame frame;
    JTextArea updatesArea;
    JButton backButton;

    public ClassUpdatesPage(String email) {
        frame = new JFrame("Class Updates");
        frame.setLayout(null);

        // TextArea for displaying updates
        updatesArea = new JTextArea();
        updatesArea.setFont(new Font("Arial", Font.PLAIN, 18));
        updatesArea.setEditable(false); // Make it read-only
        JScrollPane scrollPane = new JScrollPane(updatesArea);
        scrollPane.setBounds(50, 50, 1200, 600); // Temporary bounds; auto adjusted for full screen

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

        // Fetch and display updates based on the user's department
        String department = getDepartmentByEmail(email);
        if (department != null) {
            fetchClassUpdates(department);
        } else {
            updatesArea.setText("No department information found for the email: " + email);
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

    // Method to fetch class updates based on the student's department
    private void fetchClassUpdates(String department) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String query = "SELECT * FROM class_updates WHERE class_name = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, department);
            rs = ps.executeQuery();

            // Build the updates text
            StringBuilder updatesText = new StringBuilder();
            while (rs.next()) {
                String updateDetails = rs.getString("update_details");
                String updateDate = rs.getString("update_date");
                updatesText.append("Update: ").append(updateDetails).append("\n");
                updatesText.append("Date: ").append(updateDate).append("\n\n");
            }

            // If no updates are found, show a message
            if (updatesText.length() == 0) {
                updatesArea.setText("No updates available for your department.");
            } else {
                updatesArea.setText(updatesText.toString());
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
        new ClassUpdatesPage("user_email@example.com");  // Test with a user's email
    }
}
