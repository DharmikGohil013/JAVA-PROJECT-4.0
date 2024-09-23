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
        JButton[] buttons = new JButton[36];
        String[] buttonNames = {
            "Profile", "All Admin Profiles", "All Faculty Details", "All Student Details",
            "Add New Admin", "Add New Faculty", "Add New Student", "Change Admin Details",
            "Change Faculty Details", "Change Student Details", "Add University Update", "View Events",
            "Add Event", "Show Time Table", "Fees Section", "Salary Section",
            "Send Principal Message", "View Principal Messages", "View Results",
            "View Fees Details", "Change Time Table Details", "View University Update", " Add Next Exam Info","Add Next Holiday"
             ,"Other", 
            "Course Plan", "Supervisions", "Exams", "Grading System", "Classroom Info",
            "Help Desk", "New Courses", "Student Records", "Faculty Policies","Logout","Exit"
        };

        // Set button size, font, and colors
        Dimension buttonSize = new Dimension(250, 60);
        Font buttonFont = new Font("Arial", Font.PLAIN, 16);
        Color buttonColor = new Color(51, 153, 255);  // Light blue color
        Color textColor = Color.WHITE;

        // Initialize and style each button
        for (int i = 0; i < 36; i++) {
            buttons[i] = new JButton(buttonNames[i]);
            buttons[i].setPreferredSize(buttonSize);
            buttons[i].setFont(buttonFont);
            buttons[i].setBackground(buttonColor);
            buttons[i].setForeground(textColor);
            buttons[i].setFocusPainted(false); // Remove button border on click
            panel.add(buttons[i]);
        }

        // Set button positions: 6 rows, 6 buttons per row
        int xStart = 100, yStart = 100, xSpacing = 270, ySpacing = 80; // Adjusted for spacing
        int row = 0, col = 0;

        for (int i = 0; i < 36; i++) {
            JButton button = buttons[i];
            button.setBounds(xStart + (col * xSpacing), yStart + (row * ySpacing), buttonSize.width, buttonSize.height);
            col++;
            if (col == 6) {  // Move to the next row after 6 buttons
                col = 0;
                row++;
            }
        }

        // Frame settings for logout and exit buttons
        JButton logoutButton = buttons[24]; // 25th button in array
        JButton exitButton = buttons[25];   // 26th button in array
        
        
        // Add logout and exit buttons to the panel
        panel.add(logoutButton);
        panel.add(exitButton);

        // Button action listeners
        buttons[0].addActionListener(e -> new ProfileadminSection(email));
        buttons[1].addActionListener(e -> new AllAdminProfilesSection());
        buttons[2].addActionListener(e -> new AllFacultyDetailsSection());
        buttons[3].addActionListener(e -> new StudentDetailsSection());
        buttons[4].addActionListener(e -> new AddAdminSection());
        buttons[5].addActionListener(e -> new AddFacultySection());
        buttons[6].addActionListener(e -> new AddStudentSection());
        buttons[7].addActionListener(e -> new ChangeAdminSection());
        buttons[8].addActionListener(e -> new ChangeFacultySection());
        buttons[9].addActionListener(e -> new ChangeStudentSection());
        buttons[10].addActionListener(e -> new UniversityUpdateSection());
        buttons[11].addActionListener(e -> new ViewEvent());
        buttons[12].addActionListener(e -> new AddadminEventSection());
        buttons[13].addActionListener(e -> new ShowTimeTablesSection());
        buttons[14].addActionListener(e -> new addFeesSection());
        buttons[15].addActionListener(e -> new SalarySection());
        buttons[16].addActionListener(e -> new SendMessageToPrincipal());
        buttons[17].addActionListener(e -> new AdminMessageView());
        buttons[18].addActionListener(e -> new ViewResultSection());
        buttons[19].addActionListener(e -> new ViewFeesDetailSection());
        buttons[20].addActionListener(e -> new ChangeTimetableDetails());
        buttons[21].addActionListener(e -> new UniversityUpdates());
        buttons[22].addActionListener(e -> new AddNextExamInfoSection());
        buttons[23].addActionListener(e -> new AddNextHolidaySection());
        buttons[24].addActionListener(e -> new AdminLeaveApproval());
        buttons[25].addActionListener(e -> new courceplan());
        buttons[26].addActionListener(e -> new supervision());
        buttons[27].addActionListener(e -> new NextExamSection());
        buttons[28].addActionListener(e -> new GradingSystemInfo());
        buttons[30].addActionListener(e -> new HelpSection());
        buttons[29].addActionListener(e -> new ClassroomInformation());
        buttons[31].addActionListener(e -> new NewCourseSection());
// Assuming studentId is available
int studentId = 1;  // Replace with the actual student ID

buttons[32].addActionListener(e -> new StudentEventGallery(studentId));

        //buttons[22].addActionListener(e -> new StudentEventGallery());
        buttons[33].addActionListener(e-> new FacultyPoliciesSection());
        buttons[34].addActionListener(e -> new LogoutPage());
        buttons[35].addActionListener(e -> System.exit(0));

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new inAdminSection("admin_email@example.com");  // Assume the user is already logged in as an admin
    }
}
