package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class UniversityUpdatesSection extends JFrame {

    // Components
    private JTable updatesTable;
    private JButton backButton;

    // Database connection details (replace these with your actual values)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_db"; // Replace with your DB URL
    private static final String DB_USER = "root"; // Replace with your DB username
    private static final String DB_PASSWORD = "DHARMIKgohil@2006"; // Replace with your DB password

    public UniversityUpdatesSection() {
        // Set up the frame
        setTitle("University Updates");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set layout
        setLayout(new BorderLayout());

        // Create components
        updatesTable = new JTable();
        backButton = new JButton("Back");

        // Fetch and display university updates
        fetchUniversityUpdates();

        // Add components to the frame
        add(new JScrollPane(updatesTable), BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        // Add action listener to back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current frame
                // Navigate back to previous section (if needed)
                // For example: new PreviousFrame().setVisible(true); // Uncomment and replace with actual navigation
            }
        });

        // Set frame to be visible
        setVisible(true);
    }

    // Method to fetch university updates from the database
    private void fetchUniversityUpdates() {
        String query = "SELECT * FROM university_updates";  // Query to fetch updates

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Create a model to populate JTable
            updatesTable.setModel(buildTableModel(rs));

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching university updates: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to build a TableModel from ResultSet
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // Names of columns
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Data of the table
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }

    // Main method to run the example
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UniversityUpdatesSection());
    }
}
