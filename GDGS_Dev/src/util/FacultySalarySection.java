package util;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class FacultySalarySection extends JFrame {

    private JTable salaryTable;
    private JLabel emailLabel, statusLabel;
    private JButton backButton;
    private String facultyEmail;
    private final double monthlySalary = 200000.00;  // Monthly salary set by the admin
    
    public FacultySalarySection(String email) {
        this.facultyEmail = email;
        initializeComponents();
        fetchSalaryDetails();
    }
    
    // Initialize the GUI components
    private void initializeComponents() {
        // Email label
        emailLabel = new JLabel("Logged in as: " + facultyEmail);
        add(emailLabel);
        
        // Salary table
        salaryTable = new JTable();
        add(new JScrollPane(salaryTable));
        
        // Status label
        statusLabel = new JLabel("Salary Status: ");
        add(statusLabel);
        
        // Back button
        backButton = new JButton("Back");
        add(backButton);
        
        // Action for Back button
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Logic to go back to the previous section
                dispose(); // Close this window
            }
        });
        
        // Set Layout and Frame properties
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Fetch the salary details from the database for the logged-in faculty
    private void fetchSalaryDetails() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Salary Amount");
        model.addColumn("Payment Date");
        model.addColumn("Payment Method");

        double totalPaidThisMonth = 0.0;

        // Get the current month and year
        LocalDate currentDate = LocalDate.now();
        String currentMonth = String.valueOf(currentDate.getMonthValue());
        String currentYear = String.valueOf(currentDate.getYear());

        try {
            // Database connection setup (Modify according to your DB credentials)
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String query = "SELECT id, salary_amount, payment_date, payment_method FROM salary WHERE faculty_email = ? AND MONTH(payment_date) = ? AND YEAR(payment_date) = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, facultyEmail);
            ps.setString(2, currentMonth);  // Current month
            ps.setString(3, currentYear);   // Current year

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                double salaryAmount = rs.getDouble("salary_amount");
                String paymentDate = rs.getString("payment_date");
                String paymentMethod = rs.getString("payment_method");

                totalPaidThisMonth += salaryAmount;

                model.addRow(new Object[]{id, salaryAmount, paymentDate, paymentMethod});
            }

            salaryTable.setModel(model);

            // Check if salary is fully paid or pending
            if (totalPaidThisMonth >= monthlySalary) {
                statusLabel.setText("Salary Status: Paid");
            } else {
                statusLabel.setText("Salary Status: Pending (" + totalPaidThisMonth + " paid out of " + monthlySalary + ")");
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching salary details.");
        }
    }

    public static void main(String[] args) {
        // For demonstration, we'll pass a dummy email to the constructor
        new FacultySalarySection("faculty001@xyz.edu.in");
    }
}
