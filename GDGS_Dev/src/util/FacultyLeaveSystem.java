package util;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FacultyLeaveSystem extends JFrame {
    private JLabel emailLabel, startDateLabel, endDateLabel, statusLabel;
    private JDateChooser startDateChooser, endDateChooser;
    private JButton applyLeaveButton, backButton;
    private String facultyEmail;
    
    public FacultyLeaveSystem(String email) {
        this.facultyEmail = email;
        initializeComponents();
    }

    // Initialize the GUI components
    private void initializeComponents() {
        // Email label
        emailLabel = new JLabel("Logged in as: " + facultyEmail);
        add(emailLabel);

        // Start date
        startDateLabel = new JLabel("Start Date:");
        add(startDateLabel);
        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        add(startDateChooser);

        // End date
        endDateLabel = new JLabel("End Date:");
        add(endDateLabel);
        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        add(endDateChooser);

        // Apply Leave button
        applyLeaveButton = new JButton("Apply Leave");
        add(applyLeaveButton);

        // Action listener for Apply Leave button
        applyLeaveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyLeave();
            }
        });

        // Back button
        backButton = new JButton("Back");
        add(backButton);

        // Action for Back button
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Logic to go back to the previous section
                dispose(); // Close this window
            }
        });

        // Set Layout and Frame properties
        setLayout(new GridLayout(6, 2));
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Function to apply leave
    private void applyLeave() {
        Date startDate = startDateChooser.getDate();
        Date endDate = endDateChooser.getDate();

        if (startDate == null || endDate == null) {
            JOptionPane.showMessageDialog(this, "Please select both start and end dates.");
            return;
        }

        if (endDate.before(startDate)) {
            JOptionPane.showMessageDialog(this, "End date cannot be before start date.");
            return;
        }

        // Format dates to String
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = sdf.format(startDate);
        String endDateStr = sdf.format(endDate);
        String appliedOnStr = sdf.format(new Date());  // Current date as applied on

        try {
            // Database connection setup (Modify according to your DB credentials)
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String query = "INSERT INTO faculty_leave (faculty_email, start_date, end_date, leave_status, applied_on) VALUES (?, ?, ?, 'Pending', ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, facultyEmail);
            ps.setString(2, startDateStr);
            ps.setString(3, endDateStr);
            ps.setString(4, appliedOnStr);

            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(this, "Leave applied successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error applying leave.");
        }
    }

    public static void main(String[] args) {
        // Example: Launching the leave system for a faculty
        new FacultyLeaveSystem("faculty001@xyz.edu.in");
    }
}
