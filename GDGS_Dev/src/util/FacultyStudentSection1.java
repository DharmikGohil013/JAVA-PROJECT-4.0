package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser; // Make sure to import this for JDateChooser
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class FacultyStudentSection1 extends JFrame {

    // Components
    private JTable table;
    private JButton backButton, addButton;
    private JComboBox<String> subjectComboBox;
    private JDateChooser dateChooser;
    private JTextField timeField;
    private String facultyEmail;

    // Database connection details (replace these with your actual values)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_db"; // Replace with your DB URL
    private static final String DB_USER = "root"; // Replace with your DB username
    private static final String DB_PASSWORD = "DHARMIKgohil@2006"; // Replace with your DB password

    public FacultyStudentSection1(String facultyEmail) {
        this.facultyEmail = facultyEmail;

        // Set up the frame
        setTitle("Student Data Section");
        setSize(1200, 800); // Increase frame size to accommodate more components
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set layout
        setLayout(new BorderLayout());

        // Create components
        table = new JTable();
        backButton = new JButton("Back");
        addButton = new JButton("Add Attendance");

        // Create subject dropdown
        String[] subjects = {"s1", "s2", "s3", "s4", "s5"};
        subjectComboBox = new JComboBox<>(subjects);
        subjectComboBox.setFont(new Font("Arial", Font.PLAIN, 18));

        // Create date chooser
        dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Arial", Font.PLAIN, 18));

        // Create time field
        timeField = new JTextField(8);
        timeField.setFont(new Font("Arial", Font.PLAIN, 18));

        // Fetch and display student data based on the department
        fetchStudentData();

        // Panel for subject, date, and time inputs
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("Subject:"));
        inputPanel.add(subjectComboBox);
        inputPanel.add(new JLabel("Date:"));
        inputPanel.add(dateChooser);
        inputPanel.add(new JLabel("Time:"));
        inputPanel.add(timeField);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(backButton);
        buttonPanel.add(addButton);

        // Add components to the frame
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listener to back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current frame
                // Navigate back to the previous section (if needed)
                // For example: new PreviousFrame().setVisible(true); // Uncomment and replace with actual navigation
            }
        });

        // Add action listener to add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAttendance();
            }
        });

        // Set frame to be visible
        setVisible(true);
    }

    // Method to fetch student data based on the faculty email
    private void fetchStudentData() {
        String dept = getFacultyDepartment(facultyEmail); // Get the faculty department

        if (dept == null || dept.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Unable to find the department for the given faculty email.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Build SQL query based on department
        String query = "";
        if ("it".equalsIgnoreCase(dept)) {
            query = "SELECT * FROM student_it";
        } else if ("cd".equalsIgnoreCase(dept)) {
            query = "SELECT * FROM student_cd";
        } else if ("cs".equalsIgnoreCase(dept)) {
            query = "SELECT * FROM student_cs";
        } else if ("ce".equalsIgnoreCase(dept)) {
            query = "SELECT * FROM student_ce";
        }

        if (!query.isEmpty()) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                // Create a model to populate JTable
                table.setModel(buildTableModel(rs));

                // Add checkboxes in each row
                table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching student data: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to add attendance to the database
    private void addAttendance() {
        int rowCount = table.getRowCount();
        String subject = (String) subjectComboBox.getSelectedItem();
        java.util.Date selectedDate = dateChooser.getDate();
        String time = timeField.getText();

        if (subject == null || selectedDate == null || time.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

        // Determine the correct table based on department
        String dept = getFacultyDepartment(facultyEmail);
        String attendanceTable = dept.toLowerCase() + "ce_attendance";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO " + attendanceTable + 
                     " (student_email, subject_name, date, time, status) VALUES (?, ?, ?, ?, ?)")) {

            // Iterate over the rows in the table
            for (int i = 0; i < rowCount; i++) {
                String studentEmail = table.getValueAt(i, 0).toString();
                Boolean isPresent = (Boolean) table.getValueAt(i, 1); // Assuming checkboxes in the second column

                String status = isPresent != null && isPresent ? "Present" : "Absent";

                ps.setString(1, studentEmail);
                ps.setString(2, subject);
                ps.setDate(3, sqlDate);
                ps.setString(4, time);
                ps.setString(5, status);
                ps.addBatch();
            }

            ps.executeBatch();
            JOptionPane.showMessageDialog(this, "Attendance has been recorded successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error recording attendance: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to find faculty department based on their email from the 'faculty' table
    private String getFacultyDepartment(String email) {
        String department = null;
        String query = "SELECT dept FROM faculty WHERE email = ?";  // Query to find the department

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                department = rs.getString("dept");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching faculty department: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return department;
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
        
        columnNames.add("Present?"); // Add column for checkboxes

        // Data of the table
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            vector.add(false); // Default value for the checkbox
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == columnCount) return Boolean.class; // Checkbox column type
                return super.getColumnClass(columnIndex);
            }
        };
        
    }

       // Main method to run the example
       public static void main(String[] args) {
        // For example, we are using a hardcoded faculty email
        String facultyEmail = "faculty006@xyz.edu.in";
        SwingUtilities.invokeLater(() -> new FacultyStudentSection1(facultyEmail));
    }
}

