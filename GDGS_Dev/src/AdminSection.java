import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;
public class AdminSection implements ActionListener
{
 JFrame frame = new JFrame();
 
 JTextField emailField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("Login");
 
 AdminSection()
 
 {


    BackgroundPanel bgPanel = new BackgroundPanel();
    frame.setContentPane(bgPanel); 


    //text area

    emailField.setBounds(400, 430, 400, 50);
    emailField.setFont(new Font("Arial", Font.PLAIN, 30));
    passwordField.setBounds(400, 560, 400, 50);
    passwordField.setFont(new Font("Arial", Font.PLAIN, 30));
    loginButton.setBounds(480, 650, 200, 60);
    loginButton.setFont(new Font("Arial", Font.PLAIN, 30));
    loginButton.addActionListener(this);

    frame.add(emailField);
    frame.add(loginButton);
    frame.add(passwordField);

    //text area over
  
  
  
  
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.setVisible(true);   
}
class BackgroundPanel extends JPanel {
        private Image backgroundImage;
    
        public BackgroundPanel() {
            try {
                // Use double backslashes or forward slashes for the image path
                backgroundImage = new ImageIcon("D:\\Git Hub\\JAVA-PROJECT-4.0\\GDGS_Dev\\GDS SYSTEM(4).png").getImage();
                
                if (backgroundImage == null) {
                    System.err.println("Failed to load image from path: D:\\Git Hub\\JAVA-PROJECT-4.0\\GDGS_Dev\\GDS SYSTEM.png");
                } else {
                    System.out.println("Image loaded successfully.");
                }
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                System.err.println("Background image is null.");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Check if the login button was clicked
        if (e.getSource() == loginButton) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());  // Get the password input
            
            // For demonstration purposes, let's print the entered values
            JOptionPane.showMessageDialog(null, "Email: " + email + "\nPassword: " + password);
        }
    }
    public static void main(String[] args) {
        // Create and show the login form
        new FacultySection();
    }

}
