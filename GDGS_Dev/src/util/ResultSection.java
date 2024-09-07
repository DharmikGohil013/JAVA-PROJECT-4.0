package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ResultSection {

    JFrame frame;
    JTextField semesterField;
    JButton checkButton, backButton;
    JLabel resultLabel;
    String loggedInEmail;

    public ResultSection(String email) {
        loggedInEmail = email;
        frame = new JFrame("Result Section");
        semesterField = new JTextField();
        checkButton = new JButton("Check Result");
        backButton = new JButton("Back");
        resultLabel = new JLabel("Result: ");

        // Set layout and bounds
        frame.setLayout(null);
        semesterField.setBounds(300, 150, 200, 30);
        checkButton.setBounds(300, 200, 200, 30);
        backButton.setBounds(100, 250, 100, 30);
        resultLabel.setBounds(300, 250, 400, 30);

        // Add components to the frame
        frame.add(semesterField);
        frame.add(checkButton);
        frame.add(backButton);
        frame.add(resultLabel);

        // Check Button ActionListener
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String semester = semesterField.getText();
                fetchResult(semester);
            }
        });

        // Back Button ActionListener
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new inStudentSection(loggedInEmail); // Navigate back to the previous screen
            }
        });

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private void fetchResult(String semester) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Get database connection
            conn = DBConnection.getConnection();

            // Prepare SQL query
            String query = "SELECT result FROM results WHERE email_id = ? AND semester = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, loggedInEmail);
            ps.setString(2, semester);

            // Execute query
            rs = ps.executeQuery();

            // Display result
            if (rs.next()) {
                String result = rs.getString("result");
                resultLabel.setText("Result: " + result);
            } else {
                resultLabel.setText("No result found for the given semester.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultLabel.setText("Error fetching result.");
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
