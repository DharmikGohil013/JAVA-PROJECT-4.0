package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ShowTimeTablesSection extends JFrame {
    private JComboBox<String> departmentComboBox;
    private JTable timetableTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    private Connection connection;

    public ShowTimeTablesSection() {
        setTitle("Show Time Tables");
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

        // Create and configure the table
        tableModel = new DefaultTableModel(new String[] { "Day of Week", "Time Slot", "Subject Name", "Faculty Name" }, 0);
        timetableTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(timetableTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create panel for department selection
        JPanel selectionPanel = new JPanel();
        JLabel departmentLabel = new JLabel("Select Department:");
        departmentComboBox = new JComboBox<>(new String[] { "IT", "CE", "CS", "CD" });
        departmentComboBox.addActionListener(e -> loadTimeTable((String) departmentComboBox.getSelectedItem()));

        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            // Assuming you have a method to return to the previous screen
            new MainMenu(); // Replace with your previous screen or navigation
            dispose();
        });

        selectionPanel.add(departmentLabel);
        selectionPanel.add(departmentComboBox);
        selectionPanel.add(backButton);

        mainPanel.add(selectionPanel, BorderLayout.NORTH);

        // Load initial timetable for default selection
        loadTimeTable("IT");

        // Add the main panel to the JFrame
        add(mainPanel);
        setVisible(true);
    }

    private void loadTimeTable(String department) {
        tableModel.setRowCount(0); // Clear previous data
        String query = "SELECT * FROM timetable WHERE department = ? ORDER BY day_of_week, time_slot";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, department);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String dayOfWeek = rs.getString("day_of_week");
                String timeSlot = rs.getString("time_slot");
                String subjectName = rs.getString("subject_name");
                String facultyName = rs.getString("faculty_name");

                tableModel.addRow(new Object[] { dayOfWeek, timeSlot, subjectName, facultyName });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ShowTimeTablesSection::new);
    }
}
