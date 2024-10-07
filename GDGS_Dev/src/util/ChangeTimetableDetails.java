
package util;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class ChangeTimetableDetails {

    private JFrame frame;
    private JComboBox<String> branchComboBox, dayComboBox, timeComboBox;
    private JTextField subjectField, facultyField;
    private JButton changeButton, backButton;
    private JTable timetableTable;
    private DefaultTableModel tableModel;

    public ChangeTimetableDetails() {
        frame = new JFrame("Change Timetable Details");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel branchLabel = new JLabel("Select Branch:");
        branchLabel.setBounds(50, 50, 100, 30);
        frame.add(branchLabel);

        // Branch ComboBox (Example branches)
        String[] branches = {"IT", "CE", "CS", "CD"};
        branchComboBox = new JComboBox<>(branches);
        branchComboBox.setBounds(150, 50, 200, 30);
        frame.add(branchComboBox);

        JLabel dayLabel = new JLabel("Select Day:");
        dayLabel.setBounds(50, 100, 100, 30);
        frame.add(dayLabel);

        // Day ComboBox
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        dayComboBox = new JComboBox<>(days);
        dayComboBox.setBounds(150, 100, 200, 30);
        frame.add(dayComboBox);

        JLabel timeLabel = new JLabel("Select Time Slot:");
        timeLabel.setBounds(50, 150, 100, 30);
        frame.add(timeLabel);

        // Time Slot ComboBox
        String[] timeSlots = {"9:10 to 10:10", "10:10 to 11:10", "12:10 to 1:10", "1:10 to 2:10", "2:20 to 3:20", "3:20 to 4:20"};
        timeComboBox = new JComboBox<>(timeSlots);
        timeComboBox.setBounds(150, 150, 200, 30);
        frame.add(timeComboBox);

        JLabel subjectLabel = new JLabel("New Subject:");
        subjectLabel.setBounds(50, 200, 100, 30);
        frame.add(subjectLabel);

        // Subject Text Field
        subjectField = new JTextField();
        subjectField.setBounds(150, 200, 200, 30);
        frame.add(subjectField);

        JLabel facultyLabel = new JLabel("New Faculty:");
        facultyLabel.setBounds(50, 250, 100, 30);
        frame.add(facultyLabel);

        // Faculty Text Field
        facultyField = new JTextField();
        facultyField.setBounds(150, 250, 200, 30);
        frame.add(facultyField);

        // Change Button
        changeButton = new JButton("Change");
        changeButton.setBounds(150, 300, 100, 30);
        frame.add(changeButton);

        // Back Button
        backButton = new JButton("Back");
        backButton.setBounds(270, 300, 100, 30);
        frame.add(backButton);

        // Table for displaying timetable
        tableModel = new DefaultTableModel(new Object[]{"Day", "Time Slot", "Subject", "Faculty"}, 0);
        timetableTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(timetableTable);
        scrollPane.setBounds(50, 350, 700, 200);
        frame.add(scrollPane);

        // Load Timetable when a branch is selected
        branchComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadTimetable((String) branchComboBox.getSelectedItem());
            }
        });

        // Change timetable action
        changeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedBranch = (String) branchComboBox.getSelectedItem();
                String selectedDay = (String) dayComboBox.getSelectedItem();
                String selectedTime = (String) timeComboBox.getSelectedItem();
                String newSubject = subjectField.getText();
                String newFaculty = facultyField.getText();

                updateTimetable(selectedBranch, selectedDay, selectedTime, newSubject, newFaculty);
                loadTimetable(selectedBranch);  // Reload the updated timetable
            }
        });

        // Back button action (to implement navigation logic, if required)
        backButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                // Implement back action logic here (e.g., go to a previous screen)
                JOptionPane.showMessageDialog(frame, "Back button clicked!");
                frame.dispose();
                new ShowTimeTablesSection();
                
            }
        });

        frame.setVisible(true);
    }

    // Function to update the timetable based on user input
    private void updateTimetable(String branch, String day, String time, String subject, String faculty) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String sql = "UPDATE timetable SET subject_name = ?, faculty_name = ? WHERE department = ? AND day_of_week = ? AND time_slot = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, subject);
            pstmt.setString(2, faculty);
            pstmt.setString(3, branch);
            pstmt.setString(4, day);
            pstmt.setString(5, time);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(frame, "Timetable updated successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Error updating timetable.");
            }
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Function to load and display the timetable of the selected branch
    private void loadTimetable(String branch) {
        tableModel.setRowCount(0);  // Clear the table before loading new data
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "DHARMIKgohil@2006");
            String sql = "SELECT day_of_week, time_slot, subject_name, faculty_name FROM timetable WHERE department = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, branch);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String day = rs.getString("day_of_week");
                String time = rs.getString("time_slot");
                String subject = rs.getString("subject_name");
                String faculty = rs.getString("faculty_name");
                tableModel.addRow(new Object[]{day, time, subject, faculty});
            }
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChangeTimetableDetails();
    }
}
