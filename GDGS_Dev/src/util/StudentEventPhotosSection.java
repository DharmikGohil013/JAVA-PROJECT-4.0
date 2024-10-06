package util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StudentEventPhotosSection extends JFrame {

    // Components
    private JPanel photoPanel;
    private JButton backButton;

    // Database connection details (replace these with your actual values)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_db"; // Replace with your DB URL
    private static final String DB_USER = "root"; // Replace with your DB username
    private static final String DB_PASSWORD = "DHARMIKgohil@2006"; // Replace with your DB password

    public StudentEventPhotosSection() {
        // Set up the frame
        setTitle("Student Event Photos");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set layout
        setLayout(new BorderLayout());

        // Create a scrollable panel to display photos in a grid-like layout
        photoPanel = new JPanel();
        photoPanel.setLayout(new GridLayout(0, 3, 20, 20)); // 3 columns with 20px padding
        photoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Add photo panel inside a scrollable view
        JScrollPane scrollPane = new JScrollPane(photoPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Fetch and display all student event photos
        fetchAndDisplayEventPhotos();

        // Add action listener to the back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current frame
                // Navigate back to the previous section if needed
            }
        });

        // Set frame to be visible
        setVisible(true);
    }

    // Method to fetch and display all event photos
    private void fetchAndDisplayEventPhotos() {
        String query = "SELECT event_name, photo_path, upload_date FROM student_event_photos";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Loop through the result set and display each photo
            while (rs.next()) {
                String eventName = rs.getString("event_name");
                String photoPath = rs.getString("photo_path");
                Timestamp uploadDate = rs.getTimestamp("upload_date");

                // Create photo panel for each event photo
                JPanel singlePhotoPanel = createPhotoPanel(eventName, photoPath, uploadDate);
                photoPanel.add(singlePhotoPanel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching event photos: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to create a panel for each photo with event name and upload date
    private JPanel createPhotoPanel(String eventName, String photoPath, Timestamp uploadDate) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(300, 300)); // Set preferred size for each image block

        // Load the image
        ImageIcon imageIcon = new ImageIcon(photoPath); // Load the image using the path
        Image image = imageIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH); // Scale image to fit
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        panel.add(imageLabel, BorderLayout.CENTER);

        // Add event name and upload date below the image
        JLabel eventLabel = new JLabel("<html><b>Event:</b> " + eventName + "<br><b>Date:</b> " + uploadDate + "</html>");
        eventLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(eventLabel, BorderLayout.SOUTH);

        return panel;
    }

    // Main method to run the example
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentEventPhotosSection::new);
    }
}
