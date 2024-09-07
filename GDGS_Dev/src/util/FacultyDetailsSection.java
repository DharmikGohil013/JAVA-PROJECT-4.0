package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FacultyDetailsSection {

    JFrame frame;
    JTable table;
    String department;

    public FacultyDetailsSection(String loggedInEmail) {
        frame = new JFrame("Faculty Details");

        // Fetch the department of the logged-in user using their email
        department = getDepartmentByEmail(loggedInEmail);
        if (department == null) {
            JOptionPane.showMessageDialog(frame, "User department not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create table model
        DefaultTableModel model = new DefaultTableModel();

        // Add columns: Faculty details
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Email");
        model.addColumn("Mobile Number");
        model.addColumn("Role");

        // Fetch faculty data from the database for the user's department
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT id, name, email, mo_number, rol FROM faculty WHERE dept = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, department);
            ResultSet rs = stmt.executeQuery();

            // Fill the table model with faculty details
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("mo_number"),
                        rs.getString("rol")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create JTable with the model
        table = new JTable(model);
        table.setRowHeight(30);  // Set row height for better readability

        // Set table font and header style
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(51, 153, 255));
        table.getTableHeader().setForeground(Color.WHITE);

        // Add table to JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);  // Add table to the center of the frame

        // Add the "Back" button at the bottom-right corner
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setPreferredSize(new Dimension(100, 50));

        // Add action listener to the back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the faculty details window
            }
        });

        // Add the back button to the bottom-right corner
        bottomPanel.add(backButton, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);  // Add the panel to the bottom of the frame

        // Frame settings: Full screen mode
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  // Set frame to full screen
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private String getDepartmentByEmail(String email) {
        String[] tables = {"student_ce", "student_cs", "student_cd", "student_it"};
        String department = null;

        try (Connection conn = DBConnection.getConnection()) {
            for (String table : tables) {
                String query = "SELECT dept FROM " + table + " WHERE email = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    department = rs.getString("dept");
                    break;  // Exit the loop if the department is found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return department;
    }

    public static void main(String[] args) {
        new FacultyDetailsSection("user_cs@example.com");  // Test with a CS user's email
    }
}
