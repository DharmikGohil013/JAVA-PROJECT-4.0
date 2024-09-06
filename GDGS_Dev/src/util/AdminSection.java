package util;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminSection implements ActionListener {

    JFrame frame = new JFrame();
    
    JTextField emailField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("Login");
    JButton backButton = new JButton("Back");

    AdminSection() {
        BackgroundPanel bgPanel = new BackgroundPanel();
        frame.setContentPane(bgPanel); 

        // Text area setup
        emailField.setBounds(400, 430, 400, 50);
        emailField.setFont(new Font("Arial", Font.PLAIN, 30));
        passwordField.setBounds(400, 560, 400, 50);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 30));
        loginButton.setBounds(480, 650, 200, 60);
        loginButton.setFont(new Font("Arial", Font.PLAIN, 30));
        loginButton.addActionListener(this);
        
        backButton.setBounds(1800, 900, 100, 50);
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.addActionListener(this);

        frame.add(emailField);
        frame.add(loginButton);
        frame.add(passwordField);
        frame.add(backButton);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;
    
        public BackgroundPanel() {
            try {
                backgroundImage = new ImageIcon("D:\\Git Hub\\JAVA-PROJECT-4.0\\GDGS_Dev\\src\\util\\GDS SYSTEM(4).png").getImage();
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
            new inadminpage();  // Open the Hello World page
        } else {
            JOptionPane.showMessageDialog(null, "Invalid email or password");
        }
    } else if (e.getSource() == backButton) {
        frame.dispose();  // Close the current AdminSection window
        new index();  // Go back to the launch page
    }
}


private boolean validateAdminLogin(String email, String password) {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        // Get database connection
        conn = DBConnection.getConnection();
        // Prepare the SQL query to check email and password from the `admin` table
        String query = "SELECT * FROM admin WHERE email = ? AND password = ?";
        ps = conn.prepareStatement(query);
        ps.setString(1, email);
        ps.setString(2, password);

        // Execute the query
        rs = ps.executeQuery();

        // Debug output to check ResultSet
        if (rs.next()) {
            System.out.println("Login successful. Record found.");
            return true;
        } else {
            System.out.println("No matching record found.");
            return false;
        }
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
private boolean testDatabaseConnection() {
    Connection conn = null;
    try {
        conn = DBConnection.getConnection();
        if (conn != null && !conn.isClosed()) {
            System.out.println("Database connection successful.");
            return true;
        } else {
            System.err.println("Database connection failed.");
            return false;
        }
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    } finally {
        // Close connection if it was created
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



    public static void main(String[] args) {
        new AdminSection();
    }
}
