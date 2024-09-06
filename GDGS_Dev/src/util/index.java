
package util;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class index implements ActionListener {

    JFrame frame = new JFrame();
    JButton myButton = new JButton("Admin");
    JButton my1Button = new JButton("Student");
    JButton my2Button = new JButton("Faculty");
    JButton my3Button = new JButton("Exit");
    JLabel label = new JLabel("GDGS SYSTEM", SwingConstants.CENTER);

    index() {
       
        BackgroundPanel bgPanel = new BackgroundPanel();
        frame.setContentPane(bgPanel); 

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int labelWidth = 1000;
        int labelHeight = 100;
        int labelX = (screenSize.width - labelWidth) / 2; 
        int labelY = 100;  
        
        label.setBounds(labelX, labelY, labelWidth, labelHeight); 
        label.setFont(new Font("Arial", Font.PLAIN, 100));
        label.setForeground(Color.YELLOW);
          // Added the label to the frame

        // Configure the Admin button
        myButton.setBounds(0, 370, 300, 300);
        myButton.setFocusable(false);
        myButton.addActionListener(this);
        myButton.setForeground(Color.white);
        myButton.setFont(new Font("Arial", Font.PLAIN, 50));
        myButton.setBackground(Color.BLUE);

        // Configure the Student button
        my1Button.setBounds(1620, 0, 300, 300);
        my1Button.setFocusable(false);
        my1Button.addActionListener(this);
        my1Button.setForeground(Color.white);
        my1Button.setFont(new Font("Arial", Font.PLAIN, 50));
        my1Button.setBackground(Color.GRAY);

        // Configure the Faculty button
        my2Button.setBounds(1620, 370, 300, 300);
        my2Button.setFocusable(false);
        my2Button.addActionListener(this);
        my2Button.setForeground(Color.white);
        my2Button.setFont(new Font("Arial", Font.PLAIN, 50));
        my2Button.setBackground(Color.GRAY);

        // Configure the Exit button
        my3Button.setBounds(1620, 740, 300, 300);
        my3Button.setFocusable(false);
        my3Button.addActionListener(this);
        my3Button.setForeground(Color.white);
        my3Button.setFont(new Font("Arial", Font.PLAIN, 50));
        my3Button.setBackground(Color.GRAY);

        // Add buttons to frame
        frame.add(myButton);
        frame.add(my1Button);
        frame.add(my2Button);
        frame.add(my3Button);

        // Frame setup
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    // Single actionPerformed method that handles multiple buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == myButton) {  // If Admin button is clicked
            frame.dispose();
            AdminSection myWindow = new AdminSection();  // Open AdminSection window
        } else if (e.getSource() == my1Button) {  // If Student button is clicked
            frame.dispose();
            StudentSection studentWindow = new StudentSection();  // Open StudentSection window
        } else if (e.getSource() == my2Button) {  // If Faculty button is clicked
            frame.dispose();
            FacultySection facultyWindow = new FacultySection();  // Open FacultySection window
        } else if (e.getSource() == my3Button) {  // If Exit button is clicked
            System.exit(0);  // Terminate the program
        }
    }

    // BackgroundPanel class for setting the background image
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;
    
        public BackgroundPanel() {
            try {
                // Use double backslashes or forward slashes for the image path
                backgroundImage = new ImageIcon("D:\\Git Hub\\JAVA-PROJECT-4.0\\GDGS_Dev\\src\\util\\GDS SYSTEM.png").getImage();
                
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

    // Main method to launch the page
    public static void main(String[] args) {
        new index();
    }
}
