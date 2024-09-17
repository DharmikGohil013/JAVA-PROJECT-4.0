package util;

import javax.swing.*;
import java.awt.*;

public class GradingSystemInfo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GradingSystemInfo().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Grading System Information");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JTextArea infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);
        infoTextArea.setText(getGradingSystemInfo());

        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> frame.dispose());
        frame.add(backButton, BorderLayout.SOUTH);

        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    
    private String getGradingSystemInfo() {
        return String.format(
            "Grading System Information\n\n" +
            "1. Introduction:\n" +
            "The CGPA (Cumulative Grade Point Average) and SGPA (Semester Grade Point Average) are measures used to evaluate a student's academic performance.\n\n" +
            "2. SGPA Calculation:\n" +
            "SGPA is calculated for each semester. It represents the average performance of a student in a particular semester.\n\n" +
            "Formula:\n" +
            "SGPA = (Sum of (Grade Points × Credits for each subject)) / (Total Credits for the semester)\n\n" +
            "Example:\n" +
            "Suppose a student has the following grades for a semester:\n" +
            "- Subject 1: Grade Point = 8.0, Credits = 3\n" +
            "- Subject 2: Grade Point = 7.5, Credits = 4\n" +
            "- Subject 3: Grade Point = 9.0, Credits = 2\n\n" +
            "SGPA = [(8.0 × 3) + (7.5 × 4) + (9.0 × 2)] / (3 + 4 + 2)\n" +
            "SGPA = [24.0 + 30.0 + 18.0] / 9\n" +
            "SGPA = 72.0 / 9\n" +
            "SGPA = 8.0\n\n" +
            "3. CGPA Calculation:\n" +
            "CGPA is calculated by averaging the SGPA of all semesters completed so far. It provides an overall measure of a student’s performance throughout their academic program.\n\n" +
            "Formula:\n" +
            "CGPA = (Sum of SGPA for all semesters) / (Number of semesters)\n\n" +
            "Example:\n" +
            "Suppose a student has the following SGPA for three semesters:\n" +
            "- Semester 1: SGPA = 8.0\n" +
            "- Semester 2: SGPA = 7.8\n" +
            "- Semester 3: SGPA = 8.5\n\n" +
            "CGPA = (8.0 + 7.8 + 8.5) / 3\n" +
            "CGPA = 24.3 / 3\n" +
            "CGPA = 8.1\n\n" +
            "4. Example Calculation:\n" +
            "To calculate SGPA and CGPA, follow the above formulas and replace the values with the actual grades and credits for the respective subjects and semesters.\n\n" +
            "For accurate calculations, ensure that the credits for each subject and the grade points are correctly recorded. Regularly update your SGPA to track your semester performance and calculate CGPA to monitor your overall academic progress."
        );
    }
    
}
