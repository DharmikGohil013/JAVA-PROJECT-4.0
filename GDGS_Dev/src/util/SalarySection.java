package util;
import javax.swing.*;
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    
        // Initialize components
        facultyEmailField = new JTextField(20);
        salaryAmountField = new JTextField(20);
        paymentDateCalendar = new JCalendar();
        paymentMethodComboBox = new JComboBox<>(new String[]{"Cash", "Online", "Check"});
        addButton = new JButton("Add");
        backButton = new JButton("Back");
    
        // Set up the layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        panel.add(new JLabel("Faculty Email:"));
        panel.add(facultyEmailField);
        
        panel.add(new JLabel("Salary Amount:"));
        panel.add(salaryAmountField);
        
        panel.add(new JLabel("Payment Date:"));
        panel.add(paymentDateCalendar);
        
        panel.add(new JLabel("Payment Method:"));
        panel.add(paymentMethodComboBox);
        
        panel.add(addButton);
        panel.add(backButton);
    
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
        // Ensure that the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and display the SalarySection frame
                new SalarySection().setVisible(true);
            }
        });
    }
}
