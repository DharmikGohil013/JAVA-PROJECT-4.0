package util;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AttendanceViewer extends JFrame {
    private JTextArea resultArea;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_db"; 
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = "DHARMIKgohil@2006"; 
    public AttendanceViewer(String email) {
        setTitle("Student Attendance Viewer");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.CENTER);

    
        checkAttendance(email);
        
        setVisible(true); 
    }

    private void checkAttendance(String email) {
        String department = getDepartment(email);

        if (department != null) {
          
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
                    department = dept;
                    break; 
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

          
            if (!checkTableExists(conn, tableName)) {
                records.append("Attendance table for department ").append(department).append(" does not exist.");
                return records.toString();
            }

           
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
            return rs.next(); 
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }

    public static void main(String[] args) {
        
        String studentEmail = "23dit001@xyz.edu.in"; 
        SwingUtilities.invokeLater(() -> {
            AttendanceViewer viewer = new AttendanceViewer(studentEmail);
            viewer.setVisible(true);
        });
    }
}
