package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StudentDetailsTable {

    JFrame frame;
    JTable table;

    public StudentDetailsTable(String department) {
        frame = new JFrame("Student Details - " + department);
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
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setPreferredSize(new Dimension(100, 50));
        backButton.setBackground(new Color(255, 51, 51)); // Red color for back button
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        frame.add(backButton, BorderLayout.SOUTH);

        // Fetch and display student details for the selected department
        fetchStudentDetails(department, tableModel);

        // Button action listener
        backButton.addActionListener(e -> {
            frame.dispose();  // Close the current StudentDetailsTable window
              // Return to the department selection section
        });

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private void fetchStudentDetails(String department, DefaultTableModel tableModel) {
        String url = "jdbc:mysql://localhost:3306/test_db"; // Update with your database URL
        String user = "root"; // Update with your database username
        String password = "DHARMIKgohil@2006"; // Update with your database password

        String query = "";
        switch (department) {
            case "IT":
                query = "SELECT id, name, email, mo_number, rol, dept FROM student_it";
                break;
            case "CE":
                query = "SELECT id, name, email, mo_number, rol, dept FROM student_ce";
                break;
            case "CS":
                query = "SELECT id, name, email, mo_number, rol, dept FROM student_cs";
                break;
            case "CD":
                query = "SELECT id, name, email, mo_number, rol, dept FROM student_cd";
                break;
        }

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("mo_number");
                String role = rs.getString("rol");
                String dept = rs.getString("dept");

                // Add row to table model
                tableModel.addRow(new Object[]{id, name, email, phoneNumber, role, dept});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error fetching student details", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
