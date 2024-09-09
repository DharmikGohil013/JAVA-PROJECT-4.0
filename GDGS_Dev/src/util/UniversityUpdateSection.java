package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UniversityUpdateSection extends JFrame {
    private JTextField titleField, contentField;
    private JTextArea updatesArea;
    private JButton addButton, backButton;
    private Connection connection;

    public UniversityUpdateSection() {
        setTitle("University Updates");
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            // Setup database connection
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/test_db", 
                "root", 
                "DHARMIKgohil@2006"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Create and configure main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create and configure the updates area
        updatesArea = new JTextArea(20, 50);
        updatesArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(updatesArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create panel for adding updates
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField(30);

        JLabel contentLabel = new JLabel("Content:");
        contentField = new JTextField(30);

        addButton = new JButton("Add Update");
        addButton.addActionListener(e -> addUpdate());

        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            // Assuming you have a method to return to the previous screen
            new MainMenu(); // Replace with your previous screen or navigation
            dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        addPanel.add(titleLabel, gbc);
        gbc.gridx = 1;
        addPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addPanel.add(contentLabel, gbc);
        gbc.gridx = 1;
        addPanel.add(contentField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        addPanel.add(addButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        addPanel.add(backButton, gbc);

        mainPanel.add(addPanel, BorderLayout.SOUTH);

        // Load existing updates
        loadUpdates();

        // Add the main panel to the JFrame
        add(mainPanel);
        setVisible(true);
    }

    private void loadUpdates() {
        updatesArea.setText("");
        String query = "SELECT * FROM university_updates ORDER BY update_date DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("update_id");
                String title = rs.getString("update_title");
                String content = rs.getString("update_content");
                Date date = rs.getTimestamp("update_date");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(date);

                updatesArea.append(String.format("ID: %d\nTitle: %s\nContent: %s\nDate: %s\n\n",
                    id, title, content, formattedDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addUpdate() {
        String title = titleField.getText();
        String content = contentField.getText();
        String query = "INSERT INTO university_updates (update_title, update_content, update_date) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Update added successfully.");
            titleField.setText("");
            contentField.setText("");
            loadUpdates(); // Refresh the updates list
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding update.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UniversityUpdateSection::new);
    }
}
