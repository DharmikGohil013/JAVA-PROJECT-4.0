package util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class StudentAssignmentSubmission extends JFrame {

    private JLabel assignmentLabel, fileLabel;
    private JComboBox<String> assignmentComboBox;
    private JButton submitButton, selectFileButton, backButton;
    private JFileChooser fileChooser;
    private File selectedFile;
    private String studentEmail;  // Assuming student email is passed when logged in

    public StudentAssignmentSubmission(String email) {
        this.studentEmail = email;
        initializeComponents();
        loadAssignments();  // Load available assignments
    }

    private void initializeComponents() {
        // Assignment selection combo box
        assignmentLabel = new JLabel("Select Assignment:");
        add(assignmentLabel);
        assignmentComboBox = new JComboBox<>();
        add(assignmentComboBox);

        // File selection button
        fileLabel = new JLabel("Select Solution (PDF):");
        add(fileLabel);
        selectFileButton = new JButton("Choose File");
        add(selectFileButton);

        // File chooser for PDF selection
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

        selectFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(StudentAssignmentSubmission.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    fileLabel.setText("Selected: " + selectedFile.getName());
                }
            }
        });

        // Submit button
        submitButton = new JButton("Submit Solution");
        add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitSolution();
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
        setLayout(new GridLayout(5, 2));
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadAssignments() {
        // Load assignments from the database for the student
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String query = "SELECT id, title FROM assignments"; // Adjust query based on requirements
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                assignmentComboBox.addItem(title);
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading assignments.");
        }
    }

    private void submitSolution() {
        String selectedAssignmentTitle = (String) assignmentComboBox.getSelectedItem();

        if (selectedAssignmentTitle == null || selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please select an assignment and a file.");
            return;
        }

        // Get assignment ID from the title
        int assignmentId = getAssignmentId(selectedAssignmentTitle);

        if (assignmentId == -1) {
            JOptionPane.showMessageDialog(this, "Selected assignment not found.");
            return;
        }

        // Save the file to a specific folder on the server (e.g., "submissions/")
        String filePath = "submissions/" + selectedFile.getName();  // Customize your file storage path

        // Save file to the server directory (implement your file copy logic here)
        // Here you should implement file copying to the desired path (omitted for brevity)

        // Now insert the submission record into the database
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");

            String query = "INSERT INTO submissions (assignment_id, student_email, file_path) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, assignmentId);
            ps.setString(2, studentEmail);
            ps.setString(3, filePath);  // Path to the submitted file

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Solution submitted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit solution.");
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting solution.");
        }
    }

    private int getAssignmentId(String title) {
        int assignmentId = -1;
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String query = "SELECT id FROM assignments WHERE title = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                assignmentId = rs.getInt("id");
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignmentId;
    }

    public static void main(String[] args) {
        new StudentAssignmentSubmission("student001@xyz.edu.in");  // Example student email
    }
}
