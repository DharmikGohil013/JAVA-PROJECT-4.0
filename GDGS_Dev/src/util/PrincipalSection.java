package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrincipalSection extends JFrame {

    // Define buttons
    private JButton adminButton, viewMessagesButton, viewFeedbackButton, backButton;
    private JLabel welcomeLabel;

    // Constructor to initialize the Principal section
    public PrincipalSection() {
        // Initialize the GUI components
        initializeComponents();
    }

    private void initializeComponents() {
        // Set frame title
        setTitle("Principal Section");

        // Welcome label
        welcomeLabel = new JLabel("Welcome to Principal Section", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Bigger font for the label
        add(welcomeLabel);

        // Initialize buttons with larger font and set max size
        adminButton = createLargeButton("To Admin Messages");
        viewMessagesButton = createLargeButton("View Messages");
        viewFeedbackButton = createLargeButton("View Feedback");
        backButton = createLargeButton("Back");

        // Add action listeners for buttons
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open AdminSection when the button is clicked
                new toAdminSection();
            }
        });

        viewMessagesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "View Messages clicked");
                new toPrincipalMessagesSection();
            }
        });

        viewFeedbackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "View Feedback clicked");
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the window
            }
        });

        // Set Layout and add buttons
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Add some space at the top
        add(Box.createVerticalStrut(40));

        // Add components with spacing
        add(welcomeLabel);
        add(Box.createVerticalStrut(40)); // Spacer
        add(adminButton);
        add(Box.createVerticalStrut(20)); // Spacer
        add(viewMessagesButton);
        add(Box.createVerticalStrut(20)); // Spacer
        add(viewFeedbackButton);
        add(Box.createVerticalStrut(20)); // Spacer
        add(backButton);

        // Frame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen mode
        setVisible(true);
    }

    // Helper method to create a large button with max width
    private JButton createLargeButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 22)); // Bigger font for buttons
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button horizontally
        button.setMaximumSize(new Dimension(600, 80)); // Set maximum width and height
        button.setPreferredSize(new Dimension(600, 80)); // Set preferred size
        return button;
    }

    public static void main(String[] args) {
        // Create and show the Principal section window
        new PrincipalSection();
    }
}
