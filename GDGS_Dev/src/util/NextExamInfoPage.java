package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class NextExamInfoPage {

    JFrame frame;
    String department;

    public NextExamInfoPage(String department) {
        this.department = department;  // Get the selected department
        frame = new JFrame("Next Exam Information - " + department);

        // Create table model for the exams
        DefaultTableModel tableModel = new DefaultTableModel();

        // Add columns to the table model
        tableModel.addColumn("No");
        tableModel.addColumn("Date");
        tableModel.addColumn("Exam Type");
        tableModel.addColumn("Department");
        tableModel.addColumn("Time");
        tableModel.addColumn("Syllabus");

        // Fetch exams for the selected department
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT no, date, exam_type, department, time, syllabus FROM next_exam_info WHERE department = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, department);
            ResultSet rs = stmt.executeQuery();

            // Check if the ResultSet is empty
            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(frame, "No exam information found for department: " + department, "No Data", JOptionPane.INFORMATION_MESSAGE);
            }

            // Populate the table model with data from the result set
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(String.valueOf(rs.getInt("no")));
                row.add(rs.getDate("date").toString());
                row.add(rs.getString("exam_type"));
                row.add(rs.getString("department"));
                row.add(rs.getTime("time").toString());
                row.add(rs.getString("syllabus"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Print the SQL exception stack trace
        }

        // Create JTable with the table model
        JTable table = new JTable(tableModel);

        // Set custom styling for the table
        table.setFont(new Font("Arial", Font.PLAIN, 14)); // Font for table content
        table.setRowHeight(30); // Row height for better readability
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 16)); // Font for table headers
        tableHeader.setBackground(new Color(51, 153, 255));  // Header background color
        tableHeader.setForeground(Color.WHITE); // Header text color

        // Adjust column widths automatically
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);  // No column width
        columnModel.getColumn(1).setPreferredWidth(100); // Date column width
        columnModel.getColumn(2).setPreferredWidth(150); // Exam Type column width
        columnModel.getColumn(3).setPreferredWidth(100); // Department column width
        columnModel.getColumn(4).setPreferredWidth(100); // Time column width
        columnModel.getColumn(5).setPreferredWidth(200); // Syllabus column width

        // Create scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create a back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> frame.dispose());  // Close the current window
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Make the frame full-screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        frame.setUndecorated(true); // Remove window decorations

        // Set frame properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
