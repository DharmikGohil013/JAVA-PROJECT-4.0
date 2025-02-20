package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class infacultysection extends JFrame {
    private Image backgroundImage;

    public infacultysection(String email) {
        try {
            backgroundImage = ImageIO.read(new File("D:\\Git Hub\\Java Dev Projects\\JAVA-PROJECT-4.0\\GDGS_Dev\\src\\util\\faculty1.png"));  // Absolute path
        } catch (IOException e) {
            e.printStackTrace();
        }
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        panel.setLayout(null);
        JButton[] buttons = new JButton[30];
        String[] buttonNames = 
        {
                "Profile",  "Time Table", "Grades", "Attendance", "Meetings", "Notices",
                "Research", "Publications", "Faculty Events", "Schedules", "Salary Details", "Leaves",
                "Result", "Upload Material", "Assignments", "Lectures", "View Event", "Students",
                "University Updates", "Event Atted.", "Principal Section", "Projects", "Logout", "Exit",
                "Holidays", "Supervisions", "Exams", "Grading System", "Classroom Info", "Help Desk"
        };


        Dimension buttonSize = new Dimension(200, 50);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Color buttonColorPrimary = new Color(70, 130, 180);  // Steel blue
        Color buttonColorSecondary = new Color(60, 179, 113); // Medium sea green
        Color textColor = Color.WHITE;
        for (int i = 0; i < 30; i++) 
        {
            buttons[i] = new JButton(buttonNames[i]);
            buttons[i].setPreferredSize(buttonSize);
            buttons[i].setFont(buttonFont);
            buttons[i].setForeground(textColor);
            buttons[i].setFocusPainted(false); 
            buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (i < 15) {
                buttons[i].setBackground(buttonColorPrimary); 
            } else {
                buttons[i].setBackground(buttonColorSecondary); 
            }
        }

        int xStart = 200, yStart = 100, xSpacing = 250, ySpacing = 100;
        int row = 0, col = 0;

        for (int i = 0; i < buttons.length; i++) {
            JButton button = buttons[i];
            button.setBounds(xStart + (col * xSpacing), yStart + (row * ySpacing), buttonSize.width, buttonSize.height);
            panel.add(button);

            col++;
            if (col == 6) 
            {  
                col = 0;
                row++;
            }
        }
        buttons[0].addActionListener(e -> new ProfilefacultySection(email));
        buttons[1].addActionListener(e -> new TimeTableSection(email));
        buttons[3].addActionListener(e -> new AttendanceSystem(email));
        buttons[4].addActionListener(e-> new facultymetting());
        buttons[5].addActionListener(e -> new FacultyNoticeSection());
        buttons[6].addActionListener(e-> new research());
        buttons[7].addActionListener(e-> new publication());
        buttons[8].addActionListener(e-> new ViewEvent());
        buttons[9].addActionListener(e-> new toFacultyScheduleSection());
        buttons[10].addActionListener(e-> new FacultySalarySection(email));
        buttons[11].addActionListener(e-> new FacultyLeaveSystem(email));
        buttons[12].addActionListener(e-> new toFacultyResultSection());
        buttons[13].addActionListener(e-> new UploadMaterialSection(email));
        buttons[14].addActionListener(e-> new FacultyAssignmentUpload());
        buttons[15].addActionListener(e-> new lectures());
        buttons[16].addActionListener(e-> new ViewEvent());
        buttons[17].addActionListener(e-> new FacultyStudentSection(email));
        buttons[18].addActionListener(e-> new UniversityUpdatesSection());
        buttons[19].addActionListener(e-> new FacultyEventAttendanceViewer());
        buttons[20].addActionListener(e-> new PrincipalSection());
        buttons[21].addActionListener(e-> new project());
        buttons[22].addActionListener(e-> new LogoutPage());
        buttons[23].addActionListener(e-> new ExitPage());
        buttons[24].addActionListener(e-> new NextHolidaySection());
        buttons[25].addActionListener(e-> new supervision());
        buttons[26].addActionListener(e-> new DepartmentSelectionPage());
        buttons[27].addActionListener(e-> new facultyGradingSystemInfo());
        buttons[28].addActionListener(e-> new Classroom());
        buttons[29].addActionListener(e-> new HelpSection());

        add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    public static void main(String[] args) {
        new infacultysection("faculty001@xyz.edu.in"); 
    }
}
