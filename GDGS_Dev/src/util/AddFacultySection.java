package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddFacultySection {

    private JFrame frame;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField mobileField;
    private JTextField roleField;
    private JTextField departmentField;
    private JPasswordField passwordField;
    private JTable facultyTable;
    private DefaultTableModel tableModel;

    public AddFacultySection() {
        frame = new JFrame("Faculty Management");
        frame.setLayout(new BorderLayout());

        // Create panel for form inputs
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Faculty form fields
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 30));

        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 30));

        mobileField = new JTextField();
        mobileField.setPreferredSize(new Dimension(200, 30));

        roleField = new JTextField();
        roleField.setPreferredSize(new Dimension(200, 30));

        departmentField = new JTextField();
        departmentField.setPreferredSize(new Dimension(200, 30));

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));

        // Add fields to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Faculty Name:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Mobile Number:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(mobileField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(roleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(departmentField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(passwordField, gbc);

        // Add input panel to frame
        frame.add(inputPanel, BorderLayout.NORTH);

        // Add button panel for "Add Faculty" and "Back"
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current frame
                // Add logic to return to the previous window if needed
            }
        });
        buttonPanel.add(backButton);

        // Add button for new faculty creation
        JButton addButton = new JButton("Add Faculty");
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setPreferredSize(new Dimension(180, 40));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFacultyInfo();
            }
        });
        buttonPanel.add(addButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Create table to show existing faculty members
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Mobile", "Role", "Dept", "Password"}, 0);
        facultyTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(facultyTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Load existing faculty data
        loadFacultyData();

        // Frame settings
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    // Method to add new faculty information
    private void addFacultyInfo() {
        String name = nameField.getText();
        String email = emailField.getText();
        String mobile = mobileField.getText();
        String role = roleField.getText();
        String department = departmentField.getText();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || mobile.isEmpty() || role.isEmpty() || department.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields");
            return;
        }

        try {
            // Use your database credentials
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String sql = "INSERT INTO faculty (name, email, mo_number, rol, dept, password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, mobile);
            pstmt.setString(4, role);
            pstmt.setString(5, department);
            pstmt.setString(6, password);
            pstmt.executeUpdate();

            // Refresh the table data
            loadFacultyData();

            // Clear input fields
            nameField.setText("");
            emailField.setText("");
            mobileField.setText("");
            roleField.setText("");
            departmentField.setText("");
            passwordField.setText("");

            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding faculty info");
        }
    }

    // Method to load existing faculty data into the table
    private void loadFacultyData() {
        try {
            // Use your database credentials
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String sql = "SELECT * FROM faculty";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("mo_number"),
                        rs.getString("rol"),
                        rs.getString("dept"),
                        rs.getString("password")
                });
            }

            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading faculty data");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AddFacultySection();
            }
        });
    }
}
