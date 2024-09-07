package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class ClassUpdatesPage {

    JFrame updatesFrame;
    String loggedInEmail;

    public ClassUpdatesPage(String email) {
        this.loggedInEmail = email;  // Get the logged-in email from the calling section
        updatesFrame = new JFrame("Class Updates");
    
        // Create table model for the updates
        DefaultTableModel tableModel = new DefaultTableModel();
    
        // Add columns to the table model
        tableModel.addColumn("ID");
        tableModel.addColumn("Class Name");
        tableModel.addColumn("Update Details");
        tableModel.addColumn("Update Date");
    
        String className = getClassNameByEmail(loggedInEmail);
    
        if (className != null) {
            // Fetch updates from the database and add to the table model
            try (Connection conn = DBConnection.getConnection()) {
    
                // Select updates based on the class name (department)
                String query = "SELECT id, class_name, update_details, update_date FROM class_updates WHERE class_name = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, className);
                ResultSet rs = stmt.executeQuery();
    
                if (!rs.isBeforeFirst()) {  // Check if the ResultSet is empty
                    JOptionPane.showMessageDialog(updatesFrame, "No class updates found for class: " + className, "Info", JOptionPane.INFORMATION_MESSAGE);
                }
    
                // Populate the table model with data from the result set
                while (rs.next()) {
                    Vector<String> row = new Vector<>();
                    row.add(String.valueOf(rs.getInt("id")));
                    row.add(rs.getString("class_name"));
                    row.add(rs.getString("update_details"));
                    row.add(rs.getDate("update_date").toString());
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                e.printStackTrace();  // Print the SQL exception stack trace
                JOptionPane.showMessageDialog(updatesFrame, "Error fetching class updates.", "Error", JOptionPane.ERROR_MESSAGE);
            }
    
            // Create JTable with the table model
            JTable updatesTable = new JTable(tableModel);
    
            // Set custom styling for the table
            updatesTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Font for table content
            updatesTable.setRowHeight(30); // Row height for better readability
            JTableHeader tableHeader = updatesTable.getTableHeader();
            tableHeader.setFont(new Font("Arial", Font.BOLD, 16)); // Font for table headers
            tableHeader.setBackground(new Color(51, 153, 255));  // Header background color
            tableHeader.setForeground(Color.WHITE); // Header text color
    
            // Adjust column widths automatically
            updatesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            TableColumnModel columnModel = updatesTable.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(50);  // ID column width
            columnModel.getColumn(1).setPreferredWidth(150); // Class Name column width
            columnModel.getColumn(2).setPreferredWidth(400); // Update Details column width
            columnModel.getColumn(3).setPreferredWidth(150); // Update Date column width
    
            // Create scroll pane for the table
            JScrollPane scrollPane = new JScrollPane(updatesTable);
            updatesFrame.add(scrollPane, BorderLayout.CENTER);
    
            // Make the frame full-screen
            updatesFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
    
            // Set frame properties
            updatesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
            updatesFrame.setVisible(true);
        } else {
            // Show error message if class name is not found for the provided email
            JOptionPane.showMessageDialog(updatesFrame, "Class name not found for the provided email.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    // Method to get the class_name (department) based on the email
    private String getClassNameByEmail(String email) {
        String[] studentTables = {"student_it", "student_ce", "student_cd", "student_cs"};
        String className = null;

        try (Connection conn = DBConnection.getConnection()) {
            for (String table : studentTables) {
                String query = "SELECT class_name FROM " + table + " WHERE email_id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {  // If a record is found, get the class name
                    className = rs.getString("class_name");
                    break;  // Stop searching once we find the class name
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return className;
    }
}
