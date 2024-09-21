package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class NewCourseSection {

    JFrame frame;
    JTextField courseNameField, courseDurationField;
    JTextArea courseDescriptionArea;
    JTable courseTable;
    DefaultTableModel tableModel;

    public NewCourseSection() {
        // Create the frame
        frame = new JFrame("New Course Section");
        frame.setLayout(new BorderLayout());

        // Create the panel for adding a new course
        JPanel addCoursePanel = new JPanel(new GridLayout(5, 2, 10, 10));
        addCoursePanel.setBorder(BorderFactory.createTitledBorder("Add New Course"));

        JLabel courseNameLabel = new JLabel("Course Name:");
        courseNameField = new JTextField();
        JLabel courseDurationLabel = new JLabel("Course Duration:");
        courseDurationField = new JTextField();
        JLabel courseDescriptionLabel = new JLabel("Course Description:");
        courseDescriptionArea = new JTextArea(4, 20);

        JButton addCourseButton = new JButton("Add Course");
        addCourseButton.addActionListener(new AddCourseActionListener());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Define what should happen on back button click
                frame.dispose();  // This will close the current window
            }
        });

        // Add form fields to the panel
        addCoursePanel.add(courseNameLabel);
        addCoursePanel.add(courseNameField);
        addCoursePanel.add(courseDurationLabel);
        addCoursePanel.add(courseDurationField);
        addCoursePanel.add(courseDescriptionLabel);
        addCoursePanel.add(new JScrollPane(courseDescriptionArea));
        addCoursePanel.add(new JLabel(""));  // Empty cell
        addCoursePanel.add(addCourseButton);
        addCoursePanel.add(backButton); // Add the back button

        // Create a table model and table to display the courses
        tableModel = new DefaultTableModel(new String[]{"Course ID", "Course Name", "Duration", "Description"}, 0);
        courseTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(courseTable);

        // Fetch and display all existing courses
        fetchAndDisplayCourses();

        // Add components to the frame
        frame.add(addCoursePanel, BorderLayout.NORTH);
        frame.add(tableScrollPane, BorderLayout.CENTER);

        // Frame settings
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Action listener for adding a new course
    private class AddCourseActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String courseName = courseNameField.getText();
            String courseDuration = courseDurationField.getText();
            String courseDescription = courseDescriptionArea.getText();

            if (courseName.isEmpty() || courseDuration.isEmpty() || courseDescription.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                addNewCourseToDatabase(courseName, courseDuration, courseDescription);
                fetchAndDisplayCourses();  // Refresh the table after adding
            }
        }
    }

    // Method to add a new course to the database
    private void addNewCourseToDatabase(String courseName, String courseDuration, String courseDescription) {
        String url = "jdbc:mysql://localhost:3306/test_db"; // Update with your DB details
        String user = "root";
        String password = "DHARMIKgohil@2006";

        String insertQuery = "INSERT INTO courses (course_name, course_duration, course_description) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1, courseName);
            pstmt.setString(2, courseDuration);
            pstmt.setString(3, courseDescription);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Course added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding the course", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to fetch and display all courses from the database
    private void fetchAndDisplayCourses() {
        tableModel.setRowCount(0);  // Clear existing rows

        String url = "jdbc:mysql://localhost:3306/test_db"; // Update with your DB details
        String user = "root";
        String password = "DHARMIKgohil@2006";

        String selectQuery = "SELECT * FROM courses";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectQuery)) {

            while (rs.next()) {
                int courseId = rs.getInt("course_id");
                String courseName = rs.getString("course_name");
                String courseDuration = rs.getString("course_duration");
                String courseDescription = rs.getString("course_description");

                tableModel.addRow(new Object[]{courseId, courseName, courseDuration, courseDescription});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error fetching courses", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new NewCourseSection();
    }
}
