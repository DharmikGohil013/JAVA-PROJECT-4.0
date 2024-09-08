package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AllAdminProfilesSection {

    JFrame frame;
    JTable table;

    public AllAdminProfilesSection() {
        frame = new JFrame("All Admin Profiles");
        frame.setLayout(new BorderLayout());

        // Create a table model with column names
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Phone Number"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add scroll pane to frame
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create and position the back button
        JButton backButton = new JButton("Back");
        frame.add(backButton, BorderLayout.SOUTH);

        // Fetch and display all admin profiles
        fetchAllAdminProfiles(tableModel);

        // Button action listener
        backButton.addActionListener(e -> {
            frame.dispose();  // Close the current AllAdminProfilesSection window
            new inAdminSection("admin_email@example.com");  // Return to the admin section (replace with actual email)
        });

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private void fetchAllAdminProfiles(DefaultTableModel tableModel) {
        String url = "jdbc:mysql://localhost:3306/test_db"; // Update with your database URL
        String user = "root"; // Update with your database username
        String password = "DHARMIKgohil@2006"; // Update with your database password

        String query = "SELECT id, name, email, mo_number FROM admin";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("mo_number");

                // Add row to table model
                tableModel.addRow(new Object[]{id, name, email, phoneNumber});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error fetching admin profiles", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AllAdminProfilesSection();  // Display all admin profiles
    }
}
