package util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class StudentEventGallery {

    JFrame frame;
    JPanel galleryPanel;
    JScrollPane scrollPane;

    public StudentEventGallery(int studentId) {
        frame = new JFrame("Student Event Gallery");
        frame.setLayout(new BorderLayout());

        galleryPanel = new JPanel();
        galleryPanel.setLayout(new GridLayout(0, 3, 10, 10)); // 3 images per row

        scrollPane = new JScrollPane(galleryPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Fetch images from the database for the given student ID
        ArrayList<String> imagePaths = fetchEventPhotos(studentId);

        // Add images to the gallery panel
        for (String imagePath : imagePaths) {
            JLabel imageLabel = createImageLabel(imagePath);
            galleryPanel.add(imageLabel);
        }

        // Create an 'Add New Photo' button to upload new images
        JButton uploadButton = new JButton("Add New Photo");
        uploadButton.addActionListener(e -> uploadNewPhoto(studentId));

        // Create a 'Back' button to return to the previous screen
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose(); // Close the current frame
            // Open the previous screen, replace with actual screen
          // Assuming PreviousScreen is a class for the previous page
        });

        // Add the scrollable gallery and buttons to the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(uploadButton);
        buttonPanel.add(backButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    // Create a JLabel with a scaled image and creative styling
    private JLabel createImageLabel(String imagePath) {
        ImageIcon imageIcon = new ImageIcon(imagePath);
        Image image = imageIcon.getImage(); // Get the original image
        
        if (image == null) {
            System.err.println("Image could not be loaded from path: " + imagePath);
            return new JLabel("Image not found");
        }
        
        // Resize the image to 300x300
        Image scaledImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);

        // Create a label with a border and rounded corners
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Add a border

        // Add rounded corners
        imageLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        return imageLabel;
    }

    // Fetch event photos for the student from the database
    private ArrayList<String> fetchEventPhotos(int studentId) {
        ArrayList<String> photoPaths = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/test_db"; // Update with your DB details
        String user = "root";
        String password = "DHARMIKgohil@2006";

        String query = "SELECT photo_path FROM student_event_photos WHERE student_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String photoPath = rs.getString("photo_path");
                
                // Verify if the file exists
                if (new File(photoPath).exists()) {
                    photoPaths.add(photoPath);
                } else {
                    System.err.println("File does not exist at path: " + photoPath);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return photoPaths;
    }

    // Method to upload new photos
    private void uploadNewPhoto(int studentId) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String photoPath = selectedFile.getAbsolutePath();

            // Save the photo to the database
            savePhotoToDatabase(studentId, photoPath);

            // Reload the gallery
            galleryPanel.removeAll();
            ArrayList<String> imagePaths = fetchEventPhotos(studentId);
            for (String imagePath : imagePaths) {
                JLabel imageLabel = createImageLabel(imagePath);
                galleryPanel.add(imageLabel);
            }
            galleryPanel.revalidate();
            galleryPanel.repaint();
        }
    }

    // Save the photo path to the database
    private void savePhotoToDatabase(int studentId, String photoPath) {
        String url = "jdbc:mysql://localhost:3306/test_db";
        String user = "root";
        String password = "DHARMIKgohil@2006";

        String insertQuery = "INSERT INTO student_event_photos (student_id, event_name, photo_path) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setInt(1, studentId);
            pstmt.setString(2, "Event Name");  // Replace with actual event name
            pstmt.setString(3, photoPath);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new StudentEventGallery(1);  // Load gallery for student with ID 1
    }
}
