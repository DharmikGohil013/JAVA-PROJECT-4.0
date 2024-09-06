import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class LaunchPage implements ActionListener {

    JFrame frame = new JFrame();
    JButton myButton = new JButton("Admin");
    JButton my1Button = new JButton("Student");
    JButton my2Button = new JButton("Faculty");
    JButton my3Button = new JButton("Exit");
    JLabel labal = new JLabel("GDGS SYSTEM", SwingConstants.CENTER);

    LaunchPage() {
       
        BackgroundPanel bgPanel = new BackgroundPanel();

        frame.setContentPane(bgPanel); 
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int labelWidth = 1000;
        int labelHeight = 100;
        int labelX = (screenSize.width - labelWidth) / 2; 
        int labelY = 100;  
        labal.setBounds(labelX, labelY, labelWidth, labelHeight); 
        labal.setFont(new Font("Arial", Font.PLAIN, 100));
        labal.setForeground(Color.YELLOW);
        frame.add(labal);
        myButton.setBounds(0, 370, 300, 300);
        myButton.setFocusable(false);
        myButton.addActionListener(this);
        myButton.setForeground(Color.white);
        myButton.setFont(new Font("Arial", Font.PLAIN, 50));
        myButton.setBackground(Color.BLUE);

        my1Button.setBounds(1620, 0, 300, 300);
        my1Button.setFocusable(false);
        my1Button.addActionListener(this);
        my1Button.setForeground(Color.white);
        my1Button.setFont(new Font("Arial", Font.PLAIN, 50));
        my1Button.setBackground(Color.GRAY);
        
        my2Button.setBounds(1620, 370, 300, 300);
        my2Button.setFocusable(false);
        my2Button.addActionListener(this);
        my2Button.setForeground(Color.white);
        my2Button.setFont(new Font("Arial", Font.PLAIN, 50));
        my2Button.setBackground(Color.GRAY);

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == myButton) {
            frame.dispose();
            NewWindow myWindow = new NewWindow();
        }
    }

    
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;
    
        public BackgroundPanel() {
            try {
                // Attempt to load the background image
                backgroundImage = new ImageIcon("D:\\Git Hub\\JAVA-PROJECT-4.0\\GDGS_Dev\\3.png").getImage();
    
                // Check if the image was loaded successfully
                if (backgroundImage == null) {
                    System.err.println("Failed to load image from path: D:\\ALL BG PHOTOS\\3.png");
                } else {
                    System.out.println("Image loaded successfully.");
                }
            } catch (Exception e) {
                // Print any exceptions that occur
                System.err.println("Error loading image: " + e.getMessage());
            }
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // Draw the background image, scaled to fit the entire window
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Print an error message if the image is not loaded
                System.err.println("Background image is null.");
            }   
        }
    }

    public static void main(String[] args) {
        new LaunchPage();
    }
}
