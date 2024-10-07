package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class toAttendanceSystem extends JFrame {
    private DefaultTableModel[] tableModels = new DefaultTableModel[4]; // Store table models for each department
    private JComboBox<String> timeComboBox;
    private JButton submitButton;
    private String[] timeSlots = {"9 to 10", "10 to 11", "11 to 12", "1 to 2", "2 to 3", "3 to 4"};
    private String currentDate;
    private String email; // Email of the faculty

    // Modify the constructor to accept an email parameter
    public toAttendanceSystem(String email) {
        this.email = email; // Store the email for later use
        setTitle("Attendance System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Current date
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        currentDate = date.format(formatter);

        // Create JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("IT", createAttendancePanel("IT", 0));
        tabbedPane.addTab("CE", createAttendancePanel("CE", 1));
        tabbedPane.addTab("CS", createAttendancePanel("CS", 2));
        tabbedPane.addTab("CD", createAttendancePanel("CD", 3));
        tabbedPane.addTab("View Attendance", createViewAttendancePanel()); // New tab for viewing attendance records
        tabbedPane.addTab("Student Attendance View", createStudentAttendanceViewPanel()); // New tab for student-specific attendance view

        // Add components to frame
        add(tabbedPane, BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createAttendancePanel(String department, int index) {
        JPanel panel = new JPanel(new BorderLayout());

        // Create a table
        String[] columnNames = {"Select", "ID", "Name", "Email", "Mobile Number", "Role"};
        tableModels[index] = new DefaultTableModel(columnNames, 0); // Assign to the specific index
        JTable table = new JTable(tableModels[index]) {
            // Make the first column editable (for checkboxes)
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class; // First column for checkboxes
                }
                return super.getColumnClass(columnIndex);
            }
        };

        // Load student data
        loadStudentData(department, tableModels[index]);

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createViewAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create a table for viewing attendance
        String[] columnNames = {"Email", "Subject Name", "Date", "Time", "Status"};
        DefaultTableModel attendanceModel = new DefaultTableModel(columnNames, 0);
        JTable attendanceTable = new JTable(attendanceModel);

        // Load attendance data from the database
        loadAttendanceData(attendanceModel);

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStudentAttendanceViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create components to get student email
        JPanel inputPanel = new JPanel();
        JTextField emailField = new JTextField(20);
        JButton fetchButton = new JButton("Fetch Attendance");
        JLabel lectureCountLabel = new JLabel("Lectures Attended: 0");

        inputPanel.add(new JLabel("Student Email: "));
        inputPanel.add(emailField);
        inputPanel.add(fetchButton);
        inputPanel.add(lectureCountLabel);

        // Add action listener to fetch button
        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentEmail = emailField.getText();
                DefaultTableModel studentAttendanceModel = new DefaultTableModel(
                        new String[]{"Subject Name", "Date", "Time", "Status"}, 0);
                JTable studentAttendanceTable = new JTable(studentAttendanceModel);
                JScrollPane scrollPane = new JScrollPane(studentAttendanceTable);
                panel.add(scrollPane, BorderLayout.CENTER);
                panel.revalidate();

                // Load attendance data for the specific student email
                loadStudentAttendanceData(studentEmail, studentAttendanceModel, lectureCountLabel);
            }
        });

        panel.add(inputPanel, BorderLayout.NORTH);
        return panel;
    }

    private void loadStudentAttendanceData(String studentEmail, DefaultTableModel model, JLabel lectureCountLabel) {
        String query = "SELECT subject_name, date, time, status FROM it_attendance WHERE student_email = ?";
        int lectureCount = 0;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentEmail);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("subject_name"),
                        rs.getDate("date"),
                        rs.getTime("time"),
                        rs.getString("status")
                });
                // Count attended lectures
                if ("Present".equalsIgnoreCase(rs.getString("status"))) {
                    lectureCount++;
                }
            }
            lectureCountLabel.setText("Lectures Attended: " + lectureCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAttendanceData(DefaultTableModel model) {
        String query = "SELECT * FROM it_attendance"; // You can modify to load attendance for specific departments
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("student_email"),
                        rs.getString("subject_name"),
                        rs.getDate("date"),
                        rs.getTime("time"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        timeComboBox = new JComboBox<>(timeSlots);
        submitButton = new JButton("Submit Attendance");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitAttendance();
            }
        });

        panel.add(new JLabel("Select Time: "));
        panel.add(timeComboBox);
        panel.add(submitButton);
        return panel;
    }

    private void loadStudentData(String department, DefaultTableModel model) {
        String query = "SELECT * FROM student_" + department.toLowerCase(); // Assuming table names are like student_it
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        false, // Checkbox column for marking attendance
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("mo_number"),
                        rs.getString("rol")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void submitAttendance() {
        // Iterate through each department's table and save attendance
        saveAttendance("it", 0);
        saveAttendance("ce", 1);
        saveAttendance("cs", 2);
        saveAttendance("cd", 3);
    }

    private void saveAttendance(String department, int index) {
        // Here you will save the attendance data to the respective table
        DefaultTableModel model = tableModels[index]; // Access the correct model using index
        for (int i = 0; i < model.getRowCount(); i++) {
            boolean isSelected = (Boolean) model.getValueAt(i, 0);
            if (isSelected) {
                String email = model.getValueAt(i, 3).toString();
                String time = timeComboBox.getSelectedItem().toString();

                // Format the time from 'HH to HH' to 'HH:00:00'
                String[] parts = time.split(" to ");
                String timeFormatted = parts[0] + ":00"; // or use parts[0] + ":00:00" for full HH:MM:SS

                String status = "Present"; // Set status based on checkbox

                String insertQuery = "INSERT INTO " + department + "_attendance (student_email, subject_name, date, time, status) VALUES (?, ?, ?, ?, ?)";
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
                     PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    pstmt.setString(1, email);
                    pstmt.setString(2, "subject_name"); // Specify the subject name
                    pstmt.setString(3, currentDate);
                    pstmt.setString(4, timeFormatted); // Use the formatted time
                    pstmt.setString(5, status);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String email = "faculty001@xyz.edu.in"; // Replace with the actual email you want to use
            toAttendanceSystem toattendanceSystem = new toAttendanceSystem(email);
            toattendanceSystem.setVisible(true);
        });
    }
    
}
