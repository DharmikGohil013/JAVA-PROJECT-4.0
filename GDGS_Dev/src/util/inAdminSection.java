package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class inAdminSection {

    JFrame frame;
    String loggedInEmail;  // Store the logged-in admin's email
    Image backgroundImage;

    public inAdminSection(String email) {
        loggedInEmail = email;  // Get email from login section

        frame = new JFrame("Admin Section");

        // Load the background image from an absolute path
        try {
            backgroundImage = ImageIO.read(new File("D:\\Git Hub\\JAVA-PROJECT-4.0\\GDGS_Dev\\src\\util\\CampusADMINPRO (2).png"));  // Absolute path
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set a custom JPanel to draw the background
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);  // Stretch the background to fit the panel
                }
            }
        };

        // Set the layout to null for manual positioning
        panel.setLayout(null);
        frame.setContentPane(panel);

        // Create buttons
        JButton profileButton = new JButton("Profile");
        JButton allAdminProfileButton = new JButton("All Admin Profiles");
        JButton allFacultyDetailsButton = new JButton("All Faculty Details");
        JButton allStudentDetailsButton = new JButton("All Student Details");
        JButton newAdminAddButton = new JButton("Add New Admin");
        JButton newFacultyAddButton = new JButton("Add New Faculty");
        JButton newStudentAddButton = new JButton("Add New Student");
        JButton changeAdminDetailsButton = new JButton("Change Admin Details");
        JButton changeFacultyDetailsButton = new JButton("Change Faculty Details");
        JButton changeStudentDetailsButton = new JButton("Change Student Details");
        JButton universityUpdateAddButton = new JButton("Add University Update");
        JButton viewEventButton = new JButton("View Events");
        JButton addEventButton = new JButton("Add Event");
        JButton showTimeTableButton = new JButton("Show Time Table");
        JButton feesSectionButton = new JButton("Fees Section");
        JButton salarySectionButton = new JButton("Salary Section");
        JButton principalMessageSendButton = new JButton("Send Principal Message");
        JButton principalMessageViewButton = new JButton("View Principal Messages");
        JButton viewResultButton = new JButton("View Results");
        JButton viewFeesDetailsButton = new JButton("View Fees Details");
        JButton timeTableDetailsChangeButton = new JButton("Change Time Table Details");
        JButton otherButton = new JButton("View University Update");
        JButton other = new JButton("Other");
        JButton logoutButton = new JButton("Logout");
        JButton exitButton = new JButton("Exit");
        
        // New buttons
        JButton nextExamInfoAddButton = new JButton("Add Next Exam Info");
        JButton nextHolidayAddButton = new JButton("Add Next Holiday");

        // Set button size, font, and colors
        Dimension buttonSize = new Dimension(250, 60);
        Font buttonFont = new Font("Arial", Font.PLAIN, 16);
        Color buttonColor = new Color(51, 153, 255);  // Light blue color
        Color textColor = Color.WHITE;

        // Apply style to all buttons
        JButton[] buttons = {
            profileButton, allAdminProfileButton, allFacultyDetailsButton, allStudentDetailsButton,
            newAdminAddButton, newFacultyAddButton, newStudentAddButton, changeAdminDetailsButton,
            changeFacultyDetailsButton, changeStudentDetailsButton, universityUpdateAddButton,
            viewEventButton, addEventButton, showTimeTableButton, feesSectionButton,
            salarySectionButton, principalMessageSendButton, principalMessageViewButton,
            viewResultButton, viewFeesDetailsButton, timeTableDetailsChangeButton, otherButton,
            logoutButton, exitButton, nextExamInfoAddButton, nextHolidayAddButton,other
        };

        for (JButton button : buttons) {
            button.setPreferredSize(buttonSize);
            button.setFont(buttonFont);
            button.setBackground(buttonColor);
            button.setForeground(textColor);
            button.setFocusPainted(false); // Remove button border on click
        }

        // Set button positions (grid layout with manual adjustments)
        int xStart = 200, yStart = 100, xSpacing = 300, ySpacing = 80;
        int buttonsPerRow = 4;

        int index = 0;
        for (int row = 0; row < 7; row++) {  // Increase the row count to accommodate new buttons
            for (int col = 0; col < buttonsPerRow; col++) {
                if (index < buttons.length) {
                    buttons[index].setBounds(xStart + col * xSpacing, yStart + row * ySpacing, buttonSize.width, buttonSize.height);
                    panel.add(buttons[index]);
                    index++;
                }
            }
        }

        // Frame settings for logout and exit buttons
        logoutButton.setBounds(xStart, yStart + 7 * ySpacing, buttonSize.width, buttonSize.height);
        exitButton.setBounds(xStart + xSpacing, yStart + 7 * ySpacing, buttonSize.width, buttonSize.height);

        panel.add(logoutButton);
        panel.add(exitButton);

        // Button action listeners
        profileButton.addActionListener(e -> new ProfileadminSection(email));
        allAdminProfileButton.addActionListener(e -> new AllAdminProfilesSection());
        allFacultyDetailsButton.addActionListener(e -> new AllFacultyDetailsSection());
        allStudentDetailsButton.addActionListener(e -> new StudentDetailsSection());
        newAdminAddButton.addActionListener(e -> new  AddAdminSection());
        newFacultyAddButton.addActionListener(e -> new AddFacultySection());
        newStudentAddButton.addActionListener(e -> new AddStudentSection());
        changeAdminDetailsButton.addActionListener(e -> new ChangeAdminSection());
        changeFacultyDetailsButton.addActionListener(e -> new ChangeFacultySection());
        changeStudentDetailsButton.addActionListener(e -> new ChangeStudentSection());
        universityUpdateAddButton.addActionListener(e -> new UniversityUpdateSection());
        viewEventButton.addActionListener(e -> new ViewEvent());
        addEventButton.addActionListener(e -> new AddadminEventSection());
        showTimeTableButton.addActionListener(e -> new ShowTimeTablesSection());
        feesSectionButton.addActionListener(e -> new addFeesSection());
        salarySectionButton.addActionListener(e -> new SalarySection());
        principalMessageSendButton.addActionListener(e -> new SendMessageToPrincipal());
        principalMessageViewButton.addActionListener(e -> new AdminMessageView());

        viewResultButton.addActionListener(e -> new ViewResultSection());
        viewFeesDetailsButton.addActionListener(e -> new ViewFeesDetailSection());
        timeTableDetailsChangeButton.addActionListener(e -> new ChangeTimetableDetails());
        otherButton.addActionListener(e -> new UniversityUpdates());
        nextExamInfoAddButton.addActionListener(e -> new AddNextExamInfoSection());
        nextHolidayAddButton.addActionListener(e -> new AddNextHolidaySection());
        logoutButton.addActionListener(e -> new LogoutPage());
        exitButton.addActionListener(e -> System.exit(0));

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new inAdminSection("admin_email@example.com");  // Assume the user is already logged in as an admin
    }
}
