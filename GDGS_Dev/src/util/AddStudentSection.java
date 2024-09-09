package util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AddStudentSection extends JFrame {
    private JFrame frame;
    private JButton btnIT, btnCE, btnCS, btnCD, backButton;
    private JTextField nameField, emailField, moNumberField, passwordField, roleField;
    private JComboBox<String> deptComboBox;
    private JPanel mainPanel, formPanel, departmentPanel;
    private Connection connection;

    public AddStudentSection() {
        setTitle("Add Student Section");
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

        // Create a main panel with CardLayout to switch between views
        mainPanel = new JPanel(new CardLayout());

        // Add the department selection panel to the main panel
        departmentPanel = createDepartmentPanel();
        mainPanel.add(departmentPanel, "DepartmentPanel");

        // Add the student form panel to the main panel
        formPanel = createFormPanel();
        mainPanel.add(formPanel, "FormPanel");

        // Add components to JFrame
        add(mainPanel);
        setVisible(true);
    }

    // Create panel for department selection
    private JPanel createDepartmentPanel() {
       
        JPanel departmentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        frame = new JFrame("Add Material Link");
    
        // Buttons for each department
        btnIT = new JButton("IT Department");
        btnCE = new JButton("CE Department");
        btnCS = new JButton("CS Department");
        btnCD = new JButton("CD Department");
    
        btnIT.addActionListener(e -> showFormPanel("IT"));
        btnCE.addActionListener(e -> showFormPanel("CE"));
        btnCS.addActionListener(e -> showFormPanel("CS"));
        btnCD.addActionListener(e -> showFormPanel("CD"));
    
        // Layout for department buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        departmentPanel.add(btnIT, gbc);
    
        gbc.gridx = 1;
        departmentPanel.add(btnCE, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 1;
        departmentPanel.add(btnCS, gbc);
    
        gbc.gridx = 1;
        departmentPanel.add(btnCD, gbc);
    
        // Back button
        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            
            // If you want to close the application
            // System.exit(0);
    
            // Or show a previous panel or a different action
            // Assuming you want to close the application or navigate somewhere
            JOptionPane.showMessageDialog(this, "Back button clicked. Implement your desired action.");
        });
    
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        departmentPanel.add(backButton, gbc);
    
        return departmentPanel;
    }
    

    // Create form panel for adding student details
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel moNumberLabel = new JLabel("Mobile Number:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel roleLabel = new JLabel("Role:");
        JLabel deptLabel = new JLabel("Department:");

        nameField = new JTextField(20);
        emailField = new JTextField(20);
        moNumberField = new JTextField(20);
        passwordField = new JTextField(20);
        roleField = new JTextField(20);

        deptComboBox = new JComboBox<>(new String[]{"IT", "CE", "CS", "CD"});

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> addStudent());

        backButton = new JButton("Back");
        backButton.addActionListener(e -> showDepartmentPanel());

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(moNumberLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(moNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(roleLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(roleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(deptLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(deptComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(submitButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(backButton, gbc);

        return formPanel;
    }

    // Show the student form panel for adding students
    private void showFormPanel(String department) {
        deptComboBox.setSelectedItem(department);
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "FormPanel");
    }

    // Show the department panel
    private void showDepartmentPanel() {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "DepartmentPanel");
    }

    // Add student data to the selected department table
    private void addStudent() {
        String name = nameField.getText();
        String email = emailField.getText();
        String moNumber = moNumberField.getText();
        String password = passwordField.getText();
        String role = roleField.getText();
        String department = (String) deptComboBox.getSelectedItem();
        String tableName = "student_" + department.toLowerCase();

        String query = "INSERT INTO " + tableName + " (name, email, mo_number, password, rol, dept) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, moNumber);
            stmt.setString(4, password);
            stmt.setString(5, role);
            stmt.setString(6, department);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding student.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddStudentSection::new);
    }
}
