package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FacultyNoticeSection extends JFrame {

    // Components
    private JTable noticeTable;
    private JButton addButton, deleteButton, backButton;
    private JTextField titleField;
    private JTextArea descriptionArea;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_db"; // Replace with your DB URL
    private static final String DB_USER = "root"; // Replace with your DB username
    private static final String DB_PASSWORD = "DHARMIKgohil@2006"; // Replace with your DB password

    public FacultyNoticeSection() {
        // Set up the frame
        setTitle("Faculty Notice Section");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set layout
        setLayout(new BorderLayout());

        // Create components
        noticeTable = new JTable();
        addButton = new JButton("Add Notice");
        deleteButton = new JButton("Delete Notice");
        backButton = new JButton("Back");
        titleField = new JTextField(20);
        descriptionArea = new JTextArea(5, 20);

        // Create panel for adding notices
        JPanel addNoticePanel = new JPanel();
        addNoticePanel.setLayout(new GridLayout(3, 2));
        addNoticePanel.add(new JLabel("Title:"));
        addNoticePanel.add(titleField);
        addNoticePanel.add(new JLabel("Description:"));
        addNoticePanel.add(new JScrollPane(descriptionArea));

        // Add components to the frame
        add(new JScrollPane(noticeTable), BorderLayout.CENTER);
        add(addNoticePanel, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Fetch and display notices
        fetchNotices();

        // Add action listener to the add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNotice();
            }
        });

        // Add action listener to the delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteNotice();
            }
        });

        // Back button logic
        backButton.addActionListener(e -> dispose()); // Close the current window

        // Set frame to be visible
        setVisible(true);
    }

    // Method to fetch and display notices
    private void fetchNotices() {
        String query = "SELECT id, title, description, post_date FROM notices";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Title", "Description", "Posted Date"}, 0);
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                Timestamp postDate = rs.getTimestamp("post_date");
                model.addRow(new Object[]{id, title, description, postDate});
            }
            noticeTable.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching notices: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to add a new notice
    private void addNotice() {
        String title = titleField.getText();
        String description = descriptionArea.getText();

        if (title.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and Description are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "INSERT INTO notices (title, description, posted_by) VALUES (?, ?, 'faculty')";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Notice added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            fetchNotices(); // Refresh the table

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding notice: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to delete a selected notice
    private void deleteNotice() {
        int selectedRow = noticeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a notice to delete!", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int noticeId = (int) noticeTable.getValueAt(selectedRow, 0); // Get the ID of the selected notice

        String query = "DELETE FROM notices WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, noticeId);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Notice deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            fetchNotices(); // Refresh the table

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting notice: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Main method to run the example
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FacultyNoticeSection::new);
    }
}
