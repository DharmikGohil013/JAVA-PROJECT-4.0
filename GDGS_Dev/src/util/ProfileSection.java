package util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfileSection {
    JFrame profileFrame;
    JLabel nameLabel, idLabel, emailLabel, roleLabel, departmentLabel;
    String email;

    public ProfileSection(String email) {
        this.email = email;
        profileFrame = new JFrame("Profile Section");
        JButton backButton;

        // Set up labels to display user details
        nameLabel = new JLabel("Name: ");
        idLabel = new JLabel("ID: ");
        emailLabel = new JLabel("Email: ");
        roleLabel = new JLabel("Role: ");
        departmentLabel = new JLabel("Department: ");
        backButton = new JButton("Back");

        // Position labels
        nameLabel.setBounds(300, 200, 400, 30);
        idLabel.setBounds(300, 250, 400, 30);
        emailLabel.setBounds(300, 300, 400, 30);
        roleLabel.setBounds(300, 350, 400, 30);
        departmentLabel.setBounds(300, 400, 400, 30);
        backButton.setBounds(100, 450, 100, 30); // Updated position for the back button

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current frame and open the previous screen
                profileFrame.dispose();
                new inStudentSection(email); // Navigate back to the previous screen
            }
        });

        // Add labels to the frame
        profileFrame.add(nameLabel);
        profileFrame.add(idLabel);
        profileFrame.add(emailLabel);
        profileFrame.add(roleLabel);
        profileFrame.add(departmentLabel);
        profileFrame.add(backButton);

        // Fetch and display user details based on the email
        showUserDetails(email);

        // Frame settings
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        profileFrame.setLayout(null);
        profileFrame.setVisible(true);
    }

    // Method to fetch user details from the database
    private void showUserDetails(String email) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String[] tables = {"student_it", "student_ce", "student_cd", "student_cs"};

        try {
            // Get database connection
            conn = DBConnection.getConnection();

            boolean userFound = false;

            for (String table : tables) {
                // Prepare the SQL query for each table
                String query = "SELECT id, name, email, rol, dept FROM " + table + " WHERE email = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, email);

                // Execute the query
                rs = ps.executeQuery();

                // If record is found, update the UI and break the loop
                if (rs.next()) {
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    String userEmail = rs.getString("email");
                    String role = rs.getString("rol");
                    String department = rs.getString("dept");

                    // Set the label texts
                    idLabel.setText("ID: " + id);
                    nameLabel.setText("Name: " + name);
                    emailLabel.setText("Email: " + userEmail);
                    roleLabel.setText("Role: " + role);
                    departmentLabel.setText("Department: " + department);

                    userFound = true;
                    System.out.println("User found in table: " + table);
                    break;  // Stop after finding the user
                }
            }

            // If no matching record is found in any table
            if (!userFound) {
                JOptionPane.showMessageDialog(null, "User not found in any table!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

