package util;

import com.toedter.calendar.JCalendar;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNextHolidaySection {

    private JFrame frame;
    private JCalendar calendar;
    private JTextField descriptionField;
    private JComboBox<String> departmentComboBox;
    private JTable holidayTable;
    private DefaultTableModel tableModel;

    public AddNextHolidaySection() {
        frame = new JFrame("Add Next Holiday Info");
        frame.setLayout(new BorderLayout());

        // Create panel for form inputs
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // JCalendar for date selection
        calendar = new JCalendar();
        calendar.setPreferredSize(new Dimension(250, 200)); // Medium size

        // Form fields
        descriptionField = new JTextField();
        descriptionField.setPreferredSize(new Dimension(200, 30)); // Medium size

        // Department dropdown
        departmentComboBox = new JComboBox<>(new String[]{"IT", "CE", "CS", "CD"});
        departmentComboBox.setPreferredSize(new Dimension(200, 30)); // Medium size

        // Add fields to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Holiday Date:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(calendar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Holiday Description:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(departmentComboBox, gbc);

        // Add input panel to frame
        frame.add(inputPanel, BorderLayout.NORTH);

        // Add button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16)); // Larger font
        backButton.setPreferredSize(new Dimension(120, 40)); // Medium size
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current frame
                // You might want to add logic to show the previous window here
            }
        });
        buttonPanel.add(backButton);

        // Add button
        JButton addButton = new JButton("Add Holiday Info");
        addButton.setFont(new Font("Arial", Font.BOLD, 16)); // Larger font
        addButton.setPreferredSize(new Dimension(180, 40)); // Medium size
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addHolidayInfo();
            }
        });
        buttonPanel.add(addButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Create table to show existing holidays
        tableModel = new DefaultTableModel(new String[]{"Holiday ID", "Holiday Date", "Description", "Department"}, 0);
        holidayTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(holidayTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Load existing holiday data
        loadHolidayData();

        // Frame settings
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set fullscreen
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void addHolidayInfo() {
        // Retrieve date from JCalendar
        Date selectedDate = calendar.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(frame, "Please select a holiday date");
            return;
        }
        String holidayDate = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);

        String description = descriptionField.getText();
        String department = (String) departmentComboBox.getSelectedItem();

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter holiday description");
            return;
        }

        try {
            // Use your database credentials
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String sql = "INSERT INTO next_holiday (holiday_date, holiday_description, department) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, holidayDate);
            pstmt.setString(2, description);
            pstmt.setString(3, department);
            pstmt.executeUpdate();

            // Refresh the table data
            loadHolidayData();

            // Clear input fields
            calendar.setDate(null);
            descriptionField.setText("");

            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding holiday info");
        }
    }

    private void loadHolidayData() {
        try {
            // Use your database credentials
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String sql = "SELECT * FROM next_holiday";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("holiday_id"),
                        rs.getDate("holiday_date"),
                        rs.getString("holiday_description"),
                        rs.getString("department")
                });
            }

            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading holiday data");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AddNextHolidaySection();
            }
        });
    }
}
