package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        JButton crMenuButton = new JButton("CR Menu");
        JButton logoutButton = new JButton("Logout");
        JButton exitButton = new JButton("Exit");

        // Set button size, font, and colors
        Dimension buttonSize = new Dimension(200, 50);
        Font buttonFont = new Font("Arial", Font.PLAIN, 16);
        Color buttonColor = new Color(51, 153, 255);  // Light blue color
        Color textColor = Color.WHITE;

        // Apply style to all buttons
        JButton[] buttons = {profileButton, attendanceButton, feesButton, resultButton, classUpdatesButton, materialLinksButton,
                eventsButton, universityUpdatesButton, nextExamButton, nextHolidayButton, onlineLectureButton,
                crMenuButton, logoutButton, exitButton};

        for (JButton button : buttons) {
            button.setPreferredSize(buttonSize);
            button.setFont(buttonFont);
            button.setBackground(buttonColor);
            button.setForeground(textColor);
            button.setFocusPainted(false); // Remove button border on click
        }

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

        crMenuButton.setBounds(xStart + 250, yStart + ySpacing * 5, buttonSize.width, buttonSize.height);
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
        frame.add(crMenuButton);
        frame.add(logoutButton);
        frame.add(exitButton);

        // Profile button action listener to open profile section
        profileButton.addActionListener(e -> new ProfileSection(loggedInEmail));
        attendanceButton.addActionListener(e -> new AttendanceSection(loggedInEmail));
        feesButton.addActionListener(e -> new FeesSection(loggedInEmail));
        // Inside inStudentSection class
resultButton.addActionListener(e -> new ResultSection(loggedInEmail));
classUpdatesButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Open the ClassUpdatesPage and pass the logged-in email
        new ClassUpdatesPage(loggedInEmail);
    }
});



        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
    

    public static void main(String[] args) {
        new inStudentSection("user_email@example.com");  // Assume the user is already logged in
    }
}
