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

public class AddAdminSection {

    private JFrame frame;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField mobileField;
    private JPasswordField passwordField;
    private JTable adminTable;
    private DefaultTableModel tableModel;

    public AddAdminSection() {
        frame = new JFrame("Admin Management");
        frame.setLayout(new BorderLayout());

        // Create panel for form inputs
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Admin form fields
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 30)); // Medium size

        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 30)); // Medium size

        mobileField = new JTextField();
        mobileField.setPreferredSize(new Dimension(200, 30)); // Medium size

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30)); // Medium size

        // Add fields to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Admin Name:"), gbc);
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
        inputPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(passwordField, gbc);

        // Add input panel to frame
        frame.add(inputPanel, BorderLayout.NORTH);

        // Add button panel for "Add Admin" and "Back"
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16)); // Larger font
        backButton.setPreferredSize(new Dimension(120, 40)); // Medium size
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current frame
                // You can add logic to return to the previous window if needed
            }
        });
        buttonPanel.add(backButton);

        // Add button for new admin creation
        JButton addButton = new JButton("Add Admin");
        addButton.setFont(new Font("Arial", Font.BOLD, 16)); // Larger font
        addButton.setPreferredSize(new Dimension(180, 40)); // Medium size
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAdminInfo();
            }
        });
        buttonPanel.add(addButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Create table to show existing admins
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Mobile Number", "Password"}, 0);
        adminTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(adminTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Load existing admin data
        loadAdminData();

        // Frame settings
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    // Method to add new admin information
    private void addAdminInfo() {
        String name = nameField.getText();
        String email = emailField.getText();
        String mobile = mobileField.getText();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || mobile.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields");
            return;
        }

        try {
            // Use your database credentials
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String sql = "INSERT INTO admin (name, email, mo_number, password) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, mobile);
            pstmt.setString(4, password);
            pstmt.executeUpdate();

            // Refresh the table data
            loadAdminData();

            // Clear input fields
            nameField.setText("");
            emailField.setText("");
            mobileField.setText("");
            passwordField.setText("");

            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding admin info");
        }
    }

    // Method to load existing admin data into the table
    private void loadAdminData() {
        try {
            // Use your database credentials
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String sql = "SELECT * FROM admin";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("mo_number"),
                        rs.getString("password")
                });
            }

            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading admin data");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AddAdminSection();
            }
        });
    }
}
