package util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class UniversityUpdates {

    JFrame updatesFrame;

    public UniversityUpdates() {
        updatesFrame = new JFrame("University Updates");

        // Create table model for the updates
        DefaultTableModel tableModel = new DefaultTableModel();

        // Add columns to the table model
        tableModel.addColumn("Update ID");
        tableModel.addColumn("Update Title");
        tableModel.addColumn("Update Content");
        tableModel.addColumn("Update Date");

        // Fetch updates from the database and add them to the table model
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT update_id, update_title, update_content, update_date FROM university_updates";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (!rs.isBeforeFirst()) {  // Check if the ResultSet is empty
                JOptionPane.showMessageDialog(updatesFrame, "No university updates found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

            // Populate the table model with data from the result set
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(String.valueOf(rs.getInt("update_id")));
                row.add(rs.getString("update_title"));
                row.add(rs.getString("update_content"));
                row.add(rs.getDate("update_date").toString());
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Print the SQL exception stack trace
            JOptionPane.showMessageDialog(updatesFrame, "Error fetching updates.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Create JTable with the table model
        JTable updatesTable = new JTable(tableModel);

        // Set custom styling for the table content
        updatesTable.setFont(new Font("Arial", Font.PLAIN, 16)); // Larger font for table content
        updatesTable.setRowHeight(35); // Row height for better readability

        // Set custom styling for the table header
        JTableHeader tableHeader = updatesTable.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 18)); // Font for table headers
        tableHeader.setBackground(new Color(70, 130, 180));  // Header background color (steel blue)
        tableHeader.setForeground(Color.WHITE); // Header text color

        // Center the table content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < updatesTable.getColumnCount(); i++) {
            updatesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Create scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(updatesTable);

        // Add the table to the frame
        updatesFrame.add(scrollPane, BorderLayout.CENTER);

        // Create and style the Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(220, 20, 60));  // Crimson background color
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(100, 40));  // Set size of the button

        // Add action listener for the Back button to close the current window
        backButton.addActionListener(e -> updatesFrame.dispose());

        // Create a panel to place the Back button at the right bottom
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(backButton, BorderLayout.EAST);

        // Add the back button panel to the frame
        updatesFrame.add(bottomPanel, BorderLayout.SOUTH);

        // Make the frame full-screen
        updatesFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen

        // Set frame properties
        updatesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        updatesFrame.setVisible(true);
    }
}
