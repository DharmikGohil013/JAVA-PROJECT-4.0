package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelpSection {

    JFrame frame;
    JTextArea helpTextArea;
    JButton backButton;

    public HelpSection() {
        // Create the frame
        frame = new JFrame("Help Section for College Management E-Governance System");
        frame.setLayout(new BorderLayout());

        // Help content
        String helpContent = "Help Section for College Management E-Governance System\n" +
        "1. Introduction to the System:\n" +
        "This college management e-governance system is designed to automate various administrative, academic, and student processes.\n" +
        "It allows for easy tracking, monitoring, and managing of information related to students, faculty, courses, fees, examinations, and more.\n\n" +
        "2. System Requirements:\n" +
        "- Operating System: Windows 7 or higher, Mac OS X, Linux.\n" +
        "- Browser: Latest versions of Chrome, Firefox, Safari, or Edge.\n" +
        "- Internet Connection: A stable internet connection with at least 5 Mbps speed.\n" +
        "- Resolution: Minimum screen resolution of 1024x768 for optimal display.\n\n" +
        "3. User Roles:\n" +
        "- Admin: Full access to all modules. Can manage users, courses, departments, fees, and examination details.\n" +
        "- Faculty: Can view and manage courses, student details, attendance, and grades.\n" +
        "- Students: Can view their profile, check attendance, apply for exams, pay fees, and see results.\n" +
        "- Accounts: Manages financial details like fee payments and dues.\n" +
        "- Librarian: Manages the library system, issuing and returning books.\n\n" +
        "4. Login and Registration:\n" +
        "- 4.1 User Registration: Fill in the required details and verify your email to complete the registration process.\n" +
        "- 4.2 Login: Use your registered email and password to log in.\n\n" +
        "5. Dashboard:\n" +
        "Users land on different dashboards based on their role (Admin, Faculty, Student).\n\n" +
        "6. Modules:\n" +
        "- 6.1 Student Information Management\n" +
        "- 6.2 Attendance Management\n" +
        "- 6.3 Course Management\n" +
        "- 6.4 Examination Management\n" +
        "- 6.5 Fees Management\n" +
        "- 6.6 Library Management\n" +
        "- 6.7 Reports and Analytics\n\n" +
        "7. Notifications:\n" +
        "- Users will receive notifications for important updates like upcoming exams, new announcements, and fee deadlines.\n\n" +
        "8. Profile Management:\n" +
        "- Edit your profile and change password in the Settings section.\n\n" +
        "9. Settings:\n" +
        "- Admin can configure system-wide settings, user roles, and perform backups.\n\n" +
        "10. Troubleshooting:\n" +
        "- Login Issues: Use the Forgot Password option or check your email verification.\n" +
        "- Unable to Apply for Exam: Ensure enrollment in the course before applying.\n" +
        "- Fee Payment Issues: Check your internet connection and ensure sufficient funds.\n\n" +
        "11. FAQs:\n" +
        "- 11.1 How do I reset my password? Use the Forgot Password link on the login page.\n" +
        "- 11.2 How do I contact support? Reach support at support@xyz.edu.in.com or call +9624105887.\n\n" +
        "12. Contact Support:\n" +
        "Email: support@xyz.edu.in.com\n" +
        "Phone: +9624105887\n" +
        "Available: Monday to Friday, 9 AM - 5 PM\n" +
        "Presented by: Dharmik Gohil Film, Company Name: GOHIL DHARMIK COMPLEX SYSTEM (GDCS)\n";
    

        // Text area to display help content
        helpTextArea = new JTextArea(helpContent);
        helpTextArea.setLineWrap(true);
        helpTextArea.setWrapStyleWord(true);
        helpTextArea.setEditable(false);
        helpTextArea.setMargin(new Insets(10, 10, 10, 10));
        helpTextArea.setFont(new Font("Arial", Font.PLAIN, 50));

        // Scroll pane for the text area
        JScrollPane scrollPane = new JScrollPane(helpTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Back button
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action to go back (for demo purposes, we'll close the help window)
                frame.dispose();
                // You can redirect to another window or screen here
            }
        });

        // Bottom panel for back button
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);

        // Add components to frame
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Frame settings
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Launch the Help Section window
        new HelpSection();
    }
}
