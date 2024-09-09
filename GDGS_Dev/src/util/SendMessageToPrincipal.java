package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SendMessageToPrincipal extends JFrame {
    private JTextField senderEmailField;
    private JTextArea messageTextArea;
    private JButton sendButton;
    private JButton backButton;
    private Connection connection;

    public SendMessageToPrincipal() {
        setTitle("Send Message to Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    
        // Initialize components
        senderEmailField = new JTextField(20);
        messageTextArea = new JTextArea(10, 30);
        sendButton = new JButton("Send");
        backButton = new JButton("Back");
    
        // Set up layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
        panel.add(new JLabel("Your Email:"));
        panel.add(senderEmailField);
    
        panel.add(new JLabel("Message to Principal:"));
        JScrollPane scrollPane = new JScrollPane(messageTextArea);
        messageTextArea.setLineWrap(true);  // Enable word wrap
        messageTextArea.setWrapStyleWord(true);  // Wrap by word
        panel.add(scrollPane);
    
        panel.add(sendButton);
        panel.add(backButton);
    
        add(panel);
    
        // Initialize database connection
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit if the connection fails
        }
    
        // Add action listeners
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new inAdminSection("admin_email@example.com");  // Navigate back to admin section
                dispose(); // Close the current window
            }
        });
    
        // Make the frame visible
        setVisible(true); // Add this to ensure the window is shown when created
    }
    

    private void sendMessage() {
        String senderEmail = senderEmailField.getText().trim();
        String messageContent = messageTextArea.getText().trim();

        // Validate input
        if (senderEmail.isEmpty() || messageContent.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both email and message fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Prepare SQL query to insert message into the database
        String query = "INSERT INTO principal_messages (sender_email, message_content) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, senderEmail);
            pstmt.setString(2, messageContent);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Message sent successfully to Principal!");

            // Clear the fields after sending the message
            senderEmailField.setText("");
            messageTextArea.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send message", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SendMessageToPrincipal().setVisible(true);

            }
        });
    }
}
