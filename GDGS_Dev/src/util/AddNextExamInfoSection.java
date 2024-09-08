package util;

import com.toedter.calendar.JDateChooser;  // Import JCalendar classes
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddNextExamInfoSection {

    private JFrame frame;
    private JDateChooser dateChooser;
    private JTextField examTypeField, timeField, syllabusField;
    private JComboBox<String> departmentComboBox;
    private JTable examTable;
    private DefaultTableModel tableModel;

    public AddNextExamInfoSection() {
        frame = new JFrame("Add Next Exam Info");
        frame.setLayout(new BorderLayout());

        // Create panel for form inputs
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        // Date picker
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");

        // Form fields
        examTypeField = new JTextField();
        timeField = new JTextField();
        syllabusField = new JTextField();

        // Department dropdown
        departmentComboBox = new JComboBox<>(new String[]{"IT", "CE", "CS", "CD"});

        // Add fields to panel
        inputPanel.add(new JLabel("Date:"));
        inputPanel.add(dateChooser);
        inputPanel.add(new JLabel("Exam Type:"));
        inputPanel.add(examTypeField);
        inputPanel.add(new JLabel("Department:"));
        inputPanel.add(departmentComboBox);
        inputPanel.add(new JLabel("Time (HH:MM:SS):"));
        inputPanel.add(timeField);
        inputPanel.add(new JLabel("Syllabus:"));
        inputPanel.add(syllabusField);

        // Add input panel to frame
        frame.add(inputPanel, BorderLayout.NORTH);

        // Add button to submit new exam info
        JButton addButton = new JButton("Add Exam Info");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExamInfo();
            }
        });
        frame.add(addButton, BorderLayout.CENTER);

        // Create table to show existing exams
        tableModel = new DefaultTableModel(new String[]{"No", "Date", "Exam Type", "Department", "Time", "Syllabus"}, 0);
        examTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(examTable);
        frame.add(scrollPane, BorderLayout.SOUTH);

        // Load existing exam data
        loadExamData();

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setVisible(true);
    }

    private void addExamInfo() {
        // Retrieve date from JDateChooser
        java.util.Date selectedDate = dateChooser.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(frame, "Please select a date");
            return;
        }
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(selectedDate);

        String examType = examTypeField.getText();
        String department = (String) departmentComboBox.getSelectedItem();
        String time = timeField.getText();
        String syllabus = syllabusField.getText();

        if (examType.isEmpty() || time.isEmpty() || syllabus.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdatabase", "username", "password");
            String sql = "INSERT INTO next_exam_info (date, exam_type, department, time, syllabus) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, date);
            pstmt.setString(2, examType);
            pstmt.setString(3, department);
            pstmt.setString(4, time);
            pstmt.setString(5, syllabus);
            pstmt.executeUpdate();

            // Refresh the table data
            loadExamData();

            // Clear input fields
            dateChooser.setDate(null);
            examTypeField.setText("");
            timeField.setText("");
            syllabusField.setText("");

            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding exam info");
        }
    }

    private void loadExamData() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String sql = "SELECT * FROM next_exam_info";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("no"),
                        rs.getDate("date"),
                        rs.getString("exam_type"),
                        rs.getString("department"),
                        rs.getTime("time"),
                        rs.getString("syllabus")
                });
            }

            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading exam data");
        }
    }

    public static void main(String[] args) {
        new AddNextExamInfoSection();
    }
}
