package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class toAdminSection extends JFrame {

    private JTable messageTable;
    private DefaultTableModel model;
    private JButton addButton, editButton, deleteButton, backButton;
    private JTextArea messageInputArea;

    // Constructor to initialize the Admin section
    public toAdminSection() {
        initializeComponents();
        fetchMessages();
    }

    private void initializeComponents() {
        // Set frame title
        setTitle("Admin Messages");

        // Initialize table model and JTable
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Sender Email");
        model.addColumn("Message Content");
        model.addColumn("Sent At");
        
        messageTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(messageTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));

        // Initialize buttons
        addButton = createLargeButton("Add Message");
        editButton = createLargeButton("Edit Message");
        deleteButton = createLargeButton("Delete Message");
        backButton = createLargeButton("Back");

        // Initialize input area for message
        messageInputArea = new JTextArea(5, 30);
        messageInputArea.setLineWrap(true);
        messageInputArea.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane(messageInputArea);

        // Add action listeners for buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMessage();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editMessage();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMessage();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the window
            }
        });

        // Set Layout and add components
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Add components with spacing
        add(new JLabel("Messages to Admin:"));
        add(scrollPane);
        add(new JLabel("Write your message:"));
        add(inputScrollPane);
        add(Box.createVerticalStrut(20)); // Spacer
        add(addButton);
        add(Box.createVerticalStrut(10)); // Spacer
        add(editButton);
        add(Box.createVerticalStrut(10)); // Spacer
        add(deleteButton);
        add(Box.createVerticalStrut(20)); // Spacer
        add(backButton);

        // Frame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setVisible(true);
    }

    // Helper method to create a large button
    private JButton createLargeButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18)); // Bigger font for buttons
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button horizontally
        button.setMaximumSize(new Dimension(600, 80)); // Set maximum width and height
        button.setPreferredSize(new Dimension(600, 80)); // Set preferred size
        return button;
    }

    // Fetch messages from the database
    private void fetchMessages() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String query = "SELECT * FROM admin_messages WHERE sender_email = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "faculty001@xyz.edu.in"); // Assuming the principal's email

            ResultSet rs = ps.executeQuery();
            model.setRowCount(0); // Clear existing rows

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("sender_email"));
                row.add(rs.getString("message_content"));
                row.add(rs.getTimestamp("sent_at"));
                model.addRow(row);
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching messages.");
        }
    }

    // Add message to the database
    private void addMessage() {
        String messageContent = messageInputArea.getText().trim();
        if (messageContent.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a message.");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String query = "INSERT INTO admin_messages (sender_email, message_content, sent_at) VALUES (?, ?, NOW())";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "principal@school.edu"); // Assuming the principal's email
            ps.setString(2, messageContent);
            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(null, "Message added successfully.");
            messageInputArea.setText(""); // Clear input area
            fetchMessages(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding message.");
        }
    }

    // Edit selected message
    private void editMessage() {
        int selectedRow = messageTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a message to edit.");
            return;
        }

        String newMessageContent = messageInputArea.getText().trim();
        if (newMessageContent.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a new message.");
            return;
        }

        try {
            int messageId = (int) model.getValueAt(selectedRow, 0); // Get message ID

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String query = "UPDATE admin_messages SET message_content = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, newMessageContent);
            ps.setInt(2, messageId);
            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(null, "Message updated successfully.");
            messageInputArea.setText(""); // Clear input area
            fetchMessages(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating message.");
        }
    }

    // Delete selected message
    private void deleteMessage() {
        int selectedRow = messageTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a message to delete.");
            return;
        }

        int messageId = (int) model.getValueAt(selectedRow, 0); // Get message ID

        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this message?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
                String query = "DELETE FROM admin_messages WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, messageId);
                ps.executeUpdate();
                conn.close();

                JOptionPane.showMessageDialog(null, "Message deleted successfully.");
                fetchMessages(); // Refresh the table
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error deleting message.");
            }
        }
    }

    public static void main(String[] args) {
        new toAdminSection();
    }
}
