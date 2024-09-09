package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminMessageView extends JFrame {
    private JTable messagesTable;
    private DefaultTableModel tableModel;
    private Connection connection;
    private JButton backButton;

    public AdminMessageView() {
        setTitle("View Messages from Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window on the screen

        // Initialize database connection
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit if the connection fails
        }

        // Initialize table model and set up columns
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Sender Email");
        tableModel.addColumn("Message Content");
        tableModel.addColumn("Sent At");

        // Initialize table and set its model
        messagesTable = new JTable(tableModel);

        // Set larger font for better readability
        Font tableFont = new Font("Arial", Font.PLAIN, 18);
        messagesTable.setFont(tableFont);
        messagesTable.setRowHeight(30);

        // Set table header font larger
        Font headerFont = new Font("Arial", Font.BOLD, 20);
        messagesTable.getTableHeader().setFont(headerFont);

        JScrollPane scrollPane = new JScrollPane(messagesTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add back button
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        add(backButton, BorderLayout.SOUTH);
        
        // Back button functionality to close the current window and navigate to the admin section
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new inAdminSection("admin_email@example.com");  // Adjust the email value as needed
                dispose(); // Close the current window
            }
        });

        loadMessages();  // Load messages into the table

        // Full-screen setup with taskbar visible (removed full screen setting)
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true); // Maximize the window without hiding the taskbar
    }

    // Method to load messages from the database
    private void loadMessages() {
        String query = "SELECT * FROM admin_messages ORDER BY sent_at DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String senderEmail = rs.getString("sender_email");
                String messageContent = rs.getString("message_content");
                String sentAt = rs.getString("sent_at");

                // Add the data to the table
                tableModel.addRow(new Object[]{id, senderEmail, messageContent, sentAt});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load messages", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Ensure that the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminMessageView().setVisible(true);  // Show the message viewer
            }
        });
    }
}
