package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class ChangeAdminSection extends JFrame {
    private JTextField idField, nameField, emailField, moNumberField, passwordField;
    private JButton updateButton, backButton;
    private JComboBox<String> adminComboBox;
    private JPanel mainPanel, formPanel;
    private Connection connection;

    public ChangeAdminSection() {
        setTitle("Change Admin Details");
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

        JLabel adminLabel = new JLabel("Select Admin:");
        adminComboBox = new JComboBox<>(getAdminNames());
        adminComboBox.addActionListener(e -> loadAdminDetails((String) adminComboBox.getSelectedItem()));

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

        updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateAdminDetails());

        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            // Assuming you have a method to return to the previous screen
            new AddStudentSection();
            dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(adminLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(adminComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(moNumberLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(moNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(updateButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        formPanel.add(backButton, gbc);

        return formPanel;
    }

    private String[] getAdminNames() {
        Vector<String> adminNames = new Vector<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM admin")) {
            while (rs.next()) {
                adminNames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminNames.toArray(new String[0]);
    }

    private void loadAdminDetails(String adminName) {
        String query = "SELECT * FROM admin WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, adminName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idField.setText(rs.getString("id"));
                    nameField.setText(rs.getString("name"));
                    emailField.setText(rs.getString("email"));
                    moNumberField.setText(rs.getString("mo_number"));
                    passwordField.setText(rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateAdminDetails() {
        String id = idField.getText();
        String name = nameField.getText();
        String email = emailField.getText();
        String moNumber = moNumberField.getText();
        String password = passwordField.getText();

        String query = "UPDATE admin SET name = ?, email = ?, mo_number = ?, password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, moNumber);
            stmt.setString(4, password);
            stmt.setString(5, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Admin details updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating admin details.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChangeAdminSection::new);
    }
}
