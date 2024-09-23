package util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.toedter.calendar.JDateChooser;
import java.util.Date;

public class FacultyAssignmentUpload extends JFrame {

    private JLabel classLabel, titleLabel, descLabel, dueDateLabel, fileLabel;
    private JTextField classField, titleField;
    private JTextArea descArea;
    private JButton uploadButton, selectFileButton, backButton;
    private JFileChooser fileChooser;
    private File selectedFile;
    private JDateChooser dueDateChooser;  // Use JCalendar for date picking

    public FacultyAssignmentUpload() {
        initializeComponents();
    }

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

        // Description input
        descLabel = new JLabel("Description:");
        add(descLabel);
        descArea = new JTextArea(5, 20);
        add(new JScrollPane(descArea));

        // Due Date input
        dueDateLabel = new JLabel("Due Date:");
        add(dueDateLabel);
        dueDateChooser = new JDateChooser();
        add(dueDateChooser);

        // File selection button
        fileLabel = new JLabel("Select Assignment (PDF):");
        add(fileLabel);
        selectFileButton = new JButton("Choose File");
        add(selectFileButton);

        // File chooser for PDF selection
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

        selectFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(FacultyAssignmentUpload.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    fileLabel.setText("Selected: " + selectedFile.getName());
                }
            }
        });

        // Upload button
        uploadButton = new JButton("Upload Assignment");
        add(uploadButton);

        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadAssignment();
            }
        });

        // Back button
        backButton = new JButton("Back");
        add(backButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();  // Go back to previous section
            }
        });

        // Set Layout and Frame properties
        setLayout(new GridLayout(7, 2));
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void uploadAssignment() {
        String className = classField.getText();
        String title = titleField.getText();
        String description = descArea.getText();
        Date dueDate = dueDateChooser.getDate();

        // Validate input
        if (className.isEmpty() || title.isEmpty() || description.isEmpty() || dueDate == null || selectedFile == null) {
            JOptionPane.showMessageDialog(this, "All fields and file are required.");
            return;
        }

        // Save the file to a specific folder on the server (e.g., "assignments/")
        String filePath = "assignments/" + selectedFile.getName();  // Customize your file storage path

        try {
            // Database connection setup (Modify according to your DB credentials)
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");

            String query = "INSERT INTO assignments (class_name, title, description, file_path, due_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, className);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setString(4, filePath);  // Path to the saved file
            ps.setDate(5, new java.sql.Date(dueDate.getTime()));

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Assignment uploaded successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to upload assignment.");
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error uploading assignment.");
        }
    }

    public static void main(String[] args) {
        new FacultyAssignmentUpload();
    }
}
