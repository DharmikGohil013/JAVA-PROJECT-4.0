package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewFeesDetailSection extends JFrame {
    private JComboBox<String> branchComboBox;
    private JTable feesTable;
    private Connection connection;

    public ViewFeesDetailSection() {
        setTitle("View Fees Detail Section");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Branch Selection Dropdown
        String[] branches = {"IT", "CE", "CS", "CD"};
        branchComboBox = new JComboBox<>(branches);
        branchComboBox.addActionListener(new BranchSelectionListener());

        // Fees Table
        feesTable = new JTable();

        // Layout
        setLayout(new BorderLayout());
        add(branchComboBox, BorderLayout.NORTH);
        add(new JScrollPane(feesTable), BorderLayout.CENTER);

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

    // Listener for branch selection
    private class BranchSelectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String branch = branchComboBox.getSelectedItem().toString();
            showFeesDetails(branch);
        }
    }

    // Fetch and show fee details for the selected branch
    private void showFeesDetails(String branch) {
        String studentTable = getStudentTable(branch);
        String feesQuery = buildFeesQuery(studentTable);

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(feesQuery)) {

            // Fetch fee details
            List<Object[]> data = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String totalFees = rs.getString("total_fees");
                String payingFees = rs.getString("paying_fees");
                String paymentMethod = rs.getString("payment_method");
                String date = rs.getString("date");
                String time = rs.getString("time");

                // Add the row to the data list
                data.add(new Object[]{name, email, totalFees, payingFees, paymentMethod, date, time});
            }

            // Create table model from fetched data
            String[] columnNames = {"Name", "Email", "Total Fees", "Paying Fees", "Payment Method", "Date", "Time"};
            Object[][] tableData = data.toArray(new Object[0][]);
            feesTable.setModel(new javax.swing.table.DefaultTableModel(tableData, columnNames));

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

    // Build SQL query to fetch fee details based on the selected branch
    private String buildFeesQuery(String studentTable) {
        return "SELECT s.name, f.email, f.total_fees, f.paying_fees, f.payment_method, f.date, f.time " +
               "FROM " + studentTable + " s " +
               "JOIN fees f ON s.email = f.email";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewFeesDetailSection::new);
    }
}
