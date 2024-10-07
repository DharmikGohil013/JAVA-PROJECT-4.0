package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FacultyEventAttendanceViewer extends JFrame {
    private JTable attendanceTable;
    private DefaultTableModel tableModel;

    // Database connection settings
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_db"; // replace with your database URL
    private static final String DB_USER = "root"; // replace with your username
    private static final String DB_PASSWORD = "DHARMIKgohil@2006"; // replace with your password

    public FacultyEventAttendanceViewer() {
        setTitle("Event Attendance Viewer");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set up the table model
        tableModel = new DefaultTableModel(new String[]{"ID", "Event Name", "Email", "Event Start Time", "Event Date", "CC Email", "Event End Time"}, 0);
        attendanceTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(attendanceTable);

        add(scrollPane, BorderLayout.CENTER);
        fetchEventAttendance();

        setVisible(true);
    }

    private void fetchEventAttendance() {
        String query = "SELECT * FROM event_attendance";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Clear existing rows
            tableModel.setRowCount(0);

            // Populate the table model with data from the ResultSet
            while (rs.next()) {
                int id = rs.getInt("id");
                String eventName = rs.getString("event_name");
                String email = rs.getString("email");
                Time eventStartTime = rs.getTime("event_start_time");
                Date eventDate = rs.getDate("event_date");
                String ccEmail = rs.getString("cc_email");
                Time eventEndTime = rs.getTime("event_end_time");

                tableModel.addRow(new Object[]{id, eventName, email, eventStartTime, eventDate, ccEmail, eventEndTime});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FacultyEventAttendanceViewer::new);
    }
}
