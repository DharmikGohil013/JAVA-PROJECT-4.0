package util;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentSection implements ActionListener {

    JFrame frame = new JFrame();
    
    JTextField emailField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("Login");
    JButton backButton = new JButton("Back");

    StudentSection() {
        BackgroundPanel bgPanel = new BackgroundPanel();
        frame.setContentPane(bgPanel); 

        // Styling for text fields
        emailField.setBounds(400, 430, 400, 50);
        emailField.setFont(new Font("Arial", Font.PLAIN, 24));
        emailField.setBackground(Color.WHITE);
        emailField.setBorder(new LineBorder(Color.GRAY, 2));
        emailField.setForeground(Color.BLACK);

        passwordField.setBounds(400, 560, 400, 50);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 24));
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(new LineBorder(Color.GRAY, 2));
        passwordField.setForeground(Color.BLACK);

        // Styling for Login button
        loginButton.setBounds(450, 630, 200, 60);
        loginButton.setFont(new Font("Arial", Font.BOLD, 26));
        loginButton.setBackground(new Color(51, 153, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(new LineBorder(new Color(0, 102, 204), 3));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(this);
        
        // Styling for Back button
        backButton.setBounds(1700, 850, 100, 50);
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setBackground(new Color(255, 51, 51));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(new LineBorder(new Color(204, 0, 0), 3));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(this);

        // Add components to frame
        frame.add(emailField);
        frame.add(loginButton);
        frame.add(passwordField);
        frame.add(backButton);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  // Full-screen
        frame.setLayout(null);
        frame.setVisible(true);
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;
    
        public BackgroundPanel() {
            try {
                backgroundImage = new ImageIcon("D:\\Git Hub\\JAVA-PROJECT-4.0\\GDGS_Dev\\src\\util\\GDS SYSTEM(5).png").getImage();
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());  // Get the password input

            if (validateAdminLogin(email, password)) {
                // On successful login, open the HelloWorldPage
                frame.dispose();  // Close the current AdminSection window
                new inStudentSection(email);  // Open the student section page
            } else {
                JOptionPane.showMessageDialog(null, "Invalid email or password");
            }
        } else if (e.getSource() == backButton) {
            frame.dispose();  // Close the current window
            new index();  // Go back to the launch page
        }
    }

    private boolean validateAdminLogin(String email, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String[] tables = {"student_it", "student_ce", "student_cs", "student_cd"};  // List of tables to check

        try {
            // Get database connection
            conn = DBConnection.getConnection();

            // Loop through each table to check if email and password match
            for (String table : tables) {
                String query = "SELECT * FROM " + table + " WHERE email = ? AND password = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, email);
                ps.setString(2, password);

                // Execute the query
                rs = ps.executeQuery();

                // If a matching record is found in the current table
                if (rs.next()) {
                    System.out.println("Login successful. Record found in table: " + table);
                    return true;  // Return true immediately if a match is found
                }
            }

            // If no record was found in any table
            System.out.println("No matching record found in any table.");
            return false;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new StudentSection();
    }
}
