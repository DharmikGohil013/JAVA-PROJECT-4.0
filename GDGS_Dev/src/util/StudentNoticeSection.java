package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StudentNoticeSection extends JFrame {

    // Components
    private JTable noticeTable;
    private JButton backButton;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_db"; // Replace with your DB URL
    private static final String DB_USER = "root"; // Replace with your DB username
    private static final String DB_PASSWORD = "DHARMIKgohil@2006"; // Replace with your DB password

    public StudentNoticeSection() {
        // Set up the frame
        setTitle("Student Notice Section");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set layout
        setLayout(new BorderLayout());

        // Create components
        noticeTable = new JTable();
        backButton = new JButton("Back");

        // Add components to the frame
        add(new JScrollPane(noticeTable), BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        // Fetch and display notices
        fetchNotices();

        // Back button logic
        backButton.addActionListener(e -> dispose()); // Close the current window

        // Set frame to be visible
        setVisible(true);
    }

    // Method to fetch and display notices
    private void fetchNotices() {
        String query = "SELECT title, description, post_date FROM notices";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            DefaultTableModel model = new DefaultTableModel(new String[]{"Title", "Description", "Posted Date"}, 0);
            while (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                Timestamp postDate = rs.getTimestamp("post_date");
                model.addRow(new Object[]{title, description, postDate});
            }
            noticeTable.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching notices: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Main method to run the example
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentNoticeSection::new);
    }
}
