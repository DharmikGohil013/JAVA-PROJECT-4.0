package util;

import com.toedter.calendar.JCalendar;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class addFeesSection extends JFrame {
    private JTable feesTable;
    private DefaultTableModel tableModel;
    private JButton backButton, addButton;
    private JCalendar calendar;
    private JComboBox<String> paymentMethodComboBox;
    private JTextField emailField, payingFeesField;
    private Connection connection;

    public addFeesSection() {
        setTitle("Fees Section");
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
        tableModel = new DefaultTableModel(new String[] { "Email", "Total Fees", "Paying Fees", "Payment Method", "Date", "Time" }, 0);
        feesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(feesTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create panel for JCalendar and back button
        JPanel topPanel = new JPanel(new BorderLayout());

        // Initialize JCalendar
        calendar = new JCalendar();
        calendar.addPropertyChangeListener(e -> {
            if ("calendar".equals(e.getPropertyName())) {
                // Reload fees data based on selected date
                loadFeesData();
            }
        });
        
        topPanel.add(calendar, BorderLayout.CENTER);

        // Panel for input fields and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 5, 5));

        // Email field
        inputPanel.add(new JLabel("Student Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        // Paying fees field
        inputPanel.add(new JLabel("Paying Fees:"));
        payingFeesField = new JTextField();
        inputPanel.add(payingFeesField);

        // Payment method combo box
        inputPanel.add(new JLabel("Payment Method:"));
        paymentMethodComboBox = new JComboBox<>(new String[] { "Cash", "Online", "Check" });
        inputPanel.add(paymentMethodComboBox);

        // Add button
        addButton = new JButton("Add Fees");
        addButton.addActionListener(e -> addFees());
        inputPanel.add(addButton);

        topPanel.add(inputPanel, BorderLayout.SOUTH);

        // Panel for back button
        JPanel backPanel = new JPanel();
        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            // Navigate to the previous screen or main menu
            new MainMenu(); // Replace with your previous screen or navigation
            dispose();
        });

        backPanel.add(backButton);
        topPanel.add(backPanel, BorderLayout.NORTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Load fees data into the table
        loadFeesData();

        // Add the main panel to the JFrame
        add(mainPanel);
        setVisible(true);
    }

    private void loadFeesData() {
        tableModel.setRowCount(0); // Clear previous data
        String query = "SELECT * FROM fees";

        // If a specific date is selected, filter the query by that date
        java.util.Date selectedDate = calendar.getDate();
        if (selectedDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(selectedDate);
            query += " WHERE date = '" + formattedDate + "'";
        }

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String email = rs.getString("email");
                double totalFees = rs.getDouble("total_fees");
                double payingFees = rs.getDouble("paying_fees");
                String paymentMethod = rs.getString("payment_method");
                Date date = rs.getDate("date");
                Time time = rs.getTime("time");

                // Format date and time
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String formattedDate = date != null ? dateFormat.format(date) : "N/A";
                String formattedTime = time != null ? timeFormat.format(time) : "N/A";

                tableModel.addRow(new Object[] { email, totalFees, payingFees, paymentMethod, formattedDate, formattedTime });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addFees() {
        String email = emailField.getText().trim();
        String payingFeesStr = payingFeesField.getText().trim();
        String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();
    
        if (email.isEmpty() || payingFeesStr.isEmpty() || paymentMethod == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        double payingFees;
        try {
            payingFees = Double.parseDouble(payingFeesStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid fees amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(calendar.getDate());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = timeFormat.format(new Date());
    
        // Fixed total_fees value
        double totalFees = 128000.00;
    
        String query = "INSERT INTO fees (email, total_fees, paying_fees, payment_method, date, time) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
    
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Set parameters for the SQL query
            pstmt.setString(1, email);
            pstmt.setDouble(2, totalFees);
            pstmt.setDouble(3, payingFees);
            pstmt.setString(4, paymentMethod);
            pstmt.setString(5, formattedDate);
            pstmt.setString(6, formattedTime);
            pstmt.executeUpdate();
            loadFeesData(); // Refresh table data after insertion
            JOptionPane.showMessageDialog(this, "Fees added successfully");
            emailField.setText("");
            payingFeesField.setText("");
            paymentMethodComboBox.setSelectedIndex(0);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add fees", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(addFeesSection::new);
    }
}
