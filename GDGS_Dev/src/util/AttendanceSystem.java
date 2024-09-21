package util;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;

public class AttendanceSystem {
    private JFrame frame;
    private JPanel panel;
    private JLabel dateLabel;
    private JDateChooser dateChooser;
    private JLabel timeLabel;
    private JTextField timeField;
    private JLabel subjectLabel;
    private JTextField subjectField;
    private JButton addButton;
    private JButton backButton;
    private List<JCheckBox> checkBoxes;
    private Connection conn;
    private JComboBox<String> branchComboBox;

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            if (conn != null) {
                System.out.println("Connected to database successfully!");
            } else {
                System.out.println("Failed to connect to database!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public AttendanceSystem() {
        connectToDatabase();
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Attendance System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700); // Set the frame size
        panel = new JPanel();
        panel.setLayout(null); // Set layout to null for absolute positioning
        frame.getContentPane().add(panel);
    
        // Create date and time components
        dateLabel = new JLabel("Date:");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 24));
        dateLabel.setBounds(50, 50, 200, 40); // Set position and size for label
        panel.add(dateLabel);
    
        dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Arial", Font.BOLD, 24));
        dateChooser.setBounds(250, 50, 300, 40); // Set position and size for date chooser
        panel.add(dateChooser);
    
        timeLabel = new JLabel("Time:");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timeLabel.setBounds(50, 120, 200, 40);
        panel.add(timeLabel);
    
        timeField = new JTextField(10);
        timeField.setFont(new Font("Arial", Font.BOLD, 24));
        timeField.setBounds(250, 120, 300, 40);
        panel.add(timeField);
    
        subjectLabel = new JLabel("Subject:");
        subjectLabel.setFont(new Font("Arial", Font.BOLD, 24));
        subjectLabel.setBounds(50, 190, 200, 40);
        panel.add(subjectLabel);
    
        subjectField = new JTextField(20);
        subjectField.setFont(new Font("Arial", Font.BOLD, 24));
        subjectField.setBounds(250, 190, 300, 40);
        panel.add(subjectField);
    
        // Create branch dropdown menu
        String[] branches = {"IT", "CE", "CS", "CD"};
        branchComboBox = new JComboBox<>(branches);
        branchComboBox.setFont(new Font("Arial", Font.BOLD, 24));
        branchComboBox.setBounds(50, 260, 300, 40);
        panel.add(branchComboBox);
    
        // Create checkbox panel with scroll
        JPanel checkboxPanel = new JPanel();
        // checkboxPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Changed to FlowLayout for dynamic handling
        checkboxPanel.setPreferredSize(new Dimension(300, 200));
        JScrollPane checkboxScrollPane = new JScrollPane(checkboxPanel);
        checkboxScrollPane.setBounds(600, 50, 300, 300);
        checkboxScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        checkboxScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(checkboxScrollPane);
    
        // Create add and back buttons
        addButton = new JButton("Add Attendance");
        addButton.setFont(new Font("Arial", Font.BOLD, 24));
        addButton.setBounds(50, 350, 300, 50); // Positioning the add button
        panel.add(addButton);
    
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 24));
        backButton.setBounds(50, 420, 300, 50); // Positioning the back button
        panel.add(backButton);
    
        checkBoxes = new ArrayList<>();
    
        frame.setVisible(true);
    
        // Add action listeners
        branchComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBranch = (String) branchComboBox.getSelectedItem();
                if (selectedBranch != null) {
                    retrieveStudents(selectedBranch, checkboxPanel);
                }
            }
        });
    
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAttendance();
            }
        });
    
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }
    
    private void retrieveStudents(String branch, JPanel checkboxPanel) {
        // Clear existing checkboxes
        checkBoxes.clear();
        checkboxPanel.removeAll();
    
        // Retrieve students of selected branch from database
        try (ResultSet rs = conn.prepareStatement("SELECT email FROM student_" + branch.toLowerCase()).executeQuery()) {
            if (conn != null) {
                while (rs.next()) {
                    String email = rs.getString("email");
                    JCheckBox checkBox = new JCheckBox(email);
                    checkBox.setFont(new Font("Arial", Font.BOLD, 24));
                    checkBoxes.add(checkBox);
                    checkboxPanel.add(checkBox);
                }
            } else {
                System.out.println("Connection is null");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error retrieving students: " + e.getMessage());
        }
    
        // Repaint checkbox panel
        checkboxPanel.revalidate();
        checkboxPanel.repaint();
    }
    

    private void addAttendance() {
        // Get selected checkbox values
        List<String> selectedEmails = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                selectedEmails.add(checkBox.getText());
            }
        }

        // Get date, time, and subject values
        String date = dateChooser.getDate().toString();
        String time = timeField.getText();
        String subject = subjectField.getText();

        // Insert attendance data into database
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO attendance (date, time, subject, email) VALUES (?, ?, ?, ?)")) {
            for (String email : selectedEmails) {
                pstmt.setString(1, date);
                pstmt.setString(2, time);
                pstmt.setString(3, subject);
                pstmt.setString(4, email);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error adding attendance: " + e.getMessage());
        }

        // Show success message
        JOptionPane.showMessageDialog(frame, "Attendance added successfully!");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AttendanceSystem();
            }
        });
    }
}
