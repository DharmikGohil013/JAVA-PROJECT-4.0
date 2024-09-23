package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class TimeTableSection {

    JFrame frame;
    JTable table;
    String department;

    public TimeTableSection(String loggedInEmail) {
        frame = new JFrame("Time Table");

        // Fetch the department of the logged-in user using their email
        department = getDepartmentByEmail(loggedInEmail);
        if (department == null) {
            JOptionPane.showMessageDialog(frame, "User not found in any department!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create table model
        DefaultTableModel model = new DefaultTableModel();

        // Add columns: Days and Time slots
        model.addColumn("Days");
        model.addColumn("9:10 to 10:10");
        model.addColumn("10:10 to 11:10");
        model.addColumn("12:10 to 1:10");
        model.addColumn("1:10 to 2:10");
        model.addColumn("2:20 to 3:20");

        boolean dataFound = false;  // Variable to check if any data was found

        // Fetch timetable data from the database for the user's department
        try (Connection conn = DBConnection.getConnection()) {
            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
            for (String day : days) {
                if (day.equals("Sunday") || day.equals("Saturday")) {
                    // Skip lectures for holidays
                    model.addRow(new Object[]{day, "Holiday", "Holiday", "Holiday", "Holiday", "Holiday"});
                } else {
                    Vector<String> row = new Vector<>();
                    row.add(day);  // Add the day

                    // Fetch the lectures for each time slot for the department and day
                    String query = "SELECT time_slot, subject_name, faculty_name FROM timetable " +
                            "WHERE department = ? AND day_of_week = ? ORDER BY time_slot";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, department);
                    stmt.setString(2, day);
                    ResultSet rs = stmt.executeQuery();

                    // Check if there is data for this department
                    if (!rs.isBeforeFirst()) {  // If no data is found for the department
                        dataFound = false;  // Mark no data found
                    } else {
                        dataFound = true;
                        // Fill in the lecture slots
                        while (rs.next()) {
                            String timeSlot = rs.getString("time_slot");
                            String subject = rs.getString("subject_name");
                            String faculty = rs.getString("faculty_name");

                            // Create cell value with subject and faculty
                            String cellValue = subject + " (" + faculty + ")";

                            // Add the data to the corresponding time slot in the row
                            switch (timeSlot) {
                                case "9:10 to 10:10":
                                    row.add(cellValue);
                                    break;
                                case "10:10 to 11:10":
                                    row.add(cellValue);
                                    break;
                                case "12:10 to 1:10":
                                    row.add(cellValue);
                                    break;
                                case "1:10 to 2:10":
                                    row.add(cellValue);
                                    break;
                                case "2:20 to 3:20":
                                    row.add(cellValue);
                                    break;
                            }
                        }

                        // If the row doesn't have a value for any time slot, fill with "No Lecture"
                        for (int i = 1; i <= 5; i++) {
                            if (row.size() < i + 1) {
                                row.add("No Lecture");
                            }
                        }

                        model.addRow(row);
                    }
                }
            }

            // If no data was found, show a message in the table
            if (!dataFound) {
                JOptionPane.showMessageDialog(frame, "No timetable data found for the department: " + department, "No Data Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create JTable with the model
        table = new JTable(model);
        table.setRowHeight(30);  // Set row height for better readability

        // Set table font and header style
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(51, 153, 255));
        table.getTableHeader().setForeground(Color.WHITE);

        // Add table to JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);  // Add table to the center of the frame

        // Add the "Back" button at the bottom-right corner
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setPreferredSize(new Dimension(100, 50));

        // Add action listener to the back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the timetable window
            }
        });

        // Add the back button to the bottom-right corner
        bottomPanel.add(backButton, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);  // Add the panel to the bottom of the frame

        // Frame settings: Full screen mode
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  // Set frame to full screen
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private String getDepartmentByEmail(String email) {
        String[] tables = {"student_ce", "student_cs", "student_cd", "student_it" , "faculty"};
        String department = null;

        try (Connection conn = DBConnection.getConnection()) {
            for (String table : tables) {
                String query = "SELECT dept FROM " + table + " WHERE email = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    department = rs.getString("dept");
                    break;  // Exit the loop if the department is found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return department;
    }

    public static void main(String[] args) {
        new TimeTableSection("user_ce@example.com");  // Test with a CE user's email
    }
}
