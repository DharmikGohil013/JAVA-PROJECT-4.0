package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminMessageView extends JFrame {
    private JTable messagesTable;
    private DefaultTableModel tableModel;
    private Connection connection;

    public AdminMessageView() {
        setTitle("View Messages from Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

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
        JScrollPane scrollPane = new JScrollPane(messagesTable);
        add(scrollPane, BorderLayout.CENTER);

        loadMessages();  // Load messages into the table

        // Full-screen setup
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        setUndecorated(true); // Remove window borders and title bar
        device.setFullScreenWindow(this); // Set the window to full-screen mode
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
