package util;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class ExamSection extends JFrame {

    // Components
    private JTable examTable;
    private JButton editButton, backButton;
    private JPanel buttonPanel;
    private String selectedDepartment;
    
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_db";  // Replace with your DB URL
    private static final String DB_USER = "root";  // Replace with your DB username
    private static final String DB_PASSWORD = "DHARMIKgohil@2006";  // Replace with your DB password

    public ExamSection() {
        // Set up the frame
        setTitle("Exam Section");
        setSize(1200, 600);  // Using max width for display
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initial UI with department buttons
        buttonPanel = new JPanel(new GridLayout(1, 4));
        JButton itButton = new JButton("IT");
        JButton ceButton = new JButton("CE");
        JButton csButton = new JButton("CS");
        JButton cdButton = new JButton("CD");

        buttonPanel.add(itButton);
        buttonPanel.add(ceButton);
        buttonPanel.add(csButton);
        buttonPanel.add(cdButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Action Listeners for department buttons
        itButton.addActionListener(e -> fetchExamData("IT"));
        ceButton.addActionListener(e -> fetchExamData("CE"));
        csButton.addActionListener(e -> fetchExamData("CS"));
        cdButton.addActionListener(e -> fetchExamData("CD"));

        setVisible(true);
    }

    // Fetch exam data for the selected department
    private void fetchExamData(String department) {
        this.selectedDepartment = department;

        // Remove the department selection buttons
        remove(buttonPanel);

        // Create components for exam data display
        examTable = new JTable();
        editButton = new JButton("Edit");
        backButton = new JButton("Back");

        // Fetch and display exam data for the selected department
        loadExamData(department);

        // Add components to the frame
        add(new JScrollPane(examTable), BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(editButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Edit button functionality to open dialog for adding or deleting exams
        editButton.addActionListener(e -> openEditDialog());

        // Back button functionality to go back to the department selection screen
        backButton.addActionListener(e -> {
            remove(examTable);
            remove(bottomPanel);
            add(buttonPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        // Update UI
        revalidate();
        repaint();
    }

    // Load exam data for the selected department
    private void loadExamData(String department) {
        String query = "SELECT * FROM next_exam_info WHERE department = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, department);
            try (ResultSet rs = stmt.executeQuery()) {
                examTable.setModel(buildTableModel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching exam data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to build a TableModel from ResultSet
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // Names of columns
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Data of the table
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }

    // Opens the edit dialog for adding/removing exams
    private void openEditDialog() {
        JDialog dialog = new JDialog(this, "Edit Exam", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        // Create UI components for adding a new exam
        JTextField dateField = new JTextField("YYYY-MM-DD", 10);
        JTextField examTypeField = new JTextField("Exam Type", 10);
        JTextField timeField = new JTextField("HH:MM:SS", 10);
        JTextField syllabusField = new JTextField("Syllabus", 10);

        JButton addExamButton = new JButton("Add Exam");
        JButton removeExamButton = new JButton("Remove Selected Exam");

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Date:"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Exam Type:"));
        inputPanel.add(examTypeField);
        inputPanel.add(new JLabel("Time:"));
        inputPanel.add(timeField);
        inputPanel.add(new JLabel("Syllabus:"));
        inputPanel.add(syllabusField);

        dialog.add(inputPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addExamButton);
        buttonPanel.add(removeExamButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Add exam functionality
        addExamButton.addActionListener(e -> {
            String date = dateField.getText();
            String examType = examTypeField.getText();
            String time = timeField.getText();
            String syllabus = syllabusField.getText();

            addExam(date, examType, time, syllabus);
            dialog.dispose();  // Close dialog after adding exam
        });

        // Remove selected exam functionality
        removeExamButton.addActionListener(e -> {
            int selectedRow = examTable.getSelectedRow();
            if (selectedRow != -1) {
                int examNo = (int) examTable.getValueAt(selectedRow, 0);
                removeExam(examNo);
                dialog.dispose();  // Close dialog after removing exam
            }
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Method to add a new exam
    private void addExam(String date, String examType, String time, String syllabus) {
        String query = "INSERT INTO next_exam_info (date, exam_type, department, time, syllabus) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, date);
            stmt.setString(2, examType);
            stmt.setString(3, selectedDepartment);
            stmt.setString(4, time);
            stmt.setString(5, syllabus);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Exam added successfully!");
            fetchExamData(selectedDepartment);  // Refresh the table data
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding exam: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to remove an exam
    private void removeExam(int examNo) {
        String query = "DELETE FROM next_exam_info WHERE no = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, examNo);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Exam removed successfully!");
            fetchExamData(selectedDepartment);  // Refresh the table data
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error removing exam: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Main method to run the example
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExamSection());
    }
}
