package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewResultSection extends JFrame {
    private JComboBox<String> branchComboBox;
    private JPanel gradePanel;
    private JTable resultTable;
    private Connection connection;

    public ViewResultSection() {
        setTitle("View Results Section");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Branch Selection Dropdown
        String[] branches = {"IT", "CE", "CS", "CD"};
        branchComboBox = new JComboBox<>(branches);

        // Grade Section (A to E and F)
        gradePanel = new JPanel();
        gradePanel.setLayout(new GridLayout(1, 6)); // A to F

        String[] grades = {"A", "B", "C", "D", "E", "F"};
        for (String grade : grades) {
            JButton gradeButton = new JButton(grade);
            gradeButton.addActionListener(new GradeButtonListener());
            gradePanel.add(gradeButton);
        }

        // Result Table
        resultTable = new JTable();

        // Layout
        setLayout(new BorderLayout());
        add(branchComboBox, BorderLayout.NORTH);
        add(gradePanel, BorderLayout.CENTER);
        add(new JScrollPane(resultTable), BorderLayout.SOUTH);

        // Database Connection
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit if the connection fails
        }

        setVisible(true);
    }

    // Listener for grade buttons
    private class GradeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String branch = branchComboBox.getSelectedItem().toString();
            String grade = e.getActionCommand();
            showResults(branch, grade);
        }
    }

    // Fetch and show results for a specific branch and grade
    private void showResults(String branch, String grade) {
        String studentTable = getStudentTable(branch);
        String resultQuery = buildResultQuery(studentTable, grade);

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(resultQuery)) {

            // Fetch student results
            List<Object[]> data = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String mo_number = rs.getString("mo_number");
                String rol = rs.getString("rol");
                String semester = rs.getString("semester");
                String result = rs.getString("result");

                // Add the row to the data list
                data.add(new Object[]{name, email, mo_number, rol, semester, result});
            }

            // Create table model from fetched data
            String[] columnNames = {"Name", "Email", "Mobile", "Roll No", "Semester", "Result"};
            Object[][] tableData = data.toArray(new Object[0][]);
            resultTable.setModel(new javax.swing.table.DefaultTableModel(tableData, columnNames));

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Get the correct student table based on branch
    private String getStudentTable(String branch) {
        switch (branch) {
            case "IT": return "student_it";
            case "CE": return "student_ce";
            case "CS": return "student_cs";
            case "CD": return "student_cd";
            default: throw new IllegalArgumentException("Invalid branch: " + branch);
        }
    }

    // Build SQL query to fetch results based on the selected grade and branch
    private String buildResultQuery(String studentTable, String grade) {
        String gradeCondition = getGradeCondition(grade);
        return "SELECT s.name, s.email, s.mo_number, s.rol, r.semester, r.result " +
               "FROM " + studentTable + " s " +
               "JOIN results r ON s.email = r.email_id " +
               "WHERE r.result " + gradeCondition;
    }

    // Convert grade letter to SQL condition
    private String getGradeCondition(String grade) {
        if ("F".equals(grade)) {
            return "= 'F'";
        } else {
            return "IN ('A', 'B', 'C', 'D', 'E')";  // For grades A-E
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewResultSection::new);
    }
}
