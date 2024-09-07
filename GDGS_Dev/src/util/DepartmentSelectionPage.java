package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DepartmentSelectionPage {

    JFrame frame;

    public DepartmentSelectionPage() {
        frame = new JFrame("Select Department");

        // Create buttons for each department
        JButton itButton = new JButton("IT");
        JButton ceButton = new JButton("CE");
        JButton cdButton = new JButton("CD");
        JButton csButton = new JButton("CS");

        // Set button size, font, and colors
        Dimension buttonSize = new Dimension(200, 50);
        Font buttonFont = new Font("Arial", Font.PLAIN, 16);
        Color buttonColor = new Color(51, 153, 255);  // Light blue color
        Color textColor = Color.WHITE;

        JButton[] buttons = {itButton, ceButton, cdButton, csButton};
        for (JButton button : buttons) {
            button.setPreferredSize(buttonSize);
            button.setFont(buttonFont);
            button.setBackground(buttonColor);
            button.setForeground(textColor);
            button.setFocusPainted(false);
        }

        // Set button positions
        int xStart = 200, yStart = 100, ySpacing = 70;
        itButton.setBounds(xStart, yStart, buttonSize.width, buttonSize.height);
        ceButton.setBounds(xStart, yStart + ySpacing, buttonSize.width, buttonSize.height);
        cdButton.setBounds(xStart, yStart + ySpacing * 2, buttonSize.width, buttonSize.height);
        csButton.setBounds(xStart, yStart + ySpacing * 3, buttonSize.width, buttonSize.height);

        // Add buttons to frame
        frame.setLayout(null);
        frame.add(itButton);
        frame.add(ceButton);
        frame.add(cdButton);
        frame.add(csButton);

        // Button action listeners
        itButton.addActionListener(e -> openNextExamInfoPage("IT"));
        ceButton.addActionListener(e -> openNextExamInfoPage("CE"));
        cdButton.addActionListener(e -> openNextExamInfoPage("CD"));
        csButton.addActionListener(e -> openNextExamInfoPage("CS"));

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize()); // Full screen
        frame.setUndecorated(true); // Remove window decorations
        frame.setVisible(true);
    }

    private void openNextExamInfoPage(String department) {
        frame.dispose(); // Close current frame
        new NextExamInfoPage(department); // Open next page with selected department
    }

    public static void main(String[] args) {
        new DepartmentSelectionPage(); // Open the department selection page
    }
}
