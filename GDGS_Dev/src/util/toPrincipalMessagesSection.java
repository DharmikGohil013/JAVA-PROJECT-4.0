package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class toPrincipalMessagesSection extends JFrame {
    
    private JTable messagesTable;
    private DefaultTableModel tableModel;
    private JButton backButton;

    // Constructor
    public toPrincipalMessagesSection() {
        // Initialize the GUI components
        initializeComponents();
    }

    private void initializeComponents() {
        // Set frame title
        setTitle("Principal Messages");

        // Create table model and JTable
        String[] columnNames = {"Message ID", "Sender Email", "Message Content", "Message Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        messagesTable = new JTable(tableModel);

        // Sample data for demonstration
        // In a real application, you would fetch this from the database
        addMessageToTable(1, "admin1@xyz.edu.in", "Today was holiday declared", "2024-09-09 11:41:47");

        // Set table properties
        messagesTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(messagesTable);

        // Back button
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 22));
        backButton.setMaximumSize(new Dimension(600, 80));
        backButton.setPreferredSize(new Dimension(600, 80));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the window
            }
        });

        // Layout setup
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        // Frame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen mode
        setVisible(true);
    }

    // Method to add a message to the table
    private void addMessageToTable(int messageId, String senderEmail, String messageContent, String messageDate) {
        tableModel.addRow(new Object[]{messageId, senderEmail, messageContent, messageDate});
    }
}
