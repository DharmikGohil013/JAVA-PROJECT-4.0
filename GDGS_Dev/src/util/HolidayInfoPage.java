package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class HolidayInfoPage {

    JFrame holidayFrame;

    public HolidayInfoPage(String department) {
        holidayFrame = new JFrame("Next Holiday Information for " + department);

        // Create table model for the holidays
        DefaultTableModel tableModel = new DefaultTableModel();

        // Add columns to the table model
        tableModel.addColumn("Holiday ID");
        tableModel.addColumn("Holiday Date");
        tableModel.addColumn("Description");

        // Fetch holiday information from the database and add to the table model
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT holiday_id, holiday_date, holiday_description FROM next_holiday WHERE department = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, department);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {  // Check if the ResultSet is empty
                JOptionPane.showMessageDialog(holidayFrame, "No holiday information found for the department: " + department);
            }

            // Populate the table model with data from the result set
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(String.valueOf(rs.getInt("holiday_id")));
                row.add(rs.getDate("holiday_date").toString());
                row.add(rs.getString("holiday_description"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create JTable with the table model
        JTable holidayTable = new JTable(tableModel);

        // Set custom styling for the table
        holidayTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Font for table content
        holidayTable.setRowHeight(30); // Row height for better readability
        JTableHeader tableHeader = holidayTable.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 16)); // Font for table headers
        tableHeader.setBackground(new Color(51, 153, 255));  // Header background color
        tableHeader.setForeground(Color.WHITE); // Header text color

        // Adjust column widths automatically
        holidayTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumnModel columnModel = holidayTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);  // Holiday ID column width
        columnModel.getColumn(1).setPreferredWidth(150); // Holiday Date column width
        columnModel.getColumn(2).setPreferredWidth(400); // Description column width

        // Create scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(holidayTable);
        holidayFrame.add(scrollPane, BorderLayout.CENTER);

        // Add Back button
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBackground(new Color(51, 153, 255));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false); // Remove button border on click
        backButton.addActionListener(e -> {
            holidayFrame.dispose();  // Close the current HolidayInfoPage window
            new NextHolidaySection();  // Open the main page
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        holidayFrame.add(buttonPanel, BorderLayout.SOUTH);

        // Frame settings
        holidayFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        holidayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        holidayFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new HolidayInfoPage("IT");  // Test with a specific department
    }
}
