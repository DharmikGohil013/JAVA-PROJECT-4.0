package util;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.toedter.calendar.JCalendar;
import java.text.SimpleDateFormat;

public class SalarySection extends JFrame {
    JFrame frame;
    private JTextField facultyEmailField;
    private JTextField salaryAmountField;
    private JCalendar paymentDateCalendar;
    private JComboBox<String> paymentMethodComboBox;
    private JButton addButton;
    private JButton backButton;
    private Connection connection;

    public SalarySection() {
        setTitle("Salary Section");

        // Make the frame full screen
        GraphicsDevice device = GraphicsEnvironment
                                    .getLocalGraphicsEnvironment()
                                    .getDefaultScreenDevice();
        setUndecorated(true); // Remove window borders and title bar
        device.setFullScreenWindow(this); // Set full screen mode

        // Initialize components
        facultyEmailField = new JTextField(20);
        salaryAmountField = new JTextField(20);
        paymentDateCalendar = new JCalendar();
        paymentMethodComboBox = new JComboBox<>(new String[]{"Cash", "Online", "Check"});
        addButton = new JButton("Add");
        backButton = new JButton("Back");

        // Set up the layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Faculty Email:"), gbc);
        gbc.gridx = 1;
        panel.add(facultyEmailField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Salary Amount:"), gbc);
        gbc.gridx = 1;
        panel.add(salaryAmountField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Payment Date:"), gbc);
        gbc.gridx = 1;
        panel.add(paymentDateCalendar, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        panel.add(paymentMethodComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(addButton, gbc);
        
        gbc.gridy = 5;
        panel.add(backButton, gbc);
    
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
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSalary();
            }
        });
    
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
            }
        });
    }

    private void addSalary() {
        // Ensure all UI components are properly initialized
        if (facultyEmailField == null || salaryAmountField == null ||
            paymentDateCalendar == null || paymentMethodComboBox == null || connection == null) {
            JOptionPane.showMessageDialog(this, "UI components or database connection are not initialized properly", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String facultyEmail = facultyEmailField.getText().trim();
        String salaryAmountStr = salaryAmountField.getText().trim();
        String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();

        if (facultyEmail.isEmpty() || salaryAmountStr.isEmpty() || paymentMethod == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double salaryAmount;
        try {
            salaryAmount = Double.parseDouble(salaryAmountStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid salary amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(paymentDateCalendar.getDate());

        String query = "INSERT INTO salary (faculty_email, salary_amount, payment_date, payment_method) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, facultyEmail);
            pstmt.setDouble(2, salaryAmount);
            pstmt.setString(3, formattedDate);
            pstmt.setString(4, paymentMethod);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Salary added successfully");
            facultyEmailField.setText("");
            salaryAmountField.setText("");
            paymentMethodComboBox.setSelectedIndex(0);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add salary", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SalarySection().setVisible(true);
            }
        });
    }
}
