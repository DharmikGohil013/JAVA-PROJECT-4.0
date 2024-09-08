package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AllFacultyDetailsSection {

    JFrame frame;
    JTable table;

    public AllFacultyDetailsSection() {
        frame = new JFrame("All Faculty Details");
        frame.setLayout(new BorderLayout());

        // Create a table model with column names
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Phone Number", "Role", "Department"}, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 18)); // Large font for readability
        table.setRowHeight(30); // Adjust row height for large font
        JScrollPane scrollPane = new JScrollPane(table);

        // Add scroll pane to frame
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create and position the back button
        JButton backButton = new JButton("Back");
        frame.add(backButton, BorderLayout.SOUTH);

        // Fetch and display all faculty details
        fetchAllFacultyDetails(tableModel);

        // Button action listener
        backButton.addActionListener(e -> {
            frame.dispose();  // Close the current AllFacultyDetailsSection window
            new inAdminSection("admin_email@example.com");  // Return to the admin section (replace with actual email)
        });

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private void fetchAllFacultyDetails(DefaultTableModel tableModel) {
        String url = "jdbc:mysql://localhost:3306/test_db"; // Update with your database URL
        String user = "root"; // Update with your database username
        String password = "DHARMIKgohil@2006"; // Update with your database password

        String query = "SELECT id, name, email, mo_number, rol, dept FROM faculty";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("mo_number");
                String role = rs.getString("rol");
                String department = rs.getString("dept");

                // Add row to table model
                tableModel.addRow(new Object[]{id, name, email, phoneNumber, role, department});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error fetching faculty details", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AllFacultyDetailsSection();  // Display all faculty details
    }
}
