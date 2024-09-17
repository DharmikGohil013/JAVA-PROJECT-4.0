package util;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class NextExamSection {

    JFrame frame;
    JTable examTable;
    DefaultTableModel tableModel;

    public NextExamSection() {
        frame = new JFrame("Next Exam Information");
        frame.setLayout(new BorderLayout());

        // Table setup
        String[] columnNames = {"Exam ID", "Exam Date", "Exam Type", "Department", "Exam Time", "Syllabus"};
        tableModel = new DefaultTableModel(columnNames, 0);
        examTable = new JTable(tableModel);
        
        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(examTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Load exam data
        loadExamData();

        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> frame.dispose()); // Dispose of the frame to go back
        frame.add(backButton, BorderLayout.SOUTH);

        // Frame settings
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void loadExamData() {
        String url = "jdbc:mysql://localhost:3306/test_db"; // Update with your DB details
        String user = "root";
        String password = "DHARMIKgohil@2006";

        String query = "SELECT no, date, exam_type, department, time, syllabus FROM next_exam_info WHERE date >= CURDATE() ORDER BY date, time";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Clear existing data
            tableModel.setRowCount(0);

            while (rs.next()) {
                int examId = rs.getInt("no");
                Date examDate = rs.getDate("date");
                String examType = rs.getString("exam_type");
                String department = rs.getString("department");
                Time examTime = rs.getTime("time");
                String syllabus = rs.getString("syllabus");

                // Add row to the table
                tableModel.addRow(new Object[]{
                    examId,
                    examDate,
                    examType,
                    department,
                    examTime,
                    syllabus
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading exam data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NextExamSection::new);
    }
}
