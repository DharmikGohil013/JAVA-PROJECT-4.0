package util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadMaterialSection extends JFrame {

    private JLabel classLabel, titleLabel, linkLabel;
    private JTextField classField, titleField, linkField;
    private JButton uploadButton, backButton;
    private String facultyEmail;  // Assuming faculty email is passed when logged in
    
    public UploadMaterialSection(String email) {
        this.facultyEmail = email;  // Email from faculty login session
        initializeComponents();
    }
    
    // Initialize the GUI components
    private void initializeComponents() {
        // Class name input
        classLabel = new JLabel("Class Name:");
        add(classLabel);
        classField = new JTextField(20);
        add(classField);

        // Title input
        titleLabel = new JLabel("Title:");
        add(titleLabel);
        titleField = new JTextField(20);
        add(titleField);

        // Link input
        linkLabel = new JLabel("Material Link:");
        add(linkLabel);
        linkField = new JTextField(20);
        add(linkField);

        // Upload button
        uploadButton = new JButton("Upload Material");
        add(uploadButton);

        // Action listener for the upload button
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadMaterial();
            }
        });

        // Back button
        backButton = new JButton("Back");
        add(backButton);

        // Action for back button
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Logic to go back to the previous section
                dispose();  // Close this window
            }
        });

        // Set Layout and Frame properties
        setLayout(new GridLayout(5, 2));
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Function to upload material
    private void uploadMaterial() {
        String className = classField.getText();
        String title = titleField.getText();
        String link = linkField.getText();

        // Validate input fields
        if (className.isEmpty() || title.isEmpty() || link.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        // Insert material into the database
        try {
            // Database connection setup (Modify according to your DB credentials)
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");

            String query = "INSERT INTO materials (class_name, title, link, upload_date) VALUES (?, ?, ?, NOW())";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, className);
            ps.setString(2, title);
            ps.setString(3, link);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Material uploaded successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to upload material.");
            }

            // Clear the input fields after successful upload
            classField.setText("");
            titleField.setText("");
            linkField.setText("");

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error uploading material.");
        }
    }

    public static void main(String[] args) {
        // For demonstration, passing a dummy faculty email
        new UploadMaterialSection("faculty001@xyz.edu.in");
    }
}
