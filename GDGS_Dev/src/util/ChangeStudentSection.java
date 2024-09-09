package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class ChangeStudentSection extends JFrame {
    private JComboBox<String> deptComboBox, studentIdComboBox;
    private JTextField idField, nameField, emailField, moNumberField, passwordField, roleField;
    private JButton updateButton, backButton;
    private JPanel mainPanel, formPanel;
    private Connection connection;

    public ChangeStudentSection() {
        setTitle("Change Student Details");
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            // Setup database connection
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/test_db", 
                "root", 
                "DHARMIKgohil@2006"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Create main panel
        mainPanel = new JPanel(new CardLayout());

        // Create and add the form panel
        formPanel = createFormPanel();
        mainPanel.add(formPanel, "FormPanel");

        // Add components to JFrame
        add(mainPanel);
        setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel deptLabel = new JLabel("Select Department:");
        deptComboBox = new JComboBox<>(new String[]{"IT", "CS", "CE", "CD"});
        deptComboBox.addActionListener(e -> loadStudentIds((String) deptComboBox.getSelectedItem()));

        JLabel studentIdLabel = new JLabel("Select Student ID:");
        studentIdComboBox = new JComboBox<>();
        studentIdComboBox.addActionListener(e -> loadStudentDetails((String) studentIdComboBox.getSelectedItem()));

        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField(20);
        idField.setEditable(false);

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);

        JLabel moNumberLabel = new JLabel("Mobile Number:");
        moNumberField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JTextField(20);

        JLabel roleLabel = new JLabel("Role:");
        roleField = new JTextField(20);

        updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateStudentDetails());

        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            // Assuming you have a method to return to the previous screen
            new AddStudentSection(); // Replace this with your desired back navigation
            dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(deptLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(deptComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(studentIdLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(studentIdComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(moNumberLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(moNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(roleLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(roleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        formPanel.add(updateButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        formPanel.add(backButton, gbc);

        return formPanel;
    }

    private void loadStudentIds(String department) {
        studentIdComboBox.removeAllItems();
        String tableName = "student_" + department.toLowerCase();
        String query = "SELECT id FROM " + tableName;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                studentIdComboBox.addItem(rs.getString("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadStudentDetails(String studentId) {
        if (studentId == null || studentId.isEmpty()) {
            return;
        }

        String department = (String) deptComboBox.getSelectedItem();
        String tableName = "student_" + department.toLowerCase();
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idField.setText(rs.getString("id"));
                    nameField.setText(rs.getString("name"));
                    emailField.setText(rs.getString("email"));
                    moNumberField.setText(rs.getString("mo_number"));
                    passwordField.setText(rs.getString("password"));
                    roleField.setText(rs.getString("rol"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateStudentDetails() {
        String id = idField.getText();
        String name = nameField.getText();
        String email = emailField.getText();
        String moNumber = moNumberField.getText();
        String password = passwordField.getText();
        String role = roleField.getText();
        String department = (String) deptComboBox.getSelectedItem();
        String tableName = "student_" + department.toLowerCase();

        String query = "UPDATE " + tableName + " SET name = ?, email = ?, mo_number = ?, password = ?, rol = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, moNumber);
            stmt.setString(4, password);
            stmt.setString(5, role);
            stmt.setString(6, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student details updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating student details.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChangeStudentSection::new);
    }
}
