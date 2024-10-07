package util;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AttendanceSystem extends JFrame {
    private DefaultTableModel[] tableModels = new DefaultTableModel[4]; // Store table models for each department
    private JComboBox<String> timeComboBox;
    private JButton submitButton;
    private String[] timeSlots = {"9 to 10", "10 to 11", "11 to 12", "1 to 2", "2 to 3", "3 to 4"};
    private String currentDate;

    public AttendanceSystem() {
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

        // Add components to frame
        add(tabbedPane, BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createAttendancePanel(String department, int index) {
        JPanel panel = new JPanel(new BorderLayout());

        // Create a table
        String[] columnNames = {"Select", "ID", "Name", "Email", "Mobile Number", "Role"};
        tableModels[index] = new DefaultTableModel(columnNames, 0); // Assign to the specific index
        JTable table = new JTable(tableModels[index]);
        loadStudentData(department, tableModels[index]);

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
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
                        false, // Checkbox column
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
        // Loop through the table to check which students are marked present
        DefaultTableModel model = tableModels[index]; // Access the correct model using index
        for (int i = 0; i < model.getRowCount(); i++) {
            boolean isSelected = (Boolean) model.getValueAt(i, 0);
            if (isSelected) {
                String email = model.getValueAt(i, 3).toString();
                String time = timeComboBox.getSelectedItem().toString();
                String status = "Present"; // Set status based on checkbox

                String insertQuery = "INSERT INTO " + department + "_attendance (student_email, subject_name, date, time, status) VALUES (?, ?, ?, ?, ?)";
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
                     PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    pstmt.setString(1, email);
                    pstmt.setString(2, "subject_name"); // Specify the subject name
                    pstmt.setString(3, currentDate);
                    pstmt.setString(4, time);
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
            AttendanceSystem attendanceSystem = new AttendanceSystem();
            attendanceSystem.setVisible(true);
        });
    }
}
