package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitPage {

    JFrame frame;

    public ExitPage() {
        // Create a new JFrame for the exit page
        frame = new JFrame("Exit Application");

        // Create a label with the exit message
        JLabel exitLabel = new JLabel("Are you sure you want to exit?");
        exitLabel.setFont(new Font("Arial", Font.BOLD, 18));
        exitLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create the "Yes" and "No" buttons
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");

        // Set button size and font
        yesButton.setFont(new Font("Arial", Font.PLAIN, 16));
        noButton.setFont(new Font("Arial", Font.PLAIN, 16));

        // Add action listener to "Yes" button to exit the application
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);  // Exit the application
            }
        });

        // Add action listener to "No" button to close the exit window and go back
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the exit window and go back
            }
        });

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        // Add the label and button panel to the frame
        frame.setLayout(new BorderLayout());
        frame.add(exitLabel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Frame settings
        frame.setSize(400, 200);  // Set size of the window
        frame.setLocationRelativeTo(null);  // Center the window
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Close the window on exit
        frame.setVisible(true);  // Make the frame visible
    }

    public static void main(String[] args) {
        new ExitPage();  // Show the Exit Page
    }
}
