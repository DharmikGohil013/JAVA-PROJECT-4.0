package util;

import javax.swing.*;
import java.awt.*;

public class inStudentSection {

    JFrame frame;
    String loggedInEmail;  // Store the logged-in user's email

    public inStudentSection(String email) {
        loggedInEmail = email;  // Get email from login section

        frame = new JFrame("Student Section");

        // Set the layout to null for manual positioning
        frame.setLayout(null);

        // Create buttons
        JButton profileButton = new JButton("Profile");
        JButton attendanceButton = new JButton("Attendance");
        JButton feesButton = new JButton("Fees");
        JButton resultButton = new JButton("Result");
        JButton classUpdatesButton = new JButton("Class Updates");
        JButton materialLinksButton = new JButton("Material Links");
        JButton eventsButton = new JButton("View Events");
        JButton universityUpdatesButton = new JButton("University Updates");
        JButton nextExamButton = new JButton("Next Exam Info");
        JButton nextHolidayButton = new JButton("Next Holiday Info");
        JButton onlineLectureButton = new JButton("Online Lectures");
        JButton timeTableButton = new JButton("Time Table");
        JButton facultyDetailsButton = new JButton("Faculty Details");
        JButton ccOnlyButton = new JButton("CC Only"); // New CC Only button
        JButton crMenuButton = new JButton("CR Menu");
        JButton logoutButton = new JButton("Logout");
        JButton exitButton = new JButton("Exit");

        // Set button size, font, and colors
        Dimension buttonSize = new Dimension(200, 50);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Color buttonColorPrimary = new Color(51, 153, 255);  // Light blue color
        Color buttonColorSecondary = new Color(34, 139, 34); // Green for secondary buttons
        Color buttonColorHighlight = new Color(255, 69, 0);  // Orange for CC Only button
        Color textColor = Color.WHITE;

        // Apply style to all buttons
        JButton[] buttons = {profileButton, attendanceButton, feesButton, resultButton, classUpdatesButton, materialLinksButton,
                eventsButton, universityUpdatesButton, nextExamButton, nextHolidayButton, onlineLectureButton, 
                timeTableButton, facultyDetailsButton, ccOnlyButton, crMenuButton, logoutButton, exitButton};

        for (JButton button : buttons) {
            button.setPreferredSize(buttonSize);
            button.setFont(buttonFont);
            button.setForeground(textColor);
            button.setFocusPainted(false); // Remove button border on click
            button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Add hand cursor
        }

        // Assign different colors for better UI contrast
        profileButton.setBackground(buttonColorPrimary);
        attendanceButton.setBackground(buttonColorPrimary);
        feesButton.setBackground(buttonColorPrimary);
        resultButton.setBackground(buttonColorPrimary);
        classUpdatesButton.setBackground(buttonColorPrimary);
        materialLinksButton.setBackground(buttonColorPrimary);
        eventsButton.setBackground(buttonColorSecondary);
        universityUpdatesButton.setBackground(buttonColorSecondary);
        nextExamButton.setBackground(buttonColorSecondary);
        nextHolidayButton.setBackground(buttonColorSecondary);
        onlineLectureButton.setBackground(buttonColorSecondary);
        timeTableButton.setBackground(buttonColorSecondary);
        facultyDetailsButton.setBackground(buttonColorSecondary);
        ccOnlyButton.setBackground(buttonColorHighlight);  // Highlight for new CC Only button
        crMenuButton.setBackground(buttonColorPrimary);
        logoutButton.setBackground(buttonColorPrimary);
        exitButton.setBackground(buttonColorPrimary);

        // Set button positions (adjust manually)
        int xStart = 300, yStart = 100, ySpacing = 70;
        profileButton.setBounds(xStart, yStart, buttonSize.width, buttonSize.height);
        attendanceButton.setBounds(xStart, yStart + ySpacing, buttonSize.width, buttonSize.height);
        feesButton.setBounds(xStart, yStart + ySpacing * 2, buttonSize.width, buttonSize.height);
        resultButton.setBounds(xStart, yStart + ySpacing * 3, buttonSize.width, buttonSize.height);
        classUpdatesButton.setBounds(xStart, yStart + ySpacing * 4, buttonSize.width, buttonSize.height);
        materialLinksButton.setBounds(xStart, yStart + ySpacing * 5, buttonSize.width, buttonSize.height);

        eventsButton.setBounds(xStart + 250, yStart, buttonSize.width, buttonSize.height);
        universityUpdatesButton.setBounds(xStart + 250, yStart + ySpacing, buttonSize.width, buttonSize.height);
        nextExamButton.setBounds(xStart + 250, yStart + ySpacing * 2, buttonSize.width, buttonSize.height);
        nextHolidayButton.setBounds(xStart + 250, yStart + ySpacing * 3, buttonSize.width, buttonSize.height);
        onlineLectureButton.setBounds(xStart + 250, yStart + ySpacing * 4, buttonSize.width, buttonSize.height);
        timeTableButton.setBounds(xStart + 250, yStart + ySpacing * 5, buttonSize.width, buttonSize.height);      
        facultyDetailsButton.setBounds(xStart + 250, yStart + ySpacing * 6, buttonSize.width, buttonSize.height); 
        ccOnlyButton.setBounds(xStart + 250, yStart + ySpacing * 7, buttonSize.width, buttonSize.height); // Position for CC Only button

        crMenuButton.setBounds(xStart + 500, yStart + ySpacing * 5, buttonSize.width, buttonSize.height);
        logoutButton.setBounds(xStart + 500, yStart, buttonSize.width, buttonSize.height);
        exitButton.setBounds(xStart + 500, yStart + ySpacing, buttonSize.width, buttonSize.height);

        // Add buttons to the frame
        frame.add(profileButton);
        frame.add(attendanceButton);
        frame.add(feesButton);
        frame.add(resultButton);
        frame.add(classUpdatesButton);
        frame.add(materialLinksButton);
        frame.add(eventsButton);
        frame.add(universityUpdatesButton);
        frame.add(nextExamButton);
        frame.add(nextHolidayButton);
        frame.add(onlineLectureButton);
        frame.add(timeTableButton);       
        frame.add(facultyDetailsButton);
        frame.add(ccOnlyButton); // Add new CC Only button
        frame.add(crMenuButton);
        frame.add(logoutButton);
        frame.add(exitButton);

        // Button action listeners
        profileButton.addActionListener(e -> new ProfileSection(loggedInEmail));
        attendanceButton.addActionListener(e -> new AttendanceSection(loggedInEmail));
        feesButton.addActionListener(e -> new FeesSection(loggedInEmail));
        resultButton.addActionListener(e -> new ResultSection(loggedInEmail));
        classUpdatesButton.addActionListener(e -> new ClassUpdatesPage(loggedInEmail));
        materialLinksButton.addActionListener(e-> new MaterialLinksSection(email));
        eventsButton.addActionListener(e -> new ViewEvent());
        universityUpdatesButton.addActionListener(e -> new UniversityUpdates());
        nextExamButton.addActionListener(e -> new DepartmentSelectionPage());
        nextHolidayButton.addActionListener(e -> new NextHolidaySection());
        onlineLectureButton.addActionListener(e -> new OnlineLecturesSection());

        // New button action listeners
        timeTableButton.addActionListener(e -> new TimeTableSection(email));       
        facultyDetailsButton.addActionListener(e -> new FacultyDetailsSection(email));
        ccOnlyButton.addActionListener(e -> new CCSection(email)); // Action for CC Only button

        crMenuButton.addActionListener(e -> new CRSectionMenu(email));
        logoutButton.addActionListener(e -> new LogoutPage());
        exitButton.addActionListener(e-> new ExitPage());

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new inStudentSection("user_email@example.com");  // Assume the user is already logged in
    }
}
