package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FeesSection {
    JFrame frame;
    JLabel pendingFeesLabel;
    JButton backButton;
    String loggedInEmail;

    public FeesSection(String email) {
        loggedInEmail = email;  // Store the logged-in email
        frame = new JFrame("Fees Section");
        pendingFeesLabel = new JLabel("Pending Fees: ");
        backButton = new JButton("Back");

        // Set layout and bounds
        frame.setLayout(null);
        pendingFeesLabel.setBounds(300, 150, 400, 30);
        backButton.setBounds(100, 200, 100, 30);

        // Add components to the frame
        frame.add(pendingFeesLabel);
        frame.add(backButton);

        // Back Button ActionListener
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new inStudentSection(loggedInEmail);  // Navigate back to the previous screen
            }
        });

        // Fetch and display pending fees
        showPendingFees();

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private void showPendingFees() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
    
        try {
            // Get database connection
            conn = DBConnection.getConnection();
    
            // Prepare SQL query to get the total fees and paying fees
            String query = "SELECT total_fees, (total_fees - IFNULL(paying_fees_sum, 0)) AS pending_fees " +
                           "FROM (SELECT total_fees, " +
                           "             (SELECT SUM(paying_fees) FROM fees WHERE email = ?) AS paying_fees_sum " +
                           "      FROM fees " +
                           "      WHERE email = ?) AS fees_info";
    
            ps = conn.prepareStatement(query);
            ps.setString(1, loggedInEmail);
            ps.setString(2, loggedInEmail);
    
            // Execute query
            rs = ps.executeQuery();
    
            // Display pending fees
            if (rs.next()) {
                double totalFees = rs.getDouble("total_fees");
                double pendingFees = rs.getDouble("pending_fees");
    
                // Display pending fees
                if (pendingFees <= 0) {
                    pendingFeesLabel.setText("Pending Fees: Full fees are paid.");
                } else {
                    pendingFeesLabel.setText("Pending Fees: " + pendingFees);
                }
            } else {
                pendingFeesLabel.setText("No fee record found for this email.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            pendingFeesLabel.setText("Error fetching fees information.");
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
