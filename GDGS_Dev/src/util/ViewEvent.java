package util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class ViewEvent {

    JFrame eventFrame;

    public  ViewEvent() {
        eventFrame = new JFrame("View Events");

        // Create table model for the events
        DefaultTableModel tableModel = new DefaultTableModel();

        // Add columns to the table model
        tableModel.addColumn("Event ID");
        tableModel.addColumn("Event Name");
        tableModel.addColumn("Event Date");
        tableModel.addColumn("Event Description");
        tableModel.addColumn("Department");

        // Fetch events from the database and add them to the table model
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT event_id, event_name, event_date, event_description, department FROM event";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (!rs.isBeforeFirst()) {  // Check if the ResultSet is empty
                JOptionPane.showMessageDialog(eventFrame, "No events found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

            // Populate the table model with data from the result set
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(String.valueOf(rs.getInt("event_id")));
                row.add(rs.getString("event_name"));
                row.add(rs.getDate("event_date").toString());
                row.add(rs.getString("event_description"));
                row.add(rs.getString("department"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Print the SQL exception stack trace
            JOptionPane.showMessageDialog(eventFrame, "Error fetching events.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Create JTable with the table model
        JTable eventTable = new JTable(tableModel);

        // Set custom styling for the table content
        eventTable.setFont(new Font("Arial", Font.PLAIN, 16)); // Larger font for table content
        eventTable.setRowHeight(35); // Row height for better readability

        // Set custom styling for the table header
        JTableHeader tableHeader = eventTable.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 18)); // Font for table headers
        tableHeader.setBackground(new Color(70, 130, 180));  // Header background color (steel blue)
        tableHeader.setForeground(Color.WHITE); // Header text color

        // Center the table content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < eventTable.getColumnCount(); i++) {
            eventTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Create scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(eventTable);

        // Add the table to the frame
        eventFrame.add(scrollPane, BorderLayout.CENTER);

        // Create and style the Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(220, 20, 60));  // Crimson background color
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(100, 40));  // Set size of the button

        // Add action listener for the Back button to close the current window
        backButton.addActionListener(e -> eventFrame.dispose());

        // Create a panel to place the Back button at the right bottom
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(backButton, BorderLayout.EAST);

        // Add the back button panel to the frame
        eventFrame.add(bottomPanel, BorderLayout.SOUTH);

        // Make the frame full-screen
        eventFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen

        // Set frame properties
        eventFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        eventFrame.setVisible(true);
    }
}
