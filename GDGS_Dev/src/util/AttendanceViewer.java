package util;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AttendanceViewer extends JFrame {
    private JTextArea resultArea;

    // Database connection settings
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_db"; // replace with your database URL
    private static final String DB_USER = "root"; // replace with your username
    private static final String DB_PASSWORD = "DHARMIKgohil@2006"; // replace with your password

    // Constructor that accepts an email
    public AttendanceViewer(String email) {
        setTitle("Student Attendance Viewer");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setLocationRelativeTo(null);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.CENTER);

        // Call the method to check attendance based on the provided email
        checkAttendance(email);
        
        setVisible(true); // Set visibility after components are added
    }

    private void checkAttendance(String email) {
        String department = getDepartment(email);

        if (department != null) {
            // Now check attendance records
            String attendanceRecords = getAttendanceRecords(email, department);
            resultArea.setText(attendanceRecords);
        } else {
            resultArea.setText("Email not found in any department.");
        }
    }

    private String getDepartment(String email) {
        String department = null;
        String[] departments = {"it", "ce", "cs", "cd"};
        
        for (String dept : departments) {
            String tableName = "student_" + dept;

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM " + tableName + " WHERE email = ?")) {

                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    department = dept; // Found the department
                    break; // No need to check other departments
                }

            } catch (SQLException e) {
                e.printStackTrace();
                resultArea.setText("Database error: " + e.getMessage());
            }
        }
        return department;
    }

    private String getAttendanceRecords(String email, String department) {
        StringBuilder records = new StringBuilder();
        String tableName = department + "_attendance";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE student_email = ?")) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            // Check if the attendance table exists and fetch records
            if (!checkTableExists(conn, tableName)) {
                records.append("Attendance table for department ").append(department).append(" does not exist.");
                return records.toString();
            }

            // If attendance records exist, append them to the results
            while (rs.next()) {
                String subject = rs.getString("subject_name");
                Date date = rs.getDate("date");
                Time time = rs.getTime("time");
                String status = rs.getString("status");
                records.append(String.format("Subject: %s, Date: %s, Time: %s, Status: %s%n",
                        subject, date, time, status));
            }

            if (records.length() == 0) {
                records.append("No attendance records found for the student.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            records.append("Error accessing database: ").append(e.getMessage());
        }

        return records.toString();
    }

    private boolean checkTableExists(Connection conn, String tableName) {
        try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null)) {
            return rs.next(); // If a result is returned, the table exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // On error, assume table does not exist
        }
    }

    public static void main(String[] args) {
        // Example email to check
        String studentEmail = "23dit001@xyz.edu.in"; // Replace with the desired email
        SwingUtilities.invokeLater(() -> {
            AttendanceViewer viewer = new AttendanceViewer(studentEmail);
            viewer.setVisible(true);
        });
    }
}
