package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class FacultyStudentSection extends JFrame {

    // Components
    private JTable table;
    private JButton backButton;
    private String facultyEmail;

    // Database connection details (replace these with your actual values)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_db"; // Replace with your DB URL
    private static final String DB_USER = "root"; // Replace with your DB username
    private static final String DB_PASSWORD = "DHARMIKgohil@2006"; // Replace with your DB password

    public FacultyStudentSection(String facultyEmail) {
        this.facultyEmail = facultyEmail;

        // Set up the frame
        setTitle("Student Data Section");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set layout
        setLayout(new BorderLayout());

        // Create components
        table = new JTable();
        backButton = new JButton("Back");

        // Fetch and display student data based on the department
        fetchStudentData();

        // Add components to the frame
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        // Add action listener to back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current frame
                // Navigate back to previous section (if needed)
                // For example: new PreviousFrame().setVisible(true); // Uncomment and replace with actual navigation
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

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching student data: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
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

    // Main method to run the example
    public static void main(String[] args) {
        // For example, we are using a hardcoded faculty email
        String facultyEmail = "faculty006@xyz.edu.in";
        SwingUtilities.invokeLater(() -> new FacultyStudentSection(facultyEmail));
    }
}
