package util;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class AdminLeaveApproval extends JFrame {
    private JTable leaveTable;
    private JButton approveButton, rejectButton, backButton;

    public AdminLeaveApproval() {
        initializeComponents();
        loadLeaveRequests();
    }

    // Initialize GUI components
    private void initializeComponents() {
        // Leave request table
        leaveTable = new JTable();
        add(new JScrollPane(leaveTable));

        // Approve button
        approveButton = new JButton("Approve Leave");
        add(approveButton);

        // Reject button
        rejectButton = new JButton("Reject Leave");
        add(rejectButton);

        // Back button
        backButton = new JButton("Back");
        add(backButton);

        // Action listeners for buttons
        approveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processLeaveRequest("Approved");
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processLeaveRequest("Rejected");
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Logic to go back to the previous section
                dispose(); // Close this window
            }
        });

        // Set Layout and Frame properties
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Load pending leave requests into the table
    private void loadLeaveRequests() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Faculty Email");
        model.addColumn("Start Date");
        model.addColumn("End Date");
        model.addColumn("Status");

        try {
            // Database connection setup (Modify according to your DB credentials)
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String query = "SELECT id, faculty_email, start_date, end_date, leave_status FROM faculty_leave WHERE leave_status = 'Pending'";
            PreparedStatement ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String email = rs.getString("faculty_email");
                String startDate = rs.getString("start_date");
                String endDate = rs.getString("end_date");
                String status = rs.getString("leave_status");

                model.addRow(new Object[]{id, email, startDate, endDate, status});
            }

            leaveTable.setModel(model);
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading leave requests.");
        }
    }

    // Process leave request (approve/reject)
    private void processLeaveRequest(String status) {
        int selectedRow = leaveTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a leave request.");
            return;
        }

        int leaveId = (int) leaveTable.getValueAt(selectedRow, 0);

        try {
            // Database
            // Database connection setup (Modify according to your DB credentials)
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String query = "UPDATE faculty_leave SET leave_status = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, status);
            ps.setInt(2, leaveId);

            int updatedRows = ps.executeUpdate();

            if (updatedRows > 0) {
                JOptionPane.showMessageDialog(this, "Leave request " + status + " successfully.");
                loadLeaveRequests();  // Refresh the table to show updated leave requests
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update the leave request.");
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error processing the leave request.");
        }
    }

    public static void main(String[] args) {
        // Launch the admin leave approval panel
        new AdminLeaveApproval();
    }
}
